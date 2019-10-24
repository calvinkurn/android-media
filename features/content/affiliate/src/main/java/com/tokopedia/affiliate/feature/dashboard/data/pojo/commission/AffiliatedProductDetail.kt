package com.tokopedia.affiliate.feature.dashboard.data.pojo.commission

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AffiliatedProductDetail(

	@SerializedName("commissionFormatted")
	@Expose
	val commissionFormatted: String = "",

	@SerializedName("totalClick")
	@Expose
	val totalClick: Int = 0,

	@SerializedName("totalCommissionFormatted")
	@Expose
	val totalCommissionFormatted: String = "",

	@SerializedName("productID")
	@Expose
	val productID: Int = 0,

	@SerializedName("priceFormatted")
	@Expose
	val priceFormatted: String = "",

	@SerializedName("productImg")
	@Expose
	val productImg: String = "",

	@SerializedName("shopName")
	@Expose
	val shopName: String = "",

	@SerializedName("isActive")
	@Expose
	val isActive: Boolean = false,

	@SerializedName("totalCommission")
	@Expose
	val totalCommission: Int = 0,

	@SerializedName("productName")
	@Expose
	val productName: String = "",

	@SerializedName("totalSold")
	@Expose
	val totalSold: Int = 0,

	@SerializedName("price")
	@Expose
	val price: Int = 0,

	@SerializedName("commission")
	@Expose
	val commission: Int = 0,

	@SerializedName("shopID")
	@Expose
	val shopID: Int = 0
)