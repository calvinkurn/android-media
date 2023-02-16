package com.tokopedia.profilecompletion.profileinfo.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.profileinfo.data.ProfileInfoResponse
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ProfileInfoUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Unit, ProfileInfoResponse>(dispatcher) {

    override suspend fun execute(params: Unit): ProfileInfoResponse {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String =
        """
       	query userProfileInfo(){
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
        }""".trimIndent()
}
