package com.tokopedia.play.broadcaster.data.datastore

import androidx.lifecycle.LiveData
import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play.broadcaster.data.type.OverwriteMode
import com.tokopedia.play.broadcaster.ui.model.CoverSource
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.ui.model.result.map
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import javax.inject.Inject

class PlayBroadcastSetupDataStoreImpl @Inject constructor(
        private val productDataStore: ProductDataStore,
        private val coverDataStore: CoverDataStore
) : PlayBroadcastSetupDataStore {

    override fun overwrite(dataStore: PlayBroadcastSetupDataStore, modeExclusion: List<OverwriteMode>) {
        if (!modeExclusion.contains(OverwriteMode.Product))
            overwriteProductDataStore(dataStore)

        if (!modeExclusion.contains(OverwriteMode.Cover))
            overwriteCoverDataStore(dataStore)
    }

    override fun getProductDataStore(): ProductDataStore {
        return productDataStore
    }

    override fun getCoverDataStore(): CoverDataStore {
        return coverDataStore
    }

    private fun overwriteProductDataStore(dataStore: ProductDataStore) {
        setSelectedProducts(dataStore.getSelectedProducts())
    }

    private fun overwriteCoverDataStore(dataStore: CoverDataStore) {
        dataStore.getSelectedCover()?.let(::setFullCover)
    }

    /**
     * Product
     */
    override fun getObservableSelectedProducts(): LiveData<List<ProductData>> {
        return productDataStore.getObservableSelectedProducts()
    }

    override fun getSelectedProducts(): List<ProductData> {
        return productDataStore.getSelectedProducts()
    }

    override fun selectProduct(product: ProductData, isSelected: Boolean) {
        productDataStore.selectProduct(product, isSelected)
    }

    override fun isProductSelected(productId: Long): Boolean {
        return productDataStore.isProductSelected(productId)
    }

    override fun getTotalSelectedProduct(): Int {
        return productDataStore.getTotalSelectedProduct()
    }

    override suspend fun uploadSelectedProducts(channelId: String): NetworkResult<Unit> {
        val uploadResult = productDataStore.uploadSelectedProducts(channelId)
                .map { Unit }
        if (uploadResult is NetworkResult.Success) validateCover()
        return uploadResult
    }

    override fun setSelectedProducts(selectedProducts: List<ProductData>) {
        productDataStore.setSelectedProducts(selectedProducts)
    }

    /**
     * Cover
     */
    override fun getObservableSelectedCover(): LiveData<PlayCoverUiModel> {
        return coverDataStore.getObservableSelectedCover()
    }

    override fun getSelectedCover(): PlayCoverUiModel? {
        return coverDataStore.getSelectedCover()
    }

    override fun setFullCover(cover: PlayCoverUiModel) {
        coverDataStore.setFullCover(cover)
    }

    override fun updateCoverState(state: CoverSetupState) {
        coverDataStore.updateCoverState(state)
    }

    override fun updateCoverTitle(title: String) {
        coverDataStore.updateCoverTitle(title)
    }

    override suspend fun uploadSelectedCover(channelId: String): NetworkResult<Unit> {
        return coverDataStore.uploadSelectedCover(channelId)
    }

    override suspend fun uploadCoverTitle(channelId: String): NetworkResult<Unit> {
        return coverDataStore.uploadCoverTitle(channelId)
    }

    private fun validateCover() {
        val selectedCover = getSelectedCover()
        val selectedProducts = getSelectedProducts()
        val chosenCoverSource = when (val croppedCover = selectedCover?.croppedCover) {
            is CoverSetupState.Cropped -> croppedCover.coverSource
            is CoverSetupState.Cropping.Image -> croppedCover.coverSource
            else -> null
        }

        if (chosenCoverSource is CoverSource.Product) {
            val productId = chosenCoverSource.id
            if (selectedProducts.none { it.id == productId })
                updateCoverState(CoverSetupState.Blank)
        }
    }
}