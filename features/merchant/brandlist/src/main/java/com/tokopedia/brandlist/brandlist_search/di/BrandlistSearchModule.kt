package com.tokopedia.brandlist.brandlist_search.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.common.BrandlistDispatcherProvider
import com.tokopedia.brandlist.common.BrandlistDispatcherProviderImp
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [BrandlistSearchViewModelModule::class])
@BrandlistSearchScope
class BrandlistSearchModule {

    @BrandlistSearchScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @BrandlistSearchScope
    @Provides
    fun provideDispatcherProvider(): BrandlistDispatcherProvider = BrandlistDispatcherProviderImp()

}