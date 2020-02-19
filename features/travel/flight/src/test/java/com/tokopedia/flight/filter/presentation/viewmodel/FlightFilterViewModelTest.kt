package com.tokopedia.flight.filter.presentation.viewmodel

import com.tokopedia.common.travel.utils.TravelTestDispatcherProvider
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.shouldBe
import io.mockk.mockk
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * @author by furqan on 19/02/2020
 */
class FlightFilterViewModelTest : Spek({
    Feature("Create Flight Filter View Model") {
        Scenario("Without Init") {
            var viewModel: FlightFilterViewModel? = null

            When("Create Flight Filter View Model Object") {
                viewModel = FlightFilterViewModel(mockk(), mockk(), TravelTestDispatcherProvider())
            }

            Then("View Model should be not null") {
                (viewModel == null) shouldBe false
            }

            Then("All Value should be null") {
                viewModel?.filterModel?.value shouldBe null
                viewModel?.statisticModel?.value shouldBe null
            }
        }

        Scenario("With Init") {
            val filterViewModel = mockk<FlightFilterModel>()
            var viewModel: FlightFilterViewModel? = null

            When("Create Flight Filter View Model Object") {
                viewModel = FlightFilterViewModel(mockk(), mockk(), TravelTestDispatcherProvider())
            }

            When("Init method called") {
                viewModel?.init(filterViewModel)
            }

            Then("View Model should be not null") {
                (viewModel == null) shouldBe false
            }

            Then("Filter Model Value should be same as filter from init") {
                viewModel?.filterModel?.value shouldBe filterViewModel
            }
        }
    }
})