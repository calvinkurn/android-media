package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetIsOfficialResponse(
        @Expose
        @SerializedName("getIsOfficial")
        val getIsOfficial: GetIsOfficial = GetIsOfficial()
) {
    data class GetIsOfficial(
            @Expose
            @SerializedName("data")
            val `data`: Data = Data(),
            @Expose
            @SerializedName("message_error")
            val messageError: String = ""
    ) {
        data class Data(
                @Expose
                @SerializedName("is_official")
                val isOfficial: Boolean = false
        )
    }
}