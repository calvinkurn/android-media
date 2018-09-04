package com.tokopedia.talk.inboxtalk.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.ProductTalkItemViewModel
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

    override fun call(response: Response<DataResponse<InboxTalkPojo>>): InboxTalkViewModel {
        if (response.body().header.messages.isEmpty() ||
                response.body().header.messages[0].isBlank()) {
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

        val listTalk = ArrayList<ProductTalkItemViewModel>()
        for (data: TalkCommentItem in pojo.list) {
            listTalk.add(ProductTalkItemViewModel(
                    data.comment_user_image,
                    data.comment_user_name,
                    data.comment_create_time_fmt,
                    data.comment_message,
                    ArrayList()
            ))
        }

        return TalkThreadViewModel(
                ProductTalkItemViewModel(
                        pojo.talk_user_image,
                        pojo.talk_user_name,
                        pojo.talk_create_time_fmt,
                        pojo.talk_message,
                        ArrayList()
                ),
                listTalk
        )
    }

    private fun mapProductHeader(data: InboxTalkItemPojo): ProductHeader {
        return ProductHeader(
                data.talk_product_name,
                data.talk_product_image
        )
    }
}