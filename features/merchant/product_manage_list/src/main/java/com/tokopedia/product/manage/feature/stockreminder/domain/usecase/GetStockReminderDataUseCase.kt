package com.tokopedia.product.manage.feature.stockreminder.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.query.StockReminderQuery
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse.GetStockReminderResponse
import javax.inject.Inject

class GetStockReminderDataUseCase @Inject constructor(
        repository: GraphqlRepository
): GraphqlUseCase<GetStockReminderResponse>(repository) {

    init {
        val query = StockReminderQuery.GET_QUERY
        if(query.isNotEmpty()) {
            setGraphqlQuery(query)
            setTypeClass(GetStockReminderResponse::class.java)
        }
    }

    fun setParams(productIds: String) {
        val params = mutableMapOf(
            PARAMS_PRODUCT_IDS to productIds
        )
        setRequestParams(params)
    }

    companion object{
        private const val PARAMS_PRODUCT_IDS = "productIds"
    }
}