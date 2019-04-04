package com.tokopedia.common_digital.cart.view.model.cart

import android.os.Parcel
import android.os.Parcelable

/**
 * @author anggaprasetiyo on 2/27/17.
 */

class CartDigitalInfoData : Parcelable {

    var type: String? = null

    var id: String? = null

    var attributes: AttributesDigital? = null

    var title: String? = null

    var isInstantCheckout: Boolean = false

    var isNeedOtp: Boolean = false

    var smsState: String? = null

    var mainInfo: List<CartItemDigital>? = null

    var additionalInfos: List<CartAdditionalInfo>? = null

    var relationships: Relationships? = null

    var isForceRenderCart: Boolean = false

    var crossSellingType: Int = 0

    var crossSellingConfig: CrossSellingConfig? = null

    protected constructor(`in`: Parcel) {
        type = `in`.readString()
        id = `in`.readString()
        attributes = `in`.readParcelable(AttributesDigital::class.java.classLoader)
        title = `in`.readString()
        isInstantCheckout = `in`.readByte().toInt() != 0
        isNeedOtp = `in`.readByte().toInt() != 0
        smsState = `in`.readString()
        mainInfo = `in`.createTypedArrayList(CartItemDigital.CREATOR)
        additionalInfos = `in`.createTypedArrayList(CartAdditionalInfo.CREATOR)
        relationships = `in`.readParcelable(Relationships::class.java.classLoader)
        isForceRenderCart = `in`.readByte().toInt() != 0
        crossSellingType = `in`.readInt()
        crossSellingConfig = `in`.readParcelable(CrossSellingConfig::class.java.classLoader)
    }

    constructor() {}

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(type)
        dest.writeString(id)
        dest.writeParcelable(attributes, flags)
        dest.writeString(title)
        dest.writeByte((if (isInstantCheckout) 1 else 0).toByte())
        dest.writeByte((if (isNeedOtp) 1 else 0).toByte())
        dest.writeString(smsState)
        dest.writeTypedList(mainInfo)
        dest.writeTypedList(additionalInfos)
        dest.writeParcelable(relationships, flags)
        dest.writeByte((if (isForceRenderCart) 1 else 0).toByte())
        dest.writeInt(crossSellingType)
        dest.writeParcelable(crossSellingConfig, flags)
    }

    companion object CREATOR : Parcelable.Creator<CartDigitalInfoData> {
        override fun createFromParcel(`in`: Parcel): CartDigitalInfoData {
            return CartDigitalInfoData(`in`)
        }

        override fun newArray(size: Int): Array<CartDigitalInfoData?> {
            return arrayOfNulls(size)
        }

    }
}
