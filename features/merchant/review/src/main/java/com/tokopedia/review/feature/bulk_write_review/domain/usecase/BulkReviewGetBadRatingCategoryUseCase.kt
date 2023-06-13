package com.tokopedia.review.feature.bulk_write_review.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetBadRatingCategoryRequestState
import com.tokopedia.review.feature.createreputation.domain.usecase.GetBadRatingCategoryUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BulkReviewGetBadRatingCategoryUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val useCase: GetBadRatingCategoryUseCase
) : FlowUseCase<Unit, BulkReviewGetBadRatingCategoryRequestState>(dispatchers.io) {

    override fun graphqlQuery(): String = ""

    override suspend fun execute(
        params: Unit
    ): Flow<BulkReviewGetBadRatingCategoryRequestState> = flow {
        emit(BulkReviewGetBadRatingCategoryRequestState.Requesting())
        emit(BulkReviewGetBadRatingCategoryRequestState.Complete.Success(useCase.executeOnBackground()))
    }.catch {
        emit(BulkReviewGetBadRatingCategoryRequestState.Complete.Error(it))
    }
}
