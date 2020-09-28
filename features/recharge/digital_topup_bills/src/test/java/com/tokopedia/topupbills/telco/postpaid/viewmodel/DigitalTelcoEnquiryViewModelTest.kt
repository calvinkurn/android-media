package com.tokopedia.topupbills.telco.postpaid.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.topupbills.data.TelcoEnquiryData
import com.tokopedia.common.topupbills.data.TopupBillsEnquiry
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryAttribute
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class DigitalTelcoEnquiryViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var enquiryViewModel: DigitalTelcoEnquiryViewModel

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        enquiryViewModel = DigitalTelcoEnquiryViewModel(graphqlRepository, Dispatchers.Unconfined)
    }

    @Test
    fun getEnquiry_DataValid_SuccessGetData() {
        //given
        val telcoEnquiryData = TelcoEnquiryData(TopupBillsEnquiry(attributes = TopupBillsEnquiryAttribute(price = "2000")))
        val result = HashMap<Type, Any>()
        result[TelcoEnquiryData::class.java] = telcoEnquiryData
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        enquiryViewModel.getEnquiry("", HashMap<String, Any>())

        //then
        val actualData = enquiryViewModel.enquiryResult.value
        Assert.assertNotNull(actualData)
        assert(actualData is Success)
        val priceEnquiryResult = (actualData as Success).data.enquiry.attributes.price
        Assert.assertEquals(telcoEnquiryData.enquiry.attributes.price, priceEnquiryResult)
    }

    @Test
    fun getEnquiry_DataValid_FailedGetData() {
        //given
        val errorGql = GraphqlError()
        errorGql.message = "Error gql"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[TelcoEnquiryData::class.java] = listOf(errorGql)
        val gqlResponse = GraphqlResponse(HashMap<Type, Any>(), errors, false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        enquiryViewModel.getEnquiry("", HashMap<String, Any>())

        //then
        val actualData = enquiryViewModel.enquiryResult.value
        Assert.assertNotNull(actualData)
        assert(actualData is Fail)
        val error = (actualData as Fail).throwable
        Assert.assertEquals(errorGql.message, error.message)
    }
}