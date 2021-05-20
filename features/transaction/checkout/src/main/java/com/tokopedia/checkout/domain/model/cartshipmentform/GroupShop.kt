package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GroupShop(
        var isError: Boolean = false,
        var errorMessage: String = "",
        var shop: Shop = Shop(),
        var shopShipments: List<ShopShipment> = emptyList(),
        var products: List<Product> = emptyList(),
        var shippingId: Int = 0,
        var spId: Int = 0,
        var dropshipperName: String = "",
        var dropshipperPhone: String = "",
        var isUseInsurance: Boolean = false,
        var cartString: String = "",
        var isHasPromoList: Boolean = false,
        var isSaveStateFlag: Boolean = false,
        var isFulfillment: Boolean = false,
        var fulfillmentId: Long = 0,
        var fulfillmentName: String = "",
        var fulfillmentBadgeUrl: String = "",
        var isLeasingProduct: Boolean = false,
        var bookingFee: Int = 0,
        var listPromoCodes: List<String> = emptyList(),
        var shipmentInformationData: ShipmentInformationData = ShipmentInformationData(),
        var isDisableChangeCourier: Boolean = false,
        var boMetadata: BoMetadata = BoMetadata()
) : Parcelable