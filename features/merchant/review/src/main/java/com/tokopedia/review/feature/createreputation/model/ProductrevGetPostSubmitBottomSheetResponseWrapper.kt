package com.tokopedia.review.feature.createreputation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetPostSubmitBottomSheetResponseWrapper(
    @SerializedName("productrevGetPostSubmitBottomSheet")
    @Expose
    val productrevGetPostSubmitBottomSheetResponse: ProductrevGetPostSubmitBottomSheetResponse? = null
)