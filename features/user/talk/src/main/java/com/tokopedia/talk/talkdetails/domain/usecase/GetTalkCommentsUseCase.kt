package com.tokopedia.talk.talkdetails.domain.usecase

import com.tokopedia.talk.inboxtalk.domain.mapper.GetTalkCommentsMapperNew
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkViewModel
import com.tokopedia.talk.talkdetails.data.api.DetailTalkApi
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * Created by Hendri on 05/09/18.
 */
class GetTalkCommentsUseCase(val api:DetailTalkApi,
                             val mapper:GetTalkCommentsMapperNew): UseCase<InboxTalkViewModel>() {
    override fun createObservable(requestParams: RequestParams?): Observable<InboxTalkViewModel> {
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