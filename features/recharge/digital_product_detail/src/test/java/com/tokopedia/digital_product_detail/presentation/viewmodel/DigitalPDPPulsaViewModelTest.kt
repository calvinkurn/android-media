package com.tokopedia.digital_product_detail.presentation.viewmodel

import com.tokopedia.digital_product_detail.presentation.data.PulsaDataFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test


@ExperimentalCoroutinesApi
class DigitalPDPPulsaViewModelTest: DigitalPDPViewModelTestFixture() {

    private val dataFactory = PulsaDataFactory()

    @Test
    fun `when getting favoriteNumber should run and give success result`() {
        val response = dataFactory.getFavoriteNumberData()
        onGetFavoriteNumber_thenReturn(response)

        viewModel.getFavoriteNumber(listOf())
        verifyGetFavoriteNumberSuccess(response.persoFavoriteNumber.items)
    }

    @Test
    fun `when getting catalogPrefixSelect should run and give success result`() {
        val response = dataFactory.getPrefixOperatorData()
        onGetPrefixOperator_thenReturn(response)

        viewModel.getPrefixOperator(MENU_ID)
        verifyGetPrefixOperatorSuccess(response)
    }

    companion object {
        const val MENU_ID = 289
    }
}