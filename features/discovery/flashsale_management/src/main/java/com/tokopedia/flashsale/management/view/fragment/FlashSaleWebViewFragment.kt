package com.tokopedia.flashsale.management.view.fragment

import android.os.Bundle

import com.tokopedia.abstraction.base.view.fragment.BaseWebViewFragment

class FlashSaleWebViewFragment : BaseWebViewFragment() {
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it.getString(ARGS_URL)
        }
    }

    override fun getUrl(): String? = url

    override fun getUserIdForHeader(): String? = null

    override fun getAccessToken(): String? = null

    companion object {
        val ARGS_URL = "arg_url"

        fun newInstance(url: String) =  FlashSaleWebViewFragment().apply {
            arguments = Bundle().apply {
                putString(ARGS_URL, url)
            }
        }
    }
}
