package com.tokopedia.shop.showcase.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Rafli Syam on 05/03/2021
 */
class ShopPageShowcaseViewModel @Inject constructor(
        private val userSession: UserSessionInterface,
        private val dispatcherProvider: CoroutineDispatchers,
        private val getShopEtalaseByShopUseCase: GetShopEtalaseByShopUseCase
) : BaseViewModel(dispatcherProvider.main) {


    val etalaseList: LiveData<Result<List<ShopEtalaseModel>>>
        get() = _etalaseList
    private val _etalaseList = MutableLiveData<Result<List<ShopEtalaseModel>>>()

    fun getShowcaseList(shopId: String) {
        launchCatchError(block = {
            withContext(dispatcherProvider.io) {

                getShopEtalaseByShopUseCase.clearCache()
                getShopEtalaseByShopUseCase.isFromCacheFirst = false

                val requestParams = GetShopEtalaseByShopUseCase.createRequestParams(
                        shopId = shopId,
                        hideNoCount = GetShopEtalaseByShopUseCase.Companion.BuyerQueryParam.HIDE_NO_COUNT_VALUE,
                        hideShowCaseGroup = false,
                        isOwner = ShopUtil.isMyShop(shopId, userSession.shopId)
                )

                val showcaseList = getShopEtalaseByShopUseCase.createObservable(requestParams).toBlocking().first()
                _etalaseList.postValue(Success(showcaseList))

            }
        }) {
            _etalaseList.postValue(Fail(it))
        }
    }

}