package com.tokopedia.review.feature.bulk_write_review.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.review.feature.bulk_write_review.domain.model.RequestState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

typealias BulkReviewSubmitRequestState = RequestState<Boolean>

class BulkReviewSubmitUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val repository: GraphqlRepository
) : FlowUseCase<String, BulkReviewSubmitRequestState>(dispatchers.io) {
    override fun graphqlQuery(): String {
        return """"""
    }

    override suspend fun execute(
        params: String
    ) = flow {
        emit(RequestState.Requesting)
        kotlinx.coroutines.delay(5000L)
        emit(RequestState.Complete.Success(true))
    }.catch {
        emit(RequestState.Complete.Error(it))
    }
}
