package com.tokopedia.shop_showcase.shop_showcase_management.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.shop_showcase.R
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [ShopShowcaseManagementViewModelModule::class])
@ShopShowcaseManagementScope
class ShopShowcaseManagementModule {

    @ShopShowcaseManagementScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

}