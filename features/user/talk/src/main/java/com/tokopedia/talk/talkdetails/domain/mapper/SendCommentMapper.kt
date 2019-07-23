package com.tokopedia.talk.talkdetails.domain.mapper

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.talkdetails.data.SendCommentResponse
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * Created by Hendri on 14/09/18.
 */
class SendCommentMapper @Inject constructor() :
        Func1<Response<DataResponse<SendCommentResponse>>,SendCommentResponse> {
    override fun call(response: Response<DataResponse<SendCommentResponse>>): SendCommentResponse {
        val body = response.body()
        if (body != null) {
            if (body.header == null ||
                    (body.header != null && body.header.messages.isEmpty()) ||
                    (body.header != null && body.header.messages[0].isBlank())) {
                val pojo: SendCommentResponse = body.data
                if (pojo.is_success == 1) {
                    return pojo
                } else {
                    throw MessageErrorException("")
                }
            } else {
                throw MessageErrorException(body.header.messages[0])
            }
        } else {
            throw MessageErrorException("")
        }
    }
}