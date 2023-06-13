package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.SerializedName

data class TokopointPaging(
    @SerializedName("hasNext")
    var isHasNext: Boolean = false
)
