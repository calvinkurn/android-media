package com.tokopedia.oneclickcheckout.preference.edit.di

import android.app.Activity
import com.tokopedia.oneclickcheckout.common.PAYMENT_LISTING_URL
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class TestPreferenceEditModule(activity: Activity) : PreferenceEditModule(activity) {

    @PreferenceEditScope
    @Provides
    @Named(PAYMENT_LISTING_URL)
    override fun providePaymentListingUrl(): String {
        // override payment listing url to prevent loading prod url
        return ""
    }
}