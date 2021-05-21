package com.tokopedia.shop.score.performance.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.domain.interactor.GetShopInfoPeriodUseCase
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.score.performance.domain.mapper.ShopScoreMapper
import com.tokopedia.shop.score.performance.domain.model.ShopLevelTooltipParam
import com.tokopedia.shop.score.performance.domain.model.ShopScoreLevelParam
import com.tokopedia.shop.score.performance.domain.model.ShopScoreWrapperResponse
import com.tokopedia.shop.score.performance.domain.usecase.GetShopPerformanceUseCase
import com.tokopedia.shop.score.performance.presentation.model.BaseShopPerformance
import com.tokopedia.shop.score.performance.presentation.model.ShopInfoLevelUiModel
import com.tokopedia.shop.score.performance.presentation.model.ShopPerformanceDetailUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopPerformanceViewModel @Inject constructor(
        private val dispatchers: CoroutineDispatchers,
        private val shopScoreMapper: ShopScoreMapper,
        val userSession: UserSessionInterface,
        private val shopInfoPeriodUseCase: GetShopInfoPeriodUseCase,
        private val getShopPerformanceUseCase: GetShopPerformanceUseCase
) : BaseViewModel(dispatchers.main) {

    val shopInfoLevel: LiveData<ShopInfoLevelUiModel>
        get() = _shopInfoLevel

    val shopPerformanceDetail: LiveData<ShopPerformanceDetailUiModel>
        get() = _shopPerformanceDetail

    val shopPerformancePage: LiveData<Result<Pair<List<BaseShopPerformance>, ShopScoreWrapperResponse>>>
        get() = _shopPerformancePage

    val shopInfoPeriod: LiveData<Result<ShopInfoPeriodUiModel>>
        get() = _shopInfoPeriod

    private val _shopPerformancePage = MutableLiveData<Result<Pair<List<BaseShopPerformance>, ShopScoreWrapperResponse>>>()

    private val _shopInfoLevel = MutableLiveData<ShopInfoLevelUiModel>()
    private val _shopPerformanceDetail = MutableLiveData<ShopPerformanceDetailUiModel>()

    private val _shopInfoPeriod = MutableLiveData<Result<ShopInfoPeriodUiModel>>()

    fun getShopInfoPeriod() {
        launchCatchError(block = {
            val dataShopInfo = withContext(dispatchers.io) {
                shopInfoPeriodUseCase.requestParams = GetShopInfoPeriodUseCase.createParams(userSession.shopId.toIntOrZero())
                shopInfoPeriodUseCase.executeOnBackground()
            }
            _shopInfoPeriod.postValue(Success(dataShopInfo))
        }, onError = {
            _shopInfoPeriod.postValue(Fail(it))
        })
    }

    fun getShopScoreLevel(shopInfoPeriodUiModel: ShopInfoPeriodUiModel) {
        launchCatchError(block = {
            val shopScoreLevelResponse = withContext(dispatchers.io) {
                getShopPerformanceUseCase.requestParams = GetShopPerformanceUseCase.createParams(
                        userSession.shopId.toIntOrZero(), ShopScoreLevelParam(shopID = userSession.shopId),
                        ShopLevelTooltipParam(shopID = userSession.shopId)
                )
                getShopPerformanceUseCase.executeOnBackground()
            }
            val shopScoreLevelData = shopScoreMapper.mapToShopPerformanceVisitable(shopScoreLevelResponse, shopInfoPeriodUiModel)
            _shopPerformancePage.postValue(Success(Pair(shopScoreLevelData, shopScoreLevelResponse)))
        }, onError = {
            _shopPerformancePage.postValue(Fail(it))
        })
    }

    fun getShopPerformanceDetail(identifierPerformance: String) {
        launch {
            _shopPerformanceDetail.value = shopScoreMapper.mapToShopPerformanceDetail(identifierPerformance)
        }
    }

    fun getShopInfoLevel(level: Int) {
        launch {
            _shopInfoLevel.value = shopScoreMapper.mapToShopInfoLevelUiModel(level)
        }
    }
}