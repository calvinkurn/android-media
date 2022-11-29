package com.tokopedia.logisticseller.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticseller.data.param.GeneralInfoRtsParam
import com.tokopedia.logisticseller.data.response.ReqGeneralInfoRtsResponse
import javax.inject.Inject

class RequestGeneralInfoRtsUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GeneralInfoRtsParam, ReqGeneralInfoRtsResponse>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return REQUEST_GENERAL_INFORMATION_QUERY
    }

    override suspend fun execute(params: GeneralInfoRtsParam): ReqGeneralInfoRtsResponse {
        return repository.request(graphqlQuery(), params)
    }

    companion object {
        const val REQUEST_GENERAL_INFORMATION_QUERY = """
          mutation requestGeneralInformation(${'$'}input:MpLogisticRequestGeneralInformationInputs!){
            requestGeneralInformation(input: ${'$'}input) {
              status
              message
            }
          }  
        """
    }
}
