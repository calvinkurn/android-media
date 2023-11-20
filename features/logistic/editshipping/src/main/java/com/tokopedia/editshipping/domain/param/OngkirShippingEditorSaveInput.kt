package com.tokopedia.editshipping.domain.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class OngkirShippingEditorSaveInput(
    @SerializedName("shop_id")
    val shopId: Long = 0L,

    @SerializedName("activated_sp_id")
    val spIds: String = "",

    @SerializedName("feature_id")
    val featureId: String = ""
) : GqlParam

data class SaveShipperParam(
    @SerializedName("input")
    val input: OngkirShippingEditorSaveInput = OngkirShippingEditorSaveInput()
) : GqlParam
