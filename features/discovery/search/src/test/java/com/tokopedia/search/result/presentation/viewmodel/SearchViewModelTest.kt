package com.tokopedia.search.result.presentation.viewmodel

import com.tokopedia.search.InstantTaskExecutorRuleSpek
import com.tokopedia.search.result.TestDispatcherProvider
import com.tokopedia.search.result.presentation.model.ChildViewVisibilityChangedModel
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationListener
import com.tokopedia.search.shouldBe
import io.mockk.mockk
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

        Scenario("handle shop view visibilty changed to visible") {
            lateinit var searchViewModel: SearchViewModel
            val productSearchNavigationOnClick = mockk<SearchNavigationListener.ClickListener>()
            val shopSearchNavigationOnClick = mockk<SearchNavigationListener.ClickListener>()
            val catalogSearchNavigationOnClick = mockk<SearchNavigationListener.ClickListener>()
            val profileSearchNavigationOnClick = null

            val productListVisibilityModel = ChildViewVisibilityChangedModel(
                    isChildViewVisibleToUser = false,
                    isChildViewReady = true,
                    isFilterEnabled = true,
                    isSortEnabled = true,
                    searchNavigationOnClickListener = productSearchNavigationOnClick
            )
            val catalogListVisibilityModel = ChildViewVisibilityChangedModel(
                    isChildViewVisibleToUser = false,
                    isChildViewReady = true,
                    isFilterEnabled = true,
                    isSortEnabled = false,
                    searchNavigationOnClickListener = shopSearchNavigationOnClick
            )
            val shopListVisibilityModel = ChildViewVisibilityChangedModel(
                    isChildViewVisibleToUser = true,
                    isChildViewReady = true,
                    isFilterEnabled = true,
                    isSortEnabled = false,
                    searchNavigationOnClickListener = catalogSearchNavigationOnClick
            )
            val profileListVisibilityModel = ChildViewVisibilityChangedModel(
                    isChildViewVisibleToUser = false,
                    isChildViewReady = true,
                    isFilterEnabled = false,
                    isSortEnabled = false,
                    searchNavigationOnClickListener = profileSearchNavigationOnClick
            )

            Given("search view model") {
                searchViewModel = SearchViewModel(TestDispatcherProvider())
            }

            When("handle child views visibility changed") {
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

        Scenario("handle no child is visible or ready") {
            lateinit var searchViewModel: SearchViewModel
            val productSearchNavigationOnClick = mockk<SearchNavigationListener.ClickListener>()
            val shopSearchNavigationOnClick = mockk<SearchNavigationListener.ClickListener>()
            val catalogSearchNavigationOnClick = mockk<SearchNavigationListener.ClickListener>()
            val profileSearchNavigationOnClick = null

            val productListVisibilityModel = ChildViewVisibilityChangedModel(
                    isChildViewVisibleToUser = false,
                    isChildViewReady = false,
                    isFilterEnabled = true,
                    isSortEnabled = true,
                    searchNavigationOnClickListener = productSearchNavigationOnClick
            )
            val catalogListVisibilityModel = ChildViewVisibilityChangedModel(
                    isChildViewVisibleToUser = false,
                    isChildViewReady = false,
                    isFilterEnabled = true,
                    isSortEnabled = false,
                    searchNavigationOnClickListener = shopSearchNavigationOnClick
            )
            val shopListVisibilityModel = ChildViewVisibilityChangedModel(
                    isChildViewVisibleToUser = false,
                    isChildViewReady = false,
                    isFilterEnabled = true,
                    isSortEnabled = false,
                    searchNavigationOnClickListener = catalogSearchNavigationOnClick
            )
            val profileListVisibilityModel = ChildViewVisibilityChangedModel(
                    isChildViewVisibleToUser = false,
                    isChildViewReady = false,
                    isFilterEnabled = false,
                    isSortEnabled = false,
                    searchNavigationOnClickListener = profileSearchNavigationOnClick
            )

            Given("search view model") {
                searchViewModel = SearchViewModel(TestDispatcherProvider())
            }

            When("handle child views visibility changed") {
                searchViewModel.onChildViewVisibilityChanged(productListVisibilityModel)
                searchViewModel.onChildViewVisibilityChanged(shopListVisibilityModel)
                searchViewModel.onChildViewVisibilityChanged(catalogListVisibilityModel)
                searchViewModel.onChildViewVisibilityChanged(profileListVisibilityModel)
            }

            Then("should not post child view visibility event") {
                val childViewVisibilityEvent = searchViewModel.getChildViewVisibleEventLiveData().value

                childViewVisibilityEvent?.getContentIfNotHandled() shouldBe null
            }
        }
    }
})