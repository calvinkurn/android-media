package com.tokopedia.inbox.universalinbox.view

import android.content.Intent
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxAllCounterResponse
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWrapperResponse
import com.tokopedia.inbox.universalinbox.domain.mapper.UniversalInboxMenuMapper
import com.tokopedia.inbox.universalinbox.domain.mapper.UniversalInboxMiscMapper
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
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
//
//    private val _driverChatCounter: MediatorLiveData<Result<Pair<Int, Int>>?> = MediatorLiveData()
//    val driverChatCounter: LiveData<Result<Pair<Int, Int>>?>
//        get() = _driverChatCounter.distinctUntilChanged()
//
//    var driverChatWidgetData: Pair<Int, UniversalInboxWidgetDataResponse>? = null
//    var driverChatData: Result<Pair<Int, Int>>? = null

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
                loadInboxMenuAndWidgetMeta()
                toggleLoadProductRecommendation(true) // flag for load recom
                toggleRemoveAllProductRecommendation(true)
            }

            // Recommendation process
            is UniversalInboxAction.RefreshRecommendation -> {
                toggleLoadProductRecommendation(false) // flag for already load recom
                toggleRemoveAllProductRecommendation(true)
                page = 1 // reset page
                loadProductRecommendation() // Load first page
            }
            is UniversalInboxAction.LoadNextPage -> {
                val lastPage = _productRecommendationState
                loadProductRecommendation()
            }
        }
    }

    private fun observeInboxMenuAndWidgetMetaFlow() {
        viewModelScope.launch {
            getInboxMenuAndWidgetMetaUseCase.observe()
                .filter {
                    val isDataNull = it != null
                    if (isDataNull) {
                        // If cache is null, it means new user, create the default menu first
                        setFallbackInboxMenu()
                    }
                    isDataNull
                }
                .combine(getAllCounterUseCase.observe()) { menu, counter ->
                    combineMenuAndCounter(menu!!, counter) // Safe !! because of filter
                }
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
        counter: Result<UniversalInboxAllCounterResponse>
    ): Result<Pair<UniversalInboxWrapperResponse, UniversalInboxAllCounterResponse>> {
        return when (counter) {
            is Result.Success -> {
                Result.Success(Pair(menu, counter.data))
            }
            else -> {
                Result.Success(
                    Pair(menu, UniversalInboxAllCounterResponse()) // default counter 0
                )
            }
        }
    }

    private fun handleGetMenuAndCounterResult(
        result: Result<Pair<UniversalInboxWrapperResponse, UniversalInboxAllCounterResponse>>
    ) {
        when (result) {
            is Result.Success -> {
                onSuccessGetMenuAndCounter(
                    result.data.first,
                    result.data.second
                )
            }
            is Result.Error -> {
                setLoadingInboxMenu(false)
                showErrorMessage(Pair(result.throwable, ::handleGetMenuAndCounterResult.name))
            }
            is Result.Loading -> {
                setLoadingInboxMenu(true)
            }
        }
    }

    private fun onSuccessGetMenuAndCounter(
        inboxResponse: UniversalInboxWrapperResponse,
        counterResponse: UniversalInboxAllCounterResponse
    ) {
        try {
            setLoadingInboxMenu(false)
            val menuList = inboxMenuMapper.mapToInboxMenu(
                userSession = userSession,
                inboxMenuResponse = inboxResponse.chatInboxMenu.inboxMenu,
                counterResponse = counterResponse
            )
            val miscList = inboxMiscMapper.generateMiscMenu()
            _inboxMenuUiState.update { uiState ->
                uiState.copy(
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
        toggleRemoveAllProductRecommendation(false)
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
                // Fetch inbox menu & widget from remote
                getInboxMenuAndWidgetMetaUseCase.fetchInboxMenuAndWidgetMeta(Unit)
            } catch (throwable: Throwable) {
                setFallbackInboxMenu()
                showErrorMessage(Pair(throwable, ::loadInboxMenuAndWidgetMeta.name))
            }
        }
    }

    private fun toggleLoadProductRecommendation(shouldLoad: Boolean) {
        viewModelScope.launch {
            _inboxMenuUiState.update {
                it.copy(shouldLoadRecommendation = shouldLoad)
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

    private fun toggleRemoveAllProductRecommendation(shouldRemove: Boolean) {
        viewModelScope.launch {
            _productRecommendationState.update {
                it.copy(
                    shouldRemoveAllProduct = shouldRemove
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

//    fun loadWidgetMetaAndCounter() {
//        viewModelScope.launch {
//            try {
//                val widgetMetaResponse = getWidgetMetaAsync().await()
//                driverChatWidgetData = null // reset driver widget
//                // Get driver data only when response has driver widget
//                if (widgetMetaResponse?.widgetMenu != null) {
//                    widgetMetaResponse.widgetMenu.forEachIndexed { index, response ->
//                        if (response.type == GOJEK_TYPE) {
//                            driverChatWidgetData = Pair(index, response)
//                        }
//                    }
//                }
//                if (driverChatWidgetData != null) {
//                    setAllDriverChannels()
//                }
//                driverChatData = driverChatCounter.value // Set driver chat data
//                val allCounterResponse = getAllCounterAsync().await()
//                val result = inboxMenuMapper.mapWidgetMetaToUiModel(
//                    widgetMetaResponse,
//                    allCounterResponse,
//                    driverChatData
//                )
//                _widget.value = Pair(result, allCounterResponse)
//            } catch (throwable: Throwable) {
//                _error.value = Pair(throwable, ::loadWidgetMetaAndCounter.name)
//                _widget.value = Pair(UniversalInboxWidgetMetaUiModel(isError = true), null)
//            }
//        }
//    }

//    fun setAllDriverChannels() {
//        try {
//            val allChannelsLiveData = getDriverChatCounterUseCase.getAllChannels()
//            allChannelsLiveData?.run {
//                val liveData = switchMap {
//                    getDriverUnreadCount(it)
//                }
//                _driverChatCounter.addSource(liveData) {
//                    _driverChatCounter.value = it
//                }
//            }
//        } catch (throwable: Throwable) {
//            _error.value = Pair(throwable, ::setAllDriverChannels.name)
//            _driverChatCounter.value = Fail(throwable)
//        }
//    }

//    @VisibleForTesting
//    fun getDriverUnreadCount(
//        channelList: List<ConversationsChannel>
//    ): LiveData<Result<Pair<Int, Int>>> {
//        val unreadDriverChatCount = MutableLiveData<Result<Pair<Int, Int>>>()
//        viewModelScope.launch {
//            try {
//                var activeChannel = Int.ZERO
//                var unreadTotal = Int.ZERO
//                channelList.forEach { channel ->
//                    if (channel.expiresAt > System.currentTimeMillis()) {
//                        activeChannel++
//                        unreadTotal += channel.unreadCount
//                    }
//                }
//                if (unreadTotal >= Int.ZERO) {
//                    unreadDriverChatCount.value = Success(
//                        Pair(activeChannel, unreadTotal)
//                    )
//                } else {
//                    throw IllegalArgumentException()
//                }
//            } catch (throwable: Throwable) {
//                _error.value = Pair(throwable, ::getDriverUnreadCount.name)
//                unreadDriverChatCount.value = Fail(throwable)
//            }
//        }
//        return unreadDriverChatCount
//    }

//    private suspend fun getWidgetMetaAsync(): Deferred<UniversalInboxMenuAndWidgetMetaResponse?> {
//        return viewModelScope.async {
//            try {
//                getWidgetMetaUseCase(Unit).chatInboxMenu
//            } catch (throwable: Throwable) {
//                _error.value = Pair(throwable, ::getWidgetMetaAsync.name)
//                null
//            }
//        }
//    }

//    fun loadFirstPageRecommendation() {
//        viewModelScope.launch {
//            withContext(dispatcher.io) {
//                try {
//                    val recommendationWidget = getRecommendationList(Int.ONE)
//                    _firstPageRecommendation.postValue(Success(recommendationWidget))
//                } catch (throwable: Throwable) {
//                    _firstPageRecommendation.postValue(Fail(throwable))
//                    _error.postValue(Pair(throwable, ::loadFirstPageRecommendation.name))
//                }
//            }
//        }
//    }
//
//    fun loadMoreRecommendation(page: Int) {
//        viewModelScope.launch {
//            withContext(dispatcher.io) {
//                try {
//                    val recommendationWidget = getRecommendationList(page)
//                    _morePageRecommendation.postValue(Success(recommendationWidget.recommendationItemList))
//                } catch (throwable: Throwable) {
//                    _morePageRecommendation.postValue(Fail(throwable))
//                    _error.postValue(Pair(throwable, ::loadMoreRecommendation.name))
//                }
//            }
//        }
//    }

//    fun addWishlistV2(
//        model: RecommendationItem,
//        actionListener: WishlistV2ActionListener
//    ) {
//        viewModelScope.launch {
//            withContext(dispatcher.main) {
//                try {
//                    val productId = model.productId.toString()
//                    addWishListV2UseCase.setParams(productId, userSession.userId)
//                    val result = withContext(dispatcher.io) {
//                        addWishListV2UseCase.executeOnBackground()
//                    }
//                    if (result is Success) {
//                        actionListener.onSuccessAddWishlist(result.data, productId)
//                    } else {
//                        actionListener.onErrorAddWishList(
//                            (result as Fail).throwable,
//                            productId
//                        )
//                    }
//                } catch (throwable: Throwable) {
//                    actionListener.onErrorAddWishList(throwable, model.productId.toString())
//                    _error.value = Pair(throwable, ::addWishlistV2.name)
//                }
//            }
//        }
//    }

//    fun removeWishlistV2(
//        model: RecommendationItem,
//        actionListener: WishlistV2ActionListener
//    ) {
//        viewModelScope.launch {
//            withContext(dispatcher.main) {
//                try {
//                    deleteWishlistV2UseCase.setParams(
//                        model.productId.toString(),
//                        userSession.userId
//                    )
//                    val result = withContext(dispatcher.io) {
//                        deleteWishlistV2UseCase.executeOnBackground()
//                    }
//                    if (result is Success) {
//                        actionListener.onSuccessRemoveWishlist(
//                            result.data,
//                            model.productId.toString()
//                        )
//                    } else {
//                        actionListener.onErrorRemoveWishlist(
//                            (result as Fail).throwable,
//                            model.productId.toString()
//                        )
//                    }
//                } catch (throwable: Throwable) {
//                    actionListener.onErrorRemoveWishlist(throwable, model.productId.toString())
//                    _error.value = Pair(throwable, ::removeWishlistV2.name)
//                }
//            }
//        }
//    }
}
