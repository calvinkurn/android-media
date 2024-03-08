package com.tokopedia.deals.ui.pdp.ui.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.ONE
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

class DealsPDPAllLocationViewModel @Inject constructor(dispatcher: CoroutineDispatchers) :
    BaseViewModel(dispatcher.main) {

    private val _inputSearch = MutableSharedFlow<Pair<String, List<com.tokopedia.deals.ui.pdp.data.Outlet>>>(Int.ONE)

    val flowSearchResult: SharedFlow<List<com.tokopedia.deals.ui.pdp.data.Outlet>> =
        _inputSearch.flatMapConcat {
            flow {
                emit(getSearchResult(it.first, it.second))
            }
        }.shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(SHARED_FLOW_STOP_TIMEOUT_MILLIS),
            replay = Int.ONE
        )

    fun submitSearch(key: String, outlets: List<com.tokopedia.deals.ui.pdp.data.Outlet>) {
        _inputSearch.tryEmit(Pair(key, outlets))
    }

    private fun getSearchResult(key: String, outlets: List<com.tokopedia.deals.ui.pdp.data.Outlet>): List<com.tokopedia.deals.ui.pdp.data.Outlet> {
        return outlets.filter {
            it.name.trim().lowercase().contains(key.trim().lowercase()) ||
            it.district.trim().lowercase().contains(key.trim().lowercase())
        }
    }

    companion object {
        private const val SHARED_FLOW_STOP_TIMEOUT_MILLIS = 5000L
    }
}
