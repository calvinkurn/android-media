package com.tokopedia.product.detail.data.model.productinfo

import com.tokopedia.product.detail.common.data.model.product.YoutubeVideo
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailInfoContent

/**
 * Created by Yehezkiel on 20/10/20
 */
data class ProductInfoParcelData(
        val productId: String = "",
        val shopId: String = "",
        val productTitle: String = "",
        val productImageUrl: String = "",
        val variantGuideline: String = "",
        val discussionCount: Int = 0,
        val listOfYoutubeVideo: List<YoutubeVideo> = listOf(),
        val data: List<ProductDetailInfoContent> = listOf(),
        val forceRefresh: Boolean = false,
        val isTokoNow: Boolean = false
)