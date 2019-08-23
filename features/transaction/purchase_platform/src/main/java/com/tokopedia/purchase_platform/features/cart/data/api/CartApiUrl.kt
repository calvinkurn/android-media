package com.tokopedia.purchase_platform.features.cart.data.api

import com.tokopedia.purchase_platform.common.data.common.api.CommonPurchaseApiUrl

/**
 * Created by Irfan Khoirul on 2019-08-15.
 */

class CartApiUrl : CommonPurchaseApiUrl() {

    companion object {

        const val PATH_SHOP_GROUP_LIST = "$BASE_PATH$VERSION/shop_group"
        const val PATH_REMOVE_FROM_CART = "$BASE_PATH$VERSION/remove_product_cart"
        const val PATH_UPDATE_CART = "$BASE_PATH$VERSION/update_cart"

    }

}