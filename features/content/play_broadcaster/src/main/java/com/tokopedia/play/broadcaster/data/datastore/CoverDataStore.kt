package com.tokopedia.play.broadcaster.data.datastore

import androidx.lifecycle.LiveData
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play_common.model.result.NetworkResult

/**
 * Created by jegul on 23/06/20
 */
interface CoverDataStore {

    fun getObservableSelectedCover(): LiveData<PlayCoverUiModel>

    fun getSelectedCover(): PlayCoverUiModel?

    fun setFullCover(cover: PlayCoverUiModel)

    fun updateCoverState(state: CoverSetupState)

    fun updateCoverTitle(title: String)

    suspend fun uploadSelectedCover(channelId: String): NetworkResult<Unit>

    suspend fun uploadCoverTitle(channelId: String): NetworkResult<Unit>
}