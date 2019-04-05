package com.tokopedia.product.report.model.reportSubmit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReportSubmit {

    @SerializedName("is_success")
    @Expose
    var isSuccess: Int = 0

    fun getIsSuccess(): Boolean {
        return isSuccess == 1
    }
}
