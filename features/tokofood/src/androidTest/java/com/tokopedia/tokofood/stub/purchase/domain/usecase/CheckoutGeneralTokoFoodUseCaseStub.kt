package com.tokopedia.tokofood.stub.purchase.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase.CheckoutGeneralTokoFoodUseCase

class CheckoutGeneralTokoFoodUseCaseStub(
    graphqlRepository: GraphqlRepository
): CheckoutGeneralTokoFoodUseCase(graphqlRepository)
