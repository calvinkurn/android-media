package com.tokopedia.privacycenter.main.section.privacypolicy.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.main.section.privacypolicy.PrivacyPolicyConst
import com.tokopedia.privacycenter.main.section.privacypolicy.domain.data.PrivacyPolicyDataModel
import com.tokopedia.privacycenter.main.section.privacypolicy.domain.data.PrivacyPolicyListResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetPrivacyPolicyListUseCase @Inject constructor(
    private val restRepository: RestRepository,
    dispatchers: CoroutineDispatchers
) : UseCase<PrivacyCenterStateResult<List<PrivacyPolicyDataModel>>>(dispatchers.io) {

    private var listLimit: Int = 0

    fun setParam(limit: Int) {
        listLimit = limit
    }

    override suspend fun executeOnBackground(): PrivacyCenterStateResult<List<PrivacyPolicyDataModel>> {
        val request = RestRequest.Builder(
            PrivacyPolicyConst.GET_LIST_URL,
            PrivacyPolicyListResponse::class.java
        ).setRequestType(RequestType.GET).build()

        val response = restRepository.getResponse(request)
        return if (response.isError) {
            PrivacyCenterStateResult.Fail(Throwable(response.errorBody))
        } else {
            val data = response.getData<PrivacyPolicyListResponse>()
            if (data.respCode == PrivacyPolicyConst.RESPONSE_OK && data.respDesc == PrivacyPolicyConst.RESPONSE_SUCCESS) {
                val listData = data.data
                listData.sortedByDescending {
                    it.lastUpdate
                }
                PrivacyCenterStateResult.Success(data.data.take(listLimit))
            } else {
                PrivacyCenterStateResult.Fail(MessageErrorException(data.respDesc))
            }
        }
    }
}

