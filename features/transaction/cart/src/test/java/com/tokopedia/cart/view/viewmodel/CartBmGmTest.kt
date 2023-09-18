package com.tokopedia.cart.view.viewmodel

import com.tokopedia.cartrevamp.domain.model.bmgm.request.BmGmGetGroupProductTickerParams
import com.tokopedia.cartrevamp.domain.model.bmgm.response.BmGmGetGroupProductTickerResponse
import com.tokopedia.cartrevamp.view.uimodel.GetBmGmGroupProductTickerState
import com.tokopedia.network.exception.ResponseErrorException
import io.mockk.coEvery
import org.junit.Assert.assertEquals
import org.junit.Test

class CartBmGmTest : BaseCartViewModelTest() {

    @Test
    fun `WHEN getBmGmGroupProductTicker success THEN should render ticker success`() {
        // GIVEN
        val valueOfferId = 1L
        val bmGmData = BmGmGetGroupProductTickerResponse()

        coEvery { bmGmGetGroupProductTickerUseCase(any()) } returns bmGmData

        // WHEN
        cartViewModel.getBmGmGroupProductTicker(
            offerId = valueOfferId,
            params = BmGmGetGroupProductTickerParams()
        )

        // THEN
        assertEquals(GetBmGmGroupProductTickerState.Success(Pair(valueOfferId, bmGmData)), cartViewModel.bmGmGroupProductTickerState.value)
    }

    @Test
    fun `WHEN getBmGmGroupProductTicker failed THEN should render ticker error`() {
        // GIVEN
        val valueOfferId = 1L
        val exception =
            ResponseErrorException("Terjadi kesalahan pada server. Ulangi beberapa saat lagi")

        coEvery { bmGmGetGroupProductTickerUseCase(any()) } throws exception

        // WHEN
        cartViewModel.getBmGmGroupProductTicker(
            offerId = valueOfferId,
            params = BmGmGetGroupProductTickerParams()
        )

        // THEN
        assertEquals(GetBmGmGroupProductTickerState.Failed(Pair(valueOfferId, exception)), cartViewModel.bmGmGroupProductTickerState.value)
    }
}
