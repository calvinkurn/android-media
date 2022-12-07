package com.tokopedia.epharmacy.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.epharmacy.network.gql.GQL_EPHARMACY_REMINDER_SCREEN_QUERY
import com.tokopedia.epharmacy.network.request.EpharmacyUserReminderParam
import com.tokopedia.epharmacy.network.response.EPharmacyReminderScreenResponse
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class EPharmacyReminderScreenUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<EpharmacyUserReminderParam, EPharmacyReminderScreenResponse
    >(dispatchers.io) {

    override suspend fun execute(params: EpharmacyUserReminderParam): EPharmacyReminderScreenResponse {
        return repository.request(GQL_EPHARMACY_REMINDER_SCREEN_QUERY, createRequestParams(params))
    }

    override fun graphqlQuery(): String = GQL_EPHARMACY_REMINDER_SCREEN_QUERY

    private fun createRequestParams(params: EpharmacyUserReminderParam): Map<String, Any> {
        return mapOf<String, Any>(PARAM_INPUT to params.input)
    }

    companion object {
        const val PARAM_INPUT = "input"
    }
}
