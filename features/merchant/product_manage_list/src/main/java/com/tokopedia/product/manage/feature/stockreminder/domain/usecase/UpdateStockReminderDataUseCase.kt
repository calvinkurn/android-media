package com.tokopedia.product.manage.feature.stockreminder.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.query.StockReminderQuery
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.query.param.CreateUpdateStockReminderParam
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.query.param.ProductWarehouseParam
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.CreateStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.UpdateStockReminderResponse
import javax.inject.Inject

class UpdateStockReminderDataUseCase @Inject constructor(
        repository: GraphqlRepository
): GraphqlUseCase<UpdateStockReminderResponse>(repository) {

    companion object{
        private const val PARAM_INPUT = "input"
    }

    init {
        val query = StockReminderQuery.UPDATE_QUERY
        setGraphqlQuery(query)
        setTypeClass(UpdateStockReminderResponse::class.java)
    }

    fun setParams(shopId: String, productId: String, warehouseId: String, threshold: String) {

        val productWarehouseCreateParam = ProductWarehouseParam(productId, warehouseId, threshold)
        val listProductWarehouseCreateParam = ArrayList<ProductWarehouseParam>()

        listProductWarehouseCreateParam.add(productWarehouseCreateParam)

        val stockReminderCreateParam = CreateUpdateStockReminderParam(shopId, true, listProductWarehouseCreateParam)
        val params: Map<String, Any?> = mutableMapOf(
                PARAM_INPUT to stockReminderCreateParam
        )
        setRequestParams(params)

    }

}