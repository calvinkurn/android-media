package com.tokopedia.pms.howtopay_native.di

import android.app.Activity
import android.content.res.Resources
import com.tokopedia.pms.R
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides


@Module
class HowToPayModule(val activity: Activity) {

    @Provides
    fun getResource(): Resources {
        return activity.application.resources
    }

    @Provides
    @HowToPayRawJsonFileResID
    fun getHowToPayRawJsonFileResID(): Int = R.raw.howtopay_instruction


    @Provides
    fun provideUserSession(): UserSessionInterface {
        return UserSession(activity.applicationContext)
    }
}