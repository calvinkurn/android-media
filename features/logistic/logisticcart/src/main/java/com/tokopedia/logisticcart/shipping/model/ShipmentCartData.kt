package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 22/02/18.
 */
@Parcelize
class ShipmentCartData(
    val shopShipments: List<ShopShipment> = emptyList(),
    val originDistrictId: String = "",
    val originPostalCode: String = "",
    val originLatitude: String? = null,
    val originLongitude: String? = null,
    var destinationDistrictId: String = "",
    var destinationPostalCode: String? = null,
    var destinationLatitude: String? = null,
    var destinationLongitude: String? = null,
    var destinationAddress: String = "",
    val weight: Double = 0.0,
    val weightActual: Double = 0.0,
    val token: String = "",
    val ut: String = "",
    val insurance: Int = 0,
    val productInsurance: Int = 0,
    val orderValue: Long = 0,
    val categoryIds: String = "",
    val preOrderDuration: Int = 0,
    val isFulfillment: Boolean = false,
    val shopTier: Int = 0,
    val boMetadata: BoMetadata = BoMetadata(),
    val groupType: Int = 0
) : Parcelable
