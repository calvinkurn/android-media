package com.tokopedia.shop.product.view.datamodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef.ETALASE_DEFAULT

/**
 * Created by normansyahputa on 2/28/18.
 */

data class ShopEtalaseItemDataModel(
        val etalaseId: String = "",
        val alias: String = "",
        val etalaseName: String = "",
        @ShopEtalaseTypeDef val type: Int = ETALASE_DEFAULT,
        val etalaseBadge: String = "",
        val etalaseCount: Long = 0,
        val highlighted: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readLong(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(etalaseId)
        parcel.writeString(alias)
        parcel.writeString(etalaseName)
        parcel.writeInt(type)
        parcel.writeString(etalaseBadge)
        parcel.writeLong(etalaseCount)
        parcel.writeByte(if (highlighted) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShopEtalaseItemDataModel> {
        override fun createFromParcel(parcel: Parcel): ShopEtalaseItemDataModel {
            return ShopEtalaseItemDataModel(parcel)
        }

        override fun newArray(size: Int): Array<ShopEtalaseItemDataModel?> {
            return arrayOfNulls(size)
        }
    }
}
