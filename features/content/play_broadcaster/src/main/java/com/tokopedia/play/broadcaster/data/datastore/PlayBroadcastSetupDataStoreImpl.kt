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
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class PlayBroadcastSetupDataStoreImpl @Inject constructor(
        private val coverDataStore: CoverDataStore,
        private val titleDataStore: TitleDataStore,
        private val tagsDataStore: TagsDataStore,
        private val scheduleDataStore: BroadcastScheduleDataStore,
        private val interactiveDataStore: InteractiveDataStore,
) : PlayBroadcastSetupDataStore,
    TitleDataStore by titleDataStore,
    TagsDataStore by tagsDataStore,
    InteractiveDataStore by interactiveDataStore {

    override fun overwrite(dataStore: PlayBroadcastSetupDataStore, modeExclusion: List<OverwriteMode>) {
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

    override fun getCoverDataStore(): CoverDataStore {
        return coverDataStore
    }

    override fun getBroadcastScheduleDataStore(): BroadcastScheduleDataStore {
        return scheduleDataStore
    }

    override fun getInteractiveDataStore(): InteractiveDataStore {
        return interactiveDataStore
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
     * Cover
     */
    override fun getObservableSelectedCover(): LiveData<PlayCoverUiModel> {
        return coverDataStore.getObservableSelectedCover()
    }

    override fun getSelectedCoverAsFlow(): Flow<PlayCoverUiModel> {
        return coverDataStore.getSelectedCoverAsFlow()
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

    override suspend fun uploadSelectedCover(authorId: String, channelId: String): NetworkResult<Unit> {
        return coverDataStore.uploadSelectedCover(authorId, channelId)
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
