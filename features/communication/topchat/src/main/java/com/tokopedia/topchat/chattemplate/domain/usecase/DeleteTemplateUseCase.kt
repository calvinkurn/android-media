package com.tokopedia.topchat.chattemplate.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.topchat.chattemplate.domain.pojo.ChatDeleteTemplateResponse
import javax.inject.Inject

open class DeleteTemplateUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<DeleteTemplateUseCase.Param, ChatDeleteTemplateResponse>(dispatcher.io) {

    override fun graphqlQuery(): String = QUERY

    override suspend fun execute(params: Param): ChatDeleteTemplateResponse {
        return repository.request(graphqlQuery(), params)
    }

    data class Param(
        @SerializedName(PARAM_IS_SELLER)
        val isSeller: Boolean = false,

        @SerializedName(PARAM_TEMPLATE_INDEX)
        val templateIndex: Int = 0
    ) : GqlParam

    companion object {
        private const val PARAM_IS_SELLER = "isSeller"
        private const val PARAM_TEMPLATE_INDEX = "templateIndex"
        val QUERY = """
            mutation chatDeleteTemplate($$PARAM_IS_SELLER: Boolean!, $$PARAM_TEMPLATE_INDEX: Int!) {
              chatDeleteTemplate($PARAM_IS_SELLER: $$PARAM_IS_SELLER, $PARAM_TEMPLATE_INDEX: $$PARAM_TEMPLATE_INDEX){
                success
              }
            }
        """.trimIndent()
    }
}
