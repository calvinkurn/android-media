package com.tokopedia.accountprofile.common.webview

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.tokopedia.accountprofile.di.ProfileCompletionSettingComponent
import com.tokopedia.accountprofile.settingprofile.profileinfo.tracker.ProfileInfoTracker
import com.tokopedia.webview.BaseWebViewFragment
import javax.inject.Inject


class ProfileSettingWebViewFragment : BaseWebViewFragment() {

    @Inject
    lateinit var tracker: ProfileInfoTracker

    override fun onFragmentBackPressed(): Boolean {
        if (Uri.parse(url).path?.contains(SUFFIX_CHANGE_EMAIL) == true) {
            tracker.trackClickOnBtnBackChangeEmail()
        }
        return super.onFragmentBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.activity?.let {
            getComponent(ProfileCompletionSettingComponent::class.java).inject(this)
        }
    }

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        if (isUrlAppLinkSuccessChangeEmail(url)) {
            onChangeEmailSuccess()
            return true
        }
        return super.shouldOverrideUrlLoading(webview, url)
    }

    private fun onChangeEmailSuccess() {
        activity?.apply {
            this.setResult(Activity.RESULT_OK)
            this.finish()
        }
    }

    private fun isUrlAppLinkSuccessChangeEmail(url: String): Boolean {
        return url.isNotEmpty() && url == APPLINK
    }

    companion object {
        private const val APPLINK = "tokopedia-android-internal://success-change-email"
        private const val SUFFIX_CHANGE_EMAIL = "/user/profile/email"

        fun instance(bundle: Bundle): Fragment {
            val fragment = ProfileSettingWebViewFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
