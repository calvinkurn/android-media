package com.tokopedia.product.detail.data.model

import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
import com.tokopedia.product.detail.data.model.checkouttype.CART_TYPE_DEFAULT
import com.tokopedia.product.detail.data.model.checkouttype.CART_TYPE_EXPRESS
import com.tokopedia.product.detail.data.model.checkouttype.CART_TYPE_OCS

data class ProductInfoP2Login (
        var isWishlisted: Boolean = false,
        var pdpAffiliate: TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate? = null,
        var cartType: String = CART_TYPE_DEFAULT
        ){
    val isExpressCheckoutType: Boolean
        get() {
            return cartType.equals(CART_TYPE_EXPRESS, true)
        }

    val isOcsCheckoutType: Boolean
        get() {
            return cartType.equals(CART_TYPE_OCS, true)
        }
}