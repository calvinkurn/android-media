package com.tokopedia.loginregister.shopcreation.domain

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.shopcreation.data.GetUserProfileCompletionPojo
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-12-12.
 * ade.hadian@tokopedia.com
 */

class GetUserProfileCompletionUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository, dispatcher: CoroutineDispatchers) : CoroutineUseCase<Unit, GetUserProfileCompletionPojo>(dispatcher.io) {

    override suspend fun execute(params: Unit): GetUserProfileCompletionPojo {
        return graphqlRepository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
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
}
