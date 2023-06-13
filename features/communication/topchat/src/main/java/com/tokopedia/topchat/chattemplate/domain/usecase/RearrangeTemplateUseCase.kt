package com.tokopedia.topchat.chattemplate.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.topchat.chattemplate.domain.pojo.ChatMoveTemplateResponse
import javax.inject.Inject

open class RearrangeTemplateUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<RearrangeTemplateUseCase.Param, ChatMoveTemplateResponse>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        mutation chatMoveTemplate($$PARAM_IS_SELLER: Boolean!, $$PARAM_INDEX: Int!, $$PARAM_MOVE_TO: Int!) {
          chatMoveTemplate($PARAM_IS_SELLER: $$PARAM_IS_SELLER, $PARAM_INDEX: $$PARAM_INDEX, $PARAM_MOVE_TO: $$PARAM_MOVE_TO){
            success
          }
        }
    """.trimIndent()

    override suspend fun execute(params: Param): ChatMoveTemplateResponse {
        return repository.request(graphqlQuery(), params)
    }

    data class Param(
        @SerializedName(PARAM_IS_SELLER)
        val isSeller: Boolean = false,

        @SerializedName(PARAM_INDEX)
        val index: Int = 0,

        @SerializedName(PARAM_MOVE_TO)
        val moveTo: Int = 0
    ) : GqlParam

    companion object {
        private const val PARAM_IS_SELLER = "isSeller"
        private const val PARAM_INDEX = "index"
        private const val PARAM_MOVE_TO = "moveTo"
    }
}
