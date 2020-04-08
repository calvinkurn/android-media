package com.tokopedia.talk.common.domain.usecase

import com.tokopedia.talk.common.data.TalkApi
import com.tokopedia.talk.common.domain.mapper.BaseActionMapper
import com.tokopedia.talk.common.view.BaseActionTalkViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 9/7/18.
 */
class MarkTalkNotFraudUseCase @Inject constructor(val api: TalkApi,
                                                  val mapper: BaseActionMapper) :
        UseCase<BaseActionTalkViewModel>() {
    override fun createObservable(requestParams: RequestParams): Observable<BaseActionTalkViewModel> {
        return api.markTalkNotFraud(requestParams.parameters).map(mapper)
    }

    companion object {

        private val PARAM_TALK_ID: String = "talk_id"
        private val PARAM_COMMENT_ID: String = "comment_id"


        fun getParamTalk(
                talkId: String): RequestParams {
            val requestParams: RequestParams = RequestParams.create()

            requestParams.putString(PARAM_TALK_ID, talkId)
            return requestParams
        }

        fun getParamComment(
                talkId: String,
                commentId: String): RequestParams {
            val requestParams: RequestParams = RequestParams.create()

            requestParams.putString(PARAM_TALK_ID, talkId)
            requestParams.putString(PARAM_COMMENT_ID, commentId)

            return requestParams
        }
    }

}