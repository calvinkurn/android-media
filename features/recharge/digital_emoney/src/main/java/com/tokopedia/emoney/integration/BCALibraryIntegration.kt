package com.tokopedia.emoney.integration

import com.tokopedia.emoney.integration.data.JNIResult

interface BCALibraryIntegration {

    fun bcaVersionDll(): JNIResult

    fun bcaIsMyCard(): JNIResult

    fun bcaCheckBalance(): JNIResult

    fun bcaSetConfig(strConfig: String): JNIResult

    fun bcaGetConfig(): JNIResult

    fun bcaDataSession1(strTransactionId: String, ATD: String, strCurrDateTime: String): JNIResult

    fun bcaDataSession2(responseData: String): JNIResult

    fun bcaTopUp1(strTransactionId: String, ATD: String, strAccessCardNumber: String, strAccessCode: String, strCurrDateTime: String, lngAmount: Long): JNIResult

    fun bcaTopUp2(responseData: String): JNIResult

    fun bcaDataReversal(strTransactionId: String, ATD: String): JNIResult

    fun bcaLastBCATopUp(): JNIResult

    fun bcaDataCardInfo(strTransactionId: String, ATD: String): JNIResult
}
