package com.tokopedia.product.manage.feature.quickedit.variant.data.model.param

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.manage.feature.multiedit.data.param.ShopParam

data class UpdateVariantParam(
    @Expose
    @SerializedName("shop")
    val shop: ShopParam,
    @Expose
    @SerializedName("productID")
    val productId: String,
    @Expose
    @SerializedName("variant")
    val variant: VariantInputParam
)