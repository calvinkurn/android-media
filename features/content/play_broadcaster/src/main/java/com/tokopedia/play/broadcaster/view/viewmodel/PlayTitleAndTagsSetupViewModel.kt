package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.*
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.domain.usecase.GetAddedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetRecommendedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.SetChannelTagsUseCase
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.ui.model.title.PlayTitleUiModel
import com.tokopedia.play.broadcaster.ui.validator.tag.TagSetupValidator
import com.tokopedia.play.broadcaster.ui.validator.title.TitleSetupValidator
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.util.event.Event
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.filterIsInstance
import javax.inject.Inject

/**
 * Created by jegul on 17/02/21
 */
class PlayTitleAndTagsSetupViewModel @Inject constructor(
        private val hydraConfigStore: HydraConfigStore,
        private val dispatcher: CoroutineDispatchers,
        private val setupDataStore: PlayBroadcastSetupDataStore,
        private val getAddedChannelTagsUseCase: GetAddedChannelTagsUseCase,
        private val getRecommendedChannelTagsUseCase: GetRecommendedChannelTagsUseCase,
) : ViewModel(), TitleSetupValidator, TagSetupValidator {

    val observableRecommendedTagsModel: LiveData<List<PlayTagUiModel>>
        get() = _observableRecommendedTagsModel
    val observableUploadEvent: LiveData<Event<NetworkResult<Unit>>>
        get() = _observableUploadEvent

    val observableTitle: LiveData<PlayTitleUiModel>
        get() = setupDataStore.getObservableTitle()
                .filterIsInstance<PlayTitleUiModel.HasTitle>()
                .asLiveData(viewModelScope.coroutineContext + dispatcher.computation)

    private val _observableAddedTags = MutableLiveData(setupDataStore.getTags())
    private val _observableRecommendedTags = MutableLiveData<Set<String>>()
    private val _observableRecommendedTagsModel = MediatorLiveData<List<PlayTagUiModel>>().apply {
        addSource(_observableAddedTags) {
            value = _observableRecommendedTags.value.orEmpty().map { tag ->
                PlayTagUiModel(
                        tag = tag,
                        isChosen = it.contains(tag)
                )
            }
        }
        addSource(_observableRecommendedTags) {
            value = it.map { tag ->
                PlayTagUiModel(
                        tag = tag,
                        isChosen = addedTags.contains(tag)
                )
            }
        }
    }

    private val _observableUploadEvent = MutableLiveData<Event<NetworkResult<Unit>>>()

    private val validTagRegex = Regex("[a-zA-Z0-9 ]+")

    private val addedTags: Set<String>
        get() = _observableAddedTags.value.orEmpty()

    init {
        getTags()
    }

    override fun isTitleValid(title: String): Boolean {
        return title.isNotBlank() && title.length <= hydraConfigStore.getMaxTitleChars()
    }

    override fun isTagValid(tag: String): Boolean {
        return tag.length in 2..32 && validTagRegex.matches(tag)
    }

    fun toggleTag(tag: String) {
        if(!isTagValid(tag)) return

        val oldAddedTags = addedTags
        val newAddedTags = if (!oldAddedTags.contains(tag)) oldAddedTags + tag
        else oldAddedTags - tag

        refreshAddedTags(newAddedTags)
    }

    fun saveTitleAndTags(title: String) {
        setupDataStore.setTitle(title)
        setupDataStore.setTags(addedTags)
    }

    fun finishSetup(title: String) {
        saveTitleAndTags(title)

        _observableUploadEvent.value = Event(NetworkResult.Loading)

        viewModelScope.launch(dispatcher.main) {

            try {
                uploadTags()
                /**
                 * Upload title after tags because when title is success, the channel will already be complete draft,
                 * even if the tags return error
                 */
                uploadTitle()

                _observableUploadEvent.value = Event(NetworkResult.Success(Unit))
            } catch (e: Throwable) {
                _observableUploadEvent.value = Event(NetworkResult.Fail(e))
            }
        }
    }

    private suspend fun uploadTags() {
        val isSuccess = setupDataStore.uploadTags(hydraConfigStore.getChannelId())
        if (!isSuccess) error("Set Channel Tag Failed")
    }

    private suspend fun uploadTitle() = withContext(dispatcher.io) {
        return@withContext setupDataStore.uploadTitle(hydraConfigStore.getChannelId())
    }

    private fun refreshAddedTags(newTags: Set<String>) {
        _observableAddedTags.value = newTags
    }

    /**
     * Mock data
     */
    private fun getTags() {
        viewModelScope.launch {
            _observableRecommendedTags.value = getRecommendedTags().toSet()
        }
    }

    private suspend fun getRecommendedTags(): List<String> = withContext(dispatcher.io) {
        val recommendedTags = getRecommendedChannelTagsUseCase.apply {
            setChannelId(hydraConfigStore.getChannelId())
        }.executeOnBackground()

        listOf(
                "Baju",
                "Review",
                "Tas",
                "Hiburan",
                "Produk",
                "Fashion",
                "Topi",
        )

//        return@withContext recommendedTags.recommendedTags.tags
    }

    private suspend fun getAddedTags(): List<String> = withContext(dispatcher.io) {
        val addedTags = getAddedChannelTagsUseCase.apply {
            setChannelId(hydraConfigStore.getChannelId())
        }.executeOnBackground()

        return@withContext addedTags.recommendedTags.tags
    }
}