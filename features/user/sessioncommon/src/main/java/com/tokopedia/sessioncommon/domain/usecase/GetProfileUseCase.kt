package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import javax.inject.Inject

/**
 * @author by nisie on 12/06/19.
 */

open class GetProfileUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Unit, ProfilePojo>(dispatcher.io) {

    companion object {
        const val PARAM_SKIP_CACHE = "skipCache"
    }

    override fun graphqlQuery(): String {
        return """
            query profile(${'$'}skipCache: Boolean!) {
              profile(skipCache: ${'$'}skipCache) {
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

    override suspend fun execute(params: Unit): ProfilePojo {
        return repository.request(graphqlQuery(), mapOf(PARAM_SKIP_CACHE to true))
    }
}
