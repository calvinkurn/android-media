package com.tokopedia.feedcomponent.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.FeedQuery
import com.tokopedia.feedcomponent.domain.mapper.DynamicFeedMapper
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by milhamj on 18/12/18.
 */
class GetDynamicFeedUseCase @Inject constructor(@ApplicationContext private val context: Context,
                                                private val graphqlUseCase: GraphqlUseCase,
                                                private val dynamicPostMapper: DynamicFeedMapper)
    : UseCase<DynamicFeedDomainModel>() {

    override fun createObservable(requestParams: RequestParams?): Observable<DynamicFeedDomainModel> {
        val query: String = GraphqlHelper.loadRawString(
                context.resources,
                R.raw.query_feed_dynamic
        )

        val graphqlRequest = GraphqlRequest(query, FeedQuery::class.java, requestParams?.parameters)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map(dynamicPostMapper)
    }

    companion object {
        private const val PARAM_USER_ID = "userID"
        private const val PARAM_LIMIT = "limit"
        private const val PARAM_CURSOR = "cursor"
        private const val PARAM_SOURCE = "source"

        private const val LIMIT_3 = 3
        private const val SOURCE_FEEDS = "feeds"

        @JvmOverloads
        fun createRequestParams(userId: String, cursor: String = ""): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_USER_ID, userId)
            requestParams.putInt(PARAM_LIMIT, LIMIT_3)
            requestParams.putString(PARAM_CURSOR, cursor)
            requestParams.putString(PARAM_SOURCE, SOURCE_FEEDS)
            return requestParams
        }
    }
}