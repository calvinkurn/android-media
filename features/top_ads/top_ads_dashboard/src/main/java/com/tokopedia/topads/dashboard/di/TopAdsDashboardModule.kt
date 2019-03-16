package com.tokopedia.topads.dashboard.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.product.manage.item.common.data.source.cloud.ShopApi
import com.tokopedia.shop.common.constant.ShopCommonUrl
import com.tokopedia.shop.common.data.repository.ShopCommonRepositoryImpl
import com.tokopedia.shop.common.data.source.ShopCommonDataSource
import com.tokopedia.shop.common.data.source.cloud.ShopCommonCloudDataSource
import com.tokopedia.shop.common.data.source.cloud.api.ShopCommonApi
import com.tokopedia.shop.common.data.source.cloud.api.ShopCommonWSApi
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase
import com.tokopedia.shop.common.domain.repository.ShopCommonRepository
import com.tokopedia.topads.common.data.api.TopAdsManagementApi
import com.tokopedia.topads.common.data.repository.TopAdsShopDepositRepositoryImpl
import com.tokopedia.topads.common.data.source.ShopDepositDataSource
import com.tokopedia.topads.common.data.source.cloud.ShopDepositDataSourceCloud
import com.tokopedia.topads.common.data.source.local.TopAdsCacheDataSource
import com.tokopedia.topads.common.data.source.local.TopAdsCacheDataSourceImpl
import com.tokopedia.topads.common.domain.interactor.TopAdsDatePickerInteractor
import com.tokopedia.topads.common.domain.interactor.TopAdsDatePickerInteractorImpl
import com.tokopedia.topads.common.domain.repository.TopAdsShopDepositRepository
import com.tokopedia.topads.dashboard.data.repository.TopAdsDashboardRepositoryImpl
import com.tokopedia.topads.dashboard.data.source.TopAdsDashboardDataSource
import com.tokopedia.topads.dashboard.data.source.cloud.TopAdsDashboardDataSourceCloud
import com.tokopedia.topads.dashboard.data.source.cloud.serviceapi.TopAdsDashboardApi
import com.tokopedia.topads.dashboard.domain.interactor.DeleteTopAdsStatisticsUseCase
import com.tokopedia.topads.dashboard.domain.interactor.DeleteTopAdsTotalAdUseCase
import com.tokopedia.topads.dashboard.domain.repository.TopAdsDashboardRepository
import com.tokopedia.topads.sourcetagging.data.repository.TopAdsSourceTaggingRepositoryImpl
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingDataSource
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingLocal
import com.tokopedia.topads.sourcetagging.domain.repository.TopAdsSourceTaggingRepository
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Named

/**
 * Created by hadi.putra on 23/04/18.
 */

@TopAdsDashboardScope
@Module
class TopAdsDashboardModule {

    @Provides
    @TopAdsDashboardScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = com.tokopedia.user.session.UserSession(context)

    @Provides
    @TopAdsDashboardScope
    fun provideTopAdsManagementApi(@TopAdsDashboardQualifier retrofit: Retrofit): TopAdsManagementApi {
        return retrofit.create(TopAdsManagementApi::class.java)
    }

    @ShopWsQualifier
    @TopAdsDashboardScope
    @Provides
    fun provideWSRetrofit(@ShopQualifier okHttpClient: OkHttpClient,
                          retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(ShopCommonUrl.BASE_WS_URL).client(okHttpClient).build()
    }

    @TopAdsDashboardScope
    @Provides
    fun provideShopApi(@ShopWsQualifier retrofit: Retrofit): ShopApi {
        return retrofit.create(ShopApi::class.java)
    }

    @Provides
    @TopAdsDashboardScope
    fun provideShopCommonApi(@ShopQualifier retrofit: Retrofit): ShopCommonApi {
        return retrofit.create(ShopCommonApi::class.java)
    }

    @Provides
    @TopAdsDashboardScope
    fun provideShopCommonWsApi(@ShopWsQualifier retrofit: Retrofit): ShopCommonWSApi {
        return retrofit.create(ShopCommonWSApi::class.java)
    }

    @Provides
    @TopAdsDashboardScope
    fun provideShopCommonCloudDataSource(shopCommonApi: ShopCommonApi, shopCommonWS4Api: ShopCommonWSApi,
                                         userSession: UserSessionInterface): ShopCommonCloudDataSource {
        return ShopCommonCloudDataSource(shopCommonApi, shopCommonWS4Api, userSession)
    }

    @Provides
    @TopAdsDashboardScope
    fun provideShopCommonDataSource(shopInfoCloudDataSource: ShopCommonCloudDataSource): ShopCommonDataSource {
        return ShopCommonDataSource(shopInfoCloudDataSource)
    }

    @Provides
    @TopAdsDashboardScope
    fun provideShopCommonRepository(shopInfoDataSource: ShopCommonDataSource): ShopCommonRepository {
        return ShopCommonRepositoryImpl(shopInfoDataSource)
    }

    @Provides
    @TopAdsDashboardScope
    fun provideGetShopInfoUseCase(shopCommonRepository: ShopCommonRepository): GetShopInfoUseCase {
        return GetShopInfoUseCase(shopCommonRepository)
    }

    @Provides
    @TopAdsDashboardScope
    fun provideShopDepositDataSource(shopDepositDataSourceCloud: ShopDepositDataSourceCloud): ShopDepositDataSource {
        return ShopDepositDataSource(shopDepositDataSourceCloud)
    }

    @Provides
    @TopAdsDashboardScope
    fun provideShopDepositDataSourceCloud(topAdsManagementApi: TopAdsManagementApi): ShopDepositDataSourceCloud {
        return ShopDepositDataSourceCloud(topAdsManagementApi)
    }

    @Provides
    @TopAdsDashboardScope
    fun provideTopAdsShopDepositRepository(shopDepositDataSource: ShopDepositDataSource): TopAdsShopDepositRepository {
        return TopAdsShopDepositRepositoryImpl(shopDepositDataSource)
    }

    @Provides
    @TopAdsDashboardScope
    fun provideTopAdsCacheDataSource(@ApplicationContext context: Context): TopAdsCacheDataSource {
        return TopAdsCacheDataSourceImpl(context)
    }

    @Provides
    @TopAdsDashboardScope
    fun provideTopAdsDatePickerInteractor(topAdsCacheDataSource: TopAdsCacheDataSource): TopAdsDatePickerInteractor {
        return TopAdsDatePickerInteractorImpl(topAdsCacheDataSource)
    }

    @Provides
    @TopAdsDashboardScope
    fun provideTopAdsDashboadrApi(@TopAdsDashboardQualifier retrofit: Retrofit): TopAdsDashboardApi {
        return retrofit.create(TopAdsDashboardApi::class.java)
    }

    @Provides
    @TopAdsDashboardScope
    fun provideTopAdsDashboardDataSourceCloud(topAdsDashboardApi: TopAdsDashboardApi): TopAdsDashboardDataSourceCloud {
        return TopAdsDashboardDataSourceCloud(topAdsDashboardApi)
    }

    @Provides
    @TopAdsDashboardScope
    fun provideTopAdsDashboardDataSource(topAdsDashboardDataSourceCloud: TopAdsDashboardDataSourceCloud): TopAdsDashboardDataSource {
        return TopAdsDashboardDataSource(topAdsDashboardDataSourceCloud)
    }

    @Provides
    @TopAdsDashboardScope
    fun provideTopAdsDashboardRepository(topAdsDashboardDataSource: TopAdsDashboardDataSource): TopAdsDashboardRepository {
        return TopAdsDashboardRepositoryImpl(topAdsDashboardDataSource)
    }

    @TopAdsDashboardScope
    @Provides
    fun provideTopAdsSourceTaggingLocal(@ApplicationContext context: Context): TopAdsSourceTaggingLocal {
        return TopAdsSourceTaggingLocal(context)
    }

    @TopAdsDashboardScope
    @Provides
    fun provideTopAdsSourceTaggingDataSource(topAdsSourceTaggingLocal: TopAdsSourceTaggingLocal): TopAdsSourceTaggingDataSource {
        return TopAdsSourceTaggingDataSource(topAdsSourceTaggingLocal)
    }

    @TopAdsDashboardScope
    @Provides
    fun provideTopAdsSourceTaggingRepository(dataSource: TopAdsSourceTaggingDataSource): TopAdsSourceTaggingRepository {
        return TopAdsSourceTaggingRepositoryImpl(dataSource)
    }

    @TopAdsDashboardScope
    @Provides
    fun provideDeleteTopAdsStatisticsUseCase() = DeleteTopAdsStatisticsUseCase()

    @TopAdsDashboardScope
    @Provides
    fun provideDeleteTopAdsTotalAdUseCase() = DeleteTopAdsTotalAdUseCase()

    @TopAdsDashboardScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @TopAdsDashboardScope
    @Provides
    @Named("Main")
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}
