package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class VoucherOrdersItem(
    @SerializedName("cashback_wallet_amount")
    val cashbackWalletAmount: Int = 0,

    @SerializedName("code")
    val code: String = "",

    @SerializedName("unique_id")
    val uniqueId: String = "",

    @SerializedName("discount_amount")
    val discountAmount: Int = 0,

    @SerializedName("title_description")
    val titleDescription: String = "",

    @SerializedName("is_po")
    val isPo: Int = 0,

    @SerializedName("message")
    val message: Message = Message(),

    @SerializedName("type")
    val type: String = "",

    @SuppressLint("Invalid Data Type")
    @SerializedName("shipping_id")
    val shippingId: Int = 0,

    @SuppressLint("Invalid Data Type")
    @SerializedName("sp_id")
    val spId: Int = 0,

    @SerializedName("benefit_details")
    val benefitDetails: List<BenefitDetailsItem> = emptyList(),

    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("invoice_description")
    val invoiceDescription: String = ""
)
