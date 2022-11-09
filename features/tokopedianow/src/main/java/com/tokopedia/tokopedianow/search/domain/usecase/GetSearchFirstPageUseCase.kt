package com.tokopedia.tokopedianow.search.domain.usecase

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS
import com.tokopedia.discovery.common.utils.UrlParamUtils.generateUrlParamString
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.tokopedianow.search.domain.model.SearchCategoryJumperModel
import com.tokopedia.tokopedianow.search.domain.model.SearchCategoryJumperModel.SearchCategoryJumperData
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.searchcategory.data.createAceSearchProductRequest
import com.tokopedia.tokopedianow.searchcategory.data.createCategoryFilterRequest
import com.tokopedia.tokopedianow.searchcategory.data.createDynamicChannelRequest
import com.tokopedia.tokopedianow.searchcategory.data.createFeedbackFieldToggleRequest
import com.tokopedia.tokopedianow.searchcategory.data.createQuickFilterRequest
import com.tokopedia.tokopedianow.searchcategory.data.getFeedbackFieldToggleData
import com.tokopedia.tokopedianow.searchcategory.data.getTokonowQueryParam
import com.tokopedia.tokopedianow.searchcategory.data.mapper.getBanner
import com.tokopedia.tokopedianow.searchcategory.data.mapper.getCategoryFilter
import com.tokopedia.tokopedianow.searchcategory.data.mapper.getQuickFilter
import com.tokopedia.tokopedianow.searchcategory.data.mapper.getSearchProduct
import com.tokopedia.tokopedianow.searchcategory.utils.CATEGORY_TOKONOW
import com.tokopedia.tokopedianow.searchcategory.utils.QUICK_FILTER_TOKONOW
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW_SEARCH
import com.tokopedia.usecase.coroutines.UseCase

class GetSearchFirstPageUseCase(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
): UseCase<SearchModel>() {

    override suspend fun executeOnBackground(): SearchModel {
        val queryParams = getTokonowQueryParam(useCaseRequestParams)
        val categoryFilterParams = createCategoryFilterParams(queryParams)
        val quickFilterParams = createQuickFilterParams(queryParams)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(createAceSearchProductRequest(queryParams))
        graphqlUseCase.addRequest(createCategoryFilterRequest(categoryFilterParams))
        graphqlUseCase.addRequest(createQuickFilterRequest(quickFilterParams))
        graphqlUseCase.addRequest(createDynamicChannelRequest(TOKONOW_SEARCH))
        graphqlUseCase.addRequest(createCategoryJumperRequest(queryParams))
        graphqlUseCase.addRequest(createFeedbackFieldToggleRequest())

        val graphqlResponse = graphqlUseCase.executeOnBackground()
        return SearchModel(
                searchProduct = getSearchProduct(graphqlResponse),
                categoryFilter = getCategoryFilter(graphqlResponse),
                quickFilter = getQuickFilter(graphqlResponse),
                bannerChannel = getBanner(graphqlResponse),
                searchCategoryJumper = getSearchCategoryJumper(graphqlResponse),
                feedbackFieldToggle = getFeedbackFieldToggleData(graphqlResponse)
        )
    }

    private fun createCategoryFilterParams(queryParams: Map<String?, Any>): Map<String?, Any> {
        return queryParams.toMutableMap().also {
            it[SearchApiConst.NAVSOURCE] = CATEGORY_TOKONOW
            it[SearchApiConst.SOURCE] = CATEGORY_TOKONOW
        }
    }

    private fun createQuickFilterParams(queryParams: Map<String?, Any>): Map<String?, Any> {
        return queryParams.toMutableMap().also {
            it[SearchApiConst.NAVSOURCE] = QUICK_FILTER_TOKONOW
            it[SearchApiConst.SOURCE] = QUICK_FILTER_TOKONOW
        }
    }

    private fun createCategoryJumperRequest(params: Map<String?, Any>) = GraphqlRequest(
            SEARCH_CATEGORY_JUMPER_QUERY,
            SearchCategoryJumperModel::class.java,
            mapOf(KEY_PARAMS to generateUrlParamString(params))
    )

    private fun getSearchCategoryJumper(
            graphqlResponse: GraphqlResponse
    ): SearchCategoryJumperData {

        return graphqlResponse
                .getData<SearchCategoryJumperModel?>(SearchCategoryJumperModel::class.java)
                ?.jumperData ?: SearchCategoryJumperData()
    }

    companion object {
        private const val SEARCH_CATEGORY_JUMPER_QUERY = """
            query SearchJumper(${'$'}params:String!){
              searchJumper(params:${'$'}params){
                data {
                  title
                  layout
                  data {
                    title
                    applink
                    url
                    image
                  }
                }
              }
            }
        """
    }
}
