package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.dispatcher.PlayBroadcastDispatcher
import com.tokopedia.play.broadcaster.domain.usecase.AddMediaUseCase
import com.tokopedia.play.broadcaster.domain.usecase.AddProductTagUseCase
import com.tokopedia.play.broadcaster.domain.usecase.CreateLiveStreamChannelUseCase
import com.tokopedia.play.broadcaster.mocker.PlayBroadcastMocker
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by jegul on 20/05/20
 */
class PlayBroadcastSetupViewModel @Inject constructor(
        @Named(PlayBroadcastDispatcher.MAIN) dispatcher: CoroutineDispatcher,
        @Named(PlayBroadcastDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
        private val addProductTagUseCase: AddProductTagUseCase,
        private val addMediaUseCase: AddMediaUseCase,
        private val createLiveStreamChannelUseCase: CreateLiveStreamChannelUseCase
) : ViewModel() {

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher)

    val observableFollowers: LiveData<List<FollowerUiModel>>
        get() = _observableFollowers
    private val _observableFollowers = MutableLiveData<List<FollowerUiModel>>()

    val observableSetupChannel: LiveData<ChannelSetupUiModel>
        get() = _observableSetupChannel
    private val _observableSetupChannel = MutableLiveData<ChannelSetupUiModel>()

    val observableCreateLiveStream: LiveData<NetworkResult<LiveStreamInfoUiModel>>
        get() = _observableCreateLiveStream
    private val _observableCreateLiveStream = MutableLiveData<NetworkResult<LiveStreamInfoUiModel>>()

    init {
        _observableFollowers.value = PlayBroadcastMocker.getMockUnknownFollower()
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

    private suspend fun updateProduct(channelId: String, productIds: List<String>) = withContext(ioDispatcher) {
        return@withContext addProductTagUseCase.apply {
            params = AddProductTagUseCase.createParams(
                    channelId = channelId,
                    productIds = productIds
            )
        }.executeOnBackground()
    }

    private suspend fun updateCover(channelId: String, coverUrl: String) = withContext(ioDispatcher) {
        return@withContext addMediaUseCase.apply {
            params = AddMediaUseCase.createParams(
                    channelId = channelId,
                    coverUrl = coverUrl
            )
        }.executeOnBackground()
    }

    private suspend fun createLiveStream(channelId: String) = withContext(ioDispatcher) {
        return@withContext createLiveStreamChannelUseCase.apply {
            params = CreateLiveStreamChannelUseCase.createParams(
                    channelId = channelId
            )
        }.executeOnBackground()
    }
}