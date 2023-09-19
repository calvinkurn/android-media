package com.tokopedia.feedplus.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.producttag.view.uimodel.NetworkResult
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.createpost.common.domain.usecase.cache.DeleteMediaPostCacheUseCase
import com.tokopedia.feedplus.domain.FeedRepository
import com.tokopedia.feedplus.presentation.model.ActiveTabSource
import com.tokopedia.feedplus.presentation.model.CreateContentType
import com.tokopedia.feedplus.presentation.model.FeedMainEvent
import com.tokopedia.feedplus.presentation.model.FeedTabModel
import com.tokopedia.feedplus.presentation.model.MetaModel
import com.tokopedia.feedplus.presentation.model.SwipeOnboardingStateModel
import com.tokopedia.feedplus.presentation.onboarding.OnBoardingPreferences
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
class FeedMainViewModel @AssistedInject constructor(
    @Assisted val activeTabSource: ActiveTabSource,
    private val repository: FeedRepository,
    private val deletePostCacheUseCase: DeleteMediaPostCacheUseCase,
    private val onBoardingPreferences: OnBoardingPreferences,
    private val userSession: UserSessionInterface,
    private val uiEventManager: UiEventManager<FeedMainEvent>
) : ViewModel(), OnBoardingPreferences by onBoardingPreferences {

    @AssistedFactory
    interface Factory {
        fun create(activeTabSource: ActiveTabSource): FeedMainViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            activeTabSource: ActiveTabSource
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(activeTabSource) as T
            }
        }
    }

    private val _feedTabs = MutableStateFlow<NetworkResult<FeedTabModel>?>(null)
    val feedTabs get() = _feedTabs.asStateFlow()

    private val _metaData = MutableStateFlow(MetaModel.Empty)
    val metaData get() = _metaData.asStateFlow()

    private val _isPageResumed = MutableLiveData<Boolean>(null)
    val isPageResumed get() = _isPageResumed

    private val _isMuted = MutableStateFlow<Boolean?>(null)
    val isMuted get() = _isMuted.asStateFlow()

    private val _swipeOnBoardingState = MutableStateFlow(
        SwipeOnboardingStateModel.Empty.copy(
            hasShown = onBoardingPreferences.hasShownSwipeOnBoarding() && userSession.isLoggedIn
        )
    )

    val uiEvent: Flow<FeedMainEvent?>
        get() = uiEventManager.event

    val displayName: String
        get() = userSession.name

    private val _isLoggedIn = AtomicBoolean(userSession.isLoggedIn)
    val isLoggedIn: Boolean
        get() = _isLoggedIn.get()

    val isShortEntryPointShowed: Boolean
        get() {
            val feedCreateContentData = _metaData.value
            return feedCreateContentData.entryPoints.find {
                it.type == CreateContentType.ShortVideo
            } != null
        }

    init {
        viewModelScope.launch {
            _swipeOnBoardingState
                .map { it.isEligibleToShow }
                .distinctUntilChanged()
                .collectLatest { isEligible ->
                    if (!isEligible) return@collectLatest
                    uiEventManager.emitEvent(FeedMainEvent.ShowSwipeOnboarding)

                    if (userSession.isLoggedIn) onBoardingPreferences.setHasShownSwipeOnBoarding()
                    _swipeOnBoardingState.update { it.copy(hasShown = true) }
                }
        }
    }

    fun resumePage() {
        _isPageResumed.value = true
    }

    fun pausePage() {
        _isPageResumed.value = false
    }

    fun muteSound() {
        _isMuted.value = true
    }

    fun unmuteSound() {
        _isMuted.value = false
    }

    fun setActiveTab(position: Int) {
        viewModelScope.launch {
            val tabModel =
                (_feedTabs.value as? NetworkResult.Success<FeedTabModel>)?.data ?: return@launch
            if (position < tabModel.data.size) {
                val data = tabModel.data[position]
                emitEvent(FeedMainEvent.SelectTab(data, position))
            }
        }
    }

    /**
     * type == Backend tab type (pls don't ask me why)
     */
    fun setActiveTab(type: String) {
        viewModelScope.launch {
            val tabModel =
                (_feedTabs.value as? NetworkResult.Success<FeedTabModel>)?.data ?: return@launch
            tabModel.data.forEachIndexed { index, tab ->
                if (tab.type.equals(type, true)) {
                    emitEvent(FeedMainEvent.SelectTab(tab, index))
                    return@forEachIndexed
                }
            }
        }
    }

    fun scrollCurrentTabToTop() {
        viewModelScope.launch {
            val tabValue = _feedTabs.value
            if (tabValue !is NetworkResult.Success) return@launch
            tabValue.data.data.forEachIndexed { _, tab ->
                if (!tab.isActive) return@forEachIndexed
                uiEventManager.emitEvent(
                    FeedMainEvent.ScrollToTop(tab.key)
                )
            }
        }
    }

    fun consumeEvent(event: FeedMainEvent) {
        viewModelScope.launch {
            uiEventManager.clearEvent(event.id)
        }
    }

    fun deletePostCache() {
        viewModelScope.launch {
            deletePostCacheUseCase(Unit)
        }
    }

    fun updateUserInfo() {
        val isPrevLoggedIn = _isLoggedIn.getAndSet(userSession.isLoggedIn)
        if (!isPrevLoggedIn && isLoggedIn) {
            emitEvent(FeedMainEvent.HasJustLoggedIn)
        }
    }

    fun onPostDataLoaded(isLoaded: Boolean) {
        _swipeOnBoardingState.update {
            it.copy(onDataLoaded = isLoaded)
        }
    }

    fun setReadyToShowOnboarding() {
        _swipeOnBoardingState.update {
            it.copy(isReadyToShow = true)
        }
    }

    fun fetchFeedTabs() {
        _feedTabs.value = NetworkResult.Loading
        viewModelScope.launchCatchError(block = {
            val response = repository.getTabs(activeTabSource)
            _feedTabs.value = NetworkResult.Success(response.tab)
        }) {
            _feedTabs.value = NetworkResult.Error(it)
        }
    }

    fun fetchFeedMetaData() {
        viewModelScope.launch {
            try {
                val response = repository.getMeta(activeTabSource)
                _metaData.value = response
            } catch (_: Throwable) {
                // ignored
            }
        }
    }

    private fun emitEvent(event: FeedMainEvent) {
        viewModelScope.launch {
            uiEventManager.emitEvent(event)
        }
    }
}
