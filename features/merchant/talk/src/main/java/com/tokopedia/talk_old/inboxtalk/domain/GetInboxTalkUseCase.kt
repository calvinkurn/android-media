package com.tokopedia.talk_old.inboxtalk.domain

import com.tokopedia.talk_old.common.data.TalkApi
import com.tokopedia.talk_old.inboxtalk.domain.mapper.GetInboxTalkMapper
import com.tokopedia.talk_old.inboxtalk.view.viewmodel.InboxTalkViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 9/3/18.
 */
class GetInboxTalkUseCase @Inject constructor(val api: TalkApi,
                                              val mapper: GetInboxTalkMapper) : UseCase<InboxTalkViewModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<InboxTalkViewModel> {

        return api.getInboxTalk(requestParams.parameters).map(mapper)
    }

    companion object {

        private val PARAM_FILTER: String = "filter"
        private val PARAM_NAV: String = "nav"
        private val PARAM_PAGE: String = "page"
        private val PARAM_PER_PAGE: String = "per_page"
        private val PARAM_PAGE_ID: String = "page_id"
        private val PARAM_WITH_COMMENT: String = "with_comment"
        private val TOTAL_COMMENT: Int = 1
        private val DEFAULT_PER_PAGE: Int = 10

        fun getParam(
                filter: String,
                nav: String,
                page: Int,
                page_id: Int): RequestParams {
            val requestParams: RequestParams = RequestParams.create()

            requestParams.putString(PARAM_FILTER, filter)
            requestParams.putString(PARAM_NAV, nav)
            requestParams.putInt(PARAM_PAGE, page)
            requestParams.putInt(PARAM_PER_PAGE, DEFAULT_PER_PAGE)
            requestParams.putInt(PARAM_PAGE_ID, page_id)
            requestParams.putInt(PARAM_WITH_COMMENT, TOTAL_COMMENT)
            return requestParams
        }
    }

}