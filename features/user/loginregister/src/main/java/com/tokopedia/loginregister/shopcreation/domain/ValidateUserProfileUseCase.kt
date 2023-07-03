package com.tokopedia.loginregister.shopcreation.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.shopcreation.data.UserProfileValidatePojo
import javax.inject.Inject

class ValidateUserProfileUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<ValidateUserProfileParam, UserProfileValidatePojo>(dispatcher.io) {

    override suspend fun execute(params: ValidateUserProfileParam): UserProfileValidatePojo {
        return graphqlRepository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
        mutation userProfileValidate($phone: String, $email: String, $validateToken: String) {
            userProfileValidate(phone: $phone, email: $email, validateToken: $validateToken) {
                isValid,
                message
            }
        }
    """.trimIndent()

    companion object {
        private const val phone = "\$phone"
        private const val email = "\$email"
        private const val validateToken = "\$validateToken"
    }
}

data class ValidateUserProfileParam(
    @SerializedName("phone")
    val phone: String = "",
    @SerializedName("email")
    val email: String = ""
) : GqlParam
