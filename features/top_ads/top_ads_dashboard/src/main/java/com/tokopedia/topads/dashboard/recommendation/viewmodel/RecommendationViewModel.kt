package com.tokopedia.topads.dashboard.recommendation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsGetShopInfoUiModel
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsGetShopInfoUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

class RecommendationViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val topAdsGetShopInfoUseCase: TopAdsGetShopInfoUseCase,
) : BaseViewModel(dispatcher.main) {

    init {
        getShopInfo()
    }

    private val _shopInfo =
        MutableLiveData<Result<TopAdsGetShopInfoUiModel>>()
    val shopInfo: LiveData<Result<TopAdsGetShopInfoUiModel>>
        get() = _shopInfo

    private fun getShopInfo() {
        launchCatchError(dispatcher.main, block = {
            _shopInfo.value = topAdsGetShopInfoUseCase(source = "test")
        }, onError = {
            _shopInfo.value = Fail(it)
        })
    }
}
