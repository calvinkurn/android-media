package com.tokopedia.updateinactivephone.data.mapper

import com.tokopedia.updateinactivephone.data.model.request.UploadHostModel
import com.tokopedia.updateinactivephone.data.model.request.UploadHostResponse

import javax.inject.Inject

import rx.functions.Func1

class UploadHostMapper @Inject constructor() : Func1<UploadHostResponse, UploadHostModel> {

    override fun call(response: UploadHostResponse?): UploadHostModel {
        return mappingResponse(response)
    }

    private fun mappingResponse(response: UploadHostResponse?): UploadHostModel {
        val model = UploadHostModel()
        if (response != null) {
            model.isSuccess = true
            model.uploadHostData = response.data
        } else {
            model.isSuccess = false
            model.errorMessage = errorMessage
        }
        return model
    }

    companion object {
        const val errorMessage = "Terjadi kesalahan, silakan coba lagi nanti."
    }
}
