package com.tokopedia.search.result.presentation.viewmodel

import com.tokopedia.search.result.InstantTaskExecutorRuleSpek
import com.tokopedia.search.result.TestDispatcherProvider
import com.tokopedia.search.result.presentation.model.ChildViewVisibilityModel
import com.tokopedia.search.shouldBe
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class SearchViewModelTest: Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Handle show auto complete view") {

        Scenario("handle show auto complete view") {
            lateinit var searchViewModel: SearchViewModel

            Given("search view model") {
                searchViewModel = SearchViewModel(TestDispatcherProvider())
            }

            When("show auto complete view") {
                searchViewModel.showAutoCompleteView()
            }

            Then("validate show auto complete view event") {
                val autoCompleteEvent = searchViewModel.getShowAutoCompleteViewEventLiveData().value

                autoCompleteEvent?.getContentIfNotHandled() shouldBe true
            }
        }
    }

    Feature("Handle hide search page loading") {

        Scenario("handle hide search page loading") {
            lateinit var searchViewModel: SearchViewModel

            Given("search view model") {
                searchViewModel = SearchViewModel(TestDispatcherProvider())
            }

            When("hide search page loading") {
                searchViewModel.hideSearchPageLoading()
            }

            Then("validate hide search page loading event") {
                val hideLoadingEvent = searchViewModel.getHideLoadingEventLiveData().value

                hideLoadingEvent?.getContentIfNotHandled() shouldBe true
            }
        }
    }

    Feature("Handle child view visibility changed") {

        Scenario("handle child view visibilty changed") {
            lateinit var searchViewModel: SearchViewModel
            val productListVisibilityModel = ChildViewVisibilityModel(isChildViewVisibleToUser = false, isChildViewReady = true, isFilterEnabled = true, isSortEnabled = true)
            val catalogListVisibilityModel = ChildViewVisibilityModel(isChildViewVisibleToUser = false, isChildViewReady = true, isFilterEnabled = true, isSortEnabled = false)
            val shopListVisibilityModel = ChildViewVisibilityModel(isChildViewVisibleToUser = true, isChildViewReady = true, isFilterEnabled = true, isSortEnabled = false)
            val profileListVisibilityModel = ChildViewVisibilityModel(isChildViewVisibleToUser = false, isChildViewReady = true, isFilterEnabled = false, isSortEnabled = false)

            Given("search view model") {
                searchViewModel = SearchViewModel(TestDispatcherProvider())
            }

            When("handle child views visibilty changed") {
                searchViewModel.onChildViewVisibilityChanged(productListVisibilityModel)
                searchViewModel.onChildViewVisibilityChanged(shopListVisibilityModel)
                searchViewModel.onChildViewVisibilityChanged(catalogListVisibilityModel)
                searchViewModel.onChildViewVisibilityChanged(profileListVisibilityModel)
            }

            Then("should post event for visible child view") {
                val childViewVisibilityEvent = searchViewModel.getChildViewVisibleEventLiveData().value

                childViewVisibilityEvent?.getContentIfNotHandled() shouldBe shopListVisibilityModel
            }
        }
    }
})