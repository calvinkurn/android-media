package com.tokopedia.chooseaccount.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chooseaccount.data.GetOclAccountData
import com.tokopedia.chooseaccount.data.GetOclAccountParam
import com.tokopedia.chooseaccount.data.GetOclAccountResponse
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class GetOclAccountUseCase @Inject constructor (
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
): CoroutineUseCase<GetOclAccountParam, GetOclAccountData>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        query getOclAccounts(${'$'}ocl_jwt_token: String!){
            get_ocl(ocl_jwt_token: ${'$'}ocl_jwt_token){
                list_users{
                    full_name
                    profile_picture
                    ocl_token
                    login_type
                    login_type_wording
		        }
                ocl_jwt_token
                message_error
            }
        }
        """.trimIndent()

    override suspend fun execute(params: GetOclAccountParam): GetOclAccountData {
        val result: GetOclAccountResponse = repository.request(graphqlQuery(), params)
        if(result.data.error.isNotEmpty()) {
            throw MessageErrorException(result.data.error)
        } else {
            return result.data
        }
    }
}
