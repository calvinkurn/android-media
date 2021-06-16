package com.tokopedia.tokomart.category.domain.usecase

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.tokomart.category.domain.model.CategoryModel
import com.tokopedia.tokomart.category.domain.model.TokonowCategoryDetail
import com.tokopedia.tokomart.category.domain.model.TokonowCategoryDetail.CategoryDetail
import com.tokopedia.tokomart.searchcategory.data.createAceSearchProductRequest
import com.tokopedia.tokomart.searchcategory.data.createCategoryFilterRequest
import com.tokopedia.tokomart.searchcategory.data.createDynamicChannelRequest
import com.tokopedia.tokomart.searchcategory.data.createQuickFilterRequest
import com.tokopedia.tokomart.searchcategory.data.getTokonowQueryParam
import com.tokopedia.tokomart.searchcategory.data.mapper.getBanner
import com.tokopedia.tokomart.searchcategory.data.mapper.getCategoryFilter
import com.tokopedia.tokomart.searchcategory.data.mapper.getQuickFilter
import com.tokopedia.tokomart.searchcategory.data.mapper.getSearchProduct
import com.tokopedia.tokomart.searchcategory.utils.CATEGORY_ID
import com.tokopedia.tokomart.searchcategory.utils.CATEGORY_TOKONOW_DIRECTORY
import com.tokopedia.tokomart.searchcategory.utils.QUICK_FILTER_TOKONOW_DIRECTORY
import com.tokopedia.tokomart.searchcategory.utils.SLUG
import com.tokopedia.tokomart.searchcategory.utils.TOKONOW_CATEGORY
import com.tokopedia.tokomart.searchcategory.utils.WAREHOUSE_ID
import com.tokopedia.usecase.coroutines.UseCase

class GetCategoryFirstPageUseCase(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
): UseCase<CategoryModel>() {

    override suspend fun executeOnBackground(): CategoryModel {
        val queryParams = getTokonowQueryParam(useCaseRequestParams)
        val categoryFilterParams = createCategoryFilterParams(queryParams)
        val quickFilterParams = createQuickFilterParams(queryParams)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(createTokonowCategoryDetailRequest())
        graphqlUseCase.addRequest(createAceSearchProductRequest(queryParams))
        graphqlUseCase.addRequest(createCategoryFilterRequest(categoryFilterParams))
        graphqlUseCase.addRequest(createQuickFilterRequest(quickFilterParams))
        graphqlUseCase.addRequest(createDynamicChannelRequest(TOKONOW_CATEGORY))

        val graphqlResponse = graphqlUseCase.executeOnBackground()

        return CategoryModel(
                categoryDetail = getCategoryDetail(graphqlResponse),
                searchProduct = getSearchProduct(graphqlResponse),
                categoryFilter = getCategoryFilter(graphqlResponse),
                quickFilter = getQuickFilter(graphqlResponse),
                bannerChannel = getBanner(graphqlResponse),
        )
    }

    private fun createTokonowCategoryDetailRequest(): GraphqlRequest {
        val categoryID = useCaseRequestParams.parameters[CATEGORY_ID] ?: ""
        val warehouseID = useCaseRequestParams.parameters[WAREHOUSE_ID] ?: ""

        return GraphqlRequest(
                TOKONOW_CATEGORY_DETAIL_GQL_QUERY,
                TokonowCategoryDetail::class.java,
                mapOf(
                        CATEGORY_ID to categoryID,
                        SLUG to "",
                        WAREHOUSE_ID to warehouseID,
                )
        )
    }

    private fun createCategoryFilterParams(queryParams: Map<String?, Any>): Map<String?, Any> {
        return queryParams.toMutableMap().also {
            it[SearchApiConst.SOURCE] = CATEGORY_TOKONOW_DIRECTORY
        }
    }

    private fun createQuickFilterParams(queryParams: Map<String?, Any>): Map<String?, Any> {
        return queryParams.toMutableMap().also {
            it[SearchApiConst.SOURCE] = QUICK_FILTER_TOKONOW_DIRECTORY
        }
    }

    private fun getCategoryDetail(graphqlResponse: GraphqlResponse): CategoryDetail {
        return graphqlResponse
                .getData<TokonowCategoryDetail?>(TokonowCategoryDetail::class.java)
                ?.categoryDetail ?: CategoryDetail()
    }

    companion object {
        private const val TOKONOW_CATEGORY_DETAIL_GQL_QUERY = """
            query TokonowCategoryDetail(${'$'}categoryID: String!, ${'$'}slug: String!, ${'$'}warehouseID: String!){
              TokonowCategoryDetail(categoryID: ${'$'}categoryID, slug: ${'$'}slug, warehouseID: ${'$'}warehouseID) {
                header {
                  process_time
                  messages
                  reason
                  error_code
                }
                data {
                  id
                  name
                  url
                  applinks
                  imageUrl
                  navigation {
                    prev {
                      id
                      name
                      url
                      applinks
                      imageUrl
                    }
                    next {
                      id
                      name
                      url
                      applinks
                      imageUrl
                    }
                  }
                }
              }
            }
        """
    }
}