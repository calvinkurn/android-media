package com.tokopedia.sellerhomedrawer.view.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.tokopedia.sellerhomedrawer.R

class SellerExternalWebViewFragment: Fragment() {

    companion object {
        @JvmStatic
        val ARGS_URL = "arg_url"
        @JvmStatic
        fun newInstance(url: String): SellerExternalWebViewFragment {
            val fragment = SellerExternalWebViewFragment()
            val args = Bundle()
            args.putString(ARGS_URL, url)
            fragment.arguments = args
            return fragment
        }
    }

    private var url: String? = null
    private var webView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = arguments?.getString(ARGS_URL)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sh_fragment_external_webview, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        webView = view.findViewById(R.id.webview)
        webView?.loadUrl(url)
        webView?.settings?.javaScriptEnabled = true
    }
}