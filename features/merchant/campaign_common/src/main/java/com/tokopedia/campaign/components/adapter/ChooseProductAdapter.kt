package com.tokopedia.campaign.components.adapter

import com.tokopedia.campaign.entity.ChooseProductItem
import com.tokopedia.campaign.entity.LoadingItem

class ChooseProductAdapter {
    private val delegateAdapter = ChooseProductDelegateAdapter()
    private val compositeAdapter: CompositeAdapter = CompositeAdapter.Builder()
        .add(delegateAdapter)
        .add(LoadingDelegateAdapter())
        .build()
    private var isLoading = false
    private var enableSelection = true
    private var errorMessage = ""
    private var errorMessageCriteria = ""
    private var disabledCriteriaIds: List<Long> = emptyList()

    fun getRecyclerViewAdapter() = compositeAdapter

    private fun refresh() {
        getItems().onEach {
            it.enableSelection = enableSelection
            it.errorMessage = errorMessage
        }
        compositeAdapter.notifyItemRangeChanged(0, compositeAdapter.itemCount)
    }

    fun setListener(listener: ChooseProductDelegateAdapter.ChooseProductListener) {
        delegateAdapter.setListener(listener)
    }

    fun setLoading(isLoading: Boolean) {
        if (this.isLoading == isLoading) return
        this.isLoading = isLoading
        if (isLoading) {
            compositeAdapter.addItem(LoadingItem)
        } else {
            compositeAdapter.removeItem(LoadingItem)
        }
    }

    fun submit(newList: List<ChooseProductItem>) {
        compositeAdapter.submit(newList.map {
            ChooseProductDelegateAdapter.AdapterParam(it, enableSelection, errorMessage)
        })
        disableByCriteria(disabledCriteriaIds, errorMessageCriteria)
    }

    fun addItems(newList: List<ChooseProductItem>) {
        compositeAdapter.addItems(newList.map {
            ChooseProductDelegateAdapter.AdapterParam(it, enableSelection, errorMessage)
        })
        disableByCriteria(disabledCriteriaIds, errorMessageCriteria)
    }

    fun getItems() = compositeAdapter.getItems().filterIsInstance<ChooseProductDelegateAdapter.AdapterParam>()

    fun disable(errorMessage: String) {
        enableSelection = false
        this.errorMessage = errorMessage
        refresh()
    }

    fun enable() {
        if (enableSelection) return
        enableSelection = true
        errorMessage = ""
        refresh()
    }

    fun disableByCriteria(disabledCriteriaIds: List<Long>, errorMessage: String) {
        this.errorMessageCriteria = errorMessage
        this.disabledCriteriaIds = disabledCriteriaIds
        getItems().onEachIndexed { index, item ->
            if (disabledCriteriaIds.any { item.item.criteriaId == it }) {
                item.enableSelection = false
                item.errorMessage = errorMessage
                compositeAdapter.notifyItemChanged(index)
            }
        }
    }

}
