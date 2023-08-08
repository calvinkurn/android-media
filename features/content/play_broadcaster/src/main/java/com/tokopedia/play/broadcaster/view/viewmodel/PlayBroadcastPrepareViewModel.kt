package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.*
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStore
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.domain.usecase.CreateLiveStreamChannelUseCase
import com.tokopedia.play.broadcaster.error.ClientException
import com.tokopedia.play.broadcaster.error.PlayErrorCode
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.LiveStreamInfoUiModel
import com.tokopedia.play.broadcaster.ui.model.title.PlayTitleUiModel
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.model.result.map
import com.tokopedia.play_common.util.event.Event
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Created by jegul on 20/05/20
 */
class PlayBroadcastPrepareViewModel @Inject constructor(
    private val mDataStore: PlayBroadcastDataStore,
    private val sharedPref: HydraSharedPreferences,
    private val hydraConfigStore: HydraConfigStore,
    private val setupDataStore: PlayBroadcastSetupDataStore,
    private val channelConfigStore: ChannelConfigStore,
    private val dispatcher: CoroutineDispatchers,
    private val createLiveStreamChannelUseCase: CreateLiveStreamChannelUseCase,
    private val playBroadcastMapper: PlayBroadcastMapper
) : ViewModel() {

    private val channelId: String
        get() = channelConfigStore.getChannelId()

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher.main)

    val maxDurationDesc: String
        get() = try { channelConfigStore.getMaxDurationDesc() } catch (e: Throwable) { "" }

    val maxTitleChars: Int
        get() = hydraConfigStore.getMaxTitleChars()

    val observableUploadTitleEvent: LiveData<Event<NetworkResult<Unit>>>
        get() = _observableUploadTitleEvent
    private val _observableUploadTitleEvent = MutableLiveData<Event<NetworkResult<Unit>>>()

    val observableCreateLiveStream: LiveData<NetworkResult<LiveStreamInfoUiModel>>
        get() = _observableCreateLiveStream
    private val _observableCreateLiveStream = MutableLiveData<NetworkResult<LiveStreamInfoUiModel>>()

    private var _isFromSwitchAccount = MutableStateFlow(false)
    val isFromSwitchAccount
        get() = _isFromSwitchAccount.value

    fun setDataFromSetupDataStore(setupDataStore: PlayBroadcastSetupDataStore) {
        mDataStore.setFromSetupStore(setupDataStore)
    }

    /** Setup Title */
    fun uploadTitle(authorId: String, title: String) {
        viewModelScope.launchCatchError(dispatcher.main, block = {
            val result = withContext(dispatcher.io) {
                setupDataStore.uploadTitle(authorId, hydraConfigStore.getChannelId(), title)
            }

            _observableUploadTitleEvent.value = Event(result)
        }) {
            _observableUploadTitleEvent.value = Event(NetworkResult.Fail(it))
        }
    }

    fun createLiveStream() {
        _observableCreateLiveStream.value = NetworkResult.Loading
        scope.launch {
            val liveStream = doCreateLiveStream(channelId).map { playBroadcastMapper.mapLiveStream(channelId, it) }
            _observableCreateLiveStream.value = liveStream

            if (liveStream is NetworkResult.Success) {
                setIngestUrl(liveStream.data.ingestUrl)
            }
        }
    }

    private suspend fun doCreateLiveStream(channelId: String) = withContext(dispatcher.io) {
        return@withContext try {
            NetworkResult.Success(createLiveStreamChannelUseCase.apply {
                val cover = mDataStore.getSetupDataStore().getSelectedCover() ?: throw ClientException(PlayErrorCode.Play001)
                val coverImage = when (cover.croppedCover) {
                    is CoverSetupState.Cropped -> cover.croppedCover.coverImage
                    is CoverSetupState.GeneratedCover -> cover.croppedCover.coverImage
                    else -> throw ClientException(PlayErrorCode.Play001)
                }
                val titleModel = mDataStore.getSetupDataStore().getTitle()
                val title = if (titleModel is PlayTitleUiModel.HasTitle) titleModel.title else throw ClientException(PlayErrorCode.Play002)
                params = CreateLiveStreamChannelUseCase.createParams(
                        channelId = channelId,
                        title = title,
                        thumbnail = coverImage.toString()
                )
            }.executeOnBackground())
        } catch (e: Throwable) {
            NetworkResult.Fail(e)
        }
    }

    fun isCoverAvailable(): Boolean {
        val currentCover = mDataStore.getSetupDataStore().getSelectedCover()
        return when(val cover = currentCover?.croppedCover) {
            is CoverSetupState.Cropped.Uploaded -> {
                cover.localImage != null || cover.coverImage.toString().isNotEmpty()
            }
            is CoverSetupState.GeneratedCover -> {
                cover.coverImage.isNotEmpty()
            }
            else -> false
        }
    }

    private fun setIngestUrl(ingestUrl: String) {
        channelConfigStore.setIngestUrl(ingestUrl)
    }

    fun setFromSwitchAccount(value: Boolean) {
        _isFromSwitchAccount.update { value }
    }
}
