package com.tokopedia.logisticseller.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticseller.data.param.GetGeneralInfoRtsParam
import com.tokopedia.logisticseller.data.response.ReqGeneralInfoRtsResponse
import javax.inject.Inject

class RequestGeneralInfoRtsUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetGeneralInfoRtsParam, ReqGeneralInfoRtsResponse>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return REQUEST_GENERAL_INFORMATION_QUERY
    }

    override suspend fun execute(params: GetGeneralInfoRtsParam): ReqGeneralInfoRtsResponse {
//        return repository.request(graphqlQuery(), params)
        return mockResponse()
    }

    private fun mockResponse(): ReqGeneralInfoRtsResponse{
        return ReqGeneralInfoRtsResponse(
            status = 201,
            message = "success"
        )
    }

    companion object {
        const val REQUEST_GENERAL_INFORMATION_QUERY = """
          mutation requestGeneralInformation(${'$'}input:GetGeneralInfoRtsParam!){
            requestGeneralInformation(input: ${'$'}input) {
              status
              message
            }
          }  
        """
    }
}
