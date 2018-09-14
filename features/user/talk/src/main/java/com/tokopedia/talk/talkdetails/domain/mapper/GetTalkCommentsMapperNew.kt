package com.tokopedia.talk.inboxtalk.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.common.adapter.viewmodel.TalkProductAttachmentViewModel
import com.tokopedia.talk.common.domain.CommentProduct
import com.tokopedia.talk.common.domain.InboxTalkItemPojo
import com.tokopedia.talk.common.domain.InboxTalkPojo
import com.tokopedia.talk.common.domain.TalkCommentItem
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkItemViewModel
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkViewModel
import com.tokopedia.talk.inboxtalk.view.viewmodel.ProductHeader
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkItemViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.TalkState
import com.tokopedia.talk.producttalk.view.viewmodel.TalkThreadViewModel
import com.tokopedia.talk.talkdetails.domain.pojo.TalkDetailsPojo
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 9/3/18.
 */
class GetTalkCommentsMapperNew @Inject constructor() : Func1<Response<DataResponse<TalkDetailsPojo>>,
        InboxTalkViewModel> {

    private val IS_READ = 2
    private val IS_FOLLOWED = 1

    override fun call(response: Response<DataResponse<TalkDetailsPojo>>): InboxTalkViewModel {
        if (response.body().header == null ||
                (response.body().header != null && response.body().header.messages.isEmpty()) ||
                (response.body().header != null && response.body().header.messages[0].isBlank())) {
            val pojo: TalkDetailsPojo = response.body().data
            return mapToViewModel(pojo)
        } else {
            throw MessageErrorException(response.body().header.messages[0])
        }
    }

    private fun mapToViewModel(pojo: TalkDetailsPojo): InboxTalkViewModel {
        val listTalk = ArrayList<Visitable<*>>()
//        listTalk.add(mapListThread(pojo))
//        for (data: InboxTalkItemPojo in pojo.list) {
            listTalk.add(InboxTalkItemViewModel(
                    mapProductHeader(pojo),
                    mapListThread(pojo)
            ))
//        }
        return InboxTalkViewModel("",
                listTalk,
                false,
                0,
                0)
    }

    private fun mapListThread(pojo: TalkDetailsPojo): TalkThreadViewModel {

        val listTalk = ArrayList<Visitable<*>>()
        for (data: TalkCommentItem in pojo.list) {
            listTalk.add(ProductTalkItemViewModel(
                    data.comment_user_image,
                    data.comment_user_name,
                    data.comment_create_time,
                    data.comment_message,
                    mapCommentTalkState(data),
                    true,
                    true,
                    mapProductAttachment(data),
                    data.comment_raw_message,
                    data.comment_is_owner == 1,
                    data.comment_shop_id,
                    data.comment_talk_id,
                    data.comment_id,
                    pojo.talk.talk_product_id
            ))
        }

        return TalkThreadViewModel(
                ProductTalkItemViewModel(
                        pojo.talk.talk_user_image,
                        pojo.talk.talk_user_name,
                        pojo.talk.talk_create_time,
                        pojo.talk.talk_message,
                        mapHeaderTalkState(pojo),
                        true,
                        pojo.talk.talk_follow_status == IS_FOLLOWED,
                        ArrayList(),
                        pojo.talk.talk_raw_message,
                        pojo.talk.talk_own == 1,
                        pojo.talk.talk_shop_id,
                        pojo.talk.talk_id,
                        "",
                        pojo.talk.talk_product_id
                ),
                listTalk
        )
    }

    private fun mapProductAttachment(pojo: TalkCommentItem):
            ArrayList<TalkProductAttachmentViewModel> {
        val listProduct = ArrayList<TalkProductAttachmentViewModel>()
        for (data: CommentProduct in pojo.listProduct) {
            listProduct.add(TalkProductAttachmentViewModel(
                    data.product_name,
                    data.product_id,
                    data.product_image,
                    data.product_price
            ))
        }
        return listProduct
    }

    private fun mapHeaderTalkState(pojo: TalkDetailsPojo): TalkState {
        return TalkState(
                pojo.talk.talk_state.allow_report,
                pojo.talk.talk_state.allow_delete,
                pojo.talk.talk_state.allow_follow,
                pojo.talk.talk_state.allow_unmasked,
                false,
                pojo.talk.talk_state.reported,
                pojo.talk.talk_state.masked,
                pojo.talk.talk_follow_status == IS_FOLLOWED
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

    private fun mapProductHeader(data: TalkDetailsPojo): ProductHeader {
        return ProductHeader(
                data.talk.talk_product_name,
                data.talk.talk_product_image
        )
    }
}