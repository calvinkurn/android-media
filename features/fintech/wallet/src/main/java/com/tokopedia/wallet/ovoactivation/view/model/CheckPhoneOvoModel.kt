package com.tokopedia.wallet.ovoactivation.view.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by nabillasabbaha on 24/09/18.
 */
class CheckPhoneOvoModel(
        var phoneNumber: String = "",
        var isRegistered: Boolean = false,
        var registeredApplink: String = "",
        var notRegisteredApplink: String = "",
        var changeMsisdnApplink: String = "",
        var isAllow: Boolean = false,
        var phoneActionModel: PhoneActionModel? = null,
        var errorModel: ErrorModel? = null)
    : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readParcelable(PhoneActionModel::class.java.classLoader),
            parcel.readParcelable(ErrorModel::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(phoneNumber)
        parcel.writeByte(if (isRegistered) 1 else 0)
        parcel.writeString(registeredApplink)
        parcel.writeString(notRegisteredApplink)
        parcel.writeString(changeMsisdnApplink)
        parcel.writeByte(if (isAllow) 1 else 0)
        parcel.writeParcelable(phoneActionModel, flags)
        parcel.writeParcelable(errorModel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CheckPhoneOvoModel> {
        override fun createFromParcel(parcel: Parcel): CheckPhoneOvoModel {
            return CheckPhoneOvoModel(parcel)
        }

        override fun newArray(size: Int): Array<CheckPhoneOvoModel?> {
            return arrayOfNulls(size)
        }
    }
}
