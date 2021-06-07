package com.tokopedia.talk.feature.reporttalk.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.feature.reply.data.model.report.TalkReportCommentResponseWrapper
import com.tokopedia.talk.feature.reply.data.model.report.TalkReportTalkResponseWrapper
import com.tokopedia.talk.feature.reply.domain.usecase.TalkReportCommentUseCase
import com.tokopedia.talk.feature.reply.domain.usecase.TalkReportTalkUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ReportTalkViewModel @Inject constructor(
        private val talkReportTalkUseCase: TalkReportTalkUseCase,
        private val talkReportCommentUseCase: TalkReportCommentUseCase,
        dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object {
        const val MUTATION_SUCCESS = 1
        const val INDEX_OF_OTHER_REPORT = 2
    }

    private val _reportTalkResult = MutableLiveData<Result<TalkReportTalkResponseWrapper>>()
    val reportTalkResult: LiveData<Result<TalkReportTalkResponseWrapper>>
        get() = _reportTalkResult

    private val _reportCommentResult = MutableLiveData<Result<TalkReportCommentResponseWrapper>>()
    val reportCommentResult: LiveData<Result<TalkReportCommentResponseWrapper>>
        get() = _reportCommentResult

    fun reportTalk(questionId: String, reason: String, reportType: Int) {
        launchCatchError(block = {
            talkReportTalkUseCase.setParams(questionId.toIntOrZero(), if(reportType == INDEX_OF_OTHER_REPORT) reason else "", reportType + 1)
            val response = talkReportTalkUseCase.executeOnBackground()
            if (response.talkReportTalk.data.isSuccess == MUTATION_SUCCESS) {
                _reportTalkResult.postValue(Success(response))
            } else {
                _reportTalkResult.postValue(Fail(MessageErrorException(response.talkReportTalk.messageError.firstOrNull())))
            }
        }) {
            _reportTalkResult.postValue(Fail(it))
        }
    }

    fun reportComment(commentId: String, reason: String, reportType: Int) {
        launchCatchError(block = {
            talkReportCommentUseCase.setParams(commentId.toIntOrZero(), if(reportType == INDEX_OF_OTHER_REPORT) reason else "", reportType + 1)
            val response = talkReportCommentUseCase.executeOnBackground()
            if (response.talkReportComment.data.isSuccess == MUTATION_SUCCESS) {
                _reportCommentResult.postValue(Success(response))
            } else {
                _reportCommentResult.postValue(Fail(MessageErrorException(response.talkReportComment.messageError.firstOrNull())))
            }
        }) {
            _reportCommentResult.postValue(Fail(it))
        }
    }
}