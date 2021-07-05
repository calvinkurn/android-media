package com.tokopedia.product.addedit.detail.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides


@Module(includes = [AddEditProductDetailViewModelModule::class])
class AddEditProductDetailModule {

    @AddEditProductDetailScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

}