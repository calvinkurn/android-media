package com.tokopedia.rechargegeneral.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.rechargegeneral.RechargeGeneralTestDispatchersProvider
import com.tokopedia.common.topupbills.data.product.CatalogOperator
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.common.topupbills.data.product.CatalogProductData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.rechargegeneral.model.RechargeGeneralOperatorCluster
import com.tokopedia.rechargegeneral.model.RechargeGeneralProductData
import com.tokopedia.rechargegeneral.model.RechargeGeneralProductItemData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RechargeGeneralViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mapParams = mapOf<String, String>()
    lateinit var gqlResponseFail: GraphqlResponse

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    lateinit var rechargeGeneralViewModel: RechargeGeneralViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        gqlResponseFail = GraphqlResponse(
                mapOf(),
                mapOf(MessageErrorException::class.java to listOf(GraphqlError())), false)

        rechargeGeneralViewModel =
                RechargeGeneralViewModel(graphqlRepository, RechargeGeneralTestDispatchersProvider())
    }

    @Test
    fun getOperatorCluster_Success() {
        val operatorCluster = RechargeGeneralOperatorCluster.Response(RechargeGeneralOperatorCluster(
                operatorGroups = listOf(RechargeGeneralOperatorCluster.CatalogOperatorGroup(
                        operators = listOf(CatalogOperator(1))
                ))
        ))
        val gqlResponseSuccess = GraphqlResponse(
                mapOf(RechargeGeneralOperatorCluster.Response::class.java to operatorCluster),
                mapOf(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess

        rechargeGeneralViewModel.getOperatorCluster("", mapParams)
        val actualData = rechargeGeneralViewModel.operatorCluster.value
        assert(actualData is Success)
        val operatorGroups = (actualData as Success).data.operatorGroups
        assertNotNull(operatorGroups)
        operatorGroups?.run {
            assertEquals(operatorGroups.first().operators.first().id, 1)
        }
    }

    // Field value in response is null
    @Test
    fun getOperatorCluster_Fail_NullResponse() {
        val operatorCluster = RechargeGeneralOperatorCluster.Response(RechargeGeneralOperatorCluster(
                operatorGroups = null
        ))
        val gqlResponseNull = GraphqlResponse(
                mapOf(RechargeGeneralOperatorCluster.Response::class.java to operatorCluster),
                mapOf(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseNull

        rechargeGeneralViewModel.getOperatorCluster("", mapParams)
        val actualData = rechargeGeneralViewModel.operatorCluster.value
        assert(actualData is Fail)
    }

    @Test
    fun getOperatorCluster_Fail_ErrorResponse() {
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseFail

        rechargeGeneralViewModel.getOperatorCluster("", mapParams)
        val actualData = rechargeGeneralViewModel.operatorCluster.value
        assert(actualData is Fail)
    }

    @Test
    fun getProductList_Success() {
        val productData = RechargeGeneralProductData.Response(RechargeGeneralProductData(
                product = RechargeGeneralProductItemData(
                        dataCollections = listOf(CatalogProductData.DataCollection(
                                products = listOf(CatalogProduct(id = "1"))
                        ))
                )
        ))
        val gqlResponseSuccess = GraphqlResponse(
                mapOf(RechargeGeneralProductData.Response::class.java to productData),
                mapOf(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess

        rechargeGeneralViewModel.getProductList("", mapParams)
        val actualData = rechargeGeneralViewModel.productList.value
        assert(actualData is Success)
        val product = (actualData as Success).data.product
        assertNotNull(product)
        product?.run {
            assertEquals(product.dataCollections.first().products.first().id, "1")
        }
    }

    // Field value in response is null
    @Test
    fun getProductList_Fail_NullResponse() {
        val productData = RechargeGeneralProductData.Response(RechargeGeneralProductData(
                product = null
        ))
        val gqlResponseNull = GraphqlResponse(
                mapOf(RechargeGeneralProductData.Response::class.java to productData),
                mapOf(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseNull

        rechargeGeneralViewModel.getProductList("", mapParams)
        val actualData = rechargeGeneralViewModel.productList.value
        assert(actualData is Fail)
        assertEquals((actualData as Fail).throwable.message, RechargeGeneralViewModel.NULL_PRODUCT_ERROR)
    }

    @Test
    fun getProductList_Fail_ErrorResponse() {
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseFail

        rechargeGeneralViewModel.getProductList("", mapParams)
        val actualData = rechargeGeneralViewModel.productList.value
        assert(actualData is Fail)
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
        val operatorId = 1

        val actual = rechargeGeneralViewModel.createProductListParams(menuId, operatorId)
        assertEquals(actual, mapOf(
                RechargeGeneralViewModel.PARAM_MENU_ID to menuId,
                RechargeGeneralViewModel.PARAM_OPERATOR to operatorId.toString()))
    }
}