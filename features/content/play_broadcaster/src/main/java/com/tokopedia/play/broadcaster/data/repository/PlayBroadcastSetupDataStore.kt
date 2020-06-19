package com.tokopedia.play.broadcaster.data.repository

import androidx.lifecycle.LiveData
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult

interface PlayBroadcastSetupDataStore {

    /**
     * Product Setup
     */
    fun getObservableSelectedProducts(): LiveData<List<ProductContentUiModel>>

    fun getSelectedProducts(): List<ProductContentUiModel>

    fun selectProduct(product: ProductContentUiModel, isSelected: Boolean)

    fun isProductSelected(productId: Long): Boolean

    fun getTotalSelectedProduct(): Int

    suspend fun uploadSelectedProducts(channelId: String): NetworkResult<Unit>

    /**
     * Cover
     */
    fun getObservableSelectedCover(): LiveData<PlayCoverUiModel>

    fun getSelectedCover(): PlayCoverUiModel?

    fun setCover(cover: PlayCoverUiModel)

    suspend fun uploadSelectedCover(channelId: String): NetworkResult<Unit>
}