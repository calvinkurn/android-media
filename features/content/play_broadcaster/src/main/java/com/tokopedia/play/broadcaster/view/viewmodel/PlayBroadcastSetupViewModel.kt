package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.dispatcher.PlayBroadcastDispatcher
import com.tokopedia.play.broadcaster.domain.usecase.*
import com.tokopedia.play.broadcaster.mocker.PlayBroadcastMocker
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
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
        private val getLiveFollowersDataUseCase: GetLiveFollowersDataUseCase,
        private val addMediaUseCase: AddMediaUseCase,
        private val createLiveStreamChannelUseCase: CreateLiveStreamChannelUseCase,
        private val userSession: UserSessionInterface
) : ViewModel() {

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher)

    val observableFollowers: LiveData<FollowerDataUiModel>
        get() = _observableFollowers
    private val _observableFollowers = MutableLiveData<FollowerDataUiModel>()

    val observableSetupChannel: LiveData<ChannelSetupUiModel>
        get() = _observableSetupChannel
    private val _observableSetupChannel = MutableLiveData<ChannelSetupUiModel>()

    val observableCreateChannel: LiveData<NetworkResult<LiveStreamInfoUiModel>>
        get() = _observableCreateChannel
    private val _observableCreateChannel = MutableLiveData<NetworkResult<LiveStreamInfoUiModel>>()

    init {
        _observableFollowers.value = FollowerDataUiModel.init(MAX_FOLLOWERS_PREVIEW)
        scope.launch {
            _observableFollowers.value = getLiveFollowers()
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
        _observableCreateChannel.value = NetworkResult.Loading

        scope.launch {
            delay(3000)
            _observableCreateChannel.value = NetworkResult.Success(PlayBroadcastMocker.getLiveStreamingInfo())
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

    private suspend fun getLiveFollowers(): FollowerDataUiModel = withContext(ioDispatcher) {
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