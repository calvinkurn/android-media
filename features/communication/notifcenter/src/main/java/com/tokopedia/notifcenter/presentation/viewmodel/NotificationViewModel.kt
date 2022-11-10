package com.tokopedia.notifcenter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
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
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import javax.inject.Inject

interface INotificationViewModel {
    fun addWishlistV2(model: RecommendationItem, listener: WishlistV2ActionListener)
    fun removeWishlistV2(model: RecommendationItem, listener: WishlistV2ActionListener)
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
    private val addWishListV2UseCase: AddToWishlistV2UseCase,
    private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
    private val topAdsWishlishedUseCase: TopAdsWishlishedUseCase,
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

    private val _mutateNotificationItems =
        MutableLiveData<Result<NotificationDetailResponseModel>>()
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
                    product.productId,
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
                    product.productId,
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
                val recommendationWidget = getRecommendationUseCase
                    .createObservable(params)
                    .toBlocking()
                    .single()
                    .getOrNull(0) ?: RecommendationWidget()
                withContext(dispatcher.main) {
                    _recommendations.value = getRecommendationVisitables(
                        page, recommendationWidget
                    )
                }
            },
            {
                it.printStackTrace()
            }
        )
    }

    fun reset() {
        filter = NotifcenterDetailUseCase.FILTER_NONE
    }

    override fun addWishlistV2(
        model: RecommendationItem,
        actionListener: WishlistV2ActionListener) {
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
        actionListener: WishlistV2ActionListener) {
        launch(dispatcher.main) {
            deleteWishlistV2UseCase.setParams(model.productId.toString(), userSessionInterface.userId)
            val result = withContext(dispatcher.io) { deleteWishlistV2UseCase.executeOnBackground() }
            if (result is Success) {
                actionListener.onSuccessRemoveWishlist(result.data, model.productId.toString())
            } else if (result is Fail) {
                actionListener.onErrorRemoveWishlist(result.throwable, model.productId.toString())
            }
        }
    }

    fun clearNotifCounter(
        @RoleType
        role: Int?
    ) {
        if (role == null) return
        launchCatchError(dispatcher.io,
            {
                var type = role
                if (userSessionInterface.shopId == DEFAULT_SHOP_ID) {
                    type = CLEAR_ALL_NOTIF_TYPE
                }
                clearNotifUseCase.clearNotifCounter(type).collect {
                    _clearNotif.postValue(it)
                }
            }, { }
        )
    }

    fun addWishListTopAds(
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

    fun loadTopAdsBannerData() {
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
                    _topAdsBanner.postValue(NotificationTopAdsBannerUiModel(results))
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
        requestParams: AddToCartRequestParams,
        onSuccessAddToCart: (data: DataModel) -> Unit,
        onError: (msg: String?) -> Unit
    ) {
        launchCatchError(
            dispatcher.io,
            block = {
                addToCartUseCase.addToCartRequestParams = requestParams
                val atcResponse = addToCartUseCase.executeOnBackground()
                withContext(dispatcher.main) {
                    if (atcResponse.isDataError()) {
                        onError(atcResponse.getAtcErrorMessage())
                    } else {
                        onSuccessAddToCart(atcResponse.data)
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