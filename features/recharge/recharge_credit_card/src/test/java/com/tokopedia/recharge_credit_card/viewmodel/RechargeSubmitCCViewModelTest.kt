package com.tokopedia.recharge_credit_card.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.recharge_credit_card.data.CCRedirectUrl
import com.tokopedia.recharge_credit_card.data.RechargeCCRepository
import com.tokopedia.recharge_credit_card.data.RechargeCCRepositoryImpl
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCSignature
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCSignatureReponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import java.lang.reflect.Type

class RechargeSubmitCCViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var graphqlRepository: GraphqlRepository
    @MockK
    lateinit var rechargeSubmitRepository: RechargeCCRepository

    lateinit var rechargeSubmitViewModel: RechargeSubmitCCViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        rechargeSubmitViewModel = RechargeSubmitCCViewModel(graphqlRepository,
                Dispatchers.Unconfined, rechargeSubmitRepository)
    }

    @Test
    fun postCreditCard_SuccessGetSignature_RedirectPageToCheckout() {
        //given
        val signature = "abcdefg"
        val rechargeCCSignature = RechargeCCSignature(signature, "")
        val ccRedirectUrl = CCRedirectUrl("", "www.tokopedia.com")
        val mapParam = hashMapOf<String, String>()

        val result = HashMap<Type, Any>()
        result[RechargeCCSignatureReponse::class.java] = RechargeCCSignatureReponse(rechargeCCSignature)
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse
        coEvery { rechargeSubmitRepository.postCreditCardNumber(any())} returns ccRedirectUrl

        //when
        rechargeSubmitViewModel.postCreditCard("", "26", mapParam)

        //then
        val actualData = rechargeSubmitViewModel.redirectUrl
        assertNotNull(actualData)
        assertEquals(ccRedirectUrl, actualData.value)
        assertEquals(signature, mapParam[RechargeSubmitCCViewModel.PARAM_PCIDSS])
    }

    @Test
    fun postCreditCard_SuccessGetSignature_GetErrorMessagePCIDSS() {
        //given
        val signature = "abcdefg"
        val rechargeCCSignature = RechargeCCSignature(signature, "")
        val ccRedirectUrl = CCRedirectUrl("Error dari API", "")
        val mapParam = hashMapOf<String, String>()

        val result = HashMap<Type, Any>()
        result[RechargeCCSignatureReponse::class.java] = RechargeCCSignatureReponse(rechargeCCSignature)
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse
        coEvery { rechargeSubmitRepository.postCreditCardNumber(any())} returns ccRedirectUrl

        //when
        rechargeSubmitViewModel.postCreditCard("", "26", mapParam)

        //then
        val actualData = rechargeSubmitViewModel.errorSubmitCreditCard
        assertNotNull(actualData)
        assertEquals(ccRedirectUrl.messageError, actualData.value)
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
        coEvery { rechargeSubmitRepository.postCreditCardNumber(any())} throws IOException(RechargeCCRepositoryImpl.ERROR_DEFAULT)

        //when
        rechargeSubmitViewModel.postCreditCard("", "26", mapParam)

        //then
        val actualData = rechargeSubmitViewModel.errorSubmitCreditCard
        assertNotNull(actualData)
        assertEquals(RechargeCCRepositoryImpl.ERROR_DEFAULT, actualData.value)
    }

    @Test
    fun postCreditCard_GetErrorMessageSignature_ShowErrorSignature() {
        //given
        val rechargeCCSignature = RechargeCCSignature("", "Error signature API")
        val mapParam = hashMapOf<String, String>()

        val result = HashMap<Type, Any>()
        result[RechargeCCSignatureReponse::class.java] = RechargeCCSignatureReponse(rechargeCCSignature)
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        rechargeSubmitViewModel.postCreditCard("", "26", mapParam)

        //then
        val actualData = rechargeSubmitViewModel.errorSignature
        assertNotNull(actualData)
        assertEquals(rechargeCCSignature.messageError, actualData.value)
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
        assertEquals(errorGql.message, actualData.value)
    }
}