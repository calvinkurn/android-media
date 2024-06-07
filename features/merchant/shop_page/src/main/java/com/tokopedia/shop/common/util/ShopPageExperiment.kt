package com.tokopedia.shop.common.util

import com.tokopedia.productcard.experiments.ProductCardExperiment
import com.tokopedia.shop.common.constant.ShopParamApiConstant

object ShopPageExperiment {
    fun isProductCardV5ExperimentActive(): Boolean {
        return ProductCardExperiment.isReimagine()
    }

    /**
     * When product card v5 rollence activate, pass "usecase": "ace_get_shop_product_v2" to GetShopProduct gql query
     * When product card v5 rollence inactive, pass "usecase": ""  to GetShopProduct gql query
     */
    fun determineProductCardUseCaseParam(): String {
        return if (isProductCardV5ExperimentActive()) {
            ShopParamApiConstant.SHOP_GET_PRODUCT_V2
        } else {
            ""
        }
    }
}
