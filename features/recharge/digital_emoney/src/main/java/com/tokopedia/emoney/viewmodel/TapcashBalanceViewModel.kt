package com.tokopedia.emoney.viewmodel

import android.nfc.tech.IsoDep
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

    private var errorWriteMutable = SingleLiveEvent<Throwable>()
    val errorWrite: LiveData<Throwable>
        get() = errorWriteMutable

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
                        errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                    } else {
                        val resultString = NFCUtils.toHex(result)
                        val securePurseRequest = NFCUtils.toHex(secureReadPurse(terminalRandomNumber))
                        val secureResult = isoDep.transceive(secureReadPurse(terminalRandomNumber))
                        if (isCommandFailed(secureResult)) {
                            errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                        } else {
                            val secureResultString = NFCUtils.toHex(secureResult)
                            val cardData = getCardData(secureResultString, NFCUtils.toHex(terminalRandomNumber), resultString)
                            if (!cardData.isNullOrEmpty()) {
                                updateBalance(cardData, terminalRandomNumber, balanceRawQuery)
                            } else {
                                errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                            }
                        }
                    }
                } catch (e: IOException) {
                    isoDep.close()
                    errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                }
            }
        } else {
            errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
        }
    }

    private fun updateBalance(cardData: String,
                              terminalRandomNumber: ByteArray,
                              balanceRawQuery: String) {
        launchCatchError(block = {
            val mapParam = HashMap<String, Any>()
            mapParam.put(CARD_DATA, cardData)

            val response = withContext(dispatcher) {
                val graphqlRequest = GraphqlRequest(balanceRawQuery, BalanceTapcash::class.java, mapParam)
                graphqlRequest.setUrlPath(URL_PATH)
                graphqlRepository.response(listOf(graphqlRequest))
            }

            val errors = response.getError(BalanceTapcash::class.java)

            if(errors.isNullOrEmpty()) {
                val data = response.getSuccessData<BalanceTapcash>()
                if (data.rechargeUpdateBalance.attributes.cryptogram.isNotEmpty()) {
                    writeBalance(data, terminalRandomNumber)
                } else {
                    tapcashInquiryMutable.postValue(mapTapcashtoEmoney(data, isCheckBalanceTapcash = true))
                }
            } else {
                val firstError = errors.firstOrNull()
                if (firstError?.extensions?.developerMessage?.contains(ERROR_GRPC) ?: false){
                    ServerLogger.log(Priority.P2, TAPCASH_TAG, mapOf("err" to "Error GRPC Tapcash"))
                }

                throw(MessageErrorException(firstError?.message))

            }
        }) {
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
                    errorWriteMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_WRITE_CARD_TAPCASH))
                } else {
                    recheckBalanceSecurePurse(tapcash, terminalRandomNumber)
                }
            } catch (e: IOException) {
                isoDep.close()
                errorWriteMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_WRITE_CARD_TAPCASH))
            }
        } else {
            errorWriteMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_WRITE_CARD_TAPCASH))
        }
    }

    fun recheckBalanceSecurePurse(tapcash: BalanceTapcash, terminalRandomNumber: ByteArray) {
        if (::isoDep.isInitialized && isoDep.isConnected) {
            try {
                val result = isoDep.transceive(COMMAND_GET_CHALLENGE)
                val secureResult = isoDep.transceive(secureReadPurse(terminalRandomNumber))
                if (isCommandFailed(secureResult)) {
                    errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                } else {
                    val secureResultString = NFCUtils.toHex(secureResult)
                    tapcashInquiryMutable.postValue(mapTapcashtoEmoney(tapcash, getStringFromNormalPosition(secureResultString, positionRandomPurseBalanceStart, positionRandomPurseBalanceEnd)))
                }
            } catch (e: IOException) {
                isoDep.close()
                errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
            }
        } else {
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
        plus(stringToByteArrayRadix(getStringFromNormalPosition(cryptogram, cryptogramPositionStart, cryptogramPositionEnd))). // Cryptogram
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
                    getStringFromPosition(securePurse, positionCANStart, positionCANEnd) + // CAN
                    getStringFromPosition(securePurse, positionCSNStart, positionCSNEnd) + //CSN
                    terminalRandomNumber +
                    cardRandomNumber.substring(positionCardRandomStart, positionCardRandomEnd) +
                    FIX_VALUE_6TH_ROW +
                    getStringFromPosition(securePurse, positionPurseStatus, positionPurseStatus) + // Purse Status
                    getStringFromPosition(securePurse, positionPurseBalanceStart, positionPurseBalanceEnd) + // Purse Balance
                    getStringFromPosition(securePurse, positionLastCreditTRPStart, positionLastCreditTRPEnd) + // Last Credit TRP
                    getStringFromPosition(securePurse, positionLastCreditHeaderStart, positionLastCreditHeaderEnd) + // Last Credit Header
                    getStringFromPosition(securePurse, positionLastTRXTRPStart, positionLastTRXTRPEnd) + // Last TRX TRP
                    getStringFromPosition(securePurse, positionLastTRXRecordStart, positionLastTRXRecordEnd) + // Last TRX Record
                    getStringFromPosition(securePurse, positionBadCounter, positionBadCounter) + // Bad Bebt Counter
                    getStringFromPosition(securePurse, positionLastTRXOptionByte, positionLastTRXOptionByte) + // Last Trx Debit Option Byte
                    getStringFromPosition(securePurse, positionFirstEightByteStart, positionFirstEightByteEnd) + // 1st 8 byte eData
                    getStringFromPosition(securePurse, positionLastCounterStart, positionLastCounterEnd) // last counter
            return result
        } else return ""
    }

    /**
     * @return the random 16 string that needed as TERMINAL_RANDOM_NUMBER
     */
    fun getRandomString(): String {
        val allowedChars = ('A'..'F') + ('0'..'9')
        return (positionRandomStringStart..positionRandomStringEnd)
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

        private const val TRANSCEIVE_TIMEOUT_IN_SEC = 7000

        private const val MAX_SECURE_PURSE_LENGTH = 230
        private const val MAX_16_LENGTH = 16
        private const val MAX_CHALLAGE_LENGTH = 20

        private const val FIX_VALUE = "0001"
        private const val FIX_VALUE_6TH_ROW = "00000000"

        //CAN
        private const val positionCANStart = 9
        private const val positionCANEnd = 16
        //CSN
        private const val positionCSNStart = 17
        private const val positionCSNEnd = 24
        //Card Random
        private const val positionCardRandomStart = 0
        private const val positionCardRandomEnd = 16
        //Purse Status
        private const val positionPurseStatus = 2
        //Purse Balance
        private const val positionPurseBalanceStart = 3
        private const val positionPurseBalanceEnd = 5
        //Last Credit TRP
        private const val positionLastCreditTRPStart = 29
        private const val positionLastCreditTRPEnd = 32
        //Last Credit Header
        private const val positionLastCreditHeaderStart = 33
        private const val positionLastCreditHeaderEnd = 40
        //Last TRX TRP
        private const val positionLastTRXTRPStart = 43
        private const val positionLastTRXTRPEnd = 46
        //Last TRX Record
        private const val positionLastTRXRecordStart = 47
        private const val positionLastTRXRecordEnd = 62
        //Bad Debt Counter
        private const val positionBadCounter = 64
        //Last TRX Option
        private const val positionLastTRXOptionByte = 95
        //First Eight Byte
        private const val positionFirstEightByteStart = 96
        private const val positionFirstEightByteEnd = 103
        //Last Counter
        private const val positionLastCounterStart = 104
        private const val positionLastCounterEnd = 111

        private const val cryptogramPositionStart = 32
        private const val cryptogramPositionEnd = 64

        private const val positionRandomStringStart = 1
        private const val positionRandomStringEnd = 16

        private const val positionRandomPurseBalanceStart = 4
        private const val positionRandomPurseBalanceEnd = 10

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

        const val URL_PATH = "graphql/recharge/rechargeUpdateBalanceEmoneyBniTapcash"
        private const val TAPCASH_TAG = "RECHARGE_TAPCASH"
        private const val ERROR_GRPC = "GRPC timeout"
    }

}
