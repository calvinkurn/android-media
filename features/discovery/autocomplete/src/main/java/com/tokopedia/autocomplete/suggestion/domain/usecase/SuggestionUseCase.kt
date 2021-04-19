package com.tokopedia.autocomplete.suggestion.domain.usecase

import android.text.TextUtils
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionResponse
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionUniverse
import com.tokopedia.autocomplete.util.UrlParamHelper
import com.tokopedia.discovery.common.constants.SearchConstant.GQL
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class SuggestionUseCase(
        private val graphqlUseCase: GraphqlUseCase
) : UseCase<SuggestionUniverse>() {

    companion object {
        internal const val GQL_QUERY = """
            query universe_suggestion(${'$'}params: String!) {
              universe_suggestion(param: ${'$'}params) {
                data {
                  id
                  name
                  items {
                    template
                    type
                    applink
                    url
                    title
                    subtitle
                    icon_title
                    icon_subtitle
                    shortcut_image
                    image_url
                    url_tracker
                    label
                    label_type
                    tracking {
                      code
                    }
                    discount_percentage
                    original_price
                  }
                }
                top_shops{
                  type
                  id
                  applink
                  url
                  title
                  subtitle
                  icon_title
                  icon_subtitle
                  url_tracker
                  image_url
                  products{
                    image_url
                  }
                }
              }
            }
        """

        private const val KEY_DEVICE = "device"
        private const val KEY_SOURCE = "source"
        private const val KEY_UNIQUE_ID = "unique_id"
        private const val KEY_COUNT = "count"
        private const val KEY_USER_ID = "user_id"
        private const val DEFAULT_DEVICE = "android"
        private const val DEFAULT_SOURCE = "searchbar"
        private const val DEFAULT_COUNT = "5"
        private const val DEVICE_ID = "device_id"
        private const val IS_TYPING = "is_typing"

        fun getParams(searchParameter: Map<String, Any>, registrationId: String, userId: String, isTyping: Boolean): RequestParams {
            val params = RequestParams.create()

            params.putAll(searchParameter)

            params.putString(KEY_DEVICE, DEFAULT_DEVICE)
            params.putString(KEY_SOURCE, DEFAULT_SOURCE)
            params.putString(KEY_COUNT, DEFAULT_COUNT)
            var uniqueId = AuthHelper.getMD5Hash(registrationId)
            if (!TextUtils.isEmpty(userId)) {
                uniqueId = AuthHelper.getMD5Hash(userId)
                params.putString(KEY_USER_ID, userId)
            }
            params.putString(KEY_UNIQUE_ID, uniqueId)
            params.putString(DEVICE_ID, registrationId)
            params.putBoolean(IS_TYPING, isTyping)

            return params
        }
    }

    @GqlQuery("SuggestionUseCaseQuery", GQL_QUERY)
    override fun createObservable(requestParams: RequestParams): Observable<SuggestionUniverse> {
        val params = UrlParamHelper.generateUrlParamString(requestParams.parameters)
        val graphqlRequest = GraphqlRequest(
                SuggestionUseCaseQuery.GQL_QUERY,
                SuggestionResponse::class.java,
                mapOf(GQL.KEY_PARAMS to params)
        )

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequests(listOf(graphqlRequest))

        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map {
                    it.getData<SuggestionResponse>(SuggestionResponse::class.java)?.suggestionUniverse ?: SuggestionUniverse()
                }
    }
}