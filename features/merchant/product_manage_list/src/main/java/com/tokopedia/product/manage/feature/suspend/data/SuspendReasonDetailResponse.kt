package com.tokopedia.product.manage.feature.suspend.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SuspendReasonDetailResponse(

	@SerializedName("CheckProductViolation")
	@Expose
	val checkProductViolation: CheckProductViolation = CheckProductViolation()
)

data class DataItem(

	@SerializedName("productID")
	@Expose
	val productID: String = "",

	@SerializedName("infoImpact")
	@Expose
	val infoImpact: List<String> = listOf(),

	@SerializedName("infoToPrevent")
	@Expose
	val infoToPrevent: List<String> = listOf(),

	@SerializedName("infoReason")
	@Expose
	val infoReason: List<String> = listOf(),

	@SerializedName("infoToResolve")
	@Expose
	val infoToResolve: List<String> = listOf(),

	@SerializedName("infoFootNote")
	@Expose
	val infoFootNote: List<String> = listOf(),

	@SerializedName("urlHelpCenter")
	@Expose
	val urlHelpCenter: String = ""
)

data class CheckProductViolation(

	@SerializedName("data")
	@Expose
	val data: List<DataItem> = listOf()
)
