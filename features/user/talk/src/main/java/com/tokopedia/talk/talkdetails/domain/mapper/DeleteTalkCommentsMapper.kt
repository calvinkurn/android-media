package com.tokopedia.talk.talkdetails.domain.mapper

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.talk.talkdetails.view.viewmodel.TalkDetailsViewModel
import retrofit2.Response
import rx.functions.Func1

/**
 * Created by Hendri on 05/09/18.
 */
class DeleteTalkCommentsMapper: Func1<Response<DataResponse<String>>, String> {
    override fun call(t: Response<DataResponse<String>>?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}