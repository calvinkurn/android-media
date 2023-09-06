package com.tokopedia.inbox.universalinbox.view

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import com.tokopedia.inbox.universalinbox.util.Result
import com.tokopedia.inbox.universalinbox.view.uiState.UniversalInboxMenuUiState
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val getRecommendationUseCase: GetRecommendationUseCase,
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

    private val _inboxMenu = MutableStateFlow<List<Any>?>(emptyList())
    val inboxMenu: StateFlow<List<Any>?>
        get() = _inboxMenu

    private val _allCounter = MutableLiveData<Result<UniversalInboxAllCounterResponse>>()
    val allCounter: LiveData<Result<UniversalInboxAllCounterResponse>>
        get() = _allCounter

//    private val _firstPageRecommendation = MutableLiveData<Result<RecommendationWidget>>()
//    val firstPageRecommendation: LiveData<Result<RecommendationWidget>>
//        get() = _firstPageRecommendation
//
//    private val _morePageRecommendation = MutableLiveData<Result<List<RecommendationItem>>>()
//    val morePageRecommendation: LiveData<Result<List<RecommendationItem>>>
//        get() = _morePageRecommendation
//
//    private val _driverChatCounter: MediatorLiveData<Result<Pair<Int, Int>>?> = MediatorLiveData()
//    val driverChatCounter: LiveData<Result<Pair<Int, Int>>?>
//        get() = _driverChatCounter.distinctUntilChanged()
//
//    var driverChatWidgetData: Pair<Int, UniversalInboxWidgetDataResponse>? = null
//    var driverChatData: Result<Pair<Int, Int>>? = null

    // Error live data for all purpose
//    private val _error = MutableLiveData<Pair<Throwable, String>>()
//    val error: LiveData<Pair<Throwable, String>>
//        get() = _error

    init {
        _actionFlow.process().launchIn(viewModelScope)
        observeInboxMenuAndWidgetMeta()
    }

    fun processAction(action: UniversalInboxAction) = _actionFlow.tryEmit(action)

    /**
     * Flow Observe
     */

    private fun Flow<UniversalInboxAction>.process() = onEach {
        when (it) {
            is UniversalInboxAction.NavigateToPage -> {}
            is UniversalInboxAction.ShowErrorMessage -> {
                showErrorMessage(it.error)
            }
            is UniversalInboxAction.RefreshPage -> {
                loadInboxMenuAndWidgetMeta()
            }
        }
    }

    private fun observeInboxMenuAndWidgetMeta() {
        viewModelScope.launch {
            getInboxMenuAndWidgetMetaUseCase.observe()
                .filter { it != null }
                .combine(getAllCounterUseCase.observe()) { menu, counter ->
                    _allCounter.value = counter
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
                showErrorMessage(Pair(result.throwable, ::handleGetMenuAndCounterResult.name))
            }
            is Result.Loading -> {
                showLoading()
            }
        }
    }

    private fun onSuccessGetMenuAndCounter(
        inboxResponse: UniversalInboxWrapperResponse,
        counterResponse: UniversalInboxAllCounterResponse
    ) {
        try {
            val menuList = inboxMenuMapper.mapToInboxMenu(
                userSession = userSession,
                inboxMenuResponse = inboxResponse.chatInboxMenu.inboxMenu,
                counterResponse = counterResponse
            )
            val miscList = inboxMiscMapper.generateMiscMenu()
            _inboxMenuUiState.update { uiState ->
                uiState.copy(
                    isLoading = false,
                    menuList = menuList,
                    miscList = miscList
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

    /**
     * Actions
     */

    private fun loadInboxMenuAndWidgetMeta() {
        viewModelScope.launch {
            try {
                // Show loading first
                showLoading()
                // Fetch inbox menu & widget from remote
                getInboxMenuAndWidgetMetaUseCase.fetchInboxMenuAndWidgetMeta(Unit)
                // Refresh counter
                getAllCounterUseCase.refreshCounter(userSession.shopId)
            } catch (throwable: Throwable) {
                setFallbackInboxMenu()
                showErrorMessage(Pair(throwable, ::loadInboxMenuAndWidgetMeta.name))
            }
        }
    }

    private fun showErrorMessage(error: Pair<Throwable, String>) {
        viewModelScope.launch {
            _inboxMenuUiState.update {
                it.copy(
                    isLoading = false,
                    error = error
                )
            }
        }
    }

    private fun showLoading() {
        viewModelScope.launch {
            _inboxMenuUiState.update {
                it.copy(isLoading = true)
            }
        }
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

//    private suspend fun getRecommendationList(page: Int): RecommendationWidget {
//        val recommendationParams = GetRecommendationRequestParam(
//            pageNumber = page,
//            xSource = DEFAULT_VALUE_X_SOURCE,
//            pageName = PAGE_NAME,
//            productIds = emptyList(),
//            xDevice = DEFAULT_VALUE_X_DEVICE
//        )
//        return getRecommendationUseCase.getData(recommendationParams).first()
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
