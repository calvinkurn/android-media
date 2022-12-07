package com.tokopedia.review.feature.bulk_write_review.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.review.feature.bulk_write_review.domain.model.RequestState
import com.tokopedia.review.feature.createreputation.domain.usecase.GetBadRatingCategoryUseCase
import com.tokopedia.review.feature.createreputation.model.BadRatingCategoriesResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

typealias BulkReviewGetBadRatingCategoryRequestState = RequestState<BadRatingCategoriesResponse>

class BulkReviewGetBadRatingCategoryUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val useCase: GetBadRatingCategoryUseCase
) : FlowUseCase<Unit, BulkReviewGetBadRatingCategoryRequestState>(dispatchers.io) {

    override fun graphqlQuery(): String = ""

    override suspend fun execute(
        params: Unit
    ): Flow<BulkReviewGetBadRatingCategoryRequestState> = flow {
        emit(RequestState.Requesting)
        emit(RequestState.Complete.Success(useCase.executeOnBackground()))
    }.catch {
        emit(RequestState.Complete.Error(it))
    }
}
