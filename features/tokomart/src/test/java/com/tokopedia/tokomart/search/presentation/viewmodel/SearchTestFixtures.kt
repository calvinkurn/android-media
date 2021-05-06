package com.tokopedia.tokomart.search.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokomart.search.domain.model.SearchModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.UseCase
import io.mockk.mockk
import org.junit.Rule

open class SearchTestFixtures {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    protected val getSearchFirstPageUseCase = mockk<UseCase<SearchModel>>(relaxed = true)

    protected val searchViewModel = SearchViewModel(
            CoroutineTestDispatchersProvider,
            getSearchFirstPageUseCase,
    )

}