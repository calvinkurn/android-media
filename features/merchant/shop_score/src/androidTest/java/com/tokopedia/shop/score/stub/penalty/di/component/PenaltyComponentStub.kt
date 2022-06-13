package com.tokopedia.shop.score.stub.penalty.di.component

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.score.penalty.di.component.PenaltyComponent
import com.tokopedia.shop.score.penalty.di.scope.PenaltyScope
import com.tokopedia.shop.score.penalty.domain.mapper.PenaltyMapper
import com.tokopedia.shop.score.penalty.domain.usecase.GetShopPenaltyDetailMergeUseCase
import com.tokopedia.shop.score.penalty.domain.usecase.GetShopPenaltyDetailUseCase
import com.tokopedia.shop.score.stub.common.di.component.BaseAppComponentStub
import com.tokopedia.shop.score.stub.penalty.di.module.PenaltyModuleStub
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

@PenaltyScope
@Component(
    modules = [PenaltyModuleStub::class],
    dependencies = [BaseAppComponentStub::class]
)
interface PenaltyComponentStub: PenaltyComponent {

    fun coroutineDispatcher(): CoroutineDispatchers

    fun graphQlRepository(): GraphqlRepository

    fun userSessionInterface(): UserSessionInterface

    fun penaltyMapperStub(): PenaltyMapper

    fun getShopPenaltyDetailMergeUseCaseStub(): GetShopPenaltyDetailMergeUseCase

    fun getShopPenaltyDetailUseCaseStub(): GetShopPenaltyDetailUseCase
}