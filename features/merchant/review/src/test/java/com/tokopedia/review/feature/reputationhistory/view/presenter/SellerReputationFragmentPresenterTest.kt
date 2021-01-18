package com.tokopedia.review.feature.reputationhistory.view.presenter

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel
import com.tokopedia.review.feature.reputationhistory.domain.model.SellerReputationDomain
import com.tokopedia.review.feature.reputationhistory.util.NetworkStatus
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong
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
            presenter.resetHitNetwork()
        }

        presenter.formatDate(anyLong())
        presenter.setStartDate(anyLong())
        presenter.setEndDate(anyLong())
        presenter.incrementPage()
        presenter.networkStatus = NetworkStatus.LOADMORE
        presenter.loadMoreNetworkCall()

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedResponse)
        testSubscriber.assertCompleted()
        verifyReviewReputationUseCaseCalled()
    }

    @Test
    fun `when loadMoreNetworkCall network status return false should not execute expected usecase`() {
        presenter.formatDate(anyLong())
        presenter.setStartDate(anyLong())
        presenter.setEndDate(anyLong())
        presenter.incrementPage()
        presenter.networkStatus = NetworkStatus.NONETWORKCALL
        presenter.loadMoreNetworkCall()

        verify {
            reviewReputationUseCase wasNot Called
        }
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

        presenter.formatDate(anyLong())
        presenter.setStartDate(anyLong())
        presenter.setEndDate(anyLong())
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
            presenter.resetHitNetwork()
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

    @Test
    fun `when firstTimeNetworkCall2 network status return false should not execute expected usecase`() {
        presenter.resetPage()
        presenter.networkStatus = NetworkStatus.NONETWORKCALL
        presenter.firstTimeNetworkCall2()

        verify {
            reviewReputationMergeUseCase wasNot Called
        }
    }

    private fun verifyReviewReputationUseCaseCalled() {
        verify { reviewReputationUseCase.execute(any(), any()) }
    }

    private fun verifyReviewReputationMergeUseCaseCalled() {
        verify { reviewReputationMergeUseCase.execute(any(), any()) }
    }
}