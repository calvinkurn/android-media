package com.tokopedia.updateinactivephone.data.mapper

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response
import com.tokopedia.updateinactivephone.data.model.request.UploadHostData
import com.tokopedia.updateinactivephone.data.model.request.UploadHostModel

import javax.inject.Inject

import rx.functions.Func1

class UploadHostMapper @Inject constructor() : Func1<TokopediaWsV4Response, UploadHostModel> {

    override fun call(response: TokopediaWsV4Response?): UploadHostModel {
        return mappingResponse(response)
    }

    private fun mappingResponse(response: TokopediaWsV4Response?): UploadHostModel {
        val model = UploadHostModel()
        if (response?.isError == false){
            val data = response.convertDataObj(UploadHostData::class.java)
            model.isSuccess = true
            if (data != null) {
                model.uploadHostData = data
            }
        } else {
            if (response?.errorMessages == null && response?.errorMessages?.isEmpty() == true) {
                    model.isSuccess = false
                } else {
                    model.isSuccess = false
                    model.errorMessage = response?.errorMessageJoined.toString()
                }
        }
        return model
    }
}
