package com.tokopedia.notifcenter.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.notifcenter.data.consts.NotificationQueriesConstant
import com.tokopedia.notifcenter.data.entity.ProductHighlightItem
import javax.inject.Inject
import javax.inject.Named

class ProductHighlightUseCase @Inject constructor(
        @Named(NotificationQueriesConstant.PRODUCT_HIGHLIGHT)
        private val query: String,
        private val useCase: GraphqlUseCase<ProductHighlightItem>
) {

    fun get(requestParams: Map<String, Any>,
            onSuccess: (ProductHighlightItem) -> Unit,
            onError: (Throwable) -> Unit) {
        useCase.apply {
            setTypeClass(ProductHighlightItem::class.java)
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
        private const val PARAM_QUERY  = "params"

        fun params(shopId: String): HashMap<String, Any> {
            return hashMapOf<String, Any>().apply {
                put(PARAM_QUERY, "device=android&source=shop_product&rows=6&shop_id=$shopId")
            }
        }
    }

}