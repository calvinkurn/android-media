package com.tokopedia.logisticaddaddress.domain.model.add_address

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Data(

	@field:SerializedName("is_success")
	val isSuccess: Int = 0,

	@field:SerializedName("addr_id")
	val addrId: Int = 0,

	@field:SerializedName("is_state_chosen_address_change")
	val isStateChosenAddressChange: Boolean = true,

	@field:SerializedName("chosen_address")
	val chosenAddress: ChosenAddressAddResponse = ChosenAddressAddResponse(),

	@field:SerializedName("tokonow")
	val tokonow: TokonowAddAddress = TokonowAddAddress()
)