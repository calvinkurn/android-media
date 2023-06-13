package com.tokopedia.privacycenter.remote

import com.tokopedia.privacycenter.data.GetCredentialResponse
import com.tokopedia.privacycenter.ui.dsar.DsarConstants
import com.tokopedia.privacycenter.ui.dsar.DsarHelper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class GetCredentialsApi @Inject constructor(
    val oneTrustApi: OneTrustApi,
    val dsarHelper: DsarHelper
) {
    suspend fun fetchCredential(): GetCredentialResponse? {
        return oneTrustApi.getCredentials(
            CLIENT_CREDENTIALS.toRequestBody(DsarConstants.HEADER_TEXT_PLAIN.toMediaTypeOrNull()),
            dsarHelper.getClientId().toRequestBody(DsarConstants.HEADER_TEXT_PLAIN.toMediaTypeOrNull()),
            dsarHelper.getSecret().toRequestBody(DsarConstants.HEADER_TEXT_PLAIN.toMediaTypeOrNull())
        ).body()
    }

    companion object {
        const val CLIENT_CREDENTIALS = "client_credentials"
    }
}
