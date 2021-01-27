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
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject
import javax.inject.Named

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

    init {
        getReportReason()
    }

    private fun getReportReason(){
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(query, ProductReportReason.Response::class.java)
            val data = graphqlRepository.getReseponse(listOf(graphqlRequest))
            val list = data.getSuccessData<ProductReportReason.Response>().data
            reasonResponse.postValue(Success(list))
        }){
            reasonResponse.postValue(Fail(it))
        }
    }

}