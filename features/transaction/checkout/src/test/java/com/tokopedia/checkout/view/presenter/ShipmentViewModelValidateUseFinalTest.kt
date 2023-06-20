package com.tokopedia.checkout.view.presenter

import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.checkout.view.helper.ShipmentScheduleDeliveryMapData
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemTopModel
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.promocheckout.common.view.uimodel.VoucherLogisticItemUiModel
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.DEFAULT_ERROR_MESSAGE_VALIDATE_PROMO
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.AdditionalInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ErrorDetailUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoSpIdUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.TickerInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import io.mockk.coEvery
import io.mockk.verify
import io.mockk.verifyOrder
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
import org.junit.Test
import rx.observers.TestSubscriber
import rx.subjects.PublishSubject

class ShipmentViewModelValidateUseFinalTest : BaseShipmentViewModelTest() {

    @Test
    fun `WHEN validate use success THEN should update promo button`() {
        // Given
        val promoUiModel = PromoUiModel(
            voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(
                    type = "logistic",
                    messageUiModel = MessageUiModel(state = "green")
                )
            )
        )
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            ValidateUsePromoRevampUiModel(
                status = "OK",
                errorCode = "200",
                promoUiModel = promoUiModel
            )

        // When
        viewModel.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verify {
            view.updateButtonPromoCheckout(promoUiModel, false)
        }
    }

    @Test
    fun `WHEN validate use success but has error message on additional info THEN should update promo button and show error message`() {
        // Given
        val message = "error"
        val promoUiModel = PromoUiModel(
            voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(
                    type = "logistic",
                    messageUiModel = MessageUiModel(state = "green")
                )
            ),
            additionalInfoUiModel = AdditionalInfoUiModel(
                errorDetailUiModel = ErrorDetailUiModel(
                    message = message
                )
            )
        )
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            ValidateUsePromoRevampUiModel(
                status = "OK",
                errorCode = "200",
                promoUiModel = promoUiModel
            )

        // When
        viewModel.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verifyOrder {
            view.showToastNormal(message)
            view.updateButtonPromoCheckout(promoUiModel, false)
        }
    }

    @Test
    fun `WHEN validate use success but has error BBO THEN should update promo button and show error message`() {
        // Given
        val message = "error"
        val tmpCartString = "123-abc"
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = tmpCartString)
        viewModel.shipmentCartItemModelList = ArrayList<ShipmentCartItemModel>().apply {
            add(shipmentCartItemModel)
        }
        val promoUiModel = PromoUiModel(
            voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(
                    type = "logistic",
                    messageUiModel = MessageUiModel(state = "red", text = message),
                    uniqueId = tmpCartString
                )
            )
        )
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            ValidateUsePromoRevampUiModel(
                status = "OK",
                errorCode = "200",
                promoUiModel = promoUiModel
            )

        // When
        viewModel.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verifyOrder {
            view.showToastError(message)
            view.updateButtonPromoCheckout(promoUiModel, false)
        }
    }

    @Test
    fun `WHEN validate use success but has error multiple applied BBO THEN should update promo button and show error message`() {
        // Given
        val tmpCartString = "123-abc"
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartStringGroup = tmpCartString
        )
        viewModel.shipmentCartItemModelList = ArrayList<ShipmentCartItemModel>().apply {
            add(shipmentCartItemModel)
        }
        val message = "error"
        val promoUiModel = PromoUiModel(
            voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(
                    type = "logistic",
                    messageUiModel = MessageUiModel(state = "red", text = message),
                    cartStringGroup = tmpCartString
                )
            )
        )
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            ValidateUsePromoRevampUiModel(
                status = "OK",
                errorCode = "200",
                promoUiModel = promoUiModel
            )

        // When
        viewModel.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verifyOrder {
            view.showToastError(message)
            view.resetCourier(shipmentCartItemModel)
            view.updateButtonPromoCheckout(promoUiModel, false)
        }
    }

    @Test
    fun `WHEN validate use success but has no applied BBO response THEN should reset courier and show error message`() {
        // Given
        val tmpCartString = "123-abc"
        val code = "code"
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartStringGroup = tmpCartString,
            voucherLogisticItemUiModel = VoucherLogisticItemUiModel(code = code)
        )
        viewModel.shipmentCartItemModelList = ArrayList<ShipmentCartItemModel>().apply {
            add(shipmentCartItemModel)
        }
        val message = "error"
        val promoUiModel = PromoUiModel()
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            ValidateUsePromoRevampUiModel(
                status = "OK",
                errorCode = "200",
                promoUiModel = promoUiModel
            )

        // When
        viewModel.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verifyOrder {
            view.resetCourier(shipmentCartItemModel)
            view.updateButtonPromoCheckout(promoUiModel, false)
        }
    }

    @Test
    fun `WHEN validate use success with ticker data and current ticker not exist THEN should update promo button and update ticker`() {
        // Given
//        presenter.tickerAnnouncementHolderData = null
        val tickerMessage = "ticker message"
        val tickerStatusCode = "1"
        val promoUiModel = PromoUiModel(
            voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(
                    type = "logistic",
                    messageUiModel = MessageUiModel(state = "green")
                )
            ),
            tickerInfoUiModel = TickerInfoUiModel(
                statusCode = tickerStatusCode.toInt(),
                message = tickerMessage
            )
        )
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            ValidateUsePromoRevampUiModel(
                status = "OK",
                errorCode = "200",
                promoUiModel = promoUiModel
            )

        // When
        viewModel.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verify {
//            view.updateTickerAnnouncementMessage()
            shipmentAnalyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
                tickerStatusCode
            )
            view.updateButtonPromoCheckout(promoUiModel, false)
        }
    }

    @Test
    fun `WHEN validate use success with ticker data and current ticker exist THEN should update promo button and update ticker`() {
        // Given
        viewModel.tickerAnnouncementHolderData.value =
            TickerAnnouncementHolderData(id = "0", message = "")
        val tickerMessage = "ticker message"
        val tickerStatusCode = "1"
        val promoUiModel = PromoUiModel(
            voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(
                    type = "logistic",
                    messageUiModel = MessageUiModel(state = "green")
                )
            ),
            tickerInfoUiModel = TickerInfoUiModel(
                statusCode = tickerStatusCode.toInt(),
                message = tickerMessage
            )
        )
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            ValidateUsePromoRevampUiModel(
                status = "OK",
                errorCode = "200",
                promoUiModel = promoUiModel
            )

        // When
        viewModel.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verify {
//            view.updateTickerAnnouncementMessage()
            shipmentAnalyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
                tickerStatusCode
            )
            view.updateButtonPromoCheckout(promoUiModel, false)
        }
    }

    @Test
    fun `WHEN validate use success and has mvc data THEN should update promo button and reload rates`() {
        // Given
        val lastSelectedCourierOrderIndex = 1
        val cartString = "123-abc"
        val promoUiModel = PromoUiModel(
            voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(
                    type = "logistic",
                    messageUiModel = MessageUiModel(state = "green")
                )
            ),
            additionalInfoUiModel = AdditionalInfoUiModel(
                promoSpIds = ArrayList<PromoSpIdUiModel>().apply {
                    add(
                        PromoSpIdUiModel(
                            uniqueId = cartString
                        )
                    )
                }
            )
        )
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            ValidateUsePromoRevampUiModel(
                status = "OK",
                errorCode = "200",
                promoUiModel = promoUiModel
            )

        // When
        viewModel.checkPromoCheckoutFinalShipment(
            ValidateUsePromoRequest(),
            lastSelectedCourierOrderIndex,
            cartString
        )

        // Then
        verify {
            view.prepareReloadRates(lastSelectedCourierOrderIndex, false)
            view.updateButtonPromoCheckout(promoUiModel, false)
        }
    }

    @Test
    fun `WHEN validate use success and has mvc data but uniqueId not matched THEN should not reload rates`() {
        // Given
        val lastSelectedCourierOrderIndex = 1
        val cartString = "123-abc"
        val promoUiModel = PromoUiModel(
            voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(
                    type = "logistic",
                    messageUiModel = MessageUiModel(state = "green")
                )
            ),
            additionalInfoUiModel = AdditionalInfoUiModel(
                promoSpIds = ArrayList<PromoSpIdUiModel>().apply {
                    add(
                        PromoSpIdUiModel(
                            uniqueId = "other cartString"
                        )
                    )
                }
            )
        )
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            ValidateUsePromoRevampUiModel(
                status = "OK",
                promoUiModel = promoUiModel
            )

        // When
        viewModel.checkPromoCheckoutFinalShipment(
            ValidateUsePromoRequest(),
            lastSelectedCourierOrderIndex,
            cartString
        )

        // Then
        verify(inverse = true) {
            view.prepareReloadRates(lastSelectedCourierOrderIndex, false)
        }
    }

    @Test
    fun `WHEN validate use success with red state global coupon THEN should hit tracker`() {
        // Given
        val errorMessage = "error global promo"
        val promoUiModel = PromoUiModel(
            codes = listOf("code"),
            messageUiModel = MessageUiModel(state = "red", text = errorMessage)
        )
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            ValidateUsePromoRevampUiModel(
                status = "OK",
                errorCode = "200",
                promoUiModel = promoUiModel
            )
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel()

        // When
        viewModel.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verify {
            shipmentAnalyticsActionListener.sendAnalyticsViewPromoAfterAdjustItem(errorMessage)
        }
    }

    @Test
    fun `WHEN validate use status get error THEN should render error and reset promo benefit`() {
        // Given
        val message = "error"
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            ValidateUsePromoRevampUiModel(
                status = "ERROR",
                message = listOf(message),
                promoUiModel = PromoUiModel()
            )

        // When
        viewModel.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verifySequence {
            view.renderErrorCheckPromoShipmentData(message)
            view.resetPromoBenefit()
            view.resetAllCourier()
        }
    }

    @Test
    fun `WHEN validate use status get error but no error message THEN should show toaster with default error message`() {
        // Given
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            ValidateUsePromoRevampUiModel(
                status = "ERROR",
                message = emptyList(),
                promoUiModel = PromoUiModel()
            )

        // When
        viewModel.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verifySequence {
            view.renderErrorCheckPromoShipmentData(DEFAULT_ERROR_MESSAGE_VALIDATE_PROMO)
            view.resetPromoBenefit()
            view.resetAllCourier()
        }
    }

    @Test
    fun `WHEN validate use status get exception THEN should render error`() {
        // Given
        val message = "error"
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } throws
            ResponseErrorException()

        // When
        viewModel.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verifySequence {
            view.activity
            view.renderErrorCheckPromoShipmentData(any())
        }
    }

    @Test
    fun `WHEN validate use status get akamai exception THEN should show error and reset courier and clear promo`() {
        // Given
        val validateUsePromoRequest = ValidateUsePromoRequest().apply {
            codes = mutableListOf("a", "b")
            orders = mutableListOf(
                OrdersItem().apply {
                    codes = mutableListOf("c")
                }
            )
        }
        viewModel.setLastValidateUseRequest(validateUsePromoRequest)
        val message = "error"
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } throws
            AkamaiErrorException(message)
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel()

        // When
        viewModel.checkPromoCheckoutFinalShipment(validateUsePromoRequest, 0, "")

        // Then
        verifySequence {
            view.showToastError(message)
            view.resetAllCourier()
            view.doResetButtonPromoCheckout()
        }

        assertEquals(
            ValidateUsePromoRequest(orders = listOf(OrdersItem())),
            viewModel.lastValidateUseRequest
        )
    }

    @Test
    fun `WHEN validate use success from schedule delivery THEN should complete uncompleted publisher`() {
        // Given
        val promoUiModel = PromoUiModel(
            voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(type = "logistic", messageUiModel = MessageUiModel(state = "green"))
            )
        )
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            ValidateUsePromoRevampUiModel(
                status = "OK",
                errorCode = "200",
                promoUiModel = promoUiModel
            )
        val testSubscriber = TestSubscriber.create<Boolean>()
        val donePublisher = PublishSubject.create<Boolean>()
        donePublisher.subscribe(testSubscriber)
        val shipmentScheduleDeliveryMapData = ShipmentScheduleDeliveryMapData(
            donePublisher,
            shouldStopInClearCache = false,
            shouldStopInValidateUsePromo = false
        )
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartStringGroup = "123",
            shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
            cartItemModels = listOf(CartItemModel(cartStringGroup = "123"))
        )
        viewModel.shipmentCartItemModelList = listOf(shipmentCartItemModel)
        viewModel.setScheduleDeliveryMapData(shipmentCartItemModel.cartStringGroup, shipmentScheduleDeliveryMapData)

        // When
        viewModel.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verify {
            view.updateButtonPromoCheckout(promoUiModel, false)
        }
        testSubscriber.assertCompleted()
    }

    @Test
    fun `WHEN generate coupon list recommendation request THEN should return with correct number of order`() {
        // Given
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartStringGroup = "123",
            shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
            cartItemModels = listOf(CartItemModel(cartStringGroup = "123", cartStringOrder = "1"), CartItemModel(cartStringGroup = "123", cartStringOrder = "2"))
        )
        viewModel.shipmentCartItemModelList = listOf(shipmentCartItemModel)

        // When
        val couponListRecommendationRequest = viewModel.generateCouponListRecommendationRequest()

        // Then
        assertEquals(2, couponListRecommendationRequest.orders.size)
    }

    @Test
    fun `WHEN generate coupon list recommendation request with shipping but no courier THEN should return with no courier info`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "234",
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
                cartItemModels = listOf(CartItemModel(cartStringGroup = "234", cartStringOrder = "1"), CartItemModel(cartStringGroup = "234", cartStringOrder = "1")),
                selectedShipmentDetailData = ShipmentDetailData()
            )
        )

        // When
        val couponListRecommendationRequest = viewModel.generateCouponListRecommendationRequest()

        // Then
        assertEquals(false, couponListRecommendationRequest.orders[0].freeShippingMetadata.isNotEmpty())
        assertEquals(0, couponListRecommendationRequest.orders[0].spId)
    }

    @Test
    fun `WHEN generate coupon list recommendation request with shipping & bo THEN should return with correct number of order & shipping & bo`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "123",
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
                cartItemModels = listOf(CartItemModel(cartStringGroup = "123", cartStringOrder = "1"), CartItemModel(cartStringGroup = "123", cartStringOrder = "2")),
                selectedShipmentDetailData = ShipmentDetailData(selectedCourier = CourierItemData(freeShippingMetadata = "free_shipping_metadata", boCampaignId = 1)),
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(code = "code")
            ),
            ShipmentCartItemModel(
                cartStringGroup = "234",
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
                cartItemModels = listOf(CartItemModel(cartStringGroup = "234", cartStringOrder = "1"), CartItemModel(cartStringGroup = "234", cartStringOrder = "1")),
                selectedShipmentDetailData = ShipmentDetailData(selectedCourier = CourierItemData(freeShippingMetadata = "free_shipping_metadata", boCampaignId = 1))
            )
        )

        // When
        val couponListRecommendationRequest = viewModel.generateCouponListRecommendationRequest()

        // Then
        assertEquals(3, couponListRecommendationRequest.orders.size)
        assertEquals(true, couponListRecommendationRequest.orders[0].freeShippingMetadata.isNotEmpty())
        assertEquals(true, couponListRecommendationRequest.orders[1].freeShippingMetadata.isNotEmpty())
        assertEquals(false, couponListRecommendationRequest.orders[2].freeShippingMetadata.isNotEmpty())
    }

    @Test
    fun `WHEN generate coupon list recommendation request with trade in THEN should return with trade in true`() {
        // Given
        viewModel.isTradeIn = true
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "234",
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
                cartItemModels = listOf(CartItemModel(cartStringGroup = "234", cartStringOrder = "1"))
            )
        )

        // When
        val couponListRecommendationRequest = viewModel.generateCouponListRecommendationRequest()

        // Then
        assertEquals(1, couponListRecommendationRequest.isTradeIn)
    }

    @Test
    fun `WHEN generate coupon list recommendation request with trade in dropoff courier THEN should return with trade in dropoff courier`() {
        // Given
        viewModel.isTradeIn = true
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            selectedTabIndex = 1
        }
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "234",
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
                cartItemModels = listOf(CartItemModel(cartStringGroup = "234", cartStringOrder = "1")),
                selectedShipmentDetailData = ShipmentDetailData(selectedCourierTradeInDropOff = CourierItemData(shipperProductId = 1))
            )
        )

        // When
        val couponListRecommendationRequest = viewModel.generateCouponListRecommendationRequest()

        // Then
        assertEquals(1, couponListRecommendationRequest.isTradeIn)
        assertEquals(1, couponListRecommendationRequest.isTradeInDropOff)
        assertEquals(1, couponListRecommendationRequest.orders[0].spId)
    }

    @Test
    fun `WHEN generate coupon list recommendation request without trade in dropoff courier THEN should return without trade in dropoff courier`() {
        // Given
        viewModel.isTradeIn = true
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            selectedTabIndex = 1
        }
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "234",
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
                cartItemModels = listOf(CartItemModel(cartStringGroup = "234", cartStringOrder = "1")),
                selectedShipmentDetailData = ShipmentDetailData()
            )
        )

        // When
        val couponListRecommendationRequest = viewModel.generateCouponListRecommendationRequest()

        // Then
        assertEquals(1, couponListRecommendationRequest.isTradeIn)
        assertEquals(1, couponListRecommendationRequest.isTradeInDropOff)
        assertEquals(0, couponListRecommendationRequest.orders[0].spId)
    }

    @Test
    fun `WHEN generate coupon list recommendation request ocs THEN should return cart type ocs`() {
        // Given
        viewModel.isOneClickShipment = true
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "234",
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
                cartItemModels = listOf(CartItemModel(cartStringGroup = "234", cartStringOrder = "1")),
                selectedShipmentDetailData = ShipmentDetailData()
            )
        )

        // When
        val couponListRecommendationRequest = viewModel.generateCouponListRecommendationRequest()

        // Then
        assertEquals("ocs", couponListRecommendationRequest.cartType)
    }

    @Test
    fun `WHEN generate coupon list recommendation request with last apply THEN should return with global promo codes`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "234",
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
                cartItemModels = listOf(CartItemModel(cartStringGroup = "234", cartStringOrder = "1")),
                selectedShipmentDetailData = ShipmentDetailData()
            )
        )
        val codes = listOf("code")
        viewModel.lastApplyData.value = LastApplyUiModel(codes = codes)

        // When
        val couponListRecommendationRequest = viewModel.generateCouponListRecommendationRequest()

        // Then
        assertEquals(codes, couponListRecommendationRequest.codes)
    }

    @Test
    fun `WHEN generate coupon list recommendation request with insurance THEN should return with insurance price true`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemTopModel(
                cartStringGroup = "234"
            ),
            ShipmentCartItemModel(
                cartStringGroup = "234",
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
                cartItemModels = listOf(CartItemModel(cartStringGroup = "234", cartStringOrder = "1")),
                selectedShipmentDetailData = ShipmentDetailData(),
                isInsurance = true
            )
        )

        // When
        val couponListRecommendationRequest = viewModel.generateCouponListRecommendationRequest()

        // Then
        assertEquals(1, couponListRecommendationRequest.orders.first().isInsurancePrice)
    }
}
