package com.tokopedia.buyerorder.detail.data.getcancellationreason

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BuyerGetCancellationReasonData(

	@SerializedName("data")
	@Expose
	val data: Data = Data()) {

	data class Data(
			@SerializedName("get_cancellation_reason")
			@Expose
			val getCancellationReason: GetCancellationReason = GetCancellationReason()) {

		data class GetCancellationReason(
				@SerializedName("reasons")
				@Expose
				val reasons: List<ReasonsItem> = emptyList(),

				@SerializedName("is_eligible_instant_cancel")
				@Expose
				val isEligibleInstantCancel: Boolean = false,

				@SerializedName("is_show_ticker")
				@Expose
				val isShowTicker: Boolean = false,

				@SerializedName("ticker_info")
				@Expose
				val tickerInfo: TickerInfo = TickerInfo(),

				@SerializedName("order_details")
				@Expose
				val orderDetails: List<OrderDetailsCancellation> = emptyList()) {

			data class TickerInfo (
					@SerializedName("text")
					@Expose
					val text: String = "",

					@SerializedName("type")
					@Expose
					val type: String = "",

					@SerializedName("action_text")
					@Expose
					val actionText: String = "",

					@SerializedName("action_url")
					@Expose
					val actionUrl: String = ""
			)

			data class ReasonsItem(
					@SerializedName("sub_reasons")
					@Expose
					val subReasons: List<SubReasonsItem> = emptyList(),

					@SerializedName("title")
					@Expose
					val title: String = "") {

				data class SubReasonsItem(
						@SerializedName("r_code")
						@Expose
						val rCode: Int = -1,

						@SerializedName("reason")
						@Expose
						val reason: String = ""
				)
			}

			data class OrderDetailsCancellation(
				@SerializedName("product_id")
				@Expose
				val productId: String = "",

				@SerializedName("product_name")
				@Expose
				val productName: String = "",

				@SerializedName("product_price")
				@Expose
				val productPrice: String = "",

				@SerializedName("picture")
				@Expose
				val picture: String = "",

				@SerializedName("bundle_id")
				@Expose
				val bundleId: String = "",

				@SerializedName("bundle_variant_id")
				@Expose
				val bundleVariantId: String = ""
			)
		}
	}
}