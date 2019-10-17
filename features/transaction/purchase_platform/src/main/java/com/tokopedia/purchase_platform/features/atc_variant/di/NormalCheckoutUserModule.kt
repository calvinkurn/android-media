package com.tokopedia.purchase_platform.features.atc_variant.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@NormalCheckoutScope
@Module
class NormalCheckoutUserModule {

    @Provides
    @NormalCheckoutScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

}