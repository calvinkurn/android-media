package com.tokopedia.pdpsimulation.activateCheckout.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.BasePayLaterOptimizedModel
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.PaylaterGetOptimizedModel
import com.tokopedia.pdpsimulation.common.constants.GQL_PAYLATER_ACTIVATION
import javax.inject.Inject

@GqlQuery("PayLaterActivationDataQuery", GQL_PAYLATER_ACTIVATION)

class PaylaterActivationUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<BasePayLaterOptimizedModel>(graphqlRepository) {

    fun getPayLaterActivationDetail(
        onSuccess: (PaylaterGetOptimizedModel) -> Unit,
        onError: (Throwable) -> Unit,
        amount: Double,
        productId: String,
        gatewayCode: String,
        shopId: String,
        parentId: String,
        categoryId: String
    ) {
        try {
            this.setTypeClass(BasePayLaterOptimizedModel::class.java)
            this.setRequestParams(
                getRequestParams(amount, productId, gatewayCode, shopId, parentId, categoryId)
            )
            this.setGraphqlQuery(PayLaterActivationDataQuery.GQL_QUERY)
            this.execute(
                { result ->
                    onSuccess(result.paylatergetOptimizedCheckout)
                }, { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(
        amount: Double,
        productId: String,
        gatewayCode: String,
        shopId: String,
        parentId: String,
        categoryId: String
    ): Map<String, Any?> {
        return mutableMapOf(
            "request" to mutableMapOf(
                PARAM_PRODUCT_PRICE to amount,
                PARAM_PRODUCT_ID to productId,
                PARAM_REQUEST_GATEWAY_ID to 0,
                PARAM_PRODUCT_CODE to gatewayCode,
                PARAM_SHOP_ID_V2 to shopId,
                PARAM_PARENT_PRODUCT_ID to parentId,
                PARAM_CATEGORY_ID to categoryId
            )
        )
    }

    companion object {
        const val PARAM_PRODUCT_PRICE = "product_price"
        const val PARAM_PRODUCT_ID = "product_id"
        const val PARAM_REQUEST_GATEWAY_ID = "gateway_id"
        const val PARAM_PRODUCT_CODE = "product_code"
        const val PARAM_SHOP_ID_V2 = "shop_id_v2"
        const val PARAM_PARENT_PRODUCT_ID = "parent_product_id"
        const val PARAM_CATEGORY_ID = "category_id"
    }

}
