package com.tokopedia.wishlist.di

import android.app.Activity
import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
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

/**
 * Created by fwidjaja on 16/10/21.
 */

@Module
class WishlistV2Module(private val activity: Activity) {
    @WishlistV2Scope
    @Provides
    fun provideContext(): Context = activity

    @WishlistV2Scope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @WishlistV2Scope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @WishlistV2Scope
    @Provides
    fun providePreference(@ApplicationContext context: Context): WishlistV2LayoutPreference = WishlistV2LayoutPreference(context)

    @Provides
    @WishlistV2Scope
    fun provideTopAdsImageViewUseCase(userSession: UserSessionInterface, topAdsIrisSession: TopAdsIrisSession): TopAdsImageViewUseCase = TopAdsImageViewUseCase(userSession.userId, TopAdsRepository(), topAdsIrisSession.getSessionId())
}
