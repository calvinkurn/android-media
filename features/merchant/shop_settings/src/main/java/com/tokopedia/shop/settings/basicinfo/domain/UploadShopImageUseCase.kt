package com.tokopedia.shop.settings.basicinfo.domain

import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.domain.model.ImageUploadDomainModel
import com.tokopedia.shop.common.graphql.data.GraphQLSuccessMessage
import com.tokopedia.shop.settings.basicinfo.data.UploadShopEditImageModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import java.util.HashMap

import javax.inject.Inject

import okhttp3.MediaType
import okhttp3.RequestBody
import rx.Observable
import rx.functions.Func1

class UploadShopImageUseCase @Inject
constructor(internal var uploadImageUseCase: UploadImageUseCase<UploadShopEditImageModel>) : UseCase<UploadShopEditImageModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<UploadShopEditImageModel> {
        return uploadImageUseCase.getExecuteObservable(createParamUploadImage(
                requestParams.getString(EXTRA_IMAGE_PATH, "")))
                .flatMap({ model ->
                    Observable.just<UploadShopEditImageModel>(model.getDataResultImageUpload()) })
    }

    private fun createParamUploadImage(pathFileImage: String): RequestParams {
        val maps = HashMap<String, RequestBody>()
        val newAddValue = RequestBody.create(MediaType.parse("text/plain"), GOLANG_VALUE)
        val resolutionValue = RequestBody.create(MediaType.parse("text/plain"), RESOLUTION_DEFAULT_VALUE)

        maps[KEY_NEW_ADD] = newAddValue
        maps[KEY_RESOLUTION] = resolutionValue
        return uploadImageUseCase.createRequestParam(pathFileImage, PATH_UPLOAD, LOGO_FILENAME_IMAGE_JPG, maps)
    }

    companion object {

        val LOGO_FILENAME_IMAGE_JPG = "logo\"; filename=\"image.jpg"
        val PATH_UPLOAD = "/web-service/v4/action/upload-image/upload_shop_image.pl"
        val EXTRA_IMAGE_PATH = "image_path"

        val KEY_NEW_ADD = "new_add"
        val KEY_RESOLUTION = "resolution"

        val RESOLUTION_DEFAULT_VALUE = "300"
        val GOLANG_VALUE = "2"

        @JvmStatic
        fun createRequestParams(imagePath: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(EXTRA_IMAGE_PATH, imagePath)
            return requestParams
        }
    }
}
