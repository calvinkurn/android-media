package com.tokopedia.devicefingerprint.datavisor.usecase

import com.tokopedia.devicefingerprint.datavisor.response.GetDVInitStatResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class GetDVInitStatUseCase @Inject constructor(val repository: dagger.Lazy<GraphqlRepository>) {

    var useCase: GraphqlUseCase<GetDVInitStatResponse>? = null

    companion object {
        private const val PARAM_KEY = "key"
        private const val PARAM_LAST_TIME_STAMP = "last_check_ts"
        private const val UNIX_TIME_MS = 1000L
        const val QUERY = """
            mutation GetDVInitStat(${'$'}key:String!, ${'$'}last_check_ts:Int!) {
              getDVInitStat(input: { key: ${'$'}key, last_check_ts:${'$'}last_check_ts }) {
                is_error
                data {
                  is_expire
                }
              }
            }
        """
    }

    @GqlQuery("GetDVInitStat", QUERY)
    private fun getOrCreateUseCase(): GraphqlUseCase<GetDVInitStatResponse> {
        val useCaseTemp = useCase
        return if (useCaseTemp == null) {
            val newUseCase = GraphqlUseCase<GetDVInitStatResponse>(repository.get())
            newUseCase.setGraphqlQuery(GetDVInitStat())
            newUseCase.setTypeClass(GetDVInitStatResponse::class.java)
            useCase = newUseCase
            newUseCase
        } else {
            useCaseTemp
        }
    }

    suspend fun execute(key: String, lastTimeStamp: Long): GetDVInitStatResponse {
        val useCase = getOrCreateUseCase()
        val unixTime = lastTimeStamp / UNIX_TIME_MS
        val params: Map<String, Any?> = mutableMapOf(
            PARAM_KEY to key,
            PARAM_LAST_TIME_STAMP to unixTime,
        )
        useCase.setRequestParams(params)
        return useCase.executeOnBackground()
    }
}