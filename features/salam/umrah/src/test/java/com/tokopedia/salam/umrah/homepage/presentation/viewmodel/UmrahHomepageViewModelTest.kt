package com.tokopedia.salam.umrah.homepage.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.salam.umrah.common.data.UmrahSearchParameterEntity
import com.tokopedia.salam.umrah.common.data.UmrahTravelAgentsEntity
import com.tokopedia.salam.umrah.common.usecase.UmrahSearchParameterUseCase
import com.tokopedia.salam.umrah.common.usecase.UmrahTravelAgentsUseCase
import com.tokopedia.salam.umrah.homepage.data.*
import com.tokopedia.salam.umrah.homepage.presentation.usecase.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UmrahHomepageViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var umrahHomepageEmptyDataUseCase: UmrahHomepageEmptyDataUseCase
    @RelaxedMockK
    lateinit var umrahCategoriesFeaturedUseCase: UmrahHomepageCategoryFeaturedUseCase
    @RelaxedMockK
    lateinit var umrahCategoriesUseCase: UmrahHomepageCategoryUseCase
    @RelaxedMockK
    lateinit var umrahSearchParamUseCase: UmrahSearchParameterUseCase
    @RelaxedMockK
    lateinit var umrahHomepageMyUmrahUseCase: UmrahHomepageMyUmrahUseCase
    @RelaxedMockK
    lateinit var umrahHomepageBannerUseCase: UmrahHomepageBannerUseCase
    @RelaxedMockK
    lateinit var umrahTravelAgentsUseCase: UmrahTravelAgentsUseCase

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var umrahHomepageViewModel: UmrahHomepageViewModel
    val flags = listOf("TRAVEL_AGENT_FEATURED_ON_HOMEPAGE")

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        umrahHomepageViewModel = UmrahHomepageViewModel(umrahHomepageEmptyDataUseCase,
                umrahCategoriesFeaturedUseCase,
                umrahCategoriesUseCase,
                umrahSearchParamUseCase,
                umrahHomepageMyUmrahUseCase,
                umrahHomepageBannerUseCase,
                umrahTravelAgentsUseCase,
                dispatcher)
    }

    @Test
    fun `responseUmrahData_EmptyHomepageData_ShowEmptyHomepage`(){
        //given

        umrahHomepageViewModel.getIntialList(true)

        val actual = umrahHomepageViewModel.homePageModel.value
        val isError = umrahHomepageViewModel.isError.value
        assert(actual!!.isNotEmpty())
        assert(isError==false)
    }


    @Test
    fun `responseUmrahData_SuccessFetchSearchParam_ShouldSuccessSearchParam`(){
        //give
        coEvery {
            umrahSearchParamUseCase.executeUseCase(any(), true)
        } returns Success(UmrahSearchParameterEntity())
        umrahHomepageViewModel.getIntialList(true)

        //when
        umrahHomepageViewModel.getSearchParamData("",true)

        //then
        assert((umrahHomepageViewModel.homePageModel.value as List<UmrahHomepageModel>)[UmrahHomepageViewModel.SEARCH_PARAM_ORDER].isLoaded)
        assert((umrahHomepageViewModel.homePageModel.value as List<UmrahHomepageModel>)[UmrahHomepageViewModel.SEARCH_PARAM_ORDER].isSuccess)
    }


    @Test
    fun `responseUmrahData_FailFetchSearchParam_ShouldFailSearchParam`(){
        //give
        coEvery {
            umrahSearchParamUseCase.executeUseCase(any(), true)
        } returns Fail(Throwable())
        umrahHomepageViewModel.getIntialList(true)

        //when
        umrahHomepageViewModel.getSearchParamData("",true)

        //then
        assert((umrahHomepageViewModel.homePageModel.value as List<UmrahHomepageModel>)[UmrahHomepageViewModel.SEARCH_PARAM_ORDER].isLoaded)
        assert(!(umrahHomepageViewModel.homePageModel.value as List<UmrahHomepageModel>)[UmrahHomepageViewModel.SEARCH_PARAM_ORDER].isSuccess)
    }

    @Test
    fun `responseUmrahData_SuccessFetchUmrahSaya_ShouldSuccessUmrahSaya`(){
        //give
        coEvery {
            umrahHomepageMyUmrahUseCase.executeMyUmrah(any(), true)
        } returns Success(UmrahHomepageMyUmrahEntity())
        umrahHomepageViewModel.getIntialList(true)

        //when
        umrahHomepageViewModel.getUmrahSayaData("",true)

        //then
        assert((umrahHomepageViewModel.homePageModel.value as List<UmrahHomepageModel>)[UmrahHomepageViewModel.DREAM_FUND_ORDER].isLoaded)
        assert((umrahHomepageViewModel.homePageModel.value as List<UmrahHomepageModel>)[UmrahHomepageViewModel.DREAM_FUND_ORDER].isSuccess)
    }


    @Test
    fun `responseUmrahData_FailFetchUmrahSaya_ShouldFailUmrahSaya`(){
        //give
        coEvery {
            umrahHomepageMyUmrahUseCase.executeMyUmrah(any(), true)
        } returns Fail(Throwable())
        umrahHomepageViewModel.getIntialList(true)

        //when
        umrahHomepageViewModel.getUmrahSayaData("",true)

        //then
        assert((umrahHomepageViewModel.homePageModel.value as List<UmrahHomepageModel>)[UmrahHomepageViewModel.DREAM_FUND_ORDER].isLoaded)
        assert(!(umrahHomepageViewModel.homePageModel.value as List<UmrahHomepageModel>)[UmrahHomepageViewModel.DREAM_FUND_ORDER].isSuccess)
    }

    @Test
    fun `responseUmrahData_SuccessFetchCategory_ShouldSuccessCategory`(){
        //give
        coEvery {
            umrahCategoriesUseCase.executeCategory(any(), true)
        } returns Success(UmrahHomepageCategoryEntity())
        umrahHomepageViewModel.getIntialList(true)

        //when
        umrahHomepageViewModel.getCategoryData("",true)

        //then
        assert((umrahHomepageViewModel.homePageModel.value as List<UmrahHomepageModel>)[UmrahHomepageViewModel.CATEGORY_ORDER].isLoaded)
        assert((umrahHomepageViewModel.homePageModel.value as List<UmrahHomepageModel>)[UmrahHomepageViewModel.CATEGORY_ORDER].isSuccess)
    }


    @Test
    fun `responseUmrahData_FailFetchCategory_ShouldFailCategory`(){
        //give
        coEvery {
            umrahCategoriesUseCase.executeCategory(any(), true)
        } returns Fail(Throwable())
        umrahHomepageViewModel.getIntialList(true)

        //when
        umrahHomepageViewModel.getCategoryData("",true)

        //then
        assert((umrahHomepageViewModel.homePageModel.value as List<UmrahHomepageModel>)[UmrahHomepageViewModel.CATEGORY_ORDER].isLoaded)
        assert(!(umrahHomepageViewModel.homePageModel.value as List<UmrahHomepageModel>)[UmrahHomepageViewModel.CATEGORY_ORDER].isSuccess)
    }

    @Test
    fun `responseUmrahData_SuccessFetchCategoryFeatured_ShouldSuccessCategoryFeatured`(){
        //give
        coEvery {
            umrahCategoriesFeaturedUseCase.executeCategoryFeatured(any(), true)
        } returns Success(UmrahHomepageCategoryFeaturedEntity())
        umrahHomepageViewModel.getIntialList(true)

        //when
        umrahHomepageViewModel.getCategoryFeaturedData("",true)

        //then
        assert((umrahHomepageViewModel.homePageModel.value as List<UmrahHomepageModel>)[UmrahHomepageViewModel.CATEGORY_FEATURED_ORDER].isLoaded)
        assert((umrahHomepageViewModel.homePageModel.value as List<UmrahHomepageModel>)[UmrahHomepageViewModel.CATEGORY_FEATURED_ORDER].isSuccess)
    }


    @Test
    fun `responseUmrahData_FailFetchCategoryFeatured_ShouldFailCategoryFeatured`(){
        //give
        coEvery {
            umrahCategoriesFeaturedUseCase.executeCategoryFeatured(any(), true)
        } returns Fail(Throwable())
        umrahHomepageViewModel.getIntialList(true)

        //when
        umrahHomepageViewModel.getCategoryFeaturedData("",true)

        //then
        assert((umrahHomepageViewModel.homePageModel.value as List<UmrahHomepageModel>)[UmrahHomepageViewModel.CATEGORY_FEATURED_ORDER].isLoaded)
        assert(!(umrahHomepageViewModel.homePageModel.value as List<UmrahHomepageModel>)[UmrahHomepageViewModel.CATEGORY_FEATURED_ORDER].isSuccess)
    }


    @Test
    fun `responseUmrahData_SuccessFetchBanner_ShouldSuccessBanner`(){
        //give
        coEvery {
            umrahHomepageBannerUseCase.executeBanner(any(), true)
        } returns Success(UmrahHomepageBannerEntity())
        umrahHomepageViewModel.getIntialList(true)

        //when
        umrahHomepageViewModel.getBannerData("",true)

        //then
        assert((umrahHomepageViewModel.homePageModel.value as List<UmrahHomepageModel>)[UmrahHomepageViewModel.BANNER_ORDER].isLoaded)
        assert((umrahHomepageViewModel.homePageModel.value as List<UmrahHomepageModel>)[UmrahHomepageViewModel.BANNER_ORDER].isSuccess)
    }


    @Test
    fun `responseUmrahData_FailFetchBanner_ShouldFailBanner`(){
        //give
        coEvery {
            umrahHomepageBannerUseCase.executeBanner(any(), true)
        } returns Fail(Throwable())
        umrahHomepageViewModel.getIntialList(true)

        //when
        umrahHomepageViewModel.getBannerData("",true)

        //then
        assert((umrahHomepageViewModel.homePageModel.value as List<UmrahHomepageModel>)[UmrahHomepageViewModel.BANNER_ORDER].isLoaded)
        assert(!(umrahHomepageViewModel.homePageModel.value as List<UmrahHomepageModel>)[UmrahHomepageViewModel.BANNER_ORDER].isSuccess)
    }

    @Test
    fun `responseUmrahData_SuccessFetchTravel_ShouldSuccessTravel`(){
        //give
        coEvery {
            umrahTravelAgentsUseCase.executeUseCase(any(), true,1,6, flags)
        } returns Success(UmrahTravelAgentsEntity())
        umrahHomepageViewModel.getIntialList(true)

        //when
        umrahHomepageViewModel.getPartnerTravelData("",true)

        //then
        assert((umrahHomepageViewModel.homePageModel.value as List<UmrahHomepageModel>)[UmrahHomepageViewModel.PARTNER_TRAVEL_ORDER].isLoaded)
        assert((umrahHomepageViewModel.homePageModel.value as List<UmrahHomepageModel>)[UmrahHomepageViewModel.PARTNER_TRAVEL_ORDER].isSuccess)
    }


    @Test
    fun `responseUmrahData_FailFetchTravel_ShouldFailTravel`(){
        //give
        coEvery {
            umrahTravelAgentsUseCase.executeUseCase(any(), true,1,6, flags)
        } returns Fail(Throwable())
        umrahHomepageViewModel.getIntialList(true)

        //when
        umrahHomepageViewModel.getPartnerTravelData("",true)

        //then
        assert((umrahHomepageViewModel.homePageModel.value as List<UmrahHomepageModel>)[UmrahHomepageViewModel.PARTNER_TRAVEL_ORDER].isLoaded)
        assert(!(umrahHomepageViewModel.homePageModel.value as List<UmrahHomepageModel>)[UmrahHomepageViewModel.PARTNER_TRAVEL_ORDER].isSuccess)
    }

}