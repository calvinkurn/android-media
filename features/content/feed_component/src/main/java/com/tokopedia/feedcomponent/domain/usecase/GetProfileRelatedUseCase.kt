package com.tokopedia.feedcomponent.domain.usecase

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.FeedPostRelated
import com.tokopedia.feedcomponent.data.pojo.ProfileRelatedQuery
import com.tokopedia.graphql.GraphqlConstant
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
class GetProfileRelatedUseCase @Inject constructor(@Named(PROFILE_RELATED_KEY) private val query: String,
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
        val graphqlRequest = GraphqlRequest(query, ProfileRelatedQuery::class.java, requestParams?.parameters)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(requestParams)
            .map {
                val data: FeedPostRelated = (it.getData(ProfileRelatedQuery::class.java)
                    as ProfileRelatedQuery).feedPostRelated
                data
            }
    }

    companion object {
        const val PARAM_ACTIVITY_ID = "activityID"
        const val PROFILE_RELATED_KEY = "profileRelated"
        @JvmOverloads
        fun createRequestParams(activityId: String?):
            RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_ACTIVITY_ID, activityId ?: "")
            return requestParams
        }
    }
}