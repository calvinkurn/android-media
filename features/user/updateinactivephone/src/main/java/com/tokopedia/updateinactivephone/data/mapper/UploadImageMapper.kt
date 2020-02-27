package com.tokopedia.updateinactivephone.data.mapper

import com.tokopedia.updateinactivephone.data.model.request.UploadImageModel
import com.tokopedia.updateinactivephone.data.model.response.UploadImageData

import javax.inject.Inject

import retrofit2.Response
import rx.functions.Func1

class UploadImageMapper @Inject constructor() : Func1<Response<UploadImageData>, UploadImageModel> {

    override fun call(response: Response<UploadImageData>): UploadImageModel {
        return mappingResponse(response)
    }

    private fun mappingResponse(response: Response<UploadImageData>): UploadImageModel {
        val model = UploadImageModel()

        if (response.isSuccessful) {
            if (response.body() != null) {
                val data = response.body()
                model.isSuccess = true
                model.uploadImageData = data
            } else {
                model.isSuccess = false

            }
        } else {
            model.isSuccess = false
        }
        model.responseCode = response.code()
        return model
    }
}
