package com.tokopedia.talk_old.shoptalk.domain

import com.tokopedia.talk_old.common.data.TalkApi
import com.tokopedia.talk_old.inboxtalk.view.viewmodel.InboxTalkViewModel
import com.tokopedia.talk_old.shoptalk.domain.mapper.GetShopTalkMapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 9/17/18.
 */

class GetShopTalkUseCase @Inject constructor(val api: TalkApi,
                                             val mapper: GetShopTalkMapper) :
        UseCase<InboxTalkViewModel>() {
    override fun createObservable(requestParams: RequestParams): Observable<InboxTalkViewModel> {
        return api.getShopTalk(requestParams.parameters).map(mapper)
    }

    companion object {

        private val PARAM_SHOP_ID: String = "shop_id"
        private val PARAM_PAGE: String = "page"
        private val PARAM_PER_PAGE: String = "per_page"
        private val PARAM_PAGE_ID: String = "page_id"
        private val PARAM_TYPE: String = "type"
        private val PARAM_WITH_COMMENT: String = "with_comment"


        fun getParam(shopId: String, page: Int, page_id: Int): RequestParams {
            val requestParams: RequestParams = RequestParams.create()

            requestParams.putString(PARAM_SHOP_ID, shopId)
            requestParams.putString(PARAM_TYPE, "s")
            requestParams.putInt(PARAM_WITH_COMMENT, 1)
            requestParams.putInt(PARAM_PAGE, page)
            requestParams.putInt(PARAM_PER_PAGE, 10)
            requestParams.putInt(PARAM_PAGE_ID, page_id)
            return requestParams
        }
    }

}