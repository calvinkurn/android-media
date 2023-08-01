package com.tokopedia.oneclickcheckout.order.view

import com.google.gson.JsonParser
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.localizationchooseaddress.common.ChosenAddressTokonow
import com.tokopedia.localizationchooseaddress.domain.mapper.TokonowWarehouseMapper
import com.tokopedia.logisticCommon.data.constant.AddressConstant
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData.Companion.ERROR_DISTANCE_LIMIT_EXCEEDED
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData.Companion.ERROR_WEIGHT_LIMIT_EXCEEDED
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.DEFAULT_LOCAL_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.view.model.Failure
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.common.view.model.OccMutableLiveData
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryPageEnhanceECommerce
import com.tokopedia.oneclickcheckout.order.data.gocicil.GoCicilInstallmentRequest
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccProfileRequest
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccRequest
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccRequest.Companion.SOURCE_UPDATE_OCC_ADDRESS
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccRequest.Companion.SOURCE_UPDATE_OCC_PAYMENT
import com.tokopedia.oneclickcheckout.order.view.mapper.AddOnMapper
import com.tokopedia.oneclickcheckout.order.view.model.AddressState
import com.tokopedia.oneclickcheckout.order.view.model.CheckoutOccResult
import com.tokopedia.oneclickcheckout.order.view.model.OccButtonState
import com.tokopedia.oneclickcheckout.order.view.model.OccOnboarding
import com.tokopedia.oneclickcheckout.order.view.model.OccToasterAction
import com.tokopedia.oneclickcheckout.order.view.model.OccUIMessage
import com.tokopedia.oneclickcheckout.order.view.model.OrderCart
import com.tokopedia.oneclickcheckout.order.view.model.OrderCost
import com.tokopedia.oneclickcheckout.order.view.model.OrderEnableAddressFeature
import com.tokopedia.oneclickcheckout.order.view.model.OrderPayment
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentGoCicilTerms
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentInstallmentTerm
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentWalletActionData
import com.tokopedia.oneclickcheckout.order.view.model.OrderPreference
import com.tokopedia.oneclickcheckout.order.view.model.OrderProduct
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfile
import com.tokopedia.oneclickcheckout.order.view.model.OrderPromo
import com.tokopedia.oneclickcheckout.order.view.model.OrderShipment
import com.tokopedia.oneclickcheckout.order.view.model.OrderShippingDuration
import com.tokopedia.oneclickcheckout.order.view.model.OrderShop
import com.tokopedia.oneclickcheckout.order.view.model.OrderTotal
import com.tokopedia.oneclickcheckout.order.view.processor.OrderSummaryPageCalculator
import com.tokopedia.oneclickcheckout.order.view.processor.OrderSummaryPageCartProcessor
import com.tokopedia.oneclickcheckout.order.view.processor.OrderSummaryPageCheckoutProcessor
import com.tokopedia.oneclickcheckout.order.view.processor.OrderSummaryPageLogisticProcessor
import com.tokopedia.oneclickcheckout.order.view.processor.OrderSummaryPagePaymentProcessor
import com.tokopedia.oneclickcheckout.order.view.processor.OrderSummaryPagePromoProcessor
import com.tokopedia.oneclickcheckout.order.view.processor.ResultRates
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import com.tokopedia.purchase_platform.common.feature.addonsproduct.data.model.AddOnsProductDataModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.SaveAddOnStateResult
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.LastApplyUiMapper
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata
import com.tokopedia.purchase_platform.common.utils.isBlankOrZero
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderSummaryPageViewModel @Inject constructor(
    private val executorDispatchers: CoroutineDispatchers,
    private val cartProcessor: OrderSummaryPageCartProcessor,
    private val logisticProcessor: OrderSummaryPageLogisticProcessor,
    private val checkoutProcessor: OrderSummaryPageCheckoutProcessor,
    private val promoProcessor: OrderSummaryPagePromoProcessor,
    val paymentProcessor: Lazy<OrderSummaryPagePaymentProcessor>,
    private val calculator: OrderSummaryPageCalculator,
    private val userSession: UserSessionInterface,
    private val orderSummaryAnalytics: OrderSummaryAnalytics,
    private val eligibleForAddressUseCase: EligibleForAddressUseCase
) : BaseViewModel(executorDispatchers.immediate) {

    init {
        initCalculator()
    }

    var orderCart: OrderCart = OrderCart()
    val orderShop: OccMutableLiveData<OrderShop> = OccMutableLiveData(OrderShop())
    val orderProducts: OccMutableLiveData<List<OrderProduct>> = OccMutableLiveData(emptyList())
    val updateOrderProducts: OccMutableLiveData<List<Int>> = OccMutableLiveData(emptyList())

    var orderPreferenceData: OrderPreference = OrderPreference()
    val orderPreference: OccMutableLiveData<OccState<OrderPreference>> = OccMutableLiveData(OccState.Loading)
    val orderShippingDuration: OccMutableLiveData<OccState<OrderShippingDuration>> =
        OccMutableLiveData(
            OccState.FirstLoad(
                OrderShippingDuration()
            )
        )

    val orderProfile: OccMutableLiveData<OrderProfile> = OccMutableLiveData(OrderProfile(enable = false))
    val orderShipment: OccMutableLiveData<OrderShipment> = OccMutableLiveData(OrderShipment())
    val orderPayment: OccMutableLiveData<OrderPayment> = OccMutableLiveData(OrderPayment())

    var validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel? = null
    var lastValidateUsePromoRequest: ValidateUsePromoRequest? = null
    val orderPromo: OccMutableLiveData<OrderPromo> = OccMutableLiveData(OrderPromo())

    val orderTotal: OccMutableLiveData<OrderTotal> = OccMutableLiveData(OrderTotal())
    val globalEvent: OccMutableLiveData<OccGlobalEvent> = OccMutableLiveData(OccGlobalEvent.Normal)

    val addressState: OccMutableLiveData<AddressState> = OccMutableLiveData(AddressState())

    val eligibleForAnaRevamp = OccMutableLiveData<OccState<OrderEnableAddressFeature>>(OccState.Loading)

    val uploadPrescriptionUiModel: OccMutableLiveData<UploadPrescriptionUiModel> = OccMutableLiveData(UploadPrescriptionUiModel())

    private var getCartJob: Job? = null
    private var debounceJob: Job? = null
    private var finalUpdateJob: Job? = null
    private var dynamicPaymentFeeJob: Job? = null
    private val saveAddOnProductStateJobs: MutableMap<String, Job> = mutableMapOf()

    private var hasSentViewOspEe = false

    fun getShopId(): String {
        return orderCart.shop.shopId
    }

    fun getActivationData(): OrderPaymentWalletActionData {
        return orderPayment.value.walletData.activation
    }

    fun atcOcc(productIds: String) {
        launch(executorDispatchers.immediate) {
            globalEvent.value = OccGlobalEvent.Loading
            globalEvent.value = cartProcessor.atcOcc(productIds, userSession.userId)
        }
    }

    private fun isInvalidAddressState(profile: OrderProfile, addressState: AddressState): Boolean {
        return profile.address.addressId.isBlankOrZero() && addressState.errorCode != AddressState.ERROR_CODE_OPEN_ANA
    }

    fun getOccCart(
        source: String,
        uiMessage: OccUIMessage? = null,
        gatewayCode: String = "",
        tenor: Int = 0
    ) {
        getCartJob?.cancel()
        getCartJob = launch(executorDispatchers.immediate) {
            globalEvent.value = OccGlobalEvent.Normal
            val result = cartProcessor.getOccCart(source, gatewayCode, tenor)
            addressState.value = result.addressState
            orderCart = result.orderCart
            orderShop.value = orderCart.shop
            orderProducts.value = orderCart.products
            updateOrderProducts.value = emptyList()
            orderProfile.value = result.orderProfile
            orderPreferenceData = result.orderPreference
            val isValidAddressState = !isInvalidAddressState(result.orderProfile, result.addressState)
            orderPreference.value = if (result.throwable == null && isValidAddressState) {
                OccState.FirstLoad(result.orderPreference)
            } else {
                OccState.Failed(Failure(result.throwable))
            }
            orderShipment.value = OrderShipment()
                .copy(isShowLogisticPromoTickerMessage = orderShipment.value.isShowLogisticPromoTickerMessage)
            orderPayment.value = result.orderPayment
            validateUsePromoRevampUiModel = null
            lastValidateUsePromoRequest = null
            orderPromo.value = result.orderPromo
            when {
                result.globalEvent != null -> {
                    globalEvent.value = result.globalEvent
                }
                uiMessage is OccToasterAction -> {
                    globalEvent.value = OccGlobalEvent.ToasterAction(uiMessage)
                }
                result.addressState.popupMessage.isNotBlank() -> {
                    globalEvent.value = OccGlobalEvent.ToasterInfo(result.addressState.popupMessage)
                }
            }
            if (orderCart.products.isNotEmpty() && result.orderProfile.isValidProfile) {
                orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.LOADING)
                getRatesSuspend()
                sendPaymentTracker()
            } else {
                orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.DISABLE)
            }
            if (result.imageUpload.showImageUpload) {
                val prescriptionIds = cartProcessor.getPrescriptionId(result.imageUpload.checkoutId)
                uploadPrescriptionUiModel.value = uploadPrescriptionUiModel.value.copy(
                    showImageUpload = result.imageUpload.showImageUpload,
                    uploadImageText = result.imageUpload.text,
                    leftIconUrl = result.imageUpload.leftIconUrl,
                    checkoutId = result.imageUpload.checkoutId,
                    prescriptionIds = prescriptionIds.prescriptionIds,
                    uploadedImageCount = prescriptionIds.prescriptionIds.size,
                    isError = false,
                    frontEndValidation = result.imageUpload.frontEndValidation,
                    isOcc = true
                )
            }
        }
    }

    private fun sendViewOspEe() {
        if (!hasSentViewOspEe) {
            orderSummaryAnalytics.eventViewOrderSummaryPage(userSession.userId, orderProfile.value.payment.gatewayName, generateOspEeBody().build(OrderSummaryPageEnhanceECommerce.STEP_1, OrderSummaryPageEnhanceECommerce.STEP_1_OPTION))
            for (product in orderCart.products) {
                if (!product.isError && product.purchaseProtectionPlanData.isProtectionAvailable) {
                    orderSummaryAnalytics.eventPPImpressionOnInsuranceSection(userSession.userId, product.categoryId, product.purchaseProtectionPlanData.protectionPricePerProduct, product.purchaseProtectionPlanData.protectionTitle)
                }
            }
            hasSentViewOspEe = true
        }
    }

    private fun sendPaymentTracker() {
        val payment = orderPayment.value
        orderSummaryAnalytics.eventViewPaymentMethod(payment.gatewayName)
        if (payment.creditCard.selectedTerm != null) {
            orderSummaryAnalytics.eventViewTenureOption(payment.creditCard.selectedTerm.term.toString())
        }
    }

    private fun generateOspEeBody(promoCodes: List<String> = emptyList()): OrderSummaryPageEnhanceECommerce {
        val products = orderProducts.value
        return OrderSummaryPageEnhanceECommerce().apply {
            for (orderProduct in products) {
                setName(orderProduct.productName)
                setId(orderProduct.productId)
                setPrice(orderProduct.productPrice.toString())
                setBrand(null)
                setCategory(orderProduct.category)
                setVariant(null)
                setQuantity(orderProduct.orderQuantity.toString())
                setListName(orderProduct.productTrackerData.trackerListName)
                setAttribution(orderProduct.productTrackerData.attribution)
                setDiscountedPrice(orderProduct.isSlashPrice)
                setWarehouseId(orderShop.value.warehouseId)
                setProductWeight(orderProduct.weight.toString())
                setPromoCode(promoCodes)
                setPromoDetails("")
                setProductType(orderProduct.freeShippingName)
                setCartId(orderProduct.cartId)
                setBuyerAddressId(orderProfile.value.address.addressId)
                setSpid(orderShipment.value.getRealShipperProductId().toString())
                setCodFlag(false)
                setCornerFlag(false)
                setIsFullfilment(orderShop.value.isFulfillment)
                setShopIdDimension(orderShop.value.shopId)
                setShopNameDimension(orderShop.value.shopName)
                setShopTypeDimension(orderShop.value.isOfficial, orderShop.value.isGold)
                setCategoryId(orderProduct.categoryId)
                if (orderShipment.value.getRealShipperProductId() > 0) {
                    setShippingPrice(orderShipment.value.getRealShippingPrice().toString())
                } else {
                    setShippingPrice("")
                }
                setShippingDuration(orderShipment.value.serviceDuration)
                setCampaignId(orderProduct.campaignId)
                saveData()
            }
        }
    }

    fun consumeForceShowOnboarding() {
        val onboarding = orderPreferenceData.onboarding
        if (onboarding.isForceShowCoachMark) {
            orderPreferenceData = orderPreferenceData.copy(onboarding = onboarding.copy(isForceShowCoachMark = false))
            globalEvent.value = OccGlobalEvent.Normal
        }
    }

    private fun configureForceShowOnboarding() {
        val onboarding = orderPreferenceData.onboarding
        if (onboarding.isForceShowCoachMark && orderProfile.value.isValidProfile && orderShipment.value.isValid()) {
            forceShowOnboarding(onboarding)
        }
    }

    private fun forceShowOnboarding(onboarding: OccOnboarding) {
        val currentGlobalEvent = globalEvent.value
        val hasBlockingGlobalEvent = currentGlobalEvent is OccGlobalEvent.Loading || currentGlobalEvent is OccGlobalEvent.Prompt
        if (!hasBlockingGlobalEvent) {
            globalEvent.value = OccGlobalEvent.ForceOnboarding(onboarding)
        }
    }

    fun updateProduct(product: OrderProduct, productIndex: Int, shouldReloadRates: Boolean = true) {
        orderCart.products[productIndex] = product
        if (shouldReloadRates) {
            orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.LOADING)
            orderShipment.value = orderShipment.value.copy(isLoading = true)
            debounce()
        }
    }

    private fun debounce() {
        debounceJob?.cancel()
        debounceJob = launch(executorDispatchers.immediate) {
            delay(DEBOUNCE_TIME)
            if (isActive) {
                updateCart()
                if (orderProfile.value.isValidProfile) {
                    getRates()
                }
            }
        }
    }

    fun reloadRates() {
        if (orderProfile.value.isValidProfile && orderTotal.value.buttonState != OccButtonState.LOADING) {
            orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.LOADING)
            debounceJob?.cancel()
            updateCart()
            getRates()
        }
    }

    fun getRates() {
        launch(executorDispatchers.immediate) {
            orderShipment.value = orderShipment.value.copy(isLoading = true)
            orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.LOADING)
            getRatesSuspend()
        }
    }

    private suspend fun getRatesSuspend() {
        val result = if (!cartProcessor.isOrderNormal(orderCart)) {
            logisticProcessor.generateOrderErrorResultRates(orderProfile.value)
        } else if (orderProfile.value.isDisableChangeCourierAndNeedPinpoint()) {
            logisticProcessor.generateNeedPinpointResultRates(orderProfile.value)
        } else {
            val (orderCost, updatedProductIndex) = calculator.calculateOrderCostWithoutPaymentFee(
                orderCart,
                orderShipment.value,
                validateUsePromoRevampUiModel,
                orderPayment.value
            )
            updateOrderProducts.value = updatedProductIndex
            logisticProcessor.getRates(
                orderCart,
                orderProfile.value,
                orderShipment.value,
                orderCost,
                orderShop.value.shopShipment
            )
        }

        if (result.throwable is AkamaiErrorException) {
            globalEvent.value = OccGlobalEvent.Error(result.throwable)
        }

        var hasOldPromoCode = result.clearOldPromoCode.isNotEmpty()
        if (hasOldPromoCode) {
            clearOldLogisticPromo(result.clearOldPromoCode)
        }
        if (result.autoApplyPromo != null) {
            autoApplyLogisticPromo(
                result.autoApplyPromo,
                result.clearOldPromoCode,
                result.orderShipment,
                result
            )
            return
        }
        if (!hasOldPromoCode) {
            val promo = orderPromo.value
            val unexpectedBoVoucher =
                promo.lastApply.voucherOrders.firstOrNull {
                    it.uniqueId == orderCart.cartString &&
                        it.shippingId > 0 &&
                        it.spId > 0 &&
                        it.type == "logistic"
                }
            if (unexpectedBoVoucher != null) {
                promoProcessor.clearOldLogisticPromo(unexpectedBoVoucher.code, orderCart)
                promoProcessor.clearOldLogisticPromoFromLastRequest(
                    lastValidateUsePromoRequest,
                    unexpectedBoVoucher.code
                )
                val newVoucherList = promo.lastApply.voucherOrders.toMutableList()
                newVoucherList.remove(unexpectedBoVoucher)
                orderPromo.value =
                    promo.copy(lastApply = promo.lastApply.copy(voucherOrders = newVoucherList))
                hasOldPromoCode = true
            }
        }
        if (hasOldPromoCode) {
            orderShipment.value = result.orderShipment
                .copy(isShowLogisticPromoTickerMessage = true)
        } else {
            orderShipment.value = result.orderShipment
                .copy(isShowLogisticPromoTickerMessage = orderShipment.value.isShowLogisticPromoTickerMessage)
        }
        sendViewOspEe()
        sendPreselectedCourierOption(result.preselectedSpId)
        if (result.overweight != null) {
            orderShop.value = orderShop.value.copy(overweight = result.overweight)
            orderProfile.value = orderProfile.value.copy(enable = false)
            orderTotal.value = OrderTotal()
            orderPromo.value = orderPromo.value.copy(isDisabled = true)
        } else {
            orderShop.value = orderShop.value.copy(overweight = 0.0)
            orderProfile.value = orderProfile.value.copy(enable = true)
            orderPromo.value = orderPromo.value.copy(isDisabled = result.orderShipment.isDisabled)
            if (result.orderShipment.serviceErrorMessage.isNullOrEmpty()) {
                validateUsePromo(hasOldPromoCode)
            } else {
                if (hasOldPromoCode) {
                    validateUsePromo(hasOldPromoCode)
                }
                sendViewShippingErrorMessage(result.shippingErrorId)
                calculateTotal()
            }
        }
        updateCart()
        configureForceShowOnboarding()
    }

    private fun sendViewShippingErrorMessage(shippingErrorId: String?) {
        if (shippingErrorId == ERROR_DISTANCE_LIMIT_EXCEEDED) {
            orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_DISTANCE_EXCEED)
        } else if (shippingErrorId == ERROR_WEIGHT_LIMIT_EXCEEDED) {
            orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_WEIGHT_EXCEED)
        }
    }

    private fun sendPreselectedCourierOption(preselectedSpId: String?) {
        if (preselectedSpId != null) {
            orderSummaryAnalytics.eventViewPreselectedCourierOption(preselectedSpId, userSession.userId)
        }
    }

    private fun clearOldLogisticPromo(oldPromoCode: String) {
        launch(executorDispatchers.io) {
            promoProcessor.clearOldLogisticPromo(oldPromoCode, orderCart)
        }
        promoProcessor.clearOldLogisticPromoFromLastRequest(lastValidateUsePromoRequest, oldPromoCode)
    }

    private fun autoApplyLogisticPromo(logisticPromoUiModel: LogisticPromoUiModel, oldCode: String, shipping: OrderShipment, ratesResult: ResultRates) {
        launch(executorDispatchers.immediate) {
            cartProcessor.updateCartIgnoreResult(orderCart, orderProfile.value, shipping, orderPayment.value)
            orderPromo.value = orderPromo.value.copy(state = OccButtonState.LOADING)
            orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.LOADING)
            val (isApplied, resultValidateUse, newGlobalEvent) = promoProcessor.validateUseLogisticPromo(generateValidateUsePromoRequestWithBbo(logisticPromoUiModel, oldCode), logisticPromoUiModel.promoCode)
            orderShop.value = orderShop.value.copy(overweight = 0.0)
            orderProfile.value = orderProfile.value.copy(enable = true)
            if (isApplied && resultValidateUse != null) {
                val (newShipment, newEvent) = logisticProcessor.onApplyBbo(shipping, logisticPromoUiModel, newGlobalEvent)
                if (newShipment != null) {
                    orderShipment.value = newShipment
                    validateUsePromoRevampUiModel = resultValidateUse
                    globalEvent.value = newEvent
                    updatePromoState(resultValidateUse.promoUiModel)
                    updateCart()
                    sendViewOspEe()
                    sendPreselectedCourierOption(ratesResult.preselectedSpId)
                    configureForceShowOnboarding()
                    return@launch
                }
            }
            orderShipment.value = if (orderProfile.value.shipment.isDisableChangeCourier) {
                shipping.copy(serviceErrorMessage = FAIL_GET_RATES_ERROR_MESSAGE, isApplyLogisticPromo = false, logisticPromoShipping = null)
            } else {
                shipping.copy(logisticPromoTickerMessage = if (shipping.serviceErrorMessage.isNullOrEmpty()) logisticPromoUiModel.tickerAvailableFreeShippingCourierTitle else null, isApplyLogisticPromo = false, logisticPromoShipping = null)
            }
            if (resultValidateUse != null) {
                validateUsePromoRevampUiModel = resultValidateUse
                globalEvent.value = OccGlobalEvent.Normal
                updatePromoState(resultValidateUse.promoUiModel)
                updateCart()
                sendViewOspEe()
                sendPreselectedCourierOption(ratesResult.preselectedSpId)
                configureForceShowOnboarding()
                return@launch
            }
            clearAllPromoFromLastRequest()
            calculateTotal()
            globalEvent.value = newGlobalEvent
            updateCart()
            sendViewOspEe()
            sendPreselectedCourierOption(ratesResult.preselectedSpId)
            configureForceShowOnboarding()
        }
    }

    fun clearBboIfExist(): Boolean {
        val logisticPromoViewModel = orderShipment.value.logisticPromoViewModel
        if (logisticPromoViewModel != null && orderShipment.value.isApplyLogisticPromo && orderShipment.value.logisticPromoShipping != null) {
            clearOldLogisticPromo(logisticPromoViewModel.promoCode)
            return true
        }
        return false
    }

    private fun resetBbo() {
        orderShipment.value = logisticProcessor.resetBbo(orderShipment.value)
    }

    fun validateBboStacking() {
        var hasUnApply = false
        var hasApply = false
        validateUsePromoRevampUiModel?.let {
            it.promoUiModel.voucherOrderUiModels.let { voucherOrders ->
                for (voucherOrderUiModel in voucherOrders) {
                    if (voucherOrderUiModel.shippingId > 0 &&
                        voucherOrderUiModel.spId > 0 &&
                        voucherOrderUiModel.type == "logistic"
                    ) {
                        if (voucherOrderUiModel.messageUiModel.state == "green") {
                            applyBbo(voucherOrderUiModel.code)
                            hasApply = true
                        }
                    }
                }
                if (orderShipment.value.isApplyLogisticPromo && !hasApply) {
                    // if use BO but voucher BO didn't exist
                    orderShipment.value.logisticPromoViewModel?.let { logisticPromo ->
                        unApplyBbo(logisticPromo.promoCode)
                        hasUnApply = true
                    }
                }
            }
        }
        displayingAdjustmentPromoToaster(hasUnApply)
    }

    private fun unApplyBbo(code: String) {
        orderShipment.value = orderShipment.value.copy(isApplyLogisticPromo = false)
        clearOldLogisticPromo(code)
    }

    private fun applyBbo(code: String) {
        if (orderShipment.value.logisticPromoViewModel == null ||
            orderShipment.value.logisticPromoViewModel!!.promoCode != code ||
            !orderShipment.value.isApplyLogisticPromo
        ) {
            orderShipment.value = orderShipment.value.copy(
                isApplyLogisticPromo = true,
                logisticPromoViewModel = LogisticPromoUiModel(promoCode = code)
            )
        }
    }

    private fun displayingAdjustmentPromoToaster(hasUnApply: Boolean) {
        validateUsePromoRevampUiModel?.let {
            it.promoUiModel.additionalInfoUiModel.errorDetailUiModel.message.let { errMessage ->
                if (errMessage.isNotBlank()) {
                    globalEvent.value = OccGlobalEvent.ToasterInfo(errMessage)
                } else if (hasUnApply) {
                    globalEvent.value = OccGlobalEvent.AdjustShippingToaster
                }
            }
        }
    }

    fun autoUnApplyBBO() {
        lastValidateUsePromoRequest?.let { validateUsePromo ->
            validateUsePromo.orders.firstOrNull { it.uniqueId == orderCart.cartString }?.let { orderItem ->
                if (orderItem.codes.isEmpty()) {
                    orderShipment.value = orderShipment.value.copy(isApplyLogisticPromo = false)
                }
            }
        }
    }

    fun chooseDuration(selectedServiceId: Int, selectedShippingCourierUiModel: ShippingCourierUiModel, flagNeedToSetPinpoint: Boolean) {
        val newOrderShipment = logisticProcessor.chooseDuration(selectedServiceId, selectedShippingCourierUiModel, flagNeedToSetPinpoint, orderShipment.value)
        newOrderShipment?.let {
            val isBoExist = clearBboIfExist()
            orderShipment.value = it
            sendPreselectedCourierOption(selectedShippingCourierUiModel.productData.shipperProductId.toString())
            if (it.serviceErrorMessage.isNullOrEmpty()) {
                validateUsePromo(isBoExist)
            } else {
                calculateTotal()
            }
        }
    }

    fun changePinpoint() {
        if (orderShipment.value.needPinpoint) {
            orderShipment.value.needPinpoint = false
        }
    }

    fun savePinpoint(longitude: String, latitude: String) {
        launch(executorDispatchers.immediate) {
            if (getCartJob?.isCompleted == false) {
                getCartJob?.join()
                if (orderPreference.value !is OccState.FirstLoad) {
                    return@launch
                }
            }
            if (!orderProfile.value.isValidProfile) {
                globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE)
                return@launch
            }
            val result = logisticProcessor.savePinpoint(orderProfile.value.address, longitude, latitude, userSession.userId, userSession.deviceId)
            globalEvent.value = result
        }
    }

    fun chooseCourier(chosenShippingCourierViewModel: ShippingCourierUiModel) {
        val newOrderShipment = logisticProcessor.chooseCourier(chosenShippingCourierViewModel, orderShipment.value)
        newOrderShipment?.let {
            val isBoExist = clearBboIfExist()
            orderShipment.value = it
            validateUsePromo(isBoExist)
        }
    }

    fun setInsuranceCheck(checked: Boolean) {
        if (orderShipment.value.getRealShipperProductId() > 0 && orderShipment.value.insurance.isCheckInsurance != checked) {
            orderShipment.value.insurance.isCheckInsurance = checked
            calculateTotal()
        }
    }

    fun chooseLogisticPromo(logisticPromoUiModel: LogisticPromoUiModel) {
        launch(executorDispatchers.immediate) {
            val shipping = orderShipment.value
            val shippingRecommendationData = orderShipment.value.shippingRecommendationData
            if (shippingRecommendationData != null) {
                globalEvent.value = OccGlobalEvent.Loading
                val (isApplied, resultValidateUse, newGlobalEvent) = promoProcessor.validateUseLogisticPromo(generateValidateUsePromoRequestWithBbo(logisticPromoUiModel, shipping.logisticPromoViewModel?.promoCode), logisticPromoUiModel.promoCode)
                if (isApplied && resultValidateUse != null) {
                    val (newShipment, newEvent) = logisticProcessor.onApplyBbo(shipping, logisticPromoUiModel, newGlobalEvent)
                    if (newShipment != null) {
                        orderShipment.value = newShipment
                    }
                    validateUsePromoRevampUiModel = resultValidateUse
                    updatePromoState(resultValidateUse.promoUiModel)
                    globalEvent.value = newEvent
                    updateCart()
                    return@launch
                }
                if (resultValidateUse != null) {
                    validateUsePromoRevampUiModel = resultValidateUse
                } else {
                    clearAllPromoFromLastRequest()
                    calculateTotal()
                }
                globalEvent.value = newGlobalEvent
                updateCart()
            }
        }
    }

    fun chooseAddress(addressModel: RecipientAddressModel) {
        launch(executorDispatchers.immediate) {
            var param = cartProcessor.generateUpdateCartParam(orderCart, orderProfile.value, orderShipment.value, orderPayment.value)
            if (param == null) {
                globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE)
                return@launch
            }
            globalEvent.value = OccGlobalEvent.Loading
            val newChosenAddress = logisticProcessor.setChosenAddress(addressModel)
            if (newChosenAddress == null) {
                globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_ERROR_MESSAGE)
                return@launch
            }
            param = param.copy(
                profile = param.profile.copy(addressId = addressModel.id),
                skipShippingValidation = cartProcessor.shouldSkipShippingValidationWhenUpdateCart(orderShipment.value),
                source = SOURCE_UPDATE_OCC_ADDRESS
            )
            val chosenAddress = ChosenAddress(
                addressId = newChosenAddress.addressId.toString(),
                districtId = newChosenAddress.districtId.toString(),
                postalCode = newChosenAddress.postalCode,
                geolocation = if (newChosenAddress.latitude.isNotBlank() && newChosenAddress.longitude.isNotBlank()) newChosenAddress.latitude + "," + newChosenAddress.longitude else "",
                mode = ChosenAddress.MODE_ADDRESS,
                tokonow = ChosenAddressTokonow(
                    shopId = newChosenAddress.tokonowModel.shopId.toString(),
                    warehouseId = newChosenAddress.tokonowModel.warehouseId.toString(),
                    warehouses = TokonowWarehouseMapper.mapWarehousesModelToLocal(newChosenAddress.tokonowModel.warehouses),
                    serviceType = newChosenAddress.tokonowModel.serviceType
                )
            )
            param.chosenAddress = chosenAddress
            val (isSuccess, newGlobalEvent) = cartProcessor.updatePreference(param)
            if (isSuccess) {
                globalEvent.value = OccGlobalEvent.UpdateLocalCacheAddress(newChosenAddress)
                clearBboIfExist().also { isBoExist ->
                    orderShipment.value =
                        orderShipment.value.copy(isShowLogisticPromoTickerMessage = true)
                }
            }
            globalEvent.value = newGlobalEvent
        }
    }

    fun updateCart() {
        launch(executorDispatchers.immediate) {
            cartProcessor.updateCartIgnoreResult(orderCart, orderProfile.value, orderShipment.value, orderPayment.value)
        }
    }

    fun chooseInstallment(selectedInstallmentTerm: OrderPaymentInstallmentTerm, installmentList: List<OrderPaymentInstallmentTerm>) {
        launch(executorDispatchers.immediate) {
            var param = cartProcessor.generateUpdateCartParam(orderCart, orderProfile.value, orderShipment.value, orderPayment.value)
            val creditCard = orderPayment.value.creditCard
            if (param == null) {
                globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE)
                return@launch
            }
            globalEvent.value = OccGlobalEvent.Loading
            try {
                val metadata = JsonParser().parse(param.profile.metadata)
                val expressCheckoutParams = metadata.asJsonObject.getAsJsonObject(UpdateCartOccProfileRequest.EXPRESS_CHECKOUT_PARAM)
                if (expressCheckoutParams.get(UpdateCartOccProfileRequest.INSTALLMENT_TERM) == null) {
                    // unexpected null installment term param
                    throw IllegalStateException()
                }
                expressCheckoutParams.addProperty(UpdateCartOccProfileRequest.INSTALLMENT_TERM, selectedInstallmentTerm.term.toString())
                param = param.copy(
                    profile = param.profile.copy(metadata = metadata.toString()),
                    skipShippingValidation = cartProcessor.shouldSkipShippingValidationWhenUpdateCart(orderShipment.value),
                    source = SOURCE_UPDATE_OCC_PAYMENT
                )
            } catch (e: RuntimeException) {
                globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE)
                return@launch
            }
            val (isSuccess, newGlobalEvent) = cartProcessor.updatePreference(param)
            if (isSuccess) {
                installmentList.forEach {
                    it.isSelected = it.term == selectedInstallmentTerm.term
                    it.isError = false
                }
                orderPayment.value = orderPayment.value.copy(creditCard = creditCard.copy(selectedTerm = selectedInstallmentTerm, availableTerms = installmentList))
                validateUsePromo()
                globalEvent.value = OccGlobalEvent.Normal
                orderSummaryAnalytics.eventViewTenureOption(selectedInstallmentTerm.term.toString())
                return@launch
            }
            globalEvent.value = newGlobalEvent
        }
    }

    fun chooseInstallment(
        selectedInstallmentTerm: OrderPaymentGoCicilTerms,
        installmentList: List<OrderPaymentGoCicilTerms>,
        tickerMessage: String,
        isSilent: Boolean,
        shouldRevalidatePromo: Boolean = true
    ) {
        launch(executorDispatchers.immediate) {
            val walletData = orderPayment.value.walletData
            val newWalletData = walletData.copy(
                goCicilData = walletData.goCicilData.copy(
                    selectedTerm = selectedInstallmentTerm,
                    availableTerms = installmentList,
                    tickerMessage = tickerMessage
                )
            )
            orderPayment.value = orderPayment.value.copy(walletData = newWalletData)
            if (shouldRevalidatePromo) {
                var param = cartProcessor.generateUpdateCartParam(
                    orderCart,
                    orderProfile.value,
                    orderShipment.value,
                    orderPayment.value
                )
                if (param == null) {
                    globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE)
                } else {
                    param = param.copy(
                        skipShippingValidation = cartProcessor.shouldSkipShippingValidationWhenUpdateCart(
                            orderShipment.value
                        ),
                        source = SOURCE_UPDATE_OCC_PAYMENT
                    )
                    // ignore result, result is important only in final update
                    cartProcessor.updatePreference(param)
                }
                validateUsePromo()
            } else {
                calculateTotal(skipDynamicFee = true)
            }
            if (!isSilent) {
                orderSummaryAnalytics.eventViewTenureOption(selectedInstallmentTerm.installmentTerm.toString())
                if (!shouldRevalidatePromo) {
                    var param: UpdateCartOccRequest = cartProcessor.generateUpdateCartParam(
                        orderCart,
                        orderProfile.value,
                        orderShipment.value,
                        orderPayment.value
                    ) ?: return@launch
                    param = param.copy(
                        skipShippingValidation = cartProcessor.shouldSkipShippingValidationWhenUpdateCart(
                            orderShipment.value
                        ),
                        source = SOURCE_UPDATE_OCC_PAYMENT
                    )
                    // ignore result, result is important only in final update
                    cartProcessor.updatePreference(param)
                }
            }
        }
    }

    fun choosePayment(gatewayCode: String, metadata: String) {
        launch(executorDispatchers.immediate) {
            if (getCartJob?.isCompleted == false) {
                getCartJob?.join()
                if (orderPreference.value !is OccState.FirstLoad) {
                    return@launch
                }
            }
            var param = cartProcessor.generateUpdateCartParam(orderCart, orderProfile.value, orderShipment.value, orderPayment.value)
            if (param == null) {
                globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE)
                return@launch
            }
            param = param.copy(
                profile = param.profile.copy(gatewayCode = gatewayCode, metadata = metadata, tenureType = 0, optionId = ""),
                skipShippingValidation = cartProcessor.shouldSkipShippingValidationWhenUpdateCart(orderShipment.value),
                source = SOURCE_UPDATE_OCC_PAYMENT
            )
            globalEvent.value = OccGlobalEvent.Loading
            val (isSuccess, newGlobalEvent) = cartProcessor.updatePreference(param)
            if (isSuccess) {
                clearBboIfExist().also { isBoExist ->
                    if (orderShipment.value.isShowLogisticPromoTickerMessage) {
                        orderShipment.value =
                            orderShipment.value.copy(isShowLogisticPromoTickerMessage = true)
                    } else {
                        orderShipment.value =
                            orderShipment.value.copy(isShowLogisticPromoTickerMessage = isBoExist)
                    }
                }
            }
            globalEvent.value = newGlobalEvent
        }
    }

    fun updateCartPromo(onSuccess: (ValidateUsePromoRequest, PromoRequest, ArrayList<String>) -> Unit) {
        launch(executorDispatchers.immediate) {
            val param = cartProcessor.generateUpdateCartParam(orderCart, orderProfile.value, orderShipment.value, orderPayment.value)
            if (param == null) {
                globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE)
                return@launch
            }
            globalEvent.value = OccGlobalEvent.Loading
            val (isSuccess, newGlobalEvent) = cartProcessor.updateCartPromo(param)
            globalEvent.value = newGlobalEvent
            if (isSuccess) {
                onSuccess(generateValidateUsePromoRequest(), generatePromoRequest(), generateBboPromoCodes())
            }
        }
    }

    fun generatePromoRequest(): PromoRequest {
        return promoProcessor.generatePromoRequest(orderCart, orderShipment.value, lastValidateUsePromoRequest, orderPromo.value)
    }

    fun generateValidateUsePromoRequest(shouldAddLogisticPromo: Boolean = true): ValidateUsePromoRequest {
        val validateUsePromoRequest = promoProcessor.generateValidateUsePromoRequest(shouldAddLogisticPromo, lastValidateUsePromoRequest, orderCart, orderShipment.value, orderPromo.value)
        lastValidateUsePromoRequest = validateUsePromoRequest
        return validateUsePromoRequest
    }

    private fun generateValidateUsePromoRequestWithBbo(logisticPromoUiModel: LogisticPromoUiModel, oldCode: String? = null): ValidateUsePromoRequest {
        val validateUsePromoRequest = promoProcessor.generateValidateUsePromoRequestWithBbo(logisticPromoUiModel, oldCode, lastValidateUsePromoRequest, orderCart, orderShipment.value, orderPromo.value)
        lastValidateUsePromoRequest = validateUsePromoRequest
        return validateUsePromoRequest
    }

    fun generateBboPromoCodes(): ArrayList<String> {
        return promoProcessor.generateBboPromoCodes(orderShipment.value)
    }

    fun validateUsePromo(forceValidateUse: Boolean = false) {
        launch(executorDispatchers.immediate) {
            orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.LOADING)
            orderPromo.value = orderPromo.value.copy(state = OccButtonState.LOADING)
            cartProcessor.updateCartIgnoreResult(orderCart, orderProfile.value, orderShipment.value, orderPayment.value)
            val (resultValidateUse, newGlobalEvent, isAkamaiError) = promoProcessor.validateUsePromo(generateValidateUsePromoRequest(), validateUsePromoRevampUiModel, forceValidateUse)
            when {
                isAkamaiError && newGlobalEvent != null -> {
                    resetBbo()
                    clearAllPromoFromLastRequest()
                    calculateTotal()
                    globalEvent.value = newGlobalEvent
                }
                resultValidateUse != null -> {
                    validateUsePromoRevampUiModel = resultValidateUse
                    updatePromoState(resultValidateUse.promoUiModel)
                    if (newGlobalEvent != null) {
                        globalEvent.value = newGlobalEvent
                    }
                }
                newGlobalEvent != null && !isAkamaiError -> {
                    orderPromo.value = orderPromo.value.copy(state = OccButtonState.DISABLE)
                    orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.DISABLE)
                    globalEvent.value = newGlobalEvent
                }
                else -> {
                    validateUsePromoRevampUiModel = null
                    var promo = orderPromo.value
                    if (promo.lastApply.additionalInfo.usageSummaries.isNotEmpty()) {
                        promo = promo.copy(lastApply = LastApplyUiModel())
                    }
                    orderPromo.value = promo.copy(state = OccButtonState.NORMAL)
                    calculateTotal()
                }
            }
            updateCart()
        }
    }

    private fun clearAllPromoFromLastRequest() {
        validateUsePromoRevampUiModel = null
        lastValidateUsePromoRequest = promoProcessor.clearAllPromoFromLastRequest(lastValidateUsePromoRequest)
        orderPromo.value = OrderPromo(state = OccButtonState.NORMAL)
    }

    private fun updatePromoState(promoUiModel: PromoUiModel) {
        orderPromo.value = orderPromo.value.copy(
            lastApply = LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(promoUiModel),
            isDisabled = false,
            state = OccButtonState.NORMAL
        )
        calculateTotal()
    }

    fun updatePromoStateWithoutCalculate(promoUiModel: PromoUiModel) {
        orderPromo.value = orderPromo.value.copy(
            lastApply = LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(promoUiModel),
            isDisabled = false,
            state = OccButtonState.NORMAL
        )
    }

    fun calculateTotal(skipDynamicFee: Boolean = false) {
        orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.LOADING)
        if (orderPayment.value.creditCard.isAfpb && !skipDynamicFee) {
            dynamicPaymentFeeJob?.cancel()
            dynamicPaymentFeeJob = launch(executorDispatchers.immediate) {
                adjustCCAdminFee()
            }
        } else if (orderPayment.value.walletData.isGoCicil && !skipDynamicFee) {
            dynamicPaymentFeeJob?.cancel()
            dynamicPaymentFeeJob = launch(executorDispatchers.immediate) {
                adjustGoCicilFee()
            }
        } else {
            dynamicPaymentFeeJob?.cancel()
            dynamicPaymentFeeJob = launch(executorDispatchers.immediate) {
                adjustPaymentFee()
            }
        }
    }

    private fun initCalculator() {
        launch(executorDispatchers.immediate) {
            calculator.total.collect { (newOrderPayment, newOrderTotal) ->
                orderPayment.value = newOrderPayment
                orderTotal.value = if (orderShipment.value.isLoading || orderPromo.value.state == OccButtonState.LOADING) {
                    newOrderTotal.copy(buttonState = OccButtonState.LOADING)
                } else {
                    newOrderTotal
                }
            }
        }
    }

    private fun validateSelectedTerm(): Boolean {
        val creditCard = orderPayment.value.creditCard
        val selectedTerm = creditCard.selectedTerm
        val hasEnableTerm = creditCard.availableTerms.indexOfFirst { it.isEnable } > -1
        if (selectedTerm != null && !selectedTerm.isEnable && hasEnableTerm) {
            val availableTerms = creditCard.availableTerms
            availableTerms.forEach { it.isError = true }
            selectedTerm.isError = true
            orderPayment.value = orderPayment.value.copy(creditCard = creditCard.copy(selectedTerm = selectedTerm, availableTerms = availableTerms))
            orderTotal.value = orderTotal.value.copy(buttonState = OccButtonState.DISABLE)
            globalEvent.value = OccGlobalEvent.Error(errorMessage = INSTALLMENT_INVALID_MIN_AMOUNT)
            return false
        }
        return true
    }

    fun finalUpdate(onSuccessCheckout: (CheckoutOccResult) -> Unit, skipCheckIneligiblePromo: Boolean) {
        if (orderTotal.value.buttonState == OccButtonState.NORMAL && orderPromo.value.state == OccButtonState.NORMAL && !orderShipment.value.isLoading) {
            if (uploadPrescriptionUiModel.value.showImageUpload && uploadPrescriptionUiModel.value.uploadedImageCount < 1 && uploadPrescriptionUiModel.value.frontEndValidation) {
                uploadPrescriptionUiModel.value =
                    uploadPrescriptionUiModel.value.copy(isError = true)
                return
            }
            globalEvent.value = OccGlobalEvent.Loading
            val shop = orderShop.value
            if (orderProfile.value.isValidProfile && orderShipment.value.getRealShipperProductId() > 0) {
                val param = cartProcessor.generateUpdateCartParam(orderCart, orderProfile.value, orderShipment.value, orderPayment.value)
                if (param != null) {
                    if (validateSelectedTerm()) {
                        finalUpdateJob?.cancel()
                        finalUpdateJob = launch(executorDispatchers.immediate) {
                            // if there is at least one addon should follow logic to save all addons
                            val isAddOnProductAvailable = orderProducts.value.any { it.addOnsProductData.data.isNotEmpty() }
                            if (isAddOnProductAvailable) {
                                // before save all addons of all products making sure all jobs canceled
                                saveAddOnProductStateJobs.values.forEach { it.cancel() }
                                saveAddOnProductStateJobs.clear()

                                // save all addons of all products
                                val saveAddOnState = cartProcessor.saveAllAddOnsAllProductsState(
                                    products = orderProducts.value
                                )

                                // if save all addons of all products is not successful, execution will not continue and error toaster will be shown
                                if (!saveAddOnState.isSuccess) {
                                    globalEvent.value = OccGlobalEvent.Error(
                                        errorMessage = saveAddOnState.message,
                                        throwable = saveAddOnState.throwable,
                                        ctaText = ERROR_WHEN_SAVE_ADD_ONS_CTA_TEXT
                                    )
                                    return@launch
                                }
                            }

                            // if save all addons of all products is successful then do final validation
                            val (isSuccess, errorGlobalEvent) = cartProcessor.finalUpdateCart(param)
                            if (isSuccess) {
                                finalValidateUse(
                                    products = orderCart.products,
                                    shop = shop,
                                    profile = orderProfile.value,
                                    onSuccessCheckout = onSuccessCheckout,
                                    skipCheckIneligiblePromo = skipCheckIneligiblePromo
                                )
                                return@launch
                            }

                            // if final update to cart is failed then show global error
                            globalEvent.value = errorGlobalEvent
                        }
                    }
                    return
                }
            }
            globalEvent.value = OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE)
        }
    }

    private fun finalValidateUse(products: List<OrderProduct>, shop: OrderShop, profile: OrderProfile, onSuccessCheckout: (CheckoutOccResult) -> Unit, skipCheckIneligiblePromo: Boolean) {
        val validateUsePromoRequest = generateValidateUsePromoRequest()
        if (!skipCheckIneligiblePromo && promoProcessor.hasPromo(validateUsePromoRequest)) {
            launch(executorDispatchers.immediate) {
                val (resultValidateUse, isSuccess, newGlobalEvent) = promoProcessor.finalValidateUse(validateUsePromoRequest, orderCart)
                if (resultValidateUse != null) {
                    validateUsePromoRevampUiModel = resultValidateUse
                    updatePromoStateWithoutCalculate(resultValidateUse.promoUiModel)
                    if (isSuccess) {
                        doCheckout(products, shop, profile, onSuccessCheckout)
                        return@launch
                    }
                }
                globalEvent.value = newGlobalEvent
            }
        } else {
            doCheckout(products, shop, profile, onSuccessCheckout)
        }
    }

    private fun doCheckout(products: List<OrderProduct>, shop: OrderShop, profile: OrderProfile, onSuccessCheckout: (CheckoutOccResult) -> Unit) {
        launch(executorDispatchers.immediate) {
            val (checkoutOccResult, globalEventResult) = checkoutProcessor.doCheckout(
                validateUsePromoRevampUiModel,
                orderCart,
                products,
                shop,
                profile,
                orderShipment.value,
                orderPayment.value,
                orderTotal.value,
                userSession.userId,
                generateOspEeBody(emptyList()),
                uploadPrescriptionUiModel.value.prescriptionIds
            )
            if (checkoutOccResult != null) {
                onSuccessCheckout(checkoutOccResult)
            } else if (globalEventResult != null) {
                globalEvent.value = globalEventResult
            }
        }
    }

    fun cancelIneligiblePromoCheckout(notEligiblePromoHolderdataList: ArrayList<NotEligiblePromoHolderdata>, onSuccessCheckout: (CheckoutOccResult) -> Unit) {
        globalEvent.value = OccGlobalEvent.Loading
        launch(executorDispatchers.immediate) {
            val (isSuccess, newGlobalEvent) = promoProcessor.cancelIneligiblePromoCheckout(notEligiblePromoHolderdataList, orderCart)
            if (isSuccess && orderProfile.value.isValidProfile) {
                finalUpdate(onSuccessCheckout, true)
                return@launch
            }
            globalEvent.value = newGlobalEvent
        }
    }

    private suspend fun adjustCCAdminFee() {
        val (orderCost, _) = calculator.calculateOrderCostWithoutPaymentFee(
            orderCart,
            orderShipment.value,
            validateUsePromoRevampUiModel,
            orderPayment.value
        )
        val dynamicPaymentFee = paymentProcessor.get().getPaymentFee(orderPayment.value, orderCost)
        val newOrderPayment = orderPayment.value
        orderPayment.value = newOrderPayment.copy(dynamicPaymentFees = dynamicPaymentFee)
        if (dynamicPaymentFee == null) {
            calculator.calculateTotal(
                orderCart,
                orderProfile.value,
                orderShipment.value,
                validateUsePromoRevampUiModel,
                orderPayment.value,
                orderTotal.value
            )
            return
        }
        val installmentTermList = paymentProcessor.get().getCreditCardAdminFee(
            orderPayment.value.creditCard,
            userSession.userId,
            orderCost,
            orderCart
        )
        if (installmentTermList == null) {
            val newOrderPayment = orderPayment.value
            orderPayment.value = newOrderPayment.copy(creditCard = newOrderPayment.creditCard.copy(selectedTerm = null, availableTerms = emptyList()))
            globalEvent.value = OccGlobalEvent.AdjustAdminFeeError
        } else {
            val newOrderPayment = orderPayment.value
            val selectedTerm = orderPayment.value.creditCard.selectedTerm?.term ?: -1
            val selectedInstallmentTerm = installmentTermList.firstOrNull { it.term == selectedTerm }
            selectedInstallmentTerm?.isSelected = true
            orderPayment.value = newOrderPayment.copy(creditCard = newOrderPayment.creditCard.copy(selectedTerm = selectedInstallmentTerm, availableTerms = installmentTermList))
        }
        calculator.calculateTotal(
            orderCart,
            orderProfile.value,
            orderShipment.value,
            validateUsePromoRevampUiModel,
            orderPayment.value,
            orderTotal.value
        )
    }

    fun checkUserEligibilityForAnaRevamp(token: Token? = null) {
        eligibleForAddressUseCase.eligibleForAddressFeature(
            {
                eligibleForAnaRevamp.value = OccState.Success(OrderEnableAddressFeature(it, token))
            },
            {
                eligibleForAnaRevamp.value = OccState.Failed(Failure(it))
            },
            AddressConstant.ANA_REVAMP_FEATURE_ID
        )
    }

    fun generateGoCicilInstallmentRequest(orderCost: OrderCost): GoCicilInstallmentRequest {
        return paymentProcessor.get().generateGoCicilInstallmentRequest(
            orderPayment.value,
            userSession.userId,
            orderCost,
            orderCart,
            orderProfile.value,
            promoProcessor.getValidPromoCodes(validateUsePromoRevampUiModel)
        )
    }
    private suspend fun adjustGoCicilFee() {
        val (orderCost, _) = calculator.calculateOrderCostWithoutPaymentFee(
            orderCart,
            orderShipment.value,
            validateUsePromoRevampUiModel,
            orderPayment.value
        )
        val payment = orderPayment.value
        if (payment.minimumAmount <= orderCost.totalPriceWithoutPaymentFees &&
            orderCost.totalPriceWithoutPaymentFees <= payment.maximumAmount &&
            orderCost.totalPriceWithoutPaymentFees <= payment.walletAmount
        ) {
            val dynamicPaymentFee = paymentProcessor.get().getPaymentFee(orderPayment.value, orderCost)
            val newOrderPayment = orderPayment.value
            orderPayment.value = newOrderPayment.copy(dynamicPaymentFees = dynamicPaymentFee)
            if (dynamicPaymentFee == null) {
                calculator.calculateTotal(
                    orderCart,
                    orderProfile.value,
                    orderShipment.value,
                    validateUsePromoRevampUiModel,
                    orderPayment.value,
                    orderTotal.value
                )
                return
            }
            val result = paymentProcessor.get().getGopayAdminFee(
                generateGoCicilInstallmentRequest(orderCost),
                orderPayment.value
            )
            if (result != null) {
                chooseInstallment(
                    result.selectedInstallment,
                    result.installmentList,
                    result.tickerMessage,
                    !result.shouldUpdateCart,
                    false
                )
                return
            } else {
                val newWalletData = orderPayment.value.walletData
                orderPayment.value = orderPayment.value.copy(
                    walletData = newWalletData.copy(
                        goCicilData = newWalletData.goCicilData.copy(availableTerms = emptyList())
                    )
                )
                globalEvent.value = OccGlobalEvent.AdjustAdminFeeError
            }
        }
        calculator.calculateTotal(
            orderCart,
            orderProfile.value,
            orderShipment.value,
            validateUsePromoRevampUiModel,
            orderPayment.value,
            orderTotal.value
        )
    }

    private suspend fun adjustPaymentFee() {
        val (orderCost, _) = calculator.calculateOrderCostWithoutPaymentFee(
            orderCart,
            orderShipment.value,
            validateUsePromoRevampUiModel,
            orderPayment.value
        )
        val dynamicPaymentFee = paymentProcessor.get().getPaymentFee(orderPayment.value, orderCost)
        val newOrderPayment = orderPayment.value
        orderPayment.value = newOrderPayment.copy(dynamicPaymentFees = dynamicPaymentFee)
        calculator.calculateTotal(
            orderCart,
            orderProfile.value,
            orderShipment.value,
            validateUsePromoRevampUiModel,
            orderPayment.value,
            orderTotal.value
        )
    }

    fun updateAddOn(saveAddOnStateResult: SaveAddOnStateResult) {
        // Add on currently only support single product on OCC
        val orderProduct = orderProducts.value.first()
        val orderShop = orderShop.value
        val addOnResult = saveAddOnStateResult.addOns.firstOrNull()
        if (addOnResult != null) {
            if (addOnResult.addOnLevel == AddOnConstant.ADD_ON_LEVEL_ORDER && addOnResult.addOnKey == "${orderCart.cartString}-0") {
                orderShop.addOn = AddOnMapper.mapAddOnBottomSheetResult(addOnResult)
                this.orderShop.value = orderShop
                orderProducts.value = listOf(orderProduct)
                orderCart.shop = this.orderShop.value

                orderTotal.value = orderTotal.value.copy(
                    orderCost = orderTotal.value.orderCost.copy(
                        hasAddOn = true,
                        addOnPrice = addOnResult.addOnData.firstOrNull()?.addOnPrice
                            ?: 0.0
                    )
                )
            } else if (addOnResult.addOnLevel == AddOnConstant.ADD_ON_LEVEL_PRODUCT && addOnResult.addOnKey == "${orderCart.cartString}-${orderProduct.cartId}") {
                orderProduct.addOn = AddOnMapper.mapAddOnBottomSheetResult(addOnResult)
                orderProducts.value = listOf(orderProduct)

                orderTotal.value = orderTotal.value.copy(
                    orderCost = orderTotal.value.orderCost.copy(
                        hasAddOn = true,
                        addOnPrice = addOnResult.addOnData.firstOrNull()?.addOnPrice
                            ?: 0.0
                    )
                )
            }
        } else {
            setDefaultAddOnState(orderShop, orderProduct)
        }

        calculateTotal()
    }

    private fun setDefaultAddOnState(orderShop: OrderShop, orderProduct: OrderProduct?) {
        if (orderShop.isFulfillment) {
            orderShop.addOn = AddOnGiftingDataModel()
            this.orderShop.value = orderShop
        } else {
            orderProduct?.let {
                it.addOn = AddOnGiftingDataModel()
                orderProducts.value = listOf(it)
            }
        }
        orderTotal.value = orderTotal.value.copy(
            orderCost = orderTotal.value.orderCost.copy(
                hasAddOn = false
            )
        )
    }

    fun getShippingBottomsheetParam() {
        launch(executorDispatchers.immediate) {
            val (orderCost, _) = calculator.calculateOrderCostWithoutPaymentFee(
                orderCart,
                orderShipment.value,
                validateUsePromoRevampUiModel,
                orderPayment.value
            )
            val (shipmentDetailData, products) = logisticProcessor.generateShippingBottomsheetParam(
                orderCart,
                orderProfile.value,
                orderCost
            )
            val pslCode = if (orderShipment.value.isApplyLogisticPromo) orderShipment.value.logisticPromoViewModel?.promoCode ?: "" else ""
            shipmentDetailData?.let {
                orderShippingDuration.value = OccState.Success(
                    OrderShippingDuration(
                        shipmentDetailData = it,
                        shopShipmentList = orderShop.value.shopShipment,
                        selectedServiceId = orderShipment.value.serviceId.toZeroIfNull(),
                        products = products,
                        cartString = orderCart.cartString,
                        pslCode = pslCode,
                        cartData = orderCart.cartData,
                        warehouseId = orderCart.shop.warehouseId
                    )
                )
            }
        }
    }

    fun updatePrescriptionIds(it: ArrayList<String>) {
        uploadPrescriptionUiModel.value = uploadPrescriptionUiModel.value.copy(
            prescriptionIds = it,
            uploadedImageCount = it.size,
            isError = false
        )
    }

    fun updateAddOnProduct(
        newAddOnProductData: AddOnsProductDataModel.Data,
        product: OrderProduct
    ) {
        changeAddOnProductStatus(
            productId = product.productId,
            addOnProductId = newAddOnProductData.id,
            status = newAddOnProductData.status
        )

        val job = launch(executorDispatchers.immediate) {
            cartProcessor.saveAddOnProductState(
                newAddOnProductData = newAddOnProductData,
                product = product
            )
        }

        job.invokeOnCompletion {
            saveAddOnProductStateJobs.remove(newAddOnProductData.id)
        }
        saveAddOnProductStateJobs[newAddOnProductData.id] = job

        calculateTotal()
    }

    private fun changeAddOnProductStatus(
        productId: String,
        addOnProductId: String,
        status: Int
    ) {
        orderProducts.value.find { it.productId == productId }?.apply {
            addOnsProductData.data.find { it.id == addOnProductId }?.status = status
        }
    }

    override fun onCleared() {
        debounceJob?.cancel()
        finalUpdateJob?.cancel()
        getCartJob?.cancel()
        dynamicPaymentFeeJob?.cancel()
        eligibleForAddressUseCase.cancelJobs()
        saveAddOnProductStateJobs.values.forEach { it.cancel() }
        saveAddOnProductStateJobs.clear()
        super.onCleared()
    }

    companion object {
        const val DEBOUNCE_TIME = 1000L

        const val FAIL_GET_RATES_ERROR_MESSAGE = "Gagal menampilkan pengiriman"
        const val NO_COURIER_SUPPORTED_ERROR_MESSAGE = "Tidak ada kurir yang mendukung pengiriman ini ke lokasi Anda."
        const val NO_DURATION_AVAILABLE = "Durasi pengiriman tidak tersedia"
        const val NEED_PINPOINT_ERROR_MESSAGE = "Butuh pinpoint lokasi"

        const val SAVE_PINPOINT_SUCCESS_MESSAGE = "Pinpoint berhasil disimpan"

        const val FAIL_APPLY_BBO_ERROR_MESSAGE = "Bebas ongkir gagal diaplikasikan, silahkan coba kembali"

        const val ERROR_CODE_PRICE_CHANGE = "513"
        const val PRICE_CHANGE_ERROR_MESSAGE = "Harga telah berubah"
        const val PRICE_CHANGE_ACTION_MESSAGE = "Cek Belanjaan"

        const val OVO_GATEWAY_CODE = "OVO"

        const val MINIMUM_AMOUNT_ERROR_MESSAGE = "Belanjaanmu kurang dari min. transaksi"
        const val MAXIMUM_AMOUNT_ERROR_MESSAGE = "Belanjaanmu melebihi limit transaksi"

        const val CHANGE_PAYMENT_METHOD_MESSAGE = "Ubah"
        const val ERROR_WHEN_SAVE_ADD_ONS_CTA_TEXT = "Oke"

        const val INSTALLMENT_INVALID_MIN_AMOUNT = "Oops, tidak bisa bayar dengan cicilan karena min. pembeliannya kurang."

        const val TRANSACTION_ID_KEY = "transaction_id"
    }
}
