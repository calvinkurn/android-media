package com.tokopedia.cart.view.uimodel

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
        var cartItemData: CartItemData = CartItemData(),
        var isEditableRemark: Boolean = false,
        var isStateHasNotes: Boolean = false,
        var isStateNotesOnFocuss: Boolean = false,
        var isSelected: Boolean = false,
        var actionsData: List<ActionData> = emptyList(),
        var errorType: String = ""
) : Parcelable