package com.tokopedia.payment.setting.detail.di

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class DetailCreditCardModule(val activity: Activity) {

    @Provides
    fun getContext(): Context = activity
}