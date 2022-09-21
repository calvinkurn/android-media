package com.tokopedia.tokofood.feature.search.searchresult.domain.usecase

import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.tokofood.feature.search.searchresult.domain.response.TokofoodFilterSortResponse
import javax.inject.Inject

private const val QUERY = """
        query TokofoodFilterAndSort(${'$'}type: String!) {
          tokofoodFilterAndSort(type: ${'$'}type) {
            filter {
              title
              subtitle
              templateName
              options {
                name
                description
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
        setCacheStrategy(createCacheStrategy())
    }

    suspend fun execute(type: String): DataValue {
        val params = generateParams(type)
        setRequestParams(params)
        return executeOnBackground().tokofoodFilterAndSort.apply {
            filter.onEach { filter ->
                filter.options.onEach { option ->
                    option.isPopular = true
                }
            }
        }
    }

    private fun createCacheStrategy(): GraphqlCacheStrategy {
        return GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
            .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_30.`val`())
            .setSessionIncluded(true)
            .build()
    }

    companion object {
        const val TYPE_QUICK = "quick"
        const val TYPE_DETAIL = "detail"

        private const val KEY_TYPE = "type"

        private fun generateParams(type: String): Map<String, Any> {
            return mapOf(KEY_TYPE to type)
        }
    }

}