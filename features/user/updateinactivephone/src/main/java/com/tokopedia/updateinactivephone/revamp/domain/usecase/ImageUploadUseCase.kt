package com.tokopedia.updateinactivephone.revamp.domain.usecase

import com.tokopedia.imagepicker.common.util.ImageUtils
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.ERROR_FAILED_UPLOAD_IMAGE
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.PARAM_EMAIL
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.PARAM_FILE_TO_UPLOAD
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.PARAM_OLD_PHONE
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.PARAM_URL_UPLOAD_IMAGE
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant.PARAM_USER_INDEX
import com.tokopedia.updateinactivephone.revamp.domain.api.InactivePhoneApi
import com.tokopedia.updateinactivephone.revamp.domain.api.InactivePhoneApiClient
import com.tokopedia.updateinactivephone.revamp.domain.data.ImageUploadDataModel
import com.tokopedia.updateinactivephone.revamp.domain.data.ImageUploadParamDataModel
import com.tokopedia.usecase.coroutines.UseCase
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class ImageUploadUseCase @Inject constructor(
        private val inactivePhoneApi: InactivePhoneApiClient<InactivePhoneApi>
) : UseCase<ImageUploadDataModel>() {

    override suspend fun executeOnBackground(): ImageUploadDataModel {
        return inactivePhoneApi.call().uploadImage(
                useCaseRequestParams.getString(PARAM_URL_UPLOAD_IMAGE, ""),
                generateParamUserId(),
                generateParamFile()
        )
    }

    fun setParam(url: String, email: String, oldMsisdn: String, userIndex: Int, filePath: String) {
        useCaseRequestParams.putString(PARAM_URL_UPLOAD_IMAGE, "https://$url/kyc/upload")
        useCaseRequestParams.putString(PARAM_OLD_PHONE, oldMsisdn)
        useCaseRequestParams.putString(PARAM_EMAIL, email)
        useCaseRequestParams.putInt(PARAM_USER_INDEX, userIndex)
        useCaseRequestParams.putString(PARAM_FILE_TO_UPLOAD, filePath)
    }

    private fun generateParamUserId(): ImageUploadParamDataModel {
//        return RequestBody.create(
//                MediaType.parse("text/plain"),
//                useCaseRequestParams.getString(PARAM_USER_INDEX, "")
//        )
        return ImageUploadParamDataModel(
                email = useCaseRequestParams.getString(PARAM_EMAIL, ""),
                phoneNumber = useCaseRequestParams.getString(PARAM_OLD_PHONE, ""),
                index = useCaseRequestParams.getInt(PARAM_USER_INDEX, 0)
        )
    }

    private fun generateParamFile(): RequestBody {
        val file: File
        try {
            file = ImageUtils.compressImageFile(useCaseRequestParams.getString(PARAM_FILE_TO_UPLOAD, ""), 100)
        } catch (e: Exception) {
            throw RuntimeException(ERROR_FAILED_UPLOAD_IMAGE)
        }

        return RequestBody.create(MediaType.parse("image/*"), file)
    }

    companion object {
        const val STATUS_OK = "OK"
    }
}