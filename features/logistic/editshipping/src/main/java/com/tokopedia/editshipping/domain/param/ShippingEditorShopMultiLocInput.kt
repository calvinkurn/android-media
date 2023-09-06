package com.tokopedia.editshipping.domain.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class ShippingEditorParam(
    @SerializedName("input")
    val input: ShippingEditorShopMultiLocInput = ShippingEditorShopMultiLocInput()
) : GqlParam

data class ShippingEditorShopMultiLocInput(
    @SerializedName("shop_id")
    var shopId: Long = 0L
) : GqlParam
