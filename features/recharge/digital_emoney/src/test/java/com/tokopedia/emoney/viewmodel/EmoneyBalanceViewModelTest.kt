package com.tokopedia.emoney.viewmodel

import android.nfc.tech.IsoDep
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common_electronic_money.data.AttributesEmoneyInquiry
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.common_electronic_money.data.EmoneyInquiryResponse
import com.tokopedia.common_electronic_money.util.NFCUtils
import com.tokopedia.common_electronic_money.util.NfcCardErrorTypeDef
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import java.lang.reflect.Type

class EmoneyBalanceViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    @MockK
    lateinit var isoDep: IsoDep

    private lateinit var emoneyBalanceViewModel: EmoneyBalanceViewModel
    private lateinit var byteNfc: ByteArray

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(NFCUtils.Companion)

        byteNfc = byteArrayOf()
        emoneyBalanceViewModel = EmoneyBalanceViewModel(graphqlRepository, Dispatchers.Unconfined)
    }

    private fun initSuccessData() {
        every { NFCUtils.toHex(byteNfc) } returns "9000"
        every { NFCUtils.formatCardUID(any()) } returns "6013 - 5020 - 0067 - 5709"
        every { NFCUtils.hexStringToByteArray(any()) } returns byteNfc

        every { isoDep.close() } returns mockk()
        every { isoDep.connect() } returns mockk()
        every { isoDep.setTimeout(any()) } returns mockk()
        every { isoDep.transceive(any()) } returns byteNfc
        every { isoDep.isConnected } returns true
        every { isoDep.tag.id } returns byteNfc
    }

    @Test
    fun processTagIntent_WriteSaldoEmoney_SuccessGetSaldoAndUpdateSaldo() {
        //given
        initSuccessData()
        val emoneyInquiry = EmoneyInquiry(attributesEmoneyInquiry = AttributesEmoneyInquiry(lastBalance = 1000, status = 1))

        val result = HashMap<Type, Any>()
        result[EmoneyInquiryResponse::class.java] = EmoneyInquiryResponse(
                EmoneyInquiry(id = "1", attributesEmoneyInquiry = AttributesEmoneyInquiry(status = 0, payload = "")))
        val gqlResponseGetInquirySuccess = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        val result1 = HashMap<Type, Any>()
        result1[EmoneyInquiryResponse::class.java] = EmoneyInquiryResponse(emoneyInquiry)
        val gqlResponseWriteBalanceSuccess = GraphqlResponse(result1, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returnsMany listOf(gqlResponseGetInquirySuccess, gqlResponseWriteBalanceSuccess)

        //when
        emoneyBalanceViewModel.processEmoneyTagIntent(isoDep, "", 0)

        //then
        assertNotNull(emoneyBalanceViewModel.issuerId.value)
        assertEquals(emoneyBalanceViewModel.issuerId.value, 1)

        assertNotNull(emoneyBalanceViewModel.emoneyInquiry.value)
        assertEquals(emoneyBalanceViewModel.emoneyInquiry.value, emoneyInquiry)
    }

    @Test
    fun processTagIntent_WriteSaldoEmoneyError_SuccessGetSaldoAndFailedUpdateSaldo() {
        //given
        initSuccessData()
        val emoneyInquiry = EmoneyInquiry(attributesEmoneyInquiry = AttributesEmoneyInquiry(lastBalance = 1000, status = 1))

        val result = HashMap<Type, Any>()
        result[EmoneyInquiryResponse::class.java] = EmoneyInquiryResponse(
                EmoneyInquiry(id = "1", attributesEmoneyInquiry = AttributesEmoneyInquiry(status = 0, payload = "")))
        val gqlResponseGetInquirySuccess = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        val errorGql = GraphqlError()
        errorGql.message = "Error get balance"
        val errors = HashMap<Type, List<GraphqlError>>()
        errors[EmoneyInquiryResponse::class.java] = listOf(errorGql)
        val gqlResponseError = GraphqlResponse(HashMap<Type, Any?>(), errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returnsMany listOf(gqlResponseGetInquirySuccess, gqlResponseError)

        //when
        emoneyBalanceViewModel.processEmoneyTagIntent(isoDep, "", 0)

        //then
        assertNotNull(emoneyBalanceViewModel.issuerId.value)
        assertEquals(emoneyBalanceViewModel.issuerId.value, 1)

        assertNotNull(emoneyBalanceViewModel.errorInquiryBalance.value)
        assertEquals(errorGql.message, emoneyBalanceViewModel.errorInquiryBalance.value?.message)
    }

    @Test
    fun processTagIntent_GetSaldoEmoney_SuccessGetSaldo() {
        //given
        initSuccessData()

        val emoneyInquiry = EmoneyInquiry(attributesEmoneyInquiry = AttributesEmoneyInquiry(lastBalance = 1000, status = 1))

        val result = HashMap<Type, Any>()
        result[EmoneyInquiryResponse::class.java] = EmoneyInquiryResponse(emoneyInquiry)
        val gqlResponseWriteBalanceSuccess = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseWriteBalanceSuccess

        //when
        emoneyBalanceViewModel.processEmoneyTagIntent(isoDep, "", 0)

        //then
        assertNotNull(emoneyBalanceViewModel.issuerId.value)
        assertEquals(emoneyBalanceViewModel.issuerId.value, 1)

        assertNotNull(emoneyBalanceViewModel.emoneyInquiry.value)
        assertEquals(emoneyBalanceViewModel.emoneyInquiry.value, emoneyInquiry)
    }

    @Test
    fun processTagIntent_GetSaldoEmoney_FailedGetSaldo() {
        //given
        initSuccessData()

        val errorGql = GraphqlError()
        errorGql.message = "Error get balance"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[EmoneyInquiryResponse::class.java] = listOf(errorGql)
        val gqlResponseError = GraphqlResponse(HashMap<Type, Any?>(), errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseError

        //when
        emoneyBalanceViewModel.processEmoneyTagIntent(isoDep, "", 0)

        //then
        assertNotNull(emoneyBalanceViewModel.issuerId.value)
        assertEquals(emoneyBalanceViewModel.issuerId.value, 1)

        assertNotNull(emoneyBalanceViewModel.errorInquiryBalance.value)
        assertEquals(errorGql.message, emoneyBalanceViewModel.errorInquiryBalance.value?.message)
    }

    @Test
    fun processTagIntent_IsodepDisconnectedWriteCard_FailedUpdateSaldo() {
        //given
        initSuccessData()
        every { isoDep.isConnected } returns false

        val result = HashMap<Type, Any>()
        result[EmoneyInquiryResponse::class.java] = EmoneyInquiryResponse(
                EmoneyInquiry(id = "1", attributesEmoneyInquiry = AttributesEmoneyInquiry(status = 0, payload = "")))
        val gqlResponseGetInquirySuccess = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseGetInquirySuccess

        //when
        emoneyBalanceViewModel.processEmoneyTagIntent(isoDep, "", 0)

        //then
        assertNotNull(emoneyBalanceViewModel.issuerId.value)
        assertEquals(emoneyBalanceViewModel.issuerId.value, 1)

        assertNotNull(emoneyBalanceViewModel.errorCardMessage.value)
        assertEquals(NfcCardErrorTypeDef.FAILED_UPDATE_BALANCE, emoneyBalanceViewModel.errorCardMessage.value)
    }

    @Test
    fun processTagIntent_ErrorOnTryCatch_ShowingErrorFailedReadCard() {
        //given
        initSuccessData()
        every { isoDep.transceive(any()) } answers { throw IOException() }

        //when
        emoneyBalanceViewModel.processEmoneyTagIntent(isoDep, "", 0)

        //then
        assertNotNull(emoneyBalanceViewModel.errorCardMessage.value)
        assertEquals(NfcCardErrorTypeDef.FAILED_READ_CARD, emoneyBalanceViewModel.errorCardMessage.value)
    }

    @Test
    fun processTagIntent_WriteSaldoEmoneyErrorIO_NullResponseInByte() {
        //given
        emoneyBalanceViewModel.isoDep = isoDep
        initSuccessData()

        var mapParam = HashMap<String, Any>()
        mapParam.put(EmoneyBalanceViewModel.TYPE_CARD, "")
        mapParam.put(EmoneyBalanceViewModel.ID_CARD, "")
        mapParam.put(EmoneyBalanceViewModel.ATTRIBUTES_CARD, "")

        val emoneyInquiry = EmoneyInquiry(attributesEmoneyInquiry = AttributesEmoneyInquiry(lastBalance = 1000, status = 1, payload = "1"))

        val result = HashMap<Type, Any>()
        result[EmoneyInquiryResponse::class.java] = EmoneyInquiryResponse(
                EmoneyInquiry(id = "1", attributesEmoneyInquiry = AttributesEmoneyInquiry(status = 0, payload = "")))
        val gqlResponseGetInquirySuccess = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        val result1 = HashMap<Type, Any>()
        result1[EmoneyInquiryResponse::class.java] = EmoneyInquiryResponse(emoneyInquiry)
        val gqlResponseWriteBalanceSuccess = GraphqlResponse(result1, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returnsMany listOf(gqlResponseGetInquirySuccess, gqlResponseWriteBalanceSuccess)
        every { isoDep.transceive(NFCUtils.hexStringToByteArray("1")) } answers { null }
        //when
        emoneyBalanceViewModel.writeBalanceToCard("", "", 0, mapParam)

        //then
        assertNotNull(emoneyBalanceViewModel.errorCardMessage.value)
        assertEquals(NfcCardErrorTypeDef.FAILED_READ_CARD, emoneyBalanceViewModel.errorCardMessage.value)
    }

    @Test
    fun processTagIntent_WriteSaldoEmoneyErrorIO_ISOError() {
        //given
        emoneyBalanceViewModel.isoDep = isoDep
        initSuccessData()

        var mapParam = HashMap<String, Any>()
        mapParam.put(EmoneyBalanceViewModel.TYPE_CARD, "")
        mapParam.put(EmoneyBalanceViewModel.ID_CARD, "")
        mapParam.put(EmoneyBalanceViewModel.ATTRIBUTES_CARD, "")

        val emoneyInquiry = EmoneyInquiry(attributesEmoneyInquiry = AttributesEmoneyInquiry(lastBalance = 1000, status = 1, payload = "1"))

        val result = HashMap<Type, Any>()
        result[EmoneyInquiryResponse::class.java] = EmoneyInquiryResponse(
                EmoneyInquiry(id = "1", attributesEmoneyInquiry = AttributesEmoneyInquiry(status = 0, payload = "")))
        val gqlResponseGetInquirySuccess = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        val result1 = HashMap<Type, Any>()
        result1[EmoneyInquiryResponse::class.java] = EmoneyInquiryResponse(emoneyInquiry)
        val gqlResponseWriteBalanceSuccess = GraphqlResponse(result1, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returnsMany listOf(gqlResponseGetInquirySuccess, gqlResponseWriteBalanceSuccess)
        every { isoDep.transceive(NFCUtils.hexStringToByteArray("1")) } answers { null }
        //when
        emoneyBalanceViewModel.writeBalanceToCard("", "", 0, mapParam)

        //then
        assertNotNull(emoneyBalanceViewModel.errorCardMessage.value)
        assertEquals(NfcCardErrorTypeDef.FAILED_READ_CARD, emoneyBalanceViewModel.errorCardMessage.value)
    }

    @Test
    fun processTagIntent_WriteSaldoEmoneyErrorIO_ISONotConnected() {
        //given
        emoneyBalanceViewModel.isoDep = isoDep
        initSuccessData()

        var mapParam = HashMap<String, Any>()
        mapParam.put(EmoneyBalanceViewModel.TYPE_CARD, "")
        mapParam.put(EmoneyBalanceViewModel.ID_CARD, "")
        mapParam.put(EmoneyBalanceViewModel.ATTRIBUTES_CARD, "")

        val emoneyInquiry = EmoneyInquiry(attributesEmoneyInquiry = AttributesEmoneyInquiry(lastBalance = 1000, status = 1, payload = "1"))

        val result = HashMap<Type, Any>()
        result[EmoneyInquiryResponse::class.java] = EmoneyInquiryResponse(
                EmoneyInquiry(id = "1", attributesEmoneyInquiry = AttributesEmoneyInquiry(status = 0, payload = "")))
        val gqlResponseGetInquirySuccess = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        val result1 = HashMap<Type, Any>()
        result1[EmoneyInquiryResponse::class.java] = EmoneyInquiryResponse(emoneyInquiry)
        val gqlResponseWriteBalanceSuccess = GraphqlResponse(result1, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returnsMany listOf(gqlResponseGetInquirySuccess, gqlResponseWriteBalanceSuccess)
        every { isoDep.transceive(NFCUtils.hexStringToByteArray("1")) } answers { null }
        every { emoneyBalanceViewModel.isoDep.isConnected } answers { false }
        //when
        emoneyBalanceViewModel.writeBalanceToCard("", "", 0, mapParam)

        //then
        assertNotNull(emoneyBalanceViewModel.errorCardMessage.value)
        assertEquals(NfcCardErrorTypeDef.FAILED_READ_CARD, emoneyBalanceViewModel.errorCardMessage.value)
    }
}