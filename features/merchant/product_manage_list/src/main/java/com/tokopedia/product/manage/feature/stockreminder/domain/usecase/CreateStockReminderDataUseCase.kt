package com.tokopedia.product.manage.feature.stockreminder.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.query.StockReminderQuery
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.query.param.ProductWarehouseParam
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.query.param.CreateStockReminderParam
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createresponse.CreateStockReminderResponse
import javax.inject.Inject

class CreateStockReminderDataUseCase @Inject constructor(
        repository: GraphqlRepository
): GraphqlUseCase<CreateStockReminderResponse>(repository) {
    init {
        val query = StockReminderQuery.CREATE_QUERY
        if(query.isNotEmpty()) {
            setGraphqlQuery(query)
            setTypeClass(CreateStockReminderResponse::class.java)
        }
    }

    fun setParams(shopId: String, productId: String, warehouseId: String, threshold: String) {

        val productWarehouseCreateParam = ProductWarehouseParam(productId, warehouseId, threshold)
        val listProductWarehouseCreateParam = ArrayList<ProductWarehouseParam>()

        listProductWarehouseCreateParam.add(productWarehouseCreateParam)

        val stockReminderCreateParam = CreateStockReminderParam(shopId, true, listProductWarehouseCreateParam)
        val params: Map<String, Any?> = mutableMapOf(
                PARAM_INPUT to stockReminderCreateParam
        )
        setRequestParams(params)

    }

    companion object{
        private const val PARAM_INPUT = "input"
    }
}