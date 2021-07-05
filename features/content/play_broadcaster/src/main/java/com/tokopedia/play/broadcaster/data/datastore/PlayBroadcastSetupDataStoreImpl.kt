package com.tokopedia.play.broadcaster.data.datastore

import androidx.lifecycle.LiveData
import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play.broadcaster.data.type.OverwriteMode
import com.tokopedia.play.broadcaster.ui.model.BroadcastScheduleUiModel
import com.tokopedia.play.broadcaster.ui.model.CoverSource
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.title.PlayTitleUiModel
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.model.result.map
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class PlayBroadcastSetupDataStoreImpl @Inject constructor(
        private val productDataStore: ProductDataStore,
        private val coverDataStore: CoverDataStore,
        private val titleDataStore: TitleDataStore,
        private val tagsDataStore: TagsDataStore,
        private val scheduleDataStore: BroadcastScheduleDataStore,
) : PlayBroadcastSetupDataStore, TitleDataStore by titleDataStore, TagsDataStore by tagsDataStore {

    override fun overwrite(dataStore: PlayBroadcastSetupDataStore, modeExclusion: List<OverwriteMode>) {
        if (!modeExclusion.contains(OverwriteMode.Product)) {
            overwriteProductDataStore(dataStore)
        }

        if (!modeExclusion.contains(OverwriteMode.Cover)) {
            overwriteCoverDataStore(dataStore)
        }

        if (!modeExclusion.contains(OverwriteMode.Title)) {
            overwriteTitleDataStore(dataStore)
        }

        if (!modeExclusion.contains(OverwriteMode.Tags)) {
            overwriteTagsDataStore(dataStore)
        }

        overwriteBroadcastScheduleDataStore(dataStore)
    }

    override fun getProductDataStore(): ProductDataStore {
        return productDataStore
    }

    override fun getCoverDataStore(): CoverDataStore {
        return coverDataStore
    }

    override fun getBroadcastScheduleDataStore(): BroadcastScheduleDataStore {
        return scheduleDataStore
    }

    private fun overwriteProductDataStore(dataStore: ProductDataStore) {
        setSelectedProducts(dataStore.getSelectedProducts())
    }

    private fun overwriteCoverDataStore(dataStore: CoverDataStore) {
        dataStore.getSelectedCover()?.let(::setFullCover)
    }

    private fun overwriteTitleDataStore(dataStore: TitleDataStore) {
        val title = dataStore.getTitle()
        if (title is PlayTitleUiModel.HasTitle) setTitle(title.title)
    }

    private fun overwriteTagsDataStore(dataStore: TagsDataStore) {
        val tags = dataStore.getTags()
        setTags(tags)
    }

    private fun overwriteBroadcastScheduleDataStore(dataStore: BroadcastScheduleDataStore) {
        dataStore.getSchedule()?.let(::setBroadcastSchedule)
    }

    /**
     * Product
     */
    override fun getObservableSelectedProducts(): Flow<List<ProductData>> {
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

    override suspend fun uploadSelectedCover(channelId: String): NetworkResult<Unit> {
        return coverDataStore.uploadSelectedCover(channelId)
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

    /**
     * Title
     */
    override fun getTitleDataStore(): TitleDataStore {
        return titleDataStore
    }

    /**
     * Broadcast Schedule
     */
    override fun getObservableSchedule(): LiveData<BroadcastScheduleUiModel> {
        return scheduleDataStore.getObservableSchedule()
    }

    override fun getSchedule(): BroadcastScheduleUiModel? {
        return scheduleDataStore.getSchedule()
    }

    override fun setBroadcastSchedule(scheduleDate: BroadcastScheduleUiModel) {
        scheduleDataStore.setBroadcastSchedule(scheduleDate)
    }

    override suspend fun updateBroadcastSchedule(channelId: String, scheduledTime: Date): NetworkResult<Unit> {
        return scheduleDataStore.updateBroadcastSchedule(channelId, scheduledTime)
    }

    override suspend fun deleteBroadcastSchedule(channelId: String): NetworkResult<Unit> {
        return scheduleDataStore.deleteBroadcastSchedule(channelId)
    }
}