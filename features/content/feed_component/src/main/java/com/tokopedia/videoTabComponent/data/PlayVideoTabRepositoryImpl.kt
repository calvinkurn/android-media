package com.tokopedia.videoTabComponent.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.widget.data.PlayWidgetReminder
import com.tokopedia.play.widget.domain.PlayWidgetReminderUseCase
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.reminded
import com.tokopedia.videoTabComponent.domain.PlayVideoTabRepository
import com.tokopedia.videoTabComponent.domain.model.data.ContentSlotResponse
import com.tokopedia.videoTabComponent.domain.model.data.VideoPageParams
import com.tokopedia.videoTabComponent.domain.usecase.GetPlayContentUseCase
import dagger.Lazy
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class PlayVideoTabRepositoryImpl @Inject constructor(
    private val baseDispatcher: CoroutineDispatchers,
    private val getPlayContentUseCase: GetPlayContentUseCase,
    private val lazyReminderUseCase: Lazy<PlayWidgetReminderUseCase>
) : PlayVideoTabRepository {

    private val reminderUseCase: PlayWidgetReminderUseCase
        get() = lazyReminderUseCase.get()

    override suspend fun getPlayData(params: VideoPageParams): ContentSlotResponse {
        try {
            return getPlayContentUseCase.execute(params)
        } catch (e: Throwable) {
            Timber.e(e)
            throw e
        }
    }

    override suspend fun getPlayDetailPageResult(currentLivePageCursor: String, sourceId: String, sourceType: String, group: String): ContentSlotResponse {
        try {
            return getPlayContentUseCase.execute(VideoPageParams(cursor = currentLivePageCursor, sourceId = sourceId, sourceType = sourceType, group = group))
        } catch (e: Throwable) {
            Timber.e(e)
            throw e
        }
    }

    override suspend fun updateToggleReminder(
        channelId: String,
        reminderType: PlayWidgetReminderType
    ): PlayWidgetReminder {
        return withContext(baseDispatcher.io) {
            reminderUseCase.setRequestParams(PlayWidgetReminderUseCase.createParams(channelId, reminderType.reminded))
            reminderUseCase.executeOnBackground()
        }
    }
}
