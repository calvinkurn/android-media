package com.tokopedia.pms.howtopay_native.domain

import android.os.Bundle
import com.google.gson.Gson
import com.tokopedia.pms.howtopay_native.data.model.AppLinkPaymentInfo
import com.tokopedia.pms.howtopay_native.util.InvalidAppLinkException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import org.json.JSONObject
import javax.inject.Inject

class AppLinkPaymentUseCase @Inject constructor() : UseCase<AppLinkPaymentInfo>() {

    fun getAppLinkPaymentInfo(bundle: Bundle, onSuccess: (AppLinkPaymentInfo) -> Unit,
                              onError: (Throwable) -> Unit) {
        val cloneBundle = bundle.clone() as Bundle
        val requestParams = getRequestParam(cloneBundle)
        this.execute(onSuccess, onError, requestParams)
    }

    override suspend fun executeOnBackground(): AppLinkPaymentInfo {
        val bundle = useCaseRequestParams.getObject(KEY_BUNDLE) as Bundle
        try {
            val json = JSONObject()
            val keys = bundle.keySet()
            for (key in keys)
                json.put(key, bundle.getString(key, ""))
            val appLinkPaymentInfo: AppLinkPaymentInfo = Gson().fromJson(json.toString(), AppLinkPaymentInfo::class.java)
            if (appLinkPaymentInfo.bank_code.isNullOrBlank())
                appLinkPaymentInfo.bank_code = null
            return appLinkPaymentInfo
        } catch (e: Exception) {
            throw InvalidAppLinkException()
        }
    }

    private fun getRequestParam(bundle: Bundle): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putObject(KEY_BUNDLE, bundle)
        return requestParams
    }

    companion object {
        private const val KEY_BUNDLE = "key_bundle"
    }
}