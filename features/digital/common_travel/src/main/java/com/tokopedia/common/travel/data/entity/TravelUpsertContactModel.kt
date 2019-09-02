package com.tokopedia.common.travel.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 2019-08-26
 */

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
            val title: String = "",

            @SerializedName("fullName")
            @Expose
            val fullName: String = "",

            @SerializedName("firstName")
            @Expose
            val firstName: String = "",

            @SerializedName("lastName")
            @Expose
            val lastName: String = "",

            @SerializedName("birthDate")
            @Expose
            val birthDate: String = "",

            @SerializedName("nationality")
            @Expose
            val nationality: String = "",

            @SerializedName("phoneCountryCode")
            @Expose
            val phoneCountryCode: Int = 0,

            @SerializedName("phoneNumber")
            @Expose
            val phoneNumber: String = "",

            @SerializedName("email")
            @Expose
            val email: String = "",

            @SerializedName("idList")
            @Expose
            val idList: List<TravelContactIdCard> = listOf()
    ) {

        override fun equals(other: Any?): Boolean {
            return if (other is Contact) {
                title.equals(other.title) && fullName.equals(other.fullName) && firstName.equals(other.firstName) &&
                        lastName.equals(other.lastName) && birthDate.equals(other.birthDate) && nationality.equals(other.nationality) &&
                        phoneNumber.equals(other.phoneNumber) && email.equals(other.email) && idList.equals(other.idList)
            } else if (other is TravelContactListModel.Contact) {
                title.equals(other.title) && fullName.equals(other.fullName) && firstName.equals(other.firstName) &&
                        lastName.equals(other.lastName) && birthDate.equals(other.birthDate) && nationality.equals(other.nationality) &&
                        phoneNumber.equals(other.phoneNumber) && email.equals(other.email) && idList.equals(other.idList)
            } else false
        }
    }

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