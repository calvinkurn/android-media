package com.tokopedia.payment.setting.list.di

import android.app.Activity
import android.content.Context
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class SettingListPaymentModule(val activity: Activity) {

    @Provides
    fun getContext(): Context = activity

    @SettingListPaymentScope
    @Provides
    fun provideUserSession(context: Context): UserSessionInterface {
        return UserSession(context)
    }
}
