package com.tokopedia.shop.score.performance.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.score.common.domain.mapper.ShopScoreMapper
import com.tokopedia.shop.score.performance.presentation.model.BaseShopPerformance
import com.tokopedia.shop.score.performance.presentation.model.ShopInfoLevelUiModel
import com.tokopedia.shop.score.performance.presentation.model.ShopPerformanceDetailUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopPerformanceViewModel @Inject constructor(
        private val dispatchers: CoroutineDispatchers,
        private val shopScoreMapper: ShopScoreMapper,
        private val userSession: UserSessionInterface
) : BaseViewModel(dispatchers.main) {

    val shopInfoLevel: LiveData<Result<ShopInfoLevelUiModel>>
        get() = _shopInfoLevel

    val shopPerformanceDetail: LiveData<ShopPerformanceDetailUiModel>
        get() = _shopPerformanceDetail

    val shopPerformancePage: LiveData<Result<List<BaseShopPerformance>>>
        get() = _shopPerformancePage

    private val _shopPerformancePage = MutableLiveData<Result<List<BaseShopPerformance>>>()

    private val _shopInfoLevel = MutableLiveData<Result<ShopInfoLevelUiModel>>()
    private val _shopPerformanceDetail = MutableLiveData<ShopPerformanceDetailUiModel>()

    fun getShopPerformancePageDummy() {
        launch {
            _shopPerformancePage.value = Success(shopScoreMapper.mapToShopPerformanceVisitableDummy())
        }
    }

    fun getShopPerformanceDetail(titlePerformance: String) {
        launch {
            _shopPerformanceDetail.value = shopScoreMapper.mapToShopPerformanceDetail(titlePerformance)
        }
    }

    fun getShopInfoLevel(level: Int) {
        launchCatchError(block = {
            //temporary dummy
            _shopInfoLevel.value = Success(shopScoreMapper.mapToShoInfoLevelUiModel(level))
        }, onError = {
            _shopInfoLevel.postValue(Fail(it))
        })
    }
}