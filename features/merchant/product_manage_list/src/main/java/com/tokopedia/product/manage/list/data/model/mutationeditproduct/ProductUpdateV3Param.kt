package com.tokopedia.product.manage.list.data.model.mutationeditproduct

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductUpdateV3Param(
        @SerializedName("productID")
        @Expose
        var productId: String = "",

        @SerializedName("status")
        @Expose
        var productStatus: String = "",

        @SerializedName("menu")
        @Expose
        var productEtalase: ProductEtalase = ProductEtalase(),

        @SerializedName("shop")
        @Expose
        var shop: ShopParam = ShopParam()
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readParcelable(ProductEtalase::class.java.classLoader) ?: ProductEtalase(),
            parcel.readParcelable(ShopParam::class.java.classLoader) ?: ShopParam())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productId)
        parcel.writeString(productStatus)
        parcel.writeParcelable(productEtalase, flags)
        parcel.writeParcelable(shop, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductUpdateV3Param> {
        override fun createFromParcel(parcel: Parcel): ProductUpdateV3Param {
            return ProductUpdateV3Param(parcel)
        }

        override fun newArray(size: Int): Array<ProductUpdateV3Param?> {
            return arrayOfNulls(size)
        }
    }
}

data class ProductEtalase(
        @SerializedName("menuID")
        @Expose
        var etalaseId: String = "",

        @SerializedName("name")
        @Expose
        var etalaseName: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(etalaseId)
        parcel.writeString(etalaseName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductEtalase> {
        override fun createFromParcel(parcel: Parcel): ProductEtalase {
            return ProductEtalase(parcel)
        }

        override fun newArray(size: Int): Array<ProductEtalase?> {
            return arrayOfNulls(size)
        }
    }
}

data class ShopParam(
        @SerializedName("id")
        @Expose
        var shopId: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString() ?: "") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(shopId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShopParam> {
        override fun createFromParcel(parcel: Parcel): ShopParam {
            return ShopParam(parcel)
        }

        override fun newArray(size: Int): Array<ShopParam?> {
            return arrayOfNulls(size)
        }
    }
}