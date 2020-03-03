package com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-11-25.
 */

data class DisabledFeaturesDetail(
        @SerializedName("disabled_multi_address_message")
        val disabledMultiAddressMessage: String = ""
)