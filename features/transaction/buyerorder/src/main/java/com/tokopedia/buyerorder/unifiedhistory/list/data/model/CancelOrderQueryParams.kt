package com.tokopedia.buyerorder.unifiedhistory.list.data.model

import com.google.gson.annotations.SerializedName

data class CancelOrderQueryParams(

	@SerializedName("ICLogoTokoNow")
	val iCLogoTokoNow: String = "",

	@SerializedName("role")
	val role: String = "",

	@SerializedName("is_official_store")
	val isOfficialStore: String = "",

	@SerializedName("accept_deadline")
	val acceptDeadline: String = "",

	@SerializedName("shop_name")
	val shopName: String = "",

	@SerializedName("estimated_arrival_min_date")
	val estimatedArrivalMinDate: String = "",

	@SerializedName("product_snapshot_url")
	val productSnapshotUrl: String = "",

	@SerializedName("shop_badge_url")
	val shopBadgeUrl: String = "",

	@SerializedName("estimated_arrival_text")
	val estimatedArrivalText: String = "",

	@SerializedName("ICLogoPMPro")
	val iCLogoPMPro: String = "",

	@SerializedName("payment_status_id")
	val paymentStatusId: String = "",

	@SerializedName("shop_id")
	val shopId: String = "",

	@SerializedName("user_id")
	val userId: String = "",

	@SerializedName("ICLogoPM")
	val iCLogoPM: String = "",

	@SerializedName("shop_url")
	val shopUrl: String = "",

	@SerializedName("confirm_shipping_deadline")
	val confirmShippingDeadline: String = "",

	@SerializedName("shop_type")
	val shopType: String = "",

	@SerializedName("estimated_arrival_max_date")
	val estimatedArrivalMaxDate: String = "",

	@SerializedName("invoice")
	val invoice: String = "",

	@SerializedName("ICLogoOS")
	val iCLogoOS: String = "",

	@SerializedName("order_id")
	val orderId: String = "",

	@SerializedName("invoice_url")
	val invoiceUrl: String = "",

	@SerializedName("payment_gateway_id")
	val paymentGatewayId: String = "",

	@SerializedName("status")
	val status: String = ""
)
