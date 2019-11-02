package com.tokopedia.loyalty.view.activity

import android.content.Context
import android.content.Intent

class TokoPointWebviewActivity {

    companion object {

        @JvmStatic
        fun getIntent(context: Context, url: String): Intent {
            return Intent(url)
        }

        @JvmStatic
        fun getIntentWithTitle(context: Context, url: String, pageTitle: String): Intent {
            return Intent(url)
        }
    }
}