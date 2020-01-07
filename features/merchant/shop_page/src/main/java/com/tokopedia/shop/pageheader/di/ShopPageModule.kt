package com.tokopedia.shop.pageheader.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides

@Module(includes = [ShopPageViewModelModule::class])
@ShopPageScope
class ShopPageModule {

    @Provides
    @ShopPageScope
    fun provideUserSession(@ApplicationContext context: Context) = UserSession(context)
}
