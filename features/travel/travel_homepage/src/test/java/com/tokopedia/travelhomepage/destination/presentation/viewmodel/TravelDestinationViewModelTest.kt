package com.tokopedia.travelhomepage.destination.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.travel.data.entity.TravelMetaModel
import com.tokopedia.common.travel.utils.TravelTestDispatcherProvider
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.travelhomepage.destination.model.*
import com.tokopedia.travelhomepage.destination.usecase.GetEmptyModelsUseCase
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageOrderListModel
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageRecommendationModel
import com.tokopedia.travelhomepage.shouldBe
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by furqan on 15/02/2020
 */
class TravelDestinationViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val graphqlRepository = mockk<GraphqlRepository>()

    private lateinit var viewModel: TravelDestinationViewModel

    private val getEmptyModelsUseCase = GetEmptyModelsUseCase()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TravelDestinationViewModel(graphqlRepository, getEmptyModelsUseCase, TravelTestDispatcherProvider())
    }

    @Test
    fun onCreateViewModelWithInitialList_InitialItemShouldHave6Items() {
        // given

        // when
        viewModel.getInitialList()

        // then
        viewModel.travelDestinationItemList.value!!.size shouldBe 6
        viewModel.isAllError.value shouldBe null
    }

    @Test
    fun onCreateViewModelWithoutInitialList_InitialItemShouldHave6Items() {
        // given

        // when

        // then
        viewModel.travelDestinationItemList.value shouldBe null
        viewModel.travelDestinationCityModel.value shouldBe null
        viewModel.isAllError.value shouldBe null
    }

    @Test
    fun onGetDestinationCityData_CityModelShouldRepresentSuccess() {
        // given
        val cityModel = TravelDestinationCityModel(cityId = "100")
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(TravelDestinationCityModel.Response::class.java to TravelDestinationCityModel.Response(cityModel)),
                mapOf(), false)

        // when
        viewModel.getDestinationCityData("", "")

        // then
        assert(viewModel.travelDestinationCityModel.value is Success)
        (viewModel.travelDestinationCityModel.value as Success).data.cityId shouldBe "100"
    }

    @Test
    fun onGetDestinationCityData_CityModelShouldRepresentFailedFetch() {
        // given
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(),
                mapOf(GraphqlError::class.java to listOf(GraphqlError())), false)

        // when
        viewModel.getDestinationCityData("", "")

        // then
        viewModel.travelDestinationCityModel.value is Fail
    }

    @Test
    fun getDestinationSummaryData_DestinationDataShouldUpdateModel() {
        // given
        val cityName = "Bandung"
        val cityDescription = "niceCity"
        val destinationSummaryData = TravelDestinationSummaryModel(title = cityName, description = cityDescription)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(TravelDestinationSummaryModel.Response::class.java to TravelDestinationSummaryModel.Response(destinationSummaryData)),
                mapOf(), false)

        // when
        viewModel.getInitialList()
        viewModel.getDestinationSummaryData("", "")

        // then
        viewModel.travelDestinationItemList.value?.size shouldBe 6
        viewModel.travelDestinationItemList.value!!.let {
            (it[TravelDestinationViewModel.SUMMARY_ORDER] as TravelDestinationSummaryModel).title shouldBe cityName
            (it[TravelDestinationViewModel.SUMMARY_ORDER] as TravelDestinationSummaryModel).description shouldBe cityDescription
            (it[TravelDestinationViewModel.SUMMARY_ORDER] as TravelDestinationSummaryModel).isSuccess shouldBe true
            (it[TravelDestinationViewModel.SUMMARY_ORDER] as TravelDestinationSummaryModel).isLoaded shouldBe true
        }
    }

    @Test
    fun getDestinationSummaryData_DestinationDataFailedToFetch() {
        // given
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(),
                mapOf(GraphqlError::class.java to listOf(GraphqlError())), false)

        // when
        viewModel.getInitialList()
        viewModel.getDestinationSummaryData("", "")

        // then
        viewModel.travelDestinationItemList.value?.size shouldBe 6
        viewModel.travelDestinationItemList.value!!.let {
            (it[TravelDestinationViewModel.SUMMARY_ORDER] as TravelDestinationSummaryModel).isSuccess shouldBe false
            viewModel.isAllError.value shouldBe true
        }
    }

    @Test
    fun getCityRecommendationData_DataShouldUpdateModel() {
        // given
        val title = "city recommendation"
        val list = mutableListOf<TravelHomepageRecommendationModel.Item>()
        for (i in 0 until 3) list.add(TravelHomepageRecommendationModel.Item(title = i.toString()))
        val data = TravelHomepageRecommendationModel(travelMeta = TravelMetaModel(title), items = list)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(TravelHomepageRecommendationModel.Response::class.java to TravelHomepageRecommendationModel.Response(data)),
                mapOf(), false)

        // when
        viewModel.getInitialList()
        viewModel.getCityRecommendationData("", "10", "", TravelDestinationViewModel.CITY_RECOMMENDATION_ORDER)

        // then
        viewModel.travelDestinationItemList.value?.size shouldBe 6
        viewModel.travelDestinationItemList.value!!.let {
            (it[TravelDestinationViewModel.CITY_RECOMMENDATION_ORDER] as TravelDestinationSectionModel).title shouldBe "city recommendation"
            (it[TravelDestinationViewModel.CITY_RECOMMENDATION_ORDER] as TravelDestinationSectionModel).list.size shouldBe 3
            (it[TravelDestinationViewModel.CITY_RECOMMENDATION_ORDER] as TravelDestinationSectionModel).list.forEach { item ->
                assert(item.title.isNotEmpty())
            }
        }
    }

    @Test
    fun getCityRecommendationData_DataShouldRepresentFailed() {
        // given
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(),
                mapOf(GraphqlError::class.java to listOf(GraphqlError())), false)

        // when
        viewModel.getInitialList()
        viewModel.getCityRecommendationData("", "10", "", TravelDestinationViewModel.CITY_RECOMMENDATION_ORDER)

        // then
        viewModel.travelDestinationItemList.value?.size shouldBe 6
        viewModel.travelDestinationItemList.value!!.let {
            (it[TravelDestinationViewModel.CITY_RECOMMENDATION_ORDER] as TravelDestinationSectionModel).title shouldBe ""
            (it[TravelDestinationViewModel.CITY_RECOMMENDATION_ORDER] as TravelDestinationSectionModel).list.size shouldBe 0
        }
        viewModel.isAllError.value shouldBe false
    }

    @Test
    fun getOrderListData_DataShouldUpdateModel() {
        // given
        val title = "ORDER LIST"
        val list = mutableListOf<TravelHomepageOrderListModel.Order>()
        for (i in 0 until 4) list.add(TravelHomepageOrderListModel.Order(title = i.toString()))
        val data = TravelHomepageOrderListModel(travelMeta = TravelMetaModel(title), orders = list)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(TravelHomepageOrderListModel.Response::class.java to TravelHomepageOrderListModel.Response(data)),
                mapOf(), false)

        // when
        viewModel.getInitialList()
        viewModel.getOrderList("", "10")

        // then
        viewModel.travelDestinationItemList.value?.size shouldBe 6
        viewModel.travelDestinationItemList.value!!.let {
            (it[TravelDestinationViewModel.ORDER_LIST_ORDER] as TravelDestinationSectionModel).title shouldBe "ORDER LIST"
            (it[TravelDestinationViewModel.ORDER_LIST_ORDER] as TravelDestinationSectionModel).list.size shouldBe 4
            (it[TravelDestinationViewModel.ORDER_LIST_ORDER] as TravelDestinationSectionModel).list.forEachIndexed { index, item ->
                item.title shouldBe index.toString()
            }
        }
    }

    @Test
    fun getOrderListData_DataShouldRepresentFailed() {
        // given
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(),
                mapOf(GraphqlError::class.java to listOf(GraphqlError())), false)

        // when
        viewModel.getInitialList()
        viewModel.getOrderList("", "10")

        // then
        viewModel.travelDestinationItemList.value?.size shouldBe 6
        viewModel.travelDestinationItemList.value!!.let {
            (it[TravelDestinationViewModel.ORDER_LIST_ORDER] as TravelDestinationSectionModel).title shouldBe ""
            (it[TravelDestinationViewModel.ORDER_LIST_ORDER] as TravelDestinationSectionModel).list.size shouldBe 0
        }
        viewModel.isAllError.value shouldBe false
    }

    @Test
    fun getCityArticle_DataShouldUpdateModel() {
        // given
        val title = "City Article"
        val list = mutableListOf<TravelArticleModel.Item>()
        for (i in 0 until 2) list.add(TravelArticleModel.Item(title = i.toString()))
        val data = TravelArticleModel(meta = TravelArticleModel.Meta(title), items = list)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(TravelArticleModel.Response::class.java to TravelArticleModel.Response(data)),
                mapOf(), false)

        // when
        viewModel.getInitialList()
        viewModel.getCityArticles("", "10")

        // then
        viewModel.travelDestinationItemList.value?.size shouldBe 6
        viewModel.travelDestinationItemList.value!!.let {
            (it[TravelDestinationViewModel.CITY_ARTICLE_ORDER] as TravelArticleModel).meta.title shouldBe title
            (it[TravelDestinationViewModel.CITY_ARTICLE_ORDER] as TravelArticleModel).items.size shouldBe 2
            (it[TravelDestinationViewModel.CITY_ARTICLE_ORDER] as TravelArticleModel).items.forEachIndexed { index, item ->
                item.title shouldBe index.toString()
            }
        }
    }

    @Test
    fun getCityArticle_DataShouldRepresentFailed() {
        // given
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(),
                mapOf(GraphqlError::class.java to listOf(GraphqlError())), false)

        // when
        viewModel.getInitialList()
        viewModel.getCityArticles("", "10")

        // then
        viewModel.travelDestinationItemList.value?.size shouldBe 6
        viewModel.travelDestinationItemList.value!!.let {
            (it[TravelDestinationViewModel.CITY_ARTICLE_ORDER] as TravelArticleModel).meta.title shouldBe ""
            (it[TravelDestinationViewModel.CITY_ARTICLE_ORDER] as TravelArticleModel).items.size shouldBe 0
        }
        viewModel.isAllError.value shouldBe false
    }

    @Test
    fun checkIfAllError_ShouldBeNull() {
        // given

        // when
        viewModel.checkIfAllError()

        // then
        viewModel.isAllError.value shouldBe null
    }

    @Test
    fun checkIfAllError_IfPartialSuccess_ShouldReturnFalse() {
        // given
        val title = "City Article"
        val list = mutableListOf<TravelArticleModel.Item>()
        for (i in 0 until 2) list.add(TravelArticleModel.Item(title = i.toString()))
        val data = TravelArticleModel(meta = TravelArticleModel.Meta(title), items = list)

        // when
        viewModel.getInitialList()
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(TravelArticleModel.Response::class.java to TravelArticleModel.Response(data)),
                mapOf(), false)
        viewModel.getCityArticles("", "10")
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(),
                mapOf(GraphqlError::class.java to listOf(GraphqlError())), false)
        viewModel.getOrderList("", "10")

        // then
        viewModel.travelDestinationItemList.value?.size shouldBe 6
        viewModel.isAllError.value shouldBe false
    }

    @Test
    fun checkIfAllError_ifPartialSuccessPartialFailedAndAllLoaded() {
        // given

        // when
        viewModel.getInitialList()
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(TravelDestinationSummaryModel.Response::class.java to TravelDestinationSummaryModel.Response()),
                mapOf(), false)
        viewModel.getDestinationSummaryData("", "10")

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(TravelArticleModel.Response::class.java to TravelArticleModel.Response()),
                mapOf(), false)
        viewModel.getCityArticles("", "10")

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(TravelHomepageRecommendationModel.Response::class.java to TravelHomepageRecommendationModel.Response()),
                mapOf(), false)
        viewModel.getCityRecommendationData("","10", "", TravelDestinationViewModel.CITY_RECOMMENDATION_ORDER)
        viewModel.getCityRecommendationData("","10", "", TravelDestinationViewModel.CITY_EVENT_ORDER)
        viewModel.getCityRecommendationData("","10", "", TravelDestinationViewModel.CITY_DEALS_ORDER)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(),
                mapOf(GraphqlError::class.java to listOf(GraphqlError())), false)
        viewModel.getOrderList("", "10")

        // then
        viewModel.travelDestinationItemList.value?.size shouldBe 6
        viewModel.isAllError.value shouldBe false
    }

    @Test
    fun checkIfAllError_ifAllLoadedAndFailed() {
        // given
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(),
                mapOf(GraphqlError::class.java to listOf(GraphqlError())), false)

        // when
        viewModel.getInitialList()
        viewModel.getOrderList("", "10")
        viewModel.getCityRecommendationData("","10", "", TravelDestinationViewModel.CITY_RECOMMENDATION_ORDER)
        viewModel.getCityRecommendationData("","10", "", TravelDestinationViewModel.CITY_EVENT_ORDER)
        viewModel.getCityRecommendationData("","10", "", TravelDestinationViewModel.CITY_DEALS_ORDER)
        viewModel.getCityArticles("", "10")
        viewModel.getDestinationSummaryData("", "10")

        // then
        viewModel.travelDestinationItemList.value?.size shouldBe 6
        viewModel.isAllError.value shouldBe true
    }

    @Test
    fun checkIfAllError_ifAllNotLoaded() {
        //given
        viewModel.getInitialList()
        viewModel.checkIfAllError()

        //then
        viewModel.isAllError.value shouldBe false
    }

    @Test
    fun checkIfAllError_ifAllSuccessAllLoaded() {
        // given

        // when
        viewModel.getInitialList()
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(TravelDestinationSummaryModel.Response::class.java to TravelDestinationSummaryModel.Response()),
                mapOf(), false)
        viewModel.getDestinationSummaryData("", "10")

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(TravelArticleModel.Response::class.java to TravelArticleModel.Response()),
                mapOf(), false)
        viewModel.getCityArticles("", "10")

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(TravelHomepageRecommendationModel.Response::class.java to TravelHomepageRecommendationModel.Response()),
                mapOf(), false)
        viewModel.getCityRecommendationData("","10", "", TravelDestinationViewModel.CITY_RECOMMENDATION_ORDER)
        viewModel.getCityRecommendationData("","10", "", TravelDestinationViewModel.CITY_EVENT_ORDER)
        viewModel.getCityRecommendationData("","10", "", TravelDestinationViewModel.CITY_DEALS_ORDER)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(TravelHomepageOrderListModel.Response::class.java to TravelHomepageOrderListModel.Response()),
                mapOf(), false)
        viewModel.getOrderList("", "10")

        viewModel.checkIfAllError()
        // then

        viewModel.travelDestinationItemList.value?.size shouldBe 6
        viewModel.isAllError.value shouldBe false
    }

    @Test
    fun checkIfAllError_ifPartialSuccessPartialFailedAndPartialLoading() {
        // given

        // when
        viewModel.getInitialList()
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(TravelDestinationSummaryModel.Response::class.java to TravelDestinationSummaryModel.Response()),
                mapOf(), false)
        viewModel.getDestinationSummaryData("", "10")

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(TravelArticleModel.Response::class.java to TravelArticleModel.Response()),
                mapOf(), false)
        viewModel.getCityArticles("", "10")

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(TravelHomepageRecommendationModel.Response::class.java to TravelHomepageRecommendationModel.Response()),
                mapOf(), false)
        viewModel.getCityRecommendationData("","10", "", TravelDestinationViewModel.CITY_RECOMMENDATION_ORDER)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(),
                mapOf(GraphqlError::class.java to listOf(GraphqlError())), false)
        viewModel.getOrderList("", "10")

        // then
        viewModel.travelDestinationItemList.value?.size shouldBe 6
        viewModel.isAllError.value shouldBe false
    }
}
