package com.tokopedia.talk.common.domain.mapper

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.common.domain.pojo.BaseActionTalkPojo
import com.tokopedia.talk.common.view.BaseActionTalkViewModel
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 9/7/18.
 */

class BaseActionMapper @Inject constructor() : Func1<Response<DataResponse<BaseActionTalkPojo>>,
        BaseActionTalkViewModel> {

    override fun call(response: Response<DataResponse<BaseActionTalkPojo>>): BaseActionTalkViewModel {
        val body = response.body()
        if (body != null) {
            if (body.header == null ||
                    (body.header != null && body.header.messages.isEmpty()) ||
                    (body.header != null && body.header.messages[0].isBlank())) {
                val pojo: BaseActionTalkPojo = body.data

                if (pojo.is_success == 1) {
                    return mapToViewModel(pojo)
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

    private fun mapToViewModel(pojo: BaseActionTalkPojo): BaseActionTalkViewModel {
        return BaseActionTalkViewModel(pojo.is_success == 1)
    }

}
