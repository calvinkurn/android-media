package com.tokopedia.rechargegeneral.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.topupbills.data.product.CatalogOperator
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.common_digital.common.presentation.model.DigitalDppoConsent
import com.tokopedia.common_digital.common.presentation.model.DigiPersoRecommendationData
import com.tokopedia.common_digital.common.presentation.model.DigiPersoRecommendationItem
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.common_digital.common.usecase.GetDppoConsentUseCase
import com.tokopedia.rechargegeneral.model.*
import com.tokopedia.rechargegeneral.model.mapper.RechargeGeneralMapper
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class RechargeGeneralViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mapParams = mapOf<String, String>()
    lateinit var gqlResponseFail: GraphqlResponse

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    @RelaxedMockK
    lateinit var getDppoConsentUseCase: GetDppoConsentUseCase

    @RelaxedMockK
    lateinit var remoteConfig: RemoteConfig

    lateinit var rechargeGeneralViewModel: RechargeGeneralViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        val result = HashMap<Type, Any?>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = MessageErrorException::class.java

        result[objectType] = null
        errors[objectType] = listOf(GraphqlError())
        gqlResponseFail = GraphqlResponse(result, errors, false)

        rechargeGeneralViewModel =
            RechargeGeneralViewModel(graphqlRepository, getDppoConsentUseCase, RechargeGeneralMapper(), remoteConfig, CoroutineTestDispatchersProvider)
    }

    @Test
    fun getOperatorCluster_Success() {
        val operatorCluster = RechargeGeneralOperatorCluster.Response(
            RechargeGeneralOperatorCluster(
                operatorGroups = listOf(
                    RechargeGeneralOperatorCluster.CatalogOperatorGroup(
                        operators = listOf(CatalogOperator("1"))
                    )
                )
            )
        )
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeGeneralOperatorCluster.Response::class.java
        result[objectType] = operatorCluster
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseSuccess

        rechargeGeneralViewModel.getOperatorCluster("", mapParams, nullErrorMessage = "")
        val actualData = rechargeGeneralViewModel.operatorCluster.value
        assert(actualData is Success)
        val operatorGroups = (actualData as Success).data.operatorGroups
        assertNotNull(operatorGroups)
        operatorGroups?.run {
            assertEquals(operatorGroups.first().operators.first().id, "1")
        }
    }

    // Field value in response is null
    @Test
    fun getOperatorCluster_Fail_NullResponse() {
        val operatorCluster = RechargeGeneralOperatorCluster.Response(
            RechargeGeneralOperatorCluster(
                operatorGroups = null
            )
        )
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeGeneralOperatorCluster.Response::class.java
        result[objectType] = operatorCluster
        val gqlResponseNull = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseNull

        rechargeGeneralViewModel.getOperatorCluster("", mapParams, nullErrorMessage = "")
        val actualData = rechargeGeneralViewModel.operatorCluster.value
        assert(actualData is Fail)
    }

    @Test
    fun getOperatorCluster_Fail_ErrorResponse() {
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseFail

        rechargeGeneralViewModel.getOperatorCluster("", mapParams, nullErrorMessage = "")
        val actualData = rechargeGeneralViewModel.operatorCluster.value
        assert(actualData is Fail)
    }

    @Test
    fun getProductList_Success() {
        val productData = RechargeGeneralDynamicInput.Response(
            RechargeGeneralDynamicInput(
                enquiryFields = listOf(
                    RechargeGeneralDynamicField(
                        name = "product_id",
                        dataCollections = listOf(
                            RechargeGeneralDynamicField.DataCollection(
                                products = listOf(CatalogProduct(id = "1"))
                            )
                        )
                    )
                )
            )
        )

        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeGeneralDynamicInput.Response::class.java
        result[objectType] = productData
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseSuccess

        rechargeGeneralViewModel.getProductList("", mapParams, nullErrorMessage = "")
        val actualData = rechargeGeneralViewModel.productList.value
        assert(actualData is Success)
        val product = (actualData as Success).data.enquiryFields
        assertNotNull(product)
        product?.run {
            assertEquals(actualData.data.enquiryFields[0].dataCollections[0].products[0].id, "1")
        }
    }

    // Field value in response is null
    @Test
    fun getProductList_Fail_NullResponse() {
        val productData = RechargeGeneralDynamicInput.Response(
            RechargeGeneralDynamicInput(
                enquiryFields = listOf(RechargeGeneralDynamicField())
            )
        )
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeGeneralDynamicInput.Response::class.java
        result[objectType] = productData
        val gqlResponseNull = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseNull

        val errorMsg = "this is error message"
        rechargeGeneralViewModel.getProductList("", mapParams, nullErrorMessage = errorMsg)
        val actualData = rechargeGeneralViewModel.productList.value
        assert(actualData is Fail)
        assertEquals((actualData as Fail).throwable.message, errorMsg)
    }

    @Test
    fun getProductList_Fail_ErrorResponse() {
        coEvery { graphqlRepository.response(any(), any()) } returns gqlResponseFail

        rechargeGeneralViewModel.getProductList("", mapParams, nullErrorMessage = "")
        val actualData = rechargeGeneralViewModel.productList.value
        assert(actualData is Fail)
    }

    @Test
    fun getProductList_CancelJob_NullResponse() {
        coEvery { graphqlRepository.response(any(), any()) } coAnswers {
            delay(5000)
            gqlResponseFail
        }

        rechargeGeneralViewModel.getProductList("", mapParams, nullErrorMessage = "")
        rechargeGeneralViewModel.productListJob?.cancel()
        val actualData = rechargeGeneralViewModel.productList.value
        assertTrue(actualData == null)
    }

    @Test
    fun getProductList_invokeTwice_WillCancelFirstJob() {
        coEvery { graphqlRepository.response(any(), any()) } coAnswers {
            delay(5000)
            gqlResponseFail
        }
        rechargeGeneralViewModel.getProductList("", mapParams, nullErrorMessage = "")
        val firstJob = rechargeGeneralViewModel.productListJob

        rechargeGeneralViewModel.getProductList("", mapParams, nullErrorMessage = "")
        val secondJob = rechargeGeneralViewModel.productListJob

        assertTrue(firstJob?.isCancelled == true)
        assertTrue(firstJob != secondJob)
    }

    @Test
    fun productListJob_setJob_shouldNotBeNull() {
        assertTrue(rechargeGeneralViewModel.productListJob == null)
        rechargeGeneralViewModel.productListJob = Job()
        assertTrue(rechargeGeneralViewModel.productListJob != null)
    }

    @Test
    fun createOperatorClusterParams() {
        val menuId = 1

        val actual = rechargeGeneralViewModel.createOperatorClusterParams(menuId)
        assertEquals(actual, mapOf(RechargeGeneralViewModel.PARAM_MENU_ID to menuId))
    }

    @Test
    fun createProductListParams() {
        val menuId = 1
        val operatorId = "1"

        val actual = rechargeGeneralViewModel.createProductListParams(menuId, operatorId)
        assertEquals(
            actual,
            mapOf(
                RechargeGeneralViewModel.PARAM_MENU_ID to menuId,
                RechargeGeneralViewModel.PARAM_OPERATOR to operatorId.toString()
            )
        )
    }

    @Test
    fun getDppoConsentRecharge_Success() {
        // given
        val consentDesc = "Tokopedia"
        val digitalDPPOConsent = DigitalDppoConsent(
            DigiPersoRecommendationData(
                items = listOf(
                    DigiPersoRecommendationItem(
                        id = "1",
                        title = consentDesc
                    )
                )
            )
        )

        coEvery { getDppoConsentUseCase.execute(any()) } returns digitalDPPOConsent

        // when
        rechargeGeneralViewModel.getDppoConsent(1)

        // then
        val actualData = rechargeGeneralViewModel.dppoConsent.value
        assertNotNull(actualData)
        assertTrue(actualData is Success)
        assertTrue((actualData as Success).data.description == consentDesc)
    }

    @Test
    fun getDppoConsentRecharge_Fail() {
        // given
        val errorMessage = "Tokopedia"
        coEvery { getDppoConsentUseCase.execute(any()) } throws MessageErrorException(errorMessage)

        // when
        rechargeGeneralViewModel.getDppoConsent(1)

        // then
        val actualData = rechargeGeneralViewModel.dppoConsent.value
        assertNotNull(actualData)
        assertTrue(actualData is Fail)
        assertTrue((actualData as Fail).throwable.message == errorMessage)
    }
}
