package com.tokopedia.talk_old.talkdetails.domain.usecase

import com.tokopedia.talk_old.talkdetails.data.api.DetailTalkApi
import com.tokopedia.talk_old.talkdetails.domain.mapper.GetTalkCommentsMapper
import com.tokopedia.talk_old.talkdetails.view.viewmodel.TalkDetailViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * Created by Hendri on 05/09/18.
 */
class GetTalkCommentsUseCase @Inject constructor(val api: DetailTalkApi,
                                                 val mapper: GetTalkCommentsMapper) : UseCase<TalkDetailViewModel>() {
    override fun createObservable(requestParams: RequestParams?): Observable<TalkDetailViewModel> {
        val parameters = requestParams?.parameters ?: HashMap()
        return api.getTalkComment(parameters).map(mapper)
    }

    companion object {
        fun getParameters(talkId: String): RequestParams {
            val requestParams: RequestParams = RequestParams.create()
            requestParams.putString("talk_id", talkId)
            return requestParams
        }
    }
}