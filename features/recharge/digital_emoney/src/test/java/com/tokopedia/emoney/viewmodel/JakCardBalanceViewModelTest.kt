package com.tokopedia.emoney.viewmodel

import android.nfc.tech.IsoDep
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.common_electronic_money.util.ElectronicMoneyEncryption
import com.tokopedia.common_electronic_money.util.NFCUtils
import com.tokopedia.config.GlobalConfig
import com.tokopedia.emoney.domain.request.JakCardRequestMapper
import com.tokopedia.emoney.domain.response.JakCardData
import com.tokopedia.emoney.domain.response.JakCardDataEnc
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

    @RelaxedMockK
    lateinit var electronicMoneyEncryption: ElectronicMoneyEncryption

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
    private val resultSelectSuccessString =
        "6F31B02F001001019360885008689978000000111020220622203206220100001E84800001000000000E48A301B19800000C009000"
    private lateinit var resultCheckBalanceSuccess: ByteArray
    private val resultCheckBalanceSuccessString = "00004E209000"
    private lateinit var resultInitLoadSuccess: ByteArray
    private val resultInitLoadSuccessString =
        "100000EA6001936088500974121300000011A300EC32CCBAE98D5CEAFFEE9000"
    private lateinit var resultLoadSuccess: ByteArray
    private val resultLoadSuccessString = "000186A0841CF19C9000"
    private val resultInitLoadSuccessStringWithoutCode =
        "100000EA6001936088500974121300000011A300EC32CCBAE98D5CEAFFEE"
    private val resultLoadSuccessSeparated = "000186A0841CF19C"
    private val deposit = "00000000"
    private val expiry = "20320622"
    private val cardNumber = "9360885008689978"
    private val lastBalance = 20000
    private val amount = 20000
    private val mockPrivateKeyString = "7dgf8agd7isgdifushkdfhskdfh"
    private val mockPublicKeyString = "f8eh8fhe8fh8fy8eyf8eyf8yef8"
    private val pairEncryptionResult = Pair("ihcdhicjhd9cj9di", "8cdis8hdvios8dvosdvh")
    private val pairEncryptionResultTopUp = Pair("8y9s8dhiocs8dhvi8hsdoivh", "i7dya89ciad8cdi8")
    private val pairEncryptionResultTopUpConfirmation = Pair("8yfe8f9a8ef98ah9df8hd", "sfi8a8dhciadhgic7")
    private val encKeyAes = "ihcdhicjhd9cj9di"
    private val encPayloadAes = "8cdis8hdvios8dvosdvh"
    private val encKeyTopUpAes = "8y9s8dhiocs8dhvi8hsdoivh"
    private val encPayloadTopUpAes = "i7dya89ciad8cdi8"
    private val encKeyTopUpConfirmationAes = "8yfe8f9a8ef98ah9df8hd"
    private val encKeyPayloadTopUpConfirmationAes = "sfi8a8dhciadhgic7"


    private val gson = Gson()

    private val checkBalanceResponse = """
       {
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
    """.trimIndent()

    private val getPendingBalanceResponse = """
      {
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
    """.trimIndent()

    private val getPendingBalanceResponseCryptogramEmpty = """
      {
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
    """.trimIndent()

    private val topUpResponseWithoutWrite = """
      {
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
    """.trimIndent()

    private val topUpResponseCryptogramEmpty = """
        {
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
    """.trimIndent()

    private val topUpResponse = """
        {
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
    """.trimIndent()

    private val topUpConfirmationResponseError = """
        {
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
    """.trimIndent()

    private val topUpConfirmationResponseWrite = """
        {
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
    """.trimIndent()

    private val topUpConfirmationResponse = """
     {
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
    """.trimIndent()

    private val COMMAND_SELECT_STAG_BYTE_ARRAY =
        NFCUtils.stringToByteArrayRadix(COMMAND_SELECT_JAK_CARD_STAG + COMMAND_AID_JAK_CARD_STAG)
    private val COMMAND_SELECT_PROD_BYTE_ARRAY =
        NFCUtils.stringToByteArrayRadix(COMMAND_SELECT_JAK_CARD_PROD + COMMAND_AID_JAK_CARD_PROD)
    private val COMMAND_CHECK_BALANCE_BYTE_ARRAY =
        NFCUtils.stringToByteArrayRadix(COMMAND_CHECK_BALANCE)
    private val COMMAND_INIT_LOAD_BYTE_ARRAY = NFCUtils.stringToByteArrayRadix(COMMAND_INIT_LOAD)
    private val COMMAND_LOAD_BYTE_ARRAY = NFCUtils.stringToByteArrayRadix(COMMAND_LOAD)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(NFCUtils.Companion)
        mockkStatic(GlobalConfig::class)
        mockkStatic(FirebaseCrashlytics::class)

        emptyByteNFC = byteArrayOf()
        resultFail = NFCUtils.stringToByteArrayRadix(resultFailString)
        resultSelectSuccess = NFCUtils.stringToByteArrayRadix(resultSelectSuccessString)
        resultCheckBalanceSuccess = NFCUtils.stringToByteArrayRadix(resultCheckBalanceSuccessString)
        resultInitLoadSuccess = NFCUtils.stringToByteArrayRadix(resultInitLoadSuccessString)
        resultLoadSuccess = NFCUtils.stringToByteArrayRadix(resultLoadSuccessString)
        jakCardBalanceViewModel =
            spyk(JakCardBalanceViewModel(Dispatchers.Unconfined, jakCardUseCase, electronicMoneyEncryption, gson))
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
    fun `processJakCardTagIntent failed select staging process and return error`() {
        // given
        initSuccessData()
        every { isoDep.transceive(COMMAND_SELECT_STAG_BYTE_ARRAY) } returns resultFail
        // when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep, true, mockPublicKeyString, mockPrivateKeyString)
        // then
        assertEquals(
            ((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun `processJakCardTagIntent failed select production process and return error`() {
        // given
        initSuccessData()
        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) } returns resultFail
        // when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep, false, mockPublicKeyString, mockPrivateKeyString)
        // then
        assertEquals(
            ((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun `processJakCardTagIntent success select production process but checkBalance error and return error`() {
        // given
        initSuccessData()
        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) } returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY) } returns resultFail
        // when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep, false, mockPublicKeyString, mockPrivateKeyString)
        // then
        assertEquals(
            ((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun `processJakCardTagIntent success select production process but checkBalance throw exception and return error`() {
        // given
        initSuccessData()
        every { FirebaseCrashlytics.getInstance().recordException(any()) } returns mockk(relaxed = true)
        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) } returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY) } throws IOException()
        // when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep, false, mockPublicKeyString, mockPrivateKeyString)
        // then
        assertEquals(
            ((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun `processJakCardTagIntent success select production process and select checkBalance success and pending balance empty`() {
        // given
        initSuccessData()
        val createPendingBalanceParam = JakCardRequestMapper.createGetPendingBalanceParam(
            resultSelectSuccessString,
            cardNumber,
            lastBalance,
            gson
        )

        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        } returns pairEncryptionResult

        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = JakCardRequestMapper.createEncryptedParam(encParam.first, encParam.second)
        val responseCheckBalanceEnc = JakCardResponse(data = JakCardDataEnc(
            encKey =  encKeyAes,
            encPayload = encPayloadAes
        ))
        val responseCheckBalance = Gson().fromJson(checkBalanceResponse, JakCardData::class.java)

        every { electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceResponse
        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) } returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY) } returns resultCheckBalanceSuccess

        coEvery { jakCardUseCase.execute(paramGetPendingBalanceQuery) } returns responseCheckBalanceEnc
        // when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep, false, mockPublicKeyString, mockPrivateKeyString)
        // then
        assertEquals(
            (jakCardBalanceViewModel.jakCardInquiry.value) as EmoneyInquiry,
            JakCardResponseMapper.jakCardResponseMapper(responseCheckBalance)
        )
    }

    @Test
    fun `processJakCardTagIntent success select production process and select checkBalance failed and return error`() {
        // given
        initSuccessData()
        val createPendingBalanceParam = JakCardRequestMapper.createGetPendingBalanceParam(
            resultSelectSuccessString,
            cardNumber,
            lastBalance,
            gson
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        } returns pairEncryptionResult

        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = JakCardRequestMapper.createEncryptedParam(encParam.first, encParam.second)

        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) } returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY) } returns resultCheckBalanceSuccess

        coEvery { jakCardUseCase.execute(paramGetPendingBalanceQuery) } throws SocketTimeoutException(
            ERROR_MESSAGE
        )
        // when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep, false, mockPublicKeyString, mockPrivateKeyString)
        // then
        assertEquals(
            ((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun `processJakCardTagIntent success select production process and get pending balance success but init load error and return error`() {
        // given
        initSuccessData()
        val createPendingBalanceParam = JakCardRequestMapper.createGetPendingBalanceParam(
            resultSelectSuccessString,
            cardNumber,
            lastBalance,
            gson
        )

        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        } returns pairEncryptionResult

        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = JakCardRequestMapper.createEncryptedParam(encParam.first, encParam.second)
        val responseCheckBalanceEnc = JakCardResponse(data = JakCardDataEnc(
            encKey =  encKeyAes,
            encPayload = encPayloadAes
        ))

        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) } returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY) } returns resultCheckBalanceSuccess
        every { isoDep.transceive(COMMAND_INIT_LOAD_BYTE_ARRAY) } returns resultFail

        every { electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns getPendingBalanceResponse

        coEvery { jakCardUseCase.execute(paramGetPendingBalanceQuery) } returns responseCheckBalanceEnc
        // when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep, false, mockPublicKeyString, mockPrivateKeyString)
        // then
        assertEquals(
            ((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun `processJakCardTagIntent success select production process and get pending balance success but init give io exception and return error`() {
        // given
        initSuccessData()
        val createPendingBalanceParam = JakCardRequestMapper.createGetPendingBalanceParam(
            resultSelectSuccessString,
            cardNumber,
            lastBalance,
            gson
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        } returns pairEncryptionResult

        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = JakCardRequestMapper.createEncryptedParam(encParam.first, encParam.second)
        val responseCheckBalanceEnc = JakCardResponse(data = JakCardDataEnc(
            encKey =  encKeyAes,
            encPayload = encPayloadAes
        ))

        val responsePendingBalance =
            Gson().fromJson(getPendingBalanceResponse, JakCardResponse::class.java)

        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) } returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY) } returns resultCheckBalanceSuccess
        every { isoDep.transceive(COMMAND_INIT_LOAD_BYTE_ARRAY) } throws IOException(ERROR_MESSAGE)

        every { electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns getPendingBalanceResponse


        coEvery { jakCardUseCase.execute(paramGetPendingBalanceQuery) } returns responseCheckBalanceEnc
        // when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep, false, mockPublicKeyString, mockPrivateKeyString)
        // then
        assertEquals(
            ((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun `processJakCardTagIntent success select production process and get pending balance success but cryptogram empty and return error`() {
        //given
        initSuccessData()
        val createPendingBalanceParam = JakCardRequestMapper.createGetPendingBalanceParam(
            resultSelectSuccessString,
            cardNumber,
            lastBalance,
            gson
        )

        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        } returns pairEncryptionResult

        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = JakCardRequestMapper.createEncryptedParam(encParam.first, encParam.second)
        val responseCheckBalanceEnc = JakCardResponse(data = JakCardDataEnc(
            encKey =  encKeyAes,
            encPayload = encPayloadAes
        ))

        val responsePendingBalanceCryptogramEmpty =
            Gson().fromJson(getPendingBalanceResponseCryptogramEmpty, JakCardResponse::class.java)

        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) } returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY) } returns resultCheckBalanceSuccess

        every { electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns getPendingBalanceResponseCryptogramEmpty

        coEvery { jakCardUseCase.execute(paramGetPendingBalanceQuery) } returns responseCheckBalanceEnc
        //when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep, false, mockPublicKeyString, mockPrivateKeyString)
        //then
        assertEquals(
            ((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun `processJakCardTagIntent success select production process and get pending balance success but isodep is not connected and return error`() {
        //given
        initSuccessDataIsoDepNotConnected()
        val createPendingBalanceParam = JakCardRequestMapper.createGetPendingBalanceParam(
            resultSelectSuccessString,
            cardNumber,
            lastBalance,
            gson
        )

        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        } returns pairEncryptionResult

        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = JakCardRequestMapper.createEncryptedParam(encParam.first, encParam.second)
        val responseCheckBalanceEnc = JakCardResponse(data = JakCardDataEnc(
            encKey =  encKeyAes,
            encPayload = encPayloadAes
        ))

        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) } returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY) } returns resultCheckBalanceSuccess
        every { electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns getPendingBalanceResponse

        coEvery { jakCardUseCase.execute(paramGetPendingBalanceQuery) } returns responseCheckBalanceEnc
        //when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep, false, mockPublicKeyString, mockPrivateKeyString)
        //then
        assertEquals(
            ((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun `processInitLoad isoDep not initialize and return error`() {
        //when
        jakCardBalanceViewModel.processInitLoad(
            resultSelectSuccessString,
            cardNumber,
            lastBalance,
            COMMAND_INIT_LOAD,
            mockPublicKeyString,
            mockPrivateKeyString
        )

        //then
        assertEquals(
            ((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun `processJakCardTagIntent success select production process and get pending balance success but init load success with no cryptogram return last balance`() {
        //given
        initSuccessData()
        val createPendingBalanceParam = JakCardRequestMapper.createGetPendingBalanceParam(
            resultSelectSuccessString,
            cardNumber,
            lastBalance,
            gson
        )
        val responsePendingBalance =
            Gson().fromJson(getPendingBalanceResponse, JakCardResponse::class.java)

        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        } returns pairEncryptionResult

        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = JakCardRequestMapper.createEncryptedParam(encParam.first, encParam.second)
        val responseCheckBalanceEnc = JakCardResponse(data = JakCardDataEnc(
            encKey =  encKeyAes,
            encPayload = encPayloadAes
        ))

        val topUpCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry
        val createTopUpParam = JakCardRequestMapper.createGetTopUpParam(
            topUpCardData,
            cardNumber,
            lastBalance,
            amount,
            gson
        )
        val topUpWithoutWrite =
            Gson().fromJson(topUpResponseWithoutWrite, JakCardData::class.java)

        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTopUpParam)
        } returns pairEncryptionResultTopUp

        val encParamTopUp = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTopUpParam)
        val paramGetPendingBalanceQueryTopUp = JakCardRequestMapper.createEncryptedParam(encParamTopUp.first, encParamTopUp.second)
        val responseCheckBalanceEncTopUp = JakCardResponse(data = JakCardDataEnc(
            encKey =  encKeyTopUpAes,
            encPayload = encPayloadTopUpAes
        ))

        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) } returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY) } returns resultCheckBalanceSuccess
        every { isoDep.transceive(COMMAND_INIT_LOAD_BYTE_ARRAY) } returns resultInitLoadSuccess

        every { electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns getPendingBalanceResponse

        every { electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyTopUpAes, encPayloadTopUpAes)
        } returns topUpResponseWithoutWrite

        coEvery { jakCardUseCase.execute(paramGetPendingBalanceQuery) } returns responseCheckBalanceEnc
        coEvery { jakCardUseCase.execute(paramGetPendingBalanceQueryTopUp) } returns responseCheckBalanceEncTopUp
        //when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep, false, mockPublicKeyString, mockPrivateKeyString)
        //then
        assertEquals(
            (jakCardBalanceViewModel.jakCardInquiry.value) as EmoneyInquiry,
            JakCardResponseMapper.jakCardResponseMapper(topUpWithoutWrite)
        )
    }

    @Test
    fun `processJakCardTagIntent success select production process and get pending balance success but init load success return error`() {
        //given
        initSuccessData()
        val createPendingBalanceParam = JakCardRequestMapper.createGetPendingBalanceParam(
            resultSelectSuccessString,
            cardNumber,
            lastBalance,
            gson
        )
        val responsePendingBalance =
            Gson().fromJson(getPendingBalanceResponse, JakCardResponse::class.java)

        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        } returns pairEncryptionResult

        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = JakCardRequestMapper.createEncryptedParam(encParam.first, encParam.second)
        val responseCheckBalanceEnc = JakCardResponse(data = JakCardDataEnc(
            encKey =  encKeyAes,
            encPayload = encPayloadAes
        ))

        val topUpCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry
        val createTopUpParam = JakCardRequestMapper.createGetTopUpParam(
            topUpCardData,
            cardNumber,
            lastBalance,
            amount,
            gson
        )
        val topUpWithoutWrite =
            Gson().fromJson(topUpResponseWithoutWrite, JakCardResponse::class.java)

        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTopUpParam)
        } returns pairEncryptionResultTopUp

        val encParamTopUp = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTopUpParam)
        val paramGetPendingBalanceQueryTopUp = JakCardRequestMapper.createEncryptedParam(encParamTopUp.first, encParamTopUp.second)
        val responseCheckBalanceEncTopUp = JakCardResponse(data = JakCardDataEnc(
            encKey =  encKeyTopUpAes,
            encPayload = encPayloadTopUpAes
        ))

        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) } returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY) } returns resultCheckBalanceSuccess
        every { isoDep.transceive(COMMAND_INIT_LOAD_BYTE_ARRAY) } returns resultInitLoadSuccess

        every { electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns getPendingBalanceResponse

        every { electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyTopUpAes, encPayloadTopUpAes)
        } returns topUpResponseWithoutWrite

        coEvery { jakCardUseCase.execute(paramGetPendingBalanceQuery) } returns responseCheckBalanceEnc
        coEvery { jakCardUseCase.execute(paramGetPendingBalanceQueryTopUp) } throws SocketTimeoutException(
            ERROR_MESSAGE
        )
        //when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep, false, mockPublicKeyString, mockPrivateKeyString)
        //then
        assertEquals(
            ((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun `processJakCardTagIntent success select production process and get pending balance success but init load success with cryptogram empty and return error `() {
        //given
        initSuccessData()
        val createPendingBalanceParam = JakCardRequestMapper.createGetPendingBalanceParam(
            resultSelectSuccessString,
            cardNumber,
            lastBalance,
            gson
        )
        val responsePendingBalance =
            Gson().fromJson(getPendingBalanceResponse, JakCardResponse::class.java)

        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        } returns pairEncryptionResult

        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = JakCardRequestMapper.createEncryptedParam(encParam.first, encParam.second)
        val responseCheckBalanceEnc = JakCardResponse(data = JakCardDataEnc(
            encKey =  encKeyAes,
            encPayload = encPayloadAes
        ))

        val topUpCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry
        val createTopUpParam = JakCardRequestMapper.createGetTopUpParam(
            topUpCardData,
            cardNumber,
            lastBalance,
            amount,
            gson
        )
        val topUpCryptogramEmpty =
            Gson().fromJson(topUpResponseCryptogramEmpty, JakCardResponse::class.java)

        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTopUpParam)
        } returns pairEncryptionResultTopUp

        val encParamTopUp = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTopUpParam)
        val paramGetPendingBalanceQueryTopUp = JakCardRequestMapper.createEncryptedParam(encParamTopUp.first, encParamTopUp.second)
        val responseCheckBalanceEncTopUp = JakCardResponse(data = JakCardDataEnc(
            encKey =  encKeyTopUpAes,
            encPayload = encPayloadTopUpAes
        ))

        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) } returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY) } returns resultCheckBalanceSuccess
        every { isoDep.transceive(COMMAND_INIT_LOAD_BYTE_ARRAY) } returns resultInitLoadSuccess

        every { electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns getPendingBalanceResponse

        every { electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyTopUpAes, encPayloadTopUpAes)
        } returns topUpResponseCryptogramEmpty

        coEvery { jakCardUseCase.execute(paramGetPendingBalanceQuery) } returns responseCheckBalanceEnc
        coEvery { jakCardUseCase.execute(paramGetPendingBalanceQueryTopUp) } returns responseCheckBalanceEncTopUp
        //when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep, false, mockPublicKeyString, mockPrivateKeyString)
        //then
        assertEquals(
            ((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun `processJakCardTagIntent success select production process and get pending balance success but init load success with isodep not connected and return error`() {
        //given
        initSuccessDataIsoDepNotConnected()
        val createPendingBalanceParam = JakCardRequestMapper.createGetPendingBalanceParam(
            resultSelectSuccessString,
            cardNumber,
            lastBalance,
            gson
        )
        val responsePendingBalance =
            Gson().fromJson(getPendingBalanceResponse, JakCardResponse::class.java)

        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        } returns pairEncryptionResult

        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = JakCardRequestMapper.createEncryptedParam(encParam.first, encParam.second)
        val responseCheckBalanceEnc = JakCardResponse(data = JakCardDataEnc(
            encKey =  encKeyAes,
            encPayload = encPayloadAes
        ))

        val topUpCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry
        val createTopUpParam = JakCardRequestMapper.createGetTopUpParam(
            topUpCardData,
            cardNumber,
            lastBalance,
            amount,
            gson
        )
        val topUp = Gson().fromJson(topUpResponse, JakCardResponse::class.java)

        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTopUpParam)
        } returns pairEncryptionResultTopUp

        val encParamTopUp = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTopUpParam)
        val paramGetPendingBalanceQueryTopUp = JakCardRequestMapper.createEncryptedParam(encParamTopUp.first, encParamTopUp.second)
        val responseCheckBalanceEncTopUp = JakCardResponse(data = JakCardDataEnc(
            encKey =  encKeyTopUpAes,
            encPayload = encPayloadTopUpAes
        ))

        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) } returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY) } returns resultCheckBalanceSuccess
        every { isoDep.transceive(COMMAND_INIT_LOAD_BYTE_ARRAY) } returns resultInitLoadSuccess

        every { electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns getPendingBalanceResponse

        every { electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyTopUpAes, encPayloadTopUpAes)
        } returns topUpResponse

        coEvery { jakCardUseCase.execute(paramGetPendingBalanceQuery) } returns responseCheckBalanceEnc
        coEvery { jakCardUseCase.execute(paramGetPendingBalanceQueryTopUp) } returns responseCheckBalanceEncTopUp
        //when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep, false, mockPublicKeyString, mockPrivateKeyString)
        //then
        assertEquals(
            ((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun `processJakCardTagIntent success select production process and get pending balance success but init load success and process load error and return error`() {
        //given
        initSuccessData()
        val createPendingBalanceParam = JakCardRequestMapper.createGetPendingBalanceParam(
            resultSelectSuccessString,
            cardNumber,
            lastBalance,
            gson
        )
        val responsePendingBalance =
            Gson().fromJson(getPendingBalanceResponse, JakCardResponse::class.java)

        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        } returns pairEncryptionResult

        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = JakCardRequestMapper.createEncryptedParam(encParam.first, encParam.second)
        val responseCheckBalanceEnc = JakCardResponse(data = JakCardDataEnc(
            encKey =  encKeyAes,
            encPayload = encPayloadAes
        ))

        val topUpCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry
        val createTopUpParam = JakCardRequestMapper.createGetTopUpParam(
            topUpCardData,
            cardNumber,
            lastBalance,
            amount,
            gson
        )
        val topUp = Gson().fromJson(topUpResponse, JakCardResponse::class.java)

        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTopUpParam)
        } returns pairEncryptionResultTopUp

        val encParamTopUp = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTopUpParam)
        val paramGetPendingBalanceQueryTopUp = JakCardRequestMapper.createEncryptedParam(encParamTopUp.first, encParamTopUp.second)
        val responseCheckBalanceEncTopUp = JakCardResponse(data = JakCardDataEnc(
            encKey =  encKeyTopUpAes,
            encPayload = encPayloadTopUpAes
        ))

        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) } returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY) } returns resultCheckBalanceSuccess
        every { isoDep.transceive(COMMAND_INIT_LOAD_BYTE_ARRAY) } returns resultInitLoadSuccess
        every { isoDep.transceive(COMMAND_LOAD_BYTE_ARRAY) } returns resultFail

        every { electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns getPendingBalanceResponse

        every { electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyTopUpAes, encPayloadTopUpAes)
        } returns topUpResponse

        coEvery { jakCardUseCase.execute(paramGetPendingBalanceQuery) } returns responseCheckBalanceEnc
        coEvery { jakCardUseCase.execute(paramGetPendingBalanceQueryTopUp) } returns responseCheckBalanceEncTopUp
        //when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep, false, mockPublicKeyString, mockPrivateKeyString)
        //then
        assertEquals(
            ((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun `processLoad isoDep not initialize and return error`() {
        //when
        jakCardBalanceViewModel.processLoad(
            JakCardData(),
            "",
            "",
            "",
            "",
            amount,
            cardNumber,
            mockPublicKeyString,
            mockPrivateKeyString
        )

        //then
        assertEquals(
            ((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun `processLoad isoDep not initialize and cryptogram is not empty and return error`() {
        //given
        initSuccessDataIsoDepNotConnected()
        jakCardBalanceViewModel.isoDep = isoDep
        //when
        jakCardBalanceViewModel.processLoad(
            JakCardData(),
            "",
            "904200001011223344556677880000000144D51AC8",
            "",
            "",
            amount,
            cardNumber,
            mockPublicKeyString,
            mockPrivateKeyString
        )

        //then
        assertEquals(
            ((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun `processLoad load command is thrown IOException return error`() {
        //given
        initSuccessData()
        jakCardBalanceViewModel.isoDep = isoDep

        val topUp = Gson().fromJson(topUpResponse, JakCardData::class.java)
        val topUpCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry
        every { isoDep.transceive(COMMAND_LOAD_BYTE_ARRAY) } throws IOException(ERROR_MESSAGE)

        //when
        jakCardBalanceViewModel.processLoad(
            topUp,
            topUpCardData,
            topUp.attributes.cryptogram,
            topUp.attributes.stan,
            topUp.attributes.refNo,
            amount,
            cardNumber,
            mockPublicKeyString,
            mockPrivateKeyString
        )

        //then
        assertEquals(
            ((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun `processLoad load command success but recheck balance is failed return error`() {
        //given
        initSuccessData()
        jakCardBalanceViewModel.isoDep = isoDep

        val topUp = Gson().fromJson(topUpResponse, JakCardData::class.java)
        val topUpCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry

        every { isoDep.transceive(COMMAND_LOAD_BYTE_ARRAY) } returns resultLoadSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY) } returns resultFail


        //when
        jakCardBalanceViewModel.processLoad(
            topUp,
            topUpCardData,
            topUp.attributes.cryptogram,
            topUp.attributes.stan,
            topUp.attributes.refNo,
            amount,
            cardNumber,
            mockPublicKeyString,
            mockPrivateKeyString
        )

        //then
        assertEquals(
            ((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun `processLoad load command success and got finished proses`() {
        //given
        initSuccessData()
        jakCardBalanceViewModel.isoDep = isoDep

        val topUp = Gson().fromJson(topUpResponse, JakCardData::class.java)
        val topUpCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry
        val topUpConfirmationCardData =
            resultInitLoadSuccessStringWithoutCode + deposit + expiry + resultLoadSuccessSeparated

        val topUpConfirmation =
            Gson().fromJson(topUpConfirmationResponse, JakCardData::class.java)

        val paramGetTopUpQuery = JakCardRequestMapper.createGetTopUpConfirmationParam(
            topUpConfirmationCardData,
            cardNumber, lastBalance, amount, topUp.attributes.stan, topUp.attributes.refNo, gson
        )

        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, paramGetTopUpQuery)
        } returns pairEncryptionResultTopUpConfirmation

        val encParamTopUpConfirmation = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, paramGetTopUpQuery)
        val paramGetPendingBalanceQueryTopUp = JakCardRequestMapper.createEncryptedParam(encParamTopUpConfirmation.first, encParamTopUpConfirmation.second)
        val responseCheckBalanceEncTopUp = JakCardResponse(data = JakCardDataEnc(
            encKey =  encKeyTopUpConfirmationAes,
            encPayload = encKeyPayloadTopUpConfirmationAes
        ))

        every { isoDep.transceive(COMMAND_LOAD_BYTE_ARRAY) } returns resultLoadSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY) } returns resultCheckBalanceSuccess

        coEvery { jakCardUseCase.execute(paramGetPendingBalanceQueryTopUp) } returns responseCheckBalanceEncTopUp

        //when
        jakCardBalanceViewModel.processLoad(
            topUp,
            topUpCardData,
            topUp.attributes.cryptogram,
            topUp.attributes.stan,
            topUp.attributes.refNo,
            amount,
            cardNumber,
            mockPublicKeyString,
            mockPrivateKeyString
        )

        //then
        assertEquals(
            (jakCardBalanceViewModel.jakCardInquiry.value) as EmoneyInquiry,
            JakCardResponseMapper.jakCardResponseMapper(topUpConfirmation)
        )
    }

    @Test
    fun `processLoad load command success and got write process but return success`() {
        //given
        initSuccessData()
        jakCardBalanceViewModel.isoDep = isoDep

        val topUp = Gson().fromJson(topUpResponse, JakCardData::class.java)
        val topUpCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry
        val topUpConfirmationCardData =
            resultInitLoadSuccessStringWithoutCode + deposit + expiry + resultLoadSuccessSeparated

        val topUpConfirmation =
            Gson().fromJson(topUpConfirmationResponse, JakCardData::class.java)

        val paramGetTopUpQuery = JakCardRequestMapper.createGetTopUpConfirmationParam(
            topUpConfirmationCardData,
            cardNumber, lastBalance, amount, topUp.attributes.stan, topUp.attributes.refNo, gson
        )

        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, paramGetTopUpQuery)
        } returns pairEncryptionResultTopUpConfirmation

        val encParamTopUpConfirmation = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, paramGetTopUpQuery)
        val paramGetPendingBalanceQueryTopUp = JakCardRequestMapper.createEncryptedParam(encParamTopUpConfirmation.first, encParamTopUpConfirmation.second)
        val responseCheckBalanceEncTopUp = JakCardResponse(data = JakCardDataEnc(
            encKey =  encKeyTopUpConfirmationAes,
            encPayload = encKeyPayloadTopUpConfirmationAes
        ))

        every { isoDep.transceive(COMMAND_LOAD_BYTE_ARRAY) } returns resultLoadSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY) } returns resultCheckBalanceSuccess

        every { electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyTopUpConfirmationAes, encKeyPayloadTopUpConfirmationAes)
        } returns topUpConfirmationResponseWrite

        coEvery { jakCardUseCase.execute(paramGetPendingBalanceQueryTopUp) } returns responseCheckBalanceEncTopUp

        //when
        jakCardBalanceViewModel.processLoad(
            topUp,
            topUpCardData,
            topUp.attributes.cryptogram,
            topUp.attributes.stan,
            topUp.attributes.refNo,
            amount,
            cardNumber,
            mockPublicKeyString,
            mockPrivateKeyString
        )

        //then
        assertEquals(
            (jakCardBalanceViewModel.jakCardInquiry.value) as EmoneyInquiry,
            JakCardResponseMapper.jakCardResponseMapper(topUpConfirmation)
        )
    }

    @Test
    fun `processLoad load command success and got error process but return success`() {
        //given
        initSuccessData()
        jakCardBalanceViewModel.isoDep = isoDep

        val topUp = Gson().fromJson(topUpResponse, JakCardData::class.java)
        val topUpCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry
        val topUpConfirmationCardData =
            resultInitLoadSuccessStringWithoutCode + deposit + expiry + resultLoadSuccessSeparated

        val topUpConfirmationError =
            Gson().fromJson(topUpConfirmationResponse, JakCardData::class.java)

        val paramGetTopUpQuery = JakCardRequestMapper.createGetTopUpConfirmationParam(
            topUpConfirmationCardData,
            cardNumber, lastBalance, amount, topUp.attributes.stan, topUp.attributes.refNo, gson
        )

        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, paramGetTopUpQuery)
        } returns pairEncryptionResultTopUpConfirmation

        val encParamTopUpConfirmation = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, paramGetTopUpQuery)
        val paramGetPendingBalanceQueryTopUp = JakCardRequestMapper.createEncryptedParam(encParamTopUpConfirmation.first, encParamTopUpConfirmation.second)
        val responseCheckBalanceEncTopUp = JakCardResponse(data = JakCardDataEnc(
            encKey =  encKeyTopUpConfirmationAes,
            encPayload = encKeyPayloadTopUpConfirmationAes
        ))

        every { isoDep.transceive(COMMAND_LOAD_BYTE_ARRAY) } returns resultLoadSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY) } returns resultCheckBalanceSuccess

        every { electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyTopUpConfirmationAes, encKeyPayloadTopUpConfirmationAes)
        } returns topUpConfirmationResponseError

        coEvery { jakCardUseCase.execute(paramGetPendingBalanceQueryTopUp) } returns responseCheckBalanceEncTopUp

        //when
        jakCardBalanceViewModel.processLoad(
            topUp,
            topUpCardData,
            topUp.attributes.cryptogram,
            topUp.attributes.stan,
            topUp.attributes.refNo,
            amount,
            cardNumber,
            mockPublicKeyString,
            mockPrivateKeyString
        )

        //then
        assertEquals(
            (jakCardBalanceViewModel.jakCardInquiry.value) as EmoneyInquiry,
            JakCardResponseMapper.jakCardResponseMapper(topUpConfirmationError)
        )
    }

    @Test
    fun `processLoad load command success and got exception process and return success`() {
        //given
        initSuccessData()
        jakCardBalanceViewModel.isoDep = isoDep

        val topUp = Gson().fromJson(topUpResponse, JakCardData::class.java)
        val topUpCardData = resultInitLoadSuccessStringWithoutCode + deposit + expiry
        val topUpConfirmationCardData =
            resultInitLoadSuccessStringWithoutCode + deposit + expiry + resultLoadSuccessSeparated

        val topUpConfirmation =
            Gson().fromJson(topUpConfirmationResponse, JakCardData::class.java)

        val paramGetTopUpQuery = JakCardRequestMapper.createGetTopUpConfirmationParam(
            topUpConfirmationCardData,
            cardNumber, lastBalance, amount, topUp.attributes.stan, topUp.attributes.refNo, gson
        )

        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, paramGetTopUpQuery)
        } returns pairEncryptionResultTopUpConfirmation

        val encParamTopUpConfirmation = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, paramGetTopUpQuery)
        val paramGetPendingBalanceQueryTopUp = JakCardRequestMapper.createEncryptedParam(encParamTopUpConfirmation.first, encParamTopUpConfirmation.second)
        val responseCheckBalanceEncTopUp = JakCardResponse(data = JakCardDataEnc(
            encKey =  encKeyTopUpConfirmationAes,
            encPayload = encKeyPayloadTopUpConfirmationAes
        ))

        every { isoDep.transceive(COMMAND_LOAD_BYTE_ARRAY) } returns resultLoadSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY) } returns resultCheckBalanceSuccess

        every { electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyTopUpConfirmationAes, encKeyPayloadTopUpConfirmationAes)
        } returns topUpConfirmationResponseError

        coEvery { jakCardUseCase.execute(paramGetPendingBalanceQueryTopUp) } throws SocketTimeoutException(
            ERROR_MESSAGE
        )

        //when
        jakCardBalanceViewModel.processLoad(
            topUp,
            topUpCardData,
            topUp.attributes.cryptogram,
            topUp.attributes.stan,
            topUp.attributes.refNo,
            amount,
            cardNumber,
            mockPublicKeyString,
            mockPrivateKeyString
        )

        //then
        assertEquals(
            (jakCardBalanceViewModel.jakCardInquiry.value) as EmoneyInquiry,
            JakCardResponseMapper.jakCardResponseMapper(topUpConfirmation)
        )
    }

    @Test
    fun `check isodep`() {
        // when
        jakCardBalanceViewModel.isoDep = isoDep
        // then
        assertEquals(jakCardBalanceViewModel.isoDep, isoDep)
        assert(jakCardBalanceViewModel.isIsoDepInitialized())
    }

    @Test
    fun `check isodep not initialize`() {
        // then
        assert(!jakCardBalanceViewModel.isIsoDepInitialized())
    }

}
