package com.tokopedia.purchase_platform.features.cart.data.api

import com.tokopedia.purchase_platform.common.data.api.CommonPurchaseApiUrl

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

class CartApiUrl : CommonPurchaseApiUrl() {

    companion object {

        const val PATH_REMOVE_FROM_CART = "$BASE_PATH$VERSION/remove_product_cart"
        const val PATH_UPDATE_CART = "$BASE_PATH$VERSION/update_cart"

    }

}