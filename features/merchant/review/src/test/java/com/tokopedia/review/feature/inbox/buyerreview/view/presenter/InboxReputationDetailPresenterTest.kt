package com.tokopedia.review.feature.inbox.buyerreview.view.presenter

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.DeleteReviewResponseDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.InboxReputationDetailDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendReplyReviewDomain
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

        presenter.sendSmiley(anyString(), anyString(), anyInt())

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

        presenter.sendSmiley(anyString(), anyString(), anyInt())

        testSubscriber.assertError(expectedResponse)
        testSubscriber.assertCompleted()
        verifySendSmileyReputationUseCaseCalled()
    }

    @Test
    fun `when deleteReviewResponse success should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<DeleteReviewResponseDomain>(relaxed = true)
        val testSubscriber: TestSubscriber<DeleteReviewResponseDomain> = TestSubscriber()

        every {
            deleteReviewResponseUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedResponse)
        }

        presenter.deleteReviewResponse(anyString(), anyString(), anyString(), anyString())

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedResponse)
        testSubscriber.assertCompleted()
        verifyDeleteReviewResponseUseCaseCalled()
    }

    @Test
    fun `when deleteReviewResponse fail should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<DeleteReviewResponseDomain> = TestSubscriber()

        every {
            deleteReviewResponseUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedResponse)
        }

        presenter.deleteReviewResponse(anyString(), anyString(), anyString(), anyString())

        testSubscriber.assertError(expectedResponse)
        testSubscriber.assertCompleted()
        verifyDeleteReviewResponseUseCaseCalled()
    }

    @Test
    fun `when sendReplyReview success should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<SendReplyReviewDomain>(relaxed = true)
        val testSubscriber: TestSubscriber<SendReplyReviewDomain> = TestSubscriber()

        every {
            sendReplyReviewUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedResponse)
        }

        presenter.sendReplyReview(anyInt(), anyString(), anyInt(), anyString(), anyString())

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedResponse)
        testSubscriber.assertCompleted()
        verifySendReplyReviewUseCaseCalled()
    }

    @Test
    fun `when sendReplyReview fail should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<SendReplyReviewDomain> = TestSubscriber()

        every {
            sendReplyReviewUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedResponse)
        }

        presenter.sendReplyReview(anyInt(), anyString(), anyInt(), anyString(), anyString())

        testSubscriber.assertError(expectedResponse)
        testSubscriber.assertCompleted()
        verifySendReplyReviewUseCaseCalled()
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

    private fun verifyDeleteReviewResponseUseCaseCalled() {
        verify { deleteReviewResponseUseCase.execute(any(), any()) }
    }

    private fun verifySendReplyReviewUseCaseCalled() {
        verify { sendReplyReviewUseCase.execute(any(), any()) }
    }
}