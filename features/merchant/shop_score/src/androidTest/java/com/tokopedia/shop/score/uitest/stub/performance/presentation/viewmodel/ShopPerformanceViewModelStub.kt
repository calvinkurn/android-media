package com.tokopedia.shop.score.uitest.stub.performance.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.domain.interactor.GetShopInfoPeriodUseCase
import com.tokopedia.shop.score.performance.domain.mapper.ShopScoreMapper
import com.tokopedia.shop.score.performance.domain.usecase.GetShopPerformanceUseCase
import com.tokopedia.shop.score.performance.presentation.viewmodel.ShopPerformanceViewModel
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import javax.inject.Inject

class ShopPerformanceViewModelStub @Inject constructor(
    dispatchers: CoroutineDispatchers,
    shopScoreMapper: ShopScoreMapper,
    userSession: UserSessionInterface,
    getShopInfoPeriodUseCase: Lazy<GetShopInfoPeriodUseCase>,
    getShopPerformanceUseCase: Lazy<GetShopPerformanceUseCase>
) : ShopPerformanceViewModel(
    dispatchers,
    shopScoreMapper,
    userSession,
    getShopInfoPeriodUseCase,
    getShopPerformanceUseCase
)