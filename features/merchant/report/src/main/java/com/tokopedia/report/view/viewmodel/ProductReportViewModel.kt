package com.tokopedia.report.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.view.fragment.models.ProductReportUiState
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductReportViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                 dispatcher: CoroutineDispatchers): BaseViewModel(dispatcher.io) {

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

    val reasonResponse =  MutableLiveData<Result<List<ProductReportReason>>>()

    private val _uiState = MutableStateFlow(ProductReportUiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        getReportReason()
    }

    private fun getReportReason() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }

        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(query, ProductReportReason.Response::class.java)
            val data = graphqlRepository.response(listOf(graphqlRequest))
            val list = data.getSuccessData<ProductReportReason.Response>().data
            _uiState.update { it.copy(isLoading = false, data = list) }
        }){ throwable ->
            _uiState.update { it.copy(isLoading = false, error = throwable.message) }
        }
    }
}