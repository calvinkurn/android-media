package com.tokopedia.shop.score.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.score.domain.usecase.GetShopScoreUseCase
import com.tokopedia.shop.score.view.mapper.ShopScoreDetailMapper
import com.tokopedia.shop.score.view.model.ShopScoreDetailData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopScoreDetailViewModel @Inject constructor(
    private val getShopScoreUseCase: GetShopScoreUseCase,
    private val userSession: UserSessionInterface,
    private val mapper: ShopScoreDetailMapper,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main){

    val shopScoreData: LiveData<Result<ShopScoreDetailData>>
        get() = _shopScoreData

    private val _shopScoreData = MutableLiveData<Result<ShopScoreDetailData>>()

    fun getShopScoreDetail() {
        launchCatchError(block = {
            val data = withContext(dispatchers.io) {
                val response = getShopScoreUseCase.execute(userSession.shopId)
                mapper.mapToShopScoreDetailData(response.result)
            }

            _shopScoreData.value = Success(data)
        }) {
            _shopScoreData.value = Fail(it)
        }
    }
}