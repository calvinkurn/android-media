package com.tokopedia.emoney.viewmodel

import android.nfc.tech.IsoDep
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.common_electronic_money.util.ElectronicMoneyEncryption
import com.tokopedia.common_electronic_money.util.NFCUtils
import com.tokopedia.emoney.domain.request.BCAFlazzRequestMapper
import com.tokopedia.emoney.domain.request.CommonBodyEnc
import com.tokopedia.emoney.domain.response.BCAFlazzData
import com.tokopedia.emoney.domain.response.BCAFlazzResponse
import com.tokopedia.emoney.domain.response.BCAFlazzResponseMapper
import com.tokopedia.emoney.domain.response.JakCardResponseMapper
import com.tokopedia.emoney.domain.usecase.GetBCAFlazzUseCase
import com.tokopedia.emoney.integration.BCALibraryIntegration
import com.tokopedia.emoney.integration.data.JNIResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class BCAFlazzBalanceViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var electronicMoneyEncryption: ElectronicMoneyEncryption

    @RelaxedMockK
    lateinit var bcaFlazzUseCase: GetBCAFlazzUseCase

    @RelaxedMockK
    private lateinit var bcaLibrary: BCALibraryIntegration

    @MockK
    lateinit var isoDep: IsoDep

    private lateinit var bcaBalanceViewModel: BCABalanceViewModel

    private val gson = Gson()

    private val mockPrivateKeyString = "7dgf8agd7isgdifushkdfhskdfh"
    private val mockPublicKeyString = "f8eh8fhe8fh8fy8eyf8eyf8yef8"
    private val encKeyAes = "ihcdhicjhd9cj9di"
    private val encPayloadAes = "8cdis8hdvios8dvosdvh"
    private val pairEncryptionResult = Pair("ihcdhicjhd9cj9di", "8cdis8hdvios8dvosdvh")
    private val encReversalKeyAes = "isduhfoshdofjsodfoi"
    private val encReversalPayloadAes = "nsidfoisnflvinsoifnvosinfovns"
    private val pairReversalEncryptionResult = Pair("isduhfoshdofjsodfoi", "nsidfoisnflvinsoifnvosinfovns")
    private val encBCALastBCATopUpKeyAes = "infinsoidnfosnodfns"
    private val encBCALastBCATopUpPayloadAes = "qereorerjoef"
    private var pairBCALastBCATopUpEncryptionResult = Pair("infinsoidnfosnodfns", "qereorerjoef")
    private val GEN_ONE = "1"
    private val GEN_TWO = "2"
    private val merchantId = "0904039009"
    private val terminalId = "9384983948"
    private val mtID = "9384983948,0904039009"
    private val strCurrDateTime = "98309849384"
    private val ATD = "989389898984"
    private val transactionId = "8394893849384"
    private val cardDataReversal = "3984798374982739420398ru293uowe"
    private val strLogResponseReversal = "UHRFKDJFBKDUFHKDJFHD483483HJFHJF"
    private val strLogResponseBCALastBCATopUp = "IJIFNINFINMOSFMODMOFODF"

    private val checkNoPendingBalanceGen1Result = """
        {"Action":0,"Status":1,"Attributes":{"CardNumber":"0145201100001171","CardData":"","Amount":0,"LastBalance":2000,"TransactionID":"","ButtonText":"Topup Sekarang","ImageIssuer":"https://images.tokopedia.net/img/recharge/operator/FlazzBCA.png","Message":"Ini saldo kamu yang paling baru, ya.","HasMorePendingBalance":false,"AccessCardNumber":"","AccessCode":""},"EncKey":"","EncPayload":""}
    """

    private val checkBalanceStatus3Result = """
        {"Action":0,"Status":3,"Attributes":{"CardNumber":"0145201100001171","CardData":"$cardDataReversal","Amount":0,"LastBalance":2000,"TransactionID":"$transactionId","ButtonText":"Topup Sekarang","ImageIssuer":"https://images.tokopedia.net/img/recharge/operator/FlazzBCA.png","Message":"Ini saldo kamu yang paling baru, ya.","HasMorePendingBalance":false,"AccessCardNumber":"","AccessCode":""},"EncKey":"","EncPayload":""}
    """

    private val checkBalanceStatus3TransactionIdEmptyResult = """
        {"Action":0,"Status":3,"Attributes":{"CardNumber":"0145201100001171","CardData":"$cardDataReversal","Amount":0,"LastBalance":2000,"TransactionID":"","ButtonText":"Topup Sekarang","ImageIssuer":"https://images.tokopedia.net/img/recharge/operator/FlazzBCA.png","Message":"Ini saldo kamu yang paling baru, ya.","HasMorePendingBalance":false,"AccessCardNumber":"","AccessCode":""},"EncKey":"","EncPayload":""}
    """

    private val reversalResult = """
        {"Action":5,"Status":1,"Attributes":{"CardNumber":"0145201100001171","CardData":"$cardDataReversal","Amount":0,"LastBalance":2000,"TransactionID":"$transactionId","ButtonText":"Topup Sekarang","ImageIssuer":"https://images.tokopedia.net/img/recharge/operator/FlazzBCA.png","Message":"Ini saldo kamu yang paling baru, ya.","HasMorePendingBalance":false,"AccessCardNumber":"","AccessCode":""},"EncKey":"","EncPayload":""}
    """

    private val reversalNotDoneResult = """
        {"Action":5,"Status":2,"Attributes":{"CardNumber":"0145201100001171","CardData":"$cardDataReversal","Amount":0,"LastBalance":2000,"TransactionID":"$transactionId","ButtonText":"Topup Sekarang","ImageIssuer":"https://images.tokopedia.net/img/recharge/operator/FlazzBCA.png","Message":"Ini saldo kamu yang paling baru, ya.","HasMorePendingBalance":false,"AccessCardNumber":"","AccessCode":""},"EncKey":"","EncPayload":""}
    """

    private val ackResult = """
        {"Action":4,"Status":1,"Attributes":{"CardNumber":"0145201100001171","CardData":"$cardDataReversal","Amount":0,"LastBalance":2000,"TransactionID":"$transactionId","ButtonText":"Topup Sekarang","ImageIssuer":"https://images.tokopedia.net/img/recharge/operator/FlazzBCA.png","Message":"Ini saldo kamu yang paling baru, ya.","HasMorePendingBalance":false,"AccessCardNumber":"","AccessCode":""},"EncKey":"","EncPayload":""}
    """

    private val ERROR_MESSAGE = "Maaf, cek saldo belum berhasil"
    private lateinit var emptyByteNFC: ByteArray
    val cardNumber = "0145201100001171"
    val balance = 2000

    private val checkBalanceResult = JNIResult(
        1,
        "0000XXXXXXX",
        1,
        balance,
        cardNumber,
        "0000"
    )

    private val checkBalanceFailResult = JNIResult(
        0,
        "0000XXXXXXX",
        1,
        balance,
        cardNumber,
        "0000"
    )

    private val setConfigResult = JNIResult(
        1,
        "0000XXXXXXX",
        0,
        balance,
        cardNumber,
        "0000"
    )

    private val setConfigFailResult = JNIResult(
        0,
        "0000XXXXXXX",
        0,
        balance,
        cardNumber,
        "0000"
    )


    private val reversalSuccessResult = JNIResult(
        1,
        "0000$strLogResponseReversal",
        0,
        balance,
        cardNumber,
        "0000"
    )

    private val reversalFailResult = JNIResult(
        1,
        "8304",
        0,
        balance,
        cardNumber,
        "0000"
    )

    private val reversalFailLessThanMinPrefixResult = JNIResult(
        1,
        "830",
        0,
        balance,
        cardNumber,
        "0000"
    )

    private val reversalFail8303Result = JNIResult(
        0,
        "8303",
        0,
        balance,
        cardNumber,
        "0000"
    )

    private val bcaLastBCATopUpResult = JNIResult(
        1,
        "0000$strLogResponseBCALastBCATopUp",
        0,
        balance,
        cardNumber,
        "0000"
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(NFCUtils)
        bcaLibrary = mockk()
        emptyByteNFC = byteArrayOf()
        bcaBalanceViewModel = spyk(
            BCABalanceViewModel(
                Dispatchers.Unconfined,
                bcaLibrary,
                gson,
                electronicMoneyEncryption,
                bcaFlazzUseCase
            )
        )
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
    fun checkBalanceGen1ShouldReturnLastBalance_Success() {
        //given
        initSuccessData()
        val createPendingBalanceParam = BCAFlazzRequestMapper.createGetPendingBalanceParam(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_ONE
        )

        every { bcaLibrary.bcaCheckBalance() } returns checkBalanceResult

        every { electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam) } returns  pairEncryptionResult

        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = BCAFlazzRequestMapper.createEncryptedParam(encParam.first, encParam.second)
        val responseCheckBalanceEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encKeyAes,
            encPayloadAes
        ))
        val responseCheckBalance = gson.fromJson(checkNoPendingBalanceGen1Result, BCAFlazzData::class.java)

        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkNoPendingBalanceGen1Result

        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc
        //when
        bcaBalanceViewModel.processBCACheckBalanceGen1(isoDep, mockPublicKeyString, mockPrivateKeyString)
        //then
        Assert.assertEquals(
            (bcaBalanceViewModel.bcaInquiry.value) as EmoneyInquiry,
            BCAFlazzResponseMapper.bcaMapper(
                cardNumber,
                responseCheckBalance.attributes.lastBalance,
                responseCheckBalance.attributes.imageIssuer,
                true,
                responseCheckBalance.attributes.amount,
                responseCheckBalance.status,
                responseCheckBalance.attributes.message,
                responseCheckBalance.attributes.hasMorePendingBalance,
            )
        )
    }


    @Test
    fun checkBalanceGen1ShouldReturnLastBalance_Fail() {
        //given
        initSuccessData()
        every { bcaLibrary.bcaCheckBalance() } throws IOException("")

        //when
        bcaBalanceViewModel.processBCACheckBalanceGen1(isoDep, mockPublicKeyString, mockPrivateKeyString)
        // then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun checkBalanceGen2ShouldReturnLastBalance_Success() {
        //given
        initSuccessData()
        val createPendingBalanceParam = BCAFlazzRequestMapper.createGetPendingBalanceParam(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )

        every { bcaLibrary.bcaSetConfig(mtID) } returns setConfigResult

        every { bcaLibrary.bcaCheckBalance() } returns checkBalanceResult

        every { electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam) } returns  pairEncryptionResult

        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = BCAFlazzRequestMapper.createEncryptedParam(encParam.first, encParam.second)
        val responseCheckBalanceEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encKeyAes,
            encPayloadAes
        ))
        val responseCheckBalance = gson.fromJson(checkNoPendingBalanceGen1Result, BCAFlazzData::class.java)

        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkNoPendingBalanceGen1Result

        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc
        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime, ATD
        )
        //then
        Assert.assertEquals(
            (bcaBalanceViewModel.bcaInquiry.value) as EmoneyInquiry,
            BCAFlazzResponseMapper.bcaMapper(
                cardNumber,
                responseCheckBalance.attributes.lastBalance,
                responseCheckBalance.attributes.imageIssuer,
                false,
                responseCheckBalance.attributes.amount,
                responseCheckBalance.status,
                responseCheckBalance.attributes.message,
                responseCheckBalance.attributes.hasMorePendingBalance,
            )
        )
    }

    @Test
    fun checkBalanceGen2ShouldReturnLastBalance_SetConfigFail() {
        //given
        initSuccessData()
        val createPendingBalanceParam = BCAFlazzRequestMapper.createGetPendingBalanceParam(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )

        every { bcaLibrary.bcaSetConfig(mtID) } returns setConfigFailResult

        every { bcaLibrary.bcaCheckBalance() } returns checkBalanceResult

        every { electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam) } returns  pairEncryptionResult

        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = BCAFlazzRequestMapper.createEncryptedParam(encParam.first, encParam.second)
        val responseCheckBalanceEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encKeyAes,
            encPayloadAes
        ))

        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkNoPendingBalanceGen1Result

        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc
        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime, ATD
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun checkBalanceGen2ShouldReturnLastBalance_CheckBalanceFail() {
        //given
        initSuccessData()
        val createPendingBalanceParam = BCAFlazzRequestMapper.createGetPendingBalanceParam(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )

        every { bcaLibrary.bcaSetConfig(mtID) } returns setConfigResult

        every { bcaLibrary.bcaCheckBalance() } returns checkBalanceFailResult

        every { electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam) } returns  pairEncryptionResult

        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = BCAFlazzRequestMapper.createEncryptedParam(encParam.first, encParam.second)
        val responseCheckBalanceEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encKeyAes,
            encPayloadAes
        ))

        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkNoPendingBalanceGen1Result

        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc
        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime, ATD
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun checkBalanceGen2ShouldReturnLastBalance_CheckBalanceThrowable() {
        //given
        initSuccessData()
        val createPendingBalanceParam = BCAFlazzRequestMapper.createGetPendingBalanceParam(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )

        every { bcaLibrary.bcaSetConfig(mtID) } returns setConfigResult

        every { bcaLibrary.bcaCheckBalance() } throws IOException()

        every { electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam) } returns  pairEncryptionResult

        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = BCAFlazzRequestMapper.createEncryptedParam(encParam.first, encParam.second)
        val responseCheckBalanceEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encKeyAes,
            encPayloadAes
        ))

        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkNoPendingBalanceGen1Result

        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc
        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime, ATD
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun checkBalanceGen2ShouldReturnLastBalance_Throwable() {
        //given
        initSuccessData()
        val createPendingBalanceParam = BCAFlazzRequestMapper.createGetPendingBalanceParam(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )

        every { bcaLibrary.bcaSetConfig(mtID) } returns setConfigResult

        every { bcaLibrary.bcaCheckBalance() } returns checkBalanceResult

        every { electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam) } returns  pairEncryptionResult

        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = BCAFlazzRequestMapper.createEncryptedParam(encParam.first, encParam.second)

        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkNoPendingBalanceGen1Result

        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } throws NullPointerException(ERROR_MESSAGE)
        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime, ATD
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun checkBalanceGen2ShouldReturnLastBalance_Reversal() {
        //given
        initSuccessData()
        val createPendingBalanceParam = BCAFlazzRequestMapper.createGetPendingBalanceParam(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )

        every { bcaLibrary.bcaSetConfig(mtID) } returns setConfigResult

        every { bcaLibrary.bcaCheckBalance() } returns checkBalanceResult

        every { electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam) } returns  pairEncryptionResult
        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = BCAFlazzRequestMapper.createEncryptedParam(encParam.first, encParam.second)
        val responseCheckBalanceEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encKeyAes,
            encPayloadAes
        ))
        val responseCheckStatus3Balance = gson.fromJson(checkBalanceStatus3Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus3Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        every { bcaLibrary.bcaDataReversal(transactionId, ATD) } returns reversalSuccessResult
        val createReversalParam = BCAFlazzRequestMapper.createGetBCADataReversal(
            gson,
            checkBalanceResult.cardNo,
            strLogResponseReversal,
            responseCheckStatus3Balance.attributes.amount,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createReversalParam)
        } returns pairReversalEncryptionResult
        val encReversalParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createReversalParam)
        val paramGetReversalQuery = BCAFlazzRequestMapper.createEncryptedParam(encReversalParam.first, encReversalParam.second)
        val responseReversalEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encReversalKeyAes, encReversalPayloadAes
        ))
        val responseReversal = gson.fromJson(reversalResult, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encReversalKeyAes, encReversalPayloadAes)
        } returns reversalResult
        coEvery {
            bcaFlazzUseCase.execute(paramGetReversalQuery)
        } returns responseReversalEnc

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime, ATD
        )
        //then
        Assert.assertEquals(
            (bcaBalanceViewModel.bcaInquiry.value) as EmoneyInquiry,
            BCAFlazzResponseMapper.bcaMapper(
                cardNumber,
                responseReversal.attributes.lastBalance,
                responseReversal.attributes.imageIssuer,
                false,
                responseReversal.attributes.amount,
                responseReversal.status,
                responseReversal.attributes.message,
                responseReversal.attributes.hasMorePendingBalance,
            )
        )
    }

    @Test
    fun checkBalanceGen2ShouldReturnLastBalance_Reversal_NotDoneStatus() {
        //given
        initSuccessData()
        val createPendingBalanceParam = BCAFlazzRequestMapper.createGetPendingBalanceParam(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )

        every { bcaLibrary.bcaSetConfig(mtID) } returns setConfigResult

        every { bcaLibrary.bcaCheckBalance() } returns checkBalanceResult

        every { electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam) } returns  pairEncryptionResult
        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = BCAFlazzRequestMapper.createEncryptedParam(encParam.first, encParam.second)
        val responseCheckBalanceEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encKeyAes,
            encPayloadAes
        ))
        val responseCheckStatus3Balance = gson.fromJson(checkBalanceStatus3Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus3Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        every { bcaLibrary.bcaDataReversal(transactionId, ATD) } returns reversalSuccessResult
        val createReversalParam = BCAFlazzRequestMapper.createGetBCADataReversal(
            gson,
            checkBalanceResult.cardNo,
            strLogResponseReversal,
            responseCheckStatus3Balance.attributes.amount,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createReversalParam)
        } returns pairReversalEncryptionResult
        val encReversalParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createReversalParam)
        val paramGetReversalQuery = BCAFlazzRequestMapper.createEncryptedParam(encReversalParam.first, encReversalParam.second)
        val responseReversalEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encReversalKeyAes, encReversalPayloadAes
        ))
        val responseReversal = gson.fromJson(reversalNotDoneResult, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encReversalKeyAes, encReversalPayloadAes)
        } returns reversalNotDoneResult
        coEvery {
            bcaFlazzUseCase.execute(paramGetReversalQuery)
        } returns responseReversalEnc

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime, ATD
        )
        //then
        Assert.assertEquals(
            (bcaBalanceViewModel.bcaInquiry.value) as EmoneyInquiry,
            BCAFlazzResponseMapper.bcaMapper(
                cardNumber,
                responseReversal.attributes.lastBalance,
                responseReversal.attributes.imageIssuer,
                false,
                responseReversal.attributes.amount,
                responseReversal.status,
                responseReversal.attributes.message,
                responseReversal.attributes.hasMorePendingBalance,
            )
        )
    }

    @Test
    fun checkBalanceGen2ShouldReturnLastBalance_Reversal_Throwable() {
        //given
        initSuccessData()
        val createPendingBalanceParam = BCAFlazzRequestMapper.createGetPendingBalanceParam(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )

        every { bcaLibrary.bcaSetConfig(mtID) } returns setConfigResult

        every { bcaLibrary.bcaCheckBalance() } returns checkBalanceResult

        every { electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam) } returns  pairEncryptionResult
        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = BCAFlazzRequestMapper.createEncryptedParam(encParam.first, encParam.second)
        val responseCheckBalanceEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encKeyAes,
            encPayloadAes
        ))
        val responseCheckStatus3Balance = gson.fromJson(checkBalanceStatus3Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus3Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        every { bcaLibrary.bcaDataReversal(transactionId, ATD) } returns reversalSuccessResult
        val createReversalParam = BCAFlazzRequestMapper.createGetBCADataReversal(
            gson,
            checkBalanceResult.cardNo,
            strLogResponseReversal,
            responseCheckStatus3Balance.attributes.amount,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createReversalParam)
        } returns pairReversalEncryptionResult
        val encReversalParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createReversalParam)
        val paramGetReversalQuery = BCAFlazzRequestMapper.createEncryptedParam(encReversalParam.first, encReversalParam.second)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encReversalKeyAes, encReversalPayloadAes)
        } returns reversalNotDoneResult
        coEvery {
            bcaFlazzUseCase.execute(paramGetReversalQuery)
        } throws IOException(ERROR_MESSAGE)

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime, ATD
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun checkBalanceGen2ShouldReturnLastBalance_Reversal_8303_not_call_bcalasttopup() {
        //given
        initSuccessData()
        val createPendingBalanceParam = BCAFlazzRequestMapper.createGetPendingBalanceParam(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )

        every { bcaLibrary.bcaSetConfig(mtID) } returns setConfigResult

        every { bcaLibrary.bcaCheckBalance() } returns checkBalanceResult

        every { electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam) } returns  pairEncryptionResult
        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = BCAFlazzRequestMapper.createEncryptedParam(encParam.first, encParam.second)
        val responseCheckBalanceEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encKeyAes,
            encPayloadAes
        ))
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus3Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        every { bcaLibrary.bcaDataReversal(transactionId, ATD) } returns reversalFail8303Result

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime, ATD
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun checkBalanceGen2ShouldReturnLastBalance_Reversal_otherThan8303() {
        //given
        initSuccessData()
        val createPendingBalanceParam = BCAFlazzRequestMapper.createGetPendingBalanceParam(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )

        every { bcaLibrary.bcaSetConfig(mtID) } returns setConfigResult

        every { bcaLibrary.bcaCheckBalance() } returns checkBalanceResult

        every { electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam) } returns  pairEncryptionResult
        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = BCAFlazzRequestMapper.createEncryptedParam(encParam.first, encParam.second)
        val responseCheckBalanceEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encKeyAes,
            encPayloadAes
        ))
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus3Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        every { bcaLibrary.bcaDataReversal(transactionId, ATD) } returns reversalFailResult

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime, ATD
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun checkBalanceGen2ShouldReturnLastBalance_Reversal_responseCodeLessThanMinPrefix() {
        //given
        initSuccessData()
        val createPendingBalanceParam = BCAFlazzRequestMapper.createGetPendingBalanceParam(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )

        every { bcaLibrary.bcaSetConfig(mtID) } returns setConfigResult

        every { bcaLibrary.bcaCheckBalance() } returns checkBalanceResult

        every { electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam) } returns  pairEncryptionResult
        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = BCAFlazzRequestMapper.createEncryptedParam(encParam.first, encParam.second)
        val responseCheckBalanceEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encKeyAes,
            encPayloadAes
        ))
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus3Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        every { bcaLibrary.bcaDataReversal(transactionId, ATD) } returns reversalFailLessThanMinPrefixResult

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime, ATD
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun checkBalanceGen2ShouldReturnLastBalance_Reversal_throwable() {
        //given
        initSuccessData()
        val createPendingBalanceParam = BCAFlazzRequestMapper.createGetPendingBalanceParam(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )

        every { bcaLibrary.bcaSetConfig(mtID) } returns setConfigResult

        every { bcaLibrary.bcaCheckBalance() } returns checkBalanceResult

        every { electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam) } returns  pairEncryptionResult
        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = BCAFlazzRequestMapper.createEncryptedParam(encParam.first, encParam.second)
        val responseCheckBalanceEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encKeyAes,
            encPayloadAes
        ))
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus3Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        every { bcaLibrary.bcaDataReversal(transactionId, ATD) } throws IOException()

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime, ATD
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }

    @Test
    fun checkBalanceGen2ShouldReturnLastBalance_Reversal_8303_Call_BCALastBCATopUp() {
        //given
        initSuccessData()
        val createPendingBalanceParam = BCAFlazzRequestMapper.createGetPendingBalanceParam(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )

        every { bcaLibrary.bcaSetConfig(mtID) } returns setConfigResult

        every { bcaLibrary.bcaCheckBalance() } returns checkBalanceResult

        every { electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam) } returns  pairEncryptionResult
        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = BCAFlazzRequestMapper.createEncryptedParam(encParam.first, encParam.second)
        val responseCheckBalanceEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encKeyAes,
            encPayloadAes
        ))
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus3Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        every { bcaLibrary.bcaDataReversal(transactionId, ATD) } returns reversalFail8303Result
        every { bcaLibrary.bcaLastBCATopUp() } returns bcaLastBCATopUpResult
        val createBCALastBCATopUpParam = BCAFlazzRequestMapper.createGetBCADataACKTopUp(
            gson,
            checkBalanceResult.cardNo,
            strLogResponseBCALastBCATopUp,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBCALastBCATopUpParam)
        } returns pairBCALastBCATopUpEncryptionResult
        val encBCALastParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBCALastBCATopUpParam)
        val paramGetBCALastQuery = BCAFlazzRequestMapper.createEncryptedParam(encBCALastParam.first, encBCALastParam.second)
        val responseBCALastEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encBCALastBCATopUpKeyAes, encBCALastBCATopUpPayloadAes
        ))
        val responseReversal = gson.fromJson(ackResult, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encBCALastBCATopUpKeyAes, encBCALastBCATopUpPayloadAes)
        } returns ackResult
        coEvery {
            bcaFlazzUseCase.execute(paramGetBCALastQuery)
        } returns responseBCALastEnc

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime, ATD
        )
        //then
        Assert.assertEquals(
            (bcaBalanceViewModel.bcaInquiry.value) as EmoneyInquiry,
            BCAFlazzResponseMapper.bcaMapper(
                cardNumber,
                responseReversal.attributes.lastBalance,
                responseReversal.attributes.imageIssuer,
                false,
                responseReversal.attributes.amount,
                responseReversal.status,
                responseReversal.attributes.message,
                responseReversal.attributes.hasMorePendingBalance,
            )
        )
    }

    @Test
    fun checkBalanceGen2ShouldReturnLastBalance_Reversal_8303_Call_BCALastBCATopUp_Throwable() {
        //given
        initSuccessData()
        val createPendingBalanceParam = BCAFlazzRequestMapper.createGetPendingBalanceParam(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )

        every { bcaLibrary.bcaSetConfig(mtID) } returns setConfigResult

        every { bcaLibrary.bcaCheckBalance() } returns checkBalanceResult

        every { electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam) } returns  pairEncryptionResult
        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = BCAFlazzRequestMapper.createEncryptedParam(encParam.first, encParam.second)
        val responseCheckBalanceEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encKeyAes,
            encPayloadAes
        ))
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus3Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        every { bcaLibrary.bcaDataReversal(transactionId, ATD) } returns reversalFail8303Result
        every { bcaLibrary.bcaLastBCATopUp() } returns bcaLastBCATopUpResult
        val createBCALastBCATopUpParam = BCAFlazzRequestMapper.createGetBCADataACKTopUp(
            gson,
            checkBalanceResult.cardNo,
            strLogResponseBCALastBCATopUp,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBCALastBCATopUpParam)
        } returns pairBCALastBCATopUpEncryptionResult
        val encBCALastParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBCALastBCATopUpParam)
        val paramGetBCALastQuery = BCAFlazzRequestMapper.createEncryptedParam(encBCALastParam.first, encBCALastParam.second)
        val responseBCALastEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encBCALastBCATopUpKeyAes, encBCALastBCATopUpPayloadAes
        ))
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encBCALastBCATopUpKeyAes, encBCALastBCATopUpPayloadAes)
        } returns ackResult
        coEvery {
            bcaFlazzUseCase.execute(paramGetBCALastQuery)
        } throws IOException(ERROR_MESSAGE)

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime, ATD
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Throwable).message,
            ERROR_MESSAGE
        )
    }
}
