package com.tokopedia.pdpsimulation.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pdpsimulation.common.constants.GQL_GET_PRODUCT_DETAIL
import com.tokopedia.pdpsimulation.common.domain.model.BaseProductDetailClass
import javax.inject.Inject

@GqlQuery("ProductDetailQuery", GQL_GET_PRODUCT_DETAIL)
class ProductDetailUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
        GraphqlUseCase<BaseProductDetailClass>(graphqlRepository) {

    fun getProductDetail(
            onSuccess: (BaseProductDetailClass) -> Unit,
            onError: (Throwable) -> Unit,
            productId: String
    ) {
        try {
            this.setTypeClass(BaseProductDetailClass::class.java)
            this.setRequestParams(getRequestParams(productId))
            this.setGraphqlQuery(ProductDetailQuery.GQL_QUERY)
            this.execute(
                    { result ->
                        onSuccess(result)
                    }, { error ->
                onError(error)
            }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(productId: String): MutableMap<String, Any?> {
        val optionMap = mutableMapOf<String, Boolean>()
        optionMap["basic"] = true
        optionMap["picture"] = true
        optionMap["variant"] = true
        optionMap["shop"] = true
        val requestMap = mutableMapOf<String, Any?>()
        requestMap["productID"] = productId
        requestMap["options"] = optionMap
        return requestMap
    }

    companion object {
        const val PARAM_PRODUCT_CODE = "productID"
        const val PARAM_OPTIONS = "options"
    }

}