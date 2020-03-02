package com.tokopedia.rechargegeneral.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
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
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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

        val observer = Observer<Result<RechargeGeneralOperatorCluster>> {
            assert(it is Success)
            assertEquals((it as Success).data.operatorGroups.first().operators.first().id, 1)
        }

        try {
            rechargeGeneralViewModel.operatorCluster.observeForever(observer)
            rechargeGeneralViewModel.getOperatorCluster(mapParams)
        } finally {
            rechargeGeneralViewModel.operatorCluster.removeObserver(observer)
        }
    }

    @Test
    fun getOperatorCluster_Fail() {
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseFail

        val observer = Observer<Result<RechargeGeneralOperatorCluster>> {
            assert(it is Fail)
            assertEquals((it as Fail).throwable.message, errorMessage)
        }

        try {
            rechargeGeneralViewModel.operatorCluster.observeForever(observer)
            rechargeGeneralViewModel.getOperatorCluster(mapParams)
        } finally {
            rechargeGeneralViewModel.operatorCluster.removeObserver(observer)
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

        val observer = Observer<Result<RechargeGeneralProductData>> {
            assert(it is Success)
            assertEquals((it as Success).data.product.dataCollections.first().products.first().id, "1")
        }

        try {
            rechargeGeneralViewModel.productList.observeForever(observer)
            rechargeGeneralViewModel.getProductList(mapParams)
        } finally {
            rechargeGeneralViewModel.productList.removeObserver(observer)
        }
    }

    @Test
    fun getProductList_Fail() {
        val observer = Observer<Result<RechargeGeneralProductData>> {
            assert(it is Fail)
            assertEquals((it as Fail).throwable.message, errorMessage)
        }

        try {
            rechargeGeneralViewModel.productList.observeForever(observer)
            rechargeGeneralViewModel.getProductList(mapParams)
        } finally {
            rechargeGeneralViewModel.productList.removeObserver(observer)
        }
    }

    @Test
    fun createParams_Partial() {
        val menuId = 1

        val actual = rechargeGeneralViewModel.createParams(menuId)
        assertEquals(actual, mapOf(RechargeGeneralViewModel.PARAM_MENU_ID to menuId))
    }

//    @Test
//    fun createParams_Full() {
//        val menuId = 1
//        val operatorId = 1
//
//        val actual = rechargeGeneralViewModel.createParams(menuId, operatorId)
//        assertEquals(actual, mapOf(
//                RechargeGeneralViewModel.PARAM_MENU_ID to menuId,
//                RechargeGeneralViewModel.PARAM_OPERATOR to operatorId))
//    }
}