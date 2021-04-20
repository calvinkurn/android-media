package com.tokopedia.digital.newcart.presentation.model.cart

import android.os.Parcel
import android.os.Parcelable

/**
 * @author anggaprasetiyo on 2/27/17.
 */

class CartDigitalInfoData(
        var type: String? = null,

        var id: String? = null,

        var attributes: AttributesDigital? = null,

        var title: String? = null,

        var isInstantCheckout: Boolean = false,

        var isNeedOtp: Boolean = false,

        var smsState: String? = null,

        var mainInfo: List<CartItemDigital>? = null,

        var additionalInfos: List<CartAdditionalInfo>? = null,

        var relationships: Relationships? = null,

        var isForceRenderCart: Boolean = false,

        var crossSellingType: Int = 0,

        var crossSellingConfig: CrossSellingConfig? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(AttributesDigital::class.java.classLoader),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.createTypedArrayList(CartItemDigital),
            parcel.createTypedArrayList(CartAdditionalInfo),
            parcel.readParcelable(Relationships::class.java.classLoader),
            parcel.readByte() != 0.toByte(),
            parcel.readInt(),
            parcel.readParcelable(CrossSellingConfig::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(id)
        parcel.writeParcelable(attributes, flags)
        parcel.writeString(title)
        parcel.writeByte(if (isInstantCheckout) 1 else 0)
        parcel.writeByte(if (isNeedOtp) 1 else 0)
        parcel.writeString(smsState)
        parcel.writeTypedList(mainInfo)
        parcel.writeTypedList(additionalInfos)
        parcel.writeParcelable(relationships, flags)
        parcel.writeByte(if (isForceRenderCart) 1 else 0)
        parcel.writeInt(crossSellingType)
        parcel.writeParcelable(crossSellingConfig, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartDigitalInfoData> {
        override fun createFromParcel(parcel: Parcel): CartDigitalInfoData {
            return CartDigitalInfoData(parcel)
        }

        override fun newArray(size: Int): Array<CartDigitalInfoData?> {
            return arrayOfNulls(size)
        }
    }


}
