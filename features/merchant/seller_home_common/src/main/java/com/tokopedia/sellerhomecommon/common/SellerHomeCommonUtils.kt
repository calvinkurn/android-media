package com.tokopedia.sellerhomecommon.common

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created By @ilhamsuaib on 02/09/20
 */

object SellerHomeCommonUtils {

    /**
     * Returns list of links contained in given text
     */
    fun extractUrls(text: String): List<String> {
        val containedUrls: MutableList<String> = ArrayList()
        val urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)"
        val pattern: Pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE)
        val urlMatcher: Matcher = pattern.matcher(text)
        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0), urlMatcher.end(0)))
        }
        return containedUrls
    }
}