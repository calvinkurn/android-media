package com.tokopedia.loginregister.external_register.base.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.external_register.base.constant.ExternalRegisterConstants
import kotlinx.android.synthetic.main.fragment_base_web_view.*

/**
 * Created by Yoris Prayogo on 25/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class ExternalRegisterWebViewFragment: BaseDaggerFragment() {

    override fun getScreenName(): String = ""

    override fun initInjector() {}

    private var mUrl = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_base_web_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mUrl = arguments?.getString(ExternalRegisterConstants.PARAM.URL, "") ?: ""
        setupWebview(mUrl)
    }

    fun setupWebview(url: String){
        base_web_view?.run {
            webViewClient = object: WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if(url?.startsWith(ApplinkConst.OVO_REGISTER_INIT) == true){
                        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.OVO_REG_INIT)
                        startActivity(intent)
                        activity?.finish()
                    }
                    return super.shouldOverrideUrlLoading(view, url)
                }
            }
            settings.javaScriptEnabled = true
            loadUrl(url)
        }
    }

    companion object {
        fun createInstance(data: Bundle?): Fragment {
            return ExternalRegisterWebViewFragment().apply {
                arguments = data
            }
        }
    }
}