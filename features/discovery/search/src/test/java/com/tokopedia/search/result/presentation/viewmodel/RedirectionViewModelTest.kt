package com.tokopedia.search.result.presentation.viewmodel

import com.tokopedia.search.result.InstantTaskExecutorRuleSpek
import com.tokopedia.search.result.TestDispatcherProvider
import com.tokopedia.search.shouldBe
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class RedirectionViewModelTest: Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Handle show auto complete view") {

        Scenario("handle show auto complete view") {
            lateinit var redirectionViewModel: RedirectionViewModel

            Given("redirection view model") {
                redirectionViewModel = RedirectionViewModel(TestDispatcherProvider())
            }

            When("show auto complete view") {
                redirectionViewModel.showAutoCompleteView()
            }

            Then("get show auto complete view event") {
                val autoCompleteEvent = redirectionViewModel.getShowAutoCompleteViewEventLiveData().value

                autoCompleteEvent?.getContentIfNotHandled() shouldBe true
            }
        }
    }
})