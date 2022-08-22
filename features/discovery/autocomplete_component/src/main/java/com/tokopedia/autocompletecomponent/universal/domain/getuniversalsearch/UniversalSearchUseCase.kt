package com.tokopedia.autocompletecomponent.universal.domain.getuniversalsearch

import com.tokopedia.autocompletecomponent.universal.domain.model.UniversalSearchModel
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.UseCase

internal class UniversalSearchUseCase(
    private val graphqlCacheStrategy: GraphqlCacheStrategy,
    private val graphqlRepository: GraphqlRepository,
): UseCase<UniversalSearchModel>() {

    @GqlQuery("UniversalSearchQuery", GQL_QUERY)
    override suspend fun executeOnBackground(): UniversalSearchModel {
        val graphqlRequest = GraphqlRequest(
            UniversalSearchQuery.GQL_QUERY,
            UniversalSearchModel::class.java,
            mapOf("param" to "q=susu&device=desktop&user_id=12341243&source=universe&app_version=5.23&user_discrict_id=23"),
        )

        val graphqlResponse = graphqlRepository.response(listOf(graphqlRequest), graphqlCacheStrategy)

        val error = graphqlResponse.getError(UniversalSearchModel::class.java)

        if (error == null || error.isEmpty()){
            return graphqlResponse.getData(UniversalSearchModel::class.java)
        } else {
            throw Exception(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    companion object {
        private const val GQL_QUERY = """
            query UniversalQuery(${'$'}param: String!) {
              universe_universal_search(param: ${'$'}param) {
                header {
                  status_code
                  message
                  time_process
                }
                data {
                  items {
                    id
                    applink
                    image_url
                    template
                    title
                    subtitle
                    type
                    component_id
                    tracking_option
                    campaign_code
                    product {
                      id
                      applink
                      image_url
                      title
                      component_id
                      tracking_option
                      price
                      original_price
                      discount_percentage
                      rating_average
                      count_sold
                      shop{
                        name
                        city
                        url
                      }
                      badge{
                        title
                        image_url
                        show
                      }
                    }
                    curated{
                      id
                      url
                      applink
                      image_url
                      title
                      component_id
                      tracking_option
                      campaign_code
                    }
                  }
                }
              }
            }
        """
    }
}