package com.tokopedia.review.feature.reputationhistory.view.presenter

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel
import com.tokopedia.review.feature.reputationhistory.domain.model.SellerReputationDomain
import com.tokopedia.review.feature.reputationhistory.util.NetworkStatus
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import rx.observers.TestSubscriber

class SellerReputationFragmentPresenterTest : SellerReputationFragmentPresenterTestFixture() {

    @Test
    fun `when loadMoreNetworkCall success should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<SellerReputationDomain>(relaxed = true)
        val testSubscriber: TestSubscriber<SellerReputationDomain> = TestSubscriber()

        every {
            reviewReputationUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedResponse)
        }

        presenter.incrementPage()
        presenter.networkStatus = NetworkStatus.LOADMORE
        presenter.loadMoreNetworkCall()

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedResponse)
        testSubscriber.assertCompleted()
        verifyReviewReputationUseCaseCalled()
    }

    @Test
    fun `when loadMoreNetworkCall fail should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<SellerReputationDomain> = TestSubscriber()

        every {
            reviewReputationUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedResponse)
        }

        presenter.incrementPage()
        presenter.networkStatus = NetworkStatus.LOADMORE
        presenter.loadMoreNetworkCall()

        testSubscriber.assertError(expectedResponse)
        testSubscriber.assertCompleted()
        verifyReviewReputationUseCaseCalled()
    }

    @Test
    fun `when firstTimeNetworkCall2 success should execute expected usecase and perform expected view actions`() {
        val expectedResponse = listOf(ShopModel(), SellerReputationDomain())
        val testSubscriber: TestSubscriber<List<Any>> = TestSubscriber()

        every {
            reviewReputationMergeUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedResponse)
        }

        presenter.resetPage()
        presenter.networkStatus = NetworkStatus.PULLTOREFRESH
        presenter.firstTimeNetworkCall2()

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedResponse)
        testSubscriber.assertCompleted()
        verifyReviewReputationMergeUseCaseCalled()
    }

    @Test
    fun `when firstTimeNetworkCall2 fail should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<List<Object>> = TestSubscriber()

        every {
            reviewReputationMergeUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedResponse)
        }

        presenter.resetPage()
        presenter.networkStatus = NetworkStatus.PULLTOREFRESH
        presenter.firstTimeNetworkCall2()

        testSubscriber.assertError(expectedResponse)
        testSubscriber.assertCompleted()
        verifyReviewReputationMergeUseCaseCalled()
    }

    private fun verifyReviewReputationUseCaseCalled() {
        verify { reviewReputationUseCase.execute(any(), any()) }
    }

    private fun verifyReviewReputationMergeUseCaseCalled() {
        verify { reviewReputationMergeUseCase.execute(any(), any()) }
    }
}