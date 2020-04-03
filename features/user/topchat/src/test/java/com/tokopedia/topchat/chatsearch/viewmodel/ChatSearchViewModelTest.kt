package com.tokopedia.topchat.chatsearch.viewmodel

import androidx.lifecycle.Observer
import com.tokopedia.topchat.InstantTaskExecutorRuleSpek
import com.tokopedia.topchat.chatsearch.data.GetChatSearchResponse
import com.tokopedia.topchat.chatsearch.data.SearchResult
import com.tokopedia.topchat.chatsearch.usecase.GetSearchQueryUseCase
import io.mockk.*
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object ChatSearchViewModelTest : Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Chat search") {

        val exQuery = "tokopedia"
        val exPage = 2
        val exGetChatSearchResponse = GetChatSearchResponse()
        val exThrowable = Throwable()

        val loadInitialDataObserver by memoized { mockk<Observer<Boolean>>(relaxed = true) }
        val triggerSearchObserver by memoized { mockk<Observer<String>>(relaxed = true) }
        val emptyQueryObserver by memoized { mockk<Observer<Boolean>>(relaxed = true) }
        val errorMessageObserver by memoized { mockk<Observer<Throwable>>(relaxed = true) }
        val searchResultObserver by memoized { mockk<Observer<List<SearchResult>>>(relaxed = true) }
        val getSearchQueryUseCase by memoized { mockk<GetSearchQueryUseCase>(relaxed = true) }
        val viewModel by memoized { ChatSearchViewModel(Dispatchers.Unconfined, getSearchQueryUseCase) }

        Scenario("User input new query") {
            Given("New query") {
                viewModel.loadInitialData.observeForever(loadInitialDataObserver)
                viewModel.triggerSearch.observeForever(triggerSearchObserver)
            }

            When("onSearchQueryChanged() triggered") {
                viewModel.onSearchQueryChanged(exQuery)
            }

            Then("Search triggered") {
                verifyOrder {
                    loadInitialDataObserver.onChanged(true)
                    triggerSearchObserver.onChanged(exQuery)
                    getSearchQueryUseCase.doSearch(any(), any(), exQuery, 1)
                }
            }
        }

        Scenario("Success load new query") {
            Given("New query") {
                viewModel.searchResult.observeForever(searchResultObserver)
                every { getSearchQueryUseCase.doSearch(any(), any(), any(), any()) } answers {
                    val onSuccess = firstArg<(GetChatSearchResponse) -> Unit>()
                    onSuccess.invoke(exGetChatSearchResponse)
                }
            }

            When("onSearchQueryChanged() triggered") {
                viewModel.onSearchQueryChanged(exQuery)
            }

            Then("Search results are updated") {
                verify { searchResultObserver.onChanged(exGetChatSearchResponse.searchResults) }
            }
        }

        Scenario("Fail load new query") {
            Given("New query") {
                viewModel.errorMessage.observeForever(errorMessageObserver)
                every { getSearchQueryUseCase.doSearch(any(), any(), any(), any()) } answers {
                    val onError = secondArg<(Throwable) -> Unit>()
                    onError.invoke(exThrowable)
                }
            }

            When("onSearchQueryChanged() triggered") {
                viewModel.onSearchQueryChanged(exQuery)
            }

            Then("Error message is updated") {
                verify { errorMessageObserver.onChanged(exThrowable) }
            }
        }

        Scenario("User input new query") {
            Given("New query") {
                every { getSearchQueryUseCase.isSearching } returns true
                viewModel.loadInitialData.observeForever(loadInitialDataObserver)
                viewModel.triggerSearch.observeForever(triggerSearchObserver)
            }

            When("onSearchQueryChanged() triggered when previous search query still loading") {
                viewModel.onSearchQueryChanged(exQuery)
            }

            Then("Cancel previous search and new search triggered") {
                verifyOrder {
                    getSearchQueryUseCase.cancelRunningSearch()
                    loadInitialDataObserver.onChanged(true)
                    triggerSearchObserver.onChanged(exQuery)
                    getSearchQueryUseCase.doSearch(any(), any(), exQuery, 1)
                }
            }
        }

        Scenario("User input empty query") {
            Given("Empty query") {
                viewModel.emptyQuery.observeForever(emptyQueryObserver)
            }

            When("onSearchQueryChanged() with empty query") {
                viewModel.onSearchQueryChanged(exQuery)
                viewModel.onSearchQueryChanged("")
            }

            Then("Cancel running search and emit emptyQuery true") {
                verifyOrder {
                    getSearchQueryUseCase.cancelRunningSearch()
                    emptyQueryObserver.onChanged(true)
                }
                verify(exactly = 1) { getSearchQueryUseCase.doSearch(any(), any(), any(), any()) }
            }
        }

        Scenario("User input the same query") {
            Given("Same query") {
                viewModel.emptyQuery.observeForever(emptyQueryObserver)
            }

            When("onSearchQueryChanged() with same previous query") {
                viewModel.onSearchQueryChanged("")
            }

            Then("Do nothing") {
                verify { getSearchQueryUseCase wasNot Called }
            }
        }

        Scenario("User load next page query") {
            Given("Request next page query") {
                every { getSearchQueryUseCase.hasNext } returns true
            }

            When("Load next page query triggered") {
                viewModel.loadPage(exPage)
            }

            Then("Search for next page triggered") {
                verify { getSearchQueryUseCase.doSearch(any(), any(), any(), exPage) }
            }
        }

        Scenario("User retry load page") {
            Given("Previous result error and can retry") {
                viewModel.errorMessage.observeForever(errorMessageObserver)
                every { getSearchQueryUseCase.doSearch(any(), any(), any(), any()) } answers {
                    val onError = secondArg<(Throwable) -> Unit>()
                    onError.invoke(exThrowable)
                }
                viewModel.onSearchQueryChanged(exQuery)
            }

            When("User retry load previously fail loaded page") {
                viewModel.loadPage(1)
            }

            Then("load previously fail loaded page") {
                verify(exactly = 2) { getSearchQueryUseCase.doSearch(any(), any(), exQuery, 1) }
            }
        }

        Scenario("Is first page") {
            When("New query triggered") {
                viewModel.onSearchQueryChanged(exQuery)
            }
            Then("Is first page return true") {
                assertEquals(true, viewModel.isFirstPage())
            }
            Given("Has next page") {
                every { getSearchQueryUseCase.hasNext } returns true
            }
            When("User load next page") {
                viewModel.loadPage(exPage)
            }
            Then("Is first page return true") {
                assertEquals(false, viewModel.isFirstPage())
            }
        }

        Scenario("Get hasNext page status") {
            Given("Does not has hasNext") {
                every { getSearchQueryUseCase.hasNext } returns false
            }
            Then("hasNext is false") {
                assertEquals(false, viewModel.hasNext)
            }
            Given("Has next page") {
                every { getSearchQueryUseCase.hasNext } returns true
            }
            Then("Is first page return true") {
                assertEquals(true, viewModel.hasNext)
            }
        }

    }
})