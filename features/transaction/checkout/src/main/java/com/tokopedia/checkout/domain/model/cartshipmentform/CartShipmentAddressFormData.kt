package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import com.tokopedia.checkout.view.uimodel.CrossSellModel
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.logisticcart.shipping.model.CodModel
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.PopUpData
import com.tokopedia.purchase_platform.common.feature.promo.view.model.PromoCheckoutErrorDefault
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData
import kotlinx.parcelize.Parcelize

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
        var popup: PopUpData? = null,
        var donation: Donation? = null,
        var crossSell: List<CrossSellModel> = ArrayList(),
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

    val getAvailablePurchaseProtection: ArrayList<String>
        get() {
            val pppList = arrayListOf<String>()
            val addresses = groupAddress
            for (address in addresses) {
                for (groupShop in address.groupShop) {
                    for (product in groupShop.products) {
                        product.purchaseProtectionPlanData.let { ppData ->
                            if (ppData.isProtectionAvailable) {
                                pppList.add("${ppData.protectionTitle} - ${ppData.protectionPricePerProduct} - ${product.productCatId}")
                            }
                        }
                    }
                }
            }
            return pppList
        }

    companion object {
        const val NO_ERROR = 0
        const val ERROR_CODE_TO_OPEN_ADD_NEW_ADDRESS = 3
        const val ERROR_CODE_TO_OPEN_ADDRESS_LIST = 4
    }
}