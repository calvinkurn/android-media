package com.tokopedia.createpost.producttag.util

import com.tokopedia.applink.ApplinkConst

/**
 * Created By : Jonathan Darwin on May 12, 2022
 */
fun getAutocompleteApplink(query: String): String =
    "${ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE}?${buildAutoCompletePageParam(query)}"

private fun buildAutoCompletePageParam(query: String): String {
    return buildString {
        append("navsource=feed_product")
        append("&")
        append("srp_page_title=Tokopedia%20Feed")
        append("&")
        append("srp_page_id=0")

        if(query.isNotEmpty())
            append("&")
            append("q=$query")
    }
}