package com.tokopedia.inbox.universalinbox.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject
import com.tokopedia.inbox.universalinbox.util.Result as Result

class UniversalInboxGetProductRecommendationUseCase @Inject constructor(
    @ApplicationContext context: Context,
    @ApplicationContext graphqlRepository: GraphqlRepository
) : GetRecommendationUseCase(context, graphqlRepository) {

    private val productRecommendationFlow = MutableStateFlow<Result<RecommendationWidget>>(
        Result.Loading
    )

    private var lastSuccessfulRecommendationWidget = RecommendationWidget()

    fun observe(): Flow<Result<RecommendationWidget>> = productRecommendationFlow.asStateFlow()

    suspend fun fetchProductRecommendation(inputParameter: GetRecommendationRequestParam) {
        productRecommendationFlow.emit(Result.Loading)
        try {
            val response = getData(inputParameter).first() // only need first
            updateFlowState(inputParameter.pageNumber, response)
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            productRecommendationFlow.emit(Result.Error(throwable))
        }
    }

    private suspend fun updateFlowState(
        page: Int,
        response: RecommendationWidget
    ) {
        lastSuccessfulRecommendationWidget = if (page <= 1) { // if first page, reset
            response
        } else {
            val updatedList = lastSuccessfulRecommendationWidget.recommendationItemList +
                response.recommendationItemList
            lastSuccessfulRecommendationWidget.copy(
                title = response.title,
                recommendationItemList = updatedList
            )
        }
        productRecommendationFlow.emit(Result.Success(lastSuccessfulRecommendationWidget))
    }

    fun reset() {
        lastSuccessfulRecommendationWidget = RecommendationWidget()
    }
}
