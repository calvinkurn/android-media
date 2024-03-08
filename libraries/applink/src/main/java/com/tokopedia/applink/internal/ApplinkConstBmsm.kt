package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * Created by @ilhamsuaib on 05/12/23.
 */

object ApplinkConstBmsm {

    private const val HOST_BMSM = "buymoresavemore"
    private const val INTERNAL_BMSM = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_BMSM"

    const val BMGM_MINI_CART_DETAIL = "$INTERNAL_BMSM/mini-cart-detail"
    const val BMGM_MINI_CART_EDITOR = "$INTERNAL_BMSM/mini-cart-editor"
}