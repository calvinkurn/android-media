package com.tokopedia.tokofood.search.searchresult

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodProgressBarUiModel
import com.tokopedia.tokofood.feature.search.common.presentation.uimodel.TokofoodSearchErrorStateUiModel
import com.tokopedia.tokofood.feature.search.searchresult.domain.usecase.TokofoodFilterSortUseCase
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchEmptyWithFilterUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchEmptyWithoutFilterUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchOOCUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchResultUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.PriceRangeChipUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.PriceRangeFilterCheckboxItemUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodQuickSortUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodSearchUiEvent
import com.tokopedia.tokofood.utils.collectFromSharedFlow
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class TokofoodSearchResultPageViewModelTest : TokofoodSearchResultPageViewModelTestFixture() {

    @Test
    fun `when setKeyword should set searchParameter`() {
        runBlocking {
            val keyword = "mcd"

            viewModel.searchParameterMap.collectFromSharedFlow(
                whenAction = {
                    viewModel.setKeyword(keyword)
                },
                then = { param ->
                    val actualKeywordParam = param?.get(SearchApiConst.Q)
                    Assert.assertEquals(keyword, actualKeywordParam)
                }
            )
        }
    }

    @Test
    fun `when setKeyword with length less than 3, should not set keyword param`() {
        runBlocking {
            val keyword = "mc"

            viewModel.searchParameterMap.collectFromSharedFlow(
                whenAction = {
                    viewModel.setKeyword(keyword)
                },
                then = { param ->
                    val actualKeywordParam = param?.get(SearchApiConst.Q)
                    Assert.assertNotEquals(keyword, actualKeywordParam)
                }
            )
        }
    }

    @Test
    fun `when setKeyword while search parameters existed, should also return both keyword and params`() {
        runBlocking {
            val keyword = "mcd"
            val expectedSearchParamKey = "pricing"
            val expectedSearchParamValue = "1"
            val searchParameter = hashMapOf(
                expectedSearchParamKey to expectedSearchParamValue
            )

            viewModel.searchParameterMap.collectFromSharedFlow(
                whenAction = {
                    viewModel.getInitialMerchantSearchResult(searchParameter)
                    viewModel.setKeyword(keyword)
                },
                then = { param ->
                    val actualKeywordParam = param?.get(SearchApiConst.Q)
                    val actualSearchParamValue = param?.get(expectedSearchParamKey)
                    Assert.assertEquals(keyword, actualKeywordParam)
                    Assert.assertEquals(expectedSearchParamValue, actualSearchParamValue)
                }
            )
        }
    }

    @Test
    fun `when getInitialMerchantSearchResult should success load search result visitables`() {
        runBlocking {
            val localCacheModel = LocalCacheModel(
                address_id = "123",
                lat = "1.23",
                long = "3.123"
            )
            val searchResult = getSearchResultResponse()
            val searchParameter = hashMapOf<String, String>()
            val expectedVisitables = searchResult.tokofoodSearchMerchant.merchants.map {
                MerchantSearchResultUiModel(
                    id = it.id,
                    merchant = it
                )
            }
            onGetSearchResult_shouldReturn(localCacheModel, searchParameter, null, searchResult)
            onGetSuccessLoadSearchResultInitial_shouldReturn(expectedVisitables)

            viewModel.visitables.collectFromSharedFlow(
                whenAction = {
                    viewModel.setLocalCacheModel(localCacheModel)
                    viewModel.getInitialMerchantSearchResult(searchParameter)
                },
                then = {
                    val actualVisitableCount = (it as? Success)?.data?.size
                    Assert.assertEquals(searchResult.tokofoodSearchMerchant.merchants.size, actualVisitableCount)
                }
            )
        }
    }

    @Test
    fun `when getInitialMerchantSearchResult but search param empty should not get merchant search result`() {
        runBlocking {
            val localCacheModel = LocalCacheModel(
                address_id = "123",
                lat = "1.23",
                long = "3.123"
            )

            viewModel.visitables.collectFromSharedFlow(
                whenAction = {
                    viewModel.setLocalCacheModel(localCacheModel)
                    viewModel.getInitialMerchantSearchResult(null)
                },
                then = {
                    assert(it == null)
                }
            )
        }
    }

    @Test
    fun `when getInitialMerchantSearchResult returns empty should show empty state`() {
        runBlocking {
            val localCacheModel = LocalCacheModel(
                address_id = "123",
                lat = "1.23",
                long = "3.123"
            )
            val searchResult = getSearchResultEmptyResponse()
            val searchParameter = hashMapOf<String, String>()
            onGetSearchResult_shouldReturn(localCacheModel, searchParameter, null, searchResult)

            viewModel.visitables.collectFromSharedFlow(
                whenAction = {
                    viewModel.setLocalCacheModel(localCacheModel)
                    viewModel.getInitialMerchantSearchResult(searchParameter)
                },
                then = {
                    assert((it as? Success)?.data?.getOrNull(Int.ZERO) is MerchantSearchEmptyWithoutFilterUiModel)
                }
            )
        }
    }

    @Test
    fun `when getInitialMerchantSearchResult returns empty and have param applid should show empty state`() {
        runBlocking {
            val localCacheModel = LocalCacheModel(
                address_id = "123",
                lat = "1.23",
                long = "3.123"
            )
            val searchResult = getSearchResultEmptyResponse()
            val searchParameter = hashMapOf("pricing" to "1")
            onGetSearchResult_shouldReturn(localCacheModel, searchParameter, null, searchResult)

            viewModel.visitables.collectFromSharedFlow(
                whenAction = {
                    viewModel.setLocalCacheModel(localCacheModel)
                    viewModel.getInitialMerchantSearchResult(searchParameter)
                },
                then = {
                    assert((it as? Success)?.data?.getOrNull(Int.ZERO) is MerchantSearchEmptyWithFilterUiModel)
                }
            )
        }
    }

    @Test
    fun `when getInitialMerchantSearchResult should fail load search result visitables`() {
        val localCacheModel = LocalCacheModel(
            address_id = "123",
            lat = "1.23",
            long = "3.123"
        )
        val throwable = MessageErrorException()
        val searchParameter = hashMapOf<String, String>()
        val expectedErrorUiModel = listOf(TokofoodSearchErrorStateUiModel(0))
        onGetSearchResult_shouldThrow(localCacheModel, searchParameter, null, throwable)
        onGetErrorSearchResultInitial_shouldReturn(throwable, expectedErrorUiModel)

        viewModel.visitables.collectFromSharedFlow(
            whenAction = {
                viewModel.setLocalCacheModel(localCacheModel)
                viewModel.getInitialMerchantSearchResult(searchParameter)
            },
            then = {
                val actualVisitable = (it as? Success)?.data
                Assert.assertEquals(expectedErrorUiModel.size, actualVisitable?.count())
                Assert.assertEquals(actualVisitable?.getOrNull(Int.ZERO), expectedErrorUiModel.getOrNull(Int.ZERO))
            }
        )
    }

    @Test
    fun `when loadQuickSortFilter if quick sort filter data hasn't been loaded, should success load filter`() {
        val sortFilterResponse = getQuickFilterResponse()
        val searchParameter = hashMapOf<String, String>()
        val expectedUiModels = getFilterSortUiModels(sortFilterResponse.tokofoodFilterAndSort)
        onGetFilter_shouldReturn(TokofoodFilterSortUseCase.TYPE_QUICK, sortFilterResponse)
        onGetQuickSortFilterUiModels_shouldReturn(sortFilterResponse.tokofoodFilterAndSort, expectedUiModels)

        viewModel.sortFilterUiModel.collectFromSharedFlow(
            whenAction = {
                viewModel.loadQuickSortFilter(searchParameter)
            },
            then = {
                val actualUiModels = (it as? Success)?.data
                expectedUiModels.forEachIndexed { index, expectedUiModel ->
                    Assert.assertEquals(expectedUiModel, actualUiModels?.getOrNull(index))
                }
            }
        )
    }

    @Test
    fun `when loadQuickSortFilter, should update applied filter count`() {
        val sortFilterResponse = getQuickFilterResponse()
        val searchParameter = hashMapOf<String, String>()
        val currentSearchParam = hashMapOf("pricing" to "1")
        val expectedUiModels = getFilterSortUiModels(sortFilterResponse.tokofoodFilterAndSort)
        onGetFilter_shouldReturn(TokofoodFilterSortUseCase.TYPE_QUICK, sortFilterResponse)
        onGetQuickSortFilterUiModels_shouldReturn(sortFilterResponse.tokofoodFilterAndSort, expectedUiModels)

        viewModel.appliedFilterCount.collectFromSharedFlow(
            whenAction = {
                viewModel.getInitialMerchantSearchResult(currentSearchParam)
                viewModel.loadQuickSortFilter(searchParameter)
            },
            then = {
                Assert.assertEquals(currentSearchParam.count(), it)
            }
        )
    }

    @Test
    fun `when loadQuickSortFilter if quick sort filter data hasn't been loaded and uimodels empty, should success load filter`() {
        val sortFilterResponse = getQuickFilterResponse()
        val searchParameter = hashMapOf<String, String>()
        onGetFilter_shouldReturn(TokofoodFilterSortUseCase.TYPE_QUICK, sortFilterResponse)
        onGetQuickSortFilterUiModels_shouldReturn(sortFilterResponse.tokofoodFilterAndSort, listOf())

        viewModel.sortFilterUiModel.collectFromSharedFlow(
            whenAction = {
                viewModel.loadQuickSortFilter(searchParameter)
                viewModel.loadQuickSortFilter(searchParameter)
            },
            then = {
                val actualUiModels = (it as? Success)?.data
                assert(actualUiModels?.isEmpty() == true)
            }
        )
    }

    @Test
    fun `when loadQuickSortFilter if quick sort filter data has been loaded but search param new, should success load filter`() {
        val sortFilterResponse = getQuickFilterResponse()
        val searchParameter = hashMapOf("pricing" to "1")
        val expectedUiModels = getFilterSortUiModels(sortFilterResponse.tokofoodFilterAndSort)
        val currentSearchParam = hashMapOf<String, String>()
        onGetFilter_shouldReturn(TokofoodFilterSortUseCase.TYPE_QUICK, sortFilterResponse)
        onGetQuickSortFilterUiModels_shouldReturn(sortFilterResponse.tokofoodFilterAndSort, expectedUiModels)

        viewModel.sortFilterUiModel.collectFromSharedFlow(
            whenAction = {
                viewModel.getInitialMerchantSearchResult(currentSearchParam)
                viewModel.loadQuickSortFilter(searchParameter)
                viewModel.loadQuickSortFilter(searchParameter)
            },
            then = {
                val actualUiModels = (it as? Success)?.data
                expectedUiModels.forEachIndexed { index, expectedUiModel ->
                    Assert.assertEquals(expectedUiModel, actualUiModels?.getOrNull(index))
                }
            }
        )
    }

    @Test
    fun `when loadQuickSortFilter if quick sort filter data has been loaded and search param is same, should not load filter`() {
        val sortFilterResponse = getQuickFilterResponse()
        val searchParameter = hashMapOf("pricing" to "1")
        val expectedUiModels = getFilterSortUiModels(sortFilterResponse.tokofoodFilterAndSort)
        onGetFilter_shouldReturn(TokofoodFilterSortUseCase.TYPE_QUICK, sortFilterResponse)
        onGetQuickSortFilterUiModels_shouldReturn(sortFilterResponse.tokofoodFilterAndSort, expectedUiModels)

        viewModel.sortFilterUiModel.collectFromSharedFlow(
            whenAction = {
                viewModel.getInitialMerchantSearchResult(searchParameter)
                viewModel.loadQuickSortFilter(searchParameter)
                viewModel.loadQuickSortFilter(searchParameter)
            },
            then = {
                val actualUiModels = (it as? Success)?.data
                assert(actualUiModels?.isEmpty() == true)
            }
        )
    }

    @Test
    fun `when loadQuickSortFilter, should failed load filter`() {
        val throwable = MessageErrorException()
        val searchParameter = hashMapOf<String, String>()
        onGetFilter_shouldThrow(TokofoodFilterSortUseCase.TYPE_QUICK, throwable)

        viewModel.sortFilterUiModel.collectFromSharedFlow(
            whenAction = {
                viewModel.loadQuickSortFilter(searchParameter)
            },
            then = {
                assert(it is Fail)
            }
        )
    }

    @Test
    fun `when resetFilterSearch, should reset search param map`() {
        val keyword = "mcd"
        val expectedSearchParamKey = "pricing"
        val expectedSearchParamValue = "1"
        val searchParameter = hashMapOf(
            expectedSearchParamKey to expectedSearchParamValue
        )

        viewModel.searchParameterMap.collectFromSharedFlow(
            whenAction = {
                viewModel.getInitialMerchantSearchResult(searchParameter)
                viewModel.setKeyword(keyword)
                viewModel.resetFilterSearch()
            },
            then = { param ->
                val actualKeywordParam = param?.get(SearchApiConst.Q)
                val actualSearchParamValue = param?.get(expectedSearchParamKey)
                Assert.assertEquals(keyword, actualKeywordParam)
                Assert.assertEquals(null, actualSearchParamValue)
            }
        )
    }

    @Test
    fun `when onScrollProductList and should load more, should load more`() {
        val localCacheModel = LocalCacheModel(
            address_id = "123",
            lat = "1.23",
            long = "3.123"
        )
        val searchResult = getSearchResultResponse()
        val nextPageKey = "123"
        val hasNextPageSearchResult = searchResult.copy(
            tokofoodSearchMerchant = searchResult.tokofoodSearchMerchant.copy(
                nextPageKey = nextPageKey
            )
        )
        val searchParameter = hashMapOf<String, String>()
        val expectedVisitables = hasNextPageSearchResult.tokofoodSearchMerchant.merchants.map {
            MerchantSearchResultUiModel(
                id = it.id,
                merchant = it
            )
        }
        val finalExpectedVisitables = expectedVisitables + expectedVisitables
        val loadMoreVisitables = expectedVisitables + listOf(TokoFoodProgressBarUiModel(""))
        onGetSearchResult_shouldReturn(localCacheModel, searchParameter, null, hasNextPageSearchResult)
        onGetSearchResult_shouldReturn(localCacheModel, searchParameter, nextPageKey, hasNextPageSearchResult)
        onGetSuccessLoadSearchResultInitial_shouldReturn(expectedVisitables)
        onGetLoadMoreVisitables_shouldReturn(expectedVisitables, loadMoreVisitables)
        onGetSuccessLoadSearchResultMore_shouldReturn(finalExpectedVisitables)
        onGetIsVisitableContainOtherStates_shouldReturn(false)

        viewModel.visitables.collectFromSharedFlow(
            whenAction = {
                viewModel.setLocalCacheModel(localCacheModel)
                viewModel.getInitialMerchantSearchResult(searchParameter)
                viewModel.onScrollProductList(9, 10)
            },
            then = {
                val actualVisitableCount = (it as? Success)?.data?.size
                Assert.assertEquals(finalExpectedVisitables.size, actualVisitableCount)
            }
        )
    }

    @Test
    fun `when onScrollProductList but not on the last index, should not load more`() {
        val localCacheModel = LocalCacheModel(
            address_id = "123",
            lat = "1.23",
            long = "1.23"
        )
        val searchResult = getSearchResultResponse()
        val nextPageKey = "123"
        val hasNextPageSearchResult = searchResult.copy(
            tokofoodSearchMerchant = searchResult.tokofoodSearchMerchant.copy(
                nextPageKey = nextPageKey
            )
        )
        val searchParameter = hashMapOf<String, String>()
        val expectedVisitables = hasNextPageSearchResult.tokofoodSearchMerchant.merchants.map {
            MerchantSearchResultUiModel(
                id = it.id,
                merchant = it
            )
        }
        onGetSearchResult_shouldReturn(localCacheModel, searchParameter, null, hasNextPageSearchResult)
        onGetSuccessLoadSearchResultInitial_shouldReturn(expectedVisitables)
        onGetIsVisitableContainOtherStates_shouldReturn(false)

        viewModel.visitables.collectFromSharedFlow(
            whenAction = {
                viewModel.setLocalCacheModel(localCacheModel)
                viewModel.getInitialMerchantSearchResult(searchParameter)
                viewModel.onScrollProductList(5, 10)
            },
            then = {
                val actualVisitableCount = (it as? Success)?.data?.size
                Assert.assertNotEquals(hasNextPageSearchResult.tokofoodSearchMerchant.merchants.size * 2, actualVisitableCount)
            }
        )
    }

    @Test
    fun `when onScrollProductList but still on the first index, should not load more`() {
        val localCacheModel = LocalCacheModel(
            address_id = "123",
            lat = "1.23",
            long = "1.23"
        )
        val searchResult = getSearchResultResponse()
        val nextPageKey = "123"
        val hasNextPageSearchResult = searchResult.copy(
            tokofoodSearchMerchant = searchResult.tokofoodSearchMerchant.copy(
                nextPageKey = nextPageKey
            )
        )
        val searchParameter = hashMapOf<String, String>()
        val expectedVisitables = hasNextPageSearchResult.tokofoodSearchMerchant.merchants.map {
            MerchantSearchResultUiModel(
                id = it.id,
                merchant = it
            )
        }
        onGetSearchResult_shouldReturn(localCacheModel, searchParameter, null, hasNextPageSearchResult)
        onGetSuccessLoadSearchResultInitial_shouldReturn(expectedVisitables)
        onGetIsVisitableContainOtherStates_shouldReturn(false)

        viewModel.visitables.collectFromSharedFlow(
            whenAction = {
                viewModel.setLocalCacheModel(localCacheModel)
                viewModel.getInitialMerchantSearchResult(searchParameter)
                viewModel.onScrollProductList(0, 1)
            },
            then = {
                val actualVisitableCount = (it as? Success)?.data?.size
                Assert.assertNotEquals(hasNextPageSearchResult.tokofoodSearchMerchant.merchants.size * 2, actualVisitableCount)
            }
        )
    }

    @Test
    fun `when onScrollProductList but does not have next page key, should not load more`() {
        val localCacheModel = LocalCacheModel(
            address_id = "123",
            lat = "1.23",
            long = "1.23"
        )
        val searchResult = getSearchResultResponse()
        val nextPageKey = ""
        val hasNextPageSearchResult = searchResult.copy(
            tokofoodSearchMerchant = searchResult.tokofoodSearchMerchant.copy(
                nextPageKey = nextPageKey
            )
        )
        val searchParameter = hashMapOf<String, String>()
        val expectedVisitables = hasNextPageSearchResult.tokofoodSearchMerchant.merchants.map {
            MerchantSearchResultUiModel(
                id = it.id,
                merchant = it
            )
        }
        onGetSearchResult_shouldReturn(localCacheModel, searchParameter, null, hasNextPageSearchResult)
        onGetSuccessLoadSearchResultInitial_shouldReturn(expectedVisitables)
        onGetIsVisitableContainOtherStates_shouldReturn(false)

        viewModel.visitables.collectFromSharedFlow(
            whenAction = {
                viewModel.setLocalCacheModel(localCacheModel)
                viewModel.getInitialMerchantSearchResult(searchParameter)
                viewModel.onScrollProductList(9, 10)
            },
            then = {
                val actualVisitableCount = (it as? Success)?.data?.size
                Assert.assertNotEquals(hasNextPageSearchResult.tokofoodSearchMerchant.merchants.size * 2, actualVisitableCount)
            }
        )
    }

    @Test
    fun `when onScrollProductList but contains other state, should not load more`() {
        val localCacheModel = LocalCacheModel(
            address_id = "123",
            lat = "1.23",
            long = "1.23"
        )
        val searchResult = getSearchResultResponse()
        val nextPageKey = "123"
        val hasNextPageSearchResult = searchResult.copy(
            tokofoodSearchMerchant = searchResult.tokofoodSearchMerchant.copy(
                nextPageKey = nextPageKey
            )
        )
        val searchParameter = hashMapOf<String, String>()
        val expectedVisitables = hasNextPageSearchResult.tokofoodSearchMerchant.merchants.map {
            MerchantSearchResultUiModel(
                id = it.id,
                merchant = it
            )
        }
        onGetSearchResult_shouldReturn(localCacheModel, searchParameter, null, hasNextPageSearchResult)
        onGetSuccessLoadSearchResultInitial_shouldReturn(expectedVisitables)
        onGetIsVisitableContainOtherStates_shouldReturn(true)

        viewModel.visitables.collectFromSharedFlow(
            whenAction = {
                viewModel.setLocalCacheModel(localCacheModel)
                viewModel.getInitialMerchantSearchResult(searchParameter)
                viewModel.onScrollProductList(9, 10)
            },
            then = {
                val actualVisitableCount = (it as? Success)?.data?.size
                Assert.assertNotEquals(hasNextPageSearchResult.tokofoodSearchMerchant.merchants.size * 2, actualVisitableCount)
            }
        )
    }

    @Test
    fun `when onScrollProductList and should load more, should fail load more`() {
        val localCacheModel = LocalCacheModel(
            address_id = "123",
            lat = "1.23",
            long = "3.123"
        )
        val searchResult = getSearchResultResponse()
        val nextPageKey = "123"
        val hasNextPageSearchResult = searchResult.copy(
            tokofoodSearchMerchant = searchResult.tokofoodSearchMerchant.copy(
                nextPageKey = nextPageKey
            )
        )
        val searchParameter = hashMapOf<String, String>()
        val expectedVisitables = hasNextPageSearchResult.tokofoodSearchMerchant.merchants.map {
            MerchantSearchResultUiModel(
                id = it.id,
                merchant = it
            )
        }
        val loadMoreVisitables = expectedVisitables + listOf(TokoFoodProgressBarUiModel(""))
        onGetSearchResult_shouldReturn(localCacheModel, searchParameter, null, hasNextPageSearchResult)
        onGetSearchResult_shouldThrow(localCacheModel, searchParameter, nextPageKey, MessageErrorException())
        onGetSuccessLoadSearchResultInitial_shouldReturn(expectedVisitables)
        onGetLoadMoreVisitables_shouldReturn(expectedVisitables, loadMoreVisitables)
        onGetIsVisitableContainOtherStates_shouldReturn(false)

        viewModel.setLocalCacheModel(localCacheModel)
        viewModel.getInitialMerchantSearchResult(searchParameter)

        viewModel.uiEventFlow.collectFromSharedFlow(
            whenAction = {
                viewModel.onScrollProductList(9, 10)
            },
            then = {
                val uiEvent = it?.state
                Assert.assertEquals(TokofoodSearchUiEvent.EVENT_FAILED_LOAD_MORE, uiEvent)
            }
        )
    }

    @Test
    fun `when openDetailFilterBottomSheet but no current dynamic filter model, should success load open detail bottomsheet`() {
        val detailFilterResult = getDetailFilterResponse()
        onGetFilter_shouldReturn(TokofoodFilterSortUseCase.TYPE_DETAIL, detailFilterResult)

        viewModel.uiEventFlow.collectFromSharedFlow(
            whenAction = {
                viewModel.openDetailFilterBottomSheet()
            },
            then = {
                val actualEvent = it?.state
                Assert.assertEquals(TokofoodSearchUiEvent.EVENT_SUCCESS_LOAD_DETAIL_FILTER, actualEvent)
            }
        )
    }

    @Test
    fun `when openDetailFilterBottomSheet but no current dynamic filter model, should fail load open detail bottomsheet`() {
        val throwable = MessageErrorException()
        onGetFilter_shouldThrow(TokofoodFilterSortUseCase.TYPE_DETAIL, throwable)

        viewModel.uiEventFlow.collectFromSharedFlow(
            whenAction = {
                viewModel.openDetailFilterBottomSheet()
            },
            then = {
                val actualEvent = it?.state
                Assert.assertEquals(TokofoodSearchUiEvent.EVENT_FAILED_LOAD_DETAIL_FILTER, actualEvent)
            }
        )
    }

    @Test
    fun `when openDetailFilterBottomSheet with current dynamic filter model existing, should success load open detail bottomsheet`() {
        runBlocking {
            val detailFilterResult = getDetailFilterResponse()
            onGetFilter_shouldReturn(TokofoodFilterSortUseCase.TYPE_DETAIL, detailFilterResult)

            viewModel.uiEventFlow.collectFromSharedFlow(
                whenAction = {
                    viewModel.openDetailFilterBottomSheet()
                    viewModel.openDetailFilterBottomSheet()
                },
                then = {
                    val actualEvent = it?.state
                    Assert.assertEquals(TokofoodSearchUiEvent.EVENT_SUCCESS_LOAD_DETAIL_FILTER, actualEvent)
                }
            )
        }
    }

    @Test
    fun `when applySort unselected should update removing search parameter`() {
        val sortKey = "sort"
        val sortValue = "1"
        val initialSearchParameter = hashMapOf(sortKey to sortValue)
        viewModel.searchParameterMap.collectFromSharedFlow(
            whenAction = {
                viewModel.setKeyword("mcd")
                viewModel.getInitialMerchantSearchResult(initialSearchParameter)
                viewModel.applySort(
                    Sort(key = sortKey, value = sortValue),
                    false
                )
            },
            then = {
                assert(it?.get(sortKey).isNullOrBlank())
            }
        )
    }

    @Test
    fun `when applySort selected should update adding search parameter`() {
        val sortKey = "sort"
        val sortValue = "1"
        val initialSearchParameter = hashMapOf<String, String>()
        viewModel.searchParameterMap.collectFromSharedFlow(
            whenAction = {
                viewModel.setKeyword("mcd")
                viewModel.getInitialMerchantSearchResult(initialSearchParameter)
                viewModel.applySort(
                    Sort(key = sortKey, value = sortValue),
                    true
                )
            },
            then = {
                assert(it?.get(sortKey) == sortValue)
            }
        )
    }

    @Test
    fun `when applySort but search parameter map null, should not update parameter`() {
        val sortKey = "sort"
        val sortValue = "1"

        viewModel.searchParameterMap.collectFromSharedFlow(
            whenAction = {
                viewModel.applySort(
                    Sort(key = sortKey, value = sortValue),
                    true
                )
            },
            then = {
                assert(it == null)
            }
        )
    }

    @Test
    fun `when applySortSelected should update parameter`() {
        val sortKey = "sort"
        val sortValue = "1"
        val sortUiModel = TokofoodQuickSortUiModel("", sortKey, sortValue, true)
        val initialSearchParameter = hashMapOf<String, String>()

        viewModel.searchParameterMap.collectFromSharedFlow(
            whenAction = {
                viewModel.setKeyword("mcd")
                viewModel.getInitialMerchantSearchResult(initialSearchParameter)
                viewModel.applySortSelected(sortUiModel)
            },
            then = {
                Assert.assertEquals(sortValue, it?.get(sortKey))
            }
        )
    }

    @Test
    fun `when applySortSelected but search param null should not update parameter`() {
        val sortKey = "sort"
        val sortValue = "1"
        val sortUiModel = TokofoodQuickSortUiModel("", sortKey, sortValue, true)

        viewModel.searchParameterMap.collectFromSharedFlow(
            whenAction = {
                viewModel.applySortSelected(sortUiModel)
            },
            then = {
                Assert.assertEquals(it, null)
            }
        )
    }

    @Test
    fun `when applyFilter should update search parameter`() {
        val filterKey = "pricing"
        val selectedFilterValue = "1"
        val unselectedFilterValue = "2"
        val filter = Filter(
            options = listOf(
                Option(
                    key = filterKey,
                    value = selectedFilterValue,
                    inputState = true.toString()
                ),
                Option(
                    key = filterKey,
                    value = unselectedFilterValue,
                    inputState = false.toString()
                )
            )
        )
        val initialSearchParameter = hashMapOf<String, String>()

        viewModel.searchParameterMap.collectFromSharedFlow(
            whenAction = {
                viewModel.setKeyword("mcd")
                viewModel.getInitialMerchantSearchResult(initialSearchParameter)
                viewModel.applyFilter(filter)
            },
            then = {
                Assert.assertEquals(selectedFilterValue, it?.get(filterKey))
            }
        )
    }

    @Test
    fun `when applyFilter but search param null should not update search parameter`() {
        val filterKey = "pricing"
        val selectedFilterValue = "1"
        val unselectedFilterValue = "2"
        val filter = Filter(
            options = listOf(
                Option(
                    key = filterKey,
                    value = selectedFilterValue,
                    inputState = true.toString()
                ),
                Option(
                    key = filterKey,
                    value = unselectedFilterValue,
                    inputState = false.toString()
                )
            )
        )

        viewModel.searchParameterMap.collectFromSharedFlow(
            whenAction = {
                viewModel.applyFilter(filter)
            },
            then = {
                Assert.assertEquals(null, it)
            }
        )
    }

    @Test
    fun `when applyFilter but filter option is empty should not update search parameter`() {
        val filter = Filter()
        val initialSearchParameter = hashMapOf<String, String>()

        viewModel.searchParameterMap.collectFromSharedFlow(
            whenAction = {
                viewModel.setKeyword("mcd")
                viewModel.getInitialMerchantSearchResult(initialSearchParameter)
                viewModel.applyFilter(filter)
            },
            then = {
                Assert.assertEquals(initialSearchParameter, it)
            }
        )
    }

    @Test
    fun `when applyOptions should update parameter`() {
        val filterKey = "pricing"
        val selectedFilterValue = "1"
        val unselectedFilterValue = "2"
        val options = listOf(
            Option(
                key = filterKey,
                value = selectedFilterValue,
                inputState = true.toString()
            ),
            Option(
                key = filterKey,
                value = unselectedFilterValue,
                inputState = false.toString()
            )
        )
        val initialSearchParameter = hashMapOf<String, String>()

        viewModel.searchParameterMap.collectFromSharedFlow(
            whenAction = {
                viewModel.setKeyword("mcd")
                viewModel.getInitialMerchantSearchResult(initialSearchParameter)
                viewModel.applyOptions(options)
            },
            then = {
                Assert.assertEquals(selectedFilterValue, it?.get(filterKey))
            }
        )
    }

    @Test
    fun `when applyOptions but search param null, should not update parameter`() {
        val filterKey = "pricing"
        val selectedFilterValue = "1"
        val unselectedFilterValue = "2"
        val options = listOf(
            Option(
                key = filterKey,
                value = selectedFilterValue,
                inputState = true.toString()
            ),
            Option(
                key = filterKey,
                value = unselectedFilterValue,
                inputState = false.toString()
            )
        )

        viewModel.searchParameterMap.collectFromSharedFlow(
            whenAction = {
                viewModel.applyOptions(options)
            },
            then = {
                Assert.assertEquals(null, it)
            }
        )
    }

    @Test
    fun `when applyOptions but options are empty should not update parameter`() {
        val options = listOf<Option>()
        val initialSearchParameter = hashMapOf<String, String>()

        viewModel.searchParameterMap.collectFromSharedFlow(
            whenAction = {
                viewModel.setKeyword("mcd")
                viewModel.getInitialMerchantSearchResult(initialSearchParameter)
                viewModel.applyOptions(options)
            },
            then = {
                Assert.assertEquals(initialSearchParameter, it)
            }
        )
    }

    @Test
    fun `when resetParams should update search param`() {
        val updatedParams = hashMapOf("pricing" to "1")
        val initialSearchParameter = hashMapOf<String, String>()

        viewModel.searchParameterMap.collectFromSharedFlow(
            whenAction = {
                viewModel.setKeyword("mcd")
                viewModel.getInitialMerchantSearchResult(initialSearchParameter)
                viewModel.resetParams(updatedParams)
            },
            then = {
                Assert.assertEquals(updatedParams.entries.first().value, it?.get(updatedParams.entries.first().key))
            }
        )
    }

    @Test
    fun `when resetParams but search param emtpy should not update search param`() {
        val updatedParams = hashMapOf("pricing" to "1")

        viewModel.searchParameterMap.collectFromSharedFlow(
            whenAction = {
                viewModel.resetParams(updatedParams)
            },
            then = {
                Assert.assertEquals(null, it)
            }
        )
    }

    @Test
    fun `when showQuickSortBottomSheet should emit show quick sort bottom sheet event`() {
        val sortItems = listOf(
            Sort(
                "sort",
                "1"
            ),
            Sort(
                "sort",
                "2"
            )
        )
        onGetQuickSortUiModels_shouldReturn(sortItems, "", listOf())

        viewModel.uiEventFlow.collectFromSharedFlow(
            whenAction = {
                viewModel.showQuickSortBottomSheet(sortItems)
            },
            then = {
                val actualEvent = it?.state
                Assert.assertEquals(TokofoodSearchUiEvent.EVENT_OPEN_QUICK_SORT_BOTTOMSHEET, actualEvent)
            }
        )
    }

    @Test
    fun `when showQuickSortBottomSheet and current search parameter exist should emit show quick sort bottom sheet event`() {
        runBlocking {
            val sortKey = "sort"
            val selectedSortValue = "1"
            val sortItems = listOf(
                Sort(
                    key = sortKey,
                    value = selectedSortValue
                ),
                Sort(
                    key = sortKey,
                    value = "2"
                )
            )
            val currentParam = hashMapOf(sortKey to selectedSortValue)
            val expectedUiModels = sortItems.map {
                TokofoodQuickSortUiModel(
                    it.name,
                    it.key,
                    it.value,
                    it.value == selectedSortValue
                )
            }

            onGetQuickSortUiModels_shouldReturn(sortItems, selectedSortValue, expectedUiModels)
            viewModel.setKeyword("mcd")
            viewModel.getInitialMerchantSearchResult(currentParam)

            viewModel.uiEventFlow.collectFromSharedFlow(
                whenAction = {
                    viewModel.showQuickSortBottomSheet(sortItems)
                },
                then = {
                    val actualEvent = it?.state
                    Assert.assertEquals(TokofoodSearchUiEvent.EVENT_OPEN_QUICK_SORT_BOTTOMSHEET, actualEvent)
                }
            )
        }
    }

    @Test
    fun `when showQuickSortBottomSheet and current search parameter does not contains sort items key should emit show quick sort bottom sheet event`() {
        runBlocking {
            val sortKey = "sort"
            val selectedSortKey = "1"
            val sortItems = listOf(
                Sort(
                    sortKey,
                    "2"
                ),
                Sort(
                    sortKey,
                    "3"
                )
            )
            val currentParam = hashMapOf(sortKey to selectedSortKey)
            val expectedUiModels = sortItems.map {
                TokofoodQuickSortUiModel(
                    it.name,
                    it.key,
                    it.value,
                    it.value == selectedSortKey
                )
            }

            onGetQuickSortUiModels_shouldReturn(sortItems, selectedSortKey, expectedUiModels)
            viewModel.setKeyword("mcd")
            viewModel.getInitialMerchantSearchResult(currentParam)

            viewModel.uiEventFlow.collectFromSharedFlow(
                whenAction = {
                    viewModel.showQuickSortBottomSheet(sortItems)
                },
                then = {
                    val actualEvent = it?.state
                    Assert.assertEquals(TokofoodSearchUiEvent.EVENT_OPEN_QUICK_SORT_BOTTOMSHEET, actualEvent)
                }
            )
        }
    }

    @Test
    fun `when showQuickSortBottomSheet but sort items are empty should not emit show quick sort bottom sheet event`() {
        runBlocking {
            val sortKey = "sort"
            val selectedSortKey = "1"
            val sortItems = listOf<Sort>()
            val currentParam = hashMapOf(sortKey to selectedSortKey)

            viewModel.setKeyword("mcd")
            viewModel.getInitialMerchantSearchResult(currentParam)

            viewModel.uiEventFlow.collectFromSharedFlow(
                whenAction = {
                    viewModel.showQuickSortBottomSheet(sortItems)
                },
                then = {
                    val actualEvent = it?.state
                    Assert.assertNotEquals(TokofoodSearchUiEvent.EVENT_OPEN_QUICK_SORT_BOTTOMSHEET, actualEvent)
                }
            )
        }
    }

    @Test
    fun `when showQuickFilterBottomSheet price range filter should emit open filter range bottomsheet event`() {
        val filter =
            Filter(templateName = Filter.TEMPLATE_PRICING_FOOD)
        val expectedUiModel =
            PriceRangeChipUiModel(
                listOf(
                    PriceRangeFilterCheckboxItemUiModel(Option())
                ),
                filter.subTitle
            )

        onGetQuickFilterPriceRangeUiModels_shouldReturn(filter, expectedUiModel)

        viewModel.uiEventFlow.collectFromSharedFlow(
            whenAction = {
                viewModel.showQuickFilterBottomSheet(filter)
            },
            then = {
                val actualEvent = it?.state
                Assert.assertEquals(TokofoodSearchUiEvent.EVENT_OPEN_QUICK_FILTER_PRICE_RANGE_BOTTOMSHEET, actualEvent)
            }
        )
    }

    @Test
    fun `when showQuickFilterBottomSheet normal filter should emit open normal bottomsheet event`() {
        val filter = Filter()
        val expectedUiModel =
            PriceRangeChipUiModel(
                listOf(
                    PriceRangeFilterCheckboxItemUiModel(Option())
                ),
                filter.subTitle
            )

        onGetQuickFilterPriceRangeUiModels_shouldReturn(filter, expectedUiModel)

        viewModel.uiEventFlow.collectFromSharedFlow(
            whenAction = {
                viewModel.showQuickFilterBottomSheet(filter)
            },
            then = {
                val actualEvent = it?.state
                Assert.assertEquals(TokofoodSearchUiEvent.EVENT_OPEN_QUICK_FILTER_NORMAL_BOTTOMSHEET, actualEvent)
            }
        )
    }

    @Test
    fun `when getCurrentSortValue should return current sort param value`() {
        runBlocking {
            val sortFilterResponse = getQuickFilterResponse()
            val searchParameter = hashMapOf<String, String>()
            val expectedUiModels = getFilterSortUiModels(sortFilterResponse.tokofoodFilterAndSort)
            val expectedSortKey = "sort"
            val expectedSortValue = "1"
            onGetFilter_shouldReturn(TokofoodFilterSortUseCase.TYPE_QUICK, sortFilterResponse)
            onGetQuickSortFilterUiModels_shouldReturn(sortFilterResponse.tokofoodFilterAndSort, expectedUiModels)
            onGetCurrentSortKey_shouldReturn(expectedUiModels, expectedSortKey)

            viewModel.loadQuickSortFilter(searchParameter)
            viewModel.getInitialMerchantSearchResult(hashMapOf(expectedSortKey to expectedSortValue))

            val actualSortValue = viewModel.getCurrentSortValue()
            Assert.assertEquals(expectedSortValue, actualSortValue)
        }
    }

    @Test
    fun `when getCurrentSortValue but current search param null, should return empty string`() {
        runBlocking {
            val sortFilterResponse = getQuickFilterResponse()
            val searchParameter = hashMapOf<String, String>()
            val expectedUiModels = getFilterSortUiModels(sortFilterResponse.tokofoodFilterAndSort)
            val expectedSortKey = "sort"
            onGetFilter_shouldReturn(TokofoodFilterSortUseCase.TYPE_QUICK, sortFilterResponse)
            onGetQuickSortFilterUiModels_shouldReturn(sortFilterResponse.tokofoodFilterAndSort, expectedUiModels)
            onGetCurrentSortKey_shouldReturn(expectedUiModels, expectedSortKey)

            viewModel.loadQuickSortFilter(searchParameter)

            val actualSortValue = viewModel.getCurrentSortValue()
            Assert.assertEquals(String.EMPTY, actualSortValue)
        }
    }

    @Test
    fun `when getCurrentSortValue but current search param doesn't include ui models' key, should return empty string`() {
        runBlocking {
            val sortFilterResponse = getQuickFilterResponse()
            val searchParameter = hashMapOf<String, String>()
            val expectedUiModels = getFilterSortUiModels(sortFilterResponse.tokofoodFilterAndSort)
            val expectedSortKey = "sort"
            onGetFilter_shouldReturn(TokofoodFilterSortUseCase.TYPE_QUICK, sortFilterResponse)
            onGetQuickSortFilterUiModels_shouldReturn(sortFilterResponse.tokofoodFilterAndSort, expectedUiModels)
            onGetCurrentSortKey_shouldReturn(expectedUiModels, expectedSortKey)

            viewModel.loadQuickSortFilter(searchParameter)
            viewModel.getInitialMerchantSearchResult(hashMapOf("sort2" to "2"))

            val actualSortValue = viewModel.getCurrentSortValue()
            Assert.assertEquals(String.EMPTY, actualSortValue)
        }
    }

    @Test
    fun `when latLong is blank, should emit no pinpoint state`() {
        runBlocking {
            val localCacheModel = LocalCacheModel(
                address_id = "123"
            )
            val searchParameter = hashMapOf<String, String>()
            val expectedUiModels = listOf(
                MerchantSearchOOCUiModel(MerchantSearchOOCUiModel.NO_PINPOINT)
            )
            onGetOutOfCoverageUiModels_shouldReturn(expectedUiModels)

            viewModel.visitables.collectFromSharedFlow(
                whenAction = {
                    viewModel.setLocalCacheModel(localCacheModel)
                    viewModel.getInitialMerchantSearchResult(searchParameter)
                },
                then = {
                    Assert.assertEquals(((it as? Success)?.data?.getOrNull(Int.ZERO) as? MerchantSearchOOCUiModel)?.type, MerchantSearchOOCUiModel.NO_PINPOINT)
                }
            )
        }
    }

    @Test
    fun `when ooc state is true, should emit ooc state`() {
        runBlocking {
            val localCacheModel = LocalCacheModel(
                address_id = "123",
                lat = "1.23",
                long = "3.123"
            )
            val searchResult = getSearchResultOocResponse()
            val searchParameter = hashMapOf<String, String>()
            val expectedUiModels = listOf(
                MerchantSearchOOCUiModel(MerchantSearchOOCUiModel.OUT_OF_COVERAGE)
            )
            onGetSearchResult_shouldReturn(localCacheModel, searchParameter, null, searchResult)
            onGetOutOfCoverageUiModels_shouldReturn(expectedUiModels)

            viewModel.visitables.collectFromSharedFlow(
                whenAction = {
                    viewModel.setLocalCacheModel(localCacheModel)
                    viewModel.getInitialMerchantSearchResult(searchParameter)
                },
                then = {
                    Assert.assertEquals(((it as? Success)?.data?.getOrNull(Int.ZERO) as? MerchantSearchOOCUiModel)?.type, MerchantSearchOOCUiModel.OUT_OF_COVERAGE)
                }
            )
        }
    }

    @Test
    fun `when local cache model is null but isEligible for revamp, should emit ooc state`() {
        runBlocking {
            val searchParameter = hashMapOf<String, String>()
            val expectedUiModels = listOf(
                MerchantSearchOOCUiModel(MerchantSearchOOCUiModel.NO_ADDRESS_REVAMP)
            )
            onGetOutOfCoverageUiModels_shouldReturn(expectedUiModels)

            viewModel.visitables.collectFromSharedFlow(
                whenAction = {
                    viewModel.getInitialMerchantSearchResult(searchParameter)
                },
                then = {
                    Assert.assertEquals(((it as? Success)?.data?.getOrNull(Int.ZERO) as? MerchantSearchOOCUiModel)?.type, MerchantSearchOOCUiModel.NO_ADDRESS_REVAMP)
                }
            )
        }
    }

    @Test
    fun `when address is empty, should emit no address state`() {
        runBlocking {
            val localCacheModel = LocalCacheModel(
                address_id = "",
                lat = "1.23",
                long = "3.123"
            )
            val searchParameter = hashMapOf<String, String>()
            val expectedUiModels = listOf(
                MerchantSearchOOCUiModel(MerchantSearchOOCUiModel.NO_ADDRESS_REVAMP)
            )
            onGetOutOfCoverageUiModels_shouldReturn(expectedUiModels)

            viewModel.visitables.collectFromSharedFlow(
                whenAction = {
                    viewModel.setLocalCacheModel(localCacheModel)
                    viewModel.getInitialMerchantSearchResult(searchParameter)
                },
                then = {
                    Assert.assertEquals(((it as? Success)?.data?.getOrNull(Int.ZERO) as? MerchantSearchOOCUiModel)?.type, MerchantSearchOOCUiModel.NO_ADDRESS_REVAMP)
                }
            )
        }
    }

    @Test
    fun `when local cache model is null but is not eligible for revamp, should emit ooc state`() {
        runBlocking {
            val searchParameter = hashMapOf<String, String>()
            val expectedUiModels = listOf(
                MerchantSearchOOCUiModel(MerchantSearchOOCUiModel.NO_ADDRESS_REVAMP)
            )
            onGetOutOfCoverageUiModels_shouldReturn(expectedUiModels)

            viewModel.visitables.collectFromSharedFlow(
                whenAction = {
                    viewModel.getInitialMerchantSearchResult(searchParameter)
                },
                then = {
                    Assert.assertEquals(((it as? Success)?.data?.getOrNull(Int.ZERO) as? MerchantSearchOOCUiModel)?.type, MerchantSearchOOCUiModel.NO_ADDRESS_REVAMP)
                }
            )
        }
    }

    @Test
    fun `when local cache model is null but is check eligible error, should emit ooc state`() {
        runBlocking {
            val searchParameter = hashMapOf<String, String>()
            val expectedUiModels = listOf(
                MerchantSearchOOCUiModel(MerchantSearchOOCUiModel.NO_ADDRESS_REVAMP)
            )
            onGetOutOfCoverageUiModels_shouldReturn(expectedUiModels)

            viewModel.visitables.collectFromSharedFlow(
                whenAction = {
                    viewModel.getInitialMerchantSearchResult(searchParameter)
                },
                then = {
                    Assert.assertEquals(((it as? Success)?.data?.getOrNull(Int.ZERO) as? MerchantSearchOOCUiModel)?.type, MerchantSearchOOCUiModel.NO_ADDRESS_REVAMP)
                }
            )
        }
    }

    @Test
    fun `when edit pinpoint should success set pinpoint event`() {
        val addressId = "123"
        val localCacheModel = LocalCacheModel(
            address_id = addressId
        )
        val latitude = "1.123"
        val longitude = "2.345"
        onEditAddress_shouldReturn(addressId, latitude, longitude, true)
        runBlocking {
            viewModel.uiEventFlow.collectFromSharedFlow(
                whenAction = {
                    viewModel.setLocalCacheModel(localCacheModel)
                    viewModel.updatePinpoint(latitude, longitude)
                },
                then = {
                    Assert.assertEquals(it?.state, TokofoodSearchUiEvent.EVENT_SUCCESS_EDIT_PINPOINT)
                }
            )
        }
    }

    @Test
    fun `when edit pinpoint but unsuccessful, should set failed pinpoint event`() {
        val addressId = "123"
        val localCacheModel = LocalCacheModel(
            address_id = addressId
        )
        val latitude = "1.123"
        val longitude = "2.345"
        onEditAddress_shouldReturn(addressId, latitude, longitude, false)
        runBlocking {
            viewModel.uiEventFlow.collectFromSharedFlow(
                whenAction = {
                    viewModel.setLocalCacheModel(localCacheModel)
                    viewModel.updatePinpoint(latitude, longitude)
                },
                then = {
                    Assert.assertEquals(it?.state, TokofoodSearchUiEvent.EVENT_FAILED_EDIT_PINPOINT)
                }
            )
        }
    }

    @Test
    fun `when edit pinpoint but throw exception, should set failed pinpoint event`() {
        val addressId = "123"
        val localCacheModel = LocalCacheModel(
            address_id = addressId
        )
        val latitude = "1.123"
        val longitude = "2.345"
        onEditAddress_shouldThrow(addressId, latitude, longitude, MessageErrorException())
        runBlocking {
            viewModel.uiEventFlow.collectFromSharedFlow(
                whenAction = {
                    viewModel.setLocalCacheModel(localCacheModel)
                    viewModel.updatePinpoint(latitude, longitude)
                },
                then = {
                    Assert.assertEquals(it?.state, TokofoodSearchUiEvent.EVENT_FAILED_EDIT_PINPOINT)
                }
            )
        }
    }

    @Test
    fun `when edit pinpoint but addressId empty, should set no address state`() {
        val addressId = ""
        val localCacheModel = LocalCacheModel(
            address_id = addressId
        )
        val latitude = "1.123"
        val longitude = "2.345"
        onEditAddress_shouldThrow(addressId, latitude, longitude, MessageErrorException())
        val expectedUiModels = listOf(
            MerchantSearchOOCUiModel(MerchantSearchOOCUiModel.NO_ADDRESS_REVAMP)
        )
        onGetOutOfCoverageUiModels_shouldReturn(expectedUiModels)

        runBlocking {
            viewModel.visitables.collectFromSharedFlow(
                whenAction = {
                    viewModel.setLocalCacheModel(localCacheModel)
                    viewModel.updatePinpoint(latitude, longitude)
                },
                then = {
                    Assert.assertEquals(((it as? Success)?.data?.getOrNull(Int.ZERO) as? MerchantSearchOOCUiModel)?.type, MerchantSearchOOCUiModel.NO_ADDRESS_REVAMP)
                }
            )
        }
    }

    @Test
    fun `when edit pinpoint but local cache model null, should set no address state`() {
        val expectedUiModels = listOf(
            MerchantSearchOOCUiModel(MerchantSearchOOCUiModel.NO_ADDRESS_REVAMP)
        )
        onGetOutOfCoverageUiModels_shouldReturn(expectedUiModels)

        runBlocking {
            viewModel.visitables.collectFromSharedFlow(
                whenAction = {
                    viewModel.updatePinpoint("123", "123")
                },
                then = {
                    Assert.assertEquals(((it as? Success)?.data?.getOrNull(Int.ZERO) as? MerchantSearchOOCUiModel)?.type, MerchantSearchOOCUiModel.NO_ADDRESS_REVAMP)
                }
            )
        }
    }
}
