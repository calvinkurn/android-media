package com.tokopedia.play.broadcaster.testdouble

import androidx.lifecycle.LiveData
import com.tokopedia.play.broadcaster.data.datastore.CoverDataStore
import com.tokopedia.play.broadcaster.data.datastore.CoverDataStoreImpl
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import io.mockk.mockk

/**
 * Created by jegul on 25/09/20
 */
class MockCoverDataStore(
        dispatcherProvider: CoroutineDispatchers,
        private val uploadCoverTitleException: Throwable = IllegalStateException()
) : CoverDataStore {

    private val realImpl = CoverDataStoreImpl(dispatcherProvider, mockk(), mockk())

    private var isSuccess: Boolean = false

    fun setIsSuccess(isSuccess: Boolean) {
        this.isSuccess = isSuccess
    }

    override fun getObservableSelectedCover(): LiveData<PlayCoverUiModel> {
        return realImpl.getObservableSelectedCover()
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

    override fun updateCoverTitle(title: String) {
        realImpl.updateCoverTitle(title)
    }

    override suspend fun uploadSelectedCover(channelId: String): NetworkResult<Unit> {
        return realImpl.uploadSelectedCover(channelId)
    }

    override suspend fun uploadCoverTitle(channelId: String): NetworkResult<Unit> {
        return if (isSuccess) NetworkResult.Success(Unit)
        else NetworkResult.Fail(uploadCoverTitleException)
    }
}