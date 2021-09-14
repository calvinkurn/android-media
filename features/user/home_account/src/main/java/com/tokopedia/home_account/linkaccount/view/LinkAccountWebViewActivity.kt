package com.tokopedia.home_account.linkaccount.view

import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_LD
import com.tokopedia.home_account.R
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
import com.tokopedia.webview.BaseSimpleWebViewActivity
import com.tokopedia.webview.WebViewHelper

/**
 * Created by Yoris on 10/08/21.
 */

class LinkAccountWebViewActivity: BaseSimpleWebViewActivity() {

    companion object {
        const val KEY_URL = "webview_url"
        const val BASE_URL = "https://accounts-staging.tokopedia.com/account-link/v1/gojek-auth"

        fun newInstance(context: Context?, url: String?): Intent {
            val intent = Intent(context, LinkAccountWebViewActivity::class.java)
            intent.putExtra(KEY_URL, url)
            return intent
        }

        private fun appendSuccessQuery(url: String): String  {
            return "${url}&page=success"
        }

        fun getSuccessUrl(context: Context, redirectionApplink: String = ""): String {
            return appendSuccessQuery(getLinkAccountUrl(context, redirectionApplink))
        }

        fun gotoSuccessPage(activity: FragmentActivity?, redirectionApplink: String) {
            activity?.run {
                val i = newInstance(this, getSuccessUrl(this, redirectionApplink))
                startActivityForResult(i, LinkAccountFragment.LINK_ACCOUNT_WEBVIEW_REQUEST)
            }
        }

        fun getLinkAccountUrl(context: Context, redirectionApplink: String): String {
            var finalUrl = WebViewHelper.appendGAClientIdAsQueryParam(BASE_URL, context)
            if (finalUrl != null) {
                finalUrl += "&ld=${redirectionApplink.encodeToUtf8()}"
            } else {
                finalUrl = "${BASE_URL}?ld=${redirectionApplink.encodeToUtf8()}"
            }
            return finalUrl
        }
    }

    fun showToolbar() {
        supportActionBar?.show()
    }

    fun hideToolbar() {
        supportActionBar?.hide()
    }

    fun hideToolbarBackBtn() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_link_account_skip) {
            (fragment as LinkAccountWebviewFragment).showSkipDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    fun hideSkipBtnToolbar() {
        val skipBtn = toolbar?.menu?.findItem(R.id.menu_link_account_skip)
        skipBtn?.isVisible = false
    }

    fun showSkipBtnToolbar() {
        val skipBtn = toolbar?.menu?.findItem(R.id.menu_link_account_skip)
        skipBtn?.isVisible = true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_link_account, menu)

        val item = menu.findItem(R.id.menu_link_account_skip)
        val s = SpannableString("Lewatin Dulu")
        s.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.unify_G500)), 0, s.length, 0)
        item.title = s
        return true
    }

    override fun getNewFragment(): Fragment {
        val redirection = intent.getStringExtra(PARAM_LD) ?: ApplinkConst.HOME
        val mUrl = getLinkAccountUrl(this, redirection)
        return LinkAccountWebviewFragment.newInstance(mUrl)
    }

}