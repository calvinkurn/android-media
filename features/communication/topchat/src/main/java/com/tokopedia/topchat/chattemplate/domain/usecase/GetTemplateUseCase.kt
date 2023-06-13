package com.tokopedia.topchat.chattemplate.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.topchat.chattemplate.domain.pojo.GetChatTemplateResponse
import javax.inject.Inject

open class GetTemplateUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetTemplateUseCase.Param, GetChatTemplateResponse>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        query chatTemplatesAll($$PARAM_IS_SELLER: Boolean!) {
            chatTemplatesAll($PARAM_IS_SELLER: $$PARAM_IS_SELLER){
                buyerTemplate {
                  isEnable
                  IsEnableSmartReply
                  IsSeller
                  templates
                }
                sellerTemplate {
                  isEnable
                  IsEnableSmartReply
                  IsSeller
                  templates
                }
            }
        }
    """.trimIndent()

    override suspend fun execute(params: Param): GetChatTemplateResponse {
        return repository.request(graphqlQuery(), params)
    }

    data class Param(
        @SerializedName(PARAM_IS_SELLER)
        val isSeller: Boolean = false
    ) : GqlParam

    companion object {
        private const val PARAM_IS_SELLER = "isSeller"
    }
}
