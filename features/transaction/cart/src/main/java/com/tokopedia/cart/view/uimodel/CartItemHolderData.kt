package com.tokopedia.cart.view.uimodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.cart.domain.model.cartlist.ActionData
import com.tokopedia.cart.domain.model.cartlist.CartItemData
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * @author anggaprasetiyo on 18/01/18.
 */

@Parcelize
data class CartItemHolderData(
        var cartItemData: CartItemData?,
        var errorFormItemValidationType: Int = 0,
        var errorFormItemValidationMessage: String? = null,
        var isEditableRemark: Boolean = false,
        var isStateHasNotes: Boolean = false,
        var isStateNotesOnFocuss: Boolean = false,
        var isSelected: Boolean = false,
        var actionsData: List<ActionData> = emptyList(),
        var errorType: String = ""
) : Parcelable {

    companion object {
        val ERROR_FIELD_BETWEEN = 1
        val ERROR_FIELD_MAX_CHAR = 2
        val ERROR_FIELD_REQUIRED = 3
        val ERROR_FIELD_AVAILABLE_STOCK = 4
        val ERROR_PRODUCT_MAX_QUANTITY = 5
        val ERROR_PRODUCT_MIN_QUANTITY = 6
        val ERROR_EMPTY = 0
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

}
