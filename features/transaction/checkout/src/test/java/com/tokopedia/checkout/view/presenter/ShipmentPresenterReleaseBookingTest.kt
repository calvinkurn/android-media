package com.tokopedia.checkout.view.presenter

import com.tokopedia.checkout.data.model.response.ReleaseBookingResponse
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import io.mockk.every
import io.mockk.verify
import org.junit.Test
import rx.Observable

class ShipmentPresenterReleaseBookingTest : BaseShipmentPresenterTest() {

    @Test
    fun `WHEN release booking THEN should hit release booking use case with first productId`() {
        // Given
        every { releaseBookingUseCase.execute(any()) } returns Observable.just(
            ReleaseBookingResponse()
        )
        val productId = 300L
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartString = "",
                cartItemModels = listOf(
                    CartItemModel(
                        cartString = "",
                        productId = productId
                    ),
                    CartItemModel(
                        cartString = "",
                        productId = productId + 1
                    )
                )
            )
        )

        // When
        presenter.releaseBooking()

        // Then
        verify { releaseBookingUseCase.execute(productId) }
    }

    @Test
    fun `GIVEN no cart item WHEN release booking THEN should not hit release booking use case`() {
        // Given
        every { releaseBookingUseCase.execute(any()) } returns Observable.just(
            ReleaseBookingResponse()
        )
        presenter.shipmentCartItemModelList = emptyList()

        // When
        presenter.releaseBooking()

        // Then
        verify(inverse = true) { releaseBookingUseCase.execute(any()) }
    }
}
