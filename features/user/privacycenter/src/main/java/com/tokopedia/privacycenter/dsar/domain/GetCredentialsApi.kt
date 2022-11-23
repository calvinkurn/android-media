package com.tokopedia.privacycenter.dsar.domain

import com.tokopedia.privacycenter.dsar.DsarConstants.HEADER_TEXT_PLAIN
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
            CLIENT_CREDENTIALS.toRequestBody(HEADER_TEXT_PLAIN.toMediaTypeOrNull()),
            dsarHelper.getClientId().toRequestBody(HEADER_TEXT_PLAIN.toMediaTypeOrNull()),
            dsarHelper.getSecret().toRequestBody(HEADER_TEXT_PLAIN.toMediaTypeOrNull())
        ).body()
    }

    companion object {
        const val CLIENT_CREDENTIALS = "client_credentials"
    }
}
