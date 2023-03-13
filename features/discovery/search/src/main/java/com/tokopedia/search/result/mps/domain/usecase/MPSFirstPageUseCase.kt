package com.tokopedia.search.result.mps.domain.usecase

import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PAGE_SOURCE
import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS
import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_SOURCE
import com.tokopedia.discovery.common.constants.SearchConstant.GQL.PAGE_SOURCE_SEARCH_SHOP
import com.tokopedia.discovery.common.constants.SearchConstant.GQL.SOURCE_QUICK_FILTER
import com.tokopedia.filter.common.helper.FilterSortProduct
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.search.result.mps.domain.model.MPSQuickFilterModel
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.result.mps.domain.model.MPSModel.AceSearchShopMPS
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.usecase.coroutines.UseCase

class MPSFirstPageUseCase(
    private val graphqlUseCase: MultiRequestGraphqlUseCase,
): UseCase<MPSModel>() {

    override suspend fun executeOnBackground(): MPSModel = graphqlUseCase.run {
        clearRequest()
        addRequests(listOf(
            mpsRequest(),
            quickFilterRequest()
        ))

        val graphqlResponse = executeOnBackground()

        MPSModel(
            aceSearchShopMPS = graphqlResponse.getSearchShopMPS(),
            quickFilterModel = graphqlResponse.getQuickFilter().quickFilterModel,
        )
    }

    @GqlQuery("MPSFirstPageQuery", MPS_GQL_QUERY)
    private fun mpsRequest(): GraphqlRequest {
        val requestParams = useCaseRequestParams.parameters

        return GraphqlRequest(
            MPSFirstPageQuery(),
            AceSearchShopMPS::class.java,
            mapOf(
                KEY_PARAMS to UrlParamUtils.generateUrlParamString(requestParams)
            )
        )
    }

    private fun quickFilterRequest(): GraphqlRequest {
        val quickFilterParams = useCaseRequestParams.parameters.toMutableMap()
        quickFilterParams[KEY_PAGE_SOURCE] = PAGE_SOURCE_SEARCH_SHOP
        quickFilterParams[KEY_SOURCE] = SOURCE_QUICK_FILTER

        return GraphqlRequest(
            FilterSortProduct(),
            MPSQuickFilterModel::class.java,
            mapOf(
                KEY_PARAMS to UrlParamUtils.generateUrlParamString(quickFilterParams)
            )
        )
    }

    private fun GraphqlResponse.getSearchShopMPS(): AceSearchShopMPS =
        getData(AceSearchShopMPS::class.java) ?: AceSearchShopMPS()

    private fun GraphqlResponse.getQuickFilter(): MPSQuickFilterModel =
        getData<MPSQuickFilterModel>(MPSQuickFilterModel::class.java) ?: MPSQuickFilterModel()

    companion object {
        private const val MPS_GQL_QUERY = """
            query MPSQuery(${'$'}params: String!) {
              ace_search_shop_mps(params: ${'$'}params) {
                header {
                  total_data
                }
                data {
                  id
                  name
                  location
                  applink
                  image_url
                  component_id
                  tracking_option
                  ticker {
                    type
                    image_url
                    message
                  }
                  badges {
                    title
                    image_url
                    show
                  }
                  products {
                    id
                    name
                    applink
                    image_url
                    price
                    price_format
                    original_price
                    discount_percentage
                    rating_average
                    parent_id
                    stock
                    min_order
                    component_id
                    tracking_option
                    label_groups {
                      url
                      type
                      title
                      position
                    }
                    buttons {
                      name
                      applink
                      text
                      is_cta
                      component_id
                      tracking_option
                    }
                  }
                  buttons {
                    name
                    applink
                    text
                    is_cta
                    component_id
                    tracking_option
                  }
                }
              }
            }
        """
    }
}
