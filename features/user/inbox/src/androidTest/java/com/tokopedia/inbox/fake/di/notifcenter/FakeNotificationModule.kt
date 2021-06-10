package com.tokopedia.inbox.fake.di.notifcenter

import android.content.Context
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.FakeNotifcenterCacheManager
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.topads.FakeTopAdsRepository
import com.tokopedia.notifcenter.common.network.NotifcenterCacheManager
import com.tokopedia.notifcenter.di.scope.NotificationContext
import com.tokopedia.notifcenter.di.scope.NotificationScope
import com.tokopedia.recommendation_widget_common.di.RecommendationModule
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides

@Module(includes = [RecommendationModule::class, TopAdsWishlistModule::class])
class FakeNotificationModule {

    @Provides
    @NotificationScope
    fun provideTopAdsImageViewUseCase(
        userSession: UserSessionInterface,
        fakeRepo: FakeTopAdsRepository
    ): TopAdsImageViewUseCase {
        return TopAdsImageViewUseCase(userSession.userId, fakeRepo)
    }

    @Provides
    @NotificationScope
    fun provideFakeTopAdsRepository(): FakeTopAdsRepository {
        return FakeTopAdsRepository()
    }

    // -- separator -- //

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

    @Provides
    @NotificationScope
    fun provideNotifCacheManager(
        fake: FakeNotifcenterCacheManager
    ): NotifcenterCacheManager {
        return fake
    }

    @Provides
    @NotificationScope
    fun provideFakeNotifCacheManager(): FakeNotifcenterCacheManager {
        return FakeNotifcenterCacheManager()
    }

}

