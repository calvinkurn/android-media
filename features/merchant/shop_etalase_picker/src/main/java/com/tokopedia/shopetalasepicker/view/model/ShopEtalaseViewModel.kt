package com.tokopedia.shopetalasepicker.view.model

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shopetalasepicker.view.adapter.ShopEtalaseAdapterTypeFactory

/**
 * Created by normansyahputa on 2/28/18.
 */

class ShopEtalaseViewModel() : Visitable<ShopEtalaseAdapterTypeFactory>, Parcelable {

    var isUseAce: Boolean = false
    var etalaseId: String? = null
    var etalaseName: String? = null
        get() {
            if (field == null) {
                return ""
            }; return field
        }
    var etalaseCount: Long = 0
    var etalaseBadge: String? = null
        get() {
            if (field == null) {
                return ""
            }; return field
        }
    @ShopEtalaseTypeDef
    var type: Int = 0
    var isHighlight: Boolean = false

    var isSelected: Boolean = false

    val isCustomType: Boolean
        get() = type == ShopEtalaseTypeDef.ETALASE_CUSTOM

    constructor(parcel: Parcel) : this() {
        isUseAce = parcel.readByte() != 0.toByte()
        etalaseId = parcel.readString()
        etalaseName = parcel.readString()
        etalaseCount = parcel.readLong()
        etalaseBadge = parcel.readString()
        type = parcel.readInt()
        isHighlight = parcel.readByte() != 0.toByte()
        isSelected = parcel.readByte() != 0.toByte()
    }

    constructor(shopEtalaseModel: ShopEtalaseModel) : this() {
        etalaseBadge = shopEtalaseModel.badge
        val idOrAlias: String
        if (shopEtalaseModel.type == ShopEtalaseTypeDef.ETALASE_DEFAULT) {
            idOrAlias = shopEtalaseModel.alias
        } else {
            idOrAlias = shopEtalaseModel.id
        }
        etalaseId = idOrAlias
        etalaseName = shopEtalaseModel.name
        etalaseCount = shopEtalaseModel.count.toLong()
        isUseAce = shopEtalaseModel.useAce
        type = shopEtalaseModel.type
        isHighlight = shopEtalaseModel.highlighted
    }

    override fun type(shopEtalaseAdapterTypeFactory: ShopEtalaseAdapterTypeFactory): Int {
        return shopEtalaseAdapterTypeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isUseAce) 1 else 0)
        parcel.writeString(etalaseId)
        parcel.writeString(etalaseName)
        parcel.writeLong(etalaseCount)
        parcel.writeString(etalaseBadge)
        parcel.writeInt(type)
        parcel.writeByte(if (isHighlight) 1 else 0)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShopEtalaseViewModel> {
        override fun createFromParcel(parcel: Parcel): ShopEtalaseViewModel {
            return ShopEtalaseViewModel(parcel)
        }

        override fun newArray(size: Int): Array<ShopEtalaseViewModel?> {
            return arrayOfNulls(size)
        }
    }


}
