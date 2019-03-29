package com.tokopedia.talk.talkdetails.domain.mapper

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.common.domain.pojo.BaseActionTalkPojo
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkViewModel
import com.tokopedia.talk.talkdetails.data.SendCommentResponse
import com.tokopedia.talk.talkdetails.domain.pojo.TalkDetailsPojo
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * Created by Hendri on 14/09/18.
 */
class SendCommentMapper @Inject constructor() :
        Func1<Response<DataResponse<SendCommentResponse>>,SendCommentResponse> {
    override fun call(response: Response<DataResponse<SendCommentResponse>>): SendCommentResponse {
        if (response.body().header == null ||
                (response.body().header != null && response.body().header.messages.isEmpty()) ||
                (response.body().header != null && response.body().header.messages[0].isBlank())) {
            val pojo: SendCommentResponse = response.body().data
            if (pojo.is_success == 1) {
                return pojo
            } else {
                throw MessageErrorException("")
            }
        } else {
            throw MessageErrorException(response.body().header.messages[0])
        }
    }
}