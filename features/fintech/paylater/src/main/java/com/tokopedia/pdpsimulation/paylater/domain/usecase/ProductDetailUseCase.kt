package com.tokopedia.pdpsimulation.paylater.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pdpsimulation.paylater.domain.model.BaseProductDetailClass
import javax.inject.Inject

class ProductDetailUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<BaseProductDetailClass>(graphqlRepository) {

    fun getProductDetail(
        onSuccess: (BaseProductDetailClass) -> Unit,
        onError: (Throwable) -> Unit,
        productId: String
    ) {
        try {
            this.setTypeClass(BaseProductDetailClass::class.java)
            //  this.setRequestParams(getRequestParams(productId))
            this.setGraphqlQuery(PayLaterAvailableOptionData.GQL_QUERY)
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

//    @Throws(ParseException::class)
//    private fun getRequestParams(productId: String): MutableMap<String, Any?> {
//
//    }

    companion object {
        const val PARAM_PRODUCT_CODE = "productID"
        const val PARAM_OPTIONS = "options"
    }

}