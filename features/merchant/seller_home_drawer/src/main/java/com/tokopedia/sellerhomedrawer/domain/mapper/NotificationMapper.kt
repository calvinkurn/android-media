package com.tokopedia.sellerhomedrawer.domain.mapper

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomedrawer.data.drawernotification.NotificationData
import com.tokopedia.sellerhomedrawer.data.drawernotification.NotificationModel
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

class NotificationMapper @Inject constructor() : Func1<Response<TokopediaWsV4Response>, NotificationModel> {

    override fun call(response: Response<TokopediaWsV4Response>?): NotificationModel {
        val model = NotificationModel()
        if (response != null) {
            if (response.isSuccessful) {
                val isError = response.body()?.isError
                if (isError != null && !isError) {
                    val data = response.body()?.convertDataObj(NotificationData::class.java)
                    model.isSuccess = true
                    model.notificationData = data
                } else {
                    val error = response.body()?.errorMessages
                    if (error.isNullOrEmpty())
                        model.isSuccess = false
                    else throw MessageErrorException(response.body()?.errorMessageJoined)
                }
            } else {
                throw RuntimeException("${response.code()}")
            }
        }
        return model
    }
}