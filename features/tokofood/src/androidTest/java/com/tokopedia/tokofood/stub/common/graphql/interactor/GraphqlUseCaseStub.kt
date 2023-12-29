package com.tokopedia.tokofood.stub.common.graphql.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.tokofood.stub.common.graphql.repository.GraphqlRepositoryStub
import java.lang.reflect.Type

class GraphqlUseCaseStub<T : Any>(
    private val graphqlRepositoryStub: GraphqlRepositoryStub
) : GraphqlUseCase<T>(graphqlRepositoryStub) {

    fun createMapResult(pojo: Type, resultData: Any) {
        graphqlRepositoryStub.createMapResult(pojo, resultData)
    }

    fun clearMocks() {
        graphqlRepositoryStub.clearMocks()
    }
}
