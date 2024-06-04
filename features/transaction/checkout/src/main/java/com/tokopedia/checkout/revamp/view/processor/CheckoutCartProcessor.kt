package com.tokopedia.checkout.revamp.view.processor

import com.google.gson.JsonParser
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cartcommon.data.request.updatecart.BundleInfo
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartPaymentRequest
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.checkout.data.model.request.changeaddress.DataChangeAddressRequest
import com.tokopedia.checkout.data.model.request.saf.ShipmentAddressFormRequest
import com.tokopedia.checkout.data.model.request.saveshipmentstate.SaveShipmentStateRequest
import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateProductData
import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateProductPreorder
import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateRequestData
import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateShippingInfoData
import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateShopProductData
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressGqlUseCase
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressRequest
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormV4UseCase
import com.tokopedia.checkout.domain.usecase.ReleaseBookingUseCase
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateGqlUseCase
import com.tokopedia.checkout.revamp.view.firstOrNullInstanceOf
import com.tokopedia.checkout.revamp.view.payment
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutItem
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageState
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.revamp.view.uimodel.OriginalCheckoutPaymentData
import com.tokopedia.checkout.view.CheckoutLogger
import com.tokopedia.checkout.view.converter.ShipmentDataRequestConverter
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.toZeroStringIfNullOrBlank
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.purchase_platform.common.feature.promo.view.model.PromoExternalAutoApply
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyVoucherOrdersItemUiModel
import com.tokopedia.usecase.RequestParams
import dagger.Lazy
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class CheckoutCartProcessor @Inject constructor(
    private val getShipmentAddressFormV4UseCase: GetShipmentAddressFormV4UseCase,
    private val saveShipmentStateGqlUseCase: SaveShipmentStateGqlUseCase,
    private val changeShippingAddressGqlUseCase: Lazy<ChangeShippingAddressGqlUseCase>,
    private val releaseBookingUseCase: Lazy<ReleaseBookingUseCase>,
    private val updateCartUseCase: Lazy<UpdateCartUseCase>,
    private val helper: CheckoutDataHelper,
    private val dispatchers: CoroutineDispatchers
) {

    suspend fun hitSAF(
        isOneClickShipment: Boolean,
        isTradeIn: Boolean,
        isTradeInDropOff: Boolean,
        skipUpdateOnboardingState: Boolean,
        cornerId: String?,
        deviceId: String?,
        checkoutLeasingId: String?,
        isPlusSelected: Boolean,
        isReloadData: Boolean,
        isReloadAfterPriceChangeHigher: Boolean,
        shipmentAction: String,
        listPromoExternalAutoApplyCode: List<PromoExternalAutoApply>
    ): CheckoutPageState {
        return withContext(dispatchers.io) {
            try {
                val cartShipmentAddressFormData = getShipmentAddressFormV4UseCase(
                    ShipmentAddressFormRequest(
                        isOneClickShipment,
                        isTradeIn,
                        skipUpdateOnboardingState,
                        cornerId,
                        deviceId,
                        checkoutLeasingId,
                        isPlusSelected,
                        true,
                        shipmentAction
                    )
                )
                validateShipmentAddressFormData(
                    cartShipmentAddressFormData,
                    isReloadData,
                    isReloadAfterPriceChangeHigher,
                    isOneClickShipment,
                    isTradeIn,
                    isTradeInDropOff,
                    listPromoExternalAutoApplyCode
                )
            } catch (t: Throwable) {
                Timber.d(t)
                CheckoutPageState.Error(t)
            }
        }
    }

    private fun validateShipmentAddressFormData(
        cartShipmentAddressFormData: CartShipmentAddressFormData,
        isReloadData: Boolean,
        isReloadAfterPriceChangeHigher: Boolean,
        isOneClickShipment: Boolean,
        isTradeIn: Boolean,
        isTradeInDropOff: Boolean,
        listPromoExternalAutoApplyCode: List<PromoExternalAutoApply>
    ): CheckoutPageState {
        if (cartShipmentAddressFormData.isError) {
            if (cartShipmentAddressFormData.isOpenPrerequisiteSite) {
                return CheckoutPageState.CacheExpired(cartShipmentAddressFormData.errorMessage)
            } else {
                CheckoutLogger.logOnErrorLoadCheckoutPage(
                    MessageErrorException(
                        cartShipmentAddressFormData.errorMessage
                    ),
                    isOneClickShipment,
                    isTradeIn,
                    isTradeInDropOff
                )
                return CheckoutPageState.Error(
                    MessageErrorException(
                        cartShipmentAddressFormData.errorMessage
                    )
                )
            }
        } else {
            val groupAddressList = cartShipmentAddressFormData.groupAddress
            val userAddress = groupAddressList.firstOrNull()?.userAddress
            return validateRenderCheckoutPage(
                cartShipmentAddressFormData,
                userAddress,
                isOneClickShipment,
                isTradeIn,
                isTradeInDropOff,
                listPromoExternalAutoApplyCode
            )
        }
    }

    private fun validateRenderCheckoutPage(
        cartShipmentAddressFormData: CartShipmentAddressFormData,
        userAddress: UserAddress?,
        isOneClickShipment: Boolean,
        isTradeIn: Boolean,
        isTradeInDropOff: Boolean,
        listPromoExternalAutoApplyCode: List<PromoExternalAutoApply>
    ): CheckoutPageState {
        when (cartShipmentAddressFormData.errorCode) {
            CartShipmentAddressFormData.ERROR_CODE_TO_OPEN_ADD_NEW_ADDRESS -> {
                return CheckoutPageState.NoAddress(
                    cartShipmentAddressFormData
                )
            }

            CartShipmentAddressFormData.ERROR_CODE_TO_OPEN_ADDRESS_LIST -> {
                return CheckoutPageState.NoMatchedAddress(
                    userAddress?.state ?: 0
                )
            }

            CartShipmentAddressFormData.NO_ERROR -> {
                return if (userAddress == null) {
                    CheckoutPageState.EmptyData
                } else {
                    CheckoutPageState.Success(injectPromoExternalAutoApply(cartShipmentAddressFormData, listPromoExternalAutoApplyCode))
                }
            }

            else -> {
                val exception = MessageErrorException(
                    "unhandled error code ${cartShipmentAddressFormData.errorCode}"
                )
                CheckoutLogger.logOnErrorLoadCheckoutPage(
                    exception,
                    isOneClickShipment,
                    isTradeIn,
                    isTradeInDropOff
                )
                return CheckoutPageState.Error(
                    exception
                )
            }
        }
    }

    private fun injectPromoExternalAutoApply(cartShipmentAddressFormData: CartShipmentAddressFormData, listPromoExternalAutoApplyCode: List<PromoExternalAutoApply>): CartShipmentAddressFormData {
        if (listPromoExternalAutoApplyCode.isEmpty()) {
            return cartShipmentAddressFormData
        }
        val groupShop = cartShipmentAddressFormData.groupAddress.firstOrNull()?.groupShop?.firstOrNull()
        val (listGlobalVoucher, listMerchantVoucher) = listPromoExternalAutoApplyCode.partition { it.type != TYPE_PROMO_MV }
        val newLastApply = cartShipmentAddressFormData.lastApplyData.copy(
            codes = listGlobalVoucher.map { it.code },
            voucherOrders = listMerchantVoucher.map {
                LastApplyVoucherOrdersItemUiModel(
                    code = it.code,
                    uniqueId = groupShop?.groupShopData?.firstOrNull()?.cartStringOrder ?: "",
                    cartStringGroup = groupShop?.cartString ?: "",
                    type = TYPE_PROMO_MV
                )
            }
        )
        return cartShipmentAddressFormData.copy(lastApplyData = newLastApply)
    }

    suspend fun changeShippingAddress(
        items: List<CheckoutItem>,
        newRecipientAddressModel: RecipientAddressModel?,
        chosenAddressModel: ChosenAddressModel?,
        isOneClickShipment: Boolean,
        isTradeInDropOff: Boolean
    ): ChangeAddressResult {
        return withContext(dispatchers.io) {
            val dataChangeAddressRequests: MutableList<DataChangeAddressRequest> = ArrayList()
            for (item in items) {
                if (item is CheckoutOrderModel) {
                    for (product in helper.getOrderProducts(items, item.cartStringGroup).filterIsInstance(CheckoutProductModel::class.java)) {
                        val dataChangeAddressRequest = DataChangeAddressRequest()
                        dataChangeAddressRequest.quantity = product.quantity
                        dataChangeAddressRequest.productId = product.productId
                        dataChangeAddressRequest.notes = product.noteToSeller
                        dataChangeAddressRequest.cartIdStr = product.cartId.toString()
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
                            dataChangeAddressRequest.addressId =
                                chosenAddressModel.addressId.toString()
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
            try {
                val setShippingAddressData = changeShippingAddressGqlUseCase.get().invoke(
                    ChangeShippingAddressRequest(
                        dataChangeAddressRequests,
                        isOneClickShipment
                    )
                )
                if (setShippingAddressData.isSuccess) {
                    return@withContext ChangeAddressResult(
                        isSuccess = true,
                        toasterMessage = (
                            setShippingAddressData.messages.firstOrNull()
                                ?: ""
                            ).ifEmpty { "Alamat berhasil diganti" }
                    )
                } else {
                    return@withContext ChangeAddressResult(
                        isSuccess = false,
                        toasterMessage = if (setShippingAddressData.messages.isEmpty()) {
                            "Alamat gagal diganti"
                        } else {
                            setShippingAddressData.messages.joinToString(
                                " "
                            )
                        }
                    )
                }
            } catch (t: Throwable) {
                Timber.d(t)
                return@withContext ChangeAddressResult(isSuccess = false, throwable = t)
            }
        }
    }

    suspend fun processSaveShipmentState(
        shipmentCartItemModel: CheckoutOrderModel,
        recipientAddressModel: RecipientAddressModel,
        listData: List<CheckoutItem>
    ) {
        withContext(dispatchers.io) {
            try {
                val params =
                    generateSaveShipmentStateRequestSingleAddress(
                        listOf(shipmentCartItemModel),
                        recipientAddressModel,
                        listData
                    )
                if (params.requestDataList.first().shopProductDataList.isNotEmpty()) {
                    saveShipmentStateGqlUseCase(params)
                }
            } catch (e: Throwable) {
                Timber.d(e)
            }
        }
    }

    suspend fun processSaveShipmentState(
        listData: List<CheckoutItem>,
        recipientAddressModel: RecipientAddressModel
    ) {
        withContext(dispatchers.io) {
            try {
                val params = generateSaveShipmentStateRequestSingleAddress(
                    listData.filterIsInstance(CheckoutOrderModel::class.java),
                    recipientAddressModel,
                    listData
                )
                if (params.requestDataList.first().shopProductDataList.isNotEmpty()) {
                    saveShipmentStateGqlUseCase(params)
                }
            } catch (e: Throwable) {
                Timber.d(e)
            }
        }
    }

    private fun generateSaveShipmentStateRequestSingleAddress(
        shipmentCartItemModels: List<CheckoutOrderModel>,
        recipientAddressModel: RecipientAddressModel,
        listData: List<CheckoutItem>
    ): SaveShipmentStateRequest {
        val shipmentStateShopProductDataList: MutableList<ShipmentStateShopProductData> =
            ArrayList()
        val shipmentStateRequestDataList: MutableList<ShipmentStateRequestData> =
            ArrayList()
        for (shipmentCartItemModel in shipmentCartItemModels) {
            setSaveShipmentStateData(shipmentCartItemModel, listData, shipmentStateShopProductDataList)
        }
        val shipmentStateRequestData = ShipmentStateRequestData()
        shipmentStateRequestData.addressId = recipientAddressModel.id
        shipmentStateRequestData.shopProductDataList = shipmentStateShopProductDataList
        shipmentStateRequestDataList.add(shipmentStateRequestData)
        return SaveShipmentStateRequest(shipmentStateRequestDataList)
    }

    private fun setSaveShipmentStateData(
        shipmentCartItemModel: CheckoutOrderModel,
        listData: List<CheckoutItem>,
        shipmentStateShopProductDataList: MutableList<ShipmentStateShopProductData>
    ) {
        var courierData: CourierItemData? = shipmentCartItemModel.shipment.courierItemData
        // todo handle trade in
//        if (shipmentCartItemModel.selectedShipmentDetailData != null) {
//            courierData = if (view!!.isTradeInByDropOff) {
//                shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff
//            } else {
//                shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier
//            }
//        }
//        courierData = shipmentCartItemModel.shipment.courierItemData
        if (courierData != null) {
            val shipmentStateProductDataList: MutableList<ShipmentStateProductData> =
                ArrayList()
            val products = helper.getOrderProducts(listData, shipmentCartItemModel.cartStringGroup).filterIsInstance(CheckoutProductModel::class.java)
            for (cartItemModel in products) {
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
            val ratesFeature = ShipmentDataRequestConverter.generateRatesFeature(courierData)
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
                if (shipmentCartItemModel.shipment.insurance.isCheckInsurance) {
                    1
                } else {
                    0
                }
            shipmentStateShopProductData.isPreorder =
                if (shipmentCartItemModel.isProductIsPreorder) 1 else 0
            shipmentStateShopProductData.warehouseId = shipmentCartItemModel.fulfillmentId
            shipmentStateShopProductData.shippingInfoData = shipmentStateShippingInfoData
            shipmentStateShopProductData.productDataList = shipmentStateProductDataList
            shipmentStateShopProductData.validationMetadata =
                shipmentCartItemModel.validationMetadata
            shipmentStateShopProductDataList.add(shipmentStateShopProductData)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun releaseBooking(listData: List<CheckoutItem>) {
        // As deals product is using OCS, the shipment should only contain 1 product
        val productId =
            listData.firstOrNullInstanceOf(CheckoutProductModel::class.java)?.productId ?: 0
        if (productId != 0L) {
            GlobalScope.launch(dispatchers.io) {
                try {
                    releaseBookingUseCase.get().invoke(productId)
                } catch (t: Throwable) {
                    Timber.d(t)
                }
            }
        }
    }

    suspend fun updateCart(params: List<UpdateCartRequest>, source: String, paymentRequest: UpdateCartPaymentRequest?): UpdateCartResult {
        return withContext(dispatchers.io) {
            try {
                updateCartUseCase.get().setParams(params, source, paymentRequest)
                val response = updateCartUseCase.get().executeOnBackground()
                if (response.data.status) {
                    return@withContext UpdateCartResult(true)
                }
                return@withContext UpdateCartResult(
                    false,
                    toasterMessage = response.data.error,
                    toasterActionMessage = if (response.data.toasterAction.showCta) response.data.toasterAction.text else ""
                )
            } catch (e: Exception) {
                Timber.d(e)
                return@withContext UpdateCartResult(
                    false,
                    throwable = e
                )
            }
        }
    }

    fun generateUpdateCartRequest(checkoutItems: List<CheckoutItem>): List<UpdateCartRequest> {
        val updateCartRequestList = ArrayList<UpdateCartRequest>()
        for (checkoutItem in checkoutItems) {
            if (checkoutItem is CheckoutProductModel) {
                val updateCartRequest = UpdateCartRequest().apply {
                    productId = checkoutItem.productId.toString()
                    cartId = checkoutItem.cartId.toString()
                    notes = checkoutItem.noteToSeller
                    quantity = checkoutItem.quantity
                    bundleInfo = BundleInfo().apply {
                        bundleId = checkoutItem.bundleId.toZeroStringIfNullOrBlank()
                        bundleGroupId = checkoutItem.bundleGroupId.toZeroStringIfNullOrBlank()
                        bundleQty = checkoutItem.bundleQuantity
                    }
                }
                updateCartRequestList.add(updateCartRequest)
            }
        }
        return updateCartRequestList
    }

    fun generateUpdateCartPaymentRequest(payment: CheckoutPaymentModel?): UpdateCartPaymentRequest? {
        val paymentData = payment?.data?.paymentWidgetData?.firstOrNull()
        if (paymentData != null) {
            val selectedTenure = paymentData.installmentPaymentData.selectedTenure
            var currPaymentGatewayCode = paymentData.gatewayCode
            var currPaymentMetadata = paymentData.metadata
            val selectedInstallment = payment.tenorList?.firstOrNull { it.tenure == selectedTenure }
            if (selectedInstallment != null) {
                currPaymentGatewayCode = selectedInstallment.gatewayCode
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
                }
            }
            return UpdateCartPaymentRequest(
                currPaymentGatewayCode,
                selectedTenure,
                payment.installmentData?.installmentOptions?.firstOrNull { it.installmentTerm == selectedTenure }?.optionId ?: "",
                currPaymentMetadata
            )
        }
        return UpdateCartPaymentRequest()
    }

    fun generateModifiedCheckoutItems(checkoutItems: List<CheckoutItem>, prevCheckoutItems: List<CheckoutItem>): List<CheckoutItem> {
        val modifiedCheckoutItems = ArrayList<CheckoutItem>()
        val prevCheckoutProducts = prevCheckoutItems.filterIsInstance(CheckoutProductModel::class.java)
        for (checkoutItem in checkoutItems) {
            when (checkoutItem) {
                is CheckoutProductModel -> {
                    val currPairingCheckoutItem = prevCheckoutProducts.firstOrNull { it.cartId == checkoutItem.cartId }
                    val newCheckoutItem = checkoutItem.copy(
                        shouldShowMaxQtyError = currPairingCheckoutItem?.shouldShowMaxQtyError ?: false,
                        shouldShowMinQtyError = currPairingCheckoutItem?.shouldShowMinQtyError ?: false
                    )
                    modifiedCheckoutItems.add(newCheckoutItem)
                }

                is CheckoutPaymentModel -> {
                    val prevCheckoutPayment = prevCheckoutItems.payment()?.data?.paymentWidgetData?.firstOrNull()
                    val selectedTenure = prevCheckoutPayment?.installmentPaymentData?.selectedTenure
                    val newCheckoutPaymentItem = checkoutItem.copy(
                        originalData = OriginalCheckoutPaymentData(
                            gatewayCode = prevCheckoutPayment?.gatewayCode ?: "",
                            tenureType = selectedTenure ?: 0,
                            optionId = prevCheckoutItems.payment()?.installmentData?.installmentOptions?.firstOrNull { it.installmentTerm == selectedTenure }?.optionId ?: "",
                            metadata = prevCheckoutPayment?.metadata ?: ""
                        )
                    )
                    modifiedCheckoutItems.add(newCheckoutPaymentItem)
                }

                else -> {
                    modifiedCheckoutItems.add(checkoutItem)
                }
            }
        }
        return modifiedCheckoutItems
    }

    companion object {
        const val UPDATE_CART_SOURCE_CHECKOUT = "checkout"
        const val UPDATE_CART_SOURCE_NOTES = "update_notes"
        const val UPDATE_CART_SOURCE_QUANTITY = "update_quantity"
        const val UPDATE_CART_SOURCE_PAYMENT = "update_payment"
        const val UPDATE_CART_SOURCE_CHECKOUT_OPEN_PROMO = "saf_coupon_list"
    }
}

data class ChangeAddressResult(
    val isSuccess: Boolean,
    val toasterMessage: String = "",
    val throwable: Throwable? = null
)

data class UpdateCartResult(
    val isSuccess: Boolean,
    val toasterMessage: String = "",
    val toasterActionMessage: String = "",
    val throwable: Throwable? = null
)

private const val TYPE_PROMO_MV = "mv"
