package com.tokopedia.attachproduct.fake.usecase

import com.tokopedia.attachproduct.data.model.AceSearchProductResponse
import com.tokopedia.attachproduct.di.AttachProductScope
import com.tokopedia.attachproduct.domain.usecase.AttachProductUseCase
import com.tokopedia.attachproduct.utils.FileUtils
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import com.tokopedia.attachproduct.test.R

class FakeAttachProductUseCase @Inject constructor(
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