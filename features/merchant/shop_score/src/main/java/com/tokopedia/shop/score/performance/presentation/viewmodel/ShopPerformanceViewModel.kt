package com.tokopedia.shop.score.performance.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.score.common.domain.mapper.ShopScoreMapper
import com.tokopedia.shop.score.performance.presentation.model.ShopInfoLevelUiModel
import com.tokopedia.shop.score.performance.presentation.model.ShopPerformanceDetailUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopPerformanceViewModel @Inject constructor(
        private val dispatchers: CoroutineDispatchers,
) : BaseViewModel(dispatchers.main) {

    val shopInfoLevel: LiveData<Result<ShopInfoLevelUiModel>>
        get() = _shopInfoLevel

    val shopPerformanceDetail: LiveData<ShopPerformanceDetailUiModel>
        get() = _shopPerformanceDetail


    private val _shopInfoLevel = MutableLiveData<Result<ShopInfoLevelUiModel>>()
    private val _shopPerformanceDetail = MutableLiveData<ShopPerformanceDetailUiModel>()

    fun getShopPerformanceDetail(titlePerformance: String) {
        launch {
            _shopPerformanceDetail.value = ShopScoreMapper.mapToShopPerformanceDetail(titlePerformance)
        }
    }

    fun getShopInfoLevel(level: Int) {
        launchCatchError(block = {
            _shopInfoLevel.postValue(Success(ShopScoreMapper.mapToShoInfoLevelUiModel(level)))
        }, onError = {
            _shopInfoLevel.postValue(Fail(it))
        })
    }
}