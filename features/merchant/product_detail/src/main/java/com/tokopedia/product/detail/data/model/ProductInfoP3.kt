package com.tokopedia.product.detail.data.model

import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
import com.tokopedia.product.detail.data.model.checkouttype.CART_TYPE_DEFAULT
import com.tokopedia.product.detail.data.model.checkouttype.CART_TYPE_EXPRESS
import com.tokopedia.product.detail.data.model.checkouttype.CART_TYPE_OCS
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.SummaryText
import com.tokopedia.topads.sdk.domain.model.TopAdsModel

data class ProductInfoP3(
        var rateEstSummarizeText: SummaryText? = null,
        var userCod: Boolean = false
)