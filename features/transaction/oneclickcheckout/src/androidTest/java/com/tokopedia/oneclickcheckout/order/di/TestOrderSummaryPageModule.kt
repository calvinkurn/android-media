package com.tokopedia.oneclickcheckout.order.di

import android.app.Activity
import com.tokopedia.oneclickcheckout.common.OCC_OVO_ACTIVATION_URL
import com.tokopedia.oneclickcheckout.common.OCC_QUANTITY_DEBOUNCE
import com.tokopedia.oneclickcheckout.common.robot.DEBOUNCE_TIME
import dagger.Provides
import javax.inject.Named

class TestOrderSummaryPageModule(activity: Activity) : OrderSummaryPageModule(activity) {

    @OrderSummaryPageScope
    @Provides
    @Named(OCC_OVO_ACTIVATION_URL)
    override fun provideOvoActivationLink(): String {
        return "https://www.google.com"
    }

    @Provides
    @OrderSummaryPageScope
    @Named(OCC_QUANTITY_DEBOUNCE)
    override fun provideQuantityDebounceLength(): Long {
        return DEBOUNCE_TIME
    }
}
