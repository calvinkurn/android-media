package com.tokopedia.checkout.revamp.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.analytics.performance.util.EmbraceKey
import com.tokopedia.analytics.performance.util.EmbraceMonitoring
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
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
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutUpsellModel
import com.tokopedia.checkout.view.CheckoutMutableLiveData
import com.tokopedia.checkout.view.uimodel.ShipmentAddOnSummaryModel
import com.tokopedia.common_epharmacy.network.response.EPharmacyMiniConsultationResult
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.logisticCommon.data.constant.InsuranceConstant
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.logisticcart.shipping.model.CodModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import com.tokopedia.purchase_platform.common.feature.addons.data.model.AddOnProductDataItemModel
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.data.request.DynamicDataPassingParamRequest
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
    private val mTrackerShipment: CheckoutAnalyticsCourierSelection,
    private val mTrackerPurchaseProtection: CheckoutAnalyticsPurchaseProtection,
    private val helper: CheckoutDataHelper,
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

    private var campaignTimer: CampaignTimerUi? = null

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
                    mTrackerShipment.eventViewInformationAndWarningTickerInCheckout(tickerData.id)
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
                campaignTimer = saf.cartShipmentAddressFormData.campaignTimerUi
                logisticProcessor.isBoUnstackEnabled =
                    saf.cartShipmentAddressFormData.lastApplyData.additionalInfo.bebasOngkirInfo.isBoUnstackEnabled
                summariesAddOnUiModel = ShipmentAddOnProductServiceMapper.getShoppingSummaryAddOns(saf.cartShipmentAddressFormData.listSummaryAddons)

                val items = dataConverter.getCheckoutItems(
                    saf.cartShipmentAddressFormData,
                    address.recipientAddressModel.locationDataModel != null,
                    userSessionInterface.name
                )

                val uploadPrescriptionUiModel = UploadPrescriptionUiModel(
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
                val epharmacy = CheckoutEpharmacyModel(
                    epharmacy = uploadPrescriptionUiModel
                )

                val promo = CheckoutPromoModel(
                    promo = saf.cartShipmentAddressFormData.lastApplyData
                )

                val cost = CheckoutCostModel()

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
                    crossSellList.add(CheckoutDonationModel(saf.cartShipmentAddressFormData.donation!!))
                }
                val crossSellGroup = CheckoutCrossSellGroupModel(crossSellList = crossSellList)

                val buttonPayment = CheckoutButtonPaymentModel("")

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
        }
    }

    fun isLoading(): Boolean {
        return listData.value.indexOfFirst { it is CheckoutOrderModel && it.shipment.isLoading } != -1
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
                if (isHandleFallback) {
                    // todo test this in trade in
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
//                if (listData.value.indexOfFirst { it is CheckoutOrderModel && it.isDisableChangeCourier } != -1) {
                loadSAF(
                    isReloadData = true,
                    skipUpdateOnboardingState = true,
                    isReloadAfterPriceChangeHigher = false
                )
//                }
            } else {
                commonToaster.emit(CheckoutPageToaster(Toaster.TYPE_ERROR, editAddressResult.errorMessage, editAddressResult.throwable))
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

    internal fun calculateTotal() {
        viewModelScope.launch(dispatchers.immediate) {
            listData.value = calculator.calculateWithoutPayment(listData.value, isTradeInByDropOff, summariesAddOnUiModel)
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
                calculator.updateShipmentCostModel(listData.value, cost, isTradeInByDropOff, summariesAddOnUiModel)
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

    fun updateAddOnGiftingProductLevelDataBottomSheet(saveAddOnStateResult: SaveAddOnStateResult) {
        val checkoutItems = listData.value.toMutableList()
        for (addOnResult in saveAddOnStateResult.addOns) {
            for (item in checkoutItems.withIndex()) {
                val product = item.value
                if (product is CheckoutProductModel) {
//                    val cartItemModelList = shipmentCartItemModel.products
//                    for (i in cartItemModelList.indices) {
//                        val cartItemModel = cartItemModelList[i]
                    val keyProductLevel =
                        "${product.cartStringGroup}-${product.cartId}"
                    if (keyProductLevel.equals(addOnResult.addOnKey, ignoreCase = true)) {
                        checkoutItems[item.index] = product.copy(
                            addOnGiftingProductLevelModel = setAddOnsGiftingData(
                                product.addOnGiftingProductLevelModel.copy(),
                                addOnResult,
                                0,
                                product.cartStringGroup,
                                product.cartId
                            )
                        )
//                        }
                    }
                }
            }
        }
        listData.value = checkoutItems
    }

    private fun setAddOnsGiftingData(
        addOnsDataModel: AddOnGiftingDataModel,
        addOnResult: AddOnResult,
        identifier: Int,
        cartString: String,
        cartId: Long
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
        val addOnBottomSheetModel = AddOnGiftingBottomSheetModel()
        addOnBottomSheetModel.headerTitle = addOnBottomSheet.headerTitle
        addOnBottomSheetModel.description = addOnBottomSheet.description
        val addOnTickerModel = AddOnGiftingTickerModel()
        addOnTickerModel.text = addOnBottomSheet.ticker.text
        addOnBottomSheetModel.ticker = addOnTickerModel
        val listProductAddOn = java.util.ArrayList<AddOnGiftingProductItemModel>()
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
//        view?.updateAddOnsData(identifier, cartString, cartId)
//        if (isUsingDynamicDataPassing()) {
//            view?.updateAddOnsDynamicDataPassing(
//                addOnResult,
//                identifier,
//                cartString,
//                cartId
//            )
//        }
        return addOnsDataModel
    }

    fun getProductForRatesRequest(order: CheckoutOrderModel): ArrayList<Product> {
        return logisticProcessor.getProductForRatesRequest(helper.getOrderProducts(listData.value, order.cartStringGroup))
    }

    fun generateRatesMvcParam(cartStringGroup: String): String {
        return logisticProcessor.generateRatesMvcParam(cartStringGroup)
    }

    fun generateRatesParam(order: CheckoutOrderModel): RatesParam {
        return logisticProcessor.getRatesParam(
            order,
            helper.getOrderProducts(listData.value, order.cartStringGroup),
            listData.value.address()!!.recipientAddressModel,
            isTradeIn,
            isTradeInByDropOff,
            codData,
            cartDataForRates
        )
    }

    fun loadShipping(order: CheckoutOrderModel, cartPosition: Int) {
        if (order.ratesValidationFlow) {
            viewModelScope.launch(dispatchers.immediate) {
                loadShippingWithSelly(cartPosition, order)
            }
        } else {
            viewModelScope.launch(dispatchers.immediate) {
                loadShippingNormal(cartPosition, order)
            }
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
                cartDataForRates
            ),
            order.shopShipmentList,
            order.shippingId,
            order.spId,
            order.fulfillmentId.toString(),
            order
        )
        val list = listData.value.toMutableList()
        val orderModel = list[cartPosition] as? CheckoutOrderModel
        if (orderModel != null) {
            if (result?.first != null) {
                val courierItemData = result.first
                val shouldValidatePromo =
                    result.first.selectedShipper.logPromoCode != null && result.first.selectedShipper.logPromoCode!!.isNotEmpty()
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
                    //                        removeInvalidBoCodeFromPromoRequest(
                    //                            shipmentGetCourierHolderData,
                    //                            validateUsePromoRequest
                    //                        )
                    doValidateUseLogisticPromoNew(
                        cartPosition,
                        orderModel.cartStringGroup,
                        validateUsePromoRequest,
                        result.first.logPromoCode!!,
                        true,
                        result.first,
                        result.second
                    )
                    return
                }
            }
            val newOrderModel = orderModel.copy(
                shipment = orderModel.shipment.copy(
                    isLoading = false,
                    courierItemData = result?.first,
                    shippingCourierUiModels = result?.third ?: emptyList(),
                    insurance = result?.second?.let {
                        CheckoutOrderInsurance(
                            when (result.second.insuranceType) {
                                InsuranceConstant.INSURANCE_TYPE_MUST -> {
                                    true
                                }

                                InsuranceConstant.INSURANCE_TYPE_NO -> {
                                    false
                                }

                                InsuranceConstant.INSURANCE_TYPE_OPTIONAL -> {
                                    result.second.insuranceUsedDefault == InsuranceConstant.INSURANCE_USED_DEFAULT_YES
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
                cartDataForRates
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
            if (result?.first != null) {
                val courierItemData = result.first
                val shouldValidatePromo =
                    result.first.selectedShipper.logPromoCode != null && result.first.selectedShipper.logPromoCode!!.isNotEmpty()
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
                    //                        removeInvalidBoCodeFromPromoRequest(
                    //                            shipmentGetCourierHolderData,
                    //                            validateUsePromoRequest
                    //                        )
                    doValidateUseLogisticPromoNew(
                        cartPosition,
                        orderModel.cartStringGroup,
                        validateUsePromoRequest,
                        result.first.logPromoCode!!,
                        false,
                        result.first,
                        result.second
                    )
                    return
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
                    )
                )
                commonToaster.emit(CheckoutPageToaster(Toaster.TYPE_NORMAL, "Bebas ongkir gagal diaplikasikan, silahkan coba lagi"))
            }
            val newOrderModel = orderModel.copy(
                shipment = orderModel.shipment.copy(
                    isLoading = false,
                    courierItemData = result?.first,
                    shippingCourierUiModels = result?.third ?: emptyList(),
                    insurance = result?.second?.let {
                        CheckoutOrderInsurance(
                            when (result.second.insuranceType) {
                                InsuranceConstant.INSURANCE_TYPE_MUST -> {
                                    true
                                }

                                InsuranceConstant.INSURANCE_TYPE_NO -> {
                                    false
                                }

                                InsuranceConstant.INSURANCE_TYPE_OPTIONAL -> {
                                    result.second.insuranceUsedDefault == InsuranceConstant.INSURANCE_USED_DEFAULT_YES
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
            if (shipment.courierItemData?.logPromoCode?.isNotEmpty() == true) {
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
                        arrayListOf(shipment.courierItemData.logPromoCode!!),
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
//            val newOrder = checkoutOrderModel.copy(shipment = newShipment)
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

    fun generateValidateUsePromoRequest(): ValidateUsePromoRequest {
        return promoProcessor.generateValidateUsePromoRequest(
            listData.value,
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

    private suspend fun validatePromo() {
        val checkoutItems = listData.value
        val newItems = promoProcessor.validateUse(
            promoProcessor.generateValidateUsePromoRequest(
                checkoutItems,
                isTradeIn,
                isTradeInByDropOff,
                isOneClickShipment
            ),
            checkoutItems
        )
        listData.value = newItems
        calculateTotal()
    }

    fun doValidateUseLogisticPromoNew(
        cartPosition: Int,
        cartString: String,
        validateUsePromoRequest: ValidateUsePromoRequest,
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
            val newItems = promoProcessor.validateUseLogisticPromo(
                validateUsePromoRequest,
                cartString,
                promoCode,
                listData.value,
                courierItemData,
                isOneClickShipment,
                isTradeIn,
                isTradeInByDropOff
            )
            listData.value = newItems
            cartProcessor.processSaveShipmentState(listData.value, listData.value.address()!!.recipientAddressModel)
            pageState.value = CheckoutPageState.Normal
            calculateTotal()
        }
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
            if (scheduleDeliveryUiModel.isSelected) {
                order.scheduleDate = newCourierItemData.selectedShipper.scheduleDate
                order.timeslotId = newCourierItemData.selectedShipper.timeslotId
                order.validationMetadata =
                    scheduleDeliveryUiModel.deliveryProduct.validationMetadata
            } else {
                order.scheduleDate = ""
                order.timeslotId = 0
                order.validationMetadata = ""
            }
            if (courierItemData.selectedShipper.logPromoCode.isNullOrEmpty() && newCourierItemData.selectedShipper.logPromoCode.isNullOrEmpty()) {
                // no promo
                val checkoutItems = listData.value.toMutableList()
                val checkoutOrderModel = checkoutItems[cartPosition] as CheckoutOrderModel
                val shipment = checkoutOrderModel.shipment
                val newShipment = shipment.copy(
                    isLoading = false,
                    courierItemData = newCourierItemData
//                    shippingCourierUiModels = shippingCourierUiModels
                )
                val newOrder = checkoutOrderModel.copy(shipment = newShipment)
                checkoutItems[cartPosition] = newOrder
                listData.value = checkoutItems
                cartProcessor.processSaveShipmentState(
                    newOrder,
                    listData.value.address()!!.recipientAddressModel
                )
                validatePromo()
                pageState.value = CheckoutPageState.Normal
            } else if (!courierItemData.selectedShipper.logPromoCode.isNullOrEmpty() && newCourierItemData.selectedShipper.logPromoCode.isNullOrEmpty()) {
                // need to clear old promo code
                val checkoutItems = listData.value.toMutableList()
                val checkoutOrderModel = checkoutItems[cartPosition] as CheckoutOrderModel
                val shipment = checkoutOrderModel.shipment
                val newShipment = shipment.copy(
                    isLoading = true,
                    courierItemData = newCourierItemData
                )
                val newOrder = checkoutOrderModel.copy(shipment = newShipment)
                checkoutItems[cartPosition] = newOrder
                listData.value = checkoutItems
                val shouldClearPromoBenefit = promoProcessor.clearPromo(
                    ClearPromoOrder(
                        checkoutOrderModel.boUniqueId,
                        checkoutOrderModel.boMetadata.boType,
                        arrayListOf(courierItemData.logPromoCode!!),
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
            } else {
                // need to apply promo
                if (courierItemData.selectedShipper.logPromoCode != newCourierItemData.selectedShipper.logPromoCode) {
                    // clear old promo before applying new one
                    promoProcessor.clearPromo(
                        ClearPromoOrder(
                            order.boUniqueId,
                            order.boMetadata.boType,
                            arrayListOf(courierItemData.logPromoCode!!),
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
                            if (order.voucherLogisticItemUiModel != null) {
                                // remove previous logistic promo code
                                orderPromo.codes.remove(order.voucherLogisticItemUiModel!!.code)
                            }
                            orderPromo.codes.add(selectedShipper.logPromoCode!!)
                            orderPromo.boCode = selectedShipper.logPromoCode!!
                        }
                    }
                }
                val shipmentCartItemModelLists =
                    listData.value.filterIsInstance(CheckoutOrderModel::class.java)
                if (shipmentCartItemModelLists.isNotEmpty()) {
                    for (tmpShipmentCartItemModel in shipmentCartItemModelLists) {
                        for (orderPromo in validateUsePromoRequest.orders) {
                            if (order.cartStringGroup != tmpShipmentCartItemModel.cartStringGroup && tmpShipmentCartItemModel.cartStringGroup == orderPromo.cartStringGroup && tmpShipmentCartItemModel.voucherLogisticItemUiModel != null &&
                                !tmpShipmentCartItemModel.isFreeShippingPlus
                            ) {
                                orderPromo.codes.remove(tmpShipmentCartItemModel.voucherLogisticItemUiModel!!.code)
                                orderPromo.boCode = ""
                            }
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
                        ordersItem.validationMetadata = order.validationMetadata
                    }
                }
                val newItems = promoProcessor.validateUseLogisticPromo(
                    validateUsePromoRequest,
                    order.cartStringGroup,
                    newCourierItemData.selectedShipper.logPromoCode!!,
                    listData.value,
                    newCourierItemData,
                    isOneClickShipment,
                    isTradeIn,
                    isTradeInByDropOff
                )
                listData.value = newItems
                cartProcessor.processSaveShipmentState(listData.value, listData.value.address()!!.recipientAddressModel)
                validatePromo()
                pageState.value = CheckoutPageState.Normal
            }
        }
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

    fun checkout(fingerprintPublicKey: String?, onSuccessCheckout: (CheckoutResult) -> Unit) {
        viewModelScope.launch(dispatchers.immediate) {
            pageState.value = CheckoutPageState.Loading
            val items = listData.value.toMutableList()
            var firstErrorIndex = -1
            var continueCheckout = true
            var hasValidOrder = false
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
                    } else if (!checkoutItem.isError) {
                        hasValidOrder = true
                    }
                }
            }
            if (firstErrorIndex > -1) {
                commonToaster.emit(
                    CheckoutPageToaster(
                        Toaster.TYPE_NORMAL,
                        "Pilih pengiriman dulu yuk sebelum lanjut bayar."
                    )
                )
                pageState.value = CheckoutPageState.Normal
                listData.value = items
                pageState.value = CheckoutPageState.ScrollTo(firstErrorIndex)
                return@launch
            }
            val errorToaster = addOnProcessor.saveAddOnsProductBeforeCheckout(listData.value, isOneClickShipment)
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
//                sendAnalyticsEpharmacyClickPembayaran()
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
                        if (deletedVoucherOrder.size > 0) {
                            for (voucherOrdersItemUiModel in deletedVoucherOrder) {
                                voucherOrderUiModels.remove(
                                    voucherOrdersItemUiModel
                                )
                            }
                            validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels =
                                voucherOrderUiModels
                        }
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
                                "Gagal clear, coba lagi"
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
                        "Gagal validate use, coba lagi"
                    )
                )
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
            onSuccessCheckout(checkoutResult)
            pageState.value = CheckoutPageState.Normal
        } else if (checkoutResult.throwable != null) {
            commonToaster.emit(
                CheckoutPageToaster(
                    Toaster.TYPE_ERROR,
                    throwable = checkoutResult.throwable
                )
            )
            loadSAF(true, true, false)
        } else if (checkoutResult.checkoutData == null) {
            commonToaster.emit(
                CheckoutPageToaster(
                    Toaster.TYPE_ERROR,
                    "Barangmu lagi nggak bisa dibeli. Silakan balik ke keranjang untuk cek belanjaanmu."
                )
            )
            pageState.value = CheckoutPageState.Normal
        } else if (checkoutResult.checkoutData.priceValidationData.isUpdated) {
            pageState.value = CheckoutPageState.PriceValidation(checkoutResult.checkoutData.priceValidationData)
        } else if (checkoutResult.checkoutData.prompt.eligible) {
            pageState.value = CheckoutPageState.Prompt(checkoutResult.checkoutData.prompt)
        } else {
            commonToaster.emit(
                CheckoutPageToaster(
                    Toaster.TYPE_ERROR,
                    checkoutResult.checkoutData.errorMessage.ifEmpty { "Terjadi kesalahan. Ulangi beberapa saat lagi" }
                )
            )
            loadSAF(true, true, false)
        }
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

    fun validatePrescriptionOnBackPressed(): CheckoutEpharmacyModel? {
        val epharmacy = listData.value.epharmacy()
        if (epharmacy != null && epharmacy.epharmacy.showImageUpload) {
            if (epharmacy.epharmacy.uploadedImageCount > 0 || epharmacy.epharmacy.hasInvalidPrescription) {
                return epharmacy
            }
        }
        return null
    }

    fun validateClearAllBoPromo(lastValidateUsePromoRequest: ValidateUsePromoRequest, promoUiModel: PromoUiModel) {
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
                    checkoutItems[index] = checkoutItem.copy(promo = LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(promoUiModel))
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
            val unappliedBoPromoUniqueIds = java.util.ArrayList<String>()
            val reloadedUniqueIds = java.util.ArrayList<String>()
            val unprocessedUniqueIds = java.util.ArrayList<String>()
            var checkoutItems = listData.value.toMutableList()
            for (shipmentCartItemModel in checkoutItems) {
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
            var firstScrollIndex = -1
            for ((index, shipmentCartItemModel) in checkoutItems.withIndex()) {
                if (shipmentCartItemModel is CheckoutOrderModel) {
                    val logPromoCode = shipmentCartItemModel.shipment.courierItemData?.logPromoCode
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
//            if (unappliedBoPromoUniqueIds.size > 0) {
//                view?.renderUnapplyBoIncompleteShipment(unappliedBoPromoUniqueIds)
//            }
            toBeAppliedVoucherOrders.forEach { voucher ->
                val cartPosition = checkoutItems.indexOfFirst { it is CheckoutOrderModel && it.cartStringGroup == voucher.cartStringGroup }
                val order = checkoutItems[cartPosition] as CheckoutOrderModel
                val result = logisticProcessor.getRatesWithBoCode(
                    logisticProcessor.getRatesParam(
                        order,
                        helper.getOrderProducts(checkoutItems, order.cartStringGroup),
                        checkoutItems.address()!!.recipientAddressModel,
                        isTradeIn,
                        isTradeInByDropOff,
                        codData,
                        cartDataForRates
                    ),
                    order.shopShipmentList,
                    voucher.shippingId,
                    voucher.spId,
                    order,
                    isTradeInByDropOff,
                    voucher.code
                )
                if (result?.first != null) {
                    val courierItemData = result.first
                    val shouldValidatePromo =
                        !result.first.selectedShipper.logPromoCode.isNullOrEmpty()
                    if (shouldValidatePromo) {
                        val validateUsePromoRequest = generateValidateUsePromoRequest()
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
                        //                        removeInvalidBoCodeFromPromoRequest(
                        //                            shipmentGetCourierHolderData,
                        //                            validateUsePromoRequest
                        //                        )
                        checkoutItems = promoProcessor.validateUseLogisticPromo(
                            validateUsePromoRequest,
                            order.cartStringGroup,
                            result.first.logPromoCode!!,
                            checkoutItems,
                            courierItemData,
                            isOneClickShipment,
                            isTradeIn,
                            isTradeInByDropOff
                        ).toMutableList()
//                        doValidateUseLogisticPromoNew(
//                            cartPosition,
//                            order.cartStringGroup,
//                            validateUsePromoRequest,
//                            result.first.logPromoCode!!,
//                            false,
//                            result.first,
//                            result.second
//                        )
//                        return
                    }
                } else {
                    val newOrderModel = order.copy(
                        shipment = order.shipment.copy(
                            isLoading = false,
                            courierItemData = result?.first,
                            shippingCourierUiModels = result?.third ?: emptyList(),
                            insurance = result?.second?.let {
                                CheckoutOrderInsurance(
                                    when (result.second.insuranceType) {
                                        InsuranceConstant.INSURANCE_TYPE_MUST -> {
                                            true
                                        }

                                        InsuranceConstant.INSURANCE_TYPE_NO -> {
                                            false
                                        }

                                        InsuranceConstant.INSURANCE_TYPE_OPTIONAL -> {
                                            result.second.insuranceUsedDefault == InsuranceConstant.INSURANCE_USED_DEFAULT_YES
                                        }

                                        else -> false
                                    }
                                )
                            } ?: CheckoutOrderInsurance()
                        )
                    )
                    checkoutItems[cartPosition] = newOrderModel
                }
//                listData.value = list
//                cartProcessor.processSaveShipmentState(
//                    newOrderModel,
//                    listData.value.address()!!.recipientAddressModel
//                )
//                calculateTotal()
            }
//            for (voucherOrders in toBeAppliedVoucherOrders) {
//                reloadedUniqueIds.add(voucherOrders.cartStringGroup)
//            }
            listData.value = checkoutItems
            if (firstScrollIndex != -1) {
                pageState.value = CheckoutPageState.ScrollTo(firstScrollIndex)
            }
            pageState.value = CheckoutPageState.Normal
            launch {
                cartProcessor.processSaveShipmentState(
                    listData.value,
                    listData.value.address()!!.recipientAddressModel
                )
            }
            calculateTotal()
        }
    }

    private suspend fun hitClearAllBo() {
        promoProcessor.clearAllBo(listData.value)
    }

    fun setAddon(
        checked: Boolean,
        addOnProductDataItemModel: AddOnProductDataItemModel,
        product: CheckoutProductModel,
        position: Int
    ) {
        val checkoutItems = listData.value.toMutableList()
        val checkoutProductModel = checkoutItems[position] as CheckoutProductModel
        val oldList = checkoutProductModel.addOnProduct.listAddOnProductData
        val newProduct = checkoutProductModel.copy(
            addOnProduct = checkoutProductModel.addOnProduct.copy(
                listAddOnProductData = oldList.map {
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
        checkoutItems[position] = newProduct
        viewModelScope.launch {
            addOnProcessor.saveAddonsProduct(newProduct, isOneClickShipment)
        }
        listData.value = checkoutItems
        calculateTotal()
//        shipmentViewModel.saveAddOnsProduct(cartItemModel)
//        shipmentAdapter.checkHasSelectAllCourier(true, -1, "", false, false)
//        shipmentAdapter.updateSubtotal()
    }

    fun setSelectedCourierInsurance(checked: Boolean, order: CheckoutOrderModel, position: Int) {
        if (order.shipment.insurance.isCheckInsurance == checked) return
        val checkoutItems = listData.value.toMutableList()
        val checkoutOrderModel = checkoutItems[position] as CheckoutOrderModel
        val newOrder = checkoutOrderModel.copy(shipment = checkoutOrderModel.shipment.copy(insurance = checkoutOrderModel.shipment.insurance.copy(isCheckInsurance = checked)))
        checkoutItems[position] = newOrder
        listData.value = checkoutItems
        calculateTotal()
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

internal fun List<CheckoutItem>.address(): CheckoutAddressModel? {
    val item = getOrNull(1)
    return item as? CheckoutAddressModel
}

internal fun List<CheckoutItem>.upsell(): CheckoutUpsellModel? {
    val item = getOrNull(2)
    return item as? CheckoutUpsellModel
}

internal fun List<CheckoutItem>.epharmacy(): CheckoutEpharmacyModel? {
    val item = getOrNull(size - 4)
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
