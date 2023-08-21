package com.tokopedia.feedplus.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.model.FeedXHeaderRequestFields
import com.tokopedia.content.common.usecase.FeedXHeaderUseCase
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.createpost.common.domain.usecase.cache.DeleteMediaPostCacheUseCase
import com.tokopedia.feedplus.domain.mapper.MapperFeedTabs
import com.tokopedia.feedplus.presentation.model.ActiveTabSource
import com.tokopedia.feedplus.presentation.model.ContentCreationItem
import com.tokopedia.feedplus.presentation.model.ContentCreationTypeItem
import com.tokopedia.feedplus.presentation.model.CreateContentType
import com.tokopedia.feedplus.presentation.model.CreatorType
import com.tokopedia.feedplus.presentation.model.FeedDataModel
import com.tokopedia.feedplus.presentation.model.FeedMainEvent
import com.tokopedia.feedplus.presentation.model.FeedTabModel
import com.tokopedia.feedplus.presentation.model.MetaModel
import com.tokopedia.feedplus.presentation.model.SwipeOnboardingStateModel
import com.tokopedia.feedplus.presentation.onboarding.OnboardingPreferences
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
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
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
class FeedMainViewModel @AssistedInject constructor(
    @Assisted val activeTabSource: ActiveTabSource,
    private val feedXHeaderUseCase: FeedXHeaderUseCase,
    private val deletePostCacheUseCase: DeleteMediaPostCacheUseCase,
    private val dispatchers: CoroutineDispatchers,
    private val onboardingPreferences: OnboardingPreferences,
    private val userSession: UserSessionInterface,
    private val uiEventManager: UiEventManager<FeedMainEvent>
) : ViewModel(), OnboardingPreferences by onboardingPreferences {

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
    private val _feedTabs = MutableStateFlow<Result<FeedTabModel>?>(null)
    val feedTabs get() = _feedTabs.asStateFlow()

    private val _metaData = MutableStateFlow<Result<MetaModel>?>(null)
    val metaData get() = _metaData.asStateFlow()

    val activeTab: FeedDataModel?
        get() = _activeTab.value
    private val _activeTab = MutableLiveData<FeedDataModel>()

    private val _isPageResumed = MutableLiveData<Boolean>(null)
    val isPageResumed get() = _isPageResumed

    private val _feedCreateContentBottomSheetData =
        MutableLiveData<Result<List<ContentCreationTypeItem>>>()
    val feedCreateContentBottomSheetData: LiveData<Result<List<ContentCreationTypeItem>>>
        get() = _feedCreateContentBottomSheetData

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
            val feedCreateContentData = _feedCreateContentBottomSheetData.value

            return feedCreateContentData is Success &&
                feedCreateContentData.data.find {
                it.type == CreateContentType.CREATE_SHORT_VIDEO
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

    fun setActiveTab(position: Int) {
        viewModelScope.launch {
            val tabModel = (_feedTabs.value as? Success<FeedTabModel>)?.data ?: return@launch
            if (position < tabModel.data.size) {
                val data = tabModel.data[position]
                emitSelectedTabEvent(data, position)
                // keep track active tab
                _activeTab.value = tabModel.data[position]
            }
        }
    }

    /**
     * type == Backend tab type (pls don't ask me why)
     */
    fun setActiveTab(type: String) {
        viewModelScope.launch {
            val tabModel = (_feedTabs.value as? Success<FeedTabModel>)?.data ?: return@launch
            tabModel.data.forEachIndexed { index, tab ->
                if (tab.type.equals(type, true)) {
                    emitSelectedTabEvent(tab, index)

                    // keep track active tab
                    _activeTab.value = tab
                    return@forEachIndexed
                }
            }
        }
    }

    private suspend fun emitSelectedTabEvent(data: FeedDataModel, position: Int) {
        uiEventManager.emitEvent(FeedMainEvent.SelectTab(data, position))
    }

    fun scrollCurrentTabToTop() {
        viewModelScope.launch {
            val feedTabsValue = feedTabs.value
            if (feedTabsValue !is Success) return@launch
            feedTabsValue.data.data.forEachIndexed { _, tab ->
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
        viewModelScope.launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                feedXHeaderUseCase.setRequestParams(
                    FeedXHeaderUseCase.createParam(
                        listOf(
                            FeedXHeaderRequestFields.TAB.value
                        )
                    )
                )
                feedXHeaderUseCase.executeOnBackground()
            }
            val mappedData = MapperFeedTabs.transform(response.feedXHeaderData, activeTabSource)
            _feedTabs.value = Success(mappedData.tab)
        }) {
            _feedTabs.value = Fail(it)
        }
    }

    fun fetchFeedMetaData() {
        viewModelScope.launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                feedXHeaderUseCase.setRequestParams(
                    FeedXHeaderUseCase.createParam(
                        listOf(
                            FeedXHeaderRequestFields.LIVE.value,
                            FeedXHeaderRequestFields.CREATION.value,
                            FeedXHeaderRequestFields.USER.value
                        )
                    )
                )
                feedXHeaderUseCase.executeOnBackground()
            }
            val mappedData = MapperFeedTabs.transform(response.feedXHeaderData, activeTabSource)
            _metaData.value = Success(mappedData.meta)

            handleCreationData(
                MapperFeedTabs.getCreationBottomSheetData(
                    response.feedXHeaderData
                )
            )
        }) {
            _metaData.value = Fail(it)
            _feedCreateContentBottomSheetData.value = Fail(it)
        }
    }

    /**
     * Creation Button Position is Static :
     * 1. Short
     * 2. Post
     * 3. Live
     */
    private fun handleCreationData(creationDataList: List<ContentCreationItem>) {
        val authorUserdataList = creationDataList.find { it.type == CreatorType.USER }?.items
        val authorShopDataList = creationDataList.find { it.type == CreatorType.SHOP }?.items

        val creatorList = mutableListOf<ContentCreationTypeItem>()

        authorShopDataList?.find {
            it.type == CreateContentType.CREATE_SHORT_VIDEO && it.isActive
        }?.let {
            creatorList.add(it)
        } ?: authorUserdataList?.find {
            it.type == CreateContentType.CREATE_SHORT_VIDEO && it.isActive
        }?.let {
            creatorList.add(it)
        }

        authorUserdataList?.find {
            it.type == CreateContentType.CREATE_POST && it.isActive
        }?.let {
            creatorList.add(it)
        } ?: authorShopDataList?.find {
            it.type == CreateContentType.CREATE_POST && it.isActive
        }?.let {
            creatorList.add(it)
        }

        authorShopDataList?.find {
            it.type == CreateContentType.CREATE_LIVE && it.isActive
        }?.let {
            creatorList.add(it)
        } ?: authorUserdataList?.find {
            it.type == CreateContentType.CREATE_LIVE && it.isActive
        }?.let {
            creatorList.add(it)
        }

        _feedCreateContentBottomSheetData.value = Success(creatorList)
    }

    private fun emitEvent(event: FeedMainEvent) {
        viewModelScope.launch {
            uiEventManager.emitEvent(event)
        }
    }
}
