package com.tokopedia.sellerhome.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerhome.domain.usecase.GetNotificationUseCase
import com.tokopedia.sellerhome.domain.usecase.GetShopInfoUseCase
import com.tokopedia.sellerhome.view.model.NotificationUiModel
import com.tokopedia.sellerhome.view.model.ShopInfoUiModel
import com.tokopedia.shop.common.domain.interactor.AdminInfoUseCase
import com.tokopedia.shop.common.domain.interactor.model.adminrevamp.AdminInfoResult
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 2020-02-27
 */

class SellerHomeActivityViewModel @Inject constructor(
        private val userSession: UserSessionInterface,
        private val getNotificationUseCase: GetNotificationUseCase,
        private val getSopInfoUseCase: GetShopInfoUseCase,
        private val adminInfoUseCase: AdminInfoUseCase,
        dispatcher: CoroutineDispatchers
) : CustomBaseViewModel(dispatcher) {

    private val _notifications = MutableLiveData<Result<NotificationUiModel>>()
    val notifications: LiveData<Result<NotificationUiModel>>
        get() = _notifications

    private val _shopInfo = MutableLiveData<Result<ShopInfoUiModel>>()
    val shopInfo: LiveData<Result<ShopInfoUiModel>>
        get() = _shopInfo

    private val _isRoleEligible = MutableLiveData<Result<Boolean>>()
    val isRoleEligible: LiveData<Result<Boolean>>
        get() = _isRoleEligible

    fun getNotifications() = executeCall(_notifications) {
        getNotificationUseCase.params = GetNotificationUseCase.getRequestParams()
        getNotificationUseCase.executeOnBackground()
    }

    fun getShopInfo() = executeCall(_shopInfo) {
        getSopInfoUseCase.params = GetShopInfoUseCase.getRequestParam(userSession.userId)
        getSopInfoUseCase.executeOnBackground()
    }

    fun getAdminInfo() = executeCall(_isRoleEligible) {
        val requestParams = AdminInfoUseCase.createRequestParams(userSession.shopId.toIntOrZero())
        (adminInfoUseCase.execute(requestParams) as? AdminInfoResult.Success)?.let { result ->
            result.data?.let { data ->
                val roleType = data.detailInfo?.adminRoleType?.also { roleType ->
                    with(userSession) {
                        // TODO: Update user session value
                    }
                }
                roleType?.isShopOwner == true || roleType?.isLocationAdmin == false
            }
            // TODO: Add logic when admin info request fails. Still asking PM
        } ?: true
    }

}