package com.tokopedia.report.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class ProductReportViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                 @Named("product_report_reason")
                                                 private val reportReasonQuery: String,
                                                 dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher) {

    val reasonResponse =  MutableLiveData<Result<List<ProductReportReason>>>()

    init {
        getReportReason()
    }

    private fun getReportReason(){
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(reportReasonQuery, ProductReportReason.Response::class.java)
            val data = withContext(Dispatchers.IO){
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }
            val list = data.getSuccessData<ProductReportReason.Response>().data
            reasonResponse.value = Success(list)
        }){
            reasonResponse.value = Fail(it)
        }
    }

}