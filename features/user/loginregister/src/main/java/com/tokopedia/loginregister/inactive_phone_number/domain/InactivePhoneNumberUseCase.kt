package com.tokopedia.loginregister.inactive_phone_number.domain

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.inactive_phone_number.data.model.RegisterCheckModel
import com.tokopedia.sessioncommon.domain.query.LoginQueries
import javax.inject.Inject

class InactivePhoneNumberUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<String, RegisterCheckModel>(dispatcher.io) {

    override fun graphqlQuery(): String =
        LoginQueries.registerCheckQuery

    override suspend fun execute(params: String): RegisterCheckModel {
        val parameters = mapOf(
            ID to params
        )
        return graphqlRepository.request(graphqlQuery(), parameters)
    }

    companion object {
        private const val ID = "id"
    }

}