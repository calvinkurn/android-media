package com.tokopedia.privacycenter.main.section.privacypolicy.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.main.section.privacypolicy.PrivacyPolicyConst
import com.tokopedia.privacycenter.main.section.privacypolicy.PrivacyPolicyConst.RESPONSE_OK
import com.tokopedia.privacycenter.main.section.privacypolicy.PrivacyPolicyConst.RESPONSE_SUCCESS
import com.tokopedia.privacycenter.main.section.privacypolicy.domain.data.PrivacyPolicyDetailDataModel
import com.tokopedia.privacycenter.main.section.privacypolicy.domain.data.PrivacyPolicyDetailResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetPrivacyPolicyDetailUseCase @Inject constructor(
    private val restRepository: RestRepository,
    dispatchers: CoroutineDispatchers
) : UseCase<PrivacyCenterStateResult<PrivacyPolicyDetailDataModel>>(dispatchers.io) {

    private var sectionId: String = ""

    fun setParam(sectionId: String) {
        this.sectionId = sectionId
    }

    override suspend fun executeOnBackground(): PrivacyCenterStateResult<PrivacyPolicyDetailDataModel> {
        val request = RestRequest.Builder(
            PrivacyPolicyConst.getPrivacyPolicyDetailUrl(sectionId),
            PrivacyPolicyDetailResponse::class.java
        ).setRequestType(RequestType.GET).build()

        val response = restRepository.getResponse(request)
        return if (response.isError) {
            PrivacyCenterStateResult.Fail(Throwable(response.errorBody))
        } else {
            val data = response.getData<PrivacyPolicyDetailResponse>()
            if (data.respCode == RESPONSE_OK && data.respDesc == RESPONSE_SUCCESS) {
                PrivacyCenterStateResult.Success(data.data)
            } else {
                PrivacyCenterStateResult.Fail(MessageErrorException(data.respDesc))
            }
        }
    }
}
