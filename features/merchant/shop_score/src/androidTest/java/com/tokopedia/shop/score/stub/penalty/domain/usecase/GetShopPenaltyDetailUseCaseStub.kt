package com.tokopedia.shop.score.stub.penalty.domain.usecase

import com.tokopedia.shop.score.penalty.domain.usecase.GetShopPenaltyDetailUseCase
import com.tokopedia.shop.score.stub.common.graphql.repository.GraphqlRepositoryStub

class GetShopPenaltyDetailUseCaseStub(
    graphqlRepositoryStub: GraphqlRepositoryStub
): GetShopPenaltyDetailUseCase(graphqlRepositoryStub)