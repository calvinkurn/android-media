package com.tokopedia.oneclickcheckout.order.di

import android.app.Activity
import com.tokopedia.oneclickcheckout.common.OVO_ACTIVATION_URL
import dagger.Provides
import javax.inject.Named

class TestOrderSummaryPageModule(activity: Activity): OrderSummaryPageModule(activity) {

    @OrderSummaryPageScope
    @Provides
    @Named(OVO_ACTIVATION_URL)
    override fun provideOvoActivationLink(): String {
        return "https://www.google.com"
    }
}