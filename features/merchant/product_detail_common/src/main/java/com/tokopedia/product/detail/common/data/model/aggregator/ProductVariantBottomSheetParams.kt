package com.tokopedia.product.detail.common.data.model.aggregator

import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.DEFAULT_PRICE_MINIMUM_SHIPPING

/**
 * Created by Yehezkiel on 11/05/21
 */
data class ProductVariantBottomSheetParams(
        // general info
        var productId: String = "",
        var pageSource: String = "",
        var trackerCdListName: String = "",
        var isTokoNow: Boolean = false,
        var whId: String = "",
        var shopId: String = "",
        var dismissAfterTransaction: Boolean = false,
        var saveAfterClose: Boolean = true,

        /**
         * PDP only
         */
        var pdpSession: String = "",
        // only be used in AtcVariantViewModel, dont use this except from AtcVariantViewModel
        var variantAggregator: ProductVariantAggregatorUiData = ProductVariantAggregatorUiData(),
        var miniCartData: Map<String, MiniCartItem>? = null,
        var alreadyFollowShop: Boolean = false,

        //Basic info pdp
        var minimumShippingPrice: Double = DEFAULT_PRICE_MINIMUM_SHIPPING,
        var trackerAttribution: String = "",
        var trackerListNamePdp: String = "",
        var isShopOwner: Boolean = false,
        var cacheId: String = ""
)