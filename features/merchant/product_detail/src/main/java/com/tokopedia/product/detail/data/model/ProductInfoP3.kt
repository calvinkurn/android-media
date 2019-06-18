package com.tokopedia.product.detail.data.model

import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
import com.tokopedia.product.detail.data.model.checkouttype.CART_TYPE_DEFAULT
import com.tokopedia.product.detail.data.model.checkouttype.CART_TYPE_EXPRESS
import com.tokopedia.product.detail.data.model.checkouttype.CART_TYPE_OCS
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.SummaryText
import com.tokopedia.topads.sdk.domain.model.TopAdsModel

data class ProductInfoP3(
        var rateEstSummarizeText: SummaryText? = null,
        var isWishlisted: Boolean = false,
        var displayAds: TopAdsModel? = null,
        var pdpAffiliate: TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate? = null,
        var cartType: String = CART_TYPE_DEFAULT,
        var userCod: Boolean = false
) {

    val isExpressCheckoutType: Boolean
        get() {
            return cartType.equals(CART_TYPE_EXPRESS, true)
        }

    val isOcsCheckoutType: Boolean
        get() {
            return cartType.equals(CART_TYPE_OCS, true)
        }
}