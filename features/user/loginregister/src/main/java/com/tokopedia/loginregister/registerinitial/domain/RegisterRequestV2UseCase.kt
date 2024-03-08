package com.tokopedia.loginregister.registerinitial.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterRequestV2
import javax.inject.Inject

class RegisterRequestV2UseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<RegisterRequestParam, RegisterRequestV2>(dispatchers.io) {

    override fun graphqlQuery(): String {
        return """mutation register(
            ${'$'}reg_type: String!, 
            ${'$'}fullname: String!, 
            ${'$'}email: String!, 
            ${'$'}password: String!, 
            ${'$'}os_type: String!, 
            ${'$'}validate_token: String!, 
            ${'$'}h: String!) {
                register_v2(input: {
                    reg_type: ${'$'}reg_type
                    fullname: ${'$'}fullname
                    email   : ${'$'}email
                    password: ${'$'}password
                    os_type : ${'$'}os_type
                    validate_token: ${'$'}validate_token
                    h:${'$'}h
                }) {
                    user_id
                    sid
                    access_token
                    refresh_token
                    token_type
                    is_active
                    action
                    errors {
                        name
                        message
                    }
                }
            }
        """.trimIndent()
    }

    override suspend fun execute(params: RegisterRequestParam): RegisterRequestV2 {
        return graphqlRepository.request(graphqlQuery(), params)
    }
}

data class RegisterRequestParam(
    @SerializedName("email")
    var email: String = "",
    @SerializedName("password")
    var password: String = "",
    @SerializedName("os_type")
    var osType: String = "",
    @SerializedName("reg_type")
    var regType: String = "",
    @SerializedName("fullname")
    var fullname: String = "",
    @SerializedName("validate_token")
    var validateToken: String = "",
    @SerializedName("h")
    var passwordHash: String = ""
): GqlParam
