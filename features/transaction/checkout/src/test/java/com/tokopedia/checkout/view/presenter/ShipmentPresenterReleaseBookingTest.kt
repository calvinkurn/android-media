package com.tokopedia.checkout.view.presenter

import com.tokopedia.checkout.data.model.response.releasebookingstock.ReleaseBookingResponse
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test

class ShipmentPresenterReleaseBookingTest : BaseShipmentPresenterTest() {

    @Test
    fun `WHEN release booking THEN should hit release booking use case with first productId`() {
        // Given
        coEvery { releaseBookingUseCase(any()) } returns ReleaseBookingResponse()
        val productId = 300L
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "",
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "",
                        productId = productId
                    ),
                    CartItemModel(
                        cartStringGroup = "",
                        productId = productId + 1
                    )
                )
            )
        )

        // When
        presenter.releaseBooking()

        // Then
        coVerify { releaseBookingUseCase(productId) }
    }

    @Test
    fun `GIVEN no cart item WHEN release booking THEN should not hit release booking use case`() {
        // Given
        coEvery { releaseBookingUseCase(any()) } returns ReleaseBookingResponse()
        presenter.shipmentCartItemModelList = emptyList()

        // When
        presenter.releaseBooking()

        // Then
        coVerify(inverse = true) { releaseBookingUseCase(any()) }
    }
}
