package com.tokopedia.checkout.webview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.tokopedia.logisticCommon.data.analytics.CodAnalytics
import com.tokopedia.webview.BaseSessionWebViewFragment
import com.tokopedia.webview.BaseSimpleWebViewActivity
import com.tokopedia.webview.KEY_TITLE

class CheckoutWebViewActivity : BaseSimpleWebViewActivity() {

    private var mCodTracker: CodAnalytics? = null
    private var mCallerCode = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCodTracker = CodAnalytics()
        mCallerCode = intent.getIntExtra(EXTRA_CALLER_CODE, -1)
    }

    override fun getNewFragment(): Fragment {
        val mUrl = intent.getStringExtra(EXTRA_URL)
        return BaseSessionWebViewFragment.newInstance(mUrl!!)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun isShowCloseButton(): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (mCallerCode == CALLER_CODE_COD) {
                    mCodTracker?.eventClickXPadaSyarat()
                }
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_URL = "EXTRA_URL"
        const val EXTRA_CALLER_CODE = "EXTRA_CALLER_CODE"
        const val CALLER_CODE_COD = 7

        /**
         * @param context activity context
         * @param url of webview to be displayed
         * @param title will display on toolbar Title
         * @param callerCode for analytics purpose
         * @return Intent to start activity with
         */
        @JvmOverloads
        fun newInstance(
            context: Context,
            url: String,
            title: String,
            callerCode: Int = -1
        ): Intent {
            val intent = Intent(context, CheckoutWebViewActivity::class.java)
            intent.putExtra(EXTRA_URL, url)
            intent.putExtra(EXTRA_CALLER_CODE, callerCode)
            intent.putExtra(KEY_TITLE, title)
            return intent
        }
    }
}
