package com.tokopedia.cart.domain.model.cartlist

import android.os.Parcelable
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created by Irfan Khoirul on 21/08/18.
 */

@Parcelize
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
        var fulfillmentBadgeUrl: String = "",
        var isHasPromoList: Boolean = false,
        var cartString: String? = null,
        var promoCodes: List<String>? = emptyList(),

        // Total data which is calculated from cartItemDataList
        var totalPrice: Long = 0,
        var totalCashback: Long = 0,
        var totalItem: Int = 0,

        var preOrderInfo: String = "",
        var isFreeShippingExtra: Boolean = false,
        var freeShippingBadgeUrl: String = "",
        var incidentInfo: String = "",
        var estimatedTimeArrival: String = ""

) : Parcelable {

    val cartItemDataList: MutableList<CartItemHolderData>?
        get() = cartItemHolderDataList

}
