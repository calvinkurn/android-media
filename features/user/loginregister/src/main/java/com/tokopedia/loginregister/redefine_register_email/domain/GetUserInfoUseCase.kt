package com.tokopedia.loginregister.redefine_register_email.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.redefine_register_email.domain.data.GetUserInfoModel
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Unit, GetUserInfoModel>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
            query profile() {
                profile() {
                    user_id
                    full_name
                    first_name
                    email
                    bday
                    gender
                    phone
                    phone_masked
                    phone_verified
                    profile_picture
                    created_password
                    isLoggedIn
                }
                shopBasicData(){
                    result{
                        shopID
                        name
                        domain
                        level
                        logo
                    }
                }
            }
        """.trimIndent()

    override suspend fun execute(params: Unit): GetUserInfoModel {
        return repository.request(graphqlQuery(), params)
    }
}
