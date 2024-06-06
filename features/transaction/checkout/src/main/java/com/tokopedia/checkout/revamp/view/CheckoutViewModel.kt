package com.tokopedia.checkout.revamp.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.addon.presentation.uimodel.AddOnPageResult
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.analytics.performance.util.EmbraceKey
import com.tokopedia.analytics.performance.util.EmbraceMonitoring
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartPaymentRequest
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.analytics.CheckoutTradeInAnalytics
import com.tokopedia.checkout.domain.mapper.ShipmentAddOnProductServiceMapper
import com.tokopedia.checkout.domain.model.cartshipmentform.CampaignTimerUi
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.cartshipmentform.ShipmentAction
import com.tokopedia.checkout.domain.model.cartshipmentform.ShipmentPlatformFeeData
import com.tokopedia.checkout.domain.model.platformfee.PaymentFeeCheckoutRequest
import com.tokopedia.checkout.revamp.view.converter.CheckoutDataConverter
import com.tokopedia.checkout.revamp.view.processor.CheckoutAddOnProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutCalculator
import com.tokopedia.checkout.revamp.view.processor.CheckoutCartProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutCartProcessor.Companion.UPDATE_CART_SOURCE_CHECKOUT
import com.tokopedia.checkout.revamp.view.processor.CheckoutCartProcessor.Companion.UPDATE_CART_SOURCE_CHECKOUT_OPEN_PROMO
import com.tokopedia.checkout.revamp.view.processor.CheckoutCartProcessor.Companion.UPDATE_CART_SOURCE_NOTES
import com.tokopedia.checkout.revamp.view.processor.CheckoutCartProcessor.Companion.UPDATE_CART_SOURCE_PAYMENT
import com.tokopedia.checkout.revamp.view.processor.CheckoutCartProcessor.Companion.UPDATE_CART_SOURCE_QUANTITY
import com.tokopedia.checkout.revamp.view.processor.CheckoutDataHelper
import com.tokopedia.checkout.revamp.view.processor.CheckoutLogisticProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutPaymentProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutPromoProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutResult
import com.tokopedia.checkout.revamp.view.processor.CheckoutToasterProcessor
import com.tokopedia.checkout.revamp.view.processor.generateCheckoutOrderInsuranceFromCourier
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutButtonPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCostModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellGroupModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellItem
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutDonationModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEgoldModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEpharmacyModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutItem
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderInsurance
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderShipment
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageState
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageToaster
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPromoModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerErrorModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutUpsellModel
import com.tokopedia.checkout.revamp.view.uimodel.OriginalCheckoutPaymentData
import com.tokopedia.checkout.revamp.view.uimodel.ShippingComponents
import com.tokopedia.checkout.revamp.view.widget.CheckoutDropshipWidget
import com.tokopedia.checkout.view.CheckoutLogger
import com.tokopedia.checkout.view.CheckoutMutableLiveData
import com.tokopedia.checkout.view.uimodel.ShipmentPaymentFeeModel
import com.tokopedia.checkoutpayment.data.CreditCardTenorListRequest
import com.tokopedia.checkoutpayment.data.GetPaymentWidgetChosenPaymentRequest
import com.tokopedia.checkoutpayment.data.GetPaymentWidgetRequest
import com.tokopedia.checkoutpayment.data.GoCicilInstallmentRequest
import com.tokopedia.checkoutpayment.data.PaymentFeeRequest
import com.tokopedia.checkoutpayment.data.PaymentRequest
import com.tokopedia.checkoutpayment.domain.GoCicilInstallmentData
import com.tokopedia.checkoutpayment.domain.GoCicilInstallmentOption
import com.tokopedia.checkoutpayment.domain.PaymentWidgetData.Companion.MANDATORY_HIT_CC_TENOR_LIST
import com.tokopedia.checkoutpayment.domain.PaymentWidgetData.Companion.MANDATORY_HIT_INSTALLMENT_OPTIONS
import com.tokopedia.checkoutpayment.domain.TenorListData
import com.tokopedia.checkoutpayment.view.CheckoutPaymentWidgetData
import com.tokopedia.checkoutpayment.view.CheckoutPaymentWidgetState
import com.tokopedia.common_epharmacy.network.response.EPharmacyMiniConsultationResult
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.logisticCommon.data.constant.InsuranceConstant
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.logisticcart.shipping.model.CodModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.processor.CheckoutShippingProcessor
import com.tokopedia.logisticcart.shipping.processor.LogisticProcessorGetRatesParam
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promousage.util.PromoUsageRollenceManager
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.analytics.PromoRevampAnalytics
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCheckout
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.feature.addons.data.model.AddOnProductDataItemModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingBottomSheetModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingButtonModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingMetadataItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingNoteItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingProductItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingTickerModel
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnResult
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.SaveAddOnStateResult
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrder
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.LastApplyUiMapper
import com.tokopedia.purchase_platform.common.feature.promo.view.model.PromoExternalAutoApply
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class CheckoutViewModel @Inject constructor(
    private val cartProcessor: CheckoutCartProcessor,
    private val logisticProcessor: CheckoutLogisticProcessor,
    private val logisticCartProcessor: CheckoutShippingProcessor,
    private val promoProcessor: CheckoutPromoProcessor,
    private val addOnProcessor: CheckoutAddOnProcessor,
    val paymentProcessor: CheckoutPaymentProcessor,
    private val checkoutProcessor: CheckoutProcessor,
    private val calculator: CheckoutCalculator,
    private val toasterProcessor: CheckoutToasterProcessor,
    private val dataConverter: CheckoutDataConverter,
    private val mTrackerShipment: CheckoutAnalyticsCourierSelection,
    private val mTrackerTradeIn: CheckoutTradeInAnalytics,
    private val mTrackerPurchaseProtection: CheckoutAnalyticsPurchaseProtection,
    private val helper: CheckoutDataHelper,
    private val userSessionInterface: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    val listData: CheckoutMutableLiveData<List<CheckoutItem>> = CheckoutMutableLiveData(emptyList())

    val pageState: CheckoutMutableLiveData<CheckoutPageState> =
        CheckoutMutableLiveData(CheckoutPageState.Loading)

    val commonToaster = toasterProcessor.commonToaster

    private var campaignTimer: CampaignTimerUi? = null

    var isOneClickShipment: Boolean = false

    var checkoutLeasingId: String = "0"

    var isTradeIn: Boolean = false

    var deviceId: String = ""

    var isPlusSelected: Boolean = false

    var shipmentAction: String = "merge"

    var checkoutPageSource: String = CheckoutConstant.CHECKOUT_PAGE_SOURCE_PDP

    var listPromoExternalAutoApplyCode: List<PromoExternalAutoApply> = emptyList()

    val cornerId: String?
        get() = recipientAddressModel.cornerId

    val recipientAddressModel: RecipientAddressModel
        get() = listData.value.address()?.recipientAddressModel
            ?: RecipientAddressModel()

    val isTradeInByDropOff: Boolean
        get() {
            val recipientAddressModel = this.recipientAddressModel
            return recipientAddressModel.selectedTabIndex == 1
        }

    private var isUsingDdp = false

    internal var shipmentPlatformFeeData: ShipmentPlatformFeeData = ShipmentPlatformFeeData()

    private var cartDataForRates = ""

    private var codData: CodModel? = null

    // list summary default
    private var summariesAddOnUiModel: HashMap<Int, String> = hashMapOf()

    private var isPromoRevamp: Boolean? = null

    private var isBoUnstackEnabled: Boolean = false

    var isCartCheckoutRevamp: Boolean = false
    var usePromoEntryPointNewInterface: Boolean = false

    private var cartType: String = ""
    private var terms: String = ""

    private var debounceQtyJob: Job? = null

    fun stopEmbraceTrace() {
        val emptyMap: Map<String, Any> = HashMap()
        EmbraceMonitoring.stopMoments(EmbraceKey.KEY_ACT_BUY, null, emptyMap)
    }

    fun loadSAF(
        isReloadData: Boolean,
        skipUpdateOnboardingState: Boolean,
        isReloadAfterPriceChangeHigher: Boolean,
        gatewayCode: String = "",
        tenor: Int = 0,
        modifiedCheckoutItems: ((List<CheckoutItem>) -> List<CheckoutItem>)? = null
    ) {
        promoProcessor.isCartCheckoutRevamp = isCartCheckoutRevamp
        viewModelScope.launch(dispatchers.io) {
            withContext(dispatchers.main) {
                pageState.value = CheckoutPageState.Loading
            }
            val saf = cartProcessor.hitSAF(
                isOneClickShipment,
                isTradeIn,
                isTradeInByDropOff,
                skipUpdateOnboardingState,
                cornerId,
                deviceId,
                checkoutLeasingId,
                isPlusSelected,
                isReloadData,
                isReloadAfterPriceChangeHigher,
                shipmentAction,
                listPromoExternalAutoApplyCode
            )
            stopEmbraceTrace()
            when (saf) {
                is CheckoutPageState.Success -> {
                    try {
                        val tickerError =
                            CheckoutTickerErrorModel(errorMessage = saf.cartShipmentAddressFormData.errorTicker)
                        val tickerData = saf.cartShipmentAddressFormData.tickerData
                        var ticker = CheckoutTickerModel(ticker = TickerAnnouncementHolderData())
                        if (tickerData != null) {
                            ticker = CheckoutTickerModel(
                                ticker = TickerAnnouncementHolderData(
                                    tickerData.id,
                                    tickerData.title,
                                    tickerData.message
                                )
                            )
                            mTrackerShipment.eventViewInformationAndWarningTickerInCheckout(
                                tickerData.id
                            )
                        }
                        val address = CheckoutAddressModel(
                            recipientAddressModel = dataConverter.getRecipientAddressModel(saf.cartShipmentAddressFormData)
                        )

                        val upsell =
                            dataConverter.getUpsellModel(saf.cartShipmentAddressFormData.newUpsell)

                        isUsingDdp = saf.cartShipmentAddressFormData.isUsingDdp
                        shipmentPlatformFeeData =
                            saf.cartShipmentAddressFormData.shipmentPlatformFee
                        cartDataForRates = saf.cartShipmentAddressFormData.cartData
                        codData = saf.cartShipmentAddressFormData.cod
                        campaignTimer = saf.cartShipmentAddressFormData.campaignTimerUi
                        logisticCartProcessor.isBoUnstackEnabled =
                            saf.cartShipmentAddressFormData.lastApplyData.additionalInfo.bebasOngkirInfo.isBoUnstackEnabled
                        isBoUnstackEnabled =
                            saf.cartShipmentAddressFormData.lastApplyData.additionalInfo.bebasOngkirInfo.isBoUnstackEnabled
                        summariesAddOnUiModel =
                            ShipmentAddOnProductServiceMapper.getShoppingSummaryAddOns(saf.cartShipmentAddressFormData.listSummaryAddons)

                        val items = dataConverter.getCheckoutItems(
                            saf.cartShipmentAddressFormData,
                            address.recipientAddressModel.locationDataModel != null,
                            userSessionInterface.name,
                            saf.cartShipmentAddressFormData.cartType == OCC
                        )

                        var uploadPrescriptionUiModel = UploadPrescriptionUiModel()
                        if (!tickerError.isError) {
                            uploadPrescriptionUiModel = UploadPrescriptionUiModel(
                                showImageUpload = saf.cartShipmentAddressFormData.epharmacyData.showImageUpload,
                                uploadImageText = saf.cartShipmentAddressFormData.epharmacyData.uploadText,
                                leftIconUrl = saf.cartShipmentAddressFormData.epharmacyData.leftIconUrl,
                                checkoutId = saf.cartShipmentAddressFormData.epharmacyData.checkoutId,
                                frontEndValidation = saf.cartShipmentAddressFormData.epharmacyData.frontEndValidation,
                                consultationFlow = saf.cartShipmentAddressFormData.epharmacyData.consultationFlow,
                                rejectedWording = saf.cartShipmentAddressFormData.epharmacyData.rejectedWording,
                                isBlockCheckoutFlowMessage = saf.cartShipmentAddressFormData.epharmacyData.isBlockCheckoutFlowMessage
                            )
                            addOnProcessor.fetchPrescriptionIds(
                                saf.cartShipmentAddressFormData.epharmacyData,
                                items,
                                uploadPrescriptionUiModel
                            )
                        }

                        val epharmacy = CheckoutEpharmacyModel(
                            epharmacy = uploadPrescriptionUiModel
                        )

                        isPromoRevamp = PromoUsageRollenceManager()
                            .isRevamp(saf.cartShipmentAddressFormData.lastApplyData.userGroupMetadata)
                        val promo = CheckoutPromoModel(
                            isEnable = !tickerError.isError,
                            promo = saf.cartShipmentAddressFormData.lastApplyData,
                            isPromoRevamp = isPromoRevamp ?: false,
                            isLoading = isPromoRevamp ?: false,
                            enableNewInterface = usePromoEntryPointNewInterface
                        )
                        if (promo.isEnable && saf.cartShipmentAddressFormData.lastApplyData.additionalInfo.errorDetail.message.isNotEmpty()) {
                            PromoRevampAnalytics.eventCartViewPromoMessage(saf.cartShipmentAddressFormData.lastApplyData.additionalInfo.errorDetail.message)
                        }

                        val payment = CheckoutPaymentModel(
                            widget = CheckoutPaymentWidgetData(
                                state = CheckoutPaymentWidgetState.None
                            ),
                            metadata = saf.cartShipmentAddressFormData.paymentWidget.metadata,
                            enable = saf.cartShipmentAddressFormData.paymentWidget.enable,
                            defaultErrorMessage = saf.cartShipmentAddressFormData.paymentWidget.errorMessage,
                            originalData =
                            OriginalCheckoutPaymentData(
                                gatewayCode = gatewayCode.ifBlank { saf.cartShipmentAddressFormData.paymentWidget.chosenPayment.gatewayCode },
                                tenureType = if (gatewayCode.isNotBlank()) tenor else saf.cartShipmentAddressFormData.paymentWidget.chosenPayment.tenureType,
                                optionId = saf.cartShipmentAddressFormData.paymentWidget.chosenPayment.optionId,
                                metadata = saf.cartShipmentAddressFormData.paymentWidget.chosenPayment.metadata
                            )
                        )

                        cartType = saf.cartShipmentAddressFormData.cartType
                        terms = saf.cartShipmentAddressFormData.terms
                        val cost = CheckoutCostModel(useNewWording = payment.enable)

                        val paymentLevelAddOnsMap = mutableMapOf<Long, CheckoutCrossSellItem>()
                        if (!tickerError.isError) {
                            val crossSellModel =
                                saf.cartShipmentAddressFormData.crossSell.firstOrNull()
                            if (crossSellModel != null && !crossSellModel.checkboxDisabled) {
                                paymentLevelAddOnsMap[DG_ID] = CheckoutCrossSellModel(
                                    crossSellModel,
                                    crossSellModel.isChecked,
                                    !crossSellModel.checkboxDisabled,
                                    0
                                )
                                if (crossSellModel.isChecked) {
                                    val digitalCategoryName = crossSellModel.orderSummary.title
                                    val digitalProductName = crossSellModel.info.title
                                    val eventLabel = "$digitalCategoryName - ${crossSellModel.id}"
                                    mTrackerShipment.eventViewAutoCheckCrossSell(
                                        userSessionInterface.userId,
                                        0.toString(),
                                        eventLabel,
                                        digitalProductName,
                                        ArrayList(items.mapNotNull { if (it is CheckoutProductModel) it.productCatId else null })
                                    )
                                }
                            }
                            if (saf.cartShipmentAddressFormData.egoldAttributes != null && saf.cartShipmentAddressFormData.egoldAttributes!!.isEnabled && saf.cartShipmentAddressFormData.egoldAttributes!!.isEligible) {
                                paymentLevelAddOnsMap[EGOLD_ID] = CheckoutEgoldModel(
                                    saf.cartShipmentAddressFormData.egoldAttributes!!,
                                    saf.cartShipmentAddressFormData.egoldAttributes!!.isChecked,
                                    saf.cartShipmentAddressFormData.egoldAttributes!!.buyEgoldValue
                                )
                            }
                            if (saf.cartShipmentAddressFormData.donation != null && saf.cartShipmentAddressFormData.donation!!.title.isNotEmpty() && saf.cartShipmentAddressFormData.donation!!.nominal != 0) {
                                paymentLevelAddOnsMap[DONATION_ID] = CheckoutDonationModel(
                                    saf.cartShipmentAddressFormData.donation!!,
                                    saf.cartShipmentAddressFormData.donation!!.isChecked
                                )
                                if (saf.cartShipmentAddressFormData.donation!!.isChecked) {
                                    mTrackerShipment.eventViewAutoCheckDonation(
                                        userSessionInterface.userId
                                    )
                                }
                            }
                        }
                        val crossSellList = arrayListOf<CheckoutCrossSellItem>()
                        saf.cartShipmentAddressFormData.paymentLevelAddOnsPositions.forEach {
                            val crossSellItem = paymentLevelAddOnsMap[it]
                            if (crossSellItem != null) {
                                crossSellList.add(crossSellItem)
                            }
                        }

                        val crossSellGroup =
                            CheckoutCrossSellGroupModel(crossSellList = crossSellList)

                        val buttonPayment = CheckoutButtonPaymentModel(
                            "",
                            useDirectPayment = payment.enable,
                            terms = saf.cartShipmentAddressFormData.terms
                        )

                        val itemsWithLoadingState = items.map {
                            if (it is CheckoutOrderModel && loadCourierState(
                                    it,
                                    address.recipientAddressModel,
                                    false
                                )
                            ) {
                                it.copy(shipment = it.shipment.copy(isLoading = true))
                            } else {
                                it
                            }
                        }

                        var finalItems = listOf(
                            tickerError,
                            ticker,
                            address,
                            upsell
                        ) + itemsWithLoadingState + listOf(
                            epharmacy,
                            promo,
                            payment,
                            cost,
                            crossSellGroup,
                            buttonPayment
                        )

                        if (modifiedCheckoutItems != null) {
                            finalItems = modifiedCheckoutItems(finalItems)
                        }

                        withContext(dispatchers.main) {
                            listData.value = finalItems
                            pageState.value = saf
                            calculateTotal()
                        }
                        sendEEStep2()
                    } catch (t: Throwable) {
                        Timber.d(t)
                        withContext(dispatchers.main) {
                            pageState.value = CheckoutPageState.Error(RuntimeException())
                        }
                    }
                }

                else -> {
                    withContext(dispatchers.main) {
                        pageState.value = saf
                    }
                }
            }
        }
    }

    fun isLoading(): Boolean {
        return listData.value.indexOfFirst { it is CheckoutOrderModel && it.shipment.isLoading } != -1
    }

    private fun sendEEStep2() {
        triggerSendEnhancedEcommerceCheckoutAnalytics(
            null,
            EnhancedECommerceActionField.STEP_2,
            ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
            ConstantTransactionAnalytics.EventAction.VIEW_CHECKOUT_PAGE,
            ConstantTransactionAnalytics.EventLabel.SUCCESS,
            "",
            checkoutPageSource
        )
    }

    private fun sendEEStep3() {
        val shipmentCartItemModels = listData.value

        // if one of courier reseted because of apply promo logistic (PSL) and eventually not eligible after hit validate use, don't send EE
        var courierHasReseted = false
        for (shipmentCartItemModel in shipmentCartItemModels) {
            if (shipmentCartItemModel is CheckoutOrderModel) {
                val selectedCourier = shipmentCartItemModel.shipment.courierItemData
                if (selectedCourier == null) {
                    courierHasReseted = true
                    break
                }
            }
        }
        if (!courierHasReseted) {
            triggerSendEnhancedEcommerceCheckoutAnalytics(
                null,
                EnhancedECommerceActionField.STEP_3,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_ALL_COURIER_SELECTED,
                "",
                "",
                checkoutPageSource
            )
        }
    }

    private fun sendEEStep4(
        transactionId: String,
        deviceModel: String,
        devicePrice: Long,
        diagnosticId: String
    ) {
        var eventCategory = ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION
        var eventAction = ConstantTransactionAnalytics.EventAction.CLICK_PILIH_METODE_PEMBAYARAN
        var eventLabel = ConstantTransactionAnalytics.EventLabel.SUCCESS
        val tradeInCustomDimension: MutableMap<String, String> = hashMapOf()
        if (isTradeIn) {
            eventCategory =
                CheckoutTradeInAnalytics.EVENT_CATEGORY_SELF_PICKUP_ADDRESS_SELECTION_TRADE_IN
            eventLabel = String.format(
                Locale.getDefault(),
                CheckoutTradeInAnalytics.EVENT_LABEL_TRADE_IN_CHECKOUT_EE,
                deviceModel,
                devicePrice,
                diagnosticId
            )
            tradeInCustomDimension[CheckoutTradeInAnalytics.KEY_USER_ID] =
                userSessionInterface.userId
            tradeInCustomDimension[CheckoutTradeInAnalytics.KEY_BUSINESS_UNIT] =
                CheckoutTradeInAnalytics.VALUE_TRADE_IN
            if (isTradeInByDropOff) {
                eventAction = CheckoutTradeInAnalytics.EVENT_ACTION_PILIH_PEMBAYARAN_INDOMARET
                tradeInCustomDimension[CheckoutTradeInAnalytics.KEY_SCREEN_NAME] =
                    CheckoutTradeInAnalytics.SCREEN_NAME_DROP_OFF_ADDRESS
            } else {
                eventAction = CheckoutTradeInAnalytics.EVENT_ACTION_PILIH_PEMBAYARAN_NORMAL
                tradeInCustomDimension[CheckoutTradeInAnalytics.KEY_SCREEN_NAME] =
                    CheckoutTradeInAnalytics.SCREEN_NAME_NORMAL_ADDRESS
            }
        }
        triggerSendEnhancedEcommerceCheckoutAnalytics(
            tradeInCustomDimension,
            EnhancedECommerceActionField.STEP_4,
            eventCategory,
            eventAction,
            eventLabel,
            transactionId,
            checkoutPageSource
        )
    }

    private fun generateCheckoutAnalyticsDataLayer(
        step: String,
        pageSource: String
    ): Map<String, Any> {
        val checkoutMapData: MutableMap<String, Any> = HashMap()
        val enhancedECommerceActionField = EnhancedECommerceActionField()
        enhancedECommerceActionField.setStep(step)
        var option = ""
        if (step.equals(EnhancedECommerceActionField.STEP_2, ignoreCase = true)) {
            option = EnhancedECommerceActionField.STEP_2_OPTION_CHECKOUT_PAGE_LOADED
        } else if (step.equals(EnhancedECommerceActionField.STEP_3, ignoreCase = true)) {
            option = EnhancedECommerceActionField.STEP_3_OPTION_DATA_VALIDATION
        } else if (step.equals(EnhancedECommerceActionField.STEP_4, ignoreCase = true)) {
            option = EnhancedECommerceActionField.STEP_4_OPTION_CLICK_PAYMENT_OPTION_BUTTON
        }
        enhancedECommerceActionField.setOption(option)
        val enhancedECommerceCheckout = EnhancedECommerceCheckout()
        for (shipmentCartItemModel in listData.value) {
            if (shipmentCartItemModel is CheckoutOrderModel) {
                val orderProducts = getOrderProducts(shipmentCartItemModel.cartStringGroup).filterIsInstance(CheckoutProductModel::class.java)
                for (cartItemModel in orderProducts) {
                    var courierItemData: CourierItemData? = null
                    if (shipmentCartItemModel.shipment.courierItemData != null
//                            shipmentDetailData.selectedCourier != null ||
//                                shipmentDetailData.selectedCourierTradeInDropOff != null
//                            )
                    ) {
                        /*if (isTradeInByDropOff && shipmentDetailData.selectedCourierTradeInDropOff != null) {
                            courierItemData = shipmentDetailData.selectedCourierTradeInDropOff
                        } else */if (!isTradeInByDropOff && shipmentCartItemModel.shipment.courierItemData != null) {
                            courierItemData = shipmentCartItemModel.shipment.courierItemData
                        }
                    }
                    val enhancedECommerceProductCartMapData =
                        EnhancedECommerceProductCartMapData()
                    enhancedECommerceProductCartMapData.setProductName(cartItemModel.analyticsProductCheckoutData.productName)
                    enhancedECommerceProductCartMapData.setProductID(
                        cartItemModel.productId.toString()
                    )
                    enhancedECommerceProductCartMapData.setPrice(cartItemModel.analyticsProductCheckoutData.productPrice)
                    enhancedECommerceProductCartMapData.setBrand(cartItemModel.analyticsProductCheckoutData.productBrand)
                    enhancedECommerceProductCartMapData.setCategory(cartItemModel.analyticsProductCheckoutData.productCategory)
                    enhancedECommerceProductCartMapData.setVariant(cartItemModel.analyticsProductCheckoutData.productVariant)
                    enhancedECommerceProductCartMapData.setQty(
                        cartItemModel.analyticsProductCheckoutData.productQuantity
                    )
                    enhancedECommerceProductCartMapData.setShopId(cartItemModel.analyticsProductCheckoutData.productShopId)
                    enhancedECommerceProductCartMapData.setShopName(cartItemModel.analyticsProductCheckoutData.productShopName)
                    enhancedECommerceProductCartMapData.setShopType(
                        cartItemModel.analyticsProductCheckoutData.productShopType
                    )
                    enhancedECommerceProductCartMapData.setCategoryId(cartItemModel.analyticsProductCheckoutData.productCategoryId)
                    enhancedECommerceProductCartMapData.setDimension38(cartItemModel.analyticsProductCheckoutData.productAttribution)
                    enhancedECommerceProductCartMapData.setDimension40(cartItemModel.analyticsProductCheckoutData.productListName)
                    enhancedECommerceProductCartMapData.setDimension45(
                        cartItemModel.cartId.toString()
                    )
                    enhancedECommerceProductCartMapData.setDimension53(
                        cartItemModel.analyticsProductCheckoutData.isDiscountedPrice
                    )
                    enhancedECommerceProductCartMapData.setDimension54(
                        shipmentCartItemModel.isFulfillment
                    )
                    enhancedECommerceProductCartMapData.setWarehouseId(cartItemModel.analyticsProductCheckoutData.warehouseId)
                    enhancedECommerceProductCartMapData.setProductWeight(cartItemModel.analyticsProductCheckoutData.productWeight)
                    enhancedECommerceProductCartMapData.setPromoCode(cartItemModel.analyticsProductCheckoutData.promoCode)
                    enhancedECommerceProductCartMapData.setPromoDetails(cartItemModel.analyticsProductCheckoutData.promoDetails)
                    enhancedECommerceProductCartMapData.setCartId(
                        cartItemModel.cartId.toString()
                    )
                    enhancedECommerceProductCartMapData.setBuyerAddressId(cartItemModel.analyticsProductCheckoutData.buyerAddressId)
                    enhancedECommerceProductCartMapData.setShippingDuration(
                        courierItemData?.selectedShipper?.serviceId?.toString() ?: ""
                    )
                    enhancedECommerceProductCartMapData.setCourier(
                        courierItemData?.selectedShipper?.shipperProductId?.toString()
                            ?: if (shipmentCartItemModel.isSaveStateFlag) shipmentCartItemModel.spId.toString() else ""
                    )
                    enhancedECommerceProductCartMapData.setShippingPrice(
                        courierItemData?.selectedShipper?.shipperPrice?.toString() ?: ""
                    )
                    enhancedECommerceProductCartMapData.setCodFlag(cartItemModel.analyticsProductCheckoutData.codFlag)
                    enhancedECommerceProductCartMapData.setTokopediaCornerFlag(cartItemModel.analyticsProductCheckoutData.tokopediaCornerFlag)
                    enhancedECommerceProductCartMapData.setIsFulfillment(cartItemModel.analyticsProductCheckoutData.isFulfillment)
                    enhancedECommerceProductCartMapData.setDimension83(
                        cartItemModel.freeShippingName
                    )
                    enhancedECommerceProductCartMapData.setCampaignId(
                        cartItemModel.analyticsProductCheckoutData.campaignId.toString()
                    )
                    enhancedECommerceProductCartMapData.setPageSource(pageSource)
                    enhancedECommerceProductCartMapData.setDimension117(
                        cartItemModel.bundleType
                    )
                    enhancedECommerceProductCartMapData.setDimension118(
                        cartItemModel.bundleId
                    )
                    enhancedECommerceProductCartMapData.setDimension136(
                        cartItemModel.cartStringGroup
                    )
                    enhancedECommerceProductCartMapData.setDimension137(
                        cartItemModel.bmgmOfferId.toString()
                    )
                    enhancedECommerceCheckout.addProduct(
                        enhancedECommerceProductCartMapData.getProduct()
                    )
                    enhancedECommerceProductCartMapData.setDimension136(
                        cartItemModel.bmgmOfferId.toString()
                    )
                }
            }
        }
        enhancedECommerceCheckout.setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR)
        enhancedECommerceCheckout.setActionField(enhancedECommerceActionField.getActionFieldMap())
        checkoutMapData[EnhancedECommerceCheckout.KEY_CHECKOUT] =
            enhancedECommerceCheckout.getCheckoutMap()
        return checkoutMapData
    }

    private fun getPromoFlag(step: String): Boolean {
        return listData.value.promo()?.promo?.additionalInfo?.pomlAutoApplied ?: false
    }

    fun getCartTypeString(): String {
        return if (cartType == CartShipmentAddressFormData.CART_TYPE_OCC) {
            CART_TYPE_OCC
        } else if (cartType == CartShipmentAddressFormData.CART_TYPE_NORMAL) {
            CART_TYPE_ATC
        } else {
            ""
        }
    }

    fun triggerSendEnhancedEcommerceCheckoutAnalytics(
        tradeInCustomDimension: Map<String, String>?,
        step: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String,
        transactionId: String,
        pageSource: String
    ) {
        val eeDataLayer = generateCheckoutAnalyticsDataLayer(step, pageSource)
        mTrackerShipment.sendEnhancedECommerceCheckout(
            eeDataLayer,
            tradeInCustomDimension,
            transactionId,
            userSessionInterface.userId,
            getPromoFlag(step),
            eventCategory,
            eventAction,
            eventLabel,
            step,
            getCartTypeString()
        )
        mTrackerShipment.flushEnhancedECommerceCheckout()
    }

    internal fun shouldAutoLoadCourier(
        shipmentCartItemModel: CheckoutOrderModel,
        recipientAddressModel: RecipientAddressModel?
    ): Boolean {
        return recipientAddressModel != null && (
            (recipientAddressModel.isTradeIn && recipientAddressModel.selectedTabIndex != 0 && shipmentCartItemModel.shippingId != 0 && shipmentCartItemModel.spId != 0 && !recipientAddressModel.dropOffAddressName.isNullOrEmpty()) ||
                (recipientAddressModel.isTradeIn && recipientAddressModel.selectedTabIndex == 0 && shipmentCartItemModel.shippingId != 0 && shipmentCartItemModel.spId != 0 && !recipientAddressModel.provinceName.isNullOrEmpty()) ||
                (!recipientAddressModel.isTradeIn && shipmentCartItemModel.shippingId != 0 && shipmentCartItemModel.spId != 0 && !recipientAddressModel.provinceName.isNullOrEmpty()) ||
                (!recipientAddressModel.isTradeIn && shipmentCartItemModel.boCode.isNotEmpty() && !recipientAddressModel.provinceName.isNullOrEmpty()) || // normal address auto apply BO
                shipmentCartItemModel.isAutoCourierSelection // tokopedia now
            ) && !shipmentCartItemModel.isError
    }

    fun prepareFullCheckoutPage() {
        viewModelScope.launch(dispatchers.immediate) {
            val checkoutItems = listData.value
            var recipientAddressModel: RecipientAddressModel? = null
            for ((index, checkoutItem) in checkoutItems.withIndex()) {
                if (isActive) {
                    if (checkoutItem is CheckoutAddressModel) {
                        recipientAddressModel = checkoutItem.recipientAddressModel
                    }
                    if (checkoutItem is CheckoutOrderModel) {
                        if (loadCourierState(checkoutItem, recipientAddressModel)) {
                            loadShippingSuspend(checkoutItem, index)
                        }
                    }
                    if (checkoutItem is CheckoutEpharmacyModel && checkoutItem.epharmacy.showImageUpload && checkoutItem.epharmacy.consultationFlow) {
                        fetchEpharmacyData()
                    }
                    if (checkoutItem is CheckoutPromoModel) {
                        if (listData.value.promo()!!.entryPointInfo.messages.isEmpty()) {
                            // only validate if not yet done so
                            validatePromo()
                        }
                        listPromoExternalAutoApplyCode = emptyList()
                    }
                }
            }
        }
    }

    private fun loadCourierState(
        shipmentCartItemModel: CheckoutOrderModel,
        recipientAddressModel: RecipientAddressModel?,
        setHasLoadCourierState: Boolean = true
    ): Boolean {
        if (!shipmentCartItemModel.isCustomPinpointError && !shipmentCartItemModel.isStateHasLoadCourierState && shouldAutoLoadCourier(
                shipmentCartItemModel,
                recipientAddressModel
            )
        ) {
            shipmentCartItemModel.isStateHasLoadCourierState = setHasLoadCourierState
            return true
        }
        return false
    }

    fun changeAddress(
        newRecipientAddressModel: RecipientAddressModel?,
        chosenAddressModel: ChosenAddressModel?,
        isHandleFallback: Boolean
        // todo: can be true if trade in
    ) {
        viewModelScope.launch(dispatchers.immediate) {
            pageState.value = CheckoutPageState.Loading
            val changeAddressResult = cartProcessor.changeShippingAddress(
                listData.value,
                newRecipientAddressModel,
                chosenAddressModel,
                isOneClickShipment,
                isTradeIn
            )
            if (changeAddressResult.isSuccess) {
                commonToaster.emit(
                    CheckoutPageToaster(
                        Toaster.TYPE_NORMAL,
                        changeAddressResult.toasterMessage
                    )
                )
                hitClearAllBo()
                loadSAF(
                    isReloadData = true,
                    skipUpdateOnboardingState = true,
                    isReloadAfterPriceChangeHigher = false
                )
            } else {
                commonToaster.emit(
                    CheckoutPageToaster(
                        Toaster.TYPE_ERROR,
                        changeAddressResult.toasterMessage,
                        changeAddressResult.throwable
                    )
                )
                pageState.value = CheckoutPageState.Normal
//                if (isHandleFallback) {
//                    // todo test this in trade in
//                    val address = listData.value.address()?.recipientAddressModel
//                    if (address != null) {
//                        if (address.selectedTabIndex == RecipientAddressModel.TAB_ACTIVE_ADDRESS_DEFAULT) {
//                            address.selectedTabIndex =
//                                RecipientAddressModel.TAB_ACTIVE_ADDRESS_TRADE_IN
//                            address.isIgnoreSelectionAction = true
//                        } else if (address.selectedTabIndex == RecipientAddressModel.TAB_ACTIVE_ADDRESS_TRADE_IN) {
//                            address.locationDataModel = null
//                            address.dropOffAddressDetail = ""
//                            address.dropOffAddressName = ""
//                        }
//                        listData.value = listData.value
//                    }
//                }
            }
        }
    }

    fun editAddressPinpoint(
        latitude: String,
        longitude: String,
        locationPass: LocationPass?,
        onError: (message: String, locationPass: LocationPass?) -> Unit
    ) {
        viewModelScope.launch(dispatchers.immediate) {
            pageState.value = CheckoutPageState.Loading
            val editAddressResult =
                logisticCartProcessor.editAddressPinpoint(
                    latitude,
                    longitude,
                    recipientAddressModel
                )
            pageState.value = CheckoutPageState.Normal
            if (editAddressResult.isSuccess) {
                loadSAF(
                    isReloadData = true,
                    skipUpdateOnboardingState = true,
                    isReloadAfterPriceChangeHigher = false
                )
            } else if (editAddressResult.errorMessage.isNotEmpty()) {
                onError.invoke(editAddressResult.errorMessage, locationPass)
            } else {
                commonToaster.emit(
                    CheckoutPageToaster(
                        Toaster.TYPE_ERROR,
                        editAddressResult.errorMessage,
                        editAddressResult.throwable
                    )
                )
            }
        }
    }

    private suspend fun fetchEpharmacyData() {
        val result = addOnProcessor.fetchEpharmacyData(listData.value)
        if (result != null) {
            listData.value = result!!
            pageState.value = CheckoutPageState.EpharmacyCoachMark
            calculateTotal()
        }
    }

    private fun cleanPromoFromPromoRequest(promoRequest: PromoRequest): PromoRequest {
        return promoRequest.copy(
            codes = arrayListOf(),
            orders = promoRequest.orders.map {
                return@map it.copy(codes = mutableListOf())
            }
        )
    }

    private suspend fun getEntryPointInfo(
        checkoutItems: List<CheckoutItem>,
        oldCheckoutItems: List<CheckoutItem>
    ): List<CheckoutItem> {
        if (isPromoRevamp == true) {
            val checkoutModel =
                checkoutItems.firstOrNull { it is CheckoutPromoModel } as? CheckoutPromoModel
            val oldCheckoutModel =
                oldCheckoutItems.firstOrNull { it is CheckoutPromoModel } as? CheckoutPromoModel

            if (checkoutModel != null && oldCheckoutModel != null) {
                val entryPointInfo = promoProcessor
                    .getEntryPointInfo(
                        cleanPromoFromPromoRequest(
                            generateCouponListRecommendationRequestWithListData(checkoutItems)
                        )
                    )
                return checkoutItems.map { model ->
                    if (model is CheckoutPromoModel) {
                        return@map model.copy(
                            entryPointInfo = entryPointInfo,
                            isLoading = false,
                            isAnimateWording = shouldAnimateEntryPointWording(
                                checkoutModel.promo,
                                oldCheckoutModel.promo
                            )
                        )
                    } else {
                        return@map model
                    }
                }
            }
        }
        return checkoutItems
    }

    fun reloadEntryPointInfo() {
        viewModelScope.launch(dispatchers.immediate) {
            val data = listData.value
            val newData = getEntryPointInfo(data, data)
            withContext(dispatchers.main) {
                listData.value = newData
            }
        }
    }

    fun shouldAnimateEntryPointWording(
        newLastApply: LastApplyUiModel,
        oldLastApply: LastApplyUiModel
    ): Boolean {
        val oldTotalPromoAmount =
            oldLastApply.additionalInfo.usageSummaries.sumOf { it.amount }
        val newTotalPromoAmount =
            newLastApply.additionalInfo.usageSummaries.sumOf { it.amount }
        return oldTotalPromoAmount in (1 until newTotalPromoAmount)
    }

    fun setPrescriptionIds(prescriptionIds: ArrayList<String>) {
        addOnProcessor.setPrescriptionIds(prescriptionIds, listData.value)
    }

    fun setMiniConsultationResult(results: ArrayList<EPharmacyMiniConsultationResult>) {
        pageState.value = CheckoutPageState.Loading
        val result = addOnProcessor.setMiniConsultationResult(results, listData.value)
        if (result != null) {
            listData.value = result!!
            pageState.value = CheckoutPageState.Normal
            calculateTotal()
        }
    }

    private fun calculateTotalWithoutPayment() {
        listData.value = calculator.calculateTotalAndUpdateButtonPaymentWithoutPaymentData(
            listData.value,
            isTradeInByDropOff,
            summariesAddOnUiModel
        )
    }

    internal fun calculateTotal(skipPaymentMandatoryHit: Boolean = false) {
        viewModelScope.launch(dispatchers.immediate) {
            calculateTotalWithoutPayment()
            val shouldGetPayment = shouldGetPayment(listData.value)
            if (shouldGetPayment) {
                getCheckoutPaymentData(skipPaymentMandatoryHit)
            } else {
                getCheckoutPlatformFee()
            }
        }
    }

    private suspend fun getCheckoutPlatformFee() {
        var cost = listData.value.cost()!!
        val paymentFeeCheckoutRequest = PaymentFeeCheckoutRequest(
            gatewayCode = "",
            profileCode = shipmentPlatformFeeData.profileCode,
            paymentAmount = cost.totalPrice,
            additionalData = shipmentPlatformFeeData.additionalData
        )
        cost = paymentProcessor.checkPlatformFee(
            shipmentPlatformFeeData,
            cost,
            paymentFeeCheckoutRequest
        )
        listData.value =
            calculator.updateButtonPaymentWithoutPaymentData(
                listData.value,
                cost,
                isTradeInByDropOff,
                summariesAddOnUiModel
            )
    }

    fun updateAddOnGiftingProductLevelDataBottomSheet(saveAddOnStateResult: SaveAddOnStateResult) {
        val checkoutItems = listData.value.toMutableList()
        for (addOnResult in saveAddOnStateResult.addOns) {
            for (item in checkoutItems.withIndex()) {
                val product = item.value
                if (product is CheckoutProductModel) {
                    val keyProductLevel =
                        "${product.cartStringGroup}-${product.cartId}"
                    if (keyProductLevel.equals(addOnResult.addOnKey, ignoreCase = true)) {
                        checkoutItems[item.index] = product.copy(
                            addOnGiftingProductLevelModel = setAddOnsGiftingData(
                                product.addOnGiftingProductLevelModel.copy(),
                                addOnResult
                            )
                        )
                    }
                }
            }
        }
        listData.value = checkoutItems
        calculateTotal()
    }

    fun updateAddOnGiftingOrderLevelDataBottomSheet(saveAddOnStateResult: SaveAddOnStateResult) {
        val checkoutItems = listData.value.toMutableList()
        for (addOnResult in saveAddOnStateResult.addOns) {
            for (item in checkoutItems.withIndex()) {
                val order = item.value
                if (order is CheckoutOrderModel && (order.cartStringGroup + "-0").equals(
                        addOnResult.addOnKey,
                        ignoreCase = true
                    )
                ) {
                    checkoutItems[item.index] = order.copy(
                        addOnsOrderLevelModel = setAddOnsGiftingData(
                            order.addOnsOrderLevelModel.copy(),
                            addOnResult
                        )
                    )
                }
            }
        }
        listData.value = checkoutItems
        calculateTotal()
    }

    private fun setAddOnsGiftingData(
        addOnsDataModel: AddOnGiftingDataModel,
        addOnResult: AddOnResult
    ): AddOnGiftingDataModel {
        addOnsDataModel.status = addOnResult.status
        val addOnButton = addOnResult.addOnButton
        addOnsDataModel.addOnsButtonModel = AddOnGiftingButtonModel(
            addOnButton.leftIconUrl,
            addOnButton.rightIconUrl,
            addOnButton.description,
            addOnButton.action,
            addOnButton.title
        )
        val addOnBottomSheet = addOnResult.addOnBottomSheet
        addOnsDataModel.addOnsBottomSheetModel = AddOnGiftingBottomSheetModel(
            ticker = AddOnGiftingTickerModel(
                text = addOnBottomSheet.ticker.text
            ),
            headerTitle = addOnBottomSheet.headerTitle,
            description = addOnBottomSheet.description,
            products = addOnBottomSheet.products.map {
                AddOnGiftingProductItemModel(
                    productName = it.productName,
                    productImageUrl = it.productImageUrl
                )
            }
        )
        addOnsDataModel.addOnsDataItemModelList = addOnResult.addOnData.map {
            val addOnNote = it.addOnMetadata.addOnNote
            AddOnGiftingDataItemModel(
                addOnId = it.addOnId,
                addOnUniqueId = it.addOnUniqueId,
                addOnPrice = it.addOnPrice,
                addOnQty = it.addOnQty.toLong(),
                addOnMetadata = AddOnGiftingMetadataItemModel(
                    AddOnGiftingNoteItemModel(
                        addOnNote.isCustomNote,
                        addOnNote.to,
                        addOnNote.from,
                        addOnNote.notes
                    )
                )
            )
        }
        return addOnsDataModel
    }

    fun generateRatesParam(order: CheckoutOrderModel, mvcPromoCode: String): RatesParam {
        return logisticProcessor.getRatesParam(
            order,
            helper.getOrderProducts(listData.value, order.cartStringGroup),
            listData.value.address()!!.recipientAddressModel,
            isTradeIn,
            isTradeInByDropOff,
            codData,
            cartDataForRates,
            mvcPromoCode,
            true,
            listData.value.promo()!!
        )
    }

    fun loadShipping(order: CheckoutOrderModel, cartPosition: Int) {
        viewModelScope.launch(dispatchers.immediate) {
            loadShippingSuspend(order, cartPosition)
        }
    }

    private suspend fun loadShippingSuspend(order: CheckoutOrderModel, cartPosition: Int) {
        when (order.shippingComponents) {
            ShippingComponents.SCHELLY_WITH_RATES -> {
                loadShippingWithSelly(order = order, cartPosition = cartPosition)
            }

            ShippingComponents.SCHELLY -> {
                loadSelly(cartPosition = cartPosition, order = order)
            }

            ShippingComponents.RATES -> {
                loadShippingNormal(cartPosition = cartPosition, order = order)
            }
        }
    }

    private suspend fun loadSelly(
        cartPosition: Int,
        order: CheckoutOrderModel
    ) {
        val checkoutItems = listData.value.toMutableList()
        val checkoutOrderModel = checkoutItems[cartPosition] as CheckoutOrderModel
        checkoutItems[cartPosition] = checkoutOrderModel.copy(
            shipment = checkoutOrderModel.shipment.copy(isLoading = true),
            isStateHasLoadCourierState = true
        )
        listData.value = checkoutItems

        val schellyParam = logisticProcessor.getSchellyProcessorParam(
            order,
            helper.getOrderProducts(checkoutItems, order.cartStringGroup),
            listData.value.address()!!.recipientAddressModel,
            isTradeIn,
            isTradeInByDropOff,
            codData,
            cartDataForRates,
            "",
            false,
            listData.value.promo()!!,
            order.fulfillmentId.toString(),
            order.isRecommendScheduleDelivery,
            order.startDate
        )

        val result = logisticCartProcessor.getScheduleDelivery(
            schellyParam
        )
        val list = listData.value.toMutableList()
        val orderModel = list[cartPosition] as? CheckoutOrderModel
        if (orderModel != null) {
            val courierItemData = result?.courier
            if (courierItemData != null) {
                logisticProcessor.handleSyncShipmentCartItemModel(courierItemData, order)
                orderModel.validationMetadata = order.validationMetadata
                val shouldValidatePromo =
                    courierItemData.selectedShipper.logPromoCode != null && courierItemData.selectedShipper.logPromoCode!!.isNotEmpty()
                if (shouldValidatePromo) {
                    val validateUsePromoRequest = generateValidateUsePromoRequest()
                    for (ordersItem in validateUsePromoRequest.orders) {
                        if (ordersItem.cartStringGroup == orderModel.cartStringGroup) {
                            if (!ordersItem.codes.contains(
                                    courierItemData.selectedShipper.logPromoCode
                                )
                            ) {
                                ordersItem.codes.add(
                                    courierItemData.selectedShipper.logPromoCode!!
                                )
                                ordersItem.boCode =
                                    courierItemData.selectedShipper.logPromoCode!!
                            }
                            ordersItem.shippingId =
                                courierItemData.selectedShipper.shipperId
                            ordersItem.spId =
                                courierItemData.selectedShipper.shipperProductId
                            ordersItem.freeShippingMetadata =
                                courierItemData.selectedShipper.freeShippingMetadata
                            ordersItem.boCampaignId =
                                courierItemData.selectedShipper.boCampaignId
                            ordersItem.shippingSubsidy =
                                courierItemData.selectedShipper.shippingSubsidy
                            ordersItem.benefitClass =
                                courierItemData.selectedShipper.benefitClass
                            ordersItem.shippingPrice =
                                courierItemData.selectedShipper.shippingRate.toDouble()
                            ordersItem.etaText =
                                courierItemData.selectedShipper.etaText!!
                            ordersItem.validationMetadata =
                                orderModel.validationMetadata
                        }
                    }
                    doValidateUseLogisticPromo(
                        cartPosition,
                        orderModel.cartStringGroup,
                        validateUsePromoRequest,
                        courierItemData.selectedShipper.logPromoCode!!,
                        true,
                        courierItemData
                    )
                    return
                }
            }

            // handle error
            val ratesError = result?.ratesError
            val boPromoCode = getBoPromoCode(order)
            if (ratesError != null) {
                if (ratesError is MessageErrorException) {
                    CheckoutLogger.logOnErrorLoadCourierNew(
                        ratesError,
                        order,
                        isOneClickShipment,
                        isTradeIn,
                        isTradeInByDropOff,
                        boPromoCode
                    )
                } else if (ratesError is AkamaiErrorException) {
                    ratesError.message?.let {
                        pageState.value = CheckoutPageState.AkamaiRatesError(it)
                    }
                }
            }

            val newOrderModel = orderModel.copy(
                shipment = orderModel.shipment.copy(
                    isLoading = false,
                    courierItemData = result?.courier,
                    shippingCourierUiModels = emptyList(),
                    insurance = result?.courier?.run {
                        generateCheckoutOrderInsuranceFromCourier(
                            this,
                            order
                        )
                    } ?: CheckoutOrderInsurance(),
                    isHasShownCourierError = false
                )
            )
            list[cartPosition] = newOrderModel
            listData.value = list
            cartProcessor.processSaveShipmentState(
                newOrderModel,
                listData.value.address()!!.recipientAddressModel,
                listData.value
            )
            calculateTotalWithoutPayment()
            sendEEStep3()
        }
    }

    private suspend fun loadShippingWithSelly(
        cartPosition: Int,
        order: CheckoutOrderModel
    ) {
        val checkoutItems = listData.value.toMutableList()
        val checkoutOrderModel = checkoutItems[cartPosition] as CheckoutOrderModel
        checkoutItems[cartPosition] = checkoutOrderModel.copy(
            shipment = checkoutOrderModel.shipment.copy(isLoading = true),
            isStateHasLoadCourierState = true
        )
        listData.value = checkoutItems

        val ratesParam = logisticProcessor.getRatesProcessorParam(
            order,
            helper.getOrderProducts(checkoutItems, order.cartStringGroup),
            listData.value.address()!!.recipientAddressModel,
            isTradeIn,
            isTradeInByDropOff,
            codData,
            cartDataForRates,
            "",
            false,
            listData.value.promo()!!
        )
        val schellyParam = logisticProcessor.getSchellyProcessorParam(
            order,
            helper.getOrderProducts(checkoutItems, order.cartStringGroup),
            listData.value.address()!!.recipientAddressModel,
            isTradeIn,
            isTradeInByDropOff,
            codData,
            cartDataForRates,
            "",
            false,
            listData.value.promo()!!,
            order.fulfillmentId.toString(),
            order.isRecommendScheduleDelivery,
            order.startDate
        )

        val result = logisticCartProcessor.getRatesWithScheduleDelivery(
            ratesParam,
            schellyParam
        )
        val list = listData.value.toMutableList()
        val orderModel = list[cartPosition] as? CheckoutOrderModel
        if (orderModel != null) {
            val courierItemData = result?.courier
            if (courierItemData != null) {
                logisticProcessor.handleSyncShipmentCartItemModel(courierItemData, order)
                orderModel.validationMetadata = order.validationMetadata
                val shouldValidatePromo =
                    courierItemData.selectedShipper.logPromoCode != null && courierItemData.selectedShipper.logPromoCode!!.isNotEmpty()
                if (shouldValidatePromo) {
                    val validateUsePromoRequest = generateValidateUsePromoRequest()
                    for (ordersItem in validateUsePromoRequest.orders) {
                        if (ordersItem.cartStringGroup == orderModel.cartStringGroup) {
                            if (!ordersItem.codes.contains(
                                    courierItemData.selectedShipper.logPromoCode
                                )
                            ) {
                                ordersItem.codes.add(
                                    courierItemData.selectedShipper.logPromoCode!!
                                )
                                ordersItem.boCode =
                                    courierItemData.selectedShipper.logPromoCode!!
                            }
                            ordersItem.shippingId =
                                courierItemData.selectedShipper.shipperId
                            ordersItem.spId =
                                courierItemData.selectedShipper.shipperProductId
                            ordersItem.freeShippingMetadata =
                                courierItemData.selectedShipper.freeShippingMetadata
                            ordersItem.boCampaignId =
                                courierItemData.selectedShipper.boCampaignId
                            ordersItem.shippingSubsidy =
                                courierItemData.selectedShipper.shippingSubsidy
                            ordersItem.benefitClass =
                                courierItemData.selectedShipper.benefitClass
                            ordersItem.shippingPrice =
                                courierItemData.selectedShipper.shippingRate.toDouble()
                            ordersItem.etaText =
                                courierItemData.selectedShipper.etaText!!
                            ordersItem.validationMetadata =
                                orderModel.validationMetadata
                        }
                    }
                    doValidateUseLogisticPromo(
                        cartPosition,
                        orderModel.cartStringGroup,
                        validateUsePromoRequest,
                        courierItemData.selectedShipper.logPromoCode!!,
                        true,
                        courierItemData
                    )
                    return
                }
            }
            // handle error
            val ratesError = result?.ratesError
            if (ratesError != null) {
                if (ratesError is MessageErrorException) {
                    val boPromoCode = getBoPromoCode(order)
                    CheckoutLogger.logOnErrorLoadCourierNew(
                        ratesError,
                        order,
                        isOneClickShipment,
                        isTradeIn,
                        isTradeInByDropOff,
                        boPromoCode
                    )
                } else if (ratesError is AkamaiErrorException) {
                    ratesError.message?.let {
                        pageState.value = CheckoutPageState.AkamaiRatesError(it)
                    }
                }
            }

            val newOrderModel = orderModel.copy(
                shipment = orderModel.shipment.copy(
                    isLoading = false,
                    courierItemData = result?.courier,
                    shippingCourierUiModels = result?.couriers ?: emptyList(),
                    insurance = result?.courier?.run {
                        generateCheckoutOrderInsuranceFromCourier(
                            this,
                            order
                        )
                    } ?: CheckoutOrderInsurance(),
                    isHasShownCourierError = false
                )
            )
            list[cartPosition] = newOrderModel
            listData.value = list
            cartProcessor.processSaveShipmentState(
                newOrderModel,
                listData.value.address()!!.recipientAddressModel,
                listData.value
            )
            calculateTotalWithoutPayment()
            sendEEStep3()
        }
    }

    private suspend fun loadShippingNormal(
        cartPosition: Int,
        order: CheckoutOrderModel
    ) {
        val checkoutItems = listData.value.toMutableList()
        val checkoutOrderModel = checkoutItems[cartPosition] as CheckoutOrderModel
        checkoutItems[cartPosition] = checkoutOrderModel.copy(
            shipment = checkoutOrderModel.shipment.copy(isLoading = true),
            isStateHasLoadCourierState = true
        )
        listData.value = checkoutItems

        val ratesParam = logisticProcessor.getRatesProcessorParam(
            order,
            helper.getOrderProducts(checkoutItems, order.cartStringGroup),
            listData.value.address()!!.recipientAddressModel,
            isTradeIn,
            isTradeInByDropOff,
            codData,
            cartDataForRates,
            "",
            false,
            listData.value.promo()!!
        )

        val result = logisticCartProcessor.getRatesGeneral(ratesParam)
        val list = listData.value.toMutableList()
        val orderModel = list[cartPosition] as? CheckoutOrderModel
        if (orderModel != null) {
            val courierItemData = result?.courier
            if (courierItemData != null) {
                val shouldValidatePromo =
                    courierItemData.selectedShipper.logPromoCode != null && courierItemData.selectedShipper.logPromoCode!!.isNotEmpty()
                if (shouldValidatePromo) {
                    val validateUsePromoRequest = generateValidateUsePromoRequest()
                    for (ordersItem in validateUsePromoRequest.orders) {
                        if (ordersItem.cartStringGroup == orderModel.cartStringGroup) {
                            if (!ordersItem.codes.contains(
                                    courierItemData.selectedShipper.logPromoCode
                                )
                            ) {
                                ordersItem.codes.add(
                                    courierItemData.selectedShipper.logPromoCode!!
                                )
                                ordersItem.boCode =
                                    courierItemData.selectedShipper.logPromoCode!!
                            }
                            ordersItem.shippingId =
                                courierItemData.selectedShipper.shipperId
                            ordersItem.spId =
                                courierItemData.selectedShipper.shipperProductId
                            ordersItem.freeShippingMetadata =
                                courierItemData.selectedShipper.freeShippingMetadata
                            ordersItem.boCampaignId =
                                courierItemData.selectedShipper.boCampaignId
                            ordersItem.shippingSubsidy =
                                courierItemData.selectedShipper.shippingSubsidy
                            ordersItem.benefitClass =
                                courierItemData.selectedShipper.benefitClass
                            ordersItem.shippingPrice =
                                courierItemData.selectedShipper.shippingRate.toDouble()
                            ordersItem.etaText =
                                courierItemData.selectedShipper.etaText!!
                            ordersItem.validationMetadata =
                                orderModel.validationMetadata
                        }
                    }
                    doValidateUseLogisticPromo(
                        cartPosition,
                        orderModel.cartStringGroup,
                        validateUsePromoRequest,
                        courierItemData.selectedShipper.logPromoCode!!,
                        false,
                        courierItemData
                    )
                    return
                }
            }

            // handle error
            val ratesError = result?.ratesError
            if (ratesError != null) {
                if (ratesError is MessageErrorException) {
                    val boPromoCode = getBoPromoCode(order)
                    CheckoutLogger.logOnErrorLoadCourierNew(
                        ratesError,
                        order,
                        isOneClickShipment,
                        isTradeIn,
                        isTradeInByDropOff,
                        boPromoCode
                    )
                    ratesError.message?.let {
                        pageState.value = CheckoutPageState.MessageErrorException(it)
                    }
                } else if (ratesError is AkamaiErrorException) {
                    ratesError.message?.let {
                        pageState.value = CheckoutPageState.AkamaiRatesError(it)
                    }
                } else if (order.shouldResetCourier) {
                    order.shouldResetCourier = false
                }
            }

            if (orderModel.boCode.isNotEmpty()) {
                promoProcessor.clearPromo(
                    ClearPromoOrder(
                        orderModel.boUniqueId,
                        orderModel.boMetadata.boType,
                        arrayListOf(orderModel.boCode),
                        orderModel.shopId,
                        orderModel.isProductIsPreorder,
                        orderModel.preOrderDurationDay.toString(),
                        orderModel.fulfillmentId,
                        orderModel.cartStringGroup
                    ),
                    cartType
                )
                commonToaster.emit(
                    CheckoutPageToaster(
                        Toaster.TYPE_NORMAL,
                        BO_DEFAULT_ERROR_MESSAGE
                    )
                )
            }
            val newOrderModel = orderModel.copy(
                shipment = orderModel.shipment.copy(
                    isLoading = false,
                    courierItemData = result?.courier,
                    shippingCourierUiModels = result?.couriers ?: emptyList(),
                    insurance = result?.courier?.run {
                        generateCheckoutOrderInsuranceFromCourier(
                            this,
                            order
                        )
                    } ?: CheckoutOrderInsurance(),
                    isHasShownCourierError = false
                )
            )
            list[cartPosition] = newOrderModel
            listData.value = list
            cartProcessor.processSaveShipmentState(
                newOrderModel,
                listData.value.address()!!.recipientAddressModel,
                listData.value
            )
            calculateTotalWithoutPayment()
            sendEEStep3()
        }
    }

    fun setSelectedCourier(
        cartPosition: Int,
        courierItemData: CourierItemData,
        shippingCourierUiModels: List<ShippingCourierUiModel>
    ) {
        viewModelScope.launch(dispatchers.immediate) {
            pageState.value = CheckoutPageState.Loading
            val checkoutItems = listData.value.toMutableList()
            val checkoutOrderModel = checkoutItems[cartPosition] as CheckoutOrderModel
            val shipment = checkoutOrderModel.shipment
            shippingCourierUiModels.forEach {
                it.isSelected = it.productData.shipperProductId == courierItemData.shipperProductId
            }
            if (shipment.courierItemData?.selectedShipper?.logPromoCode?.isNotEmpty() == true) {
                checkoutItems[cartPosition] = checkoutOrderModel.copy(
                    shipment = shipment.copy(
                        isLoading = true
                    )
                )
                listData.value = checkoutItems
                val success = promoProcessor.clearPromoValidate(
                    ClearPromoOrder(
                        checkoutOrderModel.boUniqueId,
                        checkoutOrderModel.boMetadata.boType,
                        arrayListOf(shipment.courierItemData.selectedShipper.logPromoCode!!),
                        checkoutOrderModel.shopId,
                        checkoutOrderModel.isProductIsPreorder,
                        checkoutOrderModel.preOrderDurationDay.toString(),
                        checkoutOrderModel.fulfillmentId,
                        checkoutOrderModel.cartStringGroup
                    ),
                    cartType
                )
                if (!success) {
                    val items = listData.value.toMutableList()
                    val newShipment = shipment.copy(
                        isLoading = false
                    )
                    val newOrder = checkoutOrderModel.copy(shipment = newShipment)
                    items[cartPosition] = newOrder
                    listData.value = items
                    pageState.value = CheckoutPageState.Normal
                    toasterProcessor.commonToaster.emit(CheckoutPageToaster(Toaster.TYPE_ERROR))
                    return@launch
                }
            }
            val list = listData.value.toMutableList()
            var newOrder = list[cartPosition] as CheckoutOrderModel
            val newShipment = shipment.copy(
                isLoading = false,
                courierItemData = courierItemData,
                shippingCourierUiModels = shippingCourierUiModels,
                insurance = generateCheckoutOrderInsuranceFromCourier(courierItemData, newOrder),
                isHasShownCourierError = false
            )
            newOrder = newOrder.copy(shipment = newShipment, isShippingBorderRed = false)
            list[cartPosition] = newOrder
            listData.value = list
            cartProcessor.processSaveShipmentState(
                newOrder,
                listData.value.address()!!.recipientAddressModel,
                listData.value
            )
            validatePromo()
            pageState.value = CheckoutPageState.Normal
        }
    }

    private fun generateCheckoutOrderInsuranceFromInsuranceData(
        insurance: InsuranceData,
        checkoutOrderModel: CheckoutOrderModel
    ): CheckoutOrderInsurance {
        return CheckoutOrderInsurance(
            when (insurance.insuranceType) {
                InsuranceConstant.INSURANCE_TYPE_MUST -> {
                    true
                }

                InsuranceConstant.INSURANCE_TYPE_NO -> {
                    false
                }

                InsuranceConstant.INSURANCE_TYPE_OPTIONAL -> {
                    insurance.insuranceUsedDefault == InsuranceConstant.INSURANCE_USED_DEFAULT_YES || checkoutOrderModel.isInsurance
                }

                else -> false
            }
        )
    }

    private fun getBoPromoCode(
        shipmentCartItemModel: CheckoutOrderModel
    ): String {
        if (isBoUnstackEnabled) {
            return shipmentCartItemModel.boCode
        }
        return ""
    }

    fun generateValidateUsePromoRequest(list: List<CheckoutItem>? = null): ValidateUsePromoRequest {
        return promoProcessor.generateValidateUsePromoRequest(
            list ?: listData.value,
            isTradeIn,
            isTradeInByDropOff,
            isOneClickShipment,
            cartType
        )
    }

    fun generateValidateUsePromoRequestForPromoUsage(): ValidateUsePromoRequest {
        return promoProcessor.generateValidateUsePromoRequestForPromoUsage(
            listData.value,
            isTradeIn,
            isTradeInByDropOff,
            isOneClickShipment,
            cartType
        )
    }

    fun generateCouponListRecommendationRequest(): PromoRequest {
        return promoProcessor.generateCouponListRecommendationRequest(
            listData.value,
            isTradeIn,
            isTradeInByDropOff,
            isOneClickShipment,
            cartType
        )
    }

    fun generateCouponListRecommendationRequestWithListData(newListData: List<CheckoutItem>): PromoRequest {
        return promoProcessor.generateCouponListRecommendationRequest(
            newListData,
            isTradeIn,
            isTradeInByDropOff,
            isOneClickShipment,
            cartType
        )
    }

    fun getBboPromoCodes(): ArrayList<String> {
        return ArrayList(promoProcessor.bboPromoCodes)
    }

    private suspend fun validatePromo(skipEE: Boolean = false, skipPaymentMandatoryHit: Boolean = false) {
        val checkoutItems = listData.value.toMutableList()
        var newItems = promoProcessor.validateUse(
            promoProcessor.generateValidateUsePromoRequest(
                checkoutItems,
                isTradeIn,
                isTradeInByDropOff,
                isOneClickShipment,
                cartType
            ),
            checkoutItems,
            isOneClickShipment,
            isTradeIn,
            isTradeInByDropOff,
            cartType
        )
        newItems = getEntryPointInfo(newItems, checkoutItems)
        newItems = checkCrossSellImpressionState(newItems)
        listData.value = newItems
        calculateTotal(skipPaymentMandatoryHit)
        if (!skipEE) {
            sendEEStep3()
        }
    }

    fun doValidateUseLogisticPromoNew(
        cartPosition: Int,
        cartString: String,
        promoCode: String,
        showLoading: Boolean,
        courierItemData: CourierItemData
    ) {
        viewModelScope.launch(dispatchers.main) {
            if (showLoading) {
                pageState.value = CheckoutPageState.Loading
                val checkoutItems = listData.value.toMutableList()
                val checkoutOrderModel = checkoutItems[cartPosition] as CheckoutOrderModel
                checkoutItems[cartPosition] =
                    checkoutOrderModel.copy(
                        shipment = checkoutOrderModel.shipment.copy(isLoading = true),
                        isShippingBorderRed = false
                    )
                listData.value = checkoutItems
            }
            val shipmentCartItemModel = listData.value[cartPosition] as CheckoutOrderModel
            val validateUsePromoRequest = generateValidateUsePromoRequest().copy()
            if (promoCode.isNotEmpty()) {
                for (order in validateUsePromoRequest.orders) {
                    if (order.cartStringGroup == shipmentCartItemModel.cartStringGroup && !order.codes.contains(
                            promoCode
                        )
                    ) {
                        if (shipmentCartItemModel.shipment.courierItemData?.selectedShipper?.logPromoCode != null) {
                            // remove previous logistic promo code
                            order.codes.remove(shipmentCartItemModel.shipment.courierItemData.selectedShipper.logPromoCode)
                        }
                        order.codes.add(promoCode)
                        order.boCode = promoCode
                    }
                }
            }
            for (ordersItem in validateUsePromoRequest.orders) {
                if (ordersItem.cartStringGroup == shipmentCartItemModel.cartStringGroup) {
                    ordersItem.spId = courierItemData.shipperProductId
                    ordersItem.shippingId = courierItemData.shipperId
                    ordersItem.freeShippingMetadata = courierItemData.freeShippingMetadata
                    ordersItem.boCampaignId = courierItemData.boCampaignId
                    ordersItem.shippingSubsidy = courierItemData.shippingSubsidy
                    ordersItem.benefitClass = courierItemData.benefitClass
                    ordersItem.shippingPrice = courierItemData.shippingRate.toDouble()
                    ordersItem.etaText = courierItemData.etaText!!
                }
            }
            doValidateUseLogisticPromo(
                cartPosition,
                cartString,
                validateUsePromoRequest,
                promoCode,
                false,
                courierItemData
            )
        }
    }

    private suspend fun doValidateUseLogisticPromo(
        cartPosition: Int,
        cartString: String,
        validateUsePromoRequest: ValidateUsePromoRequest,
        promoCode: String,
        showLoading: Boolean,
        courierItemData: CourierItemData
    ) {
        if (showLoading) {
            pageState.value = CheckoutPageState.Loading
            val checkoutItems = listData.value.toMutableList()
            val checkoutOrderModel = checkoutItems[cartPosition] as CheckoutOrderModel
            checkoutItems[cartPosition] =
                checkoutOrderModel.copy(
                    shipment = checkoutOrderModel.shipment.copy(isLoading = true),
                    isShippingBorderRed = false
                )
            listData.value = checkoutItems
        }
        var newItems = promoProcessor.validateUseLogisticPromo(
            validateUsePromoRequest,
            cartString,
            promoCode,
            listData.value,
            courierItemData,
            isOneClickShipment,
            isTradeIn,
            isTradeInByDropOff,
            cartType
        )
        newItems = getEntryPointInfo(newItems, listData.value)
        listData.value = newItems
        cartProcessor.processSaveShipmentState(
            listData.value,
            listData.value.address()!!.recipientAddressModel
        )
        pageState.value = CheckoutPageState.Normal
        calculateTotal()
        sendEEStep3()
    }

    fun doShipmentAction(shipmentAction: ShipmentAction) {
        this.shipmentAction = shipmentAction.action
        loadSAF(
            isReloadData = true,
            skipUpdateOnboardingState = true,
            isReloadAfterPriceChangeHigher = false
        )
    }

    fun setSelectedScheduleDelivery(
        cartPosition: Int,
        order: CheckoutOrderModel,
        courierItemData: CourierItemData,
        scheduleDeliveryUiModel: ScheduleDeliveryUiModel,
        newCourierItemData: CourierItemData
    ) {
        viewModelScope.launch(dispatchers.immediate) {
            val shipmentAction =
                order.shipmentAction[newCourierItemData.selectedShipper.shipperProductId.toLong()]
            if (shipmentAction != null && !shipmentAction.action.equals(
                    this@CheckoutViewModel.shipmentAction,
                    ignoreCase = true
                )
            ) {
                if (shipmentAction.popup.title.isEmpty() && shipmentAction.popup.body.isEmpty()) {
                    doShipmentAction(shipmentAction)
                    return@launch
                } else {
                    pageState.value = CheckoutPageState.ShipmentActionPopUpConfirmation(
                        order.cartStringGroup,
                        shipmentAction
                    )
                    return@launch
                }
            }
            pageState.value = CheckoutPageState.Loading
            if (courierItemData.selectedShipper.logPromoCode.isNullOrEmpty() && newCourierItemData.selectedShipper.logPromoCode.isNullOrEmpty()) {
                setSelectedScheduleDeliveryWithNoPromo(
                    cartPosition,
                    newCourierItemData,
                    scheduleDeliveryUiModel
                )
            } else if (!courierItemData.selectedShipper.logPromoCode.isNullOrEmpty() && newCourierItemData.selectedShipper.logPromoCode.isNullOrEmpty()) {
                setSelectedScheduleDeliveryWithClearOldPromo(
                    cartPosition,
                    newCourierItemData,
                    scheduleDeliveryUiModel,
                    courierItemData
                )
            } else {
                setSelectedScheduleDeliveryWithApplyPromo(
                    courierItemData,
                    newCourierItemData,
                    order,
                    cartPosition,
                    scheduleDeliveryUiModel
                )
            }
        }
    }

    private suspend fun setSelectedScheduleDeliveryWithApplyPromo(
        courierItemData: CourierItemData,
        newCourierItemData: CourierItemData,
        order: CheckoutOrderModel,
        cartPosition: Int,
        scheduleDeliveryUiModel: ScheduleDeliveryUiModel
    ) {
        // need to apply promo
        if (!courierItemData.selectedShipper.logPromoCode.isNullOrEmpty() && courierItemData.selectedShipper.logPromoCode != newCourierItemData.selectedShipper.logPromoCode) {
            // clear old promo before applying new one
            promoProcessor.clearPromo(
                ClearPromoOrder(
                    order.boUniqueId,
                    order.boMetadata.boType,
                    arrayListOf(courierItemData.selectedShipper.logPromoCode!!),
                    order.shopId,
                    order.isProductIsPreorder,
                    order.preOrderDurationDay.toString(),
                    order.fulfillmentId,
                    order.cartStringGroup
                ),
                cartType
            )
        }
        val validateUsePromoRequest = generateValidateUsePromoRequest()
        val selectedShipper = newCourierItemData.selectedShipper
        if (selectedShipper.logPromoCode != null && selectedShipper.logPromoCode!!.isNotEmpty()) {
            for (orderPromo in validateUsePromoRequest.orders) {
                if (orderPromo.cartStringGroup == order.cartStringGroup && !orderPromo.codes.contains(
                        newCourierItemData.selectedShipper.logPromoCode
                    )
                ) {
                    if (order.shipment.courierItemData?.selectedShipper?.logPromoCode != null) {
                        // remove previous logistic promo code
                        orderPromo.codes.remove(order.shipment.courierItemData.selectedShipper.logPromoCode)
                    }
                    orderPromo.codes.add(selectedShipper.logPromoCode!!)
                    orderPromo.boCode = selectedShipper.logPromoCode!!
                }
            }
        }
        for (ordersItem in validateUsePromoRequest.orders) {
            if (ordersItem.cartStringGroup == order.cartStringGroup) {
                ordersItem.spId = selectedShipper.shipperProductId
                ordersItem.shippingId = selectedShipper.shipperId
                ordersItem.freeShippingMetadata = selectedShipper.freeShippingMetadata
                ordersItem.boCampaignId = selectedShipper.boCampaignId
                ordersItem.shippingSubsidy = selectedShipper.shippingSubsidy
                ordersItem.benefitClass = selectedShipper.benefitClass
                ordersItem.shippingPrice = selectedShipper.shippingRate.toDouble()
                ordersItem.etaText = selectedShipper.etaText!!
                ordersItem.validationMetadata = if (scheduleDeliveryUiModel.isSelected) {
                    scheduleDeliveryUiModel.deliveryProduct.validationMetadata
                } else {
                    ""
                }
            }
        }
        var newItems = promoProcessor.validateUseLogisticPromo(
            validateUsePromoRequest,
            order.cartStringGroup,
            newCourierItemData.selectedShipper.logPromoCode!!,
            listData.value,
            newCourierItemData,
            isOneClickShipment,
            isTradeIn,
            isTradeInByDropOff,
            cartType
        )
        val newOrder = newItems[cartPosition] as CheckoutOrderModel
        if (newOrder.shipment.courierItemData != null) {
            if (scheduleDeliveryUiModel.isSelected) {
                newOrder.scheduleDate = newCourierItemData.selectedShipper.scheduleDate
                newOrder.timeslotId = newCourierItemData.selectedShipper.timeslotId
                newOrder.validationMetadata =
                    scheduleDeliveryUiModel.deliveryProduct.validationMetadata
            } else {
                newOrder.scheduleDate = ""
                newOrder.timeslotId = 0
                newOrder.validationMetadata = ""
            }
        }
        newItems = getEntryPointInfo(newItems, listData.value)
        listData.value = newItems
        cartProcessor.processSaveShipmentState(
            listData.value,
            listData.value.address()!!.recipientAddressModel
        )
        calculateTotal()
        sendEEStep3()
        pageState.value = CheckoutPageState.Normal
    }

    private suspend fun setSelectedScheduleDeliveryWithClearOldPromo(
        cartPosition: Int,
        newCourierItemData: CourierItemData,
        scheduleDeliveryUiModel: ScheduleDeliveryUiModel,
        courierItemData: CourierItemData
    ) {
        // need to clear old promo code
        val checkoutItems = listData.value.toMutableList()
        val checkoutOrderModel = checkoutItems[cartPosition] as CheckoutOrderModel
        val shipment = checkoutOrderModel.shipment
        val newShipment = shipment.copy(
            isLoading = true,
            courierItemData = newCourierItemData,
            insurance = generateCheckoutOrderInsuranceFromCourier(
                newCourierItemData,
                checkoutOrderModel
            )
        )
        val newOrder = checkoutOrderModel.copy(shipment = newShipment)
        if (scheduleDeliveryUiModel.isSelected) {
            newOrder.scheduleDate = newCourierItemData.selectedShipper.scheduleDate
            newOrder.timeslotId = newCourierItemData.selectedShipper.timeslotId
            newOrder.validationMetadata =
                scheduleDeliveryUiModel.deliveryProduct.validationMetadata
        } else {
            newOrder.scheduleDate = ""
            newOrder.timeslotId = 0
            newOrder.validationMetadata = ""
        }
        checkoutItems[cartPosition] = newOrder
        listData.value = checkoutItems
        val shouldClearPromoBenefit = promoProcessor.clearPromo(
            ClearPromoOrder(
                checkoutOrderModel.boUniqueId,
                checkoutOrderModel.boMetadata.boType,
                arrayListOf(courierItemData.selectedShipper.logPromoCode!!),
                checkoutOrderModel.shopId,
                checkoutOrderModel.isProductIsPreorder,
                checkoutOrderModel.preOrderDurationDay.toString(),
                checkoutOrderModel.fulfillmentId,
                checkoutOrderModel.cartStringGroup
            ),
            cartType
        )
        if (shouldClearPromoBenefit) {
            val list = listData.value.toMutableList()
            val newPromo = list.promo()!!.copy(promo = LastApplyUiModel())
            list[list.size - PROMO_INDEX_FROM_BOTTOM] = newPromo
            listData.value = list
        }
        val list = listData.value.toMutableList()
        var newOrder1 = list[cartPosition] as CheckoutOrderModel
        val newShipment1 = shipment.copy(
            isLoading = false,
            courierItemData = newCourierItemData,
            insurance = generateCheckoutOrderInsuranceFromCourier(newCourierItemData, newOrder1),
            isHasShownCourierError = false
        )
        newOrder1 = newOrder1.copy(shipment = newShipment1, isShippingBorderRed = false)
        list[cartPosition] = newOrder1
        listData.value = list
        cartProcessor.processSaveShipmentState(
            newOrder1,
            listData.value.address()!!.recipientAddressModel,
            listData.value
        )
        validatePromo()
        pageState.value = CheckoutPageState.Normal
    }

    private suspend fun setSelectedScheduleDeliveryWithNoPromo(
        cartPosition: Int,
        newCourierItemData: CourierItemData,
        scheduleDeliveryUiModel: ScheduleDeliveryUiModel
    ) {
        // no promo
        val checkoutItems = listData.value.toMutableList()
        val checkoutOrderModel = checkoutItems[cartPosition] as CheckoutOrderModel
        val shipment = checkoutOrderModel.shipment
        val newShipment = shipment.copy(
            isLoading = false,
            courierItemData = newCourierItemData,
            insurance = generateCheckoutOrderInsuranceFromCourier(
                newCourierItemData,
                checkoutOrderModel
            ),
            isHasShownCourierError = false
        )
        val newOrder = checkoutOrderModel.copy(shipment = newShipment)
        if (scheduleDeliveryUiModel.isSelected) {
            newOrder.scheduleDate = newCourierItemData.selectedShipper.scheduleDate
            newOrder.timeslotId = newCourierItemData.selectedShipper.timeslotId
            newOrder.validationMetadata =
                scheduleDeliveryUiModel.deliveryProduct.validationMetadata
        } else {
            newOrder.scheduleDate = ""
            newOrder.timeslotId = 0
            newOrder.validationMetadata = ""
        }
        checkoutItems[cartPosition] = newOrder
        listData.value = checkoutItems
        cartProcessor.processSaveShipmentState(
            newOrder,
            listData.value.address()!!.recipientAddressModel,
            listData.value
        )
        validatePromo()
        pageState.value = CheckoutPageState.Normal
    }

    // region campaign
    fun getCampaignTimer(): CampaignTimerUi? {
        return if (campaignTimer == null || !campaignTimer!!.showTimer) {
            null
        } else {
            // Set necessary analytics attributes to be passed so the gtm will just trigger
            // the method without collecting the data again (quite expensive)
            campaignTimer!!.gtmProductId =
                listData.value.firstOrNullInstanceOf(CheckoutProductModel::class.java)?.productId
                    ?: 0
            campaignTimer!!.gtmUserId = userSessionInterface.userId
            campaignTimer
        }
    }

    fun releaseBooking() {
        cartProcessor.releaseBooking(listData.value)
    }
    // endregion

    fun checkout(
        isPopupConfirmed: Boolean? = false,
        fingerprintPublicKey: String?,
        triggerEpharmacyTracker: (Boolean) -> Unit,
        onSuccessCheckout: (CheckoutResult) -> Unit
    ) {
        viewModelScope.launch(dispatchers.immediate) {
            pageState.value = CheckoutPageState.Loading
            val items = listData.value.toMutableList()
            var firstErrorIndex = -1
            var hasValidOrder = false
            var hasUnselectedCourier = false
            var isPrescriptionFrontEndValidationError = false
            var productErrorPrescriptionCount = 0
            var productSuccessPrescriptionCount = 0
            val checkoutEpharmacy = items.epharmacy()
            var hasInvalidPayment = false
            var hasNoPayment = false
            items.forEachIndexed { index, checkoutItem ->
                if (checkoutItem is CheckoutOrderModel) {
                    if (!checkoutItem.isError && checkoutItem.shipment.courierItemData == null) {
                        items[index] = checkoutItem.copy(
                            isShippingBorderRed = true,
                            isTriggerShippingVibrationAnimation = true
                        )
                        if (firstErrorIndex == -1) {
                            firstErrorIndex = index
                        }
                        hasUnselectedCourier = true
                    } else if (!checkoutItem.isError) {
                        hasValidOrder = true
                    }
                    if (checkoutEpharmacy?.epharmacy?.showImageUpload == true && checkoutEpharmacy.epharmacy.frontEndValidation && checkoutItem.hasEthicalProducts && !checkoutItem.isError) {
                        for (cartItemModel in getOrderProducts(checkoutItem.cartStringGroup)) {
                            if (cartItemModel is CheckoutProductModel && !cartItemModel.isError && cartItemModel.ethicalDrugDataModel.needPrescription) {
                                val prescriptionIdsEmpty =
                                    checkoutItem.prescriptionIds.isEmpty()
                                val consultationEmpty =
                                    checkoutItem.tokoConsultationId.isEmpty() ||
                                        checkoutItem.partnerConsultationId.isEmpty() ||
                                        checkoutItem.tokoConsultationId == "0" ||
                                        checkoutItem.partnerConsultationId == "0" ||
                                        checkoutItem.consultationDataString.isEmpty()
                                if ((prescriptionIdsEmpty && consultationEmpty) || checkoutItem.isBlockCheckoutFlowEPharmacy) {
                                    isPrescriptionFrontEndValidationError = true
                                    productErrorPrescriptionCount += 1
                                } else {
                                    productSuccessPrescriptionCount += 1
                                }
                            }
                        }
                    }
                }
                if (checkoutItem is CheckoutEpharmacyModel) {
                    if (!isPrescriptionFrontEndValidationError) {
                        isPrescriptionFrontEndValidationError = checkoutItem.epharmacy.isBlockCheckoutFlowMessage.isNotEmpty()
                    }
                    if (isPrescriptionFrontEndValidationError) {
                        items[index] =
                            checkoutItem.copy(
                                epharmacy = checkoutItem.epharmacy.copy(
                                    isError = true,
                                    productErrorCount = productErrorPrescriptionCount,
                                    isIncompletePrescriptionError = productSuccessPrescriptionCount > 0
                                )
                            )
                        if (firstErrorIndex == -1) {
                            firstErrorIndex = index
                        }
                    }
                }
                if (checkoutItem is CheckoutPaymentModel) {
                    if (checkoutItem.enable && (!checkoutItem.widget.isValidStateToCheckout || checkoutItem.data == null)) {
                        hasNoPayment = true
                    } else if (checkoutItem.enable && !checkoutItem.widget.isValidToCheckout) {
                        hasInvalidPayment = true
                    } else if (checkoutItem.enable) {
                        val paymentWidgetData = checkoutItem.data?.paymentWidgetData?.firstOrNull()
                        if (paymentWidgetData != null && paymentWidgetData.mandatoryHit.contains(MANDATORY_HIT_CC_TENOR_LIST)) {
                            val selectedTenure = paymentWidgetData.installmentPaymentData.selectedTenure
                            val selectedTenor = checkoutItem.tenorList?.firstOrNull { it.tenure == selectedTenure && !it.disable }
                            if (selectedTenor == null) {
                                hasInvalidPayment = true
                            }
                        } else if (paymentWidgetData != null && paymentWidgetData.mandatoryHit.contains(MANDATORY_HIT_INSTALLMENT_OPTIONS)) {
                            val selectedTenure = paymentWidgetData.installmentPaymentData.selectedTenure
                            val selectedTenor = checkoutItem.installmentData?.installmentOptions?.firstOrNull { it.installmentTerm == selectedTenure && it.isActive }
                            if (selectedTenor == null) {
                                hasInvalidPayment = true
                            }
                        }
                    }
                }
            }
            if (firstErrorIndex > -1 || isPrescriptionFrontEndValidationError) {
                pageState.value = CheckoutPageState.Normal
                listData.value = items
                if (firstErrorIndex > -1) {
                    pageState.value = CheckoutPageState.ScrollTo(firstErrorIndex)
                }
                if (hasUnselectedCourier) {
                    commonToaster.emit(
                        CheckoutPageToaster(
                            Toaster.TYPE_NORMAL,
                            SHIPMENT_NOT_COMPLETE_ERROR_MESSAGE,
                            source = SOURCE_LOCAL
                        )
                    )
                    mTrackerShipment.eventClickBuyCourierSelectionClickPilihMetodePembayaranCourierNotComplete()
                }
                if (isPrescriptionFrontEndValidationError && checkoutEpharmacy?.epharmacy?.consultationFlow == true) {
                    triggerEpharmacyTracker.invoke(true)
                }
                return@launch
            }
            if (!hasValidOrder) {
                commonToaster.emit(
                    CheckoutPageToaster(
                        Toaster.TYPE_ERROR,
                        NO_VALID_ORDER_ERROR_MESSAGE,
                        source = SOURCE_LOCAL
                    )
                )
                pageState.value = CheckoutPageState.Normal
                return@launch
            }
            if (hasNoPayment || hasInvalidPayment) {
                pageState.value = CheckoutPageState.Normal
                commonToaster.emit(
                    CheckoutPageToaster(
                        if (hasNoPayment) Toaster.TYPE_NORMAL else Toaster.TYPE_ERROR,
                        if (hasNoPayment) NO_PAYMENT_ERROR_MESSAGE else INVALID_PAYMENT_ERROR_MESSAGE,
                        source = SOURCE_LOCAL,
                        showCta = hasInvalidPayment
                    )
                )
                pageState.value = CheckoutPageState.ScrollTo(listData.value.size - PAYMENT_INDEX_FROM_BOTTOM)
                return@launch
            }
            val errorToaster =
                addOnProcessor.saveAddOnsProductBeforeCheckout(listData.value, isOneClickShipment)
            if (errorToaster != null) {
                commonToaster.emit(errorToaster)
                pageState.value = CheckoutPageState.Normal
                return@launch
            }

            // validate dropship
            var checkoutWithDropship = false
            val itemList = listData.value.toMutableList()
            for ((index, checkoutOrderModel) in itemList.withIndex()) {
                if (checkoutOrderModel is CheckoutOrderModel) {
                    if (checkoutOrderModel.isEnableDropship &&
                        checkoutOrderModel.useDropship && (
                            checkoutOrderModel.dropshipName.isEmpty() ||
                                checkoutOrderModel.dropshipPhone.isEmpty() || !checkoutOrderModel.isDropshipNameValid ||
                                !checkoutOrderModel.isDropshipPhoneValid
                            )
                    ) {
                        itemList[index] = checkoutOrderModel.copy(
                            stateDropship = CheckoutDropshipWidget.State.ERROR
                        )
                        listData.value = itemList
                        commonToaster.emit(
                            CheckoutPageToaster(
                                Toaster.TYPE_NORMAL,
                                INVALID_DROPSHIP_ERROR_MESSAGE,
                                source = SOURCE_LOCAL
                            )
                        )
                        pageState.value = CheckoutPageState.Normal
                        pageState.value = CheckoutPageState.ScrollTo(index)
                        return@launch
                    } else if (checkoutOrderModel.isEnableDropship && checkoutOrderModel.useDropship &&
                        checkoutOrderModel.dropshipName.isNotEmpty() && checkoutOrderModel.dropshipPhone.isNotEmpty() &&
                        checkoutOrderModel.isDropshipNameValid && checkoutOrderModel.isDropshipPhoneValid
                    ) {
                        checkoutWithDropship = true
                    }
                }
            }

            if (checkoutWithDropship) mTrackerShipment.eventClickPilihPembayaranWithDropshipEnabled()

            val (validateUsePromoRevampUiModel, isForceHit) = promoProcessor.finalValidateUse(
                promoProcessor.generateValidateUsePromoRequest(
                    listData.value,
                    isTradeIn,
                    isTradeInByDropOff,
                    isOneClickShipment,
                    cartType
                )
            )
            if (validateUsePromoRevampUiModel != null) {
                if (isForceHit) {
                    val itemListNewPromo = listData.value.toMutableList()
                    itemListNewPromo[itemListNewPromo.size - PROMO_INDEX_FROM_BOTTOM] = itemListNewPromo.promo()!!.copy(
                        promo = LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(
                            validateUsePromoRevampUiModel.promoUiModel
                        )
                    )
                    listData.value = itemListNewPromo
                }
                val notEligiblePromoHolderdataList = arrayListOf<NotEligiblePromoHolderdata>()
                if (validateUsePromoRevampUiModel.promoUiModel.messageUiModel.state == "red") {
                    val notEligiblePromoHolderdata = NotEligiblePromoHolderdata()
                    if (validateUsePromoRevampUiModel.promoUiModel.codes.isNotEmpty()) {
                        notEligiblePromoHolderdata.promoCode =
                            validateUsePromoRevampUiModel.promoUiModel.codes[0]
                    }
                    notEligiblePromoHolderdata.iconType =
                        NotEligiblePromoHolderdata.TYPE_ICON_GLOBAL
                    notEligiblePromoHolderdataList.add(notEligiblePromoHolderdata)
                }
                val voucherOrdersItemUiModels =
                    validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels
                for (i in voucherOrdersItemUiModels.indices) {
                    val voucherOrdersItemUiModel = voucherOrdersItemUiModels[i]
                    if (voucherOrdersItemUiModel.messageUiModel.state == "red") {
                        val notEligiblePromoHolderdata = NotEligiblePromoHolderdata()
                        notEligiblePromoHolderdata.promoCode = voucherOrdersItemUiModel.code
                        notEligiblePromoHolderdata.uniqueId = voucherOrdersItemUiModel.uniqueId
                        notEligiblePromoHolderdata.cartStringGroup =
                            voucherOrdersItemUiModel.cartStringGroup
                        notEligiblePromoHolderdataList.add(notEligiblePromoHolderdata)
                    }
                }
                triggerEpharmacyTracker.invoke(false)
                if (notEligiblePromoHolderdataList.size > 0) {
                    if (promoProcessor.cancelNotEligiblePromo(
                            notEligiblePromoHolderdataList,
                            listData.value,
                            cartType
                        )
                    ) {
                        if (validateUsePromoRevampUiModel.promoUiModel.messageUiModel.state == "red") {
                            validateUsePromoRevampUiModel.promoUiModel.codes = listOf()
                        }
                        val deletedVoucherOrder = ArrayList<PromoCheckoutVoucherOrdersItemUiModel>()
                        val voucherOrderUiModels =
                            validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels.toMutableList()
                        for (voucherOrdersItemUiModel in voucherOrderUiModels) {
                            if (voucherOrdersItemUiModel.messageUiModel.state == "red") {
                                deletedVoucherOrder.add(voucherOrdersItemUiModel)
                            }
                        }
                        for (voucherOrdersItemUiModel in deletedVoucherOrder) {
                            voucherOrderUiModels.remove(
                                voucherOrdersItemUiModel
                            )
                        }
                        validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels =
                            voucherOrderUiModels
                        doCheckout(
                            validateUsePromoRevampUiModel,
                            fingerprintPublicKey,
                            onSuccessCheckout,
                            true,
                            isPopupConfirmed
                        )
                    } else {
                        commonToaster.emit(
                            CheckoutPageToaster(
                                Toaster.TYPE_ERROR,
                                CheckoutConstant.DEFAULT_ERROR_MESSAGE_VALIDATE_PROMO,
                                source = "cancel-promo"
                            )
                        )
                        pageState.value = CheckoutPageState.Normal
                    }
                } else {
                    doCheckout(
                        validateUsePromoRevampUiModel,
                        fingerprintPublicKey,
                        onSuccessCheckout,
                        false,
                        isPopupConfirmed
                    )
                }
            } else {
                commonToaster.emit(
                    CheckoutPageToaster(
                        Toaster.TYPE_ERROR,
                        CheckoutConstant.DEFAULT_ERROR_MESSAGE_VALIDATE_PROMO,
                        source = SOURCE_LOCAL
                    )
                )
                pageState.value = CheckoutPageState.Normal
            }
        }
    }

    private suspend fun doCheckout(
        validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel,
        fingerprintPublicKey: String?,
        onSuccessCheckout: (CheckoutResult) -> Unit,
        hasClearPromoBeforeCheckout: Boolean,
        isPopupConfirmed: Boolean? = false
    ) {
        cartProcessor.processSaveShipmentState(listData.value, recipientAddressModel)
        if (isPaymentEnable()) {
            val updateCartResult = cartProcessor.updateCart(cartProcessor.generateUpdateCartRequest(listData.value), UPDATE_CART_SOURCE_CHECKOUT, cartProcessor.generateUpdateCartPaymentRequest(listData.value.payment()))
            if (!updateCartResult.isSuccess) {
                toasterProcessor.commonToaster.emit(
                    CheckoutPageToaster(
                        Toaster.TYPE_ERROR,
                        updateCartResult.toasterMessage,
                        updateCartResult.throwable,
                        source = "update-cart"
                    )
                )
                loadSAF(
                    isReloadData = true,
                    skipUpdateOnboardingState = true,
                    isReloadAfterPriceChangeHigher = false
                )
                return
            }
        }
        val checkoutResult = checkoutProcessor.doCheckout(
            listData.value,
            recipientAddressModel,
            validateUsePromoRevampUiModel,
            isOneClickShipment,
            isTradeIn,
            isTradeInByDropOff,
            deviceId,
            checkoutLeasingId,
            fingerprintPublicKey,
            hasClearPromoBeforeCheckout,
            cartType,
            isPopupConfirmed
        )
        if (checkoutResult.success) {
            sendEEStep4(
                checkoutResult.transactionId,
                checkoutResult.deviceModel,
                checkoutResult.devicePrice,
                checkoutResult.diagnosticId
            )
            onSuccessCheckout(checkoutResult)
            pageState.value = CheckoutPageState.Normal
        } else if (checkoutResult.throwable != null) {
            if (isTradeIn) {
                mTrackerTradeIn.eventClickBayarTradeInFailed()
            }
            mTrackerShipment.eventClickAtcCourierSelectionClickPilihMetodePembayaranNotSuccess(
                checkoutResult.throwable.message
            )
            commonToaster.emit(
                CheckoutPageToaster(
                    Toaster.TYPE_ERROR,
                    throwable = checkoutResult.throwable,
                    source = "checkout"
                )
            )
            loadSAF(
                isReloadData = true,
                skipUpdateOnboardingState = true,
                isReloadAfterPriceChangeHigher = false
            )
        } else if (checkoutResult.checkoutData == null) {
            commonToaster.emit(
                CheckoutPageToaster(
                    Toaster.TYPE_ERROR,
                    NO_VALID_ORDER_ERROR_MESSAGE,
                    source = "checkout"
                )
            )
            pageState.value = CheckoutPageState.Normal
        } else if (checkoutResult.checkoutData.priceValidationData.isUpdated) {
            pageState.value =
                CheckoutPageState.PriceValidation(checkoutResult.checkoutData.priceValidationData)
        } else if (checkoutResult.checkoutData.prompt.eligible) {
            pageState.value = CheckoutPageState.Prompt(checkoutResult.checkoutData.prompt)
        } else {
            val toasterMessage =
                checkoutResult.checkoutData.errorMessage.ifEmpty { "Terjadi kesalahan. Ulangi beberapa saat lagi" }
            commonToaster.emit(
                CheckoutPageToaster(
                    Toaster.TYPE_ERROR,
                    toasterMessage,
                    source = "checkout"
                )
            )
            if (isTradeIn) {
                mTrackerTradeIn.eventClickBayarTradeInFailed()
            }
            mTrackerShipment.eventClickAtcCourierSelectionClickPilihMetodePembayaranNotSuccess(
                checkoutResult.checkoutData.errorMessage
            )
            CheckoutLogger.logOnErrorCheckout(
                MessageErrorException(toasterMessage),
                checkoutResult.checkoutRequest.toString(),
                isOneClickShipment,
                isTradeIn,
                isTradeInByDropOff
            )
            loadSAF(
                isReloadData = true,
                skipUpdateOnboardingState = true,
                isReloadAfterPriceChangeHigher = false
            )
        }
    }

    private fun checkCrossSellImpressionState(newItems: List<CheckoutItem>): List<CheckoutItem> {
        // workaround racing condition between set impression state & get promo entry point
        val checkoutItems = newItems.toMutableList()
        val crossSellGroup = checkoutItems.crossSellGroup() ?: return newItems
        val originalCrossSellGroup = listData.value.crossSellGroup() ?: return newItems
        if (crossSellGroup.shouldTriggerScrollInteraction != originalCrossSellGroup.shouldTriggerScrollInteraction) {
            checkoutItems[checkoutItems.size - CROSS_SELL_INDEX_FROM_BOTTOM] = crossSellGroup.copy(shouldTriggerScrollInteraction = originalCrossSellGroup.shouldTriggerScrollInteraction)
        }
        return checkoutItems
    }

    fun updateCrossSell(checked: Boolean) {
        val checkoutItems = listData.value.toMutableList()
        val crossSellGroup = checkoutItems.crossSellGroup()!!
        val newList: MutableList<CheckoutCrossSellItem> = arrayListOf()
        for (checkoutCrossSellItem in crossSellGroup.crossSellList) {
            if (checkoutCrossSellItem is CheckoutCrossSellModel) {
                newList.add(
                    checkoutCrossSellItem.copy(
                        crossSellModel = checkoutCrossSellItem.crossSellModel.copy(
                            isChecked = checked
                        ),
                        isChecked = checked
                    )
                )
            } else {
                newList.add(checkoutCrossSellItem)
            }
        }
        checkoutItems[checkoutItems.size - CROSS_SELL_INDEX_FROM_BOTTOM] = crossSellGroup.copy(crossSellList = newList)
        listData.value = checkoutItems
        calculateTotal()
    }

    fun updateEgold(checked: Boolean) {
        val checkoutItems = listData.value.toMutableList()
        val crossSellGroup = checkoutItems.crossSellGroup()!!
        val newList: MutableList<CheckoutCrossSellItem> = arrayListOf()
        for (checkoutCrossSellItem in crossSellGroup.crossSellList) {
            if (checkoutCrossSellItem is CheckoutEgoldModel) {
                newList.add(
                    checkoutCrossSellItem.copy(
                        egoldAttributeModel = checkoutCrossSellItem.egoldAttributeModel.copy(
                            isChecked = checked
                        ),
                        isChecked = checked
                    )
                )
            } else {
                newList.add(checkoutCrossSellItem)
            }
        }
        checkoutItems[checkoutItems.size - CROSS_SELL_INDEX_FROM_BOTTOM] = crossSellGroup.copy(crossSellList = newList)
        listData.value = checkoutItems
        calculateTotal()
    }

    fun updateDonation(checked: Boolean) {
        val checkoutItems = listData.value.toMutableList()
        val crossSellGroup = checkoutItems.crossSellGroup()!!
        val newList: MutableList<CheckoutCrossSellItem> = arrayListOf()
        for (checkoutCrossSellItem in crossSellGroup.crossSellList) {
            if (checkoutCrossSellItem is CheckoutDonationModel) {
                newList.add(
                    checkoutCrossSellItem.copy(
                        donation = checkoutCrossSellItem.donation.copy(
                            isChecked = checked
                        ),
                        isChecked = checked
                    )
                )
            } else {
                newList.add(checkoutCrossSellItem)
            }
        }
        checkoutItems[checkoutItems.size - CROSS_SELL_INDEX_FROM_BOTTOM] = crossSellGroup.copy(crossSellList = newList)
        listData.value = checkoutItems
        calculateTotal()
    }

    fun validatePrescriptionOnBackPressed(): CheckoutEpharmacyModel? {
        return addOnProcessor.validatePrescriptionOnBackPressed(listData.value.epharmacy())
    }

    fun validateClearAllBoPromo(
        lastValidateUsePromoRequest: ValidateUsePromoRequest,
        promoUiModel: PromoUiModel
    ) {
        viewModelScope.launch(dispatchers.immediate) {
            pageState.value = CheckoutPageState.Loading
            val checkoutItems = listData.value.toMutableList()
            for ((index, checkoutItem) in checkoutItems.withIndex()) {
                if (checkoutItem is CheckoutOrderModel) {
                    val logPromoCode =
                        checkoutItem.shipment.courierItemData?.selectedShipper?.logPromoCode
                    if (!logPromoCode.isNullOrEmpty()) {
                        for (order in lastValidateUsePromoRequest.orders) {
                            if (order.cartStringGroup == checkoutItem.cartStringGroup && !order.codes.contains(
                                    logPromoCode
                                )
                            ) {
                                promoProcessor.clearPromo(
                                    ClearPromoOrder(
                                        checkoutItem.boUniqueId,
                                        checkoutItem.boMetadata.boType,
                                        arrayListOf(logPromoCode),
                                        checkoutItem.shopId,
                                        checkoutItem.isProductIsPreorder,
                                        checkoutItem.preOrderDurationDay.toString(),
                                        checkoutItem.fulfillmentId,
                                        checkoutItem.cartStringGroup
                                    ),
                                    cartType
                                )
                                // reset shipment
                                checkoutItems[index] =
                                    checkoutItem.copy(shipment = CheckoutOrderShipment())
                            }
                        }
                    }
                }
                if (checkoutItem is CheckoutPromoModel) {
                    checkoutItems[index] = checkoutItem.copy(
                        promo = LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(
                            promoUiModel
                        ),
                        isLoading = isPromoRevamp ?: false
                    )
                }
            }
            listData.value = checkoutItems
            pageState.value = CheckoutPageState.Normal
            validatePromo()
        }
    }

    fun validateBoPromo(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel) {
        viewModelScope.launch(dispatchers.immediate) {
            pageState.value = CheckoutPageState.Loading
            val unappliedBoPromoUniqueIds = ArrayList<String>()
            val reloadedUniqueIds = ArrayList<String>()
            val unprocessedUniqueIds = ArrayList<String>()
            var checkoutItems = listData.value.toMutableList()
            for (shipmentCartItemModel in checkoutItems) {
                if (shipmentCartItemModel is CheckoutOrderModel) {
                    unprocessedUniqueIds.add(shipmentCartItemModel.cartStringGroup)
                }
            }
            // loop to list voucher orders to be applied this will be used later
            val toBeAppliedVoucherOrders: MutableList<PromoCheckoutVoucherOrdersItemUiModel> =
                ArrayList()
            for (voucherOrdersItemUiModel in validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels) {
                // voucher with shippingId not zero, spId not zero, and voucher type logistic as promo for BO
                if (voucherOrdersItemUiModel.shippingId > 0 && voucherOrdersItemUiModel.spId > 0 && voucherOrdersItemUiModel.type == "logistic") {
                    if (voucherOrdersItemUiModel.messageUiModel.state == "green" && unprocessedUniqueIds.contains(
                            voucherOrdersItemUiModel.cartStringGroup
                        )
                    ) {
                        toBeAppliedVoucherOrders.add(voucherOrdersItemUiModel)
                        unprocessedUniqueIds.remove(voucherOrdersItemUiModel.cartStringGroup)
                    }
                }
            }

            val list = listData.value.toMutableList()
            val promoUiModel =
                validateUsePromoRevampUiModel.promoUiModel
                    .copy(voucherOrderUiModels = validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels.filter { !it.isTypeLogistic() })
            val oldLastApply = list.promo()!!.promo
            val newLastApply = LastApplyUiMapper
                .mapValidateUsePromoUiModelToLastApplyUiModel(promoUiModel)
            val newPromo = list.promo()!!.copy(
                promo = newLastApply,
                isAnimateWording = shouldAnimateEntryPointWording(newLastApply, oldLastApply)
            )
            list[list.size - PROMO_INDEX_FROM_BOTTOM] = newPromo
            listData.value = list
            checkoutItems = list

            val resultClear =
                doValidateBoClearUnapplied(
                    checkoutItems,
                    unprocessedUniqueIds,
                    unappliedBoPromoUniqueIds,
                    reloadedUniqueIds
                )
            val firstScrollIndex = resultClear.first
            checkoutItems = resultClear.second

            doValidateBoToaster(validateUsePromoRevampUiModel, unappliedBoPromoUniqueIds)

            checkoutItems = doValidateBoApply(toBeAppliedVoucherOrders, checkoutItems)
            listData.value = checkoutItems

            if (firstScrollIndex != -1) {
                pageState.value = CheckoutPageState.ScrollTo(firstScrollIndex)
            }
            validatePromo()
            pageState.value = CheckoutPageState.Normal
            launch(dispatchers.io) {
                cartProcessor.processSaveShipmentState(
                    listData.value,
                    listData.value.address()!!.recipientAddressModel
                )
            }
        }
    }

    private suspend fun doValidateBoApply(
        toBeAppliedVoucherOrders: MutableList<PromoCheckoutVoucherOrdersItemUiModel>,
        listData: MutableList<CheckoutItem>
    ): MutableList<CheckoutItem> {
        var checkoutItems = listData
        for (voucher in toBeAppliedVoucherOrders) {
            val cartPosition =
                checkoutItems.indexOfFirst { it is CheckoutOrderModel && it.cartStringGroup == voucher.cartStringGroup }
            val order = checkoutItems[cartPosition] as CheckoutOrderModel
            if (voucher.code == order.shipment.courierItemData?.selectedShipper?.logPromoCode) {
                continue
            }
            val ratesParam = logisticProcessor.getRatesParam(
                order,
                helper.getOrderProducts(checkoutItems, order.cartStringGroup),
                checkoutItems.address()!!.recipientAddressModel,
                isTradeIn,
                isTradeInByDropOff,
                codData,
                cartDataForRates,
                "",
                false,
                checkoutItems.promo()!!
            )
            val result = logisticCartProcessor.getRatesWithBoCode(
                LogisticProcessorGetRatesParam(
                    ratesParam = ratesParam,
                    selectedServiceId = voucher.shippingId,
                    selectedSpId = voucher.spId,
                    isTradeInDropOff = isTradeInByDropOff,
                    boPromoCode = voucher.code,
                    currentServiceId = null,
                    isAutoCourierSelection = order.isAutoCourierSelection,
                    isDisableChangeCourier = order.isDisableChangeCourier,
                    shouldResetCourier = order.shouldResetCourier,
                    validationMetadata = order.validationMetadata
                )
            )
            val courierItemData = result?.courier
            if (courierItemData != null) {
                val shouldValidatePromo =
                    !courierItemData.selectedShipper.logPromoCode.isNullOrEmpty()
                if (shouldValidatePromo) {
                    val validateUsePromoRequest = generateValidateUsePromoRequest(checkoutItems)
                    for (ordersItem in validateUsePromoRequest.orders) {
                        if (ordersItem.cartStringGroup == order.cartStringGroup) {
                            if (!ordersItem.codes.contains(
                                    courierItemData.selectedShipper.logPromoCode
                                )
                            ) {
                                ordersItem.codes.add(
                                    courierItemData.selectedShipper.logPromoCode!!
                                )
                                ordersItem.boCode =
                                    courierItemData.selectedShipper.logPromoCode!!
                            }
                            ordersItem.shippingId =
                                courierItemData.selectedShipper.shipperId
                            ordersItem.spId =
                                courierItemData.selectedShipper.shipperProductId
                            ordersItem.freeShippingMetadata =
                                courierItemData.selectedShipper.freeShippingMetadata
                            ordersItem.boCampaignId =
                                courierItemData.selectedShipper.boCampaignId
                            ordersItem.shippingSubsidy =
                                courierItemData.selectedShipper.shippingSubsidy
                            ordersItem.benefitClass =
                                courierItemData.selectedShipper.benefitClass
                            ordersItem.shippingPrice =
                                courierItemData.selectedShipper.shippingRate.toDouble()
                            ordersItem.etaText =
                                courierItemData.selectedShipper.etaText!!
                            ordersItem.validationMetadata =
                                order.validationMetadata
                        }
                    }
                    checkoutItems = promoProcessor.validateUseLogisticPromo(
                        validateUsePromoRequest,
                        order.cartStringGroup,
                        courierItemData.selectedShipper.logPromoCode!!,
                        checkoutItems,
                        courierItemData,
                        isOneClickShipment,
                        isTradeIn,
                        isTradeInByDropOff,
                        cartType
                    ).toMutableList()
                }
            } else {
                // handle error
                val ratesError = result?.ratesError
                val boPromoCode = getBoPromoCode(order)
                if (ratesError != null) {
                    CheckoutLogger.logOnErrorLoadCourierNew(
                        ratesError,
                        order,
                        isOneClickShipment,
                        isTradeIn,
                        isTradeInByDropOff,
                        boPromoCode
                    )
                    if (ratesError is AkamaiErrorException) {
                        ratesError.message?.let {
                            pageState.value = CheckoutPageState.AkamaiRatesError(it)
                        }
                    }
                }
                promoProcessor.clearPromo(
                    ClearPromoOrder(
                        voucher.uniqueId,
                        order.boMetadata.boType,
                        arrayListOf(voucher.code),
                        order.shopId,
                        order.isProductIsPreorder,
                        order.preOrderDurationDay.toString(),
                        order.fulfillmentId,
                        order.cartStringGroup
                    ),
                    cartType
                )
                val newOrderModel = order.copy(
                    shipment = order.shipment.copy(
                        isLoading = false,
                        courierItemData = null,
                        shippingCourierUiModels = emptyList(),
                        insurance = CheckoutOrderInsurance()
                    )
                )
                checkoutItems[cartPosition] = newOrderModel
            }
        }
        return checkoutItems
    }

    private suspend fun doValidateBoToaster(
        validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel,
        unappliedBoPromoUniqueIds: ArrayList<String>
    ) {
        if (validateUsePromoRevampUiModel.promoUiModel.additionalInfoUiModel.errorDetailUiModel.message.isNotEmpty()) {
            commonToaster.emit(
                CheckoutPageToaster(
                    Toaster.TYPE_NORMAL,
                    validateUsePromoRevampUiModel.promoUiModel.additionalInfoUiModel.errorDetailUiModel.message
                )
            )
        } else if (unappliedBoPromoUniqueIds.size > 0) {
            // when messageInfo is empty and has unapplied BO show hard coded toast
            commonToaster.emit(
                CheckoutPageToaster(
                    Toaster.TYPE_NORMAL,
                    "Pengiriman disesuaikan karena ada perubahan di promo yang kamu pilih."
                )
            )
        }
    }

    private suspend fun doValidateBoClearUnapplied(
        checkoutItems: MutableList<CheckoutItem>,
        unprocessedUniqueIds: ArrayList<String>,
        unappliedBoPromoUniqueIds: ArrayList<String>,
        reloadedUniqueIds: ArrayList<String>
    ): Pair<Int, MutableList<CheckoutItem>> {
        var firstScrollIndex = -1
        for ((index, shipmentCartItemModel) in checkoutItems.withIndex()) {
            if (shipmentCartItemModel is CheckoutOrderModel) {
                val logPromoCode =
                    shipmentCartItemModel.shipment.courierItemData?.selectedShipper?.logPromoCode
                if (!logPromoCode.isNullOrEmpty() &&
                    unprocessedUniqueIds.contains(shipmentCartItemModel.cartStringGroup)
                ) {
                    promoProcessor.clearPromo(
                        ClearPromoOrder(
                            shipmentCartItemModel.boUniqueId,
                            shipmentCartItemModel.boMetadata.boType,
                            arrayListOf(logPromoCode),
                            shipmentCartItemModel.shopId,
                            shipmentCartItemModel.isProductIsPreorder,
                            shipmentCartItemModel.preOrderDurationDay.toString(),
                            shipmentCartItemModel.fulfillmentId,
                            shipmentCartItemModel.cartStringGroup
                        ),
                        cartType
                    )
                    // reset shipment
                    checkoutItems[index] =
                        shipmentCartItemModel.copy(
                            shipment = CheckoutOrderShipment(),
                            isTriggerShippingVibrationAnimation = true,
                            isStateAllItemViewExpanded = false,
                            isShippingBorderRed = true
                        )
                    unappliedBoPromoUniqueIds.add(shipmentCartItemModel.cartStringGroup)
                    reloadedUniqueIds.add(shipmentCartItemModel.cartStringGroup)
                    if (firstScrollIndex == -1) {
                        firstScrollIndex = index
                    }
                }
            }
        }
        return firstScrollIndex to checkoutItems
    }

    private suspend fun hitClearAllBo() {
        promoProcessor.clearAllBo(listData.value, cartType)
    }

    fun setAddon(
        checked: Boolean,
        addOnProductDataItemModel: AddOnProductDataItemModel,
        position: Int
    ) {
        val checkoutItems = listData.value.toMutableList()
        val checkoutProductModel = checkoutItems[position] as CheckoutProductModel

        val indexOrder = listData.value.indexOfFirst {
            it is CheckoutOrderModel &&
                it.cartStringGroup == checkoutProductModel.cartStringGroup
        }

        if (indexOrder > 0) {
            val order = checkoutItems[indexOrder] as CheckoutOrderModel
            val newStateDropship: CheckoutDropshipWidget.State
            if (checked && order.shipment.courierItemData?.isSelected == true && order.useDropship) {
                newStateDropship = CheckoutDropshipWidget.State.DISABLED
                viewModelScope.launch(dispatchers.immediate) {
                    commonToaster.emit(
                        CheckoutPageToaster(
                            Toaster.TYPE_NORMAL,
                            DISABLED_DROPSHIP_ERROR_MESSAGE
                        )
                    )
                }
                val newOrder = order.copy(
                    stateDropship = newStateDropship,
                    useDropship = false
                )
                checkoutItems[indexOrder] = newOrder
            } else {
                newStateDropship = CheckoutDropshipWidget.State.INIT
                val newOrder = order.copy(
                    stateDropship = newStateDropship
                )
                checkoutItems[indexOrder] = newOrder
            }
        }

        val oldList = checkoutProductModel.addOnProduct.listAddOnProductData
        val newProduct = checkoutProductModel.copy(
            addOnProduct = checkoutProductModel.addOnProduct.copy(
                listAddOnProductData = ArrayList(
                    oldList.map {
                        it.copy(
                            status = if (it.uniqueId == addOnProductDataItemModel.uniqueId) {
                                if (checked) {
                                    AddOnConstant.ADD_ON_PRODUCT_STATUS_CHECK
                                } else {
                                    AddOnConstant.ADD_ON_PRODUCT_STATUS_UNCHECK
                                }
                            } else {
                                it.status
                            }
                        )
                    }
                )
            )
        )
        checkoutItems[position] = newProduct
        viewModelScope.launch(dispatchers.io) {
            addOnProcessor.saveAddonsProduct(newProduct, isOneClickShipment)
        }
        listData.value = checkoutItems
        calculateTotal()
    }

    fun setAddonResult(cartIdAddOn: Long, addOnProductDataResult: AddOnPageResult) {
        val checkoutItems = listData.value.toMutableList()
        val itemIndex =
            checkoutItems.indexOfFirst { it is CheckoutProductModel && it.cartId == cartIdAddOn }
        if (itemIndex > 0) {
            val product = checkoutItems[itemIndex] as CheckoutProductModel
            val oldList = product.addOnProduct.listAddOnProductData
            val newProduct = product.copy(
                addOnProduct = product.addOnProduct.copy(
                    listAddOnProductData = ArrayList(
                        oldList.map {
                            for (addOnUiModel in addOnProductDataResult.aggregatedData.selectedAddons) {
                                if (it.type == addOnUiModel.addOnType) {
                                    return@map it.copy(
                                        id = addOnUiModel.id.toLongOrZero(),
                                        uniqueId = addOnUiModel.uniqueId,
                                        price = addOnUiModel.price.toDouble(),
                                        infoLink = addOnUiModel.eduLink,
                                        name = addOnUiModel.name,
                                        type = addOnUiModel.addOnType,
                                        status = addOnUiModel.getSaveAddonSelectedStatus().value
                                    )
                                }
                            }
                            return@map it
                        }
                    )
                )
            )
            checkoutItems[itemIndex] = newProduct
            viewModelScope.launch(dispatchers.io) {
                addOnProcessor.saveAddonsProduct(newProduct, isOneClickShipment)
            }
            listData.value = checkoutItems
            calculateTotal()
        }
    }

    fun setSelectedCourierInsurance(checked: Boolean, order: CheckoutOrderModel, position: Int) {
        if (order.shipment.insurance.isCheckInsurance == checked) return
        val checkoutItems = listData.value.toMutableList()
        val checkoutOrderModel = checkoutItems[position] as CheckoutOrderModel
        val newOrder = checkoutOrderModel.copy(
            shipment = checkoutOrderModel.shipment.copy(
                insurance = checkoutOrderModel.shipment.insurance.copy(isCheckInsurance = checked)
            )
        )
        checkoutItems[position] = newOrder
        listData.value = checkoutItems
        calculateTotal()
    }

    fun setDropshipSwitch(isChecked: Boolean, position: Int) {
        val checkoutItems = listData.value.toMutableList()
        val checkoutOrderModel = checkoutItems[position] as CheckoutOrderModel
        if (isChecked && checkoutProcessor.checkProtectionAddOnOptIn(
                getOrderProducts(
                        checkoutOrderModel.cartStringGroup
                    )
            )
        ) {
            viewModelScope.launch(dispatchers.immediate) {
                commonToaster.emit(
                    CheckoutPageToaster(
                        Toaster.TYPE_NORMAL,
                        DISABLED_DROPSHIP_ERROR_MESSAGE
                    )
                )
            }
            val newOrder =
                checkoutOrderModel.copy(stateDropship = CheckoutDropshipWidget.State.DISABLED)
            checkoutItems[position] = newOrder
            listData.value = checkoutItems
        } else {
            if (!isChecked) {
                val newOrder = checkoutOrderModel.copy(
                    stateDropship = CheckoutDropshipWidget.State.INIT,
                    useDropship = false
                )
                checkoutItems[position] = newOrder
                listData.value = checkoutItems
            } else {
                val newOrder = checkoutOrderModel.copy(useDropship = isChecked)
                checkoutItems[position] = newOrder
                listData.value = checkoutItems
            }
        }
    }

    fun setValidationDropshipName(name: String, isValid: Boolean, position: Int) {
        val checkoutItems = listData.value.toMutableList()
        val checkoutOrderModel = checkoutItems[position] as CheckoutOrderModel
        checkoutOrderModel.isDropshipNameValid = isValid
        checkoutOrderModel.dropshipName = name
    }

    fun setValidationDropshipPhone(phone: String, isValid: Boolean, position: Int) {
        val checkoutItems = listData.value.toMutableList()
        val checkoutOrderModel = checkoutItems[position] as CheckoutOrderModel
        checkoutOrderModel.isDropshipPhoneValid = isValid
        checkoutOrderModel.dropshipPhone = phone
    }

    fun getOrderProducts(cartStringGroup: String): List<CheckoutItem> {
        return helper.getOrderProducts(listData.value, cartStringGroup)
    }

    fun getProductCatIds(): List<Long> {
        return helper.getAllProductCategoryIds(listData.value)
    }

    fun cancelUpsell(
        isReloadData: Boolean,
        skipUpdateOnboardingState: Boolean,
        isReloadAfterPriceChangeHigher: Boolean
    ) {
        viewModelScope.launch(dispatchers.immediate) {
            pageState.value = CheckoutPageState.Loading
            hitClearAllBo()
            loadSAF(
                isReloadData = isReloadData,
                skipUpdateOnboardingState = skipUpdateOnboardingState,
                isReloadAfterPriceChangeHigher = isReloadAfterPriceChangeHigher
            )
        }
    }

    fun clearAllBoOnTemporaryUpsell() {
        val upsell = listData.value.upsell()
        if (upsell != null && upsell.upsell.isShow && upsell.upsell.isSelected) {
            GlobalScope.launch(dispatchers.io) {
                hitClearAllBo()
            }
        }
    }

    fun cancelAutoApplyPromoStackLogistic(
        position: Int,
        promoCode: String,
        order: CheckoutOrderModel
    ) {
        viewModelScope.launch(dispatchers.main) {
            val checkoutItems = listData.value.toMutableList()
            checkoutItems[position] =
                order.copy(shipment = order.shipment.copy(courierItemData = null))
            listData.value = checkoutItems
            promoProcessor.clearPromo(
                ClearPromoOrder(
                    order.boUniqueId,
                    order.boMetadata.boType,
                    arrayListOf(promoCode),
                    order.shopId,
                    order.isProductIsPreorder,
                    order.preOrderDurationDay.toString(),
                    order.fulfillmentId,
                    order.cartStringGroup
                ),
                cartType
            )
            validatePromo()
        }
    }

    fun useNewPromoPage(): Boolean {
        return isPromoRevamp == true
    }

    fun generatePaymentLevelAddOnsAnalyticData(): List<Pair<String, String>> {
        val result = mutableListOf<Pair<String, String>>()
        val crossSellGroup = listData.value.crossSellGroup()
        val eGoldAttribute =
            crossSellGroup?.crossSellList?.firstOrNullInstanceOf(CheckoutEgoldModel::class.java)
        if (eGoldAttribute != null && eGoldAttribute.egoldAttributeModel.isEligible && eGoldAttribute.egoldAttributeModel.isChecked) {
            result.add(
                Pair(
                    eGoldAttribute.getCategoryName(),
                    eGoldAttribute.getCrossSellProductId()
                )
            )
        }
        val listShipmentCrossSellModel =
            crossSellGroup?.crossSellList?.filterIsInstance(CheckoutCrossSellModel::class.java)
                ?: emptyList()
        if (listShipmentCrossSellModel.isNotEmpty()) {
            for (crossSellModel in listShipmentCrossSellModel) {
                if (crossSellModel.isChecked) {
                    result.add(
                        Pair(
                            crossSellModel.getCategoryName(),
                            crossSellModel.getCrossSellProductId()
                        )
                    )
                }
            }
        }
        val donationModel =
            crossSellGroup?.crossSellList?.firstOrNullInstanceOf(CheckoutDonationModel::class.java)
        if (donationModel != null && donationModel.donation.isChecked) {
            result.add(Pair(donationModel.getCategoryName(), donationModel.getCrossSellProductId()))
        }
        return result
    }

    fun isAnyProtectionAddonOptIn(cartStringGroup: String): Boolean {
        return checkoutProcessor.checkProtectionAddOnOptIn(getOrderProducts(cartStringGroup))
    }

    fun setProductNote(
        note: String,
        position: Int
    ) {
        viewModelScope.launch(dispatchers.immediate) {
            pageState.value = CheckoutPageState.Loading

            val updateCartResult = cartProcessor.updateCart(
                cartProcessor.generateUpdateCartRequest(listData.value),
                UPDATE_CART_SOURCE_NOTES,
                cartProcessor.generateUpdateCartPaymentRequest(listData.value.payment())
            )

            val checkoutItems = listData.value.toMutableList()
            val product = checkoutItems[position] as CheckoutProductModel
            if (!updateCartResult.isSuccess) {
                pageState.value = CheckoutPageState.Normal
                toasterProcessor.commonToaster.emit(
                    CheckoutPageToaster(
                        Toaster.TYPE_ERROR,
                        updateCartResult.toasterMessage,
                        updateCartResult.throwable,
                        source = "update_notes"
                    )
                )
                return@launch
            } else {
                pageState.value = CheckoutPageState.Normal
                var showLottie = true
                if (note.isBlank()) showLottie = false
                checkoutItems[position] = product.copy(noteToSeller = note, shouldShowLottieNotes = showLottie)
                listData.value = checkoutItems
            }
        }
    }

    fun hideNoteLottie(position: Int) {
        val checkoutItems = listData.value.toMutableList()
        val product = checkoutItems[position] as CheckoutProductModel
        checkoutItems[position] = product.copy(shouldShowLottieNotes = false)
        listData.value = checkoutItems
    }

    private fun shouldGetPayment(items: List<CheckoutItem>): Boolean {
        var hasValidOrder = false
        var isPaymentWidgetEnable = false
        for (checkoutItem in items) {
            if (checkoutItem is CheckoutOrderModel) {
                if (!checkoutItem.isError && checkoutItem.shipment.courierItemData == null) {
                    return false
                } else if (!checkoutItem.isError) {
                    hasValidOrder = true
                }
            }
            if (checkoutItem is CheckoutPaymentModel) {
                isPaymentWidgetEnable = checkoutItem.enable
                break
            }
        }
        return hasValidOrder && isPaymentWidgetEnable
    }

    private suspend fun getCheckoutPaymentData(skipPaymentMandatoryHit: Boolean = false) {
        val checkoutItems = listData.value.toMutableList()
        var payment = checkoutItems.payment() ?: return

        // show loading
        payment = payment.copy(widget = payment.widget.copy(state = CheckoutPaymentWidgetState.Loading))
        checkoutItems[checkoutItems.size - PAYMENT_INDEX_FROM_BOTTOM] = payment
        listData.value = checkoutItems

        var cost = listData.value.cost()!!
        var paymentRequest = paymentProcessor.generatePaymentRequest(checkoutItems, payment)
        var shouldRevalidatePromo = false

        if (payment.data == null || payment.data?.paymentWidgetData.isNullOrEmpty()) {
            // get payment widget if not yet
            val chosenPayment = payment.originalData
            payment = paymentProcessor.getPaymentWidget(
                GetPaymentWidgetRequest(
                    source = GetPaymentWidgetRequest.SOURCE_OCC,
                    chosenPayment = GetPaymentWidgetChosenPaymentRequest(
                        gatewayCode = chosenPayment.gatewayCode,
                        metadata = chosenPayment.metadata,
                        tenureType = chosenPayment.tenureType,
                        optionId = chosenPayment.optionId
                    ),
                    cartMetadata = payment.metadata,
                    paymentRequest = Gson().toJson(paymentRequest)
                ),
                payment
            )

            if (payment.widget.state == CheckoutPaymentWidgetState.Error) {
                // show error
                cost = cost.copy(
                    dynamicPlatformFee = ShipmentPaymentFeeModel(isLoading = false),
                    usePaymentFees = true
                )
                updateTotalAndPayment(cost, payment)
                return
            }

            val paymentData = payment.data?.paymentWidgetData?.firstOrNull()
            if (paymentData?.mandatoryHit?.isNotEmpty() == true) {
                // validate promo after get tenor
                shouldRevalidatePromo = true
            } else {
                val updateCartRequest = cartProcessor.generateUpdateCartRequest(listData.value)
                val newPaymentRequest = cartProcessor.generateUpdateCartPaymentRequest(payment)
                val updateCartResult = cartProcessor.updateCart(updateCartRequest, UPDATE_CART_SOURCE_PAYMENT, newPaymentRequest)
                if (!updateCartResult.isSuccess) {
                    // show error
                    cost = cost.copy(
                        dynamicPlatformFee = ShipmentPaymentFeeModel(isLoading = false),
                        usePaymentFees = true
                    )
                    payment = payment.copy(widget = payment.widget.copy(state = CheckoutPaymentWidgetState.Error))
                    updateTotalAndPayment(cost, payment)
                    return
                }
                updateTotalAndPayment(cost, payment.copy(widget = payment.widget.copy(state = CheckoutPaymentWidgetState.Loading)), skipValidatePayment = true)
                // validate promo after get payment
                validatePromo(skipEE = true)
                return
            }
        } else {
            payment = payment.copy(widget = payment.widget.copy(state = CheckoutPaymentWidgetState.Normal))
        }

        // update payment request
        paymentRequest = paymentProcessor.generatePaymentRequest(checkoutItems, payment)

        // get platform fee
        val paymentFeeCheckoutRequest = PaymentFeeRequest(
            gatewayCode = payment.data?.paymentWidgetData?.firstOrNull()?.gatewayCode ?: "",
            profileCode = payment.data?.paymentWidgetData?.firstOrNull()?.profileCode ?: shipmentPlatformFeeData.profileCode,
            additionalData = shipmentPlatformFeeData.additionalData,
            transactionAmount = cost.totalPrice,
            paymentRequest = paymentRequest
        )
        cost = paymentProcessor.checkPlatformFeeOcc(
            cost,
            paymentFeeCheckoutRequest
        )
        if (cost.dynamicPaymentFees == null) {
            // show error
            payment = payment.copy(
                widget = payment.widget.copy(state = CheckoutPaymentWidgetState.Error)
            )
            cost = cost.copy(
                dynamicPlatformFee = ShipmentPaymentFeeModel(isLoading = false),
                usePaymentFees = true
            )
            updateTotalAndPayment(cost, payment)
            return
        }

        val paymentData = payment.data?.paymentWidgetData?.firstOrNull()
        if (!skipPaymentMandatoryHit && paymentData?.mandatoryHit?.contains(MANDATORY_HIT_CC_TENOR_LIST) == true) {
            payment = paymentProcessor.getTenorList(payment, paymentData, paymentRequest, listData.value, cost, shipmentPlatformFeeData)

            if (payment.tenorList == null) {
                toasterProcessor.commonToaster.emit(CheckoutPageToaster(Toaster.TYPE_ERROR, INSTALLMENT_ERROR_MESSAGE))
            }
        }

        if (!skipPaymentMandatoryHit && paymentData?.mandatoryHit?.contains(MANDATORY_HIT_INSTALLMENT_OPTIONS) == true) {
            payment = paymentProcessor.getInstallmentList(payment, paymentData, paymentRequest, listData.value, cost, shipmentPlatformFeeData)

            if (payment.installmentData == null) {
                toasterProcessor.commonToaster.emit(CheckoutPageToaster(Toaster.TYPE_ERROR, INSTALLMENT_ERROR_MESSAGE))
            }
        }

        if (shouldRevalidatePromo) {
            val updateCartRequest = cartProcessor.generateUpdateCartRequest(listData.value)
            val newPaymentRequest = cartProcessor.generateUpdateCartPaymentRequest(payment)
            val updateCartResult = cartProcessor.updateCart(updateCartRequest, UPDATE_CART_SOURCE_PAYMENT, newPaymentRequest)
            if (!updateCartResult.isSuccess) {
                // show error
                cost = cost.copy(
                    dynamicPlatformFee = ShipmentPaymentFeeModel(isLoading = false),
                    usePaymentFees = true
                )
                payment = payment.copy(widget = payment.widget.copy(state = CheckoutPaymentWidgetState.Error))
                updateTotalAndPayment(cost, payment)
                return
            }

            cost = cost.copy(
                usePaymentFees = true
            )
            if (payment.tenorList != null || payment.installmentData != null) {
                payment = payment.copy(widget = payment.widget.copy(state = CheckoutPaymentWidgetState.Loading))
                updateTotalAndPayment(cost, payment, skipValidatePayment = true)
                validatePromo(skipEE = true)
            } else {
                updateTotalAndPayment(cost, payment)
            }
        } else {
            cost = cost.copy(
                usePaymentFees = true
            )
            updateTotalAndPayment(cost, payment)
        }
        mTrackerShipment.sendViewPaymentMethodEvent(payment.getPaymentMethodName(), getCartTypeString())
    }

    private fun updateTotalAndPayment(cost: CheckoutCostModel, payment: CheckoutPaymentModel, skipValidatePayment: Boolean = false) {
        listData.value = calculator.calculateTotalWithPayment(
            listData.value,
            cost,
            payment,
            summariesAddOnUiModel
        )
        if (!skipValidatePayment) {
            listData.value = paymentProcessor.validatePayment(listData.value)
        }
        listData.value = calculator.updateButtonPaymentWithPaymentData(listData.value, isTradeInByDropOff)
    }

    fun generatePaymentRequest(payment: CheckoutPaymentModel): PaymentRequest {
        return paymentProcessor.generatePaymentRequest(listData.value, payment)
    }

    fun generateGoCicilInstallmentRequest(payment: CheckoutPaymentModel): GoCicilInstallmentRequest {
        return paymentProcessor.generateInstallmentRequest(payment, payment.data!!.paymentWidgetData.first(), generatePaymentRequest(payment), listData.value, listData.value.cost()!!, shipmentPlatformFeeData)
    }

    fun generateCreditCardTenorListRequest(payment: CheckoutPaymentModel): CreditCardTenorListRequest {
        return paymentProcessor.generateCreditCardTenorRequest(payment, payment.data!!.paymentWidgetData.first(), generatePaymentRequest(payment), listData.value, listData.value.cost()!!, shipmentPlatformFeeData)
    }

    fun choosePayment(gatewayCode: String, metadata: String) {
        viewModelScope.launch(dispatchers.immediate) {
            pageState.value = CheckoutPageState.Loading
            val updateCartResult = cartProcessor.updateCart(
                cartProcessor.generateUpdateCartRequest(listData.value),
                UPDATE_CART_SOURCE_PAYMENT,
                UpdateCartPaymentRequest(
                    gatewayCode = gatewayCode,
                    metadata = metadata
                )
            )
            pageState.value = CheckoutPageState.Normal
            if (!updateCartResult.isSuccess) {
                toasterProcessor.commonToaster.emit(
                    CheckoutPageToaster(
                        Toaster.TYPE_ERROR,
                        updateCartResult.toasterMessage,
                        updateCartResult.throwable,
                        source = "update-cart"
                    )
                )
                return@launch
            }
            val checkoutItems = listData.value.toMutableList()
            val currentPayment = checkoutItems.payment()!!
            checkoutItems[checkoutItems.size - PAYMENT_INDEX_FROM_BOTTOM] = currentPayment.copy(
                data = null,
                tenorList = null,
                installmentData = null,
                widget = currentPayment.widget.copy(
                    state = CheckoutPaymentWidgetState.Loading
                ),
                originalData = OriginalCheckoutPaymentData(
                    gatewayCode = gatewayCode,
                    metadata = metadata
                )
            )
            listData.value = checkoutItems
            validatePromo(skipEE = true)
        }
    }

    fun chooseInstallmentCC(selectedInstallment: TenorListData, installmentList: List<TenorListData>) {
        viewModelScope.launch(dispatchers.immediate) {
            pageState.value = CheckoutPageState.Loading
            val currPayment = listData.value.payment()!!.data!!.paymentWidgetData.first()
            var currPaymentMetadata = currPayment.metadata
            try {
                val metadata = JsonParser().parse(currPaymentMetadata)
                val jsonObject = metadata.asJsonObject
                val expressCheckoutParams =
                    jsonObject.getAsJsonObject(UpdateCartPaymentRequest.EXPRESS_CHECKOUT_PARAM)
                jsonObject.addProperty(
                    UpdateCartPaymentRequest.GATEWAY_CODE,
                    selectedInstallment.gatewayCode
                )
                expressCheckoutParams.addProperty(
                    UpdateCartPaymentRequest.INSTALLMENT_TERM,
                    selectedInstallment.tenure.toString()
                )
                currPaymentMetadata = metadata.toString()
            } catch (e: RuntimeException) {
                Timber.d(e)
                pageState.value = CheckoutPageState.Normal
                toasterProcessor.commonToaster.emit(
                    CheckoutPageToaster(
                        Toaster.TYPE_ERROR
                    )
                )
                return@launch
            }
            val updateCartResult = cartProcessor.updateCart(
                cartProcessor.generateUpdateCartRequest(listData.value),
                UPDATE_CART_SOURCE_PAYMENT,
                UpdateCartPaymentRequest(
                    gatewayCode = selectedInstallment.gatewayCode,
                    metadata = currPaymentMetadata,
                    tenureType = selectedInstallment.tenure
                )
            )
            if (!updateCartResult.isSuccess) {
                pageState.value = CheckoutPageState.Normal
                toasterProcessor.commonToaster.emit(
                    CheckoutPageToaster(
                        Toaster.TYPE_ERROR,
                        updateCartResult.toasterMessage,
                        updateCartResult.throwable,
                        source = "update-cart"
                    )
                )
                return@launch
            }
            val checkoutItems = listData.value.toMutableList()
            val payment = checkoutItems.payment()!!
            val paymentWidgetData = payment.data!!.paymentWidgetData.toMutableList()
            val originalPaymentData = paymentWidgetData.first()
            val newPaymentData = originalPaymentData.copy(
                installmentPaymentData = originalPaymentData.installmentPaymentData.copy(
                    selectedTenure = selectedInstallment.tenure
                ),
                gatewayCode = selectedInstallment.gatewayCode,
                metadata = currPaymentMetadata
            )
            paymentWidgetData[0] = newPaymentData
            checkoutItems[checkoutItems.size - PAYMENT_INDEX_FROM_BOTTOM] = payment.copy(
                widget = payment.widget.copy(state = CheckoutPaymentWidgetState.Loading),
                data = payment.data.copy(
                    paymentWidgetData = paymentWidgetData
                ),
                tenorList = installmentList
            )
            listData.value = checkoutItems
            validatePromo(skipEE = true, skipPaymentMandatoryHit = true)
            pageState.value = CheckoutPageState.Normal
        }
    }

    fun chooseInstallment(selectedInstallment: GoCicilInstallmentOption, installmentList: List<GoCicilInstallmentOption>, tickerMessage: String, silent: Boolean) {
        viewModelScope.launch(dispatchers.immediate) {
            pageState.value = CheckoutPageState.Loading
            val currPayment = listData.value.payment()!!.data!!.paymentWidgetData.first()
            val updateCartResult = cartProcessor.updateCart(
                cartProcessor.generateUpdateCartRequest(listData.value),
                UPDATE_CART_SOURCE_PAYMENT,
                UpdateCartPaymentRequest(
                    gatewayCode = currPayment.gatewayCode,
                    metadata = currPayment.metadata,
                    tenureType = selectedInstallment.installmentTerm,
                    optionId = selectedInstallment.optionId
                )
            )
            pageState.value = CheckoutPageState.Normal
            if (!updateCartResult.isSuccess) {
                toasterProcessor.commonToaster.emit(
                    CheckoutPageToaster(
                        Toaster.TYPE_ERROR,
                        updateCartResult.toasterMessage,
                        updateCartResult.throwable,
                        source = "update-cart"
                    )
                )
                return@launch
            }
            val checkoutItems = listData.value.toMutableList()
            val payment = checkoutItems.payment()!!
            val paymentWidgetData = payment.data!!.paymentWidgetData.toMutableList()
            val originalPaymentData = paymentWidgetData.first()
            val newPaymentData = originalPaymentData.copy(
                installmentPaymentData = originalPaymentData.installmentPaymentData.copy(
                    selectedTenure = selectedInstallment.installmentTerm
                )
            )
            paymentWidgetData[0] = newPaymentData
            checkoutItems[checkoutItems.size - PAYMENT_INDEX_FROM_BOTTOM] = payment.copy(
                widget = payment.widget.copy(state = CheckoutPaymentWidgetState.Loading),
                data = payment.data.copy(
                    paymentWidgetData = paymentWidgetData
                ),
                installmentData = GoCicilInstallmentData(
                    tickerMessage = tickerMessage,
                    installmentOptions = installmentList
                )
            )
            listData.value = checkoutItems
            validatePromo(skipEE = true, skipPaymentMandatoryHit = true)
        }
    }

    fun forceReloadPayment() {
        viewModelScope.launch(dispatchers.immediate) {
            val checkoutItems = listData.value.toMutableList()
            val currentPayment = checkoutItems.payment()!!
            checkoutItems[checkoutItems.size - PAYMENT_INDEX_FROM_BOTTOM] = currentPayment.copy(
                data = null,
                tenorList = null,
                installmentData = null,
                widget = currentPayment.widget.copy(
                    state = CheckoutPaymentWidgetState.Loading
                )
            )
            listData.value = checkoutItems
            validatePromo(skipEE = true)
        }
    }

    fun isPaymentEnable(): Boolean {
        return listData.value.payment()?.enable ?: false
    }

    fun updateCartForPromo(onSuccess: (CheckoutPromoBottomSheetData) -> Unit) {
        viewModelScope.launch(dispatchers.immediate) {
            pageState.value = CheckoutPageState.Loading
            val updateCartRequest = cartProcessor.generateUpdateCartRequest(listData.value)
            val paymentRequest = cartProcessor.generateUpdateCartPaymentRequest(listData.value.payment())
            val updateCartResult = cartProcessor.updateCart(updateCartRequest, UPDATE_CART_SOURCE_CHECKOUT_OPEN_PROMO, paymentRequest)
            if (!updateCartResult.isSuccess) {
                toasterProcessor.commonToaster.emit(
                    CheckoutPageToaster(
                        Toaster.TYPE_ERROR,
                        updateCartResult.toasterMessage,
                        updateCartResult.throwable,
                        source = "update-cart"
                    )
                )
                pageState.value = CheckoutPageState.Normal
            } else {
                pageState.value = CheckoutPageState.Normal
                val promoRequestParam = generateCouponListRecommendationRequest()
                val validateUseRequestParam = generateValidateUsePromoRequestForPromoUsage()
                val boPromoCodes = getBboPromoCodes()
                onSuccess(CheckoutPromoBottomSheetData(promoRequestParam, validateUseRequestParam, boPromoCodes))
            }
        }
    }

    fun updateQuantityProduct(cartId: Long, newValue: Int) {
        val checkoutItems = listData.value.toMutableList()
        val itemIndex =
            checkoutItems.indexOfFirst { it is CheckoutProductModel && it.cartId == cartId }
        if (itemIndex > 0) {
            val product = checkoutItems[itemIndex] as CheckoutProductModel
            var showMaxQtyError = false
            var showMinQtyError = false
            val newQty: Int
            val maxQty = if (product.switchInvenage == 0) {
                product.maxOrder
            } else {
                if (product.invenageValue < product.maxOrder) {
                    product.invenageValue
                } else {
                    product.maxOrder
                }
            }

            if (newValue > maxQty) {
                showMaxQtyError = true
                newQty = maxQty
            } else if (newValue < product.minOrder) {
                showMinQtyError = true
                newQty = product.minOrder
            } else {
                newQty = newValue
            }
            checkoutItems[itemIndex] = product.copy(
                shouldShowMaxQtyError = showMaxQtyError,
                shouldShowMinQtyError = showMinQtyError,
                quantity = newQty,
                prevQuantity = product.prevQuantity
            )
            listData.value = checkoutItems
            viewModelScope.launch(dispatchers.io) {
                doUpdateCartAndReload(checkoutItems, cartId)
            }
        }
    }

    private suspend fun doUpdateCartAndReload(
        checkoutItems: MutableList<CheckoutItem>,
        cartId: Long
    ) {
        debounceQtyJob?.cancel()
        debounceQtyJob = viewModelScope.launch(dispatchers.immediate) {
            delay(500L)
            if (isActive) {
                pageState.value = CheckoutPageState.Loading
                val updateCartResult = cartProcessor.updateCart(cartProcessor.generateUpdateCartRequest(checkoutItems), UPDATE_CART_SOURCE_QUANTITY, cartProcessor.generateUpdateCartPaymentRequest(listData.value.payment()))
                if (!updateCartResult.isSuccess) {
                    toasterProcessor.commonToaster.emit(
                        CheckoutPageToaster(
                            Toaster.TYPE_ERROR,
                            updateCartResult.toasterMessage,
                            updateCartResult.throwable,
                            source = "update_quantity"
                        )
                    )

                    pageState.value = CheckoutPageState.Normal
                    val checkoutItemList = listData.value.toMutableList()
                    val itemIndex =
                        checkoutItemList.indexOfFirst { it is CheckoutProductModel && it.cartId == cartId }
                    if (itemIndex > 0) {
                        val product = checkoutItemList[itemIndex] as CheckoutProductModel

                        val newProduct = product.copy(
                            quantity = product.prevQuantity
                        )
                        checkoutItemList[itemIndex] = newProduct
                        listData.value = checkoutItemList
                    }
                } else {
                    loadSAF(
                        isReloadData = true,
                        skipUpdateOnboardingState = true,
                        isReloadAfterPriceChangeHigher = false
                    ) {
                        cartProcessor.generateModifiedCheckoutItems(it, listData.value)
                    }
                }
            }
        }
    }

    companion object {
        const val PLATFORM_FEE_CODE = "platform_fee"

        const val EGOLD_ID = 1L
        const val DG_ID = 2L
        const val DONATION_ID = 3L

        const val BO_DEFAULT_ERROR_MESSAGE = "Bebas ongkir gagal diaplikasikan, silahkan coba lagi"

        const val SHIPMENT_NOT_COMPLETE_ERROR_MESSAGE = "Pilih pengiriman dulu yuk sebelum lanjut bayar."
        const val NO_VALID_ORDER_ERROR_MESSAGE = "Barangmu lagi nggak bisa dibeli. Silakan balik ke keranjang untuk cek belanjaanmu."
        const val NO_PAYMENT_ERROR_MESSAGE = "Atur pembayaran dulu yuk sebelum lanjut bayar."
        const val INVALID_PAYMENT_ERROR_MESSAGE = "Oops, metode pembayaran pilihanmu tidak bisa dipakai di transaksi ini."
        const val INVALID_DROPSHIP_ERROR_MESSAGE = "Pastikan Anda telah melengkapi informasi tambahan."
        const val DISABLED_DROPSHIP_ERROR_MESSAGE = "Fitur dropshipper tidak dapat digunakan ketika menggunakan layanan tambahan"
        const val INSTALLMENT_ERROR_MESSAGE = "Maaf, ada kendala saat memproses pesananmu. Pilih jenis pembayaran lagi."

        const val SOURCE_LOCAL = "local"

        const val CART_TYPE_OCC = "buy now"
        const val CART_TYPE_ATC = "add to cart"
        const val OCC = "occ"
    }
}

internal fun <T, R> List<T>.firstOrNullInstanceOf(kClass: Class<R>): R? {
    val item = firstOrNull { kClass.isInstance(it) }
    @Suppress("UNCHECKED_CAST")
    return item as? R
}

internal const val ERROR_TICKER_INDEX = 0
internal fun List<CheckoutItem>.errorTicker(): CheckoutTickerErrorModel? {
    val item = getOrNull(ERROR_TICKER_INDEX)
    return item as? CheckoutTickerErrorModel
}

internal const val ADDRESS_INDEX = 2
internal fun List<CheckoutItem>.address(): CheckoutAddressModel? {
    val item = getOrNull(ADDRESS_INDEX)
    return item as? CheckoutAddressModel
}

internal const val UPSELL_ADDRESS = 3
internal fun List<CheckoutItem>.upsell(): CheckoutUpsellModel? {
    val item = getOrNull(UPSELL_ADDRESS)
    return item as? CheckoutUpsellModel
}

internal const val EPHARMACY_INDEX_FROM_BOTTOM = 6
internal fun List<CheckoutItem>.epharmacy(): CheckoutEpharmacyModel? {
    val item = getOrNull(size - EPHARMACY_INDEX_FROM_BOTTOM)
    return item as? CheckoutEpharmacyModel
}

internal const val PROMO_INDEX_FROM_BOTTOM = 5
internal fun List<CheckoutItem>.promo(): CheckoutPromoModel? {
    val item = getOrNull(size - PROMO_INDEX_FROM_BOTTOM)
    return item as? CheckoutPromoModel
}

internal const val PAYMENT_INDEX_FROM_BOTTOM = 4
internal fun List<CheckoutItem>.payment(): CheckoutPaymentModel? {
    val item = getOrNull(size - PAYMENT_INDEX_FROM_BOTTOM)
    return item as? CheckoutPaymentModel
}

internal const val COST_INDEX_FROM_BOTTOM = 3
internal fun List<CheckoutItem>.cost(): CheckoutCostModel? {
    val item = getOrNull(size - COST_INDEX_FROM_BOTTOM)
    return item as? CheckoutCostModel
}

internal const val CROSS_SELL_INDEX_FROM_BOTTOM = 2
internal fun List<CheckoutItem>.crossSellGroup(): CheckoutCrossSellGroupModel? {
    val item = getOrNull(size - CROSS_SELL_INDEX_FROM_BOTTOM)
    return item as? CheckoutCrossSellGroupModel
}

internal const val BUTTON_PAYMENT_INDEX_FROM_BOTTOM = 1
internal fun List<CheckoutItem>.buttonPayment(): CheckoutButtonPaymentModel? {
    val item = getOrNull(size - BUTTON_PAYMENT_INDEX_FROM_BOTTOM)
    return item as? CheckoutButtonPaymentModel
}

data class CheckoutPromoBottomSheetData(
    val promoRequest: PromoRequest,
    val validateUsePromoRequest: ValidateUsePromoRequest,
    val boPromoCodes: List<String>
)
