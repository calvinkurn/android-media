package com.tokopedia.buyerorder.detail.data.getcancellationreason

import android.annotation.SuppressLint
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

				@SerializedName("have_product_bundle")
				@Expose
				val haveProductBundle: Boolean = false,

				@SerializedName("bundle_detail")
				@Expose
				val bundleDetail: BundleDetail? = BundleDetail()) {

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

			data class BundleDetail(
					@SerializedName("bundle")
					@Expose
					val bundleList: List<Bundle> = listOf(),

					@SerializedName("product_bundling_icon")
					@Expose
					val bundleIcon: String? = "",

					@SerializedName("non_bundle")
					@Expose
					val nonBundleList: List<Bundle.OrderDetail> = listOf()
			) {
				data class Bundle(
						@SuppressLint("Invalid Data Type")
						@SerializedName("bundle_id")
						@Expose
						val bundleId: Long = 0,

						@SerializedName("bundle_name")
						@Expose
						val bundleName: String = "",

						@SerializedName("bundle_price")
						@Expose
						val bundlePrice: Double = 0.0,

						@SerializedName("bundle_quantity")
						@Expose
						val bundleQty: Int = 0,

						@SerializedName("bundle_subtotal_price")
						@Expose
						val bundleSubtotalPrice: Double = 0.0,

						@SerializedName("order_detail")
						@Expose
						val orderDetailList: List<OrderDetail> = listOf()
				) {
					data class OrderDetail(
							@SuppressLint("Invalid Data Type")
							@SerializedName("bundle_id")
							@Expose
							val bundleId: Long = 0,

							@SuppressLint("Invalid Data Type")
							@SerializedName("order_id")
							@Expose
							val orderId: Long = 0,

							@SuppressLint("Invalid Data Type")
							@SerializedName("order_dtl_id")
							@Expose
							val orderDetailId: Long = 0,

							@SuppressLint("Invalid Data Type")
							@SerializedName("product_id")
							@Expose
							val productId: Long = 0,

							@SerializedName("product_name")
							@Expose
							val productName: String = "",

							@SerializedName("quantity")
							@Expose
							val quantity: Int = 0,

							@SerializedName("product_price")
							@Expose
							val productPrice: Double = 0.0,

							@SerializedName("subtotal_price")
							@Expose
							val subtotalPrice: Double = 0.0,

							@SerializedName("notes")
							@Expose
							val notes: String = "",

							@SerializedName("thumbnail")
							@Expose
							val thumbnail: String
					)
				}
			}
		}
	}
}