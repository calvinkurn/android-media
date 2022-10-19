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
    private var forceEnabledProductIds: List<String> = emptyList()

    fun getRecyclerViewAdapter() = compositeAdapter

    private fun refresh() {
        getProductItems().onEach {
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
        forceEnableProduct(forceEnabledProductIds)
    }

    fun addItems(newList: List<ChooseProductItem>) {
        compositeAdapter.addItems(newList.map {
            ChooseProductDelegateAdapter.AdapterParam(it, enableSelection, errorMessage)
        })
        disableByCriteria(disabledCriteriaIds, errorMessageCriteria)
        forceEnableProduct(forceEnabledProductIds)
    }

    fun getProductItems() = compositeAdapter.getItems().filterIsInstance<ChooseProductDelegateAdapter.AdapterParam>()

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
        getProductItems().onEachIndexed { index, product ->
            if (disabledCriteriaIds.any { product.item.criteriaId == it }) {
                product.enableSelection = false
                product.errorMessage = errorMessage
            } else {
                product.enableSelection = this.enableSelection
                product.errorMessage = this.errorMessage
            }
            compositeAdapter.notifyItemChanged(index)
        }
    }

    fun forceEnableProduct(forceEnabledProductIds: List<String>) {
        this.forceEnabledProductIds = forceEnabledProductIds
        getProductItems().onEachIndexed { index, product ->
            if (forceEnabledProductIds.any { product.item.productId == it }) {
                product.enableSelection = true
                product.errorMessage = ""
                compositeAdapter.notifyItemChanged(index)
            }
        }
    }

}
