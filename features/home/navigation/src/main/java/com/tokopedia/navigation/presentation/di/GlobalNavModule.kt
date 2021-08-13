package com.tokopedia.navigation.presentation.di

import android.content.Context
import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.navigation.R
import com.tokopedia.navigation.data.mapper.NotificationRequestMapper
import com.tokopedia.navigation.domain.GetBottomNavNotificationUseCase
import com.tokopedia.navigation.domain.GetDrawerNotificationUseCase
import com.tokopedia.navigation.domain.GetNewFeedCheckerUseCase
import com.tokopedia.navigation.presentation.presenter.MainParentPresenter
import com.tokopedia.recommendation_widget_common.di.RecommendationModule
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Created by Lukas on 2019-07-31
 */

@Module(includes = [TopAdsWishlistModule::class, RecommendationModule::class])
class GlobalNavModule {
    @Provides
    fun provideMainParentPresenter(getNotificationUseCase: GetBottomNavNotificationUseCase, userSession: UserSessionInterface): MainParentPresenter {
        return MainParentPresenter(getNotificationUseCase, userSession)
    }

    @Provides
    fun provideGetBottomNavNotificationUseCase(
            getDrawerNotificationUseCase: GetDrawerNotificationUseCase,
            getNewFeedCheckerUseCase: GetNewFeedCheckerUseCase): GetBottomNavNotificationUseCase {
        return GetBottomNavNotificationUseCase(
                getDrawerNotificationUseCase,
                getNewFeedCheckerUseCase)
    }

    @Provides
    fun provideGetNewFeedCheckerUseCase(@ApplicationContext context: Context): GetNewFeedCheckerUseCase {
        return GetNewFeedCheckerUseCase(context)
    }

    @Provides
    fun provideGetDrawerNotificationUseCase(@ApplicationContext context: Context, graphqlUseCase: GraphqlUseCase): GetDrawerNotificationUseCase {
        return GetDrawerNotificationUseCase(context, graphqlUseCase, NotificationRequestMapper())
    }

    @Provides
    fun provideAddWishlistUseCase(@ApplicationContext context: Context): AddWishListUseCase = AddWishListUseCase(context)

    @Provides
    fun provideRemoveWishlistUseCase(@ApplicationContext context: Context): RemoveWishListUseCase = RemoveWishListUseCase(context)

    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }
}
