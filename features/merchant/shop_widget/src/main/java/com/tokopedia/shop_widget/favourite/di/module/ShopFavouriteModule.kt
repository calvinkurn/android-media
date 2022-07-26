package com.tokopedia.shop_widget.favourite.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.shop.common.di.ShopCommonModule
import com.tokopedia.shop_widget.favourite.di.scope.ShopFavouriteScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [ShopCommonModule::class])
class ShopFavouriteModule {
    @ShopFavouriteScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }
}