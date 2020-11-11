package com.tokopedia.notifcenter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.notifcenter.common.NotificationFilterType
import com.tokopedia.notifcenter.data.entity.notification.NotificationDetailResponseModel
import com.tokopedia.notifcenter.data.uimodel.NotificationTopAdsBannerUiModel
import com.tokopedia.notifcenter.data.uimodel.RecommendationUiModel
import com.tokopedia.notifcenter.domain.NotifcenterDetailUseCase
import com.tokopedia.notifcenter.util.coroutines.DispatcherProvider
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotificationViewModel @Inject constructor(
        private val notifcenterDetailUseCase: NotifcenterDetailUseCase,
        private val topAdsImageViewUseCase: TopAdsImageViewUseCase,
        private val getRecommendationUseCase: GetRecommendationUseCase,
        private val dispatcher: DispatcherProvider
) : BaseViewModel(dispatcher.io()) {

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

    private val _recommendations = MutableLiveData<List<RecommendationUiModel>>()
    val recommendations: LiveData<List<RecommendationUiModel>>
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
                    if (!hasFilter()) {
                        loadTopAdsBannerData()
                    }
                },
                {
                    _mutateNotificationItems.value = Fail(it)
                    if (!hasFilter()) {
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
     * TODO: combine with [loadMoreRecom]
     */
    private fun getFirstRecommendationData() {
        launchCatchError(dispatcher.io(),
                {
                    val params = getRecommendationUseCase.getRecomParams(
                            0,
                            RECOM_WIDGET,
                            RECOM_SOURCE_INBOX_PAGE,
                            emptyList()
                    )
                    val recommendationWidget = getRecommendationUseCase.createObservable(params)
                            .toBlocking()
                            .single()[0]
//                        visitables.add(RecomTitle(recommendationWidget?.title))
                    withContext(dispatcher.ui()) {
                        _recommendations.value = getRecommendationVisitables(recommendationWidget)
                    }
//                        inboxView?.hideLoadMoreLoading()
//                        inboxView?.onRenderRecomInbox(visitables)
                },
                {

                }
        )
    }

    fun loadMoreRecom(page: Int) {
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
                        _recommendations.value = getRecommendationVisitables(recommendationWidget)
                    }
//                        inboxView?.hideLoadMoreLoading()
//                        inboxView?.onRenderRecomInbox(visitables)
                },
                {

                }
        )
    }

    private fun getRecommendationVisitables(
            recommendationWidget: RecommendationWidget
    ): List<RecommendationUiModel> {
        return recommendationWidget.recommendationItemList.map {
            RecommendationUiModel(it, recommendationWidget.hasNext)
        }
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
                    getFirstRecommendationData()
                },
                {
                    it.printStackTrace()
                    getFirstRecommendationData()
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