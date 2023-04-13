package com.tokopedia.play.broadcaster.shorts.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.play.broadcaster.shorts.domain.model.BroadcasterCheckAffiliateResponseModel
import javax.inject.Inject

class BroadcasterCheckIsAffiliateUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers,
) : CoroutineUseCase<Unit, BroadcasterCheckAffiliateResponseModel>(dispatcher.io) {

    override suspend fun execute(params: Unit): BroadcasterCheckAffiliateResponseModel {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String {
        return """
            {
              broadcasterCheckIsAffiliate {
                isAffiliate
                affiliateName
              }
            }
        """.trimIndent()
    }

}
