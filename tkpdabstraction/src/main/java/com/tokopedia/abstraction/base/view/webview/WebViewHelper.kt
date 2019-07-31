package com.tokopedia.abstraction.base.view.webview

import android.net.Uri

/**
 * Created by Ade Fulki on 2019-06-21.
 * ade.hadian@tokopedia.com
 */

object WebViewHelper {

    private const val PREFIX_PATTERN: String = "www."
    private const val SUFFIX_PATTERN: String = ".tokopedia.com"
    private const val DOMAIN_PATTERN: String = "tokopedia.com"
    private const val JS_DOMAIN_PATTERN: String = "js.tokopedia.com"
    private const val KEY_PARAM_URL: String = "url"

    @JvmStatic
    fun isUrlValid(url: String): Boolean {
        return if(isSeamless(url)){
            val urlSeamless = getUrlSeamless(url)
            if(!urlSeamless.isNullOrEmpty()) isUrlValid(urlSeamless) else false
        }else{
            val domain = getDomainName(url)
            (domain.endsWith(SUFFIX_PATTERN) || domain == DOMAIN_PATTERN)
        }
    }

    private fun getDomainName(url: String): String {
        val domain = Uri.parse(url).host
        return if(domain != null)
            if (domain.startsWith(PREFIX_PATTERN)) domain.substring(4) else domain
        else ""
    }

    private fun isSeamless(url: String): Boolean = getDomainName(url) == JS_DOMAIN_PATTERN

    private fun getUrlSeamless(url: String): String?{
        val uri = Uri.parse(url)
        return uri.getQueryParameter(KEY_PARAM_URL)
    }
}