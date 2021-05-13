package com.tokopedia.home.benchmark.network_request

import android.content.Context
import android.util.Log
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.home.benchmark.network_request.HomeMockResponseList.getDynamicHomeChannel
import com.tokopedia.home.benchmark.prepare_page.TestQuery.dynamicChannelQuery
import com.tokopedia.home.benchmark.prepare_page.TestQuery.homeQuery
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.datasource.local.HomeCachedDataSource
import com.tokopedia.home.beranda.data.datasource.local.HomeDatabase
import com.tokopedia.home.beranda.data.datasource.local.dao.HomeDao
import com.tokopedia.home.beranda.data.datasource.remote.GeolocationRemoteDataSource
import com.tokopedia.home.beranda.data.datasource.remote.HomeRemoteDataSource
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.HomeDynamicChannelDataMapper
import com.tokopedia.home.beranda.data.mapper.factory.HomeDynamicChannelVisitableFactoryImpl
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactoryImpl
import com.tokopedia.home.beranda.data.model.HomeAtfData
import com.tokopedia.home.beranda.data.repository.HomeRevampRepositoryImpl
import com.tokopedia.home.beranda.data.usecase.HomeRevampUseCase
import com.tokopedia.home.beranda.di.module.query.QueryHome
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.model.HomeChannelData
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.HomeFlagData
import com.tokopedia.home.beranda.domain.model.HomeRoomData
import com.tokopedia.home.beranda.domain.model.banner.HomeBannerData
import com.tokopedia.home.common.HomeAceApi
import com.tokopedia.home.mock.HomeMockResponseConfig
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import dagger.Lazy
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import rx.Observable
import kotlin.coroutines.CoroutineContext

class HomeBenchmarkTestNetworkRequest: CoroutineScope {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    private val masterJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + masterJob

    companion object {
        private lateinit var homeUseCase: HomeRevampUseCase
        private lateinit var homeDao: HomeDao
        private lateinit var context: Context
        private lateinit var gson: Gson
        private lateinit var homeDataMapper: HomeDataMapper

        @BeforeClass @JvmStatic
        fun setupComponents() {
            setupGraphqlMockResponse(HomeMockResponseConfig())
            context = ApplicationProvider.getApplicationContext<Context>()
            val homeDatabase = HomeDatabase.buildDatabase(context)
            homeDao = homeDatabase.homeDao()
            gson = Gson()
            val homeData = gson.fromJson(getDynamicHomeChannel(context), HomeData::class.java)
            runBlocking {
                homeDao.save(HomeRoomData(homeData = homeData))
            }
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            val homeVisitableFactory = HomeVisitableFactoryImpl(null, remoteConfig, HomeDefaultDataSource())
            val trackingQueue = TrackingQueue(context)

            val homeCachedDataSource = HomeCachedDataSource(homeDao)
            val userSessionInterface = UserSession(context)

            val useCaseChannel = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeChannelData>(GraphqlInteractor.getInstance().graphqlRepository)
            useCaseChannel.setGraphqlQuery(dynamicChannelQuery)
            val homeDynamicChannelDataMapper = HomeDynamicChannelDataMapper(
                    context,
                    HomeDynamicChannelVisitableFactoryImpl(userSessionInterface, remoteConfig, HomeDefaultDataSource()),
                    trackingQueue
            )
            val getDynamicChannelRepository = GetHomeDynamicChannelsRepository(
                    GraphqlInteractor.getInstance().graphqlRepository
            )
            val getTickerRepository = GetHomeTickerRepository(
                    GraphqlInteractor.getInstance().graphqlRepository
            )
            val getIconRepository = GetHomeIconRepository(
                    GraphqlInteractor.getInstance().graphqlRepository
            )

            homeDataMapper = HomeDataMapper(context, homeVisitableFactory, trackingQueue, homeDynamicChannelDataMapper)

            val useCaseHomeData = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeData>(GraphqlInteractor.getInstance().graphqlRepository)
            useCaseHomeData.setGraphqlQuery(homeQuery)
            val getHomeDataUseCase = GetHomeDataUseCase(useCaseHomeData)

            val atfHomeData = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeAtfData>(GraphqlInteractor.getInstance().graphqlRepository)
            atfHomeData.setGraphqlQuery(QueryHome.atfQuery)
            val getAtfUseCase = GetHomeAtfUseCase(atfHomeData)

            val homeFlagData = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeFlagData>(GraphqlInteractor.getInstance().graphqlRepository)
            homeFlagData.setGraphqlQuery(QueryHome.homeDataRevampQuery)
            val getHomeFlagUseCase = GetHomeFlagUseCase(homeFlagData)

            val homeBannerData = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<HomeBannerData>(GraphqlInteractor.getInstance().graphqlRepository)
            useCaseHomeData.setGraphqlQuery(QueryHome.homeSlidesQuery)
            val getHomeBannerUseCase = GetHomePageBannerUseCase(homeBannerData)

            val homeRemoteDataSource = HomeRemoteDataSource(
                    dispatchers = CoroutineDispatchersProvider,
                    getHomeDynamicChannelsRepository = getDynamicChannelRepository,
                    getHomeDataUseCase = getHomeDataUseCase,
                    getAtfDataUseCase = getAtfUseCase,
                    getHomeFlagUseCase = getHomeFlagUseCase,
                    getHomePageBannerUseCase = getHomeBannerUseCase,
                    getHomeIconRepository = getIconRepository,
                    getHomeTickerRepository = getTickerRepository)
            val geolocationRemoteDataSource: Lazy<GeolocationRemoteDataSource> = Lazy {
                GeolocationRemoteDataSource(HomeAceApi { Observable.just(Response.success("Test")) })
            }
            val homeRepository = HomeRevampRepositoryImpl(
                    homeCachedDataSource,
                    homeRemoteDataSource,
                    HomeDefaultDataSource(),
                    geolocationRemoteDataSource,
                    homeDynamicChannelDataMapper,
                    context,
                    remoteConfig
            )
            homeUseCase = HomeRevampUseCase(
                    homeRepository,
                    homeDataMapper
            )
        }
    }

    /**
     * Test UseCase
     */
    @Test
    fun benchmark_getHomeData_UseCase_HomeUseCase() {
        benchmarkRule.measureRepeated {
            Log.d("BenchmarkLog", "benchmark_getHomeData_UseCase_HomeUseCase Started!")
            runBlocking {
                val data = homeUseCase.getHomeData().first()
                Log.d("BenchmarkLog", "benchmark_getHomeData_UseCase_HomeUseCase Finished!")
            }
        }
    }

    @Test
    fun benchmark_updateHomeData_UseCase_HomeUseCase() {
        benchmarkRule.measureRepeated {
            runBlocking {
                Log.d("BenchmarkLog", "benchmark_updateHomeData_UseCase_HomeUseCase Started!")
                val data = homeUseCase.updateHomeData().first()
                Log.d("BenchmarkLog", "benchmark_updateHomeData_UseCase_HomeUseCase Finished!")
            }
        }
    }

    /**
     * Test Dao
     */
    @Test
    fun benchmark_saveToDatabase_Dao_HomeCachedDataSource() {
        val homeData = gson.fromJson(getDynamicHomeChannel(context), HomeData::class.java)
        benchmarkRule.measureRepeated {
            runBlocking {
                homeDao.save(HomeRoomData(homeData = homeData))
            }
        }
    }

    @Test
    fun benchmark_getData_Dao_HomeCachedDataSource() {
        benchmarkRule.measureRepeated {
            runBlocking {
                val data = homeDao.getHomeData().flowOn(Dispatchers.Main).first()
                Log.d("BenchmarkLog", data?.toString()?:"")
            }
        }
    }

    /**
     * Test Mapper
     */
    @Test
    fun benchmark_mapToModel_Mapper_HomeDataMapper() {
        val homeData = gson.fromJson(getDynamicHomeChannel(context), HomeData::class.java)

        benchmarkRule.measureRepeated {
            val dataFromMapper = homeDataMapper.mapToHomeViewModel(homeData, false)
            Log.d("BenchmarkLog", dataFromMapper.toString()?:"")
        }
    }
}