package com.tokopedia.inbox.universalinbox.view

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.gojek.conversations.channel.ConversationsChannel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxAllCounterResponse
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWidgetDataResponse
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWidgetMetaResponse
import com.tokopedia.inbox.universalinbox.domain.UniversalInboxGetAllCounterUseCase
import com.tokopedia.inbox.universalinbox.domain.UniversalInboxGetAllDriverChannelsUseCase
import com.tokopedia.inbox.universalinbox.domain.UniversalInboxGetWidgetMetaUseCase
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.GOJEK_TYPE
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.PAGE_NAME
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetMetaUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.recommendation_widget_common.DEFAULT_VALUE_X_DEVICE
import com.tokopedia.recommendation_widget_common.DEFAULT_VALUE_X_SOURCE
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UniversalInboxViewModel @Inject constructor(
    private val getAllCounterUseCase: UniversalInboxGetAllCounterUseCase,
    private val getWidgetMetaUseCase: UniversalInboxGetWidgetMetaUseCase,
    private val getRecommendationUseCase: GetRecommendationUseCase,
    private val addWishListV2UseCase: AddToWishlistV2UseCase,
    private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
    private val getDriverChatCounterUseCase: UniversalInboxGetAllDriverChannelsUseCase,
    private val inboxMenuMapper: UniversalInboxMenuMapper,
    private val userSession: UserSessionInterface,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main), DefaultLifecycleObserver {

    private val _inboxMenu = MutableLiveData<List<Any>>()
    val inboxMenu: LiveData<List<Any>>
        get() = _inboxMenu

    private val _widget = MutableLiveData<Pair<UniversalInboxWidgetMetaUiModel, UniversalInboxAllCounterResponse?>>()
    val widget: LiveData<Pair<UniversalInboxWidgetMetaUiModel, UniversalInboxAllCounterResponse?>>
        get() = _widget

    private val _allCounter = MutableLiveData<Result<UniversalInboxAllCounterResponse>>()
    val allCounter: LiveData<Result<UniversalInboxAllCounterResponse>>
        get() = _allCounter

    private val _firstPageRecommendation = MutableLiveData<Result<RecommendationWidget>>()
    val firstPageRecommendation: LiveData<Result<RecommendationWidget>>
        get() = _firstPageRecommendation

    private val _morePageRecommendation = MutableLiveData<Result<List<RecommendationItem>>>()
    val morePageRecommendation: LiveData<Result<List<RecommendationItem>>>
        get() = _morePageRecommendation

    private val _driverChatCounter: MediatorLiveData<Result<Pair<Int, Int>>?> = MediatorLiveData()
    val driverChatCounter: LiveData<Result<Pair<Int, Int>>?>
        get() = _driverChatCounter.distinctUntilChanged()

    var driverChatWidgetData: Pair<Int, UniversalInboxWidgetDataResponse>? = null
    var driverChatData: Result<Pair<Int, Int>>? = null

    // Error live data for all purpose
    private val _error = MutableLiveData<Pair<Throwable, String>>()
    val error: LiveData<Pair<Throwable, String>>
        get() = _error

    fun generateStaticMenu() {
        val staticMenuList = inboxMenuMapper.getStaticMenu(userSession)
        _inboxMenu.postValue(staticMenuList)
    }

    fun loadWidgetMetaAndCounter() {
        viewModelScope.launch {
            try {
                val widgetMetaResponse = getWidgetMetaAsync().await()
                driverChatWidgetData = null // reset driver widget
                // Get driver data only when response has driver widget
                if (widgetMetaResponse?.metaData != null) {
                    widgetMetaResponse.metaData.forEachIndexed { index, response ->
                        if (response.type == GOJEK_TYPE) {
                            driverChatWidgetData = Pair(index, response)
                        }
                    }
                }
                if (driverChatWidgetData != null) {
                    setAllDriverChannels()
                }
                driverChatData = driverChatCounter.value // Set driver chat data
                val allCounterResponse = getAllCounterAsync().await()
                val result = inboxMenuMapper.mapWidgetMetaToUiModel(
                    widgetMetaResponse,
                    allCounterResponse,
                    driverChatData
                )
                _widget.value = Pair(result, allCounterResponse)
            } catch (throwable: Throwable) {
                _error.value = Pair(throwable, ::loadWidgetMetaAndCounter.name)
                _widget.value = Pair(UniversalInboxWidgetMetaUiModel(isError = true), null)
            }
        }
    }

    fun setAllDriverChannels() {
        try {
            val allChannelsLiveData = getDriverChatCounterUseCase.getAllChannels()
            allChannelsLiveData?.run {
                val liveData = switchMap {
                    getDriverUnreadCount(it)
                }
                _driverChatCounter.addSource(liveData) {
                    _driverChatCounter.value = it
                }
            }
        } catch (throwable: Throwable) {
            _error.value = Pair(throwable, ::setAllDriverChannels.name)
            _driverChatCounter.value = Fail(throwable)
        }
    }

    @VisibleForTesting
    fun getDriverUnreadCount(
        channelList: List<ConversationsChannel>
    ): LiveData<Result<Pair<Int, Int>>> {
        val unreadDriverChatCount = MutableLiveData<Result<Pair<Int, Int>>>()
        viewModelScope.launch {
            try {
                var activeChannel = Int.ZERO
                var unreadTotal = Int.ZERO
                channelList.forEach { channel ->
                    if (channel.expiresAt > System.currentTimeMillis()) {
                        activeChannel++
                        unreadTotal += channel.unreadCount
                    }
                }
                if (unreadTotal >= Int.ZERO) {
                    unreadDriverChatCount.value = Success(
                        Pair(activeChannel, unreadTotal)
                    )
                } else {
                    throw IllegalArgumentException()
                }
            } catch (throwable: Throwable) {
                _error.value = Pair(throwable, ::getDriverUnreadCount.name)
                unreadDriverChatCount.value = Fail(throwable)
            }
        }
        return unreadDriverChatCount
    }

    private suspend fun getWidgetMetaAsync(): Deferred<UniversalInboxWidgetMetaResponse?> {
        return viewModelScope.async {
            try {
                getWidgetMetaUseCase(Unit).chatInboxWidgetMeta
            } catch (throwable: Throwable) {
                _error.value = Pair(throwable, ::getWidgetMetaAsync.name)
                null
            }
        }
    }

    private suspend fun getAllCounterAsync(): Deferred<UniversalInboxAllCounterResponse?> {
        return viewModelScope.async {
            try {
                val result = getAllCounterUseCase(userSession.shopId)
                _allCounter.value = Success(result)
                result
            } catch (throwable: Throwable) {
                _error.value = Pair(throwable, ::getAllCounterAsync.name)
                _allCounter.value = Fail(throwable)
                null
            }
        }
    }

    fun loadFirstPageRecommendation() {
        viewModelScope.launch {
            withContext(dispatcher.io) {
                try {
                    val recommendationWidget = getRecommendationList(Int.ONE)
                    _firstPageRecommendation.postValue(Success(recommendationWidget))
                } catch (throwable: Throwable) {
                    _firstPageRecommendation.postValue(Fail(throwable))
                    _error.value = Pair(throwable, ::loadFirstPageRecommendation.name)
                }
            }
        }
    }

    fun loadMoreRecommendation(page: Int) {
        viewModelScope.launch {
            withContext(dispatcher.io) {
                try {
                    val recommendationWidget = getRecommendationList(page)
                    _morePageRecommendation.postValue(Success(recommendationWidget.recommendationItemList))
                } catch (throwable: Throwable) {
                    _morePageRecommendation.postValue(Fail(throwable))
                    _error.value = Pair(throwable, ::loadMoreRecommendation.name)
                }
            }
        }
    }

    private suspend fun getRecommendationList(page: Int): RecommendationWidget {
        val recommendationParams = GetRecommendationRequestParam(
            pageNumber = page,
            xSource = DEFAULT_VALUE_X_SOURCE,
            pageName = PAGE_NAME,
            productIds = emptyList(),
            xDevice = DEFAULT_VALUE_X_DEVICE
        )
        return getRecommendationUseCase.getData(recommendationParams).first()
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
                    _error.value = Pair(throwable, ::addWishlistV2.name)
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
                    _error.value = Pair(throwable, ::removeWishlistV2.name)
                }
            }
        }
    }
}
