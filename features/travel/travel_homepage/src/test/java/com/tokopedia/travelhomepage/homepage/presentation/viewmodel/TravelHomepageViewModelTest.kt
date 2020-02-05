package com.tokopedia.travelhomepage.homepage.presentation.viewmodel

import com.tokopedia.common.travel.utils.TravelTestDispatcherProvider
import com.tokopedia.travelhomepage.homepage.InstantTaskExecutorRuleSpek
import com.tokopedia.travelhomepage.homepage.shouldBeEquals
import com.tokopedia.travelhomepage.homepage.usecase.GetEmptyViewModelsUseCase
import io.mockk.mockk
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * @author by furqan on 04/02/2020
 */
class TravelHomepageViewModelTest : Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Travel Homepage View Model") {
        Scenario("Create Travel Homepage View Model with Initial List") {
            val viewModel = TravelHomepageViewModel(mockk(), GetEmptyViewModelsUseCase(), mockk(), mockk(), TravelTestDispatcherProvider())

            When("Create Travel Homepage and Create Initial Items") {
                viewModel.getIntialList(true)
            }

            Then("Verify initial item is 6 items with isLoadFromCloud true for shimmering") {
                viewModel.travelItemList.value!!.size shouldBeEquals 6

                viewModel.travelItemList.value!!.forEach {
                    it.isLoadFromCloud shouldBeEquals true
                }
            }
        }

    }
})