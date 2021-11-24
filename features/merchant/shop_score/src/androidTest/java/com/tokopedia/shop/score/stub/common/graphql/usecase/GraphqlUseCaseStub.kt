package com.tokopedia.shop.score.stub.common.graphql.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class GraphqlUseCaseStub<T : Any> @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<T>(graphqlRepository)