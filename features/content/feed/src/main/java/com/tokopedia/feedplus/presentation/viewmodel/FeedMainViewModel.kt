package com.tokopedia.feedplus.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.feedplus.domain.FeedRepository
import com.tokopedia.feedplus.presentation.model.ActiveTabSource
import com.tokopedia.feedplus.presentation.model.CreateContentType
import com.tokopedia.feedplus.presentation.model.FeedDataModel
import com.tokopedia.feedplus.presentation.model.FeedMainEvent
import com.tokopedia.feedplus.presentation.model.FeedTabModel
import com.tokopedia.feedplus.presentation.model.FeedTooltipEvent
import com.tokopedia.feedplus.presentation.model.MetaModel
import com.tokopedia.feedplus.presentation.model.SwipeOnboardingStateModel
import com.tokopedia.feedplus.presentation.onboarding.OnBoardingPreferences
import com.tokopedia.feedplus.presentation.tooltip.FeedSearchTooltipCategory
import com.tokopedia.feedplus.presentation.tooltip.FeedTooltipManager
import com.tokopedia.feedplus.presentation.util.FeedContentManager
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play_common.model.result.NetworkResult
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
    private val onBoardingPreferences: OnBoardingPreferences,
    private val userSession: UserSessionInterface,
    private val uiEventManager: UiEventManager<FeedMainEvent>,
    private val tooltipManager: FeedTooltipManager,
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

    private val _swipeOnBoardingState = MutableStateFlow(
        SwipeOnboardingStateModel.Empty.copy(
            hasShown = onBoardingPreferences.hasShownSwipeOnBoarding() && userSession.isLoggedIn
        )
    )

    val uiEvent: Flow<FeedMainEvent?>
        get() = uiEventManager.event

    val tooltipEvent: Flow<FeedTooltipEvent?>
        get() = tooltipManager.tooltipEvent

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

    val selectedTab: FeedDataModel?
        get() {
            val tabs = _feedTabs.value

            return if (tabs is NetworkResult.Success<FeedTabModel>) {
                tabs.data.data.firstOrNull { it.isSelected }
            } else {
                null
            }
        }

    val currentTooltipCategory: FeedSearchTooltipCategory
        get() = tooltipManager.currentCategory

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
        FeedContentManager.muteState.value = true
    }

    fun unmuteSound() {
        FeedContentManager.muteState.value = false
    }

    fun setActiveTab(position: Int) {
        viewModelScope.launch {
            val tabModel =
                (_feedTabs.value as? NetworkResult.Success<FeedTabModel>)?.data ?: return@launch
            if (position < tabModel.data.size) {
                _feedTabs.value = NetworkResult.Success(
                    tabModel.copy(
                        data = tabModel.data.mapIndexed { idx, tab ->
                            tab.copy(
                                isSelected = idx == position,
                                hasNewContent = if (idx == position) false else tab.hasNewContent
                            )
                        }
                    )
                )
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
            _feedTabs.value = NetworkResult.Success(
                tabModel.copy(
                    data = tabModel.data.map { tab ->
                        tab.copy(
                            isSelected = tab.type.equals(type, true)
                        )
                    }
                )
            )
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

    fun consumeEvent(event: FeedTooltipEvent) {
        viewModelScope.launch {
            tooltipManager.clearTooltipEvent(event.id)
        }
    }

    fun setHasShownTooltip() {
        tooltipManager.setHasShownTooltip()
    }

    fun updateUserInfo() {
        val isPrevLoggedIn = _isLoggedIn.getAndSet(userSession.isLoggedIn)
        if (!isPrevLoggedIn && isLoggedIn) {
            emitEvent(FeedMainEvent.HasJustLoggedIn)
        }
    }

    fun setReadyToShowOnboarding() {
        _swipeOnBoardingState.update {
            it.copy(isReadyToShow = true)
        }
    }

    fun setDataEligibleForOnboarding(isEligible: Boolean) {
        _swipeOnBoardingState.update {
            it.copy(isDataEligibleForOnboarding = isEligible)
        }
    }

    fun fetchFeedTabs() {
        _feedTabs.value = NetworkResult.Loading
        viewModelScope.launchCatchError(block = {
            val response = repository.getTabs(activeTabSource)
            _feedTabs.value = NetworkResult.Success(response.tab)
        }) {
            _feedTabs.value = NetworkResult.Fail(it)
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
