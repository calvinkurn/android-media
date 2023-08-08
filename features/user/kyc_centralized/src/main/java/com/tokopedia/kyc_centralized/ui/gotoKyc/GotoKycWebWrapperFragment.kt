package com.tokopedia.kyc_centralized.ui.gotoKyc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gojek.OneKycSdk
import com.gojek.kyc.web.OneKycWebWrapper
import com.tokopedia.kyc_centralized.di.GoToKycComponent
import com.tokopedia.webview.BaseSessionWebViewFragment
import javax.inject.Inject

class GotoKycWebWrapperFragment : BaseSessionWebViewFragment() {

    @Inject
    lateinit var oneKycSdk: OneKycSdk

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        addJavascriptInterfaceKyc()

        return view
    }

    private fun addJavascriptInterfaceKyc() {
        oneKycSdk.init()
        val wrapper = OneKycWebWrapper(oneKycSdk, requireActivity(), webView)
        webView.addJavascriptInterface(wrapper, OneKycWebWrapper.PLATFORM_CODE)
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
