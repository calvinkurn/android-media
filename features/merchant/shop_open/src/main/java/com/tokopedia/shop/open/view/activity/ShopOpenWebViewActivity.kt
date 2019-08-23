package com.tokopedia.shop.open.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.view.Menu
import com.tokopedia.abstraction.base.view.activity.BaseWebViewActivity
import com.tokopedia.abstraction.base.view.fragment.BaseSessionWebViewFragment

/**
 * Created by Yehezkiel on 22/05/18.
 */
class ShopOpenWebViewActivity : BaseWebViewActivity() {

    companion object {
        private const val EXTRA_URL = "EXTRA_URL"
        private const val EXTRA_CALLER_CODE = "EXTRA_CALLER_CODE"

        fun newInstance(context: Context, url: String, title: String?): Intent {
            val intent = Intent(context, ShopOpenWebViewActivity::class.java)
            intent.putExtra(EXTRA_URL, url)
            intent.putExtra(BaseWebViewActivity.EXTRA_TITLE, title)
            return intent
        }

    }

    /**
     * @param context activity context
     * @param url of webview to be displayed
     * @param title will display on toolbar Title, see implementation on BaseWebViewActivity@setupToolbar
     * @return Intent to start activity with
     */

    override fun getContactUsIntent(): Intent? {
        return null
    }

    override fun getNewFragment(): Fragment {
        val mUrl = intent.getStringExtra(EXTRA_URL)
        return BaseSessionWebViewFragment.newInstance(mUrl)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun isShowCloseButton(): Boolean {
        return true
    }
}