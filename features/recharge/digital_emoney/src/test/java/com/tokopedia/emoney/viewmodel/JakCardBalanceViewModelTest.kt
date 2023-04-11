package com.tokopedia.emoney.viewmodel

import android.nfc.tech.IsoDep
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.common_electronic_money.util.NFCUtils
import com.tokopedia.config.GlobalConfig
import com.tokopedia.emoney.domain.request.JakCardRequestMapper
import com.tokopedia.emoney.domain.response.JakCardAttribute
import com.tokopedia.emoney.domain.response.JakCardData
import com.tokopedia.emoney.domain.response.JakCardResponse
import com.tokopedia.emoney.domain.response.JakCardResponseMapper
import com.tokopedia.emoney.domain.usecase.GetJakCardUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.spyk
import junit.framework.Assert
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import java.net.SocketTimeoutException

class JakCardBalanceViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var jakCardUseCase: GetJakCardUseCase

    @MockK
    lateinit var isoDep: IsoDep

    private lateinit var jakCardBalanceViewModel: JakCardBalanceViewModel
    private lateinit var emptyByteNFC: ByteArray

    private val ERROR_MESSAGE = "Maaf, cek saldo belum berhasil"

    private val COMMAND_SELECT_JAK_CARD_PROD = "00A4040008"
    private val COMMAND_SELECT_JAK_CARD_STAG = "00A4040007"
    private val COMMAND_AID_JAK_CARD_PROD = "A0000005714E4A43"
    private val COMMAND_AID_JAK_CARD_STAG = "D3600000030003"
    private val COMMAND_CHECK_BALANCE = "904C000004"
    private val COMMAND_INIT_LOAD = "904000011800004E202023040610535411111111111111111111111111"
    private val COMMAND_LOAD = "904200001011223344556677880000000144D51AC8"

    private lateinit var resultFail: ByteArray
    private val resultFailString = "6700"
    private lateinit var resultSelectSuccess: ByteArray
    private val resultSelectSuccessString = "6F31B02F001001019360885008689978000000111020220622203206220100001E84800001000000000E48A301B19800000C009000"
    private lateinit var resultCheckBalanceSuccess: ByteArray
    private val resultCheckBalanceSuccessString = "00004E209000"
    private lateinit var resultInitLoadSuccess: ByteArray
    private val resultInitLoadSuccessString = "100000EA6001936088500974121300000011A300EC32CCBAE98D5CEAFFEE9000"
    private lateinit var resultLoadSuccess: ByteArray
    private val resultLoadSuccessString = "000186A0841CF19C9000"
    private val resultInitLoadSuccessStringWithoutCode = "100000EA6001936088500974121300000011A300EC32CCBAE98D5CEAFFEE"
    private val resultLoadSuccessSeparated = "000186A0841CF19C"
    private val deposit = "00000000"
    private val expiry = "20320622"
    private val cardNumber = "9360885008689978"
    private val lastBalance = 20000
    private val amount = 20000

    private val checkBalanceResponse = """
      {
              "rechargeUpdateBalanceEmoneyDkiJakcard": {
                "action": 1,
                "status": 1,
                "attributes": {
                  "card_number": "9360885008689978",
                  "cryptogram": "",
                  "last_balance": 20000,
                  "button_text": "Topup Sekarang",
                  "image_issuer": "https://images.tokopedia.net/img/img/recharge/operator/Jak-Card.png",
                  "message": "Oke, saldo kamu berhasil di-update!",
                  "stan": "",
                  "ref_no": ""
                }
              }
            }
    """.trimIndent()

    private val getPendingBalanceResponse = """
      {
              "rechargeUpdateBalanceEmoneyDkiJakcard": {
                "action": 1,
                "status": 0,
                "attributes": {
                  "card_number": "9360885008689978",
                  "cryptogram": "904000011800004E202023040610535411111111111111111111111111",
                  "last_balance": 20000,
                  "button_text": "Topup Sekarang",
                  "image_issuer": "https://images.tokopedia.net/img/img/recharge/operator/Jak-Card.png",
                  "message": "Oke, saldo kamu berhasil di-update!",
                  "stan": "",
                  "ref_no": ""
                }
              }
            }
    """.trimIndent()

    private val getPendingBalanceResponseCryptogramEmpty = """
      {
              "rechargeUpdateBalanceEmoneyDkiJakcard": {
                "action": 1,
                "status": 0,
                "attributes": {
                  "card_number": "9360885008689978",
                  "cryptogram": "",
                  "last_balance": 20000,
                  "button_text": "Topup Sekarang",
                  "image_issuer": "https://images.tokopedia.net/img/img/recharge/operator/Jak-Card.png",
                  "message": "Oke, saldo kamu berhasil di-update!",
                  "stan": "",
                  "ref_no": ""
                }
              }
            }
    """.trimIndent()

    private val topUpResponseWithoutWrite = """
      {
              "rechargeUpdateBalanceEmoneyDkiJakcard": {
                "action": 2,
                "status": 1,
                "attributes": {
                  "card_number": "9360885008689978",
                  "cryptogram": "904200001011223344556677880000000144D51AC8",
                  "last_balance": 20000,
                  "button_text": "Topup Sekarang",
                  "image_issuer": "https://images.tokopedia.net/img/img/recharge/operator/Jak-Card.png",
                  "message": "Oke, saldo kamu berhasil di-update!",
                  "stan": "000148",
                  "ref_no": "000000000460"
                }
              }
            }
    """.trimIndent()

    private val topUpResponseCryptogramEmpty = """
      {
              "rechargeUpdateBalanceEmoneyDkiJakcard": {
                "action": 2,
                "status": 0,
                "attributes": {
                  "card_number": "9360885008689978",
                  "cryptogram": "",
                  "last_balance": 20000,
                  "button_text": "Topup Sekarang",
                  "image_issuer": "https://images.tokopedia.net/img/img/recharge/operator/Jak-Card.png",
                  "message": "Oke, saldo kamu berhasil di-update!",
                  "stan": "000148",
                  "ref_no": "000000000460"
                }
              }
            }
    """.trimIndent()

    private val topUpResponse = """
      {
              "rechargeUpdateBalanceEmoneyDkiJakcard": {
                "action": 2,
                "status": 0,
                "attributes": {
                  "card_number": "9360885008689978",
                  "cryptogram": "904200001011223344556677880000000144D51AC8",
                  "last_balance": 20000,
                  "button_text": "Topup Sekarang",
                  "image_issuer": "https://images.tokopedia.net/img/img/recharge/operator/Jak-Card.png",
                  "message": "Oke, saldo kamu berhasil di-update!",
                  "stan": "000148",
                  "ref_no": "000000000460"
                }
              }
            }
    """.trimIndent()

    private val topUpConfirmationResponseError = """
      {
              "rechargeUpdateBalanceEmoneyDkiJakcard": {
                "action": 3,
                "status": 2,
                "attributes": {
                  "card_number": "9360885008689978",
                  "cryptogram": "",
                  "last_balance": 20000,
                  "button_text": "Topup Sekarang",
                  "image_issuer": "https://images.tokopedia.net/img/img/recharge/operator/Jak-Card.png",
                  "message": "Oke, saldo kamu berhasil di-update!",
                  "stan": "000148",
                  "ref_no": "000000000460"
                }
              }
            }
    """.trimIndent()

    private val topUpConfirmationResponseWrite = """
      {
              "rechargeUpdateBalanceEmoneyDkiJakcard": {
                "action": 3,
                "status": 0,
                "attributes": {
                  "card_number": "9360885008689978",
                  "cryptogram": "",
                  "last_balance": 20000,
                  "button_text": "Topup Sekarang",
                  "image_issuer": "https://images.tokopedia.net/img/img/recharge/operator/Jak-Card.png",
                  "message": "Oke, saldo kamu berhasil di-update!",
                  "stan": "000148",
                  "ref_no": "000000000460"
                }
              }
            }
    """.trimIndent()

    private val topUpConfirmationResponse = """
      {
              "rechargeUpdateBalanceEmoneyDkiJakcard": {
                "action": 3,
                "status": 1,
                "attributes": {
                  "card_number": "9360885008689978",
                  "cryptogram": "",
                  "last_balance": 20000,
                  "button_text": "Topup Sekarang",
                  "image_issuer": "https://images.tokopedia.net/img/img/recharge/operator/Jak-Card.png",
                  "message": "Oke, saldo kamu berhasil di-update!",
                  "stan": "000148",
                  "ref_no": "000000000460"
                }
              }
            }
    """.trimIndent()

    private val COMMAND_SELECT_STAG_BYTE_ARRAY = NFCUtils.stringToByteArrayRadix(COMMAND_SELECT_JAK_CARD_STAG + COMMAND_AID_JAK_CARD_STAG)
    private val COMMAND_SELECT_PROD_BYTE_ARRAY = NFCUtils.stringToByteArrayRadix(COMMAND_SELECT_JAK_CARD_PROD + COMMAND_AID_JAK_CARD_PROD)
    private val COMMAND_CHECK_BALANCE_BYTE_ARRAY = NFCUtils.stringToByteArrayRadix(COMMAND_CHECK_BALANCE)
    private val COMMAND_INIT_LOAD_BYTE_ARRAY = NFCUtils.stringToByteArrayRadix(COMMAND_INIT_LOAD)
    private val COMMAND_LOAD_BYTE_ARRAY = NFCUtils.stringToByteArrayRadix(COMMAND_LOAD)
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(NFCUtils.Companion)
        mockkStatic(GlobalConfig::class)

        emptyByteNFC = byteArrayOf()
        resultFail = NFCUtils.stringToByteArrayRadix(resultFailString)
        resultSelectSuccess = NFCUtils.stringToByteArrayRadix(resultSelectSuccessString)
        resultCheckBalanceSuccess = NFCUtils.stringToByteArrayRadix(resultCheckBalanceSuccessString)
        resultInitLoadSuccess = NFCUtils.stringToByteArrayRadix(resultInitLoadSuccessString)
        resultLoadSuccess = NFCUtils.stringToByteArrayRadix(resultLoadSuccessString)
        jakCardBalanceViewModel = spyk(JakCardBalanceViewModel(Dispatchers.Unconfined, jakCardUseCase))
    }

    private fun initSuccessData() {
        every { isoDep.close() } returns mockk()
        every { isoDep.connect() } returns mockk()
        every { isoDep.setTimeout(any()) } returns mockk()
        every { isoDep.isConnected } returns true
        every { isoDep.tag.id } returns emptyByteNFC
    }

    private fun initSuccessDataIsoDepNotConnected() {
        every { isoDep.close() } returns mockk()
        every { isoDep.connect() } returns mockk()
        every { isoDep.setTimeout(any()) } returns mockk()
        every { isoDep.isConnected } returns false
        every { isoDep.tag.id } returns emptyByteNFC
    }

    @Test
    fun `processJakCardTagIntent failed select staging process and return error` () {
        //given
        initSuccessData()
        every { GlobalConfig.isAllowDebuggingTools() } returns true
        every { isoDep.transceive(COMMAND_SELECT_STAG_BYTE_ARRAY) }  returns resultFail
        //when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep)
        //then
        assertEquals(((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message, ERROR_MESSAGE)
    }

    @Test
    fun `processJakCardTagIntent failed select production process and return error` () {
        //given
        initSuccessData()
        every { GlobalConfig.isAllowDebuggingTools() } returns false
        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) }  returns resultFail
        //when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep)
        //then
        assertEquals(((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message, ERROR_MESSAGE)
    }

    @Test
    fun `processJakCardTagIntent success select production process but checkBalance error and return error` () {
        //given
        initSuccessData()
        every { GlobalConfig.isAllowDebuggingTools() } returns false
        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) }  returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY)} returns resultFail
        //when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep)
        //then
        assertEquals(((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message, ERROR_MESSAGE)
    }

    @Test
    fun `processJakCardTagIntent success select production process but checkBalance throw exception and return error` () {
        //given
        initSuccessData()
        every { GlobalConfig.isAllowDebuggingTools() } returns false
        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) } returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY)} throws IOException()
        //when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep)
        //then
        assertEquals(((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message, ERROR_MESSAGE)
    }

    @Test
    fun `processJakCardTagIntent success select production process and select checkBalance success and pending balance empty` () {
        //given
        initSuccessData()
        val createPendingBalanceParam = JakCardRequestMapper.createGetPendingBalanceParam(
            resultSelectSuccessString,
            cardNumber,
            lastBalance
        )
        val responseCheckBalance = Gson().fromJson(checkBalanceResponse, JakCardResponse::class.java)

        every { GlobalConfig.isAllowDebuggingTools() } returns false
        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) }  returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY)} returns resultCheckBalanceSuccess

        coEvery { jakCardUseCase.execute(createPendingBalanceParam) } returns responseCheckBalance
        //when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep)
        //then
        assertEquals((jakCardBalanceViewModel.jakCardInquiry.value) as EmoneyInquiry, JakCardResponseMapper.jakCardResponseMapper(responseCheckBalance))
    }

    @Test
    fun `processJakCardTagIntent success select production process and select checkBalance failed and return error` () {
        //given
        initSuccessData()
        val createPendingBalanceParam = JakCardRequestMapper.createGetPendingBalanceParam(
            resultSelectSuccessString,
            cardNumber,
            lastBalance
        )

        every { GlobalConfig.isAllowDebuggingTools() } returns false
        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) }  returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY)} returns resultCheckBalanceSuccess

        coEvery { jakCardUseCase.execute(createPendingBalanceParam) } throws SocketTimeoutException(ERROR_MESSAGE)
        //when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep)
        //then
        assertEquals(((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message, ERROR_MESSAGE)
    }

    @Test
    fun `processJakCardTagIntent success select production process and get pending balance success but init load error and return error` () {
        //given
        initSuccessData()
        val createPendingBalanceParam = JakCardRequestMapper.createGetPendingBalanceParam(
            resultSelectSuccessString,
            cardNumber,
            lastBalance
        )
        val responsePendingBalance = Gson().fromJson(getPendingBalanceResponse, JakCardResponse::class.java)

        every { GlobalConfig.isAllowDebuggingTools() } returns false
        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) }  returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY)} returns resultCheckBalanceSuccess
        every { isoDep.transceive(COMMAND_INIT_LOAD_BYTE_ARRAY) } returns resultFail

        coEvery { jakCardUseCase.execute(createPendingBalanceParam) } returns responsePendingBalance
        //when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep)
        //then
        assertEquals(((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message, ERROR_MESSAGE)
    }

    @Test
    fun `processJakCardTagIntent success select production process and get pending balance success but init give io exception and return error` () {
        //given
        initSuccessData()
        val createPendingBalanceParam = JakCardRequestMapper.createGetPendingBalanceParam(
            resultSelectSuccessString,
            cardNumber,
            lastBalance
        )
        val responsePendingBalance = Gson().fromJson(getPendingBalanceResponse, JakCardResponse::class.java)

        every { GlobalConfig.isAllowDebuggingTools() } returns false
        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) }  returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY)} returns resultCheckBalanceSuccess
        every { isoDep.transceive(COMMAND_INIT_LOAD_BYTE_ARRAY) } throws IOException(ERROR_MESSAGE)

        coEvery { jakCardUseCase.execute(createPendingBalanceParam) } returns responsePendingBalance
        //when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep)
        //then
        assertEquals(((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message, ERROR_MESSAGE)
    }

    @Test
    fun `processJakCardTagIntent success select production process and get pending balance success but cryptogram empty and return error` () {
        //given
        initSuccessData()
        val createPendingBalanceParam = JakCardRequestMapper.createGetPendingBalanceParam(
            resultSelectSuccessString,
            cardNumber,
            lastBalance
        )
        val responsePendingBalanceCryptogramEmpty = Gson().fromJson(getPendingBalanceResponseCryptogramEmpty, JakCardResponse::class.java)

        every { GlobalConfig.isAllowDebuggingTools() } returns false
        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) }  returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY)} returns resultCheckBalanceSuccess

        coEvery { jakCardUseCase.execute(createPendingBalanceParam) } returns responsePendingBalanceCryptogramEmpty
        //when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep)
        //then
        assertEquals(((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message, ERROR_MESSAGE)
    }

    @Test
    fun `processJakCardTagIntent success select production process and get pending balance success but isodep is not connected and return error` () {
        //given
        initSuccessDataIsoDepNotConnected()
        val createPendingBalanceParam = JakCardRequestMapper.createGetPendingBalanceParam(
            resultSelectSuccessString,
            cardNumber,
            lastBalance
        )
        val responsePendingBalance = Gson().fromJson(getPendingBalanceResponse, JakCardResponse::class.java)

        every { GlobalConfig.isAllowDebuggingTools() } returns false
        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) }  returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY)} returns resultCheckBalanceSuccess

        coEvery { jakCardUseCase.execute(createPendingBalanceParam) } returns responsePendingBalance
        //when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep)
        //then
        assertEquals(((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message, ERROR_MESSAGE)
    }

    @Test
    fun `processInitLoad isoDep not initialize and return error`() {
        //when
        jakCardBalanceViewModel.processInitLoad(resultSelectSuccessString, cardNumber, lastBalance, COMMAND_INIT_LOAD)

        //then
        assertEquals(((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message, ERROR_MESSAGE)
    }

    @Test
    fun `processJakCardTagIntent success select production process and get pending balance success but init load success with no cryptogram return last balance` () {
        //given
        initSuccessData()
        val createPendingBalanceParam = JakCardRequestMapper.createGetPendingBalanceParam(
            resultSelectSuccessString,
            cardNumber,
            lastBalance
        )
        val responsePendingBalance = Gson().fromJson(getPendingBalanceResponse, JakCardResponse::class.java)

        val topUpCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry
        val createTopUpParam = JakCardRequestMapper.createGetTopUpParam(
            topUpCardData,
            cardNumber,
            lastBalance,
            amount
        )
        val topUpWithoutWrite = Gson().fromJson(topUpResponseWithoutWrite, JakCardResponse::class.java)

        every { GlobalConfig.isAllowDebuggingTools() } returns false
        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) }  returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY)} returns resultCheckBalanceSuccess
        every { isoDep.transceive(COMMAND_INIT_LOAD_BYTE_ARRAY) } returns resultInitLoadSuccess

        coEvery { jakCardUseCase.execute(createPendingBalanceParam) } returns responsePendingBalance
        coEvery { jakCardUseCase.execute(createTopUpParam) } returns topUpWithoutWrite
        //when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep)
        //then
        assertEquals((jakCardBalanceViewModel.jakCardInquiry.value) as EmoneyInquiry, JakCardResponseMapper.jakCardResponseMapper(topUpWithoutWrite))
    }

    @Test
    fun `processJakCardTagIntent success select production process and get pending balance success but init load success return error` () {
        //given
        initSuccessData()
        val createPendingBalanceParam = JakCardRequestMapper.createGetPendingBalanceParam(
            resultSelectSuccessString,
            cardNumber,
            lastBalance
        )
        val responsePendingBalance = Gson().fromJson(getPendingBalanceResponse, JakCardResponse::class.java)

        val topUpCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry
        val createTopUpParam = JakCardRequestMapper.createGetTopUpParam(
            topUpCardData,
            cardNumber,
            lastBalance,
            amount
        )
        val topUpWithoutWrite = Gson().fromJson(topUpResponseWithoutWrite, JakCardResponse::class.java)

        every { GlobalConfig.isAllowDebuggingTools() } returns false
        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) }  returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY)} returns resultCheckBalanceSuccess
        every { isoDep.transceive(COMMAND_INIT_LOAD_BYTE_ARRAY) } returns resultInitLoadSuccess

        coEvery { jakCardUseCase.execute(createPendingBalanceParam) } returns responsePendingBalance
        coEvery { jakCardUseCase.execute(createTopUpParam) } throws SocketTimeoutException(ERROR_MESSAGE)
        //when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep)
        //then
        assertEquals(((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message, ERROR_MESSAGE)
    }

    @Test
    fun `processJakCardTagIntent success select production process and get pending balance success but init load success with cryptogram empty and return error ` () {
        //given
        initSuccessData()
        val createPendingBalanceParam = JakCardRequestMapper.createGetPendingBalanceParam(
            resultSelectSuccessString,
            cardNumber,
            lastBalance
        )
        val responsePendingBalance = Gson().fromJson(getPendingBalanceResponse, JakCardResponse::class.java)

        val topUpCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry
        val createTopUpParam = JakCardRequestMapper.createGetTopUpParam(
            topUpCardData,
            cardNumber,
            lastBalance,
            amount
        )
        val topUpCryptogramEmpty = Gson().fromJson(topUpResponseCryptogramEmpty, JakCardResponse::class.java)

        every { GlobalConfig.isAllowDebuggingTools() } returns false
        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) }  returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY)} returns resultCheckBalanceSuccess
        every { isoDep.transceive(COMMAND_INIT_LOAD_BYTE_ARRAY) } returns resultInitLoadSuccess

        coEvery { jakCardUseCase.execute(createPendingBalanceParam) } returns responsePendingBalance
        coEvery { jakCardUseCase.execute(createTopUpParam) } returns topUpCryptogramEmpty
        //when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep)
        //then
        assertEquals(((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message, ERROR_MESSAGE)
    }

    @Test
    fun `processJakCardTagIntent success select production process and get pending balance success but init load success with isodep not connected and return error` () {
        //given
        initSuccessDataIsoDepNotConnected()
        val createPendingBalanceParam = JakCardRequestMapper.createGetPendingBalanceParam(
            resultSelectSuccessString,
            cardNumber,
            lastBalance
        )
        val responsePendingBalance = Gson().fromJson(getPendingBalanceResponse, JakCardResponse::class.java)

        val topUpCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry
        val createTopUpParam = JakCardRequestMapper.createGetTopUpParam(
            topUpCardData,
            cardNumber,
            lastBalance,
            amount
        )
        val topUp = Gson().fromJson(topUpResponse, JakCardResponse::class.java)

        every { GlobalConfig.isAllowDebuggingTools() } returns false
        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) }  returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY)} returns resultCheckBalanceSuccess
        every { isoDep.transceive(COMMAND_INIT_LOAD_BYTE_ARRAY) } returns resultInitLoadSuccess

        coEvery { jakCardUseCase.execute(createPendingBalanceParam) } returns responsePendingBalance
        coEvery { jakCardUseCase.execute(createTopUpParam) } returns topUp
        //when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep)
        //then
        assertEquals(((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message, ERROR_MESSAGE)
    }

    @Test
    fun `processJakCardTagIntent success select production process and get pending balance success but init load success and process load error and return error` () {
        //given
        initSuccessData()
        val createPendingBalanceParam = JakCardRequestMapper.createGetPendingBalanceParam(
            resultSelectSuccessString,
            cardNumber,
            lastBalance
        )
        val responsePendingBalance = Gson().fromJson(getPendingBalanceResponse, JakCardResponse::class.java)

        val topUpCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry
        val createTopUpParam = JakCardRequestMapper.createGetTopUpParam(
            topUpCardData,
            cardNumber,
            lastBalance,
            amount
        )
        val topUp = Gson().fromJson(topUpResponse, JakCardResponse::class.java)

        every { GlobalConfig.isAllowDebuggingTools() } returns false
        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) }  returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY)} returns resultCheckBalanceSuccess
        every { isoDep.transceive(COMMAND_INIT_LOAD_BYTE_ARRAY) } returns resultInitLoadSuccess
        every { isoDep.transceive(COMMAND_LOAD_BYTE_ARRAY) } returns resultFail

        coEvery { jakCardUseCase.execute(createPendingBalanceParam) } returns responsePendingBalance
        coEvery { jakCardUseCase.execute(createTopUpParam) } returns topUp
        //when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep)
        //then
        assertEquals(((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message, ERROR_MESSAGE)
    }

    @Test
    fun `processLoad isoDep not initialize and return error`() {
        //when
        jakCardBalanceViewModel.processLoad(JakCardResponse(), "","", "", "", amount, cardNumber, lastBalance)

        //then
        assertEquals(((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message, ERROR_MESSAGE)
    }

    @Test
    fun `processLoad isoDep not initialize and cryptogram is not empty and return error`() {
        //given
        initSuccessDataIsoDepNotConnected()
        jakCardBalanceViewModel.isoDep = isoDep
        //when
        jakCardBalanceViewModel.processLoad(JakCardResponse(), "","904200001011223344556677880000000144D51AC8", "", "", amount, cardNumber, lastBalance)

        //then
        assertEquals(((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message, ERROR_MESSAGE)
    }

    @Test
    fun `processLoad load command is thrown IOException return error`() {
        //given
        initSuccessData()
        jakCardBalanceViewModel.isoDep = isoDep

        val topUp = Gson().fromJson(topUpResponse, JakCardResponse::class.java)
        val topUpCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry
        every { isoDep.transceive(COMMAND_LOAD_BYTE_ARRAY) } throws IOException(ERROR_MESSAGE)

        //when
        jakCardBalanceViewModel.processLoad(topUp, topUpCardData, topUp.data.attributes.cryptogram, topUp.data.attributes.stan, topUp.data.attributes.refNo, amount, cardNumber, lastBalance)

        //then
        assertEquals(((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message, ERROR_MESSAGE)
    }

    @Test
    fun `processLoad load command success but recheck balance is failed return error`() {
        //given
        initSuccessData()
        jakCardBalanceViewModel.isoDep = isoDep

        val topUp = Gson().fromJson(topUpResponse, JakCardResponse::class.java)
        val topUpCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry

        every { isoDep.transceive(COMMAND_LOAD_BYTE_ARRAY) } returns resultLoadSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY)} returns resultFail



        //when
        jakCardBalanceViewModel.processLoad(topUp, topUpCardData, topUp.data.attributes.cryptogram, topUp.data.attributes.stan, topUp.data.attributes.refNo, amount, cardNumber, lastBalance)

        //then
        assertEquals(((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message, ERROR_MESSAGE)
    }

    @Test
    fun `processLoad load command success and got finished proses`() {
        //given
        initSuccessData()
        jakCardBalanceViewModel.isoDep = isoDep

        val topUp = Gson().fromJson(topUpResponse, JakCardResponse::class.java)
        val topUpCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry
        val topUpConfirmationCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry + resultLoadSuccessSeparated

        val topUpConfirmation = Gson().fromJson(topUpConfirmationResponse, JakCardResponse::class.java)

        val paramGetTopUpQuery = JakCardRequestMapper.createGetTopUpConfirmationParam(topUpConfirmationCardData,
            cardNumber, lastBalance, amount, topUp.data.attributes.stan, topUp.data.attributes.refNo)

        every { isoDep.transceive(COMMAND_LOAD_BYTE_ARRAY) } returns resultLoadSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY)} returns resultCheckBalanceSuccess

        coEvery { jakCardUseCase.execute(paramGetTopUpQuery) } returns topUpConfirmation

        //when
        jakCardBalanceViewModel.processLoad(topUp, topUpCardData, topUp.data.attributes.cryptogram, topUp.data.attributes.stan, topUp.data.attributes.refNo, amount, cardNumber, lastBalance)

        //then
        assertEquals((jakCardBalanceViewModel.jakCardInquiry.value) as EmoneyInquiry, JakCardResponseMapper.jakCardResponseMapper(topUpConfirmation))
    }

    @Test
    fun `processLoad load command success and got write process but return success`() {
        //given
        initSuccessData()
        jakCardBalanceViewModel.isoDep = isoDep

        val topUp = Gson().fromJson(topUpResponse, JakCardResponse::class.java)
        val topUpCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry
        val topUpConfirmationCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry + resultLoadSuccessSeparated

        val topUpConfirmationWrite = Gson().fromJson(topUpConfirmationResponseWrite, JakCardResponse::class.java)

        val paramGetTopUpQuery = JakCardRequestMapper.createGetTopUpConfirmationParam(topUpConfirmationCardData,
            cardNumber, lastBalance, amount, topUp.data.attributes.stan, topUp.data.attributes.refNo)

        every { isoDep.transceive(COMMAND_LOAD_BYTE_ARRAY) } returns resultLoadSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY)} returns resultCheckBalanceSuccess

        coEvery { jakCardUseCase.execute(paramGetTopUpQuery) } returns topUpConfirmationWrite

        //when
        jakCardBalanceViewModel.processLoad(topUp, topUpCardData, topUp.data.attributes.cryptogram, topUp.data.attributes.stan, topUp.data.attributes.refNo, amount, cardNumber, lastBalance)

        //then
        assertEquals((jakCardBalanceViewModel.jakCardInquiry.value) as EmoneyInquiry, JakCardResponseMapper.jakCardResponseMapper(topUpConfirmationWrite))
    }

    @Test
    fun `processLoad load command success and got error process but return success`() {
        //given
        initSuccessData()
        jakCardBalanceViewModel.isoDep = isoDep

        val topUp = Gson().fromJson(topUpResponse, JakCardResponse::class.java)
        val topUpCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry
        val topUpConfirmationCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry + resultLoadSuccessSeparated

        val topUpConfirmationError = Gson().fromJson(topUpConfirmationResponseError, JakCardResponse::class.java)

        val paramGetTopUpQuery = JakCardRequestMapper.createGetTopUpConfirmationParam(topUpConfirmationCardData,
            cardNumber, lastBalance, amount, topUp.data.attributes.stan, topUp.data.attributes.refNo)

        every { isoDep.transceive(COMMAND_LOAD_BYTE_ARRAY) } returns resultLoadSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY)} returns resultCheckBalanceSuccess

        coEvery { jakCardUseCase.execute(paramGetTopUpQuery) } returns topUpConfirmationError

        //when
        jakCardBalanceViewModel.processLoad(topUp, topUpCardData, topUp.data.attributes.cryptogram, topUp.data.attributes.stan, topUp.data.attributes.refNo, amount, cardNumber, lastBalance)

        //then
        assertEquals((jakCardBalanceViewModel.jakCardInquiry.value) as EmoneyInquiry, JakCardResponseMapper.jakCardResponseMapper(topUpConfirmationError))
    }

    @Test
    fun `processLoad load command success and got exception process but return success`() {
        //given
        initSuccessData()
        jakCardBalanceViewModel.isoDep = isoDep

        val topUp = Gson().fromJson(topUpResponse, JakCardResponse::class.java)
        val topUpCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry
        val topUpConfirmationCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry + resultLoadSuccessSeparated

        val topUpConfirmation = Gson().fromJson(topUpConfirmationResponse, JakCardResponse::class.java)

        val paramGetTopUpQuery = JakCardRequestMapper.createGetTopUpConfirmationParam(topUpConfirmationCardData,
            cardNumber, lastBalance, amount, topUp.data.attributes.stan, topUp.data.attributes.refNo)

        every { isoDep.transceive(COMMAND_LOAD_BYTE_ARRAY) } returns resultLoadSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY)} returns resultCheckBalanceSuccess

        coEvery { jakCardUseCase.execute(paramGetTopUpQuery) } throws SocketTimeoutException(ERROR_MESSAGE)

        //when
        jakCardBalanceViewModel.processLoad(topUp, topUpCardData, topUp.data.attributes.cryptogram, topUp.data.attributes.stan, topUp.data.attributes.refNo, amount, cardNumber, lastBalance)

        //then
        assertEquals((jakCardBalanceViewModel.jakCardInquiry.value) as EmoneyInquiry, JakCardResponseMapper.jakCardResponseMapper(topUpConfirmation))
    }

    @Test
    fun `check isodep`() {
        //when
        jakCardBalanceViewModel.isoDep = isoDep
        //then
        assertEquals(jakCardBalanceViewModel.isoDep, isoDep)
        assert(jakCardBalanceViewModel.isIsoDepInitialized())
    }

    @Test
    fun `check isodep not initialize`() {
        //then
        assert(!jakCardBalanceViewModel.isIsoDepInitialized())
    }

}
