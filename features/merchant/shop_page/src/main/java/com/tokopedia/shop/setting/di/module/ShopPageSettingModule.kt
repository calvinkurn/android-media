package com.tokopedia.shop.setting.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.shop.setting.di.scope.ShopPageSettingScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@ShopPageSettingScope
@Module(includes = [ShopPageSettingViewModelModule::class])
class ShopPageSettingModule {

    @ShopPageSettingScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }
}