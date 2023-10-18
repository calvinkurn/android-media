package com.tokopedia.contactus.home.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.tokopedia.url.TokopediaUrl.Companion.getInstance
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.webview.BaseSessionWebViewFragment
import com.tokopedia.webview.BaseWebViewFragment
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.contactus.R
import com.tokopedia.contactus.home.ContactUsConstant
import com.tokopedia.contactus.inboxtickets.view.inbox.InboxContactUsActivity
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.contactus.inboxtickets.view.inbox.InboxContactUsFragment.Companion.FLAG_FROM_TOKOPEDIA_HELP
import java.lang.Exception

/**
 * Created by sandeepgoyal on 02/04/18.
 */
class ContactUsHomeActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        val url = intent.getStringExtra(ContactUsConstant.EXTRAS_PARAM_URL)
        return if (url != null && url.isNotEmpty()) {
            BaseSessionWebViewFragment.newInstance(url)
        } else {
            BaseSessionWebViewFragment.newInstance(URL_HELP)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initOnBackPressCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        SplitCompat.installActivity(this)
        menuInflater.inflate(R.menu.contactus_menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_inbox) {
            routeToInboxPage()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun routeToInboxPage(){
        if(intent.getBooleanExtra(FLAG_FROM_TOKOPEDIA_HELP, false).orFalse()){
            finish()
        } else {
            InboxContactUsActivity.start(this)
        }
    }
    
    private fun initOnBackPressCallback() {
        onBackPressedDispatcher.addCallback(this, getBackPressCallback())
    }
    private fun getBackPressCallback() : OnBackPressedCallback {
        return  object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                try {
                    val webViewFragment = fragment as? BaseWebViewFragment
                    if (webViewFragment != null && webViewFragment.getWebView().canGoBack()) {
                        webViewFragment.getWebView().goBack()
                    } else {
                        finish()
                    }
                } catch (e: Exception) {
                    FirebaseCrashlytics.getInstance().recordException(e)
                    finish()
                }
            }
        }
    }

    companion object {
        val URL_HELP = getInstance().WEB + "help?utm_source=android"
        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, ContactUsHomeActivity::class.java)
            intent.putExtra(FLAG_FROM_TOKOPEDIA_HELP, true)
            context.startActivity(intent)
        }
    }
}
