package com.tokopedia.talk.talkdetails.domain.usecase

import com.tokopedia.talk.inboxtalk.domain.mapper.GetTalkCommentsMapperNew
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkViewModel
import com.tokopedia.talk.talkdetails.data.SendCommentResponse
import com.tokopedia.talk.talkdetails.data.api.DetailTalkApi
import com.tokopedia.talk.talkdetails.domain.mapper.SendCommentMapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * Created by Hendri on 05/09/18.
 */
class SendCommentsUseCase(val api:DetailTalkApi,
                          val mapper:SendCommentMapper): UseCase<SendCommentResponse>() {
    override fun createObservable(requestParams: RequestParams?): Observable<SendCommentResponse> {
        return api.sendComment(requestParams?.parameters?:HashMap<String,Any>()).map(mapper)
    }

    companion object {
        val ANDROID_OS_TYPE = "1"
        fun getParameters(talkId:String,
                          productId:String,
                          userId:String,
                          message:String):RequestParams{
            val requestParams:RequestParams = RequestParams.create()
            requestParams.putString("talk_id",talkId)
            requestParams.putString("text_comment",message)
            requestParams.putString("product_id",productId)
            requestParams.putString("user_id",userId)
            requestParams.putString("os_type", ANDROID_OS_TYPE)
            return requestParams
        }
    }
}