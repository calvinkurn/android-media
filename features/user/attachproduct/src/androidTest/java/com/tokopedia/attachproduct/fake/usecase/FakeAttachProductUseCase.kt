package com.tokopedia.attachproduct.fake.usecase

import com.tokopedia.attachproduct.data.model.AceSearchProductResponse
import com.tokopedia.attachproduct.domain.usecase.AttachProductUseCase
import com.tokopedia.attachproduct.utils.FileUtils
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import com.tokopedia.attachproduct.test.R

class FakeAttachProductUseCase @Inject constructor(private val repository: GraphqlRepository,
                                                   private val graphQuery: String,
                                                   private val dispatcher: CoroutineDispatcher
) : AttachProductUseCase(repository, graphQuery, dispatcher) {

    override suspend fun execute(params: Map<String, Any>): AceSearchProductResponse {
        return FileUtils.parseRaw(R.raw.example_ace_search_product, AceSearchProductResponse::class.java)
    }
}