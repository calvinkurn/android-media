package com.tokopedia.editshipping.domain.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class OngkirShippingEditorPopupInput(
    @SerializedName("shop_id")
    val shopId: Long = 0L,

    @SerializedName("activated_sp_id")
    val spIds: String = ""
) : GqlParam

data class ValidateShippingEditorParam(
    @SerializedName("input")
    val input: OngkirShippingEditorPopupInput = OngkirShippingEditorPopupInput()
) : GqlParam
