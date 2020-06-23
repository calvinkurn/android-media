package com.tokopedia.play.broadcaster.data.datastore

import androidx.lifecycle.LiveData
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult

/**
 * Created by jegul on 23/06/20
 */
interface ProductDataStore {

    fun getObservableSelectedProducts(): LiveData<List<ProductContentUiModel>>

    fun getSelectedProducts(): List<ProductContentUiModel>

    fun selectProduct(product: ProductContentUiModel, isSelected: Boolean)

    fun isProductSelected(productId: Long): Boolean

    fun getTotalSelectedProduct(): Int

    suspend fun uploadSelectedProducts(channelId: String): NetworkResult<Unit>

    fun setSelectedProducts(selectedProducts: List<ProductContentUiModel>)
}