package com.tokopedia.review.feature.inbox.buyerreview.view.presenter

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import rx.observers.TestSubscriber

class InboxReputationPresenterTest : InboxReputationPresenterTestFixture() {

    @Test
    fun `when getFirstTimeInboxReputation success should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<InboxReputationDomain>(relaxed = true)
        val testSubscriber: TestSubscriber<InboxReputationDomain> = TestSubscriber()

        every {
            getFirstTimeInboxReputationUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedResponse)
        }

        inboxReputationPresenter.getFirstTimeInboxReputation(anyInt())

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedResponse)
        testSubscriber.assertCompleted()
        verifyGetFirstTimeInboxReputationUseCaseExecuted()
    }

    @Test
    fun `when getFirstTimeInboxReputation fail should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<InboxReputationDomain> = TestSubscriber()

        every {
            getFirstTimeInboxReputationUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedResponse)
        }

        inboxReputationPresenter.getFirstTimeInboxReputation(anyInt())

        testSubscriber.assertError(expectedResponse)
        testSubscriber.assertCompleted()
        verifyGetFirstTimeInboxReputationUseCaseExecuted()
    }

    @Test
    fun `when getNextPage success should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<InboxReputationDomain>(relaxed = true)
        val testSubscriber: TestSubscriber<InboxReputationDomain> = TestSubscriber()

        every {
            getInboxReputationUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedResponse)
        }

        inboxReputationPresenter.setHasNextPage(true)
        inboxReputationPresenter.getNextPage(anyInt(), anyInt(), anyString(), anyString(), anyString(), anyInt())

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedResponse)
        testSubscriber.assertCompleted()
        verifyGetInboxReputationUseCaseExecuted()
    }

    @Test
    fun `when getNextPage fail should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<InboxReputationDomain> = TestSubscriber()

        every {
            getInboxReputationUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedResponse)
        }

        inboxReputationPresenter.setHasNextPage(true)
        inboxReputationPresenter.getNextPage(anyInt(), anyInt(), anyString(), anyString(), anyString(), anyInt())

        testSubscriber.assertError(expectedResponse)
        testSubscriber.assertCompleted()
        verifyGetInboxReputationUseCaseExecuted()
    }

    @Test
    fun `when getFilteredInboxReputation success should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<InboxReputationDomain>(relaxed = true)
        val testSubscriber: TestSubscriber<InboxReputationDomain> = TestSubscriber()

        every {
            getInboxReputationUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedResponse)
        }

        inboxReputationPresenter.getFilteredInboxReputation(anyString(), anyString(), anyString(), anyInt())

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedResponse)
        testSubscriber.assertCompleted()
        verifyGetInboxReputationUseCaseExecuted()
    }

    @Test
    fun `when getFilteredInboxReputation fail should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<InboxReputationDomain> = TestSubscriber()

        every {
            getInboxReputationUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedResponse)
        }

        inboxReputationPresenter.getFilteredInboxReputation(anyString(), anyString(), anyString(), anyInt())

        testSubscriber.assertError(expectedResponse)
        testSubscriber.assertCompleted()
        verifyGetInboxReputationUseCaseExecuted()
    }

    @Test
    fun `when refreshPage success should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<InboxReputationDomain>(relaxed = true)
        val testSubscriber: TestSubscriber<InboxReputationDomain> = TestSubscriber()

        every {
            getInboxReputationUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedResponse)
        }

        inboxReputationPresenter.refreshPage(anyString(), anyString(), anyString(), anyInt())

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedResponse)
        testSubscriber.assertCompleted()
        verifyGetInboxReputationUseCaseExecuted()
    }

    @Test
    fun `when refreshPage fail should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<InboxReputationDomain> = TestSubscriber()

        every {
            getInboxReputationUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedResponse)
        }

        inboxReputationPresenter.refreshPage(anyString(), anyString(), anyString(), anyInt())

        testSubscriber.assertError(expectedResponse)
        testSubscriber.assertCompleted()
        verifyGetInboxReputationUseCaseExecuted()
    }

    private fun verifyGetFirstTimeInboxReputationUseCaseExecuted() {
        verify { getFirstTimeInboxReputationUseCase.execute(any(), any()) }
    }

    private fun verifyGetInboxReputationUseCaseExecuted() {
        verify { getInboxReputationUseCase.execute(any(), any()) }
    }
}