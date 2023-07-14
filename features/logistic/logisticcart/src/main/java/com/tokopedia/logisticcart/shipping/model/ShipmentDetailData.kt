package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 25/01/18.
 */
@Parcelize
class ShipmentDetailData(
    var totalQuantity: Int = 0,
    var shipmentCartData: ShipmentCartData? = null,
    var selectedCourier: CourierItemData? = null,
    var selectedCourierTradeInDropOff: CourierItemData? = null,
    var useInsurance: Boolean? = null,
    var useDropshipper: Boolean? = null,
    var dropshipperName: String? = null,
    var dropshipperPhone: String? = null,
    var isDropshipperNameValid: Boolean = false,
    var isDropshipperPhoneValid: Boolean = false,
    var isCourierPromoApplied: Boolean = false,
    var shopId: String? = null,
    var shippingCourierViewModels: List<ShippingCourierUiModel>? = null,
    var isBlackbox: Boolean = false,
    var addressId: String? = null,
    var preorder: Boolean = false,
    var isTradein: Boolean = false,
    var isOrderPriority: Boolean? = null
) : Parcelable
