package com.tokopedia.play.broadcaster.data.datastore

import androidx.lifecycle.LiveData
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.view.state.CoverSetupState

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