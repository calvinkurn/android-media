package com.tokopedia.emoney.viewmodel

import android.nfc.tech.IsoDep
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.emoney.data.AttributesEmoneyInquiry
import com.tokopedia.emoney.data.EmoneyInquiry
import com.tokopedia.emoney.data.EmoneyInquiryResponse
import com.tokopedia.emoney.data.NfcCardErrorTypeDef
import com.tokopedia.emoney.util.NFCUtils
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

        val gqlResponseGetInquirySuccess = GraphqlResponse(
                mapOf(EmoneyInquiryResponse::class.java to EmoneyInquiryResponse(
                        EmoneyInquiry(id = "1", attributesEmoneyInquiry = AttributesEmoneyInquiry(status = 0, payload = "")))),
                mapOf(), false)

        val gqlResponseWriteBalanceSuccess = GraphqlResponse(
                mapOf(EmoneyInquiryResponse::class.java to EmoneyInquiryResponse(emoneyInquiry)),
                mapOf(), false)
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

        val gqlResponseGetInquirySuccess = GraphqlResponse(
                mapOf(EmoneyInquiryResponse::class.java to EmoneyInquiryResponse(
                        EmoneyInquiry(id = "1", attributesEmoneyInquiry = AttributesEmoneyInquiry(status = 0, payload = "")))),
                mapOf(), false)

        val errorGql = GraphqlError()
        errorGql.message = "Error get balance"
        val gqlResponseError = GraphqlResponse(
                mapOf(), mapOf(EmoneyInquiryResponse::class.java to listOf(errorGql)), false)
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

        val gqlResponseWriteBalanceSuccess = GraphqlResponse(
                mapOf(EmoneyInquiryResponse::class.java to EmoneyInquiryResponse(emoneyInquiry)),
                mapOf(), false)
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
        val gqlResponseError = GraphqlResponse(
                mapOf(), mapOf(EmoneyInquiryResponse::class.java to listOf(errorGql)), false)
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

        val gqlResponseGetInquirySuccess = GraphqlResponse(
                mapOf(EmoneyInquiryResponse::class.java to EmoneyInquiryResponse(
                        EmoneyInquiry(id = "1", attributesEmoneyInquiry = AttributesEmoneyInquiry(status = 0, payload = "")))),
                mapOf(), false)
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
    fun processTagIntent_CardIsNotEmoney_ContinueProcessTagOnBrizzi() {
        //given
        initSuccessData()
        every { NFCUtils.toHex(byteNfc) } returns "2000"

        //when
        emoneyBalanceViewModel.processEmoneyTagIntent(isoDep, "", 0)

        //then
        assertNotNull(emoneyBalanceViewModel.cardIsNotEmoney.value)
        assertEquals(emoneyBalanceViewModel.cardIsNotEmoney.value, true)
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
}