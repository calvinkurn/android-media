package com.tokopedia.inbox.universalinbox.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.inbox.universalinbox.util.Result
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class UniversalInboxGetProductRecommendationUseCase @Inject constructor(
    @ApplicationContext context: Context,
    @ApplicationContext graphqlRepository: GraphqlRepository,
    private val dispatchers: CoroutineDispatchers
) : GetRecommendationUseCase(context, graphqlRepository) {

    suspend fun fetchProductRecommendation(inputParameter: GetRecommendationRequestParam): Flow<Result<RecommendationWidget>> {
        return flow {
            val response = getData(inputParameter).first() // only need first
            emit(response)
        }
            .map<RecommendationWidget, Result<RecommendationWidget>> { Result.Success(it) }
            .onStart { emit(Result.Loading) }
            .catch { emit(Result.Error(it)) }
            .flowOn(dispatchers.io)
    }
}
