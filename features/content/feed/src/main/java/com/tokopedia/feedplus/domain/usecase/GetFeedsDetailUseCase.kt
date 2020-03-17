package com.tokopedia.feedplus.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.data.pojo.FeedQuery
import com.tokopedia.feedplus.view.subscriber.FeedDetailSubscriber
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * @author by nisie on 5/24/17.
 */
class GetFeedsDetailUseCase @Inject internal constructor(@param:ApplicationContext private val context: Context,
                                                         private val graphqlUseCase: GraphqlUseCase?) {
    fun execute(requestParams: RequestParams, subscriber: FeedDetailSubscriber) {
        graphqlUseCase?.run {
            clearRequest()
            val query = GraphqlHelper.loadRawString(context.resources, R.raw.query_feed_detail)
            val variables: Map<String, Any> = requestParams.parameters
            val feedDetailGraphqlRequest = GraphqlRequest(query,
                    FeedQuery::class.java,
                    variables, false)
            addRequest(feedDetailGraphqlRequest)
            execute(subscriber)
        }
    }

    fun unsubscribe() {
        graphqlUseCase?.unsubscribe()
    }

    companion object {
        private const val PARAM_DETAIL_ID = "detailID"
        private const val PARAM_PAGE = "pageDetail"
        private const val PARAM_USER_ID = "userID"
        private const val PARAM_LIMIT_DETAIL = "limitDetail"
        private const val LIMIT_DETAIL = 30

        @JvmStatic
        fun getFeedDetailParam(loginID: String?, detailId: String?, page: Int): RequestParams {
            val params = RequestParams.create()
            params.putInt(PARAM_USER_ID, loginID?.toIntOrNull() ?: 0)
            params.putString(PARAM_DETAIL_ID, detailId)
            params.putInt(PARAM_PAGE, page)
            params.putInt(PARAM_LIMIT_DETAIL, LIMIT_DETAIL)
            return params
        }
    }

}