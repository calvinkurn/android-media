package com.tokopedia.cart.domain.model.cartlist

import android.os.Parcelable
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ShopGroupAvailableData(

        var cartItemHolderDataList: MutableList<CartItemHolderData> = ArrayList(),
        var isChecked: Boolean = false,
        var errorTitle: String = "",
        var errorDescription: String = "",
        var isWarning: Boolean = false,
        var warningTitle: String = "",
        var warningDescription: String = "",
        var shopName: String = "",
        var shopId: String = "",
        var shopTypeInfo: ShopTypeInfoData = ShopTypeInfoData(),
        var isFulfillment: Boolean = false,
        var fulfillmentName: String = "",
        var fulfillmentBadgeUrl: String = "",
        var isHasPromoList: Boolean = false,
        var cartString: String = "",
        var promoCodes: List<String> = emptyList(),

        // Total data which is calculated from cartItemDataList
        var totalPrice: Long = 0,
        var totalCashback: Long = 0,
        var totalItem: Int = 0,
        var totalWeight: Double = 0.0,

        var preOrderInfo: String = "",
        var isFreeShippingExtra: Boolean = false,
        var freeShippingBadgeUrl: String = "",
        var incidentInfo: String = "",
        var estimatedTimeArrival: String = "",

        var shopTicker: String = "",
        var maximumWeightWording: String = "",
        var maximumShippingWeight: Double = 0.0,
        var isTokoNow: Boolean = false
) : Parcelable {

    val cartItemDataList: MutableList<CartItemHolderData>
        get() = cartItemHolderDataList

    val shouldValidateWeight: Boolean
        get() = maximumShippingWeight > 0.0 && maximumWeightWording.isNotEmpty()

    companion object {
        const val MAXIMUM_WEIGHT_WORDING_REPLACE_KEY = "{{weight}}"
    }
}
