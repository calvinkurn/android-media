package com.tokopedia.product.manage.feature.quickedit.price.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.common.feature.quickedit.common.data.model.ProductUpdateV3Response
import com.tokopedia.product.manage.feature.quickedit.delete.domain.DeleteProductUseCase
import com.tokopedia.product.manage.feature.quickedit.price.data.model.ProductEditPriceParam
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("EditPriceGqlQuery", EditPriceUseCase.query)
class EditPriceUseCase @Inject constructor(repository: GraphqlRepository): GraphqlUseCase<ProductUpdateV3Response>(repository) {

    companion object {
        const val PARAM_INPUT = "input"
        const val query = """
            mutation productUpdateV3(${'$'}input: ProductInputV3!){
                ProductUpdateV3(input:${'$'}input) {
                    header {
                        messages
                        reason
                        errorCode
                    }
                    isSuccess
                }
            }
        """
    }

    init {
        setGraphqlQuery(EditPriceGqlQuery())
        setTypeClass(ProductUpdateV3Response::class.java)
    }

    fun setParams(shopId: String, productId: String, price: Double) {
        val requestParams = RequestParams.create()
        val productEditPriceParam = ProductEditPriceParam()
        productEditPriceParam.shop.shopId = shopId
        productEditPriceParam.productId = productId
        productEditPriceParam.price = price
        requestParams.putObject(PARAM_INPUT, productEditPriceParam)
        setRequestParams(requestParams.parameters)
    }

}