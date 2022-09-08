package com.tokopedia.createpost.producttag.util

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalContent

/**
 * Created By : Jonathan Darwin on May 12, 2022
 */
fun getAutocompleteApplink(query: String): String =
    "${ApplinkConstInternalContent.INTERNAL_FEED_AUTOCOMPLETE}?${buildAutoCompletePageParam(query)}"

private fun buildAutoCompletePageParam(query: String): String {
    return buildString {
        append("navsource=feed_product")
        append("&")
        append("srp_page_title=Tokopedia%20Feed")
        append("&")
        append("srp_page_id=0")
        append("&")
        append("baseSRPApplink=${ApplinkConst.FEED_CREATION_PRODUCT_SEARCH}")

        if(query.isNotEmpty())
            append("&")
            append("q=$query")
    }
}