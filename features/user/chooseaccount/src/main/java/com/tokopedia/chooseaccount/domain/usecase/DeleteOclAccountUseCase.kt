package com.tokopedia.chooseaccount.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chooseaccount.data.DeleteOclData
import com.tokopedia.chooseaccount.data.DeleteOclParam
import com.tokopedia.chooseaccount.data.DeleteOclResponse
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class DeleteOclAccountUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
): CoroutineUseCase<DeleteOclParam, DeleteOclData>(dispatcher.io){

    override fun graphqlQuery(): String = """
        mutation deleteOclAccount(${'$'}ocl_jwt_token: String!, ${'$'}ocl_token: String!){
            deleteOcl(
                input: { 
                    ocl_jwt_token: ${'$'}ocl_jwt_token
                    ocl_token: ${'$'}ocl_token 
                }
            ) {
                ocl_jwt_token
                error_code
            }
        }
    """.trimIndent()

    override suspend fun execute(params: DeleteOclParam): DeleteOclData {
        val result: DeleteOclResponse = repository.request(graphqlQuery(), params)
        if(result.data.errorMsg.isNotEmpty()) {
            throw MessageErrorException(result.data.errorMsg)
        }
        return result.data
    }
}
