package com.tokopedia.payment.utils

import android.app.Application
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

class PaymentPageTimeOutLogging(application: Application) {

    private val topPayLocalCacheHandler by lazy { LocalCacheHandler(application.applicationContext, CACHE_TIME_OUT) }

    private fun isPrevTransactionTimedOut() : Boolean{
        return topPayLocalCacheHandler.getBoolean(CACHE_KEY_IS_LAST_TRANS_TIME_OUT, false)
    }

    fun logCurrentPaymentPageTimeOut(url: String?, errorCode : String, description : String?){
        topPayLocalCacheHandler.putBoolean(CACHE_KEY_IS_LAST_TRANS_TIME_OUT, true)
        topPayLocalCacheHandler.applyEditor()
        ServerLogger.log(Priority.P2, "BUYER_FLOW_PAYMENT",
            mapOf("type" to url.toString(),
                "error_code" to errorCode,
                "desc" to description.orEmpty()
            ))
    }

    fun logPaymentPageSuccessAfterTimeOut(url: String?) {
        url?.let {
            if(isPrevTransactionTimedOut()){
                topPayLocalCacheHandler.putBoolean(CACHE_KEY_IS_LAST_TRANS_TIME_OUT, false)
                topPayLocalCacheHandler.applyEditor()
                ServerLogger.log(
                    Priority.P2, "BUYER_FLOW_PAYMENT",
                    mapOf("type" to url.toString(),
                        "error_code" to "",
                        "desc" to "Retry Success"
                    ))
            }
        }
    }


    companion object{
        private const val CACHE_TIME_OUT = "cache_top_pay_time_out"
        private const val CACHE_KEY_IS_LAST_TRANS_TIME_OUT = "cache_key_is_last_trans_time_out"
    }
}
