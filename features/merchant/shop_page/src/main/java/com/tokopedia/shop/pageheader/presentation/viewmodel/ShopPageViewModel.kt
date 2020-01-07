package com.tokopedia.shop.pageheader.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopPageViewModel @Inject constructor(
        private val shopInfoUseCase: GQLGetShopInfoUseCase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    val shopInfoResult: LiveData<Result<ShopInfo>>
        get() = shopInfoResponse

    private val shopInfoResponse = MutableLiveData<Result<ShopInfo>>()

    fun fetchData() {
        launch { getShopInfoData(listOf(2642798)) }
    }

    private fun getShopInfoData(shopIds: List<Int>) {
        shopInfoUseCase.params = GQLGetShopInfoUseCase.createParams(shopIds)
        shopInfoUseCase.execute(
                { shopInfo -> shopInfoResponse.value = Success(shopInfo) },
                { throwable -> shopInfoResponse.value = Fail(throwable) }
        )
    }
}
