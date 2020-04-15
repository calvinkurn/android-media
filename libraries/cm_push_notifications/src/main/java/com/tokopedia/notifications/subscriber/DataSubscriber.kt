package com.tokopedia.notifications.subscriber

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel.Companion.STATUS_OK
import rx.Subscriber
import java.lang.Exception

object DataSubscriber {

    fun atcSubscriber(
            onSuccess: (data: AddToCartDataModel) -> Unit
    ): Subscriber<AddToCartDataModel> {
        return object : Subscriber<AddToCartDataModel>() {
            override fun onNext(data: AddToCartDataModel) {
                val isStatusOk = data.status.equals(STATUS_OK, true)
                val isAtcSuccess = data.data.success == 1
                if (isAtcSuccess && isStatusOk) {
                    onSuccess(data)
                } else {
                    val errorException = Exception(data.errorMessage.first())
                    onError(errorException)
                }
            }

            override fun onCompleted() {}
            override fun onError(e: Throwable?) {}
        }
    }

}