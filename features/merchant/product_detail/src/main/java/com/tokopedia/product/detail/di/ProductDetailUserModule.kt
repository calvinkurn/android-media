package com.tokopedia.product.detail.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class ProductDetailUserModule {

    @Provides
    @ProductDetailScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = com.tokopedia.user.session.UserSession(context)

}