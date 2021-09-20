package com.tokopedia.home_account.linkaccount.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.tokopedia.home_account.R
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
import com.tokopedia.track.TrackApp
import com.tokopedia.webview.BaseSimpleWebViewActivity

/**
 * Created by Yoris on 10/08/21.
 */

class LinkAccountWebViewActivity: BaseSimpleWebViewActivity() {

    companion object {
        const val KEY_URL = "webview_url"
        const val BASE_URL = "https://accounts-staging.tokopedia.com/account-link/v1/gojek-auth"

        const val QUERY_LD = "ld"
        const val QUERY_PAGE = "page"
        const val QUERY_APP_CLIENT_ID = "appClientId"

        fun newInstance(context: Context?, url: String?): Intent {
            val intent = Intent(context, LinkAccountWebViewActivity::class.java)
            intent.putExtra(KEY_URL, url)
            return intent
        }

        fun getSuccessUrl(uri: Uri): Uri {
            return uri.buildUpon().appendQueryParameter(QUERY_PAGE, "success").build()
        }

        fun gotoSuccessPage(activity: FragmentActivity?, redirectionApplink: String) {
            activity?.run {
                try {
                    val baseUrl = getLinkAccountUrlFix(redirectionApplink)
                    if(baseUrl != null) {
                        val i = newInstance(this, getSuccessUrl(baseUrl).toString())
                        startActivityForResult(i, LinkAccountFragment.LINK_ACCOUNT_WEBVIEW_REQUEST)
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }

        fun getLinkAccountUrlFix(redirectionApplink: String): Uri? {
            try {
                val uri = Uri.parse(BASE_URL)
                val clientID = TrackApp.getInstance().gtm.cachedClientIDString
                return uri.buildUpon()
                        .appendQueryParameter(QUERY_APP_CLIENT_ID, clientID)
                        .appendQueryParameter(QUERY_LD, redirectionApplink.encodeToUtf8())
                        .build()
            }catch (ex: Exception) {
                ex.printStackTrace()
            }
            return null
        }
    }

    fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideToolbar()
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
        val url = intent.getStringExtra(KEY_URL) ?: ""
        return LinkAccountWebviewFragment.newInstance(url)
    }

}