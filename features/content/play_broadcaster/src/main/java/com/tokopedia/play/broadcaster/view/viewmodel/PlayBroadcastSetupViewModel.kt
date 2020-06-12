package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.dispatcher.PlayBroadcastDispatcher
import com.tokopedia.play.broadcaster.domain.usecase.AddMediaUseCase
import com.tokopedia.play.broadcaster.domain.usecase.AddProductTagUseCase
import com.tokopedia.play.broadcaster.domain.usecase.CreateChannelUseCase
import com.tokopedia.play.broadcaster.domain.usecase.CreateLiveStreamChannelUseCase
import com.tokopedia.play.broadcaster.mocker.PlayBroadcastMocker
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by jegul on 20/05/20
 */
class PlayBroadcastSetupViewModel @Inject constructor(
        @Named(PlayBroadcastDispatcher.MAIN) dispatcher: CoroutineDispatcher,
        @Named(PlayBroadcastDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
        private val createChannelUseCase: CreateChannelUseCase,
        private val addProductTagUseCase: AddProductTagUseCase,
        private val addMediaUseCase: AddMediaUseCase,
        private val createLiveStreamChannelUseCase: CreateLiveStreamChannelUseCase,
        private val userSession: UserSessionInterface
) : ViewModel() {

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher)

    val observableFollowers: LiveData<List<FollowerUiModel>>
        get() = _observableFollowers
    private val _observableFollowers = MutableLiveData<List<FollowerUiModel>>()

    val observableSetupChannel: LiveData<ChannelSetupUiModel>
        get() = _observableSetupChannel
    private val _observableSetupChannel = MutableLiveData<ChannelSetupUiModel>()

    val observableCreateChannel: LiveData<Result<LiveStreamInfoUiModel>>
        get() = _observableCreateChannel
    private val _observableCreateChannel = MutableLiveData<Result<LiveStreamInfoUiModel>>()

    val observableSelectedProduct: LiveData<List<ProductContentUiModel>>
        get() = _observableSelectedProduct
    private val _observableSelectedProduct = MutableLiveData<List<ProductContentUiModel>>()

    val observableCover: LiveData<PlayCoverUiModel>
        get() = _observableCover
    private val _observableCover = MutableLiveData<PlayCoverUiModel>()

    init {
        _observableFollowers.value = PlayBroadcastMocker.getMockUnknownFollower()
    }

    fun saveCompleteChannel(productList: List<ProductContentUiModel>, coverUrl: String, title: String) {
        scope.launch {
            _observableSetupChannel.value = ChannelSetupUiModel(
                    title = title,
                    coverUrl = coverUrl,
                    selectedProductList = productList
            )
        }
    }

    fun createChannel() {
        /**
         * TODO("require staging environment")
         * scope.launchCatchError( block = {
         * _observableSetupChannel.value?.let {
         * val channelId = createChannel(it.title)
         * addProductTags(channelId.channelId, selectedProductIds(it.selectedProductList))
         * addCover(channelId.channelId, it.coverUrl)
         * val media  = createLiveStream(channelId.channelId)
         * _observableCreateChannel.value = Success(PlayBroadcasterUiMapper.mapLiveStream(channelId.channelId, media))
         * }
         * }) {
         *  _observableCreateChannel.value = Fail(Throwable("create channel failed"))
         * }
         *
         */

        _observableCreateChannel.value = Success(PlayBroadcastMocker.getLiveStreamingInfo())
    }

    fun setupChannelWithData(
            selectedProducts: List<ProductContentUiModel>,
            cover: PlayCoverUiModel
    ) {
        _observableSelectedProduct.value = selectedProducts
        _observableCover.value = cover
    }

    private fun selectedProductIds(productList: List<ProductContentUiModel>): List<String> = productList.map { it.id.toString() }.toList()

    private suspend fun createChannel(title: String) = withContext(ioDispatcher) {
        return@withContext createChannelUseCase.apply {
            params = CreateChannelUseCase.createParams(
                    title = title,
                    authorId = userSession.shopId
            )
        }.executeOnBackground()
    }

    private suspend fun addProductTags(channelId: String, productIds: List<String>) = withContext(ioDispatcher) {
        return@withContext addProductTagUseCase.apply {
            params = AddProductTagUseCase.createParams(
                    channelId = channelId,
                    productIds = productIds
            )
        }.executeOnBackground()
    }

    private suspend fun addCover(channelId: String, coverUrl: String) = withContext(ioDispatcher) {
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