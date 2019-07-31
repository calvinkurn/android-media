package com.tokopedia.logisticaddaddress.domain.model.add_address

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class KeroAddAddress(

	@field:SerializedName("data")
	val data: Data = Data(),

	@field:SerializedName("server_process_time")
	val serverProcessTime: String = "",

	@field:SerializedName("config")
	val config: String = "",

	@field:SerializedName("status")
	val status: String = ""
)