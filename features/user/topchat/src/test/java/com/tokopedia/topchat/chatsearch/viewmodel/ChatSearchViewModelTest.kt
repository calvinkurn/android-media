package com.tokopedia.topchat.chatsearch.viewmodel

import androidx.lifecycle.Observer
import com.tokopedia.topchat.InstantTaskExecutorRuleSpek
import com.tokopedia.topchat.chatsearch.usecase.GetSearchQueryUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object ChatSearchViewModelTest : Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Chat search") {

        val exQuery = "tokopedia"
        val loadInitialDataObserver by memoized { mockk<Observer<Boolean>>(relaxed = true) }
        val triggerSearchObserver by memoized { mockk<Observer<String>>(relaxed = true) }
        val getSearchQueryUseCase by memoized { mockk<GetSearchQueryUseCase>() }
        val viewModel by memoized { ChatSearchViewModel(Dispatchers.Unconfined, getSearchQueryUseCase) }

        Scenario("User input new query") {
            Given("New query") {
                every { getSearchQueryUseCase.isSearching } returns false
                every { getSearchQueryUseCase.doSearch(any(), any(), any(), any()) } just Runs
                viewModel.loadInitialData.observeForever(loadInitialDataObserver)
                viewModel.triggerSearch.observeForever(triggerSearchObserver)
            }

            When("onSearchQueryChanged() triggered") {
                viewModel.onSearchQueryChanged(exQuery)
            }

            Then("Search triggered") {
                verify { loadInitialDataObserver.onChanged(true) }
            }
        }
    }
})