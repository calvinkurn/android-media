package com.tokopedia.talk.inboxtalk.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.ProductTalkItemViewModel
import com.tokopedia.talk.TalkState
import com.tokopedia.talk.TalkThreadViewModel
import com.tokopedia.talk.common.domain.InboxTalkItemPojo
import com.tokopedia.talk.common.domain.InboxTalkPojo
import com.tokopedia.talk.common.domain.TalkCommentItem
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkItemViewModel
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkViewModel
import com.tokopedia.talk.inboxtalk.view.viewmodel.ProductHeader
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 9/3/18.
 */
class GetInboxTalkMapper @Inject constructor() : Func1<Response<DataResponse<InboxTalkPojo>>,
        InboxTalkViewModel> {

    private val IS_READ = 2
    private val IS_FOLLOWED = 1

    override fun call(response: Response<DataResponse<InboxTalkPojo>>): InboxTalkViewModel {
        if (response.body().header == null ||
                (response.body().header != null && response.body().header.messages.isEmpty()) ||
                (response.body().header != null && response.body().header.messages[0].isBlank())) {
            val pojo: InboxTalkPojo = response.body().data
            return mapToViewModel(pojo)
        } else {
            throw MessageErrorException(response.body().header.messages[0])
        }
    }

    private fun mapToViewModel(pojo: InboxTalkPojo): InboxTalkViewModel {
        val listTalk = ArrayList<Visitable<*>>()
        for (data: InboxTalkItemPojo in pojo.list) {
            listTalk.add(InboxTalkItemViewModel(
                    mapProductHeader(data),
                    mapListThread(data)
            ))
        }
        return InboxTalkViewModel("",
                listTalk,
                pojo.paging.has_next,
                pojo.paging.page_id)
    }

    private fun mapListThread(pojo: InboxTalkItemPojo): TalkThreadViewModel {

        val listTalk = ArrayList<Visitable<*>>()
        for (data: TalkCommentItem in pojo.list) {
            listTalk.add(ProductTalkItemViewModel(
                    data.comment_user_image,
                    data.comment_user_name,
                    data.comment_create_time,
                    data.comment_message,
                    mapCommentTalkState(data),
                    true,
                    true
            ))
        }

        return TalkThreadViewModel(
                ProductTalkItemViewModel(
                        pojo.talk_user_image,
                        pojo.talk_user_name,
                        pojo.talk_create_time,
                        pojo.talk_message,
                        mapHeaderTalkState(pojo),
                        pojo.talk_read_status == IS_READ,
                        pojo.talk_follow_status == IS_FOLLOWED
                ),
                listTalk
        )
    }

    private fun mapHeaderTalkState(pojo: InboxTalkItemPojo): TalkState {
        return TalkState(
                pojo.talk_state.allow_report,
                pojo.talk_state.allow_delete,
                pojo.talk_state.allow_follow,
                pojo.talk_state.allow_unmasked,
                pojo.talk_state.allow_reply,
                pojo.talk_state.reported,
                pojo.talk_state.masked,
                pojo.talk_follow_status == IS_FOLLOWED
        )
    }

    private fun mapCommentTalkState(pojo: TalkCommentItem): TalkState {
        return TalkState(
                pojo.comment_state.allow_report,
                pojo.comment_state.allow_delete,
                pojo.comment_state.allow_follow,
                pojo.comment_state.allow_unmasked,
                pojo.comment_state.allow_reply,
                pojo.comment_state.reported,
                pojo.comment_state.masked,
                false
        )
    }

    private fun mapProductHeader(data: InboxTalkItemPojo): ProductHeader {
        return ProductHeader(
                data.talk_product_name,
                data.talk_product_image
        )
    }
}