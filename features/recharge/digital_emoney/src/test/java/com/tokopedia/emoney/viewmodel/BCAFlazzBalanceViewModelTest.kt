package com.tokopedia.emoney.viewmodel

import android.nfc.tech.IsoDep
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.common.topupbills.favoritecommon.data.TopupBillsPersoFavNumber
import com.tokopedia.common.topupbills.favoritecommon.data.TopupBillsPersoFavNumberData
import com.tokopedia.common.topupbills.favoritecommon.data.TopupBillsPersoFavNumberItem
import com.tokopedia.common.topupbills.usecase.GetBCAGenCheckerUseCase
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.common_electronic_money.data.EmoneyInquiryLogRequest
import com.tokopedia.common_electronic_money.data.RechargeEmoneyInquiryLogRequest
import com.tokopedia.common_electronic_money.util.ElectronicMoneyEncryption
import com.tokopedia.common_electronic_money.util.NFCUtils
import com.tokopedia.emoney.domain.request.BCAFlazzRequestMapper
import com.tokopedia.emoney.domain.request.CommonBodyEnc
import com.tokopedia.emoney.domain.response.BCAFlazzData
import com.tokopedia.emoney.domain.response.BCAFlazzResponse
import com.tokopedia.emoney.domain.response.BCAFlazzResponseMapper
import com.tokopedia.emoney.domain.usecase.GetBCAFlazzUseCase
import com.tokopedia.emoney.integration.BCALibraryIntegration
import com.tokopedia.emoney.integration.data.JNIResult
import com.tokopedia.network.exception.MessageErrorException
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
    lateinit var bcaCheckGen: GetBCAGenCheckerUseCase

    @RelaxedMockK
    private lateinit var bcaLibrary: BCALibraryIntegration

    @MockK
    lateinit var isoDep: IsoDep

    private val nullIsoDep: IsoDep? = null

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
    private var encTrxIdKeyAes = "oinfoidfonefwnef"
    private var encTrxIdPayloadAes = "naodfinoadiof"
    private var pairBCATrxIdEncryptionResult = Pair("oinfoidfonefwnef", "naodfinoadiof")
    private var encSessionKeyAes = "knfkdfkdofndk"
    private var encSessionPayloadAes = "asdadfadkfnkdnnv"
    private var pairBCASessionEncryptionResult = Pair("knfkdfkdofndk", "knfkdfkdofndk")
    private var encBetweenTopUpKeyAes = "isdubfisuhibd"
    private var encBetweenTopUpPayloadAes = "skufsiffdnifd"
    private var pairBCABetweenTopUpEncryptionResult = Pair("isdubfisuhibd", "skufsiffdnifd")
    private val GEN_ONE = "1"
    private val GEN_TWO = "2"
    private val merchantId = "0904039009"
    private val terminalId = "9384983948"
    private val mtID = "9384983948,0904039009"
    private val strCurrDateTime = "98309849384"
    private val ATD = "989389898984"
    private val transactionId = "8394893849384"
    private val cardDataReversal = "3984798374982739420398ru293uowe"
    private val cardDataCheckBalance = "idapfjapd9jfa9eufaodfhjoad98fja"
    private val cardDataTrxId = "9hfw8hfo09hfw9ef0wef"
    private val cardDataSessionId = "inflandfoaodfoainodfnioa"
    private val cardDataTopUp= "nviosbdofibsodnfoiinsiofn"
    private val strLogResponseReversal = "UHRFKDJFBKDUFHKDJFHD483483HJFHJF"
    private val strLogResponseBCALastBCATopUp = "IJIFNINFINMOSFMODMOFODF"
    private val strLogSession1 = "IUSFOIHSOIDOSIHDOISHD"
    private val strLogSession2 = "NFOIASODIHSODIHOSIDH"
    private val strLogTopUp1 = "INIDFIDNIFNDINFI"
    private val strLogTopUp2 = "SKASNDFIDFOINOFD"
    private val accessCardNumber = "9849384938"
    private val accessCode = "9894uijsiafjd"
    private val amount = 30000
    private var messageTopUp2 = "Message Top Up 2"
    private var messageTopUp1 = "Message Top Up 1"

    private val checkNoPendingBalanceGen1Result = """
        {"Action":0,"Status":1,"Attributes":{"CardNumber":"0145201100001171","CardData":"","Amount":0,"LastBalance":2000,"TransactionID":"","ButtonText":"Topup Sekarang","ImageIssuer":"https://images.tokopedia.net/img/recharge/operator/FlazzBCA.png","Message":"Ini saldo kamu yang paling baru, ya.","HasMorePendingBalance":false,"AccessCardNumber":"","AccessCode":""},"EncKey":"","EncPayload":""}
    """

    private val checkBalanceStatus3Result = """
        {"Action":0,"Status":3,"Attributes":{"CardNumber":"0145201100001171","CardData":"$cardDataReversal","Amount":0,"LastBalance":2000,"TransactionID":"$transactionId","ButtonText":"Topup Sekarang","ImageIssuer":"https://images.tokopedia.net/img/recharge/operator/FlazzBCA.png","Message":"Ini saldo kamu yang paling baru, ya.","HasMorePendingBalance":false,"AccessCardNumber":"","AccessCode":""},"EncKey":"","EncPayload":""}
    """

    private val checkBalanceStatus3TransactionIdEmptyResult = """
        {"Action":0,"Status":3,"Attributes":{"CardNumber":"0145201100001171","CardData":"$cardDataReversal","Amount":0,"LastBalance":2000,"TransactionID":"","ButtonText":"Topup Sekarang","ImageIssuer":"https://images.tokopedia.net/img/recharge/operator/FlazzBCA.png","Message":"Ini saldo kamu yang paling baru, ya.","HasMorePendingBalance":false,"AccessCardNumber":"","AccessCode":""},"EncKey":"","EncPayload":""}
    """

    private val checkBalanceStatus0Result = """
        {"Action":0,"Status":0,"Attributes":{"CardNumber":"0145201100001171","CardData":"$cardDataCheckBalance","Amount":0,"LastBalance":2000,"TransactionID":"$transactionId","ButtonText":"Topup Sekarang","ImageIssuer":"https://images.tokopedia.net/img/recharge/operator/FlazzBCA.png","Message":"Ini saldo kamu yang paling baru, ya.","HasMorePendingBalance":false,"AccessCardNumber":"","AccessCode":""},"EncKey":"","EncPayload":""}
    """

    private val trxIdStatus0Result = """
        {"Action":1,"Status":0,"Attributes":{"CardNumber":"0145201100001171","CardData":"$cardDataTrxId","Amount":0,"LastBalance":2000,"TransactionID":"$transactionId","ButtonText":"Topup Sekarang","ImageIssuer":"https://images.tokopedia.net/img/recharge/operator/FlazzBCA.png","Message":"Ini saldo kamu yang paling baru, ya.","HasMorePendingBalance":false,"AccessCardNumber":"","AccessCode":""},"EncKey":"","EncPayload":""}
    """

    private val trxIdStatus0TransactionIdEmptyResult = """
        {"Action":1,"Status":0,"Attributes":{"CardNumber":"0145201100001171","CardData":"$cardDataTrxId","Amount":0,"LastBalance":2000,"TransactionID":"","ButtonText":"Topup Sekarang","ImageIssuer":"https://images.tokopedia.net/img/recharge/operator/FlazzBCA.png","Message":"Ini saldo kamu yang paling baru, ya.","HasMorePendingBalance":false,"AccessCardNumber":"","AccessCode":""},"EncKey":"","EncPayload":""}
    """

    private val trxIdStatus1TransactionIdEmptyResult = """
        {"Action":1,"Status":1,"Attributes":{"CardNumber":"0145201100001171","CardData":"$cardDataTrxId","Amount":0,"LastBalance":2000,"TransactionID":"","ButtonText":"Topup Sekarang","ImageIssuer":"https://images.tokopedia.net/img/recharge/operator/FlazzBCA.png","Message":"Ini saldo kamu yang paling baru, ya.","HasMorePendingBalance":false,"AccessCardNumber":"","AccessCode":""},"EncKey":"","EncPayload":""}
    """

    private val sessionStatus1Result = """
        {"Action":2,"Status":1,"Attributes":{"CardNumber":"0145201100001171","CardData":"$cardDataSessionId","Amount":0,"LastBalance":2000,"TransactionID":"$transactionId","ButtonText":"Topup Sekarang","ImageIssuer":"https://images.tokopedia.net/img/recharge/operator/FlazzBCA.png","Message":"Ini saldo kamu yang paling baru, ya.","HasMorePendingBalance":false,"AccessCardNumber":"","AccessCode":""},"EncKey":"","EncPayload":""}
    """

    private val sessionStatus0Result = """
        {"Action":2,"Status":0,"Attributes":{"CardNumber":"0145201100001171","CardData":"$cardDataSessionId","Amount":$amount,"LastBalance":2000,"TransactionID":"$transactionId","ButtonText":"Topup Sekarang","ImageIssuer":"https://images.tokopedia.net/img/recharge/operator/FlazzBCA.png","Message":"Ini saldo kamu yang paling baru, ya.","HasMorePendingBalance":false,"AccessCardNumber":"$accessCardNumber","AccessCode":"$accessCode"},"EncKey":"","EncPayload":""}
    """

    private val sessionStatus0CardDataEmptyResult = """
        {"Action":2,"Status":0,"Attributes":{"CardNumber":"0145201100001171","CardData":"","Amount":0,"LastBalance":2000,"TransactionID":"$transactionId","ButtonText":"Topup Sekarang","ImageIssuer":"https://images.tokopedia.net/img/recharge/operator/FlazzBCA.png","Message":"Ini saldo kamu yang paling baru, ya.","HasMorePendingBalance":false,"AccessCardNumber":"","AccessCode":""},"EncKey":"","EncPayload":""}
    """

    private val sessionStatus1CardDataEmptyResult = """
        {"Action":2,"Status":1,"Attributes":{"CardNumber":"0145201100001171","CardData":"","Amount":0,"LastBalance":2000,"TransactionID":"$transactionId","ButtonText":"Topup Sekarang","ImageIssuer":"https://images.tokopedia.net/img/recharge/operator/FlazzBCA.png","Message":"Ini saldo kamu yang paling baru, ya.","HasMorePendingBalance":false,"AccessCardNumber":"","AccessCode":""},"EncKey":"","EncPayload":""}
    """

    private val topUpStatus2Result = """
        {"Action":3,"Status":2,"Attributes":{"CardNumber":"0145201100001171","CardData":"$cardDataTopUp","Amount":0,"LastBalance":2000,"TransactionID":"$transactionId","ButtonText":"Topup Sekarang","ImageIssuer":"https://images.tokopedia.net/img/recharge/operator/FlazzBCA.png","Message":"Ini saldo kamu yang paling baru, ya.","HasMorePendingBalance":false,"AccessCardNumber":"","AccessCode":""},"EncKey":"","EncPayload":""}
    """

    private val topUpStatus0Result = """
        {"Action":3,"Status":0,"Attributes":{"CardNumber":"0145201100001171","CardData":"$cardDataTopUp","Amount":$amount,"LastBalance":2000,"TransactionID":"$transactionId","ButtonText":"Topup Sekarang","ImageIssuer":"https://images.tokopedia.net/img/recharge/operator/FlazzBCA.png","Message":"Ini saldo kamu yang paling baru, ya.","HasMorePendingBalance":false,"AccessCardNumber":"$accessCardNumber","AccessCode":"$accessCode"},"EncKey":"","EncPayload":""}
    """

    private val topUpStatus1Result = """
        {"Action":3,"Status":1,"Attributes":{"CardNumber":"0145201100001171","CardData":"$cardDataTopUp","Amount":$amount,"LastBalance":2000,"TransactionID":"$transactionId","ButtonText":"Topup Sekarang","ImageIssuer":"https://images.tokopedia.net/img/recharge/operator/FlazzBCA.png","Message":"Ini saldo kamu yang paling baru, ya.","HasMorePendingBalance":false,"AccessCardNumber":"$accessCardNumber","AccessCode":"$accessCode"},"EncKey":"","EncPayload":""}
    """

    private val topUpStatusUnexpectedResult = """
        {"Action":3,"Status":4,"Attributes":{"CardNumber":"0145201100001171","CardData":"$cardDataTopUp","Amount":$amount,"LastBalance":2000,"TransactionID":"$transactionId","ButtonText":"Topup Sekarang","ImageIssuer":"https://images.tokopedia.net/img/recharge/operator/FlazzBCA.png","Message":"Ini saldo kamu yang paling baru, ya.","HasMorePendingBalance":false,"AccessCardNumber":"$accessCardNumber","AccessCode":"$accessCode"},"EncKey":"","EncPayload":""}
    """

    private val topUpStatus0CardDataEmptyResult = """
        {"Action":3,"Status":0,"Attributes":{"CardNumber":"0145201100001171","CardData":"","Amount":0,"LastBalance":2000,"TransactionID":"$transactionId","ButtonText":"Topup Sekarang","ImageIssuer":"https://images.tokopedia.net/img/recharge/operator/FlazzBCA.png","Message":"Ini saldo kamu yang paling baru, ya.","HasMorePendingBalance":false,"AccessCardNumber":"","AccessCode":""},"EncKey":"","EncPayload":""}
    """

    private val trxIdStatus1Result = """
        {"Action":1,"Status":1,"Attributes":{"CardNumber":"0145201100001171","CardData":"$cardDataSessionId","Amount":0,"LastBalance":2000,"TransactionID":"$transactionId","ButtonText":"Topup Sekarang","ImageIssuer":"https://images.tokopedia.net/img/recharge/operator/FlazzBCA.png","Message":"Ini saldo kamu yang paling baru, ya.","HasMorePendingBalance":false,"AccessCardNumber":"","AccessCode":""},"EncKey":"","EncPayload":""}
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
        "9878XXXXXXX",
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
        "9878XXXXXXX",
        0,
        balance,
        cardNumber,
        "0000"
    )

    private val setConfigFailResultLessThan4 = JNIResult(
        0,
        "789",
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

    private val session1Result = JNIResult(
        1,
        "0000$strLogSession1",
        0,
        balance,
        cardNumber,
        "0000"
    )

    private val session1FailResult = JNIResult(
        0,
        "9498",
        0,
        balance,
        cardNumber,
        "0000"
    )

    private val session1SizePrefixResult = JNIResult(
        0,
        "0000",
        0,
        balance,
        cardNumber,
        "0000"
    )

    private val session2Result = JNIResult(
        1,
        "0000$strLogSession2",
        0,
        balance,
        cardNumber,
        "0000"
    )

    private val session2FailResult = JNIResult(
        0,
        "90390",
        0,
        balance,
        cardNumber,
        "0000"
    )

    private val topUp1Result = JNIResult(
        1,
        "0000$strLogTopUp1",
        0,
        balance,
        cardNumber,
        "0000"
    )

    private val topUp1FailResult = JNIResult(
        0,
        "90390",
        0,
        balance,
        cardNumber,
        "0000"
    )

    private val topUp2Result = JNIResult(
        1,
        "0000$strLogTopUp2",
        0,
        balance,
        cardNumber,
        "0000"
    )

    private val topUp2EmptyStrLogResult = JNIResult(
        0,
        "",
        0,
        balance,
        cardNumber,
        "0000"
    )

    private val topUp2FailResult = JNIResult(
        0,
        "0000$strLogTopUp2",
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
                bcaFlazzUseCase,
                bcaCheckGen
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

    val bcaGenCheck = TopupBillsPersoFavNumberData(
        persoFavoriteNumber = TopupBillsPersoFavNumber(
            items = listOf(
                TopupBillsPersoFavNumberItem(
                    label1 = "1",
                    label2 = messageTopUp1
                )
            )
        )
    )

    val bcaGenCheckEmpty = TopupBillsPersoFavNumberData(
        persoFavoriteNumber = TopupBillsPersoFavNumber(
            items = listOf(
                TopupBillsPersoFavNumberItem(
                    label1 = "1",
                    label2 = ""
                )
            )
        )
    )

    private fun genCheckExecute() {
        coEvery { bcaCheckGen.execute(any()) } returns bcaGenCheck
    }

    private fun genCheckExecuteEmpty() {
        coEvery { bcaCheckGen.execute(any()) } returns bcaGenCheckEmpty
    }

    private fun genCheckExecuteThrows() {
        coEvery { bcaCheckGen.execute(any()) } throws MessageErrorException()
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

        genCheckExecute()

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
        bcaBalanceViewModel.processBCACheckBalanceGen1(isoDep, mockPublicKeyString, mockPrivateKeyString, messageTopUp1)
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
                false,
                messageTopUp1,
                "",
                false
            )
        )
    }

    @Test
    fun checkBalanceGen1ShouldReturnLastBalance_CheckGenEmpty_Success() {
        //given
        initSuccessData()
        val createPendingBalanceParam = BCAFlazzRequestMapper.createGetPendingBalanceParam(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_ONE
        )

        genCheckExecuteEmpty()

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
        bcaBalanceViewModel.processBCACheckBalanceGen1(isoDep, mockPublicKeyString, mockPrivateKeyString, messageTopUp1)
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
                false,
                messageTopUp1,
                "",
                false
            )
        )
    }

    @Test
    fun checkBalanceGen1ShouldReturnLastBalance_CheckGenError_Success() {
        //given
        initSuccessData()
        val createPendingBalanceParam = BCAFlazzRequestMapper.createGetPendingBalanceParam(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_ONE
        )

        genCheckExecuteThrows()

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
        bcaBalanceViewModel.processBCACheckBalanceGen1(isoDep, mockPublicKeyString, mockPrivateKeyString, messageTopUp1)
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
                false,
                messageTopUp1,
                "",
                false
            )
        )
    }


    @Test
    fun checkBalanceGen1ShouldReturnLastBalance_Fail() {
        //given
        initSuccessData()
        every { bcaLibrary.bcaCheckBalance() } throws IOException(ERROR_MESSAGE)
        genCheckExecute()

        //when
        bcaBalanceViewModel.processBCACheckBalanceGen1(isoDep, mockPublicKeyString, mockPrivateKeyString, messageTopUp1)
        // then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    "",
                    "${BCABalanceViewModel.TAG_PROCESS_PENDING_BALANCE_GEN_1}: $ERROR_MESSAGE"
                )
            )
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
            strCurrDateTime,
            ATD,
            messageTopUp2
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
    fun checkBalanceGen2ShouldReturnLastBalance_Success_With_Reversal_But_TrxIdEmpty() {
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
        val responseCheckBalance = gson.fromJson(checkBalanceStatus3TransactionIdEmptyResult, BCAFlazzData::class.java)

        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus3TransactionIdEmptyResult

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
            strCurrDateTime,
            ATD,
            messageTopUp2
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
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    checkBalanceResult.cardNo,
                    "${BCABalanceViewModel.TAG_PROCESS_PENDING_BALANCE_GEN_2}: 9878"
                )
            )
        )
    }

    @Test
    fun checkBalanceGen2ShouldReturnLastBalance_SetConfigFailLessThanMinimum() {
        //given
        initSuccessData()
        val createPendingBalanceParam = BCAFlazzRequestMapper.createGetPendingBalanceParam(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )

        every { bcaLibrary.bcaSetConfig(mtID) } returns setConfigFailResultLessThan4

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
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    checkBalanceResult.cardNo,
                    "${BCABalanceViewModel.TAG_PROCESS_PENDING_BALANCE_GEN_2}: 789"
                )
            )
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
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    checkBalanceResult.cardNo,
                    "${BCABalanceViewModel.TAG_PROCESS_PENDING_BALANCE_GEN_2}: 9878",
                    checkBalanceFailResult.balance.toDouble()
                )
            )
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

        every { bcaLibrary.bcaCheckBalance() } throws IOException(ERROR_MESSAGE)

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
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    "",
                    "${BCABalanceViewModel.TAG_PROCESS_PENDING_BALANCE_GEN_2}: $ERROR_MESSAGE"
                )
            )
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
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    checkBalanceResult.cardNo,
                    "${BCABalanceViewModel.TAG_PROCESS_PENDING_BALANCE}: $ERROR_MESSAGE",
                    checkBalanceResult.balance.toDouble()
                )
            )
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
            GEN_TWO,
            "0000"
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
            strCurrDateTime,
            ATD,
            messageTopUp2
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
            GEN_TWO,
            "0000"
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
            strCurrDateTime,
            ATD,
            messageTopUp2
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
            GEN_TWO,
            "0000"
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
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    checkBalanceResult.cardNo,
                    "${BCABalanceViewModel.TAG_PROCESS_REVERSAL}: $ERROR_MESSAGE",
                    checkBalanceResult.balance.toDouble()
                )
            )
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
        every { bcaLibrary.bcaLastBCATopUp() } throws IOException(ERROR_MESSAGE)

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    checkBalanceResult.cardNo,
                    "${BCABalanceViewModel.TAG_PROCESS_SDK_BCALASTBCATOP_UP}: $ERROR_MESSAGE",
                    checkBalanceResult.balance.toDouble()
                )
            )
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
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    checkBalanceResult.cardNo,
                    "${BCABalanceViewModel.TAG_PROCESS_SDK_REVERSAL}: 8304",
                    checkBalanceResult.balance.toDouble()
                )
            )
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
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    checkBalanceResult.cardNo,
                    "${BCABalanceViewModel.TAG_PROCESS_SDK_REVERSAL}: 830",
                    checkBalanceResult.balance.toDouble()
                )
            )
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


        every { bcaLibrary.bcaDataReversal(transactionId, ATD) } throws IOException(ERROR_MESSAGE)

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    checkBalanceResult.cardNo,
                    "${BCABalanceViewModel.TAG_PROCESS_SDK_REVERSAL}: $ERROR_MESSAGE",
                    checkBalanceResult.balance.toDouble()
                )
            )
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
            GEN_TWO,
            "0000"
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
            strCurrDateTime,
            ATD,
            messageTopUp2
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
                false,
                "",
                messageTopUp2,
                false
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
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBCALastBCATopUpParam)
        } returns pairBCALastBCATopUpEncryptionResult
        val encBCALastParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBCALastBCATopUpParam)
        val paramGetBCALastQuery = BCAFlazzRequestMapper.createEncryptedParam(encBCALastParam.first, encBCALastParam.second)
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
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    checkBalanceResult.cardNo,
                    "${BCABalanceViewModel.TAG_PROCESS_ACK}: $ERROR_MESSAGE",
                    checkBalanceResult.balance.toDouble()
                )
            )
        )
    }

    @Test
    fun checkBalanceGen2ShouldProcessTrxId_Success() {
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
        val responseCheckBalance = gson.fromJson(checkBalanceStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        val createTrxIdParam = BCAFlazzRequestMapper.createGetBCAGenerateTrxId(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        } returns pairBCATrxIdEncryptionResult
        val encTrxIdParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        val paramGetTrxIdQuery = BCAFlazzRequestMapper.createEncryptedParam(encTrxIdParam.first, encTrxIdParam.second)
        val responseTrxEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encTrxIdKeyAes, encTrxIdPayloadAes
        ))
        val responseTrxId = gson.fromJson(trxIdStatus1Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encTrxIdKeyAes, encTrxIdPayloadAes)
        } returns trxIdStatus1Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetTrxIdQuery)
        } returns responseTrxEnc

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            (bcaBalanceViewModel.bcaInquiry.value) as EmoneyInquiry,
            BCAFlazzResponseMapper.bcaMapper(
                cardNumber,
                responseTrxId.attributes.lastBalance,
                responseTrxId.attributes.imageIssuer,
                false,
                responseTrxId.attributes.amount,
                responseTrxId.status,
                responseTrxId.attributes.message,
                responseTrxId.attributes.hasMorePendingBalance,
            )
        )
    }

    @Test
    fun checkBalanceGen2ShouldProcessTrxId_Empty_Failed() {
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
        val responseCheckBalance = gson.fromJson(checkBalanceStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        val createTrxIdParam = BCAFlazzRequestMapper.createGetBCAGenerateTrxId(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        } returns pairBCATrxIdEncryptionResult
        val encTrxIdParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        val paramGetTrxIdQuery = BCAFlazzRequestMapper.createEncryptedParam(encTrxIdParam.first, encTrxIdParam.second)
        val responseTrxEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encTrxIdKeyAes, encTrxIdPayloadAes
        ))
        val responseTrxId = gson.fromJson(trxIdStatus0TransactionIdEmptyResult, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encTrxIdKeyAes, encTrxIdPayloadAes)
        } returns trxIdStatus0TransactionIdEmptyResult
        coEvery {
            bcaFlazzUseCase.execute(paramGetTrxIdQuery)
        } returns responseTrxEnc

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    checkBalanceResult.cardNo,
                    "${BCABalanceViewModel.TAG_PROCESS_TRANSACTION_ID}: ${BCABalanceViewModel.ERROR_TRANSACTION_ID_EMPTY}",
                    checkBalanceResult.balance.toDouble()
                )
            )
        )
    }

    @Test
    fun checkBalanceGen2ShouldProcessTrxId_Status1_EmptyTrxId_Success() {
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
        val responseCheckBalance = gson.fromJson(checkBalanceStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        val createTrxIdParam = BCAFlazzRequestMapper.createGetBCAGenerateTrxId(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        } returns pairBCATrxIdEncryptionResult
        val encTrxIdParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        val paramGetTrxIdQuery = BCAFlazzRequestMapper.createEncryptedParam(encTrxIdParam.first, encTrxIdParam.second)
        val responseTrxEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encTrxIdKeyAes, encTrxIdPayloadAes
        ))
        val responseTrxId = gson.fromJson(trxIdStatus1TransactionIdEmptyResult, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encTrxIdKeyAes, encTrxIdPayloadAes)
        } returns trxIdStatus1TransactionIdEmptyResult
        coEvery {
            bcaFlazzUseCase.execute(paramGetTrxIdQuery)
        } returns responseTrxEnc

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            (bcaBalanceViewModel.bcaInquiry.value) as EmoneyInquiry,
            BCAFlazzResponseMapper.bcaMapper(
                cardNumber,
                responseTrxId.attributes.lastBalance,
                responseTrxId.attributes.imageIssuer,
                false,
                responseTrxId.attributes.amount,
                responseTrxId.status,
                responseTrxId.attributes.message,
                responseTrxId.attributes.hasMorePendingBalance,
            )
        )
    }

    @Test
    fun checkBalanceGen2ShouldProcessTrxId_Throwable() {
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
        val responseCheckBalance = gson.fromJson(checkBalanceStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        val createTrxIdParam = BCAFlazzRequestMapper.createGetBCAGenerateTrxId(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        } returns pairBCATrxIdEncryptionResult
        val encTrxIdParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        val paramGetTrxIdQuery = BCAFlazzRequestMapper.createEncryptedParam(encTrxIdParam.first, encTrxIdParam.second)
        val responseTrxEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encTrxIdKeyAes, encTrxIdPayloadAes
        ))
        val responseTrxId = gson.fromJson(trxIdStatus1TransactionIdEmptyResult, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encTrxIdKeyAes, encTrxIdPayloadAes)
        } returns trxIdStatus1TransactionIdEmptyResult
        coEvery {
            bcaFlazzUseCase.execute(paramGetTrxIdQuery)
        } throws IOException(ERROR_MESSAGE)

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    checkBalanceResult.cardNo,
                    "${BCABalanceViewModel.TAG_PROCESS_TRANSACTION_ID}: $ERROR_MESSAGE",
                    checkBalanceResult.balance.toDouble()
                )
            )
        )
    }

    @Test
    fun checkBalanceGen2ShouldProcessSession_Fail_JNI() {
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
        val responseCheckBalance = gson.fromJson(checkBalanceStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        val createTrxIdParam = BCAFlazzRequestMapper.createGetBCAGenerateTrxId(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        } returns pairBCATrxIdEncryptionResult
        val encTrxIdParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        val paramGetTrxIdQuery = BCAFlazzRequestMapper.createEncryptedParam(encTrxIdParam.first, encTrxIdParam.second)
        val responseTrxEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encTrxIdKeyAes, encTrxIdPayloadAes
        ))
        val responseTrxId = gson.fromJson(trxIdStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encTrxIdKeyAes, encTrxIdPayloadAes)
        } returns trxIdStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetTrxIdQuery)
        } returns responseTrxEnc


        every {
            bcaLibrary.bcaDataSession1(
                transactionId, ATD, strCurrDateTime
            )
        } returns session1FailResult

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    checkBalanceResult.cardNo,
                    "${BCABalanceViewModel.TAG_PROCESS_SDK_SESSION_1}: 9498",
                    checkBalanceResult.balance.toDouble()
                )
            )
        )
    }

    @Test
    fun checkBalanceGen2ShouldProcessSession_Fail_JNI_PrefixSize() {
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
        val responseCheckBalance = gson.fromJson(checkBalanceStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        val createTrxIdParam = BCAFlazzRequestMapper.createGetBCAGenerateTrxId(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        } returns pairBCATrxIdEncryptionResult
        val encTrxIdParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        val paramGetTrxIdQuery = BCAFlazzRequestMapper.createEncryptedParam(encTrxIdParam.first, encTrxIdParam.second)
        val responseTrxEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encTrxIdKeyAes, encTrxIdPayloadAes
        ))
        val responseTrxId = gson.fromJson(trxIdStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encTrxIdKeyAes, encTrxIdPayloadAes)
        } returns trxIdStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetTrxIdQuery)
        } returns responseTrxEnc


        every {
            bcaLibrary.bcaDataSession1(
                transactionId, ATD, strCurrDateTime
            )
        } returns session1SizePrefixResult

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    checkBalanceResult.cardNo,
                    "${BCABalanceViewModel.TAG_PROCESS_SDK_SESSION_1}: 0000",
                    checkBalanceResult.balance.toDouble()
                )
            )
        )
    }

    @Test
    fun checkBalanceGen2ShouldProcessSession_Fail_Throwable() {
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
        val responseCheckBalance = gson.fromJson(checkBalanceStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        val createTrxIdParam = BCAFlazzRequestMapper.createGetBCAGenerateTrxId(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        } returns pairBCATrxIdEncryptionResult
        val encTrxIdParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        val paramGetTrxIdQuery = BCAFlazzRequestMapper.createEncryptedParam(encTrxIdParam.first, encTrxIdParam.second)
        val responseTrxEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encTrxIdKeyAes, encTrxIdPayloadAes
        ))
        val responseTrxId = gson.fromJson(trxIdStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encTrxIdKeyAes, encTrxIdPayloadAes)
        } returns trxIdStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetTrxIdQuery)
        } returns responseTrxEnc


        every {
            bcaLibrary.bcaDataSession1(
                transactionId, ATD, strCurrDateTime
            )
        } throws IOException(ERROR_MESSAGE)

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    checkBalanceResult.cardNo,
                    "${BCABalanceViewModel.TAG_PROCESS_SDK_SESSION_1}: $ERROR_MESSAGE",
                    checkBalanceResult.balance.toDouble()
                )
            )
        )
    }

    @Test
    fun checkBalanceGen2ShouldProcessSession_Success() {
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
        val responseCheckBalance = gson.fromJson(checkBalanceStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        val createTrxIdParam = BCAFlazzRequestMapper.createGetBCAGenerateTrxId(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        } returns pairBCATrxIdEncryptionResult
        val encTrxIdParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        val paramGetTrxIdQuery = BCAFlazzRequestMapper.createEncryptedParam(encTrxIdParam.first, encTrxIdParam.second)
        val responseTrxEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encTrxIdKeyAes, encTrxIdPayloadAes
        ))
        val responseTrxId = gson.fromJson(trxIdStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encTrxIdKeyAes, encTrxIdPayloadAes)
        } returns trxIdStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetTrxIdQuery)
        } returns responseTrxEnc

        every {
            bcaLibrary.bcaDataSession1(
                transactionId, ATD, strCurrDateTime
            )
        } returns session1Result

        val createSessionParam = BCAFlazzRequestMapper.createGetBCAGenerateSessionKey(
            gson,
            checkBalanceResult.cardNo,
            strLogSession1,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        } returns pairBCASessionEncryptionResult
        val encSessionParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        val paramGetSessionQuery = BCAFlazzRequestMapper.createEncryptedParam(encSessionParam.first, encSessionParam.second)
        val responseSessionEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encSessionKeyAes, encSessionPayloadAes
        ))
        val responseSession = gson.fromJson(sessionStatus1Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encSessionKeyAes, encSessionPayloadAes)
        } returns sessionStatus1Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetSessionQuery)
        } returns responseSessionEnc

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            (bcaBalanceViewModel.bcaInquiry.value) as EmoneyInquiry,
            BCAFlazzResponseMapper.bcaMapper(
                cardNumber,
                responseSession.attributes.lastBalance,
                responseSession.attributes.imageIssuer,
                false,
                responseSession.attributes.amount,
                responseSession.status,
                responseSession.attributes.message,
                responseSession.attributes.hasMorePendingBalance,
            )
        )
    }

    @Test
    fun checkBalanceGen2ShouldProcessSession_Throwable() {
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
        val responseCheckBalance = gson.fromJson(checkBalanceStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        val createTrxIdParam = BCAFlazzRequestMapper.createGetBCAGenerateTrxId(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        } returns pairBCATrxIdEncryptionResult
        val encTrxIdParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        val paramGetTrxIdQuery = BCAFlazzRequestMapper.createEncryptedParam(encTrxIdParam.first, encTrxIdParam.second)
        val responseTrxEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encTrxIdKeyAes, encTrxIdPayloadAes
        ))
        val responseTrxId = gson.fromJson(trxIdStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encTrxIdKeyAes, encTrxIdPayloadAes)
        } returns trxIdStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetTrxIdQuery)
        } returns responseTrxEnc

        every {
            bcaLibrary.bcaDataSession1(
                transactionId, ATD, strCurrDateTime
            )
        } returns session1Result

        val createSessionParam = BCAFlazzRequestMapper.createGetBCAGenerateSessionKey(
            gson,
            checkBalanceResult.cardNo,
            strLogSession1,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        } returns pairBCASessionEncryptionResult
        val encSessionParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        val paramGetSessionQuery = BCAFlazzRequestMapper.createEncryptedParam(encSessionParam.first, encSessionParam.second)
        val responseSessionEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encSessionKeyAes, encSessionPayloadAes
        ))
        val responseSession = gson.fromJson(sessionStatus1Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encSessionKeyAes, encSessionPayloadAes)
        } returns sessionStatus1Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetSessionQuery)
        } throws IOException(ERROR_MESSAGE)

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    checkBalanceResult.cardNo,
                    "${BCABalanceViewModel.TAG_PROCESS_SESSION_KEY}: $ERROR_MESSAGE",
                    checkBalanceResult.balance.toDouble()
                )
            )
        )
    }

    @Test
    fun checkBalanceGen2ShouldProcessSession_CardDataEmpty_Fail() {
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
        val responseCheckBalance = gson.fromJson(checkBalanceStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        val createTrxIdParam = BCAFlazzRequestMapper.createGetBCAGenerateTrxId(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        } returns pairBCATrxIdEncryptionResult
        val encTrxIdParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        val paramGetTrxIdQuery = BCAFlazzRequestMapper.createEncryptedParam(encTrxIdParam.first, encTrxIdParam.second)
        val responseTrxEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encTrxIdKeyAes, encTrxIdPayloadAes
        ))
        val responseTrxId = gson.fromJson(trxIdStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encTrxIdKeyAes, encTrxIdPayloadAes)
        } returns trxIdStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetTrxIdQuery)
        } returns responseTrxEnc

        every {
            bcaLibrary.bcaDataSession1(
                transactionId, ATD, strCurrDateTime
            )
        } returns session1Result

        val createSessionParam = BCAFlazzRequestMapper.createGetBCAGenerateSessionKey(
            gson,
            checkBalanceResult.cardNo,
            strLogSession1,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        } returns pairBCASessionEncryptionResult
        val encSessionParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        val paramGetSessionQuery = BCAFlazzRequestMapper.createEncryptedParam(encSessionParam.first, encSessionParam.second)
        val responseSessionEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encSessionKeyAes, encSessionPayloadAes
        ))
        val responseSession = gson.fromJson(sessionStatus0CardDataEmptyResult, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encSessionKeyAes, encSessionPayloadAes)
        } returns sessionStatus0CardDataEmptyResult
        coEvery {
            bcaFlazzUseCase.execute(paramGetSessionQuery)
        } returns responseSessionEnc

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    checkBalanceResult.cardNo,
                    "${BCABalanceViewModel.TAG_PROCESS_SESSION_KEY}: ${BCABalanceViewModel.ERROR_SESSION_CARD_DATA_EMPTY}",
                    checkBalanceResult.balance.toDouble()
                )
            )
        )
    }

    @Test
    fun checkBalanceGen2ShouldProcessSession_CardDataEmpty_Status1_Fail() {
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
        val responseCheckBalance = gson.fromJson(checkBalanceStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        val createTrxIdParam = BCAFlazzRequestMapper.createGetBCAGenerateTrxId(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        } returns pairBCATrxIdEncryptionResult
        val encTrxIdParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        val paramGetTrxIdQuery = BCAFlazzRequestMapper.createEncryptedParam(encTrxIdParam.first, encTrxIdParam.second)
        val responseTrxEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encTrxIdKeyAes, encTrxIdPayloadAes
        ))
        val responseTrxId = gson.fromJson(trxIdStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encTrxIdKeyAes, encTrxIdPayloadAes)
        } returns trxIdStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetTrxIdQuery)
        } returns responseTrxEnc

        every {
            bcaLibrary.bcaDataSession1(
                transactionId, ATD, strCurrDateTime
            )
        } returns session1Result

        val createSessionParam = BCAFlazzRequestMapper.createGetBCAGenerateSessionKey(
            gson,
            checkBalanceResult.cardNo,
            strLogSession1,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        } returns pairBCASessionEncryptionResult
        val encSessionParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        val paramGetSessionQuery = BCAFlazzRequestMapper.createEncryptedParam(encSessionParam.first, encSessionParam.second)
        val responseSessionEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encSessionKeyAes, encSessionPayloadAes
        ))
        val responseSession = gson.fromJson(sessionStatus1CardDataEmptyResult, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encSessionKeyAes, encSessionPayloadAes)
        } returns sessionStatus1CardDataEmptyResult
        coEvery {
            bcaFlazzUseCase.execute(paramGetSessionQuery)
        } returns responseSessionEnc

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            (bcaBalanceViewModel.bcaInquiry.value) as EmoneyInquiry,
            BCAFlazzResponseMapper.bcaMapper(
                cardNumber,
                responseSession.attributes.lastBalance,
                responseSession.attributes.imageIssuer,
                false,
                responseSession.attributes.amount,
                responseSession.status,
                responseSession.attributes.message,
                responseSession.attributes.hasMorePendingBalance,
            )
        )
    }

    @Test
    fun checkBalanceGen2ShouldProcessSession_Session2_Fail() {
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
        val responseCheckBalance = gson.fromJson(checkBalanceStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        val createTrxIdParam = BCAFlazzRequestMapper.createGetBCAGenerateTrxId(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        } returns pairBCATrxIdEncryptionResult
        val encTrxIdParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        val paramGetTrxIdQuery = BCAFlazzRequestMapper.createEncryptedParam(encTrxIdParam.first, encTrxIdParam.second)
        val responseTrxEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encTrxIdKeyAes, encTrxIdPayloadAes
        ))
        val responseTrxId = gson.fromJson(trxIdStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encTrxIdKeyAes, encTrxIdPayloadAes)
        } returns trxIdStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetTrxIdQuery)
        } returns responseTrxEnc

        every {
            bcaLibrary.bcaDataSession1(
                transactionId, ATD, strCurrDateTime
            )
        } returns session1Result

        val createSessionParam = BCAFlazzRequestMapper.createGetBCAGenerateSessionKey(
            gson,
            checkBalanceResult.cardNo,
            strLogSession1,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        } returns pairBCASessionEncryptionResult
        val encSessionParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        val paramGetSessionQuery = BCAFlazzRequestMapper.createEncryptedParam(encSessionParam.first, encSessionParam.second)
        val responseSessionEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encSessionKeyAes, encSessionPayloadAes
        ))
        val responseSession = gson.fromJson(sessionStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encSessionKeyAes, encSessionPayloadAes)
        } returns sessionStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetSessionQuery)
        } returns responseSessionEnc

        every {
            bcaLibrary.bcaDataSession2(responseSession.attributes.cardData)
        } returns session2FailResult

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    checkBalanceResult.cardNo,
                    "${BCABalanceViewModel.TAG_PROCESS_SDK_SESSION_2}: 9039",
                    checkBalanceResult.balance.toDouble()
                )
            )
        )
    }

    @Test
    fun checkBalanceGen2ShouldProcessSession_Session2_Throwable() {
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
        val responseCheckBalance = gson.fromJson(checkBalanceStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        val createTrxIdParam = BCAFlazzRequestMapper.createGetBCAGenerateTrxId(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        } returns pairBCATrxIdEncryptionResult
        val encTrxIdParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        val paramGetTrxIdQuery = BCAFlazzRequestMapper.createEncryptedParam(encTrxIdParam.first, encTrxIdParam.second)
        val responseTrxEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encTrxIdKeyAes, encTrxIdPayloadAes
        ))
        val responseTrxId = gson.fromJson(trxIdStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encTrxIdKeyAes, encTrxIdPayloadAes)
        } returns trxIdStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetTrxIdQuery)
        } returns responseTrxEnc

        every {
            bcaLibrary.bcaDataSession1(
                transactionId, ATD, strCurrDateTime
            )
        } returns session1Result

        val createSessionParam = BCAFlazzRequestMapper.createGetBCAGenerateSessionKey(
            gson,
            checkBalanceResult.cardNo,
            strLogSession1,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        } returns pairBCASessionEncryptionResult
        val encSessionParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        val paramGetSessionQuery = BCAFlazzRequestMapper.createEncryptedParam(encSessionParam.first, encSessionParam.second)
        val responseSessionEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encSessionKeyAes, encSessionPayloadAes
        ))
        val responseSession = gson.fromJson(sessionStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encSessionKeyAes, encSessionPayloadAes)
        } returns sessionStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetSessionQuery)
        } returns responseSessionEnc

        every {
            bcaLibrary.bcaDataSession2(responseSession.attributes.cardData)
        } throws IOException(ERROR_MESSAGE)

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    checkBalanceResult.cardNo,
                    "${BCABalanceViewModel.TAG_PROCESS_SDK_SESSION_2}: $ERROR_MESSAGE",
                    checkBalanceResult.balance.toDouble()
                )
            )
        )
    }

    @Test
    fun checkBalanceGen2ShouldProcessTopUp_TopUp1Error() {
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
        val responseCheckBalance = gson.fromJson(checkBalanceStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        val createTrxIdParam = BCAFlazzRequestMapper.createGetBCAGenerateTrxId(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        } returns pairBCATrxIdEncryptionResult
        val encTrxIdParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        val paramGetTrxIdQuery = BCAFlazzRequestMapper.createEncryptedParam(encTrxIdParam.first, encTrxIdParam.second)
        val responseTrxEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encTrxIdKeyAes, encTrxIdPayloadAes
        ))
        val responseTrxId = gson.fromJson(trxIdStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encTrxIdKeyAes, encTrxIdPayloadAes)
        } returns trxIdStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetTrxIdQuery)
        } returns responseTrxEnc

        every {
            bcaLibrary.bcaDataSession1(
                transactionId, ATD, strCurrDateTime
            )
        } returns session1Result

        val createSessionParam = BCAFlazzRequestMapper.createGetBCAGenerateSessionKey(
            gson,
            checkBalanceResult.cardNo,
            strLogSession1,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        } returns pairBCASessionEncryptionResult
        val encSessionParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        val paramGetSessionQuery = BCAFlazzRequestMapper.createEncryptedParam(encSessionParam.first, encSessionParam.second)
        val responseSessionEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encSessionKeyAes, encSessionPayloadAes
        ))
        val responseSession = gson.fromJson(sessionStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encSessionKeyAes, encSessionPayloadAes)
        } returns sessionStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetSessionQuery)
        } returns responseSessionEnc

        every {
            bcaLibrary.bcaDataSession2(responseSession.attributes.cardData)
        } returns session2Result

        every {
            bcaLibrary.bcaTopUp1(
                transactionId,
                ATD,
                accessCardNumber,
                accessCode,
                strCurrDateTime,
                amount.toLong()
            )
        } returns topUp1FailResult

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    checkBalanceResult.cardNo,
                    "${BCABalanceViewModel.TAG_PROCESS_SDK_TOP_UP_1}: 9039",
                    checkBalanceResult.balance.toDouble()
                )
            )
        )
    }

    @Test
    fun checkBalanceGen2ShouldProcessTopUp_TopUp1_Throwable() {
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
        val responseCheckBalance = gson.fromJson(checkBalanceStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        val createTrxIdParam = BCAFlazzRequestMapper.createGetBCAGenerateTrxId(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        } returns pairBCATrxIdEncryptionResult
        val encTrxIdParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        val paramGetTrxIdQuery = BCAFlazzRequestMapper.createEncryptedParam(encTrxIdParam.first, encTrxIdParam.second)
        val responseTrxEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encTrxIdKeyAes, encTrxIdPayloadAes
        ))
        val responseTrxId = gson.fromJson(trxIdStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encTrxIdKeyAes, encTrxIdPayloadAes)
        } returns trxIdStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetTrxIdQuery)
        } returns responseTrxEnc

        every {
            bcaLibrary.bcaDataSession1(
                transactionId, ATD, strCurrDateTime
            )
        } returns session1Result

        val createSessionParam = BCAFlazzRequestMapper.createGetBCAGenerateSessionKey(
            gson,
            checkBalanceResult.cardNo,
            strLogSession1,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        } returns pairBCASessionEncryptionResult
        val encSessionParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        val paramGetSessionQuery = BCAFlazzRequestMapper.createEncryptedParam(encSessionParam.first, encSessionParam.second)
        val responseSessionEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encSessionKeyAes, encSessionPayloadAes
        ))
        val responseSession = gson.fromJson(sessionStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encSessionKeyAes, encSessionPayloadAes)
        } returns sessionStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetSessionQuery)
        } returns responseSessionEnc

        every {
            bcaLibrary.bcaDataSession2(responseSession.attributes.cardData)
        } returns session2Result

        every {
            bcaLibrary.bcaTopUp1(
                transactionId,
                ATD,
                accessCardNumber,
                accessCode,
                strCurrDateTime,
                amount.toLong()
            )
        } throws IOException(ERROR_MESSAGE)

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    checkBalanceResult.cardNo,
                    "${BCABalanceViewModel.TAG_PROCESS_SDK_TOP_UP_1}: $ERROR_MESSAGE",
                    checkBalanceResult.balance.toDouble()
                )
            )
        )
    }

    @Test
    fun checkBalanceGen2ShouldProcessTopUp_TopUp1_Error_Then_Reversal() {
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
        val responseCheckBalance = gson.fromJson(checkBalanceStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        val createTrxIdParam = BCAFlazzRequestMapper.createGetBCAGenerateTrxId(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        } returns pairBCATrxIdEncryptionResult
        val encTrxIdParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        val paramGetTrxIdQuery = BCAFlazzRequestMapper.createEncryptedParam(encTrxIdParam.first, encTrxIdParam.second)
        val responseTrxEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encTrxIdKeyAes, encTrxIdPayloadAes
        ))
        val responseTrxId = gson.fromJson(trxIdStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encTrxIdKeyAes, encTrxIdPayloadAes)
        } returns trxIdStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetTrxIdQuery)
        } returns responseTrxEnc

        every {
            bcaLibrary.bcaDataSession1(
                transactionId, ATD, strCurrDateTime
            )
        } returns session1Result

        val createSessionParam = BCAFlazzRequestMapper.createGetBCAGenerateSessionKey(
            gson,
            checkBalanceResult.cardNo,
            strLogSession1,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        } returns pairBCASessionEncryptionResult
        val encSessionParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        val paramGetSessionQuery = BCAFlazzRequestMapper.createEncryptedParam(encSessionParam.first, encSessionParam.second)
        val responseSessionEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encSessionKeyAes, encSessionPayloadAes
        ))
        val responseSession = gson.fromJson(sessionStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encSessionKeyAes, encSessionPayloadAes)
        } returns sessionStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetSessionQuery)
        } returns responseSessionEnc

        every {
            bcaLibrary.bcaDataSession2(responseSession.attributes.cardData)
        } returns session2Result

        every {
            bcaLibrary.bcaTopUp1(
                transactionId,
                ATD,
                accessCardNumber,
                accessCode,
                strCurrDateTime,
                amount.toLong()
            )
        } returns topUp1Result

        val createBetweenTopUpParam = BCAFlazzRequestMapper.createGetBCADataBetweenTopUp(
            gson,
            checkBalanceResult.cardNo,
            strLogTopUp1,
            amount,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBetweenTopUpParam)
        } returns pairBCABetweenTopUpEncryptionResult
        val encBetweenTopUpParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBetweenTopUpParam)
        val paramGetBetweenTopUpQuery = BCAFlazzRequestMapper.createEncryptedParam(encBetweenTopUpParam.first, encBetweenTopUpParam.second)
        val responseBetweenTopUpEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encBetweenTopUpKeyAes, encBetweenTopUpPayloadAes
        ))
        val responseBetweenTopUp = gson.fromJson(topUpStatus2Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encBetweenTopUpKeyAes, encBetweenTopUpPayloadAes)
        } returns topUpStatus2Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetBetweenTopUpQuery)
        } returns responseBetweenTopUpEnc


        every { bcaLibrary.bcaDataReversal(transactionId, ATD) } returns reversalSuccessResult
        val createReversalParam = BCAFlazzRequestMapper.createGetBCADataReversal(
            gson,
            checkBalanceResult.cardNo,
            strLogResponseReversal,
            amount,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
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
            strCurrDateTime,
            ATD,
            messageTopUp2
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
    fun checkBalanceGen2ShouldProcessTopUp_TopUp1_Throw_Then_Reversal() {
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
        val responseCheckBalance = gson.fromJson(checkBalanceStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        val createTrxIdParam = BCAFlazzRequestMapper.createGetBCAGenerateTrxId(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        } returns pairBCATrxIdEncryptionResult
        val encTrxIdParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        val paramGetTrxIdQuery = BCAFlazzRequestMapper.createEncryptedParam(encTrxIdParam.first, encTrxIdParam.second)
        val responseTrxEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encTrxIdKeyAes, encTrxIdPayloadAes
        ))
        val responseTrxId = gson.fromJson(trxIdStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encTrxIdKeyAes, encTrxIdPayloadAes)
        } returns trxIdStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetTrxIdQuery)
        } returns responseTrxEnc

        every {
            bcaLibrary.bcaDataSession1(
                transactionId, ATD, strCurrDateTime
            )
        } returns session1Result

        val createSessionParam = BCAFlazzRequestMapper.createGetBCAGenerateSessionKey(
            gson,
            checkBalanceResult.cardNo,
            strLogSession1,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        } returns pairBCASessionEncryptionResult
        val encSessionParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        val paramGetSessionQuery = BCAFlazzRequestMapper.createEncryptedParam(encSessionParam.first, encSessionParam.second)
        val responseSessionEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encSessionKeyAes, encSessionPayloadAes
        ))
        val responseSession = gson.fromJson(sessionStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encSessionKeyAes, encSessionPayloadAes)
        } returns sessionStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetSessionQuery)
        } returns responseSessionEnc

        every {
            bcaLibrary.bcaDataSession2(responseSession.attributes.cardData)
        } returns session2Result

        every {
            bcaLibrary.bcaTopUp1(
                transactionId,
                ATD,
                accessCardNumber,
                accessCode,
                strCurrDateTime,
                amount.toLong()
            )
        } returns topUp1Result

        val createBetweenTopUpParam = BCAFlazzRequestMapper.createGetBCADataBetweenTopUp(
            gson,
            checkBalanceResult.cardNo,
            strLogTopUp1,
            amount,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBetweenTopUpParam)
        } returns pairBCABetweenTopUpEncryptionResult
        val encBetweenTopUpParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBetweenTopUpParam)
        val paramGetBetweenTopUpQuery = BCAFlazzRequestMapper.createEncryptedParam(encBetweenTopUpParam.first, encBetweenTopUpParam.second)
        val responseBetweenTopUpEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encBetweenTopUpKeyAes, encBetweenTopUpPayloadAes
        ))
        val responseBetweenTopUp = gson.fromJson(topUpStatus2Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encBetweenTopUpKeyAes, encBetweenTopUpPayloadAes)
        } returns topUpStatus2Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetBetweenTopUpQuery)
        } throws IOException()


        every { bcaLibrary.bcaDataReversal(transactionId, ATD) } returns reversalSuccessResult
        val createReversalParam = BCAFlazzRequestMapper.createGetBCADataReversal(
            gson,
            checkBalanceResult.cardNo,
            strLogResponseReversal,
            amount,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
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
            strCurrDateTime,
            ATD,
            messageTopUp2
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
    fun checkBalanceGen2ShouldProcessTopUp_TopUp1_Write_But_Card_Data_Empty_Then_Reversal() {
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
        val responseCheckBalance = gson.fromJson(checkBalanceStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        val createTrxIdParam = BCAFlazzRequestMapper.createGetBCAGenerateTrxId(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        } returns pairBCATrxIdEncryptionResult
        val encTrxIdParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        val paramGetTrxIdQuery = BCAFlazzRequestMapper.createEncryptedParam(encTrxIdParam.first, encTrxIdParam.second)
        val responseTrxEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encTrxIdKeyAes, encTrxIdPayloadAes
        ))
        val responseTrxId = gson.fromJson(trxIdStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encTrxIdKeyAes, encTrxIdPayloadAes)
        } returns trxIdStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetTrxIdQuery)
        } returns responseTrxEnc

        every {
            bcaLibrary.bcaDataSession1(
                transactionId, ATD, strCurrDateTime
            )
        } returns session1Result

        val createSessionParam = BCAFlazzRequestMapper.createGetBCAGenerateSessionKey(
            gson,
            checkBalanceResult.cardNo,
            strLogSession1,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        } returns pairBCASessionEncryptionResult
        val encSessionParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        val paramGetSessionQuery = BCAFlazzRequestMapper.createEncryptedParam(encSessionParam.first, encSessionParam.second)
        val responseSessionEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encSessionKeyAes, encSessionPayloadAes
        ))
        val responseSession = gson.fromJson(sessionStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encSessionKeyAes, encSessionPayloadAes)
        } returns sessionStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetSessionQuery)
        } returns responseSessionEnc

        every {
            bcaLibrary.bcaDataSession2(responseSession.attributes.cardData)
        } returns session2Result

        every {
            bcaLibrary.bcaTopUp1(
                transactionId,
                ATD,
                accessCardNumber,
                accessCode,
                strCurrDateTime,
                amount.toLong()
            )
        } returns topUp1Result

        val createBetweenTopUpParam = BCAFlazzRequestMapper.createGetBCADataBetweenTopUp(
            gson,
            checkBalanceResult.cardNo,
            strLogTopUp1,
            amount,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBetweenTopUpParam)
        } returns pairBCABetweenTopUpEncryptionResult
        val encBetweenTopUpParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBetweenTopUpParam)
        val paramGetBetweenTopUpQuery = BCAFlazzRequestMapper.createEncryptedParam(encBetweenTopUpParam.first, encBetweenTopUpParam.second)
        val responseBetweenTopUpEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encBetweenTopUpKeyAes, encBetweenTopUpPayloadAes
        ))
        val responseBetweenTopUp = gson.fromJson(topUpStatus0CardDataEmptyResult, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encBetweenTopUpKeyAes, encBetweenTopUpPayloadAes)
        } returns topUpStatus0CardDataEmptyResult
        coEvery {
            bcaFlazzUseCase.execute(paramGetBetweenTopUpQuery)
        } returns responseBetweenTopUpEnc


        every { bcaLibrary.bcaDataReversal(transactionId, ATD) } returns reversalSuccessResult
        val createReversalParam = BCAFlazzRequestMapper.createGetBCADataReversal(
            gson,
            checkBalanceResult.cardNo,
            strLogResponseReversal,
            amount,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
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
            strCurrDateTime,
            ATD,
            messageTopUp2
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
    fun checkBalanceGen2ShouldProcessTopUp_TopUp1_Done() {
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
        val responseCheckBalance = gson.fromJson(checkBalanceStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        val createTrxIdParam = BCAFlazzRequestMapper.createGetBCAGenerateTrxId(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        } returns pairBCATrxIdEncryptionResult
        val encTrxIdParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        val paramGetTrxIdQuery = BCAFlazzRequestMapper.createEncryptedParam(encTrxIdParam.first, encTrxIdParam.second)
        val responseTrxEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encTrxIdKeyAes, encTrxIdPayloadAes
        ))
        val responseTrxId = gson.fromJson(trxIdStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encTrxIdKeyAes, encTrxIdPayloadAes)
        } returns trxIdStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetTrxIdQuery)
        } returns responseTrxEnc

        every {
            bcaLibrary.bcaDataSession1(
                transactionId, ATD, strCurrDateTime
            )
        } returns session1Result

        val createSessionParam = BCAFlazzRequestMapper.createGetBCAGenerateSessionKey(
            gson,
            checkBalanceResult.cardNo,
            strLogSession1,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        } returns pairBCASessionEncryptionResult
        val encSessionParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        val paramGetSessionQuery = BCAFlazzRequestMapper.createEncryptedParam(encSessionParam.first, encSessionParam.second)
        val responseSessionEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encSessionKeyAes, encSessionPayloadAes
        ))
        val responseSession = gson.fromJson(sessionStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encSessionKeyAes, encSessionPayloadAes)
        } returns sessionStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetSessionQuery)
        } returns responseSessionEnc

        every {
            bcaLibrary.bcaDataSession2(responseSession.attributes.cardData)
        } returns session2Result

        every {
            bcaLibrary.bcaTopUp1(
                transactionId,
                ATD,
                accessCardNumber,
                accessCode,
                strCurrDateTime,
                amount.toLong()
            )
        } returns topUp1Result

        val createBetweenTopUpParam = BCAFlazzRequestMapper.createGetBCADataBetweenTopUp(
            gson,
            checkBalanceResult.cardNo,
            strLogTopUp1,
            amount,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBetweenTopUpParam)
        } returns pairBCABetweenTopUpEncryptionResult
        val encBetweenTopUpParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBetweenTopUpParam)
        val paramGetBetweenTopUpQuery = BCAFlazzRequestMapper.createEncryptedParam(encBetweenTopUpParam.first, encBetweenTopUpParam.second)
        val responseBetweenTopUpEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encBetweenTopUpKeyAes, encBetweenTopUpPayloadAes
        ))
        val responseBetweenTopUp = gson.fromJson(topUpStatus1Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encBetweenTopUpKeyAes, encBetweenTopUpPayloadAes)
        } returns topUpStatus1Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetBetweenTopUpQuery)
        } returns responseBetweenTopUpEnc

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            responseBetweenTopUp.attributes.message
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    checkBalanceResult.cardNo,
                    "${BCABalanceViewModel.TAG_PROCESS_BETWEEN_TOP_UP}: ${responseBetweenTopUp.attributes.message}",
                    checkBalanceResult.balance.toDouble()
                )
            )
        )
    }

    @Test
    fun checkBalanceGen2ShouldProcessTopUp_TopUp1_StatusUnexpected() {
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
        val responseCheckBalance = gson.fromJson(checkBalanceStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        val createTrxIdParam = BCAFlazzRequestMapper.createGetBCAGenerateTrxId(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        } returns pairBCATrxIdEncryptionResult
        val encTrxIdParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        val paramGetTrxIdQuery = BCAFlazzRequestMapper.createEncryptedParam(encTrxIdParam.first, encTrxIdParam.second)
        val responseTrxEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encTrxIdKeyAes, encTrxIdPayloadAes
        ))
        val responseTrxId = gson.fromJson(trxIdStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encTrxIdKeyAes, encTrxIdPayloadAes)
        } returns trxIdStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetTrxIdQuery)
        } returns responseTrxEnc

        every {
            bcaLibrary.bcaDataSession1(
                transactionId, ATD, strCurrDateTime
            )
        } returns session1Result

        val createSessionParam = BCAFlazzRequestMapper.createGetBCAGenerateSessionKey(
            gson,
            checkBalanceResult.cardNo,
            strLogSession1,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        } returns pairBCASessionEncryptionResult
        val encSessionParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        val paramGetSessionQuery = BCAFlazzRequestMapper.createEncryptedParam(encSessionParam.first, encSessionParam.second)
        val responseSessionEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encSessionKeyAes, encSessionPayloadAes
        ))
        val responseSession = gson.fromJson(sessionStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encSessionKeyAes, encSessionPayloadAes)
        } returns sessionStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetSessionQuery)
        } returns responseSessionEnc

        every {
            bcaLibrary.bcaDataSession2(responseSession.attributes.cardData)
        } returns session2Result

        every {
            bcaLibrary.bcaTopUp1(
                transactionId,
                ATD,
                accessCardNumber,
                accessCode,
                strCurrDateTime,
                amount.toLong()
            )
        } returns topUp1Result

        val createBetweenTopUpParam = BCAFlazzRequestMapper.createGetBCADataBetweenTopUp(
            gson,
            checkBalanceResult.cardNo,
            strLogTopUp1,
            amount,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBetweenTopUpParam)
        } returns pairBCABetweenTopUpEncryptionResult
        val encBetweenTopUpParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBetweenTopUpParam)
        val paramGetBetweenTopUpQuery = BCAFlazzRequestMapper.createEncryptedParam(encBetweenTopUpParam.first, encBetweenTopUpParam.second)
        val responseBetweenTopUpEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encBetweenTopUpKeyAes, encBetweenTopUpPayloadAes
        ))
        val responseBetweenTopUp = gson.fromJson(topUpStatusUnexpectedResult, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encBetweenTopUpKeyAes, encBetweenTopUpPayloadAes)
        } returns topUpStatusUnexpectedResult
        coEvery {
            bcaFlazzUseCase.execute(paramGetBetweenTopUpQuery)
        } returns responseBetweenTopUpEnc

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            (bcaBalanceViewModel.bcaInquiry.value) as EmoneyInquiry,
            BCAFlazzResponseMapper.bcaMapper(
                cardNumber,
                responseBetweenTopUp.attributes.lastBalance,
                responseBetweenTopUp.attributes.imageIssuer,
                false,
                responseBetweenTopUp.attributes.amount,
                responseBetweenTopUp.status,
                responseBetweenTopUp.attributes.message,
                responseBetweenTopUp.attributes.hasMorePendingBalance,
            )
        )
    }

    @Test
    fun checkBalanceGen2ShouldProcessTopUp_TopUp2_Then_ACK() {
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
        val responseCheckBalance = gson.fromJson(checkBalanceStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        val createTrxIdParam = BCAFlazzRequestMapper.createGetBCAGenerateTrxId(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        } returns pairBCATrxIdEncryptionResult
        val encTrxIdParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        val paramGetTrxIdQuery = BCAFlazzRequestMapper.createEncryptedParam(encTrxIdParam.first, encTrxIdParam.second)
        val responseTrxEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encTrxIdKeyAes, encTrxIdPayloadAes
        ))
        val responseTrxId = gson.fromJson(trxIdStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encTrxIdKeyAes, encTrxIdPayloadAes)
        } returns trxIdStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetTrxIdQuery)
        } returns responseTrxEnc

        every {
            bcaLibrary.bcaDataSession1(
                transactionId, ATD, strCurrDateTime
            )
        } returns session1Result

        val createSessionParam = BCAFlazzRequestMapper.createGetBCAGenerateSessionKey(
            gson,
            checkBalanceResult.cardNo,
            strLogSession1,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        } returns pairBCASessionEncryptionResult
        val encSessionParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        val paramGetSessionQuery = BCAFlazzRequestMapper.createEncryptedParam(encSessionParam.first, encSessionParam.second)
        val responseSessionEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encSessionKeyAes, encSessionPayloadAes
        ))
        val responseSession = gson.fromJson(sessionStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encSessionKeyAes, encSessionPayloadAes)
        } returns sessionStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetSessionQuery)
        } returns responseSessionEnc

        every {
            bcaLibrary.bcaDataSession2(responseSession.attributes.cardData)
        } returns session2Result

        every {
            bcaLibrary.bcaTopUp1(
                transactionId,
                ATD,
                accessCardNumber,
                accessCode,
                strCurrDateTime,
                amount.toLong()
            )
        } returns topUp1Result

        val createBetweenTopUpParam = BCAFlazzRequestMapper.createGetBCADataBetweenTopUp(
            gson,
            checkBalanceResult.cardNo,
            strLogTopUp1,
            amount,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBetweenTopUpParam)
        } returns pairBCABetweenTopUpEncryptionResult
        val encBetweenTopUpParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBetweenTopUpParam)
        val paramGetBetweenTopUpQuery = BCAFlazzRequestMapper.createEncryptedParam(encBetweenTopUpParam.first, encBetweenTopUpParam.second)
        val responseBetweenTopUpEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encBetweenTopUpKeyAes, encBetweenTopUpPayloadAes
        ))
        val responseBetweenTopUp = gson.fromJson(topUpStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encBetweenTopUpKeyAes, encBetweenTopUpPayloadAes)
        } returns topUpStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetBetweenTopUpQuery)
        } returns responseBetweenTopUpEnc

        every {
            bcaLibrary.bcaTopUp2(
                responseBetweenTopUp.attributes.cardData
            )
        } returns topUp2Result

        val createBCALastBCATopUpParam = BCAFlazzRequestMapper.createGetBCADataACKTopUp(
            gson,
            checkBalanceResult.cardNo,
            strLogTopUp2,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBCALastBCATopUpParam)
        } returns pairBCALastBCATopUpEncryptionResult
        val encBCALastParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBCALastBCATopUpParam)
        val paramGetBCALastQuery = BCAFlazzRequestMapper.createEncryptedParam(encBCALastParam.first, encBCALastParam.second)
        val responseBCALastEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encBCALastBCATopUpKeyAes, encBCALastBCATopUpPayloadAes
        ))
        val responseACK = gson.fromJson(ackResult, BCAFlazzData::class.java)
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
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            (bcaBalanceViewModel.bcaInquiry.value) as EmoneyInquiry,
            BCAFlazzResponseMapper.bcaMapper(
                cardNumber,
                responseACK.attributes.lastBalance,
                responseACK.attributes.imageIssuer,
                false,
                responseACK.attributes.amount,
                responseACK.status,
                responseACK.attributes.message,
                responseACK.attributes.hasMorePendingBalance,
                false,
                "",
                messageTopUp2,
            )
        )
    }

    @Test
    fun checkBalanceGen2ShouldProcessTopUp_TopUp2_StrLogResp_NotEmpty_Then_ACK() {
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
        val responseCheckBalance = gson.fromJson(checkBalanceStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        val createTrxIdParam = BCAFlazzRequestMapper.createGetBCAGenerateTrxId(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        } returns pairBCATrxIdEncryptionResult
        val encTrxIdParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        val paramGetTrxIdQuery = BCAFlazzRequestMapper.createEncryptedParam(encTrxIdParam.first, encTrxIdParam.second)
        val responseTrxEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encTrxIdKeyAes, encTrxIdPayloadAes
        ))
        val responseTrxId = gson.fromJson(trxIdStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encTrxIdKeyAes, encTrxIdPayloadAes)
        } returns trxIdStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetTrxIdQuery)
        } returns responseTrxEnc

        every {
            bcaLibrary.bcaDataSession1(
                transactionId, ATD, strCurrDateTime
            )
        } returns session1Result

        val createSessionParam = BCAFlazzRequestMapper.createGetBCAGenerateSessionKey(
            gson,
            checkBalanceResult.cardNo,
            strLogSession1,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        } returns pairBCASessionEncryptionResult
        val encSessionParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        val paramGetSessionQuery = BCAFlazzRequestMapper.createEncryptedParam(encSessionParam.first, encSessionParam.second)
        val responseSessionEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encSessionKeyAes, encSessionPayloadAes
        ))
        val responseSession = gson.fromJson(sessionStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encSessionKeyAes, encSessionPayloadAes)
        } returns sessionStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetSessionQuery)
        } returns responseSessionEnc

        every {
            bcaLibrary.bcaDataSession2(responseSession.attributes.cardData)
        } returns session2Result

        every {
            bcaLibrary.bcaTopUp1(
                transactionId,
                ATD,
                accessCardNumber,
                accessCode,
                strCurrDateTime,
                amount.toLong()
            )
        } returns topUp1Result

        val createBetweenTopUpParam = BCAFlazzRequestMapper.createGetBCADataBetweenTopUp(
            gson,
            checkBalanceResult.cardNo,
            strLogTopUp1,
            amount,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBetweenTopUpParam)
        } returns pairBCABetweenTopUpEncryptionResult
        val encBetweenTopUpParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBetweenTopUpParam)
        val paramGetBetweenTopUpQuery = BCAFlazzRequestMapper.createEncryptedParam(encBetweenTopUpParam.first, encBetweenTopUpParam.second)
        val responseBetweenTopUpEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encBetweenTopUpKeyAes, encBetweenTopUpPayloadAes
        ))
        val responseBetweenTopUp = gson.fromJson(topUpStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encBetweenTopUpKeyAes, encBetweenTopUpPayloadAes)
        } returns topUpStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetBetweenTopUpQuery)
        } returns responseBetweenTopUpEnc

        every {
            bcaLibrary.bcaTopUp2(
                responseBetweenTopUp.attributes.cardData
            )
        } returns topUp2FailResult

        val createBCALastBCATopUpParam = BCAFlazzRequestMapper.createGetBCADataACKTopUp(
            gson,
            checkBalanceResult.cardNo,
            strLogTopUp2,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBCALastBCATopUpParam)
        } returns pairBCALastBCATopUpEncryptionResult
        val encBCALastParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBCALastBCATopUpParam)
        val paramGetBCALastQuery = BCAFlazzRequestMapper.createEncryptedParam(encBCALastParam.first, encBCALastParam.second)
        val responseBCALastEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encBCALastBCATopUpKeyAes, encBCALastBCATopUpPayloadAes
        ))
        val responseACK = gson.fromJson(ackResult, BCAFlazzData::class.java)
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
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            (bcaBalanceViewModel.bcaInquiry.value) as EmoneyInquiry,
            BCAFlazzResponseMapper.bcaMapper(
                cardNumber,
                responseACK.attributes.lastBalance,
                responseACK.attributes.imageIssuer,
                false,
                responseACK.attributes.amount,
                responseACK.status,
                responseACK.attributes.message,
                responseACK.attributes.hasMorePendingBalance,
                false,
                "",
                messageTopUp2,
                true
            )
        )

        Assert.assertEquals((bcaBalanceViewModel.errorTagLog.value) as RechargeEmoneyInquiryLogRequest, RechargeEmoneyInquiryLogRequest(
            log = EmoneyInquiryLogRequest(
                5L,
                0,
                cardNumber,
                "PROCESS_SDK_TOP_UP_2: 0000",
                2000.0
            )
        ))
    }

    @Test
    fun checkBalanceGen2ShouldProcessTopUp_TopUp2_StrLogResp_Empty_Then_ACK() {
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
        val responseCheckBalance = gson.fromJson(checkBalanceStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        val createTrxIdParam = BCAFlazzRequestMapper.createGetBCAGenerateTrxId(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        } returns pairBCATrxIdEncryptionResult
        val encTrxIdParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        val paramGetTrxIdQuery = BCAFlazzRequestMapper.createEncryptedParam(encTrxIdParam.first, encTrxIdParam.second)
        val responseTrxEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encTrxIdKeyAes, encTrxIdPayloadAes
        ))
        val responseTrxId = gson.fromJson(trxIdStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encTrxIdKeyAes, encTrxIdPayloadAes)
        } returns trxIdStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetTrxIdQuery)
        } returns responseTrxEnc

        every {
            bcaLibrary.bcaDataSession1(
                transactionId, ATD, strCurrDateTime
            )
        } returns session1Result

        val createSessionParam = BCAFlazzRequestMapper.createGetBCAGenerateSessionKey(
            gson,
            checkBalanceResult.cardNo,
            strLogSession1,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        } returns pairBCASessionEncryptionResult
        val encSessionParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        val paramGetSessionQuery = BCAFlazzRequestMapper.createEncryptedParam(encSessionParam.first, encSessionParam.second)
        val responseSessionEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encSessionKeyAes, encSessionPayloadAes
        ))
        val responseSession = gson.fromJson(sessionStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encSessionKeyAes, encSessionPayloadAes)
        } returns sessionStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetSessionQuery)
        } returns responseSessionEnc

        every {
            bcaLibrary.bcaDataSession2(responseSession.attributes.cardData)
        } returns session2Result

        every {
            bcaLibrary.bcaTopUp1(
                transactionId,
                ATD,
                accessCardNumber,
                accessCode,
                strCurrDateTime,
                amount.toLong()
            )
        } returns topUp1Result

        val createBetweenTopUpParam = BCAFlazzRequestMapper.createGetBCADataBetweenTopUp(
            gson,
            checkBalanceResult.cardNo,
            strLogTopUp1,
            amount,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBetweenTopUpParam)
        } returns pairBCABetweenTopUpEncryptionResult
        val encBetweenTopUpParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBetweenTopUpParam)
        val paramGetBetweenTopUpQuery = BCAFlazzRequestMapper.createEncryptedParam(encBetweenTopUpParam.first, encBetweenTopUpParam.second)
        val responseBetweenTopUpEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encBetweenTopUpKeyAes, encBetweenTopUpPayloadAes
        ))
        val responseBetweenTopUp = gson.fromJson(topUpStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encBetweenTopUpKeyAes, encBetweenTopUpPayloadAes)
        } returns topUpStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetBetweenTopUpQuery)
        } returns responseBetweenTopUpEnc

        every {
            bcaLibrary.bcaTopUp2(
                responseBetweenTopUp.attributes.cardData
            )
        } returns topUp2EmptyStrLogResult

        every { bcaLibrary.bcaLastBCATopUp() } returns bcaLastBCATopUpResult
        val createBCALastBCATopUpParam = BCAFlazzRequestMapper.createGetBCADataACKTopUp(
            gson,
            checkBalanceResult.cardNo,
            strLogResponseBCALastBCATopUp,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBCALastBCATopUpParam)
        } returns pairBCALastBCATopUpEncryptionResult
        val encBCALastParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBCALastBCATopUpParam)
        val paramGetBCALastQuery = BCAFlazzRequestMapper.createEncryptedParam(encBCALastParam.first, encBCALastParam.second)
        val responseBCALastEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encBCALastBCATopUpKeyAes, encBCALastBCATopUpPayloadAes
        ))
        val responseACK = gson.fromJson(ackResult, BCAFlazzData::class.java)
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
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            (bcaBalanceViewModel.bcaInquiry.value) as EmoneyInquiry,
            BCAFlazzResponseMapper.bcaMapper(
                cardNumber,
                responseACK.attributes.lastBalance,
                responseACK.attributes.imageIssuer,
                false,
                responseACK.attributes.amount,
                responseACK.status,
                responseACK.attributes.message,
                responseACK.attributes.hasMorePendingBalance,
                false,
                "",
                messageTopUp2,
                true
            )
        )
    }

    @Test
    fun checkBalanceGen2ShouldProcessTopUp_TopUp2_Throw_Then_Error() {
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
        val responseCheckBalance = gson.fromJson(checkBalanceStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkBalanceStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc


        val createTrxIdParam = BCAFlazzRequestMapper.createGetBCAGenerateTrxId(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_TWO
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        } returns pairBCATrxIdEncryptionResult
        val encTrxIdParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createTrxIdParam)
        val paramGetTrxIdQuery = BCAFlazzRequestMapper.createEncryptedParam(encTrxIdParam.first, encTrxIdParam.second)
        val responseTrxEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encTrxIdKeyAes, encTrxIdPayloadAes
        ))
        val responseTrxId = gson.fromJson(trxIdStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encTrxIdKeyAes, encTrxIdPayloadAes)
        } returns trxIdStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetTrxIdQuery)
        } returns responseTrxEnc

        every {
            bcaLibrary.bcaDataSession1(
                transactionId, ATD, strCurrDateTime
            )
        } returns session1Result

        val createSessionParam = BCAFlazzRequestMapper.createGetBCAGenerateSessionKey(
            gson,
            checkBalanceResult.cardNo,
            strLogSession1,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        } returns pairBCASessionEncryptionResult
        val encSessionParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createSessionParam)
        val paramGetSessionQuery = BCAFlazzRequestMapper.createEncryptedParam(encSessionParam.first, encSessionParam.second)
        val responseSessionEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encSessionKeyAes, encSessionPayloadAes
        ))
        val responseSession = gson.fromJson(sessionStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encSessionKeyAes, encSessionPayloadAes)
        } returns sessionStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetSessionQuery)
        } returns responseSessionEnc

        every {
            bcaLibrary.bcaDataSession2(responseSession.attributes.cardData)
        } returns session2Result

        every {
            bcaLibrary.bcaTopUp1(
                transactionId,
                ATD,
                accessCardNumber,
                accessCode,
                strCurrDateTime,
                amount.toLong()
            )
        } returns topUp1Result

        val createBetweenTopUpParam = BCAFlazzRequestMapper.createGetBCADataBetweenTopUp(
            gson,
            checkBalanceResult.cardNo,
            strLogTopUp1,
            amount,
            checkBalanceResult.balance,
            transactionId,
            GEN_TWO,
            "0000"
        )
        every {
            electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBetweenTopUpParam)
        } returns pairBCABetweenTopUpEncryptionResult
        val encBetweenTopUpParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createBetweenTopUpParam)
        val paramGetBetweenTopUpQuery = BCAFlazzRequestMapper.createEncryptedParam(encBetweenTopUpParam.first, encBetweenTopUpParam.second)
        val responseBetweenTopUpEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encBetweenTopUpKeyAes, encBetweenTopUpPayloadAes
        ))
        val responseBetweenTopUp = gson.fromJson(topUpStatus0Result, BCAFlazzData::class.java)
        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encBetweenTopUpKeyAes, encBetweenTopUpPayloadAes)
        } returns topUpStatus0Result
        coEvery {
            bcaFlazzUseCase.execute(paramGetBetweenTopUpQuery)
        } returns responseBetweenTopUpEnc

        every {
            bcaLibrary.bcaTopUp2(
                responseBetweenTopUp.attributes.cardData
            )
        } throws IOException(ERROR_MESSAGE)

        //when
        bcaBalanceViewModel.processBCATagBalance(
            isoDep,
            merchantId,
            terminalId,
            mockPublicKeyString,
            mockPrivateKeyString,
            strCurrDateTime,
            ATD,
            messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    checkBalanceResult.cardNo,
                    "${BCABalanceViewModel.TAG_PROCESS_SDK_TOP_UP_2}: $ERROR_MESSAGE",
                    checkBalanceResult.balance.toDouble()
                )
            )
        )
    }

    @Test
    fun processSDKBCADataSession1_checkIsoDep() {
        //when
        bcaBalanceViewModel.processSDKBCADataSession1(
            nullIsoDep,
            "",
            0,"","","","","","", messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    "",
                    "${BCABalanceViewModel.TAG_PROCESS_SDK_SESSION_1}: ${BCABalanceViewModel.ERROR_MESSAGE_ISODEP}",
                    0.0
                )
            )
        )
    }

    @Test
    fun processSDKBCADataSession2_checkIsoDep() {
        //when
        bcaBalanceViewModel.processSDKBCADataSession2(
            nullIsoDep,
            "",
            0,"","","","","","", BCAFlazzData(), messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    "",
                    "${BCABalanceViewModel.TAG_PROCESS_SDK_SESSION_2}: ${BCABalanceViewModel.ERROR_MESSAGE_ISODEP}",
                    0.0
                )
            )
        )
    }

    @Test
    fun processSDKBCATopUp1_checkIsoDep() {
        //when
        bcaBalanceViewModel.processSDKBCATopUp1(
            nullIsoDep,
            "",
            0,"","","","","","",BCAFlazzData(), messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    "",
                    "${BCABalanceViewModel.TAG_PROCESS_SDK_TOP_UP_1}: ${BCABalanceViewModel.ERROR_MESSAGE_ISODEP}",
                    0.0
                )
            )
        )
    }

    @Test
    fun processSDKBCATopUp2_checkIsoDep() {
        //when
        bcaBalanceViewModel.processSDKBCATopUp2(
            nullIsoDep,
            "",
            "","","","",BCAFlazzData(), 0, messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    "",
                    "${BCABalanceViewModel.TAG_PROCESS_SDK_TOP_UP_2}: ${BCABalanceViewModel.ERROR_MESSAGE_ISODEP}",
                    0.0
                )
            )
        )
    }

    @Test
    fun processSDKReversal_checkIsoDep() {
        //when
        bcaBalanceViewModel.processSDKReversal(
            nullIsoDep,
            "",
            "","",0,"","","", BCAFlazzData(), messageTopUp2
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    "",
                    "${BCABalanceViewModel.TAG_PROCESS_SDK_REVERSAL}: ${BCABalanceViewModel.ERROR_MESSAGE_ISODEP}",
                    0.0
                )
            )
        )
    }

    @Test
    fun processSDKBCAlastBCATopUp_checkIsoDep() {
        //when
        bcaBalanceViewModel.processSDKBCAlastBCATopUp(
            nullIsoDep,
            "",
            "","","","", 0, messageTopUp2, false
        )
        //then
        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).first.message,
            ERROR_MESSAGE
        )

        Assert.assertEquals(
            ((bcaBalanceViewModel.errorCardMessage.value) as Pair<Throwable, RechargeEmoneyInquiryLogRequest>).second,
            RechargeEmoneyInquiryLogRequest(
                log = EmoneyInquiryLogRequest(
                    5,
                    0,
                    "",
                    "${BCABalanceViewModel.TAG_PROCESS_SDK_BCALASTBCATOP_UP}: ${BCABalanceViewModel.ERROR_MESSAGE_ISODEP}",
                    0.0
                )
            )
        )
    }

}
