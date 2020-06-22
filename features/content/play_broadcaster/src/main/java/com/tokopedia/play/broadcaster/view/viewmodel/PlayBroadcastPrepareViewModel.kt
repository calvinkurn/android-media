package com.tokopedia.play.broadcaster.view.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.domain.usecase.AddMediaUseCase
import com.tokopedia.play.broadcaster.domain.usecase.AddProductTagUseCase
import com.tokopedia.play.broadcaster.domain.usecase.CreateLiveStreamChannelUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetLiveFollowersDataUseCase
import com.tokopedia.play.broadcaster.mocker.PlayBroadcastMocker
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.broadcaster.view.state.SetupDataState
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by jegul on 20/05/20
 */
class PlayBroadcastPrepareViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatcherProvider,
        private val addProductTagUseCase: AddProductTagUseCase,
        private val getLiveFollowersDataUseCase: GetLiveFollowersDataUseCase,
        private val addMediaUseCase: AddMediaUseCase,
        private val createLiveStreamChannelUseCase: CreateLiveStreamChannelUseCase,
        private val userSession: UserSessionInterface
) : ViewModel() {

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher.main)

    val observableFollowers: LiveData<FollowerDataUiModel>
        get() = _observableFollowers
    private val _observableFollowers = MutableLiveData<FollowerDataUiModel>()

    val observableSetupChannel: LiveData<ChannelSetupUiModel>
        get() = _observableSetupChannel
    private val _observableSetupChannel = MutableLiveData<ChannelSetupUiModel>()

    val observableCreateLiveStream: LiveData<NetworkResult<LiveStreamInfoUiModel>>
        get() = _observableCreateLiveStream
    private val _observableCreateLiveStream = MutableLiveData<NetworkResult<LiveStreamInfoUiModel>>()

    init {
        _observableFollowers.value = FollowerDataUiModel.init(MAX_FOLLOWERS_PREVIEW)
        scope.launch {
            _observableFollowers.value = getLiveFollowers()
        }
    }

    fun saveCompleteChannel(productList: List<ProductContentUiModel>,
                            coverUri: Uri,
                            title: String) {
        scope.launch {
            _observableSetupChannel.value = ChannelSetupUiModel(
                    cover = PlayCoverUiModel(
                            coverImage = coverUri,
                            title = title,
                            state = SetupDataState.Uploaded,
                            source = CoverSourceEnum.NONE
                    ),
                    selectedProductList = productList
            )
        }
    }

    fun createLiveStream() {
        _observableCreateLiveStream.value = NetworkResult.Loading
        scope.launch {
            delay(3000)
            _observableCreateLiveStream.value = NetworkResult.Success(PlayBroadcastMocker.getLiveStreamingInfo())
        }
    }

    fun setupChannelWithData(
            selectedProducts: List<ProductContentUiModel>,
            cover: PlayCoverUiModel
    ) {
        _observableSetupChannel.value = ChannelSetupUiModel(
                cover = cover,
                selectedProductList = selectedProducts
        )
    }

    private fun selectedProductIds(productList: List<ProductContentUiModel>): List<String> = productList.map { it.id.toString() }.toList()

    private suspend fun updateProduct(channelId: String, productIds: List<String>) = withContext(dispatcher.io) {
        return@withContext addProductTagUseCase.apply {
            params = AddProductTagUseCase.createParams(
                    channelId = channelId,
                    productIds = productIds
            )
        }.executeOnBackground()
    }

    private suspend fun updateCover(channelId: String, coverUrl: String) = withContext(dispatcher.io) {
        return@withContext addMediaUseCase.apply {
            params = AddMediaUseCase.createParams(
                    channelId = channelId,
                    coverUrl = coverUrl
            )
        }.executeOnBackground()
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

    companion object {
        private const val MAX_FOLLOWERS_PREVIEW = 3
    }
}