package com.tokopedia.talk_old.talkdetails.domain.usecase

import com.tokopedia.talk_old.common.adapter.viewmodel.TalkProductAttachmentViewModel
import com.tokopedia.talk_old.talkdetails.data.SendCommentResponse
import com.tokopedia.talk_old.talkdetails.data.api.DetailTalkApi
import com.tokopedia.talk_old.talkdetails.domain.mapper.SendCommentMapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * Created by Hendri on 05/09/18.
 */
class SendCommentsUseCase @Inject constructor(val api: DetailTalkApi,
                                              val mapper: SendCommentMapper) : UseCase<SendCommentResponse>() {
    override fun createObservable(requestParams: RequestParams): Observable<SendCommentResponse> {
        return api.sendComment(requestParams.parameters).map(mapper)
    }

    companion object {
        fun getParameters(talkId: String,
                          attachProducts: List<TalkProductAttachmentViewModel>,
                          message: String): RequestParams {
            val requestParams: RequestParams = RequestParams.create()
            requestParams.putString("talk_id", talkId)
            requestParams.putString("text_comment", message)
            requestParams.putString("product_ids", createAttachProductPayload(attachProducts))
            return requestParams

        }

        private fun createAttachProductPayload(attachProducts:
                                               List<TalkProductAttachmentViewModel>): String {
            return if (attachProducts.isEmpty()) {
                ""
            } else {
                var productIds = "{"

                for (product in attachProducts) {
                    productIds += product.productId
                    productIds += ","
                }

                productIds = if (productIds.endsWith(",")) productIds.substring(0, productIds.lastIndexOf(",")) else productIds
                productIds += "}"
                productIds
            }
        }
    }
}