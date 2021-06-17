package com.tokopedia.emoney.viewmodel

import android.nfc.tech.IsoDep
import android.util.Log
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.common_electronic_money.util.NFCUtils
import com.tokopedia.common_electronic_money.util.NFCUtils.Companion.crypogramToByteArray
import com.tokopedia.common_electronic_money.util.NFCUtils.Companion.stringToByteArray
import com.tokopedia.common_electronic_money.util.NfcCardErrorTypeDef
import com.tokopedia.emoney.data.AttributesTapcash
import com.tokopedia.emoney.data.BalanceTapcash
import com.tokopedia.emoney.util.TapcashObjectMapper.mapTapcashtoEmoney
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class TapcashBalanceViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                  val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val issuerId = SingleLiveEvent<Int>()
    val errorCardMessage = SingleLiveEvent<String>()
    val tapcashInquiry = SingleLiveEvent<EmoneyInquiry>()

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

    private fun updateBalance(cardData: String, terminalRandomNumber: ByteArray,  balanceRawQuery: String){
        launchCatchError(block = {
            var mapParam = HashMap<String, Any>()
            mapParam.put(CARD_DATA, cardData)

            val data = withContext(dispatcher) {
                val graphqlRequest = GraphqlRequest(balanceRawQuery, BalanceTapcash::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<BalanceTapcash>()

            //val data = Gson().fromJson(DummyTapcash.dummyTapcash, BalanceTapcash::class.java)
            if (data.rechargeUpdateBalance.attributes.cryptogram.isNotEmpty()) {
                writeBalance(data.rechargeUpdateBalance.attributes, terminalRandomNumber)
            } else {
                tapcashInquiry.postValue(mapTapcashtoEmoney(data))
            }
        }){
            errorCardMessage.postValue(NfcCardErrorTypeDef.FAILED_READ_CARD)
        }
    }

    private fun writeBalance(attributesTapcash: AttributesTapcash, terminalRandomNumber: ByteArray){
        val command = writeBalanceRequest(attributesTapcash.cryptogram, terminalRandomNumber)
        val commandString = NFCUtils.toHex(command)
        Log.d("TAPCASH_REQUESTDATA", commandString)
        val writeResult = isoDep.transceive(command)
        val writeResultString = NFCUtils.toHex(writeResult)
        Log.d("TAPCASH_RESULTDATA", writeResultString)
        if (isCommandFailed(writeResult)) {
            errorCardMessage.postValue("Error")
        } else {
            errorCardMessage.postValue("Success ${attributesTapcash.amount}")
        }
    }

    private fun writeBalanceRequest(cryptogram: String, terminalRandomNumber: ByteArray): ByteArray {
       val command = COMMAND_WRITE_BALANCE.
        plus(0x03.toByte()).plus(0x14.toByte()).plus(0x02.toByte()).plus(0x14.toByte()).plus(0x03.toByte()). //fixed value
        plus(terminalRandomNumber). //Terminal Random Number
        plus(crypogramToByteArray(cryptogram)). // Cryptogram
        plus(0x00.toByte()).plus(0x00.toByte()).plus(0x00.toByte()).plus(0x00.toByte()).plus(0x00.toByte()).plus(0x00.toByte()).plus(0x00.toByte()).plus(0x00.toByte()). // fixed value
        plus(0x18.toByte()) //LE field

        return command
    }

    /**
     * @param byteArray comes from result of tranceive commands
     * check whether the byteArray have other than 9000 as flag if command fail
     */
    private fun isCommandFailed(byteArray: ByteArray): Boolean {
        val len: Int = byteArray.size
        return ((byteArray[len - 2].compareTo(0x90.toByte())) != 0) ||
                (byteArray[len - 1].compareTo(0x00.toByte()) != 0)
    }

    /**
     * get the bytearray for request secure read purse
     */
    private fun secureReadPurse(terminalRandomNumber: ByteArray): ByteArray {
        return COMMAND_SECURE_PURSE.plus(0x12.toByte()).plus(0x01.toByte()).plus(terminalRandomNumber) //Data Field
                .plus(0x00.toByte()) // LE Data Field https://stackoverflow.com/questions/51331045/how-to-send-a-command-apdu-to-a-hce-device
    }

    /**
     * this method should return 184 char, card data mapped by this method
     */
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

    /**
     * for getting the random string that needed as TERMINAL_RANDOM_NUMBER
     */
    private fun getRandomString() : String {
        val allowedChars = ('A'..'F') + ('0'..'9')
        return (1..8)
                .map { allowedChars.random() }
                .joinToString("")
    }

    /**
     * get the first index of each needed from secure purse
     */
    private fun indexBegin(index: Int): Int{
        return (index * 2) - 2
    }

    /**
     * get the last index of each needed from secure purse
     */
    private fun indexEnd(index: Int): Int{
        return (index * 2)
    }

    /**
     * get the substring of each needed from secure purse
     */
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

        private val COMMAND_WRITE_BALANCE =  byteArrayOf(
                0x90.toByte(),  // CLA Class
                0x36.toByte(),  // INS Instruction
                0x14.toByte(),  // P1  Parameter 1
                0x01.toByte(),  // P2  Parameter 2
                0x25.toByte() //  LC Data Field
        )
    }

}