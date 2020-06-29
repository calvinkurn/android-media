package com.tokopedia.talk.feature.write.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.reading.data.model.discussiondata.DiscussionDataResponseWrapper
import com.tokopedia.talk.feature.write.data.DiscussionGetWritingFormResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class DiscussionGetWritingFormUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<DiscussionGetWritingFormResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_PRODUCT_ID = "productID"

        private val query by lazy {
            """
                
            """.trimIndent()
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(DiscussionGetWritingFormResponseWrapper::class.java)
    }

    fun setParams(productId: Int) {
        setRequestParams(RequestParams.create().apply {
            putInt(PARAM_PRODUCT_ID, productId)
        }.parameters)
    }
}