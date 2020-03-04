package com.tokopedia.digital.home.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase
import com.tokopedia.digital.home.model.DigitalHomePageBannerModel
import com.tokopedia.digital.home.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.model.DigitalHomePageItemModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
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
                DigitalHomePageViewModel(Dispatchers.Unconfined, digitalHomePageUseCase)
    }

    @Test
    fun initialize() {
        coEvery { digitalHomePageUseCase.getEmptyList() } returns listOf(DigitalHomePageCategoryModel())

        val dataObserver = Observer<List<DigitalHomePageItemModel>> {
            assert(it.isNotEmpty())
            assert(it[0] is DigitalHomePageCategoryModel)
        }

        val errorObserver = Observer<Boolean> {
            assertEquals(it, false)
        }

        try {
            digitalHomePageViewModel.digitalHomePageList.observeForever(dataObserver)
            digitalHomePageViewModel.isAllError.observeForever(errorObserver)
            digitalHomePageViewModel.initialize(mapParams)
        } finally {
            digitalHomePageViewModel.digitalHomePageList.removeObserver(dataObserver)
            digitalHomePageViewModel.isAllError.removeObserver(errorObserver)
        }
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

        val observer = Observer<List<DigitalHomePageItemModel>> {
            assert(it.isNotEmpty())

            assert(it[0] is DigitalHomePageBannerModel)
            val actualBannerData = it[0] as DigitalHomePageBannerModel
            assertEquals(actualBannerData.isSuccess, true)
            assert(actualBannerData.bannerList.isNotEmpty())
            assertEquals(actualBannerData.bannerList[0].id, 1)

            assert(it[1] is DigitalHomePageCategoryModel)
            val actualCategoryData = it[1] as DigitalHomePageCategoryModel
            assertEquals(actualCategoryData.isSuccess, true)
            assert(actualCategoryData.listSubtitle.isNotEmpty())
            assertEquals(actualCategoryData.listSubtitle[0].name, "Prabayar & Pascabayar")
        }

        try {
            digitalHomePageViewModel.digitalHomePageList.observeForever(observer)
            digitalHomePageViewModel.getData(true)
        } finally {
            digitalHomePageViewModel.digitalHomePageList.removeObserver(observer)
        }
    }

    @Test
    fun getData_Success_Partial() {
        val bannerData = DigitalHomePageBannerModel(listOf(DigitalHomePageBannerModel.Banner(1)))
        val failedCategoryData = DigitalHomePageCategoryModel()
        bannerData.isSuccess = true
        failedCategoryData.isSuccess = false
        coEvery { digitalHomePageUseCase.executeOnBackground() } returns listOf(bannerData, failedCategoryData)

        val observer = Observer<List<DigitalHomePageItemModel>> {
            assert(it.isNotEmpty())

            assert(it[0] is DigitalHomePageBannerModel)
            val actualBannerData = it[0] as DigitalHomePageBannerModel
            assertEquals(actualBannerData.isSuccess, true)
            assert(actualBannerData.bannerList.isNotEmpty())
            assertEquals(actualBannerData.bannerList[0].id, 1)

            assert(it[1] is DigitalHomePageCategoryModel)
            assertEquals((it as DigitalHomePageItemModel).isSuccess, false)
        }

        try {
            digitalHomePageViewModel.digitalHomePageList.observeForever(observer)
            digitalHomePageViewModel.getData(true)
        } finally {
            digitalHomePageViewModel.digitalHomePageList.removeObserver(observer)
        }
    }

    @Test
    fun getData_Fail_Query() {
        coEvery { digitalHomePageUseCase.executeOnBackground() } returns listOf()

        val observer = Observer<Boolean> {
            assertEquals(it, true)
        }

        try {
            digitalHomePageViewModel.isAllError.observeForever(observer)
            digitalHomePageViewModel.getData(true)
        } finally {
            digitalHomePageViewModel.isAllError.removeObserver(observer)
        }
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

        val observer = Observer<Boolean> {
            assertEquals(it, true)
        }

        try {
            digitalHomePageViewModel.isAllError.observeForever(observer)
            digitalHomePageViewModel.getData(true)
        } finally {
            digitalHomePageViewModel.isAllError.removeObserver(observer)
        }
    }
}