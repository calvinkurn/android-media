package com.tokopedia.play.broadcaster.data.datastore

import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play_common.model.result.NetworkResult
import kotlinx.coroutines.flow.Flow

/**
 * Created by jegul on 23/06/20
 */
interface ProductDataStore {

    fun getObservableSelectedProducts(): Flow<List<ProductData>>

    fun getSelectedProducts(): List<ProductData>

    fun selectProduct(product: ProductData, isSelected: Boolean)

    fun isProductSelected(productId: Long): Boolean

    fun getTotalSelectedProduct(): Int

    suspend fun uploadSelectedProducts(channelId: String): NetworkResult<Unit>

    fun setSelectedProducts(selectedProducts: List<ProductData>)
}