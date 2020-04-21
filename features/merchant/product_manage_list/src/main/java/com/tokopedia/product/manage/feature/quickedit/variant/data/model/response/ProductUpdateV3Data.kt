package com.tokopedia.product.manage.feature.quickedit.variant.data.model.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.Header

data class ProductUpdateV3Data(
    @SerializedName("header")
    val header: Header?,
    @SerializedName("isSuccess")
    val isSuccess: Boolean
)