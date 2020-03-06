package com.tokopedia.purchase_platform.features.one_click_checkout.order.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.common.data.model.variant.Child
import com.tokopedia.product.detail.common.data.model.variant.Variant

data class ProductVariantResponse(
        @SerializedName("parentID")
        @Expose
        var parentId: Int = 0,

        @SerializedName("defaultChild")
        @Expose
        var defaultChild: Int = 0,

        @SerializedName("variant")
        @Expose
        var variant: List<Variant> = listOf(),

        @SerializedName("children")
        @Expose
        var children: List<Child> = listOf()
)