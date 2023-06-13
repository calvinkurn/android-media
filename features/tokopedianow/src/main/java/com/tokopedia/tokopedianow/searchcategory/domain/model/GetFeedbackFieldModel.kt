package com.tokopedia.tokopedianow.searchcategory.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.data.model.response.Header


data class GetFeedbackFieldModel(
    @Expose
    @SerializedName("TokonowFeedbackFieldToggle")
    var tokonowFeedbackFieldToggle:TokonowFeedbackFieldToggle = TokonowFeedbackFieldToggle()
){
   data class TokonowFeedbackFieldToggle(
       @Expose
       @SerializedName("header")
       val header:Header?=null,
       @Expose
       @SerializedName("data")
       var data:Data = Data()
   )
   data class Data(
       @Expose
       @SerializedName("isActive")
       var isActive:Boolean = false
   )
}
