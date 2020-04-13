package com.tokopedia.atc_common.domain.model.response

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Irfan Khoirul on 2019-07-15.
 */

data class DataModel(
        var success: Int = 0,
        var cartId: String = "",
        var productId: Int = 0,
        var quantity: Int = 0,
        var notes: String = "",
        var shopId: Int = 0,
        var customerId: Int = 0,
        var warehouseId: Int = 0,
        var trackerAttribution: String = "",
        var trackerListName: String = "",
        var ucUtParam: String = "",
        var isTradeIn: Boolean = false,
        var message: ArrayList<String> = arrayListOf(),
        var ovoValidationDataModel: OvoValidationDataModel = OvoValidationDataModel(),
        var refreshPrerequisitePage: Boolean = false
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.createStringArrayList(),
            parcel.readParcelable(OvoValidationDataModel::class.java.classLoader),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(success)
        parcel.writeString(cartId)
        parcel.writeInt(productId)
        parcel.writeInt(quantity)
        parcel.writeString(notes)
        parcel.writeInt(shopId)
        parcel.writeInt(customerId)
        parcel.writeInt(warehouseId)
        parcel.writeString(trackerAttribution)
        parcel.writeString(trackerListName)
        parcel.writeString(ucUtParam)
        parcel.writeByte(if (isTradeIn) 1 else 0)
        parcel.writeStringList(message)
        parcel.writeParcelable(ovoValidationDataModel, flags)
        parcel.writeByte(if (refreshPrerequisitePage) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataModel> {
        override fun createFromParcel(parcel: Parcel): DataModel {
            return DataModel(parcel)
        }

        override fun newArray(size: Int): Array<DataModel?> {
            return arrayOfNulls(size)
        }
    }
}