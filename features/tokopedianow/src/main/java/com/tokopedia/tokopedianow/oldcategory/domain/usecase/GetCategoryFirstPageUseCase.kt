package com.tokopedia.tokopedianow.oldcategory.domain.usecase

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.tokopedianow.oldcategory.domain.model.TokonowCategoryDetail
import com.tokopedia.tokopedianow.oldcategory.domain.model.TokonowCategoryDetail.CategoryDetail
import com.tokopedia.tokopedianow.oldcategory.domain.model.CategoryModel
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase.Companion.CATEGORY_PAGE
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase.Companion.createGetTargetedTickerRequest
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase.Companion.getTargetedTickerResponse
import com.tokopedia.tokopedianow.searchcategory.data.createAceSearchProductRequest
import com.tokopedia.tokopedianow.searchcategory.data.createCategoryFilterRequest
import com.tokopedia.tokopedianow.searchcategory.data.createDynamicChannelRequest
import com.tokopedia.tokopedianow.searchcategory.data.createFeedbackFieldToggleRequest
import com.tokopedia.tokopedianow.searchcategory.data.createQuickFilterRequest
import com.tokopedia.tokopedianow.searchcategory.data.createRepurchaseWidgetRequest
import com.tokopedia.tokopedianow.searchcategory.data.getFeedbackFieldToggleData
import com.tokopedia.tokopedianow.searchcategory.data.getTokonowQueryParam
import com.tokopedia.tokopedianow.searchcategory.data.mapper.getBanner
import com.tokopedia.tokopedianow.searchcategory.data.mapper.getCategoryFilter
import com.tokopedia.tokopedianow.searchcategory.data.mapper.getQuickFilter
import com.tokopedia.tokopedianow.searchcategory.data.mapper.getRepurchaseWidget
import com.tokopedia.tokopedianow.searchcategory.data.mapper.getSearchProduct
import com.tokopedia.tokopedianow.searchcategory.utils.CATEGORY_ID
import com.tokopedia.tokopedianow.searchcategory.utils.CATEGORY_TOKONOW_DIRECTORY
import com.tokopedia.tokopedianow.searchcategory.utils.QUICK_FILTER_TOKONOW_DIRECTORY
import com.tokopedia.tokopedianow.searchcategory.utils.SLUG
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW_CATEGORY
import com.tokopedia.tokopedianow.searchcategory.utils.WAREHOUSE_ID
import com.tokopedia.usecase.coroutines.UseCase

class GetCategoryFirstPageUseCase(
    private val graphqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<CategoryModel>() {

    override suspend fun executeOnBackground(): CategoryModel {
        val parameters = useCaseRequestParams.parameters
        val queryParams = getTokonowQueryParam(useCaseRequestParams)
        val categoryFilterParams = createCategoryFilterParams(queryParams)
        val quickFilterParams = createQuickFilterParams(queryParams)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(
            request = createGetTargetedTickerRequest(
                page = CATEGORY_PAGE,
                warehouseId = parameters[SearchApiConst.USER_WAREHOUSE_ID].toString()
            )
        )
        graphqlUseCase.addRequest(createTokonowCategoryDetailRequest())
        graphqlUseCase.addRequest(createAceSearchProductRequest(queryParams))
        graphqlUseCase.addRequest(createCategoryFilterRequest(categoryFilterParams))
        graphqlUseCase.addRequest(createQuickFilterRequest(quickFilterParams))
        graphqlUseCase.addRequest(createDynamicChannelRequest(TOKONOW_CATEGORY))
        graphqlUseCase.addRequest(createRepurchaseWidgetRequest(useCaseRequestParams.parameters))
        graphqlUseCase.addRequest(createFeedbackFieldToggleRequest())

        val graphqlResponse = graphqlUseCase.executeOnBackground()

        return CategoryModel(
            targetedTicker = getTargetedTickerResponse(graphqlResponse),categoryDetail = getCategoryDetail(graphqlResponse),
            searchProduct = getSearchProduct(graphqlResponse),
            categoryFilter = getCategoryFilter(graphqlResponse),
            quickFilter = getQuickFilter(graphqlResponse),
            bannerChannel = getBanner(graphqlResponse),
            tokonowRepurchaseWidget = getRepurchaseWidget(graphqlResponse),
            feedbackFieldToggle = getFeedbackFieldToggleData(graphqlResponse)
        )
    }

    private fun createTokonowCategoryDetailRequest(): GraphqlRequest {
        val categoryID = useCaseRequestParams.parameters[CATEGORY_ID] ?: ""
        val warehouseId = useCaseRequestParams.parameters[WAREHOUSE_ID] ?: ""

        return GraphqlRequest(
            TOKONOW_CATEGORY_DETAIL_GQL_QUERY,
            TokonowCategoryDetail::class.java,
            mapOf(
                CATEGORY_ID to categoryID,
                SLUG to "",
                WAREHOUSE_ID to warehouseId
            )
        )
    }

    private fun createCategoryFilterParams(queryParams: Map<String?, Any>): Map<String?, Any> {
        return queryParams.toMutableMap().also {
            it[SearchApiConst.NAVSOURCE] = CATEGORY_TOKONOW_DIRECTORY
            it[SearchApiConst.SOURCE] = CATEGORY_TOKONOW_DIRECTORY
        }
    }

    private fun createQuickFilterParams(queryParams: Map<String?, Any>): Map<String?, Any> {
        return queryParams.toMutableMap().also {
            it[SearchApiConst.NAVSOURCE] = QUICK_FILTER_TOKONOW_DIRECTORY
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
