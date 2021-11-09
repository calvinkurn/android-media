package com.tokopedia.contactus.inboxticket2.domain.usecase

import com.google.gson.reflect.TypeToken
import com.tokopedia.contactus.inboxticket2.data.ContactUsRepository
import com.tokopedia.contactus.inboxticket2.data.model.ChipUploadHostConfig
import com.tokopedia.contactus.inboxticket2.data.model.SecureImageParameter
import okhttp3.MultipartBody
import javax.inject.Inject

private const val SERVER_ID = "server_id"

class SecureUploadUseCase @Inject constructor(private val repository: ContactUsRepository) {

    suspend fun getSecureImageParameter(body: MultipartBody.Part, chipUploadHostConfig: ChipUploadHostConfig): SecureImageParameter {
        val secureImageParameter = repository.postMultiRestData<SecureImageParameter>(
                chipUploadHostConfig.chipUploadHostConfig?.chipUploadHostConfigData?.generatedHost?.uploadSecureHost ?: "",
                object : TypeToken<SecureImageParameter>() {}.type,
                queryMap = mapOf(SERVER_ID to (chipUploadHostConfig.chipUploadHostConfig?.chipUploadHostConfigData?.generatedHost?.serverId ?: "")),
                body = body)
        return secureImageParameter
    }
}