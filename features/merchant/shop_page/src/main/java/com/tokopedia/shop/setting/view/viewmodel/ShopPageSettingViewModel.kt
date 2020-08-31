package com.tokopedia.shop.setting.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.feedcomponent.domain.usecase.GetWhitelistUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopReputationUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import javax.inject.Inject

class ShopPageSettingViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val getShopInfoUseCase: GQLGetShopInfoUseCase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    val shopInfoResp = MutableLiveData<Result<ShopInfo>>()

    fun isMyShop(shopId: String) = userSessionInterface.shopId == shopId

    fun getShop(shopId: String? = null, shopDomain: String? = null, isRefresh: Boolean = false) {
        val id = shopId?.toIntOrNull() ?: 0
        if (id == 0 && shopDomain == null) return
        launchCatchError(block = {
            val shopInfo = withContext(Dispatchers.IO) {
                getShopInfo(id, shopDomain, isRefresh)
            }
            shopInfoResp.postValue(Success(shopInfo))
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
}