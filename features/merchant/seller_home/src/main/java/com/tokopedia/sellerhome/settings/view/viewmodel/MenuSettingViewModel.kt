package com.tokopedia.sellerhome.settings.view.viewmodel

import androidx.lifecycle.*
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerhome.settings.view.uimodel.menusetting.MenuSettingAccess
import com.tokopedia.shop.common.constant.AccessId
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MenuSettingViewModel @Inject constructor(
        private val authorizeAddressAccessUseCase: AuthorizeAccessUseCase,
        private val authorizeInfoAccessUseCase: AuthorizeAccessUseCase,
        private val authorizeNotesAccessUseCase: AuthorizeAccessUseCase,
        private val authorizeShipmentAccessUseCase: AuthorizeAccessUseCase,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main), LifecycleObserver {

    private val mShopSettingAccessLiveData = MutableLiveData<Result<MenuSettingAccess>>()
    val shopSettingAccessLiveData: LiveData<Result<MenuSettingAccess>>
        get() = mShopSettingAccessLiveData

    fun checkShopSettingAccess() {
        launchCatchError(
                block = {
                    if (userSession.isShopOwner) {
                        mShopSettingAccessLiveData.value = Success(MenuSettingAccess())
                    } else {
                        withContext(dispatchers.io) {
                            userSession.shopId.toIntOrZero().let { shopId ->
                                val addressRole = async { getAddressAccessRole(shopId) }
                                val infoRole = async { getInfoAccessRole(shopId) }
                                val notesRole = async { getNotesAccessRole(shopId) }
                                val shipmentRole = async { getShipmentAccessRole(shopId) }

                                mShopSettingAccessLiveData.postValue(Success(MenuSettingAccess(
                                        isAddressAccessAuthorized = addressRole.await(),
                                        isInfoAccessAuthorized = infoRole.await(),
                                        isNotesAccessAuthorized = notesRole.await(),
                                        isShipmentAccessAuthorized = shipmentRole.await())))
                            }
                        }
                    }
                },
                onError = {
                    mShopSettingAccessLiveData.value = Fail(it)
                }
        )
    }

    private suspend fun getAddressAccessRole(shopId: Int): Boolean {
        AuthorizeAccessUseCase.createRequestParams(shopId, AccessId.SHOP_SETTING_ADDRESS).let { requestParams ->
            return authorizeAddressAccessUseCase.execute(requestParams)
        }
    }

    private suspend fun getInfoAccessRole(shopId: Int): Boolean {
        AuthorizeAccessUseCase.createRequestParams(shopId, AccessId.SHOP_SETTING_INFO).let { requestParams ->
            return authorizeInfoAccessUseCase.execute(requestParams)
        }
    }

    private suspend fun getNotesAccessRole(shopId: Int): Boolean {
        AuthorizeAccessUseCase.createRequestParams(shopId, AccessId.SHOP_SETTING_NOTES).let { requestParams ->
            return authorizeNotesAccessUseCase.execute(requestParams)
        }
    }

    private suspend fun getShipmentAccessRole(shopId: Int): Boolean {
        AuthorizeAccessUseCase.createRequestParams(shopId, AccessId.SHOP_SETTING_SHIPMENT).let { requestParams ->
            return authorizeShipmentAccessUseCase.execute(requestParams)
        }
    }

}