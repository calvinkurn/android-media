package com.tokopedia.tokomart.searchcategory.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokomart.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokomart.searchcategory.domain.model.AceSearchProductModel.Product
import com.tokopedia.tokomart.searchcategory.presentation.model.BannerDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ChooseAddressDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.LabelGroupDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.LabelGroupVariantDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductCountDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.TitleDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.util.ModelGenerator
import com.tokopedia.tokomart.searchcategory.utils.ChooseAddressWrapper

abstract class BaseSearchCategoryViewModel(
        baseDispatcher: CoroutineDispatchers,
        protected val chooseAddressWrapper: ChooseAddressWrapper,
): BaseViewModel(baseDispatcher.io) {

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

        createVisitableListFirstPage(headerDataView, contentDataView)
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
            BannerDataView(""),
            TitleDataView(headerDataView.title, headerDataView.hasSeeAllCategoryButton),
            QuickFilterDataView(),
            ProductCountDataView(headerDataView.aceSearchProductHeader.totalDataText),
    )

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
            val aceSearchProductHeader: AceSearchProductModel.SearchProductHeader
    )

    protected data class ContentDataView(
            val productList: List<Product> = listOf(),
    )
}