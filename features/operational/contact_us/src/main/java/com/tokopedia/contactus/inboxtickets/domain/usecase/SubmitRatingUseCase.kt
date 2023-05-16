package com.tokopedia.contactus.inboxtickets.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.contactus.inboxtickets.data.model.ChipGetInboxDetail
import com.tokopedia.contactus.inboxtickets.data.model.ChipInboxDetails
import com.tokopedia.contactus.inboxtickets.domain.usecase.param.SubmitRatingParam
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

const val SUBMIT_RATING_QUERY =
    """mutation SubmitRatingCSAT(${'$'}commentID: String!, ${'$'}rating: Int!, ${'$'}reason: String!) {
   chipSubmitRatingCSAT(commentID: ${'$'}commentID, rating: ${'$'}rating, reason: ${'$'}reason) {
     data {
       isSuccess
     }
     messageError
   }
 }"""

class SubmitRatingUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<SubmitRatingParam, ChipInboxDetails>(dispatchers.io) {

    fun createRequestParams(commentID: String, rating: Int, reason: String): SubmitRatingParam {
        return SubmitRatingParam(commentID, rating, reason)
    }

    override fun graphqlQuery(): String {
        return SUBMIT_RATING_QUERY
    }

    override suspend fun execute(params: SubmitRatingParam): ChipInboxDetails {
        return repository.request(graphqlQuery(), params)
    }
}
