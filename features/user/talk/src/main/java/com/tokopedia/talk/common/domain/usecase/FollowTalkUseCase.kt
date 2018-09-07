package com.tokopedia.talk.common.domain.usecase

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
class FollowTalkUseCase @Inject constructor(val api: TalkApi,
                                            val mapper: BaseActionMapper) :
        UseCase<BaseActionTalkViewModel>() {
    override fun createObservable(requestParams: RequestParams): Observable<BaseActionTalkViewModel> {
        return api.followTalk(requestParams.parameters).map(mapper)
    }

    companion object {

        private val PARAM_PRODUCT_ID: String = "product_id"
        private val PARAM_SHOP_ID: String = "shop_id"
        private val PARAM_TALK_ID: String = "talk_id"

        fun getParam(
                productId: String,
                shopId: String,
                talkId: String): RequestParams {
            val requestParams: RequestParams = RequestParams.create()

            requestParams.putString(PARAM_PRODUCT_ID, productId)
            requestParams.putString(PARAM_SHOP_ID, shopId)
            requestParams.putString(PARAM_TALK_ID, talkId)
            return requestParams
        }
    }

}