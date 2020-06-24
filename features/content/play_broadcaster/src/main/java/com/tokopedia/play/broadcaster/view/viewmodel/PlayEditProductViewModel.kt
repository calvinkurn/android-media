package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.view.state.Selectable
import javax.inject.Inject

/**
 * Created by jegul on 23/06/20
 */
class PlayEditProductViewModel @Inject constructor(
        private val setupDataStore: PlayBroadcastSetupDataStore
) : ViewModel() {

    val observableSelectedProducts: LiveData<List<ProductData>>
        get() = setupDataStore.getObservableSelectedProducts()

    private val _selectedProductData: List<ProductData> = setupDataStore.getSelectedProducts()
    val selectedProducts: List<ProductContentUiModel> = _selectedProductData.map {
        ProductContentUiModel.createFromData(it, isSelectable = { Selectable }, isSelectedHandler = setupDataStore::isProductSelected)
    }

    private val selectedProductMap = _selectedProductData.associateBy { it.id }

    fun selectProduct(productId: Long, isSelected: Boolean) {
        val product = selectedProductMap[productId] ?: throw IllegalStateException("Product not found")
        setupDataStore.selectProduct(
                product,
                isSelected
        )
    }
}