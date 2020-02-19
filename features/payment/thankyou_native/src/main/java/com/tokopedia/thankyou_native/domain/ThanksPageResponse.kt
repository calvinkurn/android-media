package com.tokopedia.thankyou_native.domain

import com.google.gson.annotations.SerializedName

data class ThanksPageResponse(
        @SerializedName("thanksPageData")
        val thanksPageData: ThanksPageData
)

data class ThanksPageData(
        @SerializedName("payment_status")
        val paymentStatus: Int,
        @SerializedName("gateway_name")
        val gatewayName: String,
        @SerializedName("gateway_image")
        val gatewayImage: String,
        @SerializedName("expire_time_unix")
        val expireTimeUnix: Long,
        @SerializedName("amount")
        val amount: Long,
        @SerializedName("amount_str")
        val amountStr: String,
        @SerializedName("order_list")
        val orderList: ArrayList<OrderList>,
        @SerializedName("additional_info")
        val additionalInfo: AdditionalInfo,
        @SerializedName("how_to_pay")
        val howToPay: String,
        @SerializedName("whitelisted_rba")
        val whitelistedRBA: String

)

data class AdditionalInfo(
        @SerializedName("account_number")
        val accountNumber: String,
        @SerializedName("account_dest")
        val accountDest: String,
        @SerializedName("bank_name")
        val bankName: String,
        @SerializedName("payment_code")
        val paymentCode: String,
        @SerializedName("masked_number")
        val maskedNumber: String,
        @SerializedName("installment_info")
        val installmentInfo: String,
        @SerializedName("interest")
        val interest: Float
)

data class OrderList(
        @SerializedName("store_name")
        val storeName: String,
        @SerializedName("item_list")
        val purchaseItemList: ArrayList<PurchaseItem>
)

data class PurchaseItem(
        @SerializedName("product_name")
        val productName: String,
        @SerializedName("quantity")
        val quantity: Int,
        @SerializedName("weight")
        val weight: Double,
        @SerializedName("thumbnail_product")
        val thumbnailProduct: String
)