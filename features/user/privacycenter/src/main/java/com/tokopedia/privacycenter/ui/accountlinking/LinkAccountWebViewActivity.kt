package com.tokopedia.privacycenter.ui.accountlinking

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
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.di.DaggerPrivacyCenterComponent
import com.tokopedia.privacycenter.di.PrivacyCenterComponent
import com.tokopedia.track.TrackApp
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.webview.BaseSimpleWebViewActivity

/**
 * Created by Yoris on 10/08/21.
 */
@Deprecated("Remove this class after integrating SCP Login to Tokopedia")
class LinkAccountWebViewActivity : BaseSimpleWebViewActivity(), HasComponent<PrivacyCenterComponent> {

    companion object {
        const val KEY_URL = "webview_url"

        private const val QUERY_LD = "ld"
        private const val QUERY_PAGE = "page"
        private const val QUERY_APP_CLIENT_ID = "appClientId"

        private const val LINK_ACC_PATH = "account-link/v1/gojek-auth"
        private const val LINK_ACCOUNT_WEBVIEW_REQUEST = 100

        private const val KEY_SUCCESS = "success"

        private fun getAccountLinkUrl(): String =
            TokopediaUrl.Companion.getInstance().ACCOUNTS.plus(LINK_ACC_PATH)

        fun newInstance(context: Context?, url: String?): Intent {
            val intent = Intent(context, LinkAccountWebViewActivity::class.java)
            intent.putExtra(KEY_URL, url)
            return intent
        }

        fun getSuccessUrl(uri: Uri): Uri {
            return uri.buildUpon().appendQueryParameter(QUERY_PAGE, KEY_SUCCESS).build()
        }

        fun gotoSuccessPage(activity: FragmentActivity?, redirectionApplink: String) {
            activity?.run {
                try {
                    val baseUrl = getLinkAccountUrl(redirectionApplink)
                    if (baseUrl != null) {
                        val i = newInstance(this, getSuccessUrl(baseUrl).toString())
                        startActivityForResult(i, LINK_ACCOUNT_WEBVIEW_REQUEST)
                    }
                } catch (_: Exception) { }
            }
        }

        fun getLinkAccountUrl(redirectionApplink: String): Uri? {
            try {
                val uri = Uri.parse(getAccountLinkUrl())
                val clientID = TrackApp.getInstance().gtm.cachedClientIDString
                return uri.buildUpon()
                    .appendQueryParameter(QUERY_APP_CLIENT_ID, clientID)
                    .appendQueryParameter(QUERY_LD, redirectionApplink.encodeToUtf8())
                    .build()
            } catch (_: Exception) {}
            return null
        }
    }

    override fun getComponent(): PrivacyCenterComponent {
        return DaggerPrivacyCenterComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    fun setToolbarTitle(title: String) {
        updateTitle(title)
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
        if (item.itemId == R.id.menu_link_account_skip) {
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
        inflater.inflate(R.menu.menu_link_account_privacy_center, menu)

        val item = menu.findItem(R.id.menu_link_account_skip)
        val s = SpannableString(this.getString(R.string.account_linking_skip_for_now))
        s.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
            ),
            0, s.length, 0
        )
        item.title = s
        return true
    }

    override fun getNewFragment(): Fragment {
        var url = intent.getStringExtra(KEY_URL).orEmpty()
        val source = intent.getStringExtra(ApplinkConstInternalGlobal.PARAM_SOURCE).orEmpty()

        if (url.isEmpty()) {
            val redirection = intent.getStringExtra(ApplinkConstInternalGlobal.PARAM_LD) ?: ApplinkConst.HOME
            val uri = getLinkAccountUrl(redirection)
            url = uri.toString()
            if (source.isNotEmpty() && url.isNotEmpty()) {
                url = "$url&source=$source"
            }
        }
        return LinkAccountWebviewFragment.newInstance(url)
    }

    override fun onBackPressed() {
        (fragment as LinkAccountWebviewFragment).trackClickBackBtn()
        (fragment as LinkAccountWebviewFragment).checkPageFinished()
        super.onBackPressed()
    }
}
