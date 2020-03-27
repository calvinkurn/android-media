package com.tokopedia.product.addedit.detail.di

import android.content.Context
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [AddEditProductDetailViewModelModule::class])
@AddEditProductDetailScope
class AddEditProductDetailModule {

    @AddEditProductDetailScope
    @Provides
    fun provideUserSession(context: Context): UserSessionInterface = UserSession(context)
}