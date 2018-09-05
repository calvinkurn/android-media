package com.tokopedia.talk.producttalk.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.common.domain.InboxTalkItemPojo
import com.tokopedia.talk.common.domain.InboxTalkPojo
import com.tokopedia.talk.common.domain.TalkCommentItem
import com.tokopedia.talk.producttalk.view.adapter.ProductTalkListTypeFactory
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkItemViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.TalkThreadViewModel
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by Steven.
 */
class ProductTalkListMapper @Inject constructor(): Func1<Response<DataResponse<InboxTalkPojo>>,
        ProductTalkViewModel> {


    override fun call(response: Response<DataResponse<InboxTalkPojo>>): ProductTalkViewModel {
        if (response.body()!= null ){
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

        val listThread = ArrayList<Visitable<ProductTalkListTypeFactory>>()

        for (data: InboxTalkItemPojo in pojo.list) {
            listThread.add(mapThread(data))
        }
        return ProductTalkViewModel("",
                listThread,
                pojo.paging.has_next,
                pojo.paging.page_id)
    }

    private fun mapThread(pojo: InboxTalkItemPojo): TalkThreadViewModel {

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


}