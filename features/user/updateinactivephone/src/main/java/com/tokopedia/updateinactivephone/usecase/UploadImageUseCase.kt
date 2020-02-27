package com.tokopedia.updateinactivephone.usecase

import com.tokopedia.core.app.MainApplication
import com.tokopedia.core.base.domain.UseCase
import com.tokopedia.core.base.domain.executor.PostExecutionThread
import com.tokopedia.core.base.domain.executor.ThreadExecutor
import com.tokopedia.core.util.ImageUploadHandler
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.data.repository.UploadImageRepositoryImpl
import com.tokopedia.updateinactivephone.data.model.request.UploadImageModel

import java.io.File
import java.util.HashMap

import okhttp3.MediaType
import okhttp3.RequestBody
import rx.Observable

import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.ID
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.IMAGE_UPLOAD_URL
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.PARAM_FILE_TO_UPLOAD
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.RESOLUTION
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.SERVER_ID
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.TOKEN
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.USERID

class UploadImageUseCase(threadExecutor: ThreadExecutor,
         postExecutionThread: PostExecutionThread,
         private val uploadImageRepository: UploadImageRepositoryImpl) : UseCase<UploadImageModel>(threadExecutor, postExecutionThread) {

    override fun createObservable(requestParams: com.tokopedia.core.base.domain.RequestParams): Observable<UploadImageModel> {
        val uploadUrl = "https://" + requestParams.getString(IMAGE_UPLOAD_URL, "") + "/upload/attachment"

        return uploadImageRepository.uploadImage(uploadUrl,
                generateRequestBody(requestParams),
                getUploadImageFile(requestParams)
        )
    }

    private fun generateRequestBody(requestParams: com.tokopedia.core.base.domain.RequestParams): Map<String, String> {
        val requestBodyMap = HashMap<String, String>()
        requestBodyMap[USERID] = requestParams.getString(USERID, "")
        requestBodyMap[ID] = requestParams.getString(USERID, "")
        requestBodyMap[SERVER_ID] = requestParams.getString(SERVER_ID, "49")
        requestBodyMap[TOKEN] = requestParams.getString(TOKEN, "")
        requestBodyMap[RESOLUTION] = requestParams.getString(RESOLUTION, "215")

        return requestBodyMap
    }

    private fun getUploadImageFile(requestParams: com.tokopedia.core.base.domain.RequestParams): RequestBody {
        var file: File
        try {
            file = ImageUploadHandler.writeImageToTkpdPath(
                    ImageUploadHandler.compressImage(requestParams.getString(PARAM_FILE_TO_UPLOAD, ""))
            )
        } catch (e: Exception) {
            throw RuntimeException(MainApplication.getAppContext().getString(R.string.error_upload_image))
        }

        return RequestBody.create(MediaType.parse("image/*"), file)
    }
}
