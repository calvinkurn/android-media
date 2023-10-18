package com.tokopedia.report.view.viewmodel

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.nest.principles.utils.UiText
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.view.fragment.models.ProductReportUiEvent
import com.tokopedia.report.view.fragment.models.ProductReportUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductReportViewModel @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io) {

    companion object {
        private const val query = """
            {
              visionGetReportProductReason(lang: "id"){
                category_id
                child{
                  category_id
                  child{
                    category_id
                    detail
                    value
                  }
                  additional_fields{
                    detail
                    key
                    max
                    min
                    type
                    label
                  }
                  additional_info{
                    type
                    label
                    value
                  }
                  detail
                  value
                }
                additional_fields{
                  detail
                  key
                  max
                  min
                  type
                  label
                }
                additional_info{
                  type
                  label
                  value
                }
                detail
                value
              }
            }
        """
    }

    private val _uiState = MutableStateFlow(ProductReportUiState())
    val uiState get() = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ProductReportUiEvent>(replay = 1)
    val uiEffect get() = _uiEffect.asSharedFlow()

    fun onEvent(event: ProductReportUiEvent) = viewModelScope.launch {
        when (event) {
            is ProductReportUiEvent.LoadData -> getReportReason()
            is ProductReportUiEvent.OnItemClicked -> {
                onItemClicked(reason = event.reason)
            }
            is ProductReportUiEvent.OnBackPressed -> {
                onBackPressed()
            }
            else -> _uiEffect.emit(event)
        }
    }

    private fun getReportReason() = launchCatchError(block = {
        val graphqlRequest = GraphqlRequest(query, ProductReportReason.Response::class.java)
        val data = graphqlRepository.response(listOf(graphqlRequest))
        val list = data.getSuccessData<ProductReportReason.Response>().data
        val state = _uiState.value

        updateState(
            state.copy(data = list, allData = list)
        )
    }) { throwable ->
        _uiEffect.emit(ProductReportUiEvent.OnToasterError(throwable))
    }

    private fun onItemClicked(reason: ProductReportReason) = viewModelScope.launch {
        if (reason.children.isNotEmpty()) {
            setChildrenIsNotEmpty(reason = reason)
        } else {
            val filterId = _uiState.value.filterId.toMutableList()
            val parent = _uiState.value.baseParent ?: reason
            val fieldReason = if (filterId.isNotEmpty()) {
                reason.copy(
                    additionalInfo = parent.additionalInfo,
                    additionalFields = parent.additionalFields
                ).also {
                    it.parentLabel = parent.strLabel
                }
            } else {
                reason
            }

            _uiEffect.emit(ProductReportUiEvent.OnGoToForm(fieldReason))
        }
    }

    private suspend fun setChildrenIsNotEmpty(
        reason: ProductReportReason
    ) {
        val state = _uiState.value
        val filterId = state.filterId.toMutableList()
        val baseParent = if (filterId.isEmpty()) {
            reason
        } else {
            state.baseParent
        }

        filterId.add(reason.categoryId.toIntOrZero())

        updateState(
            state.copy(baseParent = baseParent, filterId = filterId)
        )
        _uiEffect.emit(ProductReportUiEvent.OnScrollTop(reason = reason))
    }

    private fun onBackPressed() = viewModelScope.launch {
        val state = _uiState.value
        val filterId = state.filterId.toMutableList()

        if (filterId.isEmpty()) {
            _uiEffect.emit(ProductReportUiEvent.OnBackPressed)
        } else {
            filterId.removeLast()
            updateState(
                state.copy(filterId = filterId)
            )
        }
    }

    private fun updateState(state: ProductReportUiState) {
        val allData = state.allData
        val filterId = state.filterId
        val id = filterId.lastOrNull() ?: -1

        if (id <= 0) {
            val title = UiText.Resource(com.tokopedia.report.R.string.product_report_header)
            _uiState.update {
                state.copy(title = title, data = allData)
            }
        } else {
            val reason = allData.firstOrNull {
                it.categoryId.toIntOrZero() == id
            }
            val title = UiText.String(reason?.value.orEmpty())
            val data = reason?.children.orEmpty()
            _uiState.update {
                state.copy(title = title, data = data)
            }
        }
    }
}
