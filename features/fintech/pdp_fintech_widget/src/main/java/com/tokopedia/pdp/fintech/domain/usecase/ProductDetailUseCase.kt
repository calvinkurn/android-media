package com.tokopedia.pdp.fintech.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pdp.fintech.constants.GQL_GET_PRODUCT_DETAIL_V3
import com.tokopedia.pdp.fintech.domain.datamodel.ProductDetailClass
import javax.inject.Inject


@GqlQuery("ProductDetailQuery", GQL_GET_PRODUCT_DETAIL_V3)
class ProductDetailUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<ProductDetailClass>(graphqlRepository) {

    fun getProductDetail(
        onSuccess: (ProductDetailClass) -> Unit,
        onError: (Throwable) -> Unit,
        productId: String
    ) {
        try {
            this.setTypeClass(ProductDetailClass::class.java)
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
        val requestMap = mutableMapOf<String, Any?>()
        requestMap["productID"] = productId
        requestMap["options"] = optionMap
        return requestMap
    }


}