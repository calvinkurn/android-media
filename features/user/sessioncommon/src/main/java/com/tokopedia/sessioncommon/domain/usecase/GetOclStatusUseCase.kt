package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.sessioncommon.data.ocl.GetOclStatusParam
import com.tokopedia.sessioncommon.data.ocl.OclStatus
import com.tokopedia.sessioncommon.data.ocl.OclStatusResponse
import javax.inject.Inject

class GetOclStatusUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetOclStatusParam, OclStatus>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        query getOclStatus(${'$'}ocl_jwt_token: String!){
            checker_ocl(ocl_jwt_token: ${'$'}ocl_jwt_token){
                is_showing
                status
                message
            }
        }
        """.trimIndent()

    override suspend fun execute(params: GetOclStatusParam): OclStatus {
        val result: OclStatusResponse = repository.request(graphqlQuery(), params)
        return result.data
    }
}
