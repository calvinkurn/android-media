package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.logisticcart.shipping.model.CodModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.PromoCheckoutErrorDefault
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartShipmentAddressFormData(
        var isHasError: Boolean = false,
        var isError: Boolean = false,
        var errorMessage: String? = null,
        var errorCode: Int = 0,
        var isShowOnboarding: Boolean = false,
        var isDropshipperDisable: Boolean = false,
        var isOrderPrioritasDisable: Boolean = false,
        var groupAddress: List<GroupAddress> = ArrayList(),
        var keroToken: String? = null,
        var keroDiscomToken: String? = null,
        var keroUnixTime: Int = 0,
        var donation: Donation? = null,
        var cod: CodModel? = null,
        var isHidingCourier: Boolean = false,
        var isBlackbox: Boolean = false,
        var egoldAttributes: EgoldAttributeModel? = null,
        var isIneligiblePromoDialogEnabled: Boolean = false,
        var tickerData: TickerData? = null,
        var addressesData: AddressesData? = null,
        var campaignTimerUi: CampaignTimerUi = CampaignTimerUi(),
        var lastApplyData: LastApplyUiModel = LastApplyUiModel(),
        var promoCheckoutErrorDefault: PromoCheckoutErrorDefault = PromoCheckoutErrorDefault(),
        var isOpenPrerequisiteSite: Boolean = false,
        var isEligibleNewShippingExperience: Boolean = false,
        var popUpMessage: String = "",
        var errorTicker: String = ""
) : Parcelable {

    val isAvailablePurchaseProtection: Boolean
        get() {
            val addresses = groupAddress
            for (address in addresses) {
                for (groupShop in address.groupShop) {
                    for (product in groupShop.products) {
                        if (product.purchaseProtectionPlanData.isProtectionAvailable) {
                            return true
                        }
                    }
                }
            }
            return false
        }

    companion object {
        const val NO_ERROR = 0
        const val ERROR_CODE_TO_OPEN_ADD_NEW_ADDRESS = 3
        const val ERROR_CODE_TO_OPEN_ADDRESS_LIST = 4
    }
}