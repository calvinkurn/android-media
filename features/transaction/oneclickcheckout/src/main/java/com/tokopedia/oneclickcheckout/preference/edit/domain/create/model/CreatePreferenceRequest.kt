package com.tokopedia.oneclickcheckout.preference.edit.domain.create.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.localizationchooseaddress.request.ChosenAddress

data class CreatePreferenceRequest(
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
        val fromFlow: Int, // OSP = 1, profile setting = 0
        @SerializedName("chosen_address")
        val chosenAddress: ChosenAddress?
)