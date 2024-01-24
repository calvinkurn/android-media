package com.tokopedia.inbox.universalinbox.view

import android.content.Intent
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.viewModelScope
import com.gojek.conversations.channel.ConversationsChannel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxAllCounterResponse
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWrapperResponse
import com.tokopedia.inbox.universalinbox.domain.mapper.UniversalInboxMenuMapper
import com.tokopedia.inbox.universalinbox.domain.mapper.UniversalInboxMiscMapper
import com.tokopedia.inbox.universalinbox.domain.mapper.UniversalInboxWidgetMetaMapper
import com.tokopedia.inbox.universalinbox.domain.usecase.UniversalInboxGetAllCounterUseCase
import com.tokopedia.inbox.universalinbox.domain.usecase.UniversalInboxGetAllDriverChannelsUseCase
import com.tokopedia.inbox.universalinbox.domain.usecase.UniversalInboxGetInboxMenuAndWidgetMetaUseCase
import com.tokopedia.inbox.universalinbox.domain.usecase.UniversalInboxGetProductRecommendationUseCase
import com.tokopedia.inbox.universalinbox.util.Result
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.INBOX_ADS_REFRESH_KEY
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.PAGE_NAME
import com.tokopedia.inbox.universalinbox.util.toggle.UniversalInboxAbPlatform
import com.tokopedia.inbox.universalinbox.view.uiState.UniversalInboxAutoScrollUiState
import com.tokopedia.inbox.universalinbox.view.uiState.UniversalInboxCombineState
import com.tokopedia.inbox.universalinbox.view.uiState.UniversalInboxErrorUiState
import com.tokopedia.inbox.universalinbox.view.uiState.UniversalInboxMenuUiState
import com.tokopedia.inbox.universalinbox.view.uiState.UniversalInboxNavigationUiState
import com.tokopedia.inbox.universalinbox.view.uiState.UniversalInboxProductRecommendationUiState
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetMetaErrorUiModel
import com.tokopedia.recommendation_widget_common.DEFAULT_VALUE_X_DEVICE
import com.tokopedia.recommendation_widget_common.DEFAULT_VALUE_X_SOURCE
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class UniversalInboxViewModel @Inject constructor(
    private val getAllCounterUseCase: UniversalInboxGetAllCounterUseCase,
    private val getInboxMenuAndWidgetMetaUseCase: UniversalInboxGetInboxMenuAndWidgetMetaUseCase,
    private val getRecommendationUseCase: UniversalInboxGetProductRecommendationUseCase,
    private val addWishListV2UseCase: AddToWishlistV2UseCase,
    private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
    private val getDriverChatCounterUseCase: UniversalInboxGetAllDriverChannelsUseCase,
    private val inboxMenuMapper: UniversalInboxMenuMapper,
    private val inboxMiscMapper: UniversalInboxMiscMapper,
    private val widgetMetaMapper: UniversalInboxWidgetMetaMapper,
    private val userSession: UserSessionInterface,
    private val dispatcher: CoroutineDispatchers,
    private val abTestPlatform: UniversalInboxAbPlatform
) : BaseViewModel(dispatcher.main), DefaultLifecycleObserver {

    private val _actionFlow =
        MutableSharedFlow<UniversalInboxAction>(extraBufferCapacity = 16)

    private val _inboxMenuUiState = MutableStateFlow(UniversalInboxMenuUiState())
    val inboxMenuUiState = _inboxMenuUiState.asStateFlow()

    private val _inboxNavigationState = MutableSharedFlow<UniversalInboxNavigationUiState>(
        extraBufferCapacity = 16,
        replay = 0
    )
    val inboxNavigationUiState = _inboxNavigationState.asSharedFlow()

    private val _productRecommendationState = MutableStateFlow(
        UniversalInboxProductRecommendationUiState()
    )
    val productRecommendationUiState = _productRecommendationState.asStateFlow()

    private val _autoScrollUiState = MutableStateFlow(UniversalInboxAutoScrollUiState())
    val autoScrollUiState = _autoScrollUiState.asStateFlow()

    private val _errorUiState = MutableSharedFlow<UniversalInboxErrorUiState>(
        extraBufferCapacity = 16
    )
    val errorUiState = _errorUiState.asSharedFlow()

    @VisibleForTesting
    var driverJob: Job? = null

    fun setupViewModelObserver() {
        _actionFlow.process()
        observeInboxMenuLocalFlow()
        observeDriverChannelFlow()
        observeInboxMenuWidgetMetaAndCounterFlow()
        loadInboxMenuAndWidgetMeta() // do not use processAction
    }

    fun processAction(action: UniversalInboxAction) {
        viewModelScope.launch {
            _actionFlow.emit(action)
        }
    }

    /**
     * Flow Observe
     */

    private fun Flow<UniversalInboxAction>.process() {
        onEach {
            when (it) {
                // Navigation process
                is UniversalInboxAction.NavigateWithIntent -> {
                    navigateWithIntent(it.intent, it.requestType)
                }
                is UniversalInboxAction.NavigateToPage -> {
                    navigateToPage(it.applink, it.requestType)
                }

                // General process
                is UniversalInboxAction.RefreshPage -> {
                    inboxMiscMapper.resetTopAdsBanner()
                    removeAllProductRecommendation(false)
                    loadInboxMenuAndWidgetMeta()
                }
                is UniversalInboxAction.RefreshCounter -> {
                    refreshCounter()
                }

                // Recommendation process
                is UniversalInboxAction.RefreshRecommendation -> {
                    removeAllProductRecommendation(true)
                    loadProductRecommendation(1) // Load first page
                }
                is UniversalInboxAction.LoadNextPage -> {
                    loadProductRecommendation(it.page)
                }
                is UniversalInboxAction.ResetUserScrollState -> {
                    resetUserScrollState()
                }
                is UniversalInboxAction.AutoScrollRecommendation -> {
                    autoScrollRecommendation()
                }

                // Widget process
                is UniversalInboxAction.RefreshDriverWidget -> {
                    resetDriverChannelFlow()
                }
            }
        }
            .flowOn(dispatcher.io)
            .launchIn(viewModelScope)
    }

    private fun observeInboxMenuLocalFlow() {
        viewModelScope.launch {
            getInboxMenuAndWidgetMetaUseCase.observeInboxMenuLocalSource()
        }
    }

    private fun observeDriverChannelFlow() {
        driverJob = viewModelScope.launch {
            getDriverChatCounterUseCase.observeDriverChannelFlow()
        }
    }

    private fun observeInboxMenuWidgetMetaAndCounterFlow() {
        viewModelScope.launch {
            combine(
                getInboxMenuAndWidgetMetaUseCase.observe(),
                getAllCounterUseCase.observe(),
                getDriverChatCounterUseCase.observe()
            ) { menu, counter, driverChannel ->
                UniversalInboxCombineState(menu, counter, driverChannel)
            }
                .filterNotNull()
                .collectLatest {
                    withContext(dispatcher.default) {
                        handleGetMenuAndCounterResult(it)
                    }
                }
        }
    }

    private fun handleGetMenuAndCounterResult(
        result: UniversalInboxCombineState
    ) {
        when {
            // Handle success case for all
            (
                result.menu is Result.Success &&
                    result.counter is Result.Success
                ) -> {
                onSuccessGetMenuAndCounter(
                    result.menu.data,
                    result.counter.data,
                    result.driverChannel,
                    shouldTrackImpression = true
                )
            }

            // Handle error case for menu
            (result.menu is Result.Error) -> {
                setErrorWidgetMeta()
                setFallbackInboxMenu(true)
                showErrorMessage(
                    Pair(result.menu.throwable, ::handleGetMenuAndCounterResult.name)
                )
            }

            // Handle loading or error case for counter
            (
                result.menu is Result.Success &&
                    (result.counter is Result.Error || result.counter is Result.Loading)
                ) -> {
                onSuccessGetMenuAndCounter(
                    result.menu.data,
                    UniversalInboxAllCounterResponse(), // Default 0 counter
                    result.driverChannel
                )
            }

            // Handle loading case for menu
            result.menu is Result.Loading -> {
                setLoadingInboxMenu()
            }
        }
    }

    private fun onSuccessGetMenuAndCounter(
        inboxResponse: UniversalInboxWrapperResponse?,
        counterResponse: UniversalInboxAllCounterResponse,
        driverResponse: Result<List<ConversationsChannel>>,
        shouldTrackImpression: Boolean = false
    ) {
        try {
            if (inboxResponse == null) {
                // If cache is null, it means new user or error
                // create the default menu first
                setFallbackInboxMenu(false)
                return
            }
            val widgetMeta = widgetMetaMapper.mapWidgetMetaToUiModel(
                widgetMetaResponse = inboxResponse.chatInboxMenu.widgetMenu,
                counterResponse = counterResponse,
                driverCounter = driverResponse
            ).apply {
                this.widgetError.isError = inboxResponse.chatInboxMenu.shouldShowLocalLoad
                this.widgetError.isLocalLoadLoading = false
            }
            val menuList = inboxMenuMapper.mapToInboxMenu(
                userSession = userSession,
                inboxMenuResponse = inboxResponse.chatInboxMenu.inboxMenu,
                counterResponse = counterResponse
            )
            val miscList = inboxMiscMapper.generateMiscMenu()
            _inboxMenuUiState.update { uiState ->
                uiState.copy(
                    isLoading = false,
                    widgetMeta = widgetMeta,
                    menuList = menuList,
                    miscList = miscList,
                    notificationCounter =
                    counterResponse.inboxCounter.notifCenterWrapperUnread.notifUnread,
                    shouldTrackImpression = shouldTrackImpression
                )
            }
        } catch (throwable: Throwable) {
            setFallbackInboxMenu(false)
            showErrorMessage(Pair(throwable, ::onSuccessGetMenuAndCounter.name))
        }
    }

    private fun setFallbackInboxMenu(shouldShowLocalLoad: Boolean) {
        viewModelScope.launch {
            try {
                val fallbackResult = inboxMenuMapper.generateFallbackMenu(shouldShowLocalLoad)
                getInboxMenuAndWidgetMetaUseCase.updateCache(fallbackResult) // Update the cache to fallback
            } catch (throwable: Throwable) {
                // If the fallback error, nothing we can do
                Timber.d(throwable)
                showErrorMessage(Pair(throwable, ::setFallbackInboxMenu.name))
            }
        }
    }

    /**
     * Actions
     */

    private fun loadInboxMenuAndWidgetMeta() {
        viewModelScope.launch {
            try {
                // Fetch inbox menu & widget from remote
                getInboxMenuAndWidgetMetaUseCase.fetchInboxMenuAndWidgetMeta()
                refreshCounter()
            } catch (throwable: Throwable) {
                setFallbackInboxMenu(true)
                showErrorMessage(Pair(throwable, ::loadInboxMenuAndWidgetMeta.name))
            }
        }
    }

    private fun refreshCounter() {
        viewModelScope.launch {
            try {
                getAllCounterUseCase.refreshCounter(userSession.shopId)
            } catch (throwable: Throwable) {
                showErrorMessage(Pair(throwable, ::refreshCounter.name))
            }
        }
    }

    private fun showErrorMessage(error: Pair<Throwable, String>) {
        viewModelScope.launch {
            _errorUiState.emit(UniversalInboxErrorUiState(error))
        }
    }

    private fun setLoadingInboxMenu() {
        _inboxMenuUiState.update {
            it.copy(
                isLoading = true,
                shouldTrackImpression = false
            )
        }
    }

    private fun setErrorWidgetMeta() {
        _inboxMenuUiState.update {
            it.copy(
                widgetMeta = it.widgetMeta.copy(
                    widgetError = UniversalInboxWidgetMetaErrorUiModel(
                        isError = true,
                        isLocalLoadLoading = false
                    )
                ),
                isLoading = false,
                shouldTrackImpression = false
            )
        }
    }

    private fun resetDriverChannelFlow() {
        driverJob?.cancel()
        observeDriverChannelFlow()
    }

    private fun navigateWithIntent(intent: Intent, requestType: UniversalInboxRequestType) {
        viewModelScope.launch {
            _inboxNavigationState.emit(
                UniversalInboxNavigationUiState(
                    intent = intent,
                    requestType = requestType
                )
            )
        }
    }

    private fun navigateToPage(applink: String, requestType: UniversalInboxRequestType) {
        viewModelScope.launch {
            _inboxNavigationState.emit(
                UniversalInboxNavigationUiState(
                    applink = applink,
                    requestType = requestType
                )
            )
        }
    }

    private fun loadProductRecommendation(page: Int) {
        viewModelScope.launch {
            try {
                getRecommendationUseCase.fetchProductRecommendation(getRecommendationParam(page)).collectLatest {
                    when (it) {
                        is Result.Success -> {
                            handleSuccessGetProductRecommendation(it.data)
                        }
                        is Result.Error -> {
                            setLoadingRecommendation(false)
                            showErrorMessage(Pair(it.throwable, ::loadProductRecommendation.name))
                        }
                        Result.Loading -> {
                            setLoadingRecommendation(true)
                        }
                    }
                }
            } catch (throwable: Throwable) {
                Timber.d(throwable)
            }
        }
    }

    private fun handleSuccessGetProductRecommendation(recommendationWidget: RecommendationWidget) {
        val productRecommendation = recommendationWidget.recommendationItemList.map { item ->
            UniversalInboxRecommendationUiModel(item)
        }
        _productRecommendationState.update { uiState ->
            uiState.copy(
                title = recommendationWidget.title,
                productRecommendation = productRecommendation,
                isLoading = false
            )
        }
    }

    private fun removeAllProductRecommendation(shouldShowLoading: Boolean) {
        _productRecommendationState.update {
            it.copy(
                title = "",
                productRecommendation = listOf(),
                isLoading = shouldShowLoading
            )
        }
    }

    private fun setLoadingRecommendation(isLoading: Boolean) {
        _productRecommendationState.update {
            it.copy(isLoading = isLoading)
        }
    }

    private fun getRecommendationParam(
        page: Int
    ): GetRecommendationRequestParam {
        return GetRecommendationRequestParam(
            pageNumber = page,
            xSource = DEFAULT_VALUE_X_SOURCE,
            pageName = PAGE_NAME,
            productIds = emptyList(),
            xDevice = DEFAULT_VALUE_X_DEVICE
        )
    }

    fun addWishlistV2(
        model: RecommendationItem,
        actionListener: WishlistV2ActionListener
    ) {
        viewModelScope.launch {
            withContext(dispatcher.main) {
                try {
                    val productId = model.productId.toString()
                    addWishListV2UseCase.setParams(productId, userSession.userId)
                    val result = withContext(dispatcher.io) {
                        addWishListV2UseCase.executeOnBackground()
                    }
                    if (result is Success) {
                        actionListener.onSuccessAddWishlist(result.data, productId)
                    } else {
                        actionListener.onErrorAddWishList(
                            (result as Fail).throwable,
                            productId
                        )
                    }
                } catch (throwable: Throwable) {
                    actionListener.onErrorAddWishList(throwable, model.productId.toString())
                    showErrorMessage(Pair(throwable, ::addWishlistV2.name))
                }
            }
        }
    }

    fun removeWishlistV2(
        model: RecommendationItem,
        actionListener: WishlistV2ActionListener
    ) {
        viewModelScope.launch {
            withContext(dispatcher.main) {
                try {
                    deleteWishlistV2UseCase.setParams(
                        model.productId.toString(),
                        userSession.userId
                    )
                    val result = withContext(dispatcher.io) {
                        deleteWishlistV2UseCase.executeOnBackground()
                    }
                    if (result is Success) {
                        actionListener.onSuccessRemoveWishlist(
                            result.data,
                            model.productId.toString()
                        )
                    } else {
                        actionListener.onErrorRemoveWishlist(
                            (result as Fail).throwable,
                            model.productId.toString()
                        )
                    }
                } catch (throwable: Throwable) {
                    actionListener.onErrorRemoveWishlist(throwable, model.productId.toString())
                    showErrorMessage(Pair(throwable, ::removeWishlistV2.name))
                }
            }
        }
    }

    private fun resetUserScrollState() {
        _autoScrollUiState.update {
            it.copy(shouldScroll = false)
        }
    }

    private fun checkAutoScrollEligibility(): Boolean {
        return abTestPlatform.getString(
            INBOX_ADS_REFRESH_KEY,
            ""
        ) == INBOX_ADS_REFRESH_KEY
    }

    private fun autoScrollRecommendation() {
        viewModelScope.launch {
            if (checkAutoScrollEligibility()) {
                _autoScrollUiState.update {
                    it.copy(shouldScroll = true)
                }
            } else {
                // Reset, so it won't trigger anything
                resetUserScrollState()
            }
        }
    }
}
