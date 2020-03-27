package com.tokopedia.notifcenter.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.notifcenter.data.consts.NotificationQueriesConstant
import com.tokopedia.notifcenter.data.entity.ProductStockReminder
import javax.inject.Inject
import javax.inject.Named

class ProductStockReminderUseCase @Inject constructor(
        @Named(NotificationQueriesConstant.PRODUCT_STOCK_REMINDER)
        private val query: String,
        private val useCase: GraphqlUseCase<ProductStockReminder>
) {

    fun get(requestParams: Map<String, Any>,
            onSuccess: (ProductStockReminder) -> Unit,
            onError: (Throwable) -> Unit) {
        useCase.apply {
            setTypeClass(ProductStockReminder::class.java)
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
        private const val PARAM_PRODUCT_ID  = "product_id"
        private const val PARAM_NOTIF_ID  = "notif_id"

        fun params(notifId: String, productId: String): HashMap<String, Any> {
            val variables = hashMapOf<String, Any>()
            variables[PARAM_PRODUCT_ID] = productId
            variables[PARAM_NOTIF_ID] = notifId
            return variables
        }
    }

}