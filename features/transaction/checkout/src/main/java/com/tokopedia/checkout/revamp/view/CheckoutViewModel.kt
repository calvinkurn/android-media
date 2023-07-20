package com.tokopedia.checkout.revamp.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.analytics.performance.util.EmbraceKey
import com.tokopedia.analytics.performance.util.EmbraceMonitoring
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.mapper.ShipmentAddOnProductServiceMapper
import com.tokopedia.checkout.domain.model.cartshipmentform.ShipmentPlatformFeeData
import com.tokopedia.checkout.domain.model.platformfee.PaymentFeeCheckoutRequest
import com.tokopedia.checkout.revamp.view.converter.CheckoutDataConverter
import com.tokopedia.checkout.revamp.view.processor.CheckoutAddOnProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutCalculator
import com.tokopedia.checkout.revamp.view.processor.CheckoutCartProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutLogisticProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutPaymentProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutPromoProcessor
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
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageState
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageToaster
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPromoModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutUpsellModel
import com.tokopedia.checkout.view.CheckoutMutableLiveData
import com.tokopedia.checkout.view.converter.ShipmentDataRequestConverter
import com.tokopedia.checkout.view.uimodel.ShipmentAddOnSummaryModel
import com.tokopedia.checkout.view.uimodel.ShipmentPaymentFeeModel
import com.tokopedia.common_epharmacy.network.response.EPharmacyMiniConsultationResult
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticcart.shipping.model.CodModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.data.request.DynamicDataPassingParamRequest
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheckoutViewModel @Inject constructor(
    private val cartProcessor: CheckoutCartProcessor,
    private val logisticProcessor: CheckoutLogisticProcessor,
    private val promoProcessor: CheckoutPromoProcessor,
    private val addOnProcessor: CheckoutAddOnProcessor,
    private val paymentProcessor: CheckoutPaymentProcessor,
    private val checkoutProcessor: CheckoutProcessor,
    private val calculator: CheckoutCalculator,
    private val dataConverter: CheckoutDataConverter,
    private val shipmentDataRequestConverter: ShipmentDataRequestConverter,
    private val mTrackerShipment: CheckoutAnalyticsCourierSelection,
    private val mTrackerPurchaseProtection: CheckoutAnalyticsPurchaseProtection,
    private val userSessionInterface: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    var currentJob: Job? = null
    var counter: Int = 0

    var ms = MutableStateFlow(true)

    var sm = flow<Boolean> { }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), true)

    val listData: CheckoutMutableLiveData<List<CheckoutItem>> = CheckoutMutableLiveData(emptyList())

    val pageState: CheckoutMutableLiveData<CheckoutPageState> =
        CheckoutMutableLiveData(CheckoutPageState.Loading)

    val commonToaster: MutableSharedFlow<CheckoutPageToaster> = MutableSharedFlow()

    var isOneClickShipment: Boolean = false

    var checkoutLeasingId: String = "0"

    var isTradeIn: Boolean = false

    var deviceId: String = ""

    var isPlusSelected: Boolean = false

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

    private var dynamicDataParam: DynamicDataPassingParamRequest = DynamicDataPassingParamRequest()

    private var shipmentPlatformFeeData: ShipmentPlatformFeeData = ShipmentPlatformFeeData()

    var dynamicData = ""

    var cartDataForRates = ""
        private set

    var codData: CodModel? = null
        private set

    // add ons product
    // list summary add on - ready to render
    private var listSummaryAddOnModel: List<ShipmentAddOnSummaryModel> = emptyList()

    // list summary default
    private var summariesAddOnUiModel: HashMap<Int, String> = hashMapOf()

    fun test() {
        currentJob = viewModelScope.launch {
            Log.i("qwertyuiop", "start launch")
            doSomething(++counter)
            Log.i("qwertyuiop", "done launch")
        }
    }

    private suspend fun doSomething(count: Int) {
        currentJob?.join()
        withContext(dispatchers.io) {
            Log.i("qwertyuiop", "start delay $count")
            delay(5_000)
            Log.i("qwertyuiop", "done delay $count")
        }
    }

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
            val saf = cartProcessor.hitSAF(
                isOneClickShipment,
                isTradeIn,
                skipUpdateOnboardingState,
                cornerId,
                deviceId,
                checkoutLeasingId,
                isPlusSelected,
                isReloadData,
                isReloadAfterPriceChangeHigher
            )
            stopEmbraceTrace()
            if (saf is CheckoutPageState.Success) {
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
//                    analyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
//                        tickerData.id
//                    )
                }
                val address = CheckoutAddressModel(
                    recipientAddressModel = dataConverter.getRecipientAddressModel(saf.cartShipmentAddressFormData)
                )

                val upsell = dataConverter.getUpsellModel(saf.cartShipmentAddressFormData.newUpsell)

                isUsingDdp = saf.cartShipmentAddressFormData.isUsingDdp
                dynamicData = saf.cartShipmentAddressFormData.dynamicData
                shipmentPlatformFeeData = saf.cartShipmentAddressFormData.shipmentPlatformFee
                listSummaryAddOnModel =
                    ShipmentAddOnProductServiceMapper.mapSummaryAddOns(saf.cartShipmentAddressFormData)
                cartDataForRates = saf.cartShipmentAddressFormData.cartData
                codData = saf.cartShipmentAddressFormData.cod

                val items = dataConverter.getCheckoutItems(
                    saf.cartShipmentAddressFormData,
                    address.recipientAddressModel.locationDataModel != null,
                    userSessionInterface.name
                )

                val epharmacy = CheckoutEpharmacyModel(
                    epharmacy = UploadPrescriptionUiModel(
                        showImageUpload = saf.cartShipmentAddressFormData.epharmacyData.showImageUpload,
                        uploadImageText = saf.cartShipmentAddressFormData.epharmacyData.uploadText,
                        leftIconUrl = saf.cartShipmentAddressFormData.epharmacyData.leftIconUrl,
                        checkoutId = saf.cartShipmentAddressFormData.epharmacyData.checkoutId,
                        frontEndValidation = saf.cartShipmentAddressFormData.epharmacyData.frontEndValidation,
                        consultationFlow = saf.cartShipmentAddressFormData.epharmacyData.consultationFlow,
                        rejectedWording = saf.cartShipmentAddressFormData.epharmacyData.rejectedWording
                    )
                )

                val promo = CheckoutPromoModel(
                    promo = saf.cartShipmentAddressFormData.lastApplyData
                )

                val cost = CheckoutCostModel()

                val crossSellGroup = CheckoutCrossSellGroupModel()
                val crossSellList = arrayListOf<CheckoutCrossSellItem>()
                crossSellList.addAll(
                    saf.cartShipmentAddressFormData.crossSell.mapIndexed { index, crossSellModel ->
                        CheckoutCrossSellModel(
                            crossSellModel,
                            crossSellModel.isChecked,
                            crossSellModel.checkboxDisabled,
                            index
                        )
                    }
                )
                if (saf.cartShipmentAddressFormData.egoldAttributes != null) {
                    crossSellList.add(CheckoutEgoldModel(saf.cartShipmentAddressFormData.egoldAttributes!!))
                }
                if (saf.cartShipmentAddressFormData.donation != null) {
                    crossSellList.add(CheckoutDonationModel(saf.cartShipmentAddressFormData.donation!!))
                }

                val buttonPayment = CheckoutButtonPaymentModel()

                withContext(dispatchers.main) {
                    listData.value = listOf(
                        ticker,
                        address,
                        upsell
                    ) + items + listOf(epharmacy, promo, cost, crossSellGroup, buttonPayment)
                    pageState.value = saf
                    calculateTotal()
                }
            } else if (saf is CheckoutPageState.CheckNoAddress) {
                logisticProcessor.checkIsUserEligibleForRevampAna(saf.cartShipmentAddressFormData) { checkoutPageState: CheckoutPageState ->
                    pageState.value = checkoutPageState
                }
            } else {
                withContext(dispatchers.main) {
                    pageState.value = saf
                }
            }
            Log.i("qwertyuiop", "saf $saf")
        }
    }

    fun changeAddress(
        newRecipientAddressModel: RecipientAddressModel?,
        chosenAddressModel: ChosenAddressModel?,
        isHandleFallback: Boolean
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
                loadSAF(true, true, false)
            } else {
                commonToaster.emit(
                    CheckoutPageToaster(
                        Toaster.TYPE_ERROR,
                        changeAddressResult.toasterMessage,
                        changeAddressResult.throwable
                    )
                )
                pageState.value = CheckoutPageState.Normal
                if (isHandleFallback) {
                    val address = listData.value.address()?.recipientAddressModel
                    if (address != null) {
                        if (address.selectedTabIndex == RecipientAddressModel.TAB_ACTIVE_ADDRESS_DEFAULT) {
                            address.selectedTabIndex =
                                RecipientAddressModel.TAB_ACTIVE_ADDRESS_TRADE_IN
                            address.isIgnoreSelectionAction = true
                        } else if (address.selectedTabIndex == RecipientAddressModel.TAB_ACTIVE_ADDRESS_TRADE_IN) {
                            address.locationDataModel = null
                            address.dropOffAddressDetail = ""
                            address.dropOffAddressName = ""
                        }
                        listData.value = listData.value
                    }
                }
            }
        }
    }

    fun editAddressPinpoint(
        latitude: String,
        longitude: String,
        locationPass: LocationPass?
    ) {
        viewModelScope.launch(dispatchers.immediate) {
            pageState.value = CheckoutPageState.Loading
            val editAddressResult =
                logisticProcessor.editAddressPinpoint(latitude, longitude, recipientAddressModel)
            pageState.value = CheckoutPageState.Normal
            if (editAddressResult.isSuccess) {
            }
        }
    }

    fun fetchEpharmacyData() {
        viewModelScope.launch(dispatchers.immediate) {
            addOnProcessor.fetchEpharmacyData(listData.value)
        }
    }

    fun setPrescriptionIds(prescriptionIds: ArrayList<String>) {
        addOnProcessor.setPrescriptionIds(prescriptionIds, listData.value)
    }

    fun setMiniConsultationResult(results: ArrayList<EPharmacyMiniConsultationResult>) {
        addOnProcessor.setMiniConsultationResult(results, listData.value)
    }

    fun calculateTotal() {
        listData.value = calculator.updateShipmentCostModel(listData.value)
        viewModelScope.launch(dispatchers.main) {
            val paymentFeeCheckoutRequest = PaymentFeeCheckoutRequest(
                gatewayCode = "",
                profileCode = shipmentPlatformFeeData.profileCode,
                paymentAmount = listData.value.cost()!!.totalPrice,
                additionalData = shipmentPlatformFeeData.additionalData
            )
            val paymentFee = paymentProcessor.getDynamicPaymentFee(paymentFeeCheckoutRequest)
            if (paymentFee != null) {
                val checkoutItems = listData.value
                val platformFeeModel = ShipmentPaymentFeeModel()
                for (fee in paymentFee.data) {
                    if (fee.code.equals(PLATFORM_FEE_CODE, ignoreCase = true)) {
                        platformFeeModel.title = fee.title
                        platformFeeModel.fee = fee.fee
                        platformFeeModel.minRange = fee.minRange
                        platformFeeModel.maxRange = fee.maxRange
                        platformFeeModel.isShowTooltip = fee.showTooltip
                        platformFeeModel.tooltip = fee.tooltipInfo
                        platformFeeModel.isShowSlashed = fee.showSlashed
                        platformFeeModel.slashedFee = fee.slashedFee.toDouble()
                    }
                }
                checkoutItems.toMutableList()[checkoutItems.size - 3] =
                    checkoutItems.cost()!!.copy(dynamicPlatformFee = platformFeeModel)
            }
            listData.value = calculator.updateShipmentCostModel(listData.value)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("qwertyuiop", "done clear")
        Log.i("qwertyuiop", "job: ${currentJob?.isCompleted}")
        Log.i(
            "qwertyuiop",
            "parent job: ${viewModelScope.coroutineContext.job?.children?.find { !it.isCompleted }}"
        )
    }

    fun getProductForRatesRequest(order: CheckoutOrderModel): ArrayList<Product> {
        val products = arrayListOf<Product>()
        for (cartItemModel in order.products) {
            if (!cartItemModel.isError) {
                val product = Product()
                product.productId = cartItemModel.productId
                product.isFreeShipping = cartItemModel.isFreeShipping
                product.isFreeShippingTc = cartItemModel.isFreeShippingExtra
                products.add(product)
            }
        }
        return products
    }

    fun generateRatesMvcParam(cartStringGroup: String): String {
        return ""
    }

    fun setSelectedCourier(
        cartPosition: Int,
        courierItemData: CourierItemData,
        shippingCourierUiModels: List<ShippingCourierUiModel>
    ) {
        val checkoutItems = listData.value.toMutableList()
        val checkoutOrderModel = checkoutItems[cartPosition] as CheckoutOrderModel
        val shipment = checkoutOrderModel.shipment
        val newShipment = shipment.copy(
            isLoading = false,
            courierItemData = courierItemData,
            shippingCourierUiModels = shippingCourierUiModels
        )
        checkoutItems[cartPosition] = checkoutOrderModel.copy(shipment = newShipment)
        listData.value = checkoutItems
        viewModelScope.launch {
            cartProcessor.processSaveShipmentState(checkoutOrderModel, listData.value.address()!!.recipientAddressModel)
        }
//        listData.value = listData.value
    }

    companion object {
        private const val PLATFORM_FEE_CODE = "platform_fee"
    }
}

internal fun <R> List<CheckoutItem>.firstOrNullInstanceOf(kClass: Class<R>): R? {
    val item = firstOrNull { kClass.isInstance(it) }
    @Suppress("UNCHECKED_CAST")
    return item as? R
}

internal fun List<CheckoutItem>.address(): CheckoutAddressModel? {
    val item = getOrNull(1)
    @Suppress("UNCHECKED_CAST")
    return item as? CheckoutAddressModel
}

internal fun List<CheckoutItem>.upsell(): CheckoutUpsellModel? {
    val item = getOrNull(2)
    @Suppress("UNCHECKED_CAST")
    return item as? CheckoutUpsellModel
}

internal fun List<CheckoutItem>.cost(): CheckoutCostModel? {
    val item = getOrNull(size - 3)
    @Suppress("UNCHECKED_CAST")
    return item as? CheckoutCostModel
}

internal fun List<CheckoutItem>.crossSellGroup(): CheckoutCrossSellGroupModel? {
    val item = getOrNull(size - 2)
    @Suppress("UNCHECKED_CAST")
    return item as? CheckoutCrossSellGroupModel
}

internal fun List<CheckoutItem>.buttonPayment(): CheckoutButtonPaymentModel? {
    val item = getOrNull(size - 1)
    @Suppress("UNCHECKED_CAST")
    return item as? CheckoutButtonPaymentModel
}
