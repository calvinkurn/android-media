package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnsDataModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupShop(
    var isError: Boolean = false,
    var errorMessage: String = "",
    var hasUnblockingError: Boolean = false,
    var unblockingErrorMessage: String = "",
    var firstProductErrorIndex: Int = -1,
    var addOns: AddOnsDataModel = AddOnsDataModel(),
    var groupShopData: List<GroupShopV2> = emptyList(),
    var shopShipments: List<ShopShipment> = emptyList(),
    var shippingId: Int = 0,
    var spId: Int = 0,
    var boCode: String = "",
    var boUniqueId: String = "",
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
    var autoCourierSelection: Boolean = false,
    var boMetadata: BoMetadata = BoMetadata(),
    var courierSelectionErrorData: CourierSelectionErrorData = CourierSelectionErrorData(),
    var scheduleDelivery: ScheduleDeliveryData = ScheduleDeliveryData(),
    var ratesValidationFlow: Boolean = false,

    // new
    val groupType: Int = 0,
    val uiGroupType: Int = 0,
    val groupInfoName: String = "",
    val groupInfoBadgeUrl: String = "",
    val groupInfoDescription: String = "",
    val groupInfoDescriptionBadgeUrl: String = ""
) : Parcelable {

    companion object {
        const val GROUP_TYPE_NORMAL = 1
        const val GROUP_TYPE_OWOC = 2

        const val UI_GROUP_TYPE_NORMAL = 0
        const val UI_GROUP_TYPE_OWOC = 1
    }
}
