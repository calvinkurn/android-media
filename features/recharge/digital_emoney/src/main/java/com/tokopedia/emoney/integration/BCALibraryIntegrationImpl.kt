package com.tokopedia.emoney.integration

import com.tokopedia.emoney.integration.data.JNIResult
import javax.inject.Inject

class BCALibraryIntegrationImpl @Inject constructor (private val bcaLibrary: BCALibrary): BCALibraryIntegration {

    override fun bcaVersionDll(): JNIResult {
        return bcaLibrary.C_BCAVersionDll()
    }

    override fun bcaIsMyCard(): JNIResult {
        return bcaLibrary.C_BCAIsMyCard()
    }

    override fun bcaCheckBalance(): JNIResult {
        return bcaLibrary.C_BCACheckBalance()
    }

    override fun bcaSetConfig(strConfig: String): JNIResult {
        return bcaLibrary.C_BCASetConfig(strConfig)
    }

    override fun bcaGetConfig(): JNIResult {
        return bcaLibrary.C_BCAGetConfig()
    }

    override fun bcaDataSession1(
        strTransactionId: String,
        ATD: String,
        strCurrDateTime: String
    ): JNIResult {
        return bcaLibrary.C_BCAdataSession_1(strTransactionId, ATD, strCurrDateTime)
    }

    override fun bcaDataSession2(responseData: String): JNIResult {
        return bcaLibrary.C_BCAdataSession_2(responseData)
    }

    override fun bcaTopUp1(
        strTransactionId: String,
        ATD: String,
        strAccessCardNumber: String,
        strAccessCode: String,
        strCurrDateTime: String,
        lngAmount: Long
    ): JNIResult {
        return bcaLibrary.BCATopUp_1(strTransactionId, ATD, strAccessCardNumber, strAccessCode,
            strCurrDateTime, lngAmount)
    }

    override fun bcaTopUp2(responseData: String): JNIResult {
        return bcaLibrary.BCATopUp_2(responseData)
    }

    override fun bcaDataReversal(strTransactionId: String, ATD: String): JNIResult {
        return bcaLibrary.BCAdataReversal(strTransactionId, ATD)
    }

    override fun bcaLastBCATopUp(): JNIResult {
        return bcaLibrary.BCAlastBCATopUp()
    }

    override fun bcaDataCardInfo(strTransactionId: String, ATD: String): JNIResult {
        return bcaLibrary.BCAdataCardInfo(strTransactionId, ATD)
    }

}
