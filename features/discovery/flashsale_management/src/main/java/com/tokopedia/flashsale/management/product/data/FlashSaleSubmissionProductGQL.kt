package com.tokopedia.flashsale.management.product.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.flashsale.management.data.FlashSaleAdminStatusIdTypeDef
import com.tokopedia.flashsale.management.data.FlashSaleCampaignStatusIdTypeDef
import com.tokopedia.flashsale.management.data.FlashSaleProductStatusTypeDef

/**
 * Created by hendry on 25/10/18.
 */
data class FlashSaleSubmissionProductGQL(
        @SerializedName("getCampaignEligibleSellerProduct")
        @Expose val mojitoEligibleSellerProduct: MojitoEligibleSellerProduct)

data class MojitoEligibleSellerProduct(
        @SerializedName("data")
        @Expose val data: FlashSaleSubmissionProductData)

data class FlashSaleSubmissionProductData(
        @SerializedName("submitted_count")
        @Expose val submittedCount: Int = 0,

        @SerializedName("pending_count")
        @Expose val pendingCount: Int = 0,

        @SerializedName("products")
        @Expose val flashSaleSubmissionProduct: List<FlashSaleSubmissionProductItem> = listOf()
)

data class FlashSaleSubmissionProductItem(
        @SerializedName("id")
        @Expose val id: Int = 0,

        @SerializedName("price")
        @Expose val price: Int = 0,

        @SerializedName("name")
        @Expose val name: String = "",

        @SerializedName("department_id")
        @Expose val departmentId: List<Int> = mutableListOf(),

        @Expose var departmentName: List<String> = mutableListOf(),

        @SerializedName("campaign")
        @Expose val campaign: FlashSaleProductItemCampaign = FlashSaleProductItemCampaign()
) : FlashSaleProductItem(), Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.createIntArray().toList(),
            parcel.createStringArrayList(),
            parcel.readParcelable(FlashSaleProductItemCampaign::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(price)
        parcel.writeString(name)
        parcel.writeIntArray(departmentId.toIntArray())
        parcel.writeStringList(departmentName)
        parcel.writeParcelable(campaign, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FlashSaleSubmissionProductItem> {
        override fun createFromParcel(parcel: Parcel): FlashSaleSubmissionProductItem {
            return FlashSaleSubmissionProductItem(parcel)
        }

        override fun newArray(size: Int): Array<FlashSaleSubmissionProductItem?> {
            return arrayOfNulls(size)
        }
    }

    override fun getProductId() = id
    override fun getProductName() = name
    override fun getProductDepartmentId()= departmentId
    override fun getProductImageUrl() = campaign.imageUrl
    override fun getProductPrice() = price
    override fun getProductDepartmentName() = departmentName
    override fun getCampOriginalPrice() = campaign.originalPrice
    override fun getDiscountPercentage() = campaign.discountedPercentage
    override fun getDiscountedPrice() = campaign.discountedPrice
    override fun getCustomStock() = campaign.stock
    override fun getOriginalCustomStock() = campaign.stock
    override fun getStockSoldPercentage() = 0
    override fun isEligible() = campaign.isEligible
    override fun getMessage() = campaign.message
    override fun getProductStatus() = campaign.productStatus
    override fun getCampaignStatusId() = FlashSaleCampaignStatusIdTypeDef.IN_SUBMISSION
    override fun getCampaignAdminStatusId() = FlashSaleAdminStatusIdTypeDef.NOT_REVIEWED
}

data class FlashSaleProductItemCampaign(
        @SerializedName("campaign_id")
        @Expose val campaignId: Int = 0,

        @SerializedName("original_price")
        @Expose val originalPrice: Int = 0,

        @SerializedName("discount_percentage")
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