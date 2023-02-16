package com.tokopedia.profilecompletion.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.addpin.data.ValidatePinPojo
import com.tokopedia.profilecompletion.profilecompletion.data.UserProfileRoleData
import javax.inject.Inject

class UserProfileRuleUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Unit, UserProfileRoleData>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
          query userProfileRole(){
            userProfileRole{
              isAllowedChangeDob
              isAllowedChangeName
              isAllowedChangeGender
              chancesChangeName
              chancesChangeGender
            }
          }
        """.trimIndent()

    override suspend fun execute(params: Unit): UserProfileRoleData {
        return repository.request(graphqlQuery(), params)
    }
}
