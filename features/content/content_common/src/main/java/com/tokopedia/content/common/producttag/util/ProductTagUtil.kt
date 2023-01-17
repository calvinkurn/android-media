package com.tokopedia.content.common.producttag.util

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalContent

/**
 * Created By : Jonathan Darwin on May 12, 2022
 */
fun getAutocompleteApplink(query: String, appLinkAfterAutocomplete: String): String =
    "${ApplinkConstInternalContent.INTERNAL_CONTENT_PRODUCT_TAG_AUTOCOMPLETE}?${buildAutoCompletePageParam(query, appLinkAfterAutocomplete)}"

private fun buildAutoCompletePageParam(query: String, appLinkAfterAutocomplete: String): String {
    return buildString {
        append("navsource=feed_product")
        append("&")
        append("srp_page_title=Tokopedia%20Feed")
        append("&")
        append("srp_page_id=0")
        append("&")
        append("baseSRPApplink=$appLinkAfterAutocomplete")

        if(query.isNotEmpty())
            append("&")
            append("q=$query")
    }
}