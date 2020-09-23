package com.tokopedia.loginregister.termprivacy.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager

/**
 * @author rival
 * @team @minion-kevin
 *
 * @created at 8/09/2020
 * */

class TermPrivacyActivity: BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when {
            intent?.data != null -> {
                intent?.data?.let {
                    if (it.getQueryParameter(PARAM).equals(MODE_TERM)) {
                        openTermPage()
                    } else if (it.getQueryParameter(PARAM).equals(MODE_PRIVACY)) {
                        openPrivacyPage()
                    }
                }
            }

            intent?.extras != null -> {
                intent?.extras?.let {
                    if (it.getString(PAGE_MODE).equals(MODE_TERM)) {
                        openTermPage()
                    } else if (it.getString(PAGE_MODE).equals(MODE_PRIVACY)) {
                        openPrivacyPage()
                    }
                }
            }

            else -> {
                finish()
            }
        }
    }

    private fun openTermPage() {
        RouteManager.route(this, String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_TERM))
        finish()
    }

    private fun openPrivacyPage() {
        RouteManager.route(this, String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_PRIVACY))
        finish()
    }

    companion object {
        private const val PARAM = "param"
        private const val PAGE_MODE = "pageMode"
        private const val MODE_TERM = "0"
        private const val MODE_PRIVACY = "1"

        private const val URL_TERM = "https://m.tokopedia.com/terms.pl?device=android&flag_app=1"
        private const val URL_PRIVACY = "https://m.tokopedia.com/privacy.pl?device=android&flag_app=1"

        fun createIntentTermAndCondition(context: Context): Intent {
            return createIntent(context, MODE_TERM)
        }

        fun createIntentPrivacy(context: Context): Intent {
            return createIntent(context, MODE_PRIVACY)
        }

        private fun createIntent(context: Context, mode: String): Intent {
            val intent = Intent(context, TermPrivacyActivity::class.java)
            intent.putExtra(PAGE_MODE, mode)
            return intent
        }
    }
}