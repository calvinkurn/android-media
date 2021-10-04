package com.tokopedia.cart.old.domain.model.cartlist

import android.os.Parcelable
import com.tokopedia.cart.old.view.uimodel.CartItemHolderData
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopGroupWithErrorData(
        var cartItemHolderDataList: List<CartItemHolderData> = emptyList(),
        var isError: Boolean = false,
        var errorLabel: String = "",
        var similarProductUrl: String? = null,
        var shopName: String = "",
        var shopId: String = "",
        var shopTypeInfoData: ShopTypeInfoData = ShopTypeInfoData(),
        var cityName: String = "",
        var isFulfillment: Boolean = false,
        var fulfillmentName: String = "",
        var hasPromoList: Boolean = false,
        var cartString: String = "",

        var isWarning: Boolean = false,
        var warningTitle: String? = null,
        var warningDescription: String? = null,
        var isTokoNow: Boolean = false
) : Parcelable