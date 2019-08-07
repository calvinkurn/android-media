package com.tokopedia.feedcomponent.domain.usecase

import com.tokopedia.feedcomponent.data.pojo.mention.GetMentionableUserData
import com.tokopedia.feedcomponent.domain.mapper.MentionableUserMapper
import com.tokopedia.feedcomponent.util.toUrlParams
import com.tokopedia.feedcomponent.view.viewmodel.mention.MentionableUserViewModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by jegul on 2019-08-05.
 */

class GetMentionableUserUseCase @Inject constructor(
        @Named("search_profile_query") val query: String,
        val graphqlUseCase: GraphqlUseCase,
        val mentionableUserMapper: MentionableUserMapper
) : UseCase<List<MentionableUserViewModel>>() {

    override fun createObservable(requestParams: RequestParams): Observable<List<MentionableUserViewModel>> {
        val graphqlRequest = GraphqlRequest(query, GetMentionableUserData::class.java, requestParams.parameters)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(requestParams).map(mentionableUserMapper)
    }

    companion object {

        private const val PARAMS = "params"
        private const val PARAM_QUERY = "q"
        private const val PARAM_DEVICE = "device"
        private const val PARAM_SOURCE = "source"
        private const val PARAM_ROWS = "rows"

        @JvmOverloads
        fun getParam(query: String, rows: Int = 30): RequestParams {
            val params = RequestParams.create()
            params.putString(PARAMS,
                    mapOf(
                            PARAM_QUERY to query,
                            PARAM_DEVICE to "android",
                            PARAM_SOURCE to "search",
                            PARAM_ROWS to rows
                    ).toUrlParams()
            )
            return params
        }
    }
}