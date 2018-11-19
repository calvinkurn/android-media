package com.tokopedia.flashsale.management.product.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class FlashSaleTncActivity : BaseSimpleActivity() {
    var tncString = ""

    override fun getNewFragment(): Fragment? {
        return FlashSaleTncFragment.newInstance(tncString)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        tncString = intent.extras.getString(EXTRA_TNC)
        super.onCreate(savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun createIntent(context: Context,
                         tncString: String): Intent {
            return Intent(context, FlashSaleTncActivity::class.java)
                    .putExtra(EXTRA_TNC, tncString)
        }
        private const val EXTRA_TNC = "tnc"
    }
}