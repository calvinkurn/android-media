package com.tokopedia.rechargegeneral.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

class RechargeGeneralViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mapParams = mapOf<String, String>()
    private val errorMessage = "unable to retrieve data"
    lateinit var gqlResponseFail: GraphqlResponse

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    lateinit var rechargeGeneralViewModel: RechargeGeneralViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        val graphqlError = GraphqlError()
        graphqlError.message = errorMessage
        gqlResponseFail = GraphqlResponse(
                mapOf(),
                mapOf(MessageErrorException::class.java to listOf(graphqlError)), false)

        rechargeGeneralViewModel =
                RechargeGeneralViewModel(graphqlRepository, Dispatchers.Unconfined)
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

        rechargeGeneralViewModel.getOperatorCluster(mapParams)

        rechargeGeneralViewModel.operatorCluster.observeForever {
            assert(it is Success)
            assertEquals((it as Success).data.operatorGroups.first().operators.first().id, 1)
        }
    }

    @Test
    fun getOperatorCluster_Fail() {
        rechargeGeneralViewModel.getOperatorCluster(mapParams)

        rechargeGeneralViewModel.operatorCluster.observeForever {
            assert(it is Fail)
            assertEquals((it as Fail).throwable.message, errorMessage)
        }
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

        rechargeGeneralViewModel.getProductList(mapParams)

        rechargeGeneralViewModel.productList.observeForever {
            assert(it is Success)
            assertEquals((it as Success).data.product.dataCollections.first().products.first().id, "1")
        }
    }

    @Test
    fun getProductList_Fail() {
        rechargeGeneralViewModel.getProductList(mapParams)

        rechargeGeneralViewModel.productList.observeForever {
            assert(it is Fail)
            assertEquals((it as Fail).throwable.message, errorMessage)
        }
    }

    @Test
    fun createParams_Partial() {
        val menuId = 1

        val actual = rechargeGeneralViewModel.createParams(menuId)
        assertEquals(actual, mapOf(RechargeGeneralViewModel.PARAM_MENU_ID to menuId))
    }

    @Test
    fun createParams_Full() {
        val menuId = 1
        val operatorId = 1

        val actual = rechargeGeneralViewModel.createParams(menuId, operatorId)
        assertEquals(actual, mapOf(
                RechargeGeneralViewModel.PARAM_MENU_ID to menuId,
                RechargeGeneralViewModel.PARAM_OPERATOR to operatorId))
    }
}