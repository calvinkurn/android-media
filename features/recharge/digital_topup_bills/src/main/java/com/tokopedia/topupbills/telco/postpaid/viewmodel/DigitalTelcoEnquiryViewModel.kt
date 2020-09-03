package com.tokopedia.topupbills.telco.postpaid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.topupbills.data.TelcoEnquiryData
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 27/05/19.
 */
class DigitalTelcoEnquiryViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                       private val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    private val _enquiryResult = MutableLiveData<Result<TelcoEnquiryData>>()
    val enquiryResult: LiveData<Result<TelcoEnquiryData>>
        get() = _enquiryResult

    fun getEnquiry(rawQuery: String, mapParam: Map<String, Any>) {
        launchCatchError(block = {
            val data = withContext(dispatcher) {
                val graphqlRequest = GraphqlRequest(rawQuery, TelcoEnquiryData::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TelcoEnquiryData>()

            _enquiryResult.postValue(Success(data))
        }) {
            _enquiryResult.postValue(Fail(it))
        }
    }
}