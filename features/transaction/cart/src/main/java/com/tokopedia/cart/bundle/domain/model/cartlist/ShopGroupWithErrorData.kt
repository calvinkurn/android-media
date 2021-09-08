package com.tokopedia.cart.bundle.domain.model.cartlist

import android.os.Parcelable
import com.tokopedia.cart.bundle.data.model.response.shopgroupsimplified.ShopTypeInfo
import com.tokopedia.cart.bundle.view.uimodel.CartItemHolderData
import kotlinx.android.parcel.Parcelize

data class ShopGroupWithErrorData(
        var cartItemHolderDataList: List<CartItemHolderData> = emptyList(),
        var isError: Boolean = false,
        var errorLabel: String = "",
        var similarProductUrl: String? = null,
        var shopName: String = "",
        var shopId: String = "",
        var shopTypeInfoData: ShopTypeInfo = ShopTypeInfo(),
        var cityName: String = "",
        var isFulfillment: Boolean = false,
        var fulfillmentName: String = "",
        var hasPromoList: Boolean = false,
        var cartString: String = "",

        var isWarning: Boolean = false,
        var warningTitle: String? = null,
        var warningDescription: String? = null,
        var isTokoNow: Boolean = false
)