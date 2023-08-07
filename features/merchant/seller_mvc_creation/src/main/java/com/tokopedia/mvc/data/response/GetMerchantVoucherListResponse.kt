package com.tokopedia.mvc.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetMerchantVoucherListResponse(
    @SerializedName("MerchantPromotionGetMVList", alternate = ["MerchantPromotionGetChildMVList"])
    val result: MerchantVoucherDataModel = MerchantVoucherDataModel()
)

data class MerchantVoucherDataModel(
    @SerializedName("data")
    val data: MerchantVoucherListDataModel = MerchantVoucherListDataModel()
)

data class MerchantVoucherListDataModel(
    @SerializedName("vouchers")
    val vouchers: List<MerchantVoucherModel> = emptyList()
)

data class MerchantVoucherModel(
    @SerializedName("confirmed_global_quota")
    val confirmedQuota: Int = 0,
    @SerializedName("parent_voucher_id")
    val parentVoucherId: String = "",
    @SerializedName("booked_global_quota")
    val bookedQuota: Int = 0,
    @SerializedName("create_time")
    val createTime: String = "",
    @SerializedName("is_public")
    val isPublic: Int = 0,
    @SerializedName("remaining_quota")
    val remainingQuota: Int = 0,
    @SerializedName("update_time")
    val updateTime: String = "",
    @SerializedName("voucher_code")
    val voucherCode: String = "",
    @SerializedName("voucher_discount_amt")
    val discountAmt: Int = 0,
    @SerializedName("voucher_discount_amt_formatted")
    val discountAmtFormatted: String = "",
    @SerializedName("voucher_discount_amt_max")
    val discountAmtMax: Int = 0,
    @SerializedName("voucher_discount_type_formatted")
    val discountTypeFormatted: String = "",
    @SerializedName("voucher_discount_type")
    val discountType: Int = 0,
    @SerializedName("voucher_finish_time")
    val finishTime: String = "",
    @SerializedName("voucher_id")
    val voucherId: String = "",
    @SerializedName("voucher_image")
    val voucherImage: String = "",
    @SerializedName("voucher_image_square")
    val imageSquare: String = "",
    @SerializedName("voucher_image_portrait")
    val imagePortrait: String = "",
    @SerializedName("voucher_minimum_amt")
    val voucherMinimumAmt: Int = 0,
    @SerializedName("voucher_name")
    val voucherName: String = "",
    @SerializedName("voucher_quota")
    val voucherQuota: Int = 0,
    @SerializedName("voucher_start_time")
    val startTime: String = "",
    @SerializedName("voucher_status")
    val voucherStatus: Int = 0,
    @SerializedName("voucher_type")
    val voucherType: Int = 0,
    @SerializedName("voucher_type_formatted")
    val voucherTypeFormatted: String = "",
    @SerializedName("is_vps")
    val isVps: Int = 0,
    @SerializedName("package_name")
    val packageName: String = "",
    @SerializedName("is_subsidy")
    val isSubsidy: Int = 0,
    @SerializedName("tnc")
    val tnc: String = "",
    @SerializedName("is_lock_to_product")
    val isLockToProduct: Int = 0,
    @SerializedName("total_child")
    val totalChild: Int = 0,
    @SerializedName("target_buyer")
    val targetBuyer: Int = 0,
    @SerializedName("product_ids")
    val productIds: List<ProductId> = listOf(),
    @SerializedName("is_parent")
    val isParent: Boolean = false,
    @SerializedName("label_voucher")
    val labelVoucher: LabelVoucher = LabelVoucher(),
    @SerializedName("is_editable")
    val isEditable: Boolean = false,
    @SerializedName("subsidy_detail")
    val subsidyDetail: SubsidyDetail = SubsidyDetail()
) {
    data class ProductId(
        @SerializedName("parent_product_id")
        val parentProductId: Long = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("child_product_id")
        val childProductId: List<Long>? = listOf()
    )

    data class LabelVoucher(
        @SerializedName("label_quota")
        val labelQuota: Int = 0,
        @SerializedName("label_quota_formatted")
        val labelQuotaFormatted: String = "",
        @SerializedName("label_quota_color_type")
        val labelQuotaColorType: String = "default",
        @SerializedName("label_creator")
        val labelCreator: Int = 0,
        @SerializedName("label_creator_formatted")
        val labelCreatorFormatted: String = "",
        @SerializedName("label_creator_color_type")
        val labelCreatorColorType: String = "default",
        @SerializedName("label_subsidy_info")
        val labelSubsidyInfo: Int = 0,
        @SerializedName("label_subsidy_info_formatted")
        val labelSubsidyInfoFormatted: String = "",
        @SerializedName("label_subsidy_info_color_type")
        val labelSubsidyInfoColorType: String = "default",
        @SerializedName("label_budgets_voucher")
        val labelBudgetsVoucher: List<LabelBudgetVoucher> = listOf()
    ) {
        data class LabelBudgetVoucher(
            @SerializedName("label_budget_voucher")
            val labelBudgetVoucher: Int = 0,
            @SerializedName("label_budget_voucher_formatted")
            val labelBudgetVoucherFormatted: String = "",
            @SerializedName("label_budget_voucher_value")
            val labelBudgetVoucherValue: Int = 0
        )
    }

    data class SubsidyDetail(
        @SerializedName("program_detail")
        val programDetail: ProgramDetail = ProgramDetail(),
        @SerializedName("quota_subsidized")
        val quotaSubsidized: QuotaSubsidized = QuotaSubsidized()
    ) {
        data class ProgramDetail(
            @SerializedName("program_name")
            val programName: String = "",
            @SerializedName("program_status")
            val programStatus: Int = 0,
            @SerializedName("program_label")
            val programLabel: String = "",
            @SerializedName("program_label_detail")
            val programLabelDetail: String = "",
            @SerializedName("promotion_status")
            val promotionStatus: Int = 0,
            @SerializedName("promotion_label")
            val promotionLabel: String = ""
        )

        data class QuotaSubsidized(
            @SerializedName("voucher_quota")
            val voucherQuota: Int = 0,
            @SerializedName("remaining_quota")
            val remainingQuota: Int = 0,
            @SerializedName("booked_global_quota")
            val bookedGlobalQuota: Int = 0,
            @SerializedName("confirmed_global_quota")
            val confirmedGlobalQuota: Int = 0
        )
    }
}
