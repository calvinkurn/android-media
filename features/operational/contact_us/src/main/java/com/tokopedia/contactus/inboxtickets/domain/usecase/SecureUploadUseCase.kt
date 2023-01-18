package com.tokopedia.contactus.inboxtickets.domain.usecase

import com.google.gson.reflect.TypeToken
import com.tokopedia.contactus.inboxtickets.data.ContactUsRepository
import com.tokopedia.contactus.inboxtickets.data.model.ChipUploadHostConfig
import com.tokopedia.contactus.inboxtickets.data.model.SecureImageParameter
import okhttp3.MultipartBody
import javax.inject.Inject

private const val SERVER_ID = "server_id"

class SecureUploadUseCase @Inject constructor(private val repository: ContactUsRepository) {

    suspend fun getSecureImageParameter(body: MultipartBody.Part, chipUploadHostConfig: ChipUploadHostConfig): SecureImageParameter {
        val secureImageParameter = repository.postMultiRestData<SecureImageParameter>(
            chipUploadHostConfig.getUploadHostConfig().getUploadHostConfigData().getHost().getSecureHost(),
                object : TypeToken<SecureImageParameter>() {}.type,
                queryMap = mapOf(SERVER_ID to chipUploadHostConfig.getUploadHostConfig().getUploadHostConfigData().getHost().getSecureHost()),
                body = body)
        return secureImageParameter
    }
}
