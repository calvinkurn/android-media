package com.tokopedia.loginregister.shopcreation.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.shopcreation.data.UserProfileUpdatePojo
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<UpdateUserProfileParam, UserProfileUpdatePojo>(dispatcher.io) {

    override suspend fun execute(params: UpdateUserProfileParam): UserProfileUpdatePojo {
        return graphqlRepository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
        mutation userProfileUpdate(
                $fullname: String,
                $gender: String,
                $birthdate: String,
                $phone: String,
                $email: String,
                $currValidateToken: String
        ) {
            userProfileUpdate(
                    fullname: $fullname,
                    gender: $gender,
                    birthdate: $birthdate,
                    phone: $phone,
                    email: $email,
                    currValidateToken: $currValidateToken
            ) {
                isSuccess,
                completionScore,
                errors
            }
        }
    """.trimIndent()

    companion object {
        private const val fullname = "\$fullname"
        private const val gender = "\$gender"
        private const val birthdate = "\$birthdate"
        private const val phone = "\$phone"
        private const val email = "\$email"
        private const val currValidateToken = "\$currValidateToken"
    }
}

data class UpdateUserProfileParam(
    @SerializedName("fullname")
    val fullname: String = "",
    @SerializedName("gender")
    val gender: String = "",
    @SerializedName("birthdate")
    val birthdate: String = "",
    @SerializedName("phone")
    val phone: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("validateToken")
    val validateToken: String = ""
) : GqlParam
