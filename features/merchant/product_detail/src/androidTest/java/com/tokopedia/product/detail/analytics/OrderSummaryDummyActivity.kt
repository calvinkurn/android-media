package com.tokopedia.product.detail.analytics

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class OrderSummaryDummyActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        if(intent.data.toString() == APP_LINK_ORDER_SUMMARY) {
            setResult(Activity.RESULT_OK)
        }
        finish()
        super.onCreate(savedInstanceState)
    }

    companion object {
        private const val APP_LINK_ORDER_SUMMARY = "tokopedia-android-internal://marketplace/one-click-checkout"
    }
}