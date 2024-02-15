package com.tokopedia.home_explore_category.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.home_explore_category.domain.mapper.ExploreCategoryMapper
import com.tokopedia.home_explore_category.domain.model.GetHomeExploreCategoryResponse
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryResultUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

const val GET_EXPLORE_CATEGORY_QUERY = """
    query getHomeCategoryV2(${'$'}types: Int, ${'$'}page: String) {
      getHomeCategoryV2(types: ${'$'}types, page: ${'$'}page) {
        categories {
          id
          title
          imageUrl
          desc
          categoryRows {
            id
            name
            url
            imageUrl
            applinks
            categoryLabel
            buIdentifier
          }
        }
      }
    }
"""

@GqlQuery("GetExploreCategoryQuery", GET_EXPLORE_CATEGORY_QUERY)
class GetExploreCategoryUseCase @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase<GetHomeExploreCategoryResponse>,
    private val exploreCategoryMapper: ExploreCategoryMapper
) {

    init {
        graphqlUseCase.setGraphqlQuery(GetExploreCategoryQuery())
        graphqlUseCase.setTypeClass(GetHomeExploreCategoryResponse::class.java)
    }

    suspend fun execute(): ExploreCategoryResultUiModel {
        graphqlUseCase.setRequestParams(createRequestParams())
        return exploreCategoryMapper.mapToExploreCategoryUiModelList(
            graphqlUseCase.executeOnBackground()
        )
    }

    private fun createRequestParams(): Map<String, Any> {
        return RequestParams.create().apply {
            putString(PARAM_PAGE, PAGE_VALUE)
            putInt(PARAM_TYPE, TYPE_VALUE)
        }.parameters
    }

    companion object {
        private const val PARAM_TYPE = "type"
        private const val PARAM_PAGE = "page"

        private const val TYPE_VALUE = 2
        private const val PAGE_VALUE = "jelajah"
    }
}
