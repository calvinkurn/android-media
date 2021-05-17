package com.tokopedia.brizzi.viewmodel

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common_electronic_money.data.AttributesEmoneyInquiry
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.common_electronic_money.util.NFCUtils
import com.tokopedia.common_electronic_money.util.NfcCardErrorTypeDef
import com.tokopedia.brizzi.data.BrizziInquiryLog
import com.tokopedia.brizzi.data.BrizziInquiryLogResponse
import com.tokopedia.brizzi.data.BrizziToken
import com.tokopedia.brizzi.data.BrizziTokenResponse
import com.tokopedia.brizzi.mapper.BrizziCardObjectMapper
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
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.internal.runners.statements.Fail
import java.lang.reflect.Type

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
        val result = HashMap<Type, Any>()
        result[BrizziTokenResponse::class.java] = BrizziTokenResponse(BrizziToken(token = "abcd"))
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse
        every { brizzi.Init(any(),any()) } returns mockk()
        every { brizzi.setUserName(any()) } returns mockk()
        every { brizzi.getBalanceInquiry(any(), any()) } answers { secondArg<Callback>().OnFailure(BrizziException(BrizziBalanceViewModel.BRIZZI_TOKEN_EXPIRED, "")) }

        //when
        brizziBalanceViewModel.processBrizziTagIntent(intent, brizzi, "", "",false)

        //then
        assertNotNull(brizziBalanceViewModel.tokenNeedRefresh.value)
        assertEquals(true, brizziBalanceViewModel.tokenNeedRefresh.value)
    }

    @Test
    fun processTagIntent_GetBalance_CardNotFound() {
        //given
        val result = HashMap<Type, Any>()
        result[BrizziTokenResponse::class.java] = BrizziTokenResponse(BrizziToken(token = "abcd"))
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse
        every { brizzi.Init(any(),any()) } returns mockk()
        every { brizzi.setUserName(any()) } returns mockk()
        every { brizzi.getBalanceInquiry(any(), any()) } answers { secondArg<Callback>().OnFailure(BrizziException(BrizziBalanceViewModel.BRIZZI_CARD_NOT_FOUND, "")) }

        //when
        brizziBalanceViewModel.processBrizziTagIntent(intent, brizzi, "", "",true)

        //then
        assertNotNull(brizziBalanceViewModel.cardIsNotBrizzi.value)
        assertEquals(true, brizziBalanceViewModel.cardIsNotBrizzi.value)
    }

    @Test
    fun processTagIntent_GetBalance_CardReadFailed() {
        //given
        val result = HashMap<Type, Any>()
        result[BrizziTokenResponse::class.java] = BrizziTokenResponse(BrizziToken(token = "abcd"))
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse
        every { brizzi.Init(any(),any()) } returns mockk()
        every { brizzi.setUserName(any()) } returns mockk()
        every { brizzi.getBalanceInquiry(any(), any()) } answers { secondArg<Callback>().OnFailure(BrizziException("100", "")) }

        //when
        brizziBalanceViewModel.processBrizziTagIntent(intent, brizzi, "", "",true)

        //then
        assertNotNull(brizziBalanceViewModel.errorCardMessage.value)
        assertEquals(NfcCardErrorTypeDef.FAILED_READ_CARD, brizziBalanceViewModel.errorCardMessage.value)
    }

    @Test
    fun processTagIntent_GetBalance_CardReadFailedMessageNull() {
        //given
        val result = HashMap<Type, Any>()
        result[BrizziTokenResponse::class.java] = BrizziTokenResponse(BrizziToken(token = "abcd"))
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse
        every { brizzi.Init(any(),any()) } returns mockk()
        every { brizzi.setUserName(any()) } returns mockk()
        every { brizzi.getBalanceInquiry(any(), any()) } answers { secondArg<Callback>().OnFailure(BrizziException()) }

        //when
        brizziBalanceViewModel.processBrizziTagIntent(intent, brizzi, "", "",true)

        //then
        assertNotNull(brizziBalanceViewModel.errorCardMessage.value)
        assertEquals(NfcCardErrorTypeDef.FAILED_READ_CARD, brizziBalanceViewModel.errorCardMessage.value)
    }

    @Test
    fun processTagIntent_RefreshToken_Failed() {
        //given
        val errorGql = GraphqlError()
        errorGql.message = "Error refresh token"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[BrizziTokenResponse::class.java] = listOf(errorGql)
        val gqlResponse = GraphqlResponse(HashMap<Type, Any?>(), errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        brizziBalanceViewModel.processBrizziTagIntent(intent, brizzi, "", "",true)

        //then
        assertNotNull(brizziBalanceViewModel.errorCommonBrizzi.value)
        assertEquals(errorGql.message, (brizziBalanceViewModel.errorCommonBrizzi.value as Throwable).message)
    }

    @Test
    fun processTagIntent_GetBalanceNoPendingBalanceLogSuccess_SuccessGetBalance() {
        //given
        val balanceInquiry = EmoneyInquiry(attributesEmoneyInquiry = AttributesEmoneyInquiry(lastBalance = 10000, pendingBalance = 0))
        val inquiryId = 10

        val result = HashMap<Type, Any>()
        result[BrizziTokenResponse::class.java] = BrizziTokenResponse(BrizziToken(token = "abcd"))
        val gqlResponseRefreshTokenSuccess = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        val result1 = HashMap<Type, Any>()
        result1[BrizziInquiryLogResponse::class.java] = BrizziInquiryLogResponse(brizziInquiryLog = BrizziInquiryLog(inquiryId = inquiryId))
        val gqlResponseLog1Success = GraphqlResponse(result1, HashMap<Type, List<GraphqlError>>(), false)

        every { brizzi.Init(any(),any()) } returns mockk()
        every { brizzi.setUserName(any()) } returns mockk()
        every { brizzi.getBalanceInquiry(any(), any()) } answers { secondArg<Callback>().OnSuccess(BrizziCardObject()) }
        every { brizziCardObjectMapper.mapperBrizzi(any(), any()) } returns balanceInquiry

        coEvery { graphqlRepository.getReseponse(any(), any()) } returnsMany listOf(gqlResponseRefreshTokenSuccess, gqlResponseLog1Success)

        //when
        brizziBalanceViewModel.processBrizziTagIntent(intent, brizzi, "", "",true)

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

        val result = HashMap<Type, Any>()
        result[BrizziTokenResponse::class.java] = BrizziTokenResponse(BrizziToken(token = "abcd"))
        val gqlResponseRefreshTokenSuccess = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        val errorGql = GraphqlError()
        errorGql.message = "Error log balance"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[BrizziInquiryLogResponse::class.java] = listOf(errorGql)
        val gqlResponseErrorLog1 = GraphqlResponse(HashMap<Type, Any?>(), errors, false)

        every { brizzi.Init(any(),any()) } returns mockk()
        every { brizzi.setUserName(any()) } returns mockk()
        every { brizzi.getBalanceInquiry(any(), any()) } answers { secondArg<Callback>().OnSuccess(BrizziCardObject()) }
        every { brizziCardObjectMapper.mapperBrizzi(any(), any()) } returns balanceInquiry

        coEvery { graphqlRepository.getReseponse(any(), any()) } returnsMany listOf(gqlResponseRefreshTokenSuccess, gqlResponseErrorLog1)

        //when
        brizziBalanceViewModel.processBrizziTagIntent(intent, brizzi, "", "",true)

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

        val result = HashMap<Type, Any>()
        result[BrizziTokenResponse::class.java] = BrizziTokenResponse(BrizziToken(token = "abcd"))
        val gqlResponseRefreshTokenSuccess = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        val result1 = HashMap<Type, Any>()
        result1[BrizziInquiryLogResponse::class.java] = BrizziInquiryLogResponse(brizziInquiryLog = BrizziInquiryLog(inquiryId = inquiryId))
        val gqlResponseLog1Success = GraphqlResponse(result1, HashMap<Type, List<GraphqlError>>(), false)

        every { brizzi.Init(any(),any()) } returns mockk()
        every { brizzi.setUserName(any()) } returns mockk()
        every { brizzi.getBalanceInquiry(any(), any()) } answers { secondArg<Callback>().OnSuccess(BrizziCardObject()) }
        every { brizziCardObjectMapper.mapperBrizzi(any(), any()) } returns balanceInquiry
        every { brizzi.doUpdateBalance(intent, any(), any()) } answers { thirdArg<Callback>().OnFailure(BrizziException(BrizziBalanceViewModel.BRIZZI_TOKEN_EXPIRED, "")) }

        coEvery { graphqlRepository.getReseponse(any(), any()) } returnsMany listOf(gqlResponseRefreshTokenSuccess, gqlResponseLog1Success)

        //when
        brizziBalanceViewModel.processBrizziTagIntent(intent, brizzi, "", "",true)

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

        val result = HashMap<Type, Any>()
        result[BrizziTokenResponse::class.java] = BrizziTokenResponse(BrizziToken(token = "abcd"))
        val gqlResponseRefreshTokenSuccess = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        val result1 = HashMap<Type, Any>()
        result1[BrizziInquiryLogResponse::class.java] = BrizziInquiryLogResponse(brizziInquiryLog = BrizziInquiryLog(inquiryId = 10))
        val gqlResponseLog1Success = GraphqlResponse(result1, HashMap<Type, List<GraphqlError>>(), false)

        val result2 = HashMap<Type, Any>()
        result2[BrizziInquiryLogResponse::class.java] = BrizziInquiryLogResponse(brizziInquiryLog = BrizziInquiryLog(inquiryId = 13))
        val gqlResponseLog2Success = GraphqlResponse(result1, HashMap<Type, List<GraphqlError>>(), false)

        every { brizzi.Init(any(),any()) } returns mockk()
        every { brizzi.setUserName(any()) } returns mockk()
        every { brizzi.getBalanceInquiry(any(), any()) } answers { secondArg<Callback>().OnSuccess(BrizziCardObject()) }
        every { brizzi.doUpdateBalance(intent, any(), any()) } answers { thirdArg<Callback>().OnSuccess(BrizziCardObject()) }
        every { brizziCardObjectMapper.mapperBrizzi(any(), any()) } returnsMany listOf(balanceInquiryBeforeUpdate, balanceInquiryAfterUpdate)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returnsMany listOf(gqlResponseRefreshTokenSuccess, gqlResponseLog1Success, gqlResponseLog2Success)

        //when
        brizziBalanceViewModel.processBrizziTagIntent(intent, brizzi, "", "",true)

        //then
        assertNotNull(brizziBalanceViewModel.issuerId.value)
        assertEquals(brizziBalanceViewModel.issuerId.value, 2)

        assertNotNull(brizziBalanceViewModel.inquiryIdBrizzi)
//        assertEquals(13, brizziBalanceViewModel.inquiryIdBrizzi)

        assertNotNull(brizziBalanceViewModel.emoneyInquiry.value)
        assertEquals(balanceInquiryAfterUpdate, brizziBalanceViewModel.emoneyInquiry.value)
    }
}