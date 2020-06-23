package com.tokopedia.updateinactivephone.usecase

import android.content.Context
import com.tokopedia.imagepicker.common.util.ImageUtils
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.IMAGE_UPLOAD_URL
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.PARAM_FILE_TO_UPLOAD
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.USERID
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QueryConstants.Companion.USER_ID
import com.tokopedia.updateinactivephone.data.model.request.UploadImageModel
import com.tokopedia.updateinactivephone.data.repository.UploadImageRepositoryImpl
import com.tokopedia.updateinactivephone.di.UpdateInActiveQualifier
import com.tokopedia.usecase.RequestParams
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.util.*

class UploadImageUseCase(
        @UpdateInActiveQualifier private val context: Context,
        private val uploadImageRepository: UploadImageRepositoryImpl
) {

    suspend fun uploadImage(requestParams: RequestParams): UploadImageModel {
        val uploadUrl = "https://" + requestParams.getString(IMAGE_UPLOAD_URL, "") + "/kyc/upload"
        return uploadImageRepository.uploadImage(uploadUrl,
                generateRequestBody(requestParams),
                getUploadImageFile(requestParams)
        )
    }

    private fun generateRequestBody(requestParams: RequestParams): RequestBody{
        return RequestBody.create(MediaType.parse("text/plain"),
                requestParams.getString(USERID, ""))
    }

    private fun getUploadImageFile(requestParams: RequestParams): RequestBody {
        var file: File
        try {
            file = ImageUtils.compressImageFile(requestParams.getString(PARAM_FILE_TO_UPLOAD, ""), 100)
        } catch (e: Exception) {
            throw RuntimeException(context.getString(R.string.error_upload_image))
        }

        return RequestBody.create(MediaType.parse("image/*"), file)
    }
}
