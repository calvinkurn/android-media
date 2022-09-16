package com.tokopedia.promocheckout.common.domain.model.deals

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DealsPromoCheckResponse(
        @SerializedName("event_validate_use_promo")
        @Expose
        val eventValidateUsePromo: EventValidateUsePromo = EventValidateUsePromo()
)

data class EventValidateUsePromo(
        @SerializedName("header")
        @Expose
        val header: Header = Header(),
        @SerializedName("data")
        @Expose
        val data: DataDeals = DataDeals(),
        @SerializedName("code")
        @Expose
        val code: String = ""
)

data class Header(
        @SerializedName("process_time")
        @Expose
        val process_time: Double = 0.0,
        @SerializedName("total_data")
        @Expose
        val total_data: Int = 0,
        @SerializedName("error_data")
        @Expose
        val error_data: ErrorDataDeals = ErrorDataDeals()
)

data class ErrorDataDeals(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("attributes")
        @Expose
        val attributes: Attributes = Attributes()
)

data class Attributes(
        @SerializedName("code")
        @Expose
        val code: String = "",
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("detail")
        @Expose
        val detail: String = "",
        @SerializedName("status")
        @Expose
        val status: String = "",
        @SerializedName("process_time")
        @Expose
        val process_time: Int = 0,
        @SerializedName("reason")
        @Expose
        val reason: String = ""
)

data class DataDeals(
        @SerializedName("global_success")
        @Expose
        val global_success: Boolean = false,
        @SerializedName("promo_code_id")
        @Expose
        val promo_code_id: String = "",
        @SerializedName("gateway_id")
        @Expose
        val gateway_id: String = "",
        @SerializedName("benefit_summary_info")
        @Expose
        val benefit_summary_info: BenefitSummaryInfoDeals = BenefitSummaryInfoDeals(),
        @SerializedName("usage_details")
        @Expose
        val usage_details: List<UsageDetails> = emptyList(),
        @SerializedName("additional_info")
        @Expose
        val additional_info: AdditionalInfo = AdditionalInfo()
)

data class BenefitSummaryInfoDeals(
        @SerializedName("final_benefit_text")
        @Expose
        val final_benefit_text: String = "",
        @SerializedName("final_benefit_amount_str")
        @Expose
        val final_benefit_amount_str: String = "",
        @SerializedName("final_benefit_amount")
        @Expose
        val final_benefit_amount: Long = 0L,
        @SerializedName("summaries")
        @Expose
        val summaries: List<Summaries> = emptyList()
)

data class Summaries(

        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("amount_str")
        @Expose
        val amount_str: String = "",
        @SerializedName("amount")
        @Expose
        val amount: Long = 0L,
        @SerializedName("details")
        @Expose
        val details: List<Details> = emptyList()
)

data class Details(
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("amount_str")
        @Expose
        val amount_str: String = "",
        @SerializedName("amount")
        @Expose
        val amount: Long = 0L,
        @SerializedName("points")
        @Expose
        val points: Int = 0
)

data class UsageDetails(
        @SerializedName("code")
        @Expose
        val code: String = "",
        @SerializedName("success")
        @Expose
        val success: Boolean = false,
        @SerializedName("message")
        @Expose
        val message: MessageDeals = MessageDeals(),
        @SerializedName("benefit_summary")
        @Expose
        val benefit_summary: List<BenefitSummary> = emptyList(),
        @SerializedName("promo_detail")
        @Expose
        val promo_detail: PromoDetail = PromoDetail()
)

data class MessageDeals(
        @SerializedName("error_code")
        @Expose
        val error_code: String = "",
        @SerializedName("text")
        @Expose
        val text: String = ""
)

data class BenefitSummary(
        @SerializedName("benefit_details")
        @Expose
        val benefit_details: List<BenefitDetails> = emptyList()
)

data class BenefitDetails(
        @SerializedName("amount_idr")
        @Expose
        val amount_idr: Long = 0L,
        @SerializedName("amount_points")
        @Expose
        val amount_points: Long = 0L,
        @SerializedName("data_type")
        @Expose
        val data_type: String = "",
        @SerializedName("benefit_type")
        @Expose
        val benefit_type: String = "",
        @SerializedName("currency_type")
        @Expose
        val currency_type: String = ""
)

data class PromoDetail(
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("is_coupon")
        @Expose
        val is_coupon: Boolean = false
)

data class AdditionalInfo(
        @SerializedName("message_info")
        @Expose
        val message_info: MessageInfo = MessageInfo(),
        @SerializedName("error_detail")
        @Expose
        val error_detail: ErrorDetail = ErrorDetail(),
        @SerializedName("usage_summaries")
        @Expose
        val usage_summaries: List<UsageSummaries> = emptyList()
)

data class MessageInfo(
        @SerializedName("message")
        @Expose
        val message: String = "",
        @SerializedName("detail")
        @Expose
        val detail: String = ""
)

data class ErrorDetail(
        @SerializedName("message")
        @Expose
        val message: String = ""
)

data class UsageSummaries(
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("amount_str")
        @Expose
        val amount_str: String = "",
        @SerializedName("amount")
        @Expose
        val amount: Long = 0,
        @SerializedName("currency_details_str")
        @Expose
        val currency_details_str: String = "",
        @SerializedName("currency_details")
        @Expose
        val currency_details: List<String> = emptyList()
)



