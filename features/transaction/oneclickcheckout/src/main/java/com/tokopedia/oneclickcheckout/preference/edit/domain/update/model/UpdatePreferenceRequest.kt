package com.tokopedia.oneclickcheckout.preference.edit.domain.update.model

import com.google.gson.annotations.SerializedName

data class UpdatePreferenceRequest (
        @SerializedName("profile_id")
        val profileId: Int,
        @SerializedName("address_id")
        val addressId: Int,
        @SerializedName("service_id")
        val serviceId: Int,
        @SerializedName("gateway_code")
        val gatewayCode: String,
        @SerializedName("metadata")
        val metadata: String,
        @SerializedName("is_default_profile_checked")
        val isDefaultProfileChecked: Boolean,
        @SerializedName("from_flow")
        val fromFlow: Int
)