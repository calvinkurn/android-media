package com.tokopedia.checkout.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
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
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.domain.model.cartshipmentform.CampaignTimerUi
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.cartshipmentform.EpharmacyData
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop.Companion.GROUP_TYPE_OWOC
import com.tokopedia.checkout.domain.model.changeaddress.SetShippingAddressData
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressGqlUseCase
import com.tokopedia.checkout.domain.usecase.CheckoutUseCase
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
import com.tokopedia.checkout.view.subscriber.GetCourierRecommendationSubscriber
import com.tokopedia.checkout.view.subscriber.GetScheduleDeliveryCourierRecommendationSubscriber
import com.tokopedia.checkout.view.subscriber.ReleaseBookingStockSubscriber
import com.tokopedia.checkout.view.uimodel.CrossSellModel
import com.tokopedia.checkout.view.uimodel.CrossSellOrderSummaryModel
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.checkout.view.uimodel.EgoldTieringModel
import com.tokopedia.checkout.view.uimodel.ShipmentButtonPaymentModel
import com.tokopedia.checkout.view.uimodel.ShipmentCostModel
import com.tokopedia.checkout.view.uimodel.ShipmentCrossSellModel
import com.tokopedia.checkout.view.uimodel.ShipmentDonationModel
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.checkout.view.uimodel.ShipmentTickerErrorModel
import com.tokopedia.checkout.view.uimodel.ShipmentUpsellModel
import com.tokopedia.common_epharmacy.EPHARMACY_CONSULTATION_STATUS_APPROVED
import com.tokopedia.common_epharmacy.EPHARMACY_CONSULTATION_STATUS_REJECTED
import com.tokopedia.common_epharmacy.network.response.EPharmacyMiniConsultationResult
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.common_epharmacy.usecase.EPharmacyPrepareProductsGroupUseCase
import com.tokopedia.fingerprint.util.FingerPrintUtil.getPublicKey
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.logisticCommon.data.constant.AddressConstant
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticCommon.data.response.KeroAddrIsEligibleForAddressFeatureData
import com.tokopedia.logisticCommon.domain.param.EditAddressParam
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticcart.scheduledelivery.domain.usecase.GetRatesWithScheduleUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.CodModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.GroupProduct
import com.tokopedia.logisticcart.shipping.model.GroupProductItem
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
import com.tokopedia.network.authentication.AuthHelper.Companion.generateParamsNetwork
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler.Companion.getErrorMessage
import com.tokopedia.network.utils.TKPDMapParam
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.analytics.PromoRevampAnalytics.eventCheckoutViewPromoMessage
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCheckout
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.DEFAULT_ERROR_MESSAGE_FAIL_APPLY_BBO
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.DEFAULT_ERROR_MESSAGE_VALIDATE_PROMO
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.data.model.UpdateDynamicDataPassingUiModel
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.data.request.DynamicDataPassingParamRequest
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.data.request.DynamicDataPassingParamRequest.DynamicDataParam
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.domain.UpdateDynamicDataPassingUseCase
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase.GetPrescriptionIdsUseCaseCoroutine
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnBottomSheetModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnButtonModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnDataItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnMetadataItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnNoteItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnProductItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnTickerModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnsDataModel
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
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ClashingInfoDetailUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MvcShippingBenefitUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.SummariesItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata.Companion.TYPE_ICON_GLOBAL
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.purchase_platform.common.utils.Utils
import com.tokopedia.purchase_platform.common.utils.isNotBlankOrZero
import com.tokopedia.purchase_platform.common.utils.isNullOrEmpty
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rx.Observable
import rx.Subscriber
import rx.subjects.PublishSubject
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ShipmentPresenter @Inject constructor(
    private val compositeSubscription: CompositeSubscription,
    private val checkoutGqlUseCase: CheckoutUseCase,
    private val getShipmentAddressFormV4UseCase: GetShipmentAddressFormV4UseCase,
    private val editAddressUseCase: EditAddressUseCase,
    private val changeShippingAddressGqlUseCase: ChangeShippingAddressGqlUseCase,
    private val saveShipmentStateGqlUseCase: SaveShipmentStateGqlUseCase,
    private val ratesUseCase: GetRatesUseCase,
    private val ratesApiUseCase: GetRatesApiUseCase,
    private val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase,
    private val stateConverter: RatesResponseStateConverter,
    private val shippingCourierConverter: ShippingCourierConverter,
    private val analyticsActionListener: AnalyticsActionListener,
    private val userSessionInterface: UserSessionInterface,
    private val mTrackerPurchaseProtection: CheckoutAnalyticsPurchaseProtection,
    private val mTrackerShipment: CheckoutAnalyticsCourierSelection,
    private val shipmentDataConverter: ShipmentDataConverter,
    private val releaseBookingUseCase: ReleaseBookingUseCase,
    private val prescriptionIdsUseCase: GetPrescriptionIdsUseCaseCoroutine,
    private val epharmacyUseCase: EPharmacyPrepareProductsGroupUseCase,
    private val validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase,
    private val gson: Gson,
    private val executorSchedulers: ExecutorSchedulers,
    private val dispatchers: CoroutineDispatchers,
    private val eligibleForAddressUseCase: EligibleForAddressUseCase,
    private val ratesWithScheduleUseCase: GetRatesWithScheduleUseCase,
    private val updateDynamicDataPassingUseCase: UpdateDynamicDataPassingUseCase,
    private val shipmentDataRequestConverter: ShipmentDataRequestConverter
) : ShipmentContract.Presenter, ViewModel() {

    private var view: ShipmentFragment? = null

    override var shipmentUpsellModel = ShipmentUpsellModel()
        private set

    override var shipmentNewUpsellModel = ShipmentNewUpsellModel()
        private set

    override var shipmentCartItemModelList: List<ShipmentCartItem> = emptyList()

    override var shipmentTickerErrorModel = ShipmentTickerErrorModel()
        private set

    override val tickerAnnouncementHolderData: CheckoutMutableLiveData<TickerAnnouncementHolderData> =
        CheckoutMutableLiveData(TickerAnnouncementHolderData())

    override var recipientAddressModel: RecipientAddressModel = RecipientAddressModel()

    val shipmentCostModel: CheckoutMutableLiveData<ShipmentCostModel> =
        CheckoutMutableLiveData(ShipmentCostModel())

    override val egoldAttributeModel: CheckoutMutableLiveData<EgoldAttributeModel?> =
        CheckoutMutableLiveData(null)

    override var shipmentDonationModel: ShipmentDonationModel? = null

    var listShipmentCrossSellModel: ArrayList<ShipmentCrossSellModel> = ArrayList()

    override val shipmentButtonPayment: CheckoutMutableLiveData<ShipmentButtonPaymentModel> =
        CheckoutMutableLiveData(ShipmentButtonPaymentModel())

    override var codData: CodModel? = null
        private set

    private var campaignTimer: CampaignTimerUi? = null

    override var validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel? = null

    override var lastValidateUseRequest: ValidateUsePromoRequest? = null
        private set

    var bboPromoCodes = ArrayList<String>()

    override var couponStateChanged = false

    private var shippingCourierViewModelsState: MutableMap<Int, List<ShippingCourierUiModel>> =
        HashMap()

    private var isPurchaseProtectionPage = false

    override var isShowOnboarding = false
        private set

    override var isIneligiblePromoDialogEnabled = false
        private set

    private var isBoUnstackEnabled = false

    override var cartDataForRates = ""
        private set

    override var lastApplyData: CheckoutMutableLiveData<LastApplyUiModel> =
        CheckoutMutableLiveData(LastApplyUiModel())

    override var uploadPrescriptionUiModel: UploadPrescriptionUiModel = UploadPrescriptionUiModel()
        private set

    private var ratesPublisher: PublishSubject<ShipmentGetCourierHolderData>? = null

    private val ratesQueue: Queue<ShipmentGetCourierHolderData> = LinkedList()
    private var shouldConsumeRatesQueue: Boolean = false

    private var ratesPromoPublisher: PublishSubject<ShipmentGetCourierHolderData>? = null

    override var logisticDonePublisher: PublishSubject<Boolean>? = null

    var logisticPromoDonePublisher: PublishSubject<Boolean>? = null

    private var scheduleDeliveryMapData: MutableMap<String, ShipmentScheduleDeliveryMapData> =
        HashMap()

    private var isUsingDdp = false

    private var dynamicDataParam: DynamicDataPassingParamRequest = DynamicDataPassingParamRequest()

    var dynamicData = ""

    var isOneClickShipment: Boolean = false

    var checkoutLeasingId: String = "0"

    var isTradeIn: Boolean = false

    val isTradeInByDropOff: Boolean
        get() {
            val recipientAddressModel = this.recipientAddressModel
            return recipientAddressModel.selectedTabIndex == 1
        }

    var deviceId: String = ""

    val cornerId: String?
        get() = recipientAddressModel.cornerId

    var checkoutPageSource: String = CheckoutConstant.CHECKOUT_PAGE_SOURCE_PDP

    var isPlusSelected: Boolean = false

    override fun attachView(view: ShipmentFragment) {
        this.view = view
    }

    override fun detachView() {
        viewModelScope.coroutineContext.cancelChildren()
        compositeSubscription.unsubscribe()
        eligibleForAddressUseCase.cancelJobs()
        epharmacyUseCase.cancelJobs()
        updateDynamicDataPassingUseCase.cancelJobs()
        ratesPublisher = null
        logisticDonePublisher = null
        ratesPromoPublisher = null
        logisticPromoDonePublisher = null
        view = null
    }

    private fun hasSetAllCourier(): Boolean {
        for (itemData in shipmentCartItemModelList) {
            if (itemData is ShipmentCartItemModel && itemData.selectedShipmentDetailData == null && !itemData.isError) {
                return false
            }
        }
        return true
    }

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
        var orderPriorityFee = 0.0
        var totalBookingFee = 0
        var hasAddOnSelected = false
        var totalAddOnPrice = 0.0
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
                        if (cartItem.addOnProductLevelModel.status == 1) {
                            if (cartItem.addOnProductLevelModel.addOnsDataItemModelList.isNotEmpty()) {
                                for (addOnsData in cartItem.addOnProductLevelModel.addOnsDataItemModelList) {
                                    totalAddOnPrice += addOnsData.addOnPrice
                                    hasAddOnSelected = true
                                }
                            }
                        }
                    }
                }
                if (shipmentData.selectedShipmentDetailData != null && !shipmentData.isError) {
                    val useInsurance = shipmentData.selectedShipmentDetailData!!.useInsurance
                    val isOrderPriority = shipmentData.selectedShipmentDetailData!!.isOrderPriority
                    val isTradeInPickup = isTradeInByDropOff
                    if (isTradeInPickup) {
                        if (shipmentData.selectedShipmentDetailData!!.selectedCourierTradeInDropOff != null) {
                            shippingFee += shipmentData.selectedShipmentDetailData!!
                                .selectedCourierTradeInDropOff!!.shipperPrice.toDouble()
                            if (useInsurance != null && useInsurance) {
                                insuranceFee += shipmentData.selectedShipmentDetailData!!
                                    .selectedCourierTradeInDropOff!!.insurancePrice.toDouble()
                            }
                            if (isOrderPriority != null && isOrderPriority) {
                                orderPriorityFee += shipmentData.selectedShipmentDetailData!!
                                    .selectedCourierTradeInDropOff!!.priorityPrice.toDouble()
                            }
                            additionalFee += shipmentData.selectedShipmentDetailData!!
                                .selectedCourierTradeInDropOff!!.additionalPrice.toDouble()
                        } else {
                            shippingFee = 0.0
                            insuranceFee = 0.0
                            orderPriorityFee = 0.0
                            additionalFee = 0.0
                        }
                    } else if (shipmentData.selectedShipmentDetailData!!.selectedCourier != null) {
                        shippingFee += shipmentData.selectedShipmentDetailData!!
                            .selectedCourier!!.selectedShipper.shipperPrice.toDouble()
                        if (useInsurance != null && useInsurance) {
                            insuranceFee += shipmentData.selectedShipmentDetailData!!
                                .selectedCourier!!.selectedShipper.insurancePrice.toDouble()
                        }
                        if (isOrderPriority != null && isOrderPriority) {
                            orderPriorityFee += shipmentData.selectedShipmentDetailData!!
                                .selectedCourier!!.priorityPrice.toDouble()
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
                        totalAddOnPrice += addOnPrice
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
            totalItemPrice + finalShippingFee + insuranceFee + orderPriorityFee + totalPurchaseProtectionPrice + additionalFee + totalBookingFee -
            shipmentCost.productDiscountAmount - tradeInPrice + totalAddOnPrice
        shipmentCost.totalWeight = totalWeight
        shipmentCost.additionalFee = additionalFee
        shipmentCost.totalItemPrice = totalItemPrice
        shipmentCost.totalItem = totalItem
        shipmentCost.shippingFee = shippingFee
        shipmentCost.insuranceFee = insuranceFee
        shipmentCost.priorityFee = orderPriorityFee
        shipmentCost.totalPurchaseProtectionItem = totalPurchaseProtectionItem
        shipmentCost.purchaseProtectionFee = totalPurchaseProtectionPrice
        shipmentCost.tradeInPrice = tradeInPrice
        shipmentCost.totalAddOnPrice = totalAddOnPrice
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
        if (listShipmentCrossSellModel.isNotEmpty()) {
            for (crossSellModel in listCheckedCrossModel) {
                if (crossSellModel.isChecked) {
                    listCheckedCrossModel.add(crossSellModel)
                    totalPrice += crossSellModel.crossSellModel.price
                    shipmentCost.totalPrice = totalPrice
                }
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

    private fun updateCheckoutButtonData(shipmentCost: ShipmentCostModel) {
        var cartItemCounter = 0
        var cartItemErrorCounter = 0
        for (shipmentCartItemModel in shipmentCartItemModelList) {
            if (shipmentCartItemModel is ShipmentCartItemModel) {
                if (shipmentCartItemModel.selectedShipmentDetailData != null) {
                    if ((shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier != null && !isTradeInByDropOff) || (shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff != null && isTradeInByDropOff)) {
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
            val priceTotalFormatted =
                Utils.removeDecimalSuffix(
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(
                        priceTotal.toLong(),
                        false
                    )
                )
            shipmentButtonPayment.value = shipmentButtonPayment.value.copy(
                enable = true,
                totalPrice = priceTotalFormatted
            )
        } else {
            shipmentButtonPayment.value = shipmentButtonPayment.value.copy(
                enable = cartItemErrorCounter < shipmentCartItemModelList.size,
                totalPrice = "-"
            )
        }
    }

    private fun validateLoadingItem(shipmentCartItemModel: ShipmentCartItemModel): Boolean {
        return shipmentCartItemModel.isStateLoadingCourierState
    }

    fun updateCheckoutButtonData() {
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
        val shipmentCost = shipmentCostModel.value
        if (cartItemCounter > 0 && cartItemCounter <= shipmentCartItemModelList.size) {
            val priceTotal: Double =
                if (shipmentCost.totalPrice <= 0) 0.0 else shipmentCost.totalPrice
            val priceTotalFormatted =
                Utils.removeDecimalSuffix(
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(
                        priceTotal,
                        false
                    )
                )
            updateShipmentButtonPaymentModel(!hasLoadingItem, priceTotalFormatted, null)
        } else {
            updateShipmentButtonPaymentModel(
                cartItemErrorCounter < shipmentCartItemModelList.size,
                "-",
                null
            )
        }
    }

    override fun updateShipmentButtonPaymentModel(
        enable: Boolean?,
        totalPrice: String?,
        loading: Boolean?
    ) {
        val buttonPaymentModel = shipmentButtonPayment.value
        shipmentButtonPayment.value = buttonPaymentModel.copy(
            enable = enable ?: buttonPaymentModel.enable,
            totalPrice = totalPrice ?: buttonPaymentModel.totalPrice,
            loading = loading ?: buttonPaymentModel.loading
        )
    }

    private fun getPromoFlag(step: String): Boolean {
        return if (step == EnhancedECommerceActionField.STEP_2) {
            lastApplyData.value.additionalInfo.pomlAutoApplied
        } else {
            validateUsePromoRevampUiModel?.promoUiModel?.additionalInfoUiModel?.pomlAutoApplied
                ?: false
        }
    }

    override fun triggerSendEnhancedEcommerceCheckoutAnalytics(
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

    override fun processInitialLoadCheckoutPage(
        isReloadData: Boolean,
        skipUpdateOnboardingState: Boolean,
        isReloadAfterPriceChangeHinger: Boolean
    ) {
        if (isReloadData) {
            view?.setShipmentNewUpsellLoading(true)
            view?.setHasRunningApiCall(true)
            view?.showLoading()
        } else {
            view?.showInitialLoading()
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
                    view?.stopEmbraceTrace()
                    if (isReloadData) {
                        view?.setShipmentNewUpsellLoading(false)
                        view?.setHasRunningApiCall(false)
                        view?.resetPromoBenefit()
                        view?.clearTotalBenefitPromoStacking()
                        view?.hideLoading()
                    } else {
                        view?.hideInitialLoading()
                    }
                    validateShipmentAddressFormData(
                        cartShipmentAddressFormData,
                        isReloadData,
                        isReloadAfterPriceChangeHinger,
                        isOneClickShipment
                    )
                    view?.stopTrace()
                }
            } catch (throwable: Throwable) {
                Timber.d(throwable)
                if (view != null) {
                    view?.stopEmbraceTrace()
                    if (isReloadData) {
                        view?.setShipmentNewUpsellLoading(false)
                        view?.setHasRunningApiCall(false)
                        view?.hideLoading()
                    } else {
                        view?.hideInitialLoading()
                    }
                    var errorMessage = throwable.message
                    if (throwable !is CartResponseErrorException && throwable !is AkamaiErrorException) {
                        errorMessage = getErrorMessage(view?.activityContext, throwable)
                    }
                    view?.showToastError(errorMessage)
                    view?.stopTrace()
                    view?.logOnErrorLoadCheckoutPage(throwable)
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
            checkIsUserEligibleForRevampAna(cartShipmentAddressFormData)
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
                    isReloadAfterPriceChangeHigher,
                    isOneClickShipment
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
    }

    private fun checkIsUserEligibleForRevampAna(cartShipmentAddressFormData: CartShipmentAddressFormData) {
        eligibleForAddressUseCase.eligibleForAddressFeature({ response: KeroAddrIsEligibleForAddressFeatureData ->
            if (view != null) {
                view?.renderCheckoutPageNoAddress(
                    cartShipmentAddressFormData,
                    response.eligibleForRevampAna.eligible
                )
            }
        }, { throwable: Throwable ->
            if (view != null) {
                var errorMessage = throwable.message
                if (errorMessage == null) {
                    errorMessage =
                        view?.activityContext?.getString(com.tokopedia.abstraction.R.string.default_request_error_unknown_short)
                }
                view?.showToastError(errorMessage)
            }
        }, AddressConstant.ANA_REVAMP_FEATURE_ID)
    }

    internal fun initializePresenterData(cartShipmentAddressFormData: CartShipmentAddressFormData) {
        setLatValidateUseRequest(null)
        validateUsePromoRevampUiModel = null
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
    }

    internal fun setPurchaseProtection(isPurchaseProtectionPage: Boolean) {
        this.isPurchaseProtectionPage = isPurchaseProtectionPage
    }

    override fun processCheckout() {
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
                    view?.setHasRunningApiCall(false)
                    if (!checkoutData.isError) {
                        view?.triggerSendEnhancedEcommerceCheckoutAnalyticAfterCheckoutSuccess(
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
                        view?.renderCheckoutCartSuccess(checkoutData)
                    } else if (checkoutData.priceValidationData.isUpdated) {
                        view?.hideLoading()
                        view?.renderCheckoutPriceUpdated(checkoutData.priceValidationData)
                    } else if (checkoutData.prompt.eligible) {
                        view?.hideLoading()
                        view?.renderPrompt(checkoutData.prompt)
                    } else {
                        analyticsActionListener.sendAnalyticsChoosePaymentMethodFailed(checkoutData.errorMessage)
                        view?.hideLoading()
                        if (checkoutData.errorMessage.isNotEmpty()) {
                            view?.renderCheckoutCartError(checkoutData.errorMessage)
                            view?.logOnErrorCheckout(
                                MessageErrorException(checkoutData.errorMessage),
                                checkoutRequest.toString()
                            )
                        } else {
                            val defaultErrorMessage =
                                view?.activityContext?.getString(com.tokopedia.abstraction.R.string.default_request_error_unknown)
                                    ?: ""
                            view?.renderCheckoutCartError(defaultErrorMessage)
                            view?.logOnErrorCheckout(
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
                } catch (e: Throwable) {
                    view?.hideLoading()
                    Timber.d(e)
                    var errorMessage = e.message
                    if (!(e is CartResponseErrorException || e is AkamaiErrorException)) {
                        errorMessage = getErrorMessage(view?.activityContext, e)
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
            view?.hideLoading()
            view?.setHasRunningApiCall(false)
            view?.showToastError(view?.activityContext?.getString(R.string.message_error_checkout_empty))
        }
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
        if (getEnableFingerprintPayment(view?.activityContext)) {
            val fpk = getFingerprintPublicKey(
                view?.activityContext
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
                    order.warehouseId
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

    override fun checkPromoCheckoutFinalShipment(
        validateUsePromoRequest: ValidateUsePromoRequest,
        lastSelectedCourierOrderIndex: Int,
        cartString: String?
    ) {
        couponStateChanged = true
        viewModelScope.launch(dispatchers.immediate) {
            try {
                setValidateUseBoCodeInOneOrderOwoc(validateUsePromoRequest)
                val validateUsePromoRevampUiModel =
                    validateUsePromoRevampUseCase.setParam(validateUsePromoRequest)
                        .executeOnBackground()
                if (view != null) {
                    this@ShipmentPresenter.validateUsePromoRevampUiModel =
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
                        view?.renderErrorCheckPromoShipmentData(message)
                        view?.resetPromoBenefit()
                        view?.cancelAllCourierPromo()
                    } else {
                        view?.updateButtonPromoCheckout(
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
                    val clashingInfoDetailUiModel =
                        validateUsePromoRevampUiModel.promoUiModel.clashingInfoDetailUiModel
                    if (clashingInfoDetailUiModel.clashMessage.isNotEmpty() || clashingInfoDetailUiModel.clashReason.isNotEmpty() || clashingInfoDetailUiModel.options.isNotEmpty()) {
                        val clashPromoCodes = ArrayList<String>()
                        for (promoClashOptionUiModel in clashingInfoDetailUiModel.options) {
                            for (voucherOrder in promoClashOptionUiModel.voucherOrders) {
                                clashPromoCodes.add(voucherOrder.code)
                            }
                        }
                        cancelAutoApplyPromoStackAfterClash(clashingInfoDetailUiModel)
                    }
                    reloadCourierForMvc(
                        validateUsePromoRevampUiModel,
                        lastSelectedCourierOrderIndex,
                        cartString
                    )
                    checkUnCompletedPublisher()
                }
            } catch (e: Throwable) {
                Timber.d(e)
                if (view != null) {
                    if (e is AkamaiErrorException) {
                        clearAllPromo()
                        view?.showToastError(e.message)
                        view?.resetAllCourier()
                        view?.cancelAllCourierPromo()
                        view?.doResetButtonPromoCheckout()
                    } else {
                        view?.renderErrorCheckPromoShipmentData(
                            getErrorMessage(
                                view?.activityContext,
                                e
                            )
                        )
                    }
                    checkUnCompletedPublisher()
                }
            }
        }
    }

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

    private fun updateTickerAnnouncementData(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel) {
        if (validateUsePromoRevampUiModel.promoUiModel.tickerInfoUiModel.message.isNotEmpty()) {
            val ticker = tickerAnnouncementHolderData.value
            ticker.id =
                validateUsePromoRevampUiModel.promoUiModel.tickerInfoUiModel.statusCode.toString()
            ticker.title = ""
            ticker.message =
                validateUsePromoRevampUiModel.promoUiModel.tickerInfoUiModel.message
            tickerAnnouncementHolderData.value = ticker
//            view?.updateTickerAnnouncementMessage()
            analyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
                ticker.id
            )
        }
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
                    enhancedECommerceProductCartMapData.setDimension12(
                        courierItemData?.selectedShipper?.shipperPrice?.toString() ?: ""
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

    override fun doValidateUseLogisticPromo(
        cartPosition: Int,
        cartString: String,
        validateUsePromoRequest: ValidateUsePromoRequest,
        promoCode: String,
        showLoading: Boolean
    ) {
        if (view != null) {
            couponStateChanged = true
            if (showLoading) {
                view?.setStateLoadingCourierStateAtIndex(cartPosition, true)
            }
            shipmentButtonPayment.value = shipmentButtonPayment.value.copy(loading = true)
            viewModelScope.launch(dispatchers.immediate) {
                try {
                    setValidateUseBoCodeInOneOrderOwoc(validateUsePromoRequest)
                    val validateUsePromoRevampUiModel =
                        validateUsePromoRevampUseCase.setParam(validateUsePromoRequest)
                            .executeOnBackground()
                    if (view != null) {
                        view?.setStateLoadingCourierStateAtIndex(
                            cartPosition,
                            false
                        )
                        this@ShipmentPresenter.validateUsePromoRevampUiModel =
                            validateUsePromoRevampUiModel
                        updateTickerAnnouncementData(validateUsePromoRevampUiModel)
                        showErrorValidateUseIfAny(validateUsePromoRevampUiModel)
                        validateBBOWithSpecificOrder(
                            validateUsePromoRevampUiModel,
                            cartString,
                            promoCode
                        )
                        val isValidatePromoRevampSuccess =
                            validateUsePromoRevampUiModel.status.equals(
                                statusOK,
                                ignoreCase = true
                            ) && validateUsePromoRevampUiModel.errorCode == statusCode200
                        if (isValidatePromoRevampSuccess) {
                            view?.updateButtonPromoCheckout(
                                validateUsePromoRevampUiModel.promoUiModel,
                                true
                            )
                        } else {
                            if (validateUsePromoRevampUiModel.message.isNotEmpty()) {
                                val errMessage =
                                    validateUsePromoRevampUiModel.message[0]
                                mTrackerShipment.eventClickLanjutkanTerapkanPromoError(
                                    errMessage
                                )
                                eventCheckoutViewPromoMessage(errMessage)
                                view?.showToastError(errMessage)
                                view?.resetCourier(cartPosition)
                                view?.getShipmentCartItemModel(cartPosition)?.let {
                                    if (it.boCode.isNotEmpty()) {
                                        clearCacheAutoApply(it, promoCode)
                                        clearOrderPromoCodeFromLastValidateUseRequest(
                                            cartString,
                                            promoCode
                                        )
                                        it.boCode = ""
                                    }
                                }
                            } else {
                                view?.showToastError(
                                    DEFAULT_ERROR_MESSAGE_FAIL_APPLY_BBO
                                )
                                view?.resetCourier(cartPosition)
                                view?.getShipmentCartItemModel(cartPosition)?.let {
                                    if (it.boCode.isNotEmpty()) {
                                        clearCacheAutoApply(it, promoCode)
                                        clearOrderPromoCodeFromLastValidateUseRequest(
                                            cartString,
                                            promoCode
                                        )
                                        it.boCode = ""
                                    }
                                }
                            }
                        }
                        shipmentButtonPayment.value =
                            shipmentButtonPayment.value.copy(loading = false)
                    }
                    logisticDonePublisher?.onCompleted()
                    logisticPromoDonePublisher?.onCompleted()
                    val shipmentScheduleDeliveryMapData =
                        getScheduleDeliveryMapData(cartString)
                    if (shipmentScheduleDeliveryMapData != null && shipmentScheduleDeliveryMapData.shouldStopInValidateUsePromo) {
                        shipmentScheduleDeliveryMapData.donePublisher.onCompleted()
                    }
                } catch (e: Throwable) {
                    Timber.d(e)
                    if (view != null) {
                        view?.setStateLoadingCourierStateAtIndex(
                            cartPosition,
                            false
                        )
                        mTrackerShipment.eventClickLanjutkanTerapkanPromoError(e.message)
                        if (e is AkamaiErrorException) {
                            clearAllPromo()
                            view?.showToastError(e.message)
                            view?.resetAllCourier()
                            view?.cancelAllCourierPromo()
                            view?.doResetButtonPromoCheckout()
                        } else {
                            view?.showToastError(e.message)
                            view?.resetCourier(cartPosition)
                            view?.getShipmentCartItemModel(cartPosition)?.let {
                                if (it.boCode.isNotEmpty()) {
                                    clearCacheAutoApply(it, promoCode)
                                    clearOrderPromoCodeFromLastValidateUseRequest(
                                        cartString,
                                        promoCode
                                    )
                                    it.boCode = ""
                                }
                            }
                        }
                        view?.logOnErrorApplyBo(e, cartPosition, promoCode)
                        shipmentButtonPayment.value =
                            shipmentButtonPayment.value.copy(loading = false)
                        logisticDonePublisher?.onCompleted()
                        logisticPromoDonePublisher?.onCompleted()
                        val shipmentScheduleDeliveryMapData =
                            getScheduleDeliveryMapData(cartString)
                        if (shipmentScheduleDeliveryMapData != null && shipmentScheduleDeliveryMapData.shouldStopInValidateUsePromo) {
                            shipmentScheduleDeliveryMapData.donePublisher.onCompleted()
                        }
                    }
                }
            }
        }
    }

    fun doValidateUseLogisticPromoNew(
        cartPosition: Int,
        cartString: String,
        validateUsePromoRequest: ValidateUsePromoRequest,
        promoCode: String,
        showLoading: Boolean,
        newCourierItemData: CourierItemData
    ) {
        if (view != null) {
            couponStateChanged = true
            if (showLoading) {
                view?.setStateLoadingCourierStateAtIndex(cartPosition, true)
            }
            shipmentButtonPayment.value = shipmentButtonPayment.value.copy(loading = true)
            viewModelScope.launch(dispatchers.immediate) {
                doValidateLogisticPromoForOneOrder(
                    validateUsePromoRequest,
                    cartPosition,
                    cartString,
                    promoCode,
                    newCourierItemData
                )
                shipmentButtonPayment.value =
                    shipmentButtonPayment.value.copy(loading = false)
            }
        }
    }

    private suspend fun doValidateLogisticPromoForOneOrder(
        validateUsePromoRequest: ValidateUsePromoRequest,
        cartPosition: Int,
        cartString: String,
        promoCode: String,
        newCourierItemData: CourierItemData
    ) {
        try {
            setValidateUseBoCodeInOneOrderOwoc(validateUsePromoRequest)
            val validateUsePromoRevampUiModel =
                validateUsePromoRevampUseCase.setParam(validateUsePromoRequest)
                    .executeOnBackground()
            if (view != null) {
                view?.setStateLoadingCourierStateAtIndex(
                    cartPosition,
                    false
                )
                this@ShipmentPresenter.validateUsePromoRevampUiModel =
                    validateUsePromoRevampUiModel
                updateTickerAnnouncementData(validateUsePromoRevampUiModel)
                showErrorValidateUseIfAny(validateUsePromoRevampUiModel)
                validateBBOWithSpecificOrder(
                    validateUsePromoRevampUiModel,
                    cartString,
                    promoCode,
                    newCourierItemData,
                    cartPosition
                )
                val isValidatePromoRevampSuccess =
                    validateUsePromoRevampUiModel.status.equals(
                        statusOK,
                        ignoreCase = true
                    ) && validateUsePromoRevampUiModel.errorCode == statusCode200
                if (isValidatePromoRevampSuccess) {
                    view?.updateButtonPromoCheckout(
                        validateUsePromoRevampUiModel.promoUiModel,
                        true
                    )
                } else {
                    if (validateUsePromoRevampUiModel.message.isNotEmpty()) {
                        val errMessage =
                            validateUsePromoRevampUiModel.message[0]
                        mTrackerShipment.eventClickLanjutkanTerapkanPromoError(
                            errMessage
                        )
                        eventCheckoutViewPromoMessage(errMessage)
                        view?.showToastError(errMessage)
                        view?.resetCourier(cartPosition)
                        view?.getShipmentCartItemModel(cartPosition)?.let {
                            if (it.boCode.isNotEmpty()) {
                                clearCacheAutoApply(it, promoCode)
                                clearOrderPromoCodeFromLastValidateUseRequest(
                                    cartString,
                                    promoCode
                                )
                                it.boCode = ""
                            }
                        }
                    } else {
                        view?.showToastError(
                            DEFAULT_ERROR_MESSAGE_FAIL_APPLY_BBO
                        )
                        view?.resetCourier(cartPosition)
                        view?.getShipmentCartItemModel(cartPosition)?.let {
                            if (it.boCode.isNotEmpty()) {
                                clearCacheAutoApply(it, promoCode)
                                clearOrderPromoCodeFromLastValidateUseRequest(
                                    cartString,
                                    promoCode
                                )
                                it.boCode = ""
                            }
                        }
                    }
                }
            }
            logisticDonePublisher?.onCompleted()
            logisticPromoDonePublisher?.onCompleted()
            val shipmentScheduleDeliveryMapData =
                getScheduleDeliveryMapData(cartString)
            if (shipmentScheduleDeliveryMapData != null && shipmentScheduleDeliveryMapData.shouldStopInValidateUsePromo) {
                shipmentScheduleDeliveryMapData.donePublisher.onCompleted()
            }
        } catch (e: Throwable) {
            Timber.d(e)
            if (view != null) {
                view?.setStateLoadingCourierStateAtIndex(
                    cartPosition,
                    false
                )
                mTrackerShipment.eventClickLanjutkanTerapkanPromoError(e.message)
                if (e is AkamaiErrorException) {
                    clearAllPromo()
                    view?.showToastError(e.message)
                    view?.resetAllCourier()
                    view?.cancelAllCourierPromo()
                    view?.doResetButtonPromoCheckout()
                } else {
                    view?.showToastError(e.message)
                    view?.resetCourier(cartPosition)
                    view?.getShipmentCartItemModel(cartPosition)?.let {
                        if (it.boCode.isNotEmpty()) {
                            clearCacheAutoApply(it, promoCode)
                            clearOrderPromoCodeFromLastValidateUseRequest(
                                cartString,
                                promoCode
                            )
                            it.boCode = ""
                        }
                    }
                }
                view?.logOnErrorApplyBo(e, cartPosition, promoCode)
                logisticDonePublisher?.onCompleted()
                logisticPromoDonePublisher?.onCompleted()
                val shipmentScheduleDeliveryMapData =
                    getScheduleDeliveryMapData(cartString)
                if (shipmentScheduleDeliveryMapData != null && shipmentScheduleDeliveryMapData.shouldStopInValidateUsePromo) {
                    shipmentScheduleDeliveryMapData.donePublisher.onCompleted()
                }
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
        for (voucherOrder in validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels) {
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
                            view?.resetCourier(shipmentCartItemModel)
                            view?.logOnErrorApplyBo(
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
    }

    private fun validateBBOWithSpecificOrder(
        validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel,
        cartString: String?,
        promoCode: String
    ) {
        var orderFound = false
        for (voucherOrder in validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels) {
            if (voucherOrder.cartStringGroup == cartString && voucherOrder.code.equals(
                    promoCode,
                    ignoreCase = true
                )
            ) {
                orderFound = true
            }
            if (voucherOrder.type.equals(
                    "logistic",
                    ignoreCase = true
                ) && voucherOrder.messageUiModel.state.equals("red", ignoreCase = true)
            ) {
                for (shipmentCartItemModel in shipmentCartItemModelList) {
                    if (shipmentCartItemModel is ShipmentCartItemModel && shipmentCartItemModel.cartStringGroup == voucherOrder.cartStringGroup) {
                        if (view != null) {
                            view?.resetCourier(shipmentCartItemModel)
                            view?.logOnErrorApplyBo(
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
                        view?.resetCourier(shipmentCartItemModel)
                        view?.logOnErrorApplyBo(
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

    private fun validateBBOWithSpecificOrder(
        validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel,
        cartString: String?,
        promoCode: String,
        recommendedCourier: CourierItemData,
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
                            if (view != null) {
                                view?.setSelectedCourier(
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
                            view?.resetCourier(shipmentCartItemModel)
                            view?.logOnErrorApplyBo(
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
                        view?.resetCourier(shipmentCartItemModel)
                        view?.logOnErrorApplyBo(
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

    override fun processCheckPromoCheckoutCodeFromSelectedCourier(
        promoCode: String?,
        itemPosition: Int,
        noToast: Boolean
    ) {
        couponStateChanged = true
        val validateUsePromoRequest = generateValidateUsePromoRequest()
        viewModelScope.launch(dispatchers.immediate) {
            try {
                setValidateUseBoCodeInOneOrderOwoc(validateUsePromoRequest)
                val validateUsePromoRevampUiModel =
                    validateUsePromoRevampUseCase.setParam(validateUsePromoRequest)
                        .executeOnBackground()
                this@ShipmentPresenter.validateUsePromoRevampUiModel =
                    validateUsePromoRevampUiModel
                couponStateChanged = true
                if (view != null) {
                    updateTickerAnnouncementData(validateUsePromoRevampUiModel)
                    showErrorValidateUseIfAny(validateUsePromoRevampUiModel)
                    validateBBO(validateUsePromoRevampUiModel)
                    val isValidatePromoRevampSuccess =
                        validateUsePromoRevampUiModel.status.equals(
                            statusOK,
                            ignoreCase = true
                        ) && validateUsePromoRevampUiModel.errorCode == statusCode200
                    if (isValidatePromoRevampSuccess) {
                        view?.renderPromoCheckoutFromCourierSuccess(
                            validateUsePromoRevampUiModel,
                            itemPosition,
                            noToast
                        )
                    } else {
                        if (validateUsePromoRevampUiModel.message.isNotEmpty()) {
                            val errMessage = validateUsePromoRevampUiModel.message[0]
                            view?.renderErrorCheckPromoShipmentData(errMessage)
                        } else {
                            view?.renderErrorCheckPromoShipmentData(
                                DEFAULT_ERROR_MESSAGE_VALIDATE_PROMO
                            )
                        }
                    }
                }
            } catch (e: Throwable) {
                Timber.d(e)
                if (view != null) {
                    if (e is AkamaiErrorException) {
                        clearAllPromo()
                        view?.showToastError(e.message)
                        view?.resetAllCourier()
                        view?.cancelAllCourierPromo()
                        view?.doResetButtonPromoCheckout()
                    } else {
                        view?.showToastError(
                            getErrorMessage(
                                view?.activityContext,
                                e
                            )
                        )
                    }
                }
            }
        }
    }

    override fun generateCheckoutRequest(): Carts {
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

    override fun processSaveShipmentState(shipmentCartItemModel: ShipmentCartItemModel) {
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

    override fun processSaveShipmentState() {
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

    override fun editAddressPinpoint(
        latitude: String,
        longitude: String,
        shipmentCartItemModel: ShipmentCartItemModel?,
        locationPass: LocationPass?
    ) {
        if (view != null) {
            view?.showLoading()
            view?.setHasRunningApiCall(true)
            val requestParams =
                generateEditAddressRequestParams(latitude, longitude)
            compositeSubscription.add(
                editAddressUseCase.createObservable(requestParams)
                    .subscribeOn(executorSchedulers.io)
                    .observeOn(executorSchedulers.main)
                    .unsubscribeOn(executorSchedulers.io)
                    .subscribe(object : Subscriber<String>() {
                        override fun onCompleted() {
                            /* no-op */
                        }

                        override fun onError(e: Throwable) {
                            Timber.d(e)
                            if (view != null) {
                                view?.setHasRunningApiCall(false)
                                view?.hideLoading()
                                view?.showToastError(
                                    getErrorMessage(
                                        view?.activityContext,
                                        e
                                    )
                                )
                            }
                        }

                        override fun onNext(stringResponse: String) {
                            if (view != null) {
                                view?.setHasRunningApiCall(false)
                                view?.hideLoading()
                                var response: JsonObject? = null
                                var messageError = ""
                                var statusSuccess: Boolean
                                try {
                                    response = JsonParser().parse(stringResponse).asJsonObject
                                    val statusCode =
                                        response.asJsonObject.getAsJsonObject(EditAddressUseCase.RESPONSE_DATA)[EditAddressUseCase.RESPONSE_IS_SUCCESS].asInt
                                    statusSuccess = statusCode == 1
                                    if (!statusSuccess) {
                                        messageError =
                                            response.getAsJsonArray("message_error")[0].asString
                                    }
                                } catch (e: Exception) {
                                    Timber.d(e)
                                    statusSuccess = false
                                }
                                if (response != null && statusSuccess) {
                                    recipientAddressModel.latitude = latitude
                                    recipientAddressModel.longitude = longitude
                                    view?.renderEditAddressSuccess(latitude, longitude)
                                } else {
                                    if (isNullOrEmpty(messageError)) {
                                        messageError =
                                            view?.activityContext?.getString(com.tokopedia.abstraction.R.string.default_request_error_unknown)
                                                ?: ""
                                    }
                                    view?.navigateToSetPinpoint(messageError, locationPass)
                                }
                            }
                        }
                    })
            )
        }
    }

    private fun generateEditAddressRequestParams(
        addressLatitude: String,
        addressLongitude: String
    ): RequestParams {
        val params: MutableMap<String, String> = generateParamsNetwork(
            userSessionInterface.userId,
            userSessionInterface.deviceId,
            TKPDMapParam()
        )
        params[EditAddressParam.ADDRESS_ID] = recipientAddressModel.id
        params[EditAddressParam.ADDRESS_NAME] = recipientAddressModel.addressName
        params[EditAddressParam.ADDRESS_STREET] =
            recipientAddressModel.street
        params[EditAddressParam.POSTAL_CODE] = recipientAddressModel.postalCode
        params[EditAddressParam.DISTRICT_ID] = recipientAddressModel.destinationDistrictId
        params[EditAddressParam.CITY_ID] = recipientAddressModel.cityId
        params[EditAddressParam.PROVINCE_ID] = recipientAddressModel.provinceId
        params[EditAddressParam.LATITUDE] = addressLatitude
        params[EditAddressParam.LONGITUDE] = addressLongitude
        params[EditAddressParam.RECEIVER_NAME] = recipientAddressModel.recipientName
        params[EditAddressParam.RECEIVER_PHONE] = recipientAddressModel.recipientPhoneNumber
        val requestParams = RequestParams.create()
        requestParams.putAllString(params)
        return requestParams
    }

    // Clear promo BBO after choose other / non BBO courier
    override fun cancelAutoApplyPromoStackLogistic(
        itemPosition: Int,
        promoCode: String,
        shipmentCartItemModel: ShipmentCartItemModel
    ) {
        couponStateChanged = true
        val promoCodeList = ArrayList<String>()
        promoCodeList.add(promoCode)
        val clearOrders = ArrayList<ClearPromoOrder>()
        clearOrders.add(
            ClearPromoOrder(
                shipmentCartItemModel.cartStringGroup,
                shipmentCartItemModel.shipmentCartData.boMetadata.boType,
                promoCodeList,
                shipmentCartItemModel.shopId,
                shipmentCartItemModel.isProductIsPreorder,
                shipmentCartItemModel.cartItemModels[0].preOrderDurationDay.toString(),
                shipmentCartItemModel.fulfillmentId
            )
        )
        viewModelScope.launch(dispatchers.immediate) {
            try {
                val responseData = clearCacheAutoApplyStackUseCase.setParams(
                    ClearPromoRequest(
                        ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
                        false,
                        ClearPromoOrderData(
                            ArrayList(),
                            clearOrders
                        )
                    )
                ).executeOnBackground()
                if (view != null) {
                    if (responseData.successDataModel.tickerMessage.isNotEmpty()) {
                        val ticker = tickerAnnouncementHolderData.value
                        ticker.title = ""
                        ticker.message =
                            responseData.successDataModel.tickerMessage
                        tickerAnnouncementHolderData.value = ticker
//                        view?.updateTickerAnnouncementMessage()
                    }
                    val isLastAppliedPromo = isLastAppliedPromo(promoCode)
                    if (isLastAppliedPromo) {
                        validateUsePromoRevampUiModel = null
                    }
                    view?.onSuccessClearPromoLogistic(itemPosition, isLastAppliedPromo)
                }
                val shipmentScheduleDeliveryMapData = getScheduleDeliveryMapData(
                    shipmentCartItemModel.cartStringGroup
                )
                if (shipmentScheduleDeliveryMapData != null && shipmentScheduleDeliveryMapData.shouldStopInClearCache) {
                    shipmentScheduleDeliveryMapData.donePublisher.onCompleted()
                }
            } catch (t: Throwable) {
                val shipmentScheduleDeliveryMapData = getScheduleDeliveryMapData(
                    shipmentCartItemModel.cartStringGroup
                )
                if (shipmentScheduleDeliveryMapData != null && shipmentScheduleDeliveryMapData.shouldStopInClearCache) {
                    shipmentScheduleDeliveryMapData.donePublisher.onCompleted()
                }
            }
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
    override fun cancelNotEligiblePromo(notEligiblePromoHolderdataArrayList: ArrayList<NotEligiblePromoHolderdata>) {
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
                        if (shipmentCartItemModel is ShipmentCartItemModel && shipmentCartItemModel.cartStringGroup == notEligiblePromo.uniqueId) {
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
                                    shipmentCartItemModel.fulfillmentId
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
                    view?.removeIneligiblePromo(notEligiblePromoHolderdataArrayList)
                } catch (t: Throwable) {
                    Timber.d(t)
                    view?.removeIneligiblePromo(notEligiblePromoHolderdataArrayList)
                }
            }
        }
    }

    // Clear promo after clash (rare, almost zero probability)
    override fun cancelAutoApplyPromoStackAfterClash(clashingInfoDetailUiModel: ClashingInfoDetailUiModel) {
        val globalPromoCode = ArrayList<String>()
        val clearOrders = ArrayList<ClearPromoOrder>()
        for (promoClashOptionUiModel in clashingInfoDetailUiModel.options) {
            for (voucherOrder in promoClashOptionUiModel.voucherOrders) {
                if (voucherOrder.uniqueId.isEmpty()) {
                    if (!globalPromoCode.contains(voucherOrder.code)) {
                        globalPromoCode.add(voucherOrder.code)
                    }
                } else {
                    val order = getClearPromoOrderByUniqueId(clearOrders, voucherOrder.uniqueId)
                    if (order != null) {
                        if (!order.codes.contains(voucherOrder.code)) {
                            order.codes.add(voucherOrder.code)
                        }
                    } else {
                        var cartItemModel: ShipmentCartItemModel? = null
                        for (shipmentCartItemModel in shipmentCartItemModelList) {
                            if (shipmentCartItemModel is ShipmentCartItemModel && shipmentCartItemModel.cartStringGroup == voucherOrder.uniqueId) {
                                cartItemModel = shipmentCartItemModel
                                break
                            }
                        }
                        if (cartItemModel != null) {
                            val codes = ArrayList<String>()
                            codes.add(voucherOrder.code)
                            clearOrders.add(
                                ClearPromoOrder(
                                    voucherOrder.uniqueId,
                                    cartItemModel.shipmentCartData.boMetadata.boType,
                                    codes,
                                    cartItemModel.shopId,
                                    cartItemModel.isProductIsPreorder,
                                    cartItemModel.cartItemModels[0].preOrderDurationDay.toString(),
                                    cartItemModel.fulfillmentId
                                )
                            )
                        }
                    }
                }
            }
        }
        couponStateChanged = true
        view?.showLoading()
        view?.setHasRunningApiCall(true)
        viewModelScope.launch(dispatchers.immediate) {
            try {
                clearCacheAutoApplyStackUseCase.setParams(
                    ClearPromoRequest(
                        ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
                        false,
                        ClearPromoOrderData(globalPromoCode, clearOrders)
                    )
                ).executeOnBackground()
                view?.hideLoading()
                view?.setHasRunningApiCall(false)
                view?.showToastNormal("Ada perubahan pada promo yang kamu pakai")
            } catch (t: Throwable) {
                view?.hideLoading()
                view?.setHasRunningApiCall(false)
            }
        }
    }

    override fun hitClearAllBo() {
        val clearOrders = ArrayList<ClearPromoOrder>()
        var hasBo = false
        for (shipmentCartItemModel in shipmentCartItemModelList) {
            if (shipmentCartItemModel is ShipmentCartItemModel && shipmentCartItemModel.voucherLogisticItemUiModel != null && shipmentCartItemModel.voucherLogisticItemUiModel!!.code.isNotEmpty()) {
                val boCodes = ArrayList<String>()
                boCodes.add(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)
                clearOrders.add(
                    ClearPromoOrder(
                        shipmentCartItemModel.cartStringGroup,
                        shipmentCartItemModel.shipmentCartData.boMetadata.boType,
                        boCodes,
                        shipmentCartItemModel.shopId,
                        shipmentCartItemModel.isProductIsPreorder,
                        shipmentCartItemModel.cartItemModels[0].preOrderDurationDay.toString(),
                        shipmentCartItemModel.fulfillmentId
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

    override fun changeShippingAddress(
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
        compositeSubscription.add(
            changeShippingAddressGqlUseCase.createObservable(requestParam)
                .subscribeOn(executorSchedulers.io)
                .observeOn(executorSchedulers.main)
                .unsubscribeOn(executorSchedulers.io)
                .subscribe(object : Subscriber<SetShippingAddressData>() {
                    override fun onCompleted() {}
                    override fun onError(e: Throwable) {
                        if (view != null) {
                            view?.hideLoading()
                            view?.setHasRunningApiCall(false)
                            Timber.d(e)
                            val errorMessage: String? = if (e is AkamaiErrorException) {
                                e.message
                            } else {
                                getErrorMessage(
                                    view?.activityContext,
                                    e
                                )
                            }
                            view?.showToastError(errorMessage)
                            if (isHandleFallback) {
                                view?.renderChangeAddressFailed(reloadCheckoutPage)
                            }
                        }
                    }

                    override fun onNext(setShippingAddressData: SetShippingAddressData) {
                        if (view != null) {
                            view?.hideLoading()
                            view?.setHasRunningApiCall(false)
                            if (setShippingAddressData.isSuccess) {
                                if (setShippingAddressData.messages.isEmpty()) {
                                    view?.showToastNormal(view?.activityContext!!.getString(R.string.label_change_address_success))
                                } else {
                                    view?.showToastNormal(setShippingAddressData.messages[0])
                                }
                                hitClearAllBo()
                                view?.renderChangeAddressSuccess(reloadCheckoutPage)
                            } else {
                                if (setShippingAddressData.messages.isNotEmpty()) {
                                    val stringBuilder = StringBuilder()
                                    for (errorMessage in setShippingAddressData.messages) {
                                        stringBuilder.append(errorMessage).append(" ")
                                    }
                                    view?.showToastError(stringBuilder.toString())
                                    if (isHandleFallback) {
                                        view?.renderChangeAddressFailed(reloadCheckoutPage)
                                    }
                                } else {
                                    view?.showToastError(view?.activityContext?.getString(R.string.label_change_address_failed))
                                    if (isHandleFallback) {
                                        view?.renderChangeAddressFailed(reloadCheckoutPage)
                                    }
                                }
                            }
                        }
                    }
                })
        )
    }

    override fun processGetCourierRecommendation(
        shipperId: Int,
        spId: Int,
        itemPosition: Int,
        shipmentDetailData: ShipmentDetailData?,
        shipmentCartItemModel: ShipmentCartItemModel,
        shopShipmentList: List<ShopShipment>,
        isInitialLoad: Boolean,
        products: ArrayList<Product>,
        cartString: String?,
        isTradeInDropOff: Boolean,
        recipientAddressModel: RecipientAddressModel?,
        isForceReload: Boolean,
        skipMvc: Boolean,
        cartStringGroup: String,
        groupProducts: List<GroupProduct>
    ) {
        val shippingParam = getShippingParam(
            shipmentDetailData,
            products,
            cartString,
            isTradeInDropOff,
            recipientAddressModel,
            cartStringGroup,
            groupProducts
        )
        val counter = if (codData == null) -1 else codData!!.counterCod
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
        val observable: Observable<ShippingRecommendationData>
        if (isTradeInDropOff) {
            observable = ratesApiUseCase.execute(param)
            compositeSubscription.add(
                observable
                    .map { shippingRecommendationData: ShippingRecommendationData? ->
                        stateConverter.fillState(
                            shippingRecommendationData!!,
                            shopShipmentList,
                            spId,
                            0
                        )
                    }
                    .subscribe(
                        GetCourierRecommendationSubscriber(
                            view!!, this, shipperId, spId, itemPosition,
                            shippingCourierConverter, shipmentCartItemModel,
                            isInitialLoad, isTradeInDropOff, isForceReload, isBoUnstackEnabled, null
                        )
                    )
            )
        } else {
            if (ratesPublisher == null) {
                ratesPublisher = PublishSubject.create()
                compositeSubscription.add(
                    ratesPublisher?.concatMap { (shipperId1, spId1, itemPosition1, shipmentCartItemModel1, shopShipmentList1, isInitialLoad1, _, isTradeInDropOff1, isForceReload1, ratesParam): ShipmentGetCourierHolderData ->
                        logisticDonePublisher = PublishSubject.create()
                        if (shipmentCartItemModel.ratesValidationFlow) {
                            ratesWithScheduleUseCase.execute(
                                ratesParam,
                                shipmentCartItemModel1.fulfillmentId.toString()
                            )
                                .map { shippingRecommendationData: ShippingRecommendationData? ->
                                    stateConverter.fillState(
                                        shippingRecommendationData!!,
                                        shopShipmentList1,
                                        spId1,
                                        0
                                    )
                                }.subscribe(
                                    GetScheduleDeliveryCourierRecommendationSubscriber(
                                        view!!, this, shipperId1, spId1, itemPosition1,
                                        shippingCourierConverter, shipmentCartItemModel1,
                                        isInitialLoad1, isForceReload1, isBoUnstackEnabled,
                                        logisticDonePublisher
                                    )
                                )
                        } else {
                            ratesUseCase.execute(ratesParam)
                                .map { shippingRecommendationData: ShippingRecommendationData? ->
                                    stateConverter.fillState(
                                        shippingRecommendationData!!,
                                        shopShipmentList1,
                                        spId1,
                                        0
                                    )
                                }.subscribe(
                                    GetCourierRecommendationSubscriber(
                                        view!!,
                                        this,
                                        shipperId1,
                                        spId1,
                                        itemPosition1,
                                        shippingCourierConverter,
                                        shipmentCartItemModel1,
                                        isInitialLoad1,
                                        isTradeInDropOff1,
                                        isForceReload1,
                                        isBoUnstackEnabled,
                                        logisticDonePublisher
                                    )
                                )
                        }
                        logisticDonePublisher
                    }
                        ?.subscribe()
                )
            }
            shipmentButtonPayment.value = shipmentButtonPayment.value.copy(loading = true)
            ratesQueue.offer(
                ShipmentGetCourierHolderData(
                    shipperId,
                    spId,
                    itemPosition,
                    shipmentCartItemModel,
                    shopShipmentList,
                    isInitialLoad,
                    "",
                    isTradeInDropOff,
                    isForceReload,
                    param,
                    ""
                )
            )
            viewModelScope.launch {
                consumeRatesQueue()
            }
//            ratesPublisher!!.onNext(
//                ShipmentGetCourierHolderData(
//                    shipperId,
//                    spId,
//                    itemPosition,
//                    shipmentCartItemModel,
//                    shopShipmentList,
//                    isInitialLoad,
//                    "",
//                    isTradeInDropOff,
//                    isForceReload,
//                    param,
//                    ""
//                )
//            )
        }
    }

    private suspend fun consumeRatesQueue() {
        var itemToProcess = ratesQueue.poll()
        loopProcess@while (itemToProcess != null) {
            val shipmentGetCourierHolderData = itemToProcess
            if (shipmentGetCourierHolderData.shipmentCartItemModel.ratesValidationFlow) {
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
                    }.subscribe(
                        GetScheduleDeliveryCourierRecommendationSubscriber(
                            view!!, this, shipmentGetCourierHolderData.shipperId, shipmentGetCourierHolderData.spId, shipmentGetCourierHolderData.itemPosition,
                            shippingCourierConverter, shipmentGetCourierHolderData.shipmentCartItemModel,
                            shipmentGetCourierHolderData.isInitialLoad, shipmentGetCourierHolderData.isForceReload, isBoUnstackEnabled,
                            logisticDonePublisher
                        )
                    )
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
                    val boPromoCode = getBoPromoCode(shipmentGetCourierHolderData.isForceReload, shipmentGetCourierHolderData.shipmentCartItemModel)
                    var errorReason = "rates invalid data"
                    if (shipmentGetCourierHolderData.isInitialLoad || shipmentGetCourierHolderData.isForceReload) {
                        if (shipmentGetCourierHolderData.isInitialLoad && shipmentGetCourierHolderData.shipmentCartItemModel.shouldResetCourier) {
                            shipmentGetCourierHolderData.shipmentCartItemModel.shouldResetCourier = false
                            error("racing condition against epharmacy validation")
                        }
                        if (shippingRecommendationData.shippingDurationUiModels.isNotEmpty()) {
                            if (!shipmentGetCourierHolderData.isForceReload && isBoUnstackEnabled && shipmentGetCourierHolderData.shipmentCartItemModel.boCode.isNotEmpty()) {
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
                                                            shipmentGetCourierHolderData.isTradeInDropOff,
                                                            false
                                                        )
                                                        view?.logOnErrorLoadCourier(
                                                            MessageErrorException(
                                                                shippingCourierUiModel.productData.error?.errorMessage
                                                            ),
                                                            shipmentGetCourierHolderData.itemPosition,
                                                            boPromoCode
                                                        )
                                                        logisticDonePublisher?.onCompleted()
                                                        itemToProcess = ratesQueue.poll()
                                                        continue@loopProcess
                                                    } else {
                                                        shippingCourierUiModel.isSelected = true
                                                        setShippingCourierViewModelsState(
                                                            shippingDurationUiModel.shippingCourierViewModelList,
                                                            shipmentGetCourierHolderData.shipmentCartItemModel.orderNumber
                                                        )
                                                        view?.renderCourierStateSuccess(
                                                            generateCourierItemData(
                                                                shippingCourierUiModel,
                                                                shippingRecommendationData,
                                                                logisticPromo
                                                            ),
                                                            shipmentGetCourierHolderData.itemPosition,
                                                            shipmentGetCourierHolderData.isTradeInDropOff,
                                                            shipmentGetCourierHolderData.isForceReload
                                                        )
                                                        itemToProcess = ratesQueue.poll()
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
                                        val selectedSpId = getSelectedSpId(shipmentGetCourierHolderData.shipmentCartItemModel, shipmentGetCourierHolderData.spId, shippingDurationUiModel)
                                        for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                            if (shipmentGetCourierHolderData.isTradeInDropOff || (shippingCourierUiModel.productData.shipperProductId == selectedSpId && !shippingCourierUiModel.serviceData.isUiRatesHidden)) {
                                                if (!shippingCourierUiModel.productData.error?.errorMessage.isNullOrEmpty()) {
                                                    view?.renderCourierStateFailed(
                                                        shipmentGetCourierHolderData.itemPosition,
                                                        shipmentGetCourierHolderData.isTradeInDropOff,
                                                        false
                                                    )
                                                    view?.logOnErrorLoadCourier(
                                                        MessageErrorException(
                                                            shippingCourierUiModel.productData.error?.errorMessage
                                                        ),
                                                        shipmentGetCourierHolderData.itemPosition,
                                                        boPromoCode
                                                    )
                                                    logisticDonePublisher?.onCompleted()
                                                    itemToProcess = ratesQueue.poll()
                                                    continue@loopProcess
                                                } else {
                                                    val courierItemData = generateCourierItemData(
                                                        shipmentGetCourierHolderData.isForceReload,
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
                                                            shipmentGetCourierHolderData.isTradeInDropOff,
                                                            false
                                                        )
                                                        view?.logOnErrorLoadCourier(
                                                            MessageErrorException("rates ui hidden but no promo"),
                                                            shipmentGetCourierHolderData.itemPosition,
                                                            boPromoCode
                                                        )
                                                        logisticDonePublisher?.onCompleted()
                                                        itemToProcess = ratesQueue.poll()
                                                        continue@loopProcess
                                                    }
                                                    shippingCourierUiModel.isSelected = true
                                                    setShippingCourierViewModelsState(
                                                        shippingDurationUiModel.shippingCourierViewModelList,
                                                        shipmentGetCourierHolderData.shipmentCartItemModel.orderNumber
                                                    )
                                                    view?.renderCourierStateSuccess(
                                                        courierItemData,
                                                        shipmentGetCourierHolderData.itemPosition,
                                                        shipmentGetCourierHolderData.isTradeInDropOff,
                                                        shipmentGetCourierHolderData.isForceReload
                                                    )
                                                    itemToProcess = ratesQueue.poll()
                                                    continue@loopProcess
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
                                            shippingCourier.isSelected = true
                                            view?.renderCourierStateSuccess(
                                                generateCourierItemData(
                                                    shipmentGetCourierHolderData.isForceReload,
                                                    shipmentGetCourierHolderData.shipperId,
                                                    shipmentGetCourierHolderData.spId,
                                                    shipmentGetCourierHolderData.shipmentCartItemModel,
                                                    shippingCourier,
                                                    shippingRecommendationData
                                                ),
                                                shipmentGetCourierHolderData.itemPosition,
                                                shipmentGetCourierHolderData.isTradeInDropOff,
                                                shipmentGetCourierHolderData.isForceReload
                                            )
                                            itemToProcess = ratesQueue.poll()
                                            continue@loopProcess
                                        }
                                    }
                                }
                            }
                        } else {
                            errorReason = "rates empty data"
                        }
                        view?.renderCourierStateFailed(shipmentGetCourierHolderData.itemPosition, shipmentGetCourierHolderData.isTradeInDropOff, false)
                        view?.logOnErrorLoadCourier(
                            MessageErrorException(errorReason),
                            shipmentGetCourierHolderData.itemPosition,
                            boPromoCode
                        )
                    } else {
                        if (shippingRecommendationData?.shippingDurationUiModels != null && shippingRecommendationData.shippingDurationUiModels.isNotEmpty()) {
                            for (shippingDurationUiModel in shippingRecommendationData.shippingDurationUiModels) {
                                for (productData in shippingDurationUiModel.serviceData.products) {
                                    if (productData.shipperId == shipmentGetCourierHolderData.shipperId && productData.shipperProductId == shipmentGetCourierHolderData.spId) {
                                        view?.updateCourierBottomssheetHasData(
                                            shippingDurationUiModel.shippingCourierViewModelList,
                                            shipmentGetCourierHolderData.itemPosition,
                                            shipmentGetCourierHolderData.shipmentCartItemModel,
                                            shippingRecommendationData.preOrderModel
                                        )
                                        logisticDonePublisher?.onCompleted()
                                        itemToProcess = ratesQueue.poll()
                                        continue@loopProcess
                                    }
                                }
                            }
                        }
                        view?.updateCourierBottomsheetHasNoData(shipmentGetCourierHolderData.itemPosition, shipmentGetCourierHolderData.shipmentCartItemModel)
                    }
                    logisticDonePublisher?.onCompleted()
                    itemToProcess = ratesQueue.poll()
                } catch (t: Throwable) {
                    Timber.d(t)
                    val boPromoCode = getBoPromoCode(shipmentGetCourierHolderData.isForceReload, shipmentGetCourierHolderData.shipmentCartItemModel)
                    if (shipmentGetCourierHolderData.isInitialLoad) {
                        view?.renderCourierStateFailed(shipmentGetCourierHolderData.itemPosition, shipmentGetCourierHolderData.isTradeInDropOff, false)
                    } else {
                        view?.updateCourierBottomsheetHasNoData(shipmentGetCourierHolderData.itemPosition, shipmentGetCourierHolderData.shipmentCartItemModel)
                    }
                    view?.logOnErrorLoadCourier(t, shipmentGetCourierHolderData.itemPosition, boPromoCode)
                    logisticDonePublisher?.onCompleted()
                    itemToProcess = ratesQueue.poll()
                }
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
            spId
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
            shippingCourierConverter.convertToCourierItemData(shippingCourierUiModel, shippingRecommendationData)

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
            courierItemData = shippingCourierConverter.convertToCourierItemData(courierUiModel, shippingRecommendationData)
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

    private fun getBoPromoCode(isForceReloadRates: Boolean, shipmentCartItemModel: ShipmentCartItemModel): String {
        if (isBoUnstackEnabled && !isForceReloadRates) {
            return shipmentCartItemModel.boCode
        }
        return ""
    }

    override fun generateRatesMvcParam(cartString: String?): String {
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
        recipientAddressModel: RecipientAddressModel?,
        cartStringGroup: String,
        groupProducts: List<GroupProduct>
    ): ShippingParam {
        val shippingParam = ShippingParam()
        shippingParam.originDistrictId = shipmentDetailData!!.shipmentCartData!!.originDistrictId
        shippingParam.originPostalCode = shipmentDetailData.shipmentCartData!!.originPostalCode
        shippingParam.originLatitude = shipmentDetailData.shipmentCartData!!.originLatitude
        shippingParam.originLongitude = shipmentDetailData.shipmentCartData!!.originLongitude
        shippingParam.weightInKilograms = shipmentDetailData.shipmentCartData!!.weight / 1000
        shippingParam.weightActualInKilograms =
            shipmentDetailData.shipmentCartData!!.weightActual / 1000
        shippingParam.shopId = shipmentDetailData.shopId
        shippingParam.shopTier = shipmentDetailData.shipmentCartData!!.shopTier
        shippingParam.token = shipmentDetailData.shipmentCartData!!.token
        shippingParam.ut = shipmentDetailData.shipmentCartData!!.ut
        shippingParam.insurance = shipmentDetailData.shipmentCartData!!.insurance
        shippingParam.productInsurance = shipmentDetailData.shipmentCartData!!.productInsurance
        shippingParam.orderValue = shipmentDetailData.shipmentCartData!!.orderValue
        shippingParam.categoryIds = shipmentDetailData.shipmentCartData!!.categoryIds
        shippingParam.isBlackbox = shipmentDetailData.isBlackbox
        shippingParam.isPreorder = shipmentDetailData.preorder
        shippingParam.addressId = recipientAddressModel!!.id
        shippingParam.isTradein = shipmentDetailData.isTradein
        shippingParam.products = products
        shippingParam.uniqueId = cartString
        shippingParam.isTradeInDropOff = isTradeInDropOff
        shippingParam.preOrderDuration = shipmentDetailData.shipmentCartData!!.preOrderDuration
        shippingParam.isFulfillment = shipmentDetailData.shipmentCartData!!.isFulfillment
        shippingParam.boMetadata = shipmentDetailData.shipmentCartData!!.boMetadata
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
        shippingParam.cartStringGroup = cartStringGroup
        shippingParam.groupProducts = groupProducts
        return shippingParam
    }

    override fun getShippingCourierViewModelsState(orderNumber: Int): List<ShippingCourierUiModel>? {
        return shippingCourierViewModelsState[orderNumber]
    }

    override fun setShippingCourierViewModelsState(
        shippingCourierUiModelsState: List<ShippingCourierUiModel>,
        orderNumber: Int
    ) {
        shippingCourierViewModelsState[orderNumber] = shippingCourierUiModelsState
    }

    override fun getCampaignTimer(): CampaignTimerUi? {
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

    override fun releaseBooking() {
        // As deals product is using OCS, the shipment should only contain 1 product
        val productId =
            getFirstProductId(shipmentCartItemModelList.filterIsInstance(ShipmentCartItemModel::class.java))
        if (productId != 0L) {
            releaseBookingUseCase
                .execute(productId)
                .subscribe(ReleaseBookingStockSubscriber())
        }
    }

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

    override fun fetchEpharmacyData() {
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
                        val position = view?.getShipmentCartItemModelAdapterPositionByCartStringGroup(
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
                                                                            view?.activityContext?.getString(
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
                                                                        if (prescriptionImage != null && !isNullOrEmpty(
                                                                                prescriptionImage.prescriptionId
                                                                            )
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
                                    view?.activityContext?.getString(
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

    override fun setPrescriptionIds(prescriptionIds: ArrayList<String>) {
        for (shipmentCartItemModel in shipmentCartItemModelList) {
            if (shipmentCartItemModel is ShipmentCartItemModel && !shipmentCartItemModel.isError && shipmentCartItemModel.hasEthicalProducts) {
                shipmentCartItemModel.prescriptionIds = prescriptionIds
            }
        }
        uploadPrescriptionUiModel.uploadedImageCount = prescriptionIds.size
    }

    override fun setMiniConsultationResult(results: ArrayList<EPharmacyMiniConsultationResult>) {
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
                                                                    view?.activityContext?.getString(
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
                                                                if (prescriptionImage != null && !isNullOrEmpty(
                                                                        prescriptionImage.prescriptionId
                                                                    )
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
                                view?.activityContext?.getString(
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

    override fun setLatValidateUseRequest(latValidateUseRequest: ValidateUsePromoRequest?) {
        lastValidateUseRequest = latValidateUseRequest
    }

    override fun setUploadPrescriptionData(uploadPrescriptionUiModel: UploadPrescriptionUiModel) {
        this.uploadPrescriptionUiModel = uploadPrescriptionUiModel
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

    override fun updateAddOnProductLevelDataBottomSheet(saveAddOnStateResult: SaveAddOnStateResult) {
        for (addOnResult in saveAddOnStateResult.addOns) {
            for (shipmentCartItemModel in shipmentCartItemModelList) {
                if (shipmentCartItemModel is ShipmentCartItemModel) {
                    val cartItemModelList = shipmentCartItemModel.cartItemModels
                    for (i in cartItemModelList.indices) {
                        val cartItemModel = cartItemModelList[i]
                        val keyProductLevel =
                            "${cartItemModel.cartStringGroup}-${cartItemModel.cartId}"
                        if (keyProductLevel.equals(addOnResult.addOnKey, ignoreCase = true)) {
                            val addOnsDataModel = cartItemModel.addOnProductLevelModel
                            setAddOnsData(
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

    override fun updateAddOnOrderLevelDataBottomSheet(saveAddOnStateResult: SaveAddOnStateResult) {
        for (addOnResult in saveAddOnStateResult.addOns) {
            for (shipmentCartItemModel in shipmentCartItemModelList) {
                if (shipmentCartItemModel is ShipmentCartItemModel && (shipmentCartItemModel.cartStringGroup + "-0").equals(
                        addOnResult.addOnKey,
                        ignoreCase = true
                    )
                ) {
                    val addOnsDataModel = shipmentCartItemModel.addOnsOrderLevelModel
                    setAddOnsData(
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
    private fun setAddOnsData(
        addOnsDataModel: AddOnsDataModel,
        addOnResult: AddOnResult,
        identifier: Int,
        cartString: String,
        cartId: Long
    ) {
        addOnsDataModel.status = addOnResult.status
        val addOnButton = addOnResult.addOnButton
        addOnsDataModel.addOnsButtonModel = AddOnButtonModel(
            addOnButton.leftIconUrl,
            addOnButton.rightIconUrl,
            addOnButton.description,
            addOnButton.action,
            addOnButton.title
        )
        val addOnBottomSheet = addOnResult.addOnBottomSheet
        val addOnBottomSheetModel = AddOnBottomSheetModel()
        addOnBottomSheetModel.headerTitle = addOnBottomSheet.headerTitle
        addOnBottomSheetModel.description = addOnBottomSheet.description
        val addOnTickerModel = AddOnTickerModel()
        addOnTickerModel.text = addOnBottomSheet.ticker.text
        addOnBottomSheetModel.ticker = addOnTickerModel
        val listProductAddOn = ArrayList<AddOnProductItemModel>()
        for (product in addOnBottomSheet.products) {
            val addOnProductItemModel = AddOnProductItemModel()
            addOnProductItemModel.productName = product.productName
            addOnProductItemModel.productImageUrl = product.productImageUrl
            listProductAddOn.add(addOnProductItemModel)
        }
        addOnBottomSheetModel.products = listProductAddOn
        addOnsDataModel.addOnsBottomSheetModel = addOnBottomSheetModel
        val listAddOnDataItem = arrayListOf<AddOnDataItemModel>()
        for (addOnData in addOnResult.addOnData) {
            val addOnDataItemModel = AddOnDataItemModel()
            val addOnNote = addOnData.addOnMetadata.addOnNote
            addOnDataItemModel.addOnId = addOnData.addOnId
            addOnDataItemModel.addOnPrice = addOnData.addOnPrice
            addOnDataItemModel.addOnQty = addOnData.addOnQty.toLong()
            addOnDataItemModel.addOnMetadata = AddOnMetadataItemModel(
                AddOnNoteItemModel(
                    addOnNote.isCustomNote,
                    addOnNote.to,
                    addOnNote.from,
                    addOnNote.notes
                )
            )
            listAddOnDataItem.add(addOnDataItemModel)
        }
        addOnsDataModel.addOnsDataItemModelList = listAddOnDataItem
        view?.updateAddOnsData(addOnsDataModel, identifier, cartString)
        if (isUsingDynamicDataPassing()) {
            view?.updateAddOnsDynamicDataPassing(
                addOnsDataModel,
                addOnResult,
                identifier,
                cartString,
                cartId
            )
        }
    }

    override fun validateBoPromo(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel): Pair<ArrayList<String>, ArrayList<String>> {
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
                    shipmentCartItemModel.voucherLogisticItemUiModel!!.code
                )
                unappliedBoPromoUniqueIds.add(shipmentCartItemModel.cartStringGroup)
                reloadedUniqueIds.add(shipmentCartItemModel.cartStringGroup)
            }
        }
        if (unappliedBoPromoUniqueIds.size > 0) {
            view?.renderUnapplyBoIncompleteShipment(unappliedBoPromoUniqueIds)
        }
        for (voucherOrders in toBeAppliedVoucherOrders) {
            doApplyBo(voucherOrders)
            reloadedUniqueIds.add(voucherOrders.uniqueId)
        }
        return Pair(reloadedUniqueIds, unappliedBoPromoUniqueIds)
    }

    override fun doUnapplyBo(cartStringGroup: String, promoCode: String) {
        val itemAdapterPosition =
            view?.getShipmentCartItemModelAdapterPositionByCartStringGroup(cartStringGroup) ?: -1
        val shipmentCartItemModel = view?.getShipmentCartItemModel(itemAdapterPosition)
        if (shipmentCartItemModel != null && itemAdapterPosition != -1) {
            view?.resetCourier(itemAdapterPosition)
            clearCacheAutoApply(shipmentCartItemModel, promoCode)
            clearOrderPromoCodeFromLastValidateUseRequest(cartStringGroup, promoCode)
            view?.onNeedUpdateViewItem(itemAdapterPosition)
        }
    }

    private fun clearCacheAutoApply(
        shipmentCartItemModel: ShipmentCartItemModel,
        promoCode: String
    ) {
        val globalCodes: List<String> = ArrayList()
        val clearOrders: MutableList<ClearPromoOrder> = ArrayList()
        val promoCodes: MutableList<String> = ArrayList()
        promoCodes.add(promoCode)
        clearOrders.add(
            ClearPromoOrder(
                shipmentCartItemModel.cartStringGroup,
                shipmentCartItemModel.shipmentCartData.boMetadata.boType,
                promoCodes,
                shipmentCartItemModel.shopId,
                shipmentCartItemModel.isProductIsPreorder,
                shipmentCartItemModel.cartItemModels[0].preOrderDurationDay.toString(),
                shipmentCartItemModel.fulfillmentId
            )
        )
        viewModelScope.launch(dispatchers.immediate) {
            try {
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

    override fun clearOrderPromoCodeFromLastValidateUseRequest(
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

    override fun doApplyBo(voucherOrdersItemUiModel: PromoCheckoutVoucherOrdersItemUiModel) {
        val itemAdapterPosition = view?.getShipmentCartItemModelAdapterPositionByCartStringGroup(
            voucherOrdersItemUiModel.cartStringGroup
        ) ?: -1
        val shipmentCartItemModel = view?.getShipmentCartItemModel(itemAdapterPosition)
        val listToProcess =
            arrayListOf<Triple<Int, PromoCheckoutVoucherOrdersItemUiModel, ShipmentCartItemModel>>()
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
        processBoPromoCourierRecommendationNew(listToProcess)
    }

    /*override fun processBoPromoCourierRecommendation(
        itemPosition: Int,
        voucherOrdersItemUiModel: PromoCheckoutVoucherOrdersItemUiModel,
        shipmentCartItemModel: ShipmentCartItemModel
    ) {
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
            recipientAddressModel,
            if (shipmentCartItemModel.groupType == GROUP_TYPE_OWOC) shipmentCartItemModel.cartStringGroup else "",
            if (shipmentCartItemModel.groupType == GROUP_TYPE_OWOC) {
                getGroupProductsForRatesRequest(
                    shipmentCartItemModel
                )
            } else {
                emptyList()
            }
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
        val observable: Observable<ShippingRecommendationData>
        val promoCode = voucherOrdersItemUiModel.code
        val shippingId = voucherOrdersItemUiModel.shippingId
        val spId = voucherOrdersItemUiModel.spId
        view?.setStateLoadingCourierStateAtIndex(itemPosition, true)
        if (isTradeInDropOff) {
            observable = ratesApiUseCase.execute(param)
            compositeSubscription.add(
                observable
                    .map { shippingRecommendationData: ShippingRecommendationData? ->
                        stateConverter.fillState(
                            shippingRecommendationData!!,
                            shopShipmentList,
                            spId,
                            0
                        )
                    }
                    .subscribe(
                        GetBoPromoCourierRecommendationSubscriber(
                            view!!, this, cartString, promoCode, shippingId, spId,
                            itemPosition, shippingCourierConverter, shipmentCartItemModel,
                            isTradeInDropOff, false, null
                        )
                    )
            )
        } else {
            if (ratesPromoPublisher == null) {
                ratesPromoPublisher = PublishSubject.create()
                compositeSubscription.add(
                    ratesPromoPublisher
                        ?.concatMap { shipmentGetCourierHolderData ->
                            logisticPromoDonePublisher = PublishSubject.create()
                            ratesUseCase.execute(shipmentGetCourierHolderData.ratesParam)
                                .map { shippingRecommendationData: ShippingRecommendationData? ->
                                    stateConverter.fillState(
                                        shippingRecommendationData!!,
                                        shipmentGetCourierHolderData.shopShipmentList,
                                        shipmentGetCourierHolderData.spId,
                                        0
                                    )
                                }.subscribe(
                                    GetBoPromoCourierRecommendationSubscriber(
                                        view!!,
                                        this,
                                        shipmentGetCourierHolderData.cartString,
                                        shipmentGetCourierHolderData.promoCode,
                                        shipmentGetCourierHolderData.shipperId,
                                        shipmentGetCourierHolderData.spId,
                                        shipmentGetCourierHolderData.itemPosition,
                                        shippingCourierConverter,
                                        shipmentGetCourierHolderData.shipmentCartItemModel,
                                        shipmentGetCourierHolderData.isTradeInDropOff,
                                        false,
                                        logisticPromoDonePublisher
                                    )
                                )
                            logisticPromoDonePublisher
                        }
                        ?.subscribe()
                )
            }
            ratesPromoPublisher!!.onNext(
                ShipmentGetCourierHolderData(
                    shippingId,
                    spId,
                    itemPosition,
                    shipmentCartItemModel,
                    shopShipmentList,
                    false,
                    cartString,
                    isTradeInDropOff,
                    false,
                    param,
                    promoCode
                )
            )
        }
    }*/

    private fun processBoPromoCourierRecommendationNew(
        listToProcess: List<Triple<Int, PromoCheckoutVoucherOrdersItemUiModel, ShipmentCartItemModel>>
    ) {
        viewModelScope.launch(dispatchers.immediate) {
            shipmentButtonPayment.value =
                shipmentButtonPayment.value.copy(loading = true)
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
                    recipientAddressModel,
                    if (shipmentCartItemModel.groupType == GROUP_TYPE_OWOC) shipmentCartItemModel.cartStringGroup else "",
                    if (shipmentCartItemModel.groupType == GROUP_TYPE_OWOC) {
                        getGroupProductsForRatesRequest(
                            shipmentCartItemModel
                        )
                    } else {
                        emptyList()
                    }
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
                                                    shipmentCartItemModel
                                                )
                                                clearOrderPromoCodeFromLastValidateUseRequest(
                                                    cartString,
                                                    promoCode
                                                )
                                                view?.resetCourier(shipmentCartItemModel)
                                                view?.renderCourierStateFailed(
                                                    itemPosition,
                                                    isTradeInDropOff,
                                                    true
                                                )
                                                view?.logOnErrorLoadCourier(
                                                    MessageErrorException(
                                                        shippingCourierUiModel.productData.error?.errorMessage
                                                    ),
                                                    itemPosition,
                                                    promoCode
                                                )
                                                continue@loopProcess
                                            } else {
                                                shippingCourierUiModel.isSelected = true
                                                setShippingCourierViewModelsState(
                                                    shippingDurationUiModel.shippingCourierViewModelList,
                                                    shipmentCartItemModel.orderNumber
                                                )
                                                val courierItemData = generateCourierItemData(
                                                    shippingCourierUiModel,
                                                    shippingRecommendationData,
                                                    logisticPromo
                                                )
                                                val validateUsePromoRequest =
                                                    generateValidateUsePromoRequest()
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
                                                doValidateLogisticPromoForOneOrder(
                                                    validateUsePromoRequest,
                                                    itemPosition,
                                                    cartString,
                                                    promoCode,
                                                    courierItemData
                                                )
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
                    cancelAutoApplyPromoStackLogistic(
                        itemPosition,
                        promoCode,
                        shipmentCartItemModel
                    )
                    clearOrderPromoCodeFromLastValidateUseRequest(cartString, promoCode)
                    view?.resetCourier(shipmentCartItemModel)
                    view?.renderCourierStateFailed(itemPosition, isTradeInDropOff, true)
                    view?.logOnErrorLoadCourier(t, itemPosition, promoCode)
                }
            }
            shipmentButtonPayment.value =
                shipmentButtonPayment.value.copy(loading = false)
        }
    }

    private fun generateCourierItemData(
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

    override fun getProductForRatesRequest(shipmentCartItemModel: ShipmentCartItemModel?): ArrayList<Product> {
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

    fun getGroupProductsForRatesRequest(shipmentCartItemModel: ShipmentCartItemModel?): ArrayList<GroupProduct> {
        val products = ArrayList<GroupProduct>()
        if (shipmentCartItemModel?.cartItemModels != null) {
            val cartItemByOrder = shipmentCartItemModel.cartItemModelsGroupByOrder
            for ((key, value) in cartItemByOrder) {
                var totalOrderValue = 0L
                var totalWeight = 0.0
                val items = value.map {
                    val weight = (it.quantity * it.weight) / WEIGHT_DIVIDER_TO_KG
                    val orderValue = (it.quantity * it.price).toLong()
                    totalOrderValue += orderValue
                    totalWeight += weight
                    GroupProductItem(
                        productId = it.productId,
                        orderValue = orderValue,
                        weight = weight.toString(),
                        warehouseIds = it.originWarehouseIds
                    )
                }
                products.add(
                    GroupProduct(
                        shopId = value.first().shopId.toLongOrZero(),
                        warehouseId = value.first().warehouseId.toLongOrZero(),
                        uniqueId = key,
                        totalOrderValue = totalOrderValue,
                        totalWeight = totalWeight.toString(),
                        products = items
                    )
                )
            }
        }
        return products
    }

    override fun validateClearAllBoPromo() {
        if (lastValidateUseRequest != null) {
            for (shipmentCartItemModel in shipmentCartItemModelList) {
                if (shipmentCartItemModel is ShipmentCartItemModel && shipmentCartItemModel.voucherLogisticItemUiModel != null) {
                    for (order in lastValidateUseRequest!!.orders) {
                        if (order.cartStringGroup == shipmentCartItemModel.cartStringGroup && order.codes.contains(
                                shipmentCartItemModel.voucherLogisticItemUiModel!!.code
                            )
                        ) {
                            doUnapplyBo(
                                shipmentCartItemModel.cartStringGroup,
                                shipmentCartItemModel.voucherLogisticItemUiModel!!.code
                            )
                        }
                    }
                }
            }
        }
    }

    override fun cancelUpsell(
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

    override fun clearAllBoOnTemporaryUpsell() {
        if (shipmentNewUpsellModel.isShow && shipmentNewUpsellModel.isSelected) {
            val clearOrders = ArrayList<ClearPromoOrder>()
            var hasBo = false
            for (shipmentCartItemModel in shipmentCartItemModelList) {
                if (shipmentCartItemModel is ShipmentCartItemModel && shipmentCartItemModel.voucherLogisticItemUiModel != null && shipmentCartItemModel.voucherLogisticItemUiModel!!.code.isNotEmpty()) {
                    val boCodes = ArrayList<String>()
                    boCodes.add(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)
                    clearOrders.add(
                        ClearPromoOrder(
                            shipmentCartItemModel.cartStringGroup,
                            shipmentCartItemModel.shipmentCartData.boMetadata.boType,
                            boCodes,
                            shipmentCartItemModel.shopId,
                            shipmentCartItemModel.isProductIsPreorder,
                            shipmentCartItemModel.cartItemModels[0].preOrderDurationDay.toString(),
                            shipmentCartItemModel.fulfillmentId
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
    }

    fun resetPromoCheckoutData() {
        lastApplyData.value = LastApplyUiModel()
    }

    fun updatePromoCheckoutData(promoUiModel: PromoUiModel) {
        lastApplyData.value = LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(
            promoUiModel
        )
    }

    override fun validatePrescriptionOnBackPressed(): Boolean {
        if (uploadPrescriptionUiModel.showImageUpload && view != null) {
            if (uploadPrescriptionUiModel.uploadedImageCount > 0 || uploadPrescriptionUiModel.hasInvalidPrescription) {
                view?.showPrescriptionReminderDialog(uploadPrescriptionUiModel)
                return false
            }
        }
        return true
    }

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
                        if (product.addOnProduct.status == 1) {
                            val dynamicDataParam = DynamicDataParam()
                            dynamicDataParam.level = PRODUCT_LEVEL
                            dynamicDataParam.parentUniqueId = groupShop.cartString
                            dynamicDataParam.uniqueId = product.cartId.toString()
                            dynamicDataParam.attribute = ATTRIBUTE_ADDON_DETAILS
                            dynamicDataParam.addOn =
                                getAddOnFromSAF(product.addOnProduct, isOneClickShipment)
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

    override fun updateDynamicData(
        dynamicDataPassingParamRequest: DynamicDataPassingParamRequest,
        isFireAndForget: Boolean
    ) {
        updateDynamicDataPassingUseCase.setParams(dynamicDataPassingParamRequest, isFireAndForget)
        updateDynamicDataPassingUseCase.execute(
            { (dynamicData): UpdateDynamicDataPassingUiModel ->
                this.dynamicData = dynamicData
                if (view != null && !isFireAndForget) {
                    view?.doCheckout()
                }
                Unit
            }
        ) { throwable: Throwable ->
            Timber.d(throwable)
            if (view != null) {
                view?.setHasRunningApiCall(false)
                view?.hideLoading()
                var errorMessage = throwable.message
                if (throwable !is CartResponseErrorException && throwable !is AkamaiErrorException) {
                    errorMessage = getErrorMessage(
                        view?.activityContext,
                        throwable
                    )
                }
                view?.showToastError(errorMessage)
            }
            Unit
        }
    }

    override fun setDynamicDataParam(dynamicDataPassingParam: DynamicDataPassingParamRequest) {
        this.dynamicDataParam = dynamicDataPassingParam
    }

    override fun getDynamicDataParam(): DynamicDataPassingParamRequest {
        return this.dynamicDataParam
    }

    override fun validateDynamicData() {
        updateDynamicData(getDynamicDataParam(), false)
    }

    override fun isUsingDynamicDataPassing(): Boolean {
        return isUsingDdp
    }

    private fun getScheduleDeliveryMapData(cartString: String): ShipmentScheduleDeliveryMapData? {
        return scheduleDeliveryMapData[cartString]
    }

    override fun setScheduleDeliveryMapData(
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
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.etaText!!
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
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.etaText!!
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

    private fun setValidateUseBoCodeInOneOrderOwoc(validateUsePromoRequest: ValidateUsePromoRequest) {
        validateUsePromoRequest.orders.filter { it.cartStringGroup != it.uniqueId }
            .groupBy { it.cartStringGroup }.filterValues { it.size > 1 }.values
            .forEach { ordersItems ->
                val boCode = ordersItems.first().boCode
                if (boCode.isNotEmpty()) {
                    ordersItems.forEachIndexed { index, ordersItem ->
                        if (index > 0 && ordersItem.codes.contains(boCode)) {
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

    companion object {
        private const val LAST_THREE_DIGIT_MODULUS: Long = 1000

        private const val statusOK = "OK"

        private const val statusCode200 = "200"

        private const val WEIGHT_DIVIDER_TO_KG = 1000
    }
}
