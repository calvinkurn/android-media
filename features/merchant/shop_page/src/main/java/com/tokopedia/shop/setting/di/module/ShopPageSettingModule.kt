package com.tokopedia.shop.setting.di.module

import android.content.Context
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.shop.common.di.ShopPageContext
import com.tokopedia.shop.setting.di.scope.ShopPageSettingScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [ShopPageSettingViewModelModule::class])
class ShopPageSettingModule {

    @ShopPageSettingScope
    @Provides
    fun provideUserSessionInterface(@ShopPageContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @ShopPageSettingScope
    @Provides
    fun provideCoroutineDispatchers(): CoroutineDispatchers = CoroutineDispatchersProvider
}