package com.tokopedia.recharge_credit_card.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCSignature
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCSignatureReponse
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class RechargeSubmitCCViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = UnconfinedTestRule()

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    lateinit var rechargeSubmitViewModel: RechargeSubmitCCViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        rechargeSubmitViewModel = RechargeSubmitCCViewModel(
            graphqlRepository,
            Dispatchers.Unconfined
        )
    }

    // ========================================= POST CREDIT CARD, SUCCESS GET SIGNATURE =====================================
    @Test
    fun postCreditCard_SuccessGetSignature_RedirectPageToCheckout() {
        // given
        val signature = "abcdefg"
        val rechargeCCSignature = RechargeCCSignature(signature, "")

        val result = HashMap<Type, Any>()
        result[RechargeCCSignatureReponse::class.java] = RechargeCCSignatureReponse(rechargeCCSignature)
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        // when
        rechargeSubmitViewModel.postCreditCard("", "26")

        // then
        val actualData = rechargeSubmitViewModel.signature.value
        assertNotNull(actualData)
        assertEquals(signature, actualData)
    }

    // ========================================= POST CREDIT CARD, FAILED GET SIGNATURE =====================================

    @Test
    fun postCreditCard_GetErrorMessageSignature_ShowErrorSignature() {
        // given
        val rechargeCCSignature = RechargeCCSignature("", "Error signature API")

        val result = HashMap<Type, Any>()
        result[RechargeCCSignatureReponse::class.java] = RechargeCCSignatureReponse(rechargeCCSignature)
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        // when
        rechargeSubmitViewModel.postCreditCard("", "26")

        // then
        val actualData = rechargeSubmitViewModel.errorSignature
        assertNotNull(actualData)
        assertEquals(rechargeCCSignature.messageError, actualData.value?.message)
    }

    @Test
    fun postCreditCard_ErrorAPISignature_ShowErrorSignature() {
        // given
        val errorGql = GraphqlError()
        errorGql.message = "Error gql"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[RechargeCCSignatureReponse::class.java] = listOf(errorGql)
        val gqlResponse = GraphqlResponse(HashMap<Type, Any?>(), errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponse

        // when
        rechargeSubmitViewModel.postCreditCard("", "26")

        // then
        val actualData = rechargeSubmitViewModel.errorSignature
        assertNotNull(actualData)
        assertEquals(errorGql.message, actualData.value?.message)
    }
}
