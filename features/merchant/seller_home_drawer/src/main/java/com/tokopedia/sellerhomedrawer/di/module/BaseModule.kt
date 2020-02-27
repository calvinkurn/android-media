package com.tokopedia.sellerhomedrawer.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.sellerhomedrawer.di.SellerHomeDashboardScope
import com.tokopedia.sellerhomedrawer.presentation.view.helper.SellerDrawerHelper
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides

@SellerHomeDashboardScope
@Module(includes = [RetrofitModule::class])
class BaseModule(val context: Context) {

    @SellerHomeDashboardScope
    @Provides
    fun provideDrawerCache(@ApplicationContext context: Context): LocalCacheHandler = LocalCacheHandler(context, SellerDrawerHelper.DRAWER_CACHE)

    @SellerHomeDashboardScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSession = UserSession(context)

    @SellerHomeDashboardScope
    @ApplicationContext
    @Provides
    fun provideContext(): Context = context.applicationContext

}