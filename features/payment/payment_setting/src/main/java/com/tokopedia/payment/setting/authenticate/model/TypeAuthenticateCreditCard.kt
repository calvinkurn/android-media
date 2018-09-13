package com.tokopedia.payment.setting.authenticate.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.payment.setting.authenticate.view.adapter.AuthenticateCCAdapterFactory

data class TypeAuthenticateCreditCard(var isSelected: Boolean = false, var description: String = "",
                                      var title : String = "", var stateWhenSelected : Int = 0)
    : Visitable<AuthenticateCCAdapterFactory>, Parcelable{

    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun type(typeFactory: AuthenticateCCAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isSelected) 1 else 0)
        parcel.writeString(description)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TypeAuthenticateCreditCard> {
        override fun createFromParcel(parcel: Parcel): TypeAuthenticateCreditCard {
            return TypeAuthenticateCreditCard(parcel)
        }

        override fun newArray(size: Int): Array<TypeAuthenticateCreditCard?> {
            return arrayOfNulls(size)
        }
    }
}
