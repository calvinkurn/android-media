package com.tokopedia.logisticCommon.data.request

import com.google.gson.annotations.SerializedName

data class AddAddressParam(
        @SerializedName("addr_name")
        val addrName: String,
        @SerializedName("receiver_name")
        val receiverName: String,
        @SerializedName("address_1")
        val address1: String,
        @SerializedName("address_1_notes")
        val address1Notes: String,
        @SerializedName("address_2")
        var address2: String = "",
        @SerializedName("postal_code")
        val postalCode: String,
        @SerializedName("phone")
        val phone: String,
        @SerializedName("province")
        val province: String,
        @SerializedName("city")
        val city: String,
        @SerializedName("district")
        val district: String,
        @SerializedName("latitude")
        val latitude: String,
        @SerializedName("longitude")
        val longitude: String,
        @SerializedName("is_ana_positive")
        var isAnaPositive: String,
        @SerializedName("checksum")
        var checksum: String = "",
        @SerializedName("feature")
        var feature: String? = null,
        @SerializedName("set_as_primary_address")
        var setAsPrimaryAddress: Boolean = false,
        @SerializedName("apply_name_as_new_user_fullname")
        var applyNameAsNewUserFullname: Boolean = false,
        @SerializedName("source")
        var source: String = "",
        @SerializedName("is_tokonow_request")
        var isTokonowRequest: Boolean = false
)
