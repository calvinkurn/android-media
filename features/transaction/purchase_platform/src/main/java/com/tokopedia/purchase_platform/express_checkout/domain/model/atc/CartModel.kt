package com.tokopedia.purchase_platform.express_checkout.domain.model.atc;

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class CartModel(
        var errors: ArrayList<String>? = null,
        var groupShopModels: ArrayList<GroupShopModel>? = null
)