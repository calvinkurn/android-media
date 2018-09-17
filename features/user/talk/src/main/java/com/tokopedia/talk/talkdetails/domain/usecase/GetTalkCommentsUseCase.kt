package com.tokopedia.talk.talkdetails.domain.usecase

import com.tokopedia.talk.talkdetails.data.api.DetailTalkApi
import com.tokopedia.talk.talkdetails.domain.mapper.GetTalkCommentsMapper
import com.tokopedia.talk.talkdetails.view.viewmodel.TalkDetailsViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * Created by Hendri on 05/09/18.
 */
class GetTalkCommentsUseCase(val api:DetailTalkApi,
                             val mapper:GetTalkCommentsMapper): UseCase<TalkDetailsViewModel>() {
    override fun createObservable(requestParams: RequestParams?): Observable<TalkDetailsViewModel> {
        val parameters = requestParams?.parameters?:HashMap()
        return api.getTalkComment(parameters).map(mapper)
    }

    companion object {
        fun getParameters(talkId:String):RequestParams{
            val requestParams:RequestParams = RequestParams.create()
            requestParams.putString("talk_id",talkId)
            return requestParams
        }
    }
}