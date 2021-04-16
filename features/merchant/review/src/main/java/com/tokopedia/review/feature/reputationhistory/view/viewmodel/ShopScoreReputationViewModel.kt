package com.tokopedia.review.feature.reputationhistory.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.domain.interactor.GetShopInfoPeriodUseCase
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopScoreReputationViewModel @Inject constructor(
        private val dispatchers: CoroutineDispatchers,
        private val getShopInfoPeriodUseCase: GetShopInfoPeriodUseCase
): BaseViewModel(dispatchers.main) {

    private val _shopPeriod = MutableLiveData<Result<ShopInfoPeriodUiModel>>()
    val shopPeriod: LiveData<Result<ShopInfoPeriodUiModel>>
        get() = _shopPeriod

    fun getShopScorePeriod(shopId: Int) {
        launchCatchError(block = {
            val periodData = withContext(dispatchers.io) {
                getShopInfoPeriodUseCase.requestParams = GetShopInfoPeriodUseCase.createParams(shopId)
                getShopInfoPeriodUseCase.executeOnBackground()
            }
            _shopPeriod.postValue(Success(periodData))
        }, onError = {
            _shopPeriod.postValue(Fail(it))
        })
    }
}