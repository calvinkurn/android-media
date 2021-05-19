package com.tokopedia.tokomart.search.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.tokomart.search.domain.model.SearchModel
import com.tokopedia.tokomart.searchcategory.utils.ChooseAddressWrapper
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
    protected val getFilterUseCase = mockk<UseCase<DynamicFilterModel>>(relaxed = true)
    protected val chooseAdddressWrapper = mockk<ChooseAddressWrapper>(relaxed = true)
    protected lateinit var searchViewModel: SearchViewModel

    @Before
    open fun setUp() {
        `Given search view model`()
    }

    protected fun `Given search view model`(queryParamMap: Map<String, String> = defaultQueryParamMap) {
        searchViewModel = SearchViewModel(
                CoroutineTestDispatchersProvider,
                queryParamMap,
                getSearchFirstPageUseCase,
                getSearchLoadMorePageUseCase,
                getFilterUseCase,
                chooseAdddressWrapper,
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

    protected fun `Given view already created`() {
        searchViewModel.onViewCreated()
    }

    protected fun `When view created`() {
        searchViewModel.onViewCreated()
    }
}