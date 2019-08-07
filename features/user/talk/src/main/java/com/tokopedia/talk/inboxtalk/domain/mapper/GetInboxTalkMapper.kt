package com.tokopedia.talk.inboxtalk.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.common.adapter.viewmodel.TalkProductAttachmentViewModel
import com.tokopedia.talk.common.domain.pojo.CommentProduct
import com.tokopedia.talk.common.domain.pojo.InboxTalkItemPojo
import com.tokopedia.talk.common.domain.pojo.InboxTalkPojo
import com.tokopedia.talk.common.domain.pojo.TalkCommentItem
import com.tokopedia.talk.common.viewmodel.LoadMoreCommentTalkViewModel
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkItemViewModel
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkViewModel
import com.tokopedia.talk.inboxtalk.view.viewmodel.ProductHeader
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkItemViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.TalkState
import com.tokopedia.talk.producttalk.view.viewmodel.TalkThreadViewModel
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
    private val SELLER_LABEL_ID = 3


    override fun call(response: Response<DataResponse<InboxTalkPojo>>): InboxTalkViewModel {
        val body = response.body()
        if(body != null) {
            if (body.header == null ||
                    (body.header != null && body.header.messages.isEmpty()) ||
                    (body.header != null && body.header.messages[0].isBlank())) {
                val pojo: InboxTalkPojo = body.data
                return mapToViewModel(pojo)
            } else {
                throw MessageErrorException(body.header.messages[0])
            }
        } else {
            throw MessageErrorException("")
        }
    }

    public fun mapToViewModel(pojo: InboxTalkPojo): InboxTalkViewModel {
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
                pojo.paging.page_id,
                pojo.unreadCount)
    }

    private fun mapListThread(pojo: InboxTalkItemPojo): TalkThreadViewModel {

        val listComment = ArrayList<Visitable<*>>()

        val totalMoreComment = pojo.talk_total_comment.toInt() - pojo.list.size
        if (totalMoreComment > 1) {
            listComment.add(LoadMoreCommentTalkViewModel(totalMoreComment,
                    pojo.talk_id,
                    pojo.talk_shop_id,
                    pojo.talk_state.allow_reply))
        }

        for (data: TalkCommentItem in pojo.list) {
            listComment.add(ProductTalkItemViewModel(
                    getCommentUserImage(data),
                    getCommentUserName(data),
                    data.comment_create_time_list.date_time_android,
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
                    pojo.talk_product_id,
                    data.comment_user_label_id,
                    data.comment_user_label,
                    data.comment_user_id,
                    false,
                    false
            ))
        }

        return TalkThreadViewModel(
                ProductTalkItemViewModel(
                        pojo.talk_user_image,
                        pojo.talk_user_name,
                        pojo.talk_create_time_list.date_time_android,
                        pojo.talk_message,
                        mapHeaderTalkState(pojo),
                        pojo.talk_read_status == IS_READ,
                        pojo.talk_follow_status == IS_FOLLOWED,
                        ArrayList(),
                        pojo.talk_raw_message,
                        pojo.talk_own == 1,
                        pojo.talk_shop_id,
                        pojo.talk_id,
                        "",
                        pojo.talk_product_id,
                        pojo.talk_user_label_id,
                        pojo.talk_user_label,
                        pojo.talk_user_id,
                        false,
                        false
                ),
                listComment
        )
    }

    private fun getCommentUserName(data: TalkCommentItem): String {
        return if (data.comment_user_label_id == SELLER_LABEL_ID) data.comment_shop_name
        else data.comment_user_name
    }

    private fun getCommentUserImage(data: TalkCommentItem): String {
        return if (data.comment_user_label_id == SELLER_LABEL_ID) data.comment_shop_image
        else data.comment_user_image
    }


    private fun mapProductAttachment(pojo: TalkCommentItem):
            ArrayList<TalkProductAttachmentViewModel> {
        val listProduct = ArrayList<TalkProductAttachmentViewModel>()
        for (data: CommentProduct in pojo.listProduct) {
            listProduct.add(TalkProductAttachmentViewModel(
                    data.product_name,
                    data.product_id,
                    data.product_image,
                    data.product_price,
                    data.product_url
            ))
        }
        return listProduct
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
                pojo.talk_state.allow_unfollow
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
                pojo.comment_state.allow_unfollow
        )
    }

    private fun mapProductHeader(data: InboxTalkItemPojo): ProductHeader {
        return ProductHeader(
                data.talk_product_name,
                data.talk_product_image,
                data.talk_product_id,
                data.talk_product_url)
    }
}