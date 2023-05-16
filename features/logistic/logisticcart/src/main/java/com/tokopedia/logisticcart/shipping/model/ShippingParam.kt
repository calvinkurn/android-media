package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 08/01/19.
 */
@Parcelize
class ShippingParam(
    var originDistrictId: String? = null,
    var originPostalCode: String? = null,
    var originLatitude: String? = null,
    var originLongitude: String? = null,
    var destinationDistrictId: String? = null,
    var destinationPostalCode: String? = null,
    var destinationLatitude: String? = null,
    var destinationLongitude: String? = null,
    var weightInKilograms: Double = 0.0,
    var weightActualInKilograms: Double = 0.0,
    var shopId: String? = null,
    var token: String? = null,
    var ut: String? = null,
    var insurance: Int = 0,
    var productInsurance: Int = 0,
    var orderValue: Long = 0,
    var categoryIds: String? = null,
    var isBlackbox: Boolean = false,
    var addressId: String? = null,
    var isPreorder: Boolean = false,
    var isTradein: Boolean = false,
    var isTradeInDropOff: Boolean = false,
    var products: List<Product>? = null,
    var uniqueId: String? = null, // this is actually cart string
    var isFulfillment: Boolean = false,
    var preOrderDuration: Int = 0,
    var shopTier: Int = 0,
    var boMetadata: BoMetadata? = null,
    // new owoc
    var groupType: Int = 0
) : Parcelable
