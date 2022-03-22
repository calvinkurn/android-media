package com.tokopedia.vouchercreation.shop.create.view.uimodel.validation

import com.google.gson.annotations.SerializedName

class PeriodValidation (
        @SerializedName("date_start")
        val dateStartError: String = "",
        @SerializedName("date_end")
        val dateEndError: String = "",
        @SerializedName("hour_start")
        val hourStartError: String = "",
        @SerializedName("hour_end")
        val hourEndError: String = ""
) : Validation {

        fun getIsHaveError() =
                !(dateStartError.isBlank() && dateEndError.isBlank() && hourStartError.isBlank() && hourEndError.isBlank())

}