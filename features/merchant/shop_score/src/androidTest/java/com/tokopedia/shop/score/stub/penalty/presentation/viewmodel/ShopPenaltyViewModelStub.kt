package com.tokopedia.shop.score.stub.penalty.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.score.penalty.domain.old.usecase.GetShopPenaltyDetailMergeUseCaseOld
import com.tokopedia.shop.score.penalty.domain.old.usecase.GetShopPenaltyDetailUseCaseOld
import com.tokopedia.shop.score.penalty.presentation.viewmodel.old.ShopPenaltyViewModelOld
import com.tokopedia.shop.score.stub.penalty.domain.mapper.PenaltyMapperOldStub
import dagger.Lazy

class ShopPenaltyViewModelStub(
    dispatchers: CoroutineDispatchers,
    getShopPenaltyDetailMergeUseCaseOld: Lazy<GetShopPenaltyDetailMergeUseCaseOld>,
    getShopPenaltyDetailUseCaseOld: Lazy<GetShopPenaltyDetailUseCaseOld>,
    penaltyMapper: PenaltyMapperOldStub
) : ShopPenaltyViewModelOld(
    dispatchers,
    getShopPenaltyDetailMergeUseCaseOld,
    getShopPenaltyDetailUseCaseOld,
    penaltyMapper
)
