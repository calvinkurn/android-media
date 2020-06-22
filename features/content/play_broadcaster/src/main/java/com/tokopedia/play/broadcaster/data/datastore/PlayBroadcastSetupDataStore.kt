package com.tokopedia.play.broadcaster.data.datastore

import androidx.lifecycle.LiveData
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.view.state.CoverSetupState

interface PlayBroadcastSetupDataStore {

    fun overwrite(dataStore: PlayBroadcastSetupDataStore)

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

    fun setFullCover(cover: PlayCoverUiModel)

    fun updateCoverState(state: CoverSetupState)

    fun updateCoverTitle(title: String)

    suspend fun uploadSelectedCover(channelId: String): NetworkResult<Unit>
}