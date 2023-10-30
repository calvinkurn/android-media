package com.tokopedia.epharmacy.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EPharmacyUpdateCartResponse(
    @SerializedName("updateEpharmacyCart")
    @Expose
    val updateEPharmacyCart: UpdateEPharmacyCart?
) {
    data class UpdateEPharmacyCart(
        @SerializedName("header")
        @Expose
        val header: Header?,
        @SerializedName("status")
        @Expose
        val status: String?
    ) {
        data class Header(
            @SerializedName("error_code")
            @Expose
            val errorCode: Int?,
            @SerializedName("error_message")
            @Expose
            val errorMessage: List<String?>?,
            @SerializedName("process_time")
            @Expose
            val processTime: Int?
        )
    }
}
