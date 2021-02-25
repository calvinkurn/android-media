package com.tokopedia.shop.setting.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
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

class ShopPageSettingViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val getShopInfoUseCase: GQLGetShopInfoUseCase,
        private val authorizeAddressAccessUseCase: AuthorizeAccessUseCase,
        private val authorizeEtalaseAccessUseCase: AuthorizeAccessUseCase,
        private val authorizeNotesAccessUseCase: AuthorizeAccessUseCase,
        private val authorizeInfoAccessUseCase: AuthorizeAccessUseCase,
        private val authorizeShipmentAccessUseCase: AuthorizeAccessUseCase,
        private val authorizeProductManageAccessUseCase: AuthorizeAccessUseCase,
        private val dispatcherProvider: CoroutineDispatchers
) : BaseViewModel(dispatcherProvider.main) {

    val shopInfoResp = MutableLiveData<Result<ShopInfo>>()

    private val mShopSettingAccessLiveData = MutableLiveData<Result<ShopSettingAccess>>()
    val shopSettingAccessLiveData: LiveData<Result<ShopSettingAccess>>
        get() = mShopSettingAccessLiveData

    fun isMyShop(shopId: String) = userSessionInterface.shopId == shopId

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
                val addressRole = asyncCatchError(
                        dispatcherProvider.io,
                        block = { getSettingAddressAccess(id) },
                        onError = {
                            if (shopInfoResp.value !is Fail) {
                                mShopSettingAccessLiveData.postValue(Fail(it))
                                shopInfoResp.postValue(Fail(it))
                            }
                            null
                        })
                val etalaseRole = asyncCatchError(
                        dispatcherProvider.io,
                        block = { getSettingEtalaseAccess(id) },
                        onError = {
                            if (shopInfoResp.value !is Fail) {
                                mShopSettingAccessLiveData.postValue(Fail(it))
                                shopInfoResp.postValue(Fail(it))
                            }
                            null
                        })
                val notesRole = asyncCatchError(
                        dispatcherProvider.io,
                        block = { getSettingNotesAccess(id) },
                        onError = {
                            if (shopInfoResp.value !is Fail) {
                                mShopSettingAccessLiveData.postValue(Fail(it))
                                shopInfoResp.postValue(Fail(it))
                            }
                            null
                        })
                val infoRole = asyncCatchError(
                        dispatcherProvider.io,
                        block = { getSettingInfoAccess(id) },
                        onError = {
                            if (shopInfoResp.value !is Fail) {
                                mShopSettingAccessLiveData.postValue(Fail(it))
                                shopInfoResp.postValue(Fail(it))
                            }
                            null
                        })
                val shipmentRole = asyncCatchError(
                        dispatcherProvider.io,
                        block = { getSettingShipmentAccess(id) },
                        onError = {
                            if (shopInfoResp.value !is Fail) {
                                mShopSettingAccessLiveData.postValue(Fail(it))
                                shopInfoResp.postValue(Fail(it))
                            }
                            null
                        })
                val productManageRole = asyncCatchError(
                        dispatcherProvider.io,
                        block = { getSettingProductManageAccess(id) },
                        onError = {
                            if (shopInfoResp.value !is Fail) {
                                mShopSettingAccessLiveData.postValue(Fail(it))
                                shopInfoResp.postValue(Fail(it))
                            }
                            null
                        })
                addressRole.await()?.let { address ->
                    etalaseRole.await()?.let { etalase ->
                        notesRole.await()?.let { notes ->
                            infoRole.await()?.let { info ->
                                shipmentRole.await()?.let { shipment ->
                                    productManageRole.await()?.let { productManage ->
                                        mShopSettingAccessLiveData.postValue(Success(
                                                ShopSettingAccess(
                                                        isAddressAccessAuthorized = address,
                                                        isEtalaseAccessAuthorized = etalase,
                                                        isNotesAccessAuthorized = notes,
                                                        isInfoAccessAuthorized = info,
                                                        isShipmentAccessAuthorized = shipment,
                                                        isProductManageAccessAuthorized = productManage
                                                )
                                        ))
                                        shopInfo.await()?.let {
                                            shopInfoResp.value = Success(it)
                                        }
                                    }
                                }
                            }
                        }
                    }
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

    private suspend fun getSettingAddressAccess(shopId: Int): Boolean {
        val requestParams = AuthorizeAccessUseCase.createRequestParams(shopId, AccessId.SHOP_SETTING_ADDRESS)
        return authorizeAddressAccessUseCase.execute(requestParams)
    }

    private suspend fun getSettingEtalaseAccess(shopId: Int): Boolean {
        val requestParams = AuthorizeAccessUseCase.createRequestParams(shopId, AccessId.SHOP_SETTING_ETALASE)
        return authorizeEtalaseAccessUseCase.execute(requestParams)
    }

    private suspend fun getSettingNotesAccess(shopId: Int): Boolean {
        val requestParams = AuthorizeAccessUseCase.createRequestParams(shopId, AccessId.SHOP_SETTING_NOTES)
        return authorizeNotesAccessUseCase.execute(requestParams)
    }

    private suspend fun getSettingInfoAccess(shopId: Int): Boolean {
        val requestParams = AuthorizeAccessUseCase.createRequestParams(shopId, AccessId.SHOP_SETTING_INFO)
        return authorizeInfoAccessUseCase.execute(requestParams)
    }

    private suspend fun getSettingShipmentAccess(shopId: Int): Boolean {
        val requestParams = AuthorizeAccessUseCase.createRequestParams(shopId, AccessId.SHOP_SETTING_SHIPMENT)
        return authorizeShipmentAccessUseCase.execute(requestParams)
    }

    private suspend fun getSettingProductManageAccess(shopId: Int): Boolean {
        val requestParams = AuthorizeAccessUseCase.createRequestParams(shopId, AccessId.PRODUCT_LIST)
        return authorizeProductManageAccessUseCase.execute(requestParams)
    }
}