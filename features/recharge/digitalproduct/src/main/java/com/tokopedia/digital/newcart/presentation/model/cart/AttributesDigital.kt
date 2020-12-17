package com.tokopedia.digital.newcart.presentation.model.cart

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by Nabilla Sabbaha on 3/1/2017.
 */

class AttributesDigital() : Parcelable {

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

    var fintechProduct: List<FintechProduct>? = null

    constructor(parcel: Parcel) : this() {
        userId = parcel.readString()
        clientNumber = parcel.readString()
        icon = parcel.readString()
        price = parcel.readString()
        categoryName = parcel.readString()
        operatorName = parcel.readString()
        pricePlain = parcel.readLong()
        isInstantCheckout = parcel.readByte() != 0.toByte()
        isNeedOtp = parcel.readByte() != 0.toByte()
        smsState = parcel.readString()
        isEnableVoucher = parcel.readByte() != 0.toByte()
        voucherAutoCode = parcel.readString()
        isCouponActive = parcel.readInt()
        userInputPrice = parcel.readParcelable(UserInputPriceDigital::class.java.classLoader)
        autoApplyVoucher = parcel.readParcelable(CartAutoApplyVoucher::class.java.classLoader)
        defaultPromoTab = parcel.readString()
        postPaidPopupAttribute = parcel.readParcelable(PostPaidPopupAttribute::class.java.classLoader)
        fintechProduct = parcel.createTypedArrayList(FintechProduct)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(clientNumber)
        parcel.writeString(icon)
        parcel.writeString(price)
        parcel.writeString(categoryName)
        parcel.writeString(operatorName)
        parcel.writeLong(pricePlain)
        parcel.writeByte(if (isInstantCheckout) 1 else 0)
        parcel.writeByte(if (isNeedOtp) 1 else 0)
        parcel.writeString(smsState)
        parcel.writeByte(if (isEnableVoucher) 1 else 0)
        parcel.writeString(voucherAutoCode)
        parcel.writeInt(isCouponActive)
        parcel.writeParcelable(userInputPrice, flags)
        parcel.writeParcelable(autoApplyVoucher, flags)
        parcel.writeString(defaultPromoTab)
        parcel.writeParcelable(postPaidPopupAttribute, flags)
        parcel.writeTypedList(fintechProduct)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AttributesDigital> {
        override fun createFromParcel(parcel: Parcel): AttributesDigital {
            return AttributesDigital(parcel)
        }

        override fun newArray(size: Int): Array<AttributesDigital?> {
            return arrayOfNulls(size)
        }
    }

}
