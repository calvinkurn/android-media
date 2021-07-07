package com.tokopedia.emoney.viewmodel

import android.nfc.tech.IsoDep
import android.util.Log
import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.common_electronic_money.util.NFCUtils
import com.tokopedia.common_electronic_money.util.NFCUtils.Companion.stringToByteArrayRadix
import com.tokopedia.common_electronic_money.util.NfcCardErrorTypeDef
import com.tokopedia.emoney.data.BalanceTapcash
import com.tokopedia.emoney.util.TapcashObjectMapper.mapTapcashtoEmoney
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class TapcashBalanceViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                  val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    private var errorCardMessageMutable = SingleLiveEvent<Throwable>()
    val errorCardMessage: LiveData<Throwable>
        get() = errorCardMessageMutable

    private var errorInquiryMutable = SingleLiveEvent<Throwable>()
    val errorInquiry: LiveData<Throwable>
        get() = errorInquiryMutable

    private var tapcashInquiryMutable = SingleLiveEvent<EmoneyInquiry>()
    val tapcashInquiry: LiveData<EmoneyInquiry>
        get() = tapcashInquiryMutable

    lateinit var isoDep: IsoDep

    fun processTapCashTagIntent(isoDep: IsoDep, balanceRawQuery: String) {
        //do something with tagFromIntent
        if (isoDep != null) {
            run {
                try {
                    val terminalRandomNumber = stringToByteArrayRadix(getRandomString())
                    this.isoDep = isoDep
                    isoDep.connect()
                    isoDep.timeout = TRANSCEIVE_TIMEOUT_IN_SEC // 5 sec time out

                    val challangeRequest = NFCUtils.toHex(COMMAND_GET_CHALLENGE)
                    val result = isoDep.transceive(COMMAND_GET_CHALLENGE)
                    if (isCommandFailed(result)) {
                        isoDep.close()
                        ServerLogger.log(Priority.P2, TAPCASH_TAG, mapOf("err" to "TAPCASH_ERROR_PURSE_CHALLANGE_FAILED"))
                        errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                    } else {
                        val resultString = NFCUtils.toHex(result)
                        val securePurseRequest = NFCUtils.toHex(secureReadPurse(terminalRandomNumber))
                        val secureResult = isoDep.transceive(secureReadPurse(terminalRandomNumber))
                        if (isCommandFailed(secureResult)) {
                            ServerLogger.log(Priority.P2, TAPCASH_TAG, mapOf("err" to "TAPCASH_ERROR_SECURE_PURSE_FAILED"))
                            errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                        } else {
                            val secureResultString = NFCUtils.toHex(secureResult)
                            val cardData = getCardData(secureResultString, NFCUtils.toHex(terminalRandomNumber), resultString)
                            if (!cardData.isNullOrEmpty()) {
                                updateBalance(cardData, terminalRandomNumber, balanceRawQuery)
                            } else {
                                ServerLogger.log(Priority.P2, TAPCASH_TAG, mapOf("err" to "TAPCASH_ERROR_CARD_DATA_EMPTY"))
                                errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                            }
                        }
                    }
                } catch (e: IOException) {
                    isoDep.close()
                    ServerLogger.log(Priority.P2, TAPCASH_TAG, mapOf("err" to "TAPCASH_ERROR_ISODEP_ISSUE_IOException"))
                    errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                }
            }
        } else {
            ServerLogger.log(Priority.P2, TAPCASH_TAG, mapOf("err" to "TAPCASH_ERROR_ISODEP_NULL"))
            errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
        }
    }

    private fun updateBalance(cardData: String,
                              terminalRandomNumber: ByteArray,
                              balanceRawQuery: String) {
        launchCatchError(block = {
            val mapParam = HashMap<String, Any>()
            mapParam.put(CARD_DATA, cardData)

            val data = withContext(dispatcher) {
                val graphqlRequest = GraphqlRequest(balanceRawQuery, BalanceTapcash::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<BalanceTapcash>()

            if (data.rechargeUpdateBalance.attributes.cryptogram.isNotEmpty()) {
                writeBalance(data, terminalRandomNumber)
            } else {
                tapcashInquiryMutable.postValue(mapTapcashtoEmoney(data, isCheckBalanceTapcash = true))
            }
        }) {
            ServerLogger.log(Priority.P2, TAPCASH_TAG, mapOf("err" to "TAPCASH_ERROR_UPDATE_BALANCE:${it.message}"))
            errorInquiryMutable.postValue(it)
        }
    }

    fun writeBalance(tapcash: BalanceTapcash, terminalRandomNumber: ByteArray) {
        val attributesTapcash = tapcash.rechargeUpdateBalance.attributes
        if (::isoDep.isInitialized && isoDep.isConnected) {
            try {
                val command = writeBalanceRequest(attributesTapcash.cryptogram, terminalRandomNumber)
                val commandString = NFCUtils.toHex(command)
                val writeResult = isoDep.transceive(command)
                val writeResultString = NFCUtils.toHex(writeResult)
                if (isCommandFailed(writeResult)) {
                    ServerLogger.log(Priority.P2, TAPCASH_TAG, mapOf("err" to "TAPCASH_ERROR_ISSUE_WRITE_FAILED"))
                    errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                } else if (writeResultString.length == MAX_WRITE_RESULT_SIZE) {
                    tapcashInquiryMutable.postValue(mapTapcashtoEmoney(tapcash, getStringFromNormalPosition(writeResultString, 48, 54)))
                } else if (writeResultString.length == MAX_WRITE_RESULT_SIZE_V6) {
                    recheckBalanceSecurePurse(tapcash, terminalRandomNumber)
                } else {
                    ServerLogger.log(Priority.P2, TAPCASH_TAG, mapOf("err" to "TAPCASH_ERROR_ISSUE_WRITE_RESULT_LENGTH"))
                    errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                }
            } catch (e: IOException) {
                isoDep.close()
                ServerLogger.log(Priority.P2, TAPCASH_TAG, mapOf("err" to "TAPCASH_ERROR_ISODEP_ISSUE_WRITE_IOException"))
                errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
            }
        } else {
            ServerLogger.log(Priority.P2, TAPCASH_TAG, mapOf("err" to "TAPCASH_ERROR_ISODEP_ISSUE_WRITE_IS_NOT_CONNECTED"))
            errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
        }
    }

    fun recheckBalanceSecurePurse(tapcash: BalanceTapcash, terminalRandomNumber: ByteArray) {
        if (::isoDep.isInitialized && isoDep.isConnected) {
            try {
                val result = isoDep.transceive(COMMAND_GET_CHALLENGE)
                val secureResult = isoDep.transceive(secureReadPurse(terminalRandomNumber))
                if (isCommandFailed(secureResult)) {
                    ServerLogger.log(Priority.P2, TAPCASH_TAG, mapOf("err" to "TAPCASH_ERROR_RECHECK_BALANCE_SECURE_PURSE_FAILED"))
                    errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                } else {
                    val secureResultString = NFCUtils.toHex(secureResult)
                    tapcashInquiryMutable.postValue(mapTapcashtoEmoney(tapcash, getStringFromNormalPosition(secureResultString, 4, 10)))
                }
            } catch (e: IOException) {
                isoDep.close()
                ServerLogger.log(Priority.P2, TAPCASH_TAG, mapOf("err" to "TAPCASH_ERROR_ISODEP_RECHECK_BALANCE_IOException"))
                errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
            }
        } else {
            ServerLogger.log(Priority.P2, TAPCASH_TAG, mapOf("err" to "TAPCASH_ERROR_ISODEP_RECHECK_BALANCE_IS_NOT_CONNECTED"))
            errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
        }
    }

    /**
     * @param cryptogram comes from backend gql
     * @param terminalRandomNumber comes from random generate string and convert it to byte
     * @return value is byteArray request for write command isoDep
     */

    private fun writeBalanceRequest(cryptogram: String, terminalRandomNumber: ByteArray): ByteArray {
        val command = COMMAND_WRITE_BALANCE.plus(0x03.toByte()).plus(0x14.toByte()).plus(0x02.toByte()).plus(0x14.toByte()).plus(0x03.toByte()). //fixed value
        plus(terminalRandomNumber). //Terminal Random Number
        plus(stringToByteArrayRadix(getStringFromNormalPosition(cryptogram, 32, 64))). // Cryptogram
        plus(0x00.toByte()).plus(0x00.toByte()).plus(0x00.toByte()).plus(0x00.toByte()).plus(0x00.toByte()).plus(0x00.toByte()).plus(0x00.toByte()).plus(0x00.toByte()). // fixed value
        plus(0x18.toByte()) //LE field

        return command
    }

    /**
     * @param byteArray comes from result of tranceive commands
     * @return check whether the byteArray have other than 9000 as flag if command fail
     */
    private fun isCommandFailed(byteArray: ByteArray): Boolean {
        val len: Int = byteArray.size
        return ((byteArray[len - 2].compareTo(0x90.toByte())) != 0) ||
                (byteArray[len - 1].compareTo(0x00.toByte()) != 0)
    }

    /**
     * @param terminalRandomNumber comes from generate random string and convert it to byte
     * @return the bytearray for request secure read purse
     */
    private fun secureReadPurse(terminalRandomNumber: ByteArray): ByteArray {
        return COMMAND_SECURE_PURSE.plus(0x12.toByte()).plus(0x01.toByte()).plus(terminalRandomNumber) //Data Field
                .plus(0x00.toByte()) // LE Data Field https://stackoverflow.com/questions/51331045/how-to-send-a-command-apdu-to-a-hce-device
    }

    /**
     * @param securePurse should comes from securePurse response result
     * @return should return 184 char, card data mapped by this method
     */
    fun getCardData(securePurse: String,
                    terminalRandomNumber: String,
                    cardRandomNumber: String
    ): String? {
        if (securePurse.isNotEmpty()
                && securePurse.length == MAX_SECURE_PURSE_LENGTH
                && terminalRandomNumber.isNotEmpty()
                && terminalRandomNumber.length == MAX_16_LENGTH
                && cardRandomNumber.isNotEmpty()
                && cardRandomNumber.length == MAX_CHALLAGE_LENGTH) {
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
        } else return ""
    }

    /**
     * @return the random 16 string that needed as TERMINAL_RANDOM_NUMBER
     */
    fun getRandomString(): String {
        val allowedChars = ('A'..'F') + ('0'..'9')
        return (1..16)
                .map { allowedChars.random() }
                .joinToString("")
    }

    /**
     * @return the first index of each needed from secure purse
     */
    private fun indexBegin(index: Int): Int {
        return (index * 2) - 2
    }

    /**
     * @return the last index of each needed from secure purse
     */
    private fun indexEnd(index: Int): Int {
        return (index * 2)
    }

    /**
     * get the substring of each needed from secure purse
     */
    fun getStringFromPosition(securePurse: String, indexBegin: Int, indexEnd: Int): String {
        return securePurse.substring(indexBegin(indexBegin), indexEnd(indexEnd))
    }

    /**
     * get the substring of each needed from normal position
     */
    fun getStringFromNormalPosition(string: String, indexBegin: Int, indexEnd: Int): String {
        return string.substring(indexBegin, indexEnd)
    }


    companion object {

        const val CARD_DATA = "cardData"

        private const val TAPCASH_TAG = "TAPCASH"
        private const val TRANSCEIVE_TIMEOUT_IN_SEC = 5000

        private const val MAX_WRITE_RESULT_SIZE = 100
        private const val MAX_WRITE_RESULT_SIZE_V6 = 52
        private const val MAX_SECURE_PURSE_LENGTH = 230
        private const val MAX_16_LENGTH = 16
        private const val MAX_CHALLAGE_LENGTH = 20

        private const val FIX_VALUE = "0001"
        private const val FIX_VALUE_6TH_ROW = "00000000"

        val COMMAND_GET_CHALLENGE = byteArrayOf(
                0x00.toByte(),  // CLA Class
                0x84.toByte(),  // INS Instruction
                0x00.toByte(),  // P1  Parameter 1
                0x00.toByte(),  // P2  Parameter 2
                0x08.toByte() // // LE Data Field
        )

        val COMMAND_SECURE_PURSE = byteArrayOf(
                0x90.toByte(),  // CLA Class
                0x32.toByte(),  // INS Instruction
                0x03.toByte(),  // P1  Parameter 1
                0x00.toByte(),  // P2  Parameter 2
                0x0A.toByte() //  LC Data Field
        )

        val COMMAND_WRITE_BALANCE = byteArrayOf(
                0x90.toByte(),  // CLA Class
                0x36.toByte(),  // INS Instruction
                0x14.toByte(),  // P1  Parameter 1
                0x01.toByte(),  // P2  Parameter 2
                0x25.toByte() //  LC Data Field
        )
    }

}