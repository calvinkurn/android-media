package com.tokopedia.tokofood.feature.search.searchresult.domain.usecase

import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokofood.feature.search.searchresult.domain.response.TokofoodFilterSortResponse
import javax.inject.Inject

private const val QUERY = """
        query TokofoodFilterAndSort(${'$'}type: String!) {
          tokofoodFilterAndSort(type: ${'$'}type) {
            filter {
              title
              subtitle
              options {
                name
                key
                value
                inputType
                isNew
              }
            }
            sort {
              name
              key
              value
              inputType
              applyFilter
            }
          }
        }
    """

@GqlQuery("TokofoodFilterAndSort", QUERY)
class TokofoodFilterSortUseCase @Inject constructor(
    repository: GraphqlRepository
): GraphqlUseCase<TokofoodFilterSortResponse>(repository) {

    init {
        setTypeClass(TokofoodFilterSortResponse::class.java)
        setGraphqlQuery(TokofoodFilterAndSort())
    }

    suspend fun execute(type: String): DataValue {
        val params = generateParams(type)
        setRequestParams(params)
        return executeOnBackground().tokofoodFilterAndSort
    }

    companion object {
        private const val KEY_TYPE = "type"

        private fun generateParams(type: String): Map<String, Any> {
            return mapOf(KEY_TYPE to type)
        }
    }

}