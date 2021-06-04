package com.tokopedia.emoney.viewmodel

import android.nfc.tech.IsoDep
import android.util.Log
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common_electronic_money.util.NFCUtils
import com.tokopedia.common_electronic_money.util.NFCUtils.Companion.get8Byte
import com.tokopedia.common_electronic_money.util.NFCUtils.Companion.hexStringToByteArray
import com.tokopedia.common_electronic_money.util.NFCUtils.Companion.stringToByteArray
import com.tokopedia.common_electronic_money.util.SingleLiveEvent
import com.tokopedia.common_electronic_money.util.NfcCardErrorTypeDef
import com.tokopedia.emoney.data.BalanceTapcash
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class TapcashBalanceViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                  val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val issuerId = SingleLiveEvent<Int>()
    val errorCardMessage = SingleLiveEvent<String>()

    private lateinit var isoDep: IsoDep

    fun processTapCashTagIntent(isoDep: IsoDep, balanceRawQuery: String) {
        //do something with tagFromIntent
        if (isoDep != null) {
            run {
                try {
                    val terminalRandomNumber = stringToByteArray(getRandomString())
                    this.isoDep = isoDep
                    isoDep.connect()
                    isoDep.timeout = TRANSCEIVE_TIMEOUT_IN_SEC // 5 sec time out

                    val challangeRequest = NFCUtils.toHex(COMMAND_GET_CHALLENGE)
                    Log.d("CHALLANGEREQUESTSTRING", challangeRequest)
                    Log.d("TERMINALRANDOMSTRING", NFCUtils.toHex(terminalRandomNumber))
                    val result = isoDep.transceive(COMMAND_GET_CHALLENGE)
                    if (isCommandFailed(result)) {
                        isoDep.close()
                        errorCardMessage.postValue(NfcCardErrorTypeDef.CARD_NOT_FOUND)
                    } else {
                        val resultString = NFCUtils.toHex(result)
                        Log.d("CHALLANGERESULTSTRING", resultString)
                        val securePurseRequest = NFCUtils.toHex(secureReadPurse(terminalRandomNumber))
                        Log.d("SECUREREQUESTSTRING", securePurseRequest)
                        val secureResult = isoDep.transceive(secureReadPurse(terminalRandomNumber))
                        if (isCommandFailed(secureResult)) {
                            errorCardMessage.postValue(secureResult.toString())
                        } else {
                            val secureResultString = NFCUtils.toHex(secureResult)
                            Log.d("SECURERESULTSTRING", secureResultString)
                            val cardData = getCardData(secureResultString, NFCUtils.toHex(terminalRandomNumber), resultString)
                            //updateBalance(getNumberCard, balanceRawQuery)
                            Log.d("CARDDATA", cardData)
                            errorCardMessage.postValue(cardData)
                        }
                    }
                } catch (e: IOException) {
                    isoDep.close()
                    errorCardMessage.postValue(NfcCardErrorTypeDef.FAILED_READ_CARD)
                }
            }
        } else {
            errorCardMessage.postValue(NfcCardErrorTypeDef.FAILED_READ_CARD)
        }
    }

    private fun updateBalance(paramCommand: String, balanceRawQuery: String){
        launchCatchError(block = {
            var mapParam = HashMap<String, Any>()
            mapParam.put(CARD_DATA, paramCommand)

            val data = withContext(dispatcher) {
                val graphqlRequest = GraphqlRequest(balanceRawQuery, BalanceTapcash::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<BalanceTapcash>()

            errorCardMessage.postValue(data.rechargeUpdateBalance.criptogram)
        }){
            errorCardMessage.postValue(NfcCardErrorTypeDef.FAILED_READ_CARD)
        }
    }

    /**
     * @param byteArray comes from result of tranceive commands
     * check whether the byteArray have other than 9000 as flag if command fail
     */
    private fun isCommandFailed(byteArray: ByteArray): Boolean {
        val len: Int = byteArray.size
        return ((byteArray[len - 2].compareTo(0x90.toByte())) > 0) &&
                (byteArray[len - 1].compareTo(0x00.toByte()) == 0)
    }

    private fun secureReadPurse(terminalRandomNumber: ByteArray): ByteArray {
        return COMMAND_SECURE_PURSE.plus(0x12.toByte()).plus(0x01.toByte()).plus(terminalRandomNumber) //Data Field
                .plus(0x00.toByte()) // LE Data Field https://stackoverflow.com/questions/51331045/how-to-send-a-command-apdu-to-a-hce-device
    }

    private fun getCardData(securePurse: String,
                            terminalRandomNumber: String,
                            cardRandomNumber: String
    ): String {
        val result = FIX_VALUE +
                getStringFromPosition(securePurse, 9, 16) + // CAN
                getStringFromPosition(securePurse, 17, 24) + //CSN
                terminalRandomNumber +
                cardRandomNumber.substring(0, 16) +
                FIX_VALUE_6TH_ROW +
                getStringFromPosition(securePurse, 2, 2) + // Purse Status
                getStringFromPosition(securePurse, 3, 5) + // Purse Balance
                getStringFromPosition(securePurse, 29, 32) + // Last Credit TRP
                getStringFromPosition(securePurse, 33, 40) + // Last Credit Header
                getStringFromPosition(securePurse, 43, 46) + // Last TRX TRP
                getStringFromPosition(securePurse, 47, 62) + // Last TRX Record
                getStringFromPosition(securePurse, 64, 64) + // Bad Bebt Counter
                getStringFromPosition(securePurse, 95, 95) + // Last Trx Debit Option Byte
                getStringFromPosition(securePurse, 96, 103) + // 1st 8 byte eData
                getStringFromPosition(securePurse, 104, 111) // last counter
        return result
    }


    private fun getRandomString() : String {
        val allowedChars = ('A'..'F') + ('0'..'9')
        return (1..8)
                .map { allowedChars.random() }
                .joinToString("")
    }

    private fun indexBegin(index: Int): Int{
        return (index * 2) - 2
    }

    private fun indexEnd(index: Int): Int{
        return (index * 2)
    }

    private fun getStringFromPosition(securePurse: String, indexBegin:Int, indexEnd: Int): String{
        return securePurse.substring(indexBegin(indexBegin), indexEnd(indexEnd))
    }


    companion object {

        const val CARD_DATA = "cardData"

        private const val TRANSCEIVE_TIMEOUT_IN_SEC = 5000

        private const val FIX_VALUE = "0001"
        private const val FIX_VALUE_6TH_ROW = "00000000"

        private val COMMAND_GET_CHALLENGE = byteArrayOf(
                0x00.toByte(),  // CLA Class
                0x84.toByte(),  // INS Instruction
                0x00.toByte(),  // P1  Parameter 1
                0x00.toByte(),  // P2  Parameter 2
                0x08.toByte() // // LE Data Field
        )

        private val COMMAND_SECURE_PURSE =  byteArrayOf(
                0x90.toByte(),  // CLA Class
                0x32.toByte(),  // INS Instruction
                0x03.toByte(),  // P1  Parameter 1
                0x00.toByte(),  // P2  Parameter 2
                0x0A.toByte() //  LC Data Field
        )
    }

}