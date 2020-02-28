package com.tokopedia.updateinactivephone.data.mapper

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response
import com.tokopedia.updateinactivephone.data.model.request.UploadHostData
import com.tokopedia.updateinactivephone.data.model.request.UploadHostModel

import javax.inject.Inject

import retrofit2.Response
import rx.functions.Func1

class UploadHostMapper @Inject constructor() : Func1<Response<TokopediaWsV4Response>, UploadHostModel> {

    override fun call(response: Response<TokopediaWsV4Response>): UploadHostModel {
        return mappingResponse(response)
    }

    private fun mappingResponse(response: Response<TokopediaWsV4Response>): UploadHostModel {
        val model = UploadHostModel()

        if (response.isSuccessful) {
            if (response.body()?.isError == false) {
                val data = response.body()?.convertDataObj(UploadHostData::class.java)
                model.isSuccess = true
                model.uploadHostData = data
            } else {
                if (response.body()?.errorMessages == null && response.body()?.errorMessages?.isEmpty() == true) {
                    model.isSuccess = false
                } else {
                    model.isSuccess = false
                    model.errorMessage = response.body()?.errorMessageJoined
                }
            }
            model.statusMessage = response.body()!!.statusMessageJoined
        } else {
            model.isSuccess = false
        }
        model.responseCode = response.code()
        return model
    }
}
