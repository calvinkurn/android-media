package com.tokopedia.travel.passenger.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 2019-08-26
 */

data class TravelDeleteContactModel(
        @SerializedName("uuid")
        @Expose
        val uuid: List<String> = listOf()
) {
    data class Response(
            @SerializedName("travelDeleteContact")
            @Expose
            val response: SuccessResponse = SuccessResponse()
    ) {
        data class SuccessResponse(
                @SerializedName("Success")
                @Expose
                val success: Boolean = false
        )
    }
}