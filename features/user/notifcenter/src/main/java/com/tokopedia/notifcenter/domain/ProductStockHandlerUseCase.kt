package com.tokopedia.notifcenter.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.notifcenter.data.consts.NotificationQueriesConstant
import com.tokopedia.notifcenter.data.entity.ProductStockHandler
import javax.inject.Inject
import javax.inject.Named

class ProductStockHandlerUseCase @Inject constructor(
        @Named(NotificationQueriesConstant.PRODUCT_STOCK_HANDLER)
        private val query: String,
        private val useCase: GraphqlUseCase<ProductStockHandler>
) {

    fun get(requestParams: Map<String, Any>,
            onSuccess: (ProductStockHandler) -> Unit,
            onError: (Throwable) -> Unit) {
        useCase.apply {
            setTypeClass(ProductStockHandler::class.java)
            setRequestParams(requestParams)
            setGraphqlQuery(query)
            execute({ result ->
                onSuccess(result)
            }, { error ->
                onError(error)
            })
        }
    }

    companion object {
        private const val PARAM_NOTIF_ID  = "notif_id"

        fun params(notifId: String): HashMap<String, Any> {
            val variables = hashMapOf<String, Any>()
            variables[PARAM_NOTIF_ID] = notifId
            return variables
        }
    }

}