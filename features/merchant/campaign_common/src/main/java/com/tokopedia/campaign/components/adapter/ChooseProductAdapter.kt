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
        compositeAdapter.submit(newList)
    }

    fun addItems(newList: List<ChooseProductItem>) {
        compositeAdapter.addItems(newList)
    }

    fun getItems() = compositeAdapter.getItems().filterIsInstance<ChooseProductItem>()

}