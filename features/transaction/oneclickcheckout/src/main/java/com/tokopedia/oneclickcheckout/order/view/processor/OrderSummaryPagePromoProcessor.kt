package com.tokopedia.oneclickcheckout.order.view.processor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.DEFAULT_LOCAL_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.STATUS_CODE_200
import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel
import com.tokopedia.oneclickcheckout.order.view.model.OrderCart
import com.tokopedia.oneclickcheckout.order.view.model.OrderPromo
import com.tokopedia.oneclickcheckout.order.view.model.OrderShipment
import com.tokopedia.promousage.data.request.GetPromoListRecommendationParam
import com.tokopedia.promousage.domain.entity.PromoEntryPointInfo
import com.tokopedia.promousage.domain.usecase.PromoUsageGetPromoListRecommendationEntryPointUseCase
import com.tokopedia.promousage.view.mapper.PromoUsageGetPromoListRecommendationMapper
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrder
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrderData
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.Order
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.ProductDetail
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ProductDetailsItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata
import com.tokopedia.purchase_platform.common.revamp.CartCheckoutRevampRollenceManager
import com.tokopedia.remoteconfig.RemoteConfigInstance
import dagger.Lazy
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderSummaryPagePromoProcessor @Inject constructor(
    private val validateUsePromoRevampUseCase: Lazy<ValidateUsePromoRevampUseCase>,
    private val clearCacheAutoApplyStackUseCase: Lazy<ClearCacheAutoApplyStackUseCase>,
    private val getPromoListRecommendationEntryPointUseCase: PromoUsageGetPromoListRecommendationEntryPointUseCase,
    private val getPromoListRecommendationMapper: PromoUsageGetPromoListRecommendationMapper,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper,
    private val orderSummaryAnalytics: OrderSummaryAnalytics,
    private val executorDispatchers: CoroutineDispatchers
) {

    suspend fun validateUsePromo(validateUsePromoRequest: ValidateUsePromoRequest, lastValidateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel?, forceValidateUse: Boolean): Triple<ValidateUsePromoRevampUiModel?, OccGlobalEvent?, Boolean> {
        if (!forceValidateUse && !hasPromo(validateUsePromoRequest)) return Triple(null, null, false)
        OccIdlingResource.increment()
        val resultValidateUse = withContext(executorDispatchers.io) {
            try {
                val result = validateUsePromoRevampUseCase.get().setParam(validateUsePromoRequest).executeOnBackground()
                var newGlobalEvent: OccGlobalEvent? = null
                if (result.status.equals(STATUS_OK, true) && result.errorCode == STATUS_CODE_200) {
                    var isPromoReleased = false
                    if (!lastValidateUsePromoRevampUiModel?.promoUiModel?.codes.isNullOrEmpty() && result.promoUiModel.codes.isNotEmpty() && result.promoUiModel.messageUiModel.state == "red") {
                        isPromoReleased = true
                    } else {
                        result.promoUiModel.voucherOrderUiModels.firstOrNull { it.messageUiModel.state == "red" }?.let {
                            isPromoReleased = true
                        }
                    }
                    if (isPromoReleased) {
                        orderSummaryAnalytics.eventViewPromoDecreasedOrReleased(true)
                    } else if (lastValidateUsePromoRevampUiModel != null && result.promoUiModel.benefitSummaryInfoUiModel.finalBenefitAmount < lastValidateUsePromoRevampUiModel.promoUiModel.benefitSummaryInfoUiModel.finalBenefitAmount) {
                        orderSummaryAnalytics.eventViewPromoDecreasedOrReleased(false)
                    }
                    if (result.promoUiModel.additionalInfoUiModel.errorDetailUiModel.message.isNotEmpty()) {
                        newGlobalEvent = OccGlobalEvent.ToasterInfo(result.promoUiModel.additionalInfoUiModel.errorDetailUiModel.message)
                    }
                    return@withContext Triple(result, newGlobalEvent, false)
                } else {
                    val errorMessage = result.message.firstOrNull() ?: DEFAULT_ERROR_MESSAGE
                    return@withContext Triple(null, OccGlobalEvent.Error(errorMessage = errorMessage), false)
                }
            } catch (t: Throwable) {
                val throwable = t.cause ?: t
                return@withContext Triple(null, OccGlobalEvent.Error(throwable), handlePromoThrowable(throwable, validateUsePromoRequest))
            }
        }
        OccIdlingResource.decrement()
        return resultValidateUse
    }

    suspend fun clearOldLogisticPromo(oldPromoCode: String, orderCart: OrderCart) {
        withContext(executorDispatchers.io) {
            try {
                val params = ClearPromoRequest(
                    ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
                    isOcc = true,
                    ClearPromoOrderData(
                        orders = listOf(
                            ClearPromoOrder(
                                uniqueId = orderCart.cartString,
                                boType = orderCart.shop.boMetadata.boType,
                                codes = arrayListOf(oldPromoCode),
                                isPo = orderCart.products[0].isPreOrder == 1,
                                poDuration = orderCart.products[0].preOrderDuration.toString(),
                                warehouseId = orderCart.shop.warehouseId.toLongOrZero(),
                                shopId = orderCart.shop.shopId.toLongOrZero()
                            )
                        )
                    )
                )
                clearCacheAutoApplyStackUseCase.get().setParams(params).executeOnBackground()
            } catch (t: Throwable) {
                // ignore throwable
            }
        }
    }

    fun clearOldLogisticPromoFromLastRequest(lastValidateUsePromoRequest: ValidateUsePromoRequest?, oldPromoCode: String): ValidateUsePromoRequest? {
        val orders = lastValidateUsePromoRequest?.orders ?: emptyList()
        if (orders.isNotEmpty()) {
            orders[0].codes.remove(oldPromoCode)
        }
        return lastValidateUsePromoRequest
    }

    fun clearAllPromoFromLastRequest(lastValidateUsePromoRequest: ValidateUsePromoRequest?): ValidateUsePromoRequest? {
        val orders = lastValidateUsePromoRequest?.orders ?: emptyList()
        if (orders.isNotEmpty()) {
            orders[0].codes.clear()
        }
        lastValidateUsePromoRequest?.codes?.clear()
        return lastValidateUsePromoRequest
    }

    suspend fun validateUseLogisticPromo(validateUsePromoRequest: ValidateUsePromoRequest, logisticPromoCode: String): Triple<Boolean, ValidateUsePromoRevampUiModel?, OccGlobalEvent> {
        OccIdlingResource.increment()
        val resultValidateUse = withContext(executorDispatchers.io) {
            try {
                val response = validateUsePromoRevampUseCase.get().setParam(validateUsePromoRequest).executeOnBackground()
                if (response.status.equals(STATUS_OK, true) && response.errorCode == STATUS_CODE_200) {
                    val voucherOrderUiModel = response.promoUiModel.voucherOrderUiModels.firstOrNull { it.code == logisticPromoCode }
                    if (voucherOrderUiModel != null && voucherOrderUiModel.messageUiModel.state != "red") {
                        var globalEvent: OccGlobalEvent = OccGlobalEvent.Normal
                        if (response.promoUiModel.additionalInfoUiModel.errorDetailUiModel.message.isNotEmpty()) {
                            globalEvent = OccGlobalEvent.ToasterInfo(response.promoUiModel.additionalInfoUiModel.errorDetailUiModel.message)
                        }
                        return@withContext Triple(true, response, globalEvent)
                    } else if (voucherOrderUiModel != null && voucherOrderUiModel.messageUiModel.text.isNotEmpty()) {
                        return@withContext Triple(false, response, OccGlobalEvent.Error(errorMessage = voucherOrderUiModel.messageUiModel.text))
                    }
                }
                return@withContext Triple(false, response, OccGlobalEvent.Error(errorMessage = OrderSummaryPageViewModel.FAIL_APPLY_BBO_ERROR_MESSAGE))
            } catch (t: Throwable) {
                val throwable = t.cause ?: t
                handlePromoThrowable(throwable, validateUsePromoRequest)
                return@withContext Triple(false, null, OccGlobalEvent.Error(throwable))
            }
        }
        OccIdlingResource.decrement()
        return resultValidateUse
    }

    suspend fun finalValidateUse(validateUsePromoRequest: ValidateUsePromoRequest, orderCart: OrderCart): Triple<ValidateUsePromoRevampUiModel?, Boolean, OccGlobalEvent> {
        OccIdlingResource.increment()
        val resultValidateUse = withContext(executorDispatchers.io) {
            try {
                val response = validateUsePromoRevampUseCase.get().setParam(validateUsePromoRequest).executeOnBackground()
                if (response.status.equals(STATUS_OK, true) && response.errorCode == STATUS_CODE_200) {
                    val (isSuccess, newGlobalEvent) = checkIneligiblePromo(response, orderCart)
                    return@withContext Triple(response, isSuccess, newGlobalEvent)
                }
                return@withContext Triple(null, false, OccGlobalEvent.TriggerRefresh(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE))
            } catch (t: Throwable) {
                val throwable = t.cause ?: t
                handlePromoThrowable(throwable, validateUsePromoRequest)
                return@withContext Triple(null, false, OccGlobalEvent.TriggerRefresh(throwable = throwable))
            }
        }
        OccIdlingResource.decrement()
        return resultValidateUse
    }

    suspend fun cancelIneligiblePromoCheckout(notEligiblePromoHolderdataList: ArrayList<NotEligiblePromoHolderdata>, orderCart: OrderCart): Pair<Boolean, OccGlobalEvent> {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.io) {
            try {
                val params = ClearPromoRequest(
                    ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
                    isOcc = true,
                    ClearPromoOrderData(
                        codes = notEligiblePromoHolderdataList.mapNotNull { if (it.iconType == NotEligiblePromoHolderdata.TYPE_ICON_GLOBAL) it.promoCode else null },
                        orders = listOf(
                            ClearPromoOrder(
                                uniqueId = orderCart.cartString,
                                boType = orderCart.shop.boMetadata.boType,
                                codes = notEligiblePromoHolderdataList.mapNotNull { if (it.iconType == NotEligiblePromoHolderdata.TYPE_ICON_GLOBAL) null else it.promoCode }
                                    .toMutableList(),
                                warehouseId = orderCart.shop.warehouseId.toLongOrZero(),
                                isPo = orderCart.products[0].isPreOrder == 1,
                                poDuration = orderCart.products[0].preOrderDuration.toString(),
                                shopId = orderCart.shop.shopId.toLongOrZero()
                            )
                        )
                    )
                )
                clearCacheAutoApplyStackUseCase.get().setParams(params).executeOnBackground()
                return@withContext true to OccGlobalEvent.Loading
            } catch (t: Throwable) {
                return@withContext false to OccGlobalEvent.Error(t.cause ?: t)
            }
        }
        OccIdlingResource.decrement()
        return result
    }

    fun generatePromoRequest(orderCart: OrderCart, shipping: OrderShipment, lastValidateUsePromoRequest: ValidateUsePromoRequest?, orderPromo: OrderPromo): PromoRequest {
        val promoRequest = PromoRequest()

        val ordersItem = Order()
        ordersItem.shopId = orderCart.shop.shopId.toLongOrZero()
        ordersItem.uniqueId = orderCart.cartString
        ordersItem.boType = orderCart.shop.boMetadata.boType
        val productDetails: ArrayList<ProductDetail> = ArrayList()
        orderCart.products.forEach {
            if (!it.isError) {
                productDetails.add(
                    ProductDetail(
                        productId = it.productId.toLongOrZero(),
                        quantity = it.orderQuantity,
                        cartId = it.cartId,
                        isChecked = true
                    )
                )
            }
        }
        ordersItem.product_details = productDetails
        ordersItem.isChecked = true

        ordersItem.shippingId = shipping.getRealShipperId()
        ordersItem.spId = shipping.getRealShipperProductId()
        if (shipping.isApplyLogisticPromo && shipping.logisticPromoViewModel != null && shipping.logisticPromoShipping != null) {
            ordersItem.freeShippingMetadata = shipping.logisticPromoViewModel.freeShippingMetadata
        }

        if (shipping.insurance.isCheckInsurance && shipping.insurance.insuranceData != null) {
            ordersItem.isInsurancePrice = 1
        } else {
            ordersItem.isInsurancePrice = 0
        }

        ordersItem.codes = generateOrderPromoCodes(lastValidateUsePromoRequest, ordersItem.uniqueId, shipping, orderPromo)

        promoRequest.orders = listOf(ordersItem)
        promoRequest.state = CheckoutConstant.PARAM_CHECKOUT
        promoRequest.cartType = CheckoutConstant.PARAM_OCC_MULTI
        promoRequest.isCartCheckoutRevamp = CartCheckoutRevampRollenceManager(RemoteConfigInstance.getInstance().abTestPlatform).isRevamp()

        if (lastValidateUsePromoRequest != null) {
            promoRequest.codes = ArrayList(lastValidateUsePromoRequest.codes)
        } else {
            promoRequest.codes = ArrayList(orderPromo.lastApply.codes)
        }
        return promoRequest
    }

    private fun generateOrderPromoCodes(lastRequest: ValidateUsePromoRequest?, uniqueId: String, shipping: OrderShipment, orderPromo: OrderPromo, shouldAddLogisticPromo: Boolean = true): MutableList<String> {
        var codes: MutableList<String> = ArrayList()
        val lastRequestOrderCodes = lastRequest?.orders?.firstOrNull()?.codes
        if (lastRequestOrderCodes != null) {
            codes = lastRequestOrderCodes
        } else {
            val voucherOrders = orderPromo.lastApply.voucherOrders
            for (voucherOrder in voucherOrders) {
                if (voucherOrder.uniqueId.equals(uniqueId, true)) {
                    if (!codes.contains(voucherOrder.code)) {
                        codes.add(voucherOrder.code)
                    }
                }
            }
        }

        if (shouldAddLogisticPromo && shipping.isApplyLogisticPromo && shipping.logisticPromoViewModel != null && shipping.logisticPromoShipping != null) {
            if (!codes.contains(shipping.logisticPromoViewModel.promoCode)) {
                codes.add(shipping.logisticPromoViewModel.promoCode)
            }
        } else if (shipping.logisticPromoViewModel?.promoCode?.isNotEmpty() == true) {
            codes.remove(shipping.logisticPromoViewModel.promoCode)
        }
        return codes
    }

    fun generateValidateUsePromoRequest(shouldAddLogisticPromo: Boolean, lastValidateUsePromoRequest: ValidateUsePromoRequest?, orderCart: OrderCart, shipping: OrderShipment, orderPromo: OrderPromo): ValidateUsePromoRequest {
        val validateUsePromoRequest = lastValidateUsePromoRequest ?: ValidateUsePromoRequest()

        val ordersItem = OrdersItem()
        ordersItem.shopId = orderCart.shop.shopId.toLongOrZero()
        ordersItem.uniqueId = orderCart.cartString
        ordersItem.boType = orderCart.shop.boMetadata.boType
        ordersItem.warehouseId = orderCart.shop.warehouseId.toLongOrZero()
        ordersItem.isPo = orderCart.products[0].isPreOrder == 1
        ordersItem.poDuration = orderCart.products[0].preOrderDuration

        val productDetails: ArrayList<ProductDetailsItem> = ArrayList()
        orderCart.products.forEach {
            if (!it.isError) {
                productDetails.add(ProductDetailsItem(it.orderQuantity, it.productId.toLongOrZero()))
            }
        }
        ordersItem.productDetails = productDetails

        ordersItem.shippingId = shipping.getRealShipperId()
        ordersItem.spId = shipping.getRealShipperProductId()
        ordersItem.etaText = shipping.shippingEta ?: ""
        ordersItem.shippingPrice = shipping.getRealOriginalPrice().toDouble()
        if (shouldAddLogisticPromo && shipping.isApplyLogisticPromo && shipping.logisticPromoViewModel != null && shipping.logisticPromoShipping != null) {
            ordersItem.freeShippingMetadata = shipping.logisticPromoViewModel.freeShippingMetadata
            ordersItem.benefitClass = shipping.logisticPromoViewModel.benefitClass
            ordersItem.shippingSubsidy = shipping.logisticPromoViewModel.shippingSubsidy
            ordersItem.etaText = shipping.logisticPromoViewModel.etaData.textEta
            ordersItem.boCampaignId = shipping.logisticPromoViewModel.boCampaignId
        }

        ordersItem.codes = generateOrderPromoCodes(lastValidateUsePromoRequest, ordersItem.uniqueId, shipping, orderPromo, shouldAddLogisticPromo)

        validateUsePromoRequest.orders = listOf(ordersItem)
        validateUsePromoRequest.state = CheckoutConstant.PARAM_CHECKOUT
        validateUsePromoRequest.cartType = CheckoutConstant.PARAM_OCC_MULTI

        if (lastValidateUsePromoRequest != null) {
            validateUsePromoRequest.codes = lastValidateUsePromoRequest.codes
        } else {
            val globalCodes = orderPromo.lastApply.codes
            validateUsePromoRequest.codes = globalCodes.toMutableList()
        }
        validateUsePromoRequest.skipApply = 0
        validateUsePromoRequest.isSuggested = 0
        validateUsePromoRequest.isCartCheckoutRevamp = CartCheckoutRevampRollenceManager(RemoteConfigInstance.getInstance().abTestPlatform).isRevamp()

        return validateUsePromoRequest
    }

    fun generateValidateUsePromoRequestWithBbo(logisticPromoUiModel: LogisticPromoUiModel, oldCode: String?, lastValidateUsePromoRequest: ValidateUsePromoRequest?, orderCart: OrderCart, _orderShipment: OrderShipment, orderPromo: OrderPromo): ValidateUsePromoRequest {
        return generateValidateUsePromoRequest(false, lastValidateUsePromoRequest, orderCart, _orderShipment, orderPromo).apply {
            orders[0].apply {
                shippingId = logisticPromoUiModel.shipperId
                spId = logisticPromoUiModel.shipperProductId
                if (oldCode != null) {
                    codes.remove(oldCode)
                }
                codes.add(logisticPromoUiModel.promoCode)
                freeShippingMetadata = logisticPromoUiModel.freeShippingMetadata
                benefitClass = logisticPromoUiModel.benefitClass
                shippingSubsidy = logisticPromoUiModel.shippingSubsidy
                shippingPrice = logisticPromoUiModel.shippingRate.toDouble()
                etaText = logisticPromoUiModel.etaData.textEta
                boCampaignId = logisticPromoUiModel.boCampaignId
            }
        }
    }

    fun generateBboPromoCodes(shipping: OrderShipment): ArrayList<String> {
        if (shipping.isApplyLogisticPromo && shipping.logisticPromoViewModel != null && shipping.logisticPromoShipping != null) {
            return arrayListOf(shipping.logisticPromoViewModel.promoCode)
        }
        return ArrayList()
    }

    fun hasPromo(validateUsePromoRequest: ValidateUsePromoRequest): Boolean {
        return validateUsePromoRequest.codes.isNotEmpty() || validateUsePromoRequest.orders.first().codes.isNotEmpty()
    }

    private fun shouldShowIneligibleBottomSheet(): Boolean {
        return false
    }

    private suspend fun checkIneligiblePromo(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel, orderCart: OrderCart): Pair<Boolean, OccGlobalEvent> {
        var notEligiblePromoHolderdataList = ArrayList<NotEligiblePromoHolderdata>()
        notEligiblePromoHolderdataList = addIneligibleGlobalPromo(validateUsePromoRevampUiModel, notEligiblePromoHolderdataList)
        notEligiblePromoHolderdataList = addIneligibleVoucherPromo(validateUsePromoRevampUiModel, notEligiblePromoHolderdataList, orderCart)

        if (notEligiblePromoHolderdataList.size > 0) {
            return if (shouldShowIneligibleBottomSheet()) {
                false to OccGlobalEvent.PromoClashing(notEligiblePromoHolderdataList)
            } else {
                return cancelIneligiblePromoCheckout(notEligiblePromoHolderdataList, orderCart)
            }
        }
        return true to OccGlobalEvent.Loading
    }

    private fun addIneligibleGlobalPromo(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel, notEligiblePromoHolderdataList: ArrayList<NotEligiblePromoHolderdata>): ArrayList<NotEligiblePromoHolderdata> {
        if (validateUsePromoRevampUiModel.promoUiModel.messageUiModel.state == "red") {
            val notEligiblePromoHolderdata = NotEligiblePromoHolderdata()
            notEligiblePromoHolderdata.promoTitle = validateUsePromoRevampUiModel.promoUiModel.titleDescription
            if (validateUsePromoRevampUiModel.promoUiModel.codes.isNotEmpty()) {
                notEligiblePromoHolderdata.promoCode = validateUsePromoRevampUiModel.promoUiModel.codes[0]
            }
            notEligiblePromoHolderdata.shopName = "Kode promo"
            notEligiblePromoHolderdata.iconType = NotEligiblePromoHolderdata.TYPE_ICON_GLOBAL
            notEligiblePromoHolderdata.showShopSection = true
            notEligiblePromoHolderdata.errorMessage = validateUsePromoRevampUiModel.promoUiModel.messageUiModel.text
            notEligiblePromoHolderdataList.add(notEligiblePromoHolderdata)
        }
        return notEligiblePromoHolderdataList
    }

    private fun addIneligibleVoucherPromo(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel, notEligiblePromoHolderdataList: ArrayList<NotEligiblePromoHolderdata>, orderCart: OrderCart): ArrayList<NotEligiblePromoHolderdata> {
        val voucherOrdersItemUiModels = validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels
        for (i in voucherOrdersItemUiModels.indices) {
            val voucherOrdersItemUiModel = voucherOrdersItemUiModels[i]
            if (voucherOrdersItemUiModel.messageUiModel.state == "red") {
                val notEligiblePromoHolderdata = NotEligiblePromoHolderdata()
                notEligiblePromoHolderdata.promoTitle = voucherOrdersItemUiModel.titleDescription
                notEligiblePromoHolderdata.promoCode = voucherOrdersItemUiModel.code
                if (orderCart.cartString == voucherOrdersItemUiModel.uniqueId) {
                    notEligiblePromoHolderdata.shopName = orderCart.shop.shopName
                    notEligiblePromoHolderdata.shopBadge = orderCart.shop.shopBadge
                }
                if (i == 0) {
                    notEligiblePromoHolderdata.showShopSection = true
                } else {
                    notEligiblePromoHolderdata.showShopSection = voucherOrdersItemUiModels[i - 1].uniqueId != voucherOrdersItemUiModel.uniqueId
                }

                notEligiblePromoHolderdata.errorMessage = voucherOrdersItemUiModel.messageUiModel.text
                notEligiblePromoHolderdataList.add(notEligiblePromoHolderdata)
            }
        }
        return notEligiblePromoHolderdataList
    }

    private suspend fun handlePromoThrowable(throwable: Throwable, validateUsePromoRequest: ValidateUsePromoRequest): Boolean {
        if (throwable is AkamaiErrorException) {
            try {
                val order = validateUsePromoRequest.orders.first()
                val params = ClearPromoRequest(
                    ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
                    isOcc = true,
                    ClearPromoOrderData(
                        codes = validateUsePromoRequest.codes,
                        orders = listOf(
                            ClearPromoOrder(
                                uniqueId = order.uniqueId,
                                boType = order.boType,
                                codes = order.codes,
                                warehouseId = order.warehouseId,
                                isPo = order.isPo,
                                poDuration = order.poDuration.toString(),
                                shopId = order.shopId
                            )
                        )
                    )
                )
                clearCacheAutoApplyStackUseCase.get().setParams(params).executeOnBackground()
            } catch (t: Throwable) {
                // ignore throwable
            }
            return true
        }
        return false
    }

    fun getValidPromoCodes(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel?): List<String> {
        val promoCodes = mutableListOf<String>()
        if (validateUsePromoRevampUiModel != null) {
            if (validateUsePromoRevampUiModel.promoUiModel.messageUiModel.state != "red") {
                promoCodes.addAll(validateUsePromoRevampUiModel.promoUiModel.codes)
            }
            validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels.forEach {
                if (it.messageUiModel.state != "red") {
                    promoCodes.add(it.code)
                }
            }
        }
        return promoCodes
    }

    suspend fun getEntryPointInfo(
        promoRequest: PromoRequest
    ): PromoEntryPointInfo {
        return try {
            val param = GetPromoListRecommendationParam.create(
                promoRequest = promoRequest,
                chosenAddress = chosenAddressRequestHelper.getChosenAddress(),
                isPromoRevamp = true
            )
            val response = getPromoListRecommendationEntryPointUseCase(param)
            getPromoListRecommendationMapper
                .mapPromoListRecommendationEntryPointResponseToEntryPointInfo(response)
        } catch (_: Throwable) {
            PromoEntryPointInfo(isSuccess = false)
        }
    }
}
