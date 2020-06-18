package com.tokopedia.play.broadcaster.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import javax.inject.Inject

class PlayBroadcastSetupDataStoreImpl @Inject constructor() : PlayBroadcastSetupDataStore {

    private val selectedProductMap = mutableMapOf<Long, ProductContentUiModel>()

    private val _selectedProductsLiveData = MutableLiveData<List<ProductContentUiModel>>().apply {
        value = emptyList()
    }

    private val _selectedCoverLiveData = MutableLiveData<PlayCoverUiModel>()

    /**
     * Product
     */
    override fun getObservableSelectedProducts(): LiveData<List<ProductContentUiModel>> {
        return _selectedProductsLiveData
    }

    override fun getSelectedProducts(): List<ProductContentUiModel> {
        return _selectedProductsLiveData.value.orEmpty()
    }

    override fun selectProduct(product: ProductContentUiModel, isSelected: Boolean) {
        if (isSelected) selectedProductMap[product.id] = product
        else selectedProductMap.remove(product.id)

        updateSelectedProducts()
    }

    override fun isProductSelected(productId: Long): Boolean {
        return selectedProductMap.contains(productId)
    }

    override fun getTotalSelectedProduct(): Int {
        return selectedProductMap.size
    }

    private fun updateSelectedProducts() {
        _selectedProductsLiveData.value = selectedProductMap.values.toList()
    }

    /**
     * Cover
     */
    override fun getObservableSelectedCover(): LiveData<PlayCoverUiModel> {
        return _selectedCoverLiveData
    }

    override fun getSelectedCover(): PlayCoverUiModel? {
        return _selectedCoverLiveData.value
    }

    override fun setCover(cover: PlayCoverUiModel) {
        _selectedCoverLiveData.value = cover
    }
}