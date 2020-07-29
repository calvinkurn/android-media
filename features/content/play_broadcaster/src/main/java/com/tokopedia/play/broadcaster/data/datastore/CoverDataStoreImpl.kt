package com.tokopedia.play.broadcaster.data.datastore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.play.broadcaster.domain.usecase.AddMediaUseCase
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.SetupDataState
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 23/06/20
 */
class CoverDataStoreImpl @Inject constructor(
        private val dispatcher: CoroutineDispatcherProvider,
        private val addMediaUseCase: AddMediaUseCase
): CoverDataStore {

    private val _selectedCoverLiveData = MutableLiveData<PlayCoverUiModel>()

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
                        is CoverSetupState.Cropped -> croppedCover.coverImage.path ?: throw IllegalStateException("Cover Image path is null")
                        else -> throw IllegalStateException("Cover url must not be null")
                    })
        }.executeOnBackground()
    }
}