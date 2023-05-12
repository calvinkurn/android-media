package com.tokopedia.feedplus.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.model.FeedComplaintSubmitReportResponse
import com.tokopedia.content.common.usecase.FeedComplaintSubmitReportUseCase
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.createpost.common.domain.usecase.cache.DeleteMediaPostCacheUseCase
import com.tokopedia.feedplus.data.FeedXHeaderRequestFields
import com.tokopedia.feedplus.domain.mapper.MapperFeedTabs
import com.tokopedia.feedplus.domain.usecase.FeedXHeaderUseCase
import com.tokopedia.feedplus.presentation.model.ContentCreationItem
import com.tokopedia.feedplus.presentation.model.ContentCreationTypeItem
import com.tokopedia.feedplus.presentation.model.CreateContentType
import com.tokopedia.feedplus.presentation.model.CreatorType
import com.tokopedia.feedplus.presentation.model.FeedDataModel
import com.tokopedia.feedplus.presentation.model.FeedMainEvent
import com.tokopedia.feedplus.presentation.model.MetaModel
import com.tokopedia.feedplus.presentation.model.SwipeOnboardingStateModel
import com.tokopedia.feedplus.presentation.onboarding.OnboardingPreferences
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
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
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
class FeedMainViewModel @Inject constructor(
    private val feedXHeaderUseCase: FeedXHeaderUseCase,
    private val submitReportUseCase: FeedComplaintSubmitReportUseCase,
    private val deletePostCacheUseCase: DeleteMediaPostCacheUseCase,
    private val dispatchers: CoroutineDispatchers,
    private val onboardingPreferences: OnboardingPreferences,
    private val userSession: UserSessionInterface,
    private val uiEventManager: UiEventManager<FeedMainEvent>,
) : ViewModel(), OnboardingPreferences by onboardingPreferences {

    private val _feedTabs = MutableStateFlow<Result<List<FeedDataModel>>?>(null)
    val feedTabs get() = _feedTabs.asStateFlow()

    private val _metaData = MutableStateFlow<Result<MetaModel>?>(null)
    val metaData get() = _metaData.asStateFlow()

    private val _isPageResumed = MutableLiveData<Boolean>(false)
    val isPageResumed get() = _isPageResumed

    private val _reportResponse = MutableLiveData<Result<FeedComplaintSubmitReportResponse>>()
    val reportResponse: LiveData<Result<FeedComplaintSubmitReportResponse>>
        get() = _reportResponse

    private val _feedCreateContentBottomSheetData =
        MutableLiveData<Result<List<ContentCreationTypeItem>>>()
    val feedCreateContentBottomSheetData: LiveData<Result<List<ContentCreationTypeItem>>>
        get() = _feedCreateContentBottomSheetData

    private val _currentTabIndex = MutableLiveData<Int>()
    val currentTabIndex: LiveData<Int>
        get() = _currentTabIndex

    private val _swipeOnboardingState = MutableStateFlow(SwipeOnboardingStateModel.Empty)

    val uiEvent: Flow<FeedMainEvent?>
        get() = uiEventManager.event

    val displayName: String
        get() = userSession.name

    private val _isLoggedIn = AtomicBoolean(userSession.isLoggedIn)
    val isLoggedIn: Boolean
        get() = _isLoggedIn.get()

    init {
        viewModelScope.launch {
            _swipeOnboardingState
                .map { it.isEligibleToShow }
                .distinctUntilChanged()
                .collectLatest { isEligible ->
                    if (!isEligible) return@collectLatest
                    uiEventManager.emitEvent(FeedMainEvent.ShowSwipeOnboarding)

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

    fun changeCurrentTabByType(type: String) {
        feedTabs.value?.let {
            if (it is Success) {
                it.data.forEachIndexed { index, tab ->
                    if (tab.type == type && tab.isActive) {
                        _currentTabIndex.value = index
                    }
                }
            }
        }
    }

    fun scrollCurrentTabToTop() {
        viewModelScope.launch {
            val feedTabsValue = feedTabs.value
            if (feedTabsValue !is Success) return@launch
            feedTabsValue.data.forEachIndexed { _, tab ->
                if (!tab.isActive) return@forEachIndexed
                uiEventManager.emitEvent(
                    FeedMainEvent.ScrollToTop(tab.key)
                )
            }
        }
    }

    fun getCurrentTabType() =
        feedTabs.value?.let {
            if (it is Success) {
                it.data[currentTabIndex.value ?: 0].type
            } else {
                ""
            }
        } ?: ""

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
            val mappedData = MapperFeedTabs.transform(response.feedXHeaderData)
            _feedTabs.value = Success(mappedData.data)
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
            val mappedData = MapperFeedTabs.transform(response.feedXHeaderData)
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

    fun getTabType(index: Int): String =
        feedTabs.value?.let {
            if (it is Success && it.data.size > index) {
                it.data[index].type
            } else {
                ""
            }
        } ?: ""

    fun reportContent(feedReportRequestParamModel: FeedComplaintSubmitReportUseCase.Param) {
        viewModelScope.launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                submitReportUseCase(feedReportRequestParamModel)
            }
            if (response.data.success.not()) {
                throw MessageErrorException("Error in Reporting")
            } else {
                _reportResponse.value = Success(response)
            }
        }) {
            _reportResponse.value = Fail(it)
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

        authorShopDataList?.find {
            it.type == CreateContentType.CREATE_POST && it.isActive
        }?.let {
            creatorList.add(it)
        } ?: authorUserdataList?.find {
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
