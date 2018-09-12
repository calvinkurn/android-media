package com.tokopedia.talk.talkdetails.domain.usecase

import com.tokopedia.talk.talkdetails.data.api.DetailTalkApi
import com.tokopedia.talk.talkdetails.domain.mapper.DeleteTalkCommentsMapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * Created by Hendri on 05/09/18.
 */
class DeleteTalkCommentsUseCase(val api: DetailTalkApi,
                                val mapper: DeleteTalkCommentsMapper):UseCase<String>() {
    override fun createObservable(requestParams: RequestParams?): Observable<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        fun getParameters(talkId:String):RequestParams{
            val requestParams:RequestParams = RequestParams.create()
            requestParams.putString("talk_id",talkId)
            return requestParams
        }
    }
}