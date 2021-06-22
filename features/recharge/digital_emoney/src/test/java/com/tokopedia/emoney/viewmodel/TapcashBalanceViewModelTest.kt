package com.tokopedia.emoney.viewmodel

import android.nfc.tech.IsoDep
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.common_electronic_money.data.EmoneyInquiry

import com.tokopedia.common_electronic_money.util.NFCUtils
import com.tokopedia.emoney.data.BalanceTapcash
import com.tokopedia.emoney.util.TapcashObjectMapper.mapTapcashtoEmoney
import com.tokopedia.emoney.viewmodel.TapcashBalanceViewModel.Companion.COMMAND_GET_CHALLENGE
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class TapcashBalanceViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    @MockK
    lateinit var isoDep: IsoDep

    private lateinit var tapcashBalanceViewModel: TapcashBalanceViewModel
    private lateinit var emptyByteNfc: ByteArray
    private lateinit var challangeResultSuccess: ByteArray
    private lateinit var challangeResultFail: ByteArray

    val challangeResult = "895EEC0E771D3A369000"
    val challangeFail = "6A80"
    val secureRequest = "903203000A1201EFCE6ACABEA98BF900"
    val secureResult = "0701004E2000000075461300000568547546130000056854C1F125BD000000000600271031CAD03F0220000000000600271031CAD03F0000000000000000000000000000000000010000000000001E8480000000000000000000000000000034780056F547A8C5C9BD9CB81358802B62D49000"
    val terminalRandomNumber = "EFCE6ACABEA98BF9"
    val writeRequest = "90361401250314021403EFCE6ACABEA98BF985A00E33BD0F26DFDB71CA6C6CBCE500000000000000000018"
    val writeResult = "06D514A3859256C46C23792A757F400F3C258FB7A98578D9009C40B65448823BB65448823B30FA7200000400000400009000"
    val writeResultFailedSize = "0614A3859256C46C23792A757F400F3C258FB7A98578D9009C40B65448823BB65448823B30FA7200000400000400009000"

    val dummyResponseNoCrypto = """
        {"rechargeUpdateBalanceEmoneyBniTapcash":{"attributes":{"cryptogram":"","rrn":0,"amount":10000,"button_text":"Top-up Sekarang","image_issuer":"https://ecs7.tokopedia.net/img/attachment/2020/11/12/66301108/66301108_3bd5585d-f39b-4d62-b7da-fe677b200e1a.png","card_number":"7546130000056854"},"error":{"id":0,"title":"Ini saldo kamu yang paling baru, ya.","status":0}}}
    """.trimIndent()

    val dummyResponseWithCrypto = """
        {"rechargeUpdateBalanceEmoneyBniTapcash":{"attributes":{"cryptogram":"0600271031CADAAE000000000000000085A00E33BD0F26DFDB71CA6C6CBCE500","rrn":0,"amount":40000,"button_text":"Top-up Sekarang","image_issuer":"https://ecs7.tokopedia.net/img/attachment/2020/11/12/66301108/66301108_3bd5585d-f39b-4d62-b7da-fe677b200e1a.png","card_number":"7546130000056854"},"error":{"id":0,"title":"Ini saldo kamu yang paling baru, ya.","status":0}}}
    """.trimIndent()

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        mockkObject(NFCUtils.Companion)

        emptyByteNfc = byteArrayOf()
        challangeResultSuccess = NFCUtils.stringToByteArrayRadix(challangeResult)
        challangeResultFail = NFCUtils.stringToByteArrayRadix(challangeFail)
        tapcashBalanceViewModel = spyk(TapcashBalanceViewModel(graphqlRepository, Dispatchers.Unconfined))
    }

    private fun initSuccessData(){
        every { isoDep.close() } returns mockk()
        every { isoDep.connect() } returns mockk()
        every { isoDep.setTimeout(any()) } returns mockk()
        every { isoDep.isConnected } returns true
        every { isoDep.tag.id } returns emptyByteNfc
    }

    @Test
    fun processTagIntent_WriteBalanceTapcash_FailedChallange() {
        //given
        initSuccessData()
        every { isoDep.transceive(COMMAND_GET_CHALLENGE) } returns challangeResultFail

        //when
        tapcashBalanceViewModel.processTapCashTagIntent(isoDep, "")

        //then
        assertEquals(((tapcashBalanceViewModel.errorCardMessage.value) as Throwable).message, "Maaf, cek saldo belum berhasil")
    }

    @Test
    fun processTagIntent_WriteBalanceTapcash_SuccessChallangeFailedSecurePurse() {
        //given
        initSuccessData()
        val byteRequest = NFCUtils.stringToByteArrayRadix(secureRequest)
        every { isoDep.transceive(COMMAND_GET_CHALLENGE) } returns challangeResultSuccess
        every { tapcashBalanceViewModel.getRandomString() } returns terminalRandomNumber
        every { isoDep.transceive(byteRequest) } returns challangeResultFail

        //when
        tapcashBalanceViewModel.processTapCashTagIntent(isoDep, "")

        //then
        assertEquals(((tapcashBalanceViewModel.errorCardMessage.value) as Throwable).message, "Maaf, cek saldo belum berhasil")
    }

    @Test
    fun processTagIntent_WriteBalanceTapcash_SuccessChallangeSuccessSecurePurseCardFailed() {
        //given
        initSuccessData()
        val secureByteRequest = NFCUtils.stringToByteArrayRadix(secureRequest)
        val secureByteResult =
                NFCUtils.stringToByteArrayRadix(secureResult)
        every { isoDep.transceive(COMMAND_GET_CHALLENGE) } returns challangeResultSuccess
        every { tapcashBalanceViewModel.getRandomString() } returns terminalRandomNumber
        every { isoDep.transceive(secureByteRequest) } returns secureByteResult
        every { tapcashBalanceViewModel.getCardData(secureResult, terminalRandomNumber,challangeResult) } returns ""

        //when
        tapcashBalanceViewModel.processTapCashTagIntent(isoDep, "")

        //then
        assertEquals(((tapcashBalanceViewModel.errorCardMessage.value) as Throwable).message, "Maaf, cek saldo belum berhasil")
    }

    @Test
    fun getCardData_SuccessGetCardData_Success() {
        //given
        val cardData = "000175461300000568547546130000056854EFCE6ACABEA98BF9895EEC0E771D3A360000000001004E20000000000600271031CAD03F000000000600271031CAD03F0000000000000000000034780056F547A8C5C9BD9CB81358802B"

        //when
        val expectCardData = tapcashBalanceViewModel.getCardData(secureResult, terminalRandomNumber,challangeResult)

        //then
        assertEquals(expectCardData, cardData)
    }

    @Test
    fun getCardData_SuccessGetCardData_Failed() {
        //given
        val cardData = ""

        //when
        val expectCardData = tapcashBalanceViewModel.getCardData("", "","")

        //then
        assertEquals(expectCardData, cardData)
    }

    @Test
    fun getCardData_SuccessGetCardData_FailedSizeNotMax() {
        //given
        val cardData = ""

        //when
        val expectCardData = tapcashBalanceViewModel.getCardData("A", terminalRandomNumber,challangeResult)

        //then
        assertEquals(expectCardData, cardData)
    }

    @Test
    fun getCardData_SuccessGetCardData_FailedSecureSizeNotMax() {
        //given
        val cardData = ""

        //when
        val expectCardData = tapcashBalanceViewModel.getCardData(secureResult, "B",challangeResult)

        //then
        assertEquals(expectCardData, cardData)
    }

    @Test
    fun getCardData_SuccessGetCardData_FailedChallangeSizeNotMax() {
        //given
        val cardData = ""

        //when
        val expectCardData = tapcashBalanceViewModel.getCardData(secureResult, terminalRandomNumber,"C")

        //then
        assertEquals(expectCardData, cardData)
    }

    @Test
    fun getCardData_SuccessGetCardData_FailedSecureEmpty() {
        //given
        val cardData = ""

        //when
        val expectCardData = tapcashBalanceViewModel.getCardData(secureResult, "",challangeResult)

        //then
        assertEquals(expectCardData, cardData)
    }

    @Test
    fun getCardData_SuccessGetCardData_FailedChallangeEmpty() {
        //given
        val cardData = ""

        //when
        val expectCardData = tapcashBalanceViewModel.getCardData(secureResult, terminalRandomNumber,"")

        //then
        assertEquals(expectCardData, cardData)
    }

    @Test
    fun processTagIntent_WriteBalanceTapcash_SuccessNoCrypto() {
        //given
        initSuccessData()
        val secureByteRequest = NFCUtils.stringToByteArrayRadix(secureRequest)
        val secureByteResult =
                NFCUtils.stringToByteArrayRadix(secureResult)
        every { isoDep.transceive(COMMAND_GET_CHALLENGE) } returns challangeResultSuccess
        every { tapcashBalanceViewModel.getRandomString() } returns terminalRandomNumber
        every { isoDep.transceive(secureByteRequest) } returns secureByteResult

        val balanceTapcash = Gson().fromJson(dummyResponseNoCrypto, BalanceTapcash::class.java)
        val result = HashMap<Type, Any>()
        result[BalanceTapcash::class.java] = balanceTapcash
        val gqlResponseWriteBalanceSuccess = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseWriteBalanceSuccess
        //when
        tapcashBalanceViewModel.processTapCashTagIntent(isoDep, "")

        //then
        checkAssertEmoneyInquiry((tapcashBalanceViewModel.tapcashInquiry.value as EmoneyInquiry), mapTapcashtoEmoney(balanceTapcash))
    }

    @Test
    fun processTagIntent_WriteBalanceTapcash_SuccessWithCryptoSuccessWrite() {
        //given
        initSuccessData()
        val secureByteRequest = NFCUtils.stringToByteArrayRadix(secureRequest)
        val secureByteResult =
                NFCUtils.stringToByteArrayRadix(secureResult)

        val writeByteRequest = NFCUtils.stringToByteArrayRadix(writeRequest)
        val writeByteResult = NFCUtils.stringToByteArrayRadix(writeResult)
        every { isoDep.transceive(COMMAND_GET_CHALLENGE) } returns challangeResultSuccess
        every { tapcashBalanceViewModel.getRandomString() } returns terminalRandomNumber
        every { isoDep.transceive(secureByteRequest) } returns secureByteResult
        every { isoDep.transceive(writeByteRequest) } returns writeByteResult

        val balanceTapcash = Gson().fromJson(dummyResponseWithCrypto, BalanceTapcash::class.java)
        val result = HashMap<Type, Any>()
        result[BalanceTapcash::class.java] = balanceTapcash
        val gqlResponseWriteBalanceSuccess = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseWriteBalanceSuccess
        //when
        tapcashBalanceViewModel.processTapCashTagIntent(isoDep, "")

        //then
        checkAssertEmoneyInquiry((tapcashBalanceViewModel.tapcashInquiry.value as EmoneyInquiry), mapTapcashtoEmoney(balanceTapcash))
    }

    @Test
    fun processTagIntent_WriteBalanceTapcash_SuccessWithCryptoFailed() {
        //given
        initSuccessData()
        val secureByteRequest = NFCUtils.stringToByteArrayRadix(secureRequest)
        val secureByteResult =
                NFCUtils.stringToByteArrayRadix(secureResult)

        val writeByteRequest = NFCUtils.stringToByteArrayRadix(writeRequest)
        val writeByteResult = NFCUtils.stringToByteArrayRadix(challangeFail)
        every { isoDep.transceive(COMMAND_GET_CHALLENGE) } returns challangeResultSuccess
        every { tapcashBalanceViewModel.getRandomString() } returns terminalRandomNumber
        every { isoDep.transceive(secureByteRequest) } returns secureByteResult
        every { isoDep.transceive(writeByteRequest) } returns writeByteResult

        val balanceTapcash = Gson().fromJson(dummyResponseWithCrypto, BalanceTapcash::class.java)
        val result = HashMap<Type, Any>()
        result[BalanceTapcash::class.java] = balanceTapcash
        val gqlResponseWriteBalanceSuccess = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseWriteBalanceSuccess
        //when
        tapcashBalanceViewModel.processTapCashTagIntent(isoDep, "")

        //then
        assertEquals(((tapcashBalanceViewModel.errorCardMessage.value) as Throwable).message, "Maaf, cek saldo belum berhasil")
    }

    @Test
    fun processTagIntent_WriteBalanceTapcash_SuccessWithCryptoFailedSizeNoMatch() {
        //given
        initSuccessData()
        val secureByteRequest = NFCUtils.stringToByteArrayRadix(secureRequest)
        val secureByteResult =
                NFCUtils.stringToByteArrayRadix(secureResult)

        val writeByteRequest = NFCUtils.stringToByteArrayRadix(writeRequest)
        val writeByteResult = NFCUtils.stringToByteArrayRadix(writeResultFailedSize)
        every { isoDep.transceive(COMMAND_GET_CHALLENGE) } returns challangeResultSuccess
        every { tapcashBalanceViewModel.getRandomString() } returns terminalRandomNumber
        every { isoDep.transceive(secureByteRequest) } returns secureByteResult
        every { isoDep.transceive(writeByteRequest) } returns writeByteResult

        val balanceTapcash = Gson().fromJson(dummyResponseWithCrypto, BalanceTapcash::class.java)
        val result = HashMap<Type, Any>()
        result[BalanceTapcash::class.java] = balanceTapcash
        val gqlResponseWriteBalanceSuccess = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseWriteBalanceSuccess
        //when
        tapcashBalanceViewModel.processTapCashTagIntent(isoDep, "")

        //then
        assertEquals(((tapcashBalanceViewModel.errorCardMessage.value) as Throwable).message, "Maaf, cek saldo belum berhasil")
    }


    private fun checkAssertEmoneyInquiry(expected: EmoneyInquiry, actual: EmoneyInquiry){
        assertEquals(expected.attributesEmoneyInquiry?.buttonText, actual.attributesEmoneyInquiry?.buttonText)
        assertEquals(expected.attributesEmoneyInquiry?.cardNumber, actual.attributesEmoneyInquiry?.cardNumber)
        assertEquals(expected.attributesEmoneyInquiry?.imageIssuer, actual.attributesEmoneyInquiry?.imageIssuer)
        assertEquals(expected.attributesEmoneyInquiry?.lastBalance, actual.attributesEmoneyInquiry?.lastBalance)
        assertEquals(expected.attributesEmoneyInquiry?.payload, actual.attributesEmoneyInquiry?.payload)
        assertEquals(expected.attributesEmoneyInquiry?.status, actual.attributesEmoneyInquiry?.status)
        assertEquals(expected.attributesEmoneyInquiry?.formattedCardNumber, actual.attributesEmoneyInquiry?.formattedCardNumber)
        assertEquals(expected.error?.id, actual.error?.id)
        assertEquals(expected.error?.title, actual.error?.title)
        assertEquals(expected.error?.status, actual.error?.status)
    }

}