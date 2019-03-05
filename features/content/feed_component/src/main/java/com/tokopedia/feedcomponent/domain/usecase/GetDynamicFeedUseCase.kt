package com.tokopedia.feedcomponent.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.FeedQuery
import com.tokopedia.feedcomponent.domain.mapper.DynamicFeedMapper
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
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

    var queryRaw = R.raw.query_feed_dynamic;
    init {
        graphqlUseCase.setCacheStrategy(
                GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                        .setExpiryTime(GraphqlConstant.ExpiryTimes.WEEK.`val`())
                        .setSessionIncluded(true)
                        .build()
        )
    }

    override fun createObservable(requestParams: RequestParams?): Observable<DynamicFeedDomainModel> {
        val query: String = GraphqlHelper.loadRawString(
                context.resources,
                queryRaw
        )

        val graphqlRequest = GraphqlRequest(query, FeedQuery::class.java, requestParams?.parameters)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map(dynamicPostMapper)
    }

    public fun setQuery(queryRes: Int) {
        queryRaw = queryRes
    }

    companion object {
        private const val PARAM_USER_ID = "userID"
        private const val PARAM_LIMIT = "limit"
        private const val PARAM_CURSOR = "cursor"
        private const val PARAM_SOURCE = "source"

        private const val LIMIT_3 = 3
        const val SOURCE_FEEDS = "feeds"
        const val SOURCE_PROFILE = "profile"

        @JvmOverloads
        fun createRequestParams(userId: String, cursor: String = "", source: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_USER_ID, userId)
            requestParams.putInt(PARAM_LIMIT, LIMIT_3)
            requestParams.putString(PARAM_CURSOR, cursor)
            requestParams.putString(PARAM_SOURCE, source)
            return requestParams
        }
    }
}