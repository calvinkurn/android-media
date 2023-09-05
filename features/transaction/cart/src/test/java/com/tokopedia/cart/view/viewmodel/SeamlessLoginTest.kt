package com.tokopedia.cart.view.viewmodel

import com.tokopedia.cartrevamp.view.uimodel.SeamlessLoginEvent
import com.tokopedia.seamless_login_common.subscriber.SeamlessLoginSubscriber
import io.mockk.every
import io.mockk.slot
import org.junit.Assert.assertEquals
import org.junit.Test

class SeamlessLoginTest : BaseCartViewModelTest() {

    @Test
    fun `WHEN get seamless login url success THEN should navigate to lite`() {
        // GIVEN
        val url = "http://"

        val slot = slot<SeamlessLoginSubscriber>()
        every {
            seamlessLoginUsecase.generateSeamlessUrl(url, capture(slot))
        } answers {
            val captured = slot.captured
            captured.onUrlGenerated(url)
        }
        val adsId = "123"

        // WHEN
        cartViewModel.redirectToLite(url, adsId)

        // THEN
        assertEquals(SeamlessLoginEvent.Success(url), cartViewModel.seamlessLoginEvent.value)
    }

    @Test
    fun `WHEN get seamless login url failed THEN should render error`() {
        // GIVEN
        val url = "http://"
        val errorMessage = "error"

        val slot = slot<SeamlessLoginSubscriber>()
        every {
            seamlessLoginUsecase.generateSeamlessUrl(url, capture(slot))
        } answers {
            val captured = slot.captured
            captured.onError(errorMessage)
        }
        val adsId = "123"

        // WHEN
        cartViewModel.redirectToLite(url, adsId)

        // THEN
        assertEquals(SeamlessLoginEvent.Failed(errorMessage), cartViewModel.seamlessLoginEvent.value)
    }

    @Test
    fun `WHEN get seamless login url adsId empty failed THEN should render error`() {
        // GIVEN
        val url = "http://"
        val errorMessage = "error"

        val slot = slot<SeamlessLoginSubscriber>()
        every {
            seamlessLoginUsecase.generateSeamlessUrl(url, capture(slot))
        } answers {
            val captured = slot.captured
            captured.onError(errorMessage)
        }
        val adsId = ""

        // WHEN
        cartViewModel.redirectToLite(url, adsId)

        // THEN
        assertEquals(SeamlessLoginEvent.Failed(""), cartViewModel.seamlessLoginEvent.value)
    }

    @Test
    fun `WHEN get seamless login url failed because error params THEN should render error`() {
        // GIVEN
        val url = "http://"
        val errorMessage = "error"

        val slot = slot<SeamlessLoginSubscriber>()
        every {
            seamlessLoginUsecase.generateSeamlessUrl(url, capture(slot))
        } answers {
            val captured = slot.captured
            captured.onError(errorMessage)
        }
        val adsId = "123"

        // WHEN
        cartViewModel.redirectToLite(url, adsId)

        // THEN
        assertEquals(SeamlessLoginEvent.Failed(errorMessage), cartViewModel.seamlessLoginEvent.value)
    }
}
