package com.tokopedia.common.travel.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 2019-08-26
 */

data class TravelContactListModel (
        @SerializedName("contacts")
        @Expose
        val contacts: List<Contact> = listOf()
) {

    data class Response(
            @SerializedName("travelGetContact")
            @Expose
            val response: TravelContactListModel = TravelContactListModel()
    )

    data class Contact(
            @SerializedName("uuid")
            @Expose
            val uuid: String = "",

            @SerializedName("type")
            @Expose
            val type: String = "",

            @SerializedName("titleID")
            @Expose
            val titleID: String = "",

            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("shortTitle")
            @Expose
            val shortTitle: String = "",

            @SerializedName("firstName")
            @Expose
            val firstName: String = "",

            @SerializedName("lastName")
            @Expose
            val lastName: String = "",

            @SerializedName("fullName")
            @Expose
            val fullName: String = "",

            @SerializedName("gender")
            @Expose
            val gender: String = "",

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
            override fun toString(): String {
                    return if (fullName.isNotBlank()) "$shortTitle $fullName"
                    else "$firstName $lastName"
            }

            override fun equals(other: Any?): Boolean {
                    return if (other is Contact) {
                            (this.uuid.equals(other.uuid)) && (this.type.equals(other.type)) &&
                                    (this.titleID.equals(other.titleID)) && (this.title.equals(other.title)) &&
                                    (this.shortTitle.equals(other.shortTitle)) && (this.firstName.equals(other.firstName)) &&
                                    (this.lastName.equals(other.lastName)) && (this.fullName.equals(other.fullName)) &&
                                    (this.gender.equals(other.gender)) && (this.birthDate.equals(other.birthDate)) &&
                                    (this.nationality.equals(other.nationality)) && (this.phoneNumber.equals(other.phoneNumber)) &&
                                    (this.email.equals(other.email)) && (this.idList.equals(other.idList))
                    } else false
            }
    }
}