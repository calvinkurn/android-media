package com.tokopedia.loginregister.redefineregisteremail.view.registeremail.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.redefineregisteremail.view.registeremail.domain.data.ValidateUserDataModel
import com.tokopedia.loginregister.redefineregisteremail.view.registeremail.domain.data.ValidateUserDataParam
import javax.inject.Inject

class ValidateUserDataUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<ValidateUserDataParam, ValidateUserDataModel>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
            query validate_user_data(${'$'}email: String!, ${'$'}fullname: String!, ${'$'}password: String!, ${'$'}h: String!){
              validate_user_data(email: ${'$'}email, fullname: ${'$'}fullname, password: ${'$'}password, h: ${'$'}h){
                error
                error_email
                error_password
                error_fullname
                error_phone
                is_valid
                is_exist
              }
            }
        """.trimIndent()

    override suspend fun execute(params: ValidateUserDataParam): ValidateUserDataModel {
        return repository.request(graphqlQuery(), params)
    }
}