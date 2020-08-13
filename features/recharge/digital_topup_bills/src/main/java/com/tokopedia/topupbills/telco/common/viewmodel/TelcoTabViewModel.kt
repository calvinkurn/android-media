package com.tokopedia.topupbills.telco.common.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.topupbills.telco.common.model.TelcoTabItem
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TelcoTabViewModel @Inject constructor(dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    private val tabs = mutableListOf<TelcoTabItem>()

    fun addAll(listTab: List<TelcoTabItem>) {
        tabs.clear()
        tabs.addAll(listTab)
    }

    fun getAll(): List<TelcoTabItem> {
        return tabs
    }

    fun createIdSnapshot(): List<Long> = (0 until tabs.size).map { position -> tabs[position].id }
}