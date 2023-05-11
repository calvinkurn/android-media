package com.tokopedia.tokopedianow.category.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.category.domain.query.CategoryDetail
import com.tokopedia.tokopedianow.category.domain.response.CategoryModel
import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse
import com.tokopedia.tokopedianow.common.domain.query.GetCategoryListQuery
import com.tokopedia.usecase.coroutines.UseCase

class GetMainCategoryFirstPageUseCase(
    private val graphqlUseCase: MultiRequestGraphqlUseCase,
): UseCase<CategoryModel>() {

    internal companion object {
        private const val PARAM_CATEGORY_ID = "categoryID"
        private const val PARAM_WAREHOUSE_ID = "warehouseID"
        private const val PARAM_SLUG = "slug"
        private const val PARAM_DEPTH = "depth"

        private const val CATEGORY_LEVEL_DEPTH = 2
    }

    override suspend fun executeOnBackground(): CategoryModel {
        val categoryId = useCaseRequestParams.parameters[PARAM_CATEGORY_ID] ?: String.EMPTY
        val warehouseId = useCaseRequestParams.parameters[PARAM_WAREHOUSE_ID] ?: String.EMPTY

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(createCategoryDetailRequest(categoryId, warehouseId))
        graphqlUseCase.addRequest(createCategoryNavigationRequest(warehouseId))
        val graphqlResponse = graphqlUseCase.executeOnBackground()

        return CategoryModel(
            categoryDetail = getCategoryDetail(graphqlResponse),
            categoryNavigation = getCategoryNavigation(graphqlResponse)
        )
    }

    private fun getCategoryDetail(
        graphqlResponse: GraphqlResponse
    ): CategoryDetailResponse.CategoryDetail {
        return graphqlResponse.getData<CategoryDetailResponse>(CategoryDetailResponse::class.java)?.categoryDetail ?: CategoryDetailResponse.CategoryDetail()
    }

    private fun getCategoryNavigation(
        graphqlResponse: GraphqlResponse
    ): GetCategoryListResponse.CategoryListResponse {
        return graphqlResponse.getData<GetCategoryListResponse>(GetCategoryListResponse::class.java)?.response ?: GetCategoryListResponse.CategoryListResponse()
    }

    private fun createCategoryDetailRequest(
        categoryId: Any,
        warehouseId: Any
    ): GraphqlRequest = GraphqlRequest(
        gqlQueryInterface = CategoryDetail,
        typeOfT = CategoryDetailResponse::class.java,
        variables = mapOf(
            PARAM_CATEGORY_ID to categoryId,
            PARAM_SLUG to String.EMPTY,
            PARAM_WAREHOUSE_ID to warehouseId,
        )
    )

    private fun createCategoryNavigationRequest(
        warehouseId: Any,
    ): GraphqlRequest = GraphqlRequest(
        gqlQueryInterface = GetCategoryListQuery,
        typeOfT = GetCategoryListResponse::class.java,
        variables = mapOf(
            PARAM_WAREHOUSE_ID to warehouseId,
            PARAM_DEPTH to CATEGORY_LEVEL_DEPTH
        )
    )

    fun setParams(categoryId: String, warehouseId: String) {
        useCaseRequestParams.parameters[PARAM_CATEGORY_ID] = categoryId
        useCaseRequestParams.parameters[PARAM_WAREHOUSE_ID] = warehouseId
    }
}
