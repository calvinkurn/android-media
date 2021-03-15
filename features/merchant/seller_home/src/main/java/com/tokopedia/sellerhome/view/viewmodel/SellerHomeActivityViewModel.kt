package com.tokopedia.sellerhome.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerhome.domain.usecase.GetNotificationUseCase
import com.tokopedia.sellerhome.domain.usecase.GetShopInfoUseCase
import com.tokopedia.sellerhome.domain.usecase.SellerAdminUseCase
import com.tokopedia.sellerhome.view.model.NotificationUiModel
import com.tokopedia.sellerhome.view.model.ShopInfoUiModel
import com.tokopedia.sessioncommon.data.admin.AdminRoleType
import com.tokopedia.sessioncommon.domain.usecase.GetAdminTypeUseCase
import com.tokopedia.shop.common.constant.AccessId
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 2020-02-27
 */

class SellerHomeActivityViewModel @Inject constructor(
        private val userSession: UserSessionInterface,
        private val getNotificationUseCase: GetNotificationUseCase,
        private val getSopInfoUseCase: GetShopInfoUseCase,
        private val sellerAdminUseCase: SellerAdminUseCase,
        private val authorizeChatAccessUseCase: AuthorizeAccessUseCase,
        private val authorizeOrderAccessUseCase: AuthorizeAccessUseCase,
        dispatcher: CoroutineDispatchers
) : CustomBaseViewModel(dispatcher) {

    companion object {
        private const val SOURCE = "stuart_seller_home"
    }

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
        val notificationUiModelDeferred = async {
            getNotificationUseCase.params = GetNotificationUseCase.getRequestParams()
            getNotificationUseCase.executeOnBackground()
        }
        val isRoleChatAdminDeferred = async { getIsRoleChatAdmin() }
        val isRoleOrderAdminDeferred = async { getIsRoleOrderAdmin() }
        notificationUiModelDeferred.await().let {
            it.copy(
                    chat =
                        if (isRoleChatAdminDeferred.await()) {
                            it.chat
                        } else {
                            0
                        },
                    sellerOrderStatus =
                        if (isRoleOrderAdminDeferred.await()) {
                            it.sellerOrderStatus
                        } else {
                            it.sellerOrderStatus.copy(
                                    newOrder = 0,
                                    readyToShip = 0
                            )
                        }
            )
        }
    }

    fun getShopInfo() = executeCall(_shopInfo) {
        getSopInfoUseCase.params = GetShopInfoUseCase.getRequestParam(userSession.userId)
        getSopInfoUseCase.executeOnBackground()
    }

    fun getAdminInfo() = executeCall(_isRoleEligible) {
        getEligiblityOnlyWhenAdminShouldCheckRole {
            sellerAdminUseCase.requestParams = SellerAdminUseCase.createRequestParams(SOURCE)
            sellerAdminUseCase.executeOnBackground().let { adminDataResponse ->
                adminDataResponse.data.detail.roleType.also { roleType ->
                    updateUserSessionAdminValues(roleType, adminDataResponse.isMultiLocationShop)
                }
            }.run {
                isShopOwner || !isLocationAdmin
            }
        }
    }

    private suspend fun getIsRoleChatAdmin(): Boolean {
        return getEligiblityOnlyWhenAdminShouldCheckRole {
            try {
                val requestParams = AuthorizeAccessUseCase.createRequestParams(userSession.shopId.toLongOrZero(), AccessId.CHAT)
                authorizeChatAccessUseCase.execute(requestParams)
            } catch (ex: Exception) {
                false
            }
        }
    }

    private suspend fun getIsRoleOrderAdmin(): Boolean {
        return getEligiblityOnlyWhenAdminShouldCheckRole {
            try {
                val requestParams = AuthorizeAccessUseCase.createRequestParams(userSession.shopId.toLongOrZero(), AccessId.SOM_LIST)
                authorizeOrderAccessUseCase.execute(requestParams)
            } catch (ex: Exception) {
                false
            }
        }
    }

    private suspend fun getEligiblityOnlyWhenAdminShouldCheckRole(action: suspend () -> Boolean): Boolean {
        return if (userSession.isShopOwner) {
            true
        } else {
            action.invoke()
        }
    }

    private fun updateUserSessionAdminValues(roleType: AdminRoleType, isMultiLocationShop: Boolean) {
        with(userSession) {
            setIsShopOwner(roleType.isShopOwner)
            setIsLocationAdmin(roleType.isLocationAdmin)
            setIsShopAdmin(roleType.isShopAdmin)
            setIsMultiLocationShop(isMultiLocationShop)
        }
    }

}