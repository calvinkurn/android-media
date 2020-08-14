package com.tokopedia.home.benchmark.network_request

import android.content.Context
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import com.tokopedia.home.benchmark.network_request.HomeMockResponseList.getDynamicHomeChannel
import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.datasource.local.HomeDatabase
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactoryImpl
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.HomeRoomData
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.trackingoptimizer.TrackingQueue
import kotlinx.coroutines.*
import org.junit.Rule
import org.junit.Test
import kotlin.coroutines.CoroutineContext

class HomeBenchmarkTestNetworkRequest: CoroutineScope {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    private val masterJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + masterJob

    @Test
    fun benchmark_HomeCachedDataSource_saveToDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val homeDatabase = HomeDatabase.buildDatabase(context)
        val homeDao = homeDatabase.homeDao()
        val gson = Gson()

        val homeData = gson.fromJson(getDynamicHomeChannel(context), HomeData::class.java)

        benchmarkRule.measureRepeated {
            launch { homeDao.save(HomeRoomData(homeData = homeData)) }
        }
    }

    @Test
    fun benchmark_HomeDataMapper_mapToHomeViewModel() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        val homeVisitableFactory = HomeVisitableFactoryImpl(null, remoteConfig, HomeDefaultDataSource())
        val trackingQueue = TrackingQueue(context)
        val homeDataMapper = HomeDataMapper(context, homeVisitableFactory, trackingQueue)

        val gson = Gson()
        val homeData = gson.fromJson(getDynamicHomeChannel(context), HomeData::class.java)

        benchmarkRule.measureRepeated {
            homeDataMapper.mapToHomeViewModel(homeData, false)
        }
    }
}