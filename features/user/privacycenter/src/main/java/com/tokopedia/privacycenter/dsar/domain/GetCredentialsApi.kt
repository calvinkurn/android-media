package com.tokopedia.privacycenter.dsar.domain

import com.tokopedia.privacycenter.dsar.DsarHelper
import com.tokopedia.privacycenter.dsar.model.GetCredentialResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class GetCredentialsApi @Inject constructor(
    val oneTrustApi: OneTrustApi,
    val dsarHelper: DsarHelper
) {
    suspend fun fetchCredential(): GetCredentialResponse? {
        return oneTrustApi.getCredentials(
            "client_credentials".toRequestBody("text/plain".toMediaTypeOrNull()),
            dsarHelper.getClientId().toRequestBody("text/plain".toMediaTypeOrNull()),
            dsarHelper.getSecret().toRequestBody("text/plain".toMediaTypeOrNull())
        ).body()
    }
}
