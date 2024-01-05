package com.tokopedia.kyc_centralized.ui.gotoKyc.webview

import android.os.Bundle
import android.webkit.WebView
import com.gojek.OneKycSdk
import com.gojek.kyc.plus.OneKycConstants
import com.gojek.kyc.plus.getKycSdkDocumentDirectoryPath
import com.gojek.kyc.plus.getKycSdkFrameDirectoryPath
import com.gojek.kyc.plus.getKycSdkLogDirectoryPath
import com.gojek.kyc.web.OneKycWebWrapper
import com.tokopedia.kyc_centralized.di.GoToKycComponent
import com.tokopedia.kyc_centralized.ui.gotoKyc.utils.removeGotoKycImage
import com.tokopedia.kyc_centralized.ui.gotoKyc.utils.removeGotoKycPreference
import com.tokopedia.webview.BaseSessionWebViewFragment
import javax.inject.Inject

class GotoKycWebWrapperFragment : BaseSessionWebViewFragment() {

    @Inject
    lateinit var oneKycSdk: OneKycSdk

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        removeGotoKycCache()
    }

    override fun addJavascriptInterface(webView: WebView) {
        oneKycSdk.init()
        val wrapper = OneKycWebWrapper(oneKycSdk, requireActivity(), webView)
        webView.addJavascriptInterface(wrapper, OneKycWebWrapper.PLATFORM_CODE)
    }

    private fun removeGotoKycCache() {
        val preferenceName = OneKycConstants.KYC_SDK_PREFERENCE_NAME
        val preferenceKey = OneKycConstants.KYC_UPLOAD_PROGRESS_STATE
        removeGotoKycPreference(
            context = requireContext(),
            preferenceName = preferenceName,
            preferenceKey = preferenceKey
        )

        val documentDirectory = getKycSdkDocumentDirectoryPath(requireContext())
        val frameDirectory = getKycSdkFrameDirectoryPath(requireContext())
        val logDirectory = getKycSdkLogDirectoryPath(requireContext())
        removeGotoKycImage(documentDirectory)
        removeGotoKycImage(frameDirectory)
        removeGotoKycImage(logDirectory)
    }

    override fun initInjector() {
        getComponent(GoToKycComponent::class.java).inject(this)
    }

    companion object {
        private const val KEY_URL = "url"

        fun newInstance(url: String): GotoKycWebWrapperFragment {
            val fragment = GotoKycWebWrapperFragment()
            val args = Bundle()
            args.putString(KEY_URL, url)
            fragment.arguments = args
            return fragment
        }
    }
}
