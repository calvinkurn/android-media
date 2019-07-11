package com.tokopedia.profilecompletion.settingprofile.domain

import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.profilecompletion.data.UploadProfileImageModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import okhttp3.RequestBody
import rx.Observable
import java.util.HashMap
import javax.inject.Inject


class UploadProfilePictureUseCase @Inject
constructor(internal var uploadImageUseCase:  UploadImageUseCase<UploadProfileImageModel>,
            internal var submitProfilePictureUseCase : SubmitProfilePictureUseCase)
    : UseCase<UploadProfileImageModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<UploadProfileImageModel> {
        return uploadImageUseCase.getExecuteObservable(createUploadProfilePictureParams(requestParams))
                .flatMap {  model ->
                    Observable.just<UploadProfileImageModel>(model.dataResultImageUpload)  }
    }

    private fun createUploadProfilePictureParams(requestParams: RequestParams): RequestParams? {
        val imagePath = requestParams.getString(EXTRA_IMAGE_PATH, "")
        val LOGO_FILENAME_IMAGE_JPG = "profile_img\"; filename=\"image.jpg"
        val PATH_UPLOAD = "/web-service/v4/action/upload-image/upload_profile_image.pl"

        val maps = HashMap<String, RequestBody>()

        return uploadImageUseCase.createRequestParam(imagePath, PATH_UPLOAD, LOGO_FILENAME_IMAGE_JPG, maps)
    }


    companion object {
        val EXTRA_IMAGE_PATH = "image_path"

        @JvmStatic
        fun createRequestParams(imagePath: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(EXTRA_IMAGE_PATH, imagePath)
            return requestParams
        }
    }
}
