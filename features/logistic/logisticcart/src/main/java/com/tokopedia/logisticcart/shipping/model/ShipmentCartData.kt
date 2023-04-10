package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 22/02/18.
 */
@Parcelize
class ShipmentCartData(
    var shopShipments: List<ShopShipment>? = null,
    var deliveryPriceTotal: Int = 0,
    var shippingServices: String? = null,
    var shippingNames: String? = null,
    var originDistrictId: String? = null,
    var originPostalCode: String? = null,
    var originLatitude: String? = null,
    var originLongitude: String? = null,
    var destinationDistrictId: String? = null,
    var destinationPostalCode: String? = null,
    var destinationLatitude: String? = null,
    var destinationLongitude: String? = null,
    var destinationAddress: String? = null,
    var weight: Double = 0.0,
    var weightActual: Double = 0.0,
    var token: String? = null,
    var ut: String? = null,
    var insurance: Int = 0,
    var productInsurance: Int = 0,
    var orderValue: Long = 0,
    var categoryIds: String? = null,
    var preOrderDuration: Int = 0,
    var isFulfillment: Boolean = false,
    var shopTier: Int = 0,
    var boMetadata: BoMetadata? = null
) : Parcelable
