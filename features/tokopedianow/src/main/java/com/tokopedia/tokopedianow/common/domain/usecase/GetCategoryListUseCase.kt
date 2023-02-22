package com.tokopedia.tokopedianow.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetCategoryListUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<GetCategoryListResponse>(graphqlRepository) {

    companion object {
        private const val PARAM_WAREHOUSE_ID = "warehouseID"
        private const val PARAM_DEPTH = "depth"

        private val QUERY = """
            query TokonowCategoryTree(${'$'}warehouseID:String!, ${'$'}depth:Int!){
                TokonowCategoryTree(warehouseID:${'$'}warehouseID, depth:${'$'}depth){
                    header{
                        process_time
                        messages
                        reason
                        error_code
                    }
                    data{
                      id
                      name
                      url
                      isAdult
                      applinks
                      imageUrl
                      color
                      child {
                        id
                        name
                        url
                        applinks
                        imageUrl
                        parentID
                      }
                    }
                  }
                }
        """.trimIndent()
    }

    init {
        setGraphqlQuery(QUERY)
        setTypeClass(GetCategoryListResponse::class.java)
    }

    suspend fun execute(warehouseId: String, depth: Int): GetCategoryListResponse.CategoryListResponse {
        val requestParams = RequestParams.create().apply {
            putString(PARAM_WAREHOUSE_ID, warehouseId)
            putInt(PARAM_DEPTH, depth)
        }
        setRequestParams(requestParams.parameters)
        return executeOnBackground().response
    }
}
