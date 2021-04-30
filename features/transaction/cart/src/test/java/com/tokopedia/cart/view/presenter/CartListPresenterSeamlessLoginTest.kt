package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.view.subscriber.CartSeamlessLoginSubscriber
import io.mockk.every
import io.mockk.slot
import io.mockk.verifyOrder
import org.junit.Test

class CartListPresenterSeamlessLoginTest : CartListPresenterBaseTest() {

    @Test
    fun `WHEN redirect to lite success THEN should redirect to lite`() {
        // Given
        val url = "http://"
        val slot = slot<CartSeamlessLoginSubscriber>()
        every {
            seamlessLoginUsecase.generateSeamlessUrl(url, capture(slot))
        } answers {
            val captured = slot.captured
            captured.onUrlGenerated(url)
        }
        val adsId = "123"
        every { view.getAdsId() } returns adsId

        // When
        presenter.redirectToLite(url)

        // Then
        verifyOrder {
            view.showProgressLoading()
            view.getAdsId()
            view.hideProgressLoading()
            view.goToLite(url)
        }
    }

    @Test
    fun `WHEN redirect to lite failed THEN should show error`() {
        // Given
        val url = "http://"
        val errorMessage = "error"
        val slot = slot<CartSeamlessLoginSubscriber>()
        every {
            seamlessLoginUsecase.generateSeamlessUrl(url, capture(slot))
        } answers {
            val captured = slot.captured
            captured.onError(errorMessage)
        }
        val adsId = "123"
        every { view.getAdsId() } returns adsId

        // When
        presenter.redirectToLite(url)

        // Then
        verifyOrder {
            view.showProgressLoading()
            view.getAdsId()
            view.hideProgressLoading()
            view.showToastMessageRed(errorMessage)
        }
    }

    @Test
    fun `WHEN redirect to lite with ads id is null THEN should show error`() {
        // Given
        val url = "http://"
        val adsId = null
        every { view.getAdsId() } returns adsId

        // When
        presenter.redirectToLite(url)

        // Then
        verifyOrder {
            view.showProgressLoading()
            view.getAdsId()
            view.hideProgressLoading()
            view.showToastMessageRed()
        }
    }
}