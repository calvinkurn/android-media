package com.tokopedia.product.manage.stock_reminder.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.stock_reminder.data.source.cloud.query.GetStockReminder
import com.tokopedia.product.manage.stock_reminder.data.source.cloud.response.StockReminderResponse
import javax.inject.Inject

class GetStockReminderDataUseCase @Inject constructor(
        repository: GraphqlRepository
): GraphqlUseCase<StockReminderResponse>(repository) {

    init {
        val query = GetStockReminder.QUERY
        if(query.isNotEmpty()) {
            setGraphqlQuery(query)
            setTypeClass(StockReminderResponse::class.java)
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