package com.tokopedia.talk.feature.write.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.write.data.TalkCreateNewTalkResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class TalkCreateNewTalkUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<TalkCreateNewTalkResponseWrapper>(graphqlRepository) {

    companion object {

        const val PARAM_PRODUCT_ID = "product_id"
        const val PARAM_TEXT = "text"

        private val query by lazy {
            """
                mutation talkCreateNewTalk(${'$'}product_id: Int, ${'$'}text: String) {
                  talkCreateNewTalk(product_id:${'$'}product_id, text:${'$'}text) {
                    status
                    messageError
                    data {
                      isSuccess
                      talkID
                    }
                    messageErrorOriginal
                  }
                }
            """.trimIndent()
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(TalkCreateNewTalkResponseWrapper::class.java)
    }

    fun setParams(productId: Int, text: String) {
        setRequestParams(
                RequestParams.create().apply {
                    putInt(PARAM_PRODUCT_ID, productId)
                    putString(PARAM_TEXT, text)
                }.parameters
        )
    }


}