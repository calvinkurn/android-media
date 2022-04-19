package com.tokopedia.review.feature.reputationhistory.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import com.tokopedia.review.feature.reputationhistory.data.model.response.ReputationPenaltyAndRewardResponse
import com.tokopedia.review.feature.reputationhistory.data.model.response.ReputationShopResponse
import com.tokopedia.review.feature.reputationhistory.domain.mapper.SellerReputationPenaltyMapper
import com.tokopedia.review.feature.reputationhistory.domain.usecase.GetReputationShopAndPenaltyRewardUseCase
import com.tokopedia.review.feature.reputationhistory.domain.usecase.GetReputationPenaltyRewardUseCase
import com.tokopedia.review.feature.reputationhistory.domain.usecase.GetReputationShopUseCase
import com.tokopedia.review.feature.reputationhistory.view.model.SellerReputationPenaltyMergeUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class SellerReputationViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var mapper: SellerReputationPenaltyMapper

    @RelaxedMockK
    lateinit var getReputationShopUseCase: GetReputationShopUseCase

    @RelaxedMockK
    lateinit var getReputationPenaltyRewardUseCase: GetReputationPenaltyRewardUseCase

    lateinit var getReputationShopAndPenaltyRewardUseCase: GetReputationShopAndPenaltyRewardUseCase

    lateinit var sellerReputationViewModel: SellerReputationViewModel

    protected lateinit var lifecycle: LifecycleRegistry

    protected val shopId = 12345L
    protected val startDate = "2021-01-20"
    protected val endDate = "2021-07-25"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        getReputationShopAndPenaltyRewardUseCase = GetReputationShopAndPenaltyRewardUseCase(
            getReputationShopUseCase, getReputationPenaltyRewardUseCase,
            CoroutineTestDispatchersProvider, mapper
        )
        sellerReputationViewModel = SellerReputationViewModel(
            CoroutineTestDispatchersProvider,
            userSession, getReputationShopAndPenaltyRewardUseCase,
            getReputationPenaltyRewardUseCase, mapper
        )
        lifecycle = LifecycleRegistry(mockk()).apply {
            handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        }
    }

    @After
    fun finish() {
        unmockkAll()
    }

    protected fun onGetReputationShopAndPenaltyRewardUseCase_thenReturn(
        shopId: Long, page: Int, startDate: String, endDate: String
    ) {
        val reputationMergeSuccess: Result<SellerReputationPenaltyMergeUiModel> =
            Success(SellerReputationPenaltyMergeUiModel())
        coEvery {
            getReputationShopAndPenaltyRewardUseCase.execute(
                shopId,
                page,
                startDate,
                endDate
            )
        } returns reputationMergeSuccess
    }

    protected fun onGetReputationShopAndPenaltyRewardUseCaseError_thenReturn(
        shopId: Long, page: Int, startDate: String, endDate: String,
        exception: Throwable
    ) {
        coEvery {
            getReputationShopAndPenaltyRewardUseCase.execute(shopId, page, startDate, endDate)
        } throws exception
    }

    protected fun verifyGetReputationShopUseCaseCalled() {
        coVerify { getReputationShopUseCase.executeOnBackground() }
    }

    protected fun verifyGetReputationPenaltyRewardUseCaseCaseCalled() {
        coVerify { getReputationPenaltyRewardUseCase.executeOnBackground() }
    }

    protected fun verifyGetReputationShopAndPenaltyRewardUseCaseCaseCalled(shopId: Long, page: Int, startDate: String, endDate: String) {
        coVerify { getReputationShopAndPenaltyRewardUseCase.execute(shopId, page, startDate, endDate) }
    }

    protected fun onGetPenaltyRewardListUseCase_thenReturn(shopId: Long, page: Int, startDate: String, endDate: String, reputationPenaltyRewardResponse: ReputationPenaltyAndRewardResponse) {
        coEvery { getReputationPenaltyRewardUseCase.executeOnBackground() } returns reputationPenaltyRewardResponse.data
        getReputationPenaltyRewardUseCase.setParams(shopId, page, startDate, endDate)
    }

    protected fun onGetReputationShopUseCase_thenReturn(shopId: Long, reputationShopResponse: ReputationShopResponse) {
        coEvery { getReputationShopUseCase.executeOnBackground() } returns reputationShopResponse
        getReputationShopUseCase.setParams(shopId)
    }

    protected fun onGetPenaltyRewardListUseCaseError_thenReturn(exception: Throwable) {
        coEvery { getReputationPenaltyRewardUseCase.executeOnBackground() } throws exception
    }
}