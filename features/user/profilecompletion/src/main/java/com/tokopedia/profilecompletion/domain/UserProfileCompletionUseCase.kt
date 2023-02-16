package com.tokopedia.profilecompletion.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.profilecompletion.data.UserProfileInfoData
import com.tokopedia.profilecompletion.profilecompletion.data.UserProfileRoleData
import javax.inject.Inject

class UserProfileCompletionUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Unit, UserProfileInfoData>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
          query userProfileCompletion(){
            userProfileCompletion {
              isActive,
              fullName,
              birthDate,
              birthDay,
              birthMonth,
              birthYear,
              gender,
              email,
              msisdn,
              isMsisdnVerified,
              isCreatedPassword,
              isBiodataDone,
              isEmailDone,
              isPasswordDone,
              isMsisdnDone,
              completionDone,
              completionScore
            }
          }
        """.trimIndent()

    override suspend fun execute(params: Unit): UserProfileInfoData {
        return repository.request(graphqlQuery(), params)
    }
}
