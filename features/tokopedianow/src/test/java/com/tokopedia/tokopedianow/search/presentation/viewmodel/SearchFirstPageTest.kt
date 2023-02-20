package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.constant.ServiceType.NOW_15M
import com.tokopedia.tokopedianow.search.domain.model.SearchCategoryJumperModel.SearchCategoryJumperData
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.search.presentation.model.CTATokopediaNowHomeDataView
import com.tokopedia.tokopedianow.search.presentation.model.CategoryJumperDataView
import com.tokopedia.tokopedianow.searchcategory.assertBannerDataView
import com.tokopedia.tokopedianow.searchcategory.assertCategoryFilterDataView
import com.tokopedia.tokopedianow.searchcategory.assertChooseAddressDataView
import com.tokopedia.tokopedianow.searchcategory.assertProductCountDataView
import com.tokopedia.tokopedianow.searchcategory.assertQuickFilterDataView
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.SearchTitle
import com.tokopedia.tokopedianow.searchcategory.presentation.model.SwitcherWidgetDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TitleDataView
import com.tokopedia.tokopedianow.searchcategory.verifyProductItemDataViewList
import com.tokopedia.tokopedianow.util.SearchCategoryDummyUtils.dummyChooseAddressData
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class SearchFirstPageTest: BaseSearchPageLoadTest() {

    override fun setUp() {}

    @Test
    fun `test first page is last page 2h`() {
        val searchModel = "search/first-page-8-products.json".jsonToObject<SearchModel>()

        `Given get search first page use case will be successful`(searchModel, requestParamsSlot)
        `Given choose address data`()
        `Given search view model`()

        `When view created`()

        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!

        `Then assert request params map`(createExpectedMandatoryTokonowQueryParams(1))
        `Then assert first page visitables`(visitableList, searchModel)
        `Then assert visitable list footer 2h`(visitableList, searchModel)
        `Then assert visitable list does not end with loading more model`(visitableList)
        `Then assert has next page value`(false)
        `Then assert get first page success interactions`(searchModel)
    }

    @Test
    fun `test first page is last page 15m`() {
        val searchModel = "search/first-page-8-products.json".jsonToObject<SearchModel>()

        `Given get search first page use case will be successful`(searchModel, requestParamsSlot)
        `Given choose address data`(dummyChooseAddressData.copy(service_type = NOW_15M))
        `Given search view model`()

        `When view created`()

        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!

        `Then assert request params map`(createExpectedMandatoryTokonowQueryParams(1))
        `Then assert first page visitables`(visitableList, searchModel)
        `Then assert visitable list footer 15m`(visitableList, searchModel)
        `Then assert visitable list does not end with loading more model`(visitableList)
        `Then assert has next page value`(false)
        `Then assert get first page success interactions`(searchModel)
    }

    private fun `Then assert first page visitables`(
            visitableList: List<Visitable<*>>,
            searchModel: SearchModel
    ) {
        `Then assert visitable list header`(visitableList, searchModel)
        `Then assert visitable list contents`(visitableList, searchModel)
    }

    private fun `Then assert visitable list header`(
            visitableList: List<Visitable<*>>,
            searchModel: SearchModel,
    ) {
        val mapParameter = tokoNowSearchViewModel.queryParam

        visitableList[0].assertChooseAddressDataView()
        visitableList[1].assertBannerDataView()
        visitableList[2].assertTitleDataView()
        visitableList[3].assertCategoryFilterDataView(searchModel.categoryFilter)
        visitableList[4].assertQuickFilterDataView(searchModel.quickFilter, mapParameter)
        visitableList[5].assertProductCountDataView(searchModel.searchProduct.header.totalDataText)
    }

    private fun Visitable<*>.assertTitleDataView() {
        assertThat(this, instanceOf(TitleDataView::class.java))

        val titleDataView = this as TitleDataView
        assertThat(titleDataView.hasSeeAllCategoryButton, shouldBe(false))
        assertThat(titleDataView.titleType, instanceOf(SearchTitle::class.java))
    }

    private fun `Then assert visitable list contents`(
            visitableList: List<Visitable<*>>,
            searchModel: SearchModel,
    ) {
        val expectedProductList = searchModel.searchProduct.data.productList
        val actualProductItemDataViewList = visitableList.filterIsInstance<ProductItemDataView>()

        verifyProductItemDataViewList(expectedProductList, actualProductItemDataViewList, 1)
    }

    private fun `Then assert visitable list footer 2h`(
            visitableList: List<Visitable<*>>,
            searchModel: SearchModel
    ) {
        val lastProductIndex = visitableList.indexOfLast { it is ProductItemDataView }
        val footerStartIndex = lastProductIndex + 1
        val footerList = visitableList.subList(footerStartIndex, visitableList.size)

        footerList.first().assertCategoryJumperDataView(searchModel.searchCategoryJumper)
        footerList.last().assertCTATokopediaNowHomeDataView()
    }

    private fun `Then assert visitable list footer 15m`(
        visitableList: List<Visitable<*>>,
        searchModel: SearchModel
    ) {
        val lastProductIndex = visitableList.indexOfLast { it is ProductItemDataView }
        val footerStartIndex = lastProductIndex + 1
        val footerList = visitableList.subList(footerStartIndex, visitableList.size)

        footerList.first().assertSwitcherWidgetDataView()
        footerList.last().assertCategoryJumperDataView(searchModel.searchCategoryJumper)
    }

    private fun Visitable<*>.assertCategoryJumperDataView(
            searchCategoryJumper: SearchCategoryJumperData
    ) {
        assertThat(this, instanceOf(CategoryJumperDataView::class.java))

        val categoryJumperDataView = this as CategoryJumperDataView
        assertThat(categoryJumperDataView.title, shouldBe(searchCategoryJumper.getTitle()))

        val expectedItemList = searchCategoryJumper.getJumperItemList()
        assertThat(categoryJumperDataView.itemList.size, shouldBe(expectedItemList.size))

        categoryJumperDataView.itemList.forEachIndexed { index, actualItem ->
            val expectedItem = expectedItemList[index]

            assertThat(actualItem.title, shouldBe(expectedItem.title))
            assertThat(actualItem.applink, shouldBe(expectedItem.applink))
        }
    }

    private fun Visitable<*>.assertCTATokopediaNowHomeDataView() {
        assertThat(this, instanceOf(CTATokopediaNowHomeDataView::class.java))
    }

    private fun Visitable<*>.assertSwitcherWidgetDataView() {
        assertThat(this, instanceOf(SwitcherWidgetDataView::class.java))
    }

    private fun `Then assert get first page success interactions`(searchModel: SearchModel) {
        `Then assert auto complete applink from API`(searchModel)
        `Then assert header background is shown`()
        `Then assert content is not loading`()
    }

    private fun `Then assert auto complete applink from API`(searchModel: SearchModel) {
        val expectedApplink = searchModel.searchProduct.data.autocompleteApplink

        assertThat(tokoNowSearchViewModel.autoCompleteApplink, shouldBe(expectedApplink))
    }

    private fun `Then assert header background is shown`() {
        assertThat(tokoNowSearchViewModel.isHeaderBackgroundVisibleLiveData.value, shouldBe(true))
    }

    private fun `Then assert content is not loading`() {
        assertThat(tokoNowSearchViewModel.isContentLoadingLiveData.value, shouldBe(false))
    }

    @Test
    fun `test first page has next page`() {
        val searchModel = "search/first-page-16-products.json".jsonToObject<SearchModel>()
        `Given get search first page use case will be successful`(searchModel, requestParamsSlot)
        `Given choose address data`()
        `Given search view model`()

        `When view created`()

        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!

        `Then assert request params map`(createExpectedMandatoryTokonowQueryParams(1))
        `Then assert first page visitables`(visitableList, searchModel)
        `Then assert visitable list end with loading more model`(visitableList)
        `Then assert has next page value`(true)
        `Then assert get first page success interactions`(searchModel)
    }
}
