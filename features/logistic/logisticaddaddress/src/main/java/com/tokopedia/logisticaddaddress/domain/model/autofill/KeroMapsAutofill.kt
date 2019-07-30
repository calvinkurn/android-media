package com.tokopedia.logisticaddaddress.domain.model.autofill

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class KeroMapsAutofill(

        @field:SerializedName("data")
        val data: Data = Data(),

        @field:SerializedName("status")
        val status: String = "",

        @field:SerializedName("message_error")
        val error: List<String> = emptyList()

)