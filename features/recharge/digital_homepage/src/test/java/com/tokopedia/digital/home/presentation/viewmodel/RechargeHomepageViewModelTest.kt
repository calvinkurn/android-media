package com.tokopedia.digital.home.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.digital.home.RechargeHomepageTestDispatchersProvider
import com.tokopedia.digital.home.model.RechargeHomepageSectionAction
import com.tokopedia.digital.home.model.RechargeHomepageSectionSkeleton
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.util.RechargeHomepageSectionMapper
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
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class RechargeHomepageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mapParams = mapOf<String, String>()
    lateinit var gqlResponseFail: GraphqlResponse

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    lateinit var rechargeHomepageViewModel: RechargeHomepageViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        val result = HashMap<Type, Any?>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = MessageErrorException::class.java

        result[objectType] = null
        errors[objectType] = listOf(GraphqlError())
        gqlResponseFail = GraphqlResponse(result, errors, false)

        rechargeHomepageViewModel = RechargeHomepageViewModel(
                graphqlRepository,
                RechargeHomepageTestDispatchersProvider()
        )
    }

    @Test
    fun getRechargeHomepageSectionSkeleton_Success() {
        val sectionSkeletonItem = listOf(RechargeHomepageSectionSkeleton.Item(1, "TOP_BANNER"))
        val skeletonResponse = RechargeHomepageSectionSkeleton(sectionSkeletonItem)
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeHomepageSectionSkeleton.Response::class.java
        result[objectType] = RechargeHomepageSectionSkeleton.Response(skeletonResponse)
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess

        rechargeHomepageViewModel.getRechargeHomepageSectionSkeleton(mapParams)
        val expectedData = RechargeHomepageSectionMapper.mapInitialHomepageSections(sectionSkeletonItem)
        val actualData = rechargeHomepageViewModel.rechargeHomepageSections.value
        assertNotNull(actualData)
        actualData?.run {
            assertEquals(actualData, expectedData)
        }
    }

    @Test
    fun getRechargeHomepageSectionSkeleton_Fail() {
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseFail

        rechargeHomepageViewModel.getRechargeHomepageSectionSkeleton(mapParams)
        val actualData = rechargeHomepageViewModel.rechargeHomepageSectionSkeleton.value
        assert(actualData is Fail)
    }

    @Test
    fun getRechargeHomepageSections_Success() {
        val errors = HashMap<Type, List<GraphqlError>>()

        // Sections Skeleton
        val sectionSkeletonItem = listOf(RechargeHomepageSectionSkeleton.Item(1, "TOP_ICONS"))
        val skeletonResponse = RechargeHomepageSectionSkeleton(sectionSkeletonItem)
        val sectionSkeletonResult = HashMap<Type, Any>()
        val skeletonObjectType = RechargeHomepageSectionSkeleton.Response::class.java
        sectionSkeletonResult[skeletonObjectType] = RechargeHomepageSectionSkeleton.Response(skeletonResponse)
        val gqlsectionSkeletonResponseSuccess = GraphqlResponse(sectionSkeletonResult, errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlsectionSkeletonResponseSuccess

        rechargeHomepageViewModel.getRechargeHomepageSectionSkeleton(mapParams)
        val expectedData = RechargeHomepageSectionMapper.mapInitialHomepageSections(sectionSkeletonItem)
        val actualData = rechargeHomepageViewModel.rechargeHomepageSections.value
        assertNotNull(actualData)
        actualData?.run {
            assertEquals(actualData, expectedData)
        }

        // Sections
        val sectionsResponse = RechargeHomepageSections(listOf(
                RechargeHomepageSections.Section(1, "1", "Test", "test", "TOP_ICONS", listOf(), "", listOf(
                        RechargeHomepageSections.Item(1, "1", "Test1", "test1")
                ))))
        val sectionResult = HashMap<Type, Any>()
        val sectionObjectType = RechargeHomepageSections.Response::class.java
        sectionResult[sectionObjectType] = RechargeHomepageSections.Response(sectionsResponse)
        val gqlSectionResponseSuccess = GraphqlResponse(sectionResult, errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlSectionResponseSuccess

        rechargeHomepageViewModel.getRechargeHomepageSections(
                rechargeHomepageViewModel.createRechargeHomepageSectionsParams(31, listOf(1))
        )
        val sections = rechargeHomepageViewModel.rechargeHomepageSections.value
        assert(!sections.isNullOrEmpty())
        val section = sections?.get(0)
        assertNotNull(section)
        section?.run {
            assertEquals(section.title, "Test")
            assertEquals(section.subtitle, "test")
            assertEquals(section.template, "TOP_ICONS")
            assert(section.items.isNotEmpty())
            val item = section.items[0]
            assertEquals(item.title, "Test1")
            assertEquals(item.subtitle, "test1")
        }
    }

    @Test
    fun getRechargeHomepageSections_Fail() {
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseFail

        rechargeHomepageViewModel.getRechargeHomepageSections(mapParams)
        val sections = rechargeHomepageViewModel.rechargeHomepageSections.value
        assertNotNull(sections)
        sections?.run {
            assert(isEmpty())
        }
    }

    @Test
    fun triggerRechargeSectionAction_Success() {
        val actionResponse = RechargeHomepageSectionAction("")
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeHomepageSectionAction.Response::class.java
        result[objectType] = RechargeHomepageSectionAction.Response(actionResponse)
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess

        rechargeHomepageViewModel.triggerRechargeSectionAction(mapParams)
        val actualData = rechargeHomepageViewModel.rechargeHomepageSectionAction.value
        assert(actualData is Success)
    }

    @Test
    fun triggerRechargeSectionAction_Fail() {
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseFail

        rechargeHomepageViewModel.triggerRechargeSectionAction(mapParams)
        val actualData = rechargeHomepageViewModel.rechargeHomepageSectionAction.value
        assert(actualData is Fail)
    }

    @Test
    fun createRechargeHomepageSectionSkeletonParams() {
        with (RechargeHomepageViewModel.Companion) {
            val enablePersonalize = true
            val actual = rechargeHomepageViewModel.createRechargeHomepageSectionSkeletonParams(31, true)
            assertEquals(actual, mapOf(
                    PARAM_RECHARGE_HOMEPAGE_SECTIONS_PLATFORM_ID to 31,
                    PARAM_RECHARGE_HOMEPAGE_SECTIONS_PERSONALIZE to enablePersonalize
            ))
        }
    }

    @Test
    fun createRechargeHomepageSectionSkeletonParams_Default() {
        with (RechargeHomepageViewModel.Companion) {
            val actual = rechargeHomepageViewModel.createRechargeHomepageSectionSkeletonParams(31)
            assertEquals(actual, mapOf(
                    PARAM_RECHARGE_HOMEPAGE_SECTIONS_PLATFORM_ID to 31,
                    PARAM_RECHARGE_HOMEPAGE_SECTIONS_PERSONALIZE to false
            ))
        }
    }

    @Test
    fun createRechargeHomepageSectionsParams() {
        with (RechargeHomepageViewModel.Companion) {
            val enablePersonalize = true
            val actual = rechargeHomepageViewModel.createRechargeHomepageSectionsParams(31, listOf(1), enablePersonalize)
            assertEquals(actual, mapOf(
                    PARAM_RECHARGE_HOMEPAGE_SECTIONS_PLATFORM_ID to 31,
                    PARAM_RECHARGE_HOMEPAGE_SECTIONS_SECTION_IDS to listOf(1),
                    PARAM_RECHARGE_HOMEPAGE_SECTIONS_PERSONALIZE to enablePersonalize
            ))
        }
    }

    @Test
    fun createRechargeHomepageSectionsParams_Default() {
        with (RechargeHomepageViewModel.Companion) {
            val actual = rechargeHomepageViewModel.createRechargeHomepageSectionsParams(31, listOf(1))
            assertEquals(actual, mapOf(
                    PARAM_RECHARGE_HOMEPAGE_SECTIONS_PLATFORM_ID to 31,
                    PARAM_RECHARGE_HOMEPAGE_SECTIONS_SECTION_IDS to listOf(1),
                    PARAM_RECHARGE_HOMEPAGE_SECTIONS_PERSONALIZE to false
            ))
        }
    }

    @Test
    fun createRechargeHomepageSectionActionParams() {
        with (RechargeHomepageViewModel.Companion) {
            val sectionId = 1
            val actual = rechargeHomepageViewModel.createRechargeHomepageSectionActionParams(sectionId, "action", "2", "3")
            assertEquals(actual, mapOf(
                    PARAM_RECHARGE_HOMEPAGE_SECTION_ID to sectionId,
                    PARAM_RECHARGE_HOMEPAGE_SECTION_ACTION to "action:2:3"
            ))
        }
    }

}