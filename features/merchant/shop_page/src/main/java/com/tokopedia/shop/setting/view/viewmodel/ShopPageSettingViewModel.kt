package com.tokopedia.shop.setting.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.common.constant.AccessId
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.setting.view.model.ShopSettingAccess
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Provider

class ShopPageSettingViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val getShopInfoUseCase: GQLGetShopInfoUseCase,
        private val authorizeAccessUseCase: Provider<AuthorizeAccessUseCase>,
        private val dispatcherProvider: CoroutineDispatchers
) : BaseViewModel(dispatcherProvider.main) {

    val shopInfoResp = MutableLiveData<Result<ShopInfo>>()

    private val mShopSettingAccessLiveData = MutableLiveData<Result<ShopSettingAccess>>()
    val shopSettingAccessLiveData: LiveData<Result<ShopSettingAccess>>
        get() = mShopSettingAccessLiveData

    fun isMyShop(shopId: String) = userSessionInterface.shopId == shopId

    private val adminAccessList by lazy {
        listOf(
                AccessId.SHOP_SETTING_ADDRESS, AccessId.SHOP_SETTING_ETALASE, AccessId.SHOP_SETTING_NOTES,
                AccessId.SHOP_SETTING_INFO, AccessId.SHOP_SETTING_SHIPMENT, AccessId.PRODUCT_LIST
        )
    }

    fun getShop(shopId: String? = null, shopDomain: String? = null, isRefresh: Boolean = false) {
        val id = shopId?.toIntOrNull() ?: 0
        if (id == 0 && shopDomain == null) return
        launchCatchError(block = {
            val shopInfo = asyncCatchError(
                    dispatcherProvider.io,
                    block = {
                        getShopInfo(id, shopDomain, isRefresh)
                    },
                    onError = {
                        shopInfoResp.value = Fail(it)
                        null
                    })

            // Check for access role
            if (userSessionInterface.isShopOwner) {
                mShopSettingAccessLiveData.value = Success(ShopSettingAccess())
                shopInfo.await()?.let {
                    shopInfoResp.value = Success(it)
                }
            } else {
                val adminAccessEligibilityMap = adminAccessList.associateBy(
                        { it },
                        { accessId ->
                            asyncCatchError(
                                    dispatcherProvider.io,
                                    block = {
                                        getSettingAccess(shopId.toLongOrZero(), accessId, authorizeAccessUseCase.get())
                                    },
                                    onError = {
                                        mShopSettingAccessLiveData.postValue(Fail(it))
                                        shopInfoResp.postValue(Fail(it))
                                        null
                                    })
                        }
                )

                val adminAccessMap: Map<Int, Boolean> =
                        adminAccessEligibilityMap.mapValues {
                            it.value.await().let { eligibility ->
                                eligibility ?: return@launchCatchError }
                        }
                mShopSettingAccessLiveData.postValue(Success(
                        ShopSettingAccess(
                                isAddressAccessAuthorized = adminAccessMap[AccessId.SHOP_SETTING_ADDRESS] ?: false,
                                isEtalaseAccessAuthorized = adminAccessMap[AccessId.SHOP_SETTING_ETALASE] ?: false,
                                isNotesAccessAuthorized = adminAccessMap[AccessId.SHOP_SETTING_NOTES] ?: false,
                                isInfoAccessAuthorized = adminAccessMap[AccessId.SHOP_SETTING_INFO] ?: false,
                                isShipmentAccessAuthorized = adminAccessMap[AccessId.SHOP_SETTING_SHIPMENT] ?: false,
                                isProductManageAccessAuthorized = adminAccessMap[AccessId.PRODUCT_LIST] ?: false
                        )
                ))
                shopInfo.await()?.let {
                    shopInfoResp.value = Success(it)
                }
            }
        }) {
            shopInfoResp.value = Fail(it)
        }
    }

    private suspend fun getShopInfo(shopId: Int, shopDomain: String?, isRefresh: Boolean): ShopInfo {
        getShopInfoUseCase.params = GQLGetShopInfoUseCase
                .createParams(if (shopId == 0) listOf() else listOf(shopId), shopDomain)
        getShopInfoUseCase.isFromCacheFirst = !isRefresh
        return getShopInfoUseCase.executeOnBackground()
    }

    private suspend fun getSettingAccess(shopId: Long, @AccessId accessId: Int, useCase: AuthorizeAccessUseCase): Boolean {
        val requestParams = AuthorizeAccessUseCase.createRequestParams(shopId, accessId)
        return useCase.execute(requestParams)
    }

}