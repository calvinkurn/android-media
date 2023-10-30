package com.tokopedia.shop.score.stub.penalty.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.score.penalty.domain.usecase.GetNotYetDeductedPenaltyUseCase
import com.tokopedia.shop.score.penalty.domain.usecase.GetShopPenaltyDetailMergeUseCase
import com.tokopedia.shop.score.penalty.domain.usecase.GetShopPenaltyDetailUseCase
import com.tokopedia.shop.score.penalty.domain.usecase.ShopPenaltyTickerUseCase
import com.tokopedia.shop.score.penalty.presentation.viewmodel.ShopPenaltyViewModel
import com.tokopedia.shop.score.stub.penalty.domain.mapper.PenaltyMapperStub
import dagger.Lazy

class ShopPenaltyViewModelStub(
    dispatchers: CoroutineDispatchers,
    getShopPenaltyDetailMergeUseCase: Lazy<GetShopPenaltyDetailMergeUseCase>,
    getShopPenaltyDetailUseCase: Lazy<GetShopPenaltyDetailUseCase>,
    getNotYetDeductedPenaltyUseCase: Lazy<GetNotYetDeductedPenaltyUseCase>,
    shopPenaltyTickerUseCase: Lazy<ShopPenaltyTickerUseCase>,
    penaltyMapper: PenaltyMapperStub
) : ShopPenaltyViewModel(
    dispatchers,
    getShopPenaltyDetailMergeUseCase,
    getShopPenaltyDetailUseCase,
    getNotYetDeductedPenaltyUseCase,
    shopPenaltyTickerUseCase,
    penaltyMapper
)
