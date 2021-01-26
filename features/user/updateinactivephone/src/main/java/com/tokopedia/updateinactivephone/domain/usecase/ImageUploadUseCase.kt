package com.tokopedia.updateinactivephone.domain.usecase

import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.ERROR_FAILED_UPLOAD_IMAGE
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.PARAM_EMAIL
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.PARAM_FILE_TO_UPLOAD
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.PARAM_OLD_PHONE
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.PARAM_USER_INDEX
import com.tokopedia.updateinactivephone.domain.api.InactivePhoneApi
import com.tokopedia.updateinactivephone.domain.api.InactivePhoneApiClient
import com.tokopedia.updateinactivephone.domain.data.ImageUploadDataModel
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.utils.image.ImageProcessingUtil
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class ImageUploadUseCase @Inject constructor(
        private val inactivePhoneApi: InactivePhoneApiClient<InactivePhoneApi>
) : UseCase<ImageUploadDataModel>() {

    override suspend fun executeOnBackground(): ImageUploadDataModel {
        return inactivePhoneApi.call().uploadImage(
                userIndex = generateParamString(PARAM_USER_INDEX),
                email = generateParamString(PARAM_EMAIL),
                oldMsisdn = generateParamString(PARAM_OLD_PHONE),
                file = generateParamFile()
        )
    }

    fun setParam(email: String, oldMsisdn: String, userIndex: Int, filePath: String) {
        useCaseRequestParams.putString(PARAM_OLD_PHONE, oldMsisdn)
        useCaseRequestParams.putString(PARAM_EMAIL, email)
        useCaseRequestParams.putString(PARAM_USER_INDEX, userIndex.toString())
        useCaseRequestParams.putString(PARAM_FILE_TO_UPLOAD, filePath)
    }

    private fun generateParamString(key: String): RequestBody {
        return RequestBody.create(
                MediaType.parse("text/plain"),
                useCaseRequestParams.getString(key, "") ?: ""
        )
    }

    private fun generateParamFile(): RequestBody {
        val file: File
        try {
            file = ImageProcessingUtil.compressImageFile(useCaseRequestParams.getString(PARAM_FILE_TO_UPLOAD, ""), 100)
        } catch (e: Exception) {
            throw RuntimeException(ERROR_FAILED_UPLOAD_IMAGE)
        }

        return RequestBody.create(MediaType.parse("image/*"), file)
    }

    companion object {
        const val STATUS_OK = "OK"
    }
}