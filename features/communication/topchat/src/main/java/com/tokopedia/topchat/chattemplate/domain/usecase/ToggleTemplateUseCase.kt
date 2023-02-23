package com.tokopedia.topchat.chattemplate.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.topchat.chattemplate.domain.pojo.ChatToggleTemplateResponse
import javax.inject.Inject

open class ToggleTemplateUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<ToggleTemplateUseCase.Param, ChatToggleTemplateResponse>(dispatcher.io) {

    override fun graphqlQuery(): String = QUERY

    override suspend fun execute(params: Param): ChatToggleTemplateResponse {
        return repository.request(graphqlQuery(), params)
    }

    data class Param(
        @SerializedName(PARAM_IS_ENABLE)
        val isEnable: Boolean = false
    ) : GqlParam

    companion object {
        private const val PARAM_IS_ENABLE = "isEnable"
        val QUERY = """
            mutation chatToggleTemplate($$PARAM_IS_ENABLE: Boolean!) {
              chatToggleTemplate($PARAM_IS_ENABLE: $$PARAM_IS_ENABLE){
                success
              }
            }
        """.trimIndent()
    }
}
