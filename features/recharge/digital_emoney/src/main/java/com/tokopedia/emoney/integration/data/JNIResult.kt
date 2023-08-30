package com.tokopedia.emoney.integration.data

data class JNIResult(val isSuccess: Int, val strLogRsp: String, val isCheckBalance: Int,
                     val balance: Int, val cardNo: String, val strConfig: String)
