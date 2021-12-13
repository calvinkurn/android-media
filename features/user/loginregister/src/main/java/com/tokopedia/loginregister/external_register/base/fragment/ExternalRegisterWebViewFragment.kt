package com.tokopedia.loginregister.external_register.base.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.databinding.FragmentBaseWebViewBinding
import com.tokopedia.loginregister.external_register.base.constant.ExternalRegisterConstants
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by Yoris Prayogo on 25/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class ExternalRegisterWebViewFragment: BaseDaggerFragment() {

    override fun getScreenName(): String = ""

    override fun initInjector() {}

    private var mUrl = ""

    private val binding: FragmentBaseWebViewBinding? by viewBinding()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_base_web_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mUrl = arguments?.getString(ExternalRegisterConstants.PARAM.URL, "") ?: ""
        setupWebview(mUrl)
    }

    fun setupWebview(url: String){
        binding?.baseWebView?.run {
            webViewClient = object: WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    val response = if(url?.contains("authCode") == true) url else ""
                    if(url?.startsWith(ApplinkConstInternalGlobal.OVO_REG_INIT) == true && response.isNotEmpty()){
                        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.OVO_REG_INIT)
                        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MESSAGE_BODY, response)
                        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
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