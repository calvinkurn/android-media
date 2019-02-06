package com.tokopedia.flashsale.management.product.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.design.utils.StringUtils
import com.tokopedia.flashsale.management.data.FlashSaleCampaignStatusIdTypeDef
import com.tokopedia.flashsale.management.data.FlashSaleProductStatusTypeDef

/**
 * Created by hendry on 25/10/18.
 */
data class FlashSalePostProductGQL(
        @SerializedName("getCampaignPostProductList")
        @Expose val getMojitoPostProduct: GetMojitoPostProduct)

data class GetMojitoPostProduct(
        @SerializedName("header")
        @Expose val header: FlashSalePostProductHeader,

        @SerializedName("data")
        @Expose val data: FlashSalePostProductData)

data class FlashSalePostProductHeader(
        @SerializedName("total_data")
        @Expose val totalData: Int = 0)

data class FlashSalePostProductData(
        @SerializedName("products")
        @Expose val flashSalePostProductList: List<FlashSalePostProductItem> = listOf()
)

data class FlashSalePostProductItem(
        @SerializedName("id")
        @Expose val id: Int = 0,

        @SerializedName("price_float")
        @Expose val price: Float = 0F,

        @SerializedName("name")
        @Expose val name: String = "",

        @SerializedName("image_url")
        @Expose val imageUrl: String,

        @SerializedName("department_id")
        @Expose val departmentId: List<Int> = mutableListOf(),

        @Expose var departmentName: List<String> = mutableListOf(),

        @SerializedName("campaign")
        @Expose val campaign: FlashSalePostProductItemCampaign = FlashSalePostProductItemCampaign()
) : FlashSaleProductItem(), Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readFloat(),
            parcel.readString(),
            parcel.readString(),
            parcel.createIntArray().toList(),
            parcel.createStringArrayList(),
            parcel.readParcelable(FlashSalePostProductItemCampaign::class.java.classLoader)) {
    }

    override fun getProductId() = id
    override fun getProductName() = name
    override fun getProductDepartmentId() = departmentId
    override fun getProductImageUrl() = imageUrl
    override fun getProductPrice() = price.toInt()
    override fun getProductDepartmentName() = departmentName
    override fun getCampOriginalPrice() = campaign.originalPrice.toInt()
    override fun getDiscountPercentage() = campaign.discountedPercentage
    override fun getDiscountedPrice() = campaign.discountedPrice.toInt()
    override fun getCustomStock() = campaign.customStock
    override fun getOriginalCustomStock() = campaign.originalCustomStock
    override fun getStockSoldPercentage() = 0
    override fun isEligible() = false //no operation edit is needed for post submission
    override fun getMessage() = ""
    override fun getProductStatus() = FlashSaleProductStatusTypeDef.NOTHING
    override fun getCampaignStatusId() = campaign.statusId
    override fun getCampaignAdminStatusId() = campaign.adminStatus

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeFloat(price)
        parcel.writeString(name)
        parcel.writeString(imageUrl)
        parcel.writeIntArray(departmentId.toIntArray())
        parcel.writeStringList(departmentName)
        parcel.writeParcelable(campaign, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FlashSalePostProductItem> {
        override fun createFromParcel(parcel: Parcel): FlashSalePostProductItem {
            return FlashSalePostProductItem(parcel)
        }

        override fun newArray(size: Int): Array<FlashSalePostProductItem?> {
            return arrayOfNulls(size)
        }
    }

}

data class FlashSalePostProductItemCampaign(
        @SerializedName("campaign_id")
        @Expose val campaignId: Int = 0,

        @SerializedName("original_price_float")
        @Expose val originalPrice: Float = 0F,

        @SerializedName("discount_percentage")
        @Expose val discountedPercentage: Float = 0f,

        @SerializedName("discounted_price_float")
        @Expose val discountedPrice: Float = 0F,

        @SerializedName("custom_stock")
        @Expose val customStock: Int = 0,

        @SerializedName("original_custom_stock")
        @Expose val originalCustomStock: Int = 0,

        @SerializedName("stock_sold_percentage")
        @Expose val stockSoldPercentage: Int = 0,

        @SerializedName("status_id")
        @Expose val statusId: Int = 0,

        @SerializedName("admin_status")
        @Expose val adminStatus: Int = 0

) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(campaignId)
        parcel.writeFloat(originalPrice)
        parcel.writeFloat(discountedPercentage)
        parcel.writeFloat(discountedPrice)
        parcel.writeInt(customStock)
        parcel.writeInt(originalCustomStock)
        parcel.writeInt(stockSoldPercentage)
        parcel.writeInt(statusId)
        parcel.writeInt(adminStatus)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FlashSalePostProductItemCampaign> {
        override fun createFromParcel(parcel: Parcel): FlashSalePostProductItemCampaign {
            return FlashSalePostProductItemCampaign(parcel)
        }

        override fun newArray(size: Int): Array<FlashSalePostProductItemCampaign?> {
            return arrayOfNulls(size)
        }
    }


}