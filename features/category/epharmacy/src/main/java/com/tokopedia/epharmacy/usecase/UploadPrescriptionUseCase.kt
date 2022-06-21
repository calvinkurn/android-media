package com.tokopedia.epharmacy.usecase

import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.epharmacy.network.request.UploadPrescriptionRequest
import com.tokopedia.epharmacy.network.response.EPharmacyPrescriptionUploadResponse
import com.tokopedia.user.session.UserSessionInterface
import java.lang.reflect.Type
import javax.inject.Inject

class UploadPrescriptionUseCase @Inject constructor(
        private val repository: RestRepository
): RestRequestUseCase(repository) {

    private var base64Image: String = ""
    private var id = ""
    private var userAccessToken = ""

    override suspend fun executeOnBackground(): Map<Type, RestResponse> {
        val restRequest = RestRequest.Builder(ENDPOINT_URL, EPharmacyPrescriptionUploadResponse::class.java)
            .setBody(getUploadPrescriptionBody())
            .setHeaders(mapOf(HEADER_AUTHORIZATION to "Bearer $userAccessToken"))
            .setRequestType(RequestType.POST)
            .build()
        restRequestList.clear()
        restRequestList.add(restRequest)
        return repository.getResponses(restRequestList)
    }

    private fun getUploadPrescriptionBody(): UploadPrescriptionRequest{
        return UploadPrescriptionRequest(arrayListOf(
            UploadPrescriptionRequest.PrescriptionRequest(
            base64Image,"", KEY_FORMAT_VALUE,id, KEY_SOURCE_VALUE
        )))
    }

    fun setBase64Image(id : String, imageBase64: String, accessToken : String) {
        try {
            this.id = id
            this.base64Image = imageBase64
            this.userAccessToken = accessToken
        } catch (e: NullPointerException) {
        }
    }

    companion object {
        const val ENDPOINT_URL = "https://api-staging.tokopedia.com/epharmacy/prescription/upload"
        const val KEY_FORMAT_VALUE="FILE"
        const val KEY_SOURCE_VALUE="BUYER"
        const val HEADER_AUTHORIZATION = "Authorization"
    }
}