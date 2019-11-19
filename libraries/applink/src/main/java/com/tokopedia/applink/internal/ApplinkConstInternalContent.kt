package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalContent {
    const val HOST_CONTENT = "content"
    const val INTERNAL_CONTENT = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_CONTENT"
    const val AFFILIATE_EDIT = "$INTERNAL_CONTENT/affiliate/{id}/edit"
    const val SHOP_POST_EDIT = "$INTERNAL_CONTENT/content-shop/{id}/edit"
    const val HASHTAG_PAGE = "$INTERNAL_CONTENT/explore-hashtag/{hashtag}/"
    const val SHOP_POST_PICKER = "$INTERNAL_CONTENT/content-shop/picker"

    const val HOST_AFFILIATE = "affiliate"
    const val INTERNAL_AFFILIATE = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_AFFILIATE"
    const val AFFILIATE_EXPLORE = "$INTERNAL_AFFILIATE/explore"
    const val AFFILIATE_DASHBOARD = "$INTERNAL_AFFILIATE/dashboard"
    const val AFFILIATE_EDUCATION = "$INTERNAL_AFFILIATE/education"
}