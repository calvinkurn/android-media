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
        viewModel.travelItemList.value!!.size shouldBe 2
        viewModel.travelItemList.value!!.forEach {
            it.isLoaded shouldBe false
            it.isLoadFromCloud shouldBe true
        }
        viewModel.isAllError.value shouldBe false
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
        viewModel.travelItemList.value shouldBe null
        viewModel.isAllError.value shouldBe null
    }

    @Test
    fun onGetTravelUnifiedData_AllSuccess_ItemModelShouldRepresentFailedToFetch() {
        // given
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(TravelUnifiedSubhomepageData.Response::class.java to TravelUnifiedSubhomepageData.Response()),
                mapOf(), false)
        val list = mutableListOf<TravelHomepageItemModel>()
        list.add(TravelHomepageProductCardModel())
        list.add(TravelHomepageBannerModel())
        viewModel.travelItemList.value = list

        // when
        viewModel.getTravelUnifiedData("", TravelLayoutSubhomepage.Data(), 0, true)
        viewModel.getTravelUnifiedData("", TravelLayoutSubhomepage.Data(), 1, true)

        // then
        viewModel.travelItemList.value!!.forEach {
            it.isLoaded shouldBe true
            it.isSuccess shouldBe true
        }
        viewModel.isAllError.value shouldBe false
    }

    @Test
    fun onGetTravelUnifiedData_FailedToFetchData_ItemModelShouldRepresentFailedToFetch() {
        // given
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(),
                mapOf(GraphqlError::class.java to listOf(GraphqlError())), false)
        val list = mutableListOf<TravelHomepageItemModel>()
        list.add(TravelHomepageProductCardModel())
        viewModel.travelItemList.value = list

        // when
        viewModel.getTravelUnifiedData("", TravelLayoutSubhomepage.Data(), 0, true)

        // then
        viewModel.travelItemList.value!!.forEach {
            it.isLoaded shouldBe true
            it.isSuccess shouldBe true
        }
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[0].isLoaded shouldBe true
        (viewModel.travelItemList.value as List<TravelHomepageItemModel>)[0].isSuccess shouldBe false
        viewModel.isAllError.value shouldBe true
    }

    @Test
    fun checkIfAllError_ifAllLoadedAndSuccess_ValueShouldReturnFalse() {
        // given
        val list = mutableListOf<TravelHomepageItemModel>()
        list.add(TravelHomepageProductCardModel())
        list.add(TravelHomepageLegoBannerModel())
        viewModel.travelItemList.value = list

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(TravelUnifiedSubhomepageData.Response::class.java to TravelUnifiedSubhomepageData.Response()),
                mapOf(), false)
        viewModel.getTravelUnifiedData("", TravelLayoutSubhomepage.Data(), 0, true)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns GraphqlResponse(
                mapOf(),
                mapOf(GraphqlError::class.java to listOf(GraphqlError())), false)
        viewModel.getTravelUnifiedData("", TravelLayoutSubhomepage.Data(), 1, true)

        // then
        viewModel.isAllError.value shouldBe false
    }
}