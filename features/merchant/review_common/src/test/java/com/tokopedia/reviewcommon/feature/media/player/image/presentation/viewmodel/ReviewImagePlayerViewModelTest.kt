package com.tokopedia.reviewcommon.feature.media.player.image.presentation.viewmodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.reviewcommon.extension.getSavedState
import com.tokopedia.reviewcommon.feature.media.player.image.presentation.uistate.ReviewImagePlayerUiState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

class ReviewImagePlayerViewModelTest: ReviewImagePlayerViewModelTestFixture() {
    @Test
    fun `setImageUri should update _imageUri`() = runBlockingTest {
        val newImageUri = "https://tokopedia.com/patrickstar.png"
        viewModel.setImageUri(newImageUri)
        Assert.assertEquals(newImageUri, viewModel.uiState.first().imageUri)
    }

    @Test
    fun `setShowSeeMore and setTotalMediaCount should update uiState to ReviewImagePlayerUiState#ShowingSeeMore`() = runBlockingTest {
        viewModel.setShowSeeMore(true)
        viewModel.setTotalMediaCount(722)
        Assert.assertTrue(viewModel.uiState.first() is ReviewImagePlayerUiState.ShowingSeeMore)
    }

    @Test
    fun `setShowSeeMore should not update uiState to ReviewImagePlayerUiState#ShowingSeeMore if _totalMediaCount is not more than zero`() = runBlockingTest {
        viewModel.setShowSeeMore(true)
        Assert.assertTrue(viewModel.uiState.first() is ReviewImagePlayerUiState.Showing)
    }

    @Test
    fun `saveState should save current states`() = runBlockingTest {
        val outState = mockk<Bundle>(relaxed = true)
        viewModel.saveState(outState)
        verify { outState.putString(ReviewImagePlayerViewModel.SAVED_STATE_IMAGE_URI, any()) }
        verify { outState.putBoolean(ReviewImagePlayerViewModel.SAVED_STATE_SHOW_SEE_MORE, any()) }
        verify { outState.putInt(ReviewImagePlayerViewModel.SAVED_STATE_TOTAL_MEDIA_COUNT, any()) }
    }

    @Test
    fun `restoreState should save current states`() = runBlockingTest {
        val latestImageUri = "https://tokopedia.com/patrickstar.png"
        val latestShowSeeMore = true
        val latestTotalMediaCount = 722
        val savedState = mockk<Bundle>(relaxed = true) {
            every {
                getSavedState(ReviewImagePlayerViewModel.SAVED_STATE_IMAGE_URI, "")
            } returns latestImageUri
            every {
                getSavedState(ReviewImagePlayerViewModel.SAVED_STATE_SHOW_SEE_MORE, false)
            } returns latestShowSeeMore
            every {
                getSavedState(ReviewImagePlayerViewModel.SAVED_STATE_TOTAL_MEDIA_COUNT, 0)
            } returns latestTotalMediaCount
        }
        viewModel.restoreSavedState(savedState)
        val currentUiState = viewModel.uiState.first()
        Assert.assertEquals(latestImageUri, currentUiState.imageUri)
        Assert.assertTrue(currentUiState is ReviewImagePlayerUiState.ShowingSeeMore)
    }

    @Test
    fun `getImpressHolder should return an ImpressHolder`() {
        Assert.assertTrue(viewModel.getImpressHolder() is ImpressHolder)
    }
}