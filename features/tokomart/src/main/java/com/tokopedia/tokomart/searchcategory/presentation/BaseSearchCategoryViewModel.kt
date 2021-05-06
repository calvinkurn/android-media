package com.tokopedia.tokomart.searchcategory.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokomart.searchcategory.presentation.model.BannerDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ChooseAddressDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductCountDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.TitleDataView

abstract class BaseSearchCategoryViewModel(
        baseDispatcher: CoroutineDispatchers
): BaseViewModel(baseDispatcher.io) {

    protected val loadingMoreModel = LoadingMoreModel()
    protected val visitableList = mutableListOf<Visitable<*>>()

    protected val visitableListMutableLiveData = MutableLiveData<List<Visitable<*>>>(visitableList)
    val visitableListLiveData: LiveData<List<Visitable<*>>> = visitableListMutableLiveData

    protected var totalData = 0
    protected var totalFetchedData = 0

    abstract fun onViewCreated()

    protected fun onGetFirstPageSuccess(headerDataView: HeaderDataView) {
        totalData = headerDataView.totalData
        totalFetchedData = 3

        createVisitableListFirstPage(headerDataView)
        updateVisitableListLiveData()
    }

    private fun createVisitableListFirstPage(headerDataView: HeaderDataView) {
        visitableList.clear()

        visitableList.addAll(createHeaderVisitableList(headerDataView))
        visitableList.addAll(createContentVisitableList())
        visitableList.addFooter()
    }

    protected open fun createHeaderVisitableList(headerDataView: HeaderDataView) = listOf(
            ChooseAddressDataView(),
            BannerDataView(),
            TitleDataView(headerDataView.title, headerDataView.hasSeeAllCategoryButton),
            QuickFilterDataView(),
            ProductCountDataView(headerDataView.totalData),
    )

    protected open fun createContentVisitableList() = listOf(
            ProductItemDataView(),
            ProductItemDataView(),
            ProductItemDataView(),
    )

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

    fun onLoadMore() {

    }

    protected data class HeaderDataView(
            val title: String = "",
            val hasSeeAllCategoryButton: Boolean = false,
            val totalData: Int = 0
    )
}