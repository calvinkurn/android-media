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
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPromoModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutUpsellModel
import com.tokopedia.checkout.view.CheckoutMutableLiveData
import com.tokopedia.checkout.view.converter.ShipmentDataRequestConverter
import com.tokopedia.checkout.view.uimodel.ShipmentAddOnSummaryModel
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
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
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
                campaignTimer = saf.cartShipmentAddressFormData.campaignTimerUi
                logisticProcessor.isBoUnstackEnabled =
                    saf.cartShipmentAddressFormData.lastApplyData.additionalInfo.bebasOngkirInfo.isBoUnstackEnabled

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

    internal fun calculateTotal() {
        viewModelScope.launch(dispatchers.immediate) {
            listData.value = calculator.calculateWithoutPayment(listData.value, isTradeInByDropOff)
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
                calculator.updateShipmentCostModel(listData.value, cost, isTradeInByDropOff)
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
        return logisticProcessor.getProductForRatesRequest(order)
    }

    fun generateRatesMvcParam(cartStringGroup: String): String {
        return logisticProcessor.generateRatesMvcParam(cartStringGroup)
    }

    fun loadShipping(order: CheckoutOrderModel, cartPosition: Int) {
        if (order.isShowScheduleDelivery) {
            viewModelScope.launch(dispatchers.immediate) {
                loadShippingNormal(cartPosition, order)
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

        val result = logisticProcessor.getRates(
            logisticProcessor.getRatesParam(
                order,
                listData.value.address()!!.recipientAddressModel,
                isTradeIn,
                isTradeInByDropOff,
                codData,
                cartDataForRates
            ),
            order.shopShipmentList,
            order.shippingId,
            order.spId,
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
                        result.first
                    )
                    return
                }
            }
            val newOrderModel = orderModel.copy(
                shipment = orderModel.shipment.copy(
                    isLoading = false,
                    courierItemData = result?.first,
                    shippingCourierUiModels = result?.second ?: emptyList()
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
                listData.value.address()!!.recipientAddressModel,
                isTradeIn,
                isTradeInByDropOff,
                codData,
                cartDataForRates
            ),
            order.shopShipmentList,
            order.shippingId,
            order.spId,
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
                        result.first
                    )
                    return
                }
            }
            val newOrderModel = orderModel.copy(
                shipment = orderModel.shipment.copy(
                    isLoading = false,
                    courierItemData = result?.first,
                    shippingCourierUiModels = result?.second ?: emptyList()
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
        shippingCourierUiModels: List<ShippingCourierUiModel>
    ) {
        viewModelScope.launch(dispatchers.immediate) {
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
                    shippingCourierUiModels = shippingCourierUiModels
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
                        checkoutOrderModel.products.first().preOrderDurationDay.toString(),
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
            calculateTotal()
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

    fun doValidateUseLogisticPromoNew(
        cartPosition: Int,
        cartString: String,
        validateUsePromoRequest: ValidateUsePromoRequest,
        promoCode: String,
        showLoading: Boolean,
        courierItemData: CourierItemData
    ) {
        viewModelScope.launch {
            if (showLoading) {
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
                courierItemData
            )
            listData.value = newItems
            calculateTotal()
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

    fun checkout() {
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
            }
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
                        )
                    )
                )
            } else {
                newList.add(checkoutCrossSellItem)
            }
        }
        checkoutItems[checkoutItems.size - 2] = crossSellGroup.copy(crossSellList = newList)
        listData.value = checkoutItems
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

// internal fun <R> List<CheckoutItem>.firstOrNullInstanceOf(kClass: Class<R>): R? {
//    val item = firstOrNull { kClass.isInstance(it) }
//    @Suppress("UNCHECKED_CAST")
//    return item as? R
// }

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

internal fun List<CheckoutItem>.promo(): CheckoutPromoModel? {
    val item = getOrNull(size - 4)
    @Suppress("UNCHECKED_CAST")
    return item as? CheckoutPromoModel
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
