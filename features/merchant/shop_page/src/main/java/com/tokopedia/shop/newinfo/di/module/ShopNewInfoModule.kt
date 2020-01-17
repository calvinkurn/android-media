package com.tokopedia.shop.newinfo.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.shop.newinfo.di.scope.ShopNewInfoScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@ShopNewInfoScope
@Module(includes = [ShopNewInfoViewModelModule::class])
class ShopNewInfoModule {

    @ShopNewInfoScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }
}