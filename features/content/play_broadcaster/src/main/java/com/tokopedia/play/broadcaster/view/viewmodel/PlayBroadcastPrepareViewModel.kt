package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.domain.usecase.CreateLiveStreamChannelUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetLiveFollowersDataUseCase
import com.tokopedia.play.broadcaster.mocker.PlayBroadcastMocker
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.ui.model.FollowerDataUiModel
import com.tokopedia.play.broadcaster.ui.model.LiveStreamInfoUiModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by jegul on 20/05/20
 */
class PlayBroadcastPrepareViewModel @Inject constructor(
        private val mDataStore: PlayBroadcastDataStore,
        private val dispatcher: CoroutineDispatcherProvider,
        private val getLiveFollowersDataUseCase: GetLiveFollowersDataUseCase,
        private val createLiveStreamChannelUseCase: CreateLiveStreamChannelUseCase,
        private val userSession: UserSessionInterface
) : ViewModel() {

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher.main)

    val title: String
        get() = mDataStore.getSetupDataStore().getSelectedCover()?.title ?: throw IllegalStateException("Cover / Cover Title is null")

    val observableFollowers: LiveData<FollowerDataUiModel>
        get() = _observableFollowers
    private val _observableFollowers = MutableLiveData<FollowerDataUiModel>()

    val observableCreateLiveStream: LiveData<NetworkResult<LiveStreamInfoUiModel>>
        get() = _observableCreateLiveStream
    private val _observableCreateLiveStream = MutableLiveData<NetworkResult<LiveStreamInfoUiModel>>()

    init {
        _observableFollowers.value = FollowerDataUiModel.init(MAX_FOLLOWERS_PREVIEW)
        scope.launch {
            _observableFollowers.value = getLiveFollowers()
        }
    }

    fun setDataFromSetupDataStore(setupDataStore: PlayBroadcastSetupDataStore) {
        mDataStore.setFromSetupStore(setupDataStore)
    }

    fun createLiveStream() {
        _observableCreateLiveStream.value = NetworkResult.Loading
        scope.launch {
            delay(3000)
            _observableCreateLiveStream.value =
                    if (isDataAlreadyValid()) NetworkResult.Success(PlayBroadcastMocker.getLiveStreamingInfo())
                    else NetworkResult.Fail(IllegalStateException("Oops tambah cover dulu sebelum mulai"))
        }
    }

    private suspend fun createLiveStream(channelId: String) = withContext(dispatcher.io) {
        return@withContext createLiveStreamChannelUseCase.apply {
            params = CreateLiveStreamChannelUseCase.createParams(
                    channelId = channelId
            )
        }.executeOnBackground()
    }

    private suspend fun getLiveFollowers(): FollowerDataUiModel = withContext(dispatcher.io) {
        getLiveFollowersDataUseCase.params = GetLiveFollowersDataUseCase.createParams(userSession.shopId, MAX_FOLLOWERS_PREVIEW)
        return@withContext try {
            PlayBroadcastUiMapper.mapLiveFollowers(getLiveFollowersDataUseCase.executeOnBackground())
        } catch (e: Throwable) {
            FollowerDataUiModel.init(MAX_FOLLOWERS_PREVIEW)
        }
    }

    private fun isDataAlreadyValid(): Boolean {
        val currentProduct = mDataStore.getSetupDataStore().getTotalSelectedProduct()
        val currentCover = mDataStore.getSetupDataStore().getSelectedCover()

        val isProductValid = currentProduct > 0
        val isCoverValid = currentCover?.croppedCover is CoverSetupState.Cropped

        return isProductValid && isCoverValid
    }

    companion object {
        private const val MAX_FOLLOWERS_PREVIEW = 3
    }
}