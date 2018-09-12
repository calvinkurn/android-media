package com.tokopedia.talk.reporttalk.domain

import com.tokopedia.talk.common.data.TalkApi
import com.tokopedia.talk.common.domain.mapper.BaseActionMapper
import com.tokopedia.talk.common.view.BaseActionTalkViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 9/7/18.
 */

class ReportTalkUseCase @Inject constructor(val api: TalkApi,
                                            val mapper: BaseActionMapper) :
        UseCase<BaseActionTalkViewModel>() {
    override fun createObservable(requestParams: RequestParams): Observable<BaseActionTalkViewModel> {
        return api.followUnfollowTalk(requestParams.parameters).map(mapper)
    }

    companion object {

        private val PARAM_PRODUCT_ID: String = "product_id"
        private val PARAM_SHOP_ID: String = "shop_id"
        private val PARAM_TALK_ID: String = "talk_id"
        private val PARAM_MESSAGE: String = "text_message"

        fun getParam(
                productId: String,
                shopId: String,
                talkId: String,
                reportMessage: String): RequestParams {
            val requestParams: RequestParams = RequestParams.create()

            requestParams.putString(PARAM_PRODUCT_ID, productId)
            requestParams.putString(PARAM_SHOP_ID, shopId)
            requestParams.putString(PARAM_TALK_ID, talkId)
            requestParams.putString(PARAM_MESSAGE, reportMessage)

            return requestParams
        }
    }

}