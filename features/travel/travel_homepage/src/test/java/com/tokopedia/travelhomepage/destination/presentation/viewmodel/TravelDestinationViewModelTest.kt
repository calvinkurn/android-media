package com.tokopedia.travelhomepage.destination.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.travel.utils.TravelTestDispatcherProvider
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.travelhomepage.destination.usecase.GetEmptyModelsUseCase
import com.tokopedia.travelhomepage.shouldBe
import com.tokopedia.usecase.coroutines.Fail
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

    @Before
    fun setup() {
        viewModel = TravelDestinationViewModel(graphqlRepository, GetEmptyModelsUseCase(), TravelTestDispatcherProvider())
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
    fun onGetDestinationCityData_CityModelShouldRepresentFailedFetch() {
        // given
        coEvery { graphqlRepository.getReseponse(any()) } coAnswers { throw Throwable() }

        // when
        viewModel.getDestinationCityData("", "")

        // then
        viewModel.travelDestinationCityModel.value is Fail
    }

}
