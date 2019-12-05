package com.tokopedia.settingbank.banklist.v2.view.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.settingbank.banklist.v2.di.QUERY_GET_KYC_RESPONSE
import com.tokopedia.settingbank.banklist.v2.domain.KYCCheckResponse
import com.tokopedia.settingbank.banklist.v2.view.viewState.*
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetKYCViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                          private val rawQueries: Map<String, String>,
                                          dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val kycInfoState = MutableLiveData<KYCInfoState>()

    private var job: Job? = null

    fun getKYCInfo() {
        cancelCurrentJob()
        createNewJob()
        if (kycInfoState.value == KYCInfoRequestStarted)
            return
        kycInfoState.value = KYCInfoRequestStarted
        launchCatchError(block = {
            val data = fetchKYCInfo(job!!)
            processResponse(data.getSuccessData())
            kycInfoState.value = KYCInfoRequestEnded

        }) {
            kycInfoState.value = KYCInfoRequestEnded
            kycInfoState.value = KYCInfoError("Error")
            it.printStackTrace()
        }
    }

    private fun processResponse(response: KYCCheckResponse) {
        kycInfoState.value = OnKYCInfoResponse(response.kycInfo)
    }

    private suspend fun fetchKYCInfo(job: Job): GraphqlResponse =
            withContext(Dispatchers.IO + job) {
                val graphRequest = GraphqlRequest(rawQueries[QUERY_GET_KYC_RESPONSE],
                        KYCCheckResponse::class.java)
                graphqlRepository.getReseponse(listOf(graphRequest))
            }

    private fun createNewJob() {
        job = Job()
    }

    private fun cancelCurrentJob() {
        job?.cancel()
    }

    override fun onCleared() {
        cancelCurrentJob()
        super.onCleared()
    }
}