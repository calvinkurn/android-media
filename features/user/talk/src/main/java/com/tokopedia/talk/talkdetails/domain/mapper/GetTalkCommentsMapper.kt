package com.tokopedia.talk.talkdetails.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.common.adapter.viewmodel.TalkProductAttachmentViewModel
import com.tokopedia.talk.common.domain.pojo.CommentProduct
import com.tokopedia.talk.common.domain.pojo.TalkCommentItem
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkItemViewModel
import com.tokopedia.talk.inboxtalk.view.viewmodel.ProductHeader
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkItemViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.TalkState
import com.tokopedia.talk.producttalk.view.viewmodel.TalkThreadViewModel
import com.tokopedia.talk.talkdetails.domain.pojo.TalkDetailsPojo
import com.tokopedia.talk.talkdetails.view.viewmodel.TalkDetailViewModel
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 9/3/18.
 */
class GetTalkCommentsMapper @Inject constructor() : Func1<Response<DataResponse<TalkDetailsPojo>>,
        TalkDetailViewModel> {

    private val IS_FOLLOWED = 1
    val SELLER_LABEL_ID = 3

    override fun call(response: Response<DataResponse<TalkDetailsPojo>>): TalkDetailViewModel {
        val body = response.body()
        if (body != null) {
            if (body.header == null ||
                    (body.header != null && body.header.messages.isEmpty()) ||
                    (body.header != null && body.header.messages[0].isBlank())) {
                val pojo: TalkDetailsPojo = body.data
                return mapToViewModel(pojo)
            } else {
                throw MessageErrorException(body.header.messages[0])
            }
        } else {
            throw MessageErrorException("")
        }
    }

    private fun mapToViewModel(pojo: TalkDetailsPojo): TalkDetailViewModel {
        val listTalk = ArrayList<Visitable<*>>()
        listTalk.add(InboxTalkItemViewModel(
                mapProductHeader(pojo),
                mapListThread(pojo)
        ))
        return TalkDetailViewModel(
                listTalk)
    }

    private fun mapListThread(pojo: TalkDetailsPojo): TalkThreadViewModel {

        val listTalk = ArrayList<Visitable<*>>()
        for (data: TalkCommentItem in pojo.list) {
            listTalk.add(ProductTalkItemViewModel(
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
                    pojo.talk.talk_product_id,
                    data.comment_user_label_id,
                    data.comment_user_label,
                    data.comment_user_id,
                    false,
                    true
            ))
        }

        return TalkThreadViewModel(
                ProductTalkItemViewModel(
                        pojo.talk.talk_user_image,
                        pojo.talk.talk_user_name,
                        pojo.talk.talk_create_time_list.date_time_android,
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
                        pojo.talk.talk_product_id,
                        pojo.talk.talk_user_label_id.toInt(),
                        pojo.talk.talk_user_label,
                        pojo.talk.talk_user_id,
                        false,
                        true
                ),
                listTalk
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
                pojo.talk.talk_state.allow_unfollow
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

    private fun mapProductHeader(data: TalkDetailsPojo): ProductHeader {
        return ProductHeader(
                data.talk.talk_product_name,
                data.talk.talk_product_image,
                data.talk.talk_product_id,
                data.talk.talk_product_url
        )
    }
}