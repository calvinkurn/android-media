package com.tokopedia.checkout.view

import android.app.Activity
import android.util.Pair
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.checkout.data.model.request.checkout.old.CheckoutRequest
import com.tokopedia.checkout.data.model.request.checkout.old.DataCheckoutRequest
import com.tokopedia.checkout.domain.model.cartshipmentform.CampaignTimerUi
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.checkout.CheckoutData
import com.tokopedia.checkout.domain.model.checkout.PriceValidationData
import com.tokopedia.checkout.domain.model.checkout.Prompt
import com.tokopedia.checkout.view.helper.ShipmentScheduleDeliveryMapData
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.checkout.view.uimodel.ShipmentButtonPaymentModel
import com.tokopedia.checkout.view.uimodel.ShipmentCostModel
import com.tokopedia.checkout.view.uimodel.ShipmentCrossSellModel
import com.tokopedia.checkout.view.uimodel.ShipmentDonationModel
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.checkout.view.uimodel.ShipmentTickerErrorModel
import com.tokopedia.checkout.view.uimodel.ShipmentUpsellModel
import com.tokopedia.common_epharmacy.network.response.EPharmacyMiniConsultationResult
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticcart.shipping.model.CodModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.PreOrderModel
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnsDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.PopUpData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.SaveAddOnStateResult
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ClashingInfoDetailUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.SummariesItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import rx.subjects.PublishSubject

/**
 * @author Irfan Khoirul on 24/04/18.
 */
interface ShipmentContract {
    interface View : CustomerView {
        fun showInitialLoading()
        fun hideInitialLoading()
        fun showLoading()
        fun hideLoading()
        fun showToastNormal(message: String?)
        fun showToastError(message: String?)
        fun renderErrorPage(message: String?)
        fun onCacheExpired(message: String?)
        fun onShipmentAddressFormEmpty()
        fun renderCheckoutPage(
            isInitialRender: Boolean,
            isReloadAfterPriceChangeHigher: Boolean,
            isOneClickShipment: Boolean
        )

        fun renderCheckoutPageNoAddress(
            cartShipmentAddressFormData: CartShipmentAddressFormData?,
            isEligibleForRevampAna: Boolean
        )

        fun renderCheckoutPageNoMatchedAddress(
            cartShipmentAddressFormData: CartShipmentAddressFormData?,
            addressState: Int
        )

        fun renderDataChanged()
        fun renderCheckoutCartSuccess(checkoutData: CheckoutData?)
        fun renderCheckoutCartError(message: String?)
        fun renderCheckoutPriceUpdated(priceValidationData: PriceValidationData?)
        fun renderPrompt(prompt: Prompt?)
        fun renderPromoCheckoutFromCourierSuccess(
            validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel?,
            itemPosition: Int,
            noToast: Boolean
        )

        fun renderErrorCheckPromoShipmentData(message: String?)
        fun renderEditAddressSuccess(latitude: String?, longitude: String?)
        fun renderChangeAddressSuccess(refreshCheckoutPage: Boolean)
        fun renderChangeAddressFailed(refreshCheckoutPageIfSuccess: Boolean)
        fun renderCourierStateSuccess(
            courierItemData: CourierItemData?,
            itemPosition: Int,
            isTradeInDropOff: Boolean,
            isForceReloadRates: Boolean
        )

        fun renderCourierStateFailed(
            itemPosition: Int,
            isTradeInDropOff: Boolean,
            isBoAutoApplyFlow: Boolean
        )

        fun cancelAllCourierPromo()
        fun updateCourierBottomssheetHasData(
            shippingCourierUiModels: List<ShippingCourierUiModel?>?,
            cartPosition: Int,
            shipmentCartItemModel: ShipmentCartItemModel?,
            preOrderModel: PreOrderModel?
        )

        fun updateCourierBottomsheetHasNoData(
            cartPosition: Int,
            shipmentCartItemModel: ShipmentCartItemModel?
        )

        fun navigateToSetPinpoint(message: String?, locationPass: LocationPass?)
        fun generateNewCheckoutRequest(
            shipmentCartItemModelList: List<ShipmentCartItemModel?>?,
            isAnalyticsPurpose: Boolean
        ): List<DataCheckoutRequest?>?

        val activityContext: Activity
        fun setCourierPromoApplied(itemPosition: Int)
        fun stopTrace()
        fun stopEmbraceTrace()
        fun onSuccessClearPromoLogistic(position: Int, isLastAppliedPromo: Boolean)
        fun resetCourier(position: Int)
        fun generateValidateUsePromoRequest(): ValidateUsePromoRequest?
        fun generateCouponListRecommendationRequest(): PromoRequest?
        fun clearTotalBenefitPromoStacking()
        fun triggerSendEnhancedEcommerceCheckoutAnalyticAfterCheckoutSuccess(
            transactionId: String?,
            deviceModel: String?,
            devicePrice: Long,
            diagnosticId: String?
        )

        fun removeIneligiblePromo(notEligiblePromoHolderdataList: ArrayList<NotEligiblePromoHolderdata>)
        fun updateTickerAnnouncementMessage()
        fun resetPromoBenefit()
        fun setPromoBenefit(summariesUiModels: List<SummariesItemUiModel?>?)
        val isTradeInByDropOff: Boolean
        fun updateButtonPromoCheckout(
            promoUiModel: PromoUiModel?,
            isNeedToHitValidateFinal: Boolean
        )

        fun doResetButtonPromoCheckout()
        fun resetCourier(shipmentCartItemModel: ShipmentCartItemModel?)
        fun setHasRunningApiCall(hasRunningApiCall: Boolean)
        fun prepareReloadRates(lastSelectedCourierOrder: Int, skipMvc: Boolean)
        fun updateLocalCacheAddressData(userAddress: UserAddress?)
        fun resetAllCourier()
        fun setStateLoadingCourierStateAtIndex(index: Int, isLoading: Boolean)
        fun logOnErrorLoadCheckoutPage(throwable: Throwable?)
        fun logOnErrorLoadCourier(throwable: Throwable?, itemPosition: Int, boPromoCode: String?)
        fun logOnErrorApplyBo(throwable: Throwable?, itemPosition: Int, boPromoCode: String?)
        fun logOnErrorApplyBo(
            throwable: Throwable?,
            shipmentCartItemModel: ShipmentCartItemModel?,
            boPromoCode: String?
        )

        fun logOnErrorCheckout(throwable: Throwable?, request: String?)
        fun showPopUp(popUpData: PopUpData?)
        fun updateAddOnsData(addOnsDataModel: AddOnsDataModel?, identifier: Int)
        fun onNeedUpdateViewItem(position: Int)
        fun renderUnapplyBoIncompleteShipment(unappliedBoPromoUniqueIds: List<String?>?)
        fun getShipmentCartItemModelAdapterPositionByUniqueId(uniqueId: String?): Int
        fun getShipmentCartItemModel(position: Int): ShipmentCartItemModel?
        fun getShipmentDetailData(
            shipmentCartItemModel: ShipmentCartItemModel?,
            recipientAddressModel: RecipientAddressModel?
        ): ShipmentDetailData?

        fun showPrescriptionReminderDialog(uploadPrescriptionUiModel: UploadPrescriptionUiModel?)
        fun updateUploadPrescription(uploadPrescriptionUiModel: UploadPrescriptionUiModel?)
        fun showCoachMarkEpharmacy(epharmacyGroupIds: UploadPrescriptionUiModel?)
    }

    interface AnalyticsActionListener {
        fun sendAnalyticsChoosePaymentMethodFailed(errorMessage: String?)
        fun sendEnhancedEcommerceAnalyticsCheckout(
            stringObjectMap: Map<String, Any>,
            tradeInCustomDimension: Map<String, String>,
            transactionId: String?,
            userId: String,
            promoFlag: Boolean,
            eventCategory: String,
            eventAction: String,
            eventLabel: String
        )

        fun sendEnhancedEcommerceAnalyticsCrossSellClickPilihPembayaran(
            eventLabel: String?,
            userId: String?,
            listProducts: List<Any?>?
        )

        fun sendAnalyticsOnClickChooseOtherAddressShipment()
        fun sendAnalyticsOnClickTopDonation()
        fun sendAnalyticsOnClickChangeAddress()
        fun sendAnalyticsOnClickSubtotal()
        fun sendAnalyticsOnClickCheckBoxDropShipperOption()
        fun sendAnalyticsOnClickCheckBoxInsuranceOption()
        fun sendAnalyticsScreenName(screenName: String?)
        fun sendAnalyticsOnClickEditPinPointErrorValidation(message: String?)
        fun sendAnalyticsCourierNotComplete()
        fun sendAnalyticsPromoRedState()
        fun sendAnalyticsDropshipperNotComplete()
        fun sendAnalyticsOnClickButtonCloseShipmentRecommendationDuration()
        fun sendAnalyticsOnClickChecklistShipmentRecommendationDuration(duration: String?)
        fun sendAnalyticsOnViewPreselectedCourierShipmentRecommendation(courier: String?)
        fun sendAnalyticsOnClickChangeCourierShipmentRecommendation(shipmentCartItemModel: ShipmentCartItemModel?)
        fun sendAnalyticsOnClickButtonCloseShipmentRecommendationCourier()
        fun sendAnalyticsOnClickChangeDurationShipmentRecommendation()
        fun sendAnalyticsOnViewPreselectedCourierAfterPilihDurasi(shippingProductId: Int)
        fun sendAnalyticsOnDisplayDurationThatContainPromo(
            isCourierPromo: Boolean,
            duration: String?
        )

        fun sendAnalyticsOnDisplayLogisticThatContainPromo(
            isCourierPromo: Boolean,
            shippingProductId: Int
        )

        fun sendAnalyticsOnClickDurationThatContainPromo(
            isCourierPromo: Boolean,
            duration: String?,
            isCod: Boolean,
            shippingPriceMin: String?,
            shippingPriceHigh: String?
        )

        fun sendAnalyticsOnClickLogisticThatContainPromo(
            isCourierPromo: Boolean,
            shippingProductId: Int,
            isCod: Boolean
        )

        fun sendAnalyticsViewInformationAndWarningTickerInCheckout(tickerId: String?)
        fun sendAnalyticsViewPromoAfterAdjustItem(msg: String?)
    }

    interface Presenter : CustomerPresenter<View?> {
        val logisticDonePublisher: PublishSubject<Boolean?>?
        fun setScheduleDeliveryMapData(
            cartString: String?,
            shipmentScheduleDeliveryMapData: ShipmentScheduleDeliveryMapData?
        )

        fun processInitialLoadCheckoutPage(
            isReloadData: Boolean,
            isOneClickShipment: Boolean,
            isTradeIn: Boolean,
            skipUpdateOnboardingState: Boolean,
            isReloadAfterPriceChangeHinger: Boolean,
            cornerId: String?,
            deviceId: String?,
            leasingId: String?,
            isPlusSelected: Boolean
        )

        fun processCheckout(
            isOneClickShipment: Boolean,
            isTradeIn: Boolean,
            isTradeInDropOff: Boolean,
            deviceId: String?,
            cornerId: String?,
            leasingId: String?,
            isPlusSelected: Boolean
        )

        fun checkPromoCheckoutFinalShipment(
            validateUsePromoRequest: ValidateUsePromoRequest?,
            lastSelectedCourierOrderIndex: Int,
            cartString: String?
        )

        fun doValidateUseLogisticPromo(
            cartPosition: Int,
            cartString: String?,
            validateUsePromoRequest: ValidateUsePromoRequest?,
            promoCode: String?,
            showLoading: Boolean
        )

        fun processCheckPromoCheckoutCodeFromSelectedCourier(
            promoCode: String?,
            itemPosition: Int,
            noToast: Boolean
        )

        fun processSaveShipmentState(shipmentCartItemModel: ShipmentCartItemModel?)
        fun processSaveShipmentState()
        fun processGetCourierRecommendation(
            shipperId: Int,
            spId: Int,
            itemPosition: Int,
            shipmentDetailData: ShipmentDetailData?,
            shipmentCartItemModel: ShipmentCartItemModel?,
            shopShipmentList: List<ShopShipment?>?,
            isInitialLoad: Boolean,
            products: ArrayList<Product?>?,
            cartString: String?,
            isTradeInDropOff: Boolean,
            recipientAddressModel: RecipientAddressModel?,
            isForceReload: Boolean,
            skipMvc: Boolean
        )

        var recipientAddressModel: RecipientAddressModel?
        var shipmentCartItemModelList: List<ShipmentCartItemModel?>?
        fun setDataCheckoutRequestList(dataCheckoutRequestList: List<DataCheckoutRequest?>?)
        var shipmentCostModel: ShipmentCostModel?
        var egoldAttributeModel: EgoldAttributeModel?
        val shipmentTickerErrorModel: ShipmentTickerErrorModel?
        var tickerAnnouncementHolderData: TickerAnnouncementHolderData
        fun editAddressPinpoint(
            latitude: String?,
            longitude: String?,
            shipmentCartItemModel: ShipmentCartItemModel?,
            locationPass: LocationPass?
        )

        fun cancelNotEligiblePromo(notEligiblePromoHolderdataArrayList: ArrayList<NotEligiblePromoHolderdata?>?)
        fun cancelAutoApplyPromoStackLogistic(
            itemPosition: Int,
            promoCode: String?,
            shipmentCartItemModel: ShipmentCartItemModel?
        )

        fun cancelAutoApplyPromoStackAfterClash(clashingInfoDetailUiModel: ClashingInfoDetailUiModel?)
        fun changeShippingAddress(
            newRecipientAddressModel: RecipientAddressModel?,
            chosenAddressModel: ChosenAddressModel?,
            isOneClickShipment: Boolean,
            isTradeInDropOff: Boolean,
            isHandleFallback: Boolean,
            reloadCheckoutPage: Boolean
        )

        var shipmentDonationModel: ShipmentDonationModel?
        var listShipmentCrossSellModel: ArrayList<ShipmentCrossSellModel?>?
        var shipmentButtonPaymentModel: ShipmentButtonPaymentModel?
        fun setShippingCourierViewModelsState(
            shippingCourierUiModelsState: List<ShippingCourierUiModel?>?,
            orderNumber: Int
        )

        fun getShippingCourierViewModelsState(orderNumber: Int): List<ShippingCourierUiModel?>?
        var couponStateChanged: Boolean
        val codData: CodModel?
        val campaignTimer: CampaignTimerUi?
        val isShowOnboarding: Boolean
        fun triggerSendEnhancedEcommerceCheckoutAnalytics(
            dataCheckoutRequests: List<DataCheckoutRequest?>?,
            tradeInCustomDimension: Map<String?, String?>?,
            step: String?,
            eventCategory: String?,
            eventAction: String?,
            eventLabel: String?,
            leasingId: String?,
            pageSource: String?
        )

        fun updateEnhancedEcommerceCheckoutAnalyticsDataLayerShippingData(
            cartString: String?,
            shippingDuration: String?,
            shippingPrice: String?,
            courierName: String?
        ): List<DataCheckoutRequest?>?

        fun updateEnhancedEcommerceCheckoutAnalyticsDataLayerPromoData(shipmentCartItemModels: List<ShipmentCartItemModel?>?): List<DataCheckoutRequest?>?
        val isIneligiblePromoDialogEnabled: Boolean
        fun generateCheckoutRequest(
            analyticsDataCheckoutRequests: List<DataCheckoutRequest?>?,
            isDonation: Int,
            crossSellModelArrayList: ArrayList<ShipmentCrossSellModel?>?,
            leasingId: String?
        ): CheckoutRequest?

        fun releaseBooking()
        fun fetchEpharmacyData()
        fun setPrescriptionIds(prescriptionIds: ArrayList<String?>?)
        fun setMiniConsultationResult(results: ArrayList<EPharmacyMiniConsultationResult?>?)
        var lastApplyData: LastApplyUiModel?
        var validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel?
        fun setLatValidateUseRequest(latValidateUseRequest: ValidateUsePromoRequest?)
        val lastValidateUseRequest: ValidateUsePromoRequest?
        fun setUploadPrescriptionData(uploadPrescriptionUiModel: UploadPrescriptionUiModel?)
        val uploadPrescriptionUiModel: UploadPrescriptionUiModel?
        fun generateRatesMvcParam(cartString: String?): String?
        val cartDataForRates: String?
        fun setCheckoutData(checkoutData: CheckoutData?)
        fun updateAddOnProductLevelDataBottomSheet(saveAddOnStateResult: SaveAddOnStateResult?)
        fun updateAddOnOrderLevelDataBottomSheet(saveAddOnStateResult: SaveAddOnStateResult?)
        val shipmentUpsellModel: ShipmentUpsellModel?
        val shipmentNewUpsellModel: ShipmentNewUpsellModel?
        fun validateBoPromo(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel?): Pair<ArrayList<String?>?, ArrayList<String?>?>?
        fun clearOrderPromoCodeFromLastValidateUseRequest(uniqueId: String?, promoCode: String?)
        fun validateClearAllBoPromo()
        fun doUnapplyBo(uniqueId: String?, promoCode: String?)
        fun getProductForRatesRequest(shipmentCartItemModel: ShipmentCartItemModel?): List<Product?>?
        fun processBoPromoCourierRecommendation(
            itemPosition: Int,
            voucherOrdersItemUiModel: PromoCheckoutVoucherOrdersItemUiModel?,
            shipmentCartItemModel: ShipmentCartItemModel?
        )

        fun doApplyBo(voucherOrdersItemUiModel: PromoCheckoutVoucherOrdersItemUiModel?)
        fun hitClearAllBo()
        fun cancelUpsell(
            isReloadData: Boolean,
            isOneClickShipment: Boolean,
            isTradeIn: Boolean,
            skipUpdateOnboardingState: Boolean,
            isReloadAfterPriceChangeHinger: Boolean,
            cornerId: String?,
            deviceId: String?,
            leasingId: String?,
            isPlusSelected: Boolean
        )

        fun clearAllBoOnTemporaryUpsell()
        fun validatePrescriptionOnBackPressed(): Boolean
    }
}
