package com.tokopedia.product.detail.data.model.productinfo

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.product.detail.common.data.model.product.YoutubeVideo
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoDataModel

/**
 * Created by Yehezkiel on 20/10/20
 */
data class ProductInfoParcelData(
    val productId: String = String.EMPTY,
    val shopId: String = String.EMPTY,
    val productTitle: String = String.EMPTY,
    val productImageUrl: String = String.EMPTY,
    val variantGuideline: String = String.EMPTY,
    val discussionCount: Int = Int.ZERO,
    val listOfYoutubeVideo: List<YoutubeVideo> = listOf(),
    val productInfo: ProductDetailInfoDataModel = ProductDetailInfoDataModel(),
    val forceRefresh: Boolean = false,
    val isTokoNow: Boolean = false,
    val isGiftable: Boolean = false,
    val parentId: String = String.EMPTY,
    val catalogId: String = String.EMPTY,
    val isOpenSpecification: Boolean = false
) {

    val isOpenCatalogDescription
        get() = !isOpenSpecification && productInfo.isCatalog

    val bottomSheetParam = if (isOpenSpecification) {
        productInfo.catalogBottomSheet?.param.orEmpty()
    } else {
        productInfo.bottomSheet.param
    }
}