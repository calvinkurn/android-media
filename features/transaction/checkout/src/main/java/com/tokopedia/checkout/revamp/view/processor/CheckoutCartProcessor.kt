package com.tokopedia.checkout.revamp.view.processor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
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
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutItem
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageState
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.view.CheckoutLogger
import com.tokopedia.checkout.view.converter.ShipmentDataRequestConverter
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.network.exception.MessageErrorException
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
        isReloadAfterPriceChangeHigher: Boolean
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
                        true
                    )
                )
                validateShipmentAddressFormData(
                    cartShipmentAddressFormData,
                    isReloadData,
                    isReloadAfterPriceChangeHigher,
                    isOneClickShipment,
                    isTradeIn,
                    isTradeInDropOff
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
        isTradeInDropOff: Boolean
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
                isTradeInDropOff
            )
        }
    }

    private fun validateRenderCheckoutPage(
        cartShipmentAddressFormData: CartShipmentAddressFormData,
        userAddress: UserAddress?,
        isOneClickShipment: Boolean,
        isTradeIn: Boolean,
        isTradeInDropOff: Boolean
    ): CheckoutPageState {
        when (cartShipmentAddressFormData.errorCode) {
            CartShipmentAddressFormData.ERROR_CODE_TO_OPEN_ADD_NEW_ADDRESS -> {
                return CheckoutPageState.CheckNoAddress(cartShipmentAddressFormData)
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
                    CheckoutPageState.Success(cartShipmentAddressFormData)
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
                    for (product in item.products) {
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
                            ).ifEmpty { "Berhasil mengubah alamat" }
                    )
                } else {
                    return@withContext ChangeAddressResult(
                        isSuccess = false,
                        toasterMessage = if (setShippingAddressData.messages.isEmpty()) {
                            "Gagal mengubah alamat"
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
        recipientAddressModel: RecipientAddressModel
    ) {
        withContext(dispatchers.io) {
            try {
                val params =
                    generateSaveShipmentStateRequestSingleAddress(
                        listOf(shipmentCartItemModel),
                        recipientAddressModel
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
                    recipientAddressModel
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
        recipientAddressModel: RecipientAddressModel
    ): SaveShipmentStateRequest {
        val shipmentStateShopProductDataList: MutableList<ShipmentStateShopProductData> =
            ArrayList()
        val shipmentStateRequestDataList: MutableList<ShipmentStateRequestData> =
            ArrayList()
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
        shipmentCartItemModel: CheckoutOrderModel,
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
            for (cartItemModel in shipmentCartItemModel.products) {
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
            GlobalScope.launch {
                try {
                    releaseBookingUseCase.get().invoke(productId)
                } catch (t: Throwable) {
                    Timber.d(t)
                }
            }
        }
    }
}

data class ChangeAddressResult(
    val isSuccess: Boolean,
    val toasterMessage: String = "",
    val throwable: Throwable? = null
)
