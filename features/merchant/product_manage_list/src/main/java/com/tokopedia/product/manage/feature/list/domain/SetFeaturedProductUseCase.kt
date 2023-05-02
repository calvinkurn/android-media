package com.tokopedia.product.manage.feature.list.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.feature.list.data.model.FeaturedProductResponseModel
import javax.inject.Inject

@GqlQuery("SetFeaturedProductGqlQuery", SetFeaturedProductUseCase.QUERY)
class SetFeaturedProductUseCase @Inject constructor(
    repository: GraphqlRepository
) : GraphqlUseCase<FeaturedProductResponseModel>(repository) {

    companion object {
        private const val PARAM_PRODUCT_ID = "productId"
        private const val PARAM_STATUS = "status"
        const val QUERY = """
            mutation GoldManageFeaturedProductV2(${"$"}productId: Int!, ${"$"}status: Int!) {
              GoldManageFeaturedProductV2(ProductID: ${"$"}productId, Status: ${"$"}status) {
                header {
                  error_code
                  message
                }
              }
            }
        """
    }

    init {
        setGraphqlQuery(SetFeaturedProductGqlQuery())
        setTypeClass(FeaturedProductResponseModel::class.java)
    }

    fun setParams(productId: Long, status: Int) {
        val params = HashMap<String, Any>()
        params[PARAM_PRODUCT_ID] = productId
        params[PARAM_STATUS] = status
        setRequestParams(params)
    }
}
