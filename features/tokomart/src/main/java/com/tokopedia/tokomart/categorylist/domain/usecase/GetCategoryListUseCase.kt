package com.tokopedia.tokomart.categorylist.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomart.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokomart.categorylist.domain.model.GetCategoryListResponse
import com.tokopedia.tokomart.categorylist.domain.query.GetCategoryList
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetCategoryListUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<GetCategoryListResponse>(graphqlRepository) {

    companion object {
        private const val PARAM_WAREHOUSE_ID = "warehouseID"
        private const val PARAM_DEPTH = "depth"
    }

    init {
        setGraphqlQuery(GetCategoryList.QUERY)
        setTypeClass(GetCategoryListResponse::class.java)
    }

    suspend fun execute(warehouseId: String, depth: Int): List<CategoryResponse> {
        val requestParams = RequestParams.create().apply {
            putString(PARAM_WAREHOUSE_ID, warehouseId)
            putInt(PARAM_DEPTH, depth)
        }
        setRequestParams(requestParams.parameters)
        return executeOnBackground().response.data
    }
}