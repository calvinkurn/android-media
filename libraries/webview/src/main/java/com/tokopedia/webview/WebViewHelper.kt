package com.tokopedia.webview

import java.net.URI
import java.net.URISyntaxException

/**
 * Created by Ade Fulki on 2019-06-21.
 * ade.hadian@tokopedia.com
 */

object WebViewHelper{

    private const val PREFIX_PATTERN: String = "www."
    private const val SUFFIX_PATTERN: String = ".tokopedia.com"
    private const val DOMAIN_PATTERN: String = "tokopedia.com"

    @JvmStatic
    fun validateUrl(url: String): Boolean {
        val domain = getDomainName(url)
        return domain.endsWith(SUFFIX_PATTERN) || domain == DOMAIN_PATTERN
    }

    @Throws(URISyntaxException::class)
    private fun getDomainName(url: String): String {
        val domain = URI(url).host
        return if (domain.startsWith(PREFIX_PATTERN)) domain.substring(4) else domain
    }
}