package com.tokopedia.abstraction.base.view.webview

import java.util.regex.Pattern

/**
 * Created by Ade Fulki on 2019-06-21.
 * ade.hadian@tokopedia.com
 */

object WebViewHelper{

    private val PATTERN: String = "^((http|https)://(.+[.]|)tokopedia[.]com/).*"

    @JvmStatic
    fun validateUrl(url: String): Boolean = Pattern.matches(PATTERN, url)
}