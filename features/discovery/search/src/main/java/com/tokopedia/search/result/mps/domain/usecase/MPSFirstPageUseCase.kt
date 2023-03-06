package com.tokopedia.search.result.mps.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.usecase.coroutines.UseCase

class MPSFirstPageUseCase(
    private val graphqlUseCase: MultiRequestGraphqlUseCase,
): UseCase<MPSModel>() {

    override suspend fun executeOnBackground(): MPSModel = graphqlUseCase.run {
        clearRequest()
        addRequests(listOf(
            mpsRequest()
        ))

        val graphqlResponse = executeOnBackground()

        MPSModel(
            graphqlResponse.getData(MPSModel.AceSearchShopMPS::class.java),
        )
    }

    @GqlQuery("MPSFirstPageQuery", MPS_GQL_QUERY)
    private fun mpsRequest(): GraphqlRequest {
        val requestParams = useCaseRequestParams

        return GraphqlRequest(
            MPSFirstPageQuery(),
            MPSModel.AceSearchShopMPS::class.java,
            requestParams.parameters
        )
    }

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
