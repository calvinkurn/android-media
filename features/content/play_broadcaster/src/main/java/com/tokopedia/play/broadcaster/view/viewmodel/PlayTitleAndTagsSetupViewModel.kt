package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.*
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.domain.usecase.GetRecommendedChannelTagsUseCase
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.ui.validator.tag.TagSetupValidator
import com.tokopedia.play.broadcaster.ui.validator.title.TitleSetupValidator
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.util.event.Event
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by jegul on 17/02/21
 */
class PlayTitleAndTagsSetupViewModel @Inject constructor(
        private val hydraConfigStore: HydraConfigStore,
        private val dispatcher: CoroutineDispatchers,
        private val setupDataStore: PlayBroadcastSetupDataStore,
        private val getRecommendedChannelTagsUseCase: GetRecommendedChannelTagsUseCase,
) : ViewModel(), TitleSetupValidator, TagSetupValidator {

    val observableRecommendedTagsModel: LiveData<List<PlayTagUiModel>>
        get() = _observableRecommendedTagsModel
    val observableUploadEvent: LiveData<Event<NetworkResult<Unit>>>
        get() = _observableUploadEvent

    private val _observableAddedTags = MutableLiveData<Set<String>>()
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
    }

    private val _observableUploadEvent = MutableLiveData<Event<NetworkResult<Unit>>>()

    private val validTagRegex = Regex("[a-zA-Z0-9 ]+")

    private val addedTags = mutableSetOf<String>()

    init {
        getRecommendedTags()

        addedTags.addAll(getAddedTags())
        refreshAddedTags()
    }

    override fun isTitleValid(title: String): Boolean {
        return title.isNotBlank() && title.length <= hydraConfigStore.getMaxTitleChars()
    }

    override fun isTagValid(tag: String): Boolean {
        return tag.length in 2..32 && validTagRegex.matches(tag)
    }

    fun toggleTag(tag: String) {
        if(!isTagValid(tag)) return

        if (!addedTags.contains(tag)) addedTags.add(tag)
        else addedTags.remove(tag)

        refreshAddedTags()
    }

    fun removeTag(tag: String) {
        addedTags.remove(tag)
        refreshAddedTags()
    }

    fun finishSetup(title: String) {
        setupDataStore.setTitle(title)
        setupDataStore.setTags(addedTags)

        _observableUploadEvent.value = Event(NetworkResult.Loading)

        viewModelScope.launch(dispatcher.main) {

            try {
                uploadTags(_observableAddedTags.value.orEmpty())
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

    private suspend fun uploadTags(tags: Set<String>) = withContext(dispatcher.io) {
        delay(2500)
        Unit
    }

    private suspend fun uploadTitle() = withContext(dispatcher.io) {
        return@withContext setupDataStore.uploadTitle(hydraConfigStore.getChannelId())
    }

    private fun refreshAddedTags() {
        _observableAddedTags.value = addedTags
    }

    /**
     * Mock data
     */
    private fun getRecommendedTags() {
        viewModelScope.launch {
            val channelTags = getRecommendedChannelTagsUseCase.apply {
                setChannelId(hydraConfigStore.getChannelId())
            }.executeOnBackground()

            _observableRecommendedTags.value = channelTags.recommendedTags.tags.toSet()

            _observableRecommendedTagsModel
        }
    }

    private fun getAddedTags() = setOf(
            "Style",
            "Trending",
    )
}