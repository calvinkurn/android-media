package com.tokopedia.play.broadcaster.data.datastore

import androidx.lifecycle.LiveData
import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play_common.model.result.NetworkResult

/**
 * Created by jegul on 23/06/20
 */
interface ProductDataStore {

    fun getObservableSelectedProducts(): LiveData<List<ProductData>>

    fun getSelectedProducts(): List<ProductData>

    fun selectProduct(product: ProductData, isSelected: Boolean)

    fun isProductSelected(productId: Long): Boolean

    fun getTotalSelectedProduct(): Int

    suspend fun uploadSelectedProducts(channelId: String): NetworkResult<Unit>

    fun setSelectedProducts(selectedProducts: List<ProductData>)
}