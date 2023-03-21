package com.tokopedia.checkout.view.presenter

import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.checkout.view.helper.ShipmentScheduleDeliveryMapData
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.DEFAULT_ERROR_MESSAGE_VALIDATE_PROMO
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.AdditionalInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ClashingInfoDetailUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ErrorDetailUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoClashOptionUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoClashVoucherOrdersUiModel
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

class ShipmentPresenterValidateUseFinalTest : BaseShipmentPresenterTest() {

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
        presenter.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

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
        presenter.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

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
        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            cartString = tmpCartString
        }
        presenter.shipmentCartItemModelList = ArrayList<ShipmentCartItemModel>().apply {
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
        presenter.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

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
        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            cartString = tmpCartString
        }
        presenter.shipmentCartItemModelList = ArrayList<ShipmentCartItemModel>().apply {
            add(shipmentCartItemModel)
        }
        val message = "error"
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
        presenter.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verifyOrder {
            view.showToastError(message)
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
        presenter.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verify {
            view.updateTickerAnnouncementMessage()
            shipmentAnalyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
                tickerStatusCode
            )
            view.updateButtonPromoCheckout(promoUiModel, false)
        }
    }

    @Test
    fun `WHEN validate use success with ticker data and current ticker exist THEN should update promo button and update ticker`() {
        // Given
        presenter.tickerAnnouncementHolderData =
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
        presenter.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verify {
            view.updateTickerAnnouncementMessage()
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
        presenter.checkPromoCheckoutFinalShipment(
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
        presenter.checkPromoCheckoutFinalShipment(
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
    fun `WHEN validate use success and clashing THEN should update promo button and reload rates`() {
        // Given
        val promoUiModel = PromoUiModel(
            voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(
                    type = "logistic",
                    messageUiModel = MessageUiModel(state = "green")
                )
            ),
            clashingInfoDetailUiModel = ClashingInfoDetailUiModel(
                clashMessage = "clash message",
                clashReason = "clash reason",
                options = ArrayList<PromoClashOptionUiModel>().apply {
                    add(
                        PromoClashOptionUiModel(
                            voucherOrders = ArrayList<PromoClashVoucherOrdersUiModel>().apply {
                                add(PromoClashVoucherOrdersUiModel(code = "123"))
                            }
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
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel()

        // When
        presenter.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verifySequence {
            view.updateButtonPromoCheckout(promoUiModel, false)
            view.showLoading()
            view.setHasRunningApiCall(true)
            view.hideLoading()
            view.setHasRunningApiCall(false)
            view.showToastNormal("Ada perubahan pada promo yang kamu pakai")
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
        presenter.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verifySequence {
            view.renderErrorCheckPromoShipmentData(message)
            view.resetPromoBenefit()
            view.cancelAllCourierPromo()
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
        presenter.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verifySequence {
            view.renderErrorCheckPromoShipmentData(DEFAULT_ERROR_MESSAGE_VALIDATE_PROMO)
            view.resetPromoBenefit()
            view.cancelAllCourierPromo()
        }
    }

    @Test
    fun `WHEN validate use status get exception THEN should render error`() {
        // Given
        val message = "error"
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } throws
            ResponseErrorException()

        // When
        presenter.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verifySequence {
            view.activityContext
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
        presenter.setLatValidateUseRequest(validateUsePromoRequest)
        val message = "error"
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } throws
            AkamaiErrorException(message)
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel()

        // When
        presenter.checkPromoCheckoutFinalShipment(validateUsePromoRequest, 0, "")

        // Then
        verifySequence {
            view.showToastError(message)
            view.resetAllCourier()
            view.cancelAllCourierPromo()
            view.doResetButtonPromoCheckout()
        }

        assertEquals(
            ValidateUsePromoRequest(orders = listOf(OrdersItem())),
            presenter.lastValidateUseRequest
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
            cartString = "123",
            shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
            cartItemModels = listOf(CartItemModel())
        )
        presenter.shipmentCartItemModelList = listOf(shipmentCartItemModel)
        presenter.setScheduleDeliveryMapData(shipmentCartItemModel.cartString!!, shipmentScheduleDeliveryMapData)

        // When
        presenter.checkPromoCheckoutFinalShipment(ValidateUsePromoRequest(), 0, "")

        // Then
        verify {
            view.updateButtonPromoCheckout(promoUiModel, false)
        }
        testSubscriber.assertCompleted()
    }
}
