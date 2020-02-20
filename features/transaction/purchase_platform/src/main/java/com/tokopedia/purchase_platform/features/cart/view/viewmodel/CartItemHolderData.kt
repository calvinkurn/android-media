package com.tokopedia.purchase_platform.features.cart.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData
import java.util.*

/**
 * @author anggaprasetiyo on 18/01/18.
 */

data class CartItemHolderData(
        var cartItemData: CartItemData?,
        var errorFormItemValidationType: Int = 0,
        var errorFormItemValidationMessage: String? = null,
        var isEditableRemark: Boolean = false,
        var isStateRemarkExpanded: Boolean = false,
        var isSelected: Boolean = false

) : Parcelable {

    companion object CREATOR : Parcelable.Creator<CartItemHolderData> {
        val ERROR_FIELD_BETWEEN = 1
        val ERROR_FIELD_MAX_CHAR = 2
        val ERROR_FIELD_REQUIRED = 3
        val ERROR_FIELD_AVAILABLE_STOCK = 4
        val ERROR_PRODUCT_MAX_QUANTITY = 5
        val ERROR_PRODUCT_MIN_QUANTITY = 6
        val ERROR_EMPTY = 0

        override fun createFromParcel(parcel: Parcel): CartItemHolderData {
            return CartItemHolderData(parcel)
        }

        override fun newArray(size: Int): Array<CartItemHolderData?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readParcelable(CartItemData::class.java.classLoader),
            parcel.readInt(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte()) {
    }

    fun getErrorFormItemValidationTypeValue(): Int {
        if (cartItemData?.updatedData?.remark?.length ?: 0 > cartItemData?.updatedData?.maxCharRemark ?: 0) {
            this.errorFormItemValidationMessage = cartItemData?.messageErrorData?.errorFieldMaxChar
                    ?.replace("{{value}}", cartItemData?.updatedData?.maxCharRemark.toString())
            return ERROR_FIELD_MAX_CHAR
        } else if (cartItemData?.updatedData?.quantity ?: 0 > cartItemData?.originData?.maxOrder ?: 0) {
            val formattedMaxCharRemark = String.format(Locale.US, "%,d", cartItemData?.originData?.maxOrder).replace(',', '.')
            this.errorFormItemValidationMessage = cartItemData?.messageErrorData?.errorProductMaxQuantity
                    ?.replace("{{value}}", formattedMaxCharRemark)
            return ERROR_PRODUCT_MAX_QUANTITY
        } else if (cartItemData?.updatedData?.quantity ?: 0 < cartItemData?.originData?.minOrder ?: 0) {
            this.errorFormItemValidationMessage = cartItemData?.messageErrorData?.errorProductMinQuantity
                    ?.replace("{{value}}", cartItemData?.originData?.minOrder.toString())
            return ERROR_PRODUCT_MIN_QUANTITY
        } else {
            this.errorFormItemValidationMessage = ""
            return ERROR_EMPTY
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(cartItemData, flags)
        parcel.writeInt(errorFormItemValidationType)
        parcel.writeString(errorFormItemValidationMessage)
        parcel.writeByte(if (isEditableRemark) 1 else 0)
        parcel.writeByte(if (isStateRemarkExpanded) 1 else 0)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

}
