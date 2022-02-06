package com.tokopedia.pdpsimulation.activateCheckout.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.OptimizedCheckoutAddToCartOcc
import com.tokopedia.pdpsimulation.common.constants.GQL_ADD_TO_CART_OCC_MULTI
import javax.inject.Inject


@GqlQuery("AddToCartDataQuery", GQL_ADD_TO_CART_OCC_MULTI)
class AddToCartUseCase@Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<OptimizedCheckoutAddToCartOcc>(graphqlRepository) {

        fun addProductToCart(onSuccess: (OptimizedCheckoutAddToCartOcc) -> Unit,
        onError:(Throwable) -> Unit,
        productId:String,
        shopId:String,
        productQuantity: Int)
        {
            try {
                this.setTypeClass(OptimizedCheckoutAddToCartOcc::class.java)
                this.setRequestParams(getRequestParams(productId,shopId,productQuantity))
                this.setGraphqlQuery(AddToCartDataQuery.GQL_QUERY)
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

    private fun getRequestParams(productId: String, shopId: String, productQuantity: Int): Map<String, Any?> {

        val addToCardMultiParams = AddToCardMultiParams(arrayListOf(AddToCartOccRequiredParams(productId,shopId,productQuantity.toString())))
        return mapOf("request" to mapOf(
            PARAM_CARTS to addToCardMultiParams.carts.map {
                mapOf<String,Any?>(
                    PARAM_PRODUCT_ID to it.productId,
                    PARAM_SHOP_ID to it.shopId,
                    PARAM_QUANTITY to it.quantity
                )
            },
                    PARAM_SOURCE to "pdp",

        ))

    }


    companion object {
        private const val PARAM_CARTS = "carts"
        private const val PARAM_PRODUCT_ID = "product_id"
        private const val PARAM_SHOP_ID = "shop_id"
        private const val PARAM_QUANTITY = "quantity"
        private const val PARAM_SOURCE = "source"

    }


}

data class AddToCardMultiParams(
    var carts: List<AddToCartOccRequiredParams>
)

data class AddToCartOccRequiredParams (
    var productId: String,
    var shopId: String,
    var quantity: String,
)


