package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserShopInfo(
	@Expose
	@SerializedName("data")
	val data: DataShop? = null,

	@Expose
	@SerializedName("info")
	val info: Info? = null
)

data class DataShop(
	@Expose
	@SerializedName("userShopInfo")
	val userShopInfo: UserShopInfo? = null
)

data class Info(
	@Expose
	@SerializedName("shop_id")
	val shopId: String? = null
)
