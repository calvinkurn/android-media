package com.tokopedia.tkpd.flashsale.presentation.chooseproduct

import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ChooseProductViewModelTrackerTest: ChooseProductViewModelTestFixture() {

    @Test
    fun `When onAddButtonClicked invoked, Expect tracker triggered`() = runBlocking {
        // given
        val campaignId = "1"

        // when
        viewModel.onAddButtonClicked(campaignId)

        // then
        coVerify {
            tracker.sendClickAddProductEvent(campaignId)
        }
    }

    @Test
    fun `When onCheckDetailButtonClicked invoked, Expect tracker triggered`() = runBlocking {
        // given
        val campaignId = "1"
        val productId = "1"

        // when
        viewModel.onCheckDetailButtonClicked(campaignId, productId)

        // then
        coVerify {
            tracker.sendClickDetailCheckAllIneligibleLocationOrVariantEvent(campaignId, productId)
        }
    }

}
