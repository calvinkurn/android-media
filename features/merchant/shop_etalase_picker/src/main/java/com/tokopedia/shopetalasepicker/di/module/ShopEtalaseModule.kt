package com.tokopedia.shopetalasepicker.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.shopetalasepicker.di.scope.ShopEtalaseScope
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides
import com.tokopedia.user.session.UserSessionInterface


@Module
class ShopEtalaseModule {
    @ShopEtalaseScope
    @Provides
    fun provideAnalyticTracker(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
}