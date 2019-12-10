package com.tokopedia.purchase_platform.features.cart.domain.model.cartlist

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.VoucherOrdersItemData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartItemHolderData
import java.util.*

/**
 * Created by Irfan Khoirul on 21/08/18.
 */

data class ShopGroupAvailableData(

        var cartItemHolderDataList: MutableList<CartItemHolderData>? = ArrayList(),
        var isChecked: Boolean = false,
        var isError: Boolean = false,
        var errorTitle: String? = null,
        var errorDescription: String? = null,
        var similarProductUrl: String? = null,
        var isWarning: Boolean = false,
        var warningTitle: String? = null,
        var warningDescription: String? = null,
        var shopName: String? = null,
        var shopId: String? = null,
        var shopType: String? = null,
        var isGoldMerchant: Boolean = false,
        var isOfficialStore: Boolean = false,
        var shopBadge: String? = null,
        var isFulfillment: Boolean = false,
        var fulfillmentName: String? = null,
        var isHasPromoList: Boolean = false,
        var cartString: String? = null,
        var voucherOrdersItemData: VoucherOrdersItemData? = null,

        // Total data which is calculated from cartItemDataList
        var totalPrice: Long = 0,
        var totalCashback: Long = 0,
        var totalItem: Int = 0

) : Parcelable {

    val cartItemDataList: List<CartItemHolderData>?
        get() = cartItemHolderDataList

    fun setCartItemDataList(cartItemDataList: List<CartItemData>) {
        for (cartItemData in cartItemDataList) {
            val cartItemHolderData = CartItemHolderData(
                    cartItemData = cartItemData,
                    isEditableRemark = false,
                    errorFormItemValidationMessage = "",
                    isSelected = if (cartItemData.isError) {
                        false
                    } else {
                        cartItemData.originData?.isCheckboxState ?: false
                    }
            )
            cartItemHolderDataList?.add(cartItemHolderData)
        }
    }

    constructor(parcel: Parcel) : this() {
        isChecked = parcel.readByte() != 0.toByte()
        isError = parcel.readByte() != 0.toByte()
        errorTitle = parcel.readString()
        errorDescription = parcel.readString()
        similarProductUrl = parcel.readString()
        isWarning = parcel.readByte() != 0.toByte()
        warningTitle = parcel.readString()
        warningDescription = parcel.readString()
        shopName = parcel.readString()
        shopId = parcel.readString()
        shopType = parcel.readString()
        isGoldMerchant = parcel.readByte() != 0.toByte()
        isOfficialStore = parcel.readByte() != 0.toByte()
        shopBadge = parcel.readString()
        isFulfillment = parcel.readByte() != 0.toByte()
        fulfillmentName = parcel.readString()
        isHasPromoList = parcel.readByte() != 0.toByte()
        cartString = parcel.readString()
        voucherOrdersItemData = parcel.readParcelable(VoucherOrdersItemData::class.java.classLoader)
        totalPrice = parcel.readLong()
        totalCashback = parcel.readLong()
        totalItem = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isChecked) 1 else 0)
        parcel.writeByte(if (isError) 1 else 0)
        parcel.writeString(errorTitle)
        parcel.writeString(errorDescription)
        parcel.writeString(similarProductUrl)
        parcel.writeByte(if (isWarning) 1 else 0)
        parcel.writeString(warningTitle)
        parcel.writeString(warningDescription)
        parcel.writeString(shopName)
        parcel.writeString(shopId)
        parcel.writeString(shopType)
        parcel.writeByte(if (isGoldMerchant) 1 else 0)
        parcel.writeByte(if (isOfficialStore) 1 else 0)
        parcel.writeString(shopBadge)
        parcel.writeByte(if (isFulfillment) 1 else 0)
        parcel.writeString(fulfillmentName)
        parcel.writeByte(if (isHasPromoList) 1 else 0)
        parcel.writeString(cartString)
        parcel.writeParcelable(voucherOrdersItemData, flags)
        parcel.writeLong(totalPrice)
        parcel.writeLong(totalCashback)
        parcel.writeInt(totalItem)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShopGroupAvailableData> {
        override fun createFromParcel(parcel: Parcel): ShopGroupAvailableData {
            return ShopGroupAvailableData(parcel)
        }

        override fun newArray(size: Int): Array<ShopGroupAvailableData?> {
            return arrayOfNulls(size)
        }
    }

}
