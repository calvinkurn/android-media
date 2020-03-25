package com.tokopedia.updateinactivephone.data.mapper

import com.tokopedia.updateinactivephone.data.model.request.UploadImageModel
import com.tokopedia.updateinactivephone.data.model.response.UploadImageData

import javax.inject.Inject

import rx.functions.Func1

class UploadImageMapper @Inject constructor() : Func1<UploadImageData, UploadImageModel> {

    override fun call(response: UploadImageData): UploadImageModel {
        return mappingResponse(response)
    }

    private fun mappingResponse(response: UploadImageData): UploadImageModel {
        val model = UploadImageModel()
        try {
            model.uploadImageData = response
            model.isSuccess = true
        } catch (e: Exception) {
            e.printStackTrace()
            model.isSuccess = false
        }
        return model
    }
}
