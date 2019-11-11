package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalContent {
    const val HOST_CONTENT = "content"
    const val INTERNAL_CONTENT = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_CONTENT"
    const val AFFILIATE_EDIT = "$INTERNAL_CONTENT/affiliate/{id}/edit"
    const val SHOP_POST_EDIT = "$INTERNAL_CONTENT/content-shop/{id}/edit"
    const val HASHTAG_PAGE = "$INTERNAL_CONTENT/explore-hashtag/{hashtag}/"
    const val SHOP_POST_PICKER = "$INTERNAL_CONTENT/content-shop/picker"

    const val HOST_PROFILE = "people"
    const val INTERNAL_PROFILE = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_PROFILE"
    const val PROFILE = "$INTERNAL_PROFILE/{user_id}"
}