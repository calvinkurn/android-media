package com.tokopedia.shop.settings.address.data

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.settings.address.view.adapter.ShopLocationTypeFactory

data class ShopLocationViewModel(
        val id: String = "",
        val name: String = "",
        val address: String = "",
        val districtId: Int = -1,
        val districtName: String = "",
        val cityId: Int = -1,
        val cityName: String = "",
        val stateId: Int = -1,
        val stateName: String = "",
        val postalCode: Int = -1,
        val email: String = "",
        val phone: String = "",
        val fax: String = ""
): Visitable<ShopLocationTypeFactory>, Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun type(typeFactory: ShopLocationTypeFactory) = typeFactory.type(this)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeInt(districtId)
        parcel.writeString(districtName)
        parcel.writeInt(cityId)
        parcel.writeString(cityName)
        parcel.writeInt(stateId)
        parcel.writeString(stateName)
        parcel.writeInt(postalCode)
        parcel.writeString(email)
        parcel.writeString(phone)
        parcel.writeString(fax)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShopLocationViewModel> {
        override fun createFromParcel(parcel: Parcel): ShopLocationViewModel {
            return ShopLocationViewModel(parcel)
        }

        override fun newArray(size: Int): Array<ShopLocationViewModel?> {
            return arrayOfNulls(size)
        }
    }
}