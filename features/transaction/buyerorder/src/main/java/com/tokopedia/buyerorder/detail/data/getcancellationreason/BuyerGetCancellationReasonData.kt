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
				val tickerInfo: TickerInfo = TickerInfo()) {

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
		}
	}
}