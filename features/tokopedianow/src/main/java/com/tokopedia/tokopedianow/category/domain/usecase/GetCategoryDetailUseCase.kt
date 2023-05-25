package com.tokopedia.tokopedianow.category.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.category.domain.query.CategoryDetail
import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetCategoryDetailUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) {
    companion object {
        const val PARAM_CATEGORY_ID = "categoryID"
        const val PARAM_SLUG = "slug"
        const val PARAM_WAREHOUSE_ID = "warehouseID"
    }

    private val graphql by lazy { GraphqlUseCase<CategoryDetailResponse>(graphqlRepository) }

    init {
        graphql.apply {
            setGraphqlQuery(CategoryDetail)
            setTypeClass(CategoryDetailResponse::class.java)
        }
    }

    suspend fun execute(
        warehouseId: String,
        categoryIdL1: String
    ): CategoryDetailResponse {
        return graphql.run {
            setRequestParams(
                RequestParams.create().apply {
                    putString(PARAM_CATEGORY_ID, categoryIdL1)
                    putString(PARAM_SLUG, String.EMPTY)
                    putString(PARAM_WAREHOUSE_ID, warehouseId)
                }.parameters
            )
            executeOnBackground()
        }
    }
}
