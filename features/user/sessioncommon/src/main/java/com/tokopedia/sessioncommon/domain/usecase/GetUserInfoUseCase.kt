package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * Created by Yoris on 18/10/21.
 */

class GetUserInfoUseCase @Inject constructor(@ApplicationContext val repository: GraphqlRepository)
    : CoroutineUseCase<Unit, ProfilePojo> (Dispatchers.IO){

    override suspend fun execute(params: Unit): ProfilePojo {
        return repository.request(graphqlQuery(), params)
    }

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
              avatarOriginal
            }
          }
        }
        """.trimIndent()
}
