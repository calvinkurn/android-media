package com.tokopedia.review.feature.inbox.buyerreview.view.presenter

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.InboxReputationDetailDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendSmileyReputationDomain
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import rx.observers.TestSubscriber

class InboxReputationDetailPresenterTest : InboxReputationDetailPresenterTestFixture() {

    @Test
    fun `when getInboxDetail success should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<InboxReputationDetailDomain>(relaxed = true)
        val testSubscriber: TestSubscriber<InboxReputationDetailDomain> = TestSubscriber()

        every {
            getInboxReputationDetailUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedResponse)
        }

        presenter.getInboxDetail(anyString(), anyInt())

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedResponse)
        testSubscriber.assertCompleted()
        verifyGetInboxReputationDetailUseCaseCalled()
    }

    @Test
    fun `when getInboxDetail but view is not attached success should execute expected usecase`() {
        val expectedResponse = mockk<InboxReputationDetailDomain>(relaxed = true)
        val testSubscriber: TestSubscriber<InboxReputationDetailDomain> = TestSubscriber()

        every {
            getInboxReputationDetailUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedResponse)
        }

        presenter.attachView(null)
        presenter.getInboxDetail(anyString(), anyInt())

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedResponse)
        testSubscriber.assertCompleted()
        verifyGetInboxReputationDetailUseCaseCalled()
    }

    @Test
    fun `when getInboxDetail fail should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<InboxReputationDetailDomain> = TestSubscriber()

        every {
            getInboxReputationDetailUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedResponse)
        }

        presenter.getInboxDetail(anyString(), anyInt())

        testSubscriber.assertError(expectedResponse)
        testSubscriber.assertCompleted()
        verifyGetInboxReputationDetailUseCaseCalled()
    }

    @Test
    fun `when sendSmiley success should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<SendSmileyReputationDomain>(relaxed = true)
        val testSubscriber: TestSubscriber<SendSmileyReputationDomain> = TestSubscriber()

        every {
            sendSmileyReputationUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedResponse)
        }

        presenter.sendSmiley(anyString(), anyString(), anyString())

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedResponse)
        testSubscriber.assertCompleted()
        verifySendSmileyReputationUseCaseCalled()
    }

    @Test
    fun `when sendSmiley but view is not attached success should execute expected usecase`() {
        val expectedResponse = mockk<SendSmileyReputationDomain>(relaxed = true)
        val testSubscriber: TestSubscriber<SendSmileyReputationDomain> = TestSubscriber()

        every {
            sendSmileyReputationUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedResponse)
        }

        presenter.attachView(null)
        presenter.sendSmiley(anyString(), anyString(), anyString())

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedResponse)
        testSubscriber.assertCompleted()
        verifySendSmileyReputationUseCaseCalled()
    }

    @Test
    fun `when sendSmiley fail should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<SendSmileyReputationDomain> = TestSubscriber()

        every {
            sendSmileyReputationUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedResponse)
        }

        presenter.attachView(view)
        presenter.sendSmiley(anyString(), anyString(), anyString())

        testSubscriber.assertError(expectedResponse)
        testSubscriber.assertCompleted()
        verifySendSmileyReputationUseCaseCalled()
    }

    @Test
    fun `when refreshPage success should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<InboxReputationDetailDomain>(relaxed = true)
        val testSubscriber: TestSubscriber<InboxReputationDetailDomain> = TestSubscriber()

        every {
            getInboxReputationDetailUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedResponse)
        }

        presenter.refreshPage(anyString(), anyInt())

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedResponse)
        testSubscriber.assertCompleted()
        verifyGetInboxReputationDetailUseCaseCalled()
    }

    @Test
    fun `when refreshPage but view is not attached success should execute expected usecase`() {
        val expectedResponse = mockk<InboxReputationDetailDomain>(relaxed = true)
        val testSubscriber: TestSubscriber<InboxReputationDetailDomain> = TestSubscriber()

        every {
            getInboxReputationDetailUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedResponse)
        }

        presenter.attachView(null)
        presenter.refreshPage(anyString(), anyInt())

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedResponse)
        testSubscriber.assertCompleted()
        verifyGetInboxReputationDetailUseCaseCalled()
    }

    @Test
    fun `when refreshPage fail should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<InboxReputationDetailDomain> = TestSubscriber()

        every {
            getInboxReputationDetailUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedResponse)
        }

        presenter.refreshPage(anyString(), anyInt())

        testSubscriber.assertError(expectedResponse)
        testSubscriber.assertCompleted()
        verifyGetInboxReputationDetailUseCaseCalled()
    }

    private fun verifyGetInboxReputationDetailUseCaseCalled() {
        verify { getInboxReputationDetailUseCase.execute(any(), any()) }
    }

    private fun verifySendSmileyReputationUseCaseCalled() {
        verify { sendSmileyReputationUseCase.execute(any(), any()) }
    }
}