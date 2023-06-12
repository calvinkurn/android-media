package com.tokopedia.sellerhome.settings.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.logisticCommon.domain.usecase.ShopMultilocWhitelistUseCase
import com.tokopedia.sellerhome.settings.view.uimodel.menusetting.MenuSettingAccess
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.shopadmin.common.util.AccessId
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class MenuSettingViewModel @Inject constructor(
    private val authorizeAccessUseCase: Provider<AuthorizeAccessUseCase>,
    private val shopLocWhitelist: Provider<ShopMultilocWhitelistUseCase>,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val mShopSettingAccessLiveData = MutableLiveData<Result<MenuSettingAccess>>()
    val shopSettingAccessLiveData: LiveData<Result<MenuSettingAccess>>
        get() = mShopSettingAccessLiveData


    val shopLocEligible: LiveData<Result<Boolean>>
        get() = _shopLocEligible

    private val _shopLocEligible = MutableLiveData<Result<Boolean>>()

    private val adminAccessList by lazy {
        listOf(
            AccessId.SHOP_SETTING_ADDRESS, AccessId.SHOP_SETTING_INFO,
            AccessId.SHOP_SETTING_NOTES, AccessId.SHOP_SETTING_SHIPMENT
        )
    }

    fun checkShopSettingAccess() {
        launch {
            if (userSession.isShopOwner) {
                mShopSettingAccessLiveData.value = Success(MenuSettingAccess())
            } else {
                userSession.shopId.toLongOrZero().let { shopId ->
                    val adminAccessEligibilityMap = adminAccessList.associateBy(
                        { it },
                        { accessId ->
                            asyncCatchError(
                                dispatchers.io,
                                block = {
                                    getSettingAccess(
                                        shopId,
                                        accessId,
                                        authorizeAccessUseCase.get()
                                    )
                                },
                                onError = {
                                    mShopSettingAccessLiveData.postValue(Fail(it))
                                    null
                                })
                        }
                    )

                    val adminAccessMap: Map<Int, Boolean> =
                        adminAccessEligibilityMap.mapValues {
                            it.value.await().let { eligibility ->
                                eligibility ?: return@launch
                            }
                        }

                    mShopSettingAccessLiveData.value =
                        Success(
                            MenuSettingAccess(
                                isAddressAccessAuthorized = adminAccessMap[AccessId.SHOP_SETTING_ADDRESS].orFalse(),
                                isInfoAccessAuthorized = adminAccessMap[AccessId.SHOP_SETTING_INFO].orFalse(),
                                isNotesAccessAuthorized = adminAccessMap[AccessId.SHOP_SETTING_NOTES].orFalse(),
                                isShipmentAccessAuthorized = adminAccessMap[AccessId.SHOP_SETTING_SHIPMENT].orFalse()
                            )
                        )

                }
            }
        }
    }

    fun getShopLocEligible(shopId: Long) {
        launchCatchError(block = {
            val shopLocWhitelist = shopLocWhitelist.get().invoke(shopId).keroGetRolloutEligibility
            val eligibilityState = shopLocWhitelist.data.eligibilityState
            val isMultilocation = eligibilityState==1
            _shopLocEligible.postValue(Success(isMultilocation))
        }, onError = {
            _shopLocEligible.postValue(Fail(it))
        })
    }

    private suspend fun getSettingAccess(
        shopId: Long,
        @com.tokopedia.shopadmin.common.util.AccessId accessId: Int,
        useCase: AuthorizeAccessUseCase
    ): Boolean {
        val requestParams = AuthorizeAccessUseCase.createRequestParams(shopId, accessId)
        return useCase.execute(requestParams)
    }

}
