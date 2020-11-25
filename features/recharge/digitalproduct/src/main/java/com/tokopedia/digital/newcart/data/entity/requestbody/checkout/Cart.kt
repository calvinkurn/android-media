package com.tokopedia.digital.newcart.data.entity.requestbody.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Cart(@field:SerializedName("data")
           @field:Expose
           private var data: Data?) {

    fun setData(data: Data) {
        this.data = data
    }
}
