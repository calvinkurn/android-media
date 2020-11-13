package com.tokopedia.notifcenter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.notifcenter.common.NotificationFilterType
import com.tokopedia.notifcenter.data.entity.notification.NotificationDetailResponseModel
import com.tokopedia.notifcenter.data.model.RecommendationDataModel
import com.tokopedia.notifcenter.data.uimodel.NotificationTopAdsBannerUiModel
import com.tokopedia.notifcenter.data.uimodel.RecommendationTitleUiModel
import com.tokopedia.notifcenter.data.uimodel.RecommendationUiModel
import com.tokopedia.notifcenter.domain.NotifcenterDetailUseCase
import com.tokopedia.notifcenter.util.coroutines.DispatcherProvider
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
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface INotificationViewModel {
    fun addWishlist(model: RecommendationItem, callback: (Boolean, Throwable?) -> Unit)
    fun removeWishList(model: RecommendationItem, callback: (Boolean, Throwable?) -> Unit)
}

class NotificationViewModel @Inject constructor(
        private val notifcenterDetailUseCase: NotifcenterDetailUseCase,
        private val topAdsImageViewUseCase: TopAdsImageViewUseCase,
        private val getRecommendationUseCase: GetRecommendationUseCase,
        private val addWishListUseCase: AddWishListUseCase,
        private val topAdsWishlishedUseCase: TopAdsWishlishedUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val userSessionInterface: UserSessionInterface,
        private val dispatcher: DispatcherProvider
) : BaseViewModel(dispatcher.io()), INotificationViewModel {

    @NotificationFilterType
    var filter = NotificationFilterType.NONE
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

    fun hasFilter(): Boolean {
        return filter != NotificationFilterType.NONE
    }

    fun cancelAllUseCase() {
        notifcenterDetailUseCase.cancelRunningOperation()
        coroutineContext.cancelChildren()
    }

    /**
     * Load notification on first page
     */
    fun loadNotification(
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
                    if (!hasFilter() && role == RoleType.BUYER) {
                        loadTopAdsBannerData()
                    }
                }
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

    /**
     * TODO: combine with [loadRecommendations]
     */
//    private fun getFirstRecommendationData() {
//        launchCatchError(dispatcher.io(),
//                {
//                    val params = getRecommendationUseCase.getRecomParams(
//                            0,
//                            RECOM_WIDGET,
//                            RECOM_SOURCE_INBOX_PAGE,
//                            emptyList()
//                    )
//                    val recommendationWidget = getRecommendationUseCase.createObservable(params)
//                            .toBlocking()
//                            .single()[0]
////                        visitables.add(RecomTitle(recommendationWidget?.title))
//                    withContext(dispatcher.ui()) {
//                        _recommendations.value = getRecommendationVisitables(recommendationWidget)
//                    }
////                        inboxView?.hideLoadMoreLoading()
////                        inboxView?.onRenderRecomInbox(visitables)
//                },
//                {
//
//                }
//        )
//    }

    fun loadRecommendations(page: Int) {
        launchCatchError(dispatcher.io(),
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
//                        visitables.add(RecomTitle(recommendationWidget?.title))
                    withContext(dispatcher.ui()) {
                        _recommendations.value = getRecommendationVisitables(
                                page, recommendationWidget
                        )
                    }
//                        inboxView?.hideLoadMoreLoading()
//                        inboxView?.onRenderRecomInbox(visitables)
                },
                {

                }
        )
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

    private fun addWishListTopAds(
            model: RecommendationItem, callback: ((Boolean, Throwable?) -> Unit)
    ) {
        launchCatchError(dispatcher.io(),
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


    private fun getRecommendationVisitables(
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

    private fun isFirstPage(page: Int): Boolean {
        return page == 1
    }

    private fun loadTopAdsBannerData() {
        launchCatchError(
                dispatcher.io(),
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

    companion object {
        const val TOP_ADS_SOURCE = "5"
        const val TOP_ADS_COUNT = 1
        const val TOP_ADS_DIMEN_ID = 4

        const val RECOM_WIDGET = "recom_widget"
        const val RECOM_SOURCE_INBOX_PAGE = "inbox"
    }
}