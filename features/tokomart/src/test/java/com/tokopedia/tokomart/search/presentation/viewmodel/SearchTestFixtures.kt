package com.tokopedia.tokomart.search.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.tokomart.search.domain.model.SearchModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.Before
import org.junit.Rule

open class SearchTestFixtures {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    protected val defaultKeyword = "samsung"
    protected val defaultQueryParamMap = mapOf(SearchApiConst.Q to defaultKeyword)
    protected val getSearchFirstPageUseCase = mockk<UseCase<SearchModel>>(relaxed = true)
    protected val getSearchLoadMorePageUseCase = mockk<UseCase<SearchModel>>(relaxed = true)
    protected lateinit var searchViewModel: SearchViewModel

    @Before
    open fun setUp() {
        setUpViewModel()
    }

    protected fun setUpViewModel(queryParamMap: Map<String, String> = defaultQueryParamMap) {
        searchViewModel = SearchViewModel(
                CoroutineTestDispatchersProvider,
                queryParamMap,
                getSearchFirstPageUseCase,
                getSearchLoadMorePageUseCase,
        )
    }

    protected fun `Given get search first page use case will be successful`(
            searchModel: SearchModel,
            requestParamsSlot: CapturingSlot<RequestParams> = slot()
    ) {
        every {
            getSearchFirstPageUseCase.execute(any(), any(), capture(requestParamsSlot))
        } answers {
            firstArg<(SearchModel) -> Unit>().invoke(searchModel)
        }
    }
}