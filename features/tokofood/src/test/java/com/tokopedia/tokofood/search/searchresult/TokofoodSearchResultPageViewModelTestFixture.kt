package com.tokopedia.tokofood.search.searchresult

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.feature.search.searchresult.domain.mapper.TokofoodFilterSortMapper
import com.tokopedia.tokofood.feature.search.searchresult.domain.mapper.TokofoodMerchantSearchResultMapper
import com.tokopedia.tokofood.feature.search.searchresult.domain.response.TokofoodFilterSortResponse
import com.tokopedia.tokofood.feature.search.searchresult.domain.response.TokofoodSearchMerchantResponse
import com.tokopedia.tokofood.feature.search.searchresult.domain.usecase.TokofoodFilterSortUseCase
import com.tokopedia.tokofood.feature.search.searchresult.domain.usecase.TokofoodSearchMerchantUseCase
import com.tokopedia.tokofood.feature.search.searchresult.presentation.viewmodel.TokofoodSearchResultPageViewModel
import com.tokopedia.tokofood.utils.JsonResourcesUtil
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.mockito.ArgumentMatchers.anyString

open class TokofoodSearchResultPageViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    lateinit var tokofoodSearchMerchantUseCase: TokofoodSearchMerchantUseCase

    @RelaxedMockK
    lateinit var tokofoodFilterSortUseCase: TokofoodFilterSortUseCase

    @RelaxedMockK
    lateinit var tokofoodMerchantSearchResultMapper: TokofoodMerchantSearchResultMapper

    @RelaxedMockK
    lateinit var tokofoodFilterSortMapper: TokofoodFilterSortMapper

    protected lateinit var viewModel: TokofoodSearchResultPageViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic(Uri::class)
        viewModel = TokofoodSearchResultPageViewModel(
            tokofoodSearchMerchantUseCase,
            tokofoodFilterSortUseCase,
            tokofoodMerchantSearchResultMapper,
            tokofoodFilterSortMapper,
            CoroutineTestDispatchersProvider
        )
    }

    @After
    fun cleanUp() {
        unmockkAll()
        unmockkStatic(Uri::class)
    }

    protected fun getQuickFilterResponse() =
        JsonResourcesUtil.createSuccessResponse<TokofoodFilterSortResponse>(QUICK_FILTER_JSON)

    protected fun getDetailFilterResponse() =
        JsonResourcesUtil.createSuccessResponse<TokofoodFilterSortResponse>(DETAIL_FILTER_JSON)

    protected fun getSearchResultResponse() =
        JsonResourcesUtil.createSuccessResponse<TokofoodSearchMerchantResponse>(SEARCH_RESULT_JSON)

    protected fun onGetFilter_shouldReturn(
        filterType: String,
        response: TokofoodFilterSortResponse
    ) {
        coEvery {
            tokofoodFilterSortUseCase.execute(filterType)
        } returns response.tokofoodFilterAndSort
    }

    protected fun onGetFilter_shouldThrow(
        filterType: String,
        throwable: Throwable
    ) {
        coEvery {
            tokofoodFilterSortUseCase.execute(filterType)
        } throws throwable
    }

    protected fun onGetSearchResult_shouldReturn(
        localCacheModel: LocalCacheModel?,
        searchParameter: SearchParameter?,
        response: TokofoodSearchMerchantResponse
    ) {
        coEvery {
            tokofoodSearchMerchantUseCase.execute(localCacheModel, any(), anyString())
        } returns response
    }

    protected fun onGetSearchResult_shouldThrow(
        localCacheModel: LocalCacheModel?,
        searchParameter: SearchParameter?,
        throwable: Throwable
    ) {
        coEvery {
            tokofoodSearchMerchantUseCase.execute(localCacheModel, any(), anyString())
        } throws throwable
    }

    companion object {
        const val QUICK_FILTER_JSON = "json/search/searchresult/quick_filter.json"
        const val DETAIL_FILTER_JSON = "json/search/searchresult/detail_filter.json"
        const val SEARCH_RESULT_JSON = "json/search/searchresult/search_result.json"
    }

}