package com.tokopedia.topupbills.telco.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class SharedTelcoViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var telcoViewModel: SharedTelcoViewModel

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        telcoViewModel = SharedTelcoViewModel(graphqlRepository, Dispatchers.Unconfined)
    }

    @Test
    fun getPrefixOperator_DataValid_SuccessGetData() {
        //given
        val listPrefixes = mutableListOf<RechargePrefix>()
        listPrefixes.add(RechargePrefix(operator = TelcoOperator(attributes = TelcoAttributesOperator(name = "simpati"))))
        val catalogPrefixSelect = TelcoCatalogPrefixSelect(RechargeCatalogPrefixSelect(prefixes = listPrefixes))

        val result = HashMap<Type, Any>()
        result[TelcoCatalogPrefixSelect::class.java] = catalogPrefixSelect
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        telcoViewModel.getPrefixOperator("", 2)

        //then
        val actualData = telcoViewModel.catalogPrefixSelect.value
        assertNotNull(actualData)
        assert(actualData is Success)
        val prefixSelect = (actualData as Success).data.rechargeCatalogPrefixSelect.prefixes
        assertEquals(listPrefixes, prefixSelect)
    }

    @Test
    fun getPrefixOperator_DataValid_FailedGetData() {
        //given
        val errorGql = GraphqlError()
        errorGql.message = "Error gql"

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[TelcoCatalogPrefixSelect::class.java] = listOf(errorGql)
        val gqlResponse = GraphqlResponse(HashMap<Type, Any>(), errors, false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        telcoViewModel.getPrefixOperator("", 2)

        //then
        val actualData = telcoViewModel.catalogPrefixSelect.value
        assertNotNull(actualData)
        assert(actualData is Fail)
        val error = (actualData as Fail).throwable
        assertEquals(errorGql.message, error.message)
    }
}