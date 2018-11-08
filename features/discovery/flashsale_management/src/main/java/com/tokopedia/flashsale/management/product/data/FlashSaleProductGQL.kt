package com.tokopedia.flashsale.management.product.data

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.FlashSaleProductStatusTypeDef
import com.tokopedia.flashsale.management.product.adapter.FlashSaleProductAdapterTypeFactory

/**
 * Created by hendry on 25/10/18.
 */
data class FlashSaleProductGQL(
        @SerializedName("getMojitoEligibleSellerProduct")
        @Expose val flashSaleProductGQLData: FlashSaleProductGQLData)

data class FlashSaleProductGQLData(
        @SerializedName("data")
        @Expose val data: FlashSaleProductHeader)

data class FlashSaleProductHeader(
        @SerializedName("submitted_count")
        @Expose val submittedCount: Int = 0,

        @SerializedName("pending_count")
        @Expose val pendingCount: Int = 0,

        @SerializedName("products")
        @Expose val flashSaleProduct: List<FlashSaleProductItem> = listOf()
)

data class FlashSaleProductItem(
        @SerializedName("id")
        @Expose val id: Int = 0,

        @SerializedName("price")
        @Expose val price: Int = 0,

        @SerializedName("rating")
        @Expose val rating: Float = 0F,

        @SerializedName("name")
        @Expose val name: String = "",

        @SerializedName("department_id")
        @Expose val departmentId: List<Int> = mutableListOf(),

        @Expose var departmentName: List<String> = mutableListOf(),

        @SerializedName("campaign")
        @Expose val campaign: FlashSaleProductItemCampaign = FlashSaleProductItemCampaign()
) : Parcelable, Visitable<FlashSaleProductAdapterTypeFactory> {

    fun getDepartmentNameString() = departmentName.map { it }.joinToString(" > ")

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readFloat(),
            parcel.readString(),
            parcel.createIntArray().toList(),
            parcel.createStringArrayList(),
            parcel.readParcelable(FlashSaleProductItemCampaign::class.java.classLoader)) {
    }

    override fun type(typeFactory: FlashSaleProductAdapterTypeFactory) = typeFactory.type(this)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(price)
        parcel.writeFloat(rating)
        parcel.writeString(name)
        parcel.writeIntArray(departmentId.toIntArray())
        parcel.writeStringList(departmentName)
        parcel.writeParcelable(campaign, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FlashSaleProductItem> {
        override fun createFromParcel(parcel: Parcel): FlashSaleProductItem {
            return FlashSaleProductItem(parcel)
        }

        override fun newArray(size: Int): Array<FlashSaleProductItem?> {
            return arrayOfNulls(size)
        }
    }
}

data class FlashSaleProductItemCampaign(
        @SerializedName("campaign_id")
        @Expose val campaignId: Int = 0,

        @SerializedName("original_price")
        @Expose val originalPrice: Int = 0,

        @SerializedName("discounted_percentage")
        @Expose val discountedPercentage: Float = 0f,

        @SerializedName("discounted_price")
        @Expose val discountedPrice: Int = 0,

        @SerializedName("stock")
        @Expose val stock: Int = 0,

        @SerializedName("product_status")
        @Expose @FlashSaleProductStatusTypeDef val productStatus: Int = 0,

        @SerializedName("image_url")
        @Expose val imageUrl: String = "",

        @SerializedName("is_eligible")
        @Expose val isEligible: Boolean = false,

        @SerializedName("message")
        @Expose val message: String = "",

        @SerializedName("criteria")
        @Expose val criteria: FlashSaleProductItemCampaignCriteria = FlashSaleProductItemCampaignCriteria()
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readFloat(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readParcelable(FlashSaleProductItemCampaignCriteria::class.java.classLoader)) {
    }

    fun getProductStatusString(context: Context): String {
        return when (productStatus) {
            FlashSaleProductStatusTypeDef.NOTHING -> ""
            FlashSaleProductStatusTypeDef.SUBMITTED -> context.getString(R.string.flash_sale_registered)
            FlashSaleProductStatusTypeDef.REJECTED -> context.getString(R.string.flash_sale_rejected)
            FlashSaleProductStatusTypeDef.RESERVE -> context.getString(R.string.flash_sale_reserve)
            FlashSaleProductStatusTypeDef.SUBMIT_CANCEL -> context.getString(R.string.flash_sale_canceled)
            FlashSaleProductStatusTypeDef.SUBMIT_CANCEL_SUBMIT -> context.getString(R.string.flash_sale_registered)
            else -> ""
        }
    }

    fun getProductStatusActionString(context: Context): String {
        return when (productStatus) {
            FlashSaleProductStatusTypeDef.NOTHING -> context.getString(R.string.flash_sale_reserve_product)
            FlashSaleProductStatusTypeDef.SUBMITTED -> context.getString(R.string.flash_sale_cancel_reserve)
            FlashSaleProductStatusTypeDef.REJECTED -> context.getString(R.string.flash_sale_resubmit_product)
            FlashSaleProductStatusTypeDef.RESERVE -> context.getString(R.string.flash_sale_cancel_reserve)
            FlashSaleProductStatusTypeDef.SUBMIT_CANCEL -> context.getString(R.string.flash_sale_reserve_product)
            FlashSaleProductStatusTypeDef.SUBMIT_CANCEL_SUBMIT -> context.getString(R.string.flash_sale_reserve_product)
            else -> context.getString(R.string.flash_sale_resubmit_product)
        }
    }

    fun getProductStatusColor(): StatusColor {
        return when (productStatus) {
            FlashSaleProductStatusTypeDef.NOTHING -> StatusColor(0, 0)
            FlashSaleProductStatusTypeDef.REJECTED,
            FlashSaleProductStatusTypeDef.SUBMIT_CANCEL -> StatusColor(R.color.white, R.drawable.rect_gray_rounded_left)
            FlashSaleProductStatusTypeDef.SUBMITTED,
            FlashSaleProductStatusTypeDef.RESERVE,
            FlashSaleProductStatusTypeDef.SUBMIT_CANCEL_SUBMIT -> StatusColor(R.color.tkpd_main_green, 0)
            else -> StatusColor(0, 0)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(campaignId)
        parcel.writeInt(originalPrice)
        parcel.writeFloat(discountedPercentage)
        parcel.writeInt(discountedPrice)
        parcel.writeInt(stock)
        parcel.writeInt(productStatus)
        parcel.writeString(imageUrl)
        parcel.writeByte(if (isEligible) 1 else 0)
        parcel.writeString(message)
        parcel.writeParcelable(criteria, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FlashSaleProductItemCampaign> {
        override fun createFromParcel(parcel: Parcel): FlashSaleProductItemCampaign {
            return FlashSaleProductItemCampaign(parcel)
        }

        override fun newArray(size: Int): Array<FlashSaleProductItemCampaign?> {
            return arrayOfNulls(size)
        }
    }
}

data class StatusColor(val textColor: Int, val bgDrawableRes: Int)

data class FlashSaleProductItemCampaignCriteria(
        @SerializedName("criteria_id")
        @Expose val criteriaId: Int = 0,

        @SerializedName("price_min")
        @Expose val priceMin: Int = 0,

        @SerializedName("price_max")
        @Expose val priceMax: Int = 0,

        @SerializedName("stock_min")
        @Expose val stockMin: Int = 0,

        @SerializedName("rating_min")
        @Expose val ratingMin: Int = 0,

        @SerializedName("rating_max")
        @Expose val ratingMax: Int = 0,

        @SerializedName("discount_percentage_min")
        @Expose val discountPercentageMin: Float = 0F,

        @SerializedName("discount_percentage_max")
        @Expose val discountPercentageMax: Float = 0F,

        @SerializedName("submission_max")
        @Expose val submissionMax: Int = 0,

        @SerializedName("submission_count")
        @Expose val submissionCount: Int = 0,

        @SerializedName("exclude_preorder")
        @Expose val excludePreorder: Boolean = true,

        @SerializedName("exclude_wholesale")
        @Expose val excludeWholesale: Boolean = true,

        @SerializedName("min_order_min")
        @Expose val minOrderMin: Int = 0,

        @SerializedName("min_order_max")
        @Expose val minOrderMax: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readInt(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(criteriaId)
        parcel.writeInt(priceMin)
        parcel.writeInt(priceMax)
        parcel.writeInt(stockMin)
        parcel.writeInt(ratingMin)
        parcel.writeInt(ratingMax)
        parcel.writeFloat(discountPercentageMin)
        parcel.writeFloat(discountPercentageMax)
        parcel.writeInt(submissionMax)
        parcel.writeInt(submissionCount)
        parcel.writeByte(if (excludePreorder) 1 else 0)
        parcel.writeByte(if (excludeWholesale) 1 else 0)
        parcel.writeInt(minOrderMin)
        parcel.writeInt(minOrderMax)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FlashSaleProductItemCampaignCriteria> {
        override fun createFromParcel(parcel: Parcel): FlashSaleProductItemCampaignCriteria {
            return FlashSaleProductItemCampaignCriteria(parcel)
        }

        override fun newArray(size: Int): Array<FlashSaleProductItemCampaignCriteria?> {
            return arrayOfNulls(size)
        }
    }

}