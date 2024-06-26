package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import com.tokopedia.checkout.view.uimodel.CrossSellModel
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.logisticcart.shipping.model.CodModel
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnWordingData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.PopUpData
import com.tokopedia.purchase_platform.common.feature.promo.view.model.PromoCheckoutErrorDefault
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartShipmentAddressFormData(
    var isHasError: Boolean = false,
    var isError: Boolean = false,
    var errorMessage: String = "",
    var errorCode: Int = 0,
    var isShowOnboarding: Boolean = false,
    var isDropshipperDisable: Boolean = false,
    var isOrderPrioritasDisable: Boolean = false,
    var groupAddress: List<GroupAddress> = ArrayList(),
    var keroToken: String = "",
    var keroDiscomToken: String = "",
    var keroUnixTime: Int = 0,
    var popup: PopUpData = PopUpData(),
    var addOnWording: AddOnWordingData = AddOnWordingData(),
    var donation: Donation? = null,
    var crossSell: List<CrossSellModel> = ArrayList(),
    var cod: CodModel = CodModel(),
    var isHidingCourier: Boolean = false,
    var isBlackbox: Boolean = false,
    var egoldAttributes: EgoldAttributeModel? = null,
    var isIneligiblePromoDialogEnabled: Boolean = false,
    var tickerData: TickerData? = null,
    var addressesData: AddressesData = AddressesData(),
    var campaignTimerUi: CampaignTimerUi = CampaignTimerUi(),
    var lastApplyData: LastApplyUiModel = LastApplyUiModel(),
    var promoCheckoutErrorDefault: PromoCheckoutErrorDefault = PromoCheckoutErrorDefault(),
    var isOpenPrerequisiteSite: Boolean = false,
    var isEligibleNewShippingExperience: Boolean = false,
    var popUpMessage: String = "",
    var errorTicker: String = "",
    var epharmacyData: EpharmacyData = EpharmacyData(),
    var upsell: UpsellData = UpsellData(),
    var newUpsell: NewUpsellData = NewUpsellData(),
    var cartData: String = "",
    var isUsingDdp: Boolean = false,
    var dynamicData: String = "",
    var coachmarkPlus: CheckoutCoachmarkPlusData = CheckoutCoachmarkPlusData(),
    var shipmentPlatformFee: ShipmentPlatformFeeData = ShipmentPlatformFeeData(),
    var listSummaryAddons: List<ShipmentSummaryAddOnData> = emptyList(),
    var paymentLevelAddOnsPositions: List<Long> = emptyList(),
    var additionalFeature: AdditionalFeature = AdditionalFeature(),
    var paymentWidget: PaymentWidget = PaymentWidget(),
    var cartType: String = "",
    var terms: String = ""
) : Parcelable {

    val getAvailablePurchaseProtection: ArrayList<String>
        get() {
            val pppList = arrayListOf<String>()
            val addresses = groupAddress
            for (address in addresses) {
                for (groupShop in address.groupShop) {
                    for (groupShopV2 in groupShop.groupShopData) {
                        for (product in groupShopV2.products) {
                            product.purchaseProtectionPlanData.let { ppData ->
                                if (ppData.isProtectionAvailable) {
                                    pppList.add("${ppData.protectionTitle} - ${ppData.protectionPricePerProduct} - ${product.productCatId}")
                                }
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

        const val CART_TYPE_OCC = "occ"
        const val CART_TYPE_NORMAL = "normal"
    }
}
