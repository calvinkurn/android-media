package com.tokopedia.unifyorderhistory.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.unifyorderhistory.data.model.LsPrintData
import com.tokopedia.unifyorderhistory.util.UohConsts.LS_PRINT_GQL_PARAM_ACTION
import com.tokopedia.unifyorderhistory.util.UohConsts.LS_PRINT_GQL_PARAM_BUSINESS_CODE
import com.tokopedia.unifyorderhistory.util.UohConsts.LS_PRINT_GQL_PARAM_UUID
import com.tokopedia.unifyorderhistory.util.UohConsts.LS_PRINT_GQL_PARAM_VALUE
import com.tokopedia.unifyorderhistory.util.UohConsts.PARAM_LS_PRINT_BUSINESS_CODE
import com.tokopedia.unifyorderhistory.util.UohConsts.PARAM_LS_PRINT_FINISH_ACTION
import javax.inject.Inject

@GqlQuery("LsPrintFinishOrderQuery", TrainResendEmailUseCase.query)
class LsPrintFinishOrderUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) :
    CoroutineUseCase<String, LsPrintData>(dispatchers.io) {

    override fun graphqlQuery(): String = query

    override suspend fun execute(verticalId: String): LsPrintData {
        return repository.request(LsPrintFinishOrderQuery(), generateParam(verticalId))
    }

    private fun generateParam(verticalId: String): Map<String, Any?> {
        return mapOf(
            LS_PRINT_GQL_PARAM_BUSINESS_CODE to PARAM_LS_PRINT_BUSINESS_CODE,
            LS_PRINT_GQL_PARAM_ACTION to PARAM_LS_PRINT_FINISH_ACTION,
            LS_PRINT_GQL_PARAM_UUID to verticalId,
            LS_PRINT_GQL_PARAM_VALUE to ""
        )
    }

    companion object {
        const val query = """
            mutation OrderInternalActionMutation(${'$'}businessCode:String, ${'$'}action:String, ${'$'}uuid:String, ${'$'}value:String) {
              oiaction(businessCode:${'$'}businessCode, action:${'$'}action, uuid:${'$'}uuid, value:${'$'}value) {
                error
                status
                data {
                  message
                }
              }
            }
        """
    }
}
