package com.tokopedia.digital_product_detail.domain.usecase

import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogOperatorSelectGroup
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetRechargeCatalogOperatorSelectGroupUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
): GraphqlUseCase<DigitalCatalogOperatorSelectGroup>(graphqlRepository) {

    private var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): DigitalCatalogOperatorSelectGroup {
        val gqlRequest = GraphqlRequest(QUERY, DigitalCatalogOperatorSelectGroup::class.java, params.parameters)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), GraphqlCacheStrategy
            .Builder(CacheType.ALWAYS_CLOUD).build())

        val error = gqlResponse.getError(DigitalCatalogOperatorSelectGroup::class.java)
        if (error == null || error.isEmpty()){
            return (gqlResponse.getData(DigitalCatalogOperatorSelectGroup::class.java) as DigitalCatalogOperatorSelectGroup)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.toString())
        }
    }

    fun createParams(menuID: Int){
        params = RequestParams.create().apply {
            putInt(KEY_MENU_ID, menuID)
        }
    }

    companion object {
        private const val KEY_MENU_ID = "menuID"

        private val QUERY = """
        query catalogOperatorSelectGroup(${'$'}menuID: Int!) {
          rechargeCatalogOperatorSelectGroup(menuID:${'$'}menuID, platformID: 5){
            text
            style
            help
            validations {
              id
              title
              rule
              message
            }
            operatorGroup{
              name
              operators{
                type
                id
                attributes{
                  name
                  operator_labels
                  image_url
                  description
                }
              }
            }
          }
        }
    """
    }
}