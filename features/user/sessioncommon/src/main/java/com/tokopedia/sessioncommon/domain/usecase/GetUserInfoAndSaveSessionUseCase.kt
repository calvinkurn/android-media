package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.domain.commonaction.GetProfileInfoSaveSession
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import com.tokopedia.usecase.coroutines.Result

class GetUserInfoAndSaveSessionUseCase @Inject constructor(
    @ApplicationContext val repository: GraphqlRepository,
    private val userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Unit, Result<ProfilePojo>>(dispatchers.io){

    override fun graphqlQuery(): String = """
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

    override suspend fun execute(params: Unit): Result<ProfilePojo> {
        val response: ProfilePojo = repository.request(graphqlQuery(), params)

        return object : GetProfileInfoSaveSession(response, userSession) {}.data()
    }
}