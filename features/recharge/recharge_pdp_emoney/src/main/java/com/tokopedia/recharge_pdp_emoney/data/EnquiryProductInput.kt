package com.tokopedia.recharge_pdp_emoney.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EnquiryProductInput(
        @SerializedName("param_name")
        @Expose
        var paramName: String = "",

        @SerializedName("name")
        @Expose
        var name: String = "",


        )