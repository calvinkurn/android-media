package com.tokopedia.play.broadcaster.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.play.broadcaster.domain.usecase.AddMediaUseCase
import com.tokopedia.play.broadcaster.domain.usecase.AddProductTagUseCase
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.util.coroutine.CoroutineDispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlayBroadcastSetupDataStoreImpl @Inject constructor(
        private val dispatcher: CoroutineDispatcherProvider,
        private val addProductTagUseCase: AddProductTagUseCase,
        private val addMediaUseCase: AddMediaUseCase
) : PlayBroadcastSetupDataStore {

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

    override suspend fun uploadSelectedProducts(channelId: String): NetworkResult<Unit> {
        return try {
            addProductTag(channelId)
            NetworkResult.Success(Unit)
        } catch (e: Throwable) {
            NetworkResult.Fail(e)
        }
    }

    private suspend fun addProductTag(channelId: String) = withContext(dispatcher.io) {
        return@withContext addProductTagUseCase.apply {
            params = AddProductTagUseCase.createParams(
                    channelId = channelId,
                    productIds = selectedProductMap.keys.map { it.toString() }
            )
        }.executeOnBackground()
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

    override suspend fun uploadSelectedCover(channelId: String): NetworkResult<Unit> {
        return try {
            updateCover(channelId)
            NetworkResult.Success(Unit)
        } catch (e: Throwable) {
            NetworkResult.Fail(e)
        }
    }

    private suspend fun updateCover(channelId: String) = withContext(dispatcher.io) {
        return@withContext addMediaUseCase.apply {
            params = AddMediaUseCase.createParams(
                    channelId = channelId,
                    coverUrl = getSelectedCover()?.coverImage?.path ?: throw IllegalStateException("Cover url must not be null")
            )
        }.executeOnBackground()
    }
}