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
				@SerializedName("cancellation_notes")
				@Expose
				val cancellationNotes: String = "",

				@SerializedName("reasons")
				@Expose
				val reasons: List<ReasonsItem> = emptyList(),

				@SerializedName("cancellation_min_time")
				@Expose
				val cancellationMinTime: String = "",

				@SerializedName("is_requested_cancel")
				@Expose
				val isRequestedCancel: Boolean = false,

				@SerializedName("is_requested_cancel_available")
				@Expose
				val isRequestedCancelAvailable: Boolean = false,

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
				val orderDetails: List<OrderDetailsCancellation> = emptyList(),

				@SerializedName("bundle_detail")
				@Expose
				val bundleDetail: BundleDetailCancellation = BundleDetailCancellation(),

				@SerializedName("have_product_bundle")
				@Expose
				val isHaveProductBundle: Boolean = false) {

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

					@SerializedName("action_key")
					@Expose
					val actionKey: String = "",

					@SerializedName("action_url")
					@Expose
					val actionUrl: String = ""
			)

			data class ReasonsItem(
					@SerializedName("question")
					@Expose
					val question: String = "",

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

			data class BundleDetailCancellation(
				@SerializedName("total_product")
				@Expose
				val totalProduct: Int = 0,

				@SerializedName("product_bundling_icon")
				@Expose
				val productBundlingIcon: String = "",

				@SerializedName("bundle")
				@Expose
				val bundle: List<BundleDataCancellation> = emptyList(),

				@SerializedName("non_bundle")
				@Expose
				val nonBundle: List<OrderDetailsCancellation> = emptyList()) {

				data class BundleDataCancellation(
					@SerializedName("bundle_id")
					@Expose
					val bundleId: String = "",

					@SerializedName("bundle_variant_id")
					@Expose
					val bundleVariantId: String = "",

					@SerializedName("bundle_name")
					@Expose
					val bundleName: String = "",

					@SerializedName("bundle_price")
					@Expose
					val bundlePrice: Float = 0F,

					@SerializedName("bundle_quantity")
					@Expose
					val bundleQty: Int = 0,

					@SerializedName("bundle_subtotal_price")
					@Expose
					val bundleSubtotalPrice: Float = 0F,

					@SerializedName("order_detail")
					@Expose
					val orderDetails: OrderDetailsCancellation = OrderDetailsCancellation()
				)
			}
		}
	}
}