package com.tokopedia.product.warehouse.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductActionSubmit {

    @SerializedName("is_success")
    @Expose
    var isSuccess: Int = 0

    fun getIsSuccess(): Boolean {
        return isSuccess == 1
    }
}
