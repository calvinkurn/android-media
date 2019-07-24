package com.tokopedia.talk.addtalk.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.addtalk.domain.CreateTalkPojo
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkItemViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.TalkThreadViewModel
import retrofit2.Response
import rx.functions.Func1
import java.util.*
import javax.inject.Inject

/**
 * @author : Steven 17/09/18
 */

class CreateTalkMapper @Inject constructor() : Func1<Response<DataResponse<CreateTalkPojo>>,
        TalkThreadViewModel> {
    override fun call(response: Response<DataResponse<CreateTalkPojo>>): TalkThreadViewModel {
        val body = response.body()
        if (body != null) {
            if ((body != null) && (body.header == null ||
                            (body.header != null && body.header.messages.isEmpty()) ||
                            (body.header != null && body.header.messages[0].isBlank()))) {
                val pojo: CreateTalkPojo = body.data
                return mapToViewModel(pojo)
            } else {
                throw MessageErrorException(body.header.messages[0])
            }
        } else {
            throw MessageErrorException("")
        }
    }

    private fun mapToViewModel(pojo: CreateTalkPojo): TalkThreadViewModel {
        return TalkThreadViewModel(ProductTalkItemViewModel(), ArrayList<Visitable<*>>())
    }
}

