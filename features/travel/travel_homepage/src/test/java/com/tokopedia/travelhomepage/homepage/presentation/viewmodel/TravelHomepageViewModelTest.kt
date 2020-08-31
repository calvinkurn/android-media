package com.tokopedia.travelhomepage.homepage.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.travel.utils.TravelTestDispatcherProvider
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.travelhomepage.homepage.data.*
import com.tokopedia.travelhomepage.homepage.usecase.GetEmptyModelsUseCase
import com.tokopedia.travelhomepage.shouldBe
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type


/**
 * @author by furqan on 04/02/2020
 */

class TravelHomepageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val graphqlRepository = mockk<GraphqlRepository>()

    private val travelDispatcherProvider = TravelTestDispatcherProvider()

    private lateinit var viewModel: TravelHomepageViewModel

    @RelaxedMockK
    lateinit var emptyUseCase: GetEmptyModelsUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TravelHomepageViewModel(graphqlRepository, emptyUseCase, travelDispatcherProvider)
    }

    @Test
    fun getListFromCloud_ReturnListOfModel_valueShouldBeListWithTwoItem() {
        // given
        val list = mutableListOf<TravelHomepageItemModel>()
        list.add(TravelHomepageProductCardModel())
        list.add(TravelHomepageLegoBannerModel())
        coEvery {
            emptyUseCase.getTravelLayoutSubhomepage(any(), any())
        } returns Success(list)

        // when
        viewModel.getListFromCloud("", true)

        // then
        viewModel.travelItemList.size shouldBe 2
        viewModel.travelItemList.forEach {
            it.isLoaded shouldBe false
            it.isLoadFromCloud shouldBe true
        }
        viewModel.isAllError.value shouldBe null
    }

    @Test
    fun getListFromCloud_FailedToFetchLayout_LayoutShouldBeNull() {
        // given
        coEvery {
            emptyUseCase.getTravelLayoutSubhomepage(any(), any())
        } returns Fail(Throwable())

        // when
        viewModel.getListFromCloud("", true)

        // then
        viewModel.travelItemList.size shouldBe 0
        viewModel.isAllError.value shouldBe true
    }

    @Test
    fun onGetTravelUnifiedData_AllSuccess_ItemModelShouldRepresentFailedToFetch() {
        // given
        val list = mutableListOf<TravelHomepageItemModel>()
        list.add(TravelHomepageProductCardModel())
        list.add(TravelHomepageLegoBannerModel())
        coEvery {
            emptyUseCase.getTravelLayoutSubhomepage(any(), any())
        } returns Success(list)

        val result = HashMap<Type, Any>()
        result[TravelUnifiedSubhomepageData.Response::class.java] = TravelUnifiedSubhomepageData.Response()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        // when
        viewModel.getListFromCloud("", true)
        viewModel.getTravelUnifiedData("", TravelLayoutSubhomepage.Data(),true)
        viewModel.getTravelUnifiedData("", TravelLayoutSubhomepage.Data(), true)

        viewModel.isAllError.value shouldBe null
    }

    @Test
    fun onGetTravelUnifiedData_FailedToFetchData_ItemModelShouldRepresentFailedToFetch() {
        // given
        val list = mutableListOf<TravelHomepageItemModel>()
        list.add(TravelHomepageProductCardModel())
        list.add(TravelHomepageLegoBannerModel())
        coEvery {
            emptyUseCase.getTravelLayoutSubhomepage(any(), any())
        } returns Success(list)

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[GraphqlError::class.java] = listOf(GraphqlError())
        val gqlResponse = GraphqlResponse(HashMap<Type, Any?>(), errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        // when
        viewModel.getListFromCloud("", true)
        viewModel.getTravelUnifiedData("", TravelLayoutSubhomepage.Data(), true)
        viewModel.getTravelUnifiedData("", TravelLayoutSubhomepage.Data(), true)

        // then
//        viewModel.travelItemList.value!!.forEach {
//            it.isLoaded shouldBe false
//            it.isSuccess shouldBe false
//        }
        viewModel.isAllError.value shouldBe null
    }

    @Test
    fun onGetTravelUnifiedData_IfPartialSuccess_ItemModelShouldRepresentPartialSuccess() {
        // given
        val list = mutableListOf<TravelHomepageItemModel>()
        list.add(TravelHomepageProductCardModel())
        list.add(TravelHomepageLegoBannerModel())
        coEvery {
            emptyUseCase.getTravelLayoutSubhomepage(any(), any())
        } returns Success(list)

        // when
        viewModel.getListFromCloud("", true)

        val result = HashMap<Type, Any>()
        result[TravelUnifiedSubhomepageData.Response::class.java] = TravelUnifiedSubhomepageData.Response()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse
        viewModel.getTravelUnifiedData("", TravelLayoutSubhomepage.Data(), true)

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[GraphqlError::class.java] = listOf(GraphqlError())
        val gqlResponseError = GraphqlResponse(HashMap<Type, Any?>(), errors, false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseError
        viewModel.getTravelUnifiedData("", TravelLayoutSubhomepage.Data(), true)

        // then
        viewModel.travelItemList.let {
            it[0].isLoaded shouldBe true
            it[1].isLoaded shouldBe false

            it[0].isSuccess shouldBe true
            it[1].isSuccess shouldBe false
        }
        viewModel.isAllError.value shouldBe null
    }

    @Test
    fun checkIfAllError_ifAllLoadedAndSuccess_ValueShouldReturnFalse() {
        // given
        val list = mutableListOf<TravelHomepageItemModel>()
        list.add(TravelHomepageProductCardModel())
        list.add(TravelHomepageLegoBannerModel())
        viewModel.travelItemList = list

        val result = HashMap<Type, Any>()
        result[TravelUnifiedSubhomepageData.Response::class.java] = TravelUnifiedSubhomepageData.Response()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse
        viewModel.getTravelUnifiedData("", TravelLayoutSubhomepage.Data(), true)

        val errors = HashMap<Type, List<GraphqlError>>()
        errors[GraphqlError::class.java] = listOf(GraphqlError())
        val gqlResponseError = GraphqlResponse(HashMap<Type, Any?>(), errors, false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseError
        viewModel.getTravelUnifiedData("", TravelLayoutSubhomepage.Data(), true)

        // then
        viewModel.isAllError.value shouldBe null
    }
}