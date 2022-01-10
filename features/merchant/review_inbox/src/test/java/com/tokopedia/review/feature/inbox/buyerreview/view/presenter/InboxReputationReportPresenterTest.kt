package com.tokopedia.review.feature.inbox.buyerreview.view.presenter

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.report.ReportReviewDomain
import com.tokopedia.review.inbox.R
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import rx.observers.TestSubscriber

class InboxReputationReportPresenterTest: InboxReputationReportPresenterTestFixture() {

    @Test
    fun `when reportReview spam success should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<ReportReviewDomain>(relaxed = true)
        val testSubscriber: TestSubscriber<ReportReviewDomain> = TestSubscriber()
        presenter.attachView(view)

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
    fun `when reportReview SARA success should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<ReportReviewDomain>(relaxed = true)
        val testSubscriber: TestSubscriber<ReportReviewDomain> = TestSubscriber()
        presenter.attachView(view)

        every {
            reportReviewUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedResponse)
        }

        presenter.reportReview(anyString(), anyString(), R.id.report_sara, anyString())

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedResponse)
        testSubscriber.assertCompleted()
        verifyReportReviewUseCaseExecuted()
    }

    @Test
    fun `when reportReview fail should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<ReportReviewDomain> = TestSubscriber()
        presenter.attachView(view)

        every {
            reportReviewUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedResponse)
        }

        presenter.reportReview(anyString(), anyString(), anyInt(), anyString())

        testSubscriber.assertError(expectedResponse)
        testSubscriber.assertCompleted()
        verifyReportReviewUseCaseExecuted()
    }

    @Test
    fun `when reportReview other success should execute expected usecase and perform expected view actions`() {
        val expectedResponse = mockk<ReportReviewDomain>(relaxed = true)
        val testSubscriber: TestSubscriber<ReportReviewDomain> = TestSubscriber()
        presenter.attachView(view)

        every {
            reportReviewUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedResponse)
        }

        presenter.reportReview(anyString(), anyString(), anyInt(), anyString())

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedResponse)
        testSubscriber.assertCompleted()
        verifyReportReviewUseCaseExecuted()
    }

    @Test
    fun `when reportReview but view is not attached success should still execute expected usecase`() {
        val expectedResponse = mockk<ReportReviewDomain>(relaxed = true)
        val testSubscriber: TestSubscriber<ReportReviewDomain> = TestSubscriber()

        every {
            reportReviewUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedResponse)
        }

        presenter.reportReview(anyString(), anyString(), anyInt(), anyString())

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedResponse)
        testSubscriber.assertCompleted()
        verifyReportReviewUseCaseExecuted()
    }

    private fun verifyReportReviewUseCaseExecuted() {
        verify { reportReviewUseCase.execute(any(), any()) }
    }
}