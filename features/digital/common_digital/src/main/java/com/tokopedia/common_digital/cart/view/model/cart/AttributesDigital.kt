package com.tokopedia.common_digital.cart.view.model.cart

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.common_digital.cart.domain.model.PostPaidPopupAttribute

/**
 * @author by Nabilla Sabbaha on 3/1/2017.
 */

class AttributesDigital : Parcelable {

    var userId: String? = null

    var clientNumber: String? = null

    var icon: String? = null

    var price: String? = null

    var categoryName: String? = null

    var operatorName: String? = null

    var pricePlain: Long = 0

    var isInstantCheckout: Boolean = false

    var isNeedOtp: Boolean = false

    var smsState: String? = null

    var isEnableVoucher: Boolean = false

    var voucherAutoCode: String? = null

    var isCouponActive: Int = 0

    var userInputPrice: UserInputPriceDigital? = null

    var autoApplyVoucher: CartAutoApplyVoucher? = null

    var defaultPromoTab: String? = null

    var postPaidPopupAttribute: PostPaidPopupAttribute? = null

    protected constructor(`in`: Parcel) {
        userId = `in`.readString()
        clientNumber = `in`.readString()
        icon = `in`.readString()
        price = `in`.readString()
        categoryName = `in`.readString()
        operatorName = `in`.readString()
        pricePlain = `in`.readLong()
        isInstantCheckout = `in`.readByte().toInt() != 0
        isNeedOtp = `in`.readByte().toInt() != 0
        smsState = `in`.readString()
        isEnableVoucher = `in`.readByte().toInt() != 0
        voucherAutoCode = `in`.readString()
        isCouponActive = `in`.readInt()
        userInputPrice = `in`.readParcelable(UserInputPriceDigital::class.java.classLoader)
        autoApplyVoucher = `in`.readParcelable(CartAutoApplyVoucher::class.java.classLoader)
        defaultPromoTab = `in`.readString()
        postPaidPopupAttribute = `in`.readParcelable(PostPaidPopupAttribute::class.java.classLoader)
    }

    constructor() {}

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(userId)
        parcel.writeString(clientNumber)
        parcel.writeString(icon)
        parcel.writeString(price)
        parcel.writeString(categoryName)
        parcel.writeString(operatorName)
        parcel.writeLong(pricePlain)
        parcel.writeByte((if (isInstantCheckout) 1 else 0).toByte())
        parcel.writeByte((if (isNeedOtp) 1 else 0).toByte())
        parcel.writeString(smsState)
        parcel.writeByte((if (isEnableVoucher) 1 else 0).toByte())
        parcel.writeString(voucherAutoCode)
        parcel.writeInt(isCouponActive)
        parcel.writeParcelable(userInputPrice, i)
        parcel.writeParcelable(autoApplyVoucher, i)
        parcel.writeString(defaultPromoTab)
        parcel.writeParcelable(postPaidPopupAttribute, i)
    }

    companion object CREATOR : Parcelable.Creator<AttributesDigital> {
        override fun createFromParcel(`in`: Parcel): AttributesDigital {
            return AttributesDigital(`in`)
        }

        override fun newArray(size: Int): Array<AttributesDigital?> {
            return arrayOfNulls(size)
        }
    }
}
