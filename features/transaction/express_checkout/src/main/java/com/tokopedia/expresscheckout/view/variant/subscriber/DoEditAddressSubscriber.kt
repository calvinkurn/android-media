package com.tokopedia.expresscheckout.view.variant.subscriber

import com.tokopedia.expresscheckout.view.variant.CheckoutVariantContract
import org.json.JSONException
import org.json.JSONObject
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 12/02/19.
 */

class DoEditAddressSubscriber(val view: CheckoutVariantContract.View?,
                              val presenter: CheckoutVariantContract.Presenter,
                              val latitude: String,
                              val longitude: String)
    : Subscriber<String>() {

    override fun onCompleted() {
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
    }

    override fun onNext(stringResponse: String?) {
        view?.hideLoading()
        var response: JSONObject? = null
        var statusSuccess: Boolean
        try {
            response = JSONObject(stringResponse)
            val statusCode = response.getJSONObject("data").getInt("is_success")
            statusSuccess = statusCode == 1
        } catch (e: JSONException) {
            e.printStackTrace()
            statusSuccess = false
        }

        if (response != null && statusSuccess) {
            view?.showDurationOptions(latitude, longitude)
        } else {
            view?.showErrorPinpoint()
        }
    }

}