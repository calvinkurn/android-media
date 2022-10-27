package com.tokopedia.content.common.producttag.domain.usecase

import com.tokopedia.content.common.producttag.model.FeedQuickFilterResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 13, 2022
 */
@GqlQuery(FeedQuickFilterUseCase.QUERY_NAME, FeedQuickFilterUseCase.QUERY)
class FeedQuickFilterUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<FeedQuickFilterResponse>(gqlRepository) {

    init {
        setGraphqlQuery(FeedQuickFilterUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(FeedQuickFilterResponse::class.java)
    }

    companion object {
        private const val KEY_QUERY = "query"
        private const val KEY_EXTRA_PARAMS = "extraParams"

        const val QUERY_NAME = "FeedQuickFilterUseCaseQuery"
        const val QUERY = """
            query FeedQuickFilterUseCase(${"$$KEY_QUERY"}: String!, ${"$$KEY_EXTRA_PARAMS"}: String!) {
              quick_filter(
                $KEY_QUERY: ${"$$KEY_QUERY"},
                $KEY_EXTRA_PARAMS: ${"$$KEY_EXTRA_PARAMS"}
              ) {
                filter {
                  title
                  template_name
                  options {
                    name
                    key
                    icon
                    value
                  }
                }
              }
            }
        """

        fun createParams(
            query: String,
            extraParams: String,
        ): Map<String, Any> {
            return mapOf<String, Any>(
                KEY_QUERY to query,
                KEY_EXTRA_PARAMS to extraParams
            )
        }
    }
}