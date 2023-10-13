package com.tokopedia.digital_product_detail.presentation.webview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.digital_product_detail.R
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.webview.BaseSimpleWebViewActivity

class RechargeCheckBalanceWebViewActivity : BaseSimpleWebViewActivity() {

    override fun getScreenName(): String {
        return RechargeCheckBalanceWebViewActivity::class.java.simpleName
    }

    override fun getNewFragment(): Fragment {
        val bundle = intent.extras
        val webUrl = bundle?.getString(EXTRA_CHECK_BALANCE_WEB_URL, "").toEmptyStringIfNull()
        return RechargeCheckBalanceWebViewFragment.createInstance(webUrl)
    }

    override fun getParentViewResourceID() = R.id.parent_view

    override fun getLayoutRes(): Int {
        return R.layout.activity_recharge_check_balance_web_view
    }

    companion object {
        fun createInstance(
            context: Context,
            webUrl: String
        ): Intent {
            val intent = Intent(context, RechargeCheckBalanceWebViewActivity::class.java)
            val extras = Bundle().apply {
                putString(EXTRA_CHECK_BALANCE_WEB_URL, webUrl)
            }
            intent.putExtras(extras)
            return intent
        }

        const val EXTRA_CHECK_BALANCE_WEB_URL = "EXTRA_CHECK_BALANCE_WEB_URL"
    }
}
