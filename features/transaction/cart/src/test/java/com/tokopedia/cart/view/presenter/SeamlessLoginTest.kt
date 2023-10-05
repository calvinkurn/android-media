package com.tokopedia.cart.view.presenter

import com.tokopedia.cart.view.subscriber.CartSeamlessLoginSubscriber
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test

class SeamlessLoginTest : BaseCartTest() {

    @Test
    fun `WHEN get seamless login url success THEN should navigate to lite`() {
        // GIVEN
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

        // WHEN
        cartListPresenter.redirectToLite(url)

        // THEN
        verifyOrder {
            view.showProgressLoading()
            view.getAdsId()
            view.hideProgressLoading()
            view.goToLite(url)
        }
    }

    @Test
    fun `WHEN get seamless login url failed THEN should render error`() {
        // GIVEN
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

        // WHEN
        cartListPresenter.redirectToLite(url)

        // THEN
        verifyOrder {
            view.showProgressLoading()
            view.getAdsId()
            view.hideProgressLoading()
            view.showToastMessageRed(errorMessage)
        }
    }

    @Test
    fun `WHEN get seamless login url failed because error params THEN should render error`() {
        // GIVEN
        val url = "http://"
        val errorMessage = "error"

        val slot = slot<CartSeamlessLoginSubscriber>()
        every {
            seamlessLoginUsecase.generateSeamlessUrl(url, capture(slot))
        } answers {
            val captured = slot.captured
            captured.onError(errorMessage)
        }
        val adsId = ""
        every { view.getAdsId() } returns adsId

        // WHEN
        cartListPresenter.redirectToLite(url)

        // THEN
        verifyOrder {
            view.showProgressLoading()
            view.getAdsId()
            view.hideProgressLoading()
            view.showToastMessageRed()
        }
    }

    @Test
    fun `WHEN get seamless login url with view is detached THEN should not render view`() {
        // GIVEN
        val url = "http://"

        cartListPresenter.detachView()

        // WHEN
        cartListPresenter.redirectToLite(url)

        // THEN
        verify(inverse = true) {
            view.showProgressLoading()
        }
    }
}
