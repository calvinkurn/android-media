package com.tokopedia.tokofood.search.initialstate

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokofood.feature.search.initialstate.domain.mapper.TokoFoodInitStateSearchMapper
import com.tokopedia.tokofood.feature.search.initialstate.domain.usecase.GetInitSearchStateUseCase
import com.tokopedia.tokofood.feature.search.initialstate.domain.usecase.RemoveSearchHistoryUseCase
import com.tokopedia.tokofood.feature.search.initialstate.presentation.viewmodel.InitialStateSearchViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Rule

open class InitialSearchStateViewModelTestFixture {

    @RelaxedMockK
    lateinit var initialStateUseCase: Lazy<GetInitSearchStateUseCase>

    @RelaxedMockK
    lateinit var removeSearchHistoryUseCase: Lazy<RemoveSearchHistoryUseCase>

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    protected lateinit var viewModel: InitialStateSearchViewModel
    protected lateinit var mapper: TokoFoodInitStateSearchMapper

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mapper = TokoFoodInitStateSearchMapper()
        viewModel = InitialStateSearchViewModel(
            CoroutineTestDispatchersProvider,
            initialStateUseCase,
            removeSearchHistoryUseCase
        )
    }

    @After
    fun finish() {
        unmockkAll()
    }

    companion object {
        const val INITIAL_STATE_SUCCESS = "json/search/initialstate/initial_state_success.json"
        const val REMOVE_RECENT_SEARCH_SUCCESS = "json/search/initialstate/remove_recent_search.json"
    }

}