package com.tokopedia.thankyou_native.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class ThanksPageResponse(
        @SerializedName("thanksPageData")
        val thanksPageData: ThanksPageData
)

@Parcelize
data class ThanksPageData(
        @SerializedName("payment_id")
        val paymentID: Long,
        @SerializedName("profile_code")
        val profileCode: String,
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
        @SerializedName("combine_amount")
        val combinedAmount: Double,
        @SerializedName("order_list")
        val shopOrder: ArrayList<ShopOrder>,
        @SerializedName("additional_info")
        val additionalInfo: AdditionalInfo,
        @SerializedName("how_to_pay")
        val howToPay: String?,
        @SerializedName("how_to_pay_applink")
        val howToPayAPP : String?,
        @SerializedName("whitelisted_rba")
        val whitelistedRBA: Boolean,
        @SerializedName("payment_type")
        val paymentType: String,
        @SerializedName("expire_time_str")
        val expireTimeStr: String,
        @SerializedName("page_type")
        val pageType: String,
        @SerializedName("payment_items")
        val paymentItems: ArrayList<PaymentItem>?,
        @SerializedName("payment_deduction")
        val paymentDeductions: ArrayList<PaymentItem>?,
        @SerializedName("payment_details")
        val paymentDetails: ArrayList<PaymentDetail>?,
        @SerializedName("order_amount_str")
        val orderAmountStr: String,
        @SerializedName("order_amount")
        val orderAmount: Double,
        @SerializedName("current_site")
        val currentSite: String,
        @SerializedName("business_unit")
        val businessUnit: String,
        @SerializedName("event")
        val event: String,
        @SerializedName("event_category")
        val eventCategory: String,
        @SerializedName("event_action")
        val eventAction: String,
        @SerializedName("event_label")
        val eventLabel: String,
        @SerializedName("push_gtm")
        val pushGtm: Boolean,
        @SerializedName("merchant_code")
        val merchantCode: String,
        @SerializedName("new_user")
        val isNewUser: Boolean,
        @SerializedName("is_mub")
        val isMonthlyNewUser: Boolean,
        @SerializedName("custom_data")
        val thanksCustomization: ThanksCustomization?,
        @SerializedName("config_flag")
        val configFlag: String?,
        @SerializedName("config_list")
        val configList: String?,
        @SerializedName("gateway_additional_data")
        val gatewayAdditionalDataList: ArrayList<GatewayAdditionalData>?,
        @SerializedName("fee_details")
        val feeDetailList : ArrayList<FeeDetail>?,
        //created and used locally
        var paymentMethodCount: Int
) : Parcelable

@Parcelize
data class FeeDetail (
        @SerializedName("name")
        val name: String,
        @SerializedName("amount")
        val amount: Long,
) : Parcelable

@Parcelize
data class GatewayAdditionalData(
        @SerializedName("key")
        val key : String?,
        @SerializedName("value")
        val value : String?
) : Parcelable


data class PaymentDetail(
        @SerializedName("gateway_code")
        val gatewayCode: String,
        @SerializedName("gateway_name")
        val gatewayName: String,
        @SerializedName("amount")
        val amount: Float,
        @SerializedName("amount_str")
        val amountStr: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readFloat(),
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(gatewayCode)
        parcel.writeString(gatewayName)
        parcel.writeFloat(amount)
        parcel.writeString(amountStr)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PaymentDetail> {
        override fun createFromParcel(parcel: Parcel): PaymentDetail {
            return PaymentDetail(parcel)
        }

        override fun newArray(size: Int): Array<PaymentDetail?> {
            return arrayOfNulls(size)
        }
    }
}

@Parcelize
data class AdditionalInfo(
        @SerializedName("account_number")
        val accountNumber: String,
        @SerializedName("account_dest")
        val accountDest: String,
        @SerializedName("bank_name")
        val bankName: String,
        @SerializedName("bank_branch")
        val bankBranch: String,
        @SerializedName("payment_code")
        val paymentCode: String,
        @SerializedName("masked_number")
        val maskedNumber: String,
        @SerializedName("installment_info")
        val installmentInfo: String?,
        @SerializedName("interest")
        val interest: Float,
        @SerializedName("revenue")
        val revenue: Float) : Parcelable

@Parcelize
data class ShopOrder(
        @SerializedName("order_id")
        val orderId: String,
        @SerializedName("store_id")
        val storeId: String,
        @SerializedName("store_type")
        val storeType: String,
        @SerializedName("logistic_type")
        val logisticType: String,
        @SerializedName("store_name")
        val storeName: String?,
        @SerializedName("item_list")
        val purchaseItemList: ArrayList<PurchaseItem>,
        @SerializedName("shipping_amount")
        val shippingAmount: Float,
        @SerializedName("shipping_amount_str")
        val shippingAmountStr: String,
        @SerializedName("shipping_desc")
        val shippingDesc: String,
        @SerializedName("insurance_amount")
        val insuranceAmount: Float,
        @SerializedName("insurance_amount_str")
        val insuranceAmountStr: String,
        @SerializedName("address")
        val address: String,
        @SerializedName("promo_data")
        val promoData: ArrayList<PromoData>?,
        @SerializedName("tax")
        val tax: Long,
        @SerializedName("coupon")
        val coupon: String,
        @SerializedName("revenue")
        val revenue: Float,
        @SerializedName("logistic_duration")
        val logisticDuration: String?,
        @SerializedName("logistic_eta")
        val logisticETA: String?


) : Parcelable

data class PromoData(
        @SerializedName("promo_code")
        val promoCode: String,
        @SerializedName("promo_desc")
        val promoDesc: String,
        @SerializedName("total_cashback")
        val totalCashback: Float,
        @SerializedName("total_cashback_str")
        val totalCashbackStr: String,
        @SerializedName("total_discount")
        val totalDiscount: Float,
        @SerializedName("total_discount_str")
        val totalDiscountStr: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readFloat(),
            parcel.readString() ?: "",
            parcel.readFloat(),
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(promoCode)
        parcel.writeString(promoDesc)
        parcel.writeFloat(totalCashback)
        parcel.writeString(totalCashbackStr)
        parcel.writeFloat(totalDiscount)
        parcel.writeString(totalDiscountStr)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PromoData> {
        override fun createFromParcel(parcel: Parcel): PromoData {
            return PromoData(parcel)
        }

        override fun newArray(size: Int): Array<PromoData?> {
            return arrayOfNulls(size)
        }
    }
}

data class PurchaseItem(
        @SerializedName("product_id")
        val productId: String,
        @SerializedName("product_name")
        val productName: String,
        @SerializedName("product_brand")
        val productBrand: String,
        @SerializedName("price")
        val price: Float,
        @SerializedName("price_str")
        val priceStr: String,
        @SerializedName("quantity")
        val quantity: Int,
        @SerializedName("weight")
        val weight: Float,
        @SerializedName("weight_unit")
        val weightUnit: String,
        @SerializedName("total_price")
        val totalPrice: Float,
        @SerializedName("total_price_str")
        val totalPriceStr: String,
        @SerializedName("promo_code")
        val promoCode: String,
        @SerializedName("category")
        val category: String,
        @SerializedName("variant")
        val variant: String,
        @SerializedName("thumbnail_product")
        val thumbnailProduct: String,
        @SerializedName("product_plan_protection")
        val productPlanProtection: Double,
        @SerializedName("bebas_ongkir_dimension")
        val bebasOngkirDimension: String,
        @SerializedName("is_bbi")
        val isBBIProduct: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readFloat(),
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readFloat(),
            parcel.readString() ?: "",
            parcel.readFloat(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readDouble(),
            parcel.readString() ?: "",
            parcel.readByte() == 1.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productId)
        parcel.writeString(productName)
        parcel.writeString(productBrand)
        parcel.writeFloat(price)
        parcel.writeString(priceStr)
        parcel.writeInt(quantity)
        parcel.writeFloat(weight)
        parcel.writeString(weightUnit)
        parcel.writeFloat(totalPrice)
        parcel.writeString(totalPriceStr)
        parcel.writeString(promoCode)
        parcel.writeString(category)
        parcel.writeString(variant)
        parcel.writeString(thumbnailProduct)
        parcel.writeDouble(productPlanProtection)
        parcel.writeString(bebasOngkirDimension)
        parcel.writeByte(if (isBBIProduct) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PurchaseItem> {
        override fun createFromParcel(parcel: Parcel): PurchaseItem {
            return PurchaseItem(parcel)
        }

        override fun newArray(size: Int): Array<PurchaseItem?> {
            return arrayOfNulls(size)
        }
    }
}

data class PaymentItem(
        @SerializedName("item_name")
        val itemName: String,
        @SerializedName("item_desc")
        val itemDesc: String,
        @SerializedName("amount")
        val amount: Float,
        @SerializedName("amount_str")
        val amountStr: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readFloat(),
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(itemName)
        parcel.writeString(itemDesc)
        parcel.writeFloat(amount)
        parcel.writeString(amountStr)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PaymentItem> {
        override fun createFromParcel(parcel: Parcel): PaymentItem {
            return PaymentItem(parcel)
        }

        override fun newArray(size: Int): Array<PaymentItem?> {
            return arrayOfNulls(size)
        }
    }
}

@Parcelize
data class ThanksCustomization(
        @SerializedName("tracking_data")
        val trackingData: String?,
        @SerializedName("custom_order_url_app")
        val customOrderUrlApp: String?,
        @SerializedName("custom_home_url_app")
        val customHomeUrlApp: String?,
        @SerializedName("custom_title")
        val customTitle: String?,
        @SerializedName("custom_subtitle")
        val customSubtitle: String?,
        @SerializedName("custom_title_order_button")
        val customTitleOrderButton: String?,
        @SerializedName("custom_wtv_text")
        val customWtvText: String?,
        @SerializedName("custom_title_home_button")
        val customHomeButtonTitle: String?) : Parcelable

data class ConfigFlag(
        @SerializedName("enable_thanks_widget")
        val isThanksWidgetEnabled : Boolean
)

data class Tickers(
        @SerializedName("tickers")
        val tickerDataListStr : String?
)
data class ThankPageTopTickerData(
        @SerializedName("ticker_title")
        val tickerTitle : String?,
        @SerializedName("ticker_text")
        val tickerDescription : String?,
        @SerializedName("ticker_type")
        val ticketType : String?,
        @SerializedName("ticker_cta_url")
        val tickerCTAUrl : String?,
        @SerializedName("ticker_cta_title")
        val tickerCTATitle: String?,
        @SerializedName("ticker_cta_applink")
        val tickerCTAApplink: String?
) {
    fun getURL(): String? {
        return if (!tickerCTAApplink.isNullOrEmpty()) {
            tickerCTAApplink
        } else {
            tickerCTAUrl
        }
    }

    fun isAppLink() : Boolean{
        return !tickerCTAApplink.isNullOrEmpty()
    }
}