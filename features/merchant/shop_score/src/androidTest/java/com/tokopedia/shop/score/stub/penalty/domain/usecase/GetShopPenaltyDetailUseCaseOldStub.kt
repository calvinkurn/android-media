package com.tokopedia.shop.score.stub.penalty.domain.usecase

import com.tokopedia.shop.score.penalty.domain.old.usecase.GetShopPenaltyDetailUseCaseOld
import com.tokopedia.shop.score.stub.common.graphql.repository.GraphqlRepositoryStub

class GetShopPenaltyDetailUseCaseOldStub(
    graphqlRepositoryStub: GraphqlRepositoryStub
): GetShopPenaltyDetailUseCaseOld(graphqlRepositoryStub)
