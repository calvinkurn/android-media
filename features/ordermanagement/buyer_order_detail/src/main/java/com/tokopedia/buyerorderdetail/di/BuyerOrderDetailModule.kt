package com.tokopedia.buyerorderdetail.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant
import com.tokopedia.digital.digital_recommendation.di.DigitalRecommendationCacheEnablerQualifier
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named
import com.tokopedia.atc_common.R as atc_commonR

@Module
class BuyerOrderDetailModule {

    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @BuyerOrderDetailScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface =
        UserSession(context)

    @BuyerOrderDetailScope
    @Provides
    @Named(BuyerOrderDetailMiscConstant.DAGGER_ATC_QUERY_NAME)
    fun provideAtcMultiQuery(@ApplicationContext context: Context): String =
        GraphqlHelper.loadRawString(
            context.resources,
            atc_commonR.raw.mutation_add_to_cart_multi
        )

    @BuyerOrderDetailScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(
        graphqlRepository: GraphqlRepository
    ): MultiRequestGraphqlUseCase = MultiRequestGraphqlUseCase(graphqlRepository)


    @BuyerOrderDetailScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @BuyerOrderDetailScope
    @Provides
    @DigitalRecommendationCacheEnablerQualifier
    fun provideCacheEnabler(remoteConfig: RemoteConfig): Boolean {
        return remoteConfig.getBoolean(RemoteConfigKey.ANDROID_ENABLE_DIGITAL_GQL_CACHE, false)
    }
}
