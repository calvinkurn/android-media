package com.tokopedia.play.broadcaster.data.datastore

import com.tokopedia.play.broadcaster.ui.model.title.PlayTitleUiModel
import com.tokopedia.play_common.model.result.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Created by jegul on 29/03/21
 */
interface TitleDataStore {

    fun getObservableTitle(): MutableStateFlow<PlayTitleUiModel>

    fun getTitle(): PlayTitleUiModel

    fun setTitle(title: String)

    suspend fun uploadTitle(
        authorId: String,
        channelId: String,
        title: String,
    ): NetworkResult<Unit>
}
