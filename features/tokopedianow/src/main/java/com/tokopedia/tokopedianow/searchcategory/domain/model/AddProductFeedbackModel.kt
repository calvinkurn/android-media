package com.tokopedia.tokopedianow.searchcategory.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.data.model.response.Header

data class AddProductFeedbackModel(
    @Expose
    @SerializedName("TokonowAddFeedback")
  val tokonowAddFeedback: TokonowAddFeedback? = TokonowAddFeedback()
){
    data class TokonowAddFeedback(
        @Expose
        @SerializedName("header")
        val header:Header? = null
    )
}
