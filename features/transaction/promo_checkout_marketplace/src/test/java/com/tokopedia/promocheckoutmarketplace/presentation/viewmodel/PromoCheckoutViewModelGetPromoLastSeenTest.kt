package com.tokopedia.promocheckoutmarketplace.presentation.viewmodel

import com.tokopedia.promocheckoutmarketplace.GetPromoLastSeenDataProvider.provideGetPromoLastSeenSuccessEmpty
import com.tokopedia.promocheckoutmarketplace.GetPromoLastSeenDataProvider.provideGetPromoLastSeenSuccessWithData
import com.tokopedia.promocheckoutmarketplace.data.response.GetPromoSuggestionResponse
import io.mockk.coEvery
import org.junit.Assert.assertNotNull
import org.junit.Test

class PromoCheckoutViewModelGetPromoLastSeenTest : BasePromoCheckoutViewModelTest() {

    @Test
    fun `WHEN get promo last seen and success THEN get promo last seen response should not be null`() {
        // given
        val response = provideGetPromoLastSeenSuccessWithData()

        coEvery { getPromoSuggestionUseCase.execute(any(), any()) } answers {
            firstArg<(GetPromoSuggestionResponse) -> Unit>().invoke(response)
        }

        // when
        viewModel.getPromoSuggestion()

        // then
        assertNotNull(viewModel.getPromoSuggestionResponse.value)
    }

    @Test
    fun `WHEN get promo last seen and success with not empty data THEN get promo last seen response state should be show promo last seen`() {
        // given
        val response = provideGetPromoLastSeenSuccessWithData()

        coEvery { getPromoSuggestionUseCase.execute(any(), any()) } answers {
            firstArg<(GetPromoSuggestionResponse) -> Unit>().invoke(response)
        }

        // when
        viewModel.getPromoSuggestion()

        // then
        assert(viewModel.getPromoSuggestionResponse.value?.state == GetPromoSuggestionAction.ACTION_SHOW)
    }

    @Test
    fun `WHEN get promo last seen and success with not empty data THEN promo last seen data should not be empty`() {
        // given
        val response = provideGetPromoLastSeenSuccessWithData()

        coEvery { getPromoSuggestionUseCase.execute(any(), any()) } answers {
            firstArg<(GetPromoSuggestionResponse) -> Unit>().invoke(response)
        }

        // when
        viewModel.getPromoSuggestion()

        // then
        assert(viewModel.getPromoSuggestionResponse.value?.data?.uiData?.promoSuggestionItemUiModelList?.isNotEmpty() == true)
    }

    @Test
    fun `WHEN get promo last seen and success with empty data THEN get promo last seen response state should not be show promo last seen`() {
        // given
        val response = provideGetPromoLastSeenSuccessEmpty()

        coEvery { getPromoSuggestionUseCase.execute(any(), any()) } answers {
            firstArg<(GetPromoSuggestionResponse) -> Unit>().invoke(response)
        }

        // when
        viewModel.getPromoSuggestion()

        // then
        assert(viewModel.getPromoSuggestionResponse.value?.state != GetPromoSuggestionAction.ACTION_SHOW)
    }
}
