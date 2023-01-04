package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.order.data.checkout.OrderMetadata
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.response.GetPrescriptionIdsResponse
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class OrderSummaryPageViewModelEpharmacyTest : BaseOrderSummaryPageViewModelTest() {

    @Test
    fun `Get Occ Cart Success With Hide Upload Prescription Widget`() {
        // Given
        val response = helper.orderData
        every { getOccCartUseCase.createRequestParams(any(), any(), any()) } returns emptyMap()
        coEvery { getOccCartUseCase.executeSuspend(any()) } returns response
        coEvery {
            getPrescriptionIdsUseCase.setParams(any()).executeOnBackground()
        } returns GetPrescriptionIdsResponse(null)

        // When
        orderSummaryPageViewModel.getOccCart("")

        // Then
        assertEquals(
            false,
            orderSummaryPageViewModel.uploadPrescriptionUiModel.value.showImageUpload
        )
        assertEquals(
            true,
            orderSummaryPageViewModel.uploadPrescriptionUiModel.value.prescriptionIds.isEmpty()
        )
        coVerify(inverse = true) {
            getPrescriptionIdsUseCase.setParams(any()).executeOnBackground()
        }
    }

    @Test
    fun `Get Occ Cart Success With Render Upload Prescription Widget not yet upload prescriptions`() {
        // Given
        val response = helper.orderData.copy(imageUpload = helper.imageUploadDataModel)
        every { getOccCartUseCase.createRequestParams(any(), any(), any()) } returns emptyMap()
        coEvery { getOccCartUseCase.executeSuspend(any()) } returns response
        coEvery {
            getPrescriptionIdsUseCase.setParams(any(), any()).executeOnBackground()
        } returns GetPrescriptionIdsResponse(null)

        // When
        orderSummaryPageViewModel.getOccCart("")

        // Then
        assertEquals(
            true,
            orderSummaryPageViewModel.uploadPrescriptionUiModel.value.showImageUpload
        )
        assertEquals(
            true,
            orderSummaryPageViewModel.uploadPrescriptionUiModel.value.prescriptionIds.isEmpty()
        )
        assertEquals(false, orderSummaryPageViewModel.uploadPrescriptionUiModel.value.isError)
        coVerify(exactly = 1) { getPrescriptionIdsUseCase.setParams(any(), any()).executeOnBackground() }
    }

    @Test
    fun `Get Occ Cart Success With Render Upload Prescription Widget with uploaded prescriptions`() {
        // Given
        val response = helper.orderData.copy(imageUpload = helper.imageUploadDataModel)
        every { getOccCartUseCase.createRequestParams(any(), any(), any()) } returns emptyMap()
        coEvery { getOccCartUseCase.executeSuspend(any()) } returns response
        coEvery {
            getPrescriptionIdsUseCase.setParams(any(), any()).executeOnBackground()
        } returns helper.prescriptionIdsResponse

        // When
        orderSummaryPageViewModel.getOccCart("")

        // Then
        assertEquals(
            true,
            orderSummaryPageViewModel.uploadPrescriptionUiModel.value.showImageUpload
        )
        assertEquals(false, orderSummaryPageViewModel.uploadPrescriptionUiModel.value.isError)
        assertEquals(
            helper.uploadPrescriptionUiModel.prescriptionIds.toString(),
            orderSummaryPageViewModel.uploadPrescriptionUiModel.value.prescriptionIds.toString()
        )
        assertEquals(
            helper.uploadPrescriptionUiModel.prescriptionIds.size,
            orderSummaryPageViewModel.uploadPrescriptionUiModel.value.uploadedImageCount
        )
        coVerify(exactly = 1) { getPrescriptionIdsUseCase.setParams(any(), any()).executeOnBackground() }
    }

    @Test
    fun `Get Occ Cart Success With Render Upload Prescription Widget not yet upload prescription then get one prescription id from epharmacy`() {
        // Given
        val response = helper.orderData.copy(imageUpload = helper.imageUploadDataModel)
        every { getOccCartUseCase.createRequestParams(any(), any(), any()) } returns emptyMap()
        coEvery { getOccCartUseCase.executeSuspend(any()) } returns response
        coEvery {
            getPrescriptionIdsUseCase.setParams(any(), any()).executeOnBackground()
        } returns GetPrescriptionIdsResponse(null)

        // When
        orderSummaryPageViewModel.getOccCart("")
        orderSummaryPageViewModel.updatePrescriptionIds(helper.uploadPrescriptionUiModel.prescriptionIds)

        // Then
        assertEquals(
            true,
            orderSummaryPageViewModel.uploadPrescriptionUiModel.value.showImageUpload
        )
        assertEquals(
            true,
            orderSummaryPageViewModel.uploadPrescriptionUiModel.value.prescriptionIds.isNotEmpty()
        )
        assertEquals(
            helper.uploadPrescriptionUiModel.prescriptionIds.toString(),
            orderSummaryPageViewModel.uploadPrescriptionUiModel.value.prescriptionIds.toString()
        )
        coVerify(exactly = 1) { getPrescriptionIdsUseCase.setParams(any(), any()).executeOnBackground() }
    }

    @Test
    fun `continue to payment without upload prescription with front end validation`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderPromo.value = OrderPromo(state = OccButtonState.NORMAL)
        orderSummaryPageViewModel.uploadPrescriptionUiModel.value =
            helper.uploadPrescriptionUiModel.copy(
                prescriptionIds = arrayListOf(),
                uploadedImageCount = 0,
                showImageUpload = true,
                frontEndValidation = true,
                isError = false,
            )
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null

        // When
        orderSummaryPageViewModel.finalUpdate({}, false)

        // Then
        assertEquals(
            true,
            orderSummaryPageViewModel.uploadPrescriptionUiModel.value.showImageUpload
        )
        assertEquals(true, orderSummaryPageViewModel.uploadPrescriptionUiModel.value.isError)
    }

    @Test
    fun `continue to payment without upload prescription but no front end validation`() {
        // Given
        val response = helper.orderData.copy(imageUpload = helper.imageUploadDataModel)
        every { getOccCartUseCase.createRequestParams(any(), any(), any()) } returns emptyMap()
        coEvery { getOccCartUseCase.executeSuspend(any()) } returns response
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderPromo.value = OrderPromo(state = OccButtonState.NORMAL)
        orderSummaryPageViewModel.uploadPrescriptionUiModel.value =
            helper.uploadPrescriptionUiModel.copy(
                prescriptionIds = arrayListOf(),
                uploadedImageCount = 0,
                showImageUpload = true,
                frontEndValidation = false,
                isError = false,
            )
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null

        // When
        orderSummaryPageViewModel.finalUpdate({}, false)

        // Then
        assertEquals(
            true,
            orderSummaryPageViewModel.uploadPrescriptionUiModel.value.showImageUpload
        )
        assertEquals(false, orderSummaryPageViewModel.uploadPrescriptionUiModel.value.isError)
    }

    @Test
    fun `checkout with prescriptions ensure that given prescription ids is added as order metadata param`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderPromo.value = OrderPromo(state = OccButtonState.NORMAL)
        orderSummaryPageViewModel.uploadPrescriptionUiModel.value = helper.uploadPrescriptionUiModel

        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel()
        coEvery { checkoutOccUseCase.executeSuspend(any()) } returns CheckoutOccData(
            status = STATUS_OK,
            result = CheckoutOccResult(
                success = 1,
                paymentParameter = CheckoutOccPaymentParameter(
                    redirectParam = CheckoutOccRedirectParam(
                        url = "testurl"
                    )
                )
            )
        )

        // When
        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        // Then
        assertEquals(true, isOnSuccessCalled)
        assertEquals(OccGlobalEvent.Loading, orderSummaryPageViewModel.globalEvent.value)
        coVerify(exactly = 1) {
            checkoutOccUseCase.executeSuspend(
                match {
                    it.carts.data[0].shopProducts[0].orderMetadata.firstOrNull() { orderMetadata ->
                        orderMetadata.key == OrderMetadata.PRESCRIPTION_IDS_METADATA
                    }?.value == orderSummaryPageViewModel.uploadPrescriptionUiModel.value.prescriptionIds.toString()
                }
            )
        }
        verify(exactly = 1) {
            orderSummaryAnalytics.eventClickBayarSuccess(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        }
        verify(inverse = true) {
            orderSummaryAnalytics.eventPPClickBayar(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        }
    }

    @Test
    fun `checkout without prescriptions ensure that prescription ids order metadata param not exist`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderPromo.value = OrderPromo(state = OccButtonState.NORMAL)

        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        coEvery {
            validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel()
        coEvery { checkoutOccUseCase.executeSuspend(any()) } returns CheckoutOccData(
            status = STATUS_OK,
            result = CheckoutOccResult(
                success = 1,
                paymentParameter = CheckoutOccPaymentParameter(
                    redirectParam = CheckoutOccRedirectParam(
                        url = "testurl"
                    )
                )
            )
        )

        // When
        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        // Then
        assertEquals(true, isOnSuccessCalled)
        assertEquals(OccGlobalEvent.Loading, orderSummaryPageViewModel.globalEvent.value)
        coVerify(exactly = 1) {
            checkoutOccUseCase.executeSuspend(
                match {
                    it.carts.data[0].shopProducts[0].orderMetadata.firstOrNull() { orderMetadata ->
                        orderMetadata.key == OrderMetadata.PRESCRIPTION_IDS_METADATA
                    } == null
                }
            )
        }
        verify(exactly = 1) {
            orderSummaryAnalytics.eventClickBayarSuccess(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        }
        verify(inverse = true) {
            orderSummaryAnalytics.eventPPClickBayar(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        }
    }
}
