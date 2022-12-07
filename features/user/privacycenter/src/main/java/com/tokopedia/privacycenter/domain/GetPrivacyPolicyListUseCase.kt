package com.tokopedia.privacycenter.domain

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.privacycenter.data.PrivacyPolicyDataModel
import com.tokopedia.privacycenter.data.PrivacyPolicyListResponse
import com.tokopedia.privacycenter.ui.main.section.privacypolicy.PrivacyPolicyConst
import com.tokopedia.utils.date.DateUtil
import javax.inject.Inject

class GetPrivacyPolicyListUseCase @Inject constructor(
    private val restRepository: RestRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Int, List<PrivacyPolicyDataModel>>(dispatchers.io) {

    override fun graphqlQuery(): String = ""

    override suspend fun execute(params: Int): List<PrivacyPolicyDataModel> {
        val request = RestRequest.Builder(
            PrivacyPolicyConst.GET_LIST_URL,
            PrivacyPolicyListResponse::class.java
        ).setRequestType(RequestType.GET).build()

        val response = restRepository.getResponse(request)
        return if (response.isError) {
            throw Exception(response.errorBody)
        } else {
            val data = response.getData<PrivacyPolicyListResponse>()
            if (data.respCode == PrivacyPolicyConst.RESPONSE_OK && data.respDesc == PrivacyPolicyConst.RESPONSE_SUCCESS) {
                val listData = data.data
                listData.sortedByDescending {
                    it.lastUpdate
                }.map {
                    it.sectionTitle = "${it.sectionTitle} - ${
                    DateUtil.formatDate(
                        DateUtil.YYYY_MM_DD_T_HH_MM_SS_SSS_Z,
                        DateUtil.DEFAULT_VIEW_FORMAT,
                        it.lastUpdate
                    )
                    }"
                }

                if (params > 0) {
                    data.data.take(params)
                } else {
                    data.data
                }
            } else {
                throw MessageErrorException(data.respDesc)
            }
        }
    }
}
