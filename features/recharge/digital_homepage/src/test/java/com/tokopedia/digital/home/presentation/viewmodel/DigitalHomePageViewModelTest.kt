package com.tokopedia.digital.home.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.digital.home.DigitalHomePageTestDispatchersProvider
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase
import com.tokopedia.digital.home.model.DigitalHomePageBannerModel
import com.tokopedia.digital.home.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.model.DigitalHomePageItemModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DigitalHomePageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mapParams = mapOf<String, String>()

    @RelaxedMockK
    lateinit var digitalHomePageUseCase: DigitalHomePageUseCase

    lateinit var digitalHomePageViewModel: DigitalHomePageViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        digitalHomePageViewModel =
                DigitalHomePageViewModel(digitalHomePageUseCase, DigitalHomePageTestDispatchersProvider())
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
        actualErrorData?.run { assertFalse(actualErrorData) }
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
}