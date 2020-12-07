package com.tokopedia.notifcenter.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.notifcenter.common.network.NotifcenterCacheManager
import com.tokopedia.notifcenter.common.network.NotifcenterCacheManagerImpl
import com.tokopedia.notifcenter.di.scope.NotificationContext
import com.tokopedia.notifcenter.di.scope.NotificationScope
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.repository.TopAdsRepository
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [TopAdsWishlistModule::class])
class NotificationModule {

    @Provides
    @NotificationScope
    fun provideTopAdsImageViewUseCase(userSession: UserSessionInterface): TopAdsImageViewUseCase {
        return TopAdsImageViewUseCase(userSession.userId, TopAdsRepository())
    }

    @Provides
    @NotificationScope
    fun provideGetRecomendationUseCase(@Named("recommendationQuery") recomQuery: String,
                                       graphqlUseCase: GraphqlUseCase,
                                       userSession: UserSessionInterface): GetRecommendationUseCase {
        return GetRecommendationUseCase(recomQuery, graphqlUseCase, userSession)
    }

    @Provides
    @NotificationScope
    @Named("recommendationQuery")
    fun provideRecommendationRawQuery(
            @NotificationContext context: Context
    ): String {
        return GraphqlHelper.loadRawString(
                context.resources,
                com.tokopedia.recommendation_widget_common.R.raw.query_recommendation_widget
        )
    }

    @Provides
    @NotificationScope
    fun provideAddWishlistUseCase(
            @NotificationContext context: Context
    ): AddWishListUseCase {
        return AddWishListUseCase(context)
    }

    @Provides
    @NotificationScope
    fun provideRemoveWishlistUseCase(
            @NotificationContext context: Context
    ): RemoveWishListUseCase {
        return RemoveWishListUseCase(context)
    }

    @NotificationScope
    @Provides
    internal fun provideNotificationCacheManager(
            @NotificationContext context: Context
    ): NotifcenterCacheManager {
        val notifCachePref = context.getSharedPreferences(
                "prefs_notifcenter",
                Context.MODE_PRIVATE
        )
        return NotifcenterCacheManagerImpl(notifCachePref)
    }

}

