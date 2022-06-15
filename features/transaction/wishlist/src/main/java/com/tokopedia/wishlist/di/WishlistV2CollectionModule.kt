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


@Module
class WishlistV2CollectionModule(private val activity: Activity) {
    @WishlistV2CollectionScope
    @Provides
    fun provideContext(): Context = activity

    @WishlistV2CollectionScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
}