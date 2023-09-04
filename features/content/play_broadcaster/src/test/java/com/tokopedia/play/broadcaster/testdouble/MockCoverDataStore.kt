package com.tokopedia.play.broadcaster.testdouble

import androidx.lifecycle.LiveData
import com.tokopedia.play.broadcaster.data.datastore.CoverDataStore
import com.tokopedia.play.broadcaster.data.datastore.CoverDataStoreImpl
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow

/**
 * Created by jegul on 25/09/20
 */
class MockCoverDataStore(
    dispatcherProvider: CoroutineDispatchers,
) : CoverDataStore {

    private val realImpl = CoverDataStoreImpl(dispatcherProvider, mockk())

    private var uploadSelectedCoverResponse: () -> NetworkResult<Unit> = {NetworkResult.Success(Unit) }

    override fun getObservableSelectedCover(): LiveData<PlayCoverUiModel> {
        return realImpl.getObservableSelectedCover()
    }

    override fun getSelectedCoverAsFlow(): Flow<PlayCoverUiModel> {
        return realImpl.getSelectedCoverAsFlow()
    }

    override fun getSelectedCover(): PlayCoverUiModel? {
        return realImpl.getSelectedCover()
    }

    override fun setFullCover(cover: PlayCoverUiModel) {
        realImpl.setFullCover(cover)
    }

    override fun updateCoverState(state: CoverSetupState) {
        realImpl.updateCoverState(state)
    }

    override suspend fun uploadSelectedCover(
        authorId: String,
        channelId: String
    ): NetworkResult<Unit> {
        return uploadSelectedCoverResponse()
    }

    fun setUploadSelectedCoverResponse(uploadSelectedCoverResponse: () -> NetworkResult<Unit>) {
        this.uploadSelectedCoverResponse = uploadSelectedCoverResponse
    }
}
