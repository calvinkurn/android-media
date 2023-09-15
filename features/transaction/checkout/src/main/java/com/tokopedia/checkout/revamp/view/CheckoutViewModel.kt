package com.tokopedia.checkout.revamp.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.addon.presentation.uimodel.AddOnPageResult
import com.tokopedia.analytics.performance.util.EmbraceKey
import com.tokopedia.analytics.performance.util.EmbraceMonitoring
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.analytics.CheckoutTradeInAnalytics
import com.tokopedia.checkout.domain.mapper.ShipmentAddOnProductServiceMapper
import com.tokopedia.checkout.domain.model.cartshipmentform.CampaignTimerUi
import com.tokopedia.checkout.domain.model.cartshipmentform.ShipmentPlatformFeeData
import com.tokopedia.checkout.domain.model.platformfee.PaymentFeeCheckoutRequest
import com.tokopedia.checkout.revamp.view.converter.CheckoutDataConverter
import com.tokopedia.checkout.revamp.view.processor.CheckoutAddOnProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutCalculator
import com.tokopedia.checkout.revamp.view.processor.CheckoutCartProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutDataHelper
import com.tokopedia.checkout.revamp.view.processor.CheckoutLogisticProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutPaymentProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutPromoProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutResult
import com.tokopedia.checkout.revamp.view.processor.CheckoutToasterProcessor
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
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPromoModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerErrorModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutUpsellModel
import com.tokopedia.checkout.view.CheckoutLogger
import com.tokopedia.checkout.view.CheckoutMutableLiveData
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
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class CheckoutViewModel @Inject constructor(
    private val cartProcessor: CheckoutCartProcessor,
    internal val logisticProcessor: CheckoutLogisticProcessor,
    private val promoProcessor: CheckoutPromoProcessor,
    private val addOnProcessor: CheckoutAddOnProcessor,
    private val paymentProcessor: CheckoutPaymentProcessor,
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

    var checkoutPageSource: String = CheckoutConstant.CHECKOUT_PAGE_SOURCE_PDP

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

    fun stopEmbraceTrace() {
        val emptyMap: Map<String, Any> = HashMap()
        EmbraceMonitoring.stopMoments(EmbraceKey.KEY_ACT_BUY, null, emptyMap)
    }

    fun loadSAF(
        isReloadData: Boolean,
        skipUpdateOnboardingState: Boolean,
        isReloadAfterPriceChangeHigher: Boolean
    ) {
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
                isReloadAfterPriceChangeHigher
            )
            stopEmbraceTrace()
            when (saf) {
                is CheckoutPageState.Success -> {
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
                        mTrackerShipment.eventViewInformationAndWarningTickerInCheckout(tickerData.id)
                    }
                    val address = CheckoutAddressModel(
                        recipientAddressModel = dataConverter.getRecipientAddressModel(saf.cartShipmentAddressFormData)
                    )

                    val upsell =
                        dataConverter.getUpsellModel(saf.cartShipmentAddressFormData.newUpsell)

                    isUsingDdp = saf.cartShipmentAddressFormData.isUsingDdp
                    shipmentPlatformFeeData = saf.cartShipmentAddressFormData.shipmentPlatformFee
                    cartDataForRates = saf.cartShipmentAddressFormData.cartData
                    codData = saf.cartShipmentAddressFormData.cod
                    campaignTimer = saf.cartShipmentAddressFormData.campaignTimerUi
                    logisticProcessor.isBoUnstackEnabled =
                        saf.cartShipmentAddressFormData.lastApplyData.additionalInfo.bebasOngkirInfo.isBoUnstackEnabled
                    summariesAddOnUiModel =
                        ShipmentAddOnProductServiceMapper.getShoppingSummaryAddOns(saf.cartShipmentAddressFormData.listSummaryAddons)

                    val items = dataConverter.getCheckoutItems(
                        saf.cartShipmentAddressFormData,
                        address.recipientAddressModel.locationDataModel != null,
                        userSessionInterface.name
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
                            rejectedWording = saf.cartShipmentAddressFormData.epharmacyData.rejectedWording
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
                        isPromoRevamp = isPromoRevamp ?: false
                    )
                    if (promo.isEnable && saf.cartShipmentAddressFormData.lastApplyData.additionalInfo.errorDetail.message.isNotEmpty()) {
                        PromoRevampAnalytics.eventCartViewPromoMessage(saf.cartShipmentAddressFormData.lastApplyData.additionalInfo.errorDetail.message)
                    }

                    val cost = CheckoutCostModel()

                    val crossSellList = arrayListOf<CheckoutCrossSellItem>()
                    if (!tickerError.isError) {
                        val crossSellModel = saf.cartShipmentAddressFormData.crossSell.firstOrNull()
                        if (crossSellModel != null && !crossSellModel.checkboxDisabled) {
                            crossSellList.add(
                                CheckoutCrossSellModel(
                                    crossSellModel,
                                    crossSellModel.isChecked,
                                    !crossSellModel.checkboxDisabled,
                                    0
                                )
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
                            crossSellList.add(
                                CheckoutEgoldModel(
                                    saf.cartShipmentAddressFormData.egoldAttributes!!,
                                    saf.cartShipmentAddressFormData.egoldAttributes!!.isChecked,
                                    saf.cartShipmentAddressFormData.egoldAttributes!!.buyEgoldValue
                                )
                            )
                        }
                        if (saf.cartShipmentAddressFormData.donation != null && saf.cartShipmentAddressFormData.donation!!.title.isNotEmpty() && saf.cartShipmentAddressFormData.donation!!.nominal != 0) {
                            crossSellList.add(
                                CheckoutDonationModel(
                                    saf.cartShipmentAddressFormData.donation!!,
                                    saf.cartShipmentAddressFormData.donation!!.isChecked
                                )
                            )
                            if (saf.cartShipmentAddressFormData.donation!!.isChecked) {
                                mTrackerShipment.eventViewAutoCheckDonation(
                                    userSessionInterface.userId
                                )
                            }
                        }
                    }
                    val crossSellGroup = CheckoutCrossSellGroupModel(crossSellList = crossSellList)

                    val buttonPayment = CheckoutButtonPaymentModel("")

                    withContext(dispatchers.main) {
                        listData.value = listOf(
                            tickerError,
                            ticker,
                            address,
                            upsell
                        ) + items + listOf(epharmacy, promo, cost, crossSellGroup, buttonPayment)
                        pageState.value = saf
                        calculateTotal()
                    }
                    sendEEStep2()
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
                val orderProducts = getOrderProducts(shipmentCartItemModel.cartStringGroup)
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
                    enhancedECommerceCheckout.addProduct(
                        enhancedECommerceProductCartMapData.getProduct()
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
            step
        )
        mTrackerShipment.flushEnhancedECommerceCheckout()
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
                        val hasSelectCourierInAnyOrder = listData.value
                            .any { it is CheckoutOrderModel && it.shipment.courierItemData != null }
                        if (hasSelectCourierInAnyOrder) {
                            validatePromo()
                        } else {
                            getInitialEntryPointInfo()
                        }
                    }
                }
            }
        }
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
            )
    }

    private fun loadCourierState(
        shipmentCartItemModel: CheckoutOrderModel,
        recipientAddressModel: RecipientAddressModel?
    ): Boolean {
        if (!shipmentCartItemModel.isCustomPinpointError && !shipmentCartItemModel.isStateHasLoadCourierState && shouldAutoLoadCourier(
                shipmentCartItemModel,
                recipientAddressModel
            )
        ) {
            shipmentCartItemModel.isStateHasLoadCourierState = true
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
                logisticProcessor.editAddressPinpoint(latitude, longitude, recipientAddressModel)
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

    private fun fetchEpharmacyData() {
        viewModelScope.launch(dispatchers.immediate) {
            addOnProcessor.fetchEpharmacyData(listData.value) {
                if (it != null) {
                    listData.value = it
                    pageState.value = CheckoutPageState.EpharmacyCoachMark
                    calculateTotal()
                }
            }
        }
    }

    private fun getInitialEntryPointInfo() {
        if (isPromoRevamp == true) {
            viewModelScope.launch(dispatchers.immediate) {
                val checkoutModel = listData.value.promo()!!
                withContext(dispatchers.main) {
                    val currentListData = listData.value
                    listData.value = currentListData.map { model ->
                        if (model is CheckoutPromoModel) {
                            return@map checkoutModel.copy(
                                isLoading = true
                            )
                        } else {
                            return@map model
                        }
                    }
                }
                val entryPointInfo = promoProcessor.getEntryPointInfo(
                    generateCouponListRecommendationRequest()
                )
                withContext(dispatchers.main) {
                    val currentListData = listData.value
                    listData.value = currentListData.map { model ->
                        if (model is CheckoutPromoModel) {
                            return@map checkoutModel.copy(
                                entryPointInfo = entryPointInfo,
                                isLoading = false
                            )
                        } else {
                            return@map model
                        }
                    }
                }
            }
        }
    }

    private suspend fun getEntryPointInfo(
        checkoutItems: List<CheckoutItem>,
        oldCheckoutItems: List<CheckoutItem>
    ): List<CheckoutItem> {
        withContext(dispatchers.main) {
            listData.value.promo()?.let {
                val currentListData = listData.value
                listData.value = currentListData.map { model ->
                    if (model is CheckoutPromoModel) {
                        return@map it.copy(
                            isLoading = true
                        )
                    } else {
                        return@map model
                    }
                }
            }
        }

        val checkoutModel =
            checkoutItems.firstOrNull { it is CheckoutPromoModel } as? CheckoutPromoModel
        val oldCheckoutModel =
            oldCheckoutItems.firstOrNull { it is CheckoutPromoModel } as? CheckoutPromoModel

        if (checkoutModel != null && oldCheckoutModel != null) {
            if (isPromoRevamp == true) {
                val oldTotalPromoAmount =
                    oldCheckoutModel.promo.additionalInfo.usageSummaries.sumOf { it.amount }
                val newTotalPromoAmount =
                    checkoutModel.promo.additionalInfo.usageSummaries.sumOf { it.amount }
                val isAnimateWording = newTotalPromoAmount > oldTotalPromoAmount

                val entryPointInfo = promoProcessor
                    .getEntryPointInfo(generateCouponListRecommendationRequest())
                return checkoutItems.map { model ->
                    if (model is CheckoutPromoModel) {
                        return@map model.copy(
                            entryPointInfo = entryPointInfo,
                            isLoading = false,
                            isAnimateWording = isAnimateWording
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

    internal fun calculateTotal() {
        viewModelScope.launch(dispatchers.immediate) {
            listData.value = calculator.calculateWithoutPayment(
                listData.value,
                isTradeInByDropOff,
                summariesAddOnUiModel
            )
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
                calculator.updateShipmentCostModel(
                    listData.value,
                    cost,
                    isTradeInByDropOff,
                    summariesAddOnUiModel
                )
        }
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
        if (order.ratesValidationFlow) {
            loadShippingWithSelly(cartPosition, order)
        } else {
            loadShippingNormal(cartPosition, order)
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

        val result = logisticProcessor.getRatesWithScheduleDelivery(
            logisticProcessor.getRatesParam(
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
            ),
            order.shopShipmentList,
            order.shippingId,
            order.spId,
            order.fulfillmentId.toString(),
            order,
            isOneClickShipment, isTradeIn, isTradeInByDropOff
        )
        val list = listData.value.toMutableList()
        val orderModel = list[cartPosition] as? CheckoutOrderModel
        if (orderModel != null) {
            if (result?.courier != null) {
                val courierItemData = result.courier
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
                    removeInvalidBoCodeFromPromoRequest(
                        orderModel,
                        list,
                        validateUsePromoRequest
                    )
                    doValidateUseLogisticPromo(
                        cartPosition,
                        orderModel.cartStringGroup,
                        validateUsePromoRequest,
                        courierItemData.selectedShipper.logPromoCode!!,
                        true,
                        courierItemData,
                        result.insurance
                    )
                    return
                }
            }
            if (result != null && result.akamaiError.isNotEmpty()) {
                pageState.value = CheckoutPageState.AkamaiRatesError(result.akamaiError)
            }
            val newOrderModel = orderModel.copy(
                shipment = orderModel.shipment.copy(
                    isLoading = false,
                    courierItemData = result?.courier,
                    shippingCourierUiModels = result?.couriers ?: emptyList(),
                    insurance = result?.insurance?.let {
                        CheckoutOrderInsurance(
                            when (it.insuranceType) {
                                InsuranceConstant.INSURANCE_TYPE_MUST -> {
                                    true
                                }

                                InsuranceConstant.INSURANCE_TYPE_NO -> {
                                    false
                                }

                                InsuranceConstant.INSURANCE_TYPE_OPTIONAL -> {
                                    it.insuranceUsedDefault == InsuranceConstant.INSURANCE_USED_DEFAULT_YES
                                }

                                else -> false
                            }
                        )
                    } ?: CheckoutOrderInsurance()
                )
            )
            list[cartPosition] = newOrderModel
            listData.value = list
            cartProcessor.processSaveShipmentState(
                newOrderModel,
                listData.value.address()!!.recipientAddressModel
            )
            calculateTotal()
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

        val result = logisticProcessor.getRates(
            logisticProcessor.getRatesParam(
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
            ),
            order.shopShipmentList,
            order.shippingId,
            order.spId,
            order,
            isOneClickShipment,
            isTradeIn,
            isTradeInByDropOff
        )
        val list = listData.value.toMutableList()
        val orderModel = list[cartPosition] as? CheckoutOrderModel
        if (orderModel != null) {
            if (result?.courier != null) {
                val courierItemData = result.courier
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
                    removeInvalidBoCodeFromPromoRequest(
                        order,
                        list,
                        validateUsePromoRequest
                    )
                    doValidateUseLogisticPromo(
                        cartPosition,
                        orderModel.cartStringGroup,
                        validateUsePromoRequest,
                        courierItemData.selectedShipper.logPromoCode!!,
                        false,
                        courierItemData,
                        result.insurance
                    )
                    return
                }
            }
            if (result != null && result.akamaiError.isNotEmpty()) {
                pageState.value = CheckoutPageState.AkamaiRatesError(result.akamaiError)
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
                    )
                )
                commonToaster.emit(
                    CheckoutPageToaster(
                        Toaster.TYPE_NORMAL,
                        "Bebas ongkir gagal diaplikasikan, silahkan coba lagi"
                    )
                )
            }
            val newOrderModel = orderModel.copy(
                shipment = orderModel.shipment.copy(
                    isLoading = false,
                    courierItemData = result?.courier,
                    shippingCourierUiModels = result?.couriers ?: emptyList(),
                    insurance = result?.insurance?.let {
                        CheckoutOrderInsurance(
                            when (it.insuranceType) {
                                InsuranceConstant.INSURANCE_TYPE_MUST -> {
                                    true
                                }

                                InsuranceConstant.INSURANCE_TYPE_NO -> {
                                    false
                                }

                                InsuranceConstant.INSURANCE_TYPE_OPTIONAL -> {
                                    it.insuranceUsedDefault == InsuranceConstant.INSURANCE_USED_DEFAULT_YES
                                }

                                else -> false
                            }
                        )
                    } ?: CheckoutOrderInsurance()
                )
            )
            list[cartPosition] = newOrderModel
            listData.value = list
            cartProcessor.processSaveShipmentState(
                newOrderModel,
                listData.value.address()!!.recipientAddressModel
            )
            calculateTotal()
            sendEEStep3()
        }
    }

    fun setSelectedCourier(
        cartPosition: Int,
        courierItemData: CourierItemData,
        shippingCourierUiModels: List<ShippingCourierUiModel>,
        insurance: InsuranceData
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
                val newShipment = shipment.copy(
                    isLoading = true,
                    courierItemData = courierItemData,
                    shippingCourierUiModels = shippingCourierUiModels,
                    insurance = CheckoutOrderInsurance(
                        when (insurance.insuranceType) {
                            InsuranceConstant.INSURANCE_TYPE_MUST -> {
                                true
                            }

                            InsuranceConstant.INSURANCE_TYPE_NO -> {
                                false
                            }

                            InsuranceConstant.INSURANCE_TYPE_OPTIONAL -> {
                                insurance.insuranceUsedDefault == InsuranceConstant.INSURANCE_USED_DEFAULT_YES
                            }

                            else -> false
                        }
                    )
                )
                val newOrder = checkoutOrderModel.copy(shipment = newShipment)
                checkoutItems[cartPosition] = newOrder
                listData.value = checkoutItems
                val shouldClearPromoBenefit = promoProcessor.clearPromo(
                    ClearPromoOrder(
                        checkoutOrderModel.boUniqueId,
                        checkoutOrderModel.boMetadata.boType,
                        arrayListOf(shipment.courierItemData.selectedShipper.logPromoCode!!),
                        checkoutOrderModel.shopId,
                        checkoutOrderModel.isProductIsPreorder,
                        checkoutOrderModel.preOrderDurationDay.toString(),
                        checkoutOrderModel.fulfillmentId,
                        checkoutOrderModel.cartStringGroup
                    )
                )
                if (shouldClearPromoBenefit) {
                    val list = listData.value.toMutableList()
                    val newPromo = list.promo()!!.copy(promo = LastApplyUiModel())
                    list[list.size - 4] = newPromo
                    listData.value = list
                }
            }
            val list = listData.value.toMutableList()
            var newOrder = list[cartPosition] as CheckoutOrderModel
            val newShipment = shipment.copy(
                isLoading = false,
                courierItemData = courierItemData,
                shippingCourierUiModels = shippingCourierUiModels
            )
            newOrder = newOrder.copy(shipment = newShipment, isShippingBorderRed = false)
            list[cartPosition] = newOrder
            listData.value = list
            cartProcessor.processSaveShipmentState(
                newOrder,
                listData.value.address()!!.recipientAddressModel
            )
            validatePromo()
            pageState.value = CheckoutPageState.Normal
        }
    }

    fun generateValidateUsePromoRequest(list: List<CheckoutItem>? = null): ValidateUsePromoRequest {
        return promoProcessor.generateValidateUsePromoRequest(
            list ?: listData.value,
            isTradeIn,
            isTradeInByDropOff,
            isOneClickShipment
        )
    }

    fun generateCouponListRecommendationRequest(): PromoRequest {
        return promoProcessor.generateCouponListRecommendationRequest(
            listData.value,
            isTradeIn,
            isTradeInByDropOff,
            isOneClickShipment
        )
    }

    fun getBboPromoCodes(): ArrayList<String> {
        return ArrayList(promoProcessor.bboPromoCodes)
    }

    internal fun removeInvalidBoCodeFromPromoRequest(
        order: CheckoutOrderModel,
        list: List<CheckoutItem>,
        validateUsePromoRequest: ValidateUsePromoRequest
    ) {
        if (!order.isFreeShippingPlus) {
            val shipmentCartItemModelLists =
                list.filterIsInstance(
                    CheckoutOrderModel::class.java
                )
            for (tmpShipmentCartItemModel in shipmentCartItemModelLists) {
                for (promoOrder in validateUsePromoRequest.orders) {
                    if (order.cartStringGroup != tmpShipmentCartItemModel.cartStringGroup && tmpShipmentCartItemModel.cartStringGroup == promoOrder.cartStringGroup && tmpShipmentCartItemModel.shipment.courierItemData != null && !tmpShipmentCartItemModel.isFreeShippingPlus) {
                        promoOrder.codes.remove(
                            tmpShipmentCartItemModel.shipment.courierItemData.selectedShipper.logPromoCode
                        )
                        promoOrder.boCode = ""
                    }
                }
            }
        }
    }

    private suspend fun validatePromo() {
        val checkoutItems = listData.value.toMutableList()
        var newItems = promoProcessor.validateUse(
            promoProcessor.generateValidateUsePromoRequest(
                checkoutItems,
                isTradeIn,
                isTradeInByDropOff,
                isOneClickShipment
            ),
            checkoutItems,
            isOneClickShipment,
            isTradeIn,
            isTradeInByDropOff
        )
        newItems = getEntryPointInfo(newItems, checkoutItems)
        listData.value = newItems
        calculateTotal()
        sendEEStep3()
    }

    fun doValidateUseLogisticPromoNew(
        cartPosition: Int,
        cartString: String,
        promoCode: String,
        showLoading: Boolean,
        courierItemData: CourierItemData,
        insurance: InsuranceData
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
            removeInvalidBoCodeFromPromoRequest(
                shipmentCartItemModel,
                listData.value,
                validateUsePromoRequest
            )
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
                courierItemData,
                insurance
            )
        }
    }

    private suspend fun doValidateUseLogisticPromo(
        cartPosition: Int,
        cartString: String,
        validateUsePromoRequest: ValidateUsePromoRequest,
        promoCode: String,
        showLoading: Boolean,
        courierItemData: CourierItemData,
        insurance: InsuranceData
    ) {
        if (showLoading) {
            pageState.value = CheckoutPageState.Loading
            listData.value = listData.value.map { model ->
                when (model) {
                    is CheckoutOrderModel -> {
                        return@map model.copy(
                            shipment = model.shipment.copy(isLoading = true),
                            isShippingBorderRed = false
                        )
                    }

                    is CheckoutPromoModel -> {
                        return@map model.copy(
                            isLoading = true
                        )
                    }

                    else -> {
                        return@map model
                    }
                }
            }
        }
        var newItems = promoProcessor.validateUseLogisticPromo(
            validateUsePromoRequest,
            cartString,
            promoCode,
            listData.value,
            courierItemData,
            isOneClickShipment,
            isTradeIn,
            isTradeInByDropOff
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

    fun setSelectedScheduleDelivery(
        cartPosition: Int,
        order: CheckoutOrderModel,
        courierItemData: CourierItemData,
        scheduleDeliveryUiModel: ScheduleDeliveryUiModel,
        newCourierItemData: CourierItemData
    ) {
        viewModelScope.launch(dispatchers.immediate) {
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
        if (courierItemData.selectedShipper.logPromoCode != newCourierItemData.selectedShipper.logPromoCode) {
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
                )
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
        removeInvalidBoCodeFromPromoRequest(order, listData.value, validateUsePromoRequest)
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
                ordersItem.validationMetadata = order.validationMetadata
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
            isTradeInByDropOff
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
            courierItemData = newCourierItemData
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
            )
        )
        if (shouldClearPromoBenefit) {
            val list = listData.value.toMutableList()
            val newPromo = list.promo()!!.copy(promo = LastApplyUiModel())
            list[list.size - 4] = newPromo
            listData.value = list
        }
        val list = listData.value.toMutableList()
        var newOrder1 = list[cartPosition] as CheckoutOrderModel
        val newShipment1 = shipment.copy(
            isLoading = false,
            courierItemData = newCourierItemData
        )
        newOrder1 = newOrder1.copy(shipment = newShipment1, isShippingBorderRed = false)
        list[cartPosition] = newOrder1
        listData.value = list
        cartProcessor.processSaveShipmentState(
            newOrder1,
            listData.value.address()!!.recipientAddressModel
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
            courierItemData = newCourierItemData
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
            listData.value.address()!!.recipientAddressModel
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
            val checkoutEpharmacy = items.epharmacy()
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
                            if (!cartItemModel.isError && cartItemModel.ethicalDrugDataModel.needPrescription) {
                                val prescriptionIdsEmpty =
                                    checkoutItem.prescriptionIds.isEmpty()
                                val consultationEmpty =
                                    checkoutItem.tokoConsultationId.isEmpty() ||
                                        checkoutItem.partnerConsultationId.isEmpty() ||
                                        checkoutItem.tokoConsultationId == "0" ||
                                        checkoutItem.partnerConsultationId == "0" ||
                                        checkoutItem.consultationDataString.isEmpty()
                                if (prescriptionIdsEmpty && consultationEmpty) {
                                    isPrescriptionFrontEndValidationError = true
                                    break
                                }
                            }
                        }
                    }
                }
                if (checkoutItem is CheckoutEpharmacyModel) {
                    if (isPrescriptionFrontEndValidationError) {
                        items[index] =
                            checkoutItem.copy(epharmacy = checkoutItem.epharmacy.copy(isError = true))
                    }
                }
            }
            if (firstErrorIndex > -1 || isPrescriptionFrontEndValidationError) {
                pageState.value = CheckoutPageState.Normal
                listData.value = items
                pageState.value = CheckoutPageState.ScrollTo(firstErrorIndex)
                if (hasUnselectedCourier) {
                    commonToaster.emit(
                        CheckoutPageToaster(
                            Toaster.TYPE_NORMAL,
                            "Pilih pengiriman dulu yuk sebelum lanjut bayar."
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
                        "Barangmu lagi nggak bisa dibeli. Silakan balik ke keranjang untuk cek belanjaanmu."
                    )
                )
                pageState.value = CheckoutPageState.Normal
            }
            val errorToaster =
                addOnProcessor.saveAddOnsProductBeforeCheckout(listData.value, isOneClickShipment)
            if (errorToaster != null) {
                commonToaster.emit(errorToaster)
                pageState.value = CheckoutPageState.Normal
                return@launch
            }
            val validateUsePromoRevampUiModel = promoProcessor.finalValidateUse(
                promoProcessor.generateValidateUsePromoRequest(
                    listData.value,
                    isTradeIn,
                    isTradeInByDropOff,
                    isOneClickShipment
                )
            )
            if (validateUsePromoRevampUiModel != null) {
                val itemList = listData.value.toMutableList()
                itemList[itemList.size - 4] = itemList.promo()!!.copy(
                    promo = LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(
                        validateUsePromoRevampUiModel.promoUiModel
                    )
                )
                listData.value = itemList
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
                            listData.value
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
                            true
                        )
                    } else {
                        commonToaster.emit(
                            CheckoutPageToaster(
                                Toaster.TYPE_ERROR,
                                CheckoutConstant.DEFAULT_ERROR_MESSAGE_VALIDATE_PROMO
                            )
                        )
                        pageState.value = CheckoutPageState.Normal
                    }
                } else {
                    doCheckout(
                        validateUsePromoRevampUiModel,
                        fingerprintPublicKey,
                        onSuccessCheckout,
                        false
                    )
                }
            } else {
                commonToaster.emit(
                    CheckoutPageToaster(
                        Toaster.TYPE_ERROR,
                        CheckoutConstant.DEFAULT_ERROR_MESSAGE_VALIDATE_PROMO
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
        hasClearPromoBeforeCheckout: Boolean
    ) {
        cartProcessor.processSaveShipmentState(listData.value, recipientAddressModel)
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
            hasClearPromoBeforeCheckout
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
                    throwable = checkoutResult.throwable
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
                    "Barangmu lagi nggak bisa dibeli. Silakan balik ke keranjang untuk cek belanjaanmu."
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
                    toasterMessage
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
        checkoutItems[checkoutItems.size - 2] = crossSellGroup.copy(crossSellList = newList)
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
        checkoutItems[checkoutItems.size - 2] = crossSellGroup.copy(crossSellList = newList)
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
        checkoutItems[checkoutItems.size - 2] = crossSellGroup.copy(crossSellList = newList)
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
                                    )
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
                        isLoading = true
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
                validateUsePromoRevampUiModel.promoUiModel.copy(voucherOrderUiModels = validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels.filter { !it.isTypeLogistic() })
            val newPromo = list.promo()!!.copy(
                promo = LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(
                    promoUiModel
                )
            )
            list[list.size - 4] = newPromo
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
            val result = logisticProcessor.getRatesWithBoCode(
                logisticProcessor.getRatesParam(
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
                ),
                order.shopShipmentList,
                voucher.shippingId,
                voucher.spId,
                order,
                isTradeInByDropOff,
                voucher.code,
                isOneClickShipment,
                isTradeIn
            )
            if (result?.courier != null) {
                val courierItemData = result.courier
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
                    removeInvalidBoCodeFromPromoRequest(
                        order,
                        checkoutItems,
                        validateUsePromoRequest
                    )
                    checkoutItems = promoProcessor.validateUseLogisticPromo(
                        validateUsePromoRequest,
                        order.cartStringGroup,
                        courierItemData.selectedShipper.logPromoCode!!,
                        checkoutItems,
                        courierItemData,
                        isOneClickShipment,
                        isTradeIn,
                        isTradeInByDropOff
                    ).toMutableList()
                }
            } else {
                if (result != null && result.akamaiError.isNotEmpty()) {
                    pageState.value = CheckoutPageState.AkamaiRatesError(result.akamaiError)
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
                    )
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
                        )
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
        promoProcessor.clearAllBo(listData.value)
    }

    fun setAddon(
        checked: Boolean,
        addOnProductDataItemModel: AddOnProductDataItemModel,
        position: Int
    ) {
        val checkoutItems = listData.value.toMutableList()
        val checkoutProductModel = checkoutItems[position] as CheckoutProductModel
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

    fun getOrderProducts(cartStringGroup: String): List<CheckoutProductModel> {
        return helper.getOrderProducts(listData.value, cartStringGroup)
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
                )
            )
            validatePromo()
        }
    }

    fun useNewPromoPage(): Boolean {
        return isPromoRevamp == true
    }

    companion object {
        const val PLATFORM_FEE_CODE = "platform_fee"
    }
}

internal fun <T, R> List<T>.firstOrNullInstanceOf(kClass: Class<R>): R? {
    val item = firstOrNull { kClass.isInstance(it) }
    @Suppress("UNCHECKED_CAST")
    return item as? R
}

internal fun List<CheckoutItem>.errorTicker(): CheckoutTickerErrorModel? {
    val item = getOrNull(0)
    return item as? CheckoutTickerErrorModel
}

internal fun List<CheckoutItem>.address(): CheckoutAddressModel? {
    val item = getOrNull(2)
    return item as? CheckoutAddressModel
}

internal fun List<CheckoutItem>.upsell(): CheckoutUpsellModel? {
    val item = getOrNull(3)
    return item as? CheckoutUpsellModel
}

internal fun List<CheckoutItem>.epharmacy(): CheckoutEpharmacyModel? {
    val item = getOrNull(size - 5)
    return item as? CheckoutEpharmacyModel
}

internal fun List<CheckoutItem>.promo(): CheckoutPromoModel? {
    val item = getOrNull(size - 4)
    return item as? CheckoutPromoModel
}

internal fun List<CheckoutItem>.cost(): CheckoutCostModel? {
    val item = getOrNull(size - 3)
    return item as? CheckoutCostModel
}

internal fun List<CheckoutItem>.crossSellGroup(): CheckoutCrossSellGroupModel? {
    val item = getOrNull(size - 2)
    return item as? CheckoutCrossSellGroupModel
}

internal fun List<CheckoutItem>.buttonPayment(): CheckoutButtonPaymentModel? {
    val item = getOrNull(size - 1)
    return item as? CheckoutButtonPaymentModel
}
