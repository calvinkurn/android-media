package com.tokopedia.digital.home.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.digital.home.DigitalHomePageTestDispatchersProvider
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class DigitalHomePageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mapParams = mapOf<String, String>()
    lateinit var gqlResponseFail: GraphqlResponse

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    lateinit var digitalHomePageViewModel: DigitalHomePageViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        val result = HashMap<Type, Any?>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = MessageErrorException::class.java

        result[objectType] = null
        errors[objectType] = listOf(GraphqlError())
        gqlResponseFail = GraphqlResponse(result, errors, false)

        digitalHomePageViewModel =
                DigitalHomePageViewModel(graphqlRepository, DigitalHomePageTestDispatchersProvider())
    }

    @Test
    fun getRechargeHomepageSections_Success() {
        val sectionsResponse = RechargeHomepageSections(listOf(
                RechargeHomepageSections.Section("Test", "test", "TOP_ICONS", listOf(
                        RechargeHomepageSections.Item("Test1", "test1")
        ))))
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeHomepageSections.Response::class.java
        result[objectType] = RechargeHomepageSections.Response(sectionsResponse)
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess

        digitalHomePageViewModel.getRechargeHomepageSections("", mapParams)
        val actualData = digitalHomePageViewModel.rechargeHomepageSections.value
        assert(actualData is Success)
        val sections = (actualData as Success).data.sections
        assert(sections.isNotEmpty())
        val section = sections[0]
        assertEquals(section.title, "Test")
        assertEquals(section.subTitle, "test")
        assertEquals(section.template, "TOP_ICONS")
        assert(section.items.isNotEmpty())
        val item = section.items[0]
        assertEquals(item.title, "Test1")
        assertEquals(item.subTitle, "test1")
    }

    @Test
    fun getRechargeHomepageSections_Fail() {
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseFail

        digitalHomePageViewModel.getRechargeHomepageSections("", mapParams)
        val actualData = digitalHomePageViewModel.rechargeHomepageSections.value
        assert(actualData is Fail)
    }

    @Test
    fun createRechargeHomepageSectionsParams() {
        with (DigitalHomePageViewModel.Companion) {
            val enablePersonalize = true
            val actual = digitalHomePageViewModel.createRechargeHomepageSectionsParams(enablePersonalize)
            assertEquals(actual, mapOf(PARAM_RECHARGE_HOMEPAGE_SECTIONS_PERSONALIZE to enablePersonalize))
        }
    }

    @Test
    fun createRechargeHomepageSectionsParams_Default() {
        with (DigitalHomePageViewModel.Companion) {
            val actual = digitalHomePageViewModel.createRechargeHomepageSectionsParams()
            assertEquals(actual, mapOf(PARAM_RECHARGE_HOMEPAGE_SECTIONS_PERSONALIZE to false))
        }
    }

}