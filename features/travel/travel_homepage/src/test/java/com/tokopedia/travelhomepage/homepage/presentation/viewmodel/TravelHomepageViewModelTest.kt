package com.tokopedia.travelhomepage.homepage.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.travel.domain.GetTravelCollectiveBannerUseCase
import com.tokopedia.common.travel.domain.TravelRecentSearchUseCase
import com.tokopedia.common.travel.utils.TravelTestDispatcherProvider
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.travelhomepage.homepage.data.*
import com.tokopedia.travelhomepage.homepage.presentation.*
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment
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
    private val recentSearchUseCase = mockk<TravelRecentSearchUseCase>()

    private val travelDispatcherProvider = TravelTestDispatcherProvider()

    private lateinit var viewModel: TravelHomepageViewModel

    @Before
    fun setup() {
        viewModel = TravelHomepageViewModel(graphqlRepository, emptyUseCase, bannerUseCase, recentSearchUseCase, travelDispatcherProvider)
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
    fun onGetBanner_FailedToFetchBanner_BannerValueShouldRepresentFailedToFetch() {
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
    fun onGetCategories_FailedToFetchCategories_CategoryValueShouldRepresentFailedToFetch() {
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

    @Test
    fun onGetOrderList_FailedToFetchOrderList_OrderListValueShouldRepresentFailedToFetch() {
        // given
        coEvery { graphqlRepository.getReseponse(any(), any()) } coAnswers { throw Throwable() }
        viewModel.getIntialList(true)

        // when
        viewModel.getOrderList("", true)

        // then
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER].isSuccess shouldBe false
    }

    @Test
    fun onGetOrderList_SuccessToFetchOrderList_OrderListValueShouldBeSameAsDummyData() {
        // given
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf(TravelHomepageOrderListModel.Response::class.java to TravelHomepageOrderListModel.Response(DUMMY_ORDER_LIST)),
                mapOf(),
                false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns graphqlSuccessResponse
        viewModel.getIntialList(true)

        // when
        viewModel.getOrderList("", true)

        // then
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER].isSuccess shouldBe true

        val orderList = ((viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER] as TravelHomepageSectionModel)
        orderList.title shouldBe DUMMY_ORDER_LIST.travelMeta.title
        orderList.type shouldBe TravelHomepageFragment.TYPE_ORDER_LIST
        orderList.seeAllUrl shouldBe DUMMY_ORDER_LIST.travelMeta.appUrl

        for ((index, item) in orderList.list.withIndex()) {
            item.title shouldBe DUMMY_ORDER_LIST.orders[index].title
            item.subtitle shouldBe DUMMY_ORDER_LIST.orders[index].subtitle
            item.prefix shouldBe DUMMY_ORDER_LIST.orders[index].prefix
            item.value shouldBe DUMMY_ORDER_LIST.orders[index].value
            item.appUrl shouldBe DUMMY_ORDER_LIST.orders[index].appUrl
            item.imageUrl shouldBe DUMMY_ORDER_LIST.orders[index].imageUrl
            item.product shouldBe DUMMY_ORDER_LIST.orders[index].product
        }

        viewModel.isAllError.value shouldBe false
    }

    @Test
    fun onGetRecentSearch_FailedToFetchRecentSearch_RecentSearchValueShouldRepresentFailedToFetch() {
        // given
        coEvery { graphqlRepository.getReseponse(any(), any()) } coAnswers { throw Throwable() }
        viewModel.getIntialList(true)

        // when
        viewModel.getRecentSearch("", true)

        // then
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECENT_SEARCHES_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECENT_SEARCHES_ORDER].isSuccess shouldBe false
    }

    @Test
    fun onGetRecentSearch_SuccessToFetchRecentSearch_RecentSearchValueShouldBeSameAsDummyData() {
        // given
        coEvery { recentSearchUseCase.execute(any(), true) } returns DUMMY_RECENT_SEARCH
        viewModel.getIntialList(true)

        // when
        viewModel.getRecentSearch("", true)

        // then
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECENT_SEARCHES_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECENT_SEARCHES_ORDER].isSuccess shouldBe true

        val recentSearchList = ((viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECENT_SEARCHES_ORDER] as TravelHomepageSectionModel)
        recentSearchList.title shouldBe DUMMY_RECENT_SEARCH.travelMeta.title
        recentSearchList.type shouldBe TravelHomepageFragment.TYPE_RECENT_SEARCH
        recentSearchList.seeAllUrl shouldBe DUMMY_RECENT_SEARCH.travelMeta.appUrl

        for ((index, item) in recentSearchList.list.withIndex()) {
            item.title shouldBe DUMMY_RECENT_SEARCH.items[index].title
            item.subtitle shouldBe DUMMY_RECENT_SEARCH.items[index].subtitle
            item.prefix shouldBe DUMMY_RECENT_SEARCH.items[index].prefix
            item.value shouldBe DUMMY_RECENT_SEARCH.items[index].value
            item.appUrl shouldBe DUMMY_RECENT_SEARCH.items[index].appUrl
            item.imageUrl shouldBe DUMMY_RECENT_SEARCH.items[index].imageUrl
            item.product shouldBe DUMMY_RECENT_SEARCH.items[index].product
        }

        viewModel.isAllError.value shouldBe false
    }

    @Test
    fun onGetRecommendation_FailedToFetchRecommendation_RecommendationValueShouldRepresentFailedToFetch() {
        // given
        coEvery { graphqlRepository.getReseponse(any(), any()) } coAnswers { throw Throwable() }
        viewModel.getIntialList(true)

        // when
        viewModel.getRecommendation("", true)

        // then
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECOMMENDATION_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECOMMENDATION_ORDER].isSuccess shouldBe false
    }

    @Test
    fun onGetRecommendation_SuccessToFetchRecommendation_RecommendationValueShouldBeSameAsDummyData() {
        // given
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf(TravelHomepageRecommendationModel.Response::class.java to TravelHomepageRecommendationModel.Response(DUMMY_RECOMMENDATION)),
                mapOf(),
                false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns graphqlSuccessResponse
        viewModel.getIntialList(true)

        // when
        viewModel.getRecommendation("", true)

        // then
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECOMMENDATION_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECOMMENDATION_ORDER].isSuccess shouldBe true

        val recommendationList = ((viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECOMMENDATION_ORDER] as TravelHomepageSectionModel)
        recommendationList.title shouldBe DUMMY_RECOMMENDATION.travelMeta.title
        recommendationList.type shouldBe TravelHomepageFragment.TYPE_RECOMMENDATION
        recommendationList.seeAllUrl shouldBe DUMMY_RECOMMENDATION.travelMeta.appUrl

        for ((index, item) in recommendationList.list.withIndex()) {
            item.title shouldBe DUMMY_RECOMMENDATION.items[index].title
            item.subtitle shouldBe DUMMY_RECOMMENDATION.items[index].subtitle
            item.prefix shouldBe DUMMY_RECOMMENDATION.items[index].prefix
            item.value shouldBe DUMMY_RECOMMENDATION.items[index].value
            item.appUrl shouldBe DUMMY_RECOMMENDATION.items[index].appUrl
            item.imageUrl shouldBe DUMMY_RECOMMENDATION.items[index].imageUrl
            item.product shouldBe DUMMY_RECOMMENDATION.items[index].product
        }

        viewModel.isAllError.value shouldBe false
    }

    @Test
    fun onGetDestination_FailedToFetchDestination_DestinationValueShouldRepresentFailedToFetch() {
        // given
        coEvery { graphqlRepository.getReseponse(any(), any()) } coAnswers { throw Throwable() }
        viewModel.getIntialList(true)

        // when
        viewModel.getDestination("", true)

        // then
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.DESTINATION_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.DESTINATION_ORDER].isSuccess shouldBe false
    }

    @Test
    fun onGetDestination_SuccessToFetchDestination_DestinationValueShouldBeSameAsDummyData() {
        // given
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf(TravelHomepageDestinationModel.Response::class.java to TravelHomepageDestinationModel.Response(DUMMY_DESTINATION)),
                mapOf(),
                false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns graphqlSuccessResponse
        viewModel.getIntialList(true)

        // when
        viewModel.getDestination("", true)

        // then
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.DESTINATION_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.DESTINATION_ORDER].isSuccess shouldBe true

        val destinationModel = ((viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.DESTINATION_ORDER] as TravelHomepageDestinationModel)
        destinationModel.meta.title shouldBe DUMMY_DESTINATION.meta.title

        for ((index, item) in destinationModel.destination.withIndex()) {
            item.attributes.title shouldBe DUMMY_DESTINATION.destination[index].attributes.title
            item.attributes.subtitle shouldBe DUMMY_DESTINATION.destination[index].attributes.subtitle
            item.attributes.webUrl shouldBe DUMMY_DESTINATION.destination[index].attributes.webUrl
            item.attributes.appUrl shouldBe DUMMY_DESTINATION.destination[index].attributes.appUrl
            item.attributes.imageUrl shouldBe DUMMY_DESTINATION.destination[index].attributes.imageUrl
        }

        viewModel.isAllError.value shouldBe false
    }

    @Test
    fun onFailedToFetchAllData_AllValueShouldRepresentFailedToFetch() {
        // given
        coEvery { bannerUseCase.execute(any(), any(), any()) } coAnswers { Fail(Throwable()) }
        coEvery { recentSearchUseCase.execute(any(), true) } coAnswers { throw Throwable() }
        coEvery { graphqlRepository.getReseponse(any(), any()) } coAnswers { throw Throwable() }
        viewModel.getIntialList(true)

        // when
        viewModel.getBanner("", true)
        viewModel.getCategories("", true)
        viewModel.getOrderList("", true)
        viewModel.getRecentSearch("", true)
        viewModel.getRecommendation("", true)
        viewModel.getDestination("", true)

        // then
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.BANNER_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.BANNER_ORDER].isSuccess shouldBe false

        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.CATEGORIES_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.CATEGORIES_ORDER].isSuccess shouldBe false

        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER].isSuccess shouldBe false

        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECENT_SEARCHES_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECENT_SEARCHES_ORDER].isSuccess shouldBe false

        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECOMMENDATION_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECOMMENDATION_ORDER].isSuccess shouldBe false

        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.DESTINATION_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.DESTINATION_ORDER].isSuccess shouldBe false

        viewModel.isAllError.value shouldBe true
    }

    @Test
    fun onSomeResponseSuccessAndSomeError() {
        // given
        coEvery { bannerUseCase.execute(any(), any(), any()) } returns Success(DUMMY_BANNER)
        coEvery { recentSearchUseCase.execute(any(), true) } coAnswers { throw Throwable() }
        coEvery { graphqlRepository.getReseponse(any(), any()) } coAnswers { throw Throwable() }
        viewModel.getIntialList(true)

        // when
        viewModel.getBanner("", true)
        viewModel.getCategories("", true)
        viewModel.getOrderList("", true)
        viewModel.getRecentSearch("", true)
        viewModel.getRecommendation("", true)
        viewModel.getDestination("", true)

        // then
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.BANNER_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.BANNER_ORDER].isSuccess shouldBe true

        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.CATEGORIES_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.CATEGORIES_ORDER].isSuccess shouldBe false

        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER].isSuccess shouldBe false

        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECENT_SEARCHES_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECENT_SEARCHES_ORDER].isSuccess shouldBe false

        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECOMMENDATION_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECOMMENDATION_ORDER].isSuccess shouldBe false

        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.DESTINATION_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.DESTINATION_ORDER].isSuccess shouldBe false

        viewModel.isAllError.value shouldBe false

    }

    @Test
    fun onSomeResponseSuccessAndSomeErrorWithSomeUnloaded() {
        // given
        coEvery { bannerUseCase.execute(any(), any(), any()) } returns Success(DUMMY_BANNER)
        coEvery { graphqlRepository.getReseponse(any(), any()) } coAnswers { throw Throwable() }

        viewModel.getIntialList(true)

        // when
        viewModel.getBanner("", true)
        viewModel.getCategories("", true)
        viewModel.getOrderList("", true)
        viewModel.getRecommendation("", true)
        viewModel.getDestination("", true)

        // then
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.BANNER_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.BANNER_ORDER].isSuccess shouldBe true

        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.CATEGORIES_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.CATEGORIES_ORDER].isSuccess shouldBe false

        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.ORDER_LIST_ORDER].isSuccess shouldBe false

        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECENT_SEARCHES_ORDER].isLoaded shouldBe false
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECENT_SEARCHES_ORDER].isSuccess shouldBe false

        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECOMMENDATION_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.RECOMMENDATION_ORDER].isSuccess shouldBe false

        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.DESTINATION_ORDER].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[TravelHomepageViewModel.DESTINATION_ORDER].isSuccess shouldBe false

        viewModel.isAllError.value shouldBe false

    }

    @Test
    fun onTravelItemListNull_isAllErrorShouldAlsoNull() {
        // given
        coEvery { bannerUseCase.execute(any(), any(), any()) } coAnswers { Fail(Throwable()) }

        // when
        viewModel.getBanner("", true)

        // then
        viewModel.isAllError.value shouldBe null

    }

}