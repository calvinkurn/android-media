package com.tokopedia.attachproduct.stub.usecase

import com.tokopedia.attachproduct.data.model.AceSearchProductResponse
import com.tokopedia.attachproduct.domain.usecase.AttachProductUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import kotlinx.coroutines.CoroutineDispatcher

class FakeAttachProductUseCase constructor(
    repository: GraphqlRepository,
    dispatcher: CoroutineDispatcher
) : AttachProductUseCase(repository, "", dispatcher) {

    var response = AceSearchProductResponse()
        set(value) {
            field = value
        }

    override suspend fun execute(params: Map<String, Any>): AceSearchProductResponse {
        return response
    }
}