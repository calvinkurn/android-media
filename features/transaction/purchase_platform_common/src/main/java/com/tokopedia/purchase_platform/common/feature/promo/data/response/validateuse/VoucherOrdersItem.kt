package com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class VoucherOrdersItem(

        @field:SerializedName("code")
        val code: String = "",

        @field:SerializedName("unique_id")
        val uniqueId: String = "",

        @field:SerializedName("cashback_wallet_amount")
        val cashbackWalletAmount: Int = 0,

        @field:SerializedName("discount_amount")
        val discountAmount: Int = 0,

        @field:SerializedName("address_id")
        val addressId: Int = 0,

        @field:SerializedName("title_description")
        val titleDescription: String = "",

        @field:SerializedName("is_po")
        val isPo: Int = 0,

        @field:SerializedName("type")
        val type: String = "",

        @field:SerializedName("message")
        val message: Message = Message(),

        @field:SerializedName("duration")
        val duration: String = "",

        @field:SerializedName("cart_id")
        val cartId: Int = 0,

        @field:SerializedName("shop_id")
        val shopId: Int = 0,

        @field:SerializedName("benefit_details")
        val benefitDetails: List<BenefitDetailsItem> = emptyList(),

        @field:SerializedName("success")
        val success: Boolean = false,

        @field:SerializedName("invoice_description")
        val invoiceDescription: String = "",

        @field:SerializedName("order_id")
        val orderId: Int = 0,

        @field:SerializedName("warehouse_id")
        val warehouseId: Int = 0
)