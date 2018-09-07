package com.tokopedia.talk.producttalk.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.common.domain.InboxTalkItemPojo
import com.tokopedia.talk.common.domain.InboxTalkPojo
import com.tokopedia.talk.common.domain.TalkCommentItem
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkItemViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.TalkState
import com.tokopedia.talk.producttalk.view.viewmodel.TalkThreadViewModel
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by Steven.
 */
class ProductTalkListMapper @Inject constructor() : Func1<Response<DataResponse<InboxTalkPojo>>,
        ProductTalkViewModel> {

    private val IS_READ = 2
    private val IS_FOLLOWED = 1

    override fun call(response: Response<DataResponse<InboxTalkPojo>>): ProductTalkViewModel {
        if (response.body() != null) {
//                (response.body().header!= null &&
//                        response.body().header!= null &&
//                response.body().header.messages.isEmpty() ||
//                response.body().header.messages[0].isBlank())) {
            val pojo: InboxTalkPojo = response.body().data
            return mapToViewModel(pojo)
        } else {
            throw MessageErrorException(response.body().header.messages[0])
        }
    }

    private fun mapToViewModel(pojo: InboxTalkPojo): ProductTalkViewModel {

        val listThread = ArrayList<Visitable<*>>()

        for (data: InboxTalkItemPojo in pojo.list) {
            listThread.add(mapThread(data))
        }
        return ProductTalkViewModel("",
                listThread,
                pojo.paging.has_next,
                pojo.paging.page_id)
    }

    private fun mapThread(pojo: InboxTalkItemPojo): TalkThreadViewModel {

        //TODO NISIE CHECK PRODUCT ATTACHMENT
        val listTalk = ArrayList<Visitable<*>>()
        for (data: TalkCommentItem in pojo.list) {
            listTalk.add(ProductTalkItemViewModel(
                    data.comment_user_image,
                    data.comment_user_name,
                    data.comment_create_time_fmt,
                    data.comment_message,
                    mapCommentTalkState(data),
                    true,
                    true,
                    ArrayList(),
                    data.comment_raw_message,
                    data.comment_is_owner == 1

            ))
        }

        //TODO NISIE CHECK PRODUCT ATTACHMENT
        return TalkThreadViewModel(
                ProductTalkItemViewModel(
                        pojo.talk_user_image,
                        pojo.talk_user_name,
                        pojo.talk_create_time_fmt,
                        pojo.talk_message,
                        mapHeaderTalkState(pojo),
                        pojo.talk_read_status == IS_READ,
                        pojo.talk_follow_status == IS_FOLLOWED,
                        ArrayList(),
                        pojo.talk_raw_message,
                        pojo.talk_own == 1

                ),
                listTalk)
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

}