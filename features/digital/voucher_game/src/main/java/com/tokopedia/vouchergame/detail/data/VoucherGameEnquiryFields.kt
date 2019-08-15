package com.tokopedia.vouchergame.detail.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by resakemal on 12/08/19.
 */
class VoucherGameEnquiryFields(

        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("param_name")
        @Expose
        val paramName: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("validations")
        @Expose
        val validations: List<Validation> = listOf()

) {
        class Validation(
                @SerializedName("id")
                @Expose
                val id: Int = 0
        )
}