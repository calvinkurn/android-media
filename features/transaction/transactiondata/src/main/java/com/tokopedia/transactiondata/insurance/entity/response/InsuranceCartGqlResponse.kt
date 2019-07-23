package com.tokopedia.transactiondata.insurance.entity.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class InsuranceCartGqlResponse(
        @SerializedName("cart_list_transactional")
        var data: InsuranceCartResponse
)

data class InsuranceCartResponse(
        @SerializedName("shops")
        var cartShopsList: ArrayList<InsuranceCartShops>
)

data class InsuranceCartShops(
        @SerializedName("shop_id")
        var shopId: Long,

        @SerializedName("items")
        var shopItemsList: ArrayList<InsuranceCartShopItems>
)

data class InsuranceCartShopItems(
        @SerializedName("product_id")
        var productId: Long,

        @SerializedName("digital_product")
        var digitalProductList: ArrayList<InsuranceCartDigitalProduct>
)

data class InsuranceCartDigitalProduct(
        @SerializedName("digital_product_id")
        var digitalProductId: Long,

        @SerializedName("cart_item_id")
        var cartItemId: Long,

        @SerializedName("type_id")
        var typeId: Long,

        @SerializedName("price_per_product")
        var pricePerProduct: Long,

        @SerializedName("total_price")
        var totalPrice: Long,

        @SerializedName("opt_in")
        var optIn: Boolean,

        @SerializedName("is_product_level")
        var isProductLevel: Boolean,

        @SerializedName("is_purchase_protection")
        var isPurchaseProtection: Boolean,

        @SerializedName("is_seller_money")
        var isSellerMoney: Boolean,

        @SerializedName("is_application_needed")
        var isApplicationNeeded: Boolean,

        @SerializedName("is_new")
        var isNew: Boolean,

        @SerializedName("product_info")
        var productInfo: InsuranceCartProductInfo,

        @SerializedName("application_details")
        var applicationDetails: ArrayList<InsuranceProductApplicationDetails>
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readLong(),
            parcel.readLong(),
            parcel.readLong(),
            parcel.readLong(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readParcelable<InsuranceCartProductInfo>(InsuranceCartProductInfo::class.java.classLoader)!!,
            arrayListOf<InsuranceProductApplicationDetails>().apply {
                parcel.readList(this, InsuranceProductApplicationDetails::class.java.classLoader)
            }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(digitalProductId)
        parcel.writeLong(cartItemId)
        parcel.writeLong(typeId)
        parcel.writeLong(pricePerProduct)
        parcel.writeLong(totalPrice)
        parcel.writeByte(if (optIn) 1 else 0)
        parcel.writeByte(if (isProductLevel) 1 else 0)
        parcel.writeByte(if (isPurchaseProtection) 1 else 0)
        parcel.writeByte(if (isSellerMoney) 1 else 0)
        parcel.writeByte(if (isApplicationNeeded) 1 else 0)
        parcel.writeByte(if (isNew) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InsuranceCartDigitalProduct> {
        override fun createFromParcel(parcel: Parcel): InsuranceCartDigitalProduct {
            return InsuranceCartDigitalProduct(parcel)
        }

        override fun newArray(size: Int): Array<InsuranceCartDigitalProduct?> {
            return arrayOfNulls(size)
        }
    }

}

data class InsuranceCartProductInfo(
        @SerializedName("title")
        var title: String,

        @SerializedName("sub_title")
        var subTitle: String,

        @SerializedName("detail_info_title")
        var detailInfoTitle: String,

        @SerializedName("description")
        var description: String,

        @SerializedName("section_title")
        var sectionTitle: String,

        @SerializedName("icon_url")
        var iconUrl: String,

        @SerializedName("web_link_url")
        var webLinkUrl: String,

        @SerializedName("ticker_text")
        var tickerText: String,

        @SerializedName("info_text")
        var infoText: String,

        @SerializedName("app_link_url")
        var appLinkUrl: String,

        @SerializedName("link_name")
        var linkName: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(subTitle)
        parcel.writeString(detailInfoTitle)
        parcel.writeString(description)
        parcel.writeString(sectionTitle)
        parcel.writeString(iconUrl)
        parcel.writeString(webLinkUrl)
        parcel.writeString(tickerText)
        parcel.writeString(infoText)
        parcel.writeString(appLinkUrl)
        parcel.writeString(linkName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InsuranceCartProductInfo> {
        override fun createFromParcel(parcel: Parcel): InsuranceCartProductInfo {
            return InsuranceCartProductInfo(parcel)
        }

        override fun newArray(size: Int): Array<InsuranceCartProductInfo?> {
            return arrayOfNulls(size)
        }
    }

}

data class InsuranceProductApplicationDetails(
        @SerializedName("id")
        var id: Int,

        @SerializedName("label")
        var label: String,

        @SerializedName("place_holder")
        var placeHolder: String,

        @SerializedName("type")
        var type: String,

        @SerializedName("required")
        var isRequired: Boolean,

        @SerializedName("value")
        var value: String,

        @SerializedName("values")
        var valuesList: ArrayList<InsuranceApplicationValue>,

        @SerializedName("validations")
        var validationsList: ArrayList<InsuranceApplicationValidation>,

        var isError: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readByte() != 0.toByte(),
            parcel.readString() ?: "",
            arrayListOf<InsuranceApplicationValue>().apply {
                parcel.readList(this, InsuranceApplicationValue::class.java.classLoader)
            },
            arrayListOf<InsuranceApplicationValidation>().apply {
                parcel.readList(this, InsuranceApplicationValidation::class.java.classLoader)
            }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(label)
        parcel.writeString(placeHolder)
        parcel.writeString(type)
        parcel.writeByte(if (isRequired) 1 else 0)
        parcel.writeString(value)
        parcel.writeByte(if (isError) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InsuranceProductApplicationDetails> {
        override fun createFromParcel(parcel: Parcel): InsuranceProductApplicationDetails {
            return InsuranceProductApplicationDetails(parcel)
        }

        override fun newArray(size: Int): Array<InsuranceProductApplicationDetails?> {
            return arrayOfNulls(size)
        }
    }

}

data class InsuranceApplicationValue(
        @SerializedName("id")
        var valuesId: Int,

        @SerializedName("value")
        var value: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() ?: "") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(valuesId)
        parcel.writeString(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InsuranceApplicationValue> {
        override fun createFromParcel(parcel: Parcel): InsuranceApplicationValue {
            return InsuranceApplicationValue(parcel)
        }

        override fun newArray(size: Int): Array<InsuranceApplicationValue?> {
            return arrayOfNulls(size)
        }
    }

}

data class InsuranceApplicationValidation(
        @SerializedName("id")
        var validationId: Int,

        @SerializedName("value")
        var validationValue: String,

        @SerializedName("type")
        var type: String,

        @SerializedName("error_message")
        var validationErrorMessage: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(validationId)
        parcel.writeString(validationValue)
        parcel.writeString(type)
        parcel.writeString(validationErrorMessage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InsuranceApplicationValidation> {
        override fun createFromParcel(parcel: Parcel): InsuranceApplicationValidation {
            return InsuranceApplicationValidation(parcel)
        }

        override fun newArray(size: Int): Array<InsuranceApplicationValidation?> {
            return arrayOfNulls(size)
        }
    }

}