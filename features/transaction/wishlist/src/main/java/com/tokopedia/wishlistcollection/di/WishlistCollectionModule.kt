package com.tokopedia.wishlistcollection.di

import android.app.Activity
import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.repository.TopAdsRepository
import com.tokopedia.topads.sdk.utils.TopAdsIrisSession
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.util.WishlistV2LayoutPreference
import dagger.Module
import dagger.Provides


@Module
class WishlistCollectionModule(private val activity: Activity) {
    @ActivityScope
    @Provides
    fun provideContext(): Context = activity

    @ActivityScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ActivityScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @ActivityScope
    @Provides
    fun providePreference(@ApplicationContext context: Context): WishlistV2LayoutPreference = WishlistV2LayoutPreference(context)

    @Provides
    @ActivityScope
    fun provideTopAdsImageViewUseCase(userSession: UserSessionInterface,topAdsIrisSession: TopAdsIrisSession): TopAdsImageViewUseCase = TopAdsImageViewUseCase(userSession.userId, TopAdsRepository(),topAdsIrisSession.getSessionId())
}