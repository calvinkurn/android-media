package com.tokopedia.tokofood.search.searchbar

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokofood.feature.search.container.presentation.viewmodel.SearchContainerViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.CoroutineTestRule
import org.junit.Before
import org.junit.Rule

open class SearchContainerViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    protected lateinit var viewModel: SearchContainerViewModel

    @Before
    fun setUp() {
        viewModel = SearchContainerViewModel(CoroutineTestDispatchersProvider)
    }
}