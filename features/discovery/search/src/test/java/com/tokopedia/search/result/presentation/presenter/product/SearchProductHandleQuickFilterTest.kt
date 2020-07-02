package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant.ABTestRemoteConfigKey
import com.tokopedia.discovery.common.constants.SearchConstant.ABTestRemoteConfigKey.AB_TEST_VARIANT_NEW_FILTER
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.Option
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.QuickFilterViewModel
import com.tokopedia.search.shouldBe
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test
import rx.Subscriber
import java.util.*

private const val searchProductModelWithQuickFilter = "searchproduct/quickfilter/with-quick-filter.json"
private const val searchProductModelNoQuickFilter = "searchproduct/quickfilter/no-quick-filter.json"

internal class SearchProductHandleQuickFilterTest : ProductListPresenterTestFixtures() {
    private val requestParamsSlot = slot<RequestParams>()
    private val listItemSlot = slot<ArrayList<SortFilterItem>>()
    private val visitableListSlot = slot<List<Visitable<*>>>()

    @Test
    fun `SearchProductModel has Quick Filter and enableNewQuickFilter is true`() {
        `Given new quick filter is enabled`()
        `Given isBottomSheetFilterRevampABTestEnabled return true`()
        setUp()

        val searchProductModel = searchProductModelWithQuickFilter.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When Load Data`()

        `Then verify isBottomSheetFilterRevampEnabled() is true`()
        `Then verify setNewQuick filter is called`()
        `Then verify SortFilterItem list from response`()
        `Then verify option list from response`(searchProductModel)
    }

    private fun `Given new quick filter is enabled`() {
        every { remoteConfig.getBoolean(RemoteConfigKey.ENABLE_BOTTOM_SHEET_FILTER_REVAMP, true) } answers { true }
    }

    private fun `Given isBottomSheetFilterRevampABTestEnabled return true`() {
        every {
            productListView.abTestRemoteConfig.getString(ABTestRemoteConfigKey.AB_TEST_OLD_FILER_VS_NEW_FILTER)
        } answers { AB_TEST_VARIANT_NEW_FILTER }
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(capture(requestParamsSlot), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `When Load Data`() {
        val searchParameter: Map<String, Any> = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = "samsung"
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
        }
        productListPresenter.loadData(searchParameter)
    }

    private fun `Then verify isBottomSheetFilterRevampEnabled() is true`() {
        assert(productListPresenter.isBottomSheetFilterRevampEnabled) {
            "isBottomSheetFilterRevampEnabled() should return true"
        }
    }

    private fun `Then verify setNewQuick filter is called`() {
        verifyOrder {
            productListView.hideQuickFilterShimmering()
            productListView.setNewQuickFilter(capture(listItemSlot))
            productListView.hideBottomNavigation()
        }
    }

    private fun `Then verify SortFilterItem list from response`() {
        val listItem = listItemSlot.captured

        listItem.size shouldBe 6

        val title = arrayListOf(
                "Gratis Ongkir", "Tukar Tambah", "Official Store", "Kurir Instan", "Dilayani Tokopedia", "Power Merchant"
        )

        listItem.forEachIndexed { index, sortFilterItem ->
            assert(sortFilterItem.title == title[index]) {
                "Assertion Failed, actual title: ${sortFilterItem.title}, expected title: ${title[index]}"
            }
        }
    }

    private fun `Then verify option list from response`(searchProductModel: SearchProductModel) {
        val optionList = productListPresenter.quickFilterOptionList
        val responseList = searchProductModel.quickFilterModel.getOptionList()

        assert(optionList.size == responseList.size && optionList.containsAll(responseList))
    }

    private fun DataValue.getOptionList(): List<Option> {
        val optionList: MutableList<Option> = ArrayList()

        for (filter in filter) {
            optionList.addAll(filter.options)
        }

        return optionList
    }

    @Test
    fun `SearchProductModel has No Quick Filter`() {
        val searchProductModel = searchProductModelNoQuickFilter.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When Load Data`()

        `Then verify new quick filter interactions is not called`()
    }

    private fun `Then verify new quick filter interactions is not called`() {
        verify(exactly = 0) {
            productListView.hideQuickFilterShimmering()
            productListView.setNewQuickFilter(capture(listItemSlot))
        }
    }

    @Test
    fun `SearchProductModel has Quick Filter and enableBottomSheetFilterRevampFirebase is false`() {
        val searchProductModel = searchProductModelWithQuickFilter.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When Load Data`()

        `Then verify isBottomSheetFilterRevampEnabled() is false`()
        `Then verify new quick filter interactions is not called`()
        `Then verify setProductList is called`()
        `Then verify visitable list has quick filter`()
    }

    private fun `Then verify setProductList is called`() {
        verify {
            productListView.setProductList(capture(visitableListSlot))
        }
    }

    private fun `Then verify visitable list has quick filter`() {
        val capturedVisitableList = visitableListSlot.captured

        assert(capturedVisitableList[0] is QuickFilterViewModel) {
            "Visitable List first index should be QuickFilterViewModel."
        }
    }

    @Test
    fun `SearchProductModel has Quick Filter and isBottomSheetFilterRevampABTestEnabled is false`() {
        `Given new quick filter is enabled`()
        `Given isBottomSheetFilterRevampABTestEnabled return false`()
        setUp()

        val searchProductModel = searchProductModelWithQuickFilter.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When Load Data`()

        `Then verify isBottomSheetFilterRevampEnabled() is false`()
        `Then verify new quick filter interactions is not called`()
        `Then verify setProductList is called`()
        `Then verify visitable list has quick filter`()
    }

    private fun `Given isBottomSheetFilterRevampABTestEnabled return false`() {
        every {
            productListView.abTestRemoteConfig.getString(ABTestRemoteConfigKey.AB_TEST_OLD_FILER_VS_NEW_FILTER)
        } answers { ABTestRemoteConfigKey.AB_TEST_VARIANT_OLD_FILTER }
    }

    private fun `Then verify isBottomSheetFilterRevampEnabled() is false`() {
        assert(!productListPresenter.isBottomSheetFilterRevampEnabled) {
            "isBottomSheetFilterRevampEnabled() should return false"
        }
    }
}