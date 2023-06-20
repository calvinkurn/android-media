package com.tokopedia.checkout.view.presenter

import com.tokopedia.checkout.view.helper.ShipmentScheduleDeliveryMapData
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.promocheckout.common.view.uimodel.VoucherLogisticItemUiModel
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrder
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.SuccessDataUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Assert.assertNull
import org.junit.Test
import rx.observers.TestSubscriber
import rx.subjects.PublishSubject
import java.io.IOException

class ShipmentViewModelClearPromoTest : BaseShipmentViewModelTest() {

    @Test
    fun `WHEN clear BBO promo failed THEN should not render success`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "",
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(code = "code")
            )
        )
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } throws IOException()

        // When
        viewModel.cancelAutoApplyPromoStackLogistic(
            0,
            "code",
            "",
            "",
            ShipmentCartItemModel(
                cartStringGroup = "",
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
                cartItemModels = listOf(CartItemModel(cartStringGroup = ""))
            )
        )

        // Then
        verify(inverse = true) {
            view.onSuccessClearPromoLogistic(0, any())
        }
    }

    @Test
    fun `WHEN clear BBO promo success THEN should render success`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "",
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(code = "code")
            )
        )
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )

        // When
        viewModel.cancelAutoApplyPromoStackLogistic(
            0,
            "code",
            "",
            "",
            ShipmentCartItemModel(
                cartStringGroup = "",
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
                cartItemModels = listOf(CartItemModel(cartStringGroup = ""))
            )
        )

        // Then
        verify {
            view.onSuccessClearPromoLogistic(0, any())
        }
    }

    @Test
    fun `WHEN clear BBO promo success with ticker data THEN should render success and update ticker`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "",
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(code = "code")
            )
        )
        viewModel.tickerAnnouncementHolderData.value = TickerAnnouncementHolderData("0", "", "message")

        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true,
                    tickerMessage = "message"
                )
            )

        // When
        viewModel.cancelAutoApplyPromoStackLogistic(
            0,
            "code",
            "",
            "",
            ShipmentCartItemModel(
                cartStringGroup = "",
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
                cartItemModels = listOf(CartItemModel(cartStringGroup = ""))
            )
        )

        // Then
        verifySequence {
//            view.updateTickerAnnouncementMessage()
            view.onSuccessClearPromoLogistic(0, any())
        }
    }

    @Test
    fun `WHEN clear BBO promo and it's last applied promo THEN should render success and flag last applied promo is true`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "",
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(code = "code")
            )
        )
        val promoCode = "code"
        viewModel.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = ArrayList<PromoCheckoutVoucherOrdersItemUiModel>().apply {
                    add(
                        PromoCheckoutVoucherOrdersItemUiModel(promoCode)
                    )
                }
            )
        )

        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )

        // When
        viewModel.cancelAutoApplyPromoStackLogistic(
            0,
            promoCode,
            "",
            "",
            ShipmentCartItemModel(
                cartStringGroup = "",
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
                cartItemModels = listOf(CartItemModel(cartStringGroup = ""))
            )
        )

        // Then
        verifySequence {
            view.onSuccessClearPromoLogistic(0, true)
        }
        assertNull(viewModel.validateUsePromoRevampUiModel)
    }

    @Test
    fun `WHEN clear BBO promo and still has applied merchant promo THEN should render success and flag last applied promo is false`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "",
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(code = "code")
            )
        )
        val promoCode = "code"
        viewModel.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = ArrayList<PromoCheckoutVoucherOrdersItemUiModel>().apply {
                    add(PromoCheckoutVoucherOrdersItemUiModel(promoCode))
                    add(PromoCheckoutVoucherOrdersItemUiModel("promoCode"))
                }
            )
        )

        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )

        // When
        viewModel.cancelAutoApplyPromoStackLogistic(
            0,
            promoCode,
            "",
            "",
            ShipmentCartItemModel(
                cartStringGroup = "",
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
                cartItemModels = listOf(CartItemModel(cartStringGroup = ""))
            )
        )

        // Then
        verifySequence {
            view.onSuccessClearPromoLogistic(0, false)
        }
    }

    @Test
    fun `WHEN clear BBO promo and it's last applied global promo THEN should render success and flag last applied promo is true`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "",
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(code = "code")
            )
        )
        val promoCode = "code"
        viewModel.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                codes = ArrayList<String>().apply {
                    add(promoCode)
                }
            )
        )

        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )

        // When
        viewModel.cancelAutoApplyPromoStackLogistic(
            0,
            promoCode,
            "",
            "",
            ShipmentCartItemModel(
                cartStringGroup = "",
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
                cartItemModels = listOf(CartItemModel(cartStringGroup = ""))
            )
        )

        // Then
        verifySequence {
            view.onSuccessClearPromoLogistic(0, true)
        }
        assertNull(viewModel.validateUsePromoRevampUiModel)
    }

    @Test
    fun `WHEN clear BBO promo and still has applied global promo THEN should render success and flag last applied promo is false`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "",
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(code = "code")
            )
        )
        val promoCode = "code"
        viewModel.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                codes = ArrayList<String>().apply {
                    add("promo code")
                }
            )
        )

        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )

        // When
        viewModel.cancelAutoApplyPromoStackLogistic(
            0,
            promoCode,
            "",
            "",
            ShipmentCartItemModel(
                cartStringGroup = "",
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
                cartItemModels = listOf(CartItemModel(cartStringGroup = ""))
            )
        )

        // Then
        verifySequence {
            view.onSuccessClearPromoLogistic(0, false)
        }
    }

    @Test
    fun `WHEN clear non eligible promo success THEN should render success`() {
        // Given
        val notEligilePromoList = ArrayList<NotEligiblePromoHolderdata>().apply {
            add(
                NotEligiblePromoHolderdata(
                    promoCode = "code",
                    iconType = NotEligiblePromoHolderdata.TYPE_ICON_GLOBAL
                )
            )
        }
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )

        // When
        viewModel.cancelNotEligiblePromo(notEligilePromoList)

        // Then
        verify {
            view.removeIneligiblePromo()
        }
    }

//    @Test
//    fun `WHEN clear non eligible promo by unique id success THEN should render success`() {
//        // Given
//        val notEligiblePromos = arrayListOf(
//            NotEligiblePromoHolderdata(
//                promoCode = "code",
//                iconType = -1
//            )
//        )
//        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
//            ClearPromoUiModel(
//                successDataModel = SuccessDataUiModel(
//                    success = true
//                )
//            )
//
//
//        val presenterSpy = spyk(presenter)
//        every { presenterSpy.getClearPromoOrderByUniqueId(any(), any()) } returns ClearPromoOrder(
//            uniqueId = "1"
//        )
//        presenterSpy.shipmentCartItemModelList = null
//
//        // When
//        presenterSpy.cancelNotEligiblePromo(notEligiblePromos)
//
//        // Then
//        verify {
//            view.removeIneligiblePromo(notEligiblePromos)
//        }
//    }

    @Test
    fun `WHEN clear non eligible promo from shipment cart list success THEN should render success`() {
        // Given
        val notEligiblePromos = arrayListOf(
            NotEligiblePromoHolderdata(
                cartStringGroup = "1",
                promoCode = "code",
                iconType = -1
            ),
            NotEligiblePromoHolderdata(
                cartStringGroup = "1",
                promoCode = "code2",
                iconType = -1
            )
        )
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )

        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "1",
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(boType = 1)),
                shopId = 1,
                isProductIsPreorder = false,
                cartItemModels = listOf(CartItemModel(cartStringGroup = "1", preOrderDurationDay = 10)),
                fulfillmentId = 1
            )
        )

        // When
        viewModel.cancelNotEligiblePromo(notEligiblePromos)

        // Then
        verify {
            view.removeIneligiblePromo()
        }
    }

    @Test
    fun `WHEN clear non eligible promo from shipment cart list unique id not match THEN should not update view`() {
        // Given
        val notEligiblePromos = arrayListOf(
            NotEligiblePromoHolderdata(
                uniqueId = "1",
                promoCode = "code",
                iconType = -1
            )
        )
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )

        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "2"
            )
        )

        // When
        viewModel.cancelNotEligiblePromo(notEligiblePromos)

        // Then
        verify(inverse = true) {
//            view.updateTickerAnnouncementMessage()
            view.removeIneligiblePromo()
        }
    }

    @Test
    fun `WHEN clear non eligible promo from shipment cart list empty THEN should not update view`() {
        // Given
        val notEligiblePromos = arrayListOf(
            NotEligiblePromoHolderdata(
                uniqueId = "1",
                promoCode = "code",
                iconType = -1
            )
        )
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )

        viewModel.shipmentCartItemModelList = listOf()

        // When
        viewModel.cancelNotEligiblePromo(notEligiblePromos)

        // Then
        verify(inverse = true) {
//            view.updateTickerAnnouncementMessage()
            view.removeIneligiblePromo()
        }
    }

    @Test
    fun `WHEN clear non eligible promo success with ticker data THEN should render success and update ticker`() {
        // Given
        viewModel.tickerAnnouncementHolderData.value = TickerAnnouncementHolderData("0", "", "message")

        val notEligilePromoList = ArrayList<NotEligiblePromoHolderdata>().apply {
            add(
                NotEligiblePromoHolderdata(
                    promoCode = "code",
                    iconType = NotEligiblePromoHolderdata.TYPE_ICON_GLOBAL
                )
            )
        }

        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true,
                    tickerMessage = "message"
                )
            )

        // When
        viewModel.cancelNotEligiblePromo(notEligilePromoList)

        // Then
        verifySequence {
//            view.updateTickerAnnouncementMessage()
            view.removeIneligiblePromo()
        }
    }

    @Test
    fun `WHEN clear non eligible promo failed THEN should render success`() {
        // Given
        viewModel.tickerAnnouncementHolderData.value = TickerAnnouncementHolderData("0", "", "message")

        val notEligilePromoList = ArrayList<NotEligiblePromoHolderdata>().apply {
            add(
                NotEligiblePromoHolderdata(
                    promoCode = "code",
                    iconType = NotEligiblePromoHolderdata.TYPE_ICON_GLOBAL
                )
            )
        }

        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } throws IOException()

        // When
        viewModel.cancelNotEligiblePromo(notEligilePromoList)

        // Then
        verifySequence {
//            view.updateTickerAnnouncementMessage()
            view.removeIneligiblePromo()
        }
    }

//    @Test
//    fun `WHEN clear non eligible not found THEN should not update view`() {
//        // Given
//        val notEligiblePromos = arrayListOf(
//            NotEligiblePromoHolderdata(
//                promoCode = "code",
//                iconType = -1
//            )
//        )
//        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
//            ClearPromoUiModel(
//                successDataModel = SuccessDataUiModel(
//                    success = true
//                )
//            )
//
//
//        val presenterSpy = spyk(presenter)
//        every { presenterSpy.getClearPromoOrderByUniqueId(any(), any()) } returns null
//        presenterSpy.shipmentCartItemModelList = null
//
//        // When
//        presenterSpy.cancelNotEligiblePromo(notEligiblePromos)
//
//        // Then
//        verify(inverse = true) {
//            view.updateTickerAnnouncementMessage()
//            view.removeIneligiblePromo(notEligiblePromos)
//        }
//    }

    // Test ShipmentPresenter.hitClearAllBo()

    @Test
    fun `WHEN hit clear all BO with cart list with valid voucher logistic promo THEN call clear cache auto apply use case`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "111-111-111",
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(
                    code = "TEST1"
                ),
                shipmentCartData = ShipmentCartData(
                    boMetadata = BoMetadata(
                        boType = 1
                    )
                ),
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "111-111-111",
                        preOrderDurationDay = 10
                    )
                )
            ),
            ShipmentCartItemModel(
                cartStringGroup = "222-222-222",
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(
                    code = "TEST2"
                ),
                shipmentCartData = ShipmentCartData(
                    boMetadata = BoMetadata(
                        boType = 1
                    )
                ),
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "222-222-222",
                        preOrderDurationDay = 10
                    )
                )
            )
        )

        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true,
                    tickerMessage = ""
                )
            )

        // When
        viewModel.hitClearAllBo()

        // Then
        coVerify {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
        }
    }

    @Test
    fun `GIVEN clear promo failed WHEN hit clear all BO with cart list with valid voucher logistic promo THEN call clear cache auto apply use case`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "111-111-111",
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(
                    code = "TEST1"
                ),
                shipmentCartData = ShipmentCartData(
                    boMetadata = BoMetadata(
                        boType = 1
                    )
                ),
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "111-111-111",
                        preOrderDurationDay = 10
                    )
                )
            ),
            ShipmentCartItemModel(
                cartStringGroup = "222-222-222",
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(
                    code = "TEST2"
                ),
                shipmentCartData = ShipmentCartData(
                    boMetadata = BoMetadata(
                        boType = 1
                    )
                ),
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "222-222-222",
                        preOrderDurationDay = 10
                    )
                )
            )
        )

        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } throws IOException()

        // When
        viewModel.hitClearAllBo()

        // Then
        coVerify {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
        }
    }

    @Test
    fun `WHEN hit clear all BO with invalid cart item THEN don't call clear cache auto apply use case`() {
        // Given
        // Test negative branch
        viewModel.shipmentCartItemModelList = listOf(
            // Test shipmentCartItemModel == null
//            null,
            // Test shipmentCartData == null
//            ShipmentCartItemModel(
//                cartString = "111-111-111",
//                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(code = "TEST1"),
// //                shipmentCartData = null,
//                cartItemModels = listOf(
//                    CartItemModel(
//                        cartString = "111-111-111",
//                        preOrderDurationDay = 10
//                    )
//                )
//            ),
            // Test voucherLogisticItemUiModel.code == ""
            ShipmentCartItemModel(
                cartStringGroup = "111-111-111",
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(code = ""),
                shipmentCartData = ShipmentCartData(
                    boMetadata = BoMetadata(
                        boType = 1
                    )
                ),
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "111-111-111",
                        preOrderDurationDay = 10
                    )
                )
            ),
            // Test voucherLogisticItemUiModel == null
            ShipmentCartItemModel(
                cartStringGroup = "111-111-111",
                voucherLogisticItemUiModel = null,
                shipmentCartData = ShipmentCartData(
                    boMetadata = BoMetadata(
                        boType = 1
                    )
                ),
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "111-111-111",
                        preOrderDurationDay = 10
                    )
                )
            )
        )

        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true,
                    tickerMessage = ""
                )
            )

        // When
        viewModel.hitClearAllBo()

        // Then
        coVerify(inverse = true) {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
        }
    }

    // Test ShipmentPresenter.getClearPromoOrderByUniqueId(...)

    @Test
    fun `WHEN get clear promo order unique id with valid unique id THEN promo order should not be null`() {
        // Given
        val clearPromoOrders = arrayListOf(
            ClearPromoOrder(uniqueId = "111-111-111"),
            ClearPromoOrder(uniqueId = "222-222-222"),
            ClearPromoOrder(uniqueId = "333-333-333")
        )
        val uniqueId = "111-111-111"

        // When
        val result = viewModel.getClearPromoOrderByUniqueId(clearPromoOrders, uniqueId)

        // Then
        assert(result != null)
        assert(result?.uniqueId == uniqueId)
    }

    @Test
    fun `WHEN get clear promo order unique id with invalid unique id THEN promo order should be null`() {
        // Given
        val clearPromoOrders = arrayListOf(
            ClearPromoOrder(uniqueId = "222-222-222"),
            ClearPromoOrder(uniqueId = "333-333-333")
        )
        val uniqueId = "111-111-111"

        // When
        val result = viewModel.getClearPromoOrderByUniqueId(clearPromoOrders, uniqueId)

        // Then
        assert(result == null)
    }

    @Test
    fun `WHEN get clear promo order unique id with empty clear promo list THEN promo order should be null`() {
        // Given
        val clearPromoOrders = arrayListOf<ClearPromoOrder>()
        val uniqueId = "111-111-111"

        // When
        val result = viewModel.getClearPromoOrderByUniqueId(clearPromoOrders, uniqueId)

        // Then
        assert(result == null)
    }

    @Test
    fun `WHEN get clear promo order unique id with null unique id THEN promo order should be null`() {
        // Given
        val clearPromoOrders = arrayListOf(
            ClearPromoOrder(uniqueId = "222-222-222"),
            ClearPromoOrder(uniqueId = "333-333-333")
        )
        val uniqueId = ""

        // When
        val result = viewModel.getClearPromoOrderByUniqueId(clearPromoOrders, uniqueId)

        // Then
        assert(result == null)
    }

    @Test
    fun `WHEN clear BBO promo success from schedule delivery THEN should complete publisher`() {
        // Given
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )

        val testSubscriber = TestSubscriber.create<Boolean>()
        val donePublisher = PublishSubject.create<Boolean>()
        donePublisher.subscribe(testSubscriber)
        val shipmentScheduleDeliveryMapData = ShipmentScheduleDeliveryMapData(
            donePublisher,
            shouldStopInClearCache = true,
            shouldStopInValidateUsePromo = false
        )
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartStringGroup = "123",
            shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
            cartItemModels = listOf(CartItemModel(cartStringGroup = "123")),
            voucherLogisticItemUiModel = VoucherLogisticItemUiModel(code = "code")
        )
        viewModel.setScheduleDeliveryMapData(shipmentCartItemModel.cartStringGroup, shipmentScheduleDeliveryMapData)
        viewModel.shipmentCartItemModelList = listOf(shipmentCartItemModel)

        // When
        viewModel.cancelAutoApplyPromoStackLogistic(
            0,
            "code",
            "123",
            "",
            shipmentCartItemModel
        )

        // Then
        verify {
            view.onSuccessClearPromoLogistic(0, any())
        }
        testSubscriber.assertCompleted()
    }

    @Test
    fun `WHEN clear BBO promo success from schedule delivery but with invalid voucher logistic code THEN should directly complete publisher`() {
        // Given
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )

        val testSubscriber = TestSubscriber.create<Boolean>()
        val donePublisher = PublishSubject.create<Boolean>()
        donePublisher.subscribe(testSubscriber)
        val shipmentScheduleDeliveryMapData = ShipmentScheduleDeliveryMapData(
            donePublisher,
            shouldStopInClearCache = true,
            shouldStopInValidateUsePromo = false
        )
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartStringGroup = "123",
            shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
            cartItemModels = listOf(CartItemModel(cartStringGroup = "123"))
        )
        viewModel.setScheduleDeliveryMapData(shipmentCartItemModel.cartStringGroup, shipmentScheduleDeliveryMapData)
        viewModel.shipmentCartItemModelList = listOf(shipmentCartItemModel)

        // When
        viewModel.cancelAutoApplyPromoStackLogistic(
            0,
            "code",
            "123",
            "",
            shipmentCartItemModel
        )

        // Then
        verify {
            view.onSuccessClearPromoLogistic(0, any())
        }
        testSubscriber.assertCompleted()
    }

    @Test
    fun `WHEN clear BBO promo failed from schedule delivery THEN should complete publisher`() {
        // Given
        val errorMessage = "error"
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } throws Throwable(errorMessage)

        val testSubscriber = TestSubscriber.create<Boolean>()
        val donePublisher = PublishSubject.create<Boolean>()
        donePublisher.subscribe(testSubscriber)
        val shipmentScheduleDeliveryMapData = ShipmentScheduleDeliveryMapData(
            donePublisher,
            shouldStopInClearCache = true,
            shouldStopInValidateUsePromo = false
        )
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartStringGroup = "123",
            shipmentCartData = ShipmentCartData(boMetadata = BoMetadata(1)),
            cartItemModels = listOf(CartItemModel(cartStringGroup = "123")),
            voucherLogisticItemUiModel = VoucherLogisticItemUiModel(code = "code")
        )
        viewModel.setScheduleDeliveryMapData(shipmentCartItemModel.cartStringGroup, shipmentScheduleDeliveryMapData)
        viewModel.shipmentCartItemModelList = listOf(shipmentCartItemModel)

        // When
        viewModel.cancelAutoApplyPromoStackLogistic(
            0,
            "code",
            "123",
            "",
            shipmentCartItemModel
        )

        // Then
        testSubscriber.assertCompleted()
    }
}
