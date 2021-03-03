package com.tokopedia.shop.score.performance.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.score.common.domain.mapper.ShopScoreMapper
import com.tokopedia.shop.score.performance.presentation.model.ShopInfoLevelUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ShopPerformanceViewModel @Inject constructor(
        private val dispatchers: CoroutineDispatchers,
): BaseViewModel(dispatchers.main) {

    val shopInfoLevel: LiveData<Result<ShopInfoLevelUiModel>>
        get() = _shopInfoLevel

    private val _shopInfoLevel = MutableLiveData<Result<ShopInfoLevelUiModel>>()

    fun getShopInfoLevel(level: Int) {
        launchCatchError(block = {
            _shopInfoLevel.postValue(Success(ShopScoreMapper.mapToShoInfoLevelUiModel(level)))
        }, onError = {
            _shopInfoLevel.postValue(Fail(it))
        })
    }
}