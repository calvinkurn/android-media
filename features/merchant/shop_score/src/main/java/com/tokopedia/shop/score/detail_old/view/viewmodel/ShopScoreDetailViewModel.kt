package com.tokopedia.shop.score.detail_old.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.domain.interactor.GetShopInfoPeriodUseCase
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.score.detail_old.domain.usecase.GetShopScoreUseCase
import com.tokopedia.shop.score.detail_old.view.mapper.ShopScoreDetailMapper
import com.tokopedia.shop.score.detail_old.view.model.ShopScoreDetailData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopScoreDetailViewModel @Inject constructor(
        private val getShopScoreUseCase: GetShopScoreUseCase,
        private val getShopInfoPeriodUseCase: GetShopInfoPeriodUseCase,
        val userSession: UserSessionInterface,
        private val mapper: ShopScoreDetailMapper,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    val shopScoreData: LiveData<Result<Pair<ShopScoreDetailData, ShopInfoPeriodUiModel>>>
        get() = _shopScoreData

    private val _shopScoreData = MutableLiveData<Result<Pair<ShopScoreDetailData, ShopInfoPeriodUiModel>>>()

    fun getShopScoreDetail() {
        launchCatchError(block = {
            val shopScoreDetailData = async {
                getShopScoreUseCase.execute(userSession.shopId)
            }
            val shopInfoPeriodData = async {
                getShopInfoPeriodUseCase.requestParams = GetShopInfoPeriodUseCase.createParams(userSession.shopId.toLong())
                getShopInfoPeriodUseCase.executeOnBackground()
            }
            _shopScoreData.postValue(Success(Pair(mapper.mapToShopScoreDetailData(shopScoreDetailData.await().result),
                    shopInfoPeriodData.await())))
        }) {
            _shopScoreData.postValue(Fail(it))
        }
    }
}