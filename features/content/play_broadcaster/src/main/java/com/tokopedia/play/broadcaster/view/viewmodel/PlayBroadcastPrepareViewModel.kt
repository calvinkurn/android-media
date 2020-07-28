package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.domain.usecase.AddMediaUseCase
import com.tokopedia.play.broadcaster.domain.usecase.AddProductTagUseCase
import com.tokopedia.play.broadcaster.domain.usecase.CreateLiveStreamChannelUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetLiveFollowersDataUseCase
import com.tokopedia.play.broadcaster.mocker.PlayBroadcastMocker
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.SetupDataState
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by jegul on 20/05/20
 */
class PlayBroadcastPrepareViewModel @Inject constructor(
        private val mDataStore: PlayBroadcastDataStore,
        private val dispatcher: CoroutineDispatcherProvider,
        private val addProductTagUseCase: AddProductTagUseCase,
        private val getLiveFollowersDataUseCase: GetLiveFollowersDataUseCase,
        private val addMediaUseCase: AddMediaUseCase,
        private val createLiveStreamChannelUseCase: CreateLiveStreamChannelUseCase,
        private val userSession: UserSessionInterface
) : ViewModel() {

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher.main)

    var title: String
        get() = observableSetupChannel.value?.cover?.title ?: throw IllegalStateException("Cover / Cover Title is null")
        set(value) {
            val currentDataStore = mDataStore.getSetupDataStore()
            currentDataStore.updateCoverTitle(value)
            setDataFromSetupDataStore(currentDataStore)
        }

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

    fun setDataFromSetupDataStore(setupDataStore: PlayBroadcastSetupDataStore) {
        mDataStore.setFromSetupStore(setupDataStore)
        val cover = setupDataStore.getSelectedCover()
        val productList = setupDataStore.getSelectedProducts()
        requireNotNull(cover)
        _observableSetupChannel.value = ChannelSetupUiModel(
                cover = PlayCoverUiModel(
                        croppedCover = cover.croppedCover,
                        title = cover.title,
                        state = SetupDataState.Uploaded
                ),
                selectedProductList = productList.map { ProductContentUiModel.createFromData(it) }
        )
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