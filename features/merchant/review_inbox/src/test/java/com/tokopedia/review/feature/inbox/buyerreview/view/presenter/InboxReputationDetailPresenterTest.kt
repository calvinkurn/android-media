package com.tokopedia.review.feature.inbox.buyerreview.view.presenter

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.InboxReputationDetailDomain
import io.mockk.coEvery
import io.mockk.coVerify
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
        val expectedResponse = true

        coEvery {
            sendSmileyReputationUseCase.execute(any())
        } returns expectedResponse

        presenter.attachView(view)
        presenter.sendSmiley(anyString(), anyString(), anyString())

        coVerify(exactly = 1) {
            sendSmileyReputationUseCase.execute(any())
        }

        verify(exactly = 1) {
            view.showLoadingDialog()
            view.finishLoadingDialog()
            view.onSuccessSendSmiley(any())
        }

        verify(inverse = true) { view.onErrorSendSmiley(any()) }
    }

    @Test
    fun `when sendSmiley but view is not attached success should execute expected usecase`() {
        val expectedResponse = true

        coEvery {
            sendSmileyReputationUseCase.execute(any())
        } returns expectedResponse

        presenter.attachView(null)
        presenter.sendSmiley(anyString(), anyString(), anyString())

        coVerify(exactly = 1) { sendSmileyReputationUseCase.execute(any()) }

        verify(inverse = true) {
            view.showLoadingDialog()
            view.finishLoadingDialog()
            view.onSuccessSendSmiley(any())
            view.onErrorSendSmiley(any())
        }
    }

    @Test
    fun `when sendSmiley fail with exception should execute expected usecase and perform expected view actions`() {
        val expectedResponse = Throwable()

        coEvery {
            sendSmileyReputationUseCase.execute(any())
        } throws expectedResponse

        presenter.attachView(view)
        presenter.sendSmiley(anyString(), anyString(), anyString())

        coVerify(exactly = 1) {
            sendSmileyReputationUseCase.execute(any())
        }

        verify(exactly = 1) {
            view.showLoadingDialog()
            view.finishLoadingDialog()
            view.onErrorSendSmiley(any())
        }

        verify(inverse = true) { view.onSuccessSendSmiley(any()) }
    }

    @Test
    fun `when sendSmiley fail without exception should execute expected usecase and perform expected view actions`() {
        val expectedResponse = false

        coEvery {
            sendSmileyReputationUseCase.execute(any())
        } returns expectedResponse

        presenter.attachView(view)
        presenter.sendSmiley(anyString(), anyString(), anyString())

        coVerify(exactly = 1) {
            sendSmileyReputationUseCase.execute(any())
        }

        verify(exactly = 1) {
            view.showLoadingDialog()
            view.finishLoadingDialog()
            view.onErrorSendSmiley(any())
        }

        verify(inverse = true) { view.onSuccessSendSmiley(any()) }
    }

    @Test
    fun `when sendSmiley fail with exception but view is not attached should execute expected usecase without perform expected view actions`() {
        val expectedResponse = Throwable()

        coEvery {
            sendSmileyReputationUseCase.execute(any())
        } throws expectedResponse

        presenter.attachView(null)
        presenter.sendSmiley(anyString(), anyString(), anyString())

        coVerify(exactly = 1) {
            sendSmileyReputationUseCase.execute(any())
        }

        verify(inverse = true) {
            view.showLoadingDialog()
            view.finishLoadingDialog()
            view.onErrorSendSmiley(any())
        }

        verify(inverse = true) { view.onSuccessSendSmiley(any()) }
    }

    @Test
    fun `when sendSmiley fail without exception but view is not attached should execute expected usecase without perform expected view actions`() {
        val expectedResponse = false

        coEvery {
            sendSmileyReputationUseCase.execute(any())
        } returns expectedResponse

        presenter.attachView(null)
        presenter.sendSmiley(anyString(), anyString(), anyString())

        coVerify(exactly = 1) {
            sendSmileyReputationUseCase.execute(any())
        }

        verify(inverse = true) {
            view.showLoadingDialog()
            view.finishLoadingDialog()
            view.onErrorSendSmiley(any())
        }

        verify(inverse = true) { view.onSuccessSendSmiley(any()) }
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
}