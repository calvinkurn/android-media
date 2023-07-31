package com.tokopedia.checkout.revamp.view.processor

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderShipment
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageState
import com.tokopedia.checkout.view.converter.RatesDataConverter
import com.tokopedia.logisticCommon.data.constant.AddressConstant
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.response.KeroAddrIsEligibleForAddressFeatureData
import com.tokopedia.logisticCommon.domain.param.EditAddressParam
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticcart.scheduledelivery.domain.usecase.GetRatesWithScheduleDeliveryCoroutineUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.CodModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShipmentCartData
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingParam
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiCoroutineUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesCoroutineUseCase
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.network.utils.TKPDMapParam
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class CheckoutLogisticProcessor @Inject constructor(
    private val eligibleForAddressUseCase: Lazy<EligibleForAddressUseCase>,
    private val editAddressUseCase: EditAddressUseCase,
    private val ratesUseCase: GetRatesCoroutineUseCase,
    private val ratesApiUseCase: GetRatesApiCoroutineUseCase,
    private val ratesWithScheduleUseCase: GetRatesWithScheduleDeliveryCoroutineUseCase,
    private val ratesResponseStateConverter: RatesResponseStateConverter,
    private val shippingCourierConverter: ShippingCourierConverter,
    private val userSessionInterface: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers
) {

    var isBoUnstackEnabled = false

    fun checkIsUserEligibleForRevampAna(
        cartShipmentAddressFormData: CartShipmentAddressFormData,
        callback: (CheckoutPageState) -> Unit
    ) {
        eligibleForAddressUseCase.get()
            .eligibleForAddressFeature({ response: KeroAddrIsEligibleForAddressFeatureData ->
                callback(
                    CheckoutPageState.NoAddress(
                        cartShipmentAddressFormData,
                        response.eligibleForRevampAna.eligible
                    )
                )
            }, { throwable: Throwable ->
//                    var errorMessage = throwable.message
//            if (errorMessage == null) {
//                errorMessage =
//                    view?.getStringResource(com.tokopedia.abstraction.R.string.default_request_error_unknown_short)
//            }
//            view?.showToastError(errorMessage)
                callback(CheckoutPageState.Error(throwable))
            }, AddressConstant.ANA_REVAMP_FEATURE_ID)
    }

    suspend fun editAddressPinpoint(
        latitude: String,
        longitude: String,
        recipientAddressModel: RecipientAddressModel
    ): EditAddressResult {
        return withContext(dispatchers.io) {
            return@withContext try {
                val requestParams =
                    generateEditAddressRequestParams(latitude, longitude, recipientAddressModel)
                val stringResponse =
                    editAddressUseCase.createObservable(requestParams).toBlocking().single()
//            if (view != null) {
//                view!!.setHasRunningApiCall(false)
//                view!!.hideLoading()
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
                    EditAddressResult(isSuccess = true)
//                    view?.renderEditAddressSuccess(latitude, longitude)
                } else {
//                    if (messageError.isEmpty()) {
//                        messageError =
//                            view?.getStringResource(com.tokopedia.abstraction.R.string.default_request_error_unknown)
//                                ?: ""
//                    }
//                    view?.navigateToSetPinpoint(messageError, locationPass)
                    EditAddressResult(isSuccess = false, errorMessage = messageError)
                }
//            }
            } catch (t: Throwable) {
                val exception = getActualThrowableForRx(t)
                Timber.d(exception)
                EditAddressResult(isSuccess = false, throwable = t)
//            if (view != null) {
//                view!!.setHasRunningApiCall(false)
//                view!!.hideLoading()
//                view!!.showToastError(
//                    ErrorHandler.getErrorMessage(
//                        view!!.activity,
//                        exception
//                    )
//                )
//            }
            }
        }
    }

    private fun generateEditAddressRequestParams(
        addressLatitude: String,
        addressLongitude: String,
        recipientAddressModel: RecipientAddressModel
    ): RequestParams {
        val params: MutableMap<String, String> = AuthHelper.generateParamsNetwork(
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

    private fun getActualThrowableForRx(t: Throwable) = t.cause?.cause ?: t.cause ?: t

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

    fun generateShippingBottomsheetParam(
        order: CheckoutOrderModel,
        recipientAddressModel: RecipientAddressModel,
        isTradeIn: Boolean
    ): ShipmentDetailData {
        val orderProducts = order.products
        var orderValue = 0L
        var totalWeight = 0.0
        var totalWeightActual = 0.0
        var productFInsurance = 0
        var preOrder = false
        var productPreOrderDuration = 0
        val productList: ArrayList<Product> = ArrayList()
        val categoryList: HashSet<String> = hashSetOf()
        orderProducts.forEach {
            if (!it.isError) {
                orderValue += (it.quantity * it.price).toLong()
                totalWeight += it.quantity * it.weight
                totalWeightActual += if (it.weightActual > 0) {
                    it.quantity * it.weightActual
                } else {
                    it.quantity * it.weight
                }
                if (it.fInsurance) {
                    productFInsurance = 1
                }
                preOrder = it.isPreOrder
                productPreOrderDuration = it.preOrderDurationDay
                categoryList.add(it.productCatId.toString())
                productList.add(Product(it.productId, it.isFreeShipping, it.isFreeShippingExtra))
            }
        }
//        if (orderShop.shouldValidateWeight() && totalWeight > orderShop.maximumWeight) {
//            // overweight
//            return null to productList
//        }
        return ShipmentDetailData().apply {
            shopId = order.shopId.toString()
            preorder = preOrder
            isBlackbox = order.isBlackbox
            this.isTradein = isTradeIn
            addressId =
                if (recipientAddressModel.selectedTabIndex == 1 && recipientAddressModel.locationDataModel != null) recipientAddressModel.locationDataModel.addrId else order.addressId
            shipmentCartData = ShipmentCartData(
                originDistrictId = order.districtId,
                originPostalCode = order.postalCode,
                originLatitude = order.latitude,
                originLongitude = order.longitude,
                weight = totalWeight,
                weightActual = totalWeightActual,
                shopTier = order.shopTypeInfoData.shopTier,
                groupType = order.groupType,
                token = order.keroToken,
                ut = order.keroUnixTime,
                insurance = 1,
                productInsurance = productFInsurance,
                orderValue = orderValue,
                categoryIds = categoryList.joinToString(","),
                preOrderDuration = productPreOrderDuration,
                isFulfillment = order.isFulfillment,
                boMetadata = order.boMetadata,
                destinationAddress = recipientAddressModel.addressName,
                destinationDistrictId = recipientAddressModel.destinationDistrictId,
                destinationPostalCode = recipientAddressModel.postalCode,
                destinationLatitude = recipientAddressModel.latitude,
                destinationLongitude = recipientAddressModel.longitude
            )
        }
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

    fun getRatesParam(orderModel: CheckoutOrderModel, address: RecipientAddressModel, isTradeIn: Boolean, isTradeInDropOff: Boolean, codData: CodModel?, cartDataForRates: String): RatesParam {
        val shippingParam = getShippingParam(
            generateShippingBottomsheetParam(orderModel, address, isTradeIn),
            getProductForRatesRequest(orderModel),
            orderModel.cartStringGroup,
            isTradeInDropOff,
            address
        )
        val counter = codData?.counterCod ?: -1
        val cornerId = address.isCornerAddress
        val pslCode = RatesDataConverter.getLogisticPromoCode(orderModel)
        val isLeasing = orderModel.isLeasingProduct
        val mvc = generateRatesMvcParam(orderModel.cartStringGroup)
        val ratesParamBuilder = RatesParam.Builder(orderModel.shopShipmentList, shippingParam)
            .isCorner(cornerId)
            .codHistory(counter)
            .isLeasing(isLeasing)
            .promoCode(pslCode)
            .cartData(cartDataForRates)
            .warehouseId(orderModel.fulfillmentId.toString())
            .mvc("")
        ratesParamBuilder.mvc(mvc)
        return ratesParamBuilder.build()
    }

    suspend fun getRates(
        ratesParam: RatesParam,
        shopShipments: List<ShopShipment>,
        selectedServiceId: Int,
        selectedSpId: Int,
        orderModel: CheckoutOrderModel
    ): Pair<CourierItemData, List<ShippingCourierUiModel>>? {
        return withContext(dispatchers.io) {
            try {
                var shippingRecommendationData = ratesUseCase(ratesParam)
                shippingRecommendationData = ratesResponseStateConverter.fillState(
                    shippingRecommendationData,
                    shopShipments,
                    selectedSpId,
                    selectedServiceId
                )
                val boPromoCode = getBoPromoCode(
                    orderModel
                )
                var errorReason = "rates invalid data"
                if (orderModel.shouldResetCourier) {
                    orderModel.shouldResetCourier =
                        false
                    error("racing condition against epharmacy validation")
                }
                if (shippingRecommendationData.shippingDurationUiModels.isNotEmpty()) {
                    if (isBoUnstackEnabled && orderModel.boCode.isNotEmpty()) {
                        val logisticPromo =
                            shippingRecommendationData.listLogisticPromo.firstOrNull { it.promoCode == orderModel.boCode && !it.disabled }
                        if (logisticPromo != null) {
                            for (shippingDurationUiModel in shippingRecommendationData.shippingDurationUiModels) {
                                if (shippingDurationUiModel.shippingCourierViewModelList.isNotEmpty()) {
                                    for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                        shippingCourierUiModel.isSelected = false
                                    }
                                    for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                        if (shippingCourierUiModel.productData.shipperProductId == logisticPromo.shipperProductId && shippingCourierUiModel.productData.shipperId == logisticPromo.shipperId) {
                                            if (!shippingCourierUiModel.productData.error?.errorMessage.isNullOrEmpty()) {
//                                                view?.renderCourierStateFailed(
//                                                    shipmentGetCourierHolderData.itemPosition,
//                                                    false,
//                                                    false
//                                                )
//                                                view?.logOnErrorLoadCourier(
//                                                    MessageErrorException(
//                                                        shippingCourierUiModel.productData.error?.errorMessage
//                                                    ),
//                                                    shipmentGetCourierHolderData.itemPosition,
//                                                    boPromoCode
//                                                )
//                                                ratesQueue.remove()
//                                                itemToProcess = ratesQueue.peek()
//                                                continue@loopProcess
                                            } else {
                                                val courierItemData =
                                                    generateCourierItemData(
                                                        false,
//                                                        logisticPromo.shipperId,
                                                        selectedSpId,
                                                        orderModel,
                                                        shippingCourierUiModel,
                                                        shippingRecommendationData,
                                                        logisticPromo
                                                    )
                                                return@withContext courierItemData to shippingDurationUiModel.shippingCourierViewModelList
//                                                val validateUsePromoRequest =
//                                                    generateValidateUsePromoRequest().copy()
//                                                for (ordersItem in validateUsePromoRequest.orders) {
//                                                    if (ordersItem.cartStringGroup == shipmentGetCourierHolderData.shipmentCartItemModel.cartStringGroup) {
//                                                        if (!ordersItem.codes.contains(
//                                                                courierItemData.selectedShipper.logPromoCode
//                                                            )
//                                                        ) {
//                                                            ordersItem.codes.add(
//                                                                courierItemData.selectedShipper.logPromoCode!!
//                                                            )
//                                                            ordersItem.boCode =
//                                                                courierItemData.selectedShipper.logPromoCode!!
//                                                        }
//                                                        ordersItem.shippingId =
//                                                            courierItemData.selectedShipper.shipperId
//                                                        ordersItem.spId =
//                                                            courierItemData.selectedShipper.shipperProductId
//                                                        ordersItem.freeShippingMetadata =
//                                                            courierItemData.selectedShipper.freeShippingMetadata
//                                                        ordersItem.boCampaignId =
//                                                            courierItemData.selectedShipper.boCampaignId
//                                                        ordersItem.shippingSubsidy =
//                                                            courierItemData.selectedShipper.shippingSubsidy
//                                                        ordersItem.benefitClass =
//                                                            courierItemData.selectedShipper.benefitClass
//                                                        ordersItem.shippingPrice =
//                                                            courierItemData.selectedShipper.shippingRate.toDouble()
//                                                        ordersItem.etaText =
//                                                            courierItemData.selectedShipper.etaText!!
//                                                        ordersItem.validationMetadata =
//                                                            shipmentGetCourierHolderData.shipmentCartItemModel.validationMetadata
//                                                    }
//                                                }
//                                                removeInvalidBoCodeFromPromoRequest(
//                                                    shipmentGetCourierHolderData,
//                                                    validateUsePromoRequest
//                                                )
//                                                promoQueue.offer(
//                                                    ShipmentValidatePromoHolderData(
//                                                        validateUsePromoRequest,
//                                                        shipmentGetCourierHolderData.itemPosition,
//                                                        shipmentGetCourierHolderData.shipmentCartItemModel.cartStringGroup,
//                                                        courierItemData.selectedShipper.logPromoCode!!,
//                                                        courierItemData
//                                                    )
//                                                )
//                                                awaitPromoQueue()
//                                                ratesQueue.remove()
//                                                itemToProcess = ratesQueue.peek()
//                                                continue@loopProcess
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
                                val newSelectedSpId = getSelectedSpId(
                                    orderModel,
                                    selectedSpId,
                                    shippingDurationUiModel
                                )
                                for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                    if (shippingCourierUiModel.productData.shipperProductId == newSelectedSpId && !shippingCourierUiModel.serviceData.isUiRatesHidden) {
                                        if (!shippingCourierUiModel.productData.error?.errorMessage.isNullOrEmpty()) {
//                                            view?.renderCourierStateFailed(
//                                                shipmentGetCourierHolderData.itemPosition,
//                                                false,
//                                                false
//                                            )
//                                            view?.logOnErrorLoadCourier(
//                                                MessageErrorException(
//                                                    shippingCourierUiModel.productData.error?.errorMessage
//                                                ),
//                                                shipmentGetCourierHolderData.itemPosition,
//                                                boPromoCode
//                                            )
//                                            ratesQueue.remove()
//                                            itemToProcess = ratesQueue.peek()
//                                            continue@loopProcess
                                        } else {
                                            val courierItemData = generateCourierItemData(
                                                false,
//                                                shippingCourierUiModel.productData.shipperId,
                                                newSelectedSpId,
                                                orderModel,
                                                shippingCourierUiModel,
                                                shippingRecommendationData
                                            )
//                                            return@withContext courierItemData
                                            if (shippingCourierUiModel.productData.isUiRatesHidden && shippingCourierUiModel.serviceData.selectedShipperProductId == 0 && courierItemData.logPromoCode.isNullOrEmpty()) {
                                                // courier should only be used with BO, but no BO code found
//                                                view?.renderCourierStateFailed(
//                                                    shipmentGetCourierHolderData.itemPosition,
//                                                    false,
//                                                    false
//                                                )
//                                                view?.logOnErrorLoadCourier(
//                                                    MessageErrorException("rates ui hidden but no promo"),
//                                                    shipmentGetCourierHolderData.itemPosition,
//                                                    boPromoCode
//                                                )
//                                                ratesQueue.remove()
//                                                itemToProcess = ratesQueue.peek()
//                                                continue@loopProcess
                                            }
                                            val shouldValidatePromo =
                                                courierItemData.selectedShipper.logPromoCode != null && courierItemData.selectedShipper.logPromoCode!!.isNotEmpty()
                                            if (!shouldValidatePromo) {
                                                shippingCourierUiModel.isSelected = true
//                                                setShippingCourierViewModelsState(
//                                                    shippingDurationUiModel.shippingCourierViewModelList,
//                                                    shipmentGetCourierHolderData.shipmentCartItemModel.orderNumber
//                                                )
//                                                view?.renderCourierStateSuccess(
//                                                    courierItemData,
//                                                    shipmentGetCourierHolderData.itemPosition,
//                                                    false
//                                                )
//                                                ratesQueue.remove()
//                                                itemToProcess = ratesQueue.peek()
//                                                continue@loopProcess
                                            } else {
//                                                val validateUsePromoRequest =
//                                                    generateValidateUsePromoRequest().copy()
//                                                for (ordersItem in validateUsePromoRequest.orders) {
//                                                    if (ordersItem.cartStringGroup == shipmentGetCourierHolderData.shipmentCartItemModel.cartStringGroup) {
//                                                        if (!ordersItem.codes.contains(
//                                                                courierItemData.selectedShipper.logPromoCode
//                                                            )
//                                                        ) {
//                                                            ordersItem.codes.add(
//                                                                courierItemData.selectedShipper.logPromoCode!!
//                                                            )
//                                                            ordersItem.boCode =
//                                                                courierItemData.selectedShipper.logPromoCode!!
//                                                        }
//                                                        ordersItem.shippingId =
//                                                            courierItemData.selectedShipper.shipperId
//                                                        ordersItem.spId =
//                                                            courierItemData.selectedShipper.shipperProductId
//                                                        ordersItem.freeShippingMetadata =
//                                                            courierItemData.selectedShipper.freeShippingMetadata
//                                                        ordersItem.boCampaignId =
//                                                            courierItemData.selectedShipper.boCampaignId
//                                                        ordersItem.shippingSubsidy =
//                                                            courierItemData.selectedShipper.shippingSubsidy
//                                                        ordersItem.benefitClass =
//                                                            courierItemData.selectedShipper.benefitClass
//                                                        ordersItem.shippingPrice =
//                                                            courierItemData.selectedShipper.shippingRate.toDouble()
//                                                        ordersItem.etaText =
//                                                            courierItemData.selectedShipper.etaText!!
//                                                        ordersItem.validationMetadata =
//                                                            shipmentGetCourierHolderData.shipmentCartItemModel.validationMetadata
//                                                    }
//                                                }
//                                                removeInvalidBoCodeFromPromoRequest(
//                                                    shipmentGetCourierHolderData,
//                                                    validateUsePromoRequest
//                                                )
//                                                promoQueue.offer(
//                                                    ShipmentValidatePromoHolderData(
//                                                        validateUsePromoRequest,
//                                                        shipmentGetCourierHolderData.itemPosition,
//                                                        shipmentGetCourierHolderData.shipmentCartItemModel.cartStringGroup,
//                                                        courierItemData.selectedShipper.logPromoCode!!,
//                                                        courierItemData
//                                                    )
//                                                )
//                                                awaitPromoQueue()
//                                                ratesQueue.remove()
//                                                itemToProcess = ratesQueue.peek()
//                                                continue@loopProcess
                                            }
                                            return@withContext courierItemData to shippingDurationUiModel.shippingCourierViewModelList
                                        }
                                    }
                                }
                            }
                        }

                        // corner case auto selection if BE default duration failed
                        if (orderModel.isAutoCourierSelection) {
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
//                                        shippingCourier.productData.shipperId,
                                        selectedSpId,
                                        orderModel,
                                        shippingCourier,
                                        shippingRecommendationData
                                    )
                                    val shouldValidatePromo =
                                        courierItemData.selectedShipper.logPromoCode != null && courierItemData.selectedShipper.logPromoCode!!.isNotEmpty()
                                    if (!shouldValidatePromo) {
                                        shippingCourier.isSelected = true
//                                        view?.renderCourierStateSuccess(
//                                            courierItemData,
//                                            shipmentGetCourierHolderData.itemPosition,
//                                            false
//                                        )
//                                        ratesQueue.remove()
//                                        itemToProcess = ratesQueue.peek()
//                                        continue@loopProcess
                                    } else {
//                                        val validateUsePromoRequest =
//                                            generateValidateUsePromoRequest().copy()
//                                        for (ordersItem in validateUsePromoRequest.orders) {
//                                            if (ordersItem.cartStringGroup == shipmentGetCourierHolderData.shipmentCartItemModel.cartStringGroup) {
//                                                if (!ordersItem.codes.contains(
//                                                        courierItemData.selectedShipper.logPromoCode
//                                                    )
//                                                ) {
//                                                    ordersItem.codes.add(courierItemData.selectedShipper.logPromoCode!!)
//                                                    ordersItem.boCode =
//                                                        courierItemData.selectedShipper.logPromoCode!!
//                                                }
//                                                ordersItem.shippingId =
//                                                    courierItemData.selectedShipper.shipperId
//                                                ordersItem.spId =
//                                                    courierItemData.selectedShipper.shipperProductId
//                                                ordersItem.freeShippingMetadata =
//                                                    courierItemData.selectedShipper.freeShippingMetadata
//                                                ordersItem.boCampaignId =
//                                                    courierItemData.selectedShipper.boCampaignId
//                                                ordersItem.shippingSubsidy =
//                                                    courierItemData.selectedShipper.shippingSubsidy
//                                                ordersItem.benefitClass =
//                                                    courierItemData.selectedShipper.benefitClass
//                                                ordersItem.shippingPrice =
//                                                    courierItemData.selectedShipper.shippingRate.toDouble()
//                                                ordersItem.etaText =
//                                                    courierItemData.selectedShipper.etaText!!
//                                                ordersItem.validationMetadata =
//                                                    shipmentGetCourierHolderData.shipmentCartItemModel.validationMetadata
//                                            }
//                                        }
//                                        removeInvalidBoCodeFromPromoRequest(
//                                            shipmentGetCourierHolderData,
//                                            validateUsePromoRequest
//                                        )
//                                        promoQueue.offer(
//                                            ShipmentValidatePromoHolderData(
//                                                validateUsePromoRequest,
//                                                shipmentGetCourierHolderData.itemPosition,
//                                                shipmentGetCourierHolderData.shipmentCartItemModel.cartStringGroup,
//                                                courierItemData.selectedShipper.logPromoCode!!,
//                                                courierItemData
//                                            )
//                                        )
//                                        awaitPromoQueue()
//                                        ratesQueue.remove()
//                                        itemToProcess = ratesQueue.peek()
//                                        continue@loopProcess
                                    }
                                    return@withContext courierItemData to shippingDuration.shippingCourierViewModelList
                                }
                            }
                        }
                    }
                } else {
                    errorReason = "rates empty data"
                }
//                view?.renderCourierStateFailed(
//                    shipmentGetCourierHolderData.itemPosition,
//                    false,
//                    false
//                )
//                view?.logOnErrorLoadCourier(
//                    MessageErrorException(errorReason),
//                    shipmentGetCourierHolderData.itemPosition,
//                    boPromoCode
//                )
//                CheckoutOrderShipment(
//                    isLoading = false
//                )
                return@withContext null
            } catch (t: Throwable) {
                Timber.d(t)
                return@withContext null
            }
        }
    }

    private fun generateCourierItemData(
        isForceReloadRates: Boolean,
//        shipperId: Int,
        spId: Int,
        orderModel: CheckoutOrderModel,
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
        if (orderModel.isDisableChangeCourier) {
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
                !it.disabled && /*it.shipperId == shipperId &&*/ it.shipperProductId == spId && it.promoCode.isNotEmpty()
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
        shipmentCartItemModel: CheckoutOrderModel
    ): String {
        if (isBoUnstackEnabled) {
            return shipmentCartItemModel.boCode
        }
        return ""
    }

    private fun getSelectedSpId(
        shipmentCartItemModel: CheckoutOrderModel,
        spId: Int,
        shippingDurationUiModel: ShippingDurationUiModel
    ): Int {
        val currentServiceId =
            shipmentCartItemModel.shipment.courierItemData?.serviceId
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

    suspend fun getRatesApi(
        ratesParam: RatesParam,
        shopShipments: List<ShopShipment>,
        selectedSpId: Int
    ) {
        withContext(dispatchers.io) {
            try {
                var shippingRecommendationData = ratesApiUseCase(ratesParam)
                shippingRecommendationData = ratesResponseStateConverter.fillState(
                    shippingRecommendationData,
                    shopShipments,
                    selectedSpId,
                    0
                )
                CheckoutOrderShipment(
                    isLoading = false
                )
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }

    suspend fun getRatesWithScheduleDelivery(
        ratesParam: RatesParam,
        shopShipments: List<ShopShipment>,
        selectedServiceId: Int,
        selectedSpId: Int,
        fullfilmentId: String,
        orderModel: CheckoutOrderModel
    ): Pair<CourierItemData, List<ShippingCourierUiModel>>? {
        return withContext(dispatchers.io) {
            try {
                var shippingRecommendationData = ratesWithScheduleUseCase(ratesParam to fullfilmentId)
                shippingRecommendationData = ratesResponseStateConverter.fillState(
                    shippingRecommendationData,
                    shopShipments,
                    selectedSpId,
                    0
                )
                val boPromoCode = getBoPromoCode(
                    orderModel
                )
                var errorReason = "rates invalid data"
                if (shippingRecommendationData.shippingDurationUiModels.isNotEmpty() && shippingRecommendationData.scheduleDeliveryData != null) {
                    if (isBoUnstackEnabled && orderModel.boCode.isNotEmpty()) {
                        val logisticPromo =
                            shippingRecommendationData.listLogisticPromo.firstOrNull { it.promoCode == orderModel.boCode && !it.disabled }
                        if (logisticPromo != null) {
                            for (shippingDurationUiModel in shippingRecommendationData.shippingDurationUiModels) {
                                if (shippingDurationUiModel.shippingCourierViewModelList.isNotEmpty()) {
                                    for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                        shippingCourierUiModel.isSelected = false
                                    }
                                    for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                        if (shippingCourierUiModel.productData.shipperProductId == logisticPromo.shipperProductId && shippingCourierUiModel.productData.shipperId == logisticPromo.shipperId) {
                                            if (!shippingCourierUiModel.productData.error?.errorMessage.isNullOrEmpty()) {
//                                                view?.renderCourierStateFailed(
//                                                    shipmentGetCourierHolderData.itemPosition,
//                                                    false,
//                                                    false
//                                                )
//                                                view?.logOnErrorLoadCourier(
//                                                    MessageErrorException(
//                                                        shippingCourierUiModel.productData.error?.errorMessage
//                                                    ),
//                                                    shipmentGetCourierHolderData.itemPosition,
//                                                    boPromoCode
//                                                )
//                                                ratesQueue.remove()
//                                                itemToProcess = ratesQueue.peek()
//                                                continue@loopProcess
                                            } else {
                                                val courierItemData =
                                                    generateCourierItemDataWithScheduleDelivery(
                                                        false,
                                                        selectedServiceId,
                                                        selectedSpId,
                                                        orderModel,
                                                        shippingCourierUiModel,
                                                        shippingRecommendationData,
                                                        logisticPromo
                                                    )
                                                return@withContext courierItemData to shippingDurationUiModel.shippingCourierViewModelList
//                                                val validateUsePromoRequest =
//                                                    generateValidateUsePromoRequest().copy()
//                                                for (ordersItem in validateUsePromoRequest.orders) {
//                                                    if (ordersItem.cartStringGroup == shipmentGetCourierHolderData.shipmentCartItemModel.cartStringGroup) {
//                                                        if (!ordersItem.codes.contains(
//                                                                courierItemData.selectedShipper.logPromoCode
//                                                            )
//                                                        ) {
//                                                            ordersItem.codes.add(
//                                                                courierItemData.selectedShipper.logPromoCode!!
//                                                            )
//                                                            ordersItem.boCode =
//                                                                courierItemData.selectedShipper.logPromoCode!!
//                                                        }
//                                                        ordersItem.shippingId =
//                                                            courierItemData.selectedShipper.shipperId
//                                                        ordersItem.spId =
//                                                            courierItemData.selectedShipper.shipperProductId
//                                                        ordersItem.freeShippingMetadata =
//                                                            courierItemData.selectedShipper.freeShippingMetadata
//                                                        ordersItem.boCampaignId =
//                                                            courierItemData.selectedShipper.boCampaignId
//                                                        ordersItem.shippingSubsidy =
//                                                            courierItemData.selectedShipper.shippingSubsidy
//                                                        ordersItem.benefitClass =
//                                                            courierItemData.selectedShipper.benefitClass
//                                                        ordersItem.shippingPrice =
//                                                            courierItemData.selectedShipper.shippingRate.toDouble()
//                                                        ordersItem.etaText =
//                                                            courierItemData.selectedShipper.etaText!!
//                                                        ordersItem.validationMetadata =
//                                                            shipmentGetCourierHolderData.shipmentCartItemModel.validationMetadata
//                                                    }
//                                                }
//                                                removeInvalidBoCodeFromPromoRequest(
//                                                    shipmentGetCourierHolderData,
//                                                    validateUsePromoRequest
//                                                )
//                                                promoQueue.offer(
//                                                    ShipmentValidatePromoHolderData(
//                                                        validateUsePromoRequest,
//                                                        shipmentGetCourierHolderData.itemPosition,
//                                                        shipmentGetCourierHolderData.shipmentCartItemModel.cartStringGroup,
//                                                        courierItemData.selectedShipper.logPromoCode!!,
//                                                        courierItemData
//                                                    )
//                                                )
//                                                awaitPromoQueue()
//                                                ratesQueue.remove()
//                                                itemToProcess = ratesQueue.peek()
//                                                continue@loopProcess
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
                                val currentSelectedSpId =
                                    if (shippingDurationUiModel.serviceData.selectedShipperProductId > 0) {
                                        shippingDurationUiModel.serviceData.selectedShipperProductId
                                    } else {
                                        selectedSpId
                                    }
                                for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                    if (shippingCourierUiModel.productData.shipperProductId == currentSelectedSpId && !shippingCourierUiModel.serviceData.isUiRatesHidden) {
                                        if (!shippingCourierUiModel.productData.error?.errorMessage.isNullOrEmpty()) {
//                                            view?.renderCourierStateFailed(
//                                                shipmentGetCourierHolderData.itemPosition,
//                                                false,
//                                                false
//                                            )
//                                            view?.logOnErrorLoadCourier(
//                                                MessageErrorException(
//                                                    shippingCourierUiModel.productData.error?.errorMessage
//                                                ),
//                                                shipmentGetCourierHolderData.itemPosition,
//                                                boPromoCode
//                                            )
//                                            ratesQueue.remove()
//                                            itemToProcess = ratesQueue.peek()
//                                            continue@loopProcess
                                        } else {
                                            val courierItemData =
                                                generateCourierItemDataWithScheduleDelivery(
                                                    false,
                                                    selectedServiceId,
                                                    selectedSpId,
                                                    orderModel,
                                                    shippingCourierUiModel,
                                                    shippingRecommendationData
                                                )
                                            if (shippingCourierUiModel.productData.isUiRatesHidden && shippingCourierUiModel.serviceData.selectedShipperProductId == 0 && courierItemData.logPromoCode.isNullOrEmpty()) {
                                                // courier should only be used with BO, but no BO code found
//                                                view?.renderCourierStateFailed(
//                                                    shipmentGetCourierHolderData.itemPosition,
//                                                    false,
//                                                    false
//                                                )
//                                                view?.logOnErrorLoadCourier(
//                                                    MessageErrorException("rates ui hidden but no promo"),
//                                                    shipmentGetCourierHolderData.itemPosition,
//                                                    boPromoCode
//                                                )
//                                                ratesQueue.remove()
//                                                itemToProcess = ratesQueue.peek()
//                                                continue@loopProcess
                                            }
                                            val shouldValidatePromo =
                                                courierItemData.selectedShipper.logPromoCode != null && courierItemData.selectedShipper.logPromoCode!!.isNotEmpty()
                                            if (!shouldValidatePromo) {
                                                shippingCourierUiModel.isSelected = true
                                                return@withContext courierItemData to shippingDurationUiModel.shippingCourierViewModelList
//                                                setShippingCourierViewModelsState(
//                                                    shippingDurationUiModel.shippingCourierViewModelList,
//                                                    shipmentGetCourierHolderData.shipmentCartItemModel.orderNumber
//                                                )
//                                                view?.renderCourierStateSuccess(
//                                                    courierItemData,
//                                                    shipmentGetCourierHolderData.itemPosition,
//                                                    false
//                                                )
//                                                ratesQueue.remove()
//                                                itemToProcess = ratesQueue.peek()
//                                                continue@loopProcess
                                            } else {
                                                return@withContext courierItemData to shippingDurationUiModel.shippingCourierViewModelList
//                                                val validateUsePromoRequest =
//                                                    generateValidateUsePromoRequest().copy()
//                                                for (ordersItem in validateUsePromoRequest.orders) {
//                                                    if (ordersItem.cartStringGroup == shipmentGetCourierHolderData.shipmentCartItemModel.cartStringGroup) {
//                                                        if (!ordersItem.codes.contains(
//                                                                courierItemData.selectedShipper.logPromoCode
//                                                            )
//                                                        ) {
//                                                            ordersItem.codes.add(
//                                                                courierItemData.selectedShipper.logPromoCode!!
//                                                            )
//                                                            ordersItem.boCode =
//                                                                courierItemData.selectedShipper.logPromoCode!!
//                                                        }
//                                                        ordersItem.shippingId =
//                                                            courierItemData.selectedShipper.shipperId
//                                                        ordersItem.spId =
//                                                            courierItemData.selectedShipper.shipperProductId
//                                                        ordersItem.freeShippingMetadata =
//                                                            courierItemData.selectedShipper.freeShippingMetadata
//                                                        ordersItem.boCampaignId =
//                                                            courierItemData.selectedShipper.boCampaignId
//                                                        ordersItem.shippingSubsidy =
//                                                            courierItemData.selectedShipper.shippingSubsidy
//                                                        ordersItem.benefitClass =
//                                                            courierItemData.selectedShipper.benefitClass
//                                                        ordersItem.shippingPrice =
//                                                            courierItemData.selectedShipper.shippingRate.toDouble()
//                                                        ordersItem.etaText =
//                                                            courierItemData.selectedShipper.etaText!!
//                                                        ordersItem.validationMetadata =
//                                                            shipmentGetCourierHolderData.shipmentCartItemModel.validationMetadata
//                                                    }
//                                                }
//                                                removeInvalidBoCodeFromPromoRequest(
//                                                    shipmentGetCourierHolderData,
//                                                    validateUsePromoRequest
//                                                )
//                                                promoQueue.offer(
//                                                    ShipmentValidatePromoHolderData(
//                                                        validateUsePromoRequest,
//                                                        shipmentGetCourierHolderData.itemPosition,
//                                                        shipmentGetCourierHolderData.shipmentCartItemModel.cartStringGroup,
//                                                        courierItemData.selectedShipper.logPromoCode!!,
//                                                        courierItemData
//                                                    )
//                                                )
//                                                awaitPromoQueue()
//                                                ratesQueue.remove()
//                                                itemToProcess = ratesQueue.peek()
//                                                continue@loopProcess
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // corner case auto selection if BE default duration failed
                        if (orderModel.isAutoCourierSelection || orderModel.isDisableChangeCourier) {
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
                                            selectedServiceId,
                                            selectedSpId,
                                            orderModel,
                                            shippingCourier,
                                            shippingRecommendationData
                                        )
                                    val shouldValidatePromo =
                                        courierItemData.selectedShipper.logPromoCode != null && courierItemData.selectedShipper.logPromoCode!!.isNotEmpty()
                                    if (!shouldValidatePromo) {
                                        shippingCourier.isSelected = true
                                        return@withContext courierItemData to shippingDuration.shippingCourierViewModelList
//                                        view?.renderCourierStateSuccess(
//                                            courierItemData,
//                                            shipmentGetCourierHolderData.itemPosition,
//                                            false
//                                        )
//                                        ratesQueue.remove()
//                                        itemToProcess = ratesQueue.peek()
//                                        continue@loopProcess
                                    } else {
                                        return@withContext courierItemData to shippingDuration.shippingCourierViewModelList
//                                        val validateUsePromoRequest =
//                                            generateValidateUsePromoRequest().copy()
//                                        for (ordersItem in validateUsePromoRequest.orders) {
//                                            if (ordersItem.cartStringGroup == shipmentGetCourierHolderData.shipmentCartItemModel.cartStringGroup) {
//                                                if (!ordersItem.codes.contains(
//                                                        courierItemData.selectedShipper.logPromoCode
//                                                    )
//                                                ) {
//                                                    ordersItem.codes.add(courierItemData.selectedShipper.logPromoCode!!)
//                                                    ordersItem.boCode =
//                                                        courierItemData.selectedShipper.logPromoCode!!
//                                                }
//                                                ordersItem.shippingId =
//                                                    courierItemData.selectedShipper.shipperId
//                                                ordersItem.spId =
//                                                    courierItemData.selectedShipper.shipperProductId
//                                                ordersItem.freeShippingMetadata =
//                                                    courierItemData.selectedShipper.freeShippingMetadata
//                                                ordersItem.boCampaignId =
//                                                    courierItemData.selectedShipper.boCampaignId
//                                                ordersItem.shippingSubsidy =
//                                                    courierItemData.selectedShipper.shippingSubsidy
//                                                ordersItem.benefitClass =
//                                                    courierItemData.selectedShipper.benefitClass
//                                                ordersItem.shippingPrice =
//                                                    courierItemData.selectedShipper.shippingRate.toDouble()
//                                                ordersItem.etaText =
//                                                    courierItemData.selectedShipper.etaText!!
//                                                ordersItem.validationMetadata =
//                                                    shipmentGetCourierHolderData.shipmentCartItemModel.validationMetadata
//                                            }
//                                        }
//                                        removeInvalidBoCodeFromPromoRequest(
//                                            shipmentGetCourierHolderData,
//                                            validateUsePromoRequest
//                                        )
//                                        promoQueue.offer(
//                                            ShipmentValidatePromoHolderData(
//                                                validateUsePromoRequest,
//                                                shipmentGetCourierHolderData.itemPosition,
//                                                shipmentGetCourierHolderData.shipmentCartItemModel.cartStringGroup,
//                                                courierItemData.selectedShipper.logPromoCode!!,
//                                                courierItemData
//                                            )
//                                        )
//                                        awaitPromoQueue()
//                                        ratesQueue.remove()
//                                        itemToProcess = ratesQueue.peek()
//                                        continue@loopProcess
                                    }
                                }
                            }
                        }
                    }
                } else {
                    errorReason = "rates empty data"
                }
//                view?.renderCourierStateFailed(
//                    shipmentGetCourierHolderData.itemPosition,
//                    false,
//                    false
//                )
//                view?.logOnErrorLoadCourier(
//                    MessageErrorException(errorReason),
//                    shipmentGetCourierHolderData.itemPosition,
//                    boPromoCode
//                )
                return@withContext null
            } catch (t: Throwable) {
                Timber.d(t)
                return@withContext null
            }
        }
    }

    private fun generateCourierItemDataWithScheduleDelivery(
        isForceReloadRates: Boolean,
        shipperId: Int,
        spId: Int,
        orderModel: CheckoutOrderModel,
        shippingCourierUiModel: ShippingCourierUiModel,
        shippingRecommendationData: ShippingRecommendationData,
        logisticPromo: LogisticPromoUiModel? = null
    ): CourierItemData {
        var courierItemData =
            shippingCourierConverter.convertToCourierItemDataNew(
                shippingCourierUiModel,
                shippingRecommendationData,
                orderModel.validationMetadata
            )

        // Auto apply Promo Stacking Logistic
        var logisticPromoChosen = logisticPromo
        if (orderModel.isDisableChangeCourier) {
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
            courierItemData = shippingCourierConverter.convertToCourierItemDataNew(
                courierUiModel,
                shippingRecommendationData,
                orderModel.validationMetadata
            )
        }

        handleSyncShipmentCartItemModel(courierItemData, orderModel)

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
        orderModel: CheckoutOrderModel
    ) {
        if (courierItemData.scheduleDeliveryUiModel != null) {
            val isScheduleDeliverySelected = courierItemData.scheduleDeliveryUiModel?.isSelected
            if (isScheduleDeliverySelected == true &&
                (
                    courierItemData.scheduleDeliveryUiModel?.timeslotId != orderModel.timeslotId ||
                        courierItemData.scheduleDeliveryUiModel?.scheduleDate != orderModel.scheduleDate
                    )
            ) {
                orderModel.scheduleDate =
                    courierItemData.scheduleDeliveryUiModel?.scheduleDate ?: ""
                orderModel.timeslotId =
                    courierItemData.scheduleDeliveryUiModel?.timeslotId ?: 0
                orderModel.validationMetadata =
                    courierItemData.scheduleDeliveryUiModel?.deliveryProduct?.validationMetadata
                        ?: ""
            } else if (isScheduleDeliverySelected == false) {
                orderModel.scheduleDate = ""
                orderModel.timeslotId = 0L
                orderModel.validationMetadata = ""
            }
        }
    }
}

data class EditAddressResult(
    val isSuccess: Boolean,
    val errorMessage: String = "",
    val throwable: Throwable? = null
)
