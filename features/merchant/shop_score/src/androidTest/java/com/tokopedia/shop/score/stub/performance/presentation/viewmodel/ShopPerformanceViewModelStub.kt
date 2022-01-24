package com.tokopedia.shop.score.stub.performance.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.domain.interactor.GetShopInfoPeriodUseCase
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.score.performance.domain.mapper.ShopScoreMapper
import com.tokopedia.shop.score.performance.domain.usecase.GetShopPerformanceUseCase
import com.tokopedia.shop.score.performance.presentation.viewmodel.ShopPerformanceViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import javax.inject.Inject

class ShopPerformanceViewModelStub @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val shopScoreMapper: ShopScoreMapper,
    userSession: UserSessionInterface,
    getShopInfoPeriodUseCase: Lazy<GetShopInfoPeriodUseCase>,
    private val getShopPerformanceUseCase: Lazy<GetShopPerformanceUseCase>
) : ShopPerformanceViewModel(
    dispatchers,
    shopScoreMapper,
    userSession,
    getShopInfoPeriodUseCase,
    getShopPerformanceUseCase
) {
    override fun getShopScoreLevel(shopInfoPeriodUiModel: ShopInfoPeriodUiModel) {
        launchCatchError(block = {
            val shopScoreLevelResponse = getShopPerformanceUseCase.get().executeOnBackground()
            val shopScoreLevelData = shopScoreMapper.mapToShopPerformanceVisitable(
                shopScoreLevelResponse,
                shopInfoPeriodUiModel
            )
            _shopPerformancePage.value = Success(
                Pair(
                    shopScoreLevelData,
                    shopScoreLevelResponse
                )
            )
        }, onError = {
            _shopPerformancePage.value = Fail(it)
        })
    }
}