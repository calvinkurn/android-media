package com.tokopedia.topupbills.telco.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.topupbills.telco.data.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class SharedTelcoPrepaidViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var sharedTelcoPrepaidViewModel: SharedTelcoPrepaidViewModel

    @MockK
    lateinit var graphqlRepository: GraphqlRepository
    lateinit var gson: Gson

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        gson = Gson()
        sharedTelcoPrepaidViewModel = SharedTelcoPrepaidViewModel(graphqlRepository, Dispatchers.Unconfined)
    }

    @Test
    fun getCatalogProductList_DataValid_SuccessGetData() {
        //given
        val multiTab = gson.fromJson(gson.JsonToString("multitab.json"), TelcoCatalogProductInputMultiTab::class.java)

        val result = HashMap<Type, Any>()
        result[TelcoCatalogProductInputMultiTab::class.java] = multiTab
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        sharedTelcoPrepaidViewModel.getCatalogProductList("", 2, "1")

        //then
        val actualData = sharedTelcoPrepaidViewModel.productList.value
        assertNotNull(actualData)
        assert(actualData is Success)
        val labelPulsa = (actualData as Success).data[0].label
        assertEquals(multiTab.rechargeCatalogProductDataData.productInputList[0].label, labelPulsa)
    }

    @Test
    fun getCatalogProductList_DataValid_FailedGetData() {
        //given
        val errorGql = GraphqlError()
        errorGql.message = "Error gql"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[TelcoCatalogProductInputMultiTab::class.java] = listOf(errorGql)
        val gqlResponse = GraphqlResponse(HashMap<Type, Any>(), errors, false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        sharedTelcoPrepaidViewModel.getCatalogProductList("", 2, "1")

        //then
        val actualData = sharedTelcoPrepaidViewModel.productList.value
        assertNotNull(actualData)
        assert(actualData is Fail)
        val error = (actualData as Fail).throwable
        assertEquals(errorGql.message, error.message)
    }
}