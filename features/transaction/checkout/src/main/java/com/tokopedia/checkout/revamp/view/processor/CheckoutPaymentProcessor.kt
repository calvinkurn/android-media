package com.tokopedia.checkout.revamp.view.processor

import android.annotation.SuppressLint
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkout.domain.model.cartshipmentform.ShipmentPlatformFeeData
import com.tokopedia.checkout.domain.model.platformfee.PaymentFeeCheckoutRequest
import com.tokopedia.checkout.domain.model.platformfee.PaymentFeeResponse
import com.tokopedia.checkout.domain.usecase.GetPaymentFeeCheckoutUseCase
import com.tokopedia.checkout.revamp.view.CheckoutViewModel
import com.tokopedia.checkout.revamp.view.address
import com.tokopedia.checkout.revamp.view.cost
import com.tokopedia.checkout.revamp.view.payment
import com.tokopedia.checkout.revamp.view.promo
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCostModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutItem
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.view.uimodel.ShipmentPaymentFeeModel
import com.tokopedia.checkoutpayment.data.AdditionalInfoData
import com.tokopedia.checkoutpayment.data.BenefitSummaryInfoData
import com.tokopedia.checkoutpayment.data.CartAddOnData
import com.tokopedia.checkoutpayment.data.CartAddressData
import com.tokopedia.checkoutpayment.data.CartData
import com.tokopedia.checkoutpayment.data.CartDetail
import com.tokopedia.checkoutpayment.data.CartDetailData
import com.tokopedia.checkoutpayment.data.CartGroupData
import com.tokopedia.checkoutpayment.data.CartProductCategoryData
import com.tokopedia.checkoutpayment.data.CartProductData
import com.tokopedia.checkoutpayment.data.CartShippingInfoData
import com.tokopedia.checkoutpayment.data.CartShopOrderData
import com.tokopedia.checkoutpayment.data.DetailsItemData
import com.tokopedia.checkoutpayment.data.GetPaymentWidgetRequest
import com.tokopedia.checkoutpayment.data.PaymentData
import com.tokopedia.checkoutpayment.data.PaymentFeeRequest
import com.tokopedia.checkoutpayment.data.PaymentRequest
import com.tokopedia.checkoutpayment.data.PromoDetail
import com.tokopedia.checkoutpayment.data.SummariesItemData
import com.tokopedia.checkoutpayment.data.UsageSummariesData
import com.tokopedia.checkoutpayment.data.VoucherOrderItemData
import com.tokopedia.checkoutpayment.processor.PaymentProcessor
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class CheckoutPaymentProcessor @Inject constructor(
    private val getPaymentFeeCheckoutUseCase: GetPaymentFeeCheckoutUseCase,
    private val processor: PaymentProcessor,
    private val checkoutAnalyticsCourierSelection: CheckoutAnalyticsCourierSelection,
    private val userSessionInterface: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers,
    private val helper: CheckoutDataHelper
) {

    suspend fun checkPlatformFee(
        shipmentPlatformFeeData: ShipmentPlatformFeeData,
        cost: CheckoutCostModel,
        request: PaymentFeeCheckoutRequest
    ): CheckoutCostModel {
        if (!cost.hasSelectAllShipping) {
            return cost.copy(dynamicPlatformFee = ShipmentPaymentFeeModel(isLoading = false))
        }
        if (shipmentPlatformFeeData.isEnable) {
            val platformFeeModel = cost.dynamicPlatformFee
            if (cost.totalPrice > platformFeeModel.minRange &&
                cost.totalPrice < platformFeeModel.maxRange
            ) {
                return cost.copy(dynamicPlatformFee = platformFeeModel.copy(isLoading = false))
            } else {
                val paymentFee = getDynamicPaymentFee(request)
                if (paymentFee != null) {
                    val platformFee = ShipmentPaymentFeeModel()
                    for (fee in paymentFee.data) {
                        if (fee.code.equals(
                                CheckoutViewModel.PLATFORM_FEE_CODE,
                                ignoreCase = true
                            )
                        ) {
                            platformFee.title = fee.title
                            platformFee.fee = fee.fee
                            platformFee.minRange = fee.minRange
                            platformFee.maxRange = fee.maxRange
                            platformFee.isShowTooltip = fee.showTooltip
                            platformFee.tooltip = fee.tooltipInfo
                            platformFee.isShowSlashed = fee.showSlashed
                            platformFee.slashedFee = fee.slashedFee.toDouble()
                        }
                    }
                    checkoutAnalyticsCourierSelection.eventViewPlatformFeeInCheckoutPage(
                        userSessionInterface.userId,
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(
                            platformFee.fee.toLong(),
                            false
                        ).removeDecimalSuffix()
                    )
                    return cost.copy(dynamicPlatformFee = platformFee)
                } else {
                    return cost.copy(
                        dynamicPlatformFee = ShipmentPaymentFeeModel(
                            isShowTicker = true,
                            ticker = shipmentPlatformFeeData.errorWording
                        )
                    )
                }
            }
        }
        return cost.copy(dynamicPlatformFee = cost.dynamicPlatformFee.copy(isLoading = false))
    }

    private suspend fun getDynamicPaymentFee(request: PaymentFeeCheckoutRequest): PaymentFeeResponse? {
        return withContext(dispatchers.io) {
            try {
                getPaymentFeeCheckoutUseCase.setParams(request)
                val paymentFeeGqlResponse = getPaymentFeeCheckoutUseCase.executeOnBackground()
                if (paymentFeeGqlResponse.response.success) {
                    return@withContext paymentFeeGqlResponse.response
                } else {
                    return@withContext null
                }
            } catch (t: Throwable) {
                Timber.d(t)
                return@withContext null
            }
        }
    }

    suspend fun checkPlatformFeeOcc(
        shipmentPlatformFeeData: ShipmentPlatformFeeData,
        cost: CheckoutCostModel,
        request: PaymentFeeRequest
    ): CheckoutCostModel {
        if (!cost.hasSelectAllShipping) {
            return cost.copy(dynamicPlatformFee = ShipmentPaymentFeeModel(isLoading = false))
        }
        val platformFeeModel = cost.dynamicPlatformFee
        val paymentFees = processor.getPaymentFee(request)
        if (paymentFees != null) {
            val platformFee = ShipmentPaymentFeeModel()
            for (fee in paymentFees) {
                platformFee.title = fee.title
                platformFee.fee = fee.fee
                platformFee.isShowTooltip = fee.showTooltip
                platformFee.tooltip = fee.tooltipInfo
                platformFee.isShowSlashed = fee.showSlashed
                platformFee.slashedFee = fee.slashedFee.toDouble()
            }
//                    checkoutAnalyticsCourierSelection.eventViewPlatformFeeInCheckoutPage(
//                        userSessionInterface.userId,
//                        CurrencyFormatUtil.convertPriceValueToIdrFormat(
//                            platformFee.fee.toLong(),
//                            false
//                        ).removeDecimalSuffix()
//                    )
            return cost.copy(
                dynamicPlatformFee = platformFeeModel.copy(isLoading = false),
                dynamicPaymentFees = paymentFees
            )
        } else {
            return cost.copy(
                dynamicPlatformFee = ShipmentPaymentFeeModel(
                    isShowTicker = true,
                    ticker = shipmentPlatformFeeData.errorWording
                )
            )
        }
    }

    @SuppressLint("PII Data Exposure")
    fun generatePaymentRequest(
        checkoutItems: List<CheckoutItem>
    ): PaymentRequest {
        val address = checkoutItems.address()!!
        val promo = checkoutItems.promo()!!
        val payment = checkoutItems.payment()!!
        val cost = checkoutItems.cost()!!
        return PaymentRequest(
            payment = PaymentData(
                gatewayCode = "orderPayment.gatewayCode",
                profileCode = payment.data?.paymentWidgetData?.firstOrNull()?.profileCode.orEmpty(),
                paymentAmount = cost.totalPrice
            ),
            CartDetail(
                cart = CartData(
                    data = listOf(
                        CartDetailData(
                            address = CartAddressData(
                                id = address.recipientAddressModel.id,
                                address = address.recipientAddressModel.street,
                                state = address.recipientAddressModel.provinceName,
                                city = address.recipientAddressModel.cityName,
                                country = address.recipientAddressModel.countryName,
                                postalCode = address.recipientAddressModel.postalCode
                            ),
                            checkoutItems.filterIsInstance(CheckoutOrderModel::class.java).mapNotNull {
                                if (it.isError) {
                                    return@mapNotNull null
                                } else {
                                    val selectedShipper = it.shipment.courierItemData?.selectedShipper
                                    val courierData = it.shipment.shippingCourierUiModels.firstOrNull { courier -> courier.productData.shipperProductId == selectedShipper?.shipperProductId }
                                    CartGroupData(
                                        cartStringGroup = it.cartStringGroup,
                                        shippingInfo = CartShippingInfoData(
                                            spId = selectedShipper?.shipperProductId.toZeroIfNull().toString(),
                                            originalShippingPrice = selectedShipper?.shipperPrice.toZeroIfNull().toDouble(),
                                            serviceName = courierData?.serviceData?.serviceName.orEmpty(),
                                            shipperName = courierData?.productData?.shipperName.orEmpty(),
                                            eta = selectedShipper?.etaText.orEmpty(),
                                            insurancePrice = selectedShipper?.insurancePrice.toZeroIfNull().toDouble()
                                        ),
                                        shopOrders = helper.getOrderProducts(checkoutItems, it.cartStringGroup).groupBy { item ->
                                            (item as CheckoutProductModel).cartStringOrder
                                        }.values.map { order ->
                                            val singleItem = order.first() as CheckoutProductModel
                                            CartShopOrderData(
                                                shopId = singleItem.shopId,
                                                warehouseId = singleItem.warehouseId.toLongOrZero(),
                                                shopTier = singleItem.shopTier.toLong(),
                                                products = order.mapNotNull { product ->
                                                    val orderProduct = product as CheckoutProductModel
                                                    if (!orderProduct.isError) {
                                                        CartProductData(
                                                            productId = orderProduct.productId.toString(),
                                                            name = orderProduct.name,
                                                            price = orderProduct.price.toLong(),
                                                            quantity = orderProduct.quantity.toLong(),
                                                            totalPrice = orderProduct.price.toLong() * orderProduct.quantity,
                                                            bundleGroupId = "",
                                                            addonItems = generateAddonProductLevel(
                                                                orderProduct
                                                            ),
                                                            category = CartProductCategoryData(
                                                                id = orderProduct.productCatId.toString(),
                                                                name = "orderProduct.lastLevelCategory",
                                                                identifier = "orderProduct.categoryIdentifier"
                                                            )
                                                        )
                                                    } else {
                                                        null
                                                    }
                                                },
                                                bundle = Collections.emptyList(),
                                                addonItems = generateAddonOrderLevel(it),
                                                cartStringOrder = singleItem.cartStringOrder
                                            )
                                        }
                                    )
                                }
                            }
                        )
                    )
                )
            ),
            PromoDetail(
                benefitSummaryInfo = BenefitSummaryInfoData(
                    promo.promo.benefitSummaryInfo.summaries.map { summary ->
                        SummariesItemData(
                            type = summary.type,
                            details = summary.details.map { detail ->
                                DetailsItemData(
                                    amount = detail.amount.toDouble(),
                                    type = detail.type
                                )
                            }
                        )
                    }
                ),
                voucherOrders = promo.promo.voucherOrders.mapNotNull { voucher ->
                    if (voucher.uniqueId.isNotEmpty() &&
                        voucher.message.state != "red" &&
                        voucher.code.isNotEmpty() &&
                        voucher.type.isNotEmpty()
                    ) {
                        VoucherOrderItemData(
                            code = voucher.code,
                            uniqueId = voucher.uniqueId,
                            shippingId = voucher.shippingId.toLong(),
                            spId = voucher.spId.toLong(),
                            type = voucher.type,
                            success = voucher.success,
                            cartStringGroup = voucher.cartStringGroup
                        )
                    } else {
                        null
                    }
                },
                additionalInfo = AdditionalInfoData(
                    usageSummaries = promo.promo.additionalInfo.usageSummaries.map { usage ->
                        UsageSummariesData(
                            type = usage.type,
                            amountString = usage.amountStr,
                            amount = usage.amount.toDouble()
                        )
                    }
                )
            )
        )
    }

    private fun generateAddonOrderLevel(order: CheckoutOrderModel): List<CartAddOnData> {
        val addOnItemModel = order.addOnsOrderLevelModel.addOnsDataItemModelList.firstOrNull()
        if (order.addOnsOrderLevelModel.status == 1 && addOnItemModel != null) {
            return listOf(
                CartAddOnData(
                    name = order.addOnsOrderLevelModel.addOnsButtonModel.title,
                    price = addOnItemModel.addOnPrice.toLong() / addOnItemModel.addOnQty,
                    quantity = addOnItemModel.addOnQty,
                    totalPrice = addOnItemModel.addOnPrice.toLong()
                )
            )
        }
        return emptyList()
    }

    private fun generateAddonProductLevel(orderProduct: CheckoutProductModel): List<CartAddOnData> {
        val addOnProductLevelItems = mutableListOf<CartAddOnData>()
        val addOnItemModel = orderProduct.addOnGiftingProductLevelModel.addOnsDataItemModelList.firstOrNull()
        if (orderProduct.addOnGiftingProductLevelModel.status == 1 && addOnItemModel != null) {
            addOnProductLevelItems.add(
                CartAddOnData(
                    name = orderProduct.addOnGiftingProductLevelModel.addOnsButtonModel.title,
                    price = addOnItemModel.addOnPrice.toLong() / addOnItemModel.addOnQty,
                    quantity = addOnItemModel.addOnQty,
                    totalPrice = addOnItemModel.addOnPrice.toLong()
                )
            )
        }

        orderProduct.addOnProduct.listAddOnProductData.forEach { addOnsProduct ->
            if (addOnsProduct.status == AddOnConstant.ADD_ON_PRODUCT_STATUS_CHECK || addOnsProduct.status == AddOnConstant.ADD_ON_PRODUCT_STATUS_MANDATORY) {
                val addOnQty: Long =
                    if (addOnsProduct.fixedQty) 1 else orderProduct.quantity.toLong()
                addOnProductLevelItems.add(
                    CartAddOnData(
                        name = addOnsProduct.name,
                        price = addOnsProduct.price.toLong(),
                        quantity = addOnQty,
                        totalPrice = addOnsProduct.price.toLong() * addOnQty
                    )
                )
            }
        }
        return addOnProductLevelItems
    }

    suspend fun getPaymentWidget(param: GetPaymentWidgetRequest, payment: CheckoutPaymentModel): CheckoutPaymentModel {
        return payment.copy(data = processor.getPaymentWidget(param))
    }
}
