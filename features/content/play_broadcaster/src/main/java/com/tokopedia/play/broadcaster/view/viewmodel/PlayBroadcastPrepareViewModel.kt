package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.*
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.domain.usecase.CreateLiveStreamChannelUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetLiveFollowersDataUseCase
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.FollowerDataUiModel
import com.tokopedia.play.broadcaster.ui.model.LiveStreamInfoUiModel
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.model.result.map
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by jegul on 20/05/20
 */
class PlayBroadcastPrepareViewModel @Inject constructor(
        private val mDataStore: PlayBroadcastDataStore,
        private val channelConfigStore: ChannelConfigStore,
        private val dispatcher: CoroutineDispatchers,
        private val getLiveFollowersDataUseCase: GetLiveFollowersDataUseCase,
        private val createLiveStreamChannelUseCase: CreateLiveStreamChannelUseCase,
        private val userSession: UserSessionInterface,
        private val playBroadcastMapper: PlayBroadcastMapper
) : ViewModel() {

    private val channelId: String
        get() = channelConfigStore.getChannelId()

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher.main)

    val title: String
        get() = mDataStore.getSetupDataStore().getSelectedCover()?.title ?: throw IllegalStateException("Cover / Cover Title is null")

    val maxDurationDesc: String
        get() = try { channelConfigStore.getMaxDurationDesc() } catch (e: Throwable) { "" }

    val observableFollowers: LiveData<FollowerDataUiModel>
        get() = _observableFollowers
    private val _observableFollowers = MutableLiveData<FollowerDataUiModel>()

    val observableCreateLiveStream: LiveData<NetworkResult<LiveStreamInfoUiModel>>
        get() = _observableCreateLiveStream
    private val _observableCreateLiveStream = MutableLiveData<NetworkResult<LiveStreamInfoUiModel>>()
    private val _observableIngestUrl: LiveData<String> = MediatorLiveData<String>().apply {
        addSource(_observableCreateLiveStream) {
            if (it is NetworkResult.Success) setIngestUrl(it.data.ingestUrl)
        }
    }

    private val ingestUrlObserver = object : Observer<String> {
        override fun onChanged(t: String?) {}
    }

    init {
        _observableFollowers.value = FollowerDataUiModel.init(MAX_FOLLOWERS_PREVIEW)
        scope.launch {
            _observableFollowers.value = getLiveFollowers()
        }
        _observableIngestUrl.observeForever(ingestUrlObserver)
    }

    override fun onCleared() {
        super.onCleared()
        _observableIngestUrl.removeObserver(ingestUrlObserver)
    }

    fun setDataFromSetupDataStore(setupDataStore: PlayBroadcastSetupDataStore) {
        mDataStore.setFromSetupStore(setupDataStore)
    }

    fun createLiveStream() {
        if (!isDataAlreadyValid()) {
            _observableCreateLiveStream.value = NetworkResult.Fail(IllegalStateException("Oops tambah cover dulu sebelum mulai"))
            return
        }

        _observableCreateLiveStream.value = NetworkResult.Loading
        scope.launch {
            val liveStream = doCreateLiveStream(channelId).map { playBroadcastMapper.mapLiveStream(channelId, it) }
            _observableCreateLiveStream.value = liveStream
        }
    }

    private suspend fun doCreateLiveStream(channelId: String) = withContext(dispatcher.io) {
        return@withContext try {
            NetworkResult.Success(createLiveStreamChannelUseCase.apply {
                val cover = mDataStore.getSetupDataStore().getSelectedCover() ?: throw IllegalStateException("Cover is not set")
                val coverImage =
                        if (cover.croppedCover !is CoverSetupState.Cropped) throw IllegalStateException("Cover image is not set")
                        else cover.croppedCover.coverImage
                params = CreateLiveStreamChannelUseCase.createParams(
                        channelId = channelId,
                        title = cover.title,
                        thumbnail = coverImage.toString()
                )
            }.executeOnBackground())
        } catch (e: Throwable) {
            NetworkResult.Fail(e)
        }
    }

    private suspend fun getLiveFollowers(): FollowerDataUiModel = withContext(dispatcher.io) {
        getLiveFollowersDataUseCase.params = GetLiveFollowersDataUseCase.createParams(userSession.shopId, MAX_FOLLOWERS_PREVIEW)
        return@withContext try {
            playBroadcastMapper.mapLiveFollowers(getLiveFollowersDataUseCase.executeOnBackground())
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

    private fun setIngestUrl(ingestUrl: String) {
        channelConfigStore.setIngestUrl(ingestUrl)
    }

    companion object {
        private const val MAX_FOLLOWERS_PREVIEW = 3
    }
}