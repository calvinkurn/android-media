package com.tokopedia.checkout.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.checkout.R
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.data.model.request.changeaddress.DataChangeAddressRequest
import com.tokopedia.checkout.data.model.request.checkout.Carts
import com.tokopedia.checkout.data.model.request.checkout.CheckoutRequest
import com.tokopedia.checkout.data.model.request.checkout.Data
import com.tokopedia.checkout.data.model.request.checkout.Egold
import com.tokopedia.checkout.data.model.request.checkout.FEATURE_TYPE_REGULAR_PRODUCT
import com.tokopedia.checkout.data.model.request.checkout.FEATURE_TYPE_TOKONOW_PRODUCT
import com.tokopedia.checkout.data.model.request.checkout.Promo
import com.tokopedia.checkout.data.model.request.checkout.TokopediaCorner
import com.tokopedia.checkout.data.model.request.checkout.cross_sell.CrossSellItemRequestModel
import com.tokopedia.checkout.data.model.request.checkout.cross_sell.CrossSellRequest
import com.tokopedia.checkout.data.model.request.saf.ShipmentAddressFormRequest
import com.tokopedia.checkout.data.model.request.saveshipmentstate.SaveShipmentStateRequest
import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateDropshipData
import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateProductData
import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateProductPreorder
import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateRequestData
import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateShippingInfoData
import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateShopProductData
import com.tokopedia.checkout.domain.mapper.DynamicDataPassingMapper.ATTRIBUTE_ADDON_DETAILS
import com.tokopedia.checkout.domain.mapper.DynamicDataPassingMapper.ATTRIBUTE_DONATION
import com.tokopedia.checkout.domain.mapper.DynamicDataPassingMapper.ORDER_LEVEL
import com.tokopedia.checkout.domain.mapper.DynamicDataPassingMapper.PAYMENT_LEVEL
import com.tokopedia.checkout.domain.mapper.DynamicDataPassingMapper.PRODUCT_LEVEL
import com.tokopedia.checkout.domain.mapper.DynamicDataPassingMapper.SOURCE_NORMAL
import com.tokopedia.checkout.domain.mapper.DynamicDataPassingMapper.SOURCE_OCS
import com.tokopedia.checkout.domain.mapper.DynamicDataPassingMapper.getAddOnFromSAF
import com.tokopedia.checkout.domain.mapper.ShipmentAddOnProductServiceMapper
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.domain.model.cartshipmentform.CampaignTimerUi
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.cartshipmentform.EpharmacyData
import com.tokopedia.checkout.domain.model.cartshipmentform.ShipmentPlatformFeeData
import com.tokopedia.checkout.domain.model.platformfee.PaymentFeeCheckoutRequest
import com.tokopedia.checkout.domain.model.platformfee.PaymentFeeGqlResponse
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressGqlUseCase
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressRequest
import com.tokopedia.checkout.domain.usecase.CheckoutUseCase
import com.tokopedia.checkout.domain.usecase.GetPaymentFeeCheckoutUseCase
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormV4UseCase
import com.tokopedia.checkout.domain.usecase.ReleaseBookingUseCase
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateGqlUseCase
import com.tokopedia.checkout.utils.CheckoutFingerprintUtil.getEnableFingerprintPayment
import com.tokopedia.checkout.utils.CheckoutFingerprintUtil.getFingerprintPublicKey
import com.tokopedia.checkout.view.ShipmentContract.AnalyticsActionListener
import com.tokopedia.checkout.view.converter.RatesDataConverter.Companion.getLogisticPromoCode
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.checkout.view.converter.ShipmentDataRequestConverter
import com.tokopedia.checkout.view.converter.ShipmentDataRequestConverter.Companion.generateRatesFeature
import com.tokopedia.checkout.view.helper.ShipmentCartItemModelHelper.getFirstProductId
import com.tokopedia.checkout.view.helper.ShipmentGetCourierHolderData
import com.tokopedia.checkout.view.helper.ShipmentScheduleDeliveryMapData
import com.tokopedia.checkout.view.helper.ShipmentValidatePromoHolderData
import com.tokopedia.checkout.view.uimodel.CrossSellModel
import com.tokopedia.checkout.view.uimodel.CrossSellOrderSummaryModel
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.checkout.view.uimodel.EgoldTieringModel
import com.tokopedia.checkout.view.uimodel.ShipmentAddOnSummaryModel
import com.tokopedia.checkout.view.uimodel.ShipmentButtonPaymentModel
import com.tokopedia.checkout.view.uimodel.ShipmentCostModel
import com.tokopedia.checkout.view.uimodel.ShipmentCrossSellModel
import com.tokopedia.checkout.view.uimodel.ShipmentDonationModel
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.checkout.view.uimodel.ShipmentPaymentFeeModel
import com.tokopedia.checkout.view.uimodel.ShipmentTickerErrorModel
import com.tokopedia.checkout.view.uimodel.ShipmentUpsellModel
import com.tokopedia.common_epharmacy.EPHARMACY_CONSULTATION_STATUS_APPROVED
import com.tokopedia.common_epharmacy.EPHARMACY_CONSULTATION_STATUS_REJECTED
import com.tokopedia.common_epharmacy.network.response.EPharmacyMiniConsultationResult
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.common_epharmacy.usecase.EPharmacyPrepareProductsGroupUseCase
import com.tokopedia.fingerprint.util.FingerPrintUtil.getPublicKey
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticCommon.data.request.EditPinpointParam
import com.tokopedia.logisticCommon.data.request.UpdatePinpointParam
import com.tokopedia.logisticCommon.domain.usecase.UpdatePinpointUseCase
import com.tokopedia.logisticcart.scheduledelivery.domain.usecase.GetRatesWithScheduleUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.CodModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItem
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingParam
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler.Companion.getErrorMessage
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.analytics.PromoRevampAnalytics.eventCheckoutViewPromoMessage
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCheckout
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.ADD_ON_PRODUCT_STATUS_CHECK
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.ADD_ON_PRODUCT_STATUS_MANDATORY
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.DEFAULT_ERROR_MESSAGE_FAIL_APPLY_BBO
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.DEFAULT_ERROR_MESSAGE_VALIDATE_PROMO
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.addons.domain.SaveAddOnStateUseCase
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.data.model.UpdateDynamicDataPassingUiModel
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.data.request.DynamicDataPassingParamRequest
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.data.request.DynamicDataPassingParamRequest.DynamicDataParam
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.domain.UpdateDynamicDataPassingUseCase
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase.GetPrescriptionIdsUseCaseCoroutine
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
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrderData
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.Order
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.ProductDetail
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ProductDetailsItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.LastApplyUiMapper
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MvcShippingBenefitUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.SummariesItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata.Companion.TYPE_ICON_GLOBAL
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.purchase_platform.common.utils.isNotBlankOrZero
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ShipmentViewModel @Inject constructor(
    private val getShipmentAddressFormV4UseCase: GetShipmentAddressFormV4UseCase,
    private val saveShipmentStateGqlUseCase: SaveShipmentStateGqlUseCase,
    private val changeShippingAddressGqlUseCase: ChangeShippingAddressGqlUseCase,
    private val updatePinpointUseCase: UpdatePinpointUseCase,
    private val ratesUseCase: GetRatesUseCase,
    private val ratesApiUseCase: GetRatesApiUseCase,
    private val ratesWithScheduleUseCase: GetRatesWithScheduleUseCase,
    private val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase,
    private val validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase,
    private val releaseBookingUseCase: ReleaseBookingUseCase,
    private val prescriptionIdsUseCase: GetPrescriptionIdsUseCaseCoroutine,
    private val epharmacyUseCase: EPharmacyPrepareProductsGroupUseCase,
    private val updateDynamicDataPassingUseCase: UpdateDynamicDataPassingUseCase,
    private val getPaymentFeeCheckoutUseCase: GetPaymentFeeCheckoutUseCase,
    private val checkoutGqlUseCase: CheckoutUseCase,
    private val saveAddOnProductUseCase: SaveAddOnStateUseCase,
    private val shipmentDataConverter: ShipmentDataConverter,
    private val shippingCourierConverter: ShippingCourierConverter,
    private val stateConverter: RatesResponseStateConverter,
    private val shipmentDataRequestConverter: ShipmentDataRequestConverter,
    private val analyticsActionListener: AnalyticsActionListener,
    private val mTrackerShipment: CheckoutAnalyticsCourierSelection,
    private val mTrackerPurchaseProtection: CheckoutAnalyticsPurchaseProtection,
    private val userSessionInterface: UserSessionInterface,
    private val gson: Gson,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    private var view: ShipmentFragment? = null

    var shipmentUpsellModel = ShipmentUpsellModel()
        private set

    var shipmentNewUpsellModel = ShipmentNewUpsellModel()

    var shipmentCartItemModelList: List<ShipmentCartItem> = emptyList()

    var shipmentTickerErrorModel = ShipmentTickerErrorModel()
        private set

    val tickerAnnouncementHolderData: CheckoutMutableLiveData<TickerAnnouncementHolderData> =
        CheckoutMutableLiveData(TickerAnnouncementHolderData())

    var recipientAddressModel: RecipientAddressModel = RecipientAddressModel()

    val shipmentCostModel: CheckoutMutableLiveData<ShipmentCostModel> =
        CheckoutMutableLiveData(ShipmentCostModel())

    val egoldAttributeModel: CheckoutMutableLiveData<EgoldAttributeModel?> =
        CheckoutMutableLiveData(null)

    var shipmentDonationModel: ShipmentDonationModel? = null

    var listShipmentCrossSellModel: ArrayList<ShipmentCrossSellModel> = ArrayList()

    val shipmentButtonPayment: CheckoutMutableLiveData<ShipmentButtonPaymentModel> =
        CheckoutMutableLiveData(ShipmentButtonPaymentModel())

    var codData: CodModel? = null
        private set

    private var campaignTimer: CampaignTimerUi? = null

    var validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel? = null

    var lastValidateUseRequest: ValidateUsePromoRequest? = null
        private set

    var bboPromoCodes = ArrayList<String>()
        private set

    var couponStateChanged = false

    private var shippingCourierViewModelsState: MutableMap<Int, List<ShippingCourierUiModel>> =
        HashMap()

    private var isPurchaseProtectionPage = false

    var isShowOnboarding = false
        private set

    var isIneligiblePromoDialogEnabled = false
        private set

    internal var isBoUnstackEnabled = false

    var cartDataForRates = ""
        private set

    val lastApplyData: CheckoutMutableLiveData<LastApplyUiModel> =
        CheckoutMutableLiveData(LastApplyUiModel())

    var uploadPrescriptionUiModel: UploadPrescriptionUiModel = UploadPrescriptionUiModel()
        private set

    private val ratesQueue: Queue<ShipmentGetCourierHolderData> = LinkedList()

    private val promoQueue: LinkedList<ShipmentValidatePromoHolderData> = LinkedList()

    private var scheduleDeliveryMapData: MutableMap<String, ShipmentScheduleDeliveryMapData> =
        HashMap()

    private var isUsingDdp = false

    private var dynamicDataParam: DynamicDataPassingParamRequest = DynamicDataPassingParamRequest()

    private var shipmentPlatformFeeData: ShipmentPlatformFeeData = ShipmentPlatformFeeData()

    var dynamicData = ""

    var isOneClickShipment: Boolean = false

    var checkoutLeasingId: String = "0"

    var isTradeIn: Boolean = false

    var isAnyProductHasAddOnsProduct: Boolean = false

    // add ons product
    // list summary add on - ready to render
    var listSummaryAddOnModel: List<ShipmentAddOnSummaryModel> = emptyList()

    // list summary default
    private var summariesAddOnUiModel: HashMap<Int, String> = hashMapOf()

    val isTradeInByDropOff: Boolean
        get() {
            val recipientAddressModel = this.recipientAddressModel
            return recipientAddressModel.selectedTabIndex == 1
        }

    var deviceId: String = ""

    val cornerId: String?
        get() = recipientAddressModel.cornerId

    var isPlusSelected: Boolean = false

    private var isValidatingFinalPromo: Boolean = false

    // region view
    fun attachView(view: ShipmentFragment) {
        this.view = view
    }

    fun detachView() {
        viewModelScope.coroutineContext.cancelChildren()
        epharmacyUseCase.cancelJobs()
        updateDynamicDataPassingUseCase.cancelJobs()
        getPaymentFeeCheckoutUseCase.cancelJobs()
        ratesQueue.clear()
        promoQueue.clear()
        view = null
    }
    // endregion

    // region cost
    private fun hasSetAllCourier(): Boolean {
        for (itemData in shipmentCartItemModelList) {
            if (itemData is ShipmentCartItemModel && itemData.selectedShipmentDetailData == null && !itemData.isError) {
                return false
            }
        }
        return true
    }

    fun updateShipmentCostModel() {
        var totalWeight = 0.0
        var totalPrice: Double
        var additionalFee = 0.0
        var totalItemPrice = 0.0
        var tradeInPrice = 0.0
        var totalItem = 0
        var totalPurchaseProtectionPrice = 0.0
        var totalPurchaseProtectionItem = 0
        var shippingFee = 0.0
        var insuranceFee = 0.0
        var totalBookingFee = 0
        var hasAddOnSelected = false
        var totalAddOnGiftingPrice = 0.0
        var totalAddOnProductServicePrice = 0.0
        var qtyAddOn: Int
        var totalPriceAddOn: Double
        val countMapSummaries = hashMapOf<Int, Pair<Double, Int>>()
        val listShipmentAddOnSummary: ArrayList<ShipmentAddOnSummaryModel> = arrayListOf()
        for (shipmentData in shipmentCartItemModelList) {
            if (shipmentData is ShipmentCartItemModel) {
                val cartItemModels = shipmentData.cartItemModels
                for (cartItem in cartItemModels) {
                    if (!cartItem.isError) {
                        totalWeight += cartItem.weight * cartItem.quantity
                        totalItem += cartItem.quantity
                        if (cartItem.isProtectionOptIn) {
                            totalPurchaseProtectionItem += cartItem.quantity
                            totalPurchaseProtectionPrice += cartItem.protectionPrice
                        }
                        if (cartItem.isValidTradeIn) {
                            tradeInPrice += cartItem.oldDevicePrice.toDouble()
                        }
                        if (cartItem.isBundlingItem) {
                            if (cartItem.bundlingItemPosition == ShipmentMapper.BUNDLING_ITEM_HEADER) {
                                totalItemPrice += cartItem.bundleQuantity * cartItem.bundlePrice
                            }
                        } else {
                            totalItemPrice += cartItem.quantity * cartItem.price
                        }
                        if (cartItem.addOnGiftingProductLevelModel.status == 1) {
                            if (cartItem.addOnGiftingProductLevelModel.addOnsDataItemModelList.isNotEmpty()) {
                                for (addOnsData in cartItem.addOnGiftingProductLevelModel.addOnsDataItemModelList) {
                                    totalAddOnGiftingPrice += addOnsData.addOnPrice
                                    hasAddOnSelected = true
                                }
                            }
                        }
                        if (cartItem.addOnProduct.listAddOnProductData.isNotEmpty()) {
                            for (addOnProductService in cartItem.addOnProduct.listAddOnProductData) {
                                if (addOnProductService.status == ADD_ON_PRODUCT_STATUS_CHECK || addOnProductService.status == ADD_ON_PRODUCT_STATUS_MANDATORY) {
                                    totalAddOnProductServicePrice += (addOnProductService.price * cartItem.quantity)
                                    qtyAddOn =
                                        if (countMapSummaries.containsKey(addOnProductService.type)) {
                                            countMapSummaries[addOnProductService.type]?.second?.plus(
                                                cartItem.quantity
                                            ) ?: cartItem.quantity
                                        } else {
                                            cartItem.quantity
                                        }

                                    val addOnPrice = cartItem.quantity * addOnProductService.price
                                    totalPriceAddOn =
                                        if (countMapSummaries.containsKey(addOnProductService.type)) {
                                            countMapSummaries[addOnProductService.type]?.first?.plus(
                                                addOnPrice
                                            ) ?: addOnPrice
                                        } else {
                                            addOnPrice
                                        }
                                    countMapSummaries[addOnProductService.type] =
                                        totalPriceAddOn to qtyAddOn
                                }
                            }
                        }
                    }
                }
                if (shipmentData.selectedShipmentDetailData != null && !shipmentData.isError) {
                    val useInsurance = shipmentData.selectedShipmentDetailData!!.useInsurance
                    val isTradeInPickup = isTradeInByDropOff
                    if (isTradeInPickup) {
                        if (shipmentData.selectedShipmentDetailData!!.selectedCourierTradeInDropOff != null) {
                            shippingFee += shipmentData.selectedShipmentDetailData!!
                                .selectedCourierTradeInDropOff!!.shipperPrice.toDouble()
                            if (useInsurance != null && useInsurance) {
                                insuranceFee += shipmentData.selectedShipmentDetailData!!
                                    .selectedCourierTradeInDropOff!!.insurancePrice.toDouble()
                            }
                            additionalFee += shipmentData.selectedShipmentDetailData!!
                                .selectedCourierTradeInDropOff!!.additionalPrice.toDouble()
                        } else {
                            shippingFee = 0.0
                            insuranceFee = 0.0
                            additionalFee = 0.0
                        }
                    } else if (shipmentData.selectedShipmentDetailData!!.selectedCourier != null) {
                        shippingFee += shipmentData.selectedShipmentDetailData!!
                            .selectedCourier!!.selectedShipper.shipperPrice.toDouble()
                        if (useInsurance != null && useInsurance) {
                            insuranceFee += shipmentData.selectedShipmentDetailData!!
                                .selectedCourier!!.selectedShipper.insurancePrice.toDouble()
                        }
                        additionalFee += shipmentData.selectedShipmentDetailData!!
                            .selectedCourier!!.additionalPrice.toDouble()
                    }
                }
                if (shipmentData.isLeasingProduct) {
                    totalBookingFee += shipmentData.bookingFee
                }
                val addOnsDataModel = shipmentData.addOnsOrderLevelModel
                if (addOnsDataModel.status == 1 && addOnsDataModel.addOnsDataItemModelList.isNotEmpty()) {
                    for ((addOnPrice) in addOnsDataModel.addOnsDataItemModelList) {
                        totalAddOnGiftingPrice += addOnPrice
                        hasAddOnSelected = true
                    }
                }
            }
        }
        val shipmentCost = shipmentCostModel.value
        var finalShippingFee = shippingFee - shipmentCost.shippingDiscountAmount
        if (finalShippingFee < 0) {
            finalShippingFee = 0.0
        }
        totalPrice =
            totalItemPrice + finalShippingFee + insuranceFee + totalPurchaseProtectionPrice + additionalFee + totalBookingFee -
            shipmentCost.productDiscountAmount - tradeInPrice + totalAddOnGiftingPrice + totalAddOnProductServicePrice
        shipmentCost.totalWeight = totalWeight
        shipmentCost.additionalFee = additionalFee
        shipmentCost.totalItemPrice = totalItemPrice
        shipmentCost.totalItem = totalItem
        shipmentCost.shippingFee = shippingFee
        shipmentCost.insuranceFee = insuranceFee
        shipmentCost.totalPurchaseProtectionItem = totalPurchaseProtectionItem
        shipmentCost.purchaseProtectionFee = totalPurchaseProtectionPrice
        shipmentCost.tradeInPrice = tradeInPrice
        shipmentCost.totalAddOnPrice = totalAddOnGiftingPrice
        shipmentCost.hasAddOn = hasAddOnSelected
        if (shipmentDonationModel != null && shipmentDonationModel!!.isChecked) {
            shipmentCost.donation = shipmentDonationModel!!.donation.nominal.toDouble()
        } else {
            if (shipmentCost.donation > 0) {
                shipmentCost.donation = 0.0
            }
        }
        totalPrice += shipmentCost.donation
        shipmentCost.totalPrice = totalPrice
        var upsellCost: ShipmentCrossSellModel? = null
        if (shipmentNewUpsellModel.isSelected && shipmentNewUpsellModel.isShow) {
            val crossSellModel = CrossSellModel()
            crossSellModel.orderSummary =
                CrossSellOrderSummaryModel(shipmentNewUpsellModel.summaryInfo, "")
            crossSellModel.price = shipmentNewUpsellModel.price.toDouble()
            upsellCost = ShipmentCrossSellModel(crossSellModel, true, true, -1)
        }
        val listCheckedCrossModel = ArrayList<ShipmentCrossSellModel>()
        for (crossSellModel in listShipmentCrossSellModel) {
            if (crossSellModel.isChecked) {
                listCheckedCrossModel.add(crossSellModel)
                totalPrice += crossSellModel.crossSellModel.price
                shipmentCost.totalPrice = totalPrice
            }
        }
        if (upsellCost != null) {
            listCheckedCrossModel.add(upsellCost)
            totalPrice += upsellCost.crossSellModel.price
            shipmentCost.totalPrice = totalPrice
        }
        shipmentCost.listCrossSell = listCheckedCrossModel
        val egoldAttribute = egoldAttributeModel.value
        if (egoldAttribute != null && egoldAttribute.isEligible) {
            egoldAttributeModel.value = updateEmasCostModel(shipmentCost, egoldAttribute)
            if (egoldAttribute.isChecked) {
                totalPrice += egoldAttribute.buyEgoldValue.toDouble()
                shipmentCost.totalPrice = totalPrice
                shipmentCost.emasPrice = egoldAttribute.buyEgoldValue.toDouble()
            } else if (shipmentCost.emasPrice > 0) {
                shipmentCost.emasPrice = 0.0
            }
        }
        shipmentCost.bookingFee = totalBookingFee

        for (entry in countMapSummaries) {
            val addOnWording = summariesAddOnUiModel[entry.key]?.replace(
                CartConstant.QTY_ADDON_REPLACE,
                entry.value.second.toString()
            )
            val addOnPrice =
                CurrencyFormatUtil.convertPriceValueToIdrFormat(entry.value.first, false)
                    .removeDecimalSuffix()
            val summaryAddOn = ShipmentAddOnSummaryModel(
                wording = addOnWording ?: "",
                type = entry.key,
                qty = entry.value.second,
                priceLabel = addOnPrice,
                priceValue = entry.value.first.toLong()
            )
            listShipmentAddOnSummary.add(summaryAddOn)
        }

        listSummaryAddOnModel = listShipmentAddOnSummary
        shipmentCost.listAddOnSummary = listSummaryAddOnModel
        shipmentCostModel.value = shipmentCost
        updateCheckoutButtonData(shipmentCost)
    }

    private fun updateEmasCostModel(
        shipmentCost: ShipmentCostModel,
        egoldAttribute: EgoldAttributeModel
    ): EgoldAttributeModel {
        val totalPrice = shipmentCost.totalPrice.toLong()
        var valueTOCheck = 0
        val buyEgoldValue: Long
        if (egoldAttribute.isTiering) {
            egoldAttribute.egoldTieringModelArrayList.sortWith { o1, o2 -> (o1.minTotalAmount - o2.minTotalAmount).toInt() }
            var egoldTieringModel = EgoldTieringModel()
            for (data in egoldAttribute.egoldTieringModelArrayList) {
                if (totalPrice >= data.minTotalAmount) {
                    valueTOCheck = (totalPrice % data.basisAmount).toInt()
                    egoldTieringModel = data
                }
            }
            buyEgoldValue = calculateBuyEgoldValue(
                valueTOCheck,
                egoldTieringModel.minAmount.toInt(),
                egoldTieringModel.maxAmount.toInt(),
                egoldTieringModel.basisAmount
            )
        } else {
            valueTOCheck = (totalPrice % LAST_THREE_DIGIT_MODULUS).toInt()
            buyEgoldValue = calculateBuyEgoldValue(
                valueTOCheck,
                egoldAttribute.minEgoldRange,
                egoldAttribute.maxEgoldRange,
                LAST_THREE_DIGIT_MODULUS
            )
        }
        egoldAttribute.buyEgoldValue = buyEgoldValue
        return egoldAttribute
    }

    private fun calculateBuyEgoldValue(
        valueTOCheck: Int,
        minRange: Int,
        maxRange: Int,
        basisAmount: Long
    ): Long {
        if (basisAmount == 0L) {
            return 0
        }
        var buyEgoldValue: Long = 0
        for (i in minRange..maxRange) {
            if ((valueTOCheck + i) % basisAmount == 0L) {
                buyEgoldValue = i.toLong()
                break
            }
        }
        return buyEgoldValue
    }
    // endregion

    // region checkout button
    private fun updateCheckoutButtonData(shipmentCost: ShipmentCostModel) {
        var cartItemCounter = 0
        var cartItemErrorCounter = 0
        var hasLoadingItem = false
        for (shipmentCartItemModel in shipmentCartItemModelList) {
            if (shipmentCartItemModel is ShipmentCartItemModel) {
                if (shipmentCartItemModel.selectedShipmentDetailData != null) {
                    if ((shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier != null && !isTradeInByDropOff) || (shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff != null && isTradeInByDropOff)) {
                        if (!hasLoadingItem) {
                            hasLoadingItem = validateLoadingItem(shipmentCartItemModel)
                        }
                        cartItemCounter++
                    }
                }
                if (shipmentCartItemModel.isError) {
                    cartItemErrorCounter++
                }
            }
        }
        if (cartItemCounter > 0 && cartItemCounter <= shipmentCartItemModelList.size) {
            val priceTotal: Double =
                if (shipmentCost.totalPrice <= 0) 0.0 else shipmentCost.totalPrice
            val platformFee: Double =
                if (shipmentCost.dynamicPlatformFee.fee <= 0) 0.0 else shipmentCost.dynamicPlatformFee.fee
            val finalPrice = priceTotal + platformFee
            val priceTotalFormatted =
                CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    finalPrice,
                    false
                ).removeDecimalSuffix()
            updateShipmentButtonPaymentModel(
                enable = !hasLoadingItem,
                totalPrice = priceTotalFormatted
            )
        } else {
            updateShipmentButtonPaymentModel(
                enable = cartItemErrorCounter < shipmentCartItemModelList.size,
                totalPrice = "-"
            )
        }
    }

    private fun validateLoadingItem(shipmentCartItemModel: ShipmentCartItemModel): Boolean {
        return shipmentCartItemModel.isStateLoadingCourierState
    }

    fun updateCheckoutButtonData() {
        updateCheckoutButtonData(shipmentCostModel.value)
    }

    fun updateShipmentButtonPaymentModel(
        enable: Boolean? = null,
        totalPrice: String? = null,
        loading: Boolean? = null
    ) {
        val buttonPaymentModel = shipmentButtonPayment.value
        shipmentButtonPayment.value = buttonPaymentModel.copy(
            enable = enable ?: buttonPaymentModel.enable,
            totalPrice = totalPrice ?: buttonPaymentModel.totalPrice,
            loading = if (isValidatingFinalPromo) {
                true
            } else {
                loading ?: buttonPaymentModel.loading
            }
        )
    }
    // endregion

    // region analytics
    internal fun generateCheckoutAnalyticsDataLayer(
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
        for (shipmentCartItemModel in shipmentCartItemModelList) {
            if (shipmentCartItemModel is ShipmentCartItemModel) {
                for (cartItemModel in shipmentCartItemModel.cartItemModels) {
                    val shipmentDetailData = shipmentCartItemModel.selectedShipmentDetailData
                    var courierItemData: CourierItemData? = null
                    if (shipmentDetailData != null && (
                        shipmentDetailData.selectedCourier != null ||
                            shipmentDetailData.selectedCourierTradeInDropOff != null
                        )
                    ) {
                        if (isTradeInByDropOff && shipmentDetailData.selectedCourierTradeInDropOff != null) {
                            courierItemData = shipmentDetailData.selectedCourierTradeInDropOff
                        } else if (!isTradeInByDropOff && shipmentDetailData.selectedCourier != null) {
                            courierItemData = shipmentDetailData.selectedCourier
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
                        getFulfillmentStatus(shipmentCartItemModel.shopId)
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

    private fun getFulfillmentStatus(shopId: Long): Boolean {
        for (cartItemModel in shipmentCartItemModelList) {
            if (cartItemModel is ShipmentCartItemModel && cartItemModel.shopId == shopId) {
                return cartItemModel.isFulfillment
            }
        }
        return false
    }

    private fun getPromoFlag(step: String): Boolean {
        return if (step == EnhancedECommerceActionField.STEP_2) {
            lastApplyData.value.additionalInfo.pomlAutoApplied
        } else {
            validateUsePromoRevampUiModel?.promoUiModel?.additionalInfoUiModel?.pomlAutoApplied
                ?: false
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
        analyticsActionListener.sendEnhancedEcommerceAnalyticsCheckout(
            eeDataLayer,
            tradeInCustomDimension,
            transactionId,
            userSessionInterface.userId,
            getPromoFlag(step),
            eventCategory,
            eventAction,
            eventLabel
        )
    }
    // endregion

    // region load SAF
    fun processInitialLoadCheckoutPage(
        isReloadData: Boolean,
        skipUpdateOnboardingState: Boolean,
        isReloadAfterPriceChangeHinger: Boolean
    ) {
        view?.let { v ->
            if (isReloadData) {
                v.setShipmentNewUpsellLoading(true)
                v.setHasRunningApiCall(true)
                v.showLoading()
            } else {
                v.showInitialLoading()
            }
        }
        viewModelScope.launch(dispatchers.immediate) {
            try {
                val cartShipmentAddressFormData = getShipmentAddressFormV4UseCase(
                    ShipmentAddressFormRequest(
                        isOneClickShipment,
                        isTradeIn,
                        skipUpdateOnboardingState,
                        cornerId,
                        deviceId,
                        checkoutLeasingId,
                        isPlusSelected
                    )
                )
                if (view != null) {
                    view!!.stopEmbraceTrace()
                    if (isReloadData) {
                        view!!.setShipmentNewUpsellLoading(false)
                        view!!.setHasRunningApiCall(false)
                        view!!.resetPromoBenefit()
                        view!!.clearTotalBenefitPromoStacking()
                        view!!.hideLoading()
                    } else {
                        view!!.hideInitialLoading()
                    }
                    validateShipmentAddressFormData(
                        cartShipmentAddressFormData,
                        isReloadData,
                        isReloadAfterPriceChangeHinger,
                        isOneClickShipment
                    )
                    view!!.stopTrace()
                }
            } catch (throwable: Throwable) {
                Timber.d(throwable)
                if (view != null) {
                    view!!.stopEmbraceTrace()
                    if (isReloadData) {
                        view!!.setShipmentNewUpsellLoading(false)
                        view!!.setHasRunningApiCall(false)
                        view!!.hideLoading()
                    } else {
                        view!!.hideInitialLoading()
                    }
                    var errorMessage = throwable.message
                    if (throwable !is CartResponseErrorException && throwable !is AkamaiErrorException) {
                        errorMessage = getErrorMessage(view!!.activity, throwable)
                    }
                    view!!.showToastError(errorMessage)
                    view!!.stopTrace()
                    view!!.logOnErrorLoadCheckoutPage(throwable)
                }
            }
        }
    }

    private fun validateShipmentAddressFormData(
        cartShipmentAddressFormData: CartShipmentAddressFormData,
        isReloadData: Boolean,
        isReloadAfterPriceChangeHigher: Boolean,
        isOneClickShipment: Boolean
    ) {
        if (cartShipmentAddressFormData.isError) {
            if (cartShipmentAddressFormData.isOpenPrerequisiteSite) {
                view?.onCacheExpired(cartShipmentAddressFormData.errorMessage)
            } else {
                view?.showToastError(cartShipmentAddressFormData.errorMessage)
                view?.logOnErrorLoadCheckoutPage(
                    MessageErrorException(
                        cartShipmentAddressFormData.errorMessage
                    )
                )
            }
        } else {
            val groupAddressList = cartShipmentAddressFormData.groupAddress
            if (groupAddressList.isNotEmpty()) {
                val userAddress = groupAddressList[0].userAddress
                validateRenderCheckoutPage(
                    cartShipmentAddressFormData,
                    userAddress,
                    isReloadData,
                    isReloadAfterPriceChangeHigher,
                    isOneClickShipment
                )
            } else {
                validateRenderCheckoutPage(
                    cartShipmentAddressFormData,
                    null,
                    isReloadData,
                    isReloadAfterPriceChangeHigher,
                    isOneClickShipment
                )
            }
        }
    }

    private fun validateRenderCheckoutPage(
        cartShipmentAddressFormData: CartShipmentAddressFormData,
        userAddress: UserAddress?,
        isReloadData: Boolean,
        isReloadAfterPriceChangeHigher: Boolean,
        isOneClickShipment: Boolean
    ) {
        if (cartShipmentAddressFormData.errorCode == CartShipmentAddressFormData.ERROR_CODE_TO_OPEN_ADD_NEW_ADDRESS) {
            checkoutPageNoAddress(cartShipmentAddressFormData)
        } else if (cartShipmentAddressFormData.errorCode == CartShipmentAddressFormData.ERROR_CODE_TO_OPEN_ADDRESS_LIST) {
            view?.renderCheckoutPageNoMatchedAddress(
                userAddress?.state ?: 0
            )
        } else if (cartShipmentAddressFormData.errorCode == CartShipmentAddressFormData.NO_ERROR) {
            if (userAddress == null) {
                view?.onShipmentAddressFormEmpty()
            } else {
                view?.updateLocalCacheAddressData(userAddress)
                initializePresenterData(cartShipmentAddressFormData)
                setCurrentDynamicDataParamFromSAF(cartShipmentAddressFormData, isOneClickShipment)
                view?.renderCheckoutPage(
                    !isReloadData,
                    isReloadAfterPriceChangeHigher
                )
                if (cartShipmentAddressFormData.popUpMessage.isNotEmpty()) {
                    view?.showToastNormal(cartShipmentAddressFormData.popUpMessage)
                }
                val popUpData = cartShipmentAddressFormData.popup
                if (popUpData.title.isNotEmpty() && popUpData.description.isNotEmpty()) {
                    view?.showPopUp(popUpData)
                }
            }
        }
        isUsingDdp = cartShipmentAddressFormData.isUsingDdp
        dynamicData = cartShipmentAddressFormData.dynamicData
        shipmentPlatformFeeData = cartShipmentAddressFormData.shipmentPlatformFee
        listSummaryAddOnModel =
            ShipmentAddOnProductServiceMapper.mapSummaryAddOns(cartShipmentAddressFormData)
        isAnyProductHasAddOnsProduct = validateNeedSaveAddons(cartShipmentAddressFormData)
    }

    private fun validateNeedSaveAddons(cartShipmentAddressFormData: CartShipmentAddressFormData): Boolean {
        var isAnyProductHasAddonsProduct = false
        cartShipmentAddressFormData.groupAddress.forEach { groupAddress ->
            groupAddress.groupShop.forEach { groupShop ->
                groupShop.groupShopData.forEach { groupShopV2 ->
                    groupShopV2.products.forEach { product ->
                        if (product.addOnProduct.listAddOnProductData.isNotEmpty()) {
                            isAnyProductHasAddonsProduct = true
                        }
                    }
                }
            }
        }
        return isAnyProductHasAddonsProduct
    }

    internal fun initializePresenterData(cartShipmentAddressFormData: CartShipmentAddressFormData) {
        setLastValidateUseRequest(null)
        validateUsePromoRevampUiModel = null
        ratesQueue.clear()
        promoQueue.clear()
        shipmentTickerErrorModel = ShipmentTickerErrorModel(cartShipmentAddressFormData.errorTicker)
        val tickerData = cartShipmentAddressFormData.tickerData
        if (tickerData != null) {
            tickerAnnouncementHolderData.value = TickerAnnouncementHolderData(
                tickerData.id,
                tickerData.title,
                tickerData.message
            )
            analyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
                tickerData.id
            )
        } else {
            tickerAnnouncementHolderData.value = TickerAnnouncementHolderData()
        }
        val newAddress = shipmentDataConverter
            .getRecipientAddressModel(cartShipmentAddressFormData)
        recipientAddressModel = newAddress
        shipmentUpsellModel =
            shipmentDataConverter.getShipmentUpsellModel(cartShipmentAddressFormData.upsell)
        shipmentNewUpsellModel =
            shipmentDataConverter.getShipmentNewUpsellModel(cartShipmentAddressFormData.newUpsell)
        if (cartShipmentAddressFormData.donation != null) {
            val shipmentDonationModel =
                shipmentDataConverter.getShipmentDonationModel(cartShipmentAddressFormData)
            shipmentDonationModel!!.isEnabled = !shipmentTickerErrorModel.isError
            this.shipmentDonationModel = shipmentDonationModel
        } else {
            shipmentDonationModel = null
        }
        listShipmentCrossSellModel =
            shipmentDataConverter.getListShipmentCrossSellModel(cartShipmentAddressFormData)
        lastApplyData.value = cartShipmentAddressFormData.lastApplyData
        isBoUnstackEnabled =
            cartShipmentAddressFormData.lastApplyData.additionalInfo.bebasOngkirInfo.isBoUnstackEnabled
        shipmentCartItemModelList = shipmentDataConverter.getShipmentItems(
            cartShipmentAddressFormData,
            newAddress.locationDataModel != null,
            userSessionInterface.name
        )
        codData = cartShipmentAddressFormData.cod
        campaignTimer = cartShipmentAddressFormData.campaignTimerUi
        val ppImpressionData: List<String> =
            cartShipmentAddressFormData.getAvailablePurchaseProtection
        if (ppImpressionData.isNotEmpty()) {
            isPurchaseProtectionPage = true
            mTrackerPurchaseProtection.eventImpressionOfProduct(
                userSessionInterface.userId,
                ppImpressionData
            )
        } else {
            isPurchaseProtectionPage = false
        }
        val egoldAttributes = cartShipmentAddressFormData.egoldAttributes
        if (egoldAttributes != null) {
            egoldAttributes.isEnabled = !shipmentTickerErrorModel.isError
        }
        egoldAttributeModel.value = egoldAttributes
        isShowOnboarding = cartShipmentAddressFormData.isShowOnboarding
        isIneligiblePromoDialogEnabled = cartShipmentAddressFormData.isIneligiblePromoDialogEnabled
        setUploadPrescriptionData(
            UploadPrescriptionUiModel(
                showImageUpload = cartShipmentAddressFormData.epharmacyData.showImageUpload,
                uploadImageText = cartShipmentAddressFormData.epharmacyData.uploadText,
                leftIconUrl = cartShipmentAddressFormData.epharmacyData.leftIconUrl,
                checkoutId = cartShipmentAddressFormData.epharmacyData.checkoutId,
                frontEndValidation = cartShipmentAddressFormData.epharmacyData.frontEndValidation,
                consultationFlow = cartShipmentAddressFormData.epharmacyData.consultationFlow,
                rejectedWording = cartShipmentAddressFormData.epharmacyData.rejectedWording
            )
        )
        fetchPrescriptionIds(cartShipmentAddressFormData.epharmacyData)
        cartDataForRates = cartShipmentAddressFormData.cartData
        shippingCourierViewModelsState = hashMapOf()
        summariesAddOnUiModel =
            ShipmentAddOnProductServiceMapper.getShoppingSummaryAddOns(cartShipmentAddressFormData.listSummaryAddons)
    }

    internal fun setPurchaseProtection(isPurchaseProtectionPage: Boolean) {
        this.isPurchaseProtectionPage = isPurchaseProtectionPage
    }
    // endregion

    // region add new address
    private fun checkoutPageNoAddress(cartShipmentAddressFormData: CartShipmentAddressFormData) {
        view?.renderCheckoutPageNoAddress(cartShipmentAddressFormData)
    }
    // endregion

    // region checkout
    fun processCheckout() {
        val checkoutRequest = generateCheckoutRequest()
        if (checkoutRequest.data.isNotEmpty() && checkoutRequest.data.first().groupOrders.isNotEmpty()) {
            // Get additional param for trade in analytics
            var deviceModel = ""
            var devicePrice = 0L
            var diagnosticId = ""
            if (shipmentCartItemModelList.isNotEmpty()) {
                val cartItemModels =
                    (shipmentCartItemModelList.first { it is ShipmentCartItemModel } as ShipmentCartItemModel).cartItemModels
                if (cartItemModels.isNotEmpty()) {
                    val cartItemModel = cartItemModels[0]
                    deviceModel = cartItemModel.deviceModel
                    devicePrice = cartItemModel.oldDevicePrice
                    diagnosticId = cartItemModel.diagnosticId
                }
            }
            val params = generateCheckoutParams(
                isOneClickShipment,
                isTradeIn,
                isTradeInByDropOff,
                deviceId,
                checkoutRequest,
                dynamicData
            )
            viewModelScope.launch(dispatchers.immediate) {
                try {
                    val checkoutData = checkoutGqlUseCase(params)
                    view?.let { v ->
                        v.setHasRunningApiCall(false)
                        if (!checkoutData.isError) {
                            v.triggerSendEnhancedEcommerceCheckoutAnalyticAfterCheckoutSuccess(
                                checkoutData.transactionId,
                                deviceModel,
                                devicePrice,
                                diagnosticId
                            )
                            if (isPurchaseProtectionPage) {
                                mTrackerPurchaseProtection.eventClickOnBuy(
                                    userSessionInterface.userId,
                                    checkoutRequest.protectionAnalyticsData
                                )
                            }
                            var isCrossSellChecked = false
                            for (shipmentCrossSellModel in listShipmentCrossSellModel) {
                                if (shipmentCrossSellModel.isChecked) isCrossSellChecked = true
                            }
                            if (isCrossSellChecked) triggerCrossSellClickPilihPembayaran()
                            v.renderCheckoutCartSuccess(checkoutData)
                        } else if (checkoutData.priceValidationData.isUpdated) {
                            v.hideLoading()
                            v.renderCheckoutPriceUpdated(checkoutData.priceValidationData)
                        } else if (checkoutData.prompt.eligible) {
                            v.hideLoading()
                            v.renderPrompt(checkoutData.prompt)
                        } else {
                            analyticsActionListener.sendAnalyticsChoosePaymentMethodFailed(
                                checkoutData.errorMessage
                            )
                            v.hideLoading()
                            if (checkoutData.errorMessage.isNotEmpty()) {
                                v.renderCheckoutCartError(checkoutData.errorMessage)
                                v.logOnErrorCheckout(
                                    MessageErrorException(checkoutData.errorMessage),
                                    checkoutRequest.toString()
                                )
                            } else {
                                val defaultErrorMessage =
                                    v.getStringResource(com.tokopedia.abstraction.R.string.default_request_error_unknown)
                                v.renderCheckoutCartError(defaultErrorMessage)
                                v.logOnErrorCheckout(
                                    MessageErrorException(defaultErrorMessage),
                                    checkoutRequest.toString()
                                )
                            }
                            processInitialLoadCheckoutPage(
                                isReloadData = true,
                                skipUpdateOnboardingState = true,
                                isReloadAfterPriceChangeHinger = false
                            )
                        }
                    }
                } catch (e: Throwable) {
                    view?.hideLoading()
                    Timber.d(e)
                    var errorMessage = e.message
                    if (!(e is CartResponseErrorException || e is AkamaiErrorException)) {
                        errorMessage = getErrorMessage(view?.activity, e)
                    }
                    analyticsActionListener.sendAnalyticsChoosePaymentMethodFailed(errorMessage)
                    view?.setHasRunningApiCall(false)
                    view?.showToastError(errorMessage)
                    processInitialLoadCheckoutPage(
                        isReloadData = true,
                        skipUpdateOnboardingState = true,
                        isReloadAfterPriceChangeHinger = false
                    )
                    view?.logOnErrorCheckout(e, checkoutRequest.toString())
                }
            }
        } else {
            view?.let { v ->
                v.hideLoading()
                v.setHasRunningApiCall(false)
                v.showToastError(v.getStringResource(R.string.message_error_checkout_empty))
            }
        }
    }

    fun generateCheckoutRequest(): Carts {
        // Set promo merchant request data
        val data = removeErrorShopProduct()
        if (validateUsePromoRevampUiModel != null) {
            setCheckoutRequestPromoData(data)
        }
        var cornerData: TokopediaCorner? = null
        if (recipientAddressModel.isCornerAddress) {
            cornerData = TokopediaCorner(
                true,
                recipientAddressModel.userCornerId,
                recipientAddressModel.cornerId
                    .toLongOrZero()
            )
        }
        val egoldData = Egold()
        val egoldAttribute = egoldAttributeModel.value
        if (egoldAttribute != null && egoldAttribute.isEligible) {
            egoldData.isEgold = egoldAttribute.isChecked
            egoldData.goldAmount = egoldAttribute.buyEgoldValue
        }
        val crossSellRequest = CrossSellRequest()
        val listCrossSellItemRequest = ArrayList<CrossSellItemRequestModel>()
        if (listShipmentCrossSellModel.isNotEmpty()) {
            val crossSellItemRequestModel = CrossSellItemRequestModel()
            for (shipmentCrossSellModel in listShipmentCrossSellModel) {
                if (shipmentCrossSellModel.isChecked) {
                    crossSellItemRequestModel.id =
                        shipmentCrossSellModel.crossSellModel.id
                            .toLongOrZero()
                    crossSellItemRequestModel.price = shipmentCrossSellModel.crossSellModel.price
                    crossSellItemRequestModel.additionalVerticalId =
                        shipmentCrossSellModel.crossSellModel.additionalVerticalId
                            .toLongOrZero()
                    crossSellItemRequestModel.transactionType =
                        shipmentCrossSellModel.crossSellModel.transactionType
                    listCrossSellItemRequest.add(crossSellItemRequestModel)
                }
            }
        }
        if (shipmentNewUpsellModel.isSelected && shipmentNewUpsellModel.isShow) {
            val crossSellItemRequestModel = CrossSellItemRequestModel()
            crossSellItemRequestModel.id = shipmentNewUpsellModel.id
                .toLongOrZero()
            crossSellItemRequestModel.price = shipmentNewUpsellModel.price.toDouble()
            crossSellItemRequestModel.additionalVerticalId =
                shipmentNewUpsellModel.additionalVerticalId
                    .toLongOrZero()
            crossSellItemRequestModel.transactionType = shipmentNewUpsellModel.transactionType
            listCrossSellItemRequest.add(crossSellItemRequestModel)
        }
        crossSellRequest.listItem = listCrossSellItemRequest

        val globalPromos = mutableListOf<Promo>()
        var hasPromoStackingData = false
        // Set promo global request data
        if (validateUsePromoRevampUiModel != null) {
            // Clear data first

            // Then set the data promo global
            val promoModel = validateUsePromoRevampUiModel!!.promoUiModel
            if (promoModel.codes.isNotEmpty() && promoModel.messageUiModel.state != "red") {
                for (promoCode in promoModel.codes) {
                    globalPromos.add(
                        Promo(
                            promoCode,
                            Promo.TYPE_GLOBAL
                        )
                    )
                }
            }
            hasPromoStackingData = true
        }
        return Carts().apply {
            promos = globalPromos
            isDonation = if (shipmentDonationModel?.isChecked == true) 1 else 0
            egold = egoldData
            this.data = data
            tokopediaCorner = cornerData
            hasPromoStacking = hasPromoStackingData
            if (checkoutLeasingId.isNotEmpty()) {
                leasingId = checkoutLeasingId.toLongOrZero()
            }
            featureType = setCheckoutFeatureTypeData(data)
            crossSell = crossSellRequest
        }
    }

    private fun setCheckoutFeatureTypeData(dataCheckoutRequestList: List<Data>): Int {
        var hasTokoNowProduct = false
        loopall@ for (dataCheckoutRequest in dataCheckoutRequestList) {
            for (groupOrder in dataCheckoutRequest.groupOrders) {
                for (shopOrder in groupOrder.shopOrders) {
                    if (shopOrder.isTokoNow) {
                        hasTokoNowProduct = true
                        break@loopall
                    }
                }
            }
        }
        return if (hasTokoNowProduct) FEATURE_TYPE_TOKONOW_PRODUCT else FEATURE_TYPE_REGULAR_PRODUCT
    }

    private fun setCheckoutRequestPromoData(data: List<Data>) {
        for (dataCheckoutRequest in data) {
            for (groupOrder in dataCheckoutRequest.groupOrders) {
                for (shopOrder in groupOrder.shopOrders) {
                    // reset promo to prevent duplicate bo promo in owoc order
                    shopOrder.promos = emptyList()
                    for (voucherOrder in validateUsePromoRevampUiModel!!.promoUiModel.voucherOrderUiModels) {
                        if (groupOrder.cartStringGroup == voucherOrder.cartStringGroup && shopOrder.cartStringOrder == voucherOrder.uniqueId) {
                            if (voucherOrder.code.isNotEmpty() && voucherOrder.type.isNotEmpty()) {
                                if (!hasInsertPromo(shopOrder.promos, voucherOrder.code)) {
                                    val promoRequest = Promo()
                                    promoRequest.code = voucherOrder.code
                                    promoRequest.type = voucherOrder.type
                                    shopOrder.promos =
                                        shopOrder.promos.toMutableList().apply { add(promoRequest) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun hasInsertPromo(promoRequests: List<Promo>, promoCode: String): Boolean {
        for (promoRequest in promoRequests) {
            if (promoRequest.code == promoCode) {
                return true
            }
        }
        return false
    }

    internal fun generateCheckoutParams(
        isOneClickShipment: Boolean,
        isTradeIn: Boolean,
        isTradeInDropOff: Boolean,
        deviceId: String,
        carts: Carts,
        dynamicData: String
    ): CheckoutRequest {
        var publicKey = ""
        var fingerprintSupport = false
        if (getEnableFingerprintPayment(view?.activity)) {
            val fpk = getFingerprintPublicKey(
                view?.activity
            )
            if (fpk != null) {
                publicKey = getPublicKey(fpk)!!
                fingerprintSupport = true
            }
        }
        return CheckoutRequest(
            carts,
            isOneClickShipment.toString(),
            dynamicData,
            isTradeIn,
            isTradeIn && isTradeInDropOff,
            if (isTradeIn) deviceId else "",
            0,
            true,
            true,
            false,
            fingerprintSupport.toString(),
            publicKey
        )
    }

    private fun removeErrorShopProduct(): List<Data> {
        val newShipmentCartItemModelList: MutableList<ShipmentCartItemModel> = ArrayList()
        for (shipmentCartItemModel in shipmentCartItemModelList) {
            if (shipmentCartItemModel is ShipmentCartItemModel) {
                if (shipmentCartItemModel.isAllItemError) {
                    continue
                }
                val validCartItemModels =
                    shipmentCartItemModel.cartItemModels.filter { !it.isError }
                if (validCartItemModels.isEmpty()) {
                    continue
                }
                newShipmentCartItemModelList.add(shipmentCartItemModel.copy(cartItemModels = validCartItemModels))
            }
        }
        return shipmentDataRequestConverter.createCheckoutRequestDataNew(
            newShipmentCartItemModelList,
            recipientAddressModel,
            isTradeInByDropOff
        )
    }

    private fun triggerCrossSellClickPilihPembayaran() {
        val shipmentCrossSellModelList: List<ShipmentCrossSellModel> = listShipmentCrossSellModel
        var eventLabel = ""
        var digitalProductName = ""
        if (shipmentCrossSellModelList.isNotEmpty()) {
            for (i in shipmentCrossSellModelList.indices) {
                val crossSellModel = shipmentCrossSellModelList[i].crossSellModel
                val digitalCategoryName = crossSellModel.orderSummary.title
                eventLabel = "$digitalCategoryName - ${crossSellModel.id}"
                digitalProductName = crossSellModel.info.title
            }
        }
        val productList: MutableList<Any> = ArrayList()
        for (j in shipmentCartItemModelList.indices) {
            if (shipmentCartItemModelList[j] is ShipmentCartItemModel) {
                for (cartItemModel in (shipmentCartItemModelList[j] as ShipmentCartItemModel).cartItemModels) {
                    productList.add(
                        DataLayer.mapOf(
                            ConstantTransactionAnalytics.Key.BRAND,
                            "",
                            ConstantTransactionAnalytics.Key.CATEGORY,
                            cartItemModel.analyticsProductCheckoutData.productCategoryId,
                            ConstantTransactionAnalytics.Key.ID,
                            "",
                            ConstantTransactionAnalytics.Key.NAME,
                            cartItemModel.analyticsProductCheckoutData.productName,
                            ConstantTransactionAnalytics.Key.PRICE,
                            cartItemModel.analyticsProductCheckoutData.productPrice,
                            ConstantTransactionAnalytics.Key.QUANTITY,
                            cartItemModel.analyticsProductCheckoutData.productQuantity,
                            ConstantTransactionAnalytics.Key.SHOP_ID,
                            cartItemModel.analyticsProductCheckoutData.productShopId,
                            ConstantTransactionAnalytics.Key.SHOP_NAME,
                            cartItemModel.analyticsProductCheckoutData.productShopName,
                            ConstantTransactionAnalytics.Key.SHOP_TYPE,
                            cartItemModel.analyticsProductCheckoutData.productShopType,
                            ConstantTransactionAnalytics.Key.VARIANT,
                            digitalProductName
                        )
                    )
                }
            }
        }
        analyticsActionListener.sendEnhancedEcommerceAnalyticsCrossSellClickPilihPembayaran(
            eventLabel,
            userSessionInterface.userId,
            productList
        )
    }
    // endregion

    // region promo
    fun setPromoBenefit(summariesUiModels: List<SummariesItemUiModel>) {
        val shipmentCost = shipmentCostModel.value
        for (benefitSummary in summariesUiModels) {
            if (benefitSummary.type == SummariesUiModel.TYPE_DISCOUNT) {
                if (benefitSummary.details.isNotEmpty()) {
                    shipmentCost.isHasDiscountDetails = true
                    for (detail in benefitSummary.details) {
                        if (detail.type == SummariesUiModel.TYPE_SHIPPING_DISCOUNT) {
                            shipmentCost.shippingDiscountAmount = detail.amount
                            shipmentCost.shippingDiscountLabel = detail.description
                        } else if (detail.type == SummariesUiModel.TYPE_PRODUCT_DISCOUNT) {
                            shipmentCost.productDiscountAmount = detail.amount
                            shipmentCost.productDiscountLabel = detail.description
                        }
                    }
                } else if (hasSetAllCourier()) {
                    shipmentCost.isHasDiscountDetails = false
                    shipmentCost.discountAmount = benefitSummary.amount
                    shipmentCost.discountLabel = benefitSummary.description
                }
            } else if (benefitSummary.type == SummariesUiModel.TYPE_CASHBACK) {
                shipmentCost.cashbackAmount = benefitSummary.amount
                shipmentCost.cashbackLabel = benefitSummary.description
            }
        }
        shipmentCostModel.value = shipmentCost
    }

    fun resetPromoBenefit() {
        val shipmentCost = shipmentCostModel.value
        shipmentCost.isHasDiscountDetails = false
        shipmentCost.discountAmount = 0
        shipmentCost.discountLabel = ""
        shipmentCost.shippingDiscountAmount = 0
        shipmentCost.shippingDiscountLabel = ""
        shipmentCost.productDiscountAmount = 0
        shipmentCost.productDiscountLabel = ""
        shipmentCost.cashbackAmount = 0
        shipmentCost.cashbackLabel = ""
    }

    // This is for akamai error case
    private fun clearAllPromo() {
        val validateUsePromoRequest = lastValidateUseRequest ?: return
        var hasPromo = false
        val globalCodes = ArrayList<String>()
        for (code in validateUsePromoRequest.codes) {
            globalCodes.add(code)
            hasPromo = true
        }
        validateUsePromoRequest.codes = ArrayList()
        val cloneOrders = ArrayList<OrdersItem>()
        val clearOrders = ArrayList<ClearPromoOrder>()
        for (order in validateUsePromoRequest.orders) {
            clearOrders.add(
                ClearPromoOrder(
                    order.uniqueId,
                    order.boType,
                    order.codes,
                    order.shopId,
                    order.isPo,
                    order.poDuration.toString(),
                    order.warehouseId,
                    order.cartStringGroup
                )
            )
            if (order.codes.isNotEmpty()) {
                hasPromo = true
            }
            order.codes = ArrayList()
            order.boCode = ""
            cloneOrders.add(order)
        }
        validateUsePromoRequest.orders = cloneOrders
        val params = ClearPromoRequest(
            ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
            false,
            ClearPromoOrderData(globalCodes, clearOrders)
        )
        if (hasPromo) {
            viewModelScope.launch(dispatchers.immediate) {
                try {
                    clearCacheAutoApplyStackUseCase.setParams(params).executeOnBackground()
                } catch (t: Throwable) {
                    Timber.d(t)
                }
            }
        }
        lastValidateUseRequest = validateUsePromoRequest
        validateUsePromoRevampUiModel = null
    }

    fun checkPromoCheckoutFinalShipment(
        validateUsePromoRequest: ValidateUsePromoRequest,
        lastSelectedCourierOrderIndex: Int,
        cartString: String?
    ) {
        if (promoQueue.isEmpty()) {
            isValidatingFinalPromo = true
            couponStateChanged = true
            updateShipmentButtonPaymentModel(loading = true)
            viewModelScope.launch(dispatchers.immediate) {
                try {
                    if (promoQueue.isNotEmpty()) {
                        // ignore if there is still a promo in queue
                        return@launch
                    }
                    val validateUsePromoRevampUiModel = withContext(dispatchers.io) {
                        setValidateUseBoCodeInOneOrderOwoc(validateUsePromoRequest)
                        validateUsePromoRevampUseCase.setParam(validateUsePromoRequest)
                            .executeOnBackground()
                    }
                    if (promoQueue.isNotEmpty()) {
                        // ignore if there is still a promo in queue
                        return@launch
                    }
                    if (view != null) {
                        this@ShipmentViewModel.validateUsePromoRevampUiModel =
                            validateUsePromoRevampUiModel
                        couponStateChanged = true
                        showErrorValidateUseIfAny(validateUsePromoRevampUiModel)
                        validateBBO(validateUsePromoRevampUiModel)
                        updateTickerAnnouncementData(validateUsePromoRevampUiModel)
                        if (!validateUsePromoRevampUiModel.status.equals(
                                statusOK,
                                ignoreCase = true
                            ) || validateUsePromoRevampUiModel.errorCode != statusCode200
                        ) {
                            val message: String =
                                if (validateUsePromoRevampUiModel.message.isNotEmpty()) {
                                    validateUsePromoRevampUiModel.message[0]
                                } else {
                                    DEFAULT_ERROR_MESSAGE_VALIDATE_PROMO
                                }
                            view!!.renderErrorCheckPromoShipmentData(message)
                            view!!.resetPromoBenefit()
                            view!!.resetAllCourier()
                        } else {
                            view!!.updateButtonPromoCheckout(
                                validateUsePromoRevampUiModel.promoUiModel,
                                false
                            )
                            if (validateUsePromoRevampUiModel.promoUiModel.messageUiModel.state == "red") {
                                analyticsActionListener.sendAnalyticsViewPromoAfterAdjustItem(
                                    validateUsePromoRevampUiModel.promoUiModel.messageUiModel.text
                                )
                            } else {
                                for (voucherOrder in validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels) {
                                    if (voucherOrder.messageUiModel.state == "red") {
                                        analyticsActionListener.sendAnalyticsViewPromoAfterAdjustItem(
                                            voucherOrder.messageUiModel.text
                                        )
                                        break
                                    }
                                }
                            }
                        }
                        reloadCourierForMvc(
                            validateUsePromoRevampUiModel,
                            lastSelectedCourierOrderIndex,
                            cartString
                        )
                        checkUnCompletedPublisher()
                        isValidatingFinalPromo = false
                        updateShipmentButtonPaymentModel(loading = false)
                    }
                } catch (e: Throwable) {
                    Timber.d(e)
                    if (view != null) {
                        if (e is AkamaiErrorException) {
                            clearAllPromo()
                            view!!.showToastError(e.message)
                            view!!.resetAllCourier()
                            view!!.doResetButtonPromoCheckout()
                        } else {
                            view!!.renderErrorCheckPromoShipmentData(
                                getErrorMessage(
                                    view!!.activity,
                                    e
                                )
                            )
                        }
                        checkUnCompletedPublisher()
                        isValidatingFinalPromo = false
                        updateShipmentButtonPaymentModel(loading = false)
                    }
                }
            }
        }
    }

    private suspend fun awaitPromoQueue() {
        if (promoQueue.size == 1) {
            consumePromoQueue()
        } else {
            withContext(dispatchers.default) {
                while (promoQueue.isNotEmpty()) {
                    // wait until promo queue is empty
                }
            }
        }
    }

    private suspend fun consumePromoQueue() {
        withContext(dispatchers.immediate) {
            var itemToProcess = promoQueue.peek()
            loopProcess@ while (isActive && itemToProcess != null) {
                updateShipmentButtonPaymentModel(loading = true)
                val shipmentValidatePromoHolderData = itemToProcess
                if (promoQueue.filter { it.cartString == shipmentValidatePromoHolderData.cartString }.size > 1) {
                    // ignore this, because there is a new one in the queue
                    promoQueue.remove()
                    itemToProcess = promoQueue.peek()
                    continue@loopProcess
                }
                if (shipmentValidatePromoHolderData.validateUsePromoRequest != null) {
                    // do validate
                    try {
                        val validateUsePromoRevampUiModel = withContext(dispatchers.io) {
                            setValidateUseBoCodeInOneOrderOwoc(shipmentValidatePromoHolderData.validateUsePromoRequest)
                            validateUsePromoRevampUseCase.setParam(shipmentValidatePromoHolderData.validateUsePromoRequest)
                                .executeOnBackground()
                        }
                        promoQueue.remove()
                        itemToProcess = promoQueue.peek()
                        if (promoQueue.any { it.cartString == shipmentValidatePromoHolderData.cartString && it.validateUsePromoRequest != null }) {
                            // ignore this, because there is a new one in the queue
                            continue@loopProcess
                        }
                        onValidatePromoSuccess(
                            shipmentValidatePromoHolderData,
                            validateUsePromoRevampUiModel
                        )
                    } catch (t: Throwable) {
                        if (promoQueue.isNotEmpty()) {
                            promoQueue.remove()
                            itemToProcess = promoQueue.peek()
                        }
                        if (promoQueue.any { it.cartString == shipmentValidatePromoHolderData.cartString }) {
                            // ignore this, because there is a new one in the queue
                            continue@loopProcess
                        }
                        onValidatePromoError(shipmentValidatePromoHolderData, t)
                    }
                } else {
                    // do clear
                    val shipmentCartItem =
                        shipmentCartItemModelList.first { it is ShipmentCartItemModel && it.cartStringGroup == shipmentValidatePromoHolderData.cartString }
                    if ((shipmentCartItem as ShipmentCartItemModel).voucherLogisticItemUiModel?.code != shipmentValidatePromoHolderData.promoCode) {
                        // skip due to clear on non-applied promo, this is due to the queuing system
                        promoQueue.remove()
                        itemToProcess = promoQueue.peek()
                        onCancelPromoSuccess(shipmentValidatePromoHolderData, null)
                        continue@loopProcess
                    }
                    try {
                        val responseData =
                            withContext(dispatchers.io) {
                                clearCacheAutoApplyStackUseCase.setParams(
                                    ClearPromoRequest(
                                        ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
                                        false,
                                        ClearPromoOrderData(
                                            ArrayList(),
                                            arrayListOf(shipmentValidatePromoHolderData.clearPromoOrder!!)
                                        )
                                    )
                                ).executeOnBackground()
                            }
                        promoQueue.remove()
                        itemToProcess = promoQueue.peek()
                        if (promoQueue.any { it.cartString == shipmentValidatePromoHolderData.cartString && it.clearPromoOrder != null }) {
                            // ignore this, because there is a new one in the queue
                            continue@loopProcess
                        }
                        onCancelPromoSuccess(shipmentValidatePromoHolderData, responseData)
                    } catch (t: Throwable) {
                        Timber.d(t)
                        if (promoQueue.isNotEmpty()) {
                            promoQueue.remove()
                            itemToProcess = promoQueue.peek()
                        }
                        if (promoQueue.any { it.cartString == shipmentValidatePromoHolderData.cartString }) {
                            // ignore this, because there is a new one in the queue
                            continue@loopProcess
                        }
                        onCancelPromoError(shipmentValidatePromoHolderData)
                    }
                }
                updateShipmentButtonPaymentModel(loading = false)
            }
        }
    }

    // Clear promo BBO after choose other / non BBO courier
    fun cancelAutoApplyPromoStackLogistic(
        itemPosition: Int,
        promoCode: String,
        cartString: String,
        uniqueId: String,
        shipmentCartItemModel: ShipmentCartItemModel
    ) {
        couponStateChanged = true
        promoQueue.offer(
            ShipmentValidatePromoHolderData(
                cartPosition = itemPosition,
                cartString = cartString,
                promoCode = promoCode,
                clearPromoOrder = ClearPromoOrder(
                    uniqueId,
                    shipmentCartItemModel.shipmentCartData.boMetadata.boType,
                    arrayListOf(promoCode),
                    shipmentCartItemModel.shopId,
                    shipmentCartItemModel.isProductIsPreorder,
                    shipmentCartItemModel.cartItemModels[0].preOrderDurationDay.toString(),
                    shipmentCartItemModel.fulfillmentId,
                    shipmentCartItemModel.cartStringGroup
                )
            )
        )
        if (promoQueue.size == 1) {
            viewModelScope.launch(dispatchers.immediate) {
                consumePromoQueue()
            }
        }
    }

    private fun onCancelPromoSuccess(
        shipmentValidatePromoHolderData: ShipmentValidatePromoHolderData,
        responseData: ClearPromoUiModel?
    ) {
        if (view != null) {
            if (responseData?.successDataModel?.tickerMessage?.isNotEmpty() == true) {
                val ticker = tickerAnnouncementHolderData.value
                ticker.title = ""
                ticker.message =
                    responseData.successDataModel.tickerMessage
                tickerAnnouncementHolderData.value = ticker
            }
            val isLastAppliedPromo =
                isLastAppliedPromo(shipmentValidatePromoHolderData.promoCode)
            if (isLastAppliedPromo) {
                validateUsePromoRevampUiModel = null
            }
            view!!.onSuccessClearPromoLogistic(
                shipmentValidatePromoHolderData.cartPosition,
                isLastAppliedPromo
            )
        }
        val shipmentScheduleDeliveryMapData = getScheduleDeliveryMapData(
            shipmentValidatePromoHolderData.cartString
        )
        if (shipmentScheduleDeliveryMapData != null && shipmentScheduleDeliveryMapData.shouldStopInClearCache) {
            shipmentScheduleDeliveryMapData.donePublisher.onCompleted()
        }
    }

    private fun onCancelPromoError(
        shipmentValidatePromoHolderData: ShipmentValidatePromoHolderData
    ) {
        val shipmentScheduleDeliveryMapData = getScheduleDeliveryMapData(
            shipmentValidatePromoHolderData.cartString
        )
        if (shipmentScheduleDeliveryMapData != null && shipmentScheduleDeliveryMapData.shouldStopInClearCache) {
            shipmentScheduleDeliveryMapData.donePublisher.onCompleted()
        }
    }

    internal fun getClearPromoOrderByUniqueId(
        list: ArrayList<ClearPromoOrder>,
        uniqueId: String
    ): ClearPromoOrder? {
        for (clearPromoOrder in list) {
            if (clearPromoOrder.uniqueId == uniqueId) {
                return clearPromoOrder
            }
        }
        return null
    }

    // Clear promo red state before checkout
    fun cancelNotEligiblePromo(notEligiblePromoHolderdataArrayList: ArrayList<NotEligiblePromoHolderdata>) {
        couponStateChanged = true
        var hasPromo = false
        val globalPromoCodes = ArrayList<String>()
        val clearOrders = ArrayList<ClearPromoOrder>()
        for (notEligiblePromo in notEligiblePromoHolderdataArrayList) {
            if (notEligiblePromo.iconType == TYPE_ICON_GLOBAL) {
                globalPromoCodes.add(notEligiblePromo.promoCode)
                hasPromo = true
            } else {
                val clearOrder =
                    getClearPromoOrderByUniqueId(clearOrders, notEligiblePromo.uniqueId)
                if (clearOrder != null) {
                    clearOrder.codes.add(notEligiblePromo.promoCode)
                    hasPromo = true
                } else if (shipmentCartItemModelList.isNotEmpty()) {
                    for (shipmentCartItemModel in shipmentCartItemModelList) {
                        if (shipmentCartItemModel is ShipmentCartItemModel && shipmentCartItemModel.cartStringGroup == notEligiblePromo.cartStringGroup) {
                            val codes = ArrayList<String>()
                            codes.add(notEligiblePromo.promoCode)
                            clearOrders.add(
                                ClearPromoOrder(
                                    notEligiblePromo.uniqueId,
                                    shipmentCartItemModel.shipmentCartData.boMetadata.boType,
                                    codes,
                                    shipmentCartItemModel.shopId,
                                    shipmentCartItemModel.isProductIsPreorder,
                                    shipmentCartItemModel.cartItemModels[0].preOrderDurationDay.toString(),
                                    shipmentCartItemModel.fulfillmentId,
                                    shipmentCartItemModel.cartStringGroup
                                )
                            )
                            hasPromo = true
                            break
                        }
                    }
                }
            }
        }
        if (hasPromo) {
            viewModelScope.launch(dispatchers.immediate) {
                try {
                    val response = clearCacheAutoApplyStackUseCase.setParams(
                        ClearPromoRequest(
                            ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
                            false,
                            ClearPromoOrderData(globalPromoCodes, clearOrders)
                        )
                    ).executeOnBackground()
                    if (response.successDataModel.tickerMessage.isNotBlank()) {
                        val ticker = tickerAnnouncementHolderData.value
                        ticker.title = ""
                        ticker.message =
                            response.successDataModel.tickerMessage
                        tickerAnnouncementHolderData.value = ticker
                    }
                    view?.removeIneligiblePromo()
                } catch (t: Throwable) {
                    Timber.d(t)
                    view?.removeIneligiblePromo()
                }
            }
        }
    }

    fun hitClearAllBo() {
        val clearOrders = ArrayList<ClearPromoOrder>()
        var hasBo = false
        for (shipmentCartItemModel in shipmentCartItemModelList) {
            if (shipmentCartItemModel is ShipmentCartItemModel && shipmentCartItemModel.voucherLogisticItemUiModel != null && shipmentCartItemModel.voucherLogisticItemUiModel!!.code.isNotEmpty()) {
                val boCodes = ArrayList<String>()
                boCodes.add(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)
                clearOrders.add(
                    ClearPromoOrder(
                        shipmentCartItemModel.voucherLogisticItemUiModel!!.uniqueId,
                        shipmentCartItemModel.shipmentCartData.boMetadata.boType,
                        boCodes,
                        shipmentCartItemModel.shopId,
                        shipmentCartItemModel.isProductIsPreorder,
                        shipmentCartItemModel.cartItemModels[0].preOrderDurationDay.toString(),
                        shipmentCartItemModel.fulfillmentId,
                        shipmentCartItemModel.cartStringGroup
                    )
                )
                hasBo = true
            }
        }
        if (hasBo) {
            viewModelScope.launch(dispatchers.immediate) {
                try {
                    clearCacheAutoApplyStackUseCase.setParams(
                        ClearPromoRequest(
                            ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
                            false,
                            ClearPromoOrderData(
                                ArrayList(),
                                clearOrders
                            )
                        )
                    ).executeOnBackground()
                } catch (t: Throwable) {
                    Timber.d(t)
                }
            }
        }
    }

    private fun updateTickerAnnouncementData(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel) {
        if (validateUsePromoRevampUiModel.promoUiModel.tickerInfoUiModel.message.isNotEmpty()) {
            val ticker = tickerAnnouncementHolderData.value
            ticker.id =
                validateUsePromoRevampUiModel.promoUiModel.tickerInfoUiModel.statusCode.toString()
            ticker.title = ""
            ticker.message =
                validateUsePromoRevampUiModel.promoUiModel.tickerInfoUiModel.message
            tickerAnnouncementHolderData.value = ticker
            analyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
                ticker.id
            )
        }
    }

    fun doValidateUseLogisticPromoNew(
        cartPosition: Int,
        cartString: String,
        validateUsePromoRequest: ValidateUsePromoRequest,
        promoCode: String,
        showLoading: Boolean,
        newCourierItemData: CourierItemData?
    ) {
        if (view != null) {
            couponStateChanged = true
            if (showLoading) {
                view!!.setStateLoadingCourierStateAtIndex(cartPosition, true)
            }
            updateShipmentButtonPaymentModel(loading = true)
            promoQueue.offer(
                ShipmentValidatePromoHolderData(
                    validateUsePromoRequest,
                    cartPosition,
                    cartString,
                    promoCode,
                    newCourierItemData
                )
            )
            if (promoQueue.size == 1) {
                viewModelScope.launch(dispatchers.immediate) {
                    consumePromoQueue()
                }
            }
        }
    }

    private fun onValidatePromoSuccess(
        shipmentValidatePromoHolderData: ShipmentValidatePromoHolderData,
        validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel
    ) {
        if (view != null) {
            this@ShipmentViewModel.validateUsePromoRevampUiModel =
                validateUsePromoRevampUiModel
            val isValidatePromoRevampSuccess =
                validateUsePromoRevampUiModel.status.equals(
                    statusOK,
                    ignoreCase = true
                ) && validateUsePromoRevampUiModel.errorCode == statusCode200
            if (isValidatePromoRevampSuccess) {
                view!!.updateButtonPromoCheckout(
                    validateUsePromoRevampUiModel.promoUiModel,
                    true
                )
                view!!.setStateLoadingCourierStateAtIndex(
                    shipmentValidatePromoHolderData.cartPosition,
                    false
                )
                this@ShipmentViewModel.validateUsePromoRevampUiModel =
                    validateUsePromoRevampUiModel
                updateTickerAnnouncementData(validateUsePromoRevampUiModel)
                showErrorValidateUseIfAny(validateUsePromoRevampUiModel)
                validateBBOWithSpecificOrder(
                    validateUsePromoRevampUiModel,
                    shipmentValidatePromoHolderData.cartString,
                    shipmentValidatePromoHolderData.promoCode,
                    shipmentValidatePromoHolderData.newCourierItemData,
                    shipmentValidatePromoHolderData.cartPosition
                )
            } else {
                view!!.setStateLoadingCourierStateAtIndex(
                    shipmentValidatePromoHolderData.cartPosition,
                    false
                )
                if (validateUsePromoRevampUiModel.message.isNotEmpty()) {
                    val errMessage =
                        validateUsePromoRevampUiModel.message[0]
                    mTrackerShipment.eventClickLanjutkanTerapkanPromoError(
                        errMessage
                    )
                    eventCheckoutViewPromoMessage(errMessage)
                    view!!.showToastError(errMessage)
                    view!!.resetCourier(shipmentValidatePromoHolderData.cartPosition)
                    view!!.getShipmentCartItemModel(shipmentValidatePromoHolderData.cartPosition)
                        ?.let {
                            if (it.boCode.isNotEmpty()) {
                                clearCacheAutoApply(
                                    it,
                                    shipmentValidatePromoHolderData.promoCode,
                                    it.boUniqueId
                                )
                                clearOrderPromoCodeFromLastValidateUseRequest(
                                    shipmentValidatePromoHolderData.cartString,
                                    shipmentValidatePromoHolderData.promoCode
                                )
                                it.boCode = ""
                                it.boUniqueId = ""
                            }
                        }
                } else {
                    view!!.showToastError(
                        DEFAULT_ERROR_MESSAGE_FAIL_APPLY_BBO
                    )
                    view!!.resetCourier(shipmentValidatePromoHolderData.cartPosition)
                    view!!.getShipmentCartItemModel(shipmentValidatePromoHolderData.cartPosition)
                        ?.let {
                            if (it.boCode.isNotEmpty()) {
                                clearCacheAutoApply(
                                    it,
                                    shipmentValidatePromoHolderData.promoCode,
                                    it.boUniqueId
                                )
                                clearOrderPromoCodeFromLastValidateUseRequest(
                                    shipmentValidatePromoHolderData.cartString,
                                    shipmentValidatePromoHolderData.promoCode
                                )
                                it.boCode = ""
                                it.boUniqueId = ""
                            }
                        }
                }
            }
        }
        val shipmentScheduleDeliveryMapData =
            getScheduleDeliveryMapData(shipmentValidatePromoHolderData.cartString)
        if (shipmentScheduleDeliveryMapData != null && shipmentScheduleDeliveryMapData.shouldStopInValidateUsePromo) {
            shipmentScheduleDeliveryMapData.donePublisher.onCompleted()
        }
    }

    private fun onValidatePromoError(
        shipmentValidatePromoHolderData: ShipmentValidatePromoHolderData,
        t: Throwable
    ) {
        Timber.d(t)
        if (view != null) {
            view!!.setStateLoadingCourierStateAtIndex(
                shipmentValidatePromoHolderData.cartPosition,
                false
            )
            mTrackerShipment.eventClickLanjutkanTerapkanPromoError(t.message)
            if (t is AkamaiErrorException) {
                clearAllPromo()
                view!!.showToastError(t.message)
                view!!.resetAllCourier()
                view!!.doResetButtonPromoCheckout()
            } else {
                view!!.showToastError(t.message)
                view!!.resetCourier(shipmentValidatePromoHolderData.cartPosition)
                view!!.getShipmentCartItemModel(shipmentValidatePromoHolderData.cartPosition)
                    ?.let {
                        if (it.boCode.isNotEmpty()) {
                            clearCacheAutoApply(
                                it,
                                shipmentValidatePromoHolderData.promoCode,
                                it.boUniqueId
                            )
                            clearOrderPromoCodeFromLastValidateUseRequest(
                                shipmentValidatePromoHolderData.cartString,
                                shipmentValidatePromoHolderData.promoCode
                            )
                            it.boCode = ""
                            it.boUniqueId = ""
                        }
                    }
            }
            view!!.logOnErrorApplyBo(
                t,
                shipmentValidatePromoHolderData.cartPosition,
                shipmentValidatePromoHolderData.promoCode
            )
            val shipmentScheduleDeliveryMapData =
                getScheduleDeliveryMapData(shipmentValidatePromoHolderData.cartString)
            if (shipmentScheduleDeliveryMapData != null && shipmentScheduleDeliveryMapData.shouldStopInValidateUsePromo) {
                shipmentScheduleDeliveryMapData.donePublisher.onCompleted()
            }
        }
    }

    private fun getBBOCount(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel): Int {
        var bboCount = 0
        for (voucherOrder in validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels) {
            if (voucherOrder.type.equals("logistic", ignoreCase = true)) {
                bboCount++
            }
        }
        return bboCount
    }

    private fun showErrorValidateUseIfAny(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel) {
        val bboCount = getBBOCount(validateUsePromoRevampUiModel)
        if (bboCount == 1) {
            for (voucherOrder in validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels) {
                if (voucherOrder.type.equals(
                        "logistic",
                        ignoreCase = true
                    ) && voucherOrder.messageUiModel.state.equals(
                            "red",
                            ignoreCase = true
                        )
                ) {
                    view?.showToastError(voucherOrder.messageUiModel.text)
                    return
                }
            }
        }
        val messageInfo =
            validateUsePromoRevampUiModel.promoUiModel.additionalInfoUiModel.errorDetailUiModel.message
        if (messageInfo.isNotEmpty()) {
            view?.showToastNormal(messageInfo)
        }
    }

    private fun validateBBO(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel) {
        val updatedCartStringGroup: ArrayList<String> = arrayListOf<String>()
        voucherLoop@ for (voucherOrder in validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels) {
            if (voucherOrder.type.equals(
                    "logistic",
                    ignoreCase = true
                ) && voucherOrder.messageUiModel.state.equals(
                        "red",
                        ignoreCase = true
                    )
            ) {
                for (shipmentCartItemModel in shipmentCartItemModelList) {
                    if (shipmentCartItemModel is ShipmentCartItemModel && shipmentCartItemModel.cartStringGroup == voucherOrder.cartStringGroup) {
                        if (view != null) {
                            updatedCartStringGroup.add(voucherOrder.cartStringGroup)
                            view!!.resetCourier(shipmentCartItemModel)
                            view!!.logOnErrorApplyBo(
                                MessageErrorException(
                                    voucherOrder.messageUiModel.text
                                ),
                                shipmentCartItemModel,
                                voucherOrder.code
                            )
                            break@voucherLoop
                        }
                    }
                }
            } else if (voucherOrder.type.equals(
                    "logistic",
                    ignoreCase = true
                ) && voucherOrder.messageUiModel.state.equals(
                        "green",
                        ignoreCase = true
                    )
            ) {
                updatedCartStringGroup.add(voucherOrder.cartStringGroup)
            }
        }
        // if not voucher order found for attempted apply BO order,
        // then should reset courier and not apply the BO
        // this should be a rare case
        for (shipmentCartItemModel in shipmentCartItemModelList) {
            if (shipmentCartItemModel is ShipmentCartItemModel) {
                val code = shipmentCartItemModel.voucherLogisticItemUiModel?.code
                if (!code.isNullOrEmpty() && view != null && !updatedCartStringGroup.contains(
                        shipmentCartItemModel.cartStringGroup
                    )
                ) {
                    view!!.resetCourier(shipmentCartItemModel)
                    view!!.logOnErrorApplyBo(
                        MessageErrorException("voucher order not found"),
                        shipmentCartItemModel,
                        code
                    )
                }
            }
        }
    }

    private fun validateBBOWithSpecificOrder(
        validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel,
        cartString: String?,
        promoCode: String,
        recommendedCourier: CourierItemData?,
        cartItemPosition: Int
    ) {
        var orderFound = false
        for (voucherOrder in validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels) {
            if (voucherOrder.cartStringGroup == cartString && voucherOrder.code.equals(
                    promoCode,
                    ignoreCase = true
                )
            ) {
                orderFound = true
                if (voucherOrder.messageUiModel.state != "red") {
                    for (shipmentCartItemModel in shipmentCartItemModelList) {
                        if (shipmentCartItemModel is ShipmentCartItemModel && shipmentCartItemModel.cartStringGroup == voucherOrder.cartStringGroup) {
                            if (view != null && recommendedCourier != null) {
                                view!!.setSelectedCourier(
                                    cartItemPosition,
                                    recommendedCourier,
                                    true,
                                    false
                                )
                                processSaveShipmentState(shipmentCartItemModel)
                            }
                        }
                    }
                }
            }
            if (voucherOrder.type.equals(
                    "logistic",
                    ignoreCase = true
                ) && voucherOrder.messageUiModel.state.equals("red", ignoreCase = true)
            ) {
                for (shipmentCartItemModel in shipmentCartItemModelList) {
                    if (shipmentCartItemModel is ShipmentCartItemModel && shipmentCartItemModel.cartStringGroup == voucherOrder.cartStringGroup) {
                        if (view != null) {
                            view!!.resetCourier(shipmentCartItemModel)
                            view!!.logOnErrorApplyBo(
                                MessageErrorException(
                                    voucherOrder.messageUiModel.text
                                ),
                                shipmentCartItemModel,
                                voucherOrder.code
                            )
                        }
                    }
                }
            }
        }
        if (!orderFound) {
            // if not voucher order found for attempted apply BO order,
            // then should reset courier and not apply the BO
            // this should be a rare case
            for (shipmentCartItemModel in shipmentCartItemModelList) {
                if (shipmentCartItemModel is ShipmentCartItemModel && shipmentCartItemModel.cartStringGroup == cartString) {
                    if (view != null) {
                        view!!.resetCourier(shipmentCartItemModel)
                        view!!.logOnErrorApplyBo(
                            MessageErrorException("voucher order not found"),
                            shipmentCartItemModel,
                            promoCode
                        )
                        break
                    }
                }
            }
        }
    }

    fun setLastValidateUseRequest(latValidateUseRequest: ValidateUsePromoRequest?) {
        lastValidateUseRequest = latValidateUseRequest
    }

    private fun isLastAppliedPromo(promoCode: String?): Boolean {
        if (validateUsePromoRevampUiModel != null) {
            val voucherOrders: List<PromoCheckoutVoucherOrdersItemUiModel> =
                validateUsePromoRevampUiModel!!.promoUiModel.voucherOrderUiModels
            if (voucherOrders.isNotEmpty()) {
                for (voucherOrder in voucherOrders) {
                    if (voucherOrder.code != promoCode) return false
                }
            }
            val codes: List<String> = validateUsePromoRevampUiModel!!.promoUiModel.codes
            if (codes.isNotEmpty()) {
                for (code in codes) {
                    if (code != promoCode) return false
                }
            }
        }
        return true
    }

    fun resetPromoCheckoutData() {
        lastApplyData.value = LastApplyUiModel()
    }

    fun updatePromoCheckoutData(promoUiModel: PromoUiModel) {
        lastApplyData.value = LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(
            promoUiModel
        )
    }

    fun validateBoPromo(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel): Pair<ArrayList<String>, ArrayList<String>> {
        val unappliedBoPromoUniqueIds = ArrayList<String>()
        val reloadedUniqueIds = ArrayList<String>()
        val unprocessedUniqueIds = ArrayList<String>()
        for (shipmentCartItemModel in shipmentCartItemModelList) {
            unprocessedUniqueIds.add(shipmentCartItemModel.cartStringGroup)
        }
        // loop to list voucher orders to be applied this will be used later
        val toBeAppliedVoucherOrders: MutableList<PromoCheckoutVoucherOrdersItemUiModel> =
            ArrayList()
        for (voucherOrdersItemUiModel in validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels) {
            // voucher with shippingId not zero, spId not zero, and voucher type logistic as promo for BO
            if (voucherOrdersItemUiModel.shippingId > 0 && voucherOrdersItemUiModel.spId > 0 && voucherOrdersItemUiModel.type == "logistic") {
                if (voucherOrdersItemUiModel.messageUiModel.state == "green") {
                    toBeAppliedVoucherOrders.add(voucherOrdersItemUiModel)
                    unprocessedUniqueIds.remove(voucherOrdersItemUiModel.cartStringGroup)
                }
            }
        }
        for (shipmentCartItemModel in shipmentCartItemModelList) {
            if (shipmentCartItemModel is ShipmentCartItemModel && shipmentCartItemModel.voucherLogisticItemUiModel != null &&
                unprocessedUniqueIds.contains(shipmentCartItemModel.cartStringGroup)
            ) {
                doUnapplyBo(
                    shipmentCartItemModel.cartStringGroup,
                    shipmentCartItemModel.voucherLogisticItemUiModel!!.uniqueId,
                    shipmentCartItemModel.voucherLogisticItemUiModel!!.code
                )
                unappliedBoPromoUniqueIds.add(shipmentCartItemModel.cartStringGroup)
                reloadedUniqueIds.add(shipmentCartItemModel.cartStringGroup)
            }
        }
        if (unappliedBoPromoUniqueIds.size > 0) {
            view?.renderUnapplyBoIncompleteShipment(unappliedBoPromoUniqueIds)
        }
        doApplyBoNew(toBeAppliedVoucherOrders)
        for (voucherOrders in toBeAppliedVoucherOrders) {
            reloadedUniqueIds.add(voucherOrders.cartStringGroup)
        }
        return Pair(reloadedUniqueIds, unappliedBoPromoUniqueIds)
    }

    fun doUnapplyBo(cartStringGroup: String, uniqueId: String, promoCode: String) {
        val itemAdapterPosition =
            view?.getShipmentCartItemModelAdapterPositionByCartStringGroup(cartStringGroup) ?: -1
        val shipmentCartItemModel = view?.getShipmentCartItemModel(itemAdapterPosition)
        if (shipmentCartItemModel != null && itemAdapterPosition != -1) {
            view?.resetCourier(itemAdapterPosition)
            clearCacheAutoApply(shipmentCartItemModel, promoCode, uniqueId)
            clearOrderPromoCodeFromLastValidateUseRequest(cartStringGroup, promoCode)
            view?.onNeedUpdateViewItem(itemAdapterPosition)
        }
    }

    private fun clearCacheAutoApply(
        shipmentCartItemModel: ShipmentCartItemModel,
        promoCode: String,
        uniqueId: String
    ) {
        viewModelScope.launch(dispatchers.immediate) {
            try {
                val globalCodes: List<String> = ArrayList()
                val clearOrders: MutableList<ClearPromoOrder> = ArrayList()
                val promoCodes: MutableList<String> = ArrayList()
                promoCodes.add(promoCode)
                clearOrders.add(
                    ClearPromoOrder(
                        uniqueId,
                        shipmentCartItemModel.shipmentCartData.boMetadata.boType,
                        promoCodes,
                        shipmentCartItemModel.shopId,
                        shipmentCartItemModel.isProductIsPreorder,
                        shipmentCartItemModel.cartItemModels[0].preOrderDurationDay.toString(),
                        shipmentCartItemModel.fulfillmentId,
                        shipmentCartItemModel.cartStringGroup
                    )
                )
                val params = ClearPromoRequest(
                    ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
                    false,
                    ClearPromoOrderData(globalCodes, clearOrders)
                )
                clearCacheAutoApplyStackUseCase.setParams(params).executeOnBackground()
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }

    fun clearOrderPromoCodeFromLastValidateUseRequest(
        cartStringGroup: String,
        promoCode: String
    ) {
        if (lastValidateUseRequest != null) {
            for (order in lastValidateUseRequest!!.orders) {
                if (order.cartStringGroup == cartStringGroup) {
                    order.codes.remove(promoCode)
                }
            }
        }
        val lastApplyUiModel = lastApplyData.value
        val voucherOrders = lastApplyUiModel.voucherOrders.toMutableList()
        for (voucherOrder in voucherOrders) {
            if (voucherOrder.cartStringGroup == cartStringGroup && voucherOrder.code == promoCode) {
                voucherOrders.remove(voucherOrder)
                break
            }
        }
        lastApplyUiModel.voucherOrders = voucherOrders
        lastApplyData.value = lastApplyUiModel
    }

    internal fun doApplyBoNew(voucherOrdersItemUiModels: List<PromoCheckoutVoucherOrdersItemUiModel>) {
        val listToProcess =
            arrayListOf<Triple<Int, PromoCheckoutVoucherOrdersItemUiModel, ShipmentCartItemModel>>()
        for (voucherOrdersItemUiModel in voucherOrdersItemUiModels) {
            val itemAdapterPosition =
                view?.getShipmentCartItemModelAdapterPositionByCartStringGroup(
                    voucherOrdersItemUiModel.cartStringGroup
                ) ?: -1
            val shipmentCartItemModel = view?.getShipmentCartItemModel(itemAdapterPosition)
            if (shipmentCartItemModel != null && itemAdapterPosition != -1) {
                if (shipmentCartItemModel.voucherLogisticItemUiModel == null ||
                    shipmentCartItemModel.voucherLogisticItemUiModel!!.code != voucherOrdersItemUiModel.code
                ) {
                    listToProcess.add(
                        Triple(
                            itemAdapterPosition,
                            voucherOrdersItemUiModel,
                            shipmentCartItemModel
                        )
                    )
                }
            }
        }
        processBoPromoCourierRecommendationNew(listToProcess)
    }

    fun validateClearAllBoPromo() {
        val validateUseRequest = lastValidateUseRequest
        if (validateUseRequest != null) {
            for (shipmentCartItemModel in shipmentCartItemModelList) {
                if (shipmentCartItemModel is ShipmentCartItemModel && shipmentCartItemModel.voucherLogisticItemUiModel != null) {
                    for (order in validateUseRequest.orders) {
                        if (order.cartStringGroup == shipmentCartItemModel.cartStringGroup && order.codes.isEmpty()) {
                            doUnapplyBo(
                                shipmentCartItemModel.cartStringGroup,
                                shipmentCartItemModel.voucherLogisticItemUiModel!!.uniqueId,
                                shipmentCartItemModel.voucherLogisticItemUiModel!!.code
                            )
                            break
                        }
                    }
                }
            }
        }
    }
    // endregion

    // region rates
    // Re-fetch rates to get promo mvc icon
    private fun reloadCourierForMvc(
        validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel,
        lastSelectedCourierOrderIndex: Int,
        cartString: String?
    ) {
        val promoSpids = validateUsePromoRevampUiModel.promoUiModel.additionalInfoUiModel.promoSpIds
        if (promoSpids.isNotEmpty() && lastSelectedCourierOrderIndex > -1) {
            for ((uniqueId) in promoSpids) {
                if (uniqueId.equals(cartString, ignoreCase = true)) {
                    view?.prepareReloadRates(lastSelectedCourierOrderIndex, false)
                    break
                }
            }
        }
    }

    fun processGetCourierRecommendationMvc(
        shipperId: Int,
        spId: Int,
        itemPosition: Int,
        shipmentDetailData: ShipmentDetailData?,
        shipmentCartItemModel: ShipmentCartItemModel,
        shopShipmentList: List<ShopShipment>,
        products: ArrayList<Product>,
        cartString: String?,
        isTradeInDropOff: Boolean,
        recipientAddressModel: RecipientAddressModel?,
        skipMvc: Boolean
    ) {
        val shippingParam = getShippingParam(
            shipmentDetailData,
            products,
            cartString,
            isTradeInDropOff,
            recipientAddressModel
        )
        val counter = codData?.counterCod ?: -1
        val cornerId = this.recipientAddressModel.isCornerAddress
        val pslCode = getLogisticPromoCode(shipmentCartItemModel)
        val isLeasing = shipmentCartItemModel.isLeasingProduct
        val mvc = generateRatesMvcParam(cartString)
        val ratesParamBuilder = RatesParam.Builder(shopShipmentList, shippingParam)
            .isCorner(cornerId)
            .codHistory(counter)
            .isLeasing(isLeasing)
            .promoCode(pslCode)
            .cartData(cartDataForRates)
            .warehouseId(shipmentCartItemModel.fulfillmentId.toString())
            .mvc("")
        if (!skipMvc) {
            ratesParamBuilder.mvc(mvc)
        }
        val param = ratesParamBuilder.build()
        if (isTradeInDropOff) {
            viewModelScope.launch(dispatchers.immediate) {
                try {
                    val shippingRecommendationData = withContext(dispatchers.io) {
                        ratesApiUseCase.execute(param)
                            .map { shippingRecommendationData: ShippingRecommendationData ->
                                stateConverter.fillState(
                                    shippingRecommendationData,
                                    shopShipmentList,
                                    spId,
                                    0
                                )
                            }.toBlocking().single()
                    }
                    val boPromoCode = ""
                    var errorReason = "rates invalid data"
                    if (shippingRecommendationData.shippingDurationUiModels.isNotEmpty()) {
                        for (shippingDurationUiModel in shippingRecommendationData.shippingDurationUiModels) {
                            if (shippingDurationUiModel.shippingCourierViewModelList.isNotEmpty()) {
                                for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                    shippingCourierUiModel.isSelected = false
                                }
                                val shippingCourierUiModel =
                                    shippingDurationUiModel.shippingCourierViewModelList.first()
                                if (!shippingCourierUiModel.productData.error?.errorMessage.isNullOrEmpty()) {
                                    view?.renderCourierStateFailed(
                                        itemPosition,
                                        true,
                                        false
                                    )
                                    view?.logOnErrorLoadCourier(
                                        MessageErrorException(
                                            shippingCourierUiModel.productData.error?.errorMessage
                                        ),
                                        itemPosition,
                                        boPromoCode
                                    )
                                    return@launch
                                } else {
                                    val courierItemData = generateCourierItemData(
                                        true,
                                        shipperId,
                                        spId,
                                        shipmentCartItemModel,
                                        shippingCourierUiModel,
                                        shippingRecommendationData
                                    )
                                    if (shippingCourierUiModel.productData.isUiRatesHidden && shippingCourierUiModel.serviceData.selectedShipperProductId == 0 && courierItemData.logPromoCode.isNullOrEmpty()) {
                                        // courier should only be used with BO, but no BO code found
                                        view?.renderCourierStateFailed(
                                            itemPosition,
                                            true,
                                            false
                                        )
                                        view?.logOnErrorLoadCourier(
                                            MessageErrorException("rates ui hidden but no promo"),
                                            itemPosition,
                                            boPromoCode
                                        )
                                        return@launch
                                    }
                                    shippingCourierUiModel.isSelected = true
                                    setShippingCourierViewModelsState(
                                        shippingDurationUiModel.shippingCourierViewModelList,
                                        shipmentCartItemModel.orderNumber
                                    )
                                    view?.renderCourierStateSuccess(
                                        courierItemData,
                                        itemPosition,
                                        true
                                    )
                                    return@launch
                                }
                            }
                        }
                    } else {
                        errorReason = "rates empty data"
                    }
                    view?.renderCourierStateFailed(itemPosition, true, false)
                    view?.logOnErrorLoadCourier(
                        MessageErrorException(errorReason),
                        itemPosition,
                        boPromoCode
                    )
                } catch (t: Throwable) {
                    val exception = getActualThrowableForRx(t)
                    Timber.d(exception)
                    view?.let { v ->
                        v.logOnErrorLoadCourier(exception, itemPosition, "")
                        if (exception is AkamaiErrorException) {
                            v.showToastErrorAkamai(exception.message)
                        }
                    }
                }
            }
        } else {
            updateShipmentButtonPaymentModel(loading = true)
            viewModelScope.launch(dispatchers.immediate) {
                try {
                    val shippingRecommendationData = withContext(dispatchers.io) {
                        if (shipmentCartItemModel.ratesValidationFlow) {
                            ratesWithScheduleUseCase.execute(
                                param,
                                shipmentCartItemModel.fulfillmentId.toString()
                            )
                                .map { shippingRecommendationData: ShippingRecommendationData ->
                                    stateConverter.fillState(
                                        shippingRecommendationData,
                                        shopShipmentList,
                                        spId,
                                        0
                                    )
                                }.toBlocking().single()
                        } else {
                            ratesUseCase.execute(param)
                                .map { shippingRecommendationData: ShippingRecommendationData ->
                                    stateConverter.fillState(
                                        shippingRecommendationData,
                                        shopShipmentList,
                                        spId,
                                        0
                                    )
                                }.toBlocking().single()
                        }
                    }
                    val boPromoCode = ""
                    var errorReason = "rates invalid data"
                    if (shippingRecommendationData.shippingDurationUiModels.isNotEmpty()) {
                        for (shippingDurationUiModel in shippingRecommendationData.shippingDurationUiModels) {
                            if (shippingDurationUiModel.shippingCourierViewModelList.isNotEmpty()) {
                                for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                    shippingCourierUiModel.isSelected = false
                                }
                                for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                    if (shippingCourierUiModel.productData.shipperProductId == spId && !shippingCourierUiModel.serviceData.isUiRatesHidden) {
                                        if (!shippingCourierUiModel.productData.error?.errorMessage.isNullOrEmpty()) {
                                            view?.renderCourierStateFailed(
                                                itemPosition,
                                                false,
                                                false
                                            )
                                            view?.logOnErrorLoadCourier(
                                                MessageErrorException(
                                                    shippingCourierUiModel.productData.error?.errorMessage
                                                ),
                                                itemPosition,
                                                boPromoCode
                                            )
                                            return@launch
                                        } else {
                                            val courierItemData =
                                                if (shipmentCartItemModel.ratesValidationFlow) {
                                                    generateCourierItemDataWithScheduleDelivery(
                                                        true,
                                                        shipperId,
                                                        spId,
                                                        shipmentCartItemModel,
                                                        shippingCourierUiModel,
                                                        shippingRecommendationData
                                                    )
                                                } else {
                                                    generateCourierItemData(
                                                        true,
                                                        shipperId,
                                                        spId,
                                                        shipmentCartItemModel,
                                                        shippingCourierUiModel,
                                                        shippingRecommendationData
                                                    )
                                                }
                                            if (shippingCourierUiModel.productData.isUiRatesHidden && shippingCourierUiModel.serviceData.selectedShipperProductId == 0 && courierItemData.logPromoCode.isNullOrEmpty()) {
                                                // courier should only be used with BO, but no BO code found
                                                view?.renderCourierStateFailed(
                                                    itemPosition,
                                                    false,
                                                    false
                                                )
                                                view?.logOnErrorLoadCourier(
                                                    MessageErrorException("rates ui hidden but no promo"),
                                                    itemPosition,
                                                    boPromoCode
                                                )
                                                return@launch
                                            }
                                            shippingCourierUiModel.isSelected = true
                                            setShippingCourierViewModelsState(
                                                shippingDurationUiModel.shippingCourierViewModelList,
                                                shipmentCartItemModel.orderNumber
                                            )
                                            view?.renderCourierStateSuccess(
                                                courierItemData,
                                                itemPosition,
                                                false
                                            )
                                            return@launch
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        errorReason = "rates empty data"
                    }
                    view?.renderCourierStateFailed(itemPosition, false, false)
                    view?.logOnErrorLoadCourier(
                        MessageErrorException(errorReason),
                        itemPosition,
                        boPromoCode
                    )
                } catch (t: Throwable) {
                    val exception = getActualThrowableForRx(t)
                    Timber.d(exception)
                    view?.let { v ->
                        v.logOnErrorLoadCourier(exception, itemPosition, "")
                        if (exception is AkamaiErrorException) {
                            v.showToastErrorAkamai(exception.message)
                        }
                    }
                }
            }
        }
    }

    fun processGetCourierRecommendation(
        shipperId: Int,
        spId: Int,
        itemPosition: Int,
        shipmentDetailData: ShipmentDetailData?,
        shipmentCartItemModel: ShipmentCartItemModel,
        shopShipmentList: List<ShopShipment>,
        products: ArrayList<Product>,
        cartString: String?,
        isTradeInDropOff: Boolean,
        recipientAddressModel: RecipientAddressModel?
    ) {
        val shippingParam = getShippingParam(
            shipmentDetailData,
            products,
            cartString,
            isTradeInDropOff,
            recipientAddressModel
        )
        val counter = codData?.counterCod ?: -1
        val cornerId = this.recipientAddressModel.isCornerAddress
        val pslCode = getLogisticPromoCode(shipmentCartItemModel)
        val isLeasing = shipmentCartItemModel.isLeasingProduct
        val mvc = generateRatesMvcParam(cartString)
        val ratesParamBuilder = RatesParam.Builder(shopShipmentList, shippingParam)
            .isCorner(cornerId)
            .codHistory(counter)
            .isLeasing(isLeasing)
            .promoCode(pslCode)
            .cartData(cartDataForRates)
            .warehouseId(shipmentCartItemModel.fulfillmentId.toString())
            .mvc("")
        ratesParamBuilder.mvc(mvc)
        val param = ratesParamBuilder.build()
        if (isTradeInDropOff) {
            viewModelScope.launch(dispatchers.immediate) {
                try {
                    val shippingRecommendationData = withContext(dispatchers.io) {
                        ratesApiUseCase.execute(param)
                            .map { shippingRecommendationData: ShippingRecommendationData ->
                                stateConverter.fillState(
                                    shippingRecommendationData,
                                    shopShipmentList,
                                    spId,
                                    0
                                )
                            }.toBlocking().single()
                    }
                    val boPromoCode = getBoPromoCode(shipmentCartItemModel)
                    var errorReason = "rates invalid data"
                    if (shipmentCartItemModel.shouldResetCourier) {
                        shipmentCartItemModel.shouldResetCourier = false
                        error("racing condition against epharmacy validation")
                    }
                    if (shippingRecommendationData.shippingDurationUiModels.isNotEmpty()) {
                        for (shippingDurationUiModel in shippingRecommendationData.shippingDurationUiModels) {
                            if (shippingDurationUiModel.shippingCourierViewModelList.isNotEmpty()) {
                                for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                    shippingCourierUiModel.isSelected = false
                                }
                                for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                    if (!shippingCourierUiModel.productData.error?.errorMessage.isNullOrEmpty()) {
                                        view?.renderCourierStateFailed(
                                            itemPosition,
                                            true,
                                            false
                                        )
                                        view?.logOnErrorLoadCourier(
                                            MessageErrorException(
                                                shippingCourierUiModel.productData.error?.errorMessage
                                            ),
                                            itemPosition,
                                            boPromoCode
                                        )
                                        return@launch
                                    } else {
                                        val courierItemData = generateCourierItemData(
                                            false,
                                            shipperId,
                                            spId,
                                            shipmentCartItemModel,
                                            shippingCourierUiModel,
                                            shippingRecommendationData
                                        )
                                        if (shippingCourierUiModel.productData.isUiRatesHidden && shippingCourierUiModel.serviceData.selectedShipperProductId == 0 && courierItemData.logPromoCode.isNullOrEmpty()) {
                                            // courier should only be used with BO, but no BO code found
                                            view?.renderCourierStateFailed(
                                                itemPosition,
                                                true,
                                                false
                                            )
                                            view?.logOnErrorLoadCourier(
                                                MessageErrorException("rates ui hidden but no promo"),
                                                itemPosition,
                                                boPromoCode
                                            )
                                            return@launch
                                        }
                                        shippingCourierUiModel.isSelected = true
                                        setShippingCourierViewModelsState(
                                            shippingDurationUiModel.shippingCourierViewModelList,
                                            shipmentCartItemModel.orderNumber
                                        )
                                        view?.renderCourierStateSuccess(
                                            courierItemData,
                                            itemPosition,
                                            true
                                        )
                                        return@launch
                                    }
                                }
                            }
                        }
                    } else {
                        errorReason = "rates empty data"
                    }
                    view?.renderCourierStateFailed(itemPosition, true, false)
                    view?.logOnErrorLoadCourier(
                        MessageErrorException(errorReason),
                        itemPosition,
                        boPromoCode
                    )
                } catch (t: Throwable) {
                    val exception = getActualThrowableForRx(t)
                    Timber.d(exception)
                    val boPromoCode = getBoPromoCode(shipmentCartItemModel)
                    view?.let { v ->
                        v.renderCourierStateFailed(itemPosition, true, false)
                        v.logOnErrorLoadCourier(exception, itemPosition, boPromoCode)
                        if (exception is AkamaiErrorException) {
                            v.showToastErrorAkamai(exception.message)
                        }
                    }
                }
            }
        } else {
            updateShipmentButtonPaymentModel(loading = true)
            ratesQueue.offer(
                ShipmentGetCourierHolderData(
                    shipperId,
                    spId,
                    itemPosition,
                    shipmentCartItemModel,
                    shopShipmentList,
                    true,
                    false,
                    false,
                    param
                )
            )
            if (ratesQueue.size == 1) {
                viewModelScope.launch(dispatchers.immediate) {
                    consumeRatesQueue()
                }
            }
        }
    }

    private suspend fun consumeRatesQueue() {
        withContext(dispatchers.immediate) {
            var itemToProcess = ratesQueue.peek()
            loopProcess@ while (isActive && itemToProcess != null) {
                val shipmentGetCourierHolderData = itemToProcess
                if (shipmentGetCourierHolderData.shipmentCartItemModel.ratesValidationFlow) {
                    try {
                        val shippingRecommendationData = withContext(dispatchers.io) {
                            ratesWithScheduleUseCase.execute(
                                shipmentGetCourierHolderData.ratesParam,
                                shipmentGetCourierHolderData.shipmentCartItemModel.fulfillmentId.toString()
                            )
                                .map { shippingRecommendationData: ShippingRecommendationData ->
                                    stateConverter.fillState(
                                        shippingRecommendationData,
                                        shipmentGetCourierHolderData.shopShipmentList,
                                        shipmentGetCourierHolderData.spId,
                                        0
                                    )
                                }.toBlocking().single()
                        }
                        val boPromoCode = getBoPromoCode(
                            shipmentGetCourierHolderData.shipmentCartItemModel
                        )
                        var errorReason = "rates invalid data"
                        if (shippingRecommendationData.shippingDurationUiModels.isNotEmpty() && shippingRecommendationData.scheduleDeliveryData != null) {
                            if (isBoUnstackEnabled && shipmentGetCourierHolderData.shipmentCartItemModel.boCode.isNotEmpty()) {
                                val logisticPromo =
                                    shippingRecommendationData.listLogisticPromo.firstOrNull { it.promoCode == shipmentGetCourierHolderData.shipmentCartItemModel.boCode && !it.disabled }
                                if (logisticPromo != null) {
                                    for (shippingDurationUiModel in shippingRecommendationData.shippingDurationUiModels) {
                                        if (shippingDurationUiModel.shippingCourierViewModelList.isNotEmpty()) {
                                            for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                                shippingCourierUiModel.isSelected = false
                                            }
                                            for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                                if (shippingCourierUiModel.productData.shipperProductId == logisticPromo.shipperProductId && shippingCourierUiModel.productData.shipperId == logisticPromo.shipperId) {
                                                    if (!shippingCourierUiModel.productData.error?.errorMessage.isNullOrEmpty()) {
                                                        view?.renderCourierStateFailed(
                                                            shipmentGetCourierHolderData.itemPosition,
                                                            false,
                                                            false
                                                        )
                                                        view?.logOnErrorLoadCourier(
                                                            MessageErrorException(
                                                                shippingCourierUiModel.productData.error?.errorMessage
                                                            ),
                                                            shipmentGetCourierHolderData.itemPosition,
                                                            boPromoCode
                                                        )
                                                        ratesQueue.remove()
                                                        itemToProcess = ratesQueue.peek()
                                                        continue@loopProcess
                                                    } else {
                                                        val courierItemData =
                                                            generateCourierItemDataWithScheduleDelivery(
                                                                false,
                                                                shipmentGetCourierHolderData.shipperId,
                                                                shipmentGetCourierHolderData.spId,
                                                                shipmentGetCourierHolderData.shipmentCartItemModel,
                                                                shippingCourierUiModel,
                                                                shippingRecommendationData,
                                                                logisticPromo
                                                            )
                                                        val validateUsePromoRequest =
                                                            generateValidateUsePromoRequest().copy()
                                                        for (ordersItem in validateUsePromoRequest.orders) {
                                                            if (ordersItem.cartStringGroup == shipmentGetCourierHolderData.shipmentCartItemModel.cartStringGroup) {
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
                                                                    shipmentGetCourierHolderData.shipmentCartItemModel.validationMetadata
                                                            }
                                                        }
                                                        removeInvalidBoCodeFromPromoRequest(
                                                            shipmentGetCourierHolderData,
                                                            validateUsePromoRequest
                                                        )
                                                        promoQueue.offer(
                                                            ShipmentValidatePromoHolderData(
                                                                validateUsePromoRequest,
                                                                shipmentGetCourierHolderData.itemPosition,
                                                                shipmentGetCourierHolderData.shipmentCartItemModel.cartStringGroup,
                                                                courierItemData.selectedShipper.logPromoCode!!,
                                                                courierItemData
                                                            )
                                                        )
                                                        awaitPromoQueue()
                                                        ratesQueue.remove()
                                                        itemToProcess = ratesQueue.peek()
                                                        continue@loopProcess
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    errorReason = "promo not matched"
                                }
                            } else {
                                for (shippingDurationUiModel in shippingRecommendationData.shippingDurationUiModels) {
                                    if (shippingDurationUiModel.shippingCourierViewModelList.isNotEmpty()) {
                                        for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                            shippingCourierUiModel.isSelected = false
                                        }
                                        val selectedSpId =
                                            if (shippingDurationUiModel.serviceData.selectedShipperProductId > 0) {
                                                shippingDurationUiModel.serviceData.selectedShipperProductId
                                            } else {
                                                shipmentGetCourierHolderData.spId
                                            }
                                        for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                            if (shippingCourierUiModel.productData.shipperProductId == selectedSpId && !shippingCourierUiModel.serviceData.isUiRatesHidden) {
                                                if (!shippingCourierUiModel.productData.error?.errorMessage.isNullOrEmpty()) {
                                                    view?.renderCourierStateFailed(
                                                        shipmentGetCourierHolderData.itemPosition,
                                                        false,
                                                        false
                                                    )
                                                    view?.logOnErrorLoadCourier(
                                                        MessageErrorException(
                                                            shippingCourierUiModel.productData.error?.errorMessage
                                                        ),
                                                        shipmentGetCourierHolderData.itemPosition,
                                                        boPromoCode
                                                    )
                                                    ratesQueue.remove()
                                                    itemToProcess = ratesQueue.peek()
                                                    continue@loopProcess
                                                } else {
                                                    val courierItemData =
                                                        generateCourierItemDataWithScheduleDelivery(
                                                            false,
                                                            shipmentGetCourierHolderData.shipperId,
                                                            shipmentGetCourierHolderData.spId,
                                                            shipmentGetCourierHolderData.shipmentCartItemModel,
                                                            shippingCourierUiModel,
                                                            shippingRecommendationData
                                                        )
                                                    if (shippingCourierUiModel.productData.isUiRatesHidden && shippingCourierUiModel.serviceData.selectedShipperProductId == 0 && courierItemData.logPromoCode.isNullOrEmpty()) {
                                                        // courier should only be used with BO, but no BO code found
                                                        view?.renderCourierStateFailed(
                                                            shipmentGetCourierHolderData.itemPosition,
                                                            false,
                                                            false
                                                        )
                                                        view?.logOnErrorLoadCourier(
                                                            MessageErrorException("rates ui hidden but no promo"),
                                                            shipmentGetCourierHolderData.itemPosition,
                                                            boPromoCode
                                                        )
                                                        ratesQueue.remove()
                                                        itemToProcess = ratesQueue.peek()
                                                        continue@loopProcess
                                                    }
                                                    val shouldValidatePromo =
                                                        courierItemData.selectedShipper.logPromoCode != null && courierItemData.selectedShipper.logPromoCode!!.isNotEmpty()
                                                    if (!shouldValidatePromo) {
                                                        shippingCourierUiModel.isSelected = true
                                                        setShippingCourierViewModelsState(
                                                            shippingDurationUiModel.shippingCourierViewModelList,
                                                            shipmentGetCourierHolderData.shipmentCartItemModel.orderNumber
                                                        )
                                                        view?.renderCourierStateSuccess(
                                                            courierItemData,
                                                            shipmentGetCourierHolderData.itemPosition,
                                                            false
                                                        )
                                                        ratesQueue.remove()
                                                        itemToProcess = ratesQueue.peek()
                                                        continue@loopProcess
                                                    } else {
                                                        val validateUsePromoRequest =
                                                            generateValidateUsePromoRequest().copy()
                                                        for (ordersItem in validateUsePromoRequest.orders) {
                                                            if (ordersItem.cartStringGroup == shipmentGetCourierHolderData.shipmentCartItemModel.cartStringGroup) {
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
                                                                    shipmentGetCourierHolderData.shipmentCartItemModel.validationMetadata
                                                            }
                                                        }
                                                        removeInvalidBoCodeFromPromoRequest(
                                                            shipmentGetCourierHolderData,
                                                            validateUsePromoRequest
                                                        )
                                                        promoQueue.offer(
                                                            ShipmentValidatePromoHolderData(
                                                                validateUsePromoRequest,
                                                                shipmentGetCourierHolderData.itemPosition,
                                                                shipmentGetCourierHolderData.shipmentCartItemModel.cartStringGroup,
                                                                courierItemData.selectedShipper.logPromoCode!!,
                                                                courierItemData
                                                            )
                                                        )
                                                        awaitPromoQueue()
                                                        ratesQueue.remove()
                                                        itemToProcess = ratesQueue.peek()
                                                        continue@loopProcess
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                // corner case auto selection if BE default duration failed
                                if (shipmentGetCourierHolderData.shipmentCartItemModel.isAutoCourierSelection || shipmentGetCourierHolderData.shipmentCartItemModel.isDisableChangeCourier) {
                                    val shippingDuration =
                                        shippingRecommendationData.shippingDurationUiModels.firstOrNull { it.serviceData.error?.errorId.isNullOrEmpty() && it.serviceData.error?.errorMessage.isNullOrEmpty() }
                                    if (shippingDuration != null) {
                                        val shippingCourier =
                                            shippingDuration.shippingCourierViewModelList.firstOrNull {
                                                it.productData.error?.errorMessage.isNullOrEmpty()
                                            }
                                        if (shippingCourier != null) {
                                            val courierItemData =
                                                generateCourierItemDataWithScheduleDelivery(
                                                    false,
                                                    shipmentGetCourierHolderData.shipperId,
                                                    shipmentGetCourierHolderData.spId,
                                                    shipmentGetCourierHolderData.shipmentCartItemModel,
                                                    shippingCourier,
                                                    shippingRecommendationData
                                                )
                                            val shouldValidatePromo =
                                                courierItemData.selectedShipper.logPromoCode != null && courierItemData.selectedShipper.logPromoCode!!.isNotEmpty()
                                            if (!shouldValidatePromo) {
                                                shippingCourier.isSelected = true
                                                view?.renderCourierStateSuccess(
                                                    courierItemData,
                                                    shipmentGetCourierHolderData.itemPosition,
                                                    false
                                                )
                                                ratesQueue.remove()
                                                itemToProcess = ratesQueue.peek()
                                                continue@loopProcess
                                            } else {
                                                val validateUsePromoRequest =
                                                    generateValidateUsePromoRequest().copy()
                                                for (ordersItem in validateUsePromoRequest.orders) {
                                                    if (ordersItem.cartStringGroup == shipmentGetCourierHolderData.shipmentCartItemModel.cartStringGroup) {
                                                        if (!ordersItem.codes.contains(
                                                                courierItemData.selectedShipper.logPromoCode
                                                            )
                                                        ) {
                                                            ordersItem.codes.add(courierItemData.selectedShipper.logPromoCode!!)
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
                                                            shipmentGetCourierHolderData.shipmentCartItemModel.validationMetadata
                                                    }
                                                }
                                                removeInvalidBoCodeFromPromoRequest(
                                                    shipmentGetCourierHolderData,
                                                    validateUsePromoRequest
                                                )
                                                promoQueue.offer(
                                                    ShipmentValidatePromoHolderData(
                                                        validateUsePromoRequest,
                                                        shipmentGetCourierHolderData.itemPosition,
                                                        shipmentGetCourierHolderData.shipmentCartItemModel.cartStringGroup,
                                                        courierItemData.selectedShipper.logPromoCode!!,
                                                        courierItemData
                                                    )
                                                )
                                                awaitPromoQueue()
                                                ratesQueue.remove()
                                                itemToProcess = ratesQueue.peek()
                                                continue@loopProcess
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            errorReason = "rates empty data"
                        }
                        view?.renderCourierStateFailed(
                            shipmentGetCourierHolderData.itemPosition,
                            false,
                            false
                        )
                        view?.logOnErrorLoadCourier(
                            MessageErrorException(errorReason),
                            shipmentGetCourierHolderData.itemPosition,
                            boPromoCode
                        )
                        ratesQueue.remove()
                        itemToProcess = ratesQueue.peek()
                    } catch (t: Throwable) {
                        val exception = getActualThrowableForRx(t)
                        Timber.d(exception)
                        val boPromoCode = getBoPromoCode(
                            shipmentGetCourierHolderData.shipmentCartItemModel
                        )
                        view?.let { v ->
                            v.renderCourierStateFailed(
                                shipmentGetCourierHolderData.itemPosition,
                                false,
                                false
                            )
                            v.logOnErrorLoadCourier(
                                exception,
                                shipmentGetCourierHolderData.itemPosition,
                                boPromoCode
                            )
                            if (exception is AkamaiErrorException) {
                                v.showToastErrorAkamai(exception.message)
                            }
                        }
                        if (ratesQueue.isNotEmpty()) {
                            ratesQueue.remove()
                            itemToProcess = ratesQueue.peek()
                        }
                    }
                } else {
                    try {
                        val shippingRecommendationData = withContext(dispatchers.io) {
                            ratesUseCase.execute(shipmentGetCourierHolderData.ratesParam)
                                .map { shippingRecommendationData: ShippingRecommendationData ->
                                    stateConverter.fillState(
                                        shippingRecommendationData,
                                        shipmentGetCourierHolderData.shopShipmentList,
                                        shipmentGetCourierHolderData.spId,
                                        0
                                    )
                                }.toBlocking().single()
                        }
                        val boPromoCode = getBoPromoCode(
                            shipmentGetCourierHolderData.shipmentCartItemModel
                        )
                        var errorReason = "rates invalid data"
                        if (shipmentGetCourierHolderData.shipmentCartItemModel.shouldResetCourier) {
                            shipmentGetCourierHolderData.shipmentCartItemModel.shouldResetCourier =
                                false
                            error("racing condition against epharmacy validation")
                        }
                        if (shippingRecommendationData.shippingDurationUiModels.isNotEmpty()) {
                            if (isBoUnstackEnabled && shipmentGetCourierHolderData.shipmentCartItemModel.boCode.isNotEmpty()) {
                                val logisticPromo =
                                    shippingRecommendationData.listLogisticPromo.firstOrNull { it.promoCode == shipmentGetCourierHolderData.shipmentCartItemModel.boCode && !it.disabled }
                                if (logisticPromo != null) {
                                    for (shippingDurationUiModel in shippingRecommendationData.shippingDurationUiModels) {
                                        if (shippingDurationUiModel.shippingCourierViewModelList.isNotEmpty()) {
                                            for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                                shippingCourierUiModel.isSelected = false
                                            }
                                            for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                                if (shippingCourierUiModel.productData.shipperProductId == logisticPromo.shipperProductId && shippingCourierUiModel.productData.shipperId == logisticPromo.shipperId) {
                                                    if (!shippingCourierUiModel.productData.error?.errorMessage.isNullOrEmpty()) {
                                                        view?.renderCourierStateFailed(
                                                            shipmentGetCourierHolderData.itemPosition,
                                                            false,
                                                            false
                                                        )
                                                        view?.logOnErrorLoadCourier(
                                                            MessageErrorException(
                                                                shippingCourierUiModel.productData.error?.errorMessage
                                                            ),
                                                            shipmentGetCourierHolderData.itemPosition,
                                                            boPromoCode
                                                        )
                                                        ratesQueue.remove()
                                                        itemToProcess = ratesQueue.peek()
                                                        continue@loopProcess
                                                    } else {
                                                        val courierItemData =
                                                            generateCourierItemData(
                                                                false,
                                                                shipmentGetCourierHolderData.shipperId,
                                                                shipmentGetCourierHolderData.spId,
                                                                shipmentGetCourierHolderData.shipmentCartItemModel,
                                                                shippingCourierUiModel,
                                                                shippingRecommendationData,
                                                                logisticPromo
                                                            )
                                                        val validateUsePromoRequest =
                                                            generateValidateUsePromoRequest().copy()
                                                        for (ordersItem in validateUsePromoRequest.orders) {
                                                            if (ordersItem.cartStringGroup == shipmentGetCourierHolderData.shipmentCartItemModel.cartStringGroup) {
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
                                                                    shipmentGetCourierHolderData.shipmentCartItemModel.validationMetadata
                                                            }
                                                        }
                                                        removeInvalidBoCodeFromPromoRequest(
                                                            shipmentGetCourierHolderData,
                                                            validateUsePromoRequest
                                                        )
                                                        promoQueue.offer(
                                                            ShipmentValidatePromoHolderData(
                                                                validateUsePromoRequest,
                                                                shipmentGetCourierHolderData.itemPosition,
                                                                shipmentGetCourierHolderData.shipmentCartItemModel.cartStringGroup,
                                                                courierItemData.selectedShipper.logPromoCode!!,
                                                                courierItemData
                                                            )
                                                        )
                                                        awaitPromoQueue()
                                                        ratesQueue.remove()
                                                        itemToProcess = ratesQueue.peek()
                                                        continue@loopProcess
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    errorReason = "promo not matched"
                                }
                            } else {
                                for (shippingDurationUiModel in shippingRecommendationData.shippingDurationUiModels) {
                                    if (shippingDurationUiModel.shippingCourierViewModelList.isNotEmpty()) {
                                        for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                            shippingCourierUiModel.isSelected = false
                                        }
                                        val selectedSpId = getSelectedSpId(
                                            shipmentGetCourierHolderData.shipmentCartItemModel,
                                            shipmentGetCourierHolderData.spId,
                                            shippingDurationUiModel
                                        )
                                        for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                            if (shippingCourierUiModel.productData.shipperProductId == selectedSpId && !shippingCourierUiModel.serviceData.isUiRatesHidden) {
                                                if (!shippingCourierUiModel.productData.error?.errorMessage.isNullOrEmpty()) {
                                                    view?.renderCourierStateFailed(
                                                        shipmentGetCourierHolderData.itemPosition,
                                                        false,
                                                        false
                                                    )
                                                    view?.logOnErrorLoadCourier(
                                                        MessageErrorException(
                                                            shippingCourierUiModel.productData.error?.errorMessage
                                                        ),
                                                        shipmentGetCourierHolderData.itemPosition,
                                                        boPromoCode
                                                    )
                                                    ratesQueue.remove()
                                                    itemToProcess = ratesQueue.peek()
                                                    continue@loopProcess
                                                } else {
                                                    val courierItemData = generateCourierItemData(
                                                        false,
                                                        shipmentGetCourierHolderData.shipperId,
                                                        shipmentGetCourierHolderData.spId,
                                                        shipmentGetCourierHolderData.shipmentCartItemModel,
                                                        shippingCourierUiModel,
                                                        shippingRecommendationData
                                                    )
                                                    if (shippingCourierUiModel.productData.isUiRatesHidden && shippingCourierUiModel.serviceData.selectedShipperProductId == 0 && courierItemData.logPromoCode.isNullOrEmpty()) {
                                                        // courier should only be used with BO, but no BO code found
                                                        view?.renderCourierStateFailed(
                                                            shipmentGetCourierHolderData.itemPosition,
                                                            false,
                                                            false
                                                        )
                                                        view?.logOnErrorLoadCourier(
                                                            MessageErrorException("rates ui hidden but no promo"),
                                                            shipmentGetCourierHolderData.itemPosition,
                                                            boPromoCode
                                                        )
                                                        ratesQueue.remove()
                                                        itemToProcess = ratesQueue.peek()
                                                        continue@loopProcess
                                                    }
                                                    val shouldValidatePromo =
                                                        courierItemData.selectedShipper.logPromoCode != null && courierItemData.selectedShipper.logPromoCode!!.isNotEmpty()
                                                    if (!shouldValidatePromo) {
                                                        shippingCourierUiModel.isSelected = true
                                                        setShippingCourierViewModelsState(
                                                            shippingDurationUiModel.shippingCourierViewModelList,
                                                            shipmentGetCourierHolderData.shipmentCartItemModel.orderNumber
                                                        )
                                                        view?.renderCourierStateSuccess(
                                                            courierItemData,
                                                            shipmentGetCourierHolderData.itemPosition,
                                                            false
                                                        )
                                                        ratesQueue.remove()
                                                        itemToProcess = ratesQueue.peek()
                                                        continue@loopProcess
                                                    } else {
                                                        val validateUsePromoRequest =
                                                            generateValidateUsePromoRequest().copy()
                                                        for (ordersItem in validateUsePromoRequest.orders) {
                                                            if (ordersItem.cartStringGroup == shipmentGetCourierHolderData.shipmentCartItemModel.cartStringGroup) {
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
                                                                    shipmentGetCourierHolderData.shipmentCartItemModel.validationMetadata
                                                            }
                                                        }
                                                        removeInvalidBoCodeFromPromoRequest(
                                                            shipmentGetCourierHolderData,
                                                            validateUsePromoRequest
                                                        )
                                                        promoQueue.offer(
                                                            ShipmentValidatePromoHolderData(
                                                                validateUsePromoRequest,
                                                                shipmentGetCourierHolderData.itemPosition,
                                                                shipmentGetCourierHolderData.shipmentCartItemModel.cartStringGroup,
                                                                courierItemData.selectedShipper.logPromoCode!!,
                                                                courierItemData
                                                            )
                                                        )
                                                        awaitPromoQueue()
                                                        ratesQueue.remove()
                                                        itemToProcess = ratesQueue.peek()
                                                        continue@loopProcess
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                // corner case auto selection if BE default duration failed
                                if (shipmentGetCourierHolderData.shipmentCartItemModel.isAutoCourierSelection) {
                                    val shippingDuration =
                                        shippingRecommendationData.shippingDurationUiModels.firstOrNull { it.serviceData.error?.errorId.isNullOrEmpty() && it.serviceData.error?.errorMessage.isNullOrEmpty() }
                                    if (shippingDuration != null) {
                                        val shippingCourier =
                                            shippingDuration.shippingCourierViewModelList.firstOrNull {
                                                it.productData.error?.errorMessage.isNullOrEmpty()
                                            }
                                        if (shippingCourier != null) {
                                            val courierItemData = generateCourierItemData(
                                                false,
                                                shipmentGetCourierHolderData.shipperId,
                                                shipmentGetCourierHolderData.spId,
                                                shipmentGetCourierHolderData.shipmentCartItemModel,
                                                shippingCourier,
                                                shippingRecommendationData
                                            )
                                            val shouldValidatePromo =
                                                courierItemData.selectedShipper.logPromoCode != null && courierItemData.selectedShipper.logPromoCode!!.isNotEmpty()
                                            if (!shouldValidatePromo) {
                                                shippingCourier.isSelected = true
                                                view?.renderCourierStateSuccess(
                                                    courierItemData,
                                                    shipmentGetCourierHolderData.itemPosition,
                                                    false
                                                )
                                                ratesQueue.remove()
                                                itemToProcess = ratesQueue.peek()
                                                continue@loopProcess
                                            } else {
                                                val validateUsePromoRequest =
                                                    generateValidateUsePromoRequest().copy()
                                                for (ordersItem in validateUsePromoRequest.orders) {
                                                    if (ordersItem.cartStringGroup == shipmentGetCourierHolderData.shipmentCartItemModel.cartStringGroup) {
                                                        if (!ordersItem.codes.contains(
                                                                courierItemData.selectedShipper.logPromoCode
                                                            )
                                                        ) {
                                                            ordersItem.codes.add(courierItemData.selectedShipper.logPromoCode!!)
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
                                                            shipmentGetCourierHolderData.shipmentCartItemModel.validationMetadata
                                                    }
                                                }
                                                removeInvalidBoCodeFromPromoRequest(
                                                    shipmentGetCourierHolderData,
                                                    validateUsePromoRequest
                                                )
                                                promoQueue.offer(
                                                    ShipmentValidatePromoHolderData(
                                                        validateUsePromoRequest,
                                                        shipmentGetCourierHolderData.itemPosition,
                                                        shipmentGetCourierHolderData.shipmentCartItemModel.cartStringGroup,
                                                        courierItemData.selectedShipper.logPromoCode!!,
                                                        courierItemData
                                                    )
                                                )
                                                awaitPromoQueue()
                                                ratesQueue.remove()
                                                itemToProcess = ratesQueue.peek()
                                                continue@loopProcess
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            errorReason = "rates empty data"
                        }
                        view?.renderCourierStateFailed(
                            shipmentGetCourierHolderData.itemPosition,
                            false,
                            false
                        )
                        view?.logOnErrorLoadCourier(
                            MessageErrorException(errorReason),
                            shipmentGetCourierHolderData.itemPosition,
                            boPromoCode
                        )
                        ratesQueue.remove()
                        itemToProcess = ratesQueue.peek()
                    } catch (t: Throwable) {
                        val exception = getActualThrowableForRx(t)
                        Timber.d(exception)
                        val boPromoCode = getBoPromoCode(
                            shipmentGetCourierHolderData.shipmentCartItemModel
                        )
                        view?.let { v ->
                            v.renderCourierStateFailed(
                                shipmentGetCourierHolderData.itemPosition,
                                false,
                                false
                            )
                            v.logOnErrorLoadCourier(
                                exception,
                                shipmentGetCourierHolderData.itemPosition,
                                boPromoCode
                            )
                            if (exception is AkamaiErrorException) {
                                v.showToastErrorAkamai(exception.message)
                            }
                        }
                        if (ratesQueue.isNotEmpty()) {
                            ratesQueue.remove()
                            itemToProcess = ratesQueue.peek()
                        }
                    }
                }
            }
            updateShipmentButtonPaymentModel(loading = false)
        }
    }

    private fun generateCourierItemDataWithScheduleDelivery(
        isForceReloadRates: Boolean,
        shipperId: Int,
        spId: Int,
        shipmentCartItemModel: ShipmentCartItemModel,
        shippingCourierUiModel: ShippingCourierUiModel,
        shippingRecommendationData: ShippingRecommendationData,
        logisticPromo: LogisticPromoUiModel? = null
    ): CourierItemData {
        var courierItemData =
            shippingCourierConverter.convertToCourierItemData(
                shippingCourierUiModel,
                shippingRecommendationData,
                shipmentCartItemModel
            )

        // Auto apply Promo Stacking Logistic
        var logisticPromoChosen = logisticPromo
        if (shipmentCartItemModel.isDisableChangeCourier) {
            // set error log
            shippingRecommendationData.listLogisticPromo.firstOrNull()?.let {
                courierItemData.logPromoMsg = it.disableText
                courierItemData.logPromoDesc = it.description
            }
            // must get promo for tokonow
            logisticPromoChosen = shippingRecommendationData.listLogisticPromo.firstOrNull {
                it.promoCode.isNotEmpty() && !it.disabled
            }
        } else if (isForceReloadRates) {
            logisticPromoChosen = shippingRecommendationData.listLogisticPromo.firstOrNull {
                !it.disabled && it.isApplied
            }
        } else if (!isBoUnstackEnabled) {
            logisticPromoChosen = shippingRecommendationData.listLogisticPromo.firstOrNull {
                !it.disabled && it.shipperId == shipperId && it.shipperProductId == spId && it.promoCode.isNotEmpty()
            }
        }
        if (logisticPromoChosen?.shipperProductId != null && logisticPromoChosen.shipperProductId != courierItemData.shipperProductId) {
            val courierUiModel = shippingRecommendationData.shippingDurationUiModels.first {
                it.serviceData.serviceId == logisticPromoChosen.serviceId
            }.shippingCourierViewModelList.first {
                it.productData.shipperProductId == logisticPromoChosen.shipperProductId
            }
            courierItemData = shippingCourierConverter.convertToCourierItemData(
                courierUiModel,
                shippingRecommendationData,
                shipmentCartItemModel
            )
        }

        handleSyncShipmentCartItemModel(courierItemData, shipmentCartItemModel)

        logisticPromoChosen?.let {
            courierItemData.logPromoCode = it.promoCode
            courierItemData.discountedRate = it.discountedRate
            courierItemData.shippingRate = it.shippingRate
            courierItemData.benefitAmount = it.benefitAmount
            courierItemData.promoTitle = it.title
            courierItemData.isHideShipperName = it.hideShipperName
            courierItemData.shipperName = it.shipperName
            courierItemData.etaText = it.etaData.textEta
            courierItemData.etaErrorCode = it.etaData.errorCode
            courierItemData.freeShippingChosenCourierTitle = it.freeShippingChosenCourierTitle
            courierItemData.freeShippingMetadata = it.freeShippingMetadata
            courierItemData.benefitClass = it.benefitClass
            courierItemData.shippingSubsidy = it.shippingSubsidy
            courierItemData.boCampaignId = it.boCampaignId
        }
        return courierItemData
    }

    private fun handleSyncShipmentCartItemModel(
        courierItemData: CourierItemData,
        selectedShipmentCartItemModel: ShipmentCartItemModel
    ) {
        if (courierItemData.scheduleDeliveryUiModel != null) {
            val isScheduleDeliverySelected = courierItemData.scheduleDeliveryUiModel?.isSelected
            if (isScheduleDeliverySelected == true &&
                (
                    courierItemData.scheduleDeliveryUiModel?.timeslotId != selectedShipmentCartItemModel.timeslotId ||
                        courierItemData.scheduleDeliveryUiModel?.scheduleDate != selectedShipmentCartItemModel.scheduleDate
                    )
            ) {
                selectedShipmentCartItemModel.scheduleDate =
                    courierItemData.scheduleDeliveryUiModel?.scheduleDate ?: ""
                selectedShipmentCartItemModel.timeslotId =
                    courierItemData.scheduleDeliveryUiModel?.timeslotId ?: 0
                selectedShipmentCartItemModel.validationMetadata =
                    courierItemData.scheduleDeliveryUiModel?.deliveryProduct?.validationMetadata
                        ?: ""
            } else if (isScheduleDeliverySelected == false) {
                selectedShipmentCartItemModel.scheduleDate = ""
                selectedShipmentCartItemModel.timeslotId = 0L
                selectedShipmentCartItemModel.validationMetadata = ""
            }
        }
    }

    private fun getSelectedSpId(
        shipmentCartItemModel: ShipmentCartItemModel,
        spId: Int,
        shippingDurationUiModel: ShippingDurationUiModel
    ): Int {
        val currentServiceId =
            shipmentCartItemModel.selectedShipmentDetailData?.selectedCourier?.serviceId
        return if (currentServiceId != null &&
            currentServiceId > 0 &&
            shippingDurationUiModel.serviceData.serviceId == currentServiceId &&
            shippingDurationUiModel.serviceData.selectedShipperProductId > 0
        ) {
            shippingDurationUiModel.serviceData.selectedShipperProductId
        } else {
            return spId
        }
    }

    private fun generateCourierItemData(
        isForceReloadRates: Boolean,
        shipperId: Int,
        spId: Int,
        shipmentCartItemModel: ShipmentCartItemModel,
        shippingCourierUiModel: ShippingCourierUiModel,
        shippingRecommendationData: ShippingRecommendationData,
        logisticPromo: LogisticPromoUiModel? = null
    ): CourierItemData {
        var courierItemData =
            shippingCourierConverter.convertToCourierItemData(
                shippingCourierUiModel,
                shippingRecommendationData
            )

        // Auto apply Promo Stacking Logistic
        var logisticPromoChosen = logisticPromo
        if (shipmentCartItemModel.isDisableChangeCourier) {
            // set error log
            shippingRecommendationData.listLogisticPromo.firstOrNull()?.let {
                courierItemData.logPromoMsg = it.disableText
                courierItemData.logPromoDesc = it.description
            }
            // must get promo for tokonow
            logisticPromoChosen = shippingRecommendationData.listLogisticPromo.firstOrNull {
                it.promoCode.isNotEmpty() && !it.disabled
            }
        } else if (isForceReloadRates) {
            logisticPromoChosen = shippingRecommendationData.listLogisticPromo.firstOrNull {
                !it.disabled && it.isApplied
            }
        } else if (!isBoUnstackEnabled) {
            logisticPromoChosen = shippingRecommendationData.listLogisticPromo.firstOrNull {
                !it.disabled && it.shipperId == shipperId && it.shipperProductId == spId && it.promoCode.isNotEmpty()
            }
        }
        if (logisticPromoChosen?.shipperProductId != null && logisticPromoChosen.shipperProductId != courierItemData.shipperProductId) {
            val courierUiModel = shippingRecommendationData.shippingDurationUiModels.first {
                it.serviceData.serviceId == logisticPromoChosen.serviceId
            }.shippingCourierViewModelList.first {
                it.productData.shipperProductId == logisticPromoChosen.shipperProductId
            }
            courierItemData = shippingCourierConverter.convertToCourierItemData(
                courierUiModel,
                shippingRecommendationData
            )
        }
        logisticPromoChosen?.let {
            courierItemData.logPromoCode = it.promoCode
            courierItemData.discountedRate = it.discountedRate
            courierItemData.shippingRate = it.shippingRate
            courierItemData.benefitAmount = it.benefitAmount
            courierItemData.promoTitle = it.title
            courierItemData.isHideShipperName = it.hideShipperName
            courierItemData.shipperName = it.shipperName
            courierItemData.etaText = it.etaData.textEta
            courierItemData.etaErrorCode = it.etaData.errorCode
            courierItemData.freeShippingChosenCourierTitle = it.freeShippingChosenCourierTitle
            courierItemData.freeShippingMetadata = it.freeShippingMetadata
            courierItemData.benefitClass = it.benefitClass
            courierItemData.shippingSubsidy = it.shippingSubsidy
            courierItemData.boCampaignId = it.boCampaignId
        }
        return courierItemData
    }

    private fun getBoPromoCode(
        shipmentCartItemModel: ShipmentCartItemModel
    ): String {
        if (isBoUnstackEnabled) {
            return shipmentCartItemModel.boCode
        }
        return ""
    }

    fun generateRatesMvcParam(cartString: String?): String {
        var mvc = ""
        val tmpMvcShippingBenefitUiModel: MutableList<MvcShippingBenefitUiModel> = ArrayList()
        val promoSpIdUiModels = if (validateUsePromoRevampUiModel != null) {
            validateUsePromoRevampUiModel!!.promoUiModel.additionalInfoUiModel.promoSpIds
        } else {
            lastApplyData.value.additionalInfo.promoSpIds
        }
        if (promoSpIdUiModels.isNotEmpty()) {
            for ((uniqueId, mvcShippingBenefits) in promoSpIdUiModels) {
                if (cartString == uniqueId) {
                    tmpMvcShippingBenefitUiModel.addAll(mvcShippingBenefits)
                }
            }
        }
        if (tmpMvcShippingBenefitUiModel.size > 0) {
            mvc = gson.toJson(tmpMvcShippingBenefitUiModel)
        }
        return mvc.replace("\n", "").replace(" ", "")
    }

    private fun getShippingParam(
        shipmentDetailData: ShipmentDetailData?,
        products: List<Product>?,
        cartString: String?,
        isTradeInDropOff: Boolean,
        recipientAddressModel: RecipientAddressModel?
    ): ShippingParam {
        val shippingParam = ShippingParam(
            originDistrictId = shipmentDetailData!!.shipmentCartData!!.originDistrictId,
            originPostalCode = shipmentDetailData.shipmentCartData!!.originPostalCode,
            originLatitude = shipmentDetailData.shipmentCartData!!.originLatitude,
            originLongitude = shipmentDetailData.shipmentCartData!!.originLongitude,
            weightInKilograms = shipmentDetailData.shipmentCartData!!.weight / 1000,
            weightActualInKilograms = shipmentDetailData.shipmentCartData!!.weightActual / 1000,
            shopId = shipmentDetailData.shopId,
            shopTier = shipmentDetailData.shipmentCartData!!.shopTier,
            token = shipmentDetailData.shipmentCartData!!.token,
            ut = shipmentDetailData.shipmentCartData!!.ut,
            insurance = shipmentDetailData.shipmentCartData!!.insurance,
            productInsurance = shipmentDetailData.shipmentCartData!!.productInsurance,
            orderValue = shipmentDetailData.shipmentCartData!!.orderValue,
            categoryIds = shipmentDetailData.shipmentCartData!!.categoryIds,
            isBlackbox = shipmentDetailData.isBlackbox,
            isPreorder = shipmentDetailData.preorder,
            addressId = recipientAddressModel!!.id,
            isTradein = shipmentDetailData.isTradein,
            products = products,
            uniqueId = cartString,
            isTradeInDropOff = isTradeInDropOff,
            preOrderDuration = shipmentDetailData.shipmentCartData!!.preOrderDuration,
            isFulfillment = shipmentDetailData.shipmentCartData!!.isFulfillment,
            boMetadata = shipmentDetailData.shipmentCartData!!.boMetadata
        )
        if (isTradeInDropOff && recipientAddressModel.locationDataModel != null) {
            shippingParam.destinationDistrictId = recipientAddressModel.locationDataModel.district
            shippingParam.destinationPostalCode = recipientAddressModel.locationDataModel.postalCode
            shippingParam.destinationLatitude = recipientAddressModel.locationDataModel.latitude
            shippingParam.destinationLongitude = recipientAddressModel.locationDataModel.longitude
        } else {
            shippingParam.destinationDistrictId =
                shipmentDetailData.shipmentCartData!!.destinationDistrictId
            shippingParam.destinationPostalCode =
                shipmentDetailData.shipmentCartData!!.destinationPostalCode
            shippingParam.destinationLatitude =
                shipmentDetailData.shipmentCartData!!.destinationLatitude
            shippingParam.destinationLongitude =
                shipmentDetailData.shipmentCartData!!.destinationLongitude
        }
        shippingParam.groupType = shipmentDetailData.shipmentCartData!!.groupType
        return shippingParam
    }

    fun getShippingCourierViewModelsState(orderNumber: Int): List<ShippingCourierUiModel>? {
        return shippingCourierViewModelsState[orderNumber]
    }

    fun setShippingCourierViewModelsState(
        shippingCourierUiModelsState: List<ShippingCourierUiModel>,
        orderNumber: Int
    ) {
        shippingCourierViewModelsState[orderNumber] = shippingCourierUiModelsState
    }

    internal fun processBoPromoCourierRecommendationNew(
        listToProcess: List<Triple<Int, PromoCheckoutVoucherOrdersItemUiModel, ShipmentCartItemModel>>
    ) {
        viewModelScope.launch(dispatchers.immediate) {
            updateShipmentButtonPaymentModel(loading = true)
            loopProcess@ for ((
                itemPosition: Int,
                voucherOrdersItemUiModel: PromoCheckoutVoucherOrdersItemUiModel,
                shipmentCartItemModel: ShipmentCartItemModel
            ) in listToProcess) {
                if (!this.isActive) {
                    return@launch
                }
                val selectedShipmentDetailData =
                    view?.getShipmentDetailData(shipmentCartItemModel, recipientAddressModel)
                val products = getProductForRatesRequest(shipmentCartItemModel)
                val cartString = shipmentCartItemModel.cartStringGroup
                val isTradeInDropOff = view?.isTradeInByDropOff ?: false
                val shippingParam = getShippingParam(
                    selectedShipmentDetailData,
                    products,
                    cartString,
                    isTradeInDropOff,
                    recipientAddressModel
                )
                val counter = if (codData == null) -1 else codData!!.counterCod
                val cornerId = recipientAddressModel.isCornerAddress
                val pslCode = voucherOrdersItemUiModel.code
                val isLeasing = shipmentCartItemModel.isLeasingProduct
                val mvc = generateRatesMvcParam(cartString)
                val shopShipmentList = shipmentCartItemModel.shopShipmentList
                val ratesParamBuilder = RatesParam.Builder(
                    shopShipmentList,
                    shippingParam
                )
                    .isCorner(cornerId)
                    .codHistory(counter)
                    .isLeasing(isLeasing)
                    .promoCode(pslCode)
                    .cartData(cartDataForRates)
                    .warehouseId(shipmentCartItemModel.fulfillmentId.toString())
                    .mvc(mvc)
                val param = ratesParamBuilder.build()
                val promoCode = voucherOrdersItemUiModel.code
                val shippingId = voucherOrdersItemUiModel.shippingId
                val spId = voucherOrdersItemUiModel.spId
                view?.setStateLoadingCourierStateAtIndex(itemPosition, true)
                try {
                    val shippingRecommendationData = withContext(dispatchers.io) {
                        if (isTradeInDropOff) {
                            ratesApiUseCase.execute(param)
                                .map { shippingRecommendationData: ShippingRecommendationData ->
                                    stateConverter.fillState(
                                        shippingRecommendationData,
                                        shopShipmentList,
                                        spId,
                                        0
                                    )
                                }.toBlocking().single()
                        } else {
                            ratesUseCase.execute(param)
                                .map { shippingRecommendationData: ShippingRecommendationData ->
                                    stateConverter.fillState(
                                        shippingRecommendationData,
                                        shopShipmentList,
                                        spId,
                                        0
                                    )
                                }.toBlocking().single()
                        }
                    }
                    var errorReason = "rates invalid data"
                    if (shippingRecommendationData.shippingDurationUiModels.isNotEmpty() && shippingRecommendationData.listLogisticPromo.isNotEmpty()) {
                        val logisticPromo =
                            shippingRecommendationData.listLogisticPromo.firstOrNull { it.promoCode == promoCode && !it.disabled }
                        if (logisticPromo != null) {
                            for (shippingDurationUiModel in shippingRecommendationData.shippingDurationUiModels) {
                                if (shippingDurationUiModel.shippingCourierViewModelList.isNotEmpty()) {
                                    for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                        shippingCourierUiModel.isSelected = false
                                    }
                                    for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                        if (isTradeInDropOff || shippingCourierUiModel.productData.shipperProductId == spId && shippingCourierUiModel.productData.shipperId == shippingId) {
                                            if (!shippingCourierUiModel.productData.error?.errorMessage.isNullOrEmpty()) {
                                                cancelAutoApplyPromoStackLogistic(
                                                    itemPosition,
                                                    promoCode,
                                                    cartString,
                                                    voucherOrdersItemUiModel.uniqueId,
                                                    shipmentCartItemModel
                                                )
                                                clearOrderPromoCodeFromLastValidateUseRequest(
                                                    cartString,
                                                    promoCode
                                                )
                                                view?.let { v ->
                                                    v.resetCourier(shipmentCartItemModel)
                                                    v.renderCourierStateFailed(
                                                        itemPosition,
                                                        isTradeInDropOff,
                                                        true
                                                    )
                                                    v.logOnErrorLoadCourier(
                                                        MessageErrorException(
                                                            shippingCourierUiModel.productData.error?.errorMessage
                                                        ),
                                                        itemPosition,
                                                        promoCode
                                                    )
                                                }
                                                continue@loopProcess
                                            } else {
                                                shippingCourierUiModel.isSelected = true
                                                setShippingCourierViewModelsState(
                                                    shippingDurationUiModel.shippingCourierViewModelList,
                                                    shipmentCartItemModel.orderNumber
                                                )
                                                val courierItemData =
                                                    generateCourierItemDataWithLogisticPromo(
                                                        shippingCourierUiModel,
                                                        shippingRecommendationData,
                                                        logisticPromo
                                                    )
                                                val validateUsePromoRequest =
                                                    generateValidateUsePromoRequest().copy()
                                                for (ordersItem in validateUsePromoRequest.orders) {
                                                    if (ordersItem.cartStringGroup == shipmentCartItemModel.cartStringGroup) {
                                                        if (!ordersItem.codes.contains(
                                                                courierItemData.selectedShipper.logPromoCode
                                                            )
                                                        ) {
                                                            ordersItem.codes.add(courierItemData.selectedShipper.logPromoCode!!)
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
                                                            shipmentCartItemModel.validationMetadata
                                                    }
                                                }
                                                val shipmentCartItemModelLists =
                                                    shipmentCartItemModelList.filterIsInstance(
                                                        ShipmentCartItemModel::class.java
                                                    )
                                                if (shipmentCartItemModelLists.isNotEmpty() && !shipmentCartItemModel.isFreeShippingPlus) {
                                                    for (tmpShipmentCartItemModel in shipmentCartItemModelLists) {
                                                        for (order in validateUsePromoRequest.orders) {
                                                            if (shipmentCartItemModel.cartStringGroup != tmpShipmentCartItemModel.cartStringGroup && tmpShipmentCartItemModel.cartStringGroup == order.cartStringGroup && tmpShipmentCartItemModel.selectedShipmentDetailData != null && tmpShipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier != null &&
                                                                !tmpShipmentCartItemModel.isFreeShippingPlus
                                                            ) {
                                                                order.codes.remove(
                                                                    tmpShipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.logPromoCode
                                                                )
                                                                order.boCode = ""
                                                            }
                                                        }
                                                    }
                                                }
                                                promoQueue.offer(
                                                    ShipmentValidatePromoHolderData(
                                                        validateUsePromoRequest,
                                                        itemPosition,
                                                        cartString,
                                                        promoCode,
                                                        courierItemData
                                                    )
                                                )
                                                awaitPromoQueue()
                                                continue@loopProcess
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            errorReason = "promo not found"
                        }
                    } else {
                        errorReason = "rates empty data"
                    }
                    throw MessageErrorException(errorReason)
                } catch (t: Throwable) {
                    val exception = getActualThrowableForRx(t)
                    cancelAutoApplyPromoStackLogistic(
                        itemPosition,
                        promoCode,
                        cartString,
                        voucherOrdersItemUiModel.uniqueId,
                        shipmentCartItemModel
                    )
                    clearOrderPromoCodeFromLastValidateUseRequest(cartString, promoCode)
                    view?.let { v ->
                        v.resetCourier(shipmentCartItemModel)
                        v.renderCourierStateFailed(itemPosition, isTradeInDropOff, true)
                        v.logOnErrorLoadCourier(exception, itemPosition, promoCode)
                        if (exception is AkamaiErrorException) {
                            v.showToastErrorAkamai(exception.message)
                        }
                    }
                }
            }
            updateShipmentButtonPaymentModel(loading = false)
        }
    }

    private fun getActualThrowableForRx(t: Throwable) = t.cause?.cause ?: t.cause ?: t

    private fun generateCourierItemDataWithLogisticPromo(
        shippingCourierUiModel: ShippingCourierUiModel,
        shippingRecommendationData: ShippingRecommendationData,
        logisticPromoUiModel: LogisticPromoUiModel
    ): CourierItemData {
        val courierItemData =
            shippingCourierConverter.convertToCourierItemData(
                shippingCourierUiModel,
                shippingRecommendationData
            )

        courierItemData.apply {
            logPromoMsg = logisticPromoUiModel.disableText
            logPromoDesc = logisticPromoUiModel.description
            logPromoCode = logisticPromoUiModel.promoCode
            discountedRate = logisticPromoUiModel.discountedRate
            shippingRate = logisticPromoUiModel.shippingRate
            benefitAmount = logisticPromoUiModel.benefitAmount
            promoTitle = logisticPromoUiModel.title
            isHideShipperName = logisticPromoUiModel.hideShipperName
            shipperName = logisticPromoUiModel.shipperName
            etaText = logisticPromoUiModel.etaData.textEta
            etaErrorCode = logisticPromoUiModel.etaData.errorCode
            freeShippingChosenCourierTitle = logisticPromoUiModel.freeShippingChosenCourierTitle
            freeShippingMetadata = logisticPromoUiModel.freeShippingMetadata
            benefitClass = logisticPromoUiModel.benefitClass
            shippingSubsidy = logisticPromoUiModel.shippingSubsidy
            boCampaignId = logisticPromoUiModel.boCampaignId
        }
        return courierItemData
    }

    fun getProductForRatesRequest(shipmentCartItemModel: ShipmentCartItemModel?): ArrayList<Product> {
        val products = ArrayList<Product>()
        if (shipmentCartItemModel != null) {
            for (cartItemModel in shipmentCartItemModel.cartItemModels) {
                if (!cartItemModel.isError) {
                    val product = Product()
                    product.productId = cartItemModel.productId
                    product.isFreeShipping = cartItemModel.isFreeShipping
                    product.isFreeShippingTc = cartItemModel.isFreeShippingExtra
                    products.add(product)
                }
            }
        }
        return products
    }
    // endregion

    // region save shipment
    fun processSaveShipmentState(shipmentCartItemModel: ShipmentCartItemModel) {
        viewModelScope.launch(dispatchers.immediate) {
            try {
                val params =
                    generateSaveShipmentStateRequestSingleAddress(listOf(shipmentCartItemModel))
                if (params.requestDataList.first().shopProductDataList.isNotEmpty()) {
                    saveShipmentStateGqlUseCase(params)
                }
            } catch (e: Throwable) {
                Timber.d(e)
            }
        }
    }

    fun processSaveShipmentState() {
        viewModelScope.launch(dispatchers.immediate) {
            try {
                val params = generateSaveShipmentStateRequestSingleAddress(
                    shipmentCartItemModelList.filterIsInstance(ShipmentCartItemModel::class.java)
                )
                if (params.requestDataList.first().shopProductDataList.isNotEmpty()) {
                    saveShipmentStateGqlUseCase(params)
                }
            } catch (e: Throwable) {
                Timber.d(e)
            }
        }
    }

    private fun generateSaveShipmentStateRequestSingleAddress(shipmentCartItemModels: List<ShipmentCartItemModel>): SaveShipmentStateRequest {
        val shipmentStateShopProductDataList: MutableList<ShipmentStateShopProductData> =
            ArrayList()
        val shipmentStateRequestDataList: MutableList<ShipmentStateRequestData> = ArrayList()
        for (shipmentCartItemModel in shipmentCartItemModels) {
            setSaveShipmentStateData(shipmentCartItemModel, shipmentStateShopProductDataList)
        }
        val shipmentStateRequestData = ShipmentStateRequestData()
        shipmentStateRequestData.addressId = recipientAddressModel.id
        shipmentStateRequestData.shopProductDataList = shipmentStateShopProductDataList
        shipmentStateRequestDataList.add(shipmentStateRequestData)
        return SaveShipmentStateRequest(shipmentStateRequestDataList)
    }

    private fun setSaveShipmentStateData(
        shipmentCartItemModel: ShipmentCartItemModel,
        shipmentStateShopProductDataList: MutableList<ShipmentStateShopProductData>
    ) {
        var courierData: CourierItemData? = null
        if (shipmentCartItemModel.selectedShipmentDetailData != null) {
            courierData = if (view!!.isTradeInByDropOff) {
                shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff
            } else {
                shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier
            }
        }
        if (courierData != null) {
            val shipmentStateProductDataList: MutableList<ShipmentStateProductData> = ArrayList()
            for (cartItemModel in shipmentCartItemModel.cartItemModels) {
                val shipmentStateProductData = ShipmentStateProductData()
                shipmentStateProductData.shopId = cartItemModel.shopId.toLongOrZero()
                shipmentStateProductData.productId = cartItemModel.productId
                if (cartItemModel.isPreOrder) {
                    val shipmentStateProductPreorder = ShipmentStateProductPreorder()
                    shipmentStateProductPreorder.durationDay = cartItemModel.preOrderDurationDay
                    shipmentStateProductData.productPreorder = shipmentStateProductPreorder
                }
                shipmentStateProductDataList.add(shipmentStateProductData)
            }
            val shipmentStateDropshipData = ShipmentStateDropshipData()
            shipmentStateDropshipData.name =
                shipmentCartItemModel.selectedShipmentDetailData!!.dropshipperName ?: ""
            shipmentStateDropshipData.telpNo =
                shipmentCartItemModel.selectedShipmentDetailData!!.dropshipperPhone ?: ""
            val ratesFeature = generateRatesFeature(courierData)
            val shipmentStateShippingInfoData = ShipmentStateShippingInfoData()
            shipmentStateShippingInfoData.shippingId =
                courierData.selectedShipper.shipperId.toLong()
            shipmentStateShippingInfoData.spId =
                courierData.selectedShipper.shipperProductId.toLong()
            shipmentStateShippingInfoData.ratesFeature = ratesFeature
            val shipmentStateShopProductData = ShipmentStateShopProductData()
            shipmentStateShopProductData.cartStringGroup = shipmentCartItemModel.cartStringGroup
            shipmentStateShopProductData.shopId = shipmentCartItemModel.shopId
            shipmentStateShopProductData.finsurance =
                if (shipmentCartItemModel.selectedShipmentDetailData!!.useInsurance != null &&
                    shipmentCartItemModel.selectedShipmentDetailData!!.useInsurance!!
                ) {
                    1
                } else {
                    0
                }
            shipmentStateShopProductData.isDropship =
                if (shipmentCartItemModel.selectedShipmentDetailData!!.useDropshipper != null &&
                    shipmentCartItemModel.selectedShipmentDetailData!!.useDropshipper!!
                ) {
                    1
                } else {
                    0
                }
            shipmentStateShopProductData.isOrderPriority =
                if (shipmentCartItemModel.selectedShipmentDetailData!!.isOrderPriority != null &&
                    shipmentCartItemModel.selectedShipmentDetailData!!.isOrderPriority!!
                ) {
                    1
                } else {
                    0
                }
            shipmentStateShopProductData.isPreorder =
                if (shipmentCartItemModel.isProductIsPreorder) 1 else 0
            shipmentStateShopProductData.warehouseId = shipmentCartItemModel.fulfillmentId
            shipmentStateShopProductData.dropshipData = shipmentStateDropshipData
            shipmentStateShopProductData.shippingInfoData = shipmentStateShippingInfoData
            shipmentStateShopProductData.productDataList = shipmentStateProductDataList
            shipmentStateShopProductData.validationMetadata =
                shipmentCartItemModel.validationMetadata
            shipmentStateShopProductDataList.add(shipmentStateShopProductData)
        }
    }
    // endregion

    // region edit address pinpoint
    fun editAddressPinpoint(
        latitude: String,
        longitude: String,
        locationPass: LocationPass?
    ) {
        view?.let {
            it.showLoading()
            it.setHasRunningApiCall(true)
            val requestParams = generateUpdatePinpointParam(latitude, longitude)
            viewModelScope.launch(dispatchers.immediate) {
                try {
                    val response = updatePinpointUseCase(requestParams)
                    it.setHasRunningApiCall(false)
                    it.hideLoading()
                    val statusSuccess = response.keroEditAddress.data.isSuccess == 1
                    if (statusSuccess) {
                        recipientAddressModel.latitude = latitude
                        recipientAddressModel.longitude = longitude
                        it.renderEditAddressSuccess(latitude, longitude)
                    } else {
                        val messageError =
                            it.getStringResource(com.tokopedia.abstraction.R.string.default_request_error_unknown)

                        it.navigateToSetPinpoint(messageError, locationPass)
                    }
                } catch (t: Throwable) {
                    it.setHasRunningApiCall(false)
                    it.hideLoading()
                    it.showToastError(
                        getErrorMessage(it.activity, t)
                    )
                }
            }
        }
    }

    private fun generateUpdatePinpointParam(
        addressLatitude: String,
        addressLongitude: String
    ): UpdatePinpointParam {
        val params = EditPinpointParam(
            addressId = recipientAddressModel.id.toLongOrZero(),
            addressName = recipientAddressModel.addressName,
            address1 = recipientAddressModel.street,
            postalCode = recipientAddressModel.postalCode,
            district = recipientAddressModel.destinationDistrictId,
            city = recipientAddressModel.cityId,
            province = recipientAddressModel.provinceId,
            address2 = "$addressLatitude, $addressLongitude",
            receiverName = recipientAddressModel.recipientName,
            phone = recipientAddressModel.recipientPhoneNumber
        )

        val requestParams = UpdatePinpointParam(input = params)
        return requestParams
    }
// endregion

// region change address
    fun changeShippingAddress(
        newRecipientAddressModel: RecipientAddressModel?,
        chosenAddressModel: ChosenAddressModel?,
        isOneClickShipment: Boolean,
        isTradeInDropOff: Boolean,
        isHandleFallback: Boolean,
        reloadCheckoutPage: Boolean
    ) {
        view?.showLoading()
        view?.setHasRunningApiCall(true)
        val dataChangeAddressRequests: MutableList<DataChangeAddressRequest> = ArrayList()
        for (shipmentCartItemModel in shipmentCartItemModelList) {
            if (shipmentCartItemModel is ShipmentCartItemModel) {
                for (cartItemModel in shipmentCartItemModel.cartItemModels) {
                    val dataChangeAddressRequest = DataChangeAddressRequest()
                    dataChangeAddressRequest.quantity = cartItemModel.quantity
                    dataChangeAddressRequest.productId = cartItemModel.productId
                    dataChangeAddressRequest.notes = cartItemModel.noteToSeller
                    dataChangeAddressRequest.cartIdStr = cartItemModel.cartId.toString()
                    if (newRecipientAddressModel != null) {
                        if (isTradeInDropOff) {
                            dataChangeAddressRequest.addressId =
                                newRecipientAddressModel.locationDataModel.addrId
                            dataChangeAddressRequest.isIndomaret = true
                        } else {
                            dataChangeAddressRequest.addressId = newRecipientAddressModel.id
                            dataChangeAddressRequest.isIndomaret = false
                        }
                    }
                    if (chosenAddressModel != null) {
                        dataChangeAddressRequest.addressId = chosenAddressModel.addressId.toString()
                    }
                    dataChangeAddressRequests.add(dataChangeAddressRequest)
                }
            }
        }
        val params: MutableMap<String, Any> = HashMap()
        params[ChangeShippingAddressGqlUseCase.PARAM_CARTS] = dataChangeAddressRequests
        params[ChangeShippingAddressGqlUseCase.PARAM_ONE_CLICK_SHIPMENT] = isOneClickShipment
        val requestParam = RequestParams.create()
        requestParam.putObject(
            ChangeShippingAddressGqlUseCase.CHANGE_SHIPPING_ADDRESS_PARAMS,
            params
        )
        viewModelScope.launch(dispatchers.immediate) {
            try {
                val setShippingAddressData = changeShippingAddressGqlUseCase(
                    ChangeShippingAddressRequest(
                        dataChangeAddressRequests,
                        isOneClickShipment
                    )
                )
                if (view != null) {
                    view!!.hideLoading()
                    view!!.setHasRunningApiCall(false)
                    if (setShippingAddressData.isSuccess) {
                        if (setShippingAddressData.messages.isEmpty()) {
                            view!!.showToastNormal(view!!.getStringResource(R.string.label_change_address_success))
                        } else {
                            view!!.showToastNormal(setShippingAddressData.messages[0])
                        }
                        hitClearAllBo()
                        view!!.renderChangeAddressSuccess(reloadCheckoutPage)
                    } else {
                        if (setShippingAddressData.messages.isNotEmpty()) {
                            val stringBuilder = StringBuilder()
                            for (errorMessage in setShippingAddressData.messages) {
                                stringBuilder.append(errorMessage).append(" ")
                            }
                            view!!.showToastError(stringBuilder.toString())
                            if (isHandleFallback) {
                                view!!.renderChangeAddressFailed(reloadCheckoutPage)
                            }
                        } else {
                            view!!.showToastError(view!!.getStringResource(R.string.label_change_address_failed))
                            if (isHandleFallback) {
                                view!!.renderChangeAddressFailed(reloadCheckoutPage)
                            }
                        }
                    }
                }
            } catch (t: Throwable) {
                if (view != null) {
                    view!!.hideLoading()
                    view!!.setHasRunningApiCall(false)
                    Timber.d(t)
                    val errorMessage: String? = if (t is AkamaiErrorException) {
                        t.message
                    } else {
                        getErrorMessage(
                            view!!.activity,
                            t
                        )
                    }
                    view!!.showToastError(errorMessage)
                    if (isHandleFallback) {
                        view!!.renderChangeAddressFailed(reloadCheckoutPage)
                    }
                }
            }
        }
    }
// endregion

// region campaign
    fun getCampaignTimer(): CampaignTimerUi? {
        return if (campaignTimer == null || !campaignTimer!!.showTimer) {
            null
        } else {
            // Set necessary analytics attributes to be passed so the gtm will just trigger
            // the method without collecting the data again (quite expensive)
            campaignTimer!!.gtmProductId =
                getFirstProductId(shipmentCartItemModelList.filterIsInstance(ShipmentCartItemModel::class.java))
            campaignTimer!!.gtmUserId = userSessionInterface.userId
            campaignTimer
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun releaseBooking() {
        // As deals product is using OCS, the shipment should only contain 1 product
        val productId =
            getFirstProductId(shipmentCartItemModelList.filterIsInstance(ShipmentCartItemModel::class.java))
        if (productId != 0L) {
            GlobalScope.launch {
                try {
                    releaseBookingUseCase(productId)
                } catch (t: Throwable) {
                    Timber.d(t)
                }
            }
        }
    }
// endregion

// region epharmacy
    fun fetchPrescriptionIds(epharmacyData: EpharmacyData) {
        if (epharmacyData.checkoutId.isNotEmpty() && epharmacyData.showImageUpload && !epharmacyData.consultationFlow) {
            viewModelScope.launch(dispatchers.immediate) {
                try {
                    val getPrescriptionIdsResponse = prescriptionIdsUseCase
                        .setParams(epharmacyData.checkoutId)
                        .executeOnBackground()
                    if (getPrescriptionIdsResponse.detailData != null && getPrescriptionIdsResponse.detailData!!.prescriptionData != null && getPrescriptionIdsResponse.detailData!!.prescriptionData!!.prescriptions != null) {
                        val prescriptions =
                            getPrescriptionIdsResponse.detailData!!.prescriptionData!!.prescriptions
                        val prescriptionIds = ArrayList<String>()
                        for (prescription in prescriptions!!) {
                            prescriptionIds.add(prescription!!.prescriptionId!!)
                        }
                        setPrescriptionIds(prescriptionIds)
                        uploadPrescriptionUiModel.isError = false
                        view?.updateUploadPrescription(uploadPrescriptionUiModel)
                    }
                } catch (e: Throwable) {
                    Timber.d(e)
                }
            }
        }
    }

    fun fetchEpharmacyData() {
        epharmacyUseCase.getEPharmacyPrepareProductsGroup({ ePharmacyPrepareProductsGroupResponse: EPharmacyPrepareProductsGroupResponse ->
            processEpharmacyData(ePharmacyPrepareProductsGroupResponse)
        }) { throwable: Throwable? ->
            Timber.d(throwable)
        }
    }

    private fun processEpharmacyData(ePharmacyPrepareProductsGroupResponse: EPharmacyPrepareProductsGroupResponse) {
        if (ePharmacyPrepareProductsGroupResponse.detailData != null && view != null) {
            val groupsData = ePharmacyPrepareProductsGroupResponse.detailData!!.groupsData
            if (groupsData?.epharmacyGroups != null) {
                val epharmacyGroupIds = HashSet<String>()
                val mapPrescriptionCount = HashMap<String?, Int>()
                val enablerNames = HashSet<String>()
                val shopIds = ArrayList<String>()
                val cartIds = ArrayList<String>()
                var hasInvalidPrescription = false
                for (shipmentCartItemModel in shipmentCartItemModelList) {
                    if (shipmentCartItemModel is ShipmentCartItemModel && shipmentCartItemModel.hasEthicalProducts) {
                        shopIds.add(shipmentCartItemModel.shopId.toString())
                        enablerNames.add(shipmentCartItemModel.enablerLabel)
                        for (cartItemModel in shipmentCartItemModel.cartItemModels) {
                            if (cartItemModel.ethicalDrugDataModel.needPrescription) {
                                cartIds.add(cartItemModel.cartId.toString())
                            }
                        }
                    }
                    if (shipmentCartItemModel is ShipmentCartItemModel && !shipmentCartItemModel.isError && shipmentCartItemModel.hasEthicalProducts) {
                        var updated = false
                        var shouldResetCourier = false
                        var productErrorCount = 0
                        var firstProductErrorIndex = -1
                        val position =
                            view?.getShipmentCartItemModelAdapterPositionByCartStringGroup(
                                shipmentCartItemModel.cartStringGroup
                            ) ?: 0
                        if (position > 0) {
                            for (epharmacyGroup in groupsData.epharmacyGroups!!) {
                                if (updated) {
                                    break
                                }
                                if (epharmacyGroup?.shopInfo != null) {
                                    epharmacyGroupIds.add(epharmacyGroup.epharmacyGroupId!!)
                                    for (productsInfo in epharmacyGroup.shopInfo!!) {
                                        if (updated) {
                                            break
                                        }
                                        if (productsInfo?.shopId != null &&
                                            productsInfo.shopId!!.isNotBlankOrZero() &&
                                            shipmentCartItemModel.shopId == productsInfo.shopId!!.toLong()
                                        ) {
                                            if (productsInfo.products != null) {
                                                for (product in productsInfo.products!!) {
                                                    if (updated) {
                                                        break
                                                    }
                                                    if (product?.productId != null) {
                                                        for (i in shipmentCartItemModel.cartItemModels.indices.reversed()) {
                                                            val cartItemModel =
                                                                shipmentCartItemModel.cartItemModels[i]
                                                            if (product.productId == cartItemModel.productId && !cartItemModel.isError) {
                                                                if (epharmacyGroup.consultationData != null && epharmacyGroup.consultationData!!.consultationStatus != null && epharmacyGroup.consultationData!!.consultationStatus == EPHARMACY_CONSULTATION_STATUS_REJECTED) {
                                                                    shipmentCartItemModel.tokoConsultationId =
                                                                        ""
                                                                    shipmentCartItemModel.partnerConsultationId =
                                                                        ""
                                                                    shipmentCartItemModel.consultationDataString =
                                                                        ""
                                                                    hasInvalidPrescription = true
                                                                    if (shipmentCartItemModel.hasNonEthicalProducts) {
                                                                        cartItemModel.isError = true
                                                                        cartItemModel.errorMessage =
                                                                            uploadPrescriptionUiModel.rejectedWording
                                                                        shouldResetCourier = true
                                                                    } else {
                                                                        shipmentCartItemModel.firstProductErrorIndex =
                                                                            0
                                                                        shipmentCartItemModel.isError =
                                                                            true
                                                                        shipmentCartItemModel.isAllItemError =
                                                                            true
                                                                        for (itemModel in shipmentCartItemModel.cartItemModels) {
                                                                            itemModel.isError = true
                                                                            itemModel.isShopError =
                                                                                true
                                                                        }
                                                                        shipmentCartItemModel.errorTitle =
                                                                            view?.getStringResourceWithArgs(
                                                                                R.string.checkout_error_unblocking_message,
                                                                                shipmentCartItemModel.cartItemModels.size
                                                                            ) ?: ""
                                                                        shipmentCartItemModel.isCustomEpharmacyError =
                                                                            true
                                                                        shipmentCartItemModel.spId =
                                                                            0
                                                                        view?.updateShipmentCartItemGroup(
                                                                            shipmentCartItemModel
                                                                        )
                                                                        view?.resetCourier(
                                                                            shipmentCartItemModel
                                                                        )
                                                                        updated = true
                                                                        break
                                                                    }
                                                                } else if (epharmacyGroup.consultationData != null && epharmacyGroup.consultationData!!.consultationStatus != null && epharmacyGroup.consultationData!!.consultationStatus == EPHARMACY_CONSULTATION_STATUS_APPROVED) {
                                                                    shipmentCartItemModel.tokoConsultationId =
                                                                        epharmacyGroup.consultationData!!.tokoConsultationId!!
                                                                    shipmentCartItemModel.partnerConsultationId =
                                                                        epharmacyGroup.consultationData!!.partnerConsultationId!!
                                                                    shipmentCartItemModel.consultationDataString =
                                                                        epharmacyGroup.consultationData!!.consultationString!!
                                                                    mapPrescriptionCount[epharmacyGroup.epharmacyGroupId] =
                                                                        1
                                                                    updated = true
                                                                    break
                                                                } else if (epharmacyGroup.prescriptionImages != null && epharmacyGroup.prescriptionImages!!.isNotEmpty()) {
                                                                    val prescriptionIds =
                                                                        ArrayList<String>()
                                                                    for (prescriptionImage in epharmacyGroup.prescriptionImages!!) {
                                                                        if (prescriptionImage != null && !prescriptionImage.prescriptionId.isNullOrEmpty()
                                                                        ) {
                                                                            prescriptionIds.add(
                                                                                prescriptionImage.prescriptionId!!
                                                                            )
                                                                        }
                                                                    }
                                                                    shipmentCartItemModel.prescriptionIds =
                                                                        prescriptionIds
                                                                    mapPrescriptionCount[epharmacyGroup.epharmacyGroupId] =
                                                                        prescriptionIds.size
                                                                    updated = true
                                                                    break
                                                                }
                                                            }
                                                            if (cartItemModel.isError) {
                                                                productErrorCount += 1
                                                                firstProductErrorIndex = i
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (shouldResetCourier) {
                                shipmentCartItemModel.isHasUnblockingError = true
                                shipmentCartItemModel.firstProductErrorIndex =
                                    firstProductErrorIndex
                                shipmentCartItemModel.unblockingErrorMessage =
                                    view?.getStringResourceWithArgs(
                                        R.string.checkout_error_unblocking_message,
                                        productErrorCount
                                    ) ?: ""
                                shipmentCartItemModel.spId = 0
                                shipmentCartItemModel.shouldResetCourier = true
                                view?.updateShipmentCartItemGroup(shipmentCartItemModel)
                                view?.resetCourier(shipmentCartItemModel)
                            }
                        }
                    }
                }
                var totalPrescription = 0
                for (value in mapPrescriptionCount.values) {
                    totalPrescription += value
                }
                uploadPrescriptionUiModel.epharmacyGroupIds = ArrayList(epharmacyGroupIds)
                uploadPrescriptionUiModel.isError = false
                uploadPrescriptionUiModel.uploadedImageCount = totalPrescription
                uploadPrescriptionUiModel.hasInvalidPrescription = hasInvalidPrescription
                uploadPrescriptionUiModel.enablerNames = ArrayList(enablerNames)
                uploadPrescriptionUiModel.shopIds = shopIds
                uploadPrescriptionUiModel.cartIds = cartIds
                view?.updateUploadPrescription(uploadPrescriptionUiModel)
                view?.showCoachMarkEpharmacy(uploadPrescriptionUiModel)
            }
        }
    }

    fun setPrescriptionIds(prescriptionIds: ArrayList<String>) {
        for (shipmentCartItemModel in shipmentCartItemModelList) {
            if (shipmentCartItemModel is ShipmentCartItemModel && !shipmentCartItemModel.isError && shipmentCartItemModel.hasEthicalProducts) {
                shipmentCartItemModel.prescriptionIds = prescriptionIds
            }
        }
        uploadPrescriptionUiModel.uploadedImageCount = prescriptionIds.size
    }

    fun setMiniConsultationResult(results: ArrayList<EPharmacyMiniConsultationResult>) {
        if (view != null) {
            val epharmacyGroupIds = HashSet<String>()
            val mapPrescriptionCount = HashMap<String?, Int>()
            val enablerNames = HashSet<String>()
            val shopIds = ArrayList<String>()
            val cartIds = ArrayList<String>()
            var hasInvalidPrescription = false
            for (shipmentCartItemModel in shipmentCartItemModelList) {
                if (shipmentCartItemModel is ShipmentCartItemModel && shipmentCartItemModel.hasEthicalProducts) {
                    shopIds.add(shipmentCartItemModel.shopId.toString())
                    enablerNames.add(shipmentCartItemModel.enablerLabel)
                    for (cartItemModel in shipmentCartItemModel.cartItemModels) {
                        if (cartItemModel.ethicalDrugDataModel.needPrescription) {
                            cartIds.add(cartItemModel.cartId.toString())
                        }
                    }
                }
                if (shipmentCartItemModel is ShipmentCartItemModel && !shipmentCartItemModel.isError && shipmentCartItemModel.hasEthicalProducts) {
                    var updated = false
                    var shouldResetCourier = false
                    var productErrorCount = 0
                    var firstProductErrorIndex = -1
                    val position = view?.getShipmentCartItemModelAdapterPositionByCartStringGroup(
                        shipmentCartItemModel.cartStringGroup
                    ) ?: 0
                    if (position > 0) {
                        for (result in results) {
                            if (updated) {
                                break
                            }
                            if (result.shopInfo != null) {
                                epharmacyGroupIds.add(result.epharmacyGroupId!!)
                                for (productsInfo in result.shopInfo!!) {
                                    if (updated) {
                                        break
                                    }
                                    if (productsInfo?.products != null && productsInfo.shopId != null &&
                                        productsInfo.shopId!!.isNotBlankOrZero() &&
                                        shipmentCartItemModel.shopId == productsInfo.shopId!!.toLong()
                                    ) {
                                        for (product in productsInfo.products!!) {
                                            if (updated) {
                                                break
                                            }
                                            if (product?.productId != null) {
                                                for (i in shipmentCartItemModel.cartItemModels.indices.reversed()) {
                                                    val cartItemModel =
                                                        shipmentCartItemModel.cartItemModels[i]
                                                    if (!cartItemModel.isError && product.productId == cartItemModel.productId) {
                                                        if (result.consultationStatus != null && result.consultationStatus == EPHARMACY_CONSULTATION_STATUS_REJECTED) {
                                                            shipmentCartItemModel.tokoConsultationId =
                                                                ""
                                                            shipmentCartItemModel.partnerConsultationId =
                                                                ""
                                                            shipmentCartItemModel.consultationDataString =
                                                                ""
                                                            hasInvalidPrescription = true
                                                            if (shipmentCartItemModel.hasNonEthicalProducts) {
                                                                cartItemModel.isError = true
                                                                cartItemModel.errorMessage =
                                                                    uploadPrescriptionUiModel.rejectedWording
                                                                shouldResetCourier = true
                                                            } else {
                                                                shipmentCartItemModel.firstProductErrorIndex =
                                                                    0
                                                                shipmentCartItemModel.isError = true
                                                                shipmentCartItemModel.isAllItemError =
                                                                    true
                                                                for (itemModel in shipmentCartItemModel.cartItemModels) {
                                                                    itemModel.isError = true
                                                                    itemModel.isShopError = true
                                                                }
                                                                shipmentCartItemModel.errorTitle =
                                                                    view?.getStringResourceWithArgs(
                                                                        R.string.checkout_error_unblocking_message,
                                                                        shipmentCartItemModel.cartItemModels.size
                                                                    ) ?: ""
                                                                shipmentCartItemModel.isCustomEpharmacyError =
                                                                    true
                                                                shipmentCartItemModel.spId = 0
                                                                view?.updateShipmentCartItemGroup(
                                                                    shipmentCartItemModel
                                                                )
                                                                view?.resetCourier(
                                                                    shipmentCartItemModel
                                                                )
                                                                updated = true
                                                                break
                                                            }
                                                        } else if (result.consultationStatus != null && result.consultationStatus == EPHARMACY_CONSULTATION_STATUS_APPROVED) {
                                                            shipmentCartItemModel.tokoConsultationId =
                                                                result.tokoConsultationId!!
                                                            shipmentCartItemModel.partnerConsultationId =
                                                                result.partnerConsultationId!!
                                                            shipmentCartItemModel.consultationDataString =
                                                                result.consultationString!!
                                                            mapPrescriptionCount[result.epharmacyGroupId] =
                                                                1
                                                            updated = true
                                                            break
                                                        } else if (result.prescriptionImages != null && result.prescriptionImages!!.isNotEmpty()) {
                                                            val prescriptionIds =
                                                                ArrayList<String>()
                                                            for (prescriptionImage in result.prescriptionImages!!) {
                                                                if (prescriptionImage != null && !prescriptionImage.prescriptionId.isNullOrEmpty()
                                                                ) {
                                                                    prescriptionIds.add(
                                                                        prescriptionImage.prescriptionId!!
                                                                    )
                                                                }
                                                            }
                                                            shipmentCartItemModel.prescriptionIds =
                                                                prescriptionIds
                                                            mapPrescriptionCount[result.epharmacyGroupId] =
                                                                prescriptionIds.size
                                                            updated = true
                                                            break
                                                        }
                                                    }
                                                    if (cartItemModel.isError) {
                                                        productErrorCount += 1
                                                        firstProductErrorIndex = i
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (shouldResetCourier) {
                            shipmentCartItemModel.isHasUnblockingError = true
                            shipmentCartItemModel.firstProductErrorIndex = firstProductErrorIndex
                            shipmentCartItemModel.unblockingErrorMessage =
                                view?.getStringResourceWithArgs(
                                    R.string.checkout_error_unblocking_message,
                                    productErrorCount
                                ) ?: ""
                            shipmentCartItemModel.spId = 0
                            view?.updateShipmentCartItemGroup(shipmentCartItemModel)
                            view?.resetCourier(shipmentCartItemModel)
                        }
                    }
                }
            }
            var totalPrescription = 0
            for (value in mapPrescriptionCount.values) {
                totalPrescription += value
            }
            uploadPrescriptionUiModel.epharmacyGroupIds = ArrayList(epharmacyGroupIds)
            uploadPrescriptionUiModel.isError = false
            uploadPrescriptionUiModel.uploadedImageCount = totalPrescription
            uploadPrescriptionUiModel.hasInvalidPrescription = hasInvalidPrescription
            uploadPrescriptionUiModel.enablerNames = ArrayList(enablerNames)
            uploadPrescriptionUiModel.shopIds = shopIds
            uploadPrescriptionUiModel.cartIds = cartIds
            view?.updateUploadPrescription(uploadPrescriptionUiModel)
        }
    }

    fun setUploadPrescriptionData(uploadPrescriptionUiModel: UploadPrescriptionUiModel) {
        this.uploadPrescriptionUiModel = uploadPrescriptionUiModel
    }

    fun validatePrescriptionOnBackPressed(): Boolean {
        if (uploadPrescriptionUiModel.showImageUpload && view != null) {
            if (uploadPrescriptionUiModel.uploadedImageCount > 0 || uploadPrescriptionUiModel.hasInvalidPrescription) {
                view!!.showPrescriptionReminderDialog(uploadPrescriptionUiModel)
                return false
            }
        }
        return true
    }
// endregion

// region add ons gifting
    fun updateAddOnGiftingProductLevelDataBottomSheet(saveAddOnStateResult: SaveAddOnStateResult) {
        for (addOnResult in saveAddOnStateResult.addOns) {
            for (shipmentCartItemModel in shipmentCartItemModelList) {
                if (shipmentCartItemModel is ShipmentCartItemModel) {
                    val cartItemModelList = shipmentCartItemModel.cartItemModels
                    for (i in cartItemModelList.indices) {
                        val cartItemModel = cartItemModelList[i]
                        val keyProductLevel =
                            "${cartItemModel.cartStringGroup}-${cartItemModel.cartId}"
                        if (keyProductLevel.equals(addOnResult.addOnKey, ignoreCase = true)) {
                            val addOnsDataModel = cartItemModel.addOnGiftingProductLevelModel
                            setAddOnsGiftingData(
                                addOnsDataModel,
                                addOnResult,
                                0,
                                cartItemModel.cartStringGroup,
                                cartItemModel.cartId
                            )
                        }
                    }
                }
            }
        }
    }

    fun updateAddOnGiftingOrderLevelDataBottomSheet(saveAddOnStateResult: SaveAddOnStateResult) {
        for (addOnResult in saveAddOnStateResult.addOns) {
            for (shipmentCartItemModel in shipmentCartItemModelList) {
                if (shipmentCartItemModel is ShipmentCartItemModel && (shipmentCartItemModel.cartStringGroup + "-0").equals(
                        addOnResult.addOnKey,
                        ignoreCase = true
                    )
                ) {
                    val addOnsDataModel = shipmentCartItemModel.addOnsOrderLevelModel
                    setAddOnsGiftingData(
                        addOnsDataModel,
                        addOnResult,
                        1,
                        shipmentCartItemModel.cartStringGroup,
                        0L
                    )
                }
            }
        }
    }

// identifier : 0 = product level, 1  = order level
    private fun setAddOnsGiftingData(
        addOnsDataModel: AddOnGiftingDataModel,
        addOnResult: AddOnResult,
        identifier: Int,
        cartString: String,
        cartId: Long
    ) {
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
        val addOnBottomSheetModel = AddOnGiftingBottomSheetModel()
        addOnBottomSheetModel.headerTitle = addOnBottomSheet.headerTitle
        addOnBottomSheetModel.description = addOnBottomSheet.description
        val addOnTickerModel = AddOnGiftingTickerModel()
        addOnTickerModel.text = addOnBottomSheet.ticker.text
        addOnBottomSheetModel.ticker = addOnTickerModel
        val listProductAddOn = ArrayList<AddOnGiftingProductItemModel>()
        for (product in addOnBottomSheet.products) {
            val addOnProductItemModel = AddOnGiftingProductItemModel()
            addOnProductItemModel.productName = product.productName
            addOnProductItemModel.productImageUrl = product.productImageUrl
            listProductAddOn.add(addOnProductItemModel)
        }
        addOnBottomSheetModel.products = listProductAddOn
        addOnsDataModel.addOnsBottomSheetModel = addOnBottomSheetModel
        val listAddOnDataItem = arrayListOf<AddOnGiftingDataItemModel>()
        for (addOnData in addOnResult.addOnData) {
            val addOnDataItemModel = AddOnGiftingDataItemModel()
            val addOnNote = addOnData.addOnMetadata.addOnNote
            addOnDataItemModel.addOnId = addOnData.addOnId
            addOnDataItemModel.addOnUniqueId = addOnData.addOnUniqueId
            addOnDataItemModel.addOnPrice = addOnData.addOnPrice
            addOnDataItemModel.addOnQty = addOnData.addOnQty.toLong()
            addOnDataItemModel.addOnMetadata = AddOnGiftingMetadataItemModel(
                AddOnGiftingNoteItemModel(
                    addOnNote.isCustomNote,
                    addOnNote.to,
                    addOnNote.from,
                    addOnNote.notes
                )
            )
            listAddOnDataItem.add(addOnDataItemModel)
        }
        addOnsDataModel.addOnsDataItemModelList = listAddOnDataItem
        view?.updateAddOnsData(identifier, cartString, cartId)
        if (isUsingDynamicDataPassing()) {
            view?.updateAddOnsDynamicDataPassing(
                addOnResult,
                identifier,
                cartString,
                cartId
            )
        }
    }
// endregion

// region upsell
    fun cancelUpsell(
        isReloadData: Boolean,
        skipUpdateOnboardingState: Boolean,
        isReloadAfterPriceChangeHinger: Boolean
    ) {
        hitClearAllBo()
        processInitialLoadCheckoutPage(
            isReloadData = isReloadData,
            skipUpdateOnboardingState = skipUpdateOnboardingState,
            isReloadAfterPriceChangeHinger = isReloadAfterPriceChangeHinger
        )
    }

    fun clearAllBoOnTemporaryUpsell() {
        if (shipmentNewUpsellModel.isShow && shipmentNewUpsellModel.isSelected) {
            hitClearAllBo()
        }
    }
// endregion

// region ddp
    private fun setCurrentDynamicDataParamFromSAF(
        cartShipmentAddressFormData: CartShipmentAddressFormData,
        isOneClickShipment: Boolean
    ) {
        val ddpParam = DynamicDataPassingParamRequest()
        val listDataParam = ArrayList<DynamicDataParam>()
        // donation
        if (cartShipmentAddressFormData.donation != null && cartShipmentAddressFormData.donation!!.isChecked) {
            val dynamicDataParam = DynamicDataParam()
            dynamicDataParam.level = PAYMENT_LEVEL
            dynamicDataParam.uniqueId = ""
            dynamicDataParam.attribute = ATTRIBUTE_DONATION
            dynamicDataParam.donation = cartShipmentAddressFormData.donation!!.isChecked
            listDataParam.add(dynamicDataParam)
        }

        // addons
        for (groupAddress in cartShipmentAddressFormData.groupAddress) {
            for (groupShop in groupAddress.groupShop) {
                // order level
                if (groupShop.addOns.status == 1) {
                    val dynamicDataParam = DynamicDataParam()
                    dynamicDataParam.level = ORDER_LEVEL
                    dynamicDataParam.uniqueId = groupShop.cartString
                    dynamicDataParam.attribute = ATTRIBUTE_ADDON_DETAILS
                    dynamicDataParam.addOn = getAddOnFromSAF(groupShop.addOns, isOneClickShipment)
                    listDataParam.add(dynamicDataParam)
                }
                for (groupShopV2 in groupShop.groupShopData) {
                    for (product in groupShopV2.products) {
                        // product level
                        if (product.addOnGiftingProduct.status == 1) {
                            val dynamicDataParam = DynamicDataParam()
                            dynamicDataParam.level = PRODUCT_LEVEL
                            dynamicDataParam.parentUniqueId = groupShop.cartString
                            dynamicDataParam.uniqueId = product.cartId.toString()
                            dynamicDataParam.attribute = ATTRIBUTE_ADDON_DETAILS
                            dynamicDataParam.addOn =
                                getAddOnFromSAF(product.addOnGiftingProduct, isOneClickShipment)
                            listDataParam.add(dynamicDataParam)
                        }
                    }
                }
            }
        }
        ddpParam.data = listDataParam
        var source: String? = SOURCE_NORMAL
        if (isOneClickShipment) source = SOURCE_OCS
        ddpParam.source = source!!
        setDynamicDataParam(ddpParam)
    }

    fun updateDynamicData(
        dynamicDataPassingParamRequest: DynamicDataPassingParamRequest,
        isFireAndForget: Boolean
    ) {
        updateDynamicDataPassingUseCase.setParams(dynamicDataPassingParamRequest, isFireAndForget)
        updateDynamicDataPassingUseCase.execute(
            { (dynamicData): UpdateDynamicDataPassingUiModel ->
                this.dynamicData = dynamicData
                if (view != null && !isFireAndForget) {
                    view!!.doCheckout()
                }
                Unit
            }
        ) { throwable: Throwable ->
            Timber.d(throwable)
            if (view != null) {
                view!!.setHasRunningApiCall(false)
                view!!.hideLoading()
                var errorMessage = throwable.message
                if (throwable !is CartResponseErrorException && throwable !is AkamaiErrorException) {
                    errorMessage = getErrorMessage(
                        view!!.activity,
                        throwable
                    )
                }
                view!!.showToastError(errorMessage)
            }
            Unit
        }
    }

    fun setDynamicDataParam(dynamicDataPassingParam: DynamicDataPassingParamRequest) {
        this.dynamicDataParam = dynamicDataPassingParam
    }

    fun getDynamicDataParam(): DynamicDataPassingParamRequest {
        return this.dynamicDataParam
    }

    fun validateDynamicData() {
        updateDynamicData(getDynamicDataParam(), false)
    }

    fun isUsingDynamicDataPassing(): Boolean {
        return isUsingDdp
    }
// endregion

// region selly rx
    private fun getScheduleDeliveryMapData(cartString: String): ShipmentScheduleDeliveryMapData? {
        return scheduleDeliveryMapData[cartString]
    }

    fun setScheduleDeliveryMapData(
        cartString: String,
        shipmentScheduleDeliveryMapData: ShipmentScheduleDeliveryMapData
    ) {
        scheduleDeliveryMapData[cartString] = shipmentScheduleDeliveryMapData
    }

    private fun checkUnCompletedPublisher() {
        for (shipmentCartItemModel in shipmentCartItemModelList) {
            val mapData = getScheduleDeliveryMapData(
                shipmentCartItemModel.cartStringGroup
            )
            if (mapData != null && !mapData.donePublisher.hasCompleted()) {
                mapData.donePublisher.onCompleted()
            }
        }
    }
// endregion

// region promo request
    fun generateValidateUsePromoRequest(): ValidateUsePromoRequest {
        val bboPromoCodes = ArrayList<String>()
        var validateUsePromoRequest = lastValidateUseRequest
        return if (validateUsePromoRequest != null) {
            // Update param if have done param data generation before
            val shipmentCartItemModelList = shipmentCartItemModelList
            for (shipmentCartItemModel in shipmentCartItemModelList) {
                if (shipmentCartItemModel is ShipmentCartItemModel) {
                    for (ordersItem in validateUsePromoRequest.orders) {
                        if (ordersItem.cartStringGroup == shipmentCartItemModel.cartStringGroup) {
                            for ((cartStringOrder, cartItemList) in shipmentCartItemModel.cartItemModelsGroupByOrder) {
                                if (ordersItem.uniqueId == cartStringOrder) {
                                    val productDetailsItems = ArrayList<ProductDetailsItem>()
                                    for (cartItemModel in cartItemList) {
                                        val productDetail = ProductDetailsItem()
                                        productDetail.productId = cartItemModel.productId
                                        productDetail.quantity = cartItemModel.quantity
                                        productDetail.bundleId =
                                            cartItemModel.bundleId.toLongOrZero()
                                        productDetailsItems.add(productDetail)
                                    }
                                    ordersItem.productDetails = productDetailsItems
                                    val listOrderCodes = ordersItem.codes
                                    // Add data BBO
                                    if (shipmentCartItemModel.selectedShipmentDetailData != null && shipmentCartItemModel.voucherLogisticItemUiModel != null) {
                                        if (!listOrderCodes.contains(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)) {
                                            listOrderCodes.add(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)
                                        }
                                        if (!bboPromoCodes.contains(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)) {
                                            bboPromoCodes.add(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)
                                        }
                                        ordersItem.boCode =
                                            shipmentCartItemModel.voucherLogisticItemUiModel!!.code
                                    } else {
                                        ordersItem.boCode = ""
                                    }
                                    ordersItem.codes = listOrderCodes
                                    setValidateUseSpIdParam(shipmentCartItemModel, ordersItem)
                                    break
                                }
                            }
                        }
                    }
                }
            }
            if (isTradeIn) {
                validateUsePromoRequest.isTradeIn = 1
                validateUsePromoRequest.isTradeInDropOff = if (isTradeInByDropOff) 1 else 0
            }
            if (isOneClickShipment) {
                validateUsePromoRequest.cartType = "ocs"
            } else {
                validateUsePromoRequest.cartType = "default"
            }
            lastValidateUseRequest = validateUsePromoRequest
            this.bboPromoCodes = bboPromoCodes
            validateUsePromoRequest
        } else {
            // First param data generation / initialization
            validateUsePromoRequest = ValidateUsePromoRequest()
            val listOrderItem = ArrayList<OrdersItem>()
            val shipmentCartItemModelList = shipmentCartItemModelList
            val lastApplyUiModel = lastApplyData.value
            for (shipmentCartItemModel in shipmentCartItemModelList) {
                if (shipmentCartItemModel is ShipmentCartItemModel) {
                    for ((cartStringOrder, cartItemList) in shipmentCartItemModel.cartItemModelsGroupByOrder) {
                        val ordersItem = OrdersItem()
                        val productDetailsItems = ArrayList<ProductDetailsItem>()
                        for (cartItemModel in cartItemList) {
                            val productDetail = ProductDetailsItem()
                            productDetail.productId = cartItemModel.productId
                            productDetail.quantity = cartItemModel.quantity
                            productDetail.bundleId = cartItemModel.bundleId.toLongOrZero()
                            productDetailsItems.add(productDetail)
                        }
                        ordersItem.productDetails = productDetailsItems
                        val listOrderCodes = ArrayList<String>()
                        for (lastApplyVoucherOrdersItemUiModel in lastApplyUiModel.voucherOrders) {
                            if (cartStringOrder.equals(
                                    lastApplyVoucherOrdersItemUiModel.uniqueId,
                                    ignoreCase = true
                                )
                            ) {
                                if (!listOrderCodes.contains(lastApplyVoucherOrdersItemUiModel.code) && !lastApplyVoucherOrdersItemUiModel.isTypeLogistic()) {
                                    listOrderCodes.add(lastApplyVoucherOrdersItemUiModel.code)
                                }
                            }
                        }
                        // Add data BBO
                        if (shipmentCartItemModel.voucherLogisticItemUiModel != null) {
                            if (!listOrderCodes.contains(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)) {
                                listOrderCodes.add(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)
                            }
                            if (!bboPromoCodes.contains(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)) {
                                bboPromoCodes.add(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)
                            }
                            ordersItem.boCode =
                                shipmentCartItemModel.voucherLogisticItemUiModel!!.code
                        } else {
                            ordersItem.boCode = ""
                        }
                        ordersItem.codes = listOrderCodes
                        ordersItem.uniqueId = cartStringOrder
                        ordersItem.shopId = cartItemList.first().shopId.toLongOrZero()
                        ordersItem.boType = shipmentCartItemModel.shipmentCartData.boMetadata.boType
                        ordersItem.isPo = shipmentCartItemModel.isProductIsPreorder
                        ordersItem.poDuration =
                            shipmentCartItemModel.cartItemModels[0].preOrderDurationDay
                        ordersItem.warehouseId = shipmentCartItemModel.fulfillmentId
                        ordersItem.cartStringGroup = shipmentCartItemModel.cartStringGroup
                        setValidateUseSpIdParam(shipmentCartItemModel, ordersItem)
                        listOrderItem.add(ordersItem)
                    }
                }
            }
            validateUsePromoRequest.orders = listOrderItem
            validateUsePromoRequest.state = CheckoutConstant.PARAM_CHECKOUT
            validateUsePromoRequest.cartType = CartConstant.PARAM_DEFAULT
            validateUsePromoRequest.skipApply = 0
            if (isTradeIn) {
                validateUsePromoRequest.isTradeIn = 1
                validateUsePromoRequest.isTradeInDropOff = if (isTradeInByDropOff) 1 else 0
            }
            val globalPromoCodes = ArrayList<String>()
            if (lastApplyUiModel.codes.isNotEmpty()) {
                for (code in lastApplyUiModel.codes) {
                    if (code.isNotEmpty() && !globalPromoCodes.contains(code)) {
                        globalPromoCodes.add(code)
                    }
                }
            }
            validateUsePromoRequest.codes = globalPromoCodes
            if (isOneClickShipment) {
                validateUsePromoRequest.cartType = "ocs"
            } else {
                validateUsePromoRequest.cartType = "default"
            }
            lastValidateUseRequest = validateUsePromoRequest
            this.bboPromoCodes = bboPromoCodes
            validateUsePromoRequest
        }
    }

    private fun setValidateUseSpIdParam(
        shipmentCartItemModel: ShipmentCartItemModel,
        ordersItem: OrdersItem
    ) {
        if (shipmentCartItemModel.selectedShipmentDetailData == null) {
            ordersItem.shippingId = 0
            ordersItem.spId = 0
            ordersItem.freeShippingMetadata = ""
            ordersItem.boCampaignId = 0
            ordersItem.benefitClass = ""
            ordersItem.shippingSubsidy = 0
            ordersItem.shippingPrice = 0.0
            ordersItem.etaText = ""
            ordersItem.validationMetadata = ""
        } else {
            if (isTradeInByDropOff) {
                if (shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff != null) {
                    ordersItem.shippingId =
                        shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.shipperId
                    ordersItem.spId =
                        shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.shipperProductId
                    if (shipmentCartItemModel.voucherLogisticItemUiModel != null) {
                        ordersItem.freeShippingMetadata =
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.freeShippingMetadata
                        ordersItem.benefitClass =
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.benefitClass
                        ordersItem.shippingSubsidy =
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.shippingSubsidy
                        ordersItem.shippingPrice =
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.shippingRate.toDouble()
                        ordersItem.etaText =
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.etaText.toEmptyStringIfNull()
                        ordersItem.boCampaignId =
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.boCampaignId
                    } else {
                        ordersItem.freeShippingMetadata = ""
                        ordersItem.boCampaignId = 0
                        ordersItem.benefitClass = ""
                        ordersItem.shippingSubsidy = 0
                        ordersItem.shippingPrice = 0.0
                        ordersItem.etaText = ""
                    }
                    ordersItem.validationMetadata = shipmentCartItemModel.validationMetadata
                } else {
                    ordersItem.shippingId = 0
                    ordersItem.spId = 0
                    ordersItem.freeShippingMetadata = ""
                    ordersItem.boCampaignId = 0
                    ordersItem.benefitClass = ""
                    ordersItem.shippingSubsidy = 0
                    ordersItem.shippingPrice = 0.0
                    ordersItem.etaText = ""
                    ordersItem.validationMetadata = ""
                }
            } else {
                if (shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier != null) {
                    ordersItem.shippingId =
                        shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.shipperId
                    ordersItem.spId =
                        shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.shipperProductId
                    if (shipmentCartItemModel.voucherLogisticItemUiModel != null) {
                        ordersItem.freeShippingMetadata =
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.freeShippingMetadata
                        ordersItem.benefitClass =
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.benefitClass
                        ordersItem.shippingSubsidy =
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.shippingSubsidy
                        ordersItem.shippingPrice =
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.shippingRate.toDouble()
                        ordersItem.etaText =
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.etaText.toEmptyStringIfNull()
                        ordersItem.boCampaignId =
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.boCampaignId
                    } else {
                        ordersItem.freeShippingMetadata = ""
                        ordersItem.boCampaignId = 0
                        ordersItem.benefitClass = ""
                        ordersItem.shippingSubsidy = 0
                        ordersItem.shippingPrice = 0.0
                        ordersItem.etaText = ""
                    }
                    ordersItem.validationMetadata = shipmentCartItemModel.validationMetadata
                } else {
                    ordersItem.shippingId = 0
                    ordersItem.spId = 0
                    ordersItem.freeShippingMetadata = ""
                    ordersItem.boCampaignId = 0
                    ordersItem.benefitClass = ""
                    ordersItem.shippingSubsidy = 0
                    ordersItem.shippingPrice = 0.0
                    ordersItem.etaText = ""
                    ordersItem.validationMetadata = ""
                }
            }
        }
    }

    private fun removeInvalidBoCodeFromPromoRequest(
        shipmentGetCourierHolderData: ShipmentGetCourierHolderData,
        validateUsePromoRequest: ValidateUsePromoRequest
    ) {
        if (!shipmentGetCourierHolderData.shipmentCartItemModel.isFreeShippingPlus) {
            val shipmentCartItemModelLists =
                shipmentCartItemModelList.filterIsInstance(
                    ShipmentCartItemModel::class.java
                )
            for (tmpShipmentCartItemModel in shipmentCartItemModelLists) {
                for (order in validateUsePromoRequest.orders) {
                    if (shipmentGetCourierHolderData.shipmentCartItemModel.cartStringGroup != tmpShipmentCartItemModel.cartStringGroup && tmpShipmentCartItemModel.cartStringGroup == order.cartStringGroup && tmpShipmentCartItemModel.selectedShipmentDetailData != null && tmpShipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier != null &&
                        !tmpShipmentCartItemModel.isFreeShippingPlus
                    ) {
                        order.codes.remove(
                            tmpShipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.logPromoCode
                        )
                        order.boCode = ""
                    }
                }
            }
        }
    }

    internal fun setValidateUseBoCodeInOneOrderOwoc(validateUsePromoRequest: ValidateUsePromoRequest) {
        validateUsePromoRequest.orders.filter { it.cartStringGroup != it.uniqueId }
            .groupBy { it.cartStringGroup }.filterValues { it.size > 1 }.values
            .forEach { ordersItems ->
                val boCode = ordersItems.first().boCode
                if (boCode.isNotEmpty()) {
                    ordersItems.forEachIndexed { index, ordersItem ->
                        if (index > 0) {
                            ordersItem.codes.remove(boCode)
                        }
                    }
                }
            }
    }

    fun generateCouponListRecommendationRequest(): com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest {
        val promoRequest =
            com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest()
        val listOrderItem = ArrayList<Order>()
        val shipmentCartItemModelList = shipmentCartItemModelList
        for (shipmentCartItemModel in shipmentCartItemModelList) {
            if (shipmentCartItemModel is ShipmentCartItemModel) {
                for ((cartStringOrder, cartItemList) in shipmentCartItemModel.cartItemModelsGroupByOrder) {
                    val ordersItem = Order()
                    val productDetailsItems = ArrayList<ProductDetail>()
                    for (cartItemModel in cartItemList) {
                        val productDetail = ProductDetail()
                        productDetail.productId = cartItemModel.productId
                        productDetail.quantity = cartItemModel.quantity
                        productDetail.bundleId = cartItemModel.bundleId.toLongOrZero()
                        productDetailsItems.add(productDetail)
                    }
                    ordersItem.product_details = productDetailsItems
                    ordersItem.isChecked = true
                    val listCodes = ArrayList<String>()
                    for (code in shipmentCartItemModel.listPromoCodes) {
                        listCodes.add(code)
                    }
                    ordersItem.codes = listCodes
                    ordersItem.cartStringGroup = shipmentCartItemModel.cartStringGroup
                    ordersItem.uniqueId = cartStringOrder
                    ordersItem.shopId = cartItemList.first().shopId.toLongOrZero()
                    ordersItem.boType = shipmentCartItemModel.shipmentCartData.boMetadata.boType
                    ordersItem.isInsurancePrice = if (shipmentCartItemModel.isInsurance) 1 else 0
                    if (shipmentCartItemModel.selectedShipmentDetailData == null) {
                        ordersItem.shippingId = 0
                        ordersItem.spId = 0
                        ordersItem.freeShippingMetadata = ""
                        ordersItem.validationMetadata = ""
                    } else {
                        if (isTradeInByDropOff) {
                            if (shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff != null) {
                                ordersItem.shippingId =
                                    shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.shipperId
                                ordersItem.spId =
                                    shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.shipperProductId
                                if (shipmentCartItemModel.voucherLogisticItemUiModel != null) {
                                    ordersItem.freeShippingMetadata =
                                        shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.freeShippingMetadata
                                } else {
                                    ordersItem.freeShippingMetadata = ""
                                }
                                ordersItem.validationMetadata =
                                    shipmentCartItemModel.validationMetadata
                            } else {
                                ordersItem.shippingId = 0
                                ordersItem.spId = 0
                                ordersItem.freeShippingMetadata = ""
                                ordersItem.validationMetadata = ""
                            }
                        } else {
                            if (shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier != null) {
                                ordersItem.shippingId =
                                    shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.shipperId
                                ordersItem.spId =
                                    shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.shipperProductId
                                if (shipmentCartItemModel.voucherLogisticItemUiModel != null) {
                                    ordersItem.freeShippingMetadata =
                                        shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.freeShippingMetadata
                                } else {
                                    ordersItem.freeShippingMetadata = ""
                                }
                                ordersItem.validationMetadata =
                                    shipmentCartItemModel.validationMetadata
                            } else {
                                ordersItem.shippingId = 0
                                ordersItem.spId = 0
                                ordersItem.freeShippingMetadata = ""
                                ordersItem.validationMetadata = ""
                            }
                        }
                    }
                    listOrderItem.add(ordersItem)
                }
            }
        }
        promoRequest.orders = listOrderItem
        promoRequest.state = CheckoutConstant.PARAM_CHECKOUT
        if (isOneClickShipment) {
            promoRequest.cartType = "ocs"
        } else {
            promoRequest.cartType = CartConstant.PARAM_DEFAULT
        }
        if (isTradeIn) {
            promoRequest.isTradeIn = 1
            promoRequest.isTradeInDropOff = if (isTradeInByDropOff) 1 else 0
        }
        val lastApplyUiModel = lastApplyData.value
        val globalPromoCodes = ArrayList<String>()
        if (lastApplyUiModel.codes.isNotEmpty()) {
            globalPromoCodes.addAll(lastApplyUiModel.codes)
        }
        promoRequest.codes = globalPromoCodes
        return promoRequest
    }
// endregion

// region platform fee
    fun getDynamicPaymentFee(request: PaymentFeeCheckoutRequest?) {
        if (view != null) {
            view?.showPaymentFeeSkeletonLoading()

            getPaymentFeeCheckoutUseCase.setParams(request!!)
            getPaymentFeeCheckoutUseCase.execute(
                { (platformFeeData): PaymentFeeGqlResponse ->
                    if (view != null) {
                        if (platformFeeData.success) {
                            view?.showPaymentFeeData(platformFeeData)
                        } else {
                            view?.showPaymentFeeTickerFailedToLoad(shipmentPlatformFeeData.errorWording)
                        }
                    }
                    Unit
                }
            ) { throwable: Throwable ->
                Timber.d(throwable)
                if (view != null) {
                    view?.showPaymentFeeTickerFailedToLoad(shipmentPlatformFeeData.errorWording)
                }
                Unit
            }
        }
    }

    fun getShipmentPlatformFeeData(): ShipmentPlatformFeeData {
        return shipmentPlatformFeeData
    }

    fun setPlatformFeeData(paymentFee: ShipmentPaymentFeeModel) {
        shipmentCostModel.value = shipmentCostModel.value.copy(dynamicPlatformFee = paymentFee)
    }
// endregion

// region addons product service
    fun saveAddOnsProduct(cartItemModel: CartItemModel) {
        val params = ShipmentAddOnProductServiceMapper.generateSaveAddOnProductRequestParams(
            cartItemModel,
            isOneClickShipment
        )
        saveAddOnProductUseCase.setParams(params, true)
        saveAddOnProductUseCase.execute(
            onSuccess = {
                updateShipmentCostModel()
            },
            onError = {
                updateShipmentCostModel()
            }
        )
    }

    fun saveAddOnsProductBeforeCheckout() {
        if (shipmentCartItemModelList.isNotEmpty()) {
            val allShipmentCartItemModel: ArrayList<CartItemModel> = arrayListOf()
            shipmentCartItemModelList.filterIsInstance<ShipmentCartItemModel>()
                .forEach { shipmentCartItem ->
                    shipmentCartItem.cartItemModels.forEach { cartItemModel ->
                        allShipmentCartItemModel.add(cartItemModel)
                    }
                }

            val params = ShipmentAddOnProductServiceMapper.generateSaveAddOnProductRequestParams(
                allShipmentCartItemModel,
                isOneClickShipment
            )
            saveAddOnProductUseCase.setParams(params, false)
            saveAddOnProductUseCase.execute(
                onSuccess = {
                    if (it.saveAddOns.status.equals(statusOK, true)) {
                        view?.handleOnSuccessSaveAddOnProduct()
                    } else {
                        if (it.saveAddOns.errorMessage.isNotEmpty()) {
                            view?.showToastError(it.saveAddOns.errorMessage.first())
                        } else {
                            view?.showToastError(view?.getStringResource(R.string.message_error_checkout_empty))
                        }
                    }
                },
                onError = {
                    view?.showToastError(getErrorMessage(view?.activity, it))
                }
            )
        }
    }
// end region

    companion object {
        private const val LAST_THREE_DIGIT_MODULUS: Long = 1000

        private const val statusOK = "OK"

        private const val statusCode200 = "200"

        private const val WEIGHT_DIVIDER_TO_KG = 1000
    }
}
