package com.tokopedia.talk_old.addtalk.domain.usecase

import com.tokopedia.talk_old.addtalk.domain.mapper.CreateTalkMapper
import com.tokopedia.talk_old.common.data.TalkApi
import com.tokopedia.talk_old.producttalk.view.viewmodel.TalkThreadViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author : Steven 17/09/18
 */
class CreateTalkUsecase @Inject constructor(val api: TalkApi,
                                            val mapper: CreateTalkMapper) : UseCase<TalkThreadViewModel>(){
    override fun createObservable(requestParams: RequestParams): Observable<TalkThreadViewModel> {
        return api.createTalk(requestParams.parameters).map(mapper)
    }

    companion object {

        private val PARAM_USER_ID: String = "user_id"
        private val PARAM_PRODUCT_ID: String = "product_id"
        private val PARAM_COMMENT: String = "text_comment"

        fun getParam(
                userId: String,
                productId: String,
                comment: String): RequestParams {
            val requestParams: RequestParams = RequestParams.create()

            requestParams.putString(PARAM_USER_ID, userId)
            requestParams.putString(PARAM_PRODUCT_ID, productId)
            requestParams.putString(PARAM_COMMENT, comment)

            return requestParams
        }
    }
}