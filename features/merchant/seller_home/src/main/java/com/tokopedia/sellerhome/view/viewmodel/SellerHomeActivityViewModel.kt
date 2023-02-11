package com.tokopedia.sellerhome.view.viewmodel

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.wear.remote.interactions.RemoteActivityHelper
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.NodeClient
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.device.info.DeviceInfo.await
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerhome.domain.usecase.GetNotificationUseCase
import com.tokopedia.sellerhome.domain.usecase.GetShopInfoUseCase
import com.tokopedia.sellerhome.domain.usecase.GetShopStateUseCase
import com.tokopedia.sellerhome.domain.usecase.SellerAdminUseCase
import com.tokopedia.sellerhome.view.model.NotificationUiModel
import com.tokopedia.sellerhome.view.model.ShopInfoUiModel
import com.tokopedia.sellerhome.view.model.ShopStateInfoUiModel
import com.tokopedia.sessioncommon.data.admin.AdminRoleType
import com.tokopedia.shop.common.constant.AccessId
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import kotlinx.coroutines.guava.await
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
    private val getShopStateUseCase: GetShopStateUseCase,
    private val capabilityClient: CapabilityClient,
    private val nodeClient: NodeClient,
    private val remoteActivityHelper: RemoteActivityHelper,
    dispatcher: CoroutineDispatchers
) : CustomBaseViewModel(dispatcher) {

    companion object {
        private const val SOURCE = "stuart_seller_home"

        private const val CAPABILITY_WEAR_APP = "verify_remote_tokopedia_wear_app"
        private const val SELLER_HOME_PAGE_NAME = "seller-home"
        private const val SELLER_INFO_STATE_KEY = "shopStateChanged"
    }

    private val _notifications = MutableLiveData<Result<NotificationUiModel>>()

    val notifications: LiveData<Result<NotificationUiModel>>
        get() = _notifications

    private val _shopInfo = MutableLiveData<Result<ShopInfoUiModel>>()
    val shopInfo: LiveData<Result<ShopInfoUiModel>>
        get() = _shopInfo

    private val _shopStateInfo = MutableLiveData<Result<ShopStateInfoUiModel>>()
    val shopStateInfo: LiveData<Result<ShopStateInfoUiModel>>
        get() = _shopStateInfo

    private val _isRoleEligible = MutableLiveData<Result<Boolean>>()
    val isRoleEligible: LiveData<Result<Boolean>>
        get() = _isRoleEligible

    private val _shouldAskInstallCompanionApp = MutableLiveData<Boolean>()
    val shouldAskInstallCompanionApp: LiveData<Boolean>
        get() = _shouldAskInstallCompanionApp

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
        getEligibilityOnlyWhenAdminShouldCheckRole {
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

    fun getShopStateInfo() = executeCall(_shopStateInfo) {
        getShopStateUseCase.executeInBackground(
            shopId = userSession.shopId,
            dataKey = SELLER_INFO_STATE_KEY,
            pageSource = SELLER_HOME_PAGE_NAME
        )
    }

    fun checkIfWearHasCompanionApp() {
        launch {
            nodeClient.connectedNodes.await()?.let { connectedNodes ->
                connectedNodes.firstOrNull { it.isNearby }?.let {
                    try {
                        val capabilityInfo = capabilityClient
                            .getCapability(CAPABILITY_WEAR_APP, CapabilityClient.FILTER_ALL)
                            .await()

                        withContext(Dispatchers.Main) {
                            // There should only ever be one phone in a node set (much less w/ the correct
                            // capability), so I am just grabbing the first one (which should be the only one).
                            val nodes = capabilityInfo?.nodes
                            val androidPhoneNodeWithApp =
                                nodes?.firstOrNull { node -> node.isNearby }

                            _shouldAskInstallCompanionApp.value = it != androidPhoneNodeWithApp
                        }
                    } catch (cancellationException: CancellationException) {
                        // Request was cancelled normally
                    } catch (throwable: Throwable) {
                    }
                }
            }
        }
    }

    fun launchMarket(marketIntent: Intent) {
        launch {
            startRemoteActivity(remoteActivityHelper, marketIntent)
        }
    }

    private suspend fun getIsRoleChatAdmin(): Boolean {
        return getEligibilityOnlyWhenAdminShouldCheckRole {
            try {
                val requestParams = AuthorizeAccessUseCase.createRequestParams(userSession.shopId.toLongOrZero(), AccessId.CHAT)
                authorizeChatAccessUseCase.execute(requestParams)
            } catch (ex: Exception) {
                false
            }
        }
    }

    private suspend fun getIsRoleOrderAdmin(): Boolean {
        return getEligibilityOnlyWhenAdminShouldCheckRole {
            try {
                val requestParams = AuthorizeAccessUseCase.createRequestParams(userSession.shopId.toLongOrZero(), AccessId.SOM_LIST)
                authorizeOrderAccessUseCase.execute(requestParams)
            } catch (ex: Exception) {
                false
            }
        }
    }

    private suspend fun getEligibilityOnlyWhenAdminShouldCheckRole(action: suspend () -> Boolean): Boolean {
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

    private suspend fun startRemoteActivity(
        remoteActivityHelper: RemoteActivityHelper,
        intent: Intent
    ) {
        try {
            remoteActivityHelper.startRemoteActivity(intent).await()
        } catch (cancellationException: CancellationException) {
            // Request was cancelled normally
            throw cancellationException
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }
}
