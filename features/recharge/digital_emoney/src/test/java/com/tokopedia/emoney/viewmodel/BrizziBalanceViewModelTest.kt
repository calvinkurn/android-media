package com.tokopedia.emoney.viewmodel

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.emoney.util.NFCUtils
import com.tokopedia.emoney.data.*
import com.tokopedia.emoney.view.mapper.BrizziCardObjectMapper
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import id.co.bri.sdk.Brizzi
import id.co.bri.sdk.BrizziCardObject
import id.co.bri.sdk.Callback
import id.co.bri.sdk.exception.BrizziException
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BrizziBalanceViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    private lateinit var brizziBalanceViewModel: BrizziBalanceViewModel
    private lateinit var brizzi: Brizzi
    private lateinit var intent: Intent
    private lateinit var brizziCardObjectMapper: BrizziCardObjectMapper

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(NFCUtils)

        intent = spyk()
        brizzi = mockk()
        brizziCardObjectMapper = mockk()
        brizziBalanceViewModel = BrizziBalanceViewModel(graphqlRepository, brizziCardObjectMapper, Dispatchers.Unconfined)
    }

    @Test
    fun processTagIntent_GetBalance_TokenExpired() {
        //given
        every { brizzi.getBalanceInquiry(any(), any()) } answers { secondArg<Callback>().OnFailure(BrizziException(BrizziBalanceViewModel.BRIZZI_TOKEN_EXPIRED, "")) }

        //when
        brizziBalanceViewModel.processBrizziTagIntent(intent, "", brizzi)

        //then
        assertNotNull(brizziBalanceViewModel.tokenNeedRefresh.value)
        assertEquals(true, brizziBalanceViewModel.tokenNeedRefresh.value)
    }

    @Test
    fun processTagIntent_GetBalance_CardNotFound() {
        //given
        every { brizzi.getBalanceInquiry(any(), any()) } answers { secondArg<Callback>().OnFailure(BrizziException(BrizziBalanceViewModel.BRIZZI_CARD_NOT_FOUND, "")) }

        //when
        brizziBalanceViewModel.processBrizziTagIntent(intent, "", brizzi)

        //then
        assertNotNull(brizziBalanceViewModel.cardIsBrizzi.value)
        assertEquals(false, brizziBalanceViewModel.cardIsBrizzi.value)
    }

    @Test
    fun processTagIntent_GetBalance_CardReadFailed() {
        //given
        every { brizzi.getBalanceInquiry(any(), any()) } answers { secondArg<Callback>().OnFailure(BrizziException("100", "")) }

        //when
        brizziBalanceViewModel.processBrizziTagIntent(intent, "", brizzi)

        //then
        assertNotNull(brizziBalanceViewModel.errorCardMessage.value)
        assertEquals(NfcCardErrorTypeDef.FAILED_READ_CARD, brizziBalanceViewModel.errorCardMessage.value)
    }

    @Test
    fun processTagIntent_GetBalanceNoPendingBalanceLogSuccess_SuccessGetBalance() {
        //given
        val balanceInquiry = EmoneyInquiry(attributesEmoneyInquiry = AttributesEmoneyInquiry(lastBalance = 10000, pendingBalance = 0))
        val inquiryId = 10
        every { brizzi.getBalanceInquiry(any(), any()) } answers { secondArg<Callback>().OnSuccess(BrizziCardObject()) }
        every { brizziCardObjectMapper.mapperBrizzi(any(), any()) } returns balanceInquiry

        val gqlResponseLog1Success = GraphqlResponse(
                mapOf(BrizziInquiryLogResponse::class.java to BrizziInquiryLogResponse(brizziInquiryLog = BrizziInquiryLog(inquiryId = inquiryId))),
                mapOf(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseLog1Success

        //when
        brizziBalanceViewModel.processBrizziTagIntent(intent, "", brizzi)

        //then
        assertNotNull(brizziBalanceViewModel.issuerId.value)
        assertEquals(brizziBalanceViewModel.issuerId.value, 2)

        assertNotNull(brizziBalanceViewModel.inquiryIdBrizzi)
        assertEquals(inquiryId, brizziBalanceViewModel.inquiryIdBrizzi)

        assertNotNull(brizziBalanceViewModel.emoneyInquiry.value)
        assertEquals(balanceInquiry, brizziBalanceViewModel.emoneyInquiry.value)
    }

    @Test
    fun processTagIntent_GetBalanceNoPendingBalanceLogFailed_SuccessGetBalance() {
        //given
        val balanceInquiry = EmoneyInquiry(attributesEmoneyInquiry = AttributesEmoneyInquiry(lastBalance = 10000, pendingBalance = 0))
        val inquiryId = -1
        every { brizzi.getBalanceInquiry(any(), any()) } answers { secondArg<Callback>().OnSuccess(BrizziCardObject()) }
        every { brizziCardObjectMapper.mapperBrizzi(any(), any()) } returns balanceInquiry

        val errorGql = GraphqlError()
        errorGql.message = "Error log balance"
        val gqlResponseErrorLog1 = GraphqlResponse(
                mapOf(), mapOf(BrizziInquiryLogResponse::class.java to listOf(errorGql)), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseErrorLog1

        //when
        brizziBalanceViewModel.processBrizziTagIntent(intent, "", brizzi)

        //then
        assertNotNull(brizziBalanceViewModel.issuerId.value)
        assertEquals(brizziBalanceViewModel.issuerId.value, 2)

        assertNotNull(brizziBalanceViewModel.inquiryIdBrizzi)
        assertEquals(inquiryId, brizziBalanceViewModel.inquiryIdBrizzi)

        assertNotNull(brizziBalanceViewModel.emoneyInquiry.value)
        assertEquals(balanceInquiry, brizziBalanceViewModel.emoneyInquiry.value)
    }

    @Test
    fun processTagIntent_WriteBalanceWithPendingLogSuccess_TokenExpired() {
        //given
        val balanceInquiry = EmoneyInquiry(attributesEmoneyInquiry = AttributesEmoneyInquiry(lastBalance = 10000, pendingBalance = 10000))
        val inquiryId = 10

        every { brizzi.getBalanceInquiry(any(), any()) } answers { secondArg<Callback>().OnSuccess(BrizziCardObject()) }
        every { brizziCardObjectMapper.mapperBrizzi(any(), any()) } returns balanceInquiry

        val gqlResponseLog1Success = GraphqlResponse(
                mapOf(BrizziInquiryLogResponse::class.java to BrizziInquiryLogResponse(brizziInquiryLog = BrizziInquiryLog(inquiryId = inquiryId))),
                mapOf(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseLog1Success

        every { brizzi.doUpdateBalance(intent, any(), any()) } answers { thirdArg<Callback>().OnFailure(BrizziException(BrizziBalanceViewModel.BRIZZI_TOKEN_EXPIRED, "")) }

        //when
        brizziBalanceViewModel.processBrizziTagIntent(intent, "", brizzi)

        //then
        assertNotNull(brizziBalanceViewModel.issuerId.value)
        assertEquals(brizziBalanceViewModel.issuerId.value, 2)

        assertNotNull(brizziBalanceViewModel.inquiryIdBrizzi)
        assertEquals(inquiryId, brizziBalanceViewModel.inquiryIdBrizzi)

        assertNotNull(brizziBalanceViewModel.tokenNeedRefresh.value)
        assertEquals(true, brizziBalanceViewModel.tokenNeedRefresh.value)
    }


    @Test
    fun processTagIntent_WriteBalanceWithPendingLogSuccess_SuccessWriteBalance() {
        //given
        val balanceInquiryBeforeUpdate = EmoneyInquiry(attributesEmoneyInquiry = AttributesEmoneyInquiry(lastBalance = 10000, pendingBalance = 10000))
        val balanceInquiryAfterUpdate = EmoneyInquiry(attributesEmoneyInquiry = AttributesEmoneyInquiry(lastBalance = 20000, pendingBalance = 0))

        every { brizzi.getBalanceInquiry(any(), any()) } answers { secondArg<Callback>().OnSuccess(BrizziCardObject()) }
        every { brizzi.doUpdateBalance(intent, any(), any()) } answers { thirdArg<Callback>().OnSuccess(BrizziCardObject()) }
        every { brizziCardObjectMapper.mapperBrizzi(any(), any()) } returnsMany listOf(balanceInquiryBeforeUpdate, balanceInquiryAfterUpdate)

        val gqlResponseLog1Success = GraphqlResponse(
                mapOf(BrizziInquiryLogResponse::class.java to BrizziInquiryLogResponse(brizziInquiryLog = BrizziInquiryLog(inquiryId = 10))),
                mapOf(), false)
        val gqlResponseLog2Success = GraphqlResponse(
                mapOf(BrizziInquiryLogResponse::class.java to BrizziInquiryLogResponse(brizziInquiryLog = BrizziInquiryLog(inquiryId = 13))),
                mapOf(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returnsMany listOf(gqlResponseLog1Success, gqlResponseLog2Success)

        //when
        brizziBalanceViewModel.processBrizziTagIntent(intent, "", brizzi)

        //then
        assertNotNull(brizziBalanceViewModel.issuerId.value)
        assertEquals(brizziBalanceViewModel.issuerId.value, 2)

        assertNotNull(brizziBalanceViewModel.inquiryIdBrizzi)
        assertEquals(13, brizziBalanceViewModel.inquiryIdBrizzi)

        assertNotNull(brizziBalanceViewModel.emoneyInquiry.value)
        assertEquals(balanceInquiryAfterUpdate, brizziBalanceViewModel.emoneyInquiry.value)
    }

    @Test
    fun getTokenBrizzi_RefreshToken_Success() {
        //given
        val gqlResponseRefreshTokenSuccess = GraphqlResponse(
                mapOf(BrizziTokenResponse::class.java to BrizziTokenResponse(BrizziToken(token = "abcd"))),
                mapOf(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseRefreshTokenSuccess

        //when
//        brizziBalanceViewModel.getTokenBrizzi("", true)

        //then
//        assertNotNull(brizziBalanceViewModel.tokenBrizzi.value)
//        assertEquals("abcd", brizziBalanceViewModel.tokenBrizzi.value)
    }

    @Test
    fun getTokenBrizzi_RefreshToken_Failed() {
        //given
        val errorGql = GraphqlError()
        errorGql.message = "Error refresh token"
        val gqlResponseRefreshTokenFailed = GraphqlResponse(
                mapOf(), mapOf(BrizziTokenResponse::class.java to listOf(errorGql)), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseRefreshTokenFailed

        //when
//        brizziBalanceViewModel.getTokenBrizzi("", true)

        //then
//        assertNotNull(brizziBalanceViewModel.tokenBrizzi.value)
//        assertEquals("", brizziBalanceViewModel.tokenBrizzi.value)
    }
}