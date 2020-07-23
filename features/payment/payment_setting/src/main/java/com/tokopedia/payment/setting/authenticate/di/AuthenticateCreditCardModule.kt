package com.tokopedia.payment.setting.authenticate.di

import android.app.Activity
import android.content.Context
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class AuthenticateCreditCardModule(val activity: Activity) {

    @Provides
    fun getContext(): Context = activity

    @Provides
    fun provideUserSession(context: Context): UserSessionInterface {
        return UserSession(context)
    }

}
