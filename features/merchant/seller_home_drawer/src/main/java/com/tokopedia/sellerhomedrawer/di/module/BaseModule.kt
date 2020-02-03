package com.tokopedia.sellerhomedrawer.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.sellerhomedrawer.di.SellerHomeDashboardScope
import com.tokopedia.sellerhomedrawer.presentation.view.helper.SellerDrawerHelper
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides
import javax.inject.Named

@SellerHomeDashboardScope
@Module(includes = [RetrofitModule::class])
class BaseModule(var context: Context) {

    @Provides
    fun provideContext(): Context = context

    @Provides
    @Named("application")
    fun provideApplicationContext(): Context = context.applicationContext

    @Provides
    fun provideDrawerCache(context: Context): LocalCacheHandler = LocalCacheHandler(context.applicationContext, SellerDrawerHelper.DRAWER_CACHE)

    @Provides
    fun provideUserSession(context: Context): UserSession = UserSession(context)


}