package com.tokopedia.product.detail.data.model.shop

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.detail.view.adapter.CourierTypeFactory

data class ShopShipment(
        @SerializedName("isAvailable")
        @Expose
        val isAvailable: Int = 0,

        @SerializedName("code")
        @Expose
        val code: String = "",

        @SerializedName("shipmentID")
        @Expose
        val shipmentID: String = "",

        @SerializedName("image")
        @Expose
        val image: String = "",

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("isPickup")
        @Expose
        val isPickup: Int = 0,

        @SerializedName("maxAddFee")
        @Expose
        val maxAddFee: Int = 0,

        @SerializedName("awbStatus")
        @Expose
        val awbStatus: Int = 0,

        @SerializedName("product")
        @Expose
        val product: List<ShipmentProduct> = listOf()
        ): Visitable<CourierTypeFactory>, Parcelable{

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.createTypedArrayList(ShipmentProduct))

    override fun type(typeFactory: CourierTypeFactory): Int = typeFactory.type(this)

    data class ShipmentProduct(
            @SerializedName("isAvailable")
            @Expose
            val isAvailable: Int = 0,

            @SerializedName("shipProdID")
            @Expose
            val shipProdID: String = "",

            @SerializedName("productName")
            @Expose
            val name: String = "",

            @SerializedName("uiHidden")
            @Expose
            val uiHidden: Boolean = true
    ): Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString(),
                parcel.readString(),
                parcel.readByte() != 0.toByte())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(isAvailable)
            parcel.writeString(shipProdID)
            parcel.writeString(name)
            parcel.writeByte(if (uiHidden) 1 else 0)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<ShipmentProduct> {
            override fun createFromParcel(parcel: Parcel): ShipmentProduct {
                return ShipmentProduct(parcel)
            }

            override fun newArray(size: Int): Array<ShipmentProduct?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(isAvailable)
        parcel.writeString(code)
        parcel.writeString(shipmentID)
        parcel.writeString(image)
        parcel.writeString(name)
        parcel.writeInt(isPickup)
        parcel.writeInt(maxAddFee)
        parcel.writeInt(awbStatus)
        parcel.writeTypedList(product)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShopShipment> {
        override fun createFromParcel(parcel: Parcel): ShopShipment {
            return ShopShipment(parcel)
        }

        override fun newArray(size: Int): Array<ShopShipment?> {
            return arrayOfNulls(size)
        }
    }
}