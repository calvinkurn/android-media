package com.tokopedia.notifcenter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.notifcenter.data.entity.bumpreminder.BumpReminderResponse
import com.tokopedia.notifcenter.data.entity.clearnotif.ClearNotifCounterResponse
import com.tokopedia.notifcenter.data.entity.deletereminder.DeleteReminderResponse
import com.tokopedia.notifcenter.data.entity.filter.NotifcenterFilterResponse
import com.tokopedia.notifcenter.data.entity.notification.NotificationDetailResponseModel
import com.tokopedia.notifcenter.data.entity.notification.ProductData
import com.tokopedia.notifcenter.data.entity.orderlist.NotifOrderListResponse
import com.tokopedia.notifcenter.data.model.RecommendationDataModel
import com.tokopedia.notifcenter.data.state.Resource
import com.tokopedia.notifcenter.data.uimodel.NotificationTopAdsBannerUiModel
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.data.uimodel.RecommendationTitleUiModel
import com.tokopedia.notifcenter.data.uimodel.RecommendationUiModel
import com.tokopedia.notifcenter.domain.*
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface INotificationViewModel {
    fun addWishlist(model: RecommendationItem, callback: (Boolean, Throwable?) -> Unit)
    fun removeWishList(model: RecommendationItem, callback: (Boolean, Throwable?) -> Unit)
}

class NotificationViewModel @Inject constructor(
        private val notifcenterDetailUseCase: NotifcenterDetailUseCase,
        private val notifcenterFilterUseCase: NotifcenterFilterV2UseCase,
        private val bumpReminderUseCase: NotifcenterSetReminderBumpUseCase,
        private val deleteReminderUseCase: NotifcenterDeleteReminderBumpUseCase,
        private val clearNotifUseCase: ClearNotifCounterUseCase,
        private val markAsReadUseCase: MarkNotificationAsReadUseCase,
        private val topAdsImageViewUseCase: TopAdsImageViewUseCase,
        private val getRecommendationUseCase: GetRecommendationUseCase,
        private val addWishListUseCase: AddWishListUseCase,
        private val topAdsWishlishedUseCase: TopAdsWishlishedUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val userSessionInterface: UserSessionInterface,
        private var addToCartUseCase: AddToCartUseCase,
        private var notifOrderListUseCase: NotifOrderListUseCase,
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io), INotificationViewModel {

    var filter: Long = NotifcenterDetailUseCase.FILTER_NONE
        set(value) {
            field = value
            cancelAllUseCase()
        }

    private val _mutateNotificationItems = MutableLiveData<Result<NotificationDetailResponseModel>>()
    val notificationItems: LiveData<Result<NotificationDetailResponseModel>>
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

    fun hasFilter(): Boolean {
        return filter != NotifcenterDetailUseCase.FILTER_NONE
    }

    fun cancelAllUseCase() {
        notifcenterDetailUseCase.cancelRunningOperation()
        coroutineContext.cancelChildren()
    }

    fun loadNotifOrderList(
            @RoleType
            role: Int?
    ) {
        if (role == null) return
        launchCatchError(dispatcher.io,
                {
                    notifOrderListUseCase.getOrderList(role).collect {
                        _orderList.postValue(it)
                    }
                },
                {
                    _orderList.postValue(Resource.error(it, null))
                }
        )
    }

    /**
     * Load notification on first page
     */
    fun loadFirstPageNotification(
            @RoleType
            role: Int?
    ) {
        if (role == null) return
        notifcenterDetailUseCase.getFirstPageNotification(filter, role,
                {
                    _mutateNotificationItems.value = Success(it)
                    if (!hasFilter() && role == RoleType.BUYER) {
                        loadTopAdsBannerData()
                    }
                },
                {
                    _mutateNotificationItems.value = Fail(it)
                }
        )
    }

    fun loadNotificationFilter(
            @RoleType
            role: Int?
    ) {
        if (role == null) return
        launchCatchError(dispatcher.io,
                {
                    notifcenterFilterUseCase.getFilter(role).collect {
                        _filterList.postValue(it)
                    }
                },
                {
                    _filterList.postValue(Resource.error(it, null))
                }
        )
    }


    fun markNotificationAsRead(
            @RoleType
            role: Int?,
            element: NotificationUiModel
    ) {
        if (role == null) return
        launchCatchError(dispatcher.io,
                {
                    markAsReadUseCase.markAsRead(role, element.notifId).collect { }
                },
                { }
        )
    }

    fun loadMoreEarlier(
            @RoleType
            role: Int?
    ) {
        if (role == null) return
        notifcenterDetailUseCase.getMoreEarlierNotifications(
                filter, role,
                {
                    _mutateNotificationItems.value = Success(it)
                },
                {
                    _mutateNotificationItems.value = Fail(it)
                }
        )
    }

    fun loadMoreNew(
            @RoleType
            role: Int?,
            onSuccess: (NotificationDetailResponseModel) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        if (role == null) return
        notifcenterDetailUseCase.getMoreNewNotifications(
                filter, role, onSuccess, onError
        )
    }

    fun loadMoreEarlier(
            @RoleType
            role: Int?,
            onSuccess: (NotificationDetailResponseModel) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        if (role == null) return
        notifcenterDetailUseCase.getMoreEarlierNotifications(
                filter, role, onSuccess, onError
        )
    }

    fun bumpReminder(product: ProductData, notif: NotificationUiModel) {
        launchCatchError(dispatcher.io,
                {
                    bumpReminderUseCase.bumpReminder(
                            product.productId.toString(),
                            notif.notifId
                    ).collect {
                        it.referer = product.productId
                        _bumpReminder.postValue(it)
                    }
                },
                {
                    val error = Resource.error(it, null).apply {
                        referer = product.productId
                    }
                    _bumpReminder.postValue(error)
                }
        )
    }

    fun deleteReminder(product: ProductData, notification: NotificationUiModel) {
        launchCatchError(dispatcher.io,
                {
                    deleteReminderUseCase.deleteReminder(
                            product.productId.toString(),
                            notification.notifId
                    ).collect {
                        it.referer = product.productId
                        _deleteReminder.postValue(it)
                    }
                },
                {
                    val error = Resource.error(it, null).apply {
                        referer = product.productId
                    }
                    _deleteReminder.postValue(error)
                }
        )
    }

    fun loadRecommendations(page: Int) {
        launchCatchError(dispatcher.io,
                {
                    val params = getRecommendationUseCase.getRecomParams(
                            page,
                            RECOM_WIDGET,
                            RECOM_SOURCE_INBOX_PAGE,
                            emptyList()
                    )
                    val recommendationWidget = getRecommendationUseCase.createObservable(params)
                            .toBlocking()
                            .single()[0]
                    withContext(dispatcher.main) {
                        _recommendations.value = getRecommendationVisitables(
                                page, recommendationWidget
                        )
                    }
                },
                {

                }
        )
    }

    fun reset() {
        filter = NotifcenterDetailUseCase.FILTER_NONE
    }

    override fun addWishlist(
            model: RecommendationItem,
            callback: ((Boolean, Throwable?) -> Unit)
    ) {
        if (model.isTopAds) {
            addWishListTopAds(model, callback)
        } else {
            addWishListNormal(model, callback)
        }
    }

    override fun removeWishList(
            model: RecommendationItem,
            callback: (((Boolean, Throwable?) -> Unit))
    ) {
        removeWishListUseCase.createObservable(
                model.productId.toString(),
                userSessionInterface.userId,
                object : WishListActionListener {
                    override fun onErrorAddWishList(errorMessage: String?, productId: String?) {}
                    override fun onSuccessAddWishlist(productId: String?) {}
                    override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                        callback.invoke(false, Throwable(errorMessage))
                    }

                    override fun onSuccessRemoveWishlist(productId: String?) {
                        callback.invoke(true, null)
                    }
                }
        )
    }

    fun clearNotifCounter(
            @RoleType
            role: Int?
    ) {
        if (role == null) return
        launchCatchError(dispatcher.io,
                {
                    clearNotifUseCase.clearNotifCounter(role).collect {
                        _clearNotif.postValue(it)
                    }
                }, { }
        )
    }

    private fun addWishListTopAds(
            model: RecommendationItem, callback: ((Boolean, Throwable?) -> Unit)
    ) {
        launchCatchError(dispatcher.io,
                {
                    val params = RequestParams.create()?.apply {
                        putString(TopAdsWishlishedUseCase.WISHSLIST_URL, model.wishlistUrl)
                    }
                    val response = topAdsWishlishedUseCase.createObservable(params)
                            .toBlocking().single()
                    if (response.data != null) {
                        callback.invoke(true, null)
                    }
                },
                {
                    callback.invoke(false, it)
                }
        )
    }

    private fun addWishListNormal(
            model: RecommendationItem, callback: ((Boolean, Throwable?) -> Unit)
    ) {
        addWishListUseCase.createObservable(
                model.productId.toString(),
                userSessionInterface.userId,
                object : WishListActionListener {
                    override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                        callback.invoke(false, Throwable(errorMessage))
                    }

                    override fun onSuccessAddWishlist(productId: String?) {
                        callback.invoke(true, null)
                    }

                    override fun onErrorRemoveWishlist(
                            errorMessage: String?, productId: String?
                    ) {
                    }

                    override fun onSuccessRemoveWishlist(productId: String?) {}
                }
        )
    }

    private fun loadTopAdsBannerData() {
        launchCatchError(
                dispatcher.io,
                {
                    val results = topAdsImageViewUseCase.getImageData(
                            topAdsImageViewUseCase.getQueryMap(
                                    "",
                                    TOP_ADS_SOURCE,
                                    "",
                                    TOP_ADS_COUNT,
                                    TOP_ADS_DIMEN_ID,
                                    ""
                            )
                    )
                    if (results.isNotEmpty()) {
                        _topAdsBanner.postValue(NotificationTopAdsBannerUiModel(results.first()))
                    }
                    loadRecommendations(1)
                },
                {
                    it.printStackTrace()
                    loadRecommendations(1)
                }
        )

    }

    fun addProductToCart(
            requestParams: RequestParams,
            onSuccessAddToCart: (data: DataModel) -> Unit,
            onError: (msg: String) -> Unit
    ) {
        launchCatchError(
                dispatcher.io,
                block = {
                    val atcResponse = addToCartUseCase.createObservable(requestParams)
                            .toBlocking()
                            .single().data
                    withContext(dispatcher.main) {
                        if (atcResponse.success == 1) {
                            onSuccessAddToCart(atcResponse)
                        } else {
                            onError(atcResponse.message.getOrNull(0) ?: "")
                        }
                    }
                },
                onError = {
                    withContext(dispatcher.main) {
                        it.message?.let { errorMsg ->
                            onError(errorMsg)
                        }
                    }
                }
        )
    }

    companion object {
        const val TOP_ADS_SOURCE = "5"
        const val TOP_ADS_COUNT = 1
        const val TOP_ADS_DIMEN_ID = 3

        const val RECOM_WIDGET = "recom_widget"
        const val RECOM_SOURCE_INBOX_PAGE = "inbox"

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