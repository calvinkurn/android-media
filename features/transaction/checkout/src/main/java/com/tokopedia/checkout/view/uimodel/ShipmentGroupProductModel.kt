package com.tokopedia.checkout.view.uimodel

import android.os.Parcelable
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnWordingModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShipmentGroupProductModel(
    val shipmentCartItem: ShipmentCartItemModel,
    val cartItemPosition: Int,
) : Parcelable
