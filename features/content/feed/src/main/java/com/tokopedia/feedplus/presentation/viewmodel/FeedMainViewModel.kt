package com.tokopedia.feedplus.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.model.FeedComplaintSubmitReportResponse
import com.tokopedia.content.common.producttag.view.uimodel.NetworkResult
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.createpost.common.domain.usecase.cache.DeleteMediaPostCacheUseCase
import com.tokopedia.feedplus.domain.repository.FeedRepository
import com.tokopedia.feedplus.presentation.model.CreateContentType
import com.tokopedia.feedplus.presentation.model.FeedDataModel
import com.tokopedia.feedplus.presentation.model.FeedMainEvent
import com.tokopedia.feedplus.presentation.model.MetaModel
import com.tokopedia.feedplus.presentation.model.SwipeOnboardingStateModel
import com.tokopedia.feedplus.presentation.onboarding.OnboardingPreferences
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
class FeedMainViewModel @Inject constructor(
    private val repository: FeedRepository,
    private val deletePostCacheUseCase: DeleteMediaPostCacheUseCase,
    private val onboardingPreferences: OnboardingPreferences,
    private val userSession: UserSessionInterface,
    private val uiEventManager: UiEventManager<FeedMainEvent>
) : ViewModel(), OnboardingPreferences by onboardingPreferences {

    private val _feedTabs = MutableStateFlow<NetworkResult<List<FeedDataModel>>?>(null)
    val feedTabs get() = _feedTabs.asStateFlow()

    private val _metaData = MutableStateFlow(MetaModel.Empty)
    val metaData get() = _metaData.asStateFlow()

    private val _isPageResumed = MutableLiveData<Boolean>(null)
    val isPageResumed get() = _isPageResumed

    private val _reportResponse = MutableLiveData<Result<FeedComplaintSubmitReportResponse>>()
    val reportResponse: LiveData<Result<FeedComplaintSubmitReportResponse>>
        get() = _reportResponse

    private val _currentTabIndex = MutableLiveData<Int>()
    val currentTabIndex: LiveData<Int>
        get() = _currentTabIndex

    private val _swipeOnboardingState = MutableStateFlow(
        SwipeOnboardingStateModel.Empty.copy(
            hasShown = onboardingPreferences.hasShownSwipeOnboarding() && userSession.isLoggedIn
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
            return feedCreateContentData.eligibleCreationEntryPoints.find {
                it.type == CreateContentType.ShortVideo
            } != null
        }

    init {
        viewModelScope.launch {
            _swipeOnboardingState
                .map { it.isEligibleToShow }
                .distinctUntilChanged()
                .collectLatest { isEligible ->
                    if (!isEligible) return@collectLatest
                    uiEventManager.emitEvent(FeedMainEvent.ShowSwipeOnboarding)

                    if (userSession.isLoggedIn) onboardingPreferences.setHasShownSwipeOnboarding()
                    _swipeOnboardingState.update { it.copy(hasShown = true) }
                }
        }
    }

    fun resumePage() {
        _isPageResumed.value = true
    }

    fun pausePage() {
        _isPageResumed.value = false
    }

    fun changeCurrentTabByIndex(index: Int) {
        _currentTabIndex.value = index
    }

    fun changeCurrentTabByType(type: String) {
        val tabValue = _feedTabs.value
        if (tabValue is NetworkResult.Success) {
            tabValue.data.forEachIndexed { index, tab ->
                if (tab.type == type && tab.isActive) {
                    _currentTabIndex.value = index
                }
            }
        }
    }

    fun scrollCurrentTabToTop() {
        viewModelScope.launch {
            val tabValue = _feedTabs.value
            if (tabValue !is NetworkResult.Success) return@launch
            tabValue.data.forEachIndexed { _, tab ->
                if (!tab.isActive) return@forEachIndexed
                uiEventManager.emitEvent(
                    FeedMainEvent.ScrollToTop(tab.key)
                )
            }
        }
    }

    fun getCurrentTabType(): String {
        val tabValue = _feedTabs.value
        return if (tabValue is NetworkResult.Success) {
            currentTabIndex.value?.let { idx ->
                tabValue.data.getOrNull(idx)?.type.orEmpty()
            }.orEmpty()
        } else {
            ""
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
        _swipeOnboardingState.update {
            it.copy(onDataLoaded = isLoaded)
        }
    }

    fun setReadyToShowOnboarding() {
        _swipeOnboardingState.update {
            it.copy(isReadyToShow = true)
        }
    }

    fun fetchFeedTabs() {
        _feedTabs.value = NetworkResult.Loading
        viewModelScope.launchCatchError(block = {
            val response = repository.getTabs()
            _feedTabs.value = NetworkResult.Success(response.data)
        }) {
            _feedTabs.value = NetworkResult.Error(it)
        }
    }

    fun fetchFeedMetaData() {
        viewModelScope.launchCatchError(block = {
            val response = repository.getMeta()
            _metaData.value = response
        }) {
        }
    }

    fun getTabType(index: Int): String {
        val tabValue = _feedTabs.value
        return if (tabValue is NetworkResult.Success && tabValue.data.size > index) {
            tabValue.data[index].type
        } else {
            ""
        }
    }
    private fun emitEvent(event: FeedMainEvent) {
        viewModelScope.launch {
            uiEventManager.emitEvent(event)
        }
    }
}
