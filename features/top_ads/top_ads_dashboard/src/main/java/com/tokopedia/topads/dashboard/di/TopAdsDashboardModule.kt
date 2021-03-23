package com.tokopedia.topads.dashboard.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant
import com.tokopedia.shop.common.constant.ShopCommonUrl
import com.tokopedia.shop.common.data.source.ShopCommonDataSource
import com.tokopedia.shop.common.data.source.cloud.ShopCommonCloudDataSource
import com.tokopedia.shop.common.data.source.cloud.api.ShopCommonApi
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.topads.sourcetagging.data.repository.TopAdsSourceTaggingRepositoryImpl
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingDataSource
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingLocal
import com.tokopedia.topads.sourcetagging.domain.repository.TopAdsSourceTaggingRepository
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Named

/**
 * Created by hadi.putra on 23/04/18.
 */

@Module
class TopAdsDashboardModule {

    @Provides
    @TopAdsDashboardScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = com.tokopedia.user.session.UserSession(context)


    @ShopWsQualifier
    @TopAdsDashboardScope
    @Provides
    fun provideWSRetrofit(@ShopQualifier okHttpClient: OkHttpClient,
                          retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(ShopCommonUrl.BASE_WS_URL).client(okHttpClient).build()
    }

    @Provides
    @TopAdsDashboardScope
    fun provideShopCommonApi(@ShopQualifier retrofit: Retrofit): ShopCommonApi {
        return retrofit.create(ShopCommonApi::class.java)
    }


    @Provides
    @TopAdsDashboardScope
    fun provideShopCommonCloudDataSource(shopCommonApi: ShopCommonApi): ShopCommonCloudDataSource {
        return ShopCommonCloudDataSource(shopCommonApi)
    }

    @Provides
    @TopAdsDashboardScope
    fun provideShopCommonDataSource(shopInfoCloudDataSource: ShopCommonCloudDataSource): ShopCommonDataSource {
        return ShopCommonDataSource(shopInfoCloudDataSource)
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
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @TopAdsDashboardScope
    @Provides
    @Named("Main")
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @TopAdsDashboardScope
    @Named("Main")
    fun provideDispatcherProvider(): CoroutineDispatchers = CoroutineDispatchersProvider

    @Provides
    @Named(GQLQueryNamedConstant.SHOP_INFO)
    fun provideGqlQueryShopInfo(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_shop_info)
    }

    @TopAdsDashboardScope
    @Provides
    fun provideGqlGetShopInfoUseCase(graphqlUseCase: MultiRequestGraphqlUseCase,
                                     @Named(GQLQueryNamedConstant.SHOP_INFO)
                                     gqlQuery: String): GQLGetShopInfoUseCase =
            GQLGetShopInfoUseCase(gqlQuery, graphqlUseCase)

    @TopAdsDashboardScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(): MultiRequestGraphqlUseCase =
            GraphqlInteractor.getInstance().multiRequestGraphqlUseCase
}
