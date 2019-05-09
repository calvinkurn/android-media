package com.tokopedia.shop.page.view

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopPageViewModel @Inject constructor(private val userSessionInterface: UserSessionInterface,
                                            private val getshopInfoUseCase: GQLGetShopInfoUseCase,
                                            dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher){

    fun isMyShop(shopId: String) = userSessionInterface.shopId == shopId

    val shopInfoResp = MutableLiveData<Result<ShopInfo>>()

    fun getShop(shopId: String? = null, shopDomain: String? = null, isRefresh: Boolean = false){
        val id = shopId?.toIntOrNull() ?: 0
        if (id == 0 && shopDomain == null) return
        launchCatchError(block = {
            getshopInfoUseCase.params = GQLGetShopInfoUseCase
                    .createParams(if (id == 0)listOf() else listOf(id), shopDomain)
            getshopInfoUseCase.isFromCacheFirst = !isRefresh
            shopInfoResp.value = Success(withContext(Dispatchers.IO){getshopInfoUseCase.executeOnBackground()})
        }){
            shopInfoResp.value = Fail(it)
        }
    }
}