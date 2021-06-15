package com.tokopedia.shop.favourite.di.module

import android.content.Context
import com.tokopedia.shop.common.di.ShopPageContext
import com.tokopedia.shop.favourite.di.scope.ShopFavouriteScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class
ShopFavouriteModule {
    @ShopFavouriteScope
    @Provides
    fun provideUserSessionInterface(@ShopPageContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }
}