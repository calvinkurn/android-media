package com.tokopedia.search.result.shop.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.search.result.isExecuted
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.mapper.ShopViewModelMapperModule
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.dynamicFilterModel
import com.tokopedia.search.result.shop.presentation.viewmodel.testinstance.searchShopModel
import com.tokopedia.search.result.stubExecute
import com.tokopedia.search.shouldBe
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import dagger.Lazy as daggerLazy

internal open class SearchShopDataViewTestFixtures {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val shopViewModelMapperModule = ShopViewModelMapperModule()

    protected val searchShopFirstPageUseCase = mockk<UseCase<SearchShopModel>>(relaxed = true)
    protected val searchShopLoadMoreUseCase = mockk<UseCase<SearchShopModel>>(relaxed = true)
    protected val getDynamicFilterUseCase = mockk<UseCase<DynamicFilterModel>>(relaxed = true)
    protected val getShopCountUseCase = mockk<UseCase<Int>>(relaxed = true)
    protected val userSession = mockk<UserSessionInterface>(relaxed = true)
    protected val shopCpmViewModelMapper = shopViewModelMapperModule.provideShopCpmViewModelMapper()
    protected val shopViewModelMapper = shopViewModelMapperModule.provideShopViewModelMapper()

    protected val searchShopParameterCommon = mapOf(
            SearchApiConst.Q to "samsung",
            SearchApiConst.OFFICIAL to true
    )

    protected lateinit var searchShopViewModel: SearchShopViewModel

    @Before
    open fun setUp() {
        searchShopViewModel = createSearchShopViewModel()
    }

    protected open fun createSearchShopViewModel(parameter: Map<String, Any> = searchShopParameterCommon): SearchShopViewModel {
        return SearchShopViewModel(
                CoroutineTestDispatchersProvider,
                parameter,
                daggerLazy { searchShopFirstPageUseCase },
                daggerLazy { searchShopLoadMoreUseCase },
                daggerLazy { getDynamicFilterUseCase },
                daggerLazy { getShopCountUseCase },
                daggerLazy { shopCpmViewModelMapper },
                daggerLazy { shopViewModelMapper },
                daggerLazy { userSession }
        )
    }

    protected fun `Given search shop API call will be successful`() {
        searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
        getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
    }

    protected fun `Then verify search shop and dynamic filter API is called once`() {
        searchShopFirstPageUseCase.isExecuted()
        getDynamicFilterUseCase.isExecuted()
    }

    protected fun `Then assert quick filter is shown`(filterList: List<Filter>, selectedFilterIndexList: List<Int> = listOf(0)) {
        searchShopViewModel.getQuickFilterIsVisibleLiveData().value shouldBe true
        searchShopViewModel.getShimmeringQuickFilterIsVisibleLiveData().value shouldBe false

        val sortFilterItemList = searchShopViewModel.getSortFilterItemListLiveData().value!!

        sortFilterItemList.shouldHaveSize(filterList.size)
        sortFilterItemList.forEachIndexed { index, sortFilterItem ->
            val option = filterList.map { it.options }.flatten()[index]
            sortFilterItem.title shouldBe option.name
            sortFilterItem.typeUpdated shouldBe false

            val type = if (selectedFilterIndexList.contains(index)) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
            fun String.getTypeName(): String {
                return if (this == ChipsUnify.TYPE_SELECTED) "Selected" else "Normal"
            }
            sortFilterItem.type.shouldBe(
                    type,
                    "Sort Filter Item Type index $index is not correct, " +
                            "expected ${type.getTypeName()}, " +
                            "actual ${sortFilterItem.type.getTypeName()}"
            )
        }
    }
}