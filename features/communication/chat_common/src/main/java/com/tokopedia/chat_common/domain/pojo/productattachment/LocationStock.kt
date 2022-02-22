package com.tokopedia.chat_common.domain.pojo.productattachment

import com.google.gson.annotations.SerializedName

data class LocationStock(
    @SerializedName("district_name_full_text")
    val districtFullName: String = ""
)