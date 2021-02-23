package com.tokopedia.shop.setting.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.common.constant.AccessId
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import javax.inject.Inject

class ShopPageSettingViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val getShopInfoUseCase: GQLGetShopInfoUseCase,
        private val authorizeAccessUseCase: AuthorizeAccessUseCase,
        private val dispatcherProvider: CoroutineDispatchers
) : BaseViewModel(dispatcherProvider.main) {

    val shopInfoResp = MutableLiveData<Result<ShopInfo>>()

    private val mShopSettingAccessLiveData = MutableLiveData<Result<Boolean>>()
    val shopSettingAccessLiveData: LiveData<Result<Boolean>>
        get() = mShopSettingAccessLiveData

    fun isMyShop(shopId: String) = userSessionInterface.shopId == shopId

    fun getShop(shopId: String? = null, shopDomain: String? = null, isRefresh: Boolean = false) {
        val id = shopId?.toIntOrNull() ?: 0
        if (id == 0 && shopDomain == null) return
        launchCatchError(block = {
            val shopInfo = withContext(dispatcherProvider.io) {
                getShopInfo(id, shopDomain, isRefresh)
            }
            shopInfoResp.postValue(Success(shopInfo))
        }) {
            shopInfoResp.value = Fail(it)
        }
    }

    fun checkAccess() {
        launchCatchError(block = {
            val shopId = userSessionInterface.shopId.toIntOrZero()
            val isShopSettingRoleEligible = withContext(dispatcherProvider.io) {
                getRoleAccess(shopId)
            }
            mShopSettingAccessLiveData.postValue(Success(isShopSettingRoleEligible))
        }) {
            mShopSettingAccessLiveData.value = Fail(it)
        }

    }

    private suspend fun getShopInfo(shopId: Int, shopDomain: String?, isRefresh: Boolean): ShopInfo {
        getShopInfoUseCase.params = GQLGetShopInfoUseCase
                .createParams(if (shopId == 0) listOf() else listOf(shopId), shopDomain)
        getShopInfoUseCase.isFromCacheFirst = !isRefresh
        return getShopInfoUseCase.executeOnBackground()
    }

    private suspend fun getRoleAccess(shopId: Int): Boolean {
        val requestParams = AuthorizeAccessUseCase.createRequestParams(shopId, AccessId.SHOP_SETTING)
        return authorizeAccessUseCase.execute(requestParams)
    }
}