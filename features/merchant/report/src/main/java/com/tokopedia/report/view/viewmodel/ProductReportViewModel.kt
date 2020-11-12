package com.tokopedia.report.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.coroutines.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class ProductReportViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                 @Named("product_report_reason")
                                                 private val reportReasonQuery: String,
                                                 private val dispatcher: CoroutineDispatchers): BaseViewModel(dispatcher.main) {

    val reasonResponse =  MutableLiveData<Result<List<ProductReportReason>>>()

    init {
        getReportReason()
    }

    private fun getReportReason(){
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(reportReasonQuery, ProductReportReason.Response::class.java)
            val data = withContext(dispatcher.io){
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }
            val list = data.getSuccessData<ProductReportReason.Response>().data
            reasonResponse.value = Success(list)
        }){
            reasonResponse.value = Fail(it)
        }
    }

}