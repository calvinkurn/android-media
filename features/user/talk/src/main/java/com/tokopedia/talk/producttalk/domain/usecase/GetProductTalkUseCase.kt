package com.tokopedia.talk.producttalk.domain.usecase

import com.tokopedia.talk.producttalk.domain.mapper.ProductTalkListMapper
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkListViewModel
import com.tokopedia.talk.producttalk.view.data.ProductTalkApi
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * @author by nisie on 6/8/18.
 */
class GetProductTalkUseCase(val api: ProductTalkApi,
                            val mapper: ProductTalkListMapper) : UseCase<ProductTalkListViewModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<ProductTalkListViewModel> {

        return api.getProductTalk(requestParams.parameters).map(mapper)
    }

    companion object {

        private val PARAM_PAGE: String = "page"
        private val PARAM_USER_ID: String = "user_id"

        fun getParam(
                userId: String,
                page: Int): RequestParams {
            val requestParams: RequestParams = RequestParams.create()

            requestParams.putInt(PARAM_PAGE, page)
            requestParams.putString(PARAM_USER_ID, userId)

            return requestParams
        }
    }

}