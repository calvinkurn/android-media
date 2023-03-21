package com.tokopedia.feedplus.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.model.FeedComplaintSubmitReportResponse
import com.tokopedia.content.common.report_content.model.FeedReportRequestParamModel
import com.tokopedia.content.common.usecase.FeedComplaintSubmitReportUseCase
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.feedplus.domain.mapper.MapperFeedTabs
import com.tokopedia.feedplus.domain.usecase.FeedXHeaderUseCase
import com.tokopedia.feedplus.presentation.model.ContentCreationItem
import com.tokopedia.feedplus.presentation.model.ContentCreationTypeItem
import com.tokopedia.feedplus.presentation.model.CreatorType
import com.tokopedia.feedplus.presentation.model.FeedMainEvent
import com.tokopedia.feedplus.presentation.model.FeedTabsModel
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
    private val dispatchers: CoroutineDispatchers,
    private val onboardingPreferences: OnboardingPreferences,
    private val userSession: UserSessionInterface,
    private val uiEventManager: UiEventManager<FeedMainEvent>,
) : ViewModel(), OnboardingPreferences by onboardingPreferences {

    private val _isInClearView = MutableLiveData<Boolean>(false)
    val isInClearView: LiveData<Boolean>
        get() = _isInClearView

    private val _feedTabs = MutableStateFlow<Result<FeedTabsModel>?>(null)
    val feedTabs get() = _feedTabs.asStateFlow()

    private val _reportResponse = MutableLiveData<Result<FeedComplaintSubmitReportResponse>>()
    val reportResponse: LiveData<Result<FeedComplaintSubmitReportResponse>>
        get() = _reportResponse

    private val _feedCreateContentBottomSheetData =
        MutableLiveData<Result<List<ContentCreationTypeItem>>>()
    val feedCreateContentBottomSheetData: LiveData<Result<List<ContentCreationTypeItem>>>
        get() = _feedCreateContentBottomSheetData

    private val _swipeOnboardingState = MutableStateFlow(SwipeOnboardingStateModel.Empty)

    val uiEvent: Flow<FeedMainEvent?>
        get() = uiEventManager.event

    val userName: String
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

    fun consumeEvent(event: FeedMainEvent) {
        viewModelScope.launch {
            uiEventManager.clearEvent(event.id)
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
                    FeedXHeaderUseCase.createParam()
                )
                feedXHeaderUseCase.executeOnBackground()
            }
            _feedTabs.value = Success(
                MapperFeedTabs.transform(response.feedXHeaderData, userSession.isLoggedIn)
            )

            handleCreationData(
                MapperFeedTabs.getCreationBottomSheetData(
                    response.feedXHeaderData
                )
            )
        }) {
            _feedTabs.value = Fail(it)
            _feedCreateContentBottomSheetData.value = Fail(it)
        }
    }

    fun reportContent(feedReportRequestParamModel: FeedReportRequestParamModel) {
        viewModelScope.launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                submitReportUseCase.setRequestParams(
                    FeedComplaintSubmitReportUseCase.createParam(
                        feedReportRequestParamModel
                    )
                )
                submitReportUseCase.executeOnBackground()
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

    fun toggleClearView(clearView: Boolean) {
        _isInClearView.value = clearView
    }

    private fun handleCreationData(creationDataList: List<ContentCreationItem>) {
        val authorUserdataList = creationDataList.find { it.type == CreatorType.USER }?.items
        val authorShopDataList = creationDataList.find { it.type == CreatorType.SHOP }?.items

        val creatorList =
            (authorUserdataList?.filter { it.isActive ?: false } ?: emptyList()) +
                (authorShopDataList?.filter { it.isActive ?: false } ?: emptyList()).distinct()
        _feedCreateContentBottomSheetData.value = Success(creatorList)
    }

    private fun emitEvent(event: FeedMainEvent) {
        viewModelScope.launch {
            uiEventManager.emitEvent(event)
        }
    }
}
