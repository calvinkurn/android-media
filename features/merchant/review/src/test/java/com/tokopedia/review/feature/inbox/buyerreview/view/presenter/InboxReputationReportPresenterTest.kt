package com.tokopedia.review.feature.inbox.buyerreview.view.presenter

import com.tokopedia.review.R
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.report.ReportReviewUseCase
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.report.ReportReviewDomain
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import rx.observers.TestSubscriber

class InboxReputationReportPresenterTest: InboxReputationReportPresenterTestFixture() {

    @Test
    fun `when reportReview success should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<ReportReviewDomain>(relaxed = true)
        val testSubscriber: TestSubscriber<ReportReviewDomain> = TestSubscriber()

        every {
            reportReviewUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedResponse)
        }

        presenter.reportReview(anyString(), anyString(), R.id.report_spam, anyString())

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedResponse)
        testSubscriber.assertCompleted()
        verifyReportReviewUseCaseExecuted()
    }

    @Test
    fun `when reportReview fail should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<ReportReviewDomain> = TestSubscriber()

        every {
            reportReviewUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedResponse)
        }

        presenter.reportReview(anyString(), anyString(), R.id.report_sara, anyString())

        testSubscriber.assertError(expectedResponse)
        testSubscriber.assertCompleted()
        verifyReportReviewUseCaseExecuted()
    }

    @Test
    fun `when reportReview other success should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<ReportReviewDomain>(relaxed = true)
        val testSubscriber: TestSubscriber<ReportReviewDomain> = TestSubscriber()

        every {
            reportReviewUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedResponse)
        }

        presenter.reportReview(anyString(), anyString(),R.id.report_other, anyString())

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedResponse)
        testSubscriber.assertCompleted()
        verifyReportReviewUseCaseExecuted()
    }

    private fun verifyReportReviewUseCaseExecuted() {
        verify { reportReviewUseCase.execute(any(), any()) }
    }
}