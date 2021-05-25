package com.tokopedia.oneclickcheckout.preference.edit.domain.create.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.localizationchooseaddress.util.ChosenAddress

data class CreatePreferenceRequest(
        @SuppressLint("Invalid Data Type")
        @SerializedName("address_id")
        val addressId: Long,
        @SuppressLint("Invalid Data Type")
        @SerializedName("service_id")
        val serviceId: Int,
        @SerializedName("gateway_code")
        val gatewayCode: String,
        @SerializedName("metadata")
        val metadata: String,
        @SerializedName("is_default_profile_checked")
        val isDefaultProfileChecked: Boolean,
        @SerializedName("from_flow")
        val fromFlow: Int, // OSP = 1, profile setting = 0
        @SerializedName("chosen_address")
        val chosenAddress: ChosenAddress?
)