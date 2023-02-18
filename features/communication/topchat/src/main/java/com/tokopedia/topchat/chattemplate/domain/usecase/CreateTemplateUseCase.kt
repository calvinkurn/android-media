package com.tokopedia.topchat.chattemplate.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.topchat.chattemplate.domain.pojo.ChatAddTemplateResponse
import javax.inject.Inject

open class CreateTemplateUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<CreateTemplateUseCase.Param, ChatAddTemplateResponse>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        mutation chatAddTemplate($$PARAM_IS_SELLER: Boolean!, $$PARAM_VALUE: String!) {
          chatAddTemplate($PARAM_IS_SELLER: $$PARAM_IS_SELLER, $PARAM_VALUE: $$PARAM_VALUE){
            success
          }
        }
    """.trimIndent()

    override suspend fun execute(params: Param): ChatAddTemplateResponse {
        return repository.request(graphqlQuery(), params)
    }

    data class Param(
        @SerializedName(PARAM_IS_SELLER)
        val isSeller: Boolean = false,

        @SerializedName(PARAM_VALUE)
        val value: String = ""
    ) : GqlParam

    companion object {
        private const val PARAM_IS_SELLER = "isSeller"
        private const val PARAM_VALUE = "value"
    }
}
