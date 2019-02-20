package com.tokopedia.product.detail.data.model

import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.product.detail.common.data.model.ProductOther
import com.tokopedia.product.detail.estimasiongkir.data.model.RatesEstimationModel
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.data.model.talk.ProductTalkQuery
import com.tokopedia.topads.sdk.domain.model.TopAdsModel

data class ProductInfoP3(
        var rateEstimation: RatesEstimationModel? = null,
        var isWishlisted: Boolean = false,
        var imageReviews: List<ImageReviewItem> = listOf(),
        var helpfulReviews: List<Review> = listOf(),
        var latestTalk: ProductTalkQuery = ProductTalkQuery(),
        var displayAds: TopAdsModel? = null,
        var productOthers: List<ProductOther> = listOf(),
        var pdpAffiliate: TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate? = null
)