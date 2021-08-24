package com.tokopedia.review.feature.reputationhistory.view.viewmodel

import com.tokopedia.review.feature.reputationhistory.data.model.response.ReputationPenaltyAndRewardResponse
import com.tokopedia.review.feature.reputationhistory.data.model.response.ReputationShopResponse
import com.tokopedia.review.utils.observeAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import java.lang.RuntimeException

class SellerReputationViewModelTest: SellerReputationViewModelTestFixture() {

    @Test
    fun `when getReputationAndRewardPenalty should return success`() {
        runBlocking {
            val page = 1
            val reputationPenaltyRewardResponse = ReputationPenaltyAndRewardResponse()
            val reputationShopResponse = ReputationShopResponse()
            onGetReputationShopAndPenaltyRewardUseCase_thenReturn(shopId, page, startDate, endDate)
            onGetReputationShopUseCase_thenReturn(shopId, reputationShopResponse)
            onGetPenaltyRewardListUseCase_thenReturn(shopId, page, startDate, endDate, reputationPenaltyRewardResponse)
            sellerReputationViewModel.getReputationPenaltyRewardMerge()

            verifyGetReputationShopAndPenaltyRewardUseCaseCaseCalled(shopId, page, startDate, endDate)
            verifyGetReputationShopUseCaseCalled()
            verifyGetReputationPenaltyRewardUseCaseCaseCalled()
            val actualResult = (sellerReputationViewModel.reputationAndPenaltyMerge.observeAwaitValue() as Success).data
            Assert.assertTrue(sellerReputationViewModel.reputationAndPenaltyMerge.observeAwaitValue() is Success)
            Assert.assertNotNull(actualResult)
        }
    }

    @Test
    fun `when getReputationAndRewardPenalty should return Fail`() {
        runBlocking {
            val page = 1
            val exception = RuntimeException()
            onGetReputationShopAndPenaltyRewardUseCaseError_thenReturn(shopId, page, startDate, endDate, exception)

            sellerReputationViewModel.getReputationPenaltyRewardMerge()

            verifyGetReputationPenaltyRewardUseCaseCaseCalled()
            val actualResult = (sellerReputationViewModel.reputationAndPenaltyMerge.observeAwaitValue() as Fail).throwable::class
            val expectedResult = exception::class
            Assert.assertTrue(sellerReputationViewModel.reputationAndPenaltyMerge.observeAwaitValue() is Fail)
            Assert.assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when getReputationPenaltyList should return success`() {
        runBlocking {
            val page = 5
            val penaltyRewardListResponse = ReputationPenaltyAndRewardResponse()
            onGetPenaltyRewardListUseCase_thenReturn(shopId, page, startDate, endDate, penaltyRewardListResponse)

            sellerReputationViewModel.getReputationPenaltyList(page)

            verifyGetReputationPenaltyRewardUseCaseCaseCalled()
            val actualResult = (sellerReputationViewModel.reputationAndPenaltyReward.observeAwaitValue() as Success).data
            Assert.assertTrue(sellerReputationViewModel.reputationAndPenaltyReward.observeAwaitValue() is Success)
            Assert.assertNotNull(actualResult)
        }
    }

    @Test
    fun `when getReputationPenaltyList should return Fail`() {
        runBlocking {
            val page = 3
            val exception = RuntimeException()
            onGetPenaltyRewardListUseCaseError_thenReturn(exception)

            sellerReputationViewModel.getReputationPenaltyList(page)

            val actualResult = (sellerReputationViewModel.reputationAndPenaltyReward.observeAwaitValue() as Fail).throwable::class
            val expectedResult = exception::class
            Assert.assertTrue(sellerReputationViewModel.reputationAndPenaltyReward.observeAwaitValue() is Fail)
            Assert.assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when getReputationPenaltyList by date filter should return success`() {
        runBlocking {
            val page = 1
            val shopScorePenaltyDetail = ReputationPenaltyAndRewardResponse()
            onGetPenaltyRewardListUseCase_thenReturn(shopId, page, startDate, endDate, shopScorePenaltyDetail)

            val dateFilter = Pair(1234567L, 8765432L)

            sellerReputationViewModel.reputationPenaltyRewardMediator.observe( {lifecycle}) {}

            sellerReputationViewModel.setDateFilterReputationPenalty(dateFilter)

            verifyGetReputationPenaltyRewardUseCaseCaseCalled()
            val actualResult = (sellerReputationViewModel.reputationAndPenaltyReward.observeAwaitValue() as Success).data
            Assert.assertTrue(sellerReputationViewModel.reputationAndPenaltyReward.observeAwaitValue() is Success)
            Assert.assertNotNull(actualResult)
        }
    }
}