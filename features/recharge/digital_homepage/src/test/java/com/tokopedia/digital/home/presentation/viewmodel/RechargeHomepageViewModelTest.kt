package com.tokopedia.digital.home.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.digital.home.RechargeHomepageTestDispatchersProvider
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase
import com.tokopedia.digital.home.model.*
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
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
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

    @RelaxedMockK
    lateinit var digitalHomePageUseCase: DigitalHomePageUseCase

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

        digitalHomePageViewModel = RechargeHomepageViewModel(
                digitalHomePageUseCase,
                graphqlRepository,
                RechargeHomepageTestDispatchersProvider()
        )
    }

    @Test
    fun initialize() {
        coEvery { digitalHomePageUseCase.getEmptyList() } returns listOf(DigitalHomePageCategoryModel())

        digitalHomePageViewModel.initialize(mapParams)
        val actualData = digitalHomePageViewModel.digitalHomePageList.value
        assertNotNull(actualData)
        actualData?.run {
            assert(actualData.isNotEmpty())
            assert(actualData[0] is DigitalHomePageCategoryModel)
        }
        val actualErrorData = digitalHomePageViewModel.isAllError.value
        assertNotNull(actualErrorData)
        actualErrorData?.run { Assert.assertFalse(actualErrorData) }
    }

    @Test
    fun getData_Success_All() {
        val bannerData = DigitalHomePageBannerModel(listOf(DigitalHomePageBannerModel.Banner(1)))
        val categoryData = DigitalHomePageCategoryModel(
                listOf(DigitalHomePageCategoryModel.Subtitle("Prabayar & Pascabayar"))
        )
        bannerData.isSuccess = true
        categoryData.isSuccess = true
        coEvery { digitalHomePageUseCase.executeOnBackground() } returns listOf(bannerData, categoryData)

        digitalHomePageViewModel.getData(true)
        val actualData = digitalHomePageViewModel.digitalHomePageList.value
        assertNotNull(actualData)
        actualData?.run {
            assert(actualData.isNotEmpty())

            assert(actualData[0] is DigitalHomePageBannerModel)
            val actualBannerData = actualData[0] as DigitalHomePageBannerModel
            assertEquals(actualBannerData.isSuccess, true)
            assert(actualBannerData.bannerList.isNotEmpty())
            assertEquals(actualBannerData.bannerList[0].id, 1)

            assert(actualData[1] is DigitalHomePageCategoryModel)
            val actualCategoryData = actualData[1] as DigitalHomePageCategoryModel
            assertEquals(actualCategoryData.isSuccess, true)
            assert(actualCategoryData.listSubtitle.isNotEmpty())
            assertEquals(actualCategoryData.listSubtitle[0].name, "Prabayar & Pascabayar")
        }
    }

    @Test
    fun getData_Success_Partial() {
        val bannerData = DigitalHomePageBannerModel(listOf(DigitalHomePageBannerModel.Banner(1)))
        val failedCategoryData = DigitalHomePageCategoryModel()
        bannerData.isSuccess = true
        failedCategoryData.isSuccess = false
        coEvery { digitalHomePageUseCase.executeOnBackground() } returns listOf(bannerData, failedCategoryData)

        digitalHomePageViewModel.getData(true)
        val actualData = digitalHomePageViewModel.digitalHomePageList.value
        assertNotNull(actualData)
        actualData?.run {
            assert(actualData.isNotEmpty())

            assert(actualData[0] is DigitalHomePageBannerModel)
            val actualBannerData = actualData[0] as DigitalHomePageBannerModel
            assertEquals(actualBannerData.isSuccess, true)
            assert(actualBannerData.bannerList.isNotEmpty())
            assertEquals(actualBannerData.bannerList[0].id, 1)

            assert(actualData[1] is DigitalHomePageCategoryModel)
            assertEquals(actualData[1].isSuccess, false)
        }
    }

    @Test
    fun getData_Fail_Query() {
        coEvery { digitalHomePageUseCase.executeOnBackground() } returns listOf()

        digitalHomePageViewModel.getData(true)
        val actualErrorData = digitalHomePageViewModel.isAllError.value
        assertNotNull(actualErrorData)
        actualErrorData?.run { assert(actualErrorData) }
    }

    @Test
    fun getData_Fail_Response() {
        val failedBannerData = DigitalHomePageBannerModel()
        failedBannerData.isSuccess = false
        val failedCategoryData = DigitalHomePageCategoryModel()
        failedCategoryData.isSuccess = false
        coEvery { digitalHomePageUseCase.executeOnBackground() } returns listOf(
                failedBannerData, failedCategoryData
        )

        digitalHomePageViewModel.getData(true)
        val actualErrorData = digitalHomePageViewModel.isAllError.value
        assertNotNull(actualErrorData)
        actualErrorData?.run { assert(actualErrorData) }
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

        digitalHomePageViewModel.getRechargeHomepageSectionSkeleton(mapParams)
        val expectedData = RechargeHomepageSectionMapper.mapInitialHomepageSections(sectionSkeletonItem)
        val actualData = digitalHomePageViewModel.rechargeHomepageSections.value
        assertNotNull(actualData)
        actualData?.run {
            assertEquals(actualData, expectedData)
        }
    }

    @Test
    fun getRechargeHomepageSectionSkeleton_Fail() {
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseFail

        digitalHomePageViewModel.getRechargeHomepageSectionSkeleton(mapParams)
        val actualData = digitalHomePageViewModel.rechargeHomepageSectionSkeleton.value
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

        digitalHomePageViewModel.getRechargeHomepageSectionSkeleton(mapParams)
        val expectedData = RechargeHomepageSectionMapper.mapInitialHomepageSections(sectionSkeletonItem)
        val actualData = digitalHomePageViewModel.rechargeHomepageSections.value
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

        digitalHomePageViewModel.getRechargeHomepageSections(
                digitalHomePageViewModel.createRechargeHomepageSectionsParams(31, listOf(1))
        )
        val sections = digitalHomePageViewModel.rechargeHomepageSections.value
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

        digitalHomePageViewModel.getRechargeHomepageSections(mapParams)
        val sections = digitalHomePageViewModel.rechargeHomepageSections.value
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

        digitalHomePageViewModel.triggerRechargeSectionAction(mapParams)
        val actualData = digitalHomePageViewModel.rechargeHomepageSectionAction.value
        assert(actualData is Success)
    }

    @Test
    fun triggerRechargeSectionAction_Fail() {
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseFail

        digitalHomePageViewModel.triggerRechargeSectionAction(mapParams)
        val actualData = digitalHomePageViewModel.rechargeHomepageSectionAction.value
        assert(actualData is Fail)
    }

    @Test
    fun createRechargeHomepageSectionSkeletonParams() {
        with (DigitalHomePageViewModel.Companion) {
            val enablePersonalize = true
            val actual = digitalHomePageViewModel.createRechargeHomepageSectionSkeletonParams(31, true)
            assertEquals(actual, mapOf(
                    PARAM_RECHARGE_HOMEPAGE_SECTIONS_PLATFORM_ID to 31,
                    PARAM_RECHARGE_HOMEPAGE_SECTIONS_PERSONALIZE to enablePersonalize
            ))
        }
    }

    @Test
    fun createRechargeHomepageSectionSkeletonParams_Default() {
        with (DigitalHomePageViewModel.Companion) {
            val actual = digitalHomePageViewModel.createRechargeHomepageSectionSkeletonParams(31)
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
            val actual = digitalHomePageViewModel.createRechargeHomepageSectionsParams(31, listOf(1), enablePersonalize)
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
            val actual = digitalHomePageViewModel.createRechargeHomepageSectionsParams(31, listOf(1))
            assertEquals(actual, mapOf(
                    PARAM_RECHARGE_HOMEPAGE_SECTIONS_PLATFORM_ID to 31,
                    PARAM_RECHARGE_HOMEPAGE_SECTIONS_SECTION_IDS to listOf(1),
                    PARAM_RECHARGE_HOMEPAGE_SECTIONS_PERSONALIZE to false
            ))
        }
    }

    @Test
    fun createRechargeHomepageSectionActionParams() {
        with (DigitalHomePageViewModel.Companion) {
            val sectionId = 1
            val actual = digitalHomePageViewModel.createRechargeHomepageSectionActionParams(sectionId, "action", "2", "3")
            assertEquals(actual, mapOf(
                    PARAM_RECHARGE_HOMEPAGE_SECTION__ID to sectionId,
                    PARAM_RECHARGE_HOMEPAGE_SECTION_ACTION to "action:2:3"
            ))
        }
    }

}