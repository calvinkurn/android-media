package com.tokopedia.recommendation_widget_common.widget.global

import androidx.lifecycle.viewModelScope
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.mvvm.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecommendationWidgetViewModel @Inject constructor(
    state: RecommendationWidgetState = RecommendationWidgetState(),
    private val getRecommendationWidgetUseCase: GetRecommendationUseCase,
): androidx.lifecycle.ViewModel(),
    ViewModel<RecommendationWidgetState> {

    private val _stateFlow = MutableStateFlow(state)
    override val stateFlow: StateFlow<RecommendationWidgetState>
        get() = _stateFlow

    private val stateValue: RecommendationWidgetState
        get() = stateFlow.value

    private fun updateState(action: (RecommendationWidgetState) -> RecommendationWidgetState) {
        _stateFlow.update(action)
    }

    internal fun bind(model: RecommendationWidgetModel) {
        if (model.widget != null) {
            updateState { it.from(model, listOf(model.widget)) }
        } else if (!stateValue.contains(model)) {
            updateState { it.loading(model) }

            viewModelScope.launch {
                try {
                    tryGetRecommendationWidget(model)
                } catch (ignored: Throwable) {
                    onGetRecommendationWidgetError(model)
                }
            }
        }
    }

    private suspend fun tryGetRecommendationWidget(model: RecommendationWidgetModel) {
        val recommendationWidgetList = getRecommendationWidgetUseCase.getData(
            GetRecommendationRequestParam(
                pageNumber = model.metadata.pageNumber,
                productIds = model.metadata.productIds,
                queryParam = model.metadata.queryParam,
                pageName = model.metadata.pageName,
                categoryIds = model.metadata.categoryIds,
                keywords = model.metadata.keyword,
                isTokonow = model.metadata.isTokonow,
            )
        )

        updateState { it.from(model, recommendationWidgetList) }
    }

    private fun onGetRecommendationWidgetError(model: RecommendationWidgetModel) {
        updateState { it.error(model) }
    }

    fun refresh() {
        updateState { it.clear() }
    }
}
