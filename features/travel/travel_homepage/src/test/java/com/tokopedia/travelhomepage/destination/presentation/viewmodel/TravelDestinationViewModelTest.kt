package com.tokopedia.travelhomepage.destination.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.travel.data.entity.TravelMetaModel
import com.tokopedia.common.travel.utils.TravelTestDispatcherProvider
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.travelhomepage.destination.model.TravelArticleModel
import com.tokopedia.travelhomepage.destination.model.TravelDestinationCityModel
import com.tokopedia.travelhomepage.destination.model.TravelDestinationSectionModel
import com.tokopedia.travelhomepage.destination.model.TravelDestinationSummaryModel
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
import java.lang.reflect.Type

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

        val result = HashMap<Type, Any>()
        result[TravelDestinationCityModel.Response::class.java] = TravelDestinationCityModel.Response(cityModel)
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        // when
        viewModel.getDestinationCityData("", "")

        // then
        assert(viewModel.travelDestinationCityModel.value is Success)
        (viewModel.travelDestinationCityModel.value as Success).data.cityId shouldBe "100"
    }

    @Test
    fun onGetDestinationCityData_CityModelShouldRepresentFailedFetch() {
        // given
        val errors = HashMap<Type, List<GraphqlError>>()
        errors[GraphqlError::class.java] = listOf(GraphqlError())
        val gqlResponse = GraphqlResponse(HashMap<Type, Any?>(), errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

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

        val result = HashMap<Type, Any>()
        result[TravelDestinationSummaryModel.Response::class.java] = TravelDestinationSummaryModel.Response(destinationSummaryData)
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

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
        val errors = HashMap<Type, List<GraphqlError>>()
        errors[GraphqlError::class.java] = listOf(GraphqlError())
        val gqlResponse = GraphqlResponse(HashMap<Type, Any?>(), errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

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

        val result = HashMap<Type, Any>()
        result[TravelHomepageRecommendationModel.Response::class.java] = TravelHomepageRecommendationModel.Response(data)
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

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
        val errors = HashMap<Type, List<GraphqlError>>()
        errors[GraphqlError::class.java] = listOf(GraphqlError())
        val gqlResponse = GraphqlResponse(HashMap<Type, Any?>(), errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

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

        val result = HashMap<Type, Any>()
        result[TravelHomepageOrderListModel.Response::class.java] = TravelHomepageOrderListModel.Response(data)
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

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
        val errors = HashMap<Type, List<GraphqlError>>()
        errors[GraphqlError::class.java] = listOf(GraphqlError())
        val gqlResponse = GraphqlResponse(HashMap<Type, Any?>(), errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

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

        val result = HashMap<Type, Any>()
        result[TravelArticleModel.Response::class.java] = TravelArticleModel.Response(data)
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

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
        val errors = HashMap<Type, List<GraphqlError>>()
        errors[GraphqlError::class.java] = listOf(GraphqlError())
        val gqlResponse = GraphqlResponse(HashMap<Type, Any?>(), errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

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
        val result = HashMap<Type, Any>()
        result[TravelArticleModel.Response::class.java] = TravelArticleModel.Response(data)
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        viewModel.getCityArticles("", "10")

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[GraphqlError::class.java] = listOf(GraphqlError())
        val gqlResponseError = GraphqlResponse(HashMap<Type, Any?>(), errors, false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseError
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

        val result = HashMap<Type, Any>()
        result[TravelDestinationSummaryModel.Response::class.java] = TravelDestinationSummaryModel.Response()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse
        viewModel.getDestinationSummaryData("", "10")

        val resultArticle = HashMap<Type, Any>()
        resultArticle[TravelArticleModel.Response::class.java] = TravelArticleModel.Response()
        val gqlResponseArticle = GraphqlResponse(resultArticle, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseArticle
        viewModel.getCityArticles("", "10")

        val resultRecommendation = HashMap<Type, Any>()
        resultRecommendation[TravelHomepageRecommendationModel.Response::class.java] = TravelHomepageRecommendationModel.Response()
        val gqlResponseRecommendation = GraphqlResponse(resultRecommendation, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseRecommendation
        viewModel.getCityRecommendationData("","10", "", TravelDestinationViewModel.CITY_RECOMMENDATION_ORDER)
        viewModel.getCityRecommendationData("","10", "", TravelDestinationViewModel.CITY_EVENT_ORDER)
        viewModel.getCityRecommendationData("","10", "", TravelDestinationViewModel.CITY_DEALS_ORDER)

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[GraphqlError::class.java] = listOf(GraphqlError())
        val gqlResponseError = GraphqlResponse(HashMap<Type, Any?>(), errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseError
        viewModel.getOrderList("", "10")

        // then
        viewModel.travelDestinationItemList.value?.size shouldBe 6
        viewModel.isAllError.value shouldBe false
    }

    @Test
    fun checkIfAllError_ifAllLoadedAndFailed() {
        // given
        val errors = HashMap<Type, List<GraphqlError>>()
        errors[GraphqlError::class.java] = listOf(GraphqlError())
        val gqlResponseError = GraphqlResponse(HashMap<Type, Any?>(), errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseError

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

        val result = HashMap<Type, Any>()
        result[TravelDestinationSummaryModel.Response::class.java] = TravelDestinationSummaryModel.Response()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse
        viewModel.getDestinationSummaryData("", "10")

        val resultTravelArticle = HashMap<Type, Any>()
        resultTravelArticle[TravelArticleModel.Response::class.java] = TravelArticleModel.Response()
        val gqlResponseTravelArticle = GraphqlResponse(resultTravelArticle, HashMap<Type, List<GraphqlError>>(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseTravelArticle
        viewModel.getCityArticles("", "10")

        val resultTravelHomepageRecommendation = HashMap<Type, Any>()
        resultTravelHomepageRecommendation[TravelHomepageRecommendationModel.Response::class.java] = TravelHomepageRecommendationModel.Response()
        val gqlResponseTravelHomepageRecommendation = GraphqlResponse(resultTravelHomepageRecommendation, HashMap<Type, List<GraphqlError>>(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseTravelHomepageRecommendation
        viewModel.getCityRecommendationData("","10", "", TravelDestinationViewModel.CITY_RECOMMENDATION_ORDER)
        viewModel.getCityRecommendationData("","10", "", TravelDestinationViewModel.CITY_EVENT_ORDER)
        viewModel.getCityRecommendationData("","10", "", TravelDestinationViewModel.CITY_DEALS_ORDER)

        val resultTravelDestinationSummary = HashMap<Type, Any>()
        resultTravelDestinationSummary[TravelHomepageOrderListModel.Response::class.java] = TravelHomepageOrderListModel.Response()
        val gqlResponseTravelDestinationSummary = GraphqlResponse(resultTravelDestinationSummary, HashMap<Type, List<GraphqlError>>(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseTravelDestinationSummary
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
        val result = HashMap<Type, Any>()
        result[TravelDestinationSummaryModel.Response::class.java] = TravelDestinationSummaryModel.Response()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse
        viewModel.getDestinationSummaryData("", "10")

        val resultTravelArticle = HashMap<Type, Any>()
        resultTravelArticle[TravelArticleModel.Response::class.java] = TravelArticleModel.Response()
        val gqlResponseTravelArticle = GraphqlResponse(resultTravelArticle, HashMap<Type, List<GraphqlError>>(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseTravelArticle
        viewModel.getCityArticles("", "10")

        val resultTravelHomepageRecommendation = HashMap<Type, Any>()
        resultTravelHomepageRecommendation[TravelHomepageRecommendationModel.Response::class.java] = TravelHomepageRecommendationModel.Response()
        val gqlResponseTravelHomepageRecommendation = GraphqlResponse(resultTravelHomepageRecommendation, HashMap<Type, List<GraphqlError>>(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseTravelHomepageRecommendation
        viewModel.getCityRecommendationData("","10", "", TravelDestinationViewModel.CITY_RECOMMENDATION_ORDER)

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[GraphqlError::class.java] = listOf(GraphqlError())
        val gqlResponseError = GraphqlResponse(HashMap<Type, Any?>(), errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseError
        viewModel.getOrderList("", "10")

        // then
        viewModel.travelDestinationItemList.value?.size shouldBe 6
        viewModel.isAllError.value shouldBe false
    }
}
