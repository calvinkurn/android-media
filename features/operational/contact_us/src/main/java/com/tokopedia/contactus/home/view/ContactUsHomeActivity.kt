package com.tokopedia.contactus.home.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.tokopedia.url.TokopediaUrl.Companion.getInstance
import com.tokopedia.contactus.switcheractivity.inbox.InboxSwitcherActivity.Companion.start
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.contactus.createticket.ContactUsConstant
import com.tokopedia.webview.BaseSessionWebViewFragment
import com.tokopedia.webview.BaseWebViewFragment
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.contactus.R
import java.lang.Exception

/**
 * Created by sandeepgoyal on 02/04/18.
 */
class ContactUsHomeActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        val url = intent.getStringExtra(ContactUsConstant.EXTRAS_PARAM_URL)
        return if (url != null && url.length > 0) {
            BaseSessionWebViewFragment.newInstance(url)
        } else {
            BaseSessionWebViewFragment.newInstance(URL_HELP)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onBackPressed() {
        try {
            val webViewFragment = fragment as? BaseWebViewFragment
            if (webViewFragment != null && webViewFragment.getWebView().canGoBack()) {
                webViewFragment.getWebView().goBack()
            } else {
                super.onBackPressed()
            }
        } catch (e: Exception) {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        SplitCompat.installActivity(this)
        menuInflater.inflate(R.menu.contactus_menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_inbox) {
            start(this)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        val URL_HELP = getInstance().WEB + "help?utm_source=android"
    }
}
