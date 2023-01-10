package com.tokopedia.insurance

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.DialogFragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.insurance.databinding.BottomSheetInsuranceInfoBinding
import com.tokopedia.insurance.di.DaggerInsuranceInfoComponent
import com.tokopedia.insurance.di.InsuranceInfoComponent
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.network.utils.URLGenerator
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.ext.encodeOnce
import javax.inject.Inject

class InsuranceInfoBottomSheet(private val url: String) :
    BottomSheetUnify(),
    HasComponent<InsuranceInfoComponent> {

    @Inject
    lateinit var userSession: UserSessionInterface

    private var viewBinding: BottomSheetInsuranceInfoBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.InsuranceInfoBottomSheetStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = BottomSheetInsuranceInfoBinding.inflate(LayoutInflater.from(context))
        initializeBottomSheet()
        initializeWebView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDismiss(dialog: DialogInterface) {
        viewBinding = null
        activity?.finish()
        super.onDismiss(dialog)
    }

    private fun initializeBottomSheet() {
        isDragable = true
        isHideable = true
        showCloseIcon = false
        showHeader = false
        clearContentPadding = true
        showKnob = true
        customPeekHeight = (getScreenHeight() / 2).toDp()
        setChild(viewBinding?.root)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initializeWebView() {
        val webSettings = viewBinding?.webView?.settings
        webSettings?.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            builtInZoomControls = false
            displayZoomControls = true
        }
        viewBinding?.webView?.webViewClient = PurchaseProtectionInfoWebViewClient()
        webSettings?.mediaPlaybackRequiresUserGesture = false

        viewBinding?.webView?.loadAuthUrl(
            URLGenerator.generateURLSessionLogin(
                url.encodeOnce(),
                userSession.deviceId,
                userSession.userId
            ), userSession, false
        )
    }

    inner class PurchaseProtectionInfoWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            viewBinding?.progressBar?.gone()
        }
    }

    override fun getComponent(): InsuranceInfoComponent {
        return DaggerInsuranceInfoComponent
            .builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

}
