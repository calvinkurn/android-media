package com.tokopedia.logisticseller.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticseller.data.param.GeneralInfoRtsParam
import com.tokopedia.logisticseller.data.response.GetGeneralInfoRtsResponse
import javax.inject.Inject

@GqlQuery(
    GetGeneralInfoRtsUseCase.GET_GENERAL_INFO_QUERY_NAME,
    GetGeneralInfoRtsUseCase.GET_GENERAL_INFO_QUERY
)
class GetGeneralInfoRtsUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GeneralInfoRtsParam, GetGeneralInfoRtsResponse>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return GET_GENERAL_INFO_QUERY
    }

    override suspend fun execute(params: GeneralInfoRtsParam): GetGeneralInfoRtsResponse {
        return repository.request(GetGeneralInformation(), params)
    }

    companion object {
        const val GET_GENERAL_INFO_QUERY_NAME = "GetGeneralInformation"
        const val GET_GENERAL_INFO_QUERY = """
          query getGeneralInformation(${'$'}input:MpLogisticGetGeneralInformationInputs!){
            getGeneralInformation(input: ${'$'}input) {
              data {
                title
                description
                invoice
                image {
                  image_id
                  copy_writing_disclaimer
                }
                article_url
              }
              status
              message_error
            }
          }  
        """
    }
}
