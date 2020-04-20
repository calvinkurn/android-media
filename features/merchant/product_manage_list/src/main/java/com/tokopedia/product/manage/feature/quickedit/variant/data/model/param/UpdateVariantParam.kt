package com.tokopedia.product.manage.feature.quickedit.variant.data.model.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.product.manage.feature.multiedit.data.param.ShopParam

class UpdateVariantParam(
    @SerializedName("shop")
    val shop: ShopParam,
    @SerializedName("productID")
    val productId: String,
    @SerializedName("variant")
    val variant: VariantInputParam
)