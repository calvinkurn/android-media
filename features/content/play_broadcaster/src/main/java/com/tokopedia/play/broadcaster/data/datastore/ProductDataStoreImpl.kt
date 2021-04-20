package com.tokopedia.play.broadcaster.data.datastore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play.broadcaster.domain.usecase.AddProductTagUseCase
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 23/06/20
 */
class ProductDataStoreImpl @Inject constructor(
        private val dispatcher: CoroutineDispatchers,
        private val addProductTagUseCase: AddProductTagUseCase
) : ProductDataStore {

    private val mSelectedProductMap = mutableMapOf<Long, ProductData>()

    private val _selectedProductsLiveData = MutableLiveData<List<ProductData>>().apply {
        value = emptyList()
    }

    override fun getObservableSelectedProducts(): LiveData<List<ProductData>> {
        return _selectedProductsLiveData
    }

    override fun getSelectedProducts(): List<ProductData> {
        return _selectedProductsLiveData.value.orEmpty()
    }

    override fun selectProduct(product: ProductData, isSelected: Boolean) {
        if (isSelected) mSelectedProductMap[product.id] = product
        else mSelectedProductMap.remove(product.id)

        updateSelectedProducts()
    }

    override fun isProductSelected(productId: Long): Boolean {
        return mSelectedProductMap.contains(productId)
    }

    override fun getTotalSelectedProduct(): Int {
        return mSelectedProductMap.size
    }

    override suspend fun uploadSelectedProducts(channelId: String): NetworkResult<Unit> {
        return try {
            addProductTag(channelId)
            NetworkResult.Success(Unit)
        } catch (e: Throwable) {
            NetworkResult.Fail(e)
        }
    }

    override fun setSelectedProducts(selectedProducts: List<ProductData>) {
        mSelectedProductMap.clear()
        selectedProducts.associateByTo(mSelectedProductMap) { it.id }

        updateSelectedProducts()
    }

    private suspend fun addProductTag(channelId: String) = withContext(dispatcher.io) {
        return@withContext addProductTagUseCase.apply {
            params = AddProductTagUseCase.createParams(
                    channelId = channelId,
                    productIds = mSelectedProductMap.keys.map { it.toString() }
            )
        }.executeOnBackground()
    }

    private fun updateSelectedProducts() {
        _selectedProductsLiveData.value = mSelectedProductMap.values.toList()
    }
}