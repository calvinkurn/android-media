package com.tokopedia.tokofood.search.searchresult

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.logisticCommon.data.response.EligibleForAddressFeature
import com.tokopedia.logisticCommon.data.response.KeroAddrIsEligibleForAddressFeatureData
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tokofood.common.domain.usecase.KeroEditAddressUseCase
import com.tokopedia.tokofood.feature.search.searchresult.domain.mapper.TokofoodFilterSortMapper
import com.tokopedia.tokofood.feature.search.searchresult.domain.mapper.TokofoodMerchantSearchResultMapper
import com.tokopedia.tokofood.feature.search.searchresult.domain.response.TokofoodFilterSortResponse
import com.tokopedia.tokofood.feature.search.searchresult.domain.response.TokofoodSearchMerchantResponse
import com.tokopedia.tokofood.feature.search.searchresult.domain.usecase.TokofoodFilterSortUseCase
import com.tokopedia.tokofood.feature.search.searchresult.domain.usecase.TokofoodSearchMerchantUseCase
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchOOCUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.PriceRangeChipUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodFilterItemUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodQuickSortUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodSortFilterItemUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodSortItemUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.viewmodel.TokofoodSearchResultPageViewModel
import com.tokopedia.tokofood.utils.JsonResourcesUtil
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
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
    lateinit var eligibleForAddressUseCase: EligibleForAddressUseCase

    @RelaxedMockK
    lateinit var keroEditAddressUseCase: KeroEditAddressUseCase

    @RelaxedMockK
    lateinit var tokofoodMerchantSearchResultMapper: TokofoodMerchantSearchResultMapper

    @RelaxedMockK
    lateinit var tokofoodFilterSortMapper: TokofoodFilterSortMapper

    protected lateinit var viewModel: TokofoodSearchResultPageViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TokofoodSearchResultPageViewModel(
            tokofoodSearchMerchantUseCase,
            tokofoodFilterSortUseCase,
            eligibleForAddressUseCase,
            keroEditAddressUseCase,
            tokofoodMerchantSearchResultMapper,
            tokofoodFilterSortMapper,
            CoroutineTestDispatchersProvider
        )
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    protected fun getQuickFilterResponse() =
        JsonResourcesUtil.createSuccessResponse<TokofoodFilterSortResponse>(QUICK_FILTER_JSON)

    protected fun getDetailFilterResponse() =
        JsonResourcesUtil.createSuccessResponse<TokofoodFilterSortResponse>(DETAIL_FILTER_JSON)

    protected fun getSearchResultResponse() =
        JsonResourcesUtil.createSuccessResponse<TokofoodSearchMerchantResponse>(SEARCH_RESULT_JSON)

    protected fun getSearchResultEmptyResponse() =
        JsonResourcesUtil.createSuccessResponse<TokofoodSearchMerchantResponse>(
            SEARCH_RESULT_EMPTY_JSON
        )

    protected fun getSearchResultOocResponse() =
        JsonResourcesUtil.createSuccessResponse<TokofoodSearchMerchantResponse>(
            SEARCH_RESULT_OOC
        )

    protected fun getFilterSortUiModels(dataValue: DataValue): List<TokofoodSortFilterItemUiModel> {
        return mutableListOf<TokofoodSortFilterItemUiModel>(
            TokofoodSortItemUiModel(
                sortFilterItem = SortFilterItem(""),
                totalSelectedOptions = 0,
                selectedKey = "",
                sortList = dataValue.sort
            )
        ).apply {
            addAll(
                dataValue.filter.map {
                    TokofoodFilterItemUiModel(
                        SortFilterItem(""),
                        0,
                        "",
                        it
                    )
                }
            )
        }
    }

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
        searchParameter: HashMap<String, String>,
        pageKey: String?,
        response: TokofoodSearchMerchantResponse
    ) {
        coEvery {
            tokofoodSearchMerchantUseCase.execute(localCacheModel, searchParameter, pageKey)
        } returns response
    }

    protected fun onGetSearchResult_shouldThrow(
        localCacheModel: LocalCacheModel?,
        searchParameter: HashMap<String, String>,
        pageKey: String?,
        throwable: Throwable
    ) {
        coEvery {
            tokofoodSearchMerchantUseCase.execute(localCacheModel, searchParameter, pageKey)
        } throws throwable
    }

    protected fun onGetSuccessLoadSearchResultInitial_shouldReturn(visitables: List<Visitable<*>>) {
        coEvery {
            tokofoodMerchantSearchResultMapper.getSuccessLoadSearchResultInitial(any())
        } returns visitables
    }

    protected fun onGetSuccessLoadSearchResultMore_shouldReturn(visitables: List<Visitable<*>>) {
        coEvery {
            tokofoodMerchantSearchResultMapper.getSuccessLoadSearchResultMore(any(), any())
        } returns visitables
    }

    protected fun onGetErrorSearchResultInitial_shouldReturn(throwable: Throwable?,
                                                             visitables: List<Visitable<*>>) {
        coEvery {
            tokofoodMerchantSearchResultMapper.getErrorSearchResultInitial(throwable)
        } returns visitables
    }

    protected fun onGetLoadMoreVisitables_shouldReturn(
        currentVisitables: List<Visitable<*>>?,
        loadMoreVisitables: List<Visitable<*>>
    ) {
        coEvery {
            tokofoodMerchantSearchResultMapper.getLoadMoreVisitables(currentVisitables)
        } returns loadMoreVisitables
    }

    protected fun onGetIsVisitableContainOtherStates_shouldReturn(isVisitableContainOtherStates: Boolean) {
        coEvery {
            tokofoodMerchantSearchResultMapper.getIsVisitableContainOtherStates(any())
        } returns isVisitableContainOtherStates
    }

    protected fun onGetQuickSortFilterUiModels_shouldReturn(dataValue: DataValue,
                                                            uiModels: List<TokofoodSortFilterItemUiModel>) {
        coEvery {
            tokofoodFilterSortMapper.getQuickSortFilterUiModels(dataValue)
        } returns uiModels
    }

    protected fun onGetAppliedSortFilterUiModels_shouldReturn(searchParameters: HashMap<String, String>,
                                                              uiModels: List<TokofoodSortFilterItemUiModel>,
                                                              returnedUiModels: List<TokofoodSortFilterItemUiModel>) {
        coEvery {
            tokofoodFilterSortMapper.getAppliedSortFilterUiModels(searchParameters, uiModels)
        } returns returnedUiModels
    }

    protected fun onGetQuickSortUiModels_shouldReturn(sortList: List<Sort>,
                                                      selectedSortValue: String,
                                                      uiModels: List<TokofoodQuickSortUiModel>) {
        coEvery {
            tokofoodFilterSortMapper.getQuickSortUiModels(sortList, selectedSortValue)
        } returns uiModels
    }

    protected fun onGetQuickFilterPriceRangeUiModels_shouldReturn(filter: Filter,
                                                                  uiModel: PriceRangeChipUiModel) {
        coEvery {
            tokofoodFilterSortMapper.getQuickFilterPriceRangeUiModels(filter)
        } returns uiModel
    }

    protected fun onGetCurrentSortKey_shouldReturn(uiModels: List<TokofoodSortFilterItemUiModel>?,
                                                   sortKey: String) {
        coEvery {
            tokofoodFilterSortMapper.getCurrentSortKey(uiModels)
        } returns sortKey
    }

    protected fun onGetOutOfCoverageUiModels_shouldReturn(uiModels: List<MerchantSearchOOCUiModel>) {
        coEvery {
            tokofoodMerchantSearchResultMapper.getOutOfCoverageUiModels(any())
        } returns uiModels
    }

    protected fun onGetEligibleForAnaRevamp_thenReturn(isEligible: Boolean) {
        coEvery {
            eligibleForAddressUseCase.eligibleForAddressFeature(any(), any(), any())
        } answers {
            firstArg<(KeroAddrIsEligibleForAddressFeatureData)-> Unit>().invoke(
                KeroAddrIsEligibleForAddressFeatureData(
                    eligibleForRevampAna = EligibleForAddressFeature(
                        eligible = isEligible
                    )
                )
            )
        }
    }

    protected fun onGetEligibleForAnaRevamp_thenReturn(errorThrowable: Throwable) {
        coEvery {
            eligibleForAddressUseCase.eligibleForAddressFeature(any(), any(), any())
        } answers {
            secondArg<(Throwable)-> Unit>().invoke(errorThrowable)
        }
    }

    protected fun onEditAddress_shouldReturn(addressId: String,
                                             lat: String,
                                             long: String,
                                             isSuccess: Boolean) {
        coEvery {
            keroEditAddressUseCase.execute(addressId, lat, long)
        } returns isSuccess
    }

    protected fun onEditAddress_shouldThrow(addressId: String,
                                            lat: String,
                                            long: String,
                                            throwable: Throwable) {
        coEvery {
            keroEditAddressUseCase.execute(addressId, lat, long)
        } throws throwable
    }

    companion object {
        const val QUICK_FILTER_JSON = "json/search/searchresult/quick_filter.json"
        const val DETAIL_FILTER_JSON = "json/search/searchresult/detail_filter.json"
        const val SEARCH_RESULT_JSON = "json/search/searchresult/search_result.json"
        const val SEARCH_RESULT_EMPTY_JSON = "json/search/searchresult/search_result_empty.json"
        const val SEARCH_RESULT_OOC = "json/search/searchresult/search_result_ooc.json"
    }

}