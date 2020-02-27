package com.tokopedia.travelhomepage.homepage.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.travel.domain.GetTravelCollectiveBannerUseCase
import com.tokopedia.common.travel.utils.TravelTestDispatcherProvider
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageBannerModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageCategoryListModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageItemModel
import com.tokopedia.travelhomepage.homepage.presentation.DUMMY_BANNER
import com.tokopedia.travelhomepage.homepage.presentation.DUMMY_CATEGORIES
import com.tokopedia.travelhomepage.homepage.usecase.GetEmptyModelsUseCase
import com.tokopedia.travelhomepage.shouldBe
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * @author by furqan on 04/02/2020
 */

class TravelHomepageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val graphqlRepository = mockk<GraphqlRepository>()
    private val emptyUseCase = GetEmptyModelsUseCase()
    private val bannerUseCase = mockk<GetTravelCollectiveBannerUseCase>()

    private val travelDispatcherProvider = TravelTestDispatcherProvider()

    private lateinit var viewModel: TravelHomepageViewModel

    @Before
    fun setup() {
        viewModel = TravelHomepageViewModel(graphqlRepository, emptyUseCase, bannerUseCase, mockk(), travelDispatcherProvider)
    }

    @Test
    fun onGetInitialList_WithInitialList_ValueShouldBeInitialValue() {
        // given
        val viewModel = TravelHomepageViewModel(mockk(), emptyUseCase, mockk(), mockk(), travelDispatcherProvider)

        // when
        viewModel.getIntialList(true)

        // then
        viewModel.travelItemList.value!!.size shouldBe 6
        viewModel.travelItemList.value!!.forEach {
            it.isLoaded shouldBe false
            it.isLoadFromCloud shouldBe true
        }

        viewModel.isAllError.value shouldBe false
    }

    @Test
    fun onCreateViewModel_WithoutInitialList_ValueShouldBeNull() {
        //given

        // when
        val viewModel = TravelHomepageViewModel(mockk(), mockk(), mockk(), mockk(), travelDispatcherProvider)

        // then
        viewModel.travelItemList.value shouldBe null
        viewModel.isAllError.value shouldBe null
    }

    @Test
    fun onGetBanner_FailedToFetchBanner_BannerValueShouldBeRepresentFailedToFetch() {
        // given
        coEvery { bannerUseCase.execute(any(), any(), any()) } coAnswers { Fail(Throwable()) }
        viewModel.getIntialList(true)

        // when
        viewModel.getBanner("", true)

        // then
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.BANNER_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.BANNER_ORDER].isSuccess shouldBe false
    }

    @Test
    fun onGetBanner_SuccessToFetchBanner_BannerValueShouldBeSameAsDummyData() {
        // given
        coEvery { bannerUseCase.execute(any(), any(), any()) } returns Success(DUMMY_BANNER)
        viewModel.getIntialList(true)

        // when
        viewModel.getBanner("", true)

        // then
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.BANNER_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.BANNER_ORDER].isSuccess shouldBe true

        val bannerData = ((viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.BANNER_ORDER] as TravelHomepageBannerModel).travelCollectiveBannerModel
        for ((index, item) in bannerData.banners.withIndex()) {
            item.id shouldBe DUMMY_BANNER.banners[index].id
            item.product shouldBe DUMMY_BANNER.banners[index].product
            item.attribute.webUrl shouldBe DUMMY_BANNER.banners[index].attribute.webUrl
            item.attribute.appUrl shouldBe DUMMY_BANNER.banners[index].attribute.appUrl
            item.attribute.description shouldBe DUMMY_BANNER.banners[index].attribute.description
            item.attribute.imageUrl shouldBe DUMMY_BANNER.banners[index].attribute.imageUrl
            item.attribute.promoCode shouldBe DUMMY_BANNER.banners[index].attribute.promoCode
        }

        val bannerMetaData = ((viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.BANNER_ORDER] as TravelHomepageBannerModel).travelCollectiveBannerModel.meta
        bannerMetaData.webUrl shouldBe DUMMY_BANNER.meta.webUrl
        bannerMetaData.appUrl shouldBe DUMMY_BANNER.meta.appUrl
        bannerMetaData.title shouldBe DUMMY_BANNER.meta.title
        bannerMetaData.type shouldBe DUMMY_BANNER.meta.type

        viewModel.isAllError.value shouldBe false
    }

    @Test
    fun onGetCategories_FailedToFetchCategories_CategoryValueShouldBeRepresentFailedToFetch() {
        // given
        coEvery { graphqlRepository.getReseponse(any(), any()) } coAnswers { throw Throwable() }
        viewModel.getIntialList(true)

        // when
        viewModel.getCategories("", true)

        // then
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.CATEGORIES_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.CATEGORIES_ORDER].isSuccess shouldBe false
    }

    @Test
    fun onGetCategories_SuccessToFetchCategories_CategoryValueShouldBeSameAsDummyData() {
        // given
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf(TravelHomepageCategoryListModel.Response::class.java to TravelHomepageCategoryListModel.Response(DUMMY_CATEGORIES)),
                mapOf(),
                false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns graphqlSuccessResponse
        viewModel.getIntialList(true)

        // when
        viewModel.getCategories("", true)

        // then
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.CATEGORIES_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.CATEGORIES_ORDER].isSuccess shouldBe true

        val categories = ((viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.CATEGORIES_ORDER] as TravelHomepageCategoryListModel).categories
        for ((index, item) in categories.withIndex()) {
            item.product shouldBe DUMMY_CATEGORIES.categories[index].product
            item.attributes.appUrl shouldBe DUMMY_CATEGORIES.categories[index].attributes.appUrl
            item.attributes.imageUrl shouldBe DUMMY_CATEGORIES.categories[index].attributes.imageUrl
            item.attributes.title shouldBe DUMMY_CATEGORIES.categories[index].attributes.title
            item.attributes.webUrl shouldBe DUMMY_CATEGORIES.categories[index].attributes.webUrl
        }

        viewModel.isAllError.value shouldBe false
    }

    /*

     */
}
/*
    Feature("Handle Fetch Order List") {
        Scenario("Fetch Order List Failed") {
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
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER].isLoaded shouldBe true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER].isSuccess shouldBe false
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
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER].isLoaded shouldBe true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER].isSuccess shouldBe true
            }

            Then("Order List Data mapper should map response correctly") {
                val orderList = ((viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER] as TravelHomepageSectionModel)
                orderList.title shouldBe dummyData.travelMeta.title
                orderList.type shouldBe TravelHomepageFragment.TYPE_ORDER_LIST
                orderList.seeAllUrl shouldBe dummyData.travelMeta.appUrl

                for ((index, item) in orderList.list.withIndex()) {
                    item.title shouldBe dummyData.orders[index].title
                    item.subtitle shouldBe dummyData.orders[index].subtitle
                    item.prefix shouldBe dummyData.orders[index].prefix
                    item.value shouldBe dummyData.orders[index].value
                    item.appUrl shouldBe dummyData.orders[index].appUrl
                    item.imageUrl shouldBe dummyData.orders[index].imageUrl
                    item.product shouldBe dummyData.orders[index].product
                }
            }

            Then("isAllError should be false") {
                viewModel.isAllError.value shouldBe false
            }
        }
    }

    Feature("Handle Fetch Recent Search") {
        Scenario("Fetch Recent Search Failed") {
            val recentSearchUseCase = mockk<TravelRecentSearchUseCase>()
            val viewModel = TravelHomepageViewModel(mockk(), GetEmptyModelsUseCase(), mockk(), recentSearchUseCase, TravelTestDispatcherProvider())

            Given("Fetch Recent Search throw Exception") {
                coEvery { recentSearchUseCase.execute(any(), true) } coAnswers { throw Throwable() }
            }

            When("Build Initial Item List") {
                viewModel.getIntialList(true)
            }

            When("Fetch Recent Search") {
                viewModel.getRecentSearch("", true)
            }

            Then("Recent Search Data should be Loaded but Unsuccessful") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECENT_SEARCHES_ORDER].isLoaded shouldBe true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECENT_SEARCHES_ORDER].isSuccess shouldBe false
            }
        }

        Scenario("Fetch Recent Search Success") {
            val recentSearchUseCase = mockk<TravelRecentSearchUseCase>()
            val dummyData = DUMMY_RECENT_SEARCH

            val viewModel = TravelHomepageViewModel(mockk(), GetEmptyModelsUseCase(), mockk(), recentSearchUseCase, TravelTestDispatcherProvider())

            Given("Fetch Recent Search return dummy response") {
                coEvery { recentSearchUseCase.execute(any(), true) } returns dummyData
            }

            When("Build Initial Item List") {
                viewModel.getIntialList(true)
            }

            When("Fetch Recent Search") {
                viewModel.getRecentSearch("", true)
            }

            Then("Recent Search Data should be Successfully Loaded") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECENT_SEARCHES_ORDER].isLoaded shouldBe true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECENT_SEARCHES_ORDER].isSuccess shouldBe true
            }

            Then("Recent Search Data mapper should map response correctly") {
                val recentSearchList = ((viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECENT_SEARCHES_ORDER] as TravelHomepageSectionModel)
                recentSearchList.title shouldBe dummyData.travelMeta.title
                recentSearchList.type shouldBe TravelHomepageFragment.TYPE_RECENT_SEARCH
                recentSearchList.seeAllUrl shouldBe dummyData.travelMeta.appUrl

                for ((index, item) in recentSearchList.list.withIndex()) {
                    item.title shouldBe dummyData.items[index].title
                    item.subtitle shouldBe dummyData.items[index].subtitle
                    item.prefix shouldBe dummyData.items[index].prefix
                    item.value shouldBe dummyData.items[index].value
                    item.appUrl shouldBe dummyData.items[index].appUrl
                    item.imageUrl shouldBe dummyData.items[index].imageUrl
                    item.product shouldBe dummyData.items[index].product
                }
            }

            Then("isAllError should be false") {
                viewModel.isAllError.value shouldBe false
            }
        }
    }

    Feature("Handle Fetch Recommendation") {
        Scenario("Fetch Recommendation Failed") {
            val graphqlRepository = mockk<GraphqlRepository>()
            val viewModel = TravelHomepageViewModel(graphqlRepository, GetEmptyModelsUseCase(), mockk(), mockk(), TravelTestDispatcherProvider())

            Given("Fetch Recommendation throw Exception") {
                coEvery { graphqlRepository.getReseponse(any(), any()) } coAnswers { throw Throwable() }
            }

            When("Build Initial Item List") {
                viewModel.getIntialList(true)
            }

            When("Fetch Recommendation") {
                viewModel.getRecommendation("", true)
            }

            Then("Recommendation Data should be Loaded but Unsuccessful") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECOMMENDATION_ORDER].isLoaded shouldBe true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECOMMENDATION_ORDER].isSuccess shouldBe false
            }
        }

        Scenario("Fetch Recommendation Success") {
            val graphqlRepository = mockk<GraphqlRepository>()
            val dummyData = DUMMY_RECOMMENDATION
            val graphqlSuccessResponse = GraphqlResponse(
                    mapOf(TravelHomepageRecommendationModel.Response::class.java to TravelHomepageRecommendationModel.Response(dummyData)),
                    mapOf(),
                    false
            )
            val viewModel = TravelHomepageViewModel(graphqlRepository, GetEmptyModelsUseCase(), mockk(), mockk(), TravelTestDispatcherProvider())

            Given("Fetch Recommendation return dummy response") {
                coEvery { graphqlRepository.getReseponse(any(), any()) } returns graphqlSuccessResponse
            }

            When("Build Initial Item List") {
                viewModel.getIntialList(true)
            }

            When("Fetch Recommendation") {
                viewModel.getRecommendation("", true)
            }

            Then("Recommendation Data should be Successfully Loaded") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECOMMENDATION_ORDER].isLoaded shouldBe true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECOMMENDATION_ORDER].isSuccess shouldBe true
            }

            Then("Recommendation Data mapper should map response correctly") {
                val recommendationList = ((viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECOMMENDATION_ORDER] as TravelHomepageSectionModel)
                recommendationList.title shouldBe dummyData.travelMeta.title
                recommendationList.type shouldBe TravelHomepageFragment.TYPE_RECOMMENDATION
                recommendationList.seeAllUrl shouldBe dummyData.travelMeta.appUrl

                for ((index, item) in recommendationList.list.withIndex()) {
                    item.title shouldBe dummyData.items[index].title
                    item.subtitle shouldBe dummyData.items[index].subtitle
                    item.prefix shouldBe dummyData.items[index].prefix
                    item.value shouldBe dummyData.items[index].value
                    item.appUrl shouldBe dummyData.items[index].appUrl
                    item.imageUrl shouldBe dummyData.items[index].imageUrl
                    item.product shouldBe dummyData.items[index].product
                }
            }

            Then("isAllError should be false") {
                viewModel.isAllError.value shouldBe false
            }
        }
    }

    Feature("Handle Fetch Destination") {
        Scenario("Fetch Destination Failed") {
            val graphqlRepository = mockk<GraphqlRepository>()
            val viewModel = TravelHomepageViewModel(graphqlRepository, GetEmptyModelsUseCase(), mockk(), mockk(), TravelTestDispatcherProvider())

            Given("Fetch Destination throw Exception") {
                coEvery { graphqlRepository.getReseponse(any(), any()) } coAnswers { throw Throwable() }
            }

            When("Build Initial Item List") {
                viewModel.getIntialList(true)
            }

            When("Fetch Destination") {
                viewModel.getDestination("", true)
            }

            Then("Destination Data should be Loaded but Unsuccessful") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.DESTINATION_ORDER].isLoaded shouldBe true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.DESTINATION_ORDER].isSuccess shouldBe false
            }
        }

        Scenario("Fetch Destination Success") {
            val graphqlRepository = mockk<GraphqlRepository>()
            val dummyData = DUMMY_DESTINATION
            val graphqlSuccessResponse = GraphqlResponse(
                    mapOf(TravelHomepageDestinationModel.Response::class.java to TravelHomepageDestinationModel.Response(dummyData)),
                    mapOf(),
                    false
            )
            val viewModel = TravelHomepageViewModel(graphqlRepository, GetEmptyModelsUseCase(), mockk(), mockk(), TravelTestDispatcherProvider())

            Given("Fetch Destination return dummy response") {
                coEvery { graphqlRepository.getReseponse(any(), any()) } returns graphqlSuccessResponse
            }

            When("Build Initial Item List") {
                viewModel.getIntialList(true)
            }

            When("Fetch Destination") {
                viewModel.getDestination("", true)
            }

            Then("Destination Data should be Successfully Loaded") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.DESTINATION_ORDER].isLoaded shouldBe true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.DESTINATION_ORDER].isSuccess shouldBe true
            }

            Then("Destination Data should be the same as response data") {
                val destinationModel = ((viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.DESTINATION_ORDER] as TravelHomepageDestinationModel)
                destinationModel.meta.title shouldBe dummyData.meta.title

                for ((index, item) in destinationModel.destination.withIndex()) {
                    item.attributes.title shouldBe dummyData.destination[index].attributes.title
                    item.attributes.subtitle shouldBe dummyData.destination[index].attributes.subtitle
                    item.attributes.webUrl shouldBe dummyData.destination[index].attributes.webUrl
                    item.attributes.appUrl shouldBe dummyData.destination[index].attributes.appUrl
                    item.attributes.imageUrl shouldBe dummyData.destination[index].attributes.imageUrl
                }
            }

            Then("isAllError should be false") {
                viewModel.isAllError.value shouldBe false
            }
        }
    }

    Feature("Test All Error") {
        Scenario("All Response Error") {
            val graphqlRepository = mockk<GraphqlRepository>()
            val bannerUseCase = mockk<GetTravelCollectiveBannerUseCase>()
            val recentSearchUseCase = mockk<TravelRecentSearchUseCase>()
            val viewModel = TravelHomepageViewModel(graphqlRepository, GetEmptyModelsUseCase(), bannerUseCase, recentSearchUseCase, TravelTestDispatcherProvider())

            Given("Banner UseCase throw Exception") {
                coEvery { bannerUseCase.execute(any(), any(), any()) } coAnswers { Fail(Throwable()) }
            }

            Given("Recent Search UseCase throw Exception") {
                coEvery { recentSearchUseCase.execute(any(), true) } coAnswers { throw Throwable() }
            }

            Given("Graphql Repository throw Exception") {
                coEvery { graphqlRepository.getReseponse(any(), any()) } coAnswers { throw Throwable() }
            }

            When("Build Initial Item List") {
                viewModel.getIntialList(true)
            }

            When("Fetch Banner") {
                viewModel.getBanner("", true)
            }

            When("Fetch Categories") {
                viewModel.getCategories("", true)
            }

            When("Fetch Order List") {
                viewModel.getOrderList("", true)
            }

            When("Fetch Recent Search") {
                viewModel.getRecentSearch("", true)
            }

            When("Fetch Recommendation") {
                viewModel.getRecommendation("", true)
            }

            When("Fetch Destination") {
                viewModel.getDestination("", true)
            }

            Then("Banner Data should be Loaded but Unsuccessful") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.BANNER_ORDER].isLoaded shouldBe true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.BANNER_ORDER].isSuccess shouldBe false
            }

            Then("Categories Data should be Loaded but Unsuccessful") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.CATEGORIES_ORDER].isLoaded shouldBe true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.CATEGORIES_ORDER].isSuccess shouldBe false
            }

            Then("Order List Data should be Loaded but Unsuccessful") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER].isLoaded shouldBe true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER].isSuccess shouldBe false
            }

            Then("Recent Search Data should be Loaded but Unsuccessful") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECENT_SEARCHES_ORDER].isLoaded shouldBe true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECENT_SEARCHES_ORDER].isSuccess shouldBe false
            }

            Then("Recommendation Data should be Loaded but Unsuccessful") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECOMMENDATION_ORDER].isLoaded shouldBe true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECOMMENDATION_ORDER].isSuccess shouldBe false
            }

            Then("Destination Data should be Loaded but Unsuccessful") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.DESTINATION_ORDER].isLoaded shouldBe true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.DESTINATION_ORDER].isSuccess shouldBe false
            }

            Then("isAllError should be true") {
                viewModel.isAllError.value shouldBe true
            }
        }

        Scenario("Some Response Success and Some Error") {
            val graphqlRepository = mockk<GraphqlRepository>()
            val bannerUseCase = mockk<GetTravelCollectiveBannerUseCase>()
            val dummyData = DUMMY_BANNER
            val recentSearchUseCase = mockk<TravelRecentSearchUseCase>()
            val viewModel = TravelHomepageViewModel(graphqlRepository, GetEmptyModelsUseCase(), bannerUseCase, recentSearchUseCase, TravelTestDispatcherProvider())

            Given("Banner UseCase return dummy response") {
                coEvery { bannerUseCase.execute(any(), any(), any()) } returns Success(dummyData)
            }

            Given("Recent Search UseCase throw Exception") {
                coEvery { recentSearchUseCase.execute(any(), true) } coAnswers { throw Throwable() }
            }

            Given("Graphql Repository throw Exception") {
                coEvery { graphqlRepository.getReseponse(any(), any()) } coAnswers { throw Throwable() }
            }

            When("Build Initial Item List") {
                viewModel.getIntialList(true)
            }

            When("Fetch Banner") {
                viewModel.getBanner("", true)
            }

            When("Fetch Categories") {
                viewModel.getCategories("", true)
            }

            When("Fetch Order List") {
                viewModel.getOrderList("", true)
            }

            When("Fetch Recent Search") {
                viewModel.getRecentSearch("", true)
            }

            When("Fetch Recommendation") {
                viewModel.getRecommendation("", true)
            }

            When("Fetch Destination") {
                viewModel.getDestination("", true)
            }

            Then("Banner Data should be Successfully Loaded") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.BANNER_ORDER].isLoaded shouldBe true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.BANNER_ORDER].isSuccess shouldBe true
            }

            Then("Categories Data should be Loaded but Unsuccessful") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.CATEGORIES_ORDER].isLoaded shouldBe true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.CATEGORIES_ORDER].isSuccess shouldBe false
            }

            Then("Order List Data should be Loaded but Unsuccessful") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER].isLoaded shouldBe true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER].isSuccess shouldBe false
            }

            Then("Recent Search Data should be Loaded but Unsuccessful") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECENT_SEARCHES_ORDER].isLoaded shouldBe true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECENT_SEARCHES_ORDER].isSuccess shouldBe false
            }

            Then("Recommendation Data should be Loaded but Unsuccessful") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECOMMENDATION_ORDER].isLoaded shouldBe true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECOMMENDATION_ORDER].isSuccess shouldBe false
            }

            Then("Destination Data should be Loaded but Unsuccessful") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.DESTINATION_ORDER].isLoaded shouldBe true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.DESTINATION_ORDER].isSuccess shouldBe false
            }

            Then("isAllError should be false") {
                viewModel.isAllError.value shouldBe false
            }
        }

        Scenario("Some Response Success and Some Error with Some Unloaded") {
            val graphqlRepository = mockk<GraphqlRepository>()
            val bannerUseCase = mockk<GetTravelCollectiveBannerUseCase>()
            val dummyData = DUMMY_BANNER
            val viewModel = TravelHomepageViewModel(graphqlRepository, GetEmptyModelsUseCase(), bannerUseCase, mockk(), TravelTestDispatcherProvider())

            Given("Banner UseCase return dummy response") {
                coEvery { bannerUseCase.execute(any(), any(), any()) } returns Success(dummyData)
            }

            Given("Graphql Repository throw Exception") {
                coEvery { graphqlRepository.getReseponse(any(), any()) } coAnswers { throw Throwable() }
            }

            When("Build Initial Item List") {
                viewModel.getIntialList(true)
            }

            When("Fetch Banner") {
                viewModel.getBanner("", true)
            }

            When("Fetch Categories") {
                viewModel.getCategories("", true)
            }

            When("Fetch Order List") {
                viewModel.getOrderList("", true)
            }

            When("Fetch Recommendation") {
                viewModel.getRecommendation("", true)
            }

            When("Fetch Destination") {
                viewModel.getDestination("", true)
            }

            Then("Banner Data should be Successfully Loaded") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.BANNER_ORDER].isLoaded shouldBe true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.BANNER_ORDER].isSuccess shouldBe true
            }

            Then("Categories Data should be Loaded but Unsuccessful") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.CATEGORIES_ORDER].isLoaded shouldBe true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.CATEGORIES_ORDER].isSuccess shouldBe false
            }

            Then("Order List Data should be Loaded but Unsuccessful") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER].isLoaded shouldBe true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER].isSuccess shouldBe false
            }

            Then("Recent Search Data should be Unloaded") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECENT_SEARCHES_ORDER].isLoaded shouldBe false
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECENT_SEARCHES_ORDER].isSuccess shouldBe false
            }

            Then("Recommendation Data should be Loaded but Unsuccessful") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECOMMENDATION_ORDER].isLoaded shouldBe true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECOMMENDATION_ORDER].isSuccess shouldBe false
            }

            Then("Destination Data should be Loaded but Unsuccessful") {
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.DESTINATION_ORDER].isLoaded shouldBe true
                (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.DESTINATION_ORDER].isSuccess shouldBe false
            }

            Then("isAllError should be false") {
                viewModel.isAllError.value shouldBe false
            }
        }

        Scenario("travelItemList Null, isAllError also null") {
            val bannerUseCase = mockk<GetTravelCollectiveBannerUseCase>()
            val viewModel = TravelHomepageViewModel(mockk(), mockk(), bannerUseCase, mockk(), TravelTestDispatcherProvider())

            Given("Banner UseCase throw Exception") {
                coEvery { bannerUseCase.execute(any(), any(), any()) } coAnswers { Fail(Throwable()) }
            }

            When("Fetch Banner") {
                viewModel.getBanner("", true)
            }

            Then("isAllError should be null") {
                viewModel.isAllError.value shouldBe null
            }
        }
    }
})*/
