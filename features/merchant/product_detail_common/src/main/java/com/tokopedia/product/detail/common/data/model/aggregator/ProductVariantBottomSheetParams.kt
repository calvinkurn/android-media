package com.tokopedia.product.detail.common.data.model.aggregator

/**
 * Created by Yehezkiel on 11/05/21
 */
data class ProductVariantBottomSheetParams(
        // general info
        var productId: String = "",
        var pageSource: String = "",
        var productParentId: String = "",
        var isTokoNow: Boolean = false,
        var whId: String = "",

        /**
         * PDP only
         */
        var pdpSession: String = "",
        var variantAggregator: ProductVariantAggregatorUiData = ProductVariantAggregatorUiData(),

        //Basic info pdp
        var categoryName: String = "",
        var categoryId:String = "",
        var isTradein: Boolean = false,
        var isCheckImei: Boolean = false,
        var minimumShippingPrice: Int = 30000,
        var shopId: String = "",
        var trackerAttribution: String = "",
        var trackerListNamePdp: String = "",
        var shopTypeString: String = "",
        var shopName: String = "",
)