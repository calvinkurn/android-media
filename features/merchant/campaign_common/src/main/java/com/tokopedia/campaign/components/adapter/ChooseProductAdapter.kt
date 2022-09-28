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

    fun getRecyclerViewAdapter() = compositeAdapter

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
    }

    fun addItems(newList: List<ChooseProductItem>) {
        compositeAdapter.addItems(newList.map {
            ChooseProductDelegateAdapter.AdapterParam(it, enableSelection, errorMessage)
        })
    }

    fun getItems() = compositeAdapter.getItems().filterIsInstance<ChooseProductDelegateAdapter.AdapterParam>()

    fun refresh() {
        getItems().onEach {
            it.enableSelection = enableSelection
            it.errorMessage = errorMessage
        }
        compositeAdapter.notifyItemRangeChanged(0, compositeAdapter.itemCount)
    }

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

}