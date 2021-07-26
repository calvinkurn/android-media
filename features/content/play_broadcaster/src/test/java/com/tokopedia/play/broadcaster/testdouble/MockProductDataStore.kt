package com.tokopedia.play.broadcaster.testdouble

import com.tokopedia.play.broadcaster.data.datastore.ProductDataStore
import com.tokopedia.play.broadcaster.data.datastore.ProductDataStoreImpl
import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow

/**
 * Created by jegul on 25/09/20
 */
class MockProductDataStore(
        dispatcherProvider: CoroutineDispatchers
) : ProductDataStore {

    val selectedProductsId: List<Long>
        get() = getSelectedProducts().map { it.id }

    private var isSuccess = false

    private val realImpl = ProductDataStoreImpl(dispatcherProvider, mockk(relaxed = true))

    override fun getObservableSelectedProducts(): Flow<List<ProductData>> {
        return realImpl.getObservableSelectedProducts()
    }

    override fun getSelectedProducts(): List<ProductData> {
        return realImpl.getSelectedProducts()
    }

    override fun selectProduct(product: ProductData, isSelected: Boolean) {
        realImpl.selectProduct(product, isSelected)
    }

    override fun isProductSelected(productId: Long): Boolean {
        return realImpl.isProductSelected(productId)
    }

    override fun getTotalSelectedProduct(): Int {
        return realImpl.getTotalSelectedProduct()
    }

    override suspend fun uploadSelectedProducts(channelId: String): NetworkResult<Unit> {
        return if (isSuccess) NetworkResult.Success(Unit)
        else NetworkResult.Fail(IllegalArgumentException("Error Upload Product"))
    }

    override fun setSelectedProducts(selectedProducts: List<ProductData>) {
        realImpl.setSelectedProducts(selectedProducts)
    }

    fun setUploadSuccess(isSuccess: Boolean) {
        this.isSuccess = isSuccess
    }
}