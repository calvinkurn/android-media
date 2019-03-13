package com.tokopedia.product.detail.data.model

import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.product.detail.common.data.model.ProductOther
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.data.model.talk.Talk
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.SummaryText
import com.tokopedia.topads.sdk.domain.model.TopAdsModel

data class ProductInfoP3(
        var rateEstSummarizeText: SummaryText? = null,
        var isWishlisted: Boolean = false,
        var displayAds: TopAdsModel? = null,
        var pdpAffiliate: TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate? = null,
        var isExpressCheckoutType: Boolean = false,
        var userCod: Boolean = false
)