package com.tokopedia.product.detail.data.model.shop

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.product.detail.view.adapter.CourierTypeFactory
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopShipment

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
data class ProductShopShipment (
        val isAvailable: Int = 0,
        val code: String = "",
        val shipmentID: String = "",
        val image: String = "",
        val name: String = "",
        val isPickup: Int = 0,
        val maxAddFee: Int = 0,
        val awbStatus: Int = 0,
        val product: List<ShopShipment.ShipmentProduct> = listOf()
): BlackBoxShipmentHolder(), Parcelable {

    override fun type(typeFactory: CourierTypeFactory): Int = typeFactory.type(this)

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.createTypedArrayList(ShopShipment.ShipmentProduct.CREATOR)) {
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

    companion object CREATOR : Parcelable.Creator<ProductShopShipment> {
        override fun createFromParcel(parcel: Parcel): ProductShopShipment {
            return ProductShopShipment(parcel)
        }

        override fun newArray(size: Int): Array<ProductShopShipment?> {
            return arrayOfNulls(size)
        }
    }
}