package com.tokopedia.tokopedianow.category.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopedianow.category.domain.query.CategoryDetail
import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse
import com.tokopedia.tokopedianow.common.domain.model.WarehouseData
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetCategoryDetailUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) {
    companion object {
        private const val SUCCESS_CODE = "200"
        private const val DEFAULT_CODE = "0"
        private const val CATEGORY_PAGE_L1_SOURCE = "category-page-l1"

        const val PARAM_CATEGORY_ID = "categoryID"
        const val PARAM_SLUG = "slug"
        const val PARAM_SOURCE = "source"
        const val PARAM_WAREHOUSES = "warehouses"
    }

    private val graphql by lazy { GraphqlUseCase<CategoryDetailResponse>(graphqlRepository) }

    init {
        graphql.apply {
            setGraphqlQuery(CategoryDetail)
            setTypeClass(CategoryDetailResponse::class.java)
        }
    }

    suspend fun execute(
        warehouses: List<WarehouseData>,
        categoryIdL1: String
    ): CategoryDetailResponse {
        graphql.setRequestParams(
            RequestParams.create().apply {
                putString(PARAM_CATEGORY_ID, categoryIdL1)
                putString(PARAM_SLUG, String.EMPTY)
                putString(PARAM_SOURCE, CATEGORY_PAGE_L1_SOURCE)
                putObject(PARAM_WAREHOUSES, warehouses)
            }.parameters
        )
        val detailResponse = graphql.executeOnBackground()
        val headerDetailResponse = detailResponse.categoryDetail.header
        if (headerDetailResponse.errorCode == SUCCESS_CODE || headerDetailResponse.errorCode == DEFAULT_CODE) {
            return detailResponse
        } else {
            throw MessageErrorException(
                headerDetailResponse.messages.firstOrNull().orEmpty(),
                headerDetailResponse.errorCode
            )
        }
    }
}
