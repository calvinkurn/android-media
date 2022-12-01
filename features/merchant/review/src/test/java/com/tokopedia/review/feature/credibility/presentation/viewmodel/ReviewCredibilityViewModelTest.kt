package com.tokopedia.review.feature.credibility.presentation.viewmodel

import android.os.Bundle
import com.tokopedia.review.feature.createreputation.domain.RequestState
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityAchievementBoxUiState
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityFooterUiState
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityGlobalErrorUiState
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityHeaderUiState
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityStatisticBoxUiState
import com.tokopedia.review.utils.assertInstanceOf
import com.tokopedia.reviewcommon.extension.getSavedState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Test

class ReviewCredibilityViewModelTest : ReviewCredibilityViewModelTestFixture() {
    @Test
    fun `loadReviewCredibilityData should trigger getReviewCredibility when all UI finish transitioning`() {
        doGetReviewerCredibility()
    }

    @Test
    fun `loadReviewCredibilityData should not trigger getReviewCredibility when header UI have not finish transitioning`() {
        finishAllTransition()
        viewModel.loadReviewCredibilityData()
        viewModel.onAchievementBoxStopTransitioning()
        viewModel.onStatisticBoxStopTransitioning()
        viewModel.onFooterStopTransitioning()
        viewModel.onGlobalErrorStopTransitioning()
        coVerify(inverse = true) { getReviewerCredibilityUseCase.executeOnBackground() }
    }

    @Test
    fun `loadReviewCredibilityData should not trigger getReviewCredibility when achievement box UI have not finish transitioning`() {
        // need to enter to showed state first because achievement box only have hidden and showed state
        `reviewCredibilityAchievementBoxUiState should equal to ReviewCredibilityAchievementBoxUiState#Showed when getReviewCredibilityResult is RequestState#Success and achievements not empty`()
        viewModel.loadReviewCredibilityData()
        viewModel.onHeaderStopTransitioning()
        viewModel.onStatisticBoxStopTransitioning()
        viewModel.onFooterStopTransitioning()
        viewModel.onGlobalErrorStopTransitioning()
        coVerify(exactly = 1) { getReviewerCredibilityUseCase.executeOnBackground() }
    }

    @Test
    fun `loadReviewCredibilityData should not trigger getReviewCredibility when statistic box UI have not finish transitioning`() {
        finishAllTransition()
        viewModel.loadReviewCredibilityData()
        viewModel.onHeaderStopTransitioning()
        viewModel.onAchievementBoxStopTransitioning()
        viewModel.onFooterStopTransitioning()
        viewModel.onGlobalErrorStopTransitioning()
        coVerify(inverse = true) { getReviewerCredibilityUseCase.executeOnBackground() }
    }

    @Test
    fun `loadReviewCredibilityData should not trigger getReviewCredibility when footer UI have not finish transitioning`() {
        finishAllTransition()
        viewModel.loadReviewCredibilityData()
        viewModel.onHeaderStopTransitioning()
        viewModel.onAchievementBoxStopTransitioning()
        viewModel.onStatisticBoxStopTransitioning()
        viewModel.onGlobalErrorStopTransitioning()
        coVerify(inverse = true) { getReviewerCredibilityUseCase.executeOnBackground() }
    }

    @Test
    fun `loadReviewCredibilityData should not trigger getReviewCredibility when global error UI have not finish transitioning`() {
        // need to enter to showed state first because achievement box only have hidden and showed state
        `reviewCredibilityGlobalErrorUiState should equal to ReviewCredibilityGlobalErrorUiState#Showed when getReviewCredibilityResult is RequestState#Error`()
        viewModel.loadReviewCredibilityData()
        viewModel.onHeaderStopTransitioning()
        viewModel.onAchievementBoxStopTransitioning()
        viewModel.onStatisticBoxStopTransitioning()
        viewModel.onFooterStopTransitioning()
        coVerify(exactly = 1) { getReviewerCredibilityUseCase.executeOnBackground() }
    }

    @Test
    fun `reviewCredibilityHeaderUiState should equal to ReviewCredibilityHeaderUiState#Loading when shouldLoadReviewCredibilityData is true`() {
        finishAllTransition()
        viewModel.loadReviewCredibilityData()
        assertInstanceOf<ReviewCredibilityHeaderUiState.Loading>(viewModel.reviewCredibilityHeaderUiState.value)
    }

    @Test
    fun `reviewCredibilityAchievementBoxUiState should equal to ReviewCredibilityAchievementBoxUiState#Hidden when shouldLoadReviewCredibilityData is true`() {
        finishAllTransition()
        viewModel.loadReviewCredibilityData()
        assertInstanceOf<ReviewCredibilityAchievementBoxUiState.Hidden>(viewModel.reviewCredibilityAchievementBoxUiState.value)
    }

    @Test
    fun `reviewCredibilityStatisticBoxUiState should equal to ReviewCredibilityStatisticBoxUiState#Loading when shouldLoadReviewCredibilityData is true`() {
        finishAllTransition()
        viewModel.loadReviewCredibilityData()
        assertInstanceOf<ReviewCredibilityStatisticBoxUiState.Loading>(viewModel.reviewCredibilityStatisticBoxUiState.value)
    }

    @Test
    fun `reviewCredibilityFooterUiState should equal to ReviewCredibilityFooterUiState#Loading when shouldLoadReviewCredibilityData is true`() {
        finishAllTransition()
        viewModel.loadReviewCredibilityData()
        assertInstanceOf<ReviewCredibilityFooterUiState.Loading>(viewModel.reviewCredibilityFooterUiState.value)
    }

    @Test
    fun `reviewCredibilityGlobalErrorUiState should equal to ReviewCredibilityGlobalErrorUiState#Hidden when shouldLoadReviewCredibilityData is true`() {
        finishAllTransition()
        viewModel.loadReviewCredibilityData()
        assertInstanceOf<ReviewCredibilityGlobalErrorUiState.Hidden>(viewModel.reviewCredibilityGlobalErrorUiState.value)
    }

    @Test
    fun `reviewCredibilityHeaderUiState should equal to ReviewCredibilityHeaderUiState#Showed when getReviewCredibilityResult is RequestState#Success`() {
        coEvery {
            getReviewerCredibilityUseCase.executeOnBackground()
        } returns mockk(relaxed = true) {
            every { response } returns mockk(relaxed = true)
        }
        doGetReviewerCredibility()
        assertInstanceOf<ReviewCredibilityHeaderUiState.Showed>(viewModel.reviewCredibilityHeaderUiState.value)
    }

    @Test
    fun `reviewCredibilityAchievementBoxUiState should equal to ReviewCredibilityAchievementBoxUiState#Showed when getReviewCredibilityResult is RequestState#Success and achievements not empty`() {
        coEvery {
            getReviewerCredibilityUseCase.executeOnBackground()
        } returns mockk(relaxed = true) {
            every { response.label.achievements } returns listOf(mockk(relaxed = true))
        }
        doGetReviewerCredibility()
        assertInstanceOf<ReviewCredibilityAchievementBoxUiState.Showed>(viewModel.reviewCredibilityAchievementBoxUiState.value)
    }

    @Test
    fun `reviewCredibilityAchievementBoxUiState should equal to ReviewCredibilityAchievementBoxUiState#Hidden when getReviewCredibilityResult is RequestState#Success and achievements is empty`() {
        coEvery {
            getReviewerCredibilityUseCase.executeOnBackground()
        } returns mockk(relaxed = true)
        doGetReviewerCredibility()
        assertInstanceOf<ReviewCredibilityAchievementBoxUiState.Hidden>(viewModel.reviewCredibilityAchievementBoxUiState.value)
    }

    @Test
    fun `reviewCredibilityAchievementBoxUiState should equal to ReviewCredibilityAchievementBoxUiState#Hidden when getReviewCredibilityResult is RequestState#Success and achievements is null`() {
        coEvery {
            getReviewerCredibilityUseCase.executeOnBackground()
        } returns mockk(relaxed = true) {
            every { response.label.achievements } returns null
        }
        doGetReviewerCredibility()
        assertInstanceOf<ReviewCredibilityAchievementBoxUiState.Hidden>(viewModel.reviewCredibilityAchievementBoxUiState.value)
    }

    @Test
    fun `reviewCredibilityStatisticBoxUiState should equal to ReviewCredibilityStatisticBoxUiState#Showed when getReviewCredibilityResult is RequestState#Success and statistics not empty`() {
        coEvery {
            getReviewerCredibilityUseCase.executeOnBackground()
        } returns mockk(relaxed = true) {
            every { response.stats } returns listOf(mockk(relaxed = true))
        }
        doGetReviewerCredibility()
        assertInstanceOf<ReviewCredibilityStatisticBoxUiState.Showed>(viewModel.reviewCredibilityStatisticBoxUiState.value)
    }

    @Test
    fun `reviewCredibilityStatisticBoxUiState should equal to ReviewCredibilityStatisticBoxUiState#Hidden when getReviewCredibilityResult is RequestState#Success and statistics is empty`() {
        coEvery {
            getReviewerCredibilityUseCase.executeOnBackground()
        } returns mockk(relaxed = true)
        doGetReviewerCredibility()
        assertInstanceOf<ReviewCredibilityStatisticBoxUiState.Hidden>(viewModel.reviewCredibilityStatisticBoxUiState.value)
    }

    @Test
    fun `reviewCredibilityFooterUiState should equal to ReviewCredibilityFooterUiState#Showed when getReviewCredibilityResult is RequestState#Success`() {
        coEvery {
            getReviewerCredibilityUseCase.executeOnBackground()
        } returns mockk(relaxed = true)
        doGetReviewerCredibility()
        assertInstanceOf<ReviewCredibilityFooterUiState.Showed>(viewModel.reviewCredibilityFooterUiState.value)
    }

    @Test
    fun `reviewCredibilityGlobalErrorUiState should equal to ReviewCredibilityGlobalErrorUiState#Hidden when getReviewCredibilityResult is RequestState#Success`() {
        coEvery {
            getReviewerCredibilityUseCase.executeOnBackground()
        } returns mockk(relaxed = true)
        doGetReviewerCredibility()
        assertInstanceOf<ReviewCredibilityGlobalErrorUiState.Hidden>(viewModel.reviewCredibilityGlobalErrorUiState.value)
    }

    @Test
    fun `reviewCredibilityHeaderUiState should equal to ReviewCredibilityHeaderUiState#Hidden when getReviewCredibilityResult is RequestState#Error`() {
        coEvery { getReviewerCredibilityUseCase.executeOnBackground() } throws Throwable()
        doGetReviewerCredibility()
        assertInstanceOf<ReviewCredibilityHeaderUiState.Hidden>(viewModel.reviewCredibilityHeaderUiState.value)
    }

    @Test
    fun `reviewCredibilityAchievementBoxUiState should equal to ReviewCredibilityAchievementBoxUiState#Hidden when getReviewCredibilityResult is RequestState#Error`() {
        coEvery { getReviewerCredibilityUseCase.executeOnBackground() } throws Throwable()
        doGetReviewerCredibility()
        assertInstanceOf<ReviewCredibilityAchievementBoxUiState.Hidden>(viewModel.reviewCredibilityAchievementBoxUiState.value)
    }

    @Test
    fun `reviewCredibilityStatisticBoxUiState should equal to ReviewCredibilityStatisticBoxUiState#Hidden when getReviewCredibilityResult is RequestState#Error`() {
        coEvery { getReviewerCredibilityUseCase.executeOnBackground() } throws Throwable()
        doGetReviewerCredibility()
        assertInstanceOf<ReviewCredibilityStatisticBoxUiState.Hidden>(viewModel.reviewCredibilityStatisticBoxUiState.value)
    }

    @Test
    fun `reviewCredibilityFooterUiState should equal to ReviewCredibilityFooterUiState#Hidden when getReviewCredibilityResult is RequestState#Error`() {
        coEvery { getReviewerCredibilityUseCase.executeOnBackground() } throws Throwable()
        doGetReviewerCredibility()
        assertInstanceOf<ReviewCredibilityFooterUiState.Hidden>(viewModel.reviewCredibilityFooterUiState.value)
    }

    @Test
    fun `reviewCredibilityGlobalErrorUiState should equal to ReviewCredibilityGlobalErrorUiState#Showed when getReviewCredibilityResult is RequestState#Error`() {
        val scope = CoroutineScope(rule.dispatchers.coroutineDispatcher)
        scope.launch { viewModel.reviewCredibilityGlobalErrorUiState.collect() }
        coEvery { getReviewerCredibilityUseCase.executeOnBackground() } throws Throwable()
        doGetReviewerCredibility()
        assertInstanceOf<ReviewCredibilityGlobalErrorUiState.Showed>(viewModel.reviewCredibilityGlobalErrorUiState.value)
        scope.cancel()
    }

    @Test
    fun `isLoggedIn should return true when user is logged in`() {
        every { userSessionInterface.isLoggedIn } returns true
        Assert.assertTrue(viewModel.isLoggedIn())
    }

    @Test
    fun `isLoggedIn should return false when user is not logged in`() {
        every { userSessionInterface.isLoggedIn } returns false
        Assert.assertFalse(viewModel.isLoggedIn())
    }

    @Test
    fun `isUsersOwnCredibility should return true when userID from user session is equal to reviewer userID`() {
        val reviewerUserID = "1"
        val userSessionUserID = "1"
        every { userSessionInterface.userId } returns userSessionUserID
        viewModel.setReviewerUserID(reviewerUserID)
        Assert.assertTrue(viewModel.isUsersOwnCredibility())
    }

    @Test
    fun `isUsersOwnCredibility should return true when userID from user session is not equal to reviewer userID`() {
        val reviewerUserID = "2"
        val userSessionUserID = "1"
        every { userSessionInterface.userId } returns userSessionUserID
        viewModel.setReviewerUserID(reviewerUserID)
        Assert.assertFalse(viewModel.isUsersOwnCredibility())
    }

    @Test
    fun `getUserID should return userID from user session`() {
        val userSessionUserID = "1"
        every { userSessionInterface.userId } returns userSessionUserID
        Assert.assertEquals(userSessionUserID, viewModel.getUserID())
    }

    @Test
    fun `getUserID should return empty when user session userID is null`() {
        every { userSessionInterface.userId } returns null
        Assert.assertEquals("", viewModel.getUserID())
    }

    @Test
    fun `getProductID should return latest productID value`() {
        val productID = "1"
        viewModel.setProductID(productID)
        Assert.assertEquals(productID, viewModel.getProductID())
    }

    @Test
    fun `getReviewerUserID should return latest reviewerUserID value`() {
        val reviewerUserID = "1"
        viewModel.setReviewerUserID(reviewerUserID)
        Assert.assertEquals(reviewerUserID, viewModel.getReviewerUserID())
    }

    @Test
    fun `getSource should return latest source value`() {
        val source = "source"
        viewModel.setSource(source)
        Assert.assertEquals(source, viewModel.getSource())
    }

    @Test
    fun `getPendingAppLink should return latest pendingAppLink value`() {
        val pendingAppLink = "appLink"
        viewModel.setPendingAppLink(pendingAppLink)
        Assert.assertEquals(pendingAppLink, viewModel.getPendingAppLink())
    }

    @Test
    fun `isFromInbox should return true when source is equal to inbox`() {
        val source = "inbox"
        viewModel.setSource(source)
        Assert.assertTrue(viewModel.isFromInbox())
    }

    @Test
    fun `isFromInbox should return false when source is not equal to inbox`() {
        val source = "inboxing"
        viewModel.setSource(source)
        Assert.assertFalse(viewModel.isFromInbox())
    }
    
    @Test
    fun `saveUiState should save current state`() {
        val outState = mockk<Bundle>(relaxed = true)
        viewModel.saveUiState(outState)
        verify { outState.putSerializable("savedStateKeyGetReviewCredibilityResult", any()) }
        verify { outState.putString("savedStateKeyProductID", any()) }
        verify { outState.putString("savedStateKeyReviewerUserID", any()) }
        verify { outState.putString("savedStateKeySource", any()) }
        verify { outState.putString("savedStateKeyPendingAppLink", any()) }
        verify { outState.putBoolean("savedStateKeyShouldLoadReviewCredibilityData", any()) }
    }
    
    @Test
    fun `restoreUiState should restore latest state`() {
        val latestGetReviewerCredibilityResult = RequestState.Idle
        val latestProductID = "1"
        val latestReviewerUserID = "1"
        val latestSource = "source"
        val latestPendingAppLink = "appLink"
        val latestShouldLoadReviewCredibilityData = true
        val savedInstanceState = mockk<Bundle>(relaxed = true) {
            every {
                getSavedState("savedStateKeyGetReviewCredibilityResult", RequestState.Idle)
            } returns latestGetReviewerCredibilityResult
            every {
                getSavedState("savedStateKeyProductID", "")
            } returns latestProductID
            every {
                getSavedState("savedStateKeyReviewerUserID", "")
            } returns latestReviewerUserID
            every {
                getSavedState("savedStateKeySource", "")
            } returns latestSource
            every {
                getSavedState("savedStateKeyPendingAppLink", "")
            } returns latestPendingAppLink
            every {
                getSavedState("savedStateKeyShouldLoadReviewCredibilityData", false)
            } returns latestShouldLoadReviewCredibilityData
        }
        viewModel.restoreUiState(savedInstanceState)
        assertInstanceOf<ReviewCredibilityHeaderUiState.Loading>(viewModel.reviewCredibilityHeaderUiState.value)
        assertInstanceOf<ReviewCredibilityAchievementBoxUiState.Hidden>(viewModel.reviewCredibilityAchievementBoxUiState.value)
        assertInstanceOf<ReviewCredibilityStatisticBoxUiState.Loading>(viewModel.reviewCredibilityStatisticBoxUiState.value)
        assertInstanceOf<ReviewCredibilityFooterUiState.Loading>(viewModel.reviewCredibilityFooterUiState.value)
        assertInstanceOf<ReviewCredibilityGlobalErrorUiState.Hidden>(viewModel.reviewCredibilityGlobalErrorUiState.value)
        Assert.assertEquals(latestProductID, viewModel.getProductID())
        Assert.assertEquals(latestReviewerUserID, viewModel.getReviewerUserID())
        Assert.assertEquals(latestSource, viewModel.getSource())
        Assert.assertEquals(latestPendingAppLink, viewModel.getPendingAppLink())
    }

    private fun doGetReviewerCredibility() {
        finishAllTransition() // finish first transition to initial state
        viewModel.loadReviewCredibilityData()
        finishAllTransition() // finish first transition to allow UseCase to execute
        finishAllTransition() // finish last transition to success/error state
        coVerify { getReviewerCredibilityUseCase.executeOnBackground() }
    }

    private fun finishAllTransition() {
        viewModel.onHeaderStopTransitioning()
        viewModel.onAchievementBoxStopTransitioning()
        viewModel.onStatisticBoxStopTransitioning()
        viewModel.onFooterStopTransitioning()
        viewModel.onGlobalErrorStopTransitioning()
    }
}