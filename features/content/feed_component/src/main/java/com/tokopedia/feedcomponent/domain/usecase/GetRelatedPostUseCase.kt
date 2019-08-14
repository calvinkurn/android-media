package com.tokopedia.feedcomponent.domain.usecase

import com.tokopedia.feedcomponent.data.pojo.FeedPostRelated
import com.tokopedia.feedcomponent.data.pojo.FeedPostRelatedQuery
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

/**
 * Use case to get list of related other profiles given the activityId
 * ActivityId may be blank
 *
 * Example case: when user see the blank KOL profile, it will show other peoples' profile in list
 */
class GetRelatedPostUseCase @Inject constructor(@Named(RELATED_POST_KEY) private val query: String,
                                                graphqlUseCaseProvider: Provider<GraphqlUseCase>)
    : UseCase<FeedPostRelated>() {

    val graphqlUseCase: GraphqlUseCase = graphqlUseCaseProvider.get()

    init {
        graphqlUseCase.setCacheStrategy(
            GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD)
                .build()
        )
    }

    override fun createObservable(requestParams: RequestParams?): Observable<FeedPostRelated> {
        val graphqlRequest = GraphqlRequest(query, FeedPostRelatedQuery::class.java, requestParams?.parameters)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(requestParams)
            .map {
                val query = it.getData<FeedPostRelatedQuery>(FeedPostRelatedQuery::class.java)
                query.feedPostRelated
            }
    }

    companion object {
        private const val PARAM_ACTIVITY_ID = "activityID"
        const val RELATED_POST_KEY = "relatedPost"

        @JvmOverloads
        @JvmStatic
        fun createRequestParams(activityId: String? = ""): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_ACTIVITY_ID, activityId ?: "")
            return requestParams
        }
    }
}