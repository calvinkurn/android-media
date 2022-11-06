package com.tokopedia.tokopedianow.searchcategory.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.data.model.response.Header


data class GetFeedbackFieldModel(
    @Expose
    @SerializedName("TokonowFeedbackFieldToggle")
    val tokonowFeedbackFieldToggle:TokonowFeedbackFieldToggle = TokonowFeedbackFieldToggle()
){
   data class TokonowFeedbackFieldToggle(
       @Expose
       @SerializedName("header")
       val header:Header?=null,
       @Expose
       @SerializedName("data")
       val data:Data = Data()
   )
   data class Data(
       @Expose
       @SerializedName("isActive")
       val isActive:Boolean = false
   )
}
