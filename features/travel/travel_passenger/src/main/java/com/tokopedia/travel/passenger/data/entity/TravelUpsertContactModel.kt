package com.tokopedia.travel.passenger.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TravelUpsertContactModel(
        @SerializedName("updateLastUsedProduct")
        @Expose
        val updateLastUsedProduct: String = "",

        @SerializedName("contacts")
        @Expose
        val contacts: List<Contact> = listOf()
) {
    data class Contact(
            @SerializedName("title")
            @Expose
            var title: String = "",

            @SerializedName("fullName")
            @Expose
            var fullName: String = "",

            @SerializedName("firstName")
            @Expose
            var firstName: String = "",

            @SerializedName("lastName")
            @Expose
            var lastName: String = "",

            @SerializedName("birthDate")
            @Expose
            var birthDate: String = "",

            @SerializedName("nationality")
            @Expose
            var nationality: String = "",

            @SerializedName("phoneCountryCode")
            @Expose
            var phoneCountryCode: Int = 0,

            @SerializedName("phoneNumber")
            @Expose
            var phoneNumber: String = "",

            @SerializedName("email")
            @Expose
            var email: String = "",

            @SerializedName("idList")
            @Expose
            var idList: List<TravelContactIdCard> = listOf()
    )

    data class Response(
            @SerializedName("travelUpsertContact")
            @Expose
            val travelUpsertContact: SuccessResponse = SuccessResponse()
    ) {
        data class SuccessResponse(
                @SerializedName("Success")
                @Expose
                val successResponse: Boolean = false
        )
    }
}