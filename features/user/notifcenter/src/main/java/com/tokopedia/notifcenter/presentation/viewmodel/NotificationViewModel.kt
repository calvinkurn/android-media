package com.tokopedia.notifcenter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.notifcenter.common.NotificationFilterType
import com.tokopedia.notifcenter.data.uimodel.NotificationTopAdsBannerUiModel
import com.tokopedia.notifcenter.domain.NotifcenterDetailUseCase
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationTypeFactory
import com.tokopedia.notifcenter.util.coroutines.DispatcherProvider
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class NotificationViewModel @Inject constructor(
        private val notifcenterDetailUseCase: NotifcenterDetailUseCase,
        private val topAdsImageViewUseCase: TopAdsImageViewUseCase,
        private val dispatcher: DispatcherProvider
) : BaseViewModel(dispatcher.io()) {

    @NotificationFilterType
    var filter = NotificationFilterType.NONE

    private val _mutateNotificationItems = MutableLiveData<Result<List<Visitable<NotificationTypeFactory>>>>()
    val notificationItems: LiveData<Result<List<Visitable<NotificationTypeFactory>>>>
        get() = _mutateNotificationItems

    private val _topAdsBanner = MutableLiveData<NotificationTopAdsBannerUiModel>()
    val topAdsBanner: LiveData<NotificationTopAdsBannerUiModel>
        get() = _topAdsBanner

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
                    loadTopAdsBannerData()
                },
                {
                    _mutateNotificationItems.value = Fail(it)
                }
        )
    }

    fun loadMoreEarlier(
            @RoleType
            role: Int?,
            onSuccess: (List<Visitable<NotificationTypeFactory>>) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        if (role == null) return
        notifcenterDetailUseCase.getMoreEarlierNotifications(
                filter, role, onSuccess, onError
        )
    }

    fun loadMoreNew(
            @RoleType
            role: Int?,
            onSuccess: (List<Visitable<NotificationTypeFactory>>) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        if (role == null) return
        notifcenterDetailUseCase.getMoreNewNotifications(
                filter, role, onSuccess, onError
        )
    }

    fun loadTopAdsBannerData() {
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
                },
                {
                    it.printStackTrace()
                }
        )

    }

    companion object {
        const val TOP_ADS_SOURCE = "5"
        const val TOP_ADS_COUNT = 1
        const val TOP_ADS_DIMEN_ID = 4
    }
}