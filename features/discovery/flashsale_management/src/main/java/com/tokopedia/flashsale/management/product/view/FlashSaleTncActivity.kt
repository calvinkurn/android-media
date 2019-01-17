package com.tokopedia.flashsale.management.product.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class FlashSaleTncActivity : BaseSimpleActivity() {
    var tncString = ""
    var tncLastUpdated = ""

    override fun getNewFragment(): Fragment? {
        return FlashSaleTncFragment.newInstance(tncString, tncLastUpdated)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        tncString = intent.extras.getString(EXTRA_TNC)
        tncLastUpdated = intent.extras.getString(EXTRA_TNC_LAST_UPDATED, "")
        super.onCreate(savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun createIntent(context: Context,
                         tncString: String,
                         tncLastUpdated: String = ""): Intent {
            return Intent(context, FlashSaleTncActivity::class.java)
                    .putExtra(EXTRA_TNC, tncString)
                    .putExtra(EXTRA_TNC_LAST_UPDATED, tncLastUpdated)
        }

        private const val EXTRA_TNC = "tnc"
        private const val EXTRA_TNC_LAST_UPDATED = "tnc_last_updated"
    }
}