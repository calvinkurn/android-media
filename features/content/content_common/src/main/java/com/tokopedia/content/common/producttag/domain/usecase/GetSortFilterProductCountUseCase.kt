package com.tokopedia.content.common.producttag.domain.usecase

import com.tokopedia.content.common.producttag.model.GetSortFilterProductCountResponse
import com.tokopedia.content.common.producttag.model.GetSortFilterResponse
import com.tokopedia.content.common.producttag.view.uimodel.SearchParamUiModel
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 18, 2022
 */
@GqlQuery(GetSortFilterProductCountUseCase.QUERY_NAME, GetSortFilterProductCountUseCase.QUERY)
class GetSortFilterProductCountUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<GetSortFilterProductCountResponse>(gqlRepository) {

    init {
        setGraphqlQuery(GetSortFilterProductCountUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetSortFilterProductCountResponse::class.java)
    }

    companion object {
        private const val KEY_PARAMS = "params"

        const val QUERY_NAME = "GetSortFilterProductCountUseCaseQuery"
        const val QUERY = """
            query GetSortFilterProductCountUseCase(${"$$KEY_PARAMS"}: String!) {
              searchProduct($KEY_PARAMS: ${"$$KEY_PARAMS"}) {
                count_text
              }
            }
        """

        fun createParams(
            param: SearchParamUiModel,
        ): Map<String, Any> {
            return mapOf<String, Any>(
                KEY_PARAMS to param.joinToString()
            )
        }
    }
}