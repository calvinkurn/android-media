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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

open class ImageUploadUseCase @Inject constructor(
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
        return (useCaseRequestParams.getString(key, "") ?: ""
                ).toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun generateParamFile(): RequestBody {
        val filePath = useCaseRequestParams.getString(PARAM_FILE_TO_UPLOAD, "")
        var file = File(filePath)

        if (checkFileIsMoreThan10Mb(file)) {
            try {
                file = ImageProcessingUtil.compressImageFile(filePath, IMAGE_QUALITY)
            } catch (e: Exception) {
                throw RuntimeException(ERROR_FAILED_UPLOAD_IMAGE)
            }
        }

        return file.asRequestBody("image/*".toMediaTypeOrNull())
    }

    private fun checkFileIsMoreThan10Mb(file: File): Boolean {
        val maxFileSize = MAX_FILE_SIZE_IN_MB * BYTE * BYTE
        val fileSize = file.length()
        return fileSize > maxFileSize
    }

    companion object {
        private const val MAX_FILE_SIZE_IN_MB = 10
        private const val IMAGE_QUALITY = 100
        private const val BYTE = 1024

        const val STATUS_OK = "OK"
    }
}