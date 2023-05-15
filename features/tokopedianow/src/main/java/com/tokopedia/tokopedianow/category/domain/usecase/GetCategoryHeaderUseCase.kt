package com.tokopedia.tokopedianow.category.domain.usecase

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.tokopedianow.category.domain.query.CategoryDetail
import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse
import com.tokopedia.tokopedianow.category.domain.response.CategoryHeaderResponse
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse
import com.tokopedia.tokopedianow.common.domain.query.GetCategoryListQuery
import com.tokopedia.usecase.coroutines.UseCase

class GetCategoryHeaderUseCase(
    private val graphqlUseCase: MultiRequestGraphqlUseCase,
): UseCase<CategoryHeaderResponse>() {

    internal companion object {
        private const val PARAM_CATEGORY_ID = "categoryID"
        private const val PARAM_WAREHOUSE_ID = "warehouseID"
        private const val PARAM_SLUG = "slug"
        private const val PARAM_DEPTH = "depth"

        private const val CATEGORY_LEVEL_DEPTH = 2
    }

    override suspend fun executeOnBackground(): CategoryHeaderResponse {
        val categoryId = useCaseRequestParams.parameters[PARAM_CATEGORY_ID] ?: String.EMPTY
        val warehouseId = useCaseRequestParams.parameters[PARAM_WAREHOUSE_ID] ?: String.EMPTY

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(
            createCategoryDetailRequest(
                categoryId = categoryId,
                warehouseId = warehouseId
            )
        )
        graphqlUseCase.addRequest(
            createCategoryNavigationRequest(
                warehouseId = warehouseId
            )
        )

        val response = graphqlUseCase.executeOnBackground()
        val categoryDetail = getCategoryDetail(response)
        val categoryNavigation = getCategoryNavigation(response)

        return CategoryHeaderResponse(
            categoryDetail = categoryDetail,
            categoryNavigation = categoryNavigation
        )
    }

    private fun getCategoryDetail(
        graphqlResponse: GraphqlResponse
    ): CategoryDetailResponse.CategoryDetail = graphqlResponse.getData<CategoryDetailResponse>(CategoryDetailResponse::class.java)?.categoryDetail ?: CategoryDetailResponse.CategoryDetail()

    private fun getCategoryNavigation(
        graphqlResponse: GraphqlResponse
    ): GetCategoryListResponse.CategoryListResponse = graphqlResponse.getData<GetCategoryListResponse>(GetCategoryListResponse::class.java)?.response ?: GetCategoryListResponse.CategoryListResponse()

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

    fun setParams(
        categoryId: String,
        warehouseId: String
    ) {
        useCaseRequestParams.apply {
            parameters[PARAM_CATEGORY_ID] = categoryId
            parameters[PARAM_WAREHOUSE_ID] = warehouseId
        }
    }

}
