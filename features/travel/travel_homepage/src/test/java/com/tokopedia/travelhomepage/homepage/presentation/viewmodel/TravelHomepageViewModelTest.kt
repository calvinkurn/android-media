package com.tokopedia.travelhomepage.homepage.presentation.viewmodel

import com.tokopedia.common.travel.domain.GetTravelCollectiveBannerUseCase
import com.tokopedia.common.travel.utils.TravelTestDispatcherProvider
import com.tokopedia.travelhomepage.homepage.InstantTaskExecutorRuleSpek
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageBannerModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageItemModel
import com.tokopedia.travelhomepage.homepage.presentation.DUMMY_BANNER
import com.tokopedia.travelhomepage.homepage.shouldBeEquals
import com.tokopedia.travelhomepage.homepage.usecase.GetEmptyViewModelsUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * @author by furqan on 04/02/2020
 */
class TravelHomepageViewModelTest : Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Create Travel Homepage View Model") {
        Scenario("Create Travel Homepage View Model with Initial List") {
            val viewModel = TravelHomepageViewModel(mockk(), GetEmptyViewModelsUseCase(), mockk(), mockk(), TravelTestDispatcherProvider())

            When("Create Travel Homepage and Create Initial Items") {
                viewModel.getIntialList(true)
            }

            Then("Verify initial item is 6 items with isLoadFromCloud true for shimmering") {
                viewModel.travelItemList.value!!.size shouldBeEquals 6

                viewModel.travelItemList.value!!.forEach {
                    it.isLoadFromCloud shouldBeEquals true
                }

                viewModel.isAllError.value!! shouldBeEquals false
            }
        }
    }

    Feature("Handle Fetch Banner") {
        Scenario("Fetch Banner Failed") {
            val bannerUseCase = mockk<GetTravelCollectiveBannerUseCase>()
            val viewModel = TravelHomepageViewModel(mockk(), GetEmptyViewModelsUseCase(), bannerUseCase, mockk(), TravelTestDispatcherProvider())

            Given("Banner UseCase throw Exception") {
                coEvery { bannerUseCase.execute(any(), any(), any()) } coAnswers { Fail(Throwable()) }
            }

            When("Build Initial Item List") {
                viewModel.getIntialList(true)
            }

            When("Fetch Banner") {
                viewModel.getBanner("", true)
            }

            Then("Banner Data should be Loaded but Unsuccessful") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.BANNER_ORDER].isLoaded shouldBeEquals true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.BANNER_ORDER].isSuccess shouldBeEquals false
            }
        }

        Scenario("Fetch Banner Success") {
            val bannerUseCase = mockk<GetTravelCollectiveBannerUseCase>()
            val viewModel = TravelHomepageViewModel(mockk(), GetEmptyViewModelsUseCase(), bannerUseCase, mockk(), TravelTestDispatcherProvider())
            val dummyData = DUMMY_BANNER

            Given("Banner UseCase success") {
                coEvery { bannerUseCase.execute(any(), any(), any()) } coAnswers { Success(dummyData) }
            }

            When("Build Initial Item List") {
                viewModel.getIntialList(true)
            }

            When("Fetch Banner") {
                viewModel.getBanner("", true)
            }

            Then("Banner Data should be Successfully Loaded") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.BANNER_ORDER].isLoaded shouldBeEquals true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.BANNER_ORDER].isSuccess shouldBeEquals true
            }

            Then("Banner Data should be the same as response") {
                val bannerData = ((viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.BANNER_ORDER] as TravelHomepageBannerModel).travelCollectiveBannerModel
                for ((index, item) in bannerData.banners.withIndex()) {
                    item.id shouldBeEquals dummyData.banners[index].id
                    item.product shouldBeEquals dummyData.banners[index].product
                    item.attribute.webUrl shouldBeEquals dummyData.banners[index].attribute.webUrl
                    item.attribute.appUrl shouldBeEquals dummyData.banners[index].attribute.appUrl
                    item.attribute.description shouldBeEquals dummyData.banners[index].attribute.description
                    item.attribute.imageUrl shouldBeEquals dummyData.banners[index].attribute.imageUrl
                    item.attribute.promoCode shouldBeEquals dummyData.banners[index].attribute.promoCode
                }
            }
        }
    }
})