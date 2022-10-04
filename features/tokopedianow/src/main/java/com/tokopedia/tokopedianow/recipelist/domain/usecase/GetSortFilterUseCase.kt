package com.tokopedia.tokopedianow.recipelist.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopedianow.recipelist.domain.model.RecipeFilterSortDataResponse
import com.tokopedia.tokopedianow.recipelist.domain.model.TokonowRecipesFilterSort
import com.tokopedia.tokopedianow.recipelist.domain.query.GetSortFilter
import com.tokopedia.tokopedianow.recipelist.domain.query.GetSortFilter.QUERY_PARAMS
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/***
 * Docs:
 * https://tokopedia.atlassian.net/wiki/spaces/TokoNow/pages/2051344215/Query+TokonowRecipesFilterSort+GQL
 */
class GetSortFilterUseCase @Inject constructor(gqlRepository: GraphqlRepository) {

    private val graphql by lazy { GraphqlUseCase<TokonowRecipesFilterSort>(gqlRepository) }

    suspend fun execute(params: Map<String, String?>): RecipeFilterSortDataResponse {
        graphql.apply {
            setGraphqlQuery(GetSortFilter)
            setTypeClass(TokonowRecipesFilterSort::class.java)

            val queryParams = generateQueryParams(params)
            val requestParams = RequestParams().apply {
                putString(QUERY_PARAMS, queryParams)
            }.parameters
            setRequestParams(requestParams)

            val request = executeOnBackground()
            val response = request.response
            val header = response.header

            return if (header.success) {
                response.data
            } else {
                throw MessageErrorException(header.message)
            }
        }
    }

    private fun generateQueryParams(params: Map<String, String?>): String {
        val stringBuilder = StringBuilder()

        for ((key, value) in params) {
            if (!value.isNullOrBlank()) {
                if (stringBuilder.isNotBlank()) {
                    stringBuilder.append("&")
                }
                stringBuilder.append("$key=$value")
            }
        }

        return stringBuilder.toString()
    }
}