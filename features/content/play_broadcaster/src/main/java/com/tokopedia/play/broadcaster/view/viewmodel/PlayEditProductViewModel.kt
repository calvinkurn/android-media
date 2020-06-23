package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import javax.inject.Inject

/**
 * Created by jegul on 23/06/20
 */
class PlayEditProductViewModel @Inject constructor(
        private val setupDataStore: PlayBroadcastSetupDataStore
) : ViewModel() {

    val observableSelectedProducts: LiveData<List<ProductContentUiModel>>
        get() = setupDataStore.getObservableSelectedProducts()

    val selectedProducts: List<ProductContentUiModel> = setupDataStore.getSelectedProducts()
    private val selectedProductMap = selectedProducts.associateBy { it.id }

    fun selectProduct(productId: Long, isSelected: Boolean) {
        val product = selectedProductMap[productId] ?: throw IllegalStateException("Product not found")
        setupDataStore.selectProduct(
                product,
                isSelected
        )
    }

    fun getDataStore(): PlayBroadcastSetupDataStore {
        return setupDataStore
    }
}