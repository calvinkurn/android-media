package com.tokopedia.travelhomepage.destination.presentation.viewmodel

import com.tokopedia.common.travel.utils.TravelTestDispatcherProvider
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.travelhomepage.InstantTaskExecutorRuleSpek
import com.tokopedia.travelhomepage.destination.usecase.GetEmptyModelsUseCase
import com.tokopedia.travelhomepage.shouldBeEquals
import com.tokopedia.usecase.coroutines.Fail
import io.mockk.coEvery
import io.mockk.mockk
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * @author by furqan on 15/02/2020
 */
class TravelDestinationViewModelTest : Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Create Travel Destination View Model") {
        Scenario("Create Travel Destination View Model with Initial List") {
            val viewModel = TravelDestinationViewModel(mockk(), GetEmptyModelsUseCase(), TravelTestDispatcherProvider())

            When("Create Travel Destination and Create Initial Items") {
                viewModel.getInitialList()
            }

            Then("Verify initial item have 6 items") {
                viewModel.travelDestinationItemList.value!!.size shouldBeEquals 6
                viewModel.isAllError.value shouldBeEquals null
            }
        }

        Scenario("Create Travel Homepage View Model without Initial List") {
            val viewModel = TravelDestinationViewModel(mockk(), mockk(), TravelTestDispatcherProvider())

            Then("all value should be null") {
                viewModel.travelDestinationItemList.value shouldBeEquals null
                viewModel.travelDestinationCityModel.value shouldBeEquals null
                viewModel.isAllError.value shouldBeEquals null
            }
        }
    }

    Feature("Handle Fetch Destination City Data") {
        Scenario("Fetch Destination City Failed") {
            val graphqlRepository = mockk<GraphqlRepository>()
            val viewModel = TravelDestinationViewModel(graphqlRepository, GetEmptyModelsUseCase(), TravelTestDispatcherProvider())

            Given("Fetch Destination City throw Error") {
                coEvery { graphqlRepository.getReseponse(any()) } coAnswers { throw Throwable() }
            }

            When("Fetch Destination City Data") {
                viewModel.getDestinationCityData("", "")
            }

            Then("City Model should be instance of Fail") {
                viewModel.travelDestinationCityModel.value is Fail
            }
        }

    }
})