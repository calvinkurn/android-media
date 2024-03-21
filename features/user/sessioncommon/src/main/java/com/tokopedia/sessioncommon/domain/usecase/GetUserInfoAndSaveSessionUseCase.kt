package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.util.GetProfileUtils
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class GetUserInfoAndSaveSessionUseCase @Inject constructor(
    @ApplicationContext val repository: GraphqlRepository,
    private val getProfileUtils: GetProfileUtils,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Unit, Result<ProfilePojo>>(dispatchers.io) {

    override fun graphqlQuery(): String = """
        query profile() {
          profile(skipCache: true) {
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
              avatarOriginal
            }
          }
        }
    """.trimIndent()

    override suspend fun execute(params: Unit): Result<ProfilePojo> {
        val response: ProfilePojo = repository.request(graphqlQuery(), params)

        return onSuccessGetInfo(response)
    }

    private fun onSuccessGetInfo(profilePojo: ProfilePojo): Result<ProfilePojo> {
        val profile = profilePojo.profileInfo
        val isProfileValid = profile.userId.isNotBlank() && profile.userId != "0"

        return if (isProfileValid) {
            getProfileUtils.saveProfileData(profilePojo)
            Success(profilePojo)
        } else {
            Fail(Throwable())
        }
    }
}
