package com.tokopedia.notifcenter.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.notifcenter.data.entity.Notifications
import com.tokopedia.notifcenter.data.entity.bumpreminder.BumpReminderResponse
import com.tokopedia.notifcenter.data.entity.clearnotif.ClearNotifCounterResponse
import com.tokopedia.notifcenter.data.entity.deletereminder.DeleteReminderResponse
import com.tokopedia.notifcenter.data.entity.filter.NotifcenterFilterResponse
import com.tokopedia.notifcenter.data.entity.notification.NotificationDetailResponseWrapper
import com.tokopedia.notifcenter.data.entity.notification.ProductData
import com.tokopedia.notifcenter.data.entity.orderlist.NotifOrderListResponse
import com.tokopedia.notifcenter.data.model.RecommendationDataModel
import com.tokopedia.notifcenter.data.state.Resource
import com.tokopedia.notifcenter.data.uimodel.NotificationTopAdsBannerUiModel
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.data.uimodel.RecommendationTitleUiModel
import com.tokopedia.notifcenter.data.uimodel.RecommendationUiModel
import com.tokopedia.notifcenter.data.uimodel.affiliate.NotificationAffiliateEducationUiModel
import com.tokopedia.notifcenter.domain.AffiliateEducationArticleUseCase
import com.tokopedia.notifcenter.domain.ClearNotifCounterUseCase
import com.tokopedia.notifcenter.domain.GetNotificationCounterUseCase
import com.tokopedia.notifcenter.domain.MarkNotificationAsReadUseCase
import com.tokopedia.notifcenter.domain.NotifOrderListUseCase
import com.tokopedia.notifcenter.domain.NotifcenterDeleteReminderBumpUseCase
import com.tokopedia.notifcenter.domain.NotifcenterDetailUseCase
import com.tokopedia.notifcenter.domain.NotifcenterFilterV2UseCase
import com.tokopedia.notifcenter.domain.NotifcenterSetReminderBumpUseCase
import com.tokopedia.notifcenter.view.adapter.typefactory.NotificationTypeFactory
import com.tokopedia.notifcenter.view.listener.WishlistListener
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class NotificationViewModel @Inject constructor(
    private val notifcenterDetailUseCase: NotifcenterDetailUseCase,
    private val notifcenterFilterUseCase: NotifcenterFilterV2UseCase,
    private val bumpReminderUseCase: NotifcenterSetReminderBumpUseCase,
    private val deleteReminderUseCase: NotifcenterDeleteReminderBumpUseCase,
    private val clearNotifUseCase: ClearNotifCounterUseCase,
    private val markAsReadUseCase: MarkNotificationAsReadUseCase,
    private val topAdsImageViewUseCase: TopAdsImageViewUseCase,
    private val getRecommendationUseCase: GetRecommendationUseCase,
    private val addWishListV2UseCase: AddToWishlistV2UseCase,
    private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
    private val userSessionInterface: UserSessionInterface,
    private var addToCartUseCase: AddToCartUseCase,
    private var notifOrderListUseCase: NotifOrderListUseCase,
    private var affiliateEducationArticleUseCase: AffiliateEducationArticleUseCase,
    private var getNotificationCounterUseCase: GetNotificationCounterUseCase,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io), WishlistListener {

    var filter: Long = NotifcenterDetailUseCase.FILTER_NONE
        set(value) {
            field = value
            cancelAllUseCase()
        }

    private val _mutateNotificationItems = MutableLiveData<NotificationDetailResponseWrapper>()
    val notificationItems: LiveData<NotificationDetailResponseWrapper>
        get() = _mutateNotificationItems

    private val _topAdsBanner = MutableLiveData<NotificationTopAdsBannerUiModel>()
    val topAdsBanner: LiveData<NotificationTopAdsBannerUiModel>
        get() = _topAdsBanner

    private val _recommendations = MutableLiveData<RecommendationDataModel>()
    val recommendations: LiveData<RecommendationDataModel>
        get() = _recommendations

    private val _filterList = MutableLiveData<Resource<NotifcenterFilterResponse>>()
    val filterList: LiveData<Resource<NotifcenterFilterResponse>>
        get() = _filterList

    private val _clearNotif = MutableLiveData<Resource<ClearNotifCounterResponse>>()
    val clearNotif: LiveData<Resource<ClearNotifCounterResponse>>
        get() = _clearNotif

    private val _bumpReminder = MutableLiveData<Resource<BumpReminderResponse>>()
    val bumpReminder: LiveData<Resource<BumpReminderResponse>>
        get() = _bumpReminder

    private val _deleteReminder = MutableLiveData<Resource<DeleteReminderResponse>>()
    val deleteReminder: LiveData<Resource<DeleteReminderResponse>>
        get() = _deleteReminder

    private val _orderList = MutableLiveData<Resource<NotifOrderListResponse>>()
    val orderList: LiveData<Resource<NotifOrderListResponse>>
        get() = _orderList

    private val _affiliateEducationArticle =
        MutableLiveData<NotificationAffiliateEducationUiModel>()
    val affiliateEducationArticle: LiveData<NotificationAffiliateEducationUiModel>
        get() = _affiliateEducationArticle

    private val _notifications = MutableLiveData<Result<Notifications>>()
    val notifications: LiveData<Result<Notifications>>
        get() = _notifications

    fun hasFilter(): Boolean {
        return filter != NotifcenterDetailUseCase.FILTER_NONE
    }

    fun cancelAllUseCase() {
        coroutineContext.cancelChildren()
    }

    fun loadNotifOrderList(
        @RoleType
        role: Int
    ) {
        viewModelScope.launch {
            try {
                notifOrderListUseCase(role).collect {
                    _orderList.value = it
                }
            } catch (throwable: Throwable) {
                _orderList.value = Resource.error(throwable, null)
            }
        }
    }

    fun loadNotificationFilter(
        @RoleType
        role: Int
    ) {
        viewModelScope.launch {
            try {
                notifcenterFilterUseCase(role).collect {
                    _filterList.postValue(it)
                }
            } catch (throwable: Throwable) {
                _filterList.value = Resource.error(throwable, null)
            }
        }
    }

    fun markNotificationAsRead(
        @RoleType
        role: Int,
        element: NotificationUiModel
    ) {
        viewModelScope.launch {
            try {
                val params = MarkNotificationAsReadUseCase.Param(
                    role = role,
                    notifId = element.notifId
                )
                markAsReadUseCase(params)
            } catch (throwable: Throwable) {
                Timber.d(throwable)
            }
        }
    }

    fun loadFirstPageNotification(
        @RoleType
        role: Int
    ) {
        val loadType = NotifcenterDetailUseCase.NotificationDetailLoadType.FIRST_PAGE
        viewModelScope.launch {
            val param = NotifcenterDetailUseCase.Param(
                filter = filter,
                role = role
            ).also {
                it.loadType = loadType
            }
            try {
                val result = notifcenterDetailUseCase(param)
                _mutateNotificationItems.value = NotificationDetailResponseWrapper(
                    result = Success(result),
                    loadType = loadType,
                    lastKnownPair = null
                )
                if (!hasFilter() && role == RoleType.BUYER) {
                    loadTopAdsBannerData()
                }
                if (role == RoleType.AFFILIATE) {
                    loadAffiliateEducationArticles()
                }
            } catch (throwable: Throwable) {
                _mutateNotificationItems.value =
                    NotificationDetailResponseWrapper(
                        result = Fail(throwable),
                        loadType = loadType,
                        lastKnownPair = null
                    )
            }
        }
    }

    fun loadMoreEarlier(
        @RoleType
        role: Int,
        lastKnownPosition: Int? = null,
        element: Visitable<NotificationTypeFactory>? = null
    ) {
        viewModelScope.launch {
            val loadType = NotifcenterDetailUseCase.NotificationDetailLoadType.LOAD_MORE_EARLIER
            val param = NotifcenterDetailUseCase.Param(
                filter = filter,
                role = role
            ).also {
                it.loadType = loadType
            }
            val lastKnownPair = if (lastKnownPosition == null || element == null) {
                null
            } else {
                Pair(lastKnownPosition, element)
            }
            try {
                val result = notifcenterDetailUseCase(param)
                _mutateNotificationItems.value = NotificationDetailResponseWrapper(
                    result = Success(result),
                    loadType = loadType,
                    lastKnownPair = lastKnownPair
                )
            } catch (throwable: Throwable) {
                _mutateNotificationItems.value = NotificationDetailResponseWrapper(
                    result = Fail(throwable),
                    loadType = loadType,
                    lastKnownPair = lastKnownPair
                )
            }
        }
    }

    fun loadMoreNew(
        @RoleType
        role: Int,
        lastKnownPosition: Int,
        element: Visitable<NotificationTypeFactory>
    ) {
        val loadType = NotifcenterDetailUseCase.NotificationDetailLoadType.LOAD_MORE_NEW
        viewModelScope.launch {
            val param = NotifcenterDetailUseCase.Param(
                filter = filter,
                role = role
            ).also {
                it.loadType = loadType
            }
            try {
                val result = notifcenterDetailUseCase(param)
                _mutateNotificationItems.value = NotificationDetailResponseWrapper(
                    result = Success(result),
                    loadType = loadType,
                    lastKnownPair = Pair(lastKnownPosition, element)
                )
            } catch (throwable: Throwable) {
                _mutateNotificationItems.value = NotificationDetailResponseWrapper(
                    result = Fail(throwable),
                    loadType = loadType,
                    lastKnownPair = Pair(lastKnownPosition, element)
                )
            }
        }
    }

    fun bumpReminder(product: ProductData, notif: NotificationUiModel) {
        viewModelScope.launch {
            try {
                val param = NotifcenterSetReminderBumpUseCase.Param(
                    productId = product.productId,
                    notifId = notif.notifId
                )
                bumpReminderUseCase(param).collect {
                    it.referer = product.productId
                    _bumpReminder.value = it
                }
            } catch (throwable: Throwable) {
                val error = Resource.error(throwable, null).apply {
                    referer = product.productId
                }
                _bumpReminder.value = error
            }
        }
    }

    fun deleteReminder(product: ProductData, notification: NotificationUiModel) {
        viewModelScope.launch {
            try {
                val param = NotifcenterDeleteReminderBumpUseCase.Param(
                    productId = product.productId,
                    notifId = notification.notifId
                )
                deleteReminderUseCase(param).collect {
                    it.referer = product.productId
                    _deleteReminder.postValue(it)
                }
            } catch (throwable: Throwable) {
                val error = Resource.error(throwable, null).apply {
                    referer = product.productId
                }
                _deleteReminder.postValue(error)
            }
        }
    }

    fun loadRecommendations(page: Int) {
        viewModelScope.launch {
            try {
                val params = GetRecommendationRequestParam(
                    pageNumber = page,
                    xSource = RECOM_WIDGET,
                    pageName = RECOM_SOURCE_INBOX_PAGE,
                    productIds = emptyList()
                )
                val recommendationWidget = getRecommendationUseCase.getData(params).firstOrNull()
                recommendationWidget?.let {
                    _recommendations.value = getRecommendationVisitables(page, it)
                }
            } catch (throwable: Throwable) {
                Timber.d(throwable)
            }
        }
    }

    fun reset() {
        filter = NotifcenterDetailUseCase.FILTER_NONE
    }

    override fun addWishlistV2(
        model: RecommendationItem,
        actionListener: WishlistV2ActionListener
    ) {
        doAddToWishlistV2(model.productId.toString(), actionListener)
    }

    fun doAddToWishlistV2(productId: String, actionListener: WishlistV2ActionListener) {
        launch(dispatcher.main) {
            addWishListV2UseCase.setParams(productId, userSessionInterface.userId)
            val result = withContext(dispatcher.io) { addWishListV2UseCase.executeOnBackground() }
            if (result is Success) {
                actionListener.onSuccessAddWishlist(result.data, productId)
            } else if (result is Fail) {
                actionListener.onErrorAddWishList(result.throwable, productId)
            }
        }
    }

    override fun removeWishlistV2(
        model: RecommendationItem,
        actionListener: WishlistV2ActionListener
    ) {
        launch(dispatcher.main) {
            deleteWishlistV2UseCase.setParams(
                model.productId.toString(),
                userSessionInterface.userId
            )
            val result =
                withContext(dispatcher.io) { deleteWishlistV2UseCase.executeOnBackground() }
            if (result is Success) {
                actionListener.onSuccessRemoveWishlist(result.data, model.productId.toString())
            } else if (result is Fail) {
                actionListener.onErrorRemoveWishlist(result.throwable, model.productId.toString())
            }
        }
    }

    fun clearNotifCounter(
        @RoleType
        role: Int
    ) {
        viewModelScope.launch {
            try {
                var type = role
                if (userSessionInterface.shopId == DEFAULT_SHOP_ID && role != RoleType.AFFILIATE) {
                    type = CLEAR_ALL_NOTIF_TYPE
                }
                clearNotifUseCase(type).collect {
                    _clearNotif.value = it
                }
            } catch (throwable: Throwable) {
                Timber.d(throwable)
            }
        }
    }

    fun loadTopAdsBannerData() {
        viewModelScope.launch {
            try {
                val results = getTopAdsImageData()
                if (results.isNotEmpty()) {
                    _topAdsBanner.value = NotificationTopAdsBannerUiModel(results)
                }
                loadRecommendations(1)
            } catch (throwable: Throwable) {
                Timber.d(throwable)
                loadRecommendations(1)
            }
        }
    }

    private suspend fun getTopAdsImageData(): ArrayList<TopAdsImageViewModel> {
        return withContext(dispatcher.io) {
            topAdsImageViewUseCase.getImageData(
                topAdsImageViewUseCase.getQueryMap(
                    "",
                    TOP_ADS_SOURCE,
                    "",
                    TOP_ADS_COUNT,
                    TOP_ADS_DIMEN_ID,
                    ""
                )
            )
        }
    }

    fun addProductToCart(
        requestParams: AddToCartRequestParams,
        onSuccessAddToCart: (data: DataModel) -> Unit,
        onError: (msg: String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val atcResponse = withContext(dispatcher.io) {
                    addToCartUseCase.addToCartRequestParams = requestParams
                    return@withContext addToCartUseCase.executeOnBackground()
                }
                if (atcResponse.isDataError()) {
                    onError(atcResponse.getAtcErrorMessage())
                } else {
                    onSuccessAddToCart(atcResponse.data)
                }
            } catch (throwable: Throwable) {
                onError(throwable.message)
            }
        }
    }

    private fun loadAffiliateEducationArticles() {
        viewModelScope.launch {
            try {
                affiliateEducationArticleUseCase(Unit).collect { response ->
                    if (response.data != null && !response.data.cardsArticle?.data?.cards.isNullOrEmpty()) {
                        response.data.cardsArticle?.data?.cards?.get(0)?.let {
                            _affiliateEducationArticle.postValue(
                                NotificationAffiliateEducationUiModel(it)
                            )
                        }
                    }
                }
            } catch (throwable: Throwable) {
                Timber.e(throwable)
            }
        }
    }

    fun getNotifications(shopId: String) {
        viewModelScope.launch {
            try {
                val result = getNotificationCounterUseCase(shopId)
                _notifications.postValue(Success(result.notifications))
            } catch (throwable: Throwable) {
                _notifications.postValue(Fail(throwable))
            }
        }
    }

    companion object {
        const val TOP_ADS_SOURCE = "19"
        const val TOP_ADS_COUNT = 3
        const val TOP_ADS_DIMEN_ID = 3

        const val RECOM_WIDGET = "recom_widget"
        const val RECOM_SOURCE_INBOX_PAGE = "inbox"

        const val DEFAULT_SHOP_ID = "0"
        const val CLEAR_ALL_NOTIF_TYPE = 0

        private fun isFirstPage(page: Int): Boolean {
            return page == 1
        }

        /*
        * TODO:
        * seems this method similar like data mapper,
        * I suggest to you to move it onto `mapper` one.
        * so your viewModel focusing the actual behavior.
        *
        * Nit:
        * getRecommendationVisitables also used in the unit test class
        * */
        fun getRecommendationVisitables(
            page: Int,
            recommendationWidget: RecommendationWidget
        ): RecommendationDataModel {
            var items: List<Visitable<*>> = recommendationWidget.recommendationItemList.map {
                RecommendationUiModel(it)
            }
            if (isFirstPage(page)) {
                items = items.toMutableList()
                items.add(0, RecommendationTitleUiModel(recommendationWidget.title))
            }
            return RecommendationDataModel(items, recommendationWidget.hasNext)
        }
    }
}
