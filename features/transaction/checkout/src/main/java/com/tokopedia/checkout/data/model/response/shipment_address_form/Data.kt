package com.tokopedia.checkout.data.model.response.shipment_address_form

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-11-07.
 */

data class Data(
		@SerializedName("key")
		val key: String?,

		@SerializedName("value")
		val value: UserAddress?
)