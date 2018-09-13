package com.tokopedia.talk.talkdetails.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.common.adapter.viewmodel.TalkProductAttachmentViewModel
import com.tokopedia.talk.common.domain.CommentProduct
import com.tokopedia.talk.common.domain.InboxTalkItemPojo
import com.tokopedia.talk.common.domain.TalkCommentItem
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkItemViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.TalkState
import com.tokopedia.talk.talkdetails.domain.pojo.TalkDetailsPojo
import com.tokopedia.talk.talkdetails.view.viewmodel.TalkDetailsCommentViewModel
import com.tokopedia.talk.talkdetails.view.viewmodel.TalkDetailsHeaderProductViewModel
import com.tokopedia.talk.talkdetails.view.viewmodel.TalkDetailsThreadItemViewModel
import com.tokopedia.talk.talkdetails.view.viewmodel.TalkDetailsViewModel
import retrofit2.Response
import rx.functions.Func1

/**
 * Created by Hendri on 05/09/18.
 */
class GetTalkCommentsMapper: Func1<Response<DataResponse<TalkDetailsPojo>>, TalkDetailsViewModel> {
    private val IS_READ = 2
    private val IS_FOLLOWED = 1

    override fun call(response: Response<DataResponse<TalkDetailsPojo>>): TalkDetailsViewModel {
        if (response.body().header == null ||
                (response.body().header != null && response.body().header.messages.isEmpty()) ||
                (response.body().header != null && response.body().header.messages[0].isBlank())) {
            val pojo: TalkDetailsPojo = response.body().data
            return mapToViewModel(pojo)
        } else {
            throw MessageErrorException(response.body().header.messages[0])
        }
    }

    private fun mapToViewModel(pojo: TalkDetailsPojo): TalkDetailsViewModel {
        val listTalk = ArrayList<Visitable<*>>()
        listTalk.add(mapToTalk(pojo))
        listTalk.addAll(mapToCommentsList(pojo))
        return TalkDetailsViewModel(listTalk)
    }

    private fun mapToCommentsList(pojo:TalkDetailsPojo):List<ProductTalkItemViewModel> {
        var result = ArrayList<ProductTalkItemViewModel>()
        for(data in pojo.list){
            val products = ArrayList<TalkProductAttachmentViewModel>()
            for (product in data.listProduct) {
                products.add(TalkProductAttachmentViewModel(
                        productName = product.product_name,
                        productId = product.product_id,
                        productImage = product.product_image,
                        productPrice = product.product_price
                ))
            }
            result.add(ProductTalkItemViewModel(
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
                    pojo.talk.talk_product_id ))
        }
        return result
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

    private fun mapToTalk(pojo:TalkDetailsPojo):TalkDetailsThreadItemViewModel {
        return TalkDetailsThreadItemViewModel(
                id = pojo.talk.talk_id,
                avatar = pojo.talk.talk_user_image,
                name = pojo.talk.talk_user_name,
                comment = pojo.talk.talk_message,
                timestamp = pojo.talk.talk_create_time_fmt
        )
    }

    private fun mapToHeaderProduct(pojo: TalkDetailsPojo):TalkDetailsHeaderProductViewModel {

    }
}