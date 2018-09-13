package com.tokopedia.talk.talkdetails.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.common.domain.InboxTalkItemPojo
import com.tokopedia.talk.common.domain.InboxTalkPojo
import com.tokopedia.talk.common.domain.pojo.BaseActionTalkPojo
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkItemViewModel
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkViewModel
import com.tokopedia.talk.talkdetails.view.viewmodel.TalkDetailsCommentViewModel
import com.tokopedia.talk.talkdetails.view.viewmodel.TalkDetailsThreadItemViewModel
import com.tokopedia.talk.talkdetails.view.viewmodel.TalkDetailsViewModel
import retrofit2.Response
import rx.functions.Func1

/**
 * Created by Hendri on 05/09/18.
 */
class GetTalkCommentsMapper: Func1<Response<DataResponse<InboxTalkPojo>>, TalkDetailsViewModel> {
    override fun call(response: Response<DataResponse<InboxTalkPojo>>): TalkDetailsViewModel {
        if (response.body().header == null ||
                (response.body().header != null && response.body().header.messages.isEmpty()) ||
                (response.body().header != null && response.body().header.messages[0].isBlank())) {
            val pojo: InboxTalkPojo = response.body().data
            return mapToViewModel(pojo)
        } else {
            throw MessageErrorException(response.body().header.messages[0])
        }
    }

    private fun mapToViewModel(pojo: InboxTalkPojo): TalkDetailsViewModel {
        val listTalk = ArrayList<Visitable<*>>()
//        for (data: InboxTalkItemPojo in pojo.list) {
//            listTalk.add(InboxTalkItemViewModel(
//                    mapProductHeader(data),
//                    mapListThread(data)
//            ))
//        }
        return TalkDetailsViewModel(TalkDetailsThreadItemViewModel(),
                ArrayList<TalkDetailsCommentViewModel>())
    }
}