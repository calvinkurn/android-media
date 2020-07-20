package com.tokopedia.talk_old.producttalk.domain.usecase

import com.tokopedia.talk_old.common.data.TalkApi
import com.tokopedia.talk_old.producttalk.domain.mapper.ProductTalkListMapper
import com.tokopedia.talk_old.producttalk.view.viewmodel.ProductTalkViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 6/8/18.
 */
class GetProductTalkUseCase @Inject constructor(val api: TalkApi,
                                               val mapper: ProductTalkListMapper) : UseCase<ProductTalkViewModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<ProductTalkViewModel> {

        return api.getProductTalk(requestParams.parameters).map(mapper)
    }

    companion object {

        private val PARAM_PAGE: String = "page"
        private val PARAM_USER_ID: String = "user_id"
        private val PARAM_PRODUCT_ID: String = "product_id"
        private val PARAM_TYPE: String = "type"
        private val PARAM_COMMENT_ENABLED: String = "with_comment"

        fun getParam(
                userId: String,
                page: Int,
                productId: String): RequestParams {
            val requestParams: RequestParams = RequestParams.create()

            requestParams.putInt(PARAM_PAGE, page)
            requestParams.putString(PARAM_USER_ID, userId)
            requestParams.putString(PARAM_PRODUCT_ID, productId)
            requestParams.putString(PARAM_TYPE, "p")
            requestParams.putInt(PARAM_COMMENT_ENABLED, 1)

            return requestParams
        }
    }

}