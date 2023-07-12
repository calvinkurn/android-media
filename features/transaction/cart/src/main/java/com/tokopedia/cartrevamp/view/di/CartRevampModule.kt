package com.tokopedia.cartrevamp.view.di

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.atc_common.AtcConstant
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCart
import com.tokopedia.purchase_platform.common.analytics.EPharmacyAnalytics
import com.tokopedia.purchase_platform.common.di.PurchasePlatformBaseModule
import com.tokopedia.purchase_platform.common.schedulers.DefaultSchedulers
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.recommendation_widget_common.di.RecommendationModule
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.GetWishlistV2UseCase
import dagger.Module
import dagger.Provides
import rx.subscriptions.CompositeSubscription
import javax.inject.Named

@Module(
    includes = [
        RecommendationModule::class,
        PurchasePlatformBaseModule::class
    ]
)
class CartRevampModule {

    @ActivityScope
    @Provides
    fun provideAppContext(@ApplicationContext context: Context): Context {
        // for recommendation use case
        return context
    }

    @ActivityScope
    @Provides
    fun provideCompositeSubscription(): CompositeSubscription {
        return CompositeSubscription()
    }

    @ActivityScope
    @Provides
    fun provideGetWishlistV2UseCase(graphqlRepository: GraphqlRepository): GetWishlistV2UseCase {
        return GetWishlistV2UseCase(graphqlRepository)
    }

    @ActivityScope
    @Provides
    fun providesAddWishlistV2UseCase(graphqlRepository: GraphqlRepository): AddToWishlistV2UseCase {
        return AddToWishlistV2UseCase(graphqlRepository)
    }

    @ActivityScope
    @Provides
    fun providesRemoveWishlistV2UseCase(graphqlRepository: GraphqlRepository): DeleteWishlistV2UseCase {
        return DeleteWishlistV2UseCase(graphqlRepository)
    }

    @ActivityScope
    @Provides
    fun provideCheckoutAnalyticsCart(@ApplicationContext context: Context): CheckoutAnalyticsCart {
        return CheckoutAnalyticsCart(context)
    }

    // for seamless login usecase
    @ActivityScope
    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    // for seamless login usecase
    @ActivityScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return Interactor.getInstance().graphqlRepository
    }

    @ActivityScope
    @Provides
    fun provideExecutorSchedulers(): ExecutorSchedulers = DefaultSchedulers

    @ActivityScope
    @Provides
    @Named(AtcConstant.MUTATION_UPDATE_CART_COUNTER)
    fun provideUpdateCartCounterMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.gql_update_cart_counter)
    }

    @ActivityScope
    @Provides
    fun provideEPharmacyAnalytics(userSession: UserSessionInterface): EPharmacyAnalytics {
        return EPharmacyAnalytics(userSession.userId)
    }
}
