package com.tokopedia.logisticseller.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticseller.data.param.GeneralInfoRtsParam
import com.tokopedia.logisticseller.data.response.ReqGeneralInfoRtsResponse
import javax.inject.Inject

@GqlQuery(
    RequestGeneralInfoRtsUseCase.INSERT_ACTION_GENERAL_INFO_QUERY_NAME,
    RequestGeneralInfoRtsUseCase.INSERT_ACTION_GENERAL_INFO_QUERY
)
class RequestGeneralInfoRtsUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GeneralInfoRtsParam, ReqGeneralInfoRtsResponse>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return INSERT_ACTION_GENERAL_INFO_QUERY
    }

    override suspend fun execute(params: GeneralInfoRtsParam): ReqGeneralInfoRtsResponse {
        return repository.request(InsertActionGeneralInformation(), params)
    }

    companion object {
        const val INSERT_ACTION_GENERAL_INFO_QUERY_NAME = "InsertActionGeneralInformation"
        const val INSERT_ACTION_GENERAL_INFO_QUERY = """
          mutation mpLogisticInsertActionGeneralInformation(${'$'}input:MpLogisticInsertActionGeneralInformationInput!){
            mpLogisticInsertActionGeneralInformation(input: ${'$'}input) {
              status
              message_error
            }
          }  
        """
    }
}
