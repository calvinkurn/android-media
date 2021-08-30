package com.tokopedia.review.feature.inbox.buyerreview.view.presenter

import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.report.ReportReviewUseCase
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationReport
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Before

abstract class InboxReputationReportPresenterTestFixture {

    @RelaxedMockK
    lateinit var reportReviewUseCase: ReportReviewUseCase

    @RelaxedMockK
    lateinit var view: InboxReputationReport.View

    protected lateinit var presenter: InboxReputationReportPresenter

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        presenter = InboxReputationReportPresenter(reportReviewUseCase)
        presenter.attachView(view)
    }

    @After
    fun cleanup() {
        presenter.detachView()
    }
}