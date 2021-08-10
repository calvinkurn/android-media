package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import kotlinx.android.parcel.Parcelize

/**
 * Created by Irfan Khoirul on 08/01/19.
 */
@Parcelize
class ShippingParam(): Parcelable {
    var originDistrictId: String = ""
    var originPostalCode: String = ""
    var originLatitude: String? = null
    var originLongitude: String? = null
    var destinationDistrictId: String = ""
    var destinationPostalCode: String = ""
    var destinationLatitude: String? = null
    var destinationLongitude: String? = null
    var weightInKilograms: Double = 0.0
    var weightActualInKilograms: Double = 0.0
    var shopId: String = ""
    var token: String = ""
    var ut: String = ""
    var insurance: Int = 0
    var productInsurance: Int = 0
    var orderValue: Long = 0
    var categoryIds: String = ""
    var isBlackbox: Boolean = false
    var addressId: String? = null
    var isPreorder: Boolean = false
    var isTradein: Boolean = false
    var isTradeInDropOff: Boolean = false
    var products: List<Product>? = null
    var uniqueId: String = "" // this is actually cart string
    var isFulfillment: Boolean = false
    var preOrderDuration: Int = 0
    var shopTier: Int = 0
    var boMetadata: BoMetadata? = null
}