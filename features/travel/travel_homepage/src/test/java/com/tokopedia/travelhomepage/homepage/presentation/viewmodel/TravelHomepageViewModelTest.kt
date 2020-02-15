package com.tokopedia.travelhomepage.homepage.presentation.viewmodel

import com.tokopedia.common.travel.domain.GetTravelCollectiveBannerUseCase
import com.tokopedia.common.travel.utils.TravelTestDispatcherProvider
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.travelhomepage.homepage.InstantTaskExecutorRuleSpek
import com.tokopedia.travelhomepage.homepage.data.*
import com.tokopedia.travelhomepage.homepage.presentation.DUMMY_BANNER
import com.tokopedia.travelhomepage.homepage.presentation.DUMMY_CATEGORIES
import com.tokopedia.travelhomepage.homepage.presentation.DUMMY_ORDER_LIST
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment
import com.tokopedia.travelhomepage.homepage.shouldBeEquals
import com.tokopedia.travelhomepage.homepage.usecase.GetEmptyModelsUseCase
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
            val viewModel = TravelHomepageViewModel(mockk(), GetEmptyModelsUseCase(), mockk(), mockk(), TravelTestDispatcherProvider())

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
            val viewModel = TravelHomepageViewModel(mockk(), GetEmptyModelsUseCase(), bannerUseCase, mockk(), TravelTestDispatcherProvider())

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
            val viewModel = TravelHomepageViewModel(mockk(), GetEmptyModelsUseCase(), bannerUseCase, mockk(), TravelTestDispatcherProvider())
            val dummyData = DUMMY_BANNER

            Given("Banner UseCase return dummy response") {
                coEvery { bannerUseCase.execute(any(), any(), any()) } returns Success(dummyData)
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

            Then("Banner Meta should be the same as response") {
                val bannerMetaData = ((viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.BANNER_ORDER] as TravelHomepageBannerModel).travelCollectiveBannerModel.meta
                bannerMetaData.webUrl shouldBeEquals dummyData.meta.webUrl
                bannerMetaData.appUrl shouldBeEquals dummyData.meta.appUrl
                bannerMetaData.title shouldBeEquals dummyData.meta.title
                bannerMetaData.type shouldBeEquals dummyData.meta.type
            }
        }
    }

    Feature("Handle Fetch Categories") {
        Scenario("Fetch Categories Failed") {
            val graphqlRepository = mockk<GraphqlRepository>()
            val viewModel = TravelHomepageViewModel(graphqlRepository, GetEmptyModelsUseCase(), mockk(), mockk(), TravelTestDispatcherProvider())

            Given("Fetch Categories throw Exception") {
                coEvery { graphqlRepository.getReseponse(any(), any()) } coAnswers { throw Throwable() }
            }

            When("Build Initial Item List") {
                viewModel.getIntialList(true)
            }

            When("Fetch Categories") {
                viewModel.getCategories("", true)
            }

            Then("Categories Data should be Loaded but Unsuccessful") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.CATEGORIES_ORDER].isLoaded shouldBeEquals true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.CATEGORIES_ORDER].isSuccess shouldBeEquals false
            }
        }

        Scenario("Fetch Categories Success") {
            val graphqlRepository = mockk<GraphqlRepository>()
            val dummyData = DUMMY_CATEGORIES
            val graphqlSuccessResponse = GraphqlResponse(
                    mapOf(TravelHomepageCategoryListModel.Response::class.java to TravelHomepageCategoryListModel.Response(dummyData)),
                    mapOf(),
                    false
            )
            val viewModel = TravelHomepageViewModel(graphqlRepository, GetEmptyModelsUseCase(), mockk(), mockk(), TravelTestDispatcherProvider())

            Given("Fetch Categories return dummy response") {
                coEvery { graphqlRepository.getReseponse(any(), any()) } returns graphqlSuccessResponse
            }

            When("Build Initial Item List") {
                viewModel.getIntialList(true)
            }

            When("Fetch Categories") {
                viewModel.getCategories("", true)
            }

            Then("Categories Data should be Successfully Loaded") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.CATEGORIES_ORDER].isLoaded shouldBeEquals true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.CATEGORIES_ORDER].isSuccess shouldBeEquals true
            }

            Then("Category Data should be the same as response") {
                val categories = ((viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.CATEGORIES_ORDER] as TravelHomepageCategoryListModel).categories
                for ((index, item) in categories.withIndex()) {
                    item.product shouldBeEquals dummyData.categories[index].product
                    item.attributes.appUrl shouldBeEquals dummyData.categories[index].attributes.appUrl
                    item.attributes.imageUrl shouldBeEquals dummyData.categories[index].attributes.imageUrl
                    item.attributes.title shouldBeEquals dummyData.categories[index].attributes.title
                    item.attributes.webUrl shouldBeEquals dummyData.categories[index].attributes.webUrl
                }
            }
        }
    }

    Feature("Handle Fetch Order List") {
        Scenario("Fetch Order List") {
            val graphqlRepository = mockk<GraphqlRepository>()
            val viewModel = TravelHomepageViewModel(graphqlRepository, GetEmptyModelsUseCase(), mockk(), mockk(), TravelTestDispatcherProvider())

            Given("Fetch Order List throw Exception") {
                coEvery { graphqlRepository.getReseponse(any(), any()) } coAnswers { throw Throwable() }
            }

            When("Build Initial Item List") {
                viewModel.getIntialList(true)
            }

            When("Fetch Order List") {
                viewModel.getOrderList("", true)
            }

            Then("Order List Data should be Loaded but Unsuccessful") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER].isLoaded shouldBeEquals true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER].isSuccess shouldBeEquals false
            }
        }

        Scenario("Fetch Order List Success") {
            val graphqlRepository = mockk<GraphqlRepository>()
            val dummyData = DUMMY_ORDER_LIST
            val graphqlSuccessResponse = GraphqlResponse(
                    mapOf(TravelHomepageOrderListModel.Response::class.java to TravelHomepageOrderListModel.Response(dummyData)),
                    mapOf(),
                    false
            )
            val viewModel = TravelHomepageViewModel(graphqlRepository, GetEmptyModelsUseCase(), mockk(), mockk(), TravelTestDispatcherProvider())

            Given("Fetch Order List return dummy response") {
                coEvery { graphqlRepository.getReseponse(any(), any()) } returns graphqlSuccessResponse
            }

            When("Build Initial Item List") {
                viewModel.getIntialList(true)
            }

            When("Fetch Order List") {
                viewModel.getOrderList("", true)
            }

            Then("Order List Data should be Successfully Loaded") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER].isLoaded shouldBeEquals true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER].isSuccess shouldBeEquals true
            }

            Then("Order List Data mapper should map response correctly") {
                val orderList = ((viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER] as TravelHomepageSectionModel)
                orderList.title shouldBeEquals dummyData.travelMeta.title
                orderList.type shouldBeEquals TravelHomepageFragment.TYPE_ORDER_LIST
                orderList.seeAllUrl shouldBeEquals dummyData.travelMeta.appUrl

                for ((index, item) in orderList.list.withIndex()) {
                    item.title shouldBeEquals dummyData.orders[index].title
                    item.subtitle shouldBeEquals dummyData.orders[index].subtitle
                    item.prefix shouldBeEquals dummyData.orders[index].prefix
                    item.value shouldBeEquals dummyData.orders[index].value
                    item.appUrl shouldBeEquals dummyData.orders[index].appUrl
                    item.imageUrl shouldBeEquals dummyData.orders[index].imageUrl
                    item.product shouldBeEquals dummyData.orders[index].product
                }
            }
        }
    }
})