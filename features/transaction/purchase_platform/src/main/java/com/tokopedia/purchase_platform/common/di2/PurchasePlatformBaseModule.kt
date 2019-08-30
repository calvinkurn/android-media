package com.tokopedia.purchase_platform.common.di2

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.purchase_platform.common.router.ICheckoutModuleRouter
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by Irfan Khoirul on 2019-08-26.
 */

@Module
class PurchasePlatformBaseModule {

    @Provides
    fun provideRouter(@ApplicationContext context: Context): ICheckoutModuleRouter {
        return context as ICheckoutModuleRouter
    }

    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

}