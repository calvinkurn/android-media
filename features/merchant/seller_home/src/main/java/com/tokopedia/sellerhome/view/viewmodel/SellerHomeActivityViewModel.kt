package com.tokopedia.sellerhome.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.sellerhome.common.coroutine.SellerHomeCoroutineDispatcher
import com.tokopedia.sellerhome.domain.usecase.GetNotificationUseCase
import com.tokopedia.sellerhome.domain.usecase.GetShopInfoUseCase
import com.tokopedia.sellerhome.view.model.NotificationUiModel
import com.tokopedia.sellerhome.view.model.ShopInfoUiModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

/**
 * Created By @ilhamsuaib on 2020-02-27
 */

class SellerHomeActivityViewModel @Inject constructor(
        private val userSession: UserSessionInterface,
        private val getNotificationUseCase: GetNotificationUseCase,
        private val getSopInfoUseCase: GetShopInfoUseCase,
        private val dispatcher: SellerHomeCoroutineDispatcher
) : CustomBaseViewModel(dispatcher.main()) {

    private val _notifications = MutableLiveData<Result<NotificationUiModel>>()
    val notifications: LiveData<Result<NotificationUiModel>>
        get() = _notifications

    private val _shopInfo = MutableLiveData<Result<ShopInfoUiModel>>()
    val shopInfo: LiveData<Result<ShopInfoUiModel>>
        get() = _shopInfo

    fun getNotifications() = executeCall(_notifications) {
        getNotificationUseCase.params = GetNotificationUseCase.getRequestParams()
        getNotificationUseCase.executeOnBackground()
    }

    fun getShopInfo() = executeCall(_shopInfo) {
        getSopInfoUseCase.params = GetShopInfoUseCase.getRequestParam(userSession.userId)
        getSopInfoUseCase.executeOnBackground()
    }
}