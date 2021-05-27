package com.tokopedia.recharge_credit_card.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.recharge_credit_card.datamodel.CCRedirectUrl
import com.tokopedia.recharge_credit_card.datamodel.CCRedirectUrlResponse
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCSignature
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCSignatureReponse
import com.tokopedia.recharge_credit_card.usecase.RechargeSubmitCcUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import java.lang.reflect.Type
import java.net.SocketException

class RechargeSubmitCCViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    @MockK
    lateinit var rechargeSubmitCcUseCase: RechargeSubmitCcUseCase

    lateinit var rechargeSubmitViewModel: RechargeSubmitCCViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        rechargeSubmitViewModel = RechargeSubmitCCViewModel(graphqlRepository,
                Dispatchers.Unconfined, rechargeSubmitCcUseCase)
    }

    //========================================= POST CREDIT CARD, SUCCESS GET SIGNATURE =====================================
    @Test
    fun postCreditCard_SuccessGetSignature_RedirectPageToCheckout() {
        //given
        val signature = "abcdefg"
        val rechargeCCSignature = RechargeCCSignature(signature, "")
        val mapParam = hashMapOf<String, String>()

        val result = HashMap<Type, Any>()
        result[RechargeCCSignatureReponse::class.java] = RechargeCCSignatureReponse(rechargeCCSignature)
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        val ccRedirectUrl = CCRedirectUrlResponse(data = CCRedirectUrl("", "www.tokopedia.com"))
        val token = object : TypeToken<CCRedirectUrlResponse>() {}.type
        val response = RestResponse(ccRedirectUrl, 200, false)
        val submitCcResponse = mapOf<Type, RestResponse>(token to response)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse
        coEvery { rechargeSubmitCcUseCase.setMapParam(mapParam) } returns mockk()
        coEvery { rechargeSubmitCcUseCase.executeOnBackground() } returns submitCcResponse

        //when
        rechargeSubmitViewModel.postCreditCard("", "26", mapParam)

        //then
        val actualData = rechargeSubmitViewModel.redirectUrl
        assertNotNull(actualData)
        assertEquals(ccRedirectUrl.data, actualData.value)
        assertEquals(signature, mapParam[RechargeSubmitCCViewModel.PARAM_PCIDSS])
    }

    @Test
    fun postCreditCard_SuccessGetSignature_GetErrorMessagePCIDSS() {
        //given
        val signature = "abcdefg"
        val rechargeCCSignature = RechargeCCSignature(signature, "")
        val mapParam = hashMapOf<String, String>()
        val result = HashMap<Type, Any>()
        result[RechargeCCSignatureReponse::class.java] = RechargeCCSignatureReponse(rechargeCCSignature)
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        val ccRedirectUrl = CCRedirectUrlResponse(data = CCRedirectUrl("Error dari API", ""))
        val token = object : TypeToken<CCRedirectUrlResponse>() {}.type
        val response = RestResponse(ccRedirectUrl, 200, false)
        val submitCcResponse = mapOf<Type, RestResponse>(token to response)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse
        coEvery { rechargeSubmitCcUseCase.setMapParam(mapParam) } returns mockk()
        coEvery { rechargeSubmitCcUseCase.executeOnBackground() } returns submitCcResponse

        //when
        rechargeSubmitViewModel.postCreditCard("", "26", mapParam)

        //then
        val actualData = rechargeSubmitViewModel.errorSubmitCreditCard
        assertNotNull(actualData)
        assertEquals(ccRedirectUrl.data.messageError, actualData.value?.message)
    }

    @Test
    fun postCreditCard_SuccessGetSignature_ErrorApiPCIDSS() {
        //given
        val signature = "abcdefg"
        val rechargeCCSignature = RechargeCCSignature(signature, "")
        val mapParam = hashMapOf<String, String>()

        val result = HashMap<Type, Any>()
        result[RechargeCCSignatureReponse::class.java] = RechargeCCSignatureReponse(rechargeCCSignature)
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse
        coEvery { rechargeSubmitCcUseCase.setMapParam(mapParam)} returns mockk()
        coEvery { RechargeSubmitCCViewModel.convertCCResponse(rechargeSubmitCcUseCase.executeOnBackground())} throws IOException("error")

        //when
        rechargeSubmitViewModel.postCreditCard("", "26", mapParam)

        //then
        val actualData = rechargeSubmitViewModel.errorSubmitCreditCard
        assertNotNull(actualData)
        assertEquals("error", actualData.value?.message)
    }

    @Test
    fun postCreditCard_SuccessGetSignature_ErrorServerHitPCIDSS() {
        //given
        val signature = "abcdefg"
        val rechargeCCSignature = RechargeCCSignature(signature, "")
        val mapParam = hashMapOf<String, String>()

        val result = HashMap<Type, Any>()
        result[RechargeCCSignatureReponse::class.java] = RechargeCCSignatureReponse(rechargeCCSignature)
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse
        coEvery { rechargeSubmitCcUseCase.setMapParam(mapParam)} returns mockk()
        coEvery { RechargeSubmitCCViewModel.convertCCResponse(rechargeSubmitCcUseCase.executeOnBackground())} throws SocketException("error server")

        //when
        rechargeSubmitViewModel.postCreditCard("", "26", mapParam)

        //then
        val actualData = rechargeSubmitViewModel.errorSubmitCreditCard
        assertNotNull(actualData)
        assertEquals("error server", actualData.value?.message)
    }

    //========================================= POST CREDIT CARD, FAILED GET SIGNATURE =====================================

    @Test
    fun postCreditCard_GetErrorMessageSignature_ShowErrorSignature() {
        //given
        val rechargeCCSignature = RechargeCCSignature("", "Error signature API")
        val mapParam = hashMapOf<String, String>()

        val result = HashMap<Type, Any>()
        result[RechargeCCSignatureReponse::class.java] = RechargeCCSignatureReponse(rechargeCCSignature)
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { rechargeSubmitCcUseCase.setMapParam(mapParam)} returns mockk()
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        rechargeSubmitViewModel.postCreditCard("", "26", mapParam)

        //then
        val actualData = rechargeSubmitViewModel.errorSignature
        assertNotNull(actualData)
        assertEquals(rechargeCCSignature.messageError, actualData.value?.message)
    }

    @Test
    fun postCreditCard_ErrorAPISignature_ShowErrorSignature() {
        //given
        val errorGql = GraphqlError()
        errorGql.message = "Error gql"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[RechargeCCSignatureReponse::class.java] = listOf(errorGql)
        val gqlResponse = GraphqlResponse(HashMap<Type, Any?>(), errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        rechargeSubmitViewModel.postCreditCard("", "26", hashMapOf())

        //then
        val actualData = rechargeSubmitViewModel.errorSignature
        assertNotNull(actualData)
        assertEquals(errorGql.message, actualData.value?.message)
    }

    //========================================= CREATE MAP PARAM =====================================

    @Test
    fun createMapParam_successCreate_GetMapParam() {
        //given
        val clientNumber = "4111111111111111"
        val operatorId = "85"
        val productId = "2695"
        val userId = "17211378"

        val mapParam = mutableMapOf<String, String>()
        mapParam[RechargeSubmitCCViewModel.PARAM_ACTION] = RechargeSubmitCCViewModel.VALUE_ACTION
        mapParam[RechargeSubmitCCViewModel.PARAM_CLIENT_NUMBER] = clientNumber
        mapParam[RechargeSubmitCCViewModel.PARAM_OPERATOR_ID] = operatorId
        mapParam[RechargeSubmitCCViewModel.PARAM_PRODUCT_ID] = productId
        mapParam[RechargeSubmitCCViewModel.PARAM_USER_ID] = userId

        //when
        val resultMapParam = rechargeSubmitViewModel.createMapParam(clientNumber, operatorId, productId, userId)

        //then
        assertNotNull(resultMapParam)
        assertEquals(resultMapParam, mapParam)
    }

    @Test
    fun createMapParam_successCreate_GetPcidssParamFromApplink() {
        //given
        val clientNumber = "4111111111111111"
        val operatorId = "85"
        val productId = "2695"
        val userId = "17211378"
        val signature = "Signature"
        val token = "token_identifier"

        val mapParam = mutableMapOf<String, String>()
        mapParam[RechargeSubmitCCViewModel.PARAM_ACTION] = RechargeSubmitCCViewModel.VALUE_ACTION
        mapParam[RechargeSubmitCCViewModel.PARAM_MASKED_NUMBER] = clientNumber
        mapParam[RechargeSubmitCCViewModel.PARAM_OPERATOR_ID] = operatorId
        mapParam[RechargeSubmitCCViewModel.PARAM_PRODUCT_ID] = productId
        mapParam[RechargeSubmitCCViewModel.PARAM_USER_ID] = userId
        mapParam[RechargeSubmitCCViewModel.PARAM_TOKEN] = token
        mapParam[RechargeSubmitCCViewModel.PARAM_PCIDSS] = signature

        //when
        val resultMapParam = rechargeSubmitViewModel.createPcidssParamFromApplink(
                clientNumber, operatorId, productId, userId, signature, token)

        //then
        assertNotNull(resultMapParam)
        assertEquals(resultMapParam, mapParam)
    }
}