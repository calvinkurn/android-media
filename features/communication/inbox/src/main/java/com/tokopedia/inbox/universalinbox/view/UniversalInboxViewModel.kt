package com.tokopedia.inbox.universalinbox.view

import android.content.Intent
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
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.PAGE_NAME
import com.tokopedia.inbox.universalinbox.view.uiState.UniversalInboxErrorUiState
import com.tokopedia.inbox.universalinbox.view.uiState.UniversalInboxMenuUiState
import com.tokopedia.inbox.universalinbox.view.uiState.UniversalInboxNavigationUiState
import com.tokopedia.inbox.universalinbox.view.uiState.UniversalInboxProductRecommendationUiState
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
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
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main), DefaultLifecycleObserver {

    private val _actionFlow =
        MutableSharedFlow<UniversalInboxAction>(extraBufferCapacity = 16)

    private val _inboxMenuUiState = MutableStateFlow(UniversalInboxMenuUiState())
    val inboxMenuUiState: StateFlow<UniversalInboxMenuUiState>
        get() = _inboxMenuUiState

    private val _inboxNavigationState = MutableStateFlow(UniversalInboxNavigationUiState())
    val inboxNavigationUiState: StateFlow<UniversalInboxNavigationUiState>
        get() = _inboxNavigationState

    private val _productRecommendationState = MutableStateFlow(
        UniversalInboxProductRecommendationUiState()
    )
    val productRecommendationUiState: StateFlow<UniversalInboxProductRecommendationUiState>
        get() = _productRecommendationState

    private val _errorUiState = MutableSharedFlow<UniversalInboxErrorUiState>(
        extraBufferCapacity = 16
    )
    val errorUiState: SharedFlow<UniversalInboxErrorUiState>
        get() = _errorUiState

    private var page = 1

    init {
        _actionFlow.process().launchIn(viewModelScope)
        observeInboxMenuAndWidgetMetaFlow()
        observeProductRecommendationFlow()
    }

    fun processAction(action: UniversalInboxAction) = _actionFlow.tryEmit(action)

    /**
     * Flow Observe
     */

    private fun Flow<UniversalInboxAction>.process() = onEach {
        when (it) {
            // Navigation process
            is UniversalInboxAction.NavigateWithIntent -> {
                navigateWithIntent(it.intent)
            }
            is UniversalInboxAction.NavigateToPage -> {
                navigateToPage(it.applink)
            }
            is UniversalInboxAction.ResetNavigation -> {
                resetNavigation()
            }

            // General process
            is UniversalInboxAction.ShowErrorMessage -> {
                showErrorMessage(it.error)
            }
            is UniversalInboxAction.RefreshPage -> {
                removeAllProductRecommendation()
                loadInboxMenuAndWidgetMeta()
            }

            // Recommendation process
            is UniversalInboxAction.RefreshRecommendation -> {
                removeAllProductRecommendation()
                page = 1 // reset page
                loadProductRecommendation() // Load first page
            }
            is UniversalInboxAction.LoadNextPage -> {
                loadProductRecommendation()
            }
        }
    }

    private fun observeInboxMenuAndWidgetMetaFlow() {
        viewModelScope.launch {
            combine(
                getInboxMenuAndWidgetMetaUseCase.observe(),
                getAllCounterUseCase.observe(),
                getDriverChatCounterUseCase.observe()
            ) { menu, counter, driverChannel ->
                if (menu != null) {
                    combineMenuAndCounter(menu, counter, driverChannel)
                } else {
                    // If cache is null, it means new user or error
                    // create the default menu first
                    setFallbackInboxMenu()
                    null // Set null to get filtered out
                }
            }.filterNotNull()
                .catch {
                    Timber.d(it)
                    Result.Error(it)
                }
                .collectLatest {
                    handleGetMenuAndCounterResult(it)
                }
        }
    }

    private fun combineMenuAndCounter(
        menu: UniversalInboxWrapperResponse,
        counter: Result<UniversalInboxAllCounterResponse>,
        driverChannel: Result<List<ConversationsChannel>>
    ): Result<
        Pair<UniversalInboxWrapperResponse,
            Pair<UniversalInboxAllCounterResponse, List<ConversationsChannel>>>> {
        return when {
            (counter is Result.Success && driverChannel is Result.Success) -> {
                Result.Success(Pair(menu, Pair(counter.data, driverChannel.data)))
            }
            (counter is Result.Success && driverChannel is Result.Error) -> {
                // default driver 0
                Result.Success(Pair(menu, Pair(counter.data, listOf())))
            }
            else -> {
                Result.Success(
                    // default all counter 0
                    Pair(menu, Pair(UniversalInboxAllCounterResponse(), listOf()))
                )
            }
        }
    }

    private fun handleGetMenuAndCounterResult(
        result: Result<Pair<UniversalInboxWrapperResponse,
                Pair<UniversalInboxAllCounterResponse, List<ConversationsChannel>>>>
    ) {
        when (result) {
            is Result.Success -> {
                onSuccessGetMenuAndCounter(
                    result.data.first,
                    result.data.second.first,
                    result.data.second.second
                )
            }
            is Result.Error -> {
                setLoadingInboxMenu(false)
                setErrorWidgetMeta()
                showErrorMessage(Pair(result.throwable, ::handleGetMenuAndCounterResult.name))
            }
            is Result.Loading -> {
                setLoadingInboxMenu(true)
            }
        }
    }

    private fun onSuccessGetMenuAndCounter(
        inboxResponse: UniversalInboxWrapperResponse,
        counterResponse: UniversalInboxAllCounterResponse,
        driverResponse: List<ConversationsChannel>
    ) {
        try {
            setLoadingInboxMenu(false)
            val driverCounter = onSuccessGetDriverChannelList(driverResponse)
            val widgetMeta = widgetMetaMapper.mapWidgetMetaToUiModel(
                widgetMetaResponse = inboxResponse.chatInboxMenu.widgetMenu,
                counterResponse = counterResponse,
                driverCounter = Success(driverCounter)
            )
            val menuList = inboxMenuMapper.mapToInboxMenu(
                userSession = userSession,
                inboxMenuResponse = inboxResponse.chatInboxMenu.inboxMenu,
                counterResponse = counterResponse
            )
            val miscList = inboxMiscMapper.generateMiscMenu()
            _inboxMenuUiState.update { uiState ->
                uiState.copy(
                    widgetMeta = widgetMeta,
                    menuList = menuList,
                    miscList = miscList,
                    notificationCounter = counterResponse.notifCenterUnread.notifUnread
                )
            }
        } catch (throwable: Throwable) {
            setFallbackInboxMenu()
            showErrorMessage(Pair(throwable, ::onSuccessGetMenuAndCounter.name))
        }
    }

    private fun onSuccessGetDriverChannelList(
        channelList: List<ConversationsChannel>
    ): Pair<Int, Int> {
        return try {
            var activeChannel = 0
            var unreadTotal = 0
            channelList.forEach { channel ->
                if (channel.expiresAt > System.currentTimeMillis()) {
                    activeChannel++
                    unreadTotal += channel.unreadCount
                }
            }
            if (unreadTotal >= 0) {
                Pair(activeChannel, unreadTotal)
            } else {
                throw IllegalArgumentException()
            }
        } catch (throwable: Throwable) {
            showErrorMessage(Pair(throwable, ::onSuccessGetDriverChannelList.name))
            Pair(0, 0) // Default
        }
    }

    private fun setFallbackInboxMenu() {
        viewModelScope.launch {
            try {
                val fallbackResult = inboxMenuMapper.generateFallbackMenu()
                getInboxMenuAndWidgetMetaUseCase.updateCache(fallbackResult) // Update the cache to fallback
            } catch (throwable: Throwable) {
                // If the fallback error, nothing we can do
                Timber.d(throwable)
                showErrorMessage(Pair(throwable, ::setFallbackInboxMenu.name))
            }
        }
    }

    private fun observeProductRecommendationFlow() {
        viewModelScope.launch {
            getRecommendationUseCase.observe()
                .catch {
                    Timber.d(it)
                    Result.Error(it)
                }
                .collectLatest {
                    handleResultProductRecommendation(it)
                }
        }
    }

    private fun handleResultProductRecommendation(
        result: Result<RecommendationWidget>
    ) {
        turnOffRemoveProductRecommendation()
        when (result) {
            is Result.Success -> {
                onSuccessGetProductRecommendation(result.data)
                page++
            }
            is Result.Error -> {
                setLoadingRecommendation(false)
                showErrorMessage(Pair(result.throwable, ::handleResultProductRecommendation.name))
            }
            is Result.Loading -> {
                setLoadingRecommendation(true)
            }
        }
    }

    private fun onSuccessGetProductRecommendation(
        response: RecommendationWidget
    ) {
        try {
            setLoadingRecommendation(false)
            updateProductRecommendation(response)
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            showErrorMessage(Pair(throwable, ::onSuccessGetProductRecommendation.name))
        }
    }

    /**
     * Actions
     */

    private fun loadInboxMenuAndWidgetMeta() {
        viewModelScope.launch {
            try {
                // Show loading first
                setLoadingInboxMenu(true)
                // Refresh counter
                getAllCounterUseCase.refreshCounter(userSession.shopId)
                // Get driver channel list
                getDriverChatCounterUseCase.getAllChannels()
                // Fetch inbox menu & widget from remote
                getInboxMenuAndWidgetMetaUseCase.fetchInboxMenuAndWidgetMeta(Unit)
            } catch (throwable: Throwable) {
                setFallbackInboxMenu()
                showErrorMessage(Pair(throwable, ::loadInboxMenuAndWidgetMeta.name))
            }
        }
    }

    private fun showErrorMessage(error: Pair<Throwable, String>) {
        viewModelScope.launch {
            _errorUiState.emit(UniversalInboxErrorUiState(error))
        }
    }

    private fun setLoadingInboxMenu(isLoading: Boolean) {
        viewModelScope.launch {
            _inboxMenuUiState.update {
                it.copy(
                    isLoading = isLoading
                )
            }
        }
    }

    private fun setErrorWidgetMeta() {
        viewModelScope.launch {
            _inboxMenuUiState.update {
                it.copy(
                    widgetMeta = it.widgetMeta.copy(
                        isError = true
                    )
                )
            }
        }
    }

    private fun navigateWithIntent(intent: Intent) {
        viewModelScope.launch {
            _inboxNavigationState.update {
                it.copy(intent = intent)
            }
        }
    }

    private fun navigateToPage(applink: String) {
        viewModelScope.launch {
            _inboxNavigationState.update {
                it.copy(applink = applink)
            }
        }
    }

    private fun resetNavigation() {
        navigateToPage("")
    }

    private fun loadProductRecommendation() {
        viewModelScope.launch {
            setLoadingRecommendation(true) // Show loading
            getRecommendationUseCase.fetchProductRecommendation(getRecommendationParam(page))
        }
    }

    private fun updateProductRecommendation(response: RecommendationWidget) {
        viewModelScope.launch {
            _productRecommendationState.update {
                it.copy(
                    title = response.title,
                    productList = response.recommendationItemList
                )
            }
        }
    }

    private fun removeAllProductRecommendation() {
        viewModelScope.launch {
            getRecommendationUseCase.reset()
            _productRecommendationState.update {
                it.copy(
                    title = "",
                    productList = listOf(),
                    isLoading = false,
                    shouldRemoveAllProduct = true
                )
            }
        }
    }

    private fun turnOffRemoveProductRecommendation() {
        viewModelScope.launch {
            _productRecommendationState.update {
                it.copy(
                    shouldRemoveAllProduct = false
                )
            }
        }
    }

    private fun setLoadingRecommendation(isLoading: Boolean) {
        viewModelScope.launch {
            _productRecommendationState.update {
                it.copy(isLoading = isLoading)
            }
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

    fun getRecommendationPage(): Int {
        return page
    }
}
