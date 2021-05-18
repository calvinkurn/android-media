package com.tokopedia.tokomart.searchcategory.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tokomart.searchcategory.domain.model.AceSearchProductModel.Product
import com.tokopedia.tokomart.searchcategory.domain.model.AceSearchProductModel.SearchProductHeader
import com.tokopedia.tokomart.searchcategory.domain.model.FilterModel
import com.tokopedia.tokomart.searchcategory.presentation.model.BannerDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ChooseAddressDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.LabelGroupDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.LabelGroupVariantDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductCountDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.SortFilterItemDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.TitleDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.util.DummyDataViewGenerator
import com.tokopedia.tokomart.searchcategory.utils.ChooseAddressWrapper
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.UseCase

abstract class BaseSearchCategoryViewModel(
        baseDispatcher: CoroutineDispatchers,
        queryParamMap: Map<String, String>,
        protected val getFilterUseCase: UseCase<FilterModel>,
        protected val chooseAddressWrapper: ChooseAddressWrapper,
): BaseViewModel(baseDispatcher.io) {

    protected val queryParamMap = queryParamMap.toMutableMap()
    protected val filterController = FilterController()
    protected val loadingMoreModel = LoadingMoreModel()
    protected val visitableList = mutableListOf<Visitable<*>>()

    protected val visitableListMutableLiveData = MutableLiveData<List<Visitable<*>>>(visitableList)
    val visitableListLiveData: LiveData<List<Visitable<*>>> = visitableListMutableLiveData

    protected val hasNextPageMutableLiveData = MutableLiveData(false)
    val hasNextPageLiveData: LiveData<Boolean> = hasNextPageMutableLiveData

    protected var totalData = 0
    protected var totalFetchedData = 0
    protected var nextPage = 1

    abstract fun onViewCreated()

    protected fun onGetFirstPageSuccess(
            headerDataView: HeaderDataView,
            contentDataView: ContentDataView,
    ) {
        totalData = headerDataView.aceSearchProductHeader.totalData
        totalFetchedData += contentDataView.productList.size

        filterController.initFilterController(queryParamMap, headerDataView.quickFilterDataValue.filter)

        createVisitableListFirstPage(headerDataView, contentDataView)
        clearVisitableListLiveData()
        updateVisitableListLiveData()
        updateNextPageData()
    }

    private fun createVisitableListFirstPage(
            headerDataView: HeaderDataView,
            contentDataView: ContentDataView,
    ) {
        visitableList.clear()

        visitableList.addAll(createHeaderVisitableList(headerDataView))
        visitableList.addAll(createContentVisitableList(contentDataView))
        visitableList.addFooter()
    }

    protected open fun createHeaderVisitableList(headerDataView: HeaderDataView) = listOf(
            ChooseAddressDataView(),
            DummyDataViewGenerator.generateBannerDataView(),
            TitleDataView(headerDataView.title, headerDataView.hasSeeAllCategoryButton),
            QuickFilterDataView(createQuickFilterItemList(headerDataView)),
            ProductCountDataView(headerDataView.aceSearchProductHeader.totalDataText),
    )

    private fun createQuickFilterItemList(headerDataView: HeaderDataView) =
            headerDataView.quickFilterDataValue.filter.map {
                val option = it.options.getOrNull(0) ?: Option()
                val isSelected = filterController.getFilterViewState(option)
                SortFilterItemDataView(
                        option = option,
                        sortFilterItem = createSortFilterItem(it, isSelected, option)
                )
            }

    private fun createSortFilterItem(it: Filter, isSelected: Boolean, option: Option) =
            SortFilterItem(it.title, getSortFilterItemType(isSelected)) {
                onQuickFilterSelected(option, isSelected)
            }.also { sortFilterItem ->
                sortFilterItem.typeUpdated = false
            }

    private fun getSortFilterItemType(isSelected: Boolean) =
            if (isSelected)
                ChipsUnify.TYPE_SELECTED
            else
                ChipsUnify.TYPE_NORMAL

    private fun onQuickFilterSelected(option: Option, isSelected: Boolean) {
        filterController.setFilter(
                option = option,
                isFilterApplied = !isSelected,
                isCleanUpExistingFilterWithSameKey = option.isCategoryOption,
        )

        queryParamMap.clear()
        queryParamMap.putAll(filterController.getParameter())

        onViewReloadPage()
    }

    open fun onViewReloadPage() {
        totalData = 0
        totalFetchedData = 0
        nextPage = 1

        onViewCreated()
    }

    protected open fun createContentVisitableList(contentDataView: ContentDataView) =
            contentDataView.productList.map { product ->
                ProductItemDataView(
                        id = product.id,
                        imageUrl300 = product.imageUrl300,
                        name = product.name,
                        price = product.price,
                        priceInt = product.priceInt,
                        discountPercentage = product.discountPercentage,
                        originalPrice = product.originalPrice,
                        labelGroupDataViewList = product.labelGroupList.map { labelGroup ->
                            LabelGroupDataView(
                                    url = labelGroup.url,
                                    title = labelGroup.title,
                                    position = labelGroup.position,
                                    type = labelGroup.type,
                            )
                        },
                        labelGroupVariantDataViewList = product.labelGroupVariantList.map { labelGroupVariant ->
                            LabelGroupVariantDataView(
                                    title = labelGroupVariant.title,
                                    type = labelGroupVariant.type,
                                    typeVariant = labelGroupVariant.typeVariant,
                                    hexColor = labelGroupVariant.hexColor,
                            )
                        }
                )
            }

    private fun MutableList<Visitable<*>>.addFooter() {
        if (isLastPage())
            addAll(createFooterVisitableList())
        else
            add(loadingMoreModel)
    }

    protected open fun isLastPage() = totalFetchedData >= totalData

    protected open fun createFooterVisitableList() = listOf<Visitable<*>>()

    private fun clearVisitableListLiveData() {
        visitableListMutableLiveData.value = listOf()
    }

    protected fun updateVisitableListLiveData() {
        visitableListMutableLiveData.value = visitableList
    }

    private fun updateNextPageData() {
        val hasNextPage = totalData > totalFetchedData

        hasNextPageMutableLiveData.value = hasNextPage

        if (hasNextPage) nextPage++
    }

    open fun onLoadMore() {
        if (hasLoadedAllData()) return

        executeLoadMore()
    }

    private fun hasLoadedAllData() = totalData <= totalFetchedData

    abstract fun executeLoadMore()

    protected open fun onGetLoadMorePageSuccess(contentDataView: ContentDataView) {
        totalFetchedData += contentDataView.productList.size

        updateVisitableListForNextPage(contentDataView)
        updateVisitableListLiveData()
        updateNextPageData()
    }

    private fun updateVisitableListForNextPage(contentDataView: ContentDataView) {
        visitableList.remove(loadingMoreModel)
        visitableList.addAll(createContentVisitableList(contentDataView))
        visitableList.addFooter()
    }

    protected data class HeaderDataView(
            val title: String = "",
            val hasSeeAllCategoryButton: Boolean = false,
            val aceSearchProductHeader: SearchProductHeader = SearchProductHeader(),
            val quickFilterDataValue: DataValue = DataValue(),
    )

    protected data class ContentDataView(
            val productList: List<Product> = listOf(),
    )
}