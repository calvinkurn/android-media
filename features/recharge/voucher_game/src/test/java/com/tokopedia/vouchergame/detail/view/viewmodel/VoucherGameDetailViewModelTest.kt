package com.tokopedia.vouchergame.detail.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.common.topupbills.data.product.CatalogProductInput
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchergame.detail.data.VoucherGameDetailData
import com.tokopedia.vouchergame.detail.data.VoucherGameProduct
import com.tokopedia.vouchergame.detail.data.VoucherGameProductData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test

import org.junit.Rule

class VoucherGameDetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mapParams = mapOf<String, String>()
    private val errorMessage = "unable to retrieve data"
    lateinit var gqlResponseFail: GraphqlResponse

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    lateinit var voucherGameDetailViewModel: VoucherGameDetailViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        val graphqlError = GraphqlError()
        graphqlError.message = errorMessage
        gqlResponseFail = GraphqlResponse(
                mapOf(),
                mapOf(MessageErrorException::class.java to listOf(graphqlError)), false)

        voucherGameDetailViewModel =
                VoucherGameDetailViewModel(graphqlRepository, Dispatchers.Unconfined)
    }

    @Test
    fun getVoucherGameProducts_Success() {
        val dataCollection = listOf(
                VoucherGameProductData.DataCollection("collection_1", listOf(VoucherGameProduct(id = "1"), VoucherGameProduct(id = "2"))),
                VoucherGameProductData.DataCollection("collection_2", listOf(VoucherGameProduct(id = "3")))
        )
        val voucherGameDetailData = VoucherGameDetailData(
                enquiryFields = listOf(CatalogProductInput("1",
                        dataCollections = listOf(CatalogProductInput.DataCollection("value")),
                        validations = listOf(CatalogProductInput.Validation(1)))),
                product = VoucherGameProductData("data", dataCollections = dataCollection))
        val gqlResponseSuccess = GraphqlResponse(
                mapOf(VoucherGameDetailData.Response::class.java to voucherGameDetailData),
                mapOf(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess

        val observer = Observer<Result<VoucherGameDetailData>> {
            assert(it is Success)
            val response = (it as Success).data

            // Enquiry Fields
            assert(response.enquiryFields.isNotEmpty())
            assertEquals(response.enquiryFields[0].id, 1)
            assert(response.enquiryFields[0].dataCollections.isNotEmpty())
            assertEquals(response.enquiryFields[0].dataCollections[0].value, "value")
            assert(response.enquiryFields[0].validations.isNotEmpty())
            assertEquals(response.enquiryFields[0].validations[0].id, 1)

            // Products
            assertEquals(response.product.name, "data")
            assert(response.product.dataCollections.isNotEmpty())
            val dataCollection = response.product.dataCollections
            assertEquals(dataCollection[0].name, "collection_1")
            assertEquals(dataCollection[1].name, "collection_2")
            assert(dataCollection[0].products.isNotEmpty())
            assert(dataCollection[1].products.isNotEmpty())
            assertEquals(dataCollection[0].products[0].id, 1)
            assertEquals(dataCollection[0].products[1].id, 2)
            assertEquals(dataCollection[1].products[0].id, 3)

            // Product item position
            assertEquals(dataCollection[0].products[0].position, 0)
            assertEquals(dataCollection[0].products[1].position, 1)
            assertEquals(dataCollection[1].products[0].position, 2)
        }

        try {
            voucherGameDetailViewModel.voucherGameProducts.observeForever(observer)
            voucherGameDetailViewModel.getVoucherGameProducts("", mapParams)
        } finally {
            voucherGameDetailViewModel.voucherGameProducts.removeObserver(observer)
        }
    }

    @Test
    fun getVoucherGameProducts_Fail() {
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseFail

        val observer = Observer<Result<VoucherGameDetailData>> {
            assert(it is Fail)
            assertEquals((it as Fail).throwable.message, errorMessage)
        }

        try {
            voucherGameDetailViewModel.voucherGameProducts.observeForever(observer)
            voucherGameDetailViewModel.getVoucherGameProducts("", mapParams)
        } finally {
            voucherGameDetailViewModel.voucherGameProducts.removeObserver(observer)
        }
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
}