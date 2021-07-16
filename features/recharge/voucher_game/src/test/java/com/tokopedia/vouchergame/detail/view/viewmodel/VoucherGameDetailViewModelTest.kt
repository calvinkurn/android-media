package com.tokopedia.vouchergame.detail.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.topupbills.data.product.CatalogOperatorAttributes
import com.tokopedia.common.topupbills.data.product.CatalogProductInput
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.vouchergame.detail.data.VoucherGameDetailData
import com.tokopedia.vouchergame.detail.data.VoucherGameProduct
import com.tokopedia.vouchergame.detail.data.VoucherGameProductData
import com.tokopedia.vouchergame.list.data.VoucherGameListData
import com.tokopedia.vouchergame.list.data.VoucherGameOperator
import com.tokopedia.vouchergame.list.usecase.VoucherGameListUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class VoucherGameDetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mapParams = mapOf<String, String>()
    lateinit var gqlResponseFail: GraphqlResponse

    @MockK
    lateinit var graphqlRepository: GraphqlRepository
    lateinit var voucherGameDetailViewModel: VoucherGameDetailViewModel

    @MockK
    lateinit var voucherGameListUseCase: VoucherGameListUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        val result = HashMap<Type, Any?>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = MessageErrorException::class.java

        result[objectType] = null
        errors[objectType] = listOf(GraphqlError())

        gqlResponseFail = GraphqlResponse(result, errors, false)

        voucherGameDetailViewModel =
                VoucherGameDetailViewModel(voucherGameListUseCase, graphqlRepository, CoroutineTestDispatchersProvider)
    }

    @Test
    fun getVoucherGameProducts_Success() {
        val dataCollection = listOf(
                VoucherGameProductData.DataCollection("collection_1", listOf(VoucherGameProduct(id = "1"), VoucherGameProduct(id = "2"))),
                VoucherGameProductData.DataCollection("collection_2", listOf(VoucherGameProduct(id = "3")))
        )
        val voucherGameDetailData = VoucherGameDetailData.Response(VoucherGameDetailData(
                enquiryFields = listOf(CatalogProductInput("1",
                        dataCollections = listOf(CatalogProductInput.DataCollection(name = "name")),
                        validations = listOf(CatalogProductInput.Validation(1)))),
                product = VoucherGameProductData("data", dataCollections = dataCollection)
        ))
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = VoucherGameDetailData.Response::class.java
        result[objectType] = voucherGameDetailData
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess

        voucherGameDetailViewModel.getVoucherGameProducts("", mapParams)
        val actualData = voucherGameDetailViewModel.voucherGameProducts.value
        assert(actualData is Success)
        val response = (actualData as Success).data

        // Enquiry Fields
        assert(response.enquiryFields.isNotEmpty())
        assertEquals(response.enquiryFields[0].id, "1")
        assert(response.enquiryFields[0].dataCollections.isNotEmpty())
        assertEquals(response.enquiryFields[0].dataCollections[0].name, "name")
        assert(response.enquiryFields[0].validations.isNotEmpty())
        assertEquals(response.enquiryFields[0].validations[0].id, 1)

        // Products
        assertEquals(response.product.name, "data")
        assert(response.product.dataCollections.isNotEmpty())
        val dataCollectionResponse = response.product.dataCollections
        assertEquals(dataCollectionResponse[0].name, "collection_1")
        assertEquals(dataCollectionResponse[1].name, "collection_2")
        assert(dataCollectionResponse[0].products.isNotEmpty())
        assert(dataCollectionResponse[1].products.isNotEmpty())
        assertEquals(dataCollectionResponse[0].products[0].id, "1")
        assertEquals(dataCollectionResponse[0].products[1].id, "2")
        assertEquals(dataCollectionResponse[1].products[0].id, "3")

        // Product item position
        assertEquals(dataCollectionResponse[0].products[0].position, 0)
        assertEquals(dataCollectionResponse[0].products[1].position, 1)
        assertEquals(dataCollectionResponse[1].products[0].position, 2)
    }

    @Test
    fun getVoucherGameProducts_Fail() {
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseFail

        voucherGameDetailViewModel.getVoucherGameProducts("", mapParams)
        val actualData = voucherGameDetailViewModel.voucherGameProducts.value
        assert(actualData is Fail)
    }

    @Test
    fun getVoucherGameOperators_Success() {
        // given
        val attributes = CatalogOperatorAttributes(name = "vouchergame1")
        val useCaseResultSuccess = VoucherGameListData(operators = listOf(VoucherGameOperator(id = 1, attributes = attributes),
                VoucherGameOperator(id = 3)))
        coEvery {
            voucherGameListUseCase.getVoucherGameOperators(any(), any(), any(), any())
        } returns Success(useCaseResultSuccess)

        // when
        // get voucher game operator from operator Id 1
        voucherGameDetailViewModel.getVoucherGameOperators("", mapParams, false, operatorId = "1")

        // then
        val actualData = voucherGameDetailViewModel.voucherGameOperatorDetails.value
        assert(actualData is Success)
        val response = actualData as Success
        assert(response.data.id == 1)
        assert(response.data.attributes.name == "vouchergame1")
    }

    @Test
    fun getVoucherGameOperators_FailToFindCertainId() {
        // given
        val attributes = CatalogOperatorAttributes(name = "vouchergame1")
        val useCaseResultSuccess = VoucherGameListData(operators = listOf(VoucherGameOperator(id = 1, attributes = attributes),
                VoucherGameOperator(id = 3)))
        coEvery {
            voucherGameListUseCase.getVoucherGameOperators(any(), any(), any(), any())
        } returns Success(useCaseResultSuccess)

        // when
        // get voucher game operator from operator Id 2
        voucherGameDetailViewModel.getVoucherGameOperators("", mapParams, true, operatorId = "2")

        // then
        // no operator detail found
        val actualData = voucherGameDetailViewModel.voucherGameOperatorDetails.value
        assertNull(actualData)
    }

    @Test
    fun getVoucherGameOperators_Fail() {
        // given
        val errorMessage = "this is error msg"
        coEvery {
            voucherGameListUseCase.getVoucherGameOperators(any(), any(), any(), any())
        } returns Fail(MessageErrorException(errorMessage))

        // when
        voucherGameDetailViewModel.getVoucherGameOperators("", mapParams, operatorId = "1")

        // then
        val actualData = voucherGameDetailViewModel.voucherGameOperatorDetails.value
        assert(actualData is Fail)
        assert((actualData as Fail).throwable.message == errorMessage)
    }

    @Test
    fun createParams() {
        val expectedResult = mapOf(
                VoucherGameDetailViewModel.PARAM_MENU_ID to 1,
                VoucherGameDetailViewModel.PARAM_OPERATOR to "1"
        )

        val params = voucherGameDetailViewModel.createParams(1, "1")
        assertEquals(params, expectedResult)
    }

    @Test
    fun createMenuDetailParams() {
        val expectedResult = mapOf(
                VoucherGameDetailViewModel.PARAM_MENU_ID to 1,
        )

        val params = voucherGameDetailViewModel.createMenuDetailParams(1)
        assertEquals(params, expectedResult)
    }
}