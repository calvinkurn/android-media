package com.tokopedia.review.feature.inbox.buyerreview.view.viewmodel.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.report.ReportReviewUseCase
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.report.ReportReviewResponse
import com.tokopedia.review.inbox.R
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InboxReputationReportViewModel @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val reportReviewUseCase: ReportReviewUseCase
) : BaseViewModel(coroutineDispatchers.main) {

    private val _reportReviewResult = MutableLiveData<Result<ReportReviewResponse.ProductrevReportReview>>()
    val reportReviewResult: LiveData<Result<ReportReviewResponse.ProductrevReportReview>>
        get() = _reportReviewResult

    private fun getCheckedRadio(checkedRadioButtonId: Int): Int {
        return when (checkedRadioButtonId) {
            R.id.report_spam -> {
                ReportReviewUseCase.REPORT_SPAM
            }
            R.id.report_sara -> {
                ReportReviewUseCase.REPORT_SARA
            }
            else -> {
                ReportReviewUseCase.REPORT_OTHER
            }
        }
    }

    fun reportReview(
        feedbackId: String,
        checkedRadioId: Int,
        reasonText: String
    ) {
        launchCatchError(block = {
            val responseInsertReply = withContext(coroutineDispatchers.io) {
                reportReviewUseCase.execute(
                    feedbackId,
                    getCheckedRadio(checkedRadioId),
                    reasonText
                )
            }
            _reportReviewResult.value = Success(responseInsertReply)
        }, onError = {
            _reportReviewResult.value = Fail(it)
        })
    }
}