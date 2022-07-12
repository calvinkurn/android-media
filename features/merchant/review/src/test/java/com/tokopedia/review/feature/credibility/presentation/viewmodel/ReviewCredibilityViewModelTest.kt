package com.tokopedia.review.feature.credibility.presentation.viewmodel

import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityAchievementBoxUiState
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityFooterUiState
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityGlobalErrorUiState
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityHeaderUiState
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityStatisticBoxUiState
import io.mockk.coVerify
import org.junit.Assert
import org.junit.Test

class ReviewCredibilityViewModelTest : ReviewCredibilityViewModelTestFixture() {
    @Test
    fun `loadReviewCredibilityData should trigger getReviewCredibility when all UI finish transitioning`() {
        viewModel.loadReviewCredibilityData()
        viewModel.onHeaderStopTransitioning()
        viewModel.onAchievementBoxStopTransitioning()
        viewModel.onStatisticBoxStopTransitioning()
        viewModel.onFooterStopTransitioning()
        coVerify { getReviewerCredibilityUseCase.executeOnBackground() }
    }

    @Test
    fun `loadReviewCredibilityData should not trigger getReviewCredibility when header UI have not finish transitioning`() {
        viewModel.loadReviewCredibilityData()
        viewModel.onAchievementBoxStopTransitioning()
        viewModel.onStatisticBoxStopTransitioning()
        viewModel.onFooterStopTransitioning()
        coVerify(inverse = true) { getReviewerCredibilityUseCase.executeOnBackground() }
    }

    @Test
    fun `loadReviewCredibilityData should not trigger getReviewCredibility when achievement box UI have not finish transitioning`() {
        viewModel.loadReviewCredibilityData()
        viewModel.onHeaderStopTransitioning()
        viewModel.onStatisticBoxStopTransitioning()
        viewModel.onFooterStopTransitioning()
        coVerify(inverse = true) { getReviewerCredibilityUseCase.executeOnBackground() }
    }

    @Test
    fun `loadReviewCredibilityData should not trigger getReviewCredibility when statistic box UI have not finish transitioning`() {
        viewModel.loadReviewCredibilityData()
        viewModel.onHeaderStopTransitioning()
        viewModel.onAchievementBoxStopTransitioning()
        viewModel.onFooterStopTransitioning()
        coVerify(inverse = true) { getReviewerCredibilityUseCase.executeOnBackground() }
    }

    @Test
    fun `loadReviewCredibilityData should not trigger getReviewCredibility when footer UI have not finish transitioning`() {
        viewModel.loadReviewCredibilityData()
        viewModel.onHeaderStopTransitioning()
        viewModel.onAchievementBoxStopTransitioning()
        viewModel.onStatisticBoxStopTransitioning()
        coVerify(inverse = true) { getReviewerCredibilityUseCase.executeOnBackground() }
    }

    @Test
    fun `reviewCredibilityHeaderUiState should equal to ReviewCredibilityHeaderUiState#Loading when shouldLoadReviewCredibilityData is true`() {
        viewModel.loadReviewCredibilityData()
        Assert.assertEquals(ReviewCredibilityHeaderUiState.Loading, viewModel.reviewCredibilityHeaderUiState.value)
    }

    @Test
    fun `reviewCredibilityAchievementBoxUiState should equal to ReviewCredibilityAchievementBoxUiState#Hidden when shouldLoadReviewCredibilityData is true`() {
        viewModel.loadReviewCredibilityData()
        Assert.assertEquals(ReviewCredibilityAchievementBoxUiState.Hidden, viewModel.reviewCredibilityAchievementBoxUiState.value)
    }

    @Test
    fun `reviewCredibilityStatisticBoxUiState should equal to ReviewCredibilityStatisticBoxUiState#Loading when shouldLoadReviewCredibilityData is true`() {
        viewModel.loadReviewCredibilityData()
        Assert.assertEquals(ReviewCredibilityStatisticBoxUiState.Loading, viewModel.reviewCredibilityStatisticBoxUiState.value)
    }

    @Test
    fun `reviewCredibilityFooterUiState should equal to ReviewCredibilityFooterUiState#Loading when shouldLoadReviewCredibilityData is true`() {
        viewModel.loadReviewCredibilityData()
        Assert.assertEquals(ReviewCredibilityFooterUiState.Loading, viewModel.reviewCredibilityFooterUiState.value)
    }

    @Test
    fun `reviewCredibilityGlobalErrorUiState should equal to ReviewCredibilityGlobalErrorUiState#Hidden when shouldLoadReviewCredibilityData is true`() {
        viewModel.loadReviewCredibilityData()
        Assert.assertEquals(ReviewCredibilityGlobalErrorUiState.Hidden, viewModel.reviewCredibilityGlobalErrorUiState.value)
    }
}