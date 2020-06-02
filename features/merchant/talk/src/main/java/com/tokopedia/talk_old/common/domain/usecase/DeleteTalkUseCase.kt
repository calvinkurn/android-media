package com.tokopedia.talk_old.common.domain.usecase

import com.tokopedia.talk_old.common.data.TalkApi
import com.tokopedia.talk_old.common.domain.mapper.BaseActionMapper
import com.tokopedia.talk_old.common.view.BaseActionTalkViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 9/7/18.
 */

class DeleteTalkUseCase @Inject constructor(val api: TalkApi,
                                            val mapper: BaseActionMapper) :
        UseCase<BaseActionTalkViewModel>() {
    override fun createObservable(requestParams: RequestParams): Observable<BaseActionTalkViewModel> {
        return api.deleteTalk(requestParams.parameters).map(mapper)
    }

    companion object {

        private val PARAM_SHOP_ID: String = "shop_id"
        private val PARAM_TALK_ID: String = "talk_id"

        fun getParam(
                shopId: String,
                talkId: String): RequestParams {
            val requestParams: RequestParams = RequestParams.create()

            requestParams.putString(PARAM_SHOP_ID, shopId)
            requestParams.putString(PARAM_TALK_ID, talkId)
            return requestParams
        }
    }

}