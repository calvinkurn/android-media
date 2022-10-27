package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.response.GetPrescriptionIdsResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
        assertTrue(orderSummaryPageViewModel.uploadPrescriptionUiModel.value.showImageUpload == false)
        assertTrue(orderSummaryPageViewModel.uploadPrescriptionUiModel.value.prescriptionIds.isEmpty())
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
            getPrescriptionIdsUseCase.setParams(any()).executeOnBackground()
        } returns GetPrescriptionIdsResponse(null)
        // When
        orderSummaryPageViewModel.getOccCart("")

        // Then
        assertTrue(orderSummaryPageViewModel.uploadPrescriptionUiModel.value.showImageUpload == true)
        assertTrue(orderSummaryPageViewModel.uploadPrescriptionUiModel.value.prescriptionIds.isEmpty())
        coVerify(exactly = 1) { getPrescriptionIdsUseCase.setParams(any()).executeOnBackground() }
    }

    @Test
    fun `Get Occ Cart Success With Render Upload Prescription Widget with uploaded prescriptions`() {
        // Given
        val response = helper.orderData.copy(imageUpload = helper.imageUploadDataModel)
        every { getOccCartUseCase.createRequestParams(any(), any(), any()) } returns emptyMap()
        coEvery { getOccCartUseCase.executeSuspend(any()) } returns response
        coEvery {
            getPrescriptionIdsUseCase.setParams(any()).executeOnBackground()
        } returns GetPrescriptionIdsResponse(
            detailData = GetPrescriptionIdsResponse.EPharmacyCheckoutData(
                GetPrescriptionIdsResponse.EPharmacyCheckoutData.EPharmacyPrescriptionDetailData(
                    checkoutId = null, prescriptions = listOf(
                        GetPrescriptionIdsResponse.EPharmacyCheckoutData.Prescription("123"),
                        GetPrescriptionIdsResponse.EPharmacyCheckoutData.Prescription("234"),
                        GetPrescriptionIdsResponse.EPharmacyCheckoutData.Prescription("345"),
                    )
                )
            )
        )
        // When
        orderSummaryPageViewModel.getOccCart("")

        // Then
        assertTrue(orderSummaryPageViewModel.uploadPrescriptionUiModel.value.showImageUpload == true)
        assertEquals(
            "[123, 234, 345]",
            orderSummaryPageViewModel.uploadPrescriptionUiModel.value.prescriptionIds.toString()
        )
        coVerify(exactly = 1) { getPrescriptionIdsUseCase.setParams(any()).executeOnBackground() }

    }
}
