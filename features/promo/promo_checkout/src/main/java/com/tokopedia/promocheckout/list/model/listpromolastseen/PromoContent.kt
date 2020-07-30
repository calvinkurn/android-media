package com.tokopedia.promocheckout.list.model.listpromolastseen

import com.google.gson.annotations.SerializedName

data class PromoContent(

	@SerializedName("Status")
	val status: Int? = null,

	@SerializedName("Description")
	val description: String? = null,

	@SerializedName("IsBackdoor")
	val isBackdoor: Boolean? = null,

	@SerializedName("BaseCode")
	val baseCode: String? = null,

	@SerializedName("PromoTitle")
	val promoTitle: String? = null,

	@SerializedName("PromoType")
	val promoType: Int? = null,

	@SerializedName("ID")
	val iD: Int? = null,

	@SerializedName("Public")
	val public: Boolean? = null,

	@SerializedName("Name")
	val name: String? = null
)