package com.tokopedia.cart.old.view.uimodel

import android.os.Parcelable
import com.tokopedia.cart.old.domain.model.cartlist.CartTickerErrorData
import kotlinx.parcelize.Parcelize

/**
 * @author anggaprasetiyo on 02/03/18.
 */

@Parcelize
data class CartItemTickerErrorHolderData(
        var cartTickerErrorData: CartTickerErrorData = CartTickerErrorData()
) : Parcelable
