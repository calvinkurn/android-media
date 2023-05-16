package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 25/01/18.
 */
@Parcelize
class ShipmentItemData(
    var serviceId: Int = 0,
    var type: String? = null,
    var singlePriceRange: String? = null,
    var multiplePriceRange: String? = null,
    var deliveryTimeRange: String? = null,
    var isLessThanADayDelivery: Boolean = false,
    var courierItemData: List<CourierItemData>? = null,
    var isSelected: Boolean = false
) : Parcelable
