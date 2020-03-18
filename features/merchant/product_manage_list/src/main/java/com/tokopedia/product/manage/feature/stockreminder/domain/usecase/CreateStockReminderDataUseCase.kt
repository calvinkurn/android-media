package com.tokopedia.product.manage.feature.stockreminder.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.query.StockReminderQuery
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.query.param.ProductWarehouseParam
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.query.param.CreateUpdateStockReminderParam
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.CreateStockReminderResponse
import javax.inject.Inject

class CreateStockReminderDataUseCase @Inject constructor(
        repository: GraphqlRepository
): GraphqlUseCase<CreateStockReminderResponse>(repository) {

    companion object{
        private const val PARAM_INPUT = "input"
    }

    init {
        val query = StockReminderQuery.CREATE_QUERY
        setGraphqlQuery(query)
        setTypeClass(CreateStockReminderResponse::class.java)
    }

    fun setParams(shopId: String, productId: String, warehouseId: String, threshold: String) {

        val productWarehouseParam = ProductWarehouseParam(productId, warehouseId, threshold)
        val listProductWarehouseParam = ArrayList<ProductWarehouseParam>()

        listProductWarehouseParam.add(productWarehouseParam)

        val createStockReminderParam = CreateUpdateStockReminderParam(shopId, true, listProductWarehouseParam)
        val params: Map<String, Any?> = mutableMapOf(
                PARAM_INPUT to createStockReminderParam
        )
        setRequestParams(params)

    }

}