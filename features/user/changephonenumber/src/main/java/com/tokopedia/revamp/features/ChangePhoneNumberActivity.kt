package com.tokopedia.revamp.features

import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.revamp.features.webview.ChangePhoneNumberWebViewActivity

class ChangePhoneNumberActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? = null

    // get url web view
    // direct into webview activity
    //

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(resultCode) {
            REQUEST_CODE_WEB_VIEW -> {
                onSuccess()
            }

            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun openPage(url: String) {
        val intent = ChangePhoneNumberWebViewActivity.createIntent(this, url, true)
        startActivityForResult(intent, REQUEST_CODE_WEB_VIEW)
    }

    private fun onSuccess() {

    }

    companion object {
        private const val REQUEST_CODE_WEB_VIEW = 100
    }
}