package com.tokopedia.play.broadcaster.testdouble

import androidx.lifecycle.LiveData
import com.tokopedia.play.broadcaster.data.datastore.ProductDataStore
import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult

/**
 * Created by jegul on 24/09/20
 */
class MockProductDataStore : ProductDataStore {

    override fun getObservableSelectedProducts(): LiveData<List<ProductData>> {
        TODO("Not yet implemented")
    }

    override fun getSelectedProducts(): List<ProductData> {
        TODO("Not yet implemented")
    }

    override fun selectProduct(product: ProductData, isSelected: Boolean) {
        TODO("Not yet implemented")
    }

    override fun isProductSelected(productId: Long): Boolean {
        TODO("Not yet implemented")
    }

    override fun getTotalSelectedProduct(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun uploadSelectedProducts(channelId: String): NetworkResult<Unit> {
        TODO("Not yet implemented")
    }

    override fun setSelectedProducts(selectedProducts: List<ProductData>) {
        TODO("Not yet implemented")
    }
}