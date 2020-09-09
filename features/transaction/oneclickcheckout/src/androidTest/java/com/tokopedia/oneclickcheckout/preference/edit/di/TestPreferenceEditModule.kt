package com.tokopedia.oneclickcheckout.preference.edit.di

import android.app.Activity
import dagger.Module
import dagger.Provides

@Module
class TestPreferenceEditModule(activity: Activity) : PreferenceEditModule(activity) {

    @PreferenceEditScope
    @Provides
    override fun providePaymentListingUrl(): String {
        // override payment listing url to prevent loading prod url
        return ""
    }
}