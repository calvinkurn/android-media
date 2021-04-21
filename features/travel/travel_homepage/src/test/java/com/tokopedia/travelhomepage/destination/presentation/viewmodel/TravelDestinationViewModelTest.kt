package com.tokopedia.travelhomepage.destination.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.travel.data.entity.TravelMetaModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
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
        viewModel = TravelDestinationViewModel(graphqlRepository, getEmptyModelsUseCase, CoroutineTestDispatchersProvider)
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
    fun getAllContents_allSuccess_shouldUpdateModel() {
        //given
        val summaryModel = TravelDestinationSummaryModel(title = "summaryData")

        val result = HashMap<Type, Any>()
        result[TravelDestinationSummaryModel.Response::class.java] = TravelDestinationSummaryModel.Response(summaryModel)
        result[TravelHomepageOrderListModel.Response::class.java] = TravelHomepageOrderListModel.Response(TravelHomepageOrderListModel())
        result[TravelHomepageRecommendationModel.Response::class.java] = TravelHomepageRecommendationModel.Response(TravelHomepageRecommendationModel())
        result[TravelArticleModel.Response::class.java] = TravelArticleModel.Response(TravelArticleModel())
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getAllContent("", "", "", "", "10")

        //then
        viewModel.travelDestinationItemList.value?.let {
            assert(it.isNotEmpty())
            assert(it.size == 6)
        }
    }

    @Test
    fun getAllContents_partialSuccess_shouldUpdateModel() {
        //given
        val summaryModel = TravelDestinationSummaryModel(title = "summaryData")

        val result = HashMap<Type, Any>()
        result[TravelDestinationSummaryModel.Response::class.java] = TravelDestinationSummaryModel.Response(summaryModel)
        result[TravelHomepageOrderListModel.Response::class.java] = TravelHomepageOrderListModel.Response(TravelHomepageOrderListModel())
        val errors = HashMap<Type, List<GraphqlError>>()
        errors[TravelHomepageRecommendationModel.Response::class.java] = listOf(GraphqlError())
        errors[TravelArticleModel.Response::class.java] = listOf(GraphqlError())
        val gqlResponse = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getAllContent("", "", "", "", "10")

        //then
        assert(viewModel.travelDestinationItemList.value != null)
        viewModel.travelDestinationItemList.value?.let {
            assert(it.isNotEmpty())
            assert(it.size == 2)
        }
    }

    @Test
    fun getAllContents_partialSuccess2_shouldUpdateModel() {
        //given
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        errors[TravelDestinationSummaryModel.Response::class.java] = listOf(GraphqlError())
        errors[TravelHomepageOrderListModel.Response::class.java] = listOf(GraphqlError())
        result[TravelHomepageRecommendationModel.Response::class.java] = TravelHomepageRecommendationModel.Response()
        result[TravelArticleModel.Response::class.java] = TravelArticleModel.Response()
        val gqlResponse = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getAllContent("", "", "", "", "10")

        //then
        assert(viewModel.travelDestinationItemList.value != null)
        viewModel.travelDestinationItemList.value?.let {
            assert(it.isNotEmpty())
            assert(it.size == 4)
        }
    }

    @Test
    fun getAllContents_allFailed_shouldNotUpdateModel() {
        //given
        val errors = HashMap<Type, List<GraphqlError>>()
        errors[TravelDestinationSummaryModel.Response::class.java] = listOf(GraphqlError())
        errors[TravelHomepageOrderListModel.Response::class.java] = listOf(GraphqlError())
        errors[TravelHomepageRecommendationModel.Response::class.java] = listOf(GraphqlError())
        errors[TravelArticleModel.Response::class.java] = listOf(GraphqlError())
        val gqlResponse = GraphqlResponse(HashMap<Type, Any?>(), errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getAllContent("", "", "", "", "10")

        //then
        assert(viewModel.travelDestinationItemList.value != null)
        viewModel.travelDestinationItemList.value?.let {
            assert(it.isEmpty())
        }
    }

}
