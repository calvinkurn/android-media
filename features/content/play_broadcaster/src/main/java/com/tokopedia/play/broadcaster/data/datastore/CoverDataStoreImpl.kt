package com.tokopedia.play.broadcaster.data.datastore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.play.broadcaster.domain.usecase.PlayBroadcastUpdateChannelUseCase
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.SetupDataState
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 23/06/20
 */
class CoverDataStoreImpl @Inject constructor(
        private val dispatcher: CoroutineDispatchers,
        private val updateChannelUseCase: PlayBroadcastUpdateChannelUseCase,
        private val userSession: UserSessionInterface
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

    override suspend fun uploadSelectedCover(channelId: String): NetworkResult<Unit> {
        return try {
            updateCover(channelId)
            getSelectedCover()?.let {
                setFullCover(it.copy(state = SetupDataState.Uploaded))
            }
            NetworkResult.Success(Unit)
        } catch (e: Throwable) {
            NetworkResult.Fail(e)
        }
    }

    private suspend fun updateCover(channelId: String) = withContext(dispatcher.io) {
        val currentCover = getSelectedCover()
        val coverUrl = when (val croppedCover = currentCover?.croppedCover) {
            is CoverSetupState.Cropped -> croppedCover.coverImage.toString()
            else -> throw IllegalStateException("Something went wrong: Cover url not found")
        }

        updateChannelUseCase.apply {
            setQueryParams(
                    PlayBroadcastUpdateChannelUseCase.createUpdateFullCoverRequest(
                            channelId = channelId,
                            authorId = userSession.shopId,
                            coverUrl = coverUrl
                    )
            )
        }
        return@withContext updateChannelUseCase.executeOnBackground()
    }
}