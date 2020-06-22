package com.tokopedia.play.broadcaster.data.datastore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.play.broadcaster.domain.usecase.AddMediaUseCase
import com.tokopedia.play.broadcaster.domain.usecase.AddProductTagUseCase
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.SetupDataState
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlayBroadcastSetupDataStoreImpl @Inject constructor(
        private val dispatcher: CoroutineDispatcherProvider,
        private val addProductTagUseCase: AddProductTagUseCase,
        private val addMediaUseCase: AddMediaUseCase
) : PlayBroadcastSetupDataStore {

    private val mSelectedProductMap = mutableMapOf<Long, ProductContentUiModel>()

    private val _selectedProductsLiveData = MutableLiveData<List<ProductContentUiModel>>().apply {
        value = emptyList()
    }

    private val _selectedCoverLiveData = MutableLiveData<PlayCoverUiModel>()

    override fun overwrite(dataStore: PlayBroadcastSetupDataStore) {
        dataStore.getSelectedCover()?.let(::setFullCover)
        setSelectedProducts(dataStore.getSelectedProducts())
    }

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

    private fun setSelectedProducts(selectedProducts: List<ProductContentUiModel>) {
        mSelectedProductMap.clear()
        selectedProducts.associateByTo(mSelectedProductMap) { it.id }

        updateSelectedProducts()
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

    override fun setFullCover(cover: PlayCoverUiModel) {
        _selectedCoverLiveData.value = cover
    }

    override fun updateCoverState(state: CoverSetupState) {
        val currentCover = getSelectedCover() ?: PlayCoverUiModel.empty()
        _selectedCoverLiveData.value = currentCover.copy(
                croppedCover = state,
                state = SetupDataState.Draft
        )
    }

    override fun updateCoverTitle(title: String) {
        val currentCover = getSelectedCover() ?: PlayCoverUiModel.empty()
        _selectedCoverLiveData.value = currentCover.copy(
                title = title,
                state = SetupDataState.Draft
        )
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
                    coverUrl = when (val croppedCover = getSelectedCover()?.croppedCover) {
                        is CoverSetupState.Cropped -> croppedCover.coverImage.path
                        else -> throw IllegalStateException("Cover url must not be null")
                    })
        }.executeOnBackground()
    }
}