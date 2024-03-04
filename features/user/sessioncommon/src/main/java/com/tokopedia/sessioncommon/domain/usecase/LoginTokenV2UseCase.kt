package com.tokopedia.sessioncommon.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.sessioncommon.data.LoginTokenPojoV2
import com.tokopedia.sessioncommon.util.TokenGenerator
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 16/02/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */
open class LoginTokenV2UseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    val dispatcher: CoroutineDispatchers,
    private val userSession: UserSessionInterface
) :
    CoroutineUseCase<LoginTokenV2GqlParam, LoginTokenPojoV2>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return """
        mutation login_email_v2(${'$'}grant_type: String!, ${'$'}username: String!, ${'$'}password: String!, ${'$'}h: String!){
            login_token_v2(
                input: {
                    grant_type: ${'$'}grant_type
                    username: ${'$'}username
                    password: ${'$'}password
                    h: ${'$'}h
                }
            ) {
                acc_sid
                access_token
                expires_in
                refresh_token
                sid
                token_type
                sq_check
                action
                errors {
                    name
                    message
                }
                event_code
                popup_error {
                    header
                    body
                    action
                }
            }
        }
        """.trimIndent()
    }

    override suspend fun execute(params: LoginTokenV2GqlParam): LoginTokenPojoV2 {
        userSession.setToken(TokenGenerator().createBasicTokenGQL(), "")
        return repository.request(graphqlQuery(), params)
    }
}

data class LoginTokenV2GqlParam(
    @SerializedName("grant_type")
    private val grantType: String,

    @SerializedName("username")
    private val username: String,

    @SerializedName("password")
    private val password: String,

    @SerializedName("h")
    private val hash: String

) : GqlParam
